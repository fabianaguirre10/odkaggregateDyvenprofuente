package org.odk.collect.android.activities.engineclass;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.odk.collect.android.R;
import org.odk.collect.android.activities.AboutActivity;
import org.odk.collect.android.activities.FormChooserListActivity;
import org.odk.collect.android.activities.MainMenuActivity;
import org.odk.collect.android.activities.ScanQRCodeActivity;
import org.odk.collect.android.analytics.Analytics;
import org.odk.collect.android.analytics.AnalyticsEvents;
import org.odk.collect.android.application.Collect;


import org.odk.collect.android.database.BaseDatosEngine;
import org.odk.collect.android.database.Entidades.*;
import org.odk.collect.android.database.Entidades.ConfiguracionSession;
import org.odk.collect.android.database.EstructuraBD;
import org.odk.collect.android.listeners.PermissionListener;
import org.odk.collect.android.preferences.AdminKeys;
import org.odk.collect.android.preferences.AdminPasswordDialogFragment;
import org.odk.collect.android.preferences.AdminPreferencesActivity;
import org.odk.collect.android.preferences.PreferencesActivity;
import org.odk.collect.android.utilities.AdminPasswordProvider;
import org.odk.collect.android.utilities.DialogUtils;
import org.odk.collect.android.utilities.PermissionUtils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.inject.Inject;

import static org.odk.collect.android.utilities.DialogUtils.showIfNotShowing;

public class principal extends AppCompatActivity {
    private static final int PASSWORD_DIALOG = 1;
    final ConfiguracionSession objconfiguracionSession = new ConfiguracionSession();
    final BranchSession objBranchSeccion = new BranchSession();
    final CodigoSession objcodigoSession = new CodigoSession();
    final CuentaSession objcuentaSession = new CuentaSession();
    final FiltrosBusqueda objFiltrosBusqueda= new FiltrosBusqueda();
    Engine_util objutil;
    TextView txtnombrecampania;
    Spinner cmbnumeroruta;
    private static final boolean EXIT = true;
    EditText txtbuscar;
    private SharedPreferences adminPreferences;
    ProgressDialog progress;
    @Inject
    public Analytics analytics;
    @Inject
    AdminPasswordProvider adminPasswordProvider;
    JSONArray respJSON= new JSONArray();
    ArrayList<Category> category = new ArrayList<Category>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new PermissionUtils().requestLocationPermissions(this, new PermissionListener() {
            @Override public void granted() {

            }

            @Override public void denied() { }
        });
        txtnombrecampania = (TextView) findViewById(R.id.txnombrecampania);
        cmbnumeroruta = (Spinner) findViewById(R.id.cmbnumeroruta);
        txtbuscar = (EditText) findViewById(R.id.txtcodbusqueda);
        adminPreferences = this.getSharedPreferences(
                AdminPreferencesActivity.ADMIN_PREFERENCES, 0);
        objutil= new Engine_util();
        FloatingActionButton fab = findViewById(R.id.fab);
        //fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (objcuentaSession.getCu_ID() != "") {
                    final android.app.AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new android.app.AlertDialog.Builder(view.getContext(), android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new android.app.AlertDialog.Builder(view.getContext());
                    }
                    builder.setTitle("Código de local nuevo");
                    builder.setMessage("¿Desea obtener código nuevo?");
                    builder.setPositiveButton("Asignar Código", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            CargarNuevos();
                            if (objcodigoSession.getcId() != "") {
                                objBranchSeccion.setE_nuevo("nuevo");
                                objBranchSeccion.setE_mainStreet("");
                                objBranchSeccion.setE_TypeBusiness("");
                                objBranchSeccion.setE_name("");
                                objBranchSeccion.setE_reference("");
                                objBranchSeccion.setE_Phone("");
                                objBranchSeccion.setE_comment("");
                                objBranchSeccion.setFVisibilidadestado("I");
                                objBranchSeccion.setFDisponibilidadestado("I");
                                objBranchSeccion.setFAccesibilidadestado("ok");
                                objBranchSeccion.setFExtra_visibilidadestado("I");
                                objBranchSeccion.setFInventariosestado("ok");
                                objBranchSeccion.setFPosicionamientoestado("ok");
                                /*crear codigo nuevo*/
                                BaseDatosEngine usdbh = new BaseDatosEngine();
                                if (objBranchSeccion.getE_code().equals("") == false) {
                                    usdbh = usdbh.open();
                                    ContentValues Objdatosnuevos = new ContentValues();
                                    Objdatosnuevos.put(EstructuraBD.CabecerasCodigos.codeunico, objBranchSeccion.getE_code());
                                    Objdatosnuevos.put(EstructuraBD.CabecerasCodigos.uri, "ocupado");
                                    String where = "codeunico='" + objBranchSeccion.getE_code() + "'";
                                    usdbh.ActualizarTablaCodigos(Objdatosnuevos, where);
                                }
                                usdbh.close();
                                crearLocalNuevo();
                                Intent intent = new Intent(getApplication(), FormChooserListActivity.class);
                                startActivityForResult(intent, 0);
                            } else {
                                ConnectivityManager connMgr = (ConnectivityManager)
                                        getSystemService(getApplication().CONNECTIVITY_SERVICE);
                                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                                if (networkInfo != null && networkInfo.isConnected()) {
                                    // DatosConsulta();
                                    CargarCodigosNuvos fetchJsonTask = new CargarCodigosNuvos(builder.getContext());
                                    fetchJsonTask.execute();
                                    //fetchJsonTask.getStatus();

                                } else {
                                    Toast.makeText(getApplication(),
                                            "Sin Conexión a la red seleccione Digitar Código", Toast.LENGTH_SHORT).show();

                                }

                            }
                        }
                    });

                    builder.setIcon(android.R.drawable.ic_dialog_alert);
                    builder.show();


                } else

                {
                    Toast.makeText(getApplication(),
                            "Seleccione una cuenta", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button btnbuscartareacodigo = (Button) findViewById(R.id.btnbuscartareacodigo);
        Button btninforme = (Button) findViewById(R.id.btninforme);
        if (objconfiguracionSession.getCnf_CampaniaNombre() == null) {
            objutil.CargarConfiguracion();
            if (objconfiguracionSession.getCnf_CampaniaNombre() == null) {
                Toast.makeText(getApplication(),
                        "Configurar Cuenta..!!!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, configuraciogeneral.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            } else {
                txtnombrecampania.setText(objconfiguracionSession.getCnf_CampaniaNombre().toString());
                ListarRutasEngine();

            }
        } else {
            txtnombrecampania.setText(objconfiguracionSession.getCnf_CampaniaNombre().toString());
            ListarRutasEngine();
        }
        cmbnumeroruta.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               android.view.View v, int position, long id) {
                        String valor = (String) parent.getItemAtPosition(position);

                        //buscarlocalesruta(valor, objconfiguracionSession.getCnf_factorbusqueda().toString().toUpperCase());

                        //llama fragmento para cargar pendientes
                        objFiltrosBusqueda.setF_codigo("");
                        objFiltrosBusqueda.setF_Ruta(valor);
                        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
                        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(getApplication().getApplicationContext(),getSupportFragmentManager(),valor,"");
                        viewPager.setAdapter(adapter);
                        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
                        tabLayout.setupWithViewPager(viewPager);
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
        btninforme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cmbnumeroruta.getSelectedItem().toString().equals("") == false) {



                }
            }

        });
        btnbuscartareacodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtbuscar.getText().toString().equals("") == false) {
                    //buscarlocalesrutaCodigo(cmbnumeroruta.getSelectedItem().toString(), objconfiguracionSession.getCnf_factorbusqueda().toString().toUpperCase());
                    objFiltrosBusqueda.setF_codigo(txtbuscar.getText().toString());
                    objFiltrosBusqueda.setF_Ruta(cmbnumeroruta.getSelectedItem().toString());
                    ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
                    SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(getApplication().getApplicationContext(),getSupportFragmentManager(),cmbnumeroruta.getSelectedItem().toString(),txtbuscar.getText().toString());
                    viewPager.setAdapter(adapter);
                    TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
                    tabLayout.setupWithViewPager(viewPager);
                }else{
                    objFiltrosBusqueda.setF_codigo("");
                    objFiltrosBusqueda.setF_Ruta(cmbnumeroruta.getSelectedItem().toString());
                    // buscarlocalesruta(cmbnumeroruta.getSelectedItem().toString(), objconfiguracionSession.getCnf_factorbusqueda().toString().toUpperCase());
                    ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
                    SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(getApplication().getApplicationContext(),getSupportFragmentManager(),cmbnumeroruta.getSelectedItem().toString(),"");
                    viewPager.setAdapter(adapter);
                    TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
                    tabLayout.setupWithViewPager(viewPager);
                }
            }

        });
    }
    public void crearLocalNuevo(){
        BaseDatosEngine usdbh = new BaseDatosEngine();
        usdbh = usdbh.open();
        ContentValues Objdatosingreso = new ContentValues();
        Objdatosingreso.put(EstructuraBD.CabecerasEngine.idbranch, objBranchSeccion.getE_idbranch());
        Objdatosingreso.put(EstructuraBD.CabecerasEngine.idAccount, objBranchSeccion.getE_idAccount());
        Objdatosingreso.put(EstructuraBD.CabecerasEngine.externalCode, objBranchSeccion.getE_externalCode());
        Objdatosingreso.put(EstructuraBD.CabecerasEngine.code, objBranchSeccion.getE_code());
        Objdatosingreso.put(EstructuraBD.CabecerasEngine.name, objBranchSeccion.getE_name());
        Objdatosingreso.put(EstructuraBD.CabecerasEngine.mainStreet, objBranchSeccion.getE_name());
        Objdatosingreso.put(EstructuraBD.CabecerasEngine.neighborhood, objBranchSeccion.getE_name());
        Objdatosingreso.put(EstructuraBD.CabecerasEngine.reference, objBranchSeccion.getE_name());
        Objdatosingreso.put(EstructuraBD.CabecerasEngine.propietario, objBranchSeccion.getE_name());
        Objdatosingreso.put(EstructuraBD.CabecerasEngine.uriformulario, objBranchSeccion.getE_name());
        Objdatosingreso.put(EstructuraBD.CabecerasEngine.idprovince, objBranchSeccion.getE_name());
        Objdatosingreso.put(EstructuraBD.CabecerasEngine.iddistrict, objBranchSeccion.getE_name());
        Objdatosingreso.put(EstructuraBD.CabecerasEngine.idParish, objBranchSeccion.getE_name());
        Objdatosingreso.put(EstructuraBD.CabecerasEngine.rutaaggregate, cmbnumeroruta.getSelectedItem().toString());
        Objdatosingreso.put(EstructuraBD.CabecerasEngine.imeI_ID, obterImeid());
        Objdatosingreso.put(EstructuraBD.CabecerasEngine.LatitudeBranch, "0");
        Objdatosingreso.put(EstructuraBD.CabecerasEngine.LenghtBranch, "0");
        Objdatosingreso.put(EstructuraBD.CabecerasEngine.EstadoFormulario, objBranchSeccion.getE_EstadoFormulario());
        Objdatosingreso.put(EstructuraBD.CabecerasEngine.Colabora, objBranchSeccion.getE_Colabora());
        Objdatosingreso.put(EstructuraBD.CabecerasEngine.formulariomedicion,"I");
        Objdatosingreso.put(EstructuraBD.CabecerasEngine.formulariopercha,"I");
        Objdatosingreso.put(EstructuraBD.CabecerasEngine.formulariopop,"I");
        Objdatosingreso.put(EstructuraBD.CabecerasEngine.formulariopromocion,"I");
        Objdatosingreso.put(EstructuraBD.CabecerasEngine.actividades,"X0001-");
        Objdatosingreso.put(EstructuraBD.CabecerasEngine.formularioactividades,"I");
        Objdatosingreso.put(EstructuraBD.CabecerasEngine.ESTADOAGGREGATE,"S");

        Objdatosingreso.put(EstructuraBD.CabecerasEngine.FVisibilidad,"I");
        Objdatosingreso.put(EstructuraBD.CabecerasEngine.FDisponibilidad,"I");
        Objdatosingreso.put(EstructuraBD.CabecerasEngine.FAccesibilidad,"ok");
        Objdatosingreso.put(EstructuraBD.CabecerasEngine.FExtra_visibilidad,"I");
        Objdatosingreso.put(EstructuraBD.CabecerasEngine.FInventarios,"ok");
        Objdatosingreso.put(EstructuraBD.CabecerasEngine.FPosicionamiento,"ok");



        usdbh.insertardatos(Objdatosingreso);
        usdbh.close();
        objBranchSeccion.setE_actividades("X0001-");
    }
    public void ListarRutasEngine() {
        category=new ArrayList<>();
        Cursor cursor = objutil.ListarRutas();

        //startManagingCursor(cursor);
        ArrayList<String> listar = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            do {
                listar.add(String.valueOf(cursor.getString(0)));
            } while (cursor.moveToNext());
        }else{
            startActivity(new Intent(getBaseContext(), configuraciogeneral.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
            finish();
        }
        ArrayAdapter<String> adaptador;
        adaptador = new ArrayAdapter<String>(getApplication(), R.layout.spinner_personalizado, listar);
        cmbnumeroruta.setAdapter(adaptador);
        if(objBranchSeccion!=null) {
            int pos=adaptador.getPosition(objBranchSeccion.getE_rutaaggregate());
            if(pos>-1)
                cmbnumeroruta.setSelection(pos);
        }
    }


    public void CargarNuevos(){
        BaseDatosEngine usdbh = new BaseDatosEngine();
        usdbh = usdbh.open();
        Cursor cursor = usdbh.listarCodigois();
        String CodigoNuevo="";
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
        objBranchSeccion.setE_Phone("");
        objBranchSeccion.setE_Cedula("");

        objBranchSeccion.setE_name("");
        objBranchSeccion.setE_reference("");

        objcodigoSession.setcId("");
        objcodigoSession.setC_idAccount("");
        objcodigoSession.setC_code("");
        objcodigoSession.setC_estado("");
        objcodigoSession.setC_uri("");
        objcodigoSession.setC_imei_id("");



        if(cursor!=null) {
            if (cursor.moveToFirst()) {
                do {
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
                    objBranchSeccion.setE_Colabora("");
                    objBranchSeccion.setE_TypeBusiness("");
                    objBranchSeccion.setE_Phone("");
                    objBranchSeccion.setE_name("");
                    objBranchSeccion.setE_reference("");
                    objBranchSeccion.setE_Cedula("");
                    objBranchSeccion.setE_comment("");
                    Cursor cursor1 = usdbh.CodigoNuevo(cursor.getString(0));
                    if (cursor1.moveToFirst()) {
                        do {
                            objBranchSeccion.setE_code(cursor1.getString(6));
                            objcodigoSession.setcId(cursor1.getString(0));
                            objcodigoSession.setC_idAccount(cursor1.getString(1));
                            objcodigoSession.setC_code(cursor1.getString(2));
                            objcodigoSession.setC_estado(cursor1.getString(3));
                            objcodigoSession.setC_uri(cursor1.getString(4));
                            objcodigoSession.setC_imei_id(cursor1.getString(5));
                            objcodigoSession.setC_codeunico(cursor1.getString(6));
                            objBranchSeccion.setE_rutaaggregate(cmbnumeroruta.getSelectedItem().toString());
                            objBranchSeccion.setE_nuevo("nuevo");
                            objBranchSeccion.setFVisibilidadestado("I");
                            objBranchSeccion.setFDisponibilidadestado("I");
                            objBranchSeccion.setFAccesibilidadestado("ok");
                            objBranchSeccion.setFExtra_visibilidadestado("I");
                            objBranchSeccion.setFInventariosestado("ok");
                            objBranchSeccion.setFPosicionamientoestado("ok");


                        } while (cursor.moveToNext());
                    }


                } while (cursor.moveToNext());
            }
        }
        usdbh.close();
    }
    @SuppressLint("MissingPermission")
    public String  obterImeid() {
        String myIMEI = "0";
        TelephonyManager mTelephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (mTelephony.getDeviceId() != null) {
            myIMEI = mTelephony.getDeviceId();
        }
        return myIMEI;
    }
    public class CargarCodigosNuvos extends AsyncTask<Void,Void,String> {
        public CargarCodigosNuvos(Context context) {
            this.context = context;
        }
        private ProgressDialog progressDialog;
        private  Context context;
        @Override
        protected void onPreExecute() {
            // set up progress dialog
            try {
                progressDialog = new ProgressDialog(context);
                progressDialog.setIndeterminate(true);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setMessage("Please wait...");

                // show it
                progressDialog.show();
            }catch (Exception ex){

            }

        }
        @Override
        protected String doInBackground(Void... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String jsonStr = null;

            try {
                // Construct the URL somehow
                String Idaccount =objcuentaSession.getCu_idAccount();
                String Idcampania="";
                String url1 = "http://geomardis6728.cloudapp.net/servicioDyvenpro/api/codigos?idAccounut="+Idaccount+"&imail="+obterImeid();
                URL url = new URL(url1);

                // Create the request to MuslimSalat.com, and open the connection

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                jsonStr = buffer.toString();

                //JSONArray objetos = new JSONArray(result);
                respJSON = new JSONArray(jsonStr);
            } catch (IOException e) {
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                    }
                }
            }

            return jsonStr;
        }
        @Override
        protected void onPostExecute(String jsonString) {

            // dismiss the progress because downloading process is finished.
            progressDialog.dismiss();
            progress=new ProgressDialog(context);
            progress.setMessage("Descargando Codigos....");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            // progress.setIndeterminate(true);
            progress.setCanceledOnTouchOutside(false);

            progress.setProgress(0);
            progress.setMax(respJSON.length());
            progress.show();
            final int totalProgressTime = respJSON.length();
            final Thread t = new Thread() {
                @Override
                public void run() {
                    int jumpTime = 0;
                    while(jumpTime < totalProgressTime) {
                        try {
                            JSONObject obj = respJSON.getJSONObject(jumpTime);
                            String id = obj.getString("id");
                            String idAccount = obj.getString("idAccount");
                            String code = obj.getString("code");
                            String estado = obj.getString("estado");
                            String uri = "";
                            String imei_id = obj.getString("imei_id");
                            String codeunico=obj.getString("codeunico");
                            BaseDatosEngine usdbh = new BaseDatosEngine();
                            try {

                                usdbh = usdbh.open();
                                ContentValues Objdatos = new ContentValues();
                                Objdatos.put(EstructuraBD.CabecerasCodigos.ID, id);
                                Objdatos.put(EstructuraBD.CabecerasCodigos.idAccount, idAccount);
                                Objdatos.put(EstructuraBD.CabecerasCodigos.code, code.toUpperCase());
                                Objdatos.put(EstructuraBD.CabecerasCodigos.estado, estado.toUpperCase());
                                Objdatos.put(EstructuraBD.CabecerasCodigos.uri, uri.toUpperCase());
                                Objdatos.put(EstructuraBD.CabecerasCodigos.imei_id, imei_id);
                                Objdatos.put(EstructuraBD.CabecerasCodigos.codeunico, codeunico);
                                usdbh.insertardatosCodigos(Objdatos);
                                usdbh.close();
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            jumpTime += 1;
                            progress.setProgress(jumpTime);
                            sleep(25);
                        }
                        catch (InterruptedException e) {
                            Log.e("Error Carga",e.getMessage());
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                    CargarNuevos();
                    //crearLocalNuevo();
                    if (objcodigoSession.getcId() != "") {
                        BaseDatosEngine usdbh = new BaseDatosEngine();
                        Cursor cursor = usdbh.RutaLista();
                        objBranchSeccion.setE_rutaaggregate("0");
                        if(cursor!=null) {
                            if (cursor.moveToFirst()) {

                                do {
                                    objBranchSeccion.setE_rutaaggregate(cursor.getString(0));
                                    objBranchSeccion.setE_nuevo("nuevo");

                                } while (cursor.moveToNext());


                            }
                        }



                        Intent intent = new Intent(getApplication(), FormChooserListActivity.class);
                        startActivityForResult(intent, 0);
                    } else {
                        //Toast.makeText(getApplication(),
                        //"No se pudo cargar los códigos nuevos", Toast.LENGTH_SHORT).show();
                    }
                    progress.dismiss();
                }
            };
            t.start();
            try {
                t.join();

                CargarNuevos();
                BaseDatosEngine usdbh = new BaseDatosEngine();
                if (objBranchSeccion.getE_code().equals("") == false) {
                    usdbh = usdbh.open();
                    ContentValues Objdatosnuevos = new ContentValues();
                    Objdatosnuevos.put(EstructuraBD.CabecerasCodigos.codeunico, objBranchSeccion.getE_code());
                    Objdatosnuevos.put(EstructuraBD.CabecerasCodigos.uri, "ocupado");
                    String where = "codeunico='" + objBranchSeccion.getE_code() + "'";
                    usdbh.ActualizarTablaCodigos(Objdatosnuevos, where);
                }
                usdbh.close();
                crearLocalNuevo();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //progress.dismiss();



        }
    }
    @Override
    protected void onStart() {
        txtbuscar.setText("");
        if (txtbuscar.getText().toString().equals("") == false) {

            objFiltrosBusqueda.setF_codigo(txtbuscar.getText().toString());
            objFiltrosBusqueda.setF_Ruta(cmbnumeroruta.getSelectedItem().toString());
            ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
            SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(getApplication().getApplicationContext(),getSupportFragmentManager(),cmbnumeroruta.getSelectedItem().toString(),txtbuscar.getText().toString());
            viewPager.setAdapter(adapter);
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
        }else{
            objFiltrosBusqueda.setF_codigo("");
            objFiltrosBusqueda.setF_Ruta(cmbnumeroruta.getSelectedItem().toString());

            ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
            SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(getApplication().getApplicationContext(),getSupportFragmentManager(),cmbnumeroruta.getSelectedItem().toString(),"");
            viewPager.setAdapter(adapter);
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
        }
        super.onStart();

    }

    @Override
    protected void onStop() {

        super.onStop();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_configure_qr_code:
                analytics.logEvent(AnalyticsEvents.SCAN_QR_CODE, "MainMenu");

                if (adminPasswordProvider.isAdminPasswordSet()) {
                    Bundle args = new Bundle();
                    args.putSerializable(AdminPasswordDialogFragment.ARG_ACTION, AdminPasswordDialogFragment.Action.SCAN_QR_CODE);
                    showIfNotShowing(AdminPasswordDialogFragment.class, args, getSupportFragmentManager());
                } else {
                    startActivity(new Intent(this, ScanQRCodeActivity.class));
                }
                return true;
            case R.id.menu_cuentaconf:

                startActivity(new Intent(getBaseContext(), configuraciogeneral.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                finish();
                return true;

            case R.id.menu_main:

                startActivity(new Intent(getBaseContext(), MainMenuActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                finish();
                return true;



            case R.id.menu_about:

                Intent aboutIntent = new Intent(this, AboutActivity.class);
                startActivity(aboutIntent);
                return true;
            case R.id.menu_general_preferences:

                Intent ig = new Intent(this, PreferencesActivity.class);
                startActivity(ig);
                return true;
            case R.id.menu_admin_preferences:
                String pw = adminPreferences.getString(
                        AdminKeys.KEY_ADMIN_PW, "");
                if ("".equalsIgnoreCase(pw)) {
                    startActivity(new Intent(this, AdminPreferencesActivity.class));
                } else {
                    showDialog(PASSWORD_DIALOG);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = this.getSharedPreferences(
                AdminPreferencesActivity.ADMIN_PREFERENCES, 0);

        boolean edit = sharedPreferences.getBoolean(
                AdminKeys.KEY_EDIT_SAVED, true);


        boolean send = sharedPreferences.getBoolean(
                AdminKeys.KEY_SEND_FINALIZED, true);


        boolean viewSent = sharedPreferences.getBoolean(
                AdminKeys.KEY_VIEW_SENT, true);







    }


}
