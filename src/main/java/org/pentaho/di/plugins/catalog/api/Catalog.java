package org.pentaho.di.plugins.catalog.api;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.pentaho.di.plugins.catalog.api.resources.Authentication;
import org.pentaho.di.plugins.catalog.api.resources.DataResources;
import org.pentaho.di.plugins.catalog.api.resources.DataSources;

import java.io.IOException;

public class Catalog {

  public static String API_VERSION = "v2";
  public static String API_ROOT = "/api/" + API_VERSION;

  public static String HTTP = "http";
  public static String HTTPS = "https";

  private String host;
  private String port;
  private boolean secure;
  private String sessionId;

  private Authentication authentication;
  private DataResources dataResources;
  private DataSources dataSources;

  public Catalog( String host, String port, boolean secure ) {
    this.host = host;
    this.port = port;
    this.secure = secure;

    authentication = new Authentication( this );
    dataResources = new DataResources( this );
    dataSources = new DataSources( this );
  }

  public Catalog( String host, String port ) {
    this( host, port, false );
  }

  public String buildUrl( String path ) {
    return ( secure ? HTTPS : HTTP ) + "://" + host + ":" + port + path;
  }

  public HttpResponse doPost( String url, HttpEntity entity ) throws IOException {
    return ClientHelper.doPost( sessionId, buildUrl( url ), entity );
  }

  public CloseableHttpResponse doGet( String url ) throws IOException {
    return ClientHelper.doGet( sessionId, buildUrl( url ) );
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId( String sessionId ) {
    this.sessionId = sessionId;
  }

  public Authentication getAuthentication() {
    return authentication;
  }

  public DataResources getDataResources() {
    return dataResources;
  }

  public DataSources getDataSources() {
    return dataSources;
  }
}
