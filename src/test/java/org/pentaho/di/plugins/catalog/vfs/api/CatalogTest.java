package org.pentaho.di.plugins.catalog.vfs.api;

import org.junit.Before;
import org.junit.Test;
import org.pentaho.di.plugins.catalog.api.Catalog;
import org.pentaho.di.plugins.catalog.api.entities.DataResource;

public class CatalogTest {

  private Catalog catalog;

  @Before
  public void setup() {
    catalog = new Catalog( ", "" );
    catalog.getAuthentication().login( "", "" );
  }

  @Test
  public void testGetDataResources() {
    DataResource dataResource = catalog.getDataResources().read( "" );
    System.out.println( dataResource.getFullPath() );
  }

}
