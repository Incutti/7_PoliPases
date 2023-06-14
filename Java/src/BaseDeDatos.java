import java.sql.*;
import java.util.Arrays;
import java.util.List;
public class BaseDeDatos {
    private Connection conexion;
    private String nombreBaseDeDatos;
    private List<String> nombreTabla;

    public BaseDeDatos(String nombreBaseDeDatos, List<String> nombreTabla) {
        this.nombreBaseDeDatos = nombreBaseDeDatos;
        this.nombreTabla = nombreTabla;
    }

    public void conectar(String user, String password) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/"+nombreBaseDeDatos;
        try {
            conexion = DriverManager.getConnection(url, user, password);
            if (conexion != null) {
                System.out.println("Se ha conectado exitósamente a la base de datos");
            } else {
                System.out.println("No se ha podido conectar a la base de datos");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public static void main(String[] args) {
        List<String>tablas = Arrays.asList("Equipo", "Equipo_has_Posicion", "Fichaje", "Jugador","Posicion","Representante","Representante_has_Equipo");;
        BaseDeDatos baseDeDatos = new BaseDeDatos("PoliPases",tablas);
        try {
            baseDeDatos.conectar("alumno","alumnoipm");
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
}
