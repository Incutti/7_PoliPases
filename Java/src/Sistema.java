import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class Sistema {
    private HashSet<Fichaje>historiaFichaje;
    private HashSet<Equipo>listaEquipos;
    private HashSet<Representante>listaManager;
    private AccesoBaseDeDatos accesoBase;

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

    public void jugadoresPorClubPorPosicion() {
        String consulta = "SHOW COLUMNS FROM jugadoresPorClub";
        ArrayList<String> nombreCampos = new ArrayList<>();
        try {
            ResultSet data;
            PreparedStatement sentenciaSQL = DriverManager.getConnection("jdbc:mysql://localhost:3306/PoliPases", "alumno", "alumnoipm").prepareStatement(consulta);
            data = sentenciaSQL.executeQuery(consulta);
            while (data.next() == true) {
                nombreCampos.add(data.getString("Field"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        // return nombreCampos;
    }

    public Jugador jugadorMasJovenFichado(Equipo equipo){
        Jugador jugador = new Jugador();
        return jugador;
    }

    public Fichaje fichajeCaidoPorPosicion(Equipo equipo, Posicion posicion){
        Fichaje fichaje = new Fichaje();
        return fichaje;
    }

    public HashMap<Posicion, Jugador> mejorPagoPorPosicion(){
        HashMap<Posicion, Jugador>hash = new HashMap<>();
        return hash;
    }

    public Equipo clubProhibidoMasRecurrente(){
        Equipo equipo = new Equipo();
        return equipo;
    }

    public void correccionFichaje(){

    }

    public HashSet<Representante> managerRepetido(){
        HashSet<Representante>representantes = new HashSet<>();
        return representantes;
    }
    public static void main(String[] args) {
        List<String> tablas = Arrays.asList("Jugador", "Posicion", "Fichaje", "Equipo", "Representante", "Representante_has_Equipo", "Equipo_has_Posicion");

        AccesoBaseDeDatos bdd = new AccesoBaseDeDatos("PoliPases", tablas);
        try {
            bdd.conectar("alumno", "alumnoipm");

        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
}
// preguntar si podemos agregar lo de la linea 96 y 98 como atributos de la clase.