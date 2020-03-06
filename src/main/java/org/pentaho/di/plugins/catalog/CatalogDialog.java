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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.plugins.PluginInterface;
import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.core.plugins.StepPluginType;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.ui.core.ConstUI;
import org.pentaho.di.ui.core.FormDataBuilder;
import org.pentaho.di.ui.core.gui.GUIResource;
import org.pentaho.di.ui.core.widget.PasswordTextVar;
import org.pentaho.di.ui.core.widget.TextVar;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

public class CatalogDialog extends BaseStepDialog implements StepDialogInterface {

  public static final String DATA_RESOURCE = "Data Resource";
  public static final String DATA_SOURCE = "Data Source";
  private static Class<?> PKG = CatalogMeta.class; // for i18n purposes, needed by Translator2!!   $NON-NLS-1$

  private static final int MARGIN_SIZE = 15;
  private static final int LABEL_SPACING = 5;
  private static final int ELEMENT_SPACING = 10;

  private static final int LARGE_FIELD = 350;
  private static final int MEDIUM_FIELD = 250;
  private static final int SMALL_FIELD = 75;

  private CatalogMeta meta;

  private Text wStepNameField;
  private Label wStepNameLabel;

  private Label wlHost;
  private Label wlUsername;
  private Label wlPassword;
  private Label wlDataType;
  private Label wlDataResourceKey;
  private Label wlOutputFieldName;

  private TextVar wHost;
  private TextVar wPort;
  private Button wSecure;
  private TextVar wUsername;
  private TextVar wPassword;
  private Combo wDataType;
  private TextVar wDataResourceKey;
  private TextVar wOutputFieldName;

  private ScrolledComposite scrolledComposite;
  private Composite contentComposite;
  private Button wCancel;
  private Button wOK;

  private ModifyListener lsMod;
  private Listener lsCancel;
  private Listener lsOK;
  private SelectionAdapter lsDef;
  private boolean changed;

  public CatalogDialog( Shell parent, Object in, TransMeta tr, String sname ) {
    super( parent, (BaseStepMeta) in, tr, sname );
    meta = (CatalogMeta) in;
  }

  public String open() {
    //Set up window
    Shell parent = getParent();
    Display display = parent.getDisplay();

    shell = new Shell( parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX );
    shell.setMinimumSize( 450, 335 );
    props.setLook( shell );
    setShellImage( shell, meta );

    lsMod = new ModifyListener() {
      public void modifyText( ModifyEvent e ) {
        meta.setChanged();
      }
    };
    changed = meta.hasChanged();

    //15 pixel margins
    FormLayout formLayout = new FormLayout();
    formLayout.marginLeft = MARGIN_SIZE;
    formLayout.marginHeight = MARGIN_SIZE;
    shell.setLayout( formLayout );
    shell.setText( BaseMessages.getString( PKG, "CatalogStepDialog.Shell.Title" ) );

    //Build a scrolling composite and a composite for holding all content
    scrolledComposite = new ScrolledComposite( shell, SWT.V_SCROLL );
    contentComposite = new Composite( scrolledComposite, SWT.NONE );
    FormLayout contentLayout = new FormLayout();
    contentLayout.marginRight = MARGIN_SIZE;
    contentComposite.setLayout( contentLayout );
    FormData compositeLayoutData = new FormDataBuilder().fullSize()
      .result();
    contentComposite.setLayoutData( compositeLayoutData );
    props.setLook( contentComposite );

    //Step name label and text field
    wStepNameLabel = new Label( contentComposite, SWT.RIGHT );
    wStepNameLabel.setText( BaseMessages.getString( PKG, "CatalogStepDialog.Stepname.Label" ) );
    props.setLook( wStepNameLabel );
    FormData fdStepNameLabel = new FormDataBuilder().left()
      .top()
      .result();
    wStepNameLabel.setLayoutData( fdStepNameLabel );

    wStepNameField = new Text( contentComposite, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
    wStepNameField.setText( stepname );
    props.setLook( wStepNameField );
    wStepNameField.addModifyListener( lsMod );
    FormData fdStepName = new FormDataBuilder().left()
      .top( wStepNameLabel, LABEL_SPACING )
      .width( MEDIUM_FIELD )
      .result();
    wStepNameField.setLayoutData( fdStepName );

    //Job icon, centered vertically between the top of the label and the bottom of the field.
    Label wicon = new Label( contentComposite, SWT.CENTER );
    wicon.setImage( getImage() );
    FormData fdIcon = new FormDataBuilder().right()
      .top( 0, 4 )
      .bottom( new FormAttachment( wStepNameField, 0, SWT.BOTTOM ) )
      .result();
    wicon.setLayoutData( fdIcon );
    props.setLook( wicon );

    //Spacer between entry info and content
    Label topSpacer = new Label( contentComposite, SWT.HORIZONTAL | SWT.SEPARATOR );
    FormData fdSpacer = new FormDataBuilder().fullWidth()
      .top( wStepNameField, MARGIN_SIZE )
      .result();
    topSpacer.setLayoutData( fdSpacer );

    //Connection Group
    Group group = new Group( contentComposite, SWT.SHADOW_ETCHED_IN );
    group.setText( BaseMessages.getString( PKG, "CatalogStepDialog.GroupText" ) );
    FormLayout groupLayout = new FormLayout();
    groupLayout.marginWidth = MARGIN_SIZE;
    groupLayout.marginHeight = MARGIN_SIZE;
    group.setLayout( groupLayout );
    FormData groupLayoutData = new FormDataBuilder().fullWidth()
      .top( topSpacer, MARGIN_SIZE )
      .result();
    group.setLayoutData( groupLayoutData );
    props.setLook( group );

    // Host Field Start
    wlHost = new Label( group, SWT.LEFT );
    props.setLook( wlHost );
    wlHost.setText( BaseMessages.getString( PKG, "CatalogStepDialog.Host" ) );
    FormData fdlHost = new FormDataBuilder().left()
      .top()
      .result();
    wlHost.setLayoutData( fdlHost );

    wHost = new TextVar( transMeta, group, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
    props.setLook( wHost );
    FormData fdHost = new FormDataBuilder().left()
      .top( wlHost, LABEL_SPACING )
      .width( LARGE_FIELD )
      .result();
    wHost.setLayoutData( fdHost );
    // Host Field End

    // Username Field Start
    wlUsername = new Label( group, SWT.LEFT );
    props.setLook( wlUsername );
    wlUsername.setText( BaseMessages.getString( PKG, "CatalogStepDialog.Username" ) );
    FormData fdlUsername = new FormDataBuilder().left()
      .top( wHost, ELEMENT_SPACING )
      .result();
    wlUsername.setLayoutData( fdlUsername );

    wUsername = new TextVar( transMeta, group, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
    props.setLook( wUsername );
    FormData fdUsername = new FormDataBuilder().left()
      .top( wlUsername, LABEL_SPACING )
      .width( LARGE_FIELD )
      .result();
    wUsername.setLayoutData( fdUsername );
    // Username Field End

    // Password Field Start
    wlPassword = new Label( group, SWT.LEFT );
    props.setLook( wlPassword );
    wlPassword.setText( BaseMessages.getString( PKG, "CatalogStepDialog.Password" ) );
    FormData fdlPassword = new FormDataBuilder().left()
      .top( wUsername, ELEMENT_SPACING )
      .result();
    wlPassword.setLayoutData( fdlPassword );

    wPassword = new PasswordTextVar( transMeta, group, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
    props.setLook( wPassword );
    FormData fdPassword = new FormDataBuilder().left()
      .top( wlPassword, LABEL_SPACING )
      .width( LARGE_FIELD )
      .result();
    wPassword.setLayoutData( fdPassword );
    // Password Field End

    //Groups for first type of content
    Group catalogGroup = new Group( contentComposite, SWT.SHADOW_ETCHED_IN );
    catalogGroup.setText( BaseMessages.getString( PKG, "CatalogStepDialog.CatalogInfo" ) );
    catalogGroup.setLayout( groupLayout );
    FormData catalogGroupLayoutData = new FormDataBuilder().fullWidth()
      .top( group, MARGIN_SIZE )
      .result();
    catalogGroup.setLayoutData( catalogGroupLayoutData );
    props.setLook( catalogGroup );

    // Data Resource Key Field Start
    wlDataType = new Label( catalogGroup, SWT.LEFT );
    props.setLook( wlDataType );
    wlDataType.setText( BaseMessages.getString( PKG, "CatalogStepDialog.DataType" ) );
    FormData fdlDataType = new FormDataBuilder().left()
      .top()
      .result();
    wlDataType.setLayoutData( fdlDataType );

    wDataType = new Combo( catalogGroup, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
    props.setLook( wDataType );
    FormData fdDataType = new FormDataBuilder().left()
      .top( wlDataType, LABEL_SPACING )
      .width( LARGE_FIELD )
      .result();
    wDataType.setLayoutData( fdDataType );

    wDataType.setItems( CatalogMeta.DATA_TYPES );
    wDataType.setText( CatalogMeta.DATA_RESOURCE );
    // Data Resource Key Field End

    // Data Resource Key Field Start
    wlDataResourceKey = new Label( catalogGroup, SWT.LEFT );
    props.setLook( wlDataResourceKey );
    wlDataResourceKey.setText( BaseMessages.getString( PKG, "CatalogStepDialog.DataResourceKey" ) );
    FormData fdlDataResourceKey = new FormDataBuilder().left()
      .top( wDataType, ELEMENT_SPACING )
      .result();
    wlDataResourceKey.setLayoutData( fdlDataResourceKey );

    wDataResourceKey = new TextVar( transMeta, catalogGroup, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
    props.setLook( wDataResourceKey );
    FormData fdDataResourceKey = new FormDataBuilder().left()
      .top( wlDataResourceKey, LABEL_SPACING )
      .width( LARGE_FIELD )
      .result();
    wDataResourceKey.setLayoutData( fdDataResourceKey );
    // Data Resource Key Field End

    // Output Field Start
    wlOutputFieldName = new Label( catalogGroup, SWT.LEFT );
    props.setLook( wlOutputFieldName );
    wlOutputFieldName.setText( BaseMessages.getString( PKG, "CatalogStepDialog.OutputFieldName" ) );
    FormData fdlOutputFieldName = new FormDataBuilder().left()
      .top( wDataResourceKey, ELEMENT_SPACING )
      .result();
    wlOutputFieldName.setLayoutData( fdlOutputFieldName );

    wOutputFieldName = new TextVar( transMeta, catalogGroup, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
    props.setLook( wOutputFieldName );
    FormData fdOutputFieldName = new FormDataBuilder().left()
      .top( wlOutputFieldName, LABEL_SPACING )
      .width( LARGE_FIELD )
      .result();
    wOutputFieldName.setLayoutData( fdOutputFieldName );
    // Output Field End

    //Cancel, action and OK buttons for the bottom of the window.
    wCancel = new Button( shell, SWT.PUSH );
    wCancel.setText( BaseMessages.getString( PKG, "System.Button.Cancel" ) );
    FormData fdCancel = new FormDataBuilder().right( 100, -MARGIN_SIZE )
      .bottom()
      .result();
    wCancel.setLayoutData( fdCancel );

    wOK = new Button( shell, SWT.PUSH );
    wOK.setText( BaseMessages.getString( PKG, "System.Button.OK" ) );
    FormData fdOk = new FormDataBuilder().right( wCancel, -LABEL_SPACING )
      .bottom()
      .result();
    wOK.setLayoutData( fdOk );

    //Space between bottom buttons and the table, final layout for table
    Label bottomSpacer = new Label( shell, SWT.HORIZONTAL | SWT.SEPARATOR );
    FormData fdhSpacer = new FormDataBuilder().left()
      .right( 100, -MARGIN_SIZE )
      .bottom( wCancel, -MARGIN_SIZE )
      .result();
    bottomSpacer.setLayoutData( fdhSpacer );

    //Add everything to the scrolling composite
    scrolledComposite.setContent( contentComposite );
    scrolledComposite.setExpandVertical( true );
    scrolledComposite.setExpandHorizontal( true );
    scrolledComposite.setMinSize( contentComposite.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );

    scrolledComposite.setLayout( new FormLayout() );
    FormData fdScrolledComposite = new FormDataBuilder().fullWidth()
      .top()
      .bottom( bottomSpacer, -MARGIN_SIZE )
      .result();
    scrolledComposite.setLayoutData( fdScrolledComposite );
    props.setLook( scrolledComposite );

    //Listeners
    lsCancel = new Listener() {
      public void handleEvent( Event e ) {
        cancel();
      }
    };
    lsOK = new Listener() {
      public void handleEvent( Event e ) {
        ok();
      }
    };

    wOK.addListener( SWT.Selection, lsOK );
    wCancel.addListener( SWT.Selection, lsCancel );

    lsDef = new SelectionAdapter() {
      public void widgetDefaultSelected( SelectionEvent e ) {
        ok();
      }
    };
    wStepNameField.addSelectionListener( lsDef );

    shell.addShellListener( new ShellAdapter() {
      public void shellClosed( ShellEvent e ) {
        cancel();
      }
    } );

    getData( meta );

    //Show shell
    setSize();
    meta.setChanged( changed );
    shell.open();
    while ( !shell.isDisposed() ) {
      if ( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
    return stepname;
  }

  private Image getImage() {
    PluginInterface plugin =
      PluginRegistry.getInstance().getPlugin( StepPluginType.class, stepMeta.getStepMetaInterface() );
    String id = plugin.getIds()[ 0 ];
    if ( id != null ) {
      return GUIResource.getInstance().getImagesSteps().get( id ).getAsBitmapForSize( shell.getDisplay(),
        ConstUI.ICON_SIZE, ConstUI.ICON_SIZE );
    }
    return null;
  }

  private void getData( CatalogMeta meta ) {
    wHost.setText( meta.getUrl() );
    wUsername.setText( meta.getUsername() );
    wPassword.setText( meta.getPassword() );
    wDataType.setText( meta.getDataType() );
    wDataResourceKey.setText( meta.getDataResourceKey() );
    wOutputFieldName.setText( meta.getOutputFieldName() );
  }

  private void getInfo( CatalogMeta meta ) {
    meta.setUrl( wHost.getText() );
    meta.setUsername( wUsername.getText() );
    meta.setPassword( wPassword.getText() );
    meta.setDataType( wDataType.getText() );
    meta.setDataResourceKey( wDataResourceKey.getText() );
    meta.setOutputFieldName( wOutputFieldName.getText() );
  }

  private void cancel() {
    dispose();
  }

  private void ok() {
    stepname = wStepNameField.getText();

    getInfo( meta );

    dispose();
  }
}