package org.odk.collect.android.activities.engineclass;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.viewpager.widget.ViewPager;

import android.provider.Settings;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.odk.collect.android.R;
import org.odk.collect.android.activities.AboutActivity;
import org.odk.collect.android.activities.MainMenuActivity;
import org.odk.collect.android.activities.ScanQRCodeActivity;
import org.odk.collect.android.analytics.Analytics;
import org.odk.collect.android.analytics.AnalyticsEvents;
import org.odk.collect.android.application.Collect;
import org.odk.collect.android.database.BaseDatosEngine;
import org.odk.collect.android.database.Entidades.Capania;
import org.odk.collect.android.database.Entidades.ConfiguracionDB;
import org.odk.collect.android.database.Entidades.ConfiguracionSession;
import org.odk.collect.android.database.Entidades.CuentaSession;
import org.odk.collect.android.database.EstructuraBD;
import org.odk.collect.android.injection.config.AppDependencyComponent;
import org.odk.collect.android.injection.config.AppDependencyModule;
import org.odk.collect.android.listeners.PermissionListener;
import org.odk.collect.android.logic.PropertyManager;
import org.odk.collect.android.preferences.AdminKeys;
import org.odk.collect.android.preferences.AdminPasswordDialogFragment;
import org.odk.collect.android.preferences.AdminPreferencesActivity;
import org.odk.collect.android.preferences.FormMetadataFragment;
import org.odk.collect.android.preferences.PreferencesActivity;
import org.odk.collect.android.storage.StorageInitializer;
import org.odk.collect.android.utilities.AdminPasswordProvider;
import org.odk.collect.android.utilities.DeviceDetailsProvider;
import org.odk.collect.android.utilities.DialogUtils;
import org.odk.collect.android.utilities.PermissionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static org.odk.collect.android.preferences.AdminKeys.KEY_ADMIN_PW;
import static org.odk.collect.android.utilities.DialogUtils.showIfNotShowing;


public class configuraciogeneral extends AppCompatActivity {
    Spinner cmbcuentacamp;
    private static final int PASSWORD_DIALOG = 1;
    ProgressDialog progress;
    RadioButton radnombre;
    RadioButton radcodigo;
    JSONArray respJSONcuenta = new JSONArray();
    JSONArray respJSONlocales = new JSONArray();
    JSONArray respJSONhistoriales = new JSONArray();
    TextView imeicargar;
    final CuentaSession objcuentaSession = new CuentaSession();
    final ConfiguracionSession objconfiguracionSession = new ConfiguracionSession();
    String Estado = "";
    static  String imeiphone;
    private SharedPreferences adminPreferences;
    private boolean readPhoneStatePermissionRequestNeeded;
    @Inject
    public Analytics analytics;
    @Inject
    AdminPasswordProvider adminPasswordProvider;

    @Inject
    PermissionUtils permissionUtils;
    private static final boolean EXIT = true;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuraciogeneral);


        new PermissionUtils().requestReadPhoneStatePermission(this, true, new PermissionListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void granted() {

                try {
                    imeiphone="";
                    imeiphone=obterImeid();
                } catch (RuntimeException e) {
                    DialogUtils.showDialog(DialogUtils.createErrorDialog(configuraciogeneral.this,
                            e.getMessage(), EXIT), configuraciogeneral.this);
                    return;
                }
            }

            @Override
            public void denied() {
                finish();
            }
        });
       // imeiphone=obterImeid();

        TextView TextversionImeid = (TextView) findViewById(R.id.txtIdVersionDivice);
        ImageButton copibutton = (ImageButton) findViewById(R.id.IdbtnCopi);
        TextversionImeid.setText(imeiphone);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        cmbcuentacamp = (Spinner) findViewById(R.id.cmbcampaniaCL);
        ImageButton btnCargarCodigosCL = (ImageButton) findViewById(R.id.btncargarlocalesCL);
        Button btncargarlocalcuenta = (Button) findViewById(R.id.btncargarlocalcuenta);
        radnombre = (RadioButton) findViewById(R.id.radnombre);
        radcodigo = (RadioButton) findViewById(R.id.radcodigo);
        imeicargar= (TextView) findViewById(R.id.txtimei);
        imeicargar.setText("\n Dyvenpro Mercaderistas V2.0");
        BaseDatosEngine usdbh = new BaseDatosEngine();
        usdbh = usdbh.open();
        Cursor c = usdbh.ConfiguracionLista();
        ConfiguracionDB actualconf = new ConfiguracionDB();
        CargarListacuenta();
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    actualconf = new ConfiguracionDB();
                    actualconf.setId_cuenta(c.getString(1));
                    actualconf.setId_campania(c.getString(2));
                    actualconf.setFormaBusqueda(c.getString(3));
                    actualconf.setEstado(c.getString(4));
                    Cursor AccountCanpa = usdbh.ConfiguracionCanpania(actualconf.getId_cuenta(), actualconf.getId_campania());
                    if (AccountCanpa != null) {
                        if (AccountCanpa.moveToFirst()) {
                            do {
                                Capania objcampania = new Capania();
                                objcampania.setID(AccountCanpa.getString(0));
                                objcampania.setIdAccount(AccountCanpa.getString(1));
                                objcampania.setAccountNombre(AccountCanpa.getString(2));
                                objcampania.setIdCampania(AccountCanpa.getString(3));
                                objcampania.setCampaniaNombre(AccountCanpa.getString(4));

                                int pos = getIndexSpinner(cmbcuentacamp, objcampania);
                                cmbcuentacamp.setSelection(pos);
                                if (actualconf.getFormaBusqueda().toString().equalsIgnoreCase("C")) {
                                    radcodigo.setChecked(true);
                                    radnombre.setChecked(false);
                                } else {
                                    radcodigo.setChecked(false);
                                    radnombre.setChecked(true);
                                }
                            } while (AccountCanpa.moveToNext());
                        }
                    }
                } while (c.moveToNext());
            }
        }
        usdbh.close();
        copibutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ClipboardManager myClipboard = myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String text = TextversionImeid.getText().toString();
                ClipData clip = ClipData.newPlainText("text", text );
                myClipboard.setPrimaryClip(clip);
                Toast.makeText(getApplication(),
                        "ID fue copiado", Toast.LENGTH_SHORT).show();

            }
        });

        btnCargarCodigosCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(getApplication().CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    // DatosConsulta();
                    CargarCampaniasCuentas cargarCampaniasCuentas = new CargarCampaniasCuentas(v.getContext());
                    cargarCampaniasCuentas.execute();

                } else {
                    Toast.makeText(getApplication(),
                            R.string.no_connection, Toast.LENGTH_SHORT).show();

                }
            }

        });
        cmbcuentacamp.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View v, int position, long id) {
                        Capania currentLead = (Capania) parent.getItemAtPosition(position);
                        objcuentaSession.setCu_ID("");
                        objcuentaSession.setCu_idAccount("");
                        objcuentaSession.setCu_AccountNombre("");
                        objcuentaSession.setCu_idcampania("");
                        objcuentaSession.setCu_CampaniaNombre("");

                        objcuentaSession.setCu_ID(currentLead.getID());
                        objcuentaSession.setCu_idAccount(currentLead.getIdAccount());
                        objcuentaSession.setCu_AccountNombre(currentLead.getAccountNombre());
                        objcuentaSession.setCu_idcampania(currentLead.getIdCampania());
                        objcuentaSession.setCu_CampaniaNombre(currentLead.getCampaniaNombre());

                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
        btncargarlocalcuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (objcuentaSession.getCu_ID() != "") {

                    ConnectivityManager connMgr = (ConnectivityManager)
                            getSystemService(getApplication().CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {
                        // DatosConsulta();
                        BaseDatosEngine usdbh = new BaseDatosEngine();
                        usdbh = usdbh.open();
                        usdbh.EliminarRegistros();
                        usdbh.EliminarRegistrosCodigos();
                        usdbh.close();
                        CargarLocales fetchJsonTask = new CargarLocales(v.getContext());
                        fetchJsonTask.execute();
                    } else {
                        Toast.makeText(getApplication(),
                                R.string.no_connection, Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(getApplication(),
                            "Seleccione una cuenta", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    public void CargarListacuenta() {
        BaseDatosEngine usdbh = new BaseDatosEngine();
        usdbh = usdbh.open();
        Cursor cursor = usdbh.listarCanpanias();
        Capania objcampania = new Capania();
        List<Capania> listOBJ = new ArrayList<Capania>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {

                do {

                    objcampania = new Capania();
                    objcampania.setID(cursor.getString(0));
                    objcampania.setIdAccount(cursor.getString(1));
                    objcampania.setAccountNombre(cursor.getString(2));
                    objcampania.setIdCampania(cursor.getString(3));
                    objcampania.setCampaniaNombre(cursor.getString(4));

                    listOBJ.add(objcampania);

                } while (cursor.moveToNext());


            }

            ArrayAdapter<Capania> adaptador;
            adaptador = new ArrayAdapter<Capania>(getApplication(), R.layout.spinner_personalizado, listOBJ);
            cmbcuentacamp = (Spinner) findViewById(R.id.cmbcampaniaCL);
            cmbcuentacamp.setAdapter(adaptador);
        }
    }

    public static int getIndexSpinner(Spinner spinner, Capania myString) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(myString.toString())) {
                index = i;
            }
        }
        return index;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @SuppressLint("MissingPermission")
    public String obterImeid() {
        final String androidIdName = Settings.Secure.ANDROID_ID;

        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        TelephonyManager mTelephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String simSerialNo="";
        String  myIMEI = mTelephony.getDeviceId();

        if (myIMEI == null) {
            SubscriptionManager subsManager = (SubscriptionManager) getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);

            List<SubscriptionInfo> subsList = subsManager.getActiveSubscriptionInfoList();

            if (subsList!=null) {
                for (SubscriptionInfo subsInfo : subsList) {
                    if (subsInfo != null) {
                        simSerialNo  = subsInfo.getIccId();
                    }
                }

            }
            myIMEI=simSerialNo;
        }

        if (myIMEI == null) {
            myIMEI = Settings.Secure.getString(Collect.getInstance().getApplicationContext().getContentResolver(), androidIdName);
        }
     /* String  deviceId = new PropertyManager(Collect.getInstance().getApplicationContext())
                .getSingularProperty(PropertyManager.withUri(PropertyManager.PROPMGR_DEVICE_ID));*/
        return myIMEI;

    }
    private static String agregarCeros(@NotNull String string, int largo)
    {
        String ceros = "";

        int cantidad = largo - string.length();

        if (cantidad >= 1)
        {
            for(int i=0;i<cantidad;i++)
            {
                ceros += "0";
            }

            return (ceros + string);
        }
        else
            return string;
    }
    //clase para cargar campania
    public class CargarCampaniasCuentas extends AsyncTask<Void,Void,String> {
        public CargarCampaniasCuentas(Context context) {
            this.context = context;
        }
        private ProgressDialog progressDialog;
        private  Context context;
        @Override
        protected void onPreExecute() {
            try {
                progressDialog = new ProgressDialog(context);
                progressDialog.setIndeterminate(true);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
            }catch (Exception ex){
                //Log.e(BonusPackHelper.LOG_TAG, "Error ", ex);
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
                String url1 = "http://geomardis6728.cloudapp.net/servicioDyvenpro/api/Canpania";
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
                respJSONcuenta = new JSONArray(jsonStr);
            } catch (IOException e) {
                //Log.e(BonusPackHelper.LOG_TAG, "Error ", e);
                // If the code didn't successfully get the data, there's no point in attemping
                // to parse it.
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
                        //Log.e(BonusPackHelper.LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            return jsonStr;
        }
        @Override
        protected void onPostExecute(String jsonString) {
            progressDialog.dismiss();
            progress=new ProgressDialog(context);
            progress.setMessage("Descargando Cuentas....");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            // progress.setIndeterminate(true);
            progress.setCanceledOnTouchOutside(false);
            progress.setProgress(0);
            progress.setMax(respJSONcuenta.length());
            progress.show();
            final int totalProgressTime = respJSONcuenta.length();
            final Thread t = new Thread() {
                @Override
                public void run() {
                    int jumpTime = 0;
                    BaseDatosEngine usdbh = new BaseDatosEngine();
                    usdbh = usdbh.open();
                    usdbh.EliminarRegistrosCampania();
                    while(jumpTime < totalProgressTime) {
                        try {
                            JSONObject obj = respJSONcuenta.getJSONObject(jumpTime);
                            String id = obj.getString("id");
                            String name = obj.getString("name");
                            String idAccount = obj.getString("idAccount");
                            String accountName = obj.getString("accountName");
                            usdbh = usdbh.open();
                            try {
                                ContentValues Objdatos = new ContentValues();
                                Objdatos.put("ID", jumpTime);
                                Objdatos.put(EstructuraBD.CabecerasCampanias.IdCampania, id);
                                Objdatos.put(EstructuraBD.CabecerasCampanias.idAccount, idAccount);
                                Objdatos.put(EstructuraBD.CabecerasCampanias.AccountNombre, accountName.toUpperCase());
                                Objdatos.put(EstructuraBD.CabecerasCampanias.CampaniaNombre, name.toUpperCase());
                                usdbh.insertardatosCampania(Objdatos);
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
                    progress.dismiss();
                }
            };
            t.start();
            try {
                t.join();
                CargarListacuenta();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    //clase cargar locales
    public   class CargarLocales extends AsyncTask<Void, Void, String> {
        private  ProgressDialog progressDialog;
        private  Context context;

        public CargarLocales(Context context) {
            this.context = context;
        }

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
                //Log.e(BonusPackHelper.LOG_TAG, "Error ", ex);
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
                String url1 = "http://geomardis6728.cloudapp.net/servicioDyvenpro/api/Task?idAccount="+Idaccount+"&Imeid="+obterImeid();
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

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {

                    return null;
                }
                jsonStr = buffer.toString();


                respJSONlocales = new JSONArray(jsonStr);
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
            progressDialog.dismiss();
            progress=new ProgressDialog(context);
            progress.setMessage("Descargando Locales....");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            // progress.setIndeterminate(true);
            progress.setCanceledOnTouchOutside(false);

            progress.setProgress(0);
            progress.setMax(respJSONlocales.length());
            progress.show();
            final int totalProgressTime = respJSONlocales.length();
            final Thread t = new Thread() {
                @Override
                public void run() {
                    int jumpTime = 0;
                    while(jumpTime < totalProgressTime) {
                        try {
                            JSONObject obj = respJSONlocales.getJSONObject(jumpTime);
                            String id = obj.getString("id");
                            String idAccount = obj.getString("idAccount");
                            String externalCode = obj.getString("externalCode");
                            String code = obj.getString("code");
                            String name = obj.getString("name");
                            String mainStreet = obj.getString("mainStreet");
                            String neighborhood = obj.getString("neighborhood");
                            String reference = obj.getString("reference");
                            String propietario = obj.getString("propietario");
                            String uriformulario = "";
                            String idProvince = obj.getString("idProvince");
                            String idDistrict = obj.getString("idDistrict");
                            String idParish = obj.getString("idParish");
                            String rutaaggregate = obj.getString("rutaaggregate");
                            String imeI_ID = obj.getString("imeI_ID");
                            String LatitudeBranch = obj.getString("latitudeBranch");
                            String LenghtBranch = obj.getString("lenghtBranch");
                            String celular = obj.getString("celular");
                            String TypeBusiness  = obj.getString("typeBusiness");
                            String cedula=obj.getString("cedula");
                            String actividad=obj.getString("actividad").replace("[","").replace("]","");
                            String Link=obj.getString("link");
                            String[] actividades = actividad.split(",");
                            String actividadesformularios="";

                            for (String a:actividades)
                            {

                                actividadesformularios = actividadesformularios + "X" + agregarCeros(a,4) + "-";

                            }

                            String comment=obj.getString("comment");
                            //storeaudit
                            String ESTADOAGGREGATE=obj.getString("estadoaggregate");
                            //censo
                            //String ESTADOAGGREGATE="S";
                            BaseDatosEngine usdbh = new BaseDatosEngine();

                            if(jumpTime==0){

                                if (radnombre.isChecked()){
                                    Estado="N";
                                }
                                else{
                                    Estado="C";

                                }
                                usdbh = usdbh.open();
                                usdbh.EliminarRegistrosConfiguracion();
                                ContentValues Objdatos = new ContentValues();
                                Objdatos.put(EstructuraBD.CabeceraConfiguracion.Id_cuenta, idAccount);
                                Objdatos.put(EstructuraBD.CabeceraConfiguracion.Id_campania, objcuentaSession.getCu_idcampania());
                                Objdatos.put(EstructuraBD.CabeceraConfiguracion.FormaBusqueda,Estado );
                                Objdatos.put(EstructuraBD.CabeceraConfiguracion.Estado, "A");
                                usdbh.insertardatosConfiguracion(Objdatos);
                                usdbh.close();
                            }
                            try {
                                usdbh = usdbh.open();
                                ContentValues Objdatos = new ContentValues();
                                Objdatos.put(EstructuraBD.CabecerasEngine.idbranch, id);
                                Objdatos.put(EstructuraBD.CabecerasEngine.idAccount, idAccount);
                                Objdatos.put(EstructuraBD.CabecerasEngine.externalCode, externalCode.toUpperCase());
                                Objdatos.put(EstructuraBD.CabecerasEngine.code, code.toUpperCase());
                                Objdatos.put(EstructuraBD.CabecerasEngine.name, name.toUpperCase());
                                Objdatos.put(EstructuraBD.CabecerasEngine.mainStreet, mainStreet);
                                Objdatos.put(EstructuraBD.CabecerasEngine.neighborhood, neighborhood);
                                Objdatos.put(EstructuraBD.CabecerasEngine.reference, reference);
                                Objdatos.put(EstructuraBD.CabecerasEngine.propietario, propietario);
                                Objdatos.put(EstructuraBD.CabecerasEngine.uriformulario, uriformulario);
                                Objdatos.put(EstructuraBD.CabecerasEngine.idprovince, idProvince);
                                Objdatos.put(EstructuraBD.CabecerasEngine.iddistrict, idDistrict);
                                Objdatos.put(EstructuraBD.CabecerasEngine.idParish, idParish);
                                Objdatos.put(EstructuraBD.CabecerasEngine.rutaaggregate, rutaaggregate=="null"?"0":rutaaggregate);
                                Objdatos.put(EstructuraBD.CabecerasEngine.imeI_ID, imeI_ID);
                                Objdatos.put(EstructuraBD.CabecerasEngine.LatitudeBranch, LatitudeBranch);
                                Objdatos.put(EstructuraBD.CabecerasEngine.LenghtBranch, LenghtBranch);
                                Objdatos.put(EstructuraBD.CabecerasEngine.Celular, celular);
                                Objdatos.put(EstructuraBD.CabecerasEngine.TypeBusiness, TypeBusiness);
                                Objdatos.put(EstructuraBD.CabecerasEngine.Cedula, cedula);
                                Objdatos.put(EstructuraBD.CabecerasEngine.ESTADOAGGREGATE,ESTADOAGGREGATE);
                                Objdatos.put(EstructuraBD.CabecerasEngine.comment,comment);
                                Objdatos.put(EstructuraBD.CabecerasEngine.formulariomedicion,"I");
                                Objdatos.put(EstructuraBD.CabecerasEngine.formulariopercha,"I");
                                Objdatos.put(EstructuraBD.CabecerasEngine.formulariopop,"I");
                                Objdatos.put(EstructuraBD.CabecerasEngine.formulariopromocion,"I");
                                Objdatos.put(EstructuraBD.CabecerasEngine.actividades,actividadesformularios);
                                Objdatos.put(EstructuraBD.CabecerasEngine.formularioactividades,"I");
                                Objdatos.put(EstructuraBD.CabecerasEngine.Foto_Exterior,Link);

                                Objdatos.put(EstructuraBD.CabecerasEngine.FVisibilidad,"I");
                                Objdatos.put(EstructuraBD.CabecerasEngine.FDisponibilidad,"I");
                                Objdatos.put(EstructuraBD.CabecerasEngine.FAccesibilidad,"ok");
                                if(comment.equals("ok"))
                                    Objdatos.put(EstructuraBD.CabecerasEngine.FExtra_visibilidad,"ok");
                                else
                                    Objdatos.put(EstructuraBD.CabecerasEngine.FExtra_visibilidad,"I");


                                Objdatos.put(EstructuraBD.CabecerasEngine.FInventarios,"ok");
                                Objdatos.put(EstructuraBD.CabecerasEngine.FPosicionamiento,"ok");
                                Objdatos.put(EstructuraBD.CabecerasEngine.Facciontipolocal,"");

                                usdbh.insertardatos(Objdatos);
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
                    objconfiguracionSession.setCnf_idAccount(objcuentaSession.getCu_idAccount());
                    objconfiguracionSession.setCnf_idcampania(objcuentaSession.getCu_idcampania());
                    objconfiguracionSession.setCnf_AccountNombre(objcuentaSession.getCu_AccountNombre());
                    objconfiguracionSession.setCnf_CampaniaNombre(objcuentaSession.getCu_CampaniaNombre());
                    objconfiguracionSession.setCnf_factorbusqueda(Estado);


                    progress.dismiss();
                    if(totalProgressTime>0){
                        startActivity(new Intent(context, principal.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                        finish();
                    }
                }
            };
            t.start();
        }
    }
    //carga historiales
    public   class CargarHistoriales extends AsyncTask<Void, Void, String> {
        private  ProgressDialog progressDialog;
        private  Context context;

        public CargarHistoriales(Context context) {
            this.context = context;
        }

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
                //Log.e(BonusPackHelper.LOG_TAG, "Error ", ex);
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
                String url1 = "http://geomardis6728.cloudapp.net/servicioDyvenpro/api/Task?idAccount="+Idaccount+"&Imeid="+obterImeid();
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

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {

                    return null;
                }
                jsonStr = buffer.toString();


                respJSONhistoriales = new JSONArray(jsonStr);
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
            progressDialog.dismiss();
            progress=new ProgressDialog(context);
            progress.setMessage("Descargando Locales....");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            // progress.setIndeterminate(true);
            progress.setCanceledOnTouchOutside(false);

            progress.setProgress(0);
            progress.setMax(respJSONhistoriales.length());
            progress.show();
            final int totalProgressTime = respJSONhistoriales.length();
            final Thread t = new Thread() {
                @Override
                public void run() {
                    int jumpTime = 0;
                    while(jumpTime < totalProgressTime) {
                        try {
                            JSONObject obj = respJSONhistoriales.getJSONObject(jumpTime);
                            String id = obj.getString("id");
                            String idAccount = obj.getString("idAccount");
                            String externalCode = obj.getString("externalCode");
                            String code = obj.getString("code");
                            String name = obj.getString("name");
                            String mainStreet = obj.getString("mainStreet");
                            String neighborhood = obj.getString("neighborhood");
                            String reference = obj.getString("reference");
                            String propietario = obj.getString("propietario");
                            String uriformulario = "";
                            String idProvince = obj.getString("idProvince");
                            String idDistrict = obj.getString("idDistrict");
                            String idParish = obj.getString("idParish");
                            String rutaaggregate = obj.getString("rutaaggregate");
                            String imeI_ID = obj.getString("imeI_ID");
                            String LatitudeBranch = obj.getString("latitudeBranch");
                            String LenghtBranch = obj.getString("lenghtBranch");
                            String celular = obj.getString("celular");
                            String TypeBusiness  = obj.getString("typeBusiness");
                            String cedula=obj.getString("cedula");
                            String actividad=obj.getString("actividad").replace("[","").replace("]","");
                            String[] actividades = actividad.split(",");
                            String actividadesformularios="";

                            for (String a:actividades)
                            {

                                actividadesformularios = actividadesformularios + "X" + agregarCeros(a,3) + "-";

                            }

                            String comment=obj.getString("comment");
                            //storeaudit
                            //String ESTADOAGGREGATE=obj.getString("estadoaggregate");
                            //censo
                            String ESTADOAGGREGATE="S";
                            BaseDatosEngine usdbh = new BaseDatosEngine();

                            if(jumpTime==0){

                                if (radnombre.isChecked()){
                                    Estado="N";
                                }
                                else{
                                    Estado="C";

                                }
                                usdbh = usdbh.open();
                                usdbh.EliminarRegistrosConfiguracion();
                                ContentValues Objdatos = new ContentValues();
                                Objdatos.put(EstructuraBD.CabeceraConfiguracion.Id_cuenta, idAccount);
                                Objdatos.put(EstructuraBD.CabeceraConfiguracion.Id_campania, objcuentaSession.getCu_idcampania());
                                Objdatos.put(EstructuraBD.CabeceraConfiguracion.FormaBusqueda,Estado );
                                Objdatos.put(EstructuraBD.CabeceraConfiguracion.Estado, "A");
                                usdbh.insertardatosConfiguracion(Objdatos);
                                usdbh.close();
                            }
                            try {
                                usdbh = usdbh.open();
                                ContentValues Objdatos = new ContentValues();
                                Objdatos.put(EstructuraBD.CabecerasEngine.idbranch, id);
                                Objdatos.put(EstructuraBD.CabecerasEngine.idAccount, idAccount);
                                Objdatos.put(EstructuraBD.CabecerasEngine.externalCode, externalCode.toUpperCase());
                                Objdatos.put(EstructuraBD.CabecerasEngine.code, code.toUpperCase());
                                Objdatos.put(EstructuraBD.CabecerasEngine.name, name.toUpperCase());
                                Objdatos.put(EstructuraBD.CabecerasEngine.mainStreet, mainStreet);
                                Objdatos.put(EstructuraBD.CabecerasEngine.neighborhood, neighborhood);
                                Objdatos.put(EstructuraBD.CabecerasEngine.reference, reference);
                                Objdatos.put(EstructuraBD.CabecerasEngine.propietario, propietario);
                                Objdatos.put(EstructuraBD.CabecerasEngine.uriformulario, uriformulario);
                                Objdatos.put(EstructuraBD.CabecerasEngine.idprovince, idProvince);
                                Objdatos.put(EstructuraBD.CabecerasEngine.iddistrict, idDistrict);
                                Objdatos.put(EstructuraBD.CabecerasEngine.idParish, idParish);
                                Objdatos.put(EstructuraBD.CabecerasEngine.rutaaggregate, rutaaggregate=="null"?"0":rutaaggregate);
                                Objdatos.put(EstructuraBD.CabecerasEngine.imeI_ID, imeI_ID);
                                Objdatos.put(EstructuraBD.CabecerasEngine.LatitudeBranch, LatitudeBranch);
                                Objdatos.put(EstructuraBD.CabecerasEngine.LenghtBranch, LenghtBranch);
                                Objdatos.put(EstructuraBD.CabecerasEngine.Celular, celular);
                                Objdatos.put(EstructuraBD.CabecerasEngine.TypeBusiness, TypeBusiness);
                                Objdatos.put(EstructuraBD.CabecerasEngine.Cedula, cedula);
                                Objdatos.put(EstructuraBD.CabecerasEngine.ESTADOAGGREGATE,ESTADOAGGREGATE);
                                Objdatos.put(EstructuraBD.CabecerasEngine.comment,comment);
                                Objdatos.put(EstructuraBD.CabecerasEngine.formulariomedicion,"I");
                                Objdatos.put(EstructuraBD.CabecerasEngine.formulariopercha,"I");
                                Objdatos.put(EstructuraBD.CabecerasEngine.formulariopop,"I");
                                Objdatos.put(EstructuraBD.CabecerasEngine.formulariopromocion,"I");
                                Objdatos.put(EstructuraBD.CabecerasEngine.actividades,actividadesformularios);
                                Objdatos.put(EstructuraBD.CabecerasEngine.formularioactividades,"I");

                                usdbh.insertardatos(Objdatos);
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
                    objconfiguracionSession.setCnf_idAccount(objcuentaSession.getCu_idAccount());
                    objconfiguracionSession.setCnf_idcampania(objcuentaSession.getCu_idcampania());
                    objconfiguracionSession.setCnf_AccountNombre(objcuentaSession.getCu_AccountNombre());
                    objconfiguracionSession.setCnf_CampaniaNombre(objcuentaSession.getCu_CampaniaNombre());
                    objconfiguracionSession.setCnf_factorbusqueda(Estado);


                    progress.dismiss();
                    if(totalProgressTime>0){
                        startActivity(new Intent(context, principal.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                        finish();
                    }
                }
            };
            t.start();
        }
    }
    @Override
    protected void onStart() {

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
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_cuenta:

                startActivity(new Intent(getBaseContext(), principal.class)
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
                String ADMIN_PREFERENCES = "admin_prefs";
                SharedPreferences.Editor editor = this.
                        getSharedPreferences(ADMIN_PREFERENCES, MODE_PRIVATE).edit();
                editor.putString(KEY_ADMIN_PW, Collect.getInstance().getString(R.string.pass_chariot));
                editor.apply();

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



