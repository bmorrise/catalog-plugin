package org.pentaho.di.plugins.catalog.vfs;

import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.provider.AbstractFileName;
import org.apache.commons.vfs2.provider.AbstractFileObject;

import java.io.InputStream;

public class CatalogFileObject extends AbstractFileObject<CatalogFileSystem> {

  public CatalogFileObject( AbstractFileName name,
                            CatalogFileSystem fs ) {
    super( name, fs );
  }

  @Override protected long doGetContentSize() throws Exception {
    return 0;
  }

  @Override protected InputStream doGetInputStream() throws Exception {



    return null;
  }

  @Override protected FileType doGetType() throws Exception {
    return null;
  }

  @Override protected String[] doListChildren() throws Exception {
    return new String[ 0 ];
  }
}
