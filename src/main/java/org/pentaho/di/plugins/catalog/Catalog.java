/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.pentaho.di.plugins.catalog;

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.core.row.value.ValueMetaString;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.plugins.catalog.api.entities.DataResource;
import org.pentaho.di.plugins.catalog.api.entities.DataSource;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Describe your step plugin.
 */
public class Catalog extends BaseStep implements StepInterface {

  private static Class<?> PKG = CatalogMeta.class; // for i18n purposes, needed by Translator2!!   $NON-NLS-1$

  private CatalogMeta meta;
  private CatalogData data;

  public Catalog( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr, TransMeta transMeta,
                  Trans trans ) {
    super( stepMeta, stepDataInterface, copyNr, transMeta, trans );
  }


  /**
   * Initialize and do work where other steps need to wait for...
   *
   * @param smi The metadata to work with
   * @param sdi The data to initialize
   */
  public boolean init( StepMetaInterface smi, StepDataInterface sdi ) {

    if ( !super.init( smi, sdi ) ) {
      return false;
    }

    meta = (CatalogMeta) smi;
    data = (CatalogData) sdi;

    URL url;
    try {
      url = new URL( environmentSubstitute( meta.getUrl() ) );
    } catch ( MalformedURLException mue ) {
      return false;
    }

    data.catalog = new org.pentaho.di.plugins.catalog.api.Catalog( url.getHost(), String.valueOf( url.getPort() ),
      url.getProtocol().equals( org.pentaho.di.plugins.catalog.api.Catalog.HTTPS ) );
    data.catalog.getAuthentication()
      .login( environmentSubstitute( meta.getUsername() ), environmentSubstitute( meta.getPassword() ) );

    if ( data.catalog.getSessionId() == null ) {
      return false;
    }

    return true;
  }

  public boolean processRow( StepMetaInterface smi, StepDataInterface sdi ) throws KettleException {
    Object[] r = getRow(); // get row, set busy!
    if ( r == null ) {
      // no more input to be expected...
      setOutputDone();
      return false;
    }

    if ( first ) {
      first = false;
      data.inputRowMeta = getInputRowMeta();
      data.outputRowMeta = data.inputRowMeta.clone();
      meta.getFields( data.outputRowMeta, getStepname(), null, null, this, repository, metaStore );
    }

    String[] keys = environmentSubstitute( meta.getDataResourceKey() ).split( "," );
    Object[] outputRowData = r.clone();

    for ( String key : keys ) {
      String rowData;
      if ( environmentSubstitute( meta.getDataType() ).equals( CatalogMeta.DATA_RESOURCE ) ) {
        DataResource dataResource =
          data.catalog.getDataResources().read( key );
        rowData = dataResource.getFullPath();
      } else {
        DataSource dataSource = data.catalog.getDataSources().read( key );
        rowData = dataSource.getFullPath();
      }

      // send the row to the next steps...
      putRow( data.outputRowMeta,
        RowDataUtil.addValueData( outputRowData, data.outputRowMeta.size() - 1, rowData ) );
    }

    if ( checkFeedback( getLinesRead() ) ) {
      if ( log.isBasic() ) {
        logBasic( BaseMessages.getString( PKG, "CatalogStep.Log.LineNumber" ) + getLinesRead() );
      }
    }

    return true;
  }
}