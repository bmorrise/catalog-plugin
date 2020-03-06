package org.pentaho.di.plugins.catalog.api.resources;

import org.pentaho.di.plugins.catalog.api.Catalog;
import org.pentaho.di.plugins.catalog.api.entities.DataResource;
import org.pentaho.di.plugins.catalog.api.entities.DataSource;

public class DataSources extends Resource {

  private static final String BASE_URL = Catalog.API_ROOT + "/datasource";
  private static final String READ_URL = BASE_URL + "/";

  public DataSources( Catalog catalog ) {
    super( catalog );
  }

  public DataSource read( String key ) {
    return read( READ_URL + key, DataSource.class );
  }
}
