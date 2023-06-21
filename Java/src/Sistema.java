import javax.lang.model.util.SimpleAnnotationValueVisitor6;
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
            while (data.next() == true) {
                nombreCampos.add(data.getString("nombreJugador"));
                nombreCampos.add(data.getString("apellidoJugador"));// ESTA LINEA me muestra lo q hay en el campo entre comillas
                nombreCampos.add(data.getString("nombreEquipo"));
                nombreCampos.add(data.getString("rol"));
                nombreCampos.add("\n");

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        for(String campo:nombreCampos){
            System.out.println(campo);
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
        Sistema s1= new Sistema();

        s1.tablas = Arrays.asList("Jugador", "Posicion", "Fichaje", "Equipo", "Representante", "Representante_has_Equipo", "Equipo_has_Posicion");
        s1.accesoBase = new AccesoBaseDeDatos("PoliPases", s1.tablas);

        try {
            s1.accesoBase.conectar("alumno", "alumnoipm");
            s1.jugadoresPorClubPorPosicion();

        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
}
