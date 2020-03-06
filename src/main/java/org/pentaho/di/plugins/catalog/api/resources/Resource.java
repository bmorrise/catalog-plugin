package org.pentaho.di.plugins.catalog.api.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.pentaho.di.plugins.catalog.api.Catalog;

import java.io.IOException;

public abstract class Resource {
  protected Catalog catalog;
  private ObjectMapper objectMapper;

  public Resource( Catalog catalog ) {
    this.catalog = catalog;

    objectMapper = new ObjectMapper();
  }

  private <T> T convert( HttpResponse httpResponse, Class<T> clazz ) {
    try {
      return objectMapper.readValue( httpResponse.getEntity().getContent(), clazz );
    } catch ( IOException ioe ) {
      return null;
    }
  }

  public <T> T read( String url, Class<T> clazz ) {
    try ( CloseableHttpResponse httpResponse = catalog.doGet( url ) ) {
      if ( httpResponse != null && httpResponse.getEntity() != null ) {
        return convert( httpResponse, clazz );
      }
    } catch ( IOException ioe ) {
      return null;
    }

    return null;
  }
}
