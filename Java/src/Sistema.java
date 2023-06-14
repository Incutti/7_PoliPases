import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Sistema {
    private HashSet<Fichaje>historiaFichaje;
    private HashSet<Equipo>listaEquipos;
    private HashSet<Representante>listaManager;

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

    public void listaJugadorPorPosicion(Equipo equipo, Posicion posicion){
        HashSet<Jugador>jugadores=new HashSet<>();
        for(Equipo equipoEnUso:listaEquipos){
            if(equipoEnUso.equals(equipo)){
                for(Map.Entry<Integer, Jugador> jugador:equipoEnUso.getDorsales().entrySet()){
                    if(jugador.getValue().getPosicion().equals(posicion)){
                        jugadores.add(jugador.getValue());
                    }
                }
            }
        }
    } // hicimos esto pero despues de que lo hagamos explicó q teniamos q llamar a las tblas desde sql asi q no creo q sirva.
    // PD: como explico la conexion al final de la clase no llegamos a hacer nada mas que descargar el .jar
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
