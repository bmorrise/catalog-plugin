package org.pentaho.di.plugins.catalog.vfs;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.impl.DefaultFileSystemManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CatalogFileProviderTest {

  @Before
  public void setup() throws Exception {
    DefaultFileSystemManager fsm = (DefaultFileSystemManager) VFS.getManager();
    if ( !fsm.hasProvider( CatalogFileProvider.SCHEME ) ) {
      fsm.addProvider( CatalogFileProvider.SCHEME, new CatalogFileProvider() );
    }
  }

  @Test
  public void testGetFile() throws Exception {
    // b8933a41fe844cf25e0ae3e4feb88622
    FileObject fileObject = VFS.getManager().resolveFile( "catalog://54.153.37.60:8082/b8933a41fe844cf25e0ae3e4feb88622" );
    Assert.assertNotNull( fileObject );
  }

}
