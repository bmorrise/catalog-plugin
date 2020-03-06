package org.pentaho.di.plugins.catalog.vfs;

import org.apache.commons.vfs2.Capability;
import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileSystem;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.provider.AbstractOriginatingFileProvider;
import org.apache.commons.vfs2.provider.url.UrlFileNameParser;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class CatalogFileProvider extends AbstractOriginatingFileProvider {

  public static final String SCHEME = "catalog";

  protected static final Collection<Capability>
    capabilities = Collections.unmodifiableCollection( Arrays.asList( Capability.CREATE, Capability.DELETE,
    Capability.RENAME, Capability.GET_TYPE, Capability.LIST_CHILDREN, Capability.READ_CONTENT, Capability.URI,
    Capability.WRITE_CONTENT, Capability.GET_LAST_MODIFIED, Capability.RANDOM_ACCESS_READ ) );

  public CatalogFileProvider() {
    setFileNameParser( new UrlFileNameParser() );
  }

  @Override protected FileSystem doCreateFileSystem( FileName fileName, FileSystemOptions fileSystemOptions )
    throws FileSystemException {
    return new CatalogFileSystem( fileName, null, fileSystemOptions );
  }

  @Override public Collection<Capability> getCapabilities() {
    return capabilities;
  }
}
