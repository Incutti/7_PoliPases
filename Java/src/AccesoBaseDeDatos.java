
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AccesoBaseDeDatos {

    private Connection conexion;
    private String nombreBaseDeDatos;
    private List<String> nombreTabla;

    public AccesoBaseDeDatos(String nombreBaseDeDatos, List<String> nombreTabla) {
        this.nombreBaseDeDatos = nombreBaseDeDatos;
        this.nombreTabla = nombreTabla;
    }

    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }

    public String getNombreBaseDeDatos() {
        return nombreBaseDeDatos;
    }

    public void setNombreBaseDeDatos(String nombreBaseDeDatos) {
        this.nombreBaseDeDatos = nombreBaseDeDatos;
    }

    public List<String> getNombreTabla() {
        return nombreTabla;
    }

    public void setNombreTabla(List<String> nombreTabla) {
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


    public HashMap<Integer,HashMap<String,Object>> traerRepresentante(){

        String consulta = "SELECT dniRepresentante,nombreRepresentante,apellidoRepresentante,Representante.fechaNacimiento,nombreEquipo,representante_habilitado from Representante join Representante_has_Equipo ON dniRepresentante=Representante_DNI join Equipo ON Equipo_idEquipo=idEquipo;";
        HashMap<Integer,HashMap<String,Object>> datitos= new HashMap<>();
        try {
            ResultSet data;
            PreparedStatement sentenciaSQL = conexion.prepareStatement(consulta);
            data = sentenciaSQL.executeQuery(consulta);
            ArrayList<String>columnasRepresentante = obtenerColumnasDeUnaTabla("Representante");
            ArrayList<String>columnasJugador = obtenerColumnasDeUnaTabla("Jugador");
            ArrayList<String>columnasRepresentante_has_Equipo = obtenerColumnasDeUnaTabla("Representante_has_Equipo");
            ArrayList<String>columnasPosicion = obtenerColumnasDeUnaTabla("Posicion");
            while (data.next()) {
                HashMap<String, Object> auxiliar=new HashMap<>();
//              Representante representante=new Representante();
//              representante.setDni(data.getInt("dniRepresentante"));
                auxiliar.put(columnasRepresentante.get(1),data.getString(columnasRepresentante.get(1)));
                auxiliar.put(columnasRepresentante.get(2),data.getString(columnasRepresentante.get(2)));
                auxiliar.put(columnasRepresentante.get(3),LocalDate.parse(data.getString(columnasRepresentante.get(3))));
                String selectProhibidos = "select Equipo_idEquipo from Representante_has_Equipo where Representante_DNI = \"" + data.getString("dniRepresentante") +"\" and Representante_habilitado = false;";
                ResultSet datosProhidos;
                PreparedStatement sentenciaProhibida = conexion.prepareStatement(selectProhibidos);
                datosProhidos = sentenciaProhibida.executeQuery(selectProhibidos);
                HashSet<Integer> equiposProhibidos = new HashSet<>();
                while (datosProhidos.next() == true) {
                    equiposProhibidos.add(datosProhidos.getInt(columnasRepresentante_has_Equipo.get(1)));
                }
                auxiliar.put("EquiposProhibido", equiposProhibidos);
                /*
                HashMap<Integer,HashMap<String,Object>> listaEquipos = traerEquipos();
                while (datosProhidos.next() == true) {

                    for (Map.Entry<Integer, HashMap<String, Object>> id : listaEquipos.entrySet()) {
                        if (id.getKey() == data.getInt("Equipo_idEquipo")) {
                            auxiliar.put("EquipoProhibido", id);
                        }
                    }
                }
                */
                String selectContactados = "select Equipo_idEquipo from Representante_has_Equipo where Representante_DNI = \"" + data.getString("dniRepresentante") +"\" and Representante_habilitado = true;";
                ResultSet datosContactados;
                PreparedStatement sentenciaContactados = conexion.prepareStatement(selectContactados);
                datosContactados = sentenciaContactados.executeQuery(selectContactados);
                HashSet<Integer> equiposContactados = new HashSet<>();
                while (datosContactados.next() == true) {
                    equiposContactados.add(datosContactados.getInt(columnasRepresentante_has_Equipo.get(1)));
                }
                auxiliar.put("EquiposContactados", equiposContactados);
                /*
                HashSet<Equipo>clubesContactados = new HashSet<>();
                while (datosContactados.next() == true) {
                    for (Map.Entry<Integer, HashMap<String, Object>> id : listaEquipos.entrySet()) {
                        if (id.getKey() == data.getInt("Equipo_idEquipo")) {
                            auxiliar.put("EquipoContactado", id);
                        }
                    }
                }
                 */
                String consulta1 = "select DNI,nombreJugador,apellidoJugador,fechaNacimiento,salario,upper(rol) from Jugador join Posicion on Jugador.Posicion_idPosicion = Posicion.id where Representante_DNI = "+data.getInt("dniRepresentante")+";";
                ResultSet datosRepresentados;
                PreparedStatement sentenciaRepresentados = conexion.prepareStatement(consulta1);
                datosRepresentados = sentenciaRepresentados.executeQuery(consulta1);
                HashMap<Integer,HashMap<String,Object>> jugadores=new HashMap<>();
                HashMap<String,Object>aux2=new HashMap<>();
                while (datosRepresentados.next() == true) {
                    aux2.put(columnasJugador.get(1),datosRepresentados.getString(columnasJugador.get(1)));
                    aux2.put(columnasJugador.get(2),datosRepresentados.getString(columnasJugador.get(2)));
                    aux2.put(columnasJugador.get(3),LocalDate.parse(datosRepresentados.getString(columnasJugador.get(3))));
                    aux2.put(columnasJugador.get(4),datosRepresentados.getFloat(columnasJugador.get(4)));
                    aux2.put(columnasPosicion.get(1),Posicion.valueOf(datosRepresentados.getString("upper("+columnasPosicion.get(1)+")")));
                    jugadores.put(datosRepresentados.getInt(columnasJugador.get(0)),aux2);
                }
                auxiliar.put("Jugadores",jugadores);
                datitos.put(data.getInt(columnasRepresentante.get(0)), auxiliar);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return datitos;
    }



    public HashMap<Integer,HashMap<String,Object>> traerEquipos(){
        HashMap<Integer,HashMap<String,Object>> datitos=new HashMap<>();
        HashMap<String,Object> auxiliar=new HashMap<>();
        String consulta = "SELECT idEquipo,nombreEquipo FROM Equipo;";
        try {
            ResultSet data;
            PreparedStatement sentenciaSQL = conexion.prepareStatement(consulta);
            data = sentenciaSQL.executeQuery(consulta);
            while (data.next()) {
                String selectPosiciones = "SELECT upper(rol),cantidadPermitida FROM Equipo JOIN Equipo_has_Posicion ON idEquipo = Equipo_has_Posicion.Equipo_id JOIN Posicion ON idPosicion=id JOIN Fichaje ON Fichaje.Equipo_id=idEquipo where idEquipo = "+data.getInt("idEquipo")+";";
                ResultSet posiciones;
                PreparedStatement sentencia = conexion.prepareStatement(selectPosiciones);
                posiciones = sentencia.executeQuery(selectPosiciones);
//                Equipo equipoNuevo =new Equipo();
                HashMap<Posicion,Integer> cantPermitidaPosicio=new HashMap<>();
//                equipoNuevo.setId(data.getInt("idEquipo"));
//                equipoNuevo.setNombre(data.getString("nombreEquipo"));
                auxiliar.put("nombreEquipo",data.getString("nombreEquipo"));
                while (posiciones.next() == true) {
                    cantPermitidaPosicio.put(Posicion.valueOf(posiciones.getString("upper(rol)")), posiciones.getInt("cantidadPermitida"));
                }
                auxiliar.put("cantPermitidaPosicion",cantPermitidaPosicio);
//                equipoNuevo.setCantPermitidaPosicion(cantPermitidaPosicio);
                auxiliar.put("dorsales",new HashMap<>());
                // falta dorsales
                datitos.put(data.getInt("idEquipo"),auxiliar);
//                listaEquipos.add(equipoNuevo);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return datitos;
    }

    public HashMap<Integer,HashMap<String,Object>> traerFichajes(){
        HashMap<Integer,HashMap<String,Object>> datitos=new HashMap<>();
        String consulta = "select nombreJugador,apellidoJugador,fechaNacimiento,salario,upper(rol),Representante_DNI,Posicion_idPosicion,idFichaje,numCamiseta,fechaHoraFichaje,Equipo_id,Jugador_id,completado from Fichaje join Jugador on Fichaje.Jugador_id = Jugador.DNI join Posicion on Jugador.Posicion_idPosicion=Posicion.id;";
        Representante managerJugador = new Representante();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            ResultSet data;
            PreparedStatement sentenciaSQL = conexion.prepareStatement(consulta);
            data = sentenciaSQL.executeQuery(consulta);
            while (data.next()) {
                Fichaje fichajeNuevo = new Fichaje();
                HashMap<String, Object> auxiliar = new HashMap<>();
                auxiliar.put("numCamiseta", data.getInt("numCamiseta"));
                auxiliar.put("fechaHoraFichaje", data.getString("fechaHoraFichaje"));
                HashMap<String,Object> jugador=new HashMap<>();
                jugador.put("DNI",data.getInt("Jugador_id"));
                jugador.put("nombreJugador",data.getString("nombreJugador"));
                jugador.put("apellidoJugador",data.getString("apellidoJugador"));
                jugador.put("fechaNacimiento",LocalDate.parse(data.getString("fechaNacimiento")));
                jugador.put("salario",data.getFloat("salario"));
                jugador.put("rol",Posicion.valueOf(data.getString("upper(rol)")));
                auxiliar.put("Jugador",jugador);
                auxiliar.put("Equipo_id",data.getInt("Equipo_id"));
                auxiliar.put("completado", data.getBoolean("completado"));
                datitos.put(data.getInt("idFichaje"), auxiliar);
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        return datitos;
    }
    public ArrayList<String> jugadoresPorClubPorPosicion() {
        String consulta = "SELECT *,upper(rol) FROM jugadoresPorClub;";
        ArrayList<String> nombreCampos = new ArrayList<>();
        try {
            ResultSet data;
            PreparedStatement sentenciaSQL = conexion.prepareStatement(consulta);
            data = sentenciaSQL.
                    executeQuery(consulta);
            while (data.next()) {
                nombreCampos.add(data.getString("upper(rol)"));
                nombreCampos.add(data.getString("nombreJugador"));
                nombreCampos.add(data.getString("apellidoJugador"));
                nombreCampos.add(data.getString("nombreEquipo"));
                nombreCampos.add("\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return nombreCampos;
    }
    public ArrayList<String> fichajeCaidoPorPosicion(){
        String consulta = "SELECT idFichaje, Equipo_id, Jugador_id FROM Fichaje WHERE !verificarPosicion(idFichaje);";
        ArrayList<String> nombreCampos = new ArrayList<>();
        try {
            ResultSet data;
            PreparedStatement sentenciaSQL = conexion.prepareStatement(consulta);
            data = sentenciaSQL.executeQuery(consulta);

            while (data.next()) {
                nombreCampos.add(data.getString("idFichaje"));
                nombreCampos.add(data.getString("Equipo_id"));
                nombreCampos.add(data.getString("Jugador_id"));

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return nombreCampos;
    }
    public ArrayList<String> jugadorMalRepresentado(){
        String consulta = "SELECT distinct nombreJugador, apellidoJugador FROM Jugador JOIN Fichaje ON DNI=Jugador_id WHERE !verificarManager(idFichaje);";
        ArrayList<String> nombreCampos = new ArrayList<>();
        try {
            ResultSet data;
            PreparedStatement sentenciaSQL = conexion.prepareStatement(consulta);
            data = sentenciaSQL.executeQuery(consulta);

            while (data.next()) {
                nombreCampos.add(data.getString("nombreJugador"));
                nombreCampos.add(data.getString("apellidoJugador"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return nombreCampos;
    }

    public ArrayList<String> clubProhibidoMasRecurrente(){
        String consulta = "select max(cantidad), Equipo_idEquipo from (select Equipo_idEquipo, count(*) as cantidad from Representante_has_Equipo where Representante_habilitado=0 group by Equipo_idEquipo) as cualquiercosa group by Equipo_idEquipo limit 1;";
        ArrayList<String> nombreCampos = new ArrayList<>();
        try {
            ResultSet data;
            PreparedStatement sentenciaSQL = conexion.prepareStatement(consulta);
            data = sentenciaSQL.executeQuery(consulta);

            while (data.next()) {
                nombreCampos.add("CANTIDAD DE RECURRENCIAS:");
                nombreCampos.add(data.getString("max(cantidad)"));
                nombreCampos.add("ID EQUIPO:");
                nombreCampos.add(data.getString("Equipo_idEquipo"));
                nombreCampos.add("\n");

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return nombreCampos;
    }

    public void verificarFichaje(int fichajeID){
        String consulta="{call verificarFichaje(?,?,?)};";
        try{
            CallableStatement sentenciaSQL = conexion.prepareCall(consulta);
            sentenciaSQL.setInt(1,fichajeID);
            sentenciaSQL.registerOutParameter(2, Types.NVARCHAR);
            sentenciaSQL.registerOutParameter(3, Types.NVARCHAR);

            sentenciaSQL.execute();
            //
            // Process all returned result sets
            //
            String error = sentenciaSQL.getString(2);
            if (!error.equals("")) {
                System.out.println("Id: " + fichajeID);
                System.out.println("Error: " + error);
            }
            String correccion = sentenciaSQL.getString(3);
            if(!correccion.equals("")){
                System.out.println("Correcciones: " + correccion);
            }

        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
}
