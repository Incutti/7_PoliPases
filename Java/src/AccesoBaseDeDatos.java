
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AccesoBaseDeDatos {

    private Connection conexion;
    private String nombreBaseDeDatos;
    private List<String> nombreTabla;

    public AccesoBaseDeDatos(String nombreBaseDeDatos, List<String> nombreTabla) {
        this.nombreBaseDeDatos = nombreBaseDeDatos;
        this.nombreTabla = nombreTabla;
    }

    public void conectar(String user, String password) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/"+nombreBaseDeDatos;
        try {
            conexion = DriverManager.getConnection(url, user, password);
            if (conexion != null) {
                System.out.println("Se ha conectado exitósamente a la base de datos");
            } else {
                System.out.println("No se ha podido conectar a la base de datos");
            }
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
    }

    public HashMap<String, Object> almacenarDatos(Object... datos) {
        HashMap<String, Object> datosNuevos = new HashMap<>();
        for (int i = 0; i < datos.length; i = i + 2) {
            datosNuevos.put((String) datos[i], datos[i + 1]);
        }
        return datosNuevos;
    }

    public ArrayList<String> obtenerColumnasDeUnaTabla(String nombreTabla) {
        String consulta = "SHOW COLUMNS FROM " + nombreTabla;
        ArrayList<String> nombreCampos = new ArrayList<>();
        try {
            ResultSet data;
            PreparedStatement sentenciaSQL = conexion.prepareStatement(consulta);
            data = sentenciaSQL.executeQuery(consulta);
            while (data.next() == true) {
                nombreCampos.add(data.getString("Field"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return nombreCampos;
    }

    public int obtenerId(String nombreTabla, String atributo, Object valor) {
        int id = 0;
        ResultSet data ;
        String atributoPK= obtenerColumnasDeUnaTabla(nombreTabla).get(0);
        String consulta = "SELECT "+ atributoPK+ " FROM " + nombreTabla + " where " + atributo + " = " + "\"" + valor + "\"";
        try {
            PreparedStatement sentenciaSQL = conexion.prepareStatement(consulta);
            data = sentenciaSQL.executeQuery(consulta);
            while (data.next() == true) {
                id=data.getInt(atributoPK);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return id;
    }

    public ArrayList<String> obtenerSelectConMasDeUnValor(String nombreTabla, String nombreCampo,String columnaTabla,Object condicion){
        ResultSet data;
        ArrayList<String> valorCampo=new ArrayList<>();
        String consulta= "Select "+ nombreCampo+ " from " + nombreTabla+" where "+columnaTabla+"="+"\""+condicion+"\""+";";
        System.out.println(consulta);
        try {
            PreparedStatement sentenciaSQL = conexion.prepareStatement(consulta);
            data = sentenciaSQL.executeQuery(consulta);
            while (data.next() == true) {
                valorCampo.add(data.getString(nombreCampo));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return valorCampo;
    }

    public boolean ifExists(String nombreTabla, String columnaTabla,Object condicion){
        ResultSet data;
        ArrayList<String> valorCampo=new ArrayList<>();
        boolean existe=true;
        String consulta= "Select * "+ " from " + nombreTabla+" where "+columnaTabla+"="+"\""+condicion+"\""+";";
        System.out.println(consulta);
        try {
            PreparedStatement sentenciaSQL = conexion.prepareStatement(consulta);
            data = sentenciaSQL.executeQuery(consulta);
            while (data.next() == true) {

                // El campo Field es el que contiene el nombre
                // de la columna

                valorCampo.add(data.getString(columnaTabla));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        if (valorCampo.size()!=0){

        }else{
            existe=false;

        }
        return existe;
    }


    public String obtenerValorDeUnCampo(String nombreTabla, String nombreCampo, int id){
        ResultSet data;
        String valorCampo=null;
        String columnaId=obtenerColumnasDeUnaTabla(nombreTabla).get(0);
        String consulta= "Select "+ nombreCampo+ " from " + nombreTabla+" where "+columnaId+"="+id+";";
        System.out.println(consulta);
        try {
            PreparedStatement sentenciaSQL = conexion.prepareStatement(consulta);
            data = sentenciaSQL.executeQuery(consulta);
            while (data.next() == true) {
                // El campo Field es el que contiene el nombre
                // de la columna
                valorCampo=data.getString(nombreCampo);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return valorCampo;
    }
    public void actualizarVecesSolicitada(String nombreTabla, String nombreCampo, int id){
        int valorCampoPrevio= Integer.parseInt(obtenerValorDeUnCampo(nombreTabla,nombreCampo, id));
        String columnaId=obtenerColumnasDeUnaTabla(nombreTabla).get(0);
        String consulta= "Update "+nombreTabla+" set " + nombreCampo+ "=" +valorCampoPrevio + "+1"+ " where " + columnaId + "=" +id+";";
        System.out.println(consulta);

        try{
            PreparedStatement sentenciaSQL=conexion.prepareStatement(consulta);
            int result=sentenciaSQL.executeUpdate();

            if(result>0){
                System.out.println("Actualizacion hecha");
            }
            else{
                System.out.println("Fallo actualizacion");
            }

            sentenciaSQL.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void actualizarEstado(String nombreTabla, String nombreCampo, int id){
        String valorCampoPrevio= (obtenerValorDeUnCampo(nombreTabla,nombreCampo, id)).toUpperCase();
        String columnaId=obtenerColumnasDeUnaTabla(nombreTabla).get(0);
        boolean estadoQueHayQueponer=true;
        if(valorCampoPrevio.equals("1")){
            estadoQueHayQueponer=false;
        }
        String consulta= "Update "+nombreTabla+" set " + nombreCampo+ "=" + estadoQueHayQueponer + " where " + columnaId + "=" +id+";";
        System.out.println(consulta);

        try{
            PreparedStatement sentenciaSQL=conexion.prepareStatement(consulta);
            int result=sentenciaSQL.executeUpdate();

            if(result>0){
                System.out.println("Actualizacion hecha");
            }
            else{
                System.out.println("Fallo actualizacion");
            }

            sentenciaSQL.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public int obtenerValorMaximo(String nombreTabla, String nombreColumna){
        int id = 0;
        ResultSet data ;
        String consulta = "SELECT max("+ nombreColumna+ ") FROM " + nombreTabla+ ";" ;

        try {
            PreparedStatement sentenciaSQL = conexion.prepareStatement(consulta);
            data = sentenciaSQL.executeQuery(consulta);
            while (data.next() == true) {

                id=data.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return id;
    }
    public void ingresarDatos(HashMap<String, Object> valores, String nombreTabla){
        ArrayList<String> columnasTablaUsuario= obtenerColumnasDeUnaTabla(nombreTabla);
        String consulta = "Insert into " + nombreTabla + " values ( ";
        for (int i = 0; i <columnasTablaUsuario.size() ; i++) {
            String nombreColumna=columnasTablaUsuario.get(i);
            Object datoIngresado=valores.get(nombreColumna);
            if(datoIngresado.getClass().getSimpleName().equals("Boolean") || datoIngresado.getClass().getSimpleName().equals("Integer")  || datoIngresado.getClass().getSimpleName().equals("Double")){
                consulta=consulta+datoIngresado;
            }
            else {
                consulta = consulta + "\"" + datoIngresado + "\"";
            }
            if(i<columnasTablaUsuario.size()-1){
                consulta=consulta+ ", ";
            }
            if(i==columnasTablaUsuario.size()-1){
                consulta=consulta+");";
            }
        }
        System.out.println(consulta);
        try{
            PreparedStatement sentenciaSQL=conexion.prepareStatement(consulta);
            int result=sentenciaSQL.executeUpdate();

            if(result>0){
                System.out.println("Insercion hecha");
            }
            else{
                System.out.println("Fallo");
            }

            sentenciaSQL.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
}
