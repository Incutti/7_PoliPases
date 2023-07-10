import java.time.LocalDate;
import java.time.LocalDateTime;

public class Fichaje {
    private int idFichaje;
    private int numeroCamiseta;
    private LocalDate fechaHoraFichaje;
    private Jugador jugador;
    private Equipo club;
    private boolean completado;

    public Fichaje(int idFichaje, int numeroCamiseta, LocalDate fechaHoraFichaje, Jugador jugador, Equipo club, boolean completado) {
        this.idFichaje=idFichaje;
        this.numeroCamiseta = numeroCamiseta;
        this.fechaHoraFichaje = fechaHoraFichaje;
        this.jugador = jugador;
        this.club = club;
        this.completado = completado;
    }

    public Fichaje(){
        idFichaje=21311;
        numeroCamiseta= 3;
        fechaHoraFichaje=LocalDate.now();
        jugador=new Jugador();
        club=new Equipo();
        completado=false;
    }

    public int getIdFichaje() {
        return idFichaje;
    }

    public void setIdFichaje(int idFichaje) {
        this.idFichaje = idFichaje;
    }

    public int getNumeroCamiseta() {
        return numeroCamiseta;
    }

    public void setNumeroCamiseta(int numeroCamiseta) {
        this.numeroCamiseta = numeroCamiseta;
    }

    public LocalDate getFechaHoraFichaje() {
        return fechaHoraFichaje;
    }

    public void setFechaHoraFichaje(LocalDate fechaHoraFichaje) {
        this.fechaHoraFichaje = fechaHoraFichaje;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public Equipo getClub() {
        return club;
    }

    public void setClub(Equipo club) {
        this.club = club;
    }

    public boolean isCompletado() {
        return completado;
    }

    public void setCompletado(boolean completado) {
        this.completado = completado;
    }
}
