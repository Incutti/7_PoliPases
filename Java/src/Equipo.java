import java.util.HashMap;

public class Equipo {
    private String nombre;
    private HashMap<Posicion,Integer> cantPermitidaPosicion;
    private HashMap<Integer, Jugador>Dorsales;

    public Equipo(String nombre, HashMap<Posicion, Integer> cantPermitidaPosicion, HashMap<Integer, Jugador> dorsales) {
        this.nombre = nombre;
        this.cantPermitidaPosicion = cantPermitidaPosicion;
        Dorsales = dorsales;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public HashMap<Posicion, Integer> getCantPermitidaPosicion() {
        return cantPermitidaPosicion;
    }

    public void setCantPermitidaPosicion(HashMap<Posicion, Integer> cantPermitidaPosicion) {
        this.cantPermitidaPosicion = cantPermitidaPosicion;
    }

    public HashMap<Integer, Jugador> getDorsales() {
        return Dorsales;
    }

    public void setDorsales(HashMap<Integer, Jugador> dorsales) {
        Dorsales = dorsales;
    }

    public void jugadoresMalRepresentados(){

    }
}
