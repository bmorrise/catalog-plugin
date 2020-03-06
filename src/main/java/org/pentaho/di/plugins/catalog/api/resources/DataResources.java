package org.pentaho.di.plugins.catalog.api.resources;

import org.pentaho.di.plugins.catalog.api.Catalog;
import org.pentaho.di.plugins.catalog.api.entities.DataResource;

public class DataResources extends Resource {

  private static final String BASE_URL = Catalog.API_ROOT + "/dataresource";
  private static final String READ_URL = BASE_URL + "/";

  public DataResources( Catalog catalog ) {
    super( catalog );
  }

  public DataResource read( String key ) {
    return read( READ_URL + key, DataResource.class );
  }
}
