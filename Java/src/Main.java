import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> tablas = Arrays.asList("Jugador", "Posicion", "Fichaje", "Equipo", "Representante", "Representante_has_Equipo", "Equipo_has_Posicion");

        AccesoBaseDeDatos bdd = new AccesoBaseDeDatos("PoliPases", tablas);
        try {
            bdd.conectar("alumno", "alumnoipm");

        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
}