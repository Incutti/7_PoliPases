import java.sql.Date;
import java.time.LocalDate;

public class Jugador extends Persona{
    private double salario;
    private int representante_DNI;
    private Posicion posicion;

    public Jugador(){
        super();
        double salario=0;
<<<<<<< HEAD
        representante=new Representante();
        posicion=Posicion.DELANTERO;
    }

    public Jugador(int dni, String nombre, String apellido, LocalDate fechaNacimiento, double salario, Posicion posicion) {
        super(dni, nombre, apellido, fechaNacimiento);
        this.salario = salario;
        this.posicion = posicion;
    }

    public Jugador(int dni, String nombre, String apellido, LocalDate fechaNacimiento, double salario, Representante representante, Posicion posicion) {
=======
        representante_DNI = 12345678;
        posicion=new Posicion();
    }
    public Jugador(int dni, String nombre, String apellido, Date fechaNacimiento, double salario, int representante, Posicion posicion) {
>>>>>>> Caserez-Centrone
        super(dni, nombre, apellido, fechaNacimiento);
        this.salario = salario;
        this.representante_DNI = representante;
        this.posicion = posicion;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public int getRepresentante() {
        return representante_DNI;
    }

    public void setRepresentante(int representante) {
        this.representante_DNI = representante;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public void setPosicion(Posicion posicion) {
        this.posicion = posicion;
    }
}
