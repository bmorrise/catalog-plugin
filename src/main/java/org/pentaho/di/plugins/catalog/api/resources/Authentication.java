package org.pentaho.di.plugins.catalog.api.resources;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.pentaho.di.plugins.catalog.api.Catalog;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Authentication extends Resource {

  public Authentication( Catalog catalog ) {
    super( catalog );
  }

  private static final String SET_COOKIE = "Set-Cookie";
  private static String LOGIN_URL = Catalog.API_ROOT + "/login";

  public boolean login( String username, String password ) {
    String json = "{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}";
    StringEntity entity;
    try {
      entity = new StringEntity( json );
    } catch ( UnsupportedEncodingException uee ) {
      return false;
    }

    HttpResponse response;
    try {
      response = catalog.doPost( LOGIN_URL, entity );
    } catch ( IOException ioe ) {
      return false;
    }

    if ( response != null ) {
      Header header = response.getFirstHeader( SET_COOKIE );
      if ( header != null && header.getElements().length > 0 ) {
        HeaderElement headerElement = header.getElements()[ 0 ];
        catalog.setSessionId( headerElement.getValue() );
      }

      return response.getStatusLine().getStatusCode() == 200;
    }

    return false;
  }

  public void logout() {

  }

}
