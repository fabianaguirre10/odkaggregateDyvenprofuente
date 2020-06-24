package org.odk.collect.android.database.Entidades;

/**
 * Created by Mardis on 18/07/2017.
 */

public class  BranchSession  {
    public static String e_ID = "id";
    public static String e_idbranch = "idbranch";
    public static String e_idAccount = "idAccount";
    public static String e_externalCode = "externalCode";
    public static String e_code = "code";
    public static String e_name = "name";
    public static String e_mainStreet = "mainStreet";
    public static String e_neighborhood = "neighborhood";
    public static String e_reference = "reference";
    public static String e_propietario = "propietario";
    public static String e_uriformulario="uriformulario";
    public  static String e_idprovince="e_idprovince";
    public static String e_idParish="e_idParish";
    public  static String e_rutaaggregate="0";
    public  static String e_imeI_ID="imeI_ID";
    public static String e_nuevo="e_nuevo";
    public  static String e_LatitudeBranch="0";
    public static String e_LenghtBranch="0";
    public  static  String e_EstadoFormulario="e_EstadoFormulario";
    public  static  String e_AbiertoLocal="e_AbiertoLocal";
    public  static  String e_Colabora="e_Colabora";
    public  static  String e_TypeBusiness="e_TypeBusiness";
    public static  String e_Phone="Phone";
    public  static  String e_Cedula="e_Cedula";
    public static  String e_ESTADOAGGREGATE="e_ESTADOAGGREGATE";
    public static  String e_comment="e_comment";
    public  static  String e_festadoactividades="I";
    public  static  String e_festadomedicion="I";
    public static  String e_festadopercha="I";
    public static  String e_festadopop="I";
    public  static  String e_festadopromocion="I";
    public static  String e_fmedicion="1. Medición";
    public static  String e_factividades="1. Actividades";
    public static  String e_fpercha="2. Exhibición Esp. - Convenio";
    public static  String e_fpop="3. POP - Promoción";
    public static  String e_fpromocion="4. Percha";
    public static  String e_actividades="";
    public static  String e_fotoexterior="e_fotoexterior";



    /*comentar cambio app dyvenpro*/
    public  static  String e_fvisibilidad="1. Visibilidad";
    public  static  String e_fdisponibilidad="2. Disponibilidad";
    public  static  String e_faccesibilidad="3. Percha";
    public  static  String e_fextra_visibilidad="4. Extra-visibilidad";
    public  static  String e_finventarios="5. Inventarios";
    public  static  String e_fposicionamiento="6. Posicionamiento";

    public  static  String FVisibilidadestado;
    public  static String FDisponibilidadestado;
    public  static String FAccesibilidadestado;
    public  static  String FExtra_visibilidadestado;
    public  static String FInventariosestado;
    public  static String FPosicionamientoestado;

    /*campo para guardar opcion de Seleccione la acción según corresponda */

    public static  String Facciontipolocal="";

    public static String getFacciontipolocal() {
        return Facciontipolocal;
    }

    public static void setFacciontipolocal(String facciontipolocal) {
        Facciontipolocal = facciontipolocal;
    }

    public static String getFVisibilidadestado() {
        return FVisibilidadestado;
    }

    public static void setFVisibilidadestado(String FVisibilidadestado) {
        BranchSession.FVisibilidadestado = FVisibilidadestado;
    }

    public static String getFDisponibilidadestado() {
        return FDisponibilidadestado;
    }

    public static void setFDisponibilidadestado(String FDisponibilidadestado) {
        BranchSession.FDisponibilidadestado = FDisponibilidadestado;
    }

    public static String getFAccesibilidadestado() {
        return FAccesibilidadestado;
    }

    public static void setFAccesibilidadestado(String FAccesibilidadestado) {
        BranchSession.FAccesibilidadestado = FAccesibilidadestado;
    }

    public static String getFExtra_visibilidadestado() {
        return FExtra_visibilidadestado;
    }

    public static void setFExtra_visibilidadestado(String FExtra_visibilidadestado) {
        BranchSession.FExtra_visibilidadestado = FExtra_visibilidadestado;
    }

    public static String getFInventariosestado() {
        return FInventariosestado;
    }

    public static void setFInventariosestado(String FInventariosestado) {
        BranchSession.FInventariosestado = FInventariosestado;
    }

    public static String getFPosicionamientoestado() {
        return FPosicionamientoestado;
    }

    public static void setFPosicionamientoestado(String FPosicionamientoestado) {
        BranchSession.FPosicionamientoestado = FPosicionamientoestado;
    }

    public static String getE_fvisibilidad() {
        return e_fvisibilidad;
    }

    public static void setE_fvisibilidad(String e_fvisibilidad) {
        BranchSession.e_fvisibilidad = e_fvisibilidad;
    }

    public static String getE_fdisponibilidad() {
        return e_fdisponibilidad;
    }

    public static void setE_fdisponibilidad(String e_fdisponibilidad) {
        BranchSession.e_fdisponibilidad = e_fdisponibilidad;
    }

    public static String getE_faccesibilidad() {
        return e_faccesibilidad;
    }

    public static void setE_faccesibilidad(String e_faccesibilidad) {
        BranchSession.e_faccesibilidad = e_faccesibilidad;
    }

    public static String getE_fextra_visibilidad() {
        return e_fextra_visibilidad;
    }

    public static void setE_fextra_visibilidad(String e_fextra_visibilidad) {
        BranchSession.e_fextra_visibilidad = e_fextra_visibilidad;
    }

    public static String getE_finventarios() {
        return e_finventarios;
    }

    public static void setE_finventarios(String e_finventarios) {
        BranchSession.e_finventarios = e_finventarios;
    }

    public static String getE_fposicionamiento() {
        return e_fposicionamiento;
    }

    public static void setE_fposicionamiento(String e_fposicionamiento) {
        BranchSession.e_fposicionamiento = e_fposicionamiento;
    }

    public static String getE_fotoexterior() {
        return e_fotoexterior;
    }

    public static void setE_fotoexterior(String e_fotoexterior) {
        BranchSession.e_fotoexterior = e_fotoexterior;
    }

    public static String getE_festadoactividades() {
        return e_festadoactividades;
    }

    public static void setE_festadoactividades(String e_festadoactividades) {
        BranchSession.e_festadoactividades = e_festadoactividades;
    }

    public static String getE_factividades() {
        return e_factividades;
    }

    public static void setE_factividades(String e_factividades) {
        BranchSession.e_factividades = e_factividades;
    }

    public static String getE_actividades() {
        return e_actividades;
    }

    public static void setE_actividades(String e_actividades) {
        BranchSession.e_actividades = e_actividades;
    }


    public static String getE_fmedicion() {
        return e_fmedicion;
    }

    public static void setE_fmedicion(String e_fmedicion) {
        BranchSession.e_fmedicion = e_fmedicion;
    }

    public static String getE_fpercha() {
        return e_fpercha;
    }

    public static void setE_fpercha(String e_fpercha) {
        BranchSession.e_fpercha = e_fpercha;
    }

    public static String getE_fpop() {
        return e_fpop;
    }

    public static void setE_fpop(String e_fpop) {
        BranchSession.e_fpop = e_fpop;
    }

    public static String getE_fpromocion() {
        return e_fpromocion;
    }

    public static void setE_fpromocion(String e_fpromocion) {
        BranchSession.e_fpromocion = e_fpromocion;
    }

    public static String getE_festadomedicion() {
        return e_festadomedicion;
    }

    public static void setE_festadomedicion(String e_festadomedicion) {
        BranchSession.e_festadomedicion = e_festadomedicion;
    }

    public static String getE_festadopercha() {
        return e_festadopercha;
    }

    public static void setE_festadopercha(String e_festadopercha) {
        BranchSession.e_festadopercha = e_festadopercha;
    }

    public static String getE_festadopop() {
        return e_festadopop;
    }

    public static void setE_festadopop(String e_festadopop) {
        BranchSession.e_festadopop = e_festadopop;
    }

    public static String getE_festadopromocion() {
        return e_festadopromocion;
    }

    public static void setE_festadopromocion(String e_festadopromocion) {
        BranchSession.e_festadopromocion = e_festadopromocion;
    }

    public static String getE_comment() {
        return e_comment;
    }

    public static void setE_comment(String e_comment) {
        BranchSession.e_comment = e_comment;
    }

    public static String getE_ESTADOAGGREGATE() {
        return e_ESTADOAGGREGATE;
    }

    public static void setE_ESTADOAGGREGATE(String e_ESTADOAGGREGATE) {
        BranchSession.e_ESTADOAGGREGATE = e_ESTADOAGGREGATE;
    }



    public static String getE_Cedula() {
        return e_Cedula;
    }

    public static void setE_Cedula(String e_Cedula) {
        BranchSession.e_Cedula = e_Cedula;
    }

    public static String getE_TypeBusiness() {
        return e_TypeBusiness;
    }

    public static void setE_TypeBusiness(String e_TypeBusiness) {
        BranchSession.e_TypeBusiness = e_TypeBusiness;
    }

    public static String getE_Phone() {
        return e_Phone;
    }

    public static void setE_Phone(String e_Phone) {
        BranchSession.e_Phone = e_Phone;
    }

    public static String getE_Colabora() {
        return e_Colabora;
    }

    public static void setE_Colabora(String e_Colabora) {
        BranchSession.e_Colabora = e_Colabora;
    }

    public static String getE_EstadoFormulario() {
        return e_EstadoFormulario;
    }

    public static void setE_EstadoFormulario(String e_EstadoFormulario) {
        BranchSession.e_EstadoFormulario = e_EstadoFormulario;
    }

    public static String getE_AbiertoLocal() {
        return e_AbiertoLocal;
    }

    public static void setE_AbiertoLocal(String e_AbiertoLocal) {
        BranchSession.e_AbiertoLocal = e_AbiertoLocal;
    }

    public static String getE_nuevo() {
        return e_nuevo;
    }

    public static void setE_nuevo(String e_nuevo) {
        BranchSession.e_nuevo = e_nuevo;
    }

    public static String getE_rutaaggregate() {
        return e_rutaaggregate;
    }

    public static void setE_rutaaggregate(String e_rutaaggregate) {
        BranchSession.e_rutaaggregate = e_rutaaggregate;
    }

    public static String getE_imeI_ID() {
        return e_imeI_ID;
    }

    public static void setE_imeI_ID(String e_imeI_ID) {
        BranchSession.e_imeI_ID = e_imeI_ID;
    }

    public static String getE_idParish() {
        return e_idParish;
    }

    public static void setE_idParish(String e_idParish) {
        BranchSession.e_idParish = e_idParish;
    }

    public static String getE_idprovince() {
        return e_idprovince;
    }

    public static void setE_idprovince(String e_idprovince) {
        BranchSession.e_idprovince = e_idprovince;
    }

    public static String getE_iddistrict() {
        return e_iddistrict;
    }

    public static void setE_iddistrict(String e_iddistrict) {
        BranchSession.e_iddistrict = e_iddistrict;
    }

    public  static String e_iddistrict="e_iddistrict";

    public BranchSession() {
    }

    public static String getE_ID() {
        return e_ID;
    }

    public static void setE_ID(String e_ID) {
        BranchSession.e_ID = e_ID;
    }

    public static String getE_idbranch() {
        return e_idbranch;
    }

    public static void setE_idbranch(String e_idbranch) {
        BranchSession.e_idbranch = e_idbranch;
    }

    public static String getE_idAccount() {
        return e_idAccount;
    }

    public static void setE_idAccount(String e_idAccount) {
        BranchSession.e_idAccount = e_idAccount;
    }

    public static String getE_externalCode() {
        return e_externalCode;
    }

    public static void setE_externalCode(String e_externalCode) {
        BranchSession.e_externalCode = e_externalCode;
    }

    public static String getE_code() {
        return e_code;
    }

    public static void setE_code(String e_code) {
        BranchSession.e_code = e_code;
    }

    public static String getE_name() {
        return e_name;
    }

    public static void setE_name(String e_name) {
        BranchSession.e_name = e_name;
    }

    public static String getE_mainStreet() {
        return e_mainStreet;
    }

    public static void setE_mainStreet(String e_mainStreet) {
        BranchSession.e_mainStreet = e_mainStreet;
    }

    public static String getE_neighborhood() {
        return e_neighborhood;
    }

    public static void setE_neighborhood(String e_neighborhood) {
        BranchSession.e_neighborhood = e_neighborhood;
    }

    public static String getE_reference() {
        return e_reference;
    }

    public static void setE_reference(String e_reference) {
        BranchSession.e_reference = e_reference;
    }

    public static String getE_propietario() {
        return e_propietario;
    }

    public static void setE_propietario(String e_propietario) {
        BranchSession.e_propietario = e_propietario;
    }

    public static String getE_uriformulario() {
        return e_uriformulario;
    }

    public static void setE_uriformulario(String e_uriformulario) {
        BranchSession.e_uriformulario = e_uriformulario;
    }

    public static String getE_LatitudeBranch() {
        return e_LatitudeBranch;
    }

    public static void setE_LatitudeBranch(String e_LatitudeBranch) {
        BranchSession.e_LatitudeBranch = e_LatitudeBranch;
    }

    public static String getE_LenghtBranch() {
        return e_LenghtBranch;
    }

    public static void setE_LenghtBranch(String e_LenghtBranch) {
        BranchSession.e_LenghtBranch = e_LenghtBranch;
    }
}
