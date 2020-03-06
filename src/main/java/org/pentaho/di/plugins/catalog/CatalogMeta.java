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

import org.pentaho.di.core.annotations.Step;
import org.pentaho.di.core.CheckResult;
import org.pentaho.di.core.CheckResultInterface;
import org.pentaho.di.core.exception.KettlePluginException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.injection.Injection;
import org.pentaho.di.core.injection.InjectionSupported;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.row.value.ValueMetaFactory;
import org.pentaho.di.core.util.serialization.BaseSerializingMeta;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.metastore.api.IMetaStore;

import java.util.List;


/**
 * Skeleton for PDI Step plugin.
 */
@Step( id = "Catalog", image = "CatalogStep.svg", name = "Catalog",
    description = "Interact with the catalog", categoryDescription = "Input" )
@InjectionSupported( localizationPrefix = "Catalog.Injection." )
public class CatalogMeta extends BaseSerializingMeta implements StepMetaInterface {
  
  private static Class<?> PKG = Catalog.class; // for i18n purposes, needed by Translator2!!   $NON-NLS-1$

  public static final String DATA_RESOURCE = "Data Resource";
  public static final String DATA_SOURCE = "Data Source";
  public static final String[] DATA_TYPES = new String[] {
    DATA_RESOURCE, DATA_SOURCE
  };

  @Injection( name = "URL" )
  private String url;

  @Injection( name = "USERNAME" )
  private String username;

  @Injection( name = "PASSWORD" )
  private String password;

  @Injection( name = "DATA_TYPE" )
  private String dataType;

  @Injection( name = "DATA_RESOURCE_KEY" )
  private String dataResourceKey;

  @Injection( name = "OUTPUT_FIELD_NAME" )
  private String outputFieldName = "catalogUrl";

  public CatalogMeta() {
    super(); // allocate BaseStepMeta
  }

  public Object clone() {
    Object retval = super.clone();
    return retval;
  }
  
  public void setDefault() {
    url = "";
    username = "";
    password = "";
    dataType = DATA_RESOURCE;
    dataResourceKey = "";
    outputFieldName = "catalogUrl";
  }

  public void getFields( RowMetaInterface rowMeta, String origin, RowMetaInterface[] info, StepMeta nextStep,
    VariableSpace space, Repository repository, IMetaStore metaStore ) throws KettleStepException {
    // Default: nothing changes to rowMeta

    try {
      ValueMetaInterface v = ValueMetaFactory.createValueMeta( outputFieldName, ValueMetaInterface.TYPE_STRING );
      rowMeta.addValueMeta( v );
    } catch ( KettlePluginException kpe ) {
      throw new KettleStepException( "Unable to load fields" );
    }
  }
  
  public void check( List<CheckResultInterface> remarks, TransMeta transMeta, 
    StepMeta stepMeta, RowMetaInterface prev, String input[], String output[],
    RowMetaInterface info, VariableSpace space, Repository repository, 
    IMetaStore metaStore ) {
    CheckResult cr;
    if ( prev == null || prev.size() == 0 ) {
      cr = new CheckResult( CheckResultInterface.TYPE_RESULT_WARNING, BaseMessages.getString( PKG, "CatalogMeta.CheckResult.NotReceivingFields" ), stepMeta );
      remarks.add( cr );
    }
    else {
      cr = new CheckResult( CheckResultInterface.TYPE_RESULT_OK, BaseMessages.getString( PKG, "CatalogMeta.CheckResult.StepRecevingData", prev.size() + "" ), stepMeta );
      remarks.add( cr );
    }
    
    // See if we have input streams leading to this step!
    if ( input.length > 0 ) {
      cr = new CheckResult( CheckResultInterface.TYPE_RESULT_OK, BaseMessages.getString( PKG, "CatalogMeta.CheckResult.StepRecevingData2" ), stepMeta );
      remarks.add( cr );
    }
    else {
      cr = new CheckResult( CheckResultInterface.TYPE_RESULT_ERROR, BaseMessages.getString( PKG, "CatalogMeta.CheckResult.NoInputReceivedFromOtherSteps" ), stepMeta );
      remarks.add( cr );
    }
  }
  
  public StepInterface getStep( StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta tr, Trans trans ) {
    return new Catalog( stepMeta, stepDataInterface, cnr, tr, trans );
  }
  
  public StepDataInterface getStepData() {
    return new CatalogData();
  }

  public String getUsername() {
    return username;
  }

  public void setUsername( String username ) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword( String password ) {
    this.password = password;
  }

  public String getDataResourceKey() {
    return dataResourceKey;
  }

  public void setDataResourceKey( String dataResourceKey ) {
    this.dataResourceKey = dataResourceKey;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl( String url ) {
    this.url = url;
  }

  public String getOutputFieldName() {
    return outputFieldName;
  }

  public void setOutputFieldName( String outputFieldName ) {
    this.outputFieldName = outputFieldName;
  }

  public String getDataType() {
    return dataType;
  }

  public void setDataType( String dataType ) {
    this.dataType = dataType;
  }

  public String getDialogClassName() {
    return "org.pentaho.di.plugins.catalog.CatalogDialog";
  }
}
