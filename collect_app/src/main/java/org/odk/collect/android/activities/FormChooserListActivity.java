/*
 * Copyright (C) 2009 University of Washington
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.odk.collect.android.activities;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.CrossProcessCursorWrapper;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import org.odk.collect.android.R;
import org.odk.collect.android.activities.engineclass.principal;
import org.odk.collect.android.adapters.FormListAdapter;
import org.odk.collect.android.dao.FormsDao;
import org.odk.collect.android.database.Entidades.BranchSession;
import org.odk.collect.android.listeners.DiskSyncListener;
import org.odk.collect.android.listeners.PermissionListener;
import org.odk.collect.android.preferences.GeneralKeys;
import org.odk.collect.android.preferences.GeneralSharedPreferences;
import org.odk.collect.android.provider.FormsProviderAPI.FormsColumns;
import org.odk.collect.android.storage.StorageInitializer;
import org.odk.collect.android.tasks.DiskSyncTask;
import org.odk.collect.android.utilities.ApplicationConstants;
import org.odk.collect.android.utilities.MultiClickGuard;
import org.odk.collect.android.utilities.PermissionUtils;

import androidx.annotation.NonNull;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import timber.log.Timber;

import static org.odk.collect.android.utilities.PermissionUtils.finishAllActivities;

/**
 * Responsible for displaying all the valid forms in the forms directory. Stores the path to
 * selected form for use by {@link MainMenuActivity}.
 *
 * @author Yaw Anokwa (yanokwa@gmail.com)
 * @author Carl Hartung (carlhartung@gmail.com)
 */
public class FormChooserListActivity extends FormListActivity implements
        DiskSyncListener, AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    private static final String FORM_CHOOSER_LIST_SORTING_ORDER = "formChooserListSortingOrder";

    private static final boolean EXIT = true;
    private static final String syncMsgKey = "syncmsgkey";
    private static final int INTERVALO = 2000; //2 segundos para salir
    private long tiempoPrimerClick;
    private DiskSyncTask diskSyncTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_chooser_list);

        setTitle(getString(R.string.enter_data));

        new PermissionUtils().requestStoragePermissions(this, new PermissionListener() {
            @Override
            public void granted() {
                // must be at the beginning of any activity that can be called from an external intent
                try {
                    new StorageInitializer().createOdkDirsOnStorage();
                    init();
                } catch (RuntimeException e) {
                    createErrorDialog(e.getMessage(), EXIT);
                    return;
                }
            }

            @Override
            public void denied() {
                // The activity has to finish because ODK Collect cannot function without these permissions.
                finishAllActivities(FormChooserListActivity.this);
            }
        });
    }

    private void init() {
        setupAdapter();

        // DiskSyncTask checks the disk for any forms not already in the content provider
        // that is, put here by dragging and dropping onto the SDCard
        diskSyncTask = (DiskSyncTask) getLastCustomNonConfigurationInstance();
        if (diskSyncTask == null) {
            Timber.i("Starting new disk sync task");
            diskSyncTask = new DiskSyncTask();
            diskSyncTask.setDiskSyncListener(this);
            diskSyncTask.execute((Void[]) null);
        }
        sortingOptions = new int[] {
                R.string.sort_by_name_asc, R.string.sort_by_name_desc,
                R.string.sort_by_date_asc, R.string.sort_by_date_desc,
        };

        setupAdapter();
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }
    @Override
    public void onBackPressed(){
        boolean resultadop=true;

            super.onBackPressed();
            /*validar que los formularios esten terminados tanto en actividades y medición*/

            BranchSession objBranchSeccion= new BranchSession();
            if((objBranchSeccion.getE_EstadoFormulario().equals("cerrado")||objBranchSeccion.getE_EstadoFormulario().equals("no_celular")||objBranchSeccion.getE_EstadoFormulario().equals("no_existe")) &&   objBranchSeccion.getFVisibilidadestado().equals("ok")){
                resultadop=true;
            }else if (!objBranchSeccion.getFVisibilidadestado().equals("ok") && !objBranchSeccion.getE_EstadoFormulario().equals("cerrado") ) {
                resultadop = false;
            }else if(!objBranchSeccion.getFDisponibilidadestado().equals("ok")) {
                resultadop=false;
            }else if(!objBranchSeccion.getFAccesibilidadestado().equals("ok") ) {
                resultadop=false;
            }else if(!objBranchSeccion.getFExtra_visibilidadestado().equals("ok")) {
                resultadop=false;
            }else if(!objBranchSeccion.getFInventariosestado().equals("ok")) {
                resultadop=false;
            }else if (!objBranchSeccion.getFPosicionamientoestado().equals("ok")) {
                resultadop=false;
            } else  if(!objBranchSeccion.getFVisibilidadestado().equals("ok")) {
                resultadop=false;
           }
            if(!objBranchSeccion.getFVisibilidadestado().equals("ok")&&!objBranchSeccion.getFDisponibilidadestado().equals("ok")&&!objBranchSeccion.getFExtra_visibilidadestado().equals("ok"))
            {
                resultadop=true;
            }
            if(resultadop==true) {
                objBranchSeccion.setE_ID("");
                objBranchSeccion.setE_idbranch("");
                objBranchSeccion.setE_idAccount("");
                objBranchSeccion.setE_externalCode("");
                objBranchSeccion.setE_code("");
                objBranchSeccion.setE_neighborhood("");
                objBranchSeccion.setE_mainStreet("");
                objBranchSeccion.setE_reference("");
                objBranchSeccion.setE_propietario("");
                objBranchSeccion.setE_uriformulario("");
                objBranchSeccion.setE_idprovince("");
                objBranchSeccion.setE_iddistrict("");
                objBranchSeccion.setE_idParish("");
                objBranchSeccion.setE_rutaaggregate("0");
                objBranchSeccion.setE_imeI_ID("");
                objBranchSeccion.setE_TypeBusiness("");
                objBranchSeccion.setE_nuevo("");
                objBranchSeccion.setE_name("");
                objBranchSeccion.setE_festadomedicion("");
                objBranchSeccion.setE_festadopercha("");
                objBranchSeccion.setE_festadopop("");
                objBranchSeccion.setE_festadopromocion("");
                objBranchSeccion.setE_actividades("");
                objBranchSeccion.setE_festadoactividades("");
                objBranchSeccion.setE_ESTADOAGGREGATE("");

                startActivity(new Intent(this, principal.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                finish();
                return;
            }else {
                Toast.makeText(this, "Completar todos los modulos para regresar a la ruta..!!!.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, FormChooserListActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                finish();
                return;
            }

    }
    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        // pass the thread on restart
        return diskSyncTask;
    }

    /**
     * Stores the path of selected form and finishes.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (MultiClickGuard.allowClick(getClass().getName())) {
            // get uri to form
            long idFormsTable = listView.getAdapter().getItemId(position);
            Uri formUri = ContentUris.withAppendedId(FormsColumns.CONTENT_URI, idFormsTable);

            String nombre= ((CrossProcessCursorWrapper) parent.getItemAtPosition(position)).getString((((CrossProcessCursorWrapper) parent.getItemAtPosition(position)).getColumnIndex("displayName")));
            BranchSession objFormularios= new BranchSession();
            if(nombre.contains(objFormularios.getE_fvisibilidad())  && objFormularios.getFVisibilidadestado().equals("ok") ){
                Toast.makeText(this, "Tarea ya realizada por cliente", Toast.LENGTH_SHORT).show();
                return;
            }
            if(nombre.contains(objFormularios.getE_fdisponibilidad()) && objFormularios.getFDisponibilidadestado().equals("ok") ){
                Toast.makeText(this, "Tarea ya realizada por cliente", Toast.LENGTH_SHORT).show();
                return;
            }else{
                if( !objFormularios.getFVisibilidadestado().equals("ok") && !nombre.contains(objFormularios.getE_fvisibilidad())){
                    Toast.makeText(this, "Debe realizar el formulario de Visibilidad antes de ejecutar este modulo..!!!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if(nombre.contains(objFormularios.getE_faccesibilidad()) && objFormularios.getFAccesibilidadestado().equals("ok") ){
                Toast.makeText(this, "Tarea ya realizada por cliente", Toast.LENGTH_SHORT).show();
                return;
            }else{
                if( !objFormularios.getFVisibilidadestado().equals("ok") && !nombre.contains(objFormularios.getE_fvisibilidad())){
                    Toast.makeText(this, "Debe realizar el formulario de Visibilidad antes de ejecutar este modulo..!!!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if(nombre.contains(objFormularios.getE_fextra_visibilidad()) && objFormularios.getFExtra_visibilidadestado().equals("ok") ){
                Toast.makeText(this, "Tarea ya realizada por cliente", Toast.LENGTH_SHORT).show();
                return;
            }else{
                if( !objFormularios.getFVisibilidadestado().equals("ok") && !nombre.contains(objFormularios.getE_fvisibilidad())){
                    Toast.makeText(this, "Debe realizar el formulario de Visibilidad antes de ejecutar este modulo..!!!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if(nombre.contains(objFormularios.getE_finventarios()) && objFormularios.getFInventariosestado().equals("ok") ){
                Toast.makeText(this, "Tarea ya realizada por cliente", Toast.LENGTH_SHORT).show();
                return;
            }else{
                if( !objFormularios.getFVisibilidadestado().equals("ok") && !nombre.contains(objFormularios.getE_fvisibilidad())){
                    Toast.makeText(this, "Debe realizar el formulario de Visibilidad antes de ejecutar este modulo..!!!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if(nombre.contains(objFormularios.getE_fposicionamiento()) && objFormularios.getFPosicionamientoestado().equals("ok") ){
                Toast.makeText(this, "Tarea ya realizada por cliente", Toast.LENGTH_SHORT).show();
                return;
            }else{
                if( !objFormularios.getFVisibilidadestado().equals("ok") && !nombre.contains(objFormularios.getE_fvisibilidad())){
                    Toast.makeText(this, "Debe realizar el formulario de Visibilidad antes de ejecutar este modulo..!!!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            String action = getIntent().getAction();
            if (Intent.ACTION_PICK.equals(action)) {
                // caller is waiting on a picked form
                setResult(RESULT_OK, new Intent().setData(formUri));
            } else {
                // caller wants to view/edit a form, so launch formentryactivity
                Intent intent = new Intent(Intent.ACTION_EDIT, formUri);
                intent.putExtra(ApplicationConstants.BundleKeys.FORM_MODE, ApplicationConstants.FormModes.EDIT_SAVED);
                startActivity(intent);
            }

            finish();
        }
    }


    public void onMapButtonClick(AdapterView<?> parent, View view, int position, long id) {
        final Uri formUri = ContentUris.withAppendedId(FormsColumns.CONTENT_URI, id);
        final Intent intent = new Intent(Intent.ACTION_EDIT, formUri, this, FormMapActivity.class);
        new PermissionUtils().requestLocationPermissions(this, new PermissionListener() {
            @Override public void granted() {
                startActivity(intent);
            }

            @Override public void denied() { }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (diskSyncTask != null) {
            diskSyncTask.setDiskSyncListener(this);
            if (diskSyncTask.getStatus() == AsyncTask.Status.FINISHED) {
                syncComplete(diskSyncTask.getStatusMessage());
            }
        }
    }

    @Override
    protected void onPause() {
        if (diskSyncTask != null) {
            diskSyncTask.setDiskSyncListener(null);
        }
        super.onPause();
    }

    /**
     * Called by DiskSyncTask when the task is finished
     */
    @Override
    public void syncComplete(@NonNull String result) {
        Timber.i("Disk scan complete");
        hideProgressBarAndAllow();
        showSnackbar(result);
    }

    private void setupAdapter() {
        String[] columnNames = {
                FormsColumns.DISPLAY_NAME,
                FormsColumns.JR_VERSION,
                hideOldFormVersions() ? FormsColumns.MAX_DATE : FormsColumns.DATE,
                FormsColumns.GEOMETRY_XPATH
        };
        int[] viewIds = {
                R.id.form_title,
                R.id.form_subtitle,
                R.id.form_subtitle2,
                R.id.map_view
        };

        listAdapter = new FormListAdapter(
                listView, FormsColumns.JR_VERSION, this, R.layout.form_chooser_list_item,
                this::onMapButtonClick, columnNames, viewIds);
        listView.setAdapter(listAdapter);
    }

    @Override
    protected String getSortingOrderKey() {
        return FORM_CHOOSER_LIST_SORTING_ORDER;
    }

    @Override
    protected void updateAdapter() {
        getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    /**
     * Creates a dialog with the given message. Will exit the activity when the user preses "ok" if
     * shouldExit is set to true.
     */
    private void createErrorDialog(String errorMsg, final boolean shouldExit) {

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
        alertDialog.setMessage(errorMsg);
        DialogInterface.OnClickListener errorListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                switch (i) {
                    case DialogInterface.BUTTON_POSITIVE:
                        if (shouldExit) {
                            finish();
                        }
                        break;
                }
            }
        };
        alertDialog.setCancelable(false);
        alertDialog.setButton(getString(R.string.ok), errorListener);
        alertDialog.show();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        showProgressBar();

        return new FormsDao().getFormsCursorLoader(getFilterText(), getSortingOrder(), hideOldFormVersions());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        hideProgressBarIfAllowed();
        listAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
        listAdapter.swapCursor(null);
    }

    private boolean hideOldFormVersions() {
        return GeneralSharedPreferences.getInstance().getBoolean(GeneralKeys.KEY_HIDE_OLD_FORM_VERSIONS, false);
    }
}
