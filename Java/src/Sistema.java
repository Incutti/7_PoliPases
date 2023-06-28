import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Sistema {
    private HashSet<Fichaje>historiaFichaje;
    private HashSet<Equipo>listaEquipos;
    private HashSet<Representante>listaManager;
    private AccesoBaseDeDatos accesoBase;
    private List<String>tablas;

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

    public AccesoBaseDeDatos getAccesoBase() {
        return accesoBase;
    }

    public void setAccesoBase(AccesoBaseDeDatos accesoBase) {
        this.accesoBase = accesoBase;
    }

    public void traerFichajes(){
        String consulta = "select nombreJugador,apellidoJugador,fechaNacimiento,salario,upper(rol),Representante_DNI,Posicion_idPosicion,idFichaje,numCamiseta,fechaHoraFichaje,Equipo_id,Jugador_id from Fichaje join Jugador on Fichaje.Jugador_id = Jugador.DNI join Posicion on Jugador.Posicion_idPosicion=Posicion.id;";
        Representante managerJugador = new Representante();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            ResultSet data;
            PreparedStatement sentenciaSQL = accesoBase.getConexion().prepareStatement(consulta);
            data = sentenciaSQL.executeQuery(consulta);
            while (data.next()) {
                Fichaje fichajeNuevo =new Fichaje();
                fichajeNuevo.setIdFichaje(data.getInt("idFichaje"));
                fichajeNuevo.setNumeroCamiseta(data.getInt("numCamiseta"));
                fichajeNuevo.setFechaHoraFichaje(LocalDateTime.parse(data.getString("fechaHoraFichaje"),formatter));
                //Hay que traer primero los Representantes
                for(Representante r : listaManager){
                    if(r.getDni() == data.getInt("Representante_DNI")){
                        managerJugador = r;
                    }
                }
                fichajeNuevo.setJugador(new Jugador(data.getInt("Jugador_id"),data.getString("nombreJugador"),data.getString("apellidoJugador"),LocalDate.parse(data.getString("fechaNacimiento")),data.getFloat("salario"),managerJugador,Posicion.valueOf(data.getString("upper(rol)"))));
                for(Equipo e: listaEquipos){
                    if(data.getInt("Equipo_id") == e.getId()) {
                        fichajeNuevo.setClub(e);
                    }
                }
                /* falta poner el booleano de completo o no, podriamos ponerlo en la base o no c*/
                historiaFichaje.add(fichajeNuevo);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    public void traerEquipos(){
        String consulta = "SELECT idEquipo,nombreEquipo FROM Equipo;";
        try {
            ResultSet data;
            PreparedStatement sentenciaSQL = accesoBase.getConexion().prepareStatement(consulta);
            data = sentenciaSQL.executeQuery(consulta);
            while (data.next()) {
                String selectPosiciones = "SELECT upper(rol),cantidadPermitida FROM Equipo JOIN Equipo_has_Posicion ON idEquipo = Equipo_has_Posicion.Equipo_id JOIN Posicion ON idPosicion=id JOIN Fichaje ON Fichaje.Equipo_id=idEquipo where idEquipo = "+data.getInt("idEquipo")+";";
                ResultSet posiciones;
                PreparedStatement sentencia = accesoBase.getConexion().prepareStatement(selectPosiciones);
                posiciones = sentencia.executeQuery(selectPosiciones);
                Equipo equipoNuevo =new Equipo();
                HashMap<Posicion,Integer> cantPermitidaPosicio=new HashMap<>();
                equipoNuevo.setId(data.getInt("idEquipo"));
                equipoNuevo.setNombre(data.getString("nombreEquipo"));
                while (posiciones.next() == true) {
                    cantPermitidaPosicio.put(Posicion.valueOf(posiciones.getString("upper(rol)")), posiciones.getInt("cantidadPermitida"));
                }
                equipoNuevo.setCantPermitidaPosicion(cantPermitidaPosicio);
                equipoNuevo.setDorsales(new HashMap<>());
                // falta dorsales
                listaEquipos.add(equipoNuevo);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void llenarDorsales(){
        for(Fichaje f : historiaFichaje){
            HashMap<Integer, Jugador>dorsales =f.getClub().getDorsales();
            dorsales.put(f.getNumeroCamiseta(), f.getJugador());
            f.getClub().setDorsales(dorsales);
        }
    }

    public void traerRepresentante(){
        String consulta = "SELECT dniRepresentante,nombreRepresentante,apellidoRepresentante,Representante.fechaNacimiento,nombreEquipo,representante_habilitado from Representante join Representante_has_Equipo ON dniRepresentante=Representante_DNI join Equipo ON Equipo_idEquipo=idEquipo;";
        try {
            ResultSet data;
            PreparedStatement sentenciaSQL = accesoBase.getConexion().prepareStatement(consulta);
            data = sentenciaSQL.executeQuery(consulta);
            while (data.next()) {
                Representante representante=new Representante();
                representante.setDni(data.getInt("dniRepresentante"));
                representante.setNombre(data.getString("nombreRepresentante"));
                representante.setApellido(data.getString("apellidoRepresentante"));
                representante.setFechaNacimiento(LocalDate.parse(data.getString("fechaNacimiento")));
                //Primero traer Clubes
                String selectProhibidos = "select Equipo_idEquipo from Representante_has_Equipo where Representante_DNI = \"" + data.getString("dniRepresentante") +"\" and Representante_habilitado = false;";
                ResultSet datosProhidos;
                PreparedStatement sentenciaProhibida = accesoBase.getConexion().prepareStatement(selectProhibidos);
                datosProhidos = sentenciaProhibida.executeQuery(selectProhibidos);
                HashSet<Equipo>clubesProhibidos = new HashSet<>();
                while (datosProhidos.next() == true) {
                    for (Equipo e : listaEquipos) {
                        if(e.getId() == datosProhidos.getInt("Equipo_idEquipo")){
                            clubesProhibidos.add(e);
                        }
                    }
                }
                representante.setClubesProhibidos(clubesProhibidos);
                String selectContactados = "select Equipo_idEquipo from Representante_has_Equipo where Representante_DNI = \"" + data.getString("dniRepresentante") +"\" and Representante_habilitado = true;";
                ResultSet datosContactados;
                PreparedStatement sentenciaContactados = accesoBase.getConexion().prepareStatement(selectContactados);
                datosContactados = sentenciaContactados.executeQuery(selectContactados);
                HashSet<Equipo>clubesContactados = new HashSet<>();
                while (datosContactados.next() == true) {
                    for (Equipo e : listaEquipos) {
                        if(e.getId() == datosContactados.getInt("Equipo_idEquipo")){
                            clubesContactados.add(e);
                        }
                    }
                }
                representante.setClubesContactados(clubesContactados);
                String consulta1 = "select DNI,nombreJugador,apellidoJugador,fechaNacimiento,salario,upper(rol) from Jugador join Posicion on Jugador.Posicion_idPosicion = Posicion.id where Representante_DNI = "+data.getInt("dniRepresentante")+";";
                ResultSet datosRepresentados;
                PreparedStatement sentenciaRepresentados = accesoBase.getConexion().prepareStatement(consulta1);
                datosRepresentados = sentenciaRepresentados.executeQuery(consulta1);
                HashSet<Jugador>representados = new HashSet<>();
                while (datosRepresentados.next() == true) {
                    representados.add(new Jugador(datosRepresentados.getInt("DNI"), datosRepresentados.getString("nombreJugador"), datosRepresentados.getString("apellidoJugador"), LocalDate.parse(datosRepresentados.getString("fechaNacimiento")), datosRepresentados.getFloat("salario"), Posicion.valueOf(datosRepresentados.getString("upper(rol)"))));
                }
                representante.setJugadoresRepresentados(representados);
                listaManager.add(representante);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    public void jugadoresPorClubPorPosicion() {
        String consulta = "SELECT * FROM jugadoresPorClub;";
        ArrayList<String> nombreCampos = new ArrayList<>();
        try {
            ResultSet data;
            PreparedStatement sentenciaSQL = accesoBase.getConexion().prepareStatement(consulta);
            data = sentenciaSQL.executeQuery(consulta);
            while (data.next()) {
                nombreCampos.add(data.getString("nombreJugador"));
                nombreCampos.add(data.getString("apellidoJugador"));
                nombreCampos.add(data.getString("nombreEquipo"));
                nombreCampos.add(data.getString("rol"));
                nombreCampos.add("\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println("Ejercicio A");
        for(String campo:nombreCampos){
            System.out.println(campo);
        }
    }

    public void jugadorMasJovenFichado(){
        String consulta = "SELECT fechaNacimiento, DNI, apellidoJugador, nombreEquipo FROM Jugador JOIN Fichaje ON DNI=Jugador_id JOIN Equipo ON Equipo_id=idEquipo WHERE fechaNacimiento= (SELECT min(fechaNacimiento)FROM Jugador);";
        ArrayList<String> nombreCampos = new ArrayList<>();
        try {
            ResultSet data;
            PreparedStatement sentenciaSQL = accesoBase.getConexion().prepareStatement(consulta);
            data = sentenciaSQL.executeQuery(consulta);
            while (data.next()) {
                nombreCampos.add(data.getString("fechaNacimiento"));
                nombreCampos.add(data.getString("DNI"));
                nombreCampos.add(data.getString("apellidoJugador"));// ESTA LINEA me muestra lo q hay en el campo entre comillas
                nombreCampos.add(data.getString("nombreEquipo"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println("Ejercicio B");
        for(String campo:nombreCampos){
            System.out.println(campo);
        }
    }
    public Jugador jugadorMasJovenFichado2() {
        LocalDate masJoven =  LocalDate.now();
        Jugador jugadorMasJoven=new Jugador();
        for(Fichaje fichaje:historiaFichaje) {
            if(fichaje.getJugador().getFechaNacimiento().compareTo(masJoven)<0) {
                masJoven=fichaje.getJugador().getFechaNacimiento();
                jugadorMasJoven=fichaje.getJugador();
            }
        }
        return jugadorMasJoven;
    }

    public void fichajeCaidoPorPosicion(){
        String consulta = "SELECT idFichaje, Equipo_id, Jugador_id FROM Fichaje WHERE !verificarPosicion(idFichaje);";
        ArrayList<String> nombreCampos = new ArrayList<>();
        try {
            ResultSet data;
            PreparedStatement sentenciaSQL = accesoBase.getConexion().prepareStatement(consulta);
            data = sentenciaSQL.executeQuery(consulta);

            while (data.next()) {
                nombreCampos.add(data.getString("idFichaje"));
                nombreCampos.add(data.getString("Equipo_id"));
                nombreCampos.add(data.getString("Jugador_id"));
                nombreCampos.add("\n");

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println("Ejercicio C");
        for(String campo:nombreCampos){
            System.out.println(campo);
        }
    }

    public void jugadorMalRepresentado(){
        String consulta = "SELECT distinct nombreJugador, apellidoJugador FROM Jugador JOIN Fichaje ON DNI=Jugador_id WHERE !verificarManager(idFichaje);";
        ArrayList<String> nombreCampos = new ArrayList<>();
        try {
            ResultSet data;
            PreparedStatement sentenciaSQL = accesoBase.getConexion().prepareStatement(consulta);
            data = sentenciaSQL.executeQuery(consulta);

            while (data.next()) {
                nombreCampos.add(data.getString("nombreJugador"));
                nombreCampos.add(data.getString("apellidoJugador"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println("Ejercicio D");
        for(String campo:nombreCampos){
            System.out.println(campo);
        }
    }

    public void managerRepetidoEnClub(){
        String consulta = "select distinct Representante_DNI, nombreRepresentante, apellidoRepresentante from Representante_has_Equipo join Representante on Representante_DNI=dniRepresentante where 2<jugadoresManagerClub(Representante_DNI, Equipo_idEquipo);";
        ArrayList<String> nombreCampos = new ArrayList<>();
        try {
            ResultSet data;
            PreparedStatement sentenciaSQL = accesoBase.getConexion().prepareStatement(consulta);
            data = sentenciaSQL.executeQuery(consulta);

            while (data.next()) {
                nombreCampos.add(data.getString("Representante_DNI"));
                nombreCampos.add(data.getString("nombreRepresentante"));
                nombreCampos.add(data.getString("apellidoRepresentante"));
                nombreCampos.add("\n");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println("Ejercicio E");
        for(String campo:nombreCampos){
            System.out.println(campo);
        }
    }

    public HashSet<Representante> managerRepetidoEnClub2() {
        HashSet<Representante> managersRepetidos=new HashSet<>();
        HashMap<Representante, Integer> managersCompletos=new HashMap<>();
        for(Equipo equipo:listaEquipos){
            for (Map.Entry<Integer, Jugador> dorsal:equipo.getDorsales().entrySet()) {
                if(managersCompletos.containsKey(dorsal.getValue().getRepresentante())) {
                    managersCompletos.put(dorsal.getValue().getRepresentante(), managersCompletos.get(dorsal.getValue().getRepresentante()) + 1);
                }else{
                    managersCompletos.put(dorsal.getValue().getRepresentante(), 1);
                }
            }
            for (Map.Entry<Representante, Integer> representante:managersCompletos.entrySet()) {
                if(representante.getValue()>2){
                    managersRepetidos.add(representante.getKey());
                }
            }
        }
        System.out.println("Ejercicio E");
        for(Representante r: managersRepetidos){
            System.out.println(r.toString());
        }
        return managersRepetidos;
    }

    public HashMap<Posicion, Jugador> mejorPagoPorPosicion(){
        HashMap<Posicion, Jugador>hash = new HashMap<>();
        Representante managerJugador = new Representante();
        String consulta = "select distinct upper(rol),max(salario),any_value(DNI),any_value(nombreJugador),any_value(apellidoJugador),any_value(fechaNacimiento),any_value(Representante_DNI) from Jugador join Posicion on Jugador.Posicion_idPosicion = Posicion.id group by upper(rol);";
        try {
            ResultSet data;
            PreparedStatement sentenciaSQL = accesoBase.getConexion().prepareStatement(consulta);
            data = sentenciaSQL.executeQuery(consulta);

            while (data.next() == true) {
                Posicion rol = Posicion.valueOf(data.getString("upper(rol)"));
                for(Representante r : listaManager){
                    if(r.getDni() == data.getInt("any_value(Representante_DNI)")){
                        managerJugador = r;
                    }
                }
                Jugador jugador = new Jugador(data.getInt("any_value(DNI)"),data.getString("any_value(nombreJugador)"),data.getString("any_value(apellidoJugador)"),LocalDate.parse(data.getString("any_value(fechaNacimiento)")),data.getDouble("max(salario)"),managerJugador,rol);
                hash.put(rol,jugador);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return hash;
    }

    public HashMap<Posicion, Jugador> mejorPagoPorPosicion2(){
        HashMap<Posicion, Jugador>hash = new HashMap<>();
        for(Fichaje f : historiaFichaje){
            Posicion p = f.getJugador().getPosicion();
            Jugador j = f.getJugador();
            if(!hash.containsKey(p)){
                hash.put(p,j);
            }else if(hash.get(p).getSalario() < j.getSalario()){
                hash.remove(p);
                hash.put(p,j);
            }
        }
        return hash;
    }

    public void clubProhibidoMasRecurrente(){
        String consulta = "select max(cantidad), Equipo_idEquipo from (select Equipo_idEquipo, count(*) as cantidad from Representante_has_Equipo where Representante_habilitado=0 group by Equipo_idEquipo) as cualquiercosa group by Equipo_idEquipo limit 1;";
        ArrayList<String> nombreCampos = new ArrayList<>();
        try {
            ResultSet data;
            PreparedStatement sentenciaSQL = accesoBase.getConexion().prepareStatement(consulta);
            data = sentenciaSQL.executeQuery(consulta);

            while (data.next()) {
                nombreCampos.add(data.getString("max(cantidad)"));
                nombreCampos.add(data.getString("Equipo_idEquipo"));
                nombreCampos.add("\n");

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println("Ejercicio G");
        for(String campo:nombreCampos){
            System.out.println(campo);
        }

    }

    public void correccionFichaje(){
        System.out.println("EJERCICIO H");
        HashMap <Equipo, Integer> fichajesCaidosPorEquipo=new HashMap<>();
        for(Fichaje fichaje:historiaFichaje){
            if(!fichaje.isCompletado()){
//                if(fichajesCaidosPorEquipo.size()==0){
//                    fichajesCaidosPorEquipo.put(fichaje.getClub(),1);
//
//                } else{
//                    if(fichajesCaidosPorEquipo.get(fichaje.getClub()) == (null)){
//                        fichajesCaidosPorEquipo.put(fichaje.getClub(),1);
//
//                    }else{
                    fichajesCaidosPorEquipo.put(fichaje.getClub(),(fichajesCaidosPorEquipo.get(fichaje.getClub()) == null )? 1:fichajesCaidosPorEquipo.get(fichaje.getClub()) +1);
//                }}
            }
        }
        for(Map.Entry<Equipo, Integer> equipo:fichajesCaidosPorEquipo.entrySet()){
            if(equipo.getValue()>3){
                for(Fichaje fichaje:historiaFichaje){
                    if(fichaje.getClub().equals(equipo)){
                        String consulta="{call verificarFichaje("+fichaje.getIdFichaje()+", errorPosicion VARCHAR(100))};";
                        try{
                            ResultSet data;
                            PreparedStatement sentenciaSQL = accesoBase.getConexion().prepareCall(consulta);
                            data = sentenciaSQL.executeQuery(consulta);
                        }
                        catch(SQLException ex) {
                            ex.printStackTrace();
                        }

                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Sistema s1= new Sistema();
        s1.tablas = Arrays.asList("Jugador", "Posicion", "Fichaje", "Equipo", "Representante", "Representante_has_Equipo", "Equipo_has_Posicion");
        s1.accesoBase = new AccesoBaseDeDatos("PoliPases", s1.tablas);
        try {
            s1.accesoBase.conectar("alumno", "alumnoipm");
            s1.traerEquipos();//Primero Clubes
            s1.traerRepresentante();//Despues Representantes
            s1.traerFichajes();//Despues Fichajes
            s1.llenarDorsales();//Despues los Dorsales
            //Si no se pone en este Orden no funciona
            s1.jugadoresPorClubPorPosicion();
            s1.jugadorMasJovenFichado();
            s1.managerRepetidoEnClub2();
            System.out.println(s1.jugadorMasJovenFichado2().getDni());
            System.out.println(s1.jugadorMasJovenFichado2().getNombre());
            s1.fichajeCaidoPorPosicion();
            s1.jugadorMalRepresentado();
            s1.managerRepetidoEnClub();
            s1.clubProhibidoMasRecurrente();
            s1.mejorPagoPorPosicion();
            HashMap<Posicion, Jugador>hash = s1.mejorPagoPorPosicion2();
            for(Map.Entry<Posicion,Jugador>entry : hash.entrySet()){
                System.out.println("Posicion: " + entry.getKey() + ": Jugador: " + entry.getValue().getSalario());
            }
            s1.correccionFichaje();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
}

// Los metodos que tienen el nombre y un 2 al final son las versiones en java del ejercicio.
// No borramos la version vieja (hecha con sql) pq nos dijo tincho que no lo hagamos.
