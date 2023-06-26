import java.time.LocalDate;

public class Jugador extends Persona{
    private double salario;
    private Representante representante;
    private Posicion posicion;

    public Jugador(){
        super();
        double salario=0;
        representante=new Representante();
        posicion=Posicion.DELANTERO;
    }

    public Jugador(int dni, String nombre, String apellido, LocalDate fechaNacimiento, double salario, Posicion posicion) {
        super(dni, nombre, apellido, fechaNacimiento);
        this.salario = salario;
        this.posicion = posicion;
    }

    public Jugador(int dni, String nombre, String apellido, LocalDate fechaNacimiento, double salario, Representante representante, Posicion posicion) {
        super(dni, nombre, apellido, fechaNacimiento);
        this.salario = salario;
        this.representante = representante;
        this.posicion = posicion;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public Representante getRepresentante() {
        return representante;
    }

    public void setRepresentante(Representante representante) {
        this.representante = representante;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public void setPosicion(Posicion posicion) {
        this.posicion = posicion;
    }
}
