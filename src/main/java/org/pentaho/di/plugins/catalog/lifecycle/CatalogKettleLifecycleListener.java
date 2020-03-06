package org.pentaho.di.plugins.catalog.lifecycle;

import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.impl.DefaultFileSystemManager;
import org.pentaho.di.core.annotations.KettleLifecyclePlugin;
import org.pentaho.di.core.lifecycle.KettleLifecycleListener;
import org.pentaho.di.core.lifecycle.LifecycleException;
import org.pentaho.di.core.vfs.KettleVFS;
import org.pentaho.di.plugins.catalog.vfs.CatalogFileProvider;

import java.util.Arrays;

@KettleLifecyclePlugin( id = "CatalogKettleLifecycleListener", name = "CatalogKettleLifecycleListener" )
public class CatalogKettleLifecycleListener implements KettleLifecycleListener {

  public void onEnvironmentInit() throws LifecycleException {
    try {
      FileSystemManager fsm = KettleVFS.getInstance().getFileSystemManager();
      if ( fsm instanceof DefaultFileSystemManager ) {
        if ( !Arrays.asList( fsm.getSchemes() ).contains( CatalogFileProvider.SCHEME ) ) {
          ( (DefaultFileSystemManager) fsm )
            .addProvider( CatalogFileProvider.SCHEME, new CatalogFileProvider() );
        }
      }
    } catch ( FileSystemException e ) {
      throw new LifecycleException( e.getMessage(), false );
    }
  }

  public void onEnvironmentShutdown() {
  }
}
