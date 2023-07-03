
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
            while (data.next()) {
                HashMap<String, Object> auxiliar=new HashMap<>();
//                Representante representante=new Representante();
//                representante.setDni(data.getInt("dniRepresentante"));
                auxiliar.put("nombreRepresentante",data.getString("nombreRepresentante"));
                auxiliar.put("apellidoRepresentante",data.getString("apellidoRepresentante"));
                auxiliar.put("fechaNacimiento",LocalDate.parse(data.getString("fechaNacimiento")));
                //Primero traer Clubes
                String selectProhibidos = "select Equipo_idEquipo from Representante_has_Equipo where Representante_DNI = \"" + data.getString("dniRepresentante") +"\" and Representante_habilitado = false;";
                ResultSet datosProhidos;
                PreparedStatement sentenciaProhibida = conexion.prepareStatement(selectProhibidos);
                datosProhidos = sentenciaProhibida.executeQuery(selectProhibidos);
                HashMap<Integer,HashMap<String,Object>> listaEquipos = traerEquipos();
                while (datosProhidos.next() == true) {

                    for (Map.Entry<Integer, HashMap<String, Object>> id : listaEquipos.entrySet()) {
                        if (id.getKey() == data.getInt("Equipo_idEquipo")) {
                            auxiliar.put("EquipoProhibido", id);
                        }
                    }
                }
                String selectContactados = "select Equipo_idEquipo from Representante_has_Equipo where Representante_DNI = \"" + data.getString("dniRepresentante") +"\" and Representante_habilitado = true;";
                ResultSet datosContactados;
                PreparedStatement sentenciaContactados = conexion.prepareStatement(selectContactados);
                datosContactados = sentenciaContactados.executeQuery(selectContactados);
                HashSet<Equipo>clubesContactados = new HashSet<>();
                while (datosContactados.next() == true) {
                    for (Map.Entry<Integer, HashMap<String, Object>> id : listaEquipos.entrySet()) {
                        if (id.getKey() == data.getInt("Equipo_idEquipo")) {
                            auxiliar.put("EquipoContactado", id);
                        }
                    }
                }
                String consulta1 = "select DNI,nombreJugador,apellidoJugador,fechaNacimiento,salario,upper(rol) from Jugador join Posicion on Jugador.Posicion_idPosicion = Posicion.id where Representante_DNI = "+data.getInt("dniRepresentante")+";";
                ResultSet datosRepresentados;
                PreparedStatement sentenciaRepresentados = conexion.prepareStatement(consulta1);
                datosRepresentados = sentenciaRepresentados.executeQuery(consulta1);
                HashMap<Integer,HashMap<String,Object>> jugadores=new HashMap<>();
                HashMap<String,Object>aux2=new HashMap<>();
                while (datosRepresentados.next() == true) {
                    aux2.put("nombreJugador",datosRepresentados.getString("nombreJugador"));
                    aux2.put("apellidoJugador",datosRepresentados.getString("apellidoJugador"));
                    aux2.put("fechaNacimiento",LocalDate.parse(datosRepresentados.getString("fechaNacimiento")));
                    aux2.put("salario",datosRepresentados.getFloat("salario"));
                    aux2.put("posicion",Posicion.valueOf(datosRepresentados.getString("upper(rol)")));
                    jugadores.put(datosRepresentados.getInt("DNI"),aux2);
                }
                auxiliar.put("Jugadores",jugadores);
                datitos.put(data.getInt("dniRepresentante"), auxiliar);
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
        llenarDorsales();
        return datitos;
    }

    public void llenarDorsales(){

        // hay que completar fichajes :/
        for(Fichaje f : historiaFichaje){
            HashMap<Integer, Jugador>dorsales =f.getClub().getDorsales();
            dorsales.put(f.getNumeroCamiseta(), f.getJugador());
            f.getClub().setDorsales(dorsales);
        }
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
                Fichaje fichajeNuevo =new Fichaje();
                HashMap<String,Object> auxiliar=new HashMap<>();
                auxiliar.put("numCamiseta",data.getInt("numCamiseta"));
                auxiliar.put("fechaHoraFichaje",data.getString("fechaHoraFichaje"));
//                fichajeNuevo.setFechaHoraFichaje(LocalDateTime.parse(data.getString("fechaHoraFichaje"),formatter));

                //Hay que traer primero los Representantes

                HashMap<Integer,HashMap<String,Object>> listaManager=traerRepresentante();
                for(Map.Entry<Integer, HashMap<String, Object>> lista : listaManager.entrySet())
                    if(lista.getKey() == data.getInt("Representante_DNI")){

                    }
                }
                fichajeNuevo.setJugador(new Jugador(data.getInt("Jugador_id"),data.getString("nombreJugador"),data.getString("apellidoJugador"), LocalDate.parse(data.getString("fechaNacimiento")),data.getFloat("salario"),managerJugador,Posicion.valueOf(data.getString("upper(rol)"))));

                HashMap<Integer,HashMap<String,Object>> listaEquipos=traerEquipos();

                for(Map.Entry<Integer, HashMap<String,Object>> id: listaEquipos.entrySet()) {
                    if (data.getInt("Equipo_id") == id.getKey()) {
                        auxiliar.put("Equipo",id);
                    }
                }
//                    for (Map.Entry<String,Object> valores: id.getValue().entrySet()){
//
//                }
                auxiliar.put("completado",data.getBoolean("completado"));
//                historiaFichaje.add(fichajeNuevo);
                datitos.put(data.getInt("idFichaje"),auxiliar);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return datitos;
    }

}
