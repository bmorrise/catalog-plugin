package org.pentaho.di.plugins.catalog.api;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class ClientHelper {

  private ClientHelper() {
  }

  private static final String APPLICATION_JSON = "application/json";
  private static final String ACCEPT = "Accept";
  private static final String ACCEPT1 = "Accept";
  private static final String COOKIE = "Cookie";
  private static final String CONTENT_TYPE = "Content-type";
  private static final String WDSESSION_ID = "WDSessionId";

  public static CloseableHttpResponse doPost( String sessionId, String url, HttpEntity entity ) throws IOException {
    CloseableHttpClient client = HttpClients.createDefault();
    HttpPost httpPost = new HttpPost( url );
    httpPost.setEntity( entity );
    httpPost.setHeader( ACCEPT, APPLICATION_JSON );
    httpPost.setHeader( CONTENT_TYPE, APPLICATION_JSON );
    httpPost.setHeader( COOKIE, WDSESSION_ID + "=" + sessionId );
    return client.execute( httpPost );
  }

  public static CloseableHttpResponse doGet( String sessionId, String url ) throws IOException {
    CloseableHttpClient client = HttpClients.createDefault();
    HttpGet httpGet = new HttpGet( url );
    httpGet.setHeader( ACCEPT1, ClientHelper.APPLICATION_JSON );
    httpGet.setHeader( COOKIE, WDSESSION_ID + "=" + sessionId );
    return client.execute( httpGet );
  }

}
