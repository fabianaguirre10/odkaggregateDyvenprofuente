package org.odk.collect.android.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.odk.collect.android.application.Collect;


import org.odk.collect.android.database.Entidades.Branch;
import org.odk.collect.android.storage.StoragePathProvider;
import org.odk.collect.android.storage.StorageSubdirectory;

import java.util.List;

import timber.log.Timber;

import static org.odk.collect.android.database.EstructuraBD.*;

/**
 * Created by Administrador1 on 7/2/2018.
 */

public class BaseDatosEngine {
    private static final String NOMBRE_BASE_DATOS = "EngineDatos.db";

    private static final int VERSION_ACTUAL = 43;
    private DatabaseHelperEngine dbHelper;
    private SQLiteDatabase db;
    interface Tablas {
        String LocalesEngine = "LocalesEngine";
        String CodigosNuevos = "CodigosNuevos";
        String CampaniaCuentas = "CampaniaCuentas";
        String Povince = "Province";
        String District = "District";
        String Parish = "Parish";
        String Service = "Service";
        String Configuracion = "Configuracion";
        String LoclaesHistorial = "LoclaesHistorial";
        String Productos = "Productos";
        String Operacion = "Operacion";

    }

    private static class DatabaseHelperEngine extends SQLiteOpenHelper {
        DatabaseHelperEngine() {
            super(new DatabaseContext(new StoragePathProvider().getDirPath(StorageSubdirectory.METADATA)), NOMBRE_BASE_DATOS, null, VERSION_ACTUAL);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            // create table to keep track of the itemsets
                String query = "create table LocalesEngine(" +
                        "_ID integer primary key," +
                        ColumnasEngine.idbranch + " text," +
                        ColumnasEngine.idAccount + " text," +
                        ColumnasEngine.externalCode + " text," +
                        ColumnasEngine.code + " text," +
                        ColumnasEngine.name + " text," +
                        ColumnasEngine.mainStreet + " text," +
                        ColumnasEngine.neighborhood + " text," +
                        ColumnasEngine.reference + " text," +
                        ColumnasEngine.propietario + " text," +
                        ColumnasEngine.uriformulario + " text," +
                        ColumnasEngine.idprovince + " text," +
                        ColumnasEngine.iddistrict + " text," +
                        ColumnasEngine.idParish + " text," +
                        ColumnasEngine.rutaaggregate + " text," +
                        ColumnasEngine.imeI_ID + " text," +
                        ColumnasEngine.LatitudeBranch + " text," +
                        ColumnasEngine.LenghtBranch + " text," +
                        ColumnasEngine.EstadoFormulario + " text," +
                        ColumnasEngine.Colabora + " text," +
                        ColumnasEngine.Celular + " text," +
                        ColumnasEngine.TypeBusiness + " text," +
                        ColumnasEngine.Cedula + " text," +
                        ColumnasEngine.ESTADOAGGREGATE + " text," +
                        ColumnasEngine.comment + " text," +
                        ColumnasEngine.formulariomedicion + " text," +
                        ColumnasEngine.formulariopercha + " text," +
                        ColumnasEngine.formulariopop + " text," +
                        ColumnasEngine.formulariopromocion + " text," +
                        ColumnasEngine.actividades + " text," +
                        ColumnasEngine.formularioactividades  + " text," +
                        ColumnasEngine.Foto_Exterior + " text," +

                        ColumnasEngine.FVisibilidad + " text," +
                        ColumnasEngine.FDisponibilidad + " text," +
                        ColumnasEngine.FAccesibilidad + " text," +
                        ColumnasEngine.FExtra_visibilidad + " text," +
                        ColumnasEngine.FInventarios + " text," +
                        ColumnasEngine.FPosicionamiento + " text," +
                        ColumnasEngine.Facciontipolocal  + " text" +



                        ")";

                db.execSQL(query);

                query = "create table CodigosNuevos(_ID integer primary key ," +
                        ColumnasCodigos.idAccount + " text," +
                        ColumnasCodigos.code + " int," +
                        ColumnasCodigos.estado + " text," +
                        ColumnasCodigos.uri + " text," +
                        ColumnasCodigos.imei_id + " text," +
                        ColumnasCodigos.codeunico + " text" +
                        ")";

                db.execSQL(query);
                query = "create table " + Tablas.CampaniaCuentas + "(_ID integer primary key," +
                        ColumnasCampanias.idAccount + " text," +
                        ColumnasCampanias.AccountNombre + " text," +
                        ColumnasCampanias.IdCampania + " text," +
                        ColumnasCampanias.CampaniaNombre + " text" +
                        ")";
                db.execSQL(query);
                query = "create table " + Tablas.Povince + "(_ID integer primary key," +
                        ColumnasProvince.Idprovince + " text," +
                        ColumnasProvince.IdCountry + " text," +
                        ColumnasProvince.Code + " text," +
                        ColumnasProvince.Name + " text" +
                        ")";
                db.execSQL(query);
                query = "create table " + Tablas.District + "(_ID integer primary key," +
                        ColumnasDistrict.IdDistrict + " text," +
                        ColumnasDistrict.IdProvince + " text," +
                        ColumnasDistrict.Code + " text," +
                        ColumnasDistrict.Name + " text," +
                        ColumnasDistrict.StatusRegister + " text," +
                        ColumnasDistrict.IdManagement + " text" +
                        ")";
                db.execSQL(query);
                query = "create table " + Tablas.Parish + "(_ID integer primary key," +
                        Columnasparish.IdParish + " text," +
                        Columnasparish.IdDistrict + " text," +
                        Columnasparish.Code + " text," +
                        Columnasparish.Name + " text," +
                        Columnasparish.StatusRegister + " text" +
                        ")";
                db.execSQL(query);
            query = "create table " + Tablas.Configuracion + "(_ID integer primary key," +
                    ColumnasConfiguracion.Id_cuenta + " text," +
                    ColumnasConfiguracion.Id_campania + " text," +
                    ColumnasConfiguracion.FormaBusqueda + " text," +
                    ColumnasConfiguracion.Estado + " text" +
                    ")";
            db.execSQL(query);
            query = "create table " + Tablas.LoclaesHistorial + "(_ID integer primary key," +
                    CabeceraLocalHistorico.idBranch + " text," +
                    CabeceraLocalHistorico.sku + " text," +
                    CabeceraLocalHistorico.code + " text," +
                    CabeceraLocalHistorico.pvp + " text," +
                    CabeceraLocalHistorico.pvr + " text," +
                    CabeceraLocalHistorico.presencia + " text" +
                    ")";
            db.execSQL(query);

            //crear tabla productos
            query = "create table " + Tablas.Productos + "(_ID integer primary key," +
                    ColumnasProductos.codproducto + " text," +
                    ColumnasProductos.descripcion + " text," +
                    ColumnasProductos.pve + " text," +
                    ColumnasProductos.pvp + " text," +
                    ColumnasProductos.codesecundario + " text," +
                    ColumnasProductos.estado + " text" +
                    ")";
            db.execSQL(query);

            //crear tabla operaciones
            query = "create table " + Tablas.Operacion + "(_ID integer primary key," +
                    ColumnasOperacion.codproducto + " text," +
                    ColumnasOperacion.tipoorden + " text," +
                    ColumnasOperacion.valor + " text," +
                    ColumnasOperacion.estado + " text" +
                    ")";
            db.execSQL(query);





        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Timber.w("Upgrading database from version %d to %d, which will destroy all old data", oldVersion, newVersion);
            // first drop all of our generated itemset tables
            db.execSQL("DROP TABLE IF EXISTS " + Tablas.LocalesEngine);
            db.execSQL("DROP TABLE IF EXISTS " + Tablas.CodigosNuevos);
            db.execSQL("DROP TABLE IF EXISTS " + Tablas.CampaniaCuentas);
            db.execSQL("DROP TABLE IF EXISTS " + Tablas.Povince);
            db.execSQL("DROP TABLE IF EXISTS " + Tablas.District);
            db.execSQL("DROP TABLE IF EXISTS " + Tablas.Parish);
            db.execSQL("DROP TABLE IF EXISTS " + Tablas.Service);
            db.execSQL("DROP TABLE IF EXISTS " + Tablas.Configuracion);
            db.execSQL("DROP TABLE IF EXISTS " + Tablas.LoclaesHistorial);
            db.execSQL("DROP TABLE IF EXISTS " + Tablas.Configuracion);
            db.execSQL("DROP TABLE IF EXISTS " + Tablas.LoclaesHistorial);
            db.execSQL("DROP TABLE IF EXISTS " + Tablas.Productos);
            db.execSQL("DROP TABLE IF EXISTS " + Tablas.Operacion);
            onCreate(db);
            if(newVersion==43){
                onCreate(db);
            }
        }
    }

        public BaseDatosEngine open() throws SQLException {
            dbHelper = new BaseDatosEngine.DatabaseHelperEngine();
            db = dbHelper.getWritableDatabase();
            //dbHelper.onUpgrade(db,1,2);
            return this;
        }
    public void beginTransaction() {
        db.execSQL("BEGIN");
    }

     public void commit() {
        db.execSQL("COMMIT");
    }
        public void close() {
            dbHelper.close();
        }

        //sdff
        //select * from mardiscore.branch  where idbrnahgfklsfksfkl kslsd lkgsd
    /* insert in to */
        public void CerrarBase(SQLiteDatabase db) {
            db.close();
        }

        //
        public Cursor ConfiguracionLista() {

            Cursor c = null;
            try {
                String query = "";
                query = "select * from " + Tablas.Configuracion;
                //db.execSQL(query);
                c = db.rawQuery(query, null);
                //Nos aseguramos de que existe al menos un registro
                List<Branch> resultado = null;

            } catch (Exception ex) {
                String a = ex.getMessage();
            }
            return c;
        }
    public Cursor ConfiguracionCanpania(String idaccount,String idcanpania) {

        Cursor c = null;
        try {
            String query = "";
            query = "select * from " + Tablas.CampaniaCuentas + " where idaccount='"+idaccount+"' and idcampania='"+idcanpania+"'";
            //db.execSQL(query);
            c = db.rawQuery(query, null);
            //Nos aseguramos de que existe al menos un registro
            List<Branch> resultado = null;

        } catch (Exception ex) {
            String a = ex.getMessage();
        }
        return c;
    }
        public boolean insertardatosConfiguracion(ContentValues values) {

            int idUsuario;
            try {
                String query = "insert into " + Tablas.Configuracion + " values (" +
                        values.getAsString("ID") +
                        ",'" + values.getAsString(ColumnasConfiguracion.Id_cuenta) + "',"
                        + "'" + values.getAsString(ColumnasConfiguracion.Id_campania) + "',"
                        + "'" + values.getAsString(ColumnasConfiguracion.FormaBusqueda) + "',"
                        + "'" + values.getAsString(ColumnasConfiguracion.Estado) + "')";
                db.execSQL(query);
                //idUsuario = (int) db.insert(Tablas.CampaniaCuentas, null, values);
                //CerrarBase(db);
                //listar();
                return true;
            } catch (Exception ex) {
                CerrarBase(db);
                return false;
            }
        }

    public boolean insertardatosLocalhistorico(ContentValues values) {

        int idUsuario;
        try {
            String query = "insert into " + Tablas.LoclaesHistorial + " values (" +
                    values.getAsString("ID") +
                    ",'" + values.getAsString(CabeceraLocalHistorico.idBranch) + "',"
                    + "'" + values.getAsString(CabeceraLocalHistorico.sku) + "',"
                    + "'" + values.getAsString(CabeceraLocalHistorico.code) + "',"
                    + "'" + values.getAsString(CabeceraLocalHistorico.pvp) + "',"
                    + "'" + values.getAsString(CabeceraLocalHistorico.pvr) + "',"
                    + "'" + values.getAsString(CabeceraLocalHistorico.presencia) + "')";
            db.execSQL(query);
            //idUsuario = (int) db.insert(Tablas.CampaniaCuentas, null, values);
            //CerrarBase(db);
            //listar();
            return true;
        } catch (Exception ex) {
            CerrarBase(db);
            return false;
        }
    }


    public boolean EliminarRegistrosConfiguracion() {

        int idUsuario;
        try {
            idUsuario = (int) db.delete(Tablas.Configuracion, null, null);
            //CerrarBase(db);
            //listar();
            return true;
        } catch (Exception ex) {
            CerrarBase(db);
            return false;
        }
    }

        //metodos para ingresar provincia , distrinct ,parish
        public boolean insertardatosProvincia(ContentValues values) {

            int idUsuario;
            try {
                String query = "insert into " + Tablas.Povince + " values (" +
                        values.getAsString("ID") +
                        ",'" + values.getAsString(ColumnasProvince.Idprovince) + "',"
                        + "'" + values.getAsString(ColumnasProvince.IdCountry) + "',"
                        + "'" + values.getAsString(ColumnasProvince.Code) + "',"
                        + "'" + values.getAsString(ColumnasProvince.Name) + "')";
                db.execSQL(query);
                //idUsuario = (int) db.insert(Tablas.CampaniaCuentas, null, values);
                //CerrarBase(db);
                //listar();
                return true;
            } catch (Exception ex) {
                CerrarBase(db);
                return false;
            }
        }

        public Cursor listarProvincias() {
            String[] campos = new String[]{"_ID", ColumnasProvince.Idprovince, ColumnasProvince.IdCountry, ColumnasProvince.Code, ColumnasProvince.Name};

            Cursor c = null;
            String[] args = new String[]{""};
            try {
                c = db.query(Tablas.Povince, campos, "", null, null, null, null);
                if (c.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya más registros
                    do {


                    } while (c.moveToNext());
                }
            } catch (Exception ex) {
                String a = ex.getMessage();
            }
            return c;
        }

        public Cursor listarCiudad(String[] args) {
            String[] campos = new String[]{"_ID", ColumnasDistrict.IdDistrict, ColumnasDistrict.IdProvince, ColumnasDistrict.Code, ColumnasDistrict.Name};

            Cursor c = null;

            try {
                c = db.query(Tablas.District, campos, "IdProvince=?", args, null, null, null);
                if (c.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya más registros
                    do {


                    } while (c.moveToNext());
                }
            } catch (Exception ex) {
                String a = ex.getMessage();
            }
            return c;
        }

        public Cursor listarParroquia(String[] args) {
            String[] campos = new String[]{"_ID", Columnasparish.IdParish, Columnasparish.IdDistrict, Columnasparish.Code, Columnasparish.Name};

            Cursor c = null;

            try {
                c = db.query(Tablas.Parish, campos, "IdDistrict=?", args, null, null, null);
                if (c.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya más registros
                    do {


                    } while (c.moveToNext());
                }
            } catch (Exception ex) {
                String a = ex.getMessage();
            }
            return c;
        }

        public boolean insertardatosDistrict(ContentValues values) {

            int idUsuario;
            try {
                String query = "insert into " + Tablas.District + " values (" +
                        values.getAsString("ID") +
                        ",'" + values.getAsString(ColumnasDistrict.IdDistrict) + "',"
                        + "'" + values.getAsString(ColumnasDistrict.IdProvince) + "',"
                        + "'" + values.getAsString(ColumnasDistrict.Code) + "',"
                        + "'" + values.getAsString(ColumnasDistrict.Name) + "',"
                        + "'" + values.getAsString(ColumnasDistrict.StatusRegister) + "',"
                        + "'" + values.getAsString(ColumnasDistrict.IdManagement) + "')";
                db.execSQL(query);
                //idUsuario = (int) db.insert(Tablas.CampaniaCuentas, null, values);
                //CerrarBase(db);
                //listar();
                return true;
            } catch (Exception ex) {
                CerrarBase(db);
                return false;
            }
        }

        public boolean insertardatosparish(ContentValues values) {

            int idUsuario;
            try {
                String query = "insert into " + Tablas.Parish + " values (" +
                        values.getAsString("ID") +
                        ",'" + values.getAsString(Columnasparish.IdParish) + "',"
                        + "'" + values.getAsString(Columnasparish.IdDistrict) + "',"
                        + "'" + values.getAsString(Columnasparish.Code) + "',"
                        + "'" + values.getAsString(Columnasparish.Name) + "',"
                        + "'" + values.getAsString(Columnasparish.StatusRegister) + "')";
                db.execSQL(query);
                //idUsuario = (int) db.insert(Tablas.CampaniaCuentas, null, values);
                //CerrarBase(db);
                //listar();
                return true;
            } catch (Exception ex) {
                CerrarBase(db);
                return false;
            }
        }


        //metodos para ingresar cuenta campañas
        public boolean insertardatosCampania(ContentValues values) {

            int idUsuario;
            try {
                String query = "insert into " + Tablas.CampaniaCuentas + " values (" +
                        values.getAsString("ID") +
                        ",'" + values.getAsString(ColumnasCampanias.idAccount) + "',"
                        + "'" + values.getAsString(ColumnasCampanias.AccountNombre) + "',"
                        + "'" + values.getAsString(ColumnasCampanias.IdCampania) + "',"
                        + "'" + values.getAsString(ColumnasCampanias.CampaniaNombre) + "')";
                db.execSQL(query);
                //idUsuario = (int) db.insert(Tablas.CampaniaCuentas, null, values);
                //CerrarBase(db);
                //listar();
                return true;
            } catch (Exception ex) {
                CerrarBase(db);
                return false;
            }
        }

        public Cursor listarCanpanias() {
            String[] campos = new String[]{"_ID", ColumnasCampanias.idAccount, ColumnasCampanias.AccountNombre, ColumnasCampanias.IdCampania, ColumnasCampanias.CampaniaNombre};

            Cursor c = null;
            String[] args = new String[]{""};
            try {
                c = db.query(Tablas.CampaniaCuentas, campos, "", null, null, null, null);
                if (c.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya más registros
                    do {


                    } while (c.moveToNext());
                }
            } catch (Exception ex) {
                String a = ex.getMessage();
            }
            return c;
        }

        public boolean EliminarRegistrosCampania() {

            int idUsuario;
            try {
                idUsuario = (int) db.delete(Tablas.CampaniaCuentas, null, null);
                //CerrarBase(db);
                //listar();
                return true;
            } catch (Exception ex) {
                CerrarBase(db);
                return false;
            }
        }

        public boolean EliminarRegistrosParish() {

            int idUsuario;
            try {
                idUsuario = (int) db.delete(Tablas.Parish, null, null);
                //CerrarBase(db);
                //listar();
                return true;
            } catch (Exception ex) {
                CerrarBase(db);
                return false;
            }
        }

        public boolean EliminarRegistrosProvincia() {

            int idUsuario;
            try {
                idUsuario = (int) db.delete(Tablas.Povince, null, null);
                //CerrarBase(db);
                //listar();
                return true;
            } catch (Exception ex) {
                CerrarBase(db);
                return false;
            }
        }

        public boolean EliminarRegistrosDistrict() {

            int idUsuario;
            try {
                idUsuario = (int) db.delete(Tablas.District, null, null);
                //CerrarBase(db);
                //listar();
                return true;
            } catch (Exception ex) {
                CerrarBase(db);
                return false;
            }
        }


        //metodos para ingresar codigos nuevos
        public boolean insertardatosCodigos(ContentValues values) {

            int idUsuario;
            try {
                String query = "insert into " + Tablas.CodigosNuevos + " values (" +
                        values.getAsString(ColumnasCodigos.ID) + ","
                        + "'" + values.getAsString(ColumnasCodigos.idAccount) + "',"
                        + "'" + values.getAsString(ColumnasCodigos.code) + "',"
                        + "'" + values.getAsString(ColumnasCodigos.estado) + "',"
                        + "'" + values.getAsString(ColumnasCodigos.uri) + "',"
                        + "'" + values.getAsString(ColumnasCodigos.imei_id) + "',"
                        + "'" + values.getAsString(ColumnasCodigos.codeunico) + "')";
                db.execSQL(query);
                //idUsuario = (int) db.insert(Tablas.CodigosNuevos, null, values);
                //CerrarBase(db);
                //listar();
                return true;
            } catch (Exception ex) {
                CerrarBase(db);
                return false;
            }
        }

        public boolean EliminarRegistrosCodigos() {

            int idUsuario;
            try {
                idUsuario = (int) db.delete(Tablas.CodigosNuevos, null, null);
                //CerrarBase(db);
                //listar();
                return true;
            } catch (Exception ex) {
                CerrarBase(db);
                return false;
            }
        }

        public Cursor ContarEstado(String where)
        {
            Cursor c = null;
            try {
                String query = "";
                query = "select count(_id) from " + Tablas.LocalesEngine + "  "+ where;
                c = db.rawQuery(query, null);
                List<Branch> resultado = null;
                if (c.moveToFirst()) {
                }
            } catch (Exception ex) {
                String a = ex.getMessage();
            }
            return c;
        }
        public Cursor listarCodigois() {
            String[] campos = new String[]{"_ID", ColumnasCodigos.idAccount, ColumnasCodigos.code, ColumnasCodigos.estado, ColumnasCodigos.uri, ColumnasCodigos.imei_id};

            Cursor c = null;
            String[] args = new String[]{""};
            try {
                String query = "";
                query = "select max(code) from " + Tablas.CodigosNuevos + " where uri=''  ";
                //db.execSQL(query);
                c = db.rawQuery(query, null);

                //Nos aseguramos de que existe al menos un registro
                List<Branch> resultado = null;
                if (c.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya más registros
                    do {


                    } while (c.moveToNext());
                }
            } catch (Exception ex) {
                String a = ex.getMessage();
            }
            return c;
        }

        //cargar rutas que estan en ese moento en el celular censo

        public Cursor listarRutasCodigos() {
            String[] campos = new String[]{"_ID", ColumnasCodigos.idAccount, ColumnasCodigos.code, ColumnasCodigos.estado, ColumnasCodigos.uri, ColumnasCodigos.imei_id};

            Cursor c = null;
            String[] args = new String[]{""};
            try {
                String query = "";
                query = "select DISTINCT rutaaggregate from " + Tablas.LocalesEngine;
                //db.execSQL(query);
                c = db.rawQuery(query, null);

                //Nos aseguramos de que existe al menos un registro
                List<Branch> resultado = null;
                if (c.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya más registros
                    do {


                    } while (c.moveToNext());
                }
            } catch (Exception ex) {
                String a = ex.getMessage();
            }
            return c;
        }

        public int CodigosActual(String tabla) {
            String[] campos = new String[]{"_ID", ColumnasCodigos.idAccount, ColumnasCodigos.code, ColumnasCodigos.estado, ColumnasCodigos.uri, ColumnasCodigos.imei_id};

            Cursor c = null;
            String[] args = new String[]{""};
            int codigo = 1;
            try {
                String query = "";
                query = "select count(_ID) from " + tabla;
                //db.execSQL(query);
                c = db.rawQuery(query, null);

                //Nos aseguramos de que existe al menos un registro
                List<Branch> resultado = null;
                if (c.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya más registros
                    do {
                        codigo = c.getInt(0) + 1;
                    } while (c.moveToNext());
                }
            } catch (Exception ex) {
                String a = ex.getMessage();
            }
            return codigo;
        }


        public Cursor CodigoNuevo(String code) {
            String[] campos = new String[]{"_ID", ColumnasCodigos.idAccount, ColumnasCodigos.code, ColumnasCodigos.estado, ColumnasCodigos.uri, ColumnasCodigos.imei_id};

            Cursor c = null;
            String[] args = new String[]{""};
            try {
                String query = "";
                query = "select * from " + Tablas.CodigosNuevos + " where code='" + code + "'";
                //db.execSQL(query);
                c = db.rawQuery(query, null);
                if (c.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya más registros
                    do {


                    } while (c.moveToNext());
                }
            } catch (Exception ex) {
                String a = ex.getMessage();
            }
            return c;
        }
//resumen datos
public Cursor ResumenDatos(String consulta) {

    Cursor c = null;
    String[] args = new String[]{""};
    try {
        String query = "";
        query = consulta;
        //db.execSQL(query);
        c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {


            } while (c.moveToNext());
        }
    } catch (Exception ex) {
        String a = ex.getMessage();
    }
    return c;
}


        public boolean ActualizarTablaCodigos(ContentValues values, String sentencia) {

            int idUsuario;
            try {
                idUsuario = (int) db.update(Tablas.CodigosNuevos, values, sentencia, null);
                //CerrarBase(db);
                //listar();
                return true;
            } catch (Exception ex) {
                CerrarBase(db);
                return false;
            }
        }


        //metodos para insertar modificar listar datos
        public boolean insertardatos(ContentValues values) {

            int idUsuario;
            try {
                //idUsuario = (int) db.insert(Tablas.LocalesEngine, null, values);

                String query = "insert into " + Tablas.LocalesEngine + " values (" +
                        values.getAsString("ID") +
                        ",'" + values.getAsString(ColumnasEngine.idbranch) + "',"
                        + "'" + values.getAsString(ColumnasEngine.idAccount) + "',"
                        + "'" + values.getAsString(ColumnasEngine.externalCode) + "',"
                        + "'" + values.getAsString(ColumnasEngine.code) + "',"
                        + "'" + values.getAsString(ColumnasEngine.name) + "',"
                        + "'" + values.getAsString(ColumnasEngine.mainStreet) + "',"
                        + "'" + values.getAsString(ColumnasEngine.neighborhood) + "',"
                        + "'" + values.getAsString(ColumnasEngine.reference) + "',"
                        + "'" + values.getAsString(ColumnasEngine.propietario) + "',"
                        + "'" + values.getAsString(ColumnasEngine.uriformulario) + "',"
                        + "'" + values.getAsString(ColumnasEngine.idprovince) + "',"
                        + "'" + values.getAsString(ColumnasEngine.iddistrict) + "',"
                        + "'" + values.getAsString(ColumnasEngine.idParish) + "',"
                        + "'" + values.getAsString(ColumnasEngine.rutaaggregate) + "',"
                        + "'" + values.getAsString(ColumnasEngine.imeI_ID) + "',"
                        + "'" + values.getAsString(ColumnasEngine.LatitudeBranch) + "',"
                        + "'" + values.getAsString(ColumnasEngine.LenghtBranch) + "',"
                        + "'" + values.getAsString(ColumnasEngine.EstadoFormulario) + "',"
                        + "'" + values.getAsString(ColumnasEngine.Colabora) + "',"
                        + "'" + values.getAsString(ColumnasEngine.Celular) + "',"
                        + "'" + values.getAsString(ColumnasEngine.TypeBusiness) + "',"
                        + "'" + values.getAsString(ColumnasEngine.Cedula) + "',"
                        + "'" + values.getAsString(ColumnasEngine.ESTADOAGGREGATE) + "',"
                        + "'" + values.getAsString(ColumnasEngine.comment) + "',"
                        + "'" + values.getAsString(ColumnasEngine.formulariomedicion) + "',"
                        + "'" + values.getAsString(ColumnasEngine.formulariopercha) + "',"
                        + "'" + values.getAsString(ColumnasEngine.formulariopop) + "',"
                        + "'" + values.getAsString(ColumnasEngine.formulariopromocion) + "',"
                        + "'" + values.getAsString(ColumnasEngine.actividades) + "',"
                        + "'" + values.getAsString(ColumnasEngine.formularioactividades) + "',"
                        + "'" + values.getAsString(ColumnasEngine.Foto_Exterior) + "',"

                        + "'" + values.getAsString(ColumnasEngine.FVisibilidad) + "',"
                        + "'" + values.getAsString(ColumnasEngine.FDisponibilidad) + "',"
                        + "'" + values.getAsString(ColumnasEngine.FAccesibilidad) + "',"
                        + "'" + values.getAsString(ColumnasEngine.FExtra_visibilidad) + "',"
                        + "'" + values.getAsString(ColumnasEngine.FInventarios) + "',"
                        + "'" + values.getAsString(ColumnasEngine.FPosicionamiento) + "',"
                        + "'" + values.getAsString(ColumnasEngine.Facciontipolocal) + "')";
                db.execSQL(query);

                //CerrarBase(db);
                //listar();
                return true;
            } catch (Exception ex) {
                CerrarBase(db);
                return false;
            }


        }

        public boolean ActualizarTabla(ContentValues values, String sentencia) {

            int idUsuario;
            try {
                idUsuario = (int) db.update(Tablas.LocalesEngine, values, sentencia, null);
                //CerrarBase(db);
                //listar();
                return true;
            } catch (Exception ex) {
                CerrarBase(db);
                return false;
            }
        }
    public boolean ActualizarTablaScrip(ContentValues values, String sentencia) {

        int idUsuario;
        try {
            //idUsuario = (int) db.update(Tablas.LocalesEngine, values, sentencia, null);
            //CerrarBase(db);
            //listar();
            String query = "";
            query = "update "+ Tablas.LocalesEngine +" set actividades='"+values.getAsString("actividades")+"' "+ sentencia;
            //db.execSQL(query);
             db.rawQuery(query, null);
            return true;
        } catch (Exception ex) {
            CerrarBase(db);
            return false;
        }
    }
        public boolean EliminarRegistros() {

            int idUsuario;
            try {
                idUsuario = (int) db.delete(Tablas.LocalesEngine, null, null);
                //CerrarBase(db);
                //listar();
                return true;
            } catch (Exception ex) {
                CerrarBase(db);
                return false;
            }
        }

        public Cursor listar(String[] argumentos, String op, String where) {
            String[] campos = new String[]{"_ID", "idbranch", "idAccount", "externalCode", "code", "name", "mainStreet", "neighborhood", "reference", "propietario", "uriformulario"};

            Cursor c = null;
            try {
                String query = "";
                query = "select * from " + Tablas.LocalesEngine + "  " + where;
                //db.execSQL(query);
                c = db.rawQuery(query, null);


                //Nos aseguramos de que existe al menos un registro
                List<Branch> resultado = null;
                if (c.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya más registros
                    do {
                        Branch obj = new Branch();
                        obj.setID(c.getString(0));
                        obj.setIdbranch(c.getString(1));
                        obj.setIdAccount(c.getString(2));
                        obj.setExternalCode(c.getString(3));
                        obj.setCode(c.getString(4));
                        obj.setName(c.getString(5));
                        obj.setMainStreet(c.getString(6));
                        obj.setNeighborhood(c.getString(7));
                        obj.setReference(c.getString(8));
                        obj.setPropietario(c.getString(9));
                        obj.setUriformulario(c.getColumnName(10));
                        //resultado.add(obj);

                    } while (c.moveToNext());
                }
            } catch (Exception ex) {
                String a = ex.getMessage();
            }
            return c;
        }

        public Cursor RutaLista() {

            Cursor c = null;
            try {
                String query = "";
                query = "select rutaaggregate from " + Tablas.LocalesEngine;
                //db.execSQL(query);
                c = db.rawQuery(query, null);
                //Nos aseguramos de que existe al menos un registro
                List<Branch> resultado = null;

            } catch (Exception ex) {
                String a = ex.getMessage();
            }
            return c;
        }


    }

