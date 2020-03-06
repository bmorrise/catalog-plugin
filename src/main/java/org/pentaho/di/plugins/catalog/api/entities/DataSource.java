package org.pentaho.di.plugins.catalog.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class DataSource {
  private String name;
  private String hdfsUri;
  private String sourcePath;

  public String getName() {
    return name;
  }

  public void setName( String name ) {
    this.name = name;
  }

  public String getHdfsUri() {
    return hdfsUri;
  }

  public void setHdfsUri( String hdfsUri ) {
    this.hdfsUri = hdfsUri;
  }

  public String getSourcePath() {
    return sourcePath;
  }

  public void setSourcePath( String sourcePath ) {
    this.sourcePath = sourcePath;
  }

  public String getFullPath() {
    return hdfsUri + sourcePath;
  }
}
