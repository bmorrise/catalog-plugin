package org.pentaho.di.plugins.catalog.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class DataResource {
  private String name;
  private String resourcePath;
  private String dataSourceUri;

  public String getName() {
    return name;
  }

  public void setName( String name ) {
    this.name = name;
  }

  public String getResourcePath() {
    return resourcePath;
  }

  public void setResourcePath( String resourcePath ) {
    this.resourcePath = resourcePath;
  }

  public String getDataSourceUri() {
    return dataSourceUri;
  }

  public void setDataSourceUri( String dataSourceUri ) {
    this.dataSourceUri = dataSourceUri;
  }

  public String getFullPath() {
    return dataSourceUri + resourcePath;
  }
}
