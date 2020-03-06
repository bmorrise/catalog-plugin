package org.pentaho.di.plugins.catalog.vfs;

import org.apache.commons.vfs2.Capability;
import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.provider.AbstractFileName;
import org.apache.commons.vfs2.provider.AbstractFileSystem;
import org.apache.commons.vfs2.provider.URLFileName;
import org.pentaho.di.core.vfs.KettleVFS;
import org.pentaho.di.plugins.catalog.api.Catalog;
import org.pentaho.di.plugins.catalog.api.entities.DataResource;

import java.util.Collection;

public class CatalogFileSystem extends AbstractFileSystem {

  public CatalogFileSystem( FileName rootName, FileObject parentLayer,
                            FileSystemOptions fileSystemOptions ) {
    super( rootName, parentLayer, fileSystemOptions );
  }

  @Override protected FileObject createFile( AbstractFileName abstractFileName ) throws Exception {
    URLFileName urlFileName = (URLFileName) abstractFileName;

    String host = urlFileName.getHostName();
    String port = String.valueOf( urlFileName.getPort() );

    Catalog catalog = new Catalog( host, port );
    catalog.getAuthentication().login( "", "" );

    String key = abstractFileName.getBaseName();

    DataResource dataResource = catalog.getDataResources().read( key );

    return KettleVFS.getFileObject( dataResource.getFullPath() );
  }

  @Override protected void addCapabilities( Collection<Capability> collection ) {
    collection.addAll( CatalogFileProvider.capabilities );
  }
}
