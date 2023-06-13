import java.time.LocalDateTime;

public class Fichaje {
    private int numeroCamiseta;
    private LocalDateTime fechaHoraFichaje;
    private Jugador jugador;
    private Equipo club;

    public Fichaje(int numeroCamiseta, LocalDateTime fechaHoraFichaje, Jugador jugador, Equipo club) {
        this.numeroCamiseta = numeroCamiseta;
        this.fechaHoraFichaje = fechaHoraFichaje;
        this.jugador = jugador;
        this.club = club;
    }

    public int getNumeroCamiseta() {
        return numeroCamiseta;
    }

    public void setNumeroCamiseta(int numeroCamiseta) {
        this.numeroCamiseta = numeroCamiseta;
    }

    public LocalDateTime getFechaHoraFichaje() {
        return fechaHoraFichaje;
    }

    public void setFechaHoraFichaje(LocalDateTime fechaHoraFichaje) {
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
}
