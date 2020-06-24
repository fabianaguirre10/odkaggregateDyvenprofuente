package org.odk.collect.android.database;

/**
 * Created by Mardis on 14/07/2017.
 */
import java.util.UUID;

public class EstructuraBD {
    public interface ColumnasEngine {
        String ID = "id";
        String idbranch = "idbranch";
        String idAccount = "idAccount";
        String externalCode = "externalCode";
        String code = "code";
        String name = "name";
        String mainStreet = "mainStreet";
        String neighborhood = "neighborhood";
        String reference = "reference";
        String propietario = "propietario";
        String uriformulario="uriformulario";
        String idprovince="idprovince";
        String iddistrict="iddistrict";
        String idParish="idParish";
        String rutaaggregate="rutaaggregate";
        String imeI_ID="imeI_ID";
        String LatitudeBranch="LatitudeBranch";
        String LenghtBranch="LenghtBranch";
        String EstadoFormulario="EstadoFormulario";
        String AbiertoLocal="AbiertoLocal";
        String Colabora="Colabora";
        String Celular="Celular";
        String TypeBusiness="TypeBusiness";
        String Cedula="Cedula";
        String ESTADOAGGREGATE="ESTADOAGGREGATE";
        String comment="comment";
        String formulariomedicion="FMEDICION";
        String formulariopercha="FPERCHA";
        String formulariopop ="FPOP";
        String formulariopromocion ="FPROMOCION";
        String formularioactividades ="FACTIVIDADES";

        /*comentar cambio app dyvenpro*/
      String FVisibilidad="FVisibilidad";
         String FDisponibilidad="FDisponibilidad";
         String FAccesibilidad="FAccesibilidad";
         String FExtra_visibilidad="FExtravisibilidad";
          String FInventarios="FInventarios";
         String FPosicionamiento="FPosicionamiento";

         /*estado accion*/
         String Facciontipolocal="Facciontipolocal";

        String actividades  ="actividades";
        String Foto_Exterior="Foto_Exterior";

    }
    public static class CabecerasEngine implements ColumnasEngine {
        public static String generarIdCabeceraPedido() {
            return "CP-" + UUID.randomUUID().toString();
        }
    }

    interface ColumnasCodigos{
        String ID = "id";
        String idAccount = "idAccount";
        String code = "code";
        String estado = "estado";
        String uri = "uri";
        String imei_id = "imei_id";
        String codeunico="codeunico";

    }
    public static class CabecerasCodigos implements ColumnasCodigos {
            public static String generarIdCabeceraPedido() {
                return "CP-" + UUID.randomUUID().toString();
            }
    }
    interface ColumnasCampanias {
        String ID = "id";
        String idAccount = "idAccount";
        String AccountNombre = "AccountNombre";
        String IdCampania = "IdCampania";
        String CampaniaNombre = "CampaniaNombre";
    }
    public static class CabecerasCampanias implements ColumnasCampanias {
        public static String generarIdCabeceraPedido() {
            return "CP-" + UUID.randomUUID().toString();
        }
    }
    interface ColumnasProvince {
        String ID="ID";
        String Idprovince="Idprovince";
        String IdCountry="IdCountry";
        String Code="Code";
        String Name="Name";
    }
    public static class CabecerasProvince implements ColumnasProvince {
        public static String generarIdCabeceraPedido() {
            return "CP-" + UUID.randomUUID().toString();
        }
    }
    interface ColumnasDistrict{
        String ID="ID";
        String IdDistrict="IdDistrict";
        String IdProvince="IdProvince";
        String Code="Code";
        String Name="Name";
        String StatusRegister="StatusRegister";
        String IdManagement="IdManagement";
    }
    public static class CabecerasDistrict implements ColumnasDistrict {
        public static String generarIdCabeceraPedido() {
            return "CP-" + UUID.randomUUID().toString();
        }
    }
    interface Columnasparish{
        String ID="ID";
        String IdParish="IdParish";
        String IdDistrict="IdDistrict";
        String Code="Code";
        String Name="Name";
        String StatusRegister="StatusRegister";
    }
    public static class Cabecerasparish implements Columnasparish {
        public static String generarIdCabeceraPedido() {
            return "CP-" + UUID.randomUUID().toString();
        }
    }

    interface ColumnasService{
        String ID="ID";
        String Idservice="Idservice";
        String Code="Code";
        String Name="Name";
        String IdTypeService="IdTypeService";
        String PollTitle="PollTitle";
        String IdAccount="IdAccount";
        String IdCustomer="IdCustomer";
        String CreationDate="CreationDate";
        String StatusRegister="StatusRegister";
        String IdChannel="IdChannel";
        String Icon="Icon";
        String IconColor="IconColor";
        String Template="Template";
    }
    public static class CabeceraService implements ColumnasService {
        public static String generarIdCabeceraPedido() {
            return "CP-" + UUID.randomUUID().toString();
        }
    }
    interface ColumnasServiceDetail{
         String Id="Id";
         String IdServiceDetail="IdServiceDetail";
         String IdService="IdService";
         String Order="Order";
         String StatusRegister="StatusRegister";
         String SectionTitle="SectionTitle";
         String Weight="Weight";
         String HasPhoto="HasPhoto";
         String IsDynamic="IsDynamic";
         String GroupName="GroupName";
         String IdSection="IdSection";
         String NumberOfCopies="NumberOfCopies";
    }
    public static class CabeceraServiceDetail implements ColumnasServiceDetail {
        public static String generarIdCabeceraPedido() {
            return "CP-" + UUID.randomUUID().toString();
        }
    }
    interface ColumnasQuestion{
         String Id="Id";
         String IdQuestion="IdQuestion";
         String IdServiceDetail="IdServiceDetail";
         String Title="Title";
         String StatusRegister="StatusRegister";
         String Order="Order";
         String Weight="Weight";
         String IdTypePoll="IdTypePoll";
         String HasPhoto="HasPhoto";
         String CountPhoto="CountPhoto";
         String IdProductCategory="IdProductCategory";
         String IdProduct="IdProduct";
         String AnswerRequired="AnswerRequired";
    }
    public static class CabeceraQuestion implements ColumnasQuestion {
        public static String generarIdCabeceraPedido() {
            return "CP-" + UUID.randomUUID().toString();
        }
    }
    interface ColumnasQuestionDetail{
        String Id="Id";
        String IdQuestionDetail="IdQuestionDetail";
        String IdQuestion="IdQuestion";
        String Order="Order";
        String Weight="Weight";
        String Answer="Answer";
        String StatusRegister="StatusRegister";
        String IsNext="IsNext";
        String IdQuestionLink="IdQuestionLink";
    }
    public static class CabeceraQuestionDetail implements ColumnasQuestionDetail {
        public static String generarIdCabeceraPedido() {
            return "CP-" + UUID.randomUUID().toString();
        }

    }
    interface ColumnasConfiguracion{
        String Id_cuenta="Id_cuenta";
        String Id_campania="Id_campania";
        String FormaBusqueda="FormaBusqueda";
        String Estado="Estado";
    }
    public static class CabeceraConfiguracion implements ColumnasConfiguracion {
        public static String generarIdCabeceraPedido() {
            return "CP-" + UUID.randomUUID().toString();
        }

    }
    interface ColumnasLocalHistorico{
        String idBranch="idBranch";
        String sku="sku";
        String code="code";
        String pvp="pvp";
        String pvr="pvr";
        String presencia="presencia";
    }
    public static class CabeceraLocalHistorico implements ColumnasLocalHistorico {
        public static String generarIdCabeceraPedido() {
            return "CP-" + UUID.randomUUID().toString();
        }

    }
    interface ColumnasProductos{
        String codproducto="codproducto";
        String descripcion="descripcion";
        String codesecundario="codesecundario";
        String pve="pve";
        String pvp="pvp";
        String estado="estado";
    }
    public static class CabeceraLocalProductos implements ColumnasProductos {
        public static String generarIdCabeceraPedido() {
            return "CP-" + UUID.randomUUID().toString();
        }

    }
    interface ColumnasOperacion{
        String codproducto="codproducto";
        String tipoorden="tipoorden";
        String valor="valor";
        String estado="estado";
    }
    public static class CabeceraLocalOperacion implements ColumnasOperacion {
        public static String generarIdCabeceraPedido() {
            return "CP-" + UUID.randomUUID().toString();
        }

    }

}
