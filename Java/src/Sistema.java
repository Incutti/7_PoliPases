import java.util.HashMap;
import java.util.HashSet;

public class Sistema {
    private HashSet<Fichaje>HistoriaFichaje;
    private HashSet<Equipo>listaEquipos;
    private HashSet<Representante>listaManager;

    public Sistema(HashSet<Fichaje> historiaFichaje, HashSet<Equipo> listaEquipos, HashSet<Representante> listaManager) {
        HistoriaFichaje = historiaFichaje;
        this.listaEquipos = listaEquipos;
        this.listaManager = listaManager;
    }

    public HashSet<Fichaje> getHistoriaFichaje() {
        return HistoriaFichaje;
    }

    public void setHistoriaFichaje(HashSet<Fichaje> historiaFichaje) {
        HistoriaFichaje = historiaFichaje;
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

    public void listaJugadorPorPosicion(Equipo equipo, Posicion posicion){

    }

    public Jugador jugadorMasJovenFichado(Equipo equipo){

    }

    public Fichaje fichajeCaidoPorPosicion(Equipo equipo, Posicion posicion){

    }

    public HashMap<Posicion, Jugador> mejorPagoPorPosicion(){

    }

    public Equipo clubProhibidoMasRecurrente(){

    }

    public void correccionFichaje(){

    }

    public HashSet<Representante> managerRepetido(){
        
    }
}
