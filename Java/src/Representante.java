import java.time.LocalDate;
import java.util.HashSet;

public class Representante extends Persona{
    private HashSet<Equipo>clubesProhibidos;
    private HashSet<Equipo>clubesContactados;
    private HashSet<Jugador>jugadoresRepresentados;

    public Representante(int dni, String nombre, String apellido, LocalDate fechaNacimiento, HashSet<Equipo> clubesProhibidos, HashSet<Equipo> clubesContactados, HashSet<Jugador> jugadoresRepresentados) {
        super(dni, nombre, apellido, fechaNacimiento);
        this.clubesProhibidos = clubesProhibidos;
        this.clubesContactados = clubesContactados;
        this.jugadoresRepresentados = jugadoresRepresentados;
    }

    public  Representante () {
        
        clubesContactados = new HashSet<>();
        jugadoresRepresentados = new HashSet<>();
    }
    public HashSet<Equipo> getClubesProhibidos() {
        return clubesProhibidos;
    }

    public void setClubesProhibidos(HashSet<Equipo> clubesProhibidos) {
        this.clubesProhibidos = clubesProhibidos;
    }

    public HashSet<Equipo> getClubesContactados() {
        return clubesContactados;
    }

    public void setClubesContactados(HashSet<Equipo> clubesContactados) {
        this.clubesContactados = clubesContactados;
    }

    public HashSet<Jugador> getJugadoresRepresentados() {
        return jugadoresRepresentados;
    }

    public void setJugadoresRepresentados(HashSet<Jugador> jugadoresRepresentados) {
        this.jugadoresRepresentados = jugadoresRepresentados;
    }
}
