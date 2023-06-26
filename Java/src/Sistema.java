import javax.lang.model.util.SimpleAnnotationValueVisitor6;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Sistema {
    private HashSet<Fichaje>historiaFichaje;
    private HashSet<Equipo>listaEquipos;
    private HashSet<Representante>listaManager;
    private AccesoBaseDeDatos accesoBase;
    private List<String>tablas;

    public Sistema(HashSet<Fichaje> historiaFichaje, HashSet<Equipo> listaEquipos, HashSet<Representante> listaManager) {
        this.historiaFichaje = historiaFichaje;
        this.listaEquipos = listaEquipos;
        this.listaManager = listaManager;

    }

    public Sistema(){
        historiaFichaje=new HashSet<>();
        listaEquipos = new HashSet<>();
        listaManager=new HashSet<>();
    }
    public HashSet<Fichaje> getHistoriaFichaje() {
        return historiaFichaje;
    }

    public void setHistoriaFichaje(HashSet<Fichaje> historiaFichaje) {
        this.historiaFichaje = historiaFichaje;
    }

    public HashSet<Equipo> getListaEquipos() {
        return listaEquipos;
    }

    public void setListaEquipos(HashSet<Equipo> listaEquipos) {
        this.listaEquipos = listaEquipos;
    }

    public HashSet<Representante> getListaManager() {
        return listaManager;
    }

    public void setListaManager(HashSet<Representante> listaManager) {
        this.listaManager = listaManager;
    }

    public AccesoBaseDeDatos getAccesoBase() {
        return accesoBase;
    }

    public void setAccesoBase(AccesoBaseDeDatos accesoBase) {
        this.accesoBase = accesoBase;
    }

    public void jugadoresPorClubPorPosicion() {
        String consulta = "SELECT * FROM jugadoresPorClub;";
        ArrayList<String> nombreCampos = new ArrayList<>();
        try {
            ResultSet data;
            PreparedStatement sentenciaSQL = accesoBase.getConexion().prepareStatement(consulta);
            data = sentenciaSQL.executeQuery(consulta);
            while (data.next()) {
                nombreCampos.add(data.getString("nombreJugador"));
                nombreCampos.add(data.getString("apellidoJugador"));
                nombreCampos.add(data.getString("nombreEquipo"));
                nombreCampos.add(data.getString("rol"));
                nombreCampos.add("\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println("Ejercicio A");
        for(String campo:nombreCampos){
            System.out.println(campo);
        }
    }

    public void traerFichaje(){
        String consulta = " SELECT nombreEquipo,cantidadPermitida,numCamiseta,fechaHoraFichaje,Jugador_id,nombreJugador,apellidoJugador,Jugador.fechaNacimiento,Jugador.salario,rol,completado FROM Equipo JOIN Equipo_has_Posicion ON idEquipo = Equipo_has_Posicion.Equipo_id JOIN Posicion ON idPosicion=id JOIN Fichaje ON Fichaje.Equipo_id=idEquipo JOIN Jugador ON Jugador_id=DNI;";

        HashMap<Posicion,Integer> cantidadPermitidaPosicion =new HashMap<Posicion,Integer>();
        Fichaje fichajeNuevo =new Fichaje();
        try {
            ResultSet data;
            PreparedStatement sentenciaSQL = accesoBase.getConexion().prepareStatement(consulta);
            data = sentenciaSQL.executeQuery(consulta);
            while (data.next()) {
                fichajeNuevo.setNumeroCamiseta(data.getInt("numCamiseta"));
                fichajeNuevo.setFechaHoraFichaje(LocalDateTime.parse(data.getString("fechaHoraFichaje")));
                fichajeNuevo.setJugador(new Jugador(data.getInt("Jugador_id"),data.getString("nombreJugador"),data.getString("apellidoJugador"),LocalDate.parse(data.getString("fechaNacimiento")),data.getFloat("salario"),Posicion.valueOf(data.getString("rol"))));
                cantidadPermitidaPosicion.put(Posicion.valueOf(data.getString("rol")),data.getInt("cantidadPermitida"));
                fichajeNuevo.setClub(new Equipo(data.getString("nombreEquipo"),cantidadPermitidaPosicion, /* falta poner el booleano de completo o no, podriamos ponerlo en la base o no c*/));
                historiaFichaje.add(fichajeNuevo);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    public void traerEquipos(){
        String consulta = "SELECT nombreEquipo,rol,cantidadPermitida,numCamiseta FROM Equipo JOIN Equipo_has_Posicion ON idEquipo = Equipo_has_Posicion.Equipo_id JOIN Posicion ON idPosicion=id JOIN Fichaje ON Fichaje.Equipo_id=idEquipo;";
        Equipo equipoNuevo =new Equipo();
        HashMap<Posicion,Integer> cantPermitidaPosicio=new HashMap<>();
        try {
            ResultSet data;
            PreparedStatement sentenciaSQL = accesoBase.getConexion().prepareStatement(consulta);
            data = sentenciaSQL.executeQuery(consulta);
            while (data.next()) {
                equipoNuevo.setNombre(data.getString("nombreEquipo"));
                cantPermitidaPosicio.put(Posicion.valueOf(data.getString("rol")),data.getInt("cantidadPermitida"));
                equipoNuevo.setCantPermitidaPosicion(cantPermitidaPosicio);
                // falta dorsales
                listaEquipos.add(equipoNuevo);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void traerRepresentante(){
        String consulta = "SELECT dniRepresentante,nombreRepresentante,apellidoRepresentante,Representante.fechaNacimiento,nombreEquipo,representante_habilitado from Representante join Representante_has_Equipo ON dniRepresemtante=Representante_DNI join Equipo ON Equipo_idEquipo=idEquipo;";
        Representante representante=new Representante();

        try {
            ResultSet data;
            PreparedStatement sentenciaSQL = accesoBase.getConexion().prepareStatement(consulta);
            data = sentenciaSQL.executeQuery(consulta);
            while (data.next()) {
                representante.setDni(data.getInt("dniRepresentante"));
                representante.setNombre(data.getString("nombreRepresentante"));
                representante.setApellido(data.getString("apellidoRepresentante"));
                representante.setFechaNacimiento(LocalDate.parse(data.getString("fechaNacimiento")));
                // falta settear los hashsets de representante que se harian manipulando los atributos que sacamos de la base
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }}


    public void jugadorMasJovenFichado(){
        String consulta = "SELECT fechaNacimiento, DNI, apellidoJugador, nombreEquipo FROM Jugador JOIN Fichaje ON DNI=Jugador_id JOIN Equipo ON Equipo_id=idEquipo WHERE fechaNacimiento= (SELECT min(fechaNacimiento)FROM Jugador);";
        ArrayList<String> nombreCampos = new ArrayList<>();
        try {
            ResultSet data;
            PreparedStatement sentenciaSQL = accesoBase.getConexion().prepareStatement(consulta);
            data = sentenciaSQL.executeQuery(consulta);
            while (data.next()) {
                nombreCampos.add(data.getString("fechaNacimiento"));
                nombreCampos.add(data.getString("DNI"));
                nombreCampos.add(data.getString("apellidoJugador"));// ESTA LINEA me muestra lo q hay en el campo entre comillas
                nombreCampos.add(data.getString("nombreEquipo"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println("Ejercicio B");
        for(String campo:nombreCampos){
            System.out.println(campo);
        }
    }
    public Jugador jugadorMasJovenFichado2() {
        LocalDateTime masJoven = LocalDateTime.now();
        Jugador jugadorMasJoven=new Jugador();
        for(Fichaje fichaje:historiaFichaje) {
            if(fichaje.getFechaHoraFichaje().compareTo(masJoven)==-1) {
                masJoven=fichaje.getFechaHoraFichaje();
                jugadorMasJoven=fichaje.getJugador();
            }
        }
        return jugadorMasJoven;
    }

    public void fichajeCaidoPorPosicion(){
        String consulta = "SELECT idFichaje, Equipo_id, Jugador_id FROM Fichaje WHERE !verificarPosicion(idFichaje);";
        ArrayList<String> nombreCampos = new ArrayList<>();
        try {
            ResultSet data;
            PreparedStatement sentenciaSQL = accesoBase.getConexion().prepareStatement(consulta);
            data = sentenciaSQL.executeQuery(consulta);

            while (data.next()) {
                nombreCampos.add(data.getString("idFichaje"));
                nombreCampos.add(data.getString("Equipo_id"));
                nombreCampos.add(data.getString("Jugador_id"));
                nombreCampos.add("\n");

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println("Ejercicio C");
        for(String campo:nombreCampos){
            System.out.println(campo);
        }
    }

    public void jugadorMalRepresentado(){
        String consulta = "SELECT nombreJugador, apellidoJugador FROM Jugador JOIN Fichaje ON DNI=Jugador_id WHERE !verificarManager(idFichaje);";
        ArrayList<String> nombreCampos = new ArrayList<>();
        try {
            ResultSet data;
            PreparedStatement sentenciaSQL = accesoBase.getConexion().prepareStatement(consulta);
            data = sentenciaSQL.executeQuery(consulta);

            while (data.next()) {
                nombreCampos.add(data.getString("nombreJugador"));
                nombreCampos.add(data.getString("apellidoJugador"));
                nombreCampos.add("\n");

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println("Ejercicio D");
        for(String campo:nombreCampos){
            System.out.println(campo);
        }
    }

    public void managerRepetidoEnClub(){
        String consulta = "select distinct Representante_DNI, nombreRepresentante, apellidoRepresentante from Representante_has_Equipo join Representante on Representante_DNI=dniRepresentante where 2>jugadoresManagerClub(Representante_DNI, Equipo_idEquipo);";
        ArrayList<String> nombreCampos = new ArrayList<>();
        try {
            ResultSet data;
            PreparedStatement sentenciaSQL = accesoBase.getConexion().prepareStatement(consulta);
            data = sentenciaSQL.executeQuery(consulta);

            while (data.next()) {
                nombreCampos.add(data.getString("Representante_DNI"));
                nombreCampos.add(data.getString("nombreRepresentante"));
                nombreCampos.add(data.getString("apellidoRepresentante"));
                nombreCampos.add("\n");

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println("Ejercicio E");
        for(String campo:nombreCampos){
            System.out.println(campo);
        }
    }

    public HashSet<Representante> managerRepetidoEnClub2() {
        HashSet<Representante> managersRepetidos=new HashSet<>();
        for(Equipo equipo:listaEquipos){
            HashMap<Representante, Integer> managersCompletos=new HashMap<>();
            for (Map.Entry<Integer, Jugador> dorsal:equipo.getDorsales().entrySet()) {
                managersCompletos.put(dorsal.getValue().getRepresentante(), managersCompletos.get(dorsal.getValue().getRepresentante())+1);
            }
            for (Map.Entry<Representante, Integer> representante:managersCompletos.entrySet()) {
                if(representante.getValue()>2){
                    managersRepetidos.add(representante.getKey());
                }
            }
        }
        return managersRepetidos;
    }

    public HashMap<Posicion, Jugador> mejorPagoPorPosicion(){
        HashMap<Posicion, Jugador>hash = new HashMap<>();
        return hash;
    }

    public void clubProhibidoMasRecurrente(){
        String consulta = "select max(cantidad), Equipo_idEquipo from (select Equipo_idEquipo, count(*) as cantidad from Representante_has_Equipo where Representante_habilitado=0 group by Equipo_idEquipo) as cualquiercosa group by Equipo_idEquipo limit 1;";
        ArrayList<String> nombreCampos = new ArrayList<>();
        try {
            ResultSet data;
            PreparedStatement sentenciaSQL = accesoBase.getConexion().prepareStatement(consulta);
            data = sentenciaSQL.executeQuery(consulta);

            while (data.next()) {
                nombreCampos.add(data.getString("max(cantidad)"));
                nombreCampos.add(data.getString("Equipo_idEquipo"));
                nombreCampos.add("\n");

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println("Ejercicio G");
        for(String campo:nombreCampos){
            System.out.println(campo);
        }

    }

    public void correccionFichaje(){

    }

    public HashSet<Representante> managerRepetido(){
        HashSet<Representante>representantes = new HashSet<>();
        return representantes;
    }
    public static void main(String[] args) {
        Sistema s1= new Sistema();

        s1.tablas = Arrays.asList("Jugador", "Posicion", "Fichaje", "Equipo", "Representante", "Representante_has_Equipo", "Equipo_has_Posicion");
        s1.accesoBase = new AccesoBaseDeDatos("PoliPases", s1.tablas);

        try {
            s1.accesoBase.conectar("alumno", "alumnoipm");
            // s1.jugadoresPorClubPorPosicion();
            // s1.jugadorMasJovenFichado();
            s1.fichajeCaidoPorPosicion();
            s1.jugadorMalRepresentado();
            s1.managerRepetidoEnClub();
            s1.clubProhibidoMasRecurrente();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
}

// A los metodos que tienen el nombre y un 2 al final son las versiones en java del ejercicio.
// No borramos la version vieja (hecha con sql) pq nos dijo tincho q no lo hagamos.
// El F (de java) si no lo llegaron a hacer con sql, no hace falta terminarlo, solo haganlo en Java.