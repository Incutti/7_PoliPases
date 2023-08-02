import com.sun.org.apache.xpath.internal.operations.Neg;

import java.awt.image.ImageProducer;
import java.sql.*;
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


    public void traerFichajes2(HashMap<Integer,HashMap<String, Object>>datos){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ArrayList<String>columnas = accesoBase.obtenerColumnasDeUnaTabla("Fichaje");
        ArrayList<String>columnasJugador = accesoBase.obtenerColumnasDeUnaTabla("Jugador");
        ArrayList<String>columnasPosicion = accesoBase.obtenerColumnasDeUnaTabla("Posicion");
        for(Map.Entry<Integer,HashMap<String, Object>>d : datos.entrySet()){
            Fichaje fichaje = new Fichaje();
            Jugador jugador = new Jugador();
            Representante managerJugador = new Representante();
            fichaje.setIdFichaje(d.getKey());
            for(Map.Entry<String, Object>entry : d.getValue().entrySet()){
                if (entry.getKey().equals(columnas.get(1))){
                    Integer i = (Integer) entry.getValue();
                    fichaje.setNumeroCamiseta(i);
                }
                if (entry.getKey().equals(columnas.get(2))) {
                    LocalDate l = LocalDate.parse((String) entry.getValue(),formatter); //error con el LocalDate ()
                    fichaje.setFechaHoraFichaje(l);
                }
                if (entry.getKey().equals(columnas.get(3))){
                    for (Equipo e : listaEquipos){
                        if (e.getId() == (Integer) entry.getValue()){
                            fichaje.setClub(e);
                        }
                    }
                }
                if (entry.getKey().equals("Jugador")) {
                    for(Map.Entry<String,Object>e : ((HashMap<String, Object>)entry.getValue()).entrySet()) {
                        if(e.getKey().equals(columnasJugador.get(0))){
                            int i = (int) e.getValue();
                            jugador.setDni(i);
                        }
                        if (e.getKey().equals(columnasJugador.get(1))) {
                            String s = (String) e.getValue();
                            jugador.setNombre(s);
                        }
                        if (e.getKey().equals(columnasJugador.get(2))) {
                            String s = (String) e.getValue();
                            jugador.setApellido(s);
                        }
                        if (e.getKey().equals(columnasJugador.get(3))) {
                            LocalDate l = (LocalDate) e.getValue();
                            jugador.setFechaNacimiento(l);
                        }
                        if(e.getKey().equals(columnasJugador.get(4))) {
                            float f = (float) e.getValue();
                            jugador.setSalario(f);
                        }
                        if(e.getKey().equals(columnasPosicion.get(1))) {
                            Posicion p = (Posicion) e.getValue();
                            jugador.setPosicion(p);
                        }
                        if(e.getKey().equals(columnasJugador.get(5))) {
                            for (Representante r : listaManager) {
                                if (r.getDni() == (Integer) entry.getValue()) {
                                    jugador.setRepresentante(r);
                                }
                            }
                        }
                    }
                }
            }

        fichaje.setJugador(jugador);
        fichaje.setCompletado(false);
        historiaFichaje.add(fichaje);
        }
        llenarDorsales();
    }


    public void traduccionEquipo(HashMap<Integer,HashMap<String,Object>>datos){
        ArrayList<String>columnas = accesoBase.obtenerColumnasDeUnaTabla("Equipo");
        for(Map.Entry<Integer,HashMap<String,Object>>d : datos.entrySet()){
            Equipo equipo = new Equipo();
            equipo.setId(d.getKey());
            for(Map.Entry<String, Object>entry : d.getValue().entrySet()){
                if(entry.getKey().equals(columnas.get(1))) {
                    String s = (String)entry.getValue();
                    equipo.setNombre(s);
                }
                if(entry.getKey().equals("cantPermitidaPosicion")){
                    HashMap<Posicion, Integer> hashMap = (HashMap<Posicion, Integer>)entry.getValue();
                    equipo.setCantPermitidaPosicion(hashMap);

                }
            }
            equipo.setDorsales(new HashMap<>());
            listaEquipos.add(equipo);
        }
    }

    public void llenarDorsales(){
        for(Fichaje f : historiaFichaje){
            HashMap<Integer, Jugador>dorsales = f.getClub().getDorsales();
            dorsales.put(f.getNumeroCamiseta(), f.getJugador());
            f.getClub().setDorsales(dorsales);
        }
    }


    public void traduccionRepresentante(HashMap<Integer,HashMap<String,Object>>datos){
        ArrayList<String>columnas = accesoBase.obtenerColumnasDeUnaTabla("Representante");
        ArrayList<String>columnasJugador = accesoBase.obtenerColumnasDeUnaTabla("Jugador");
        ArrayList<String>columnasPosicion = accesoBase.obtenerColumnasDeUnaTabla("Posicion");
        for(Map.Entry<Integer, HashMap<String,Object>>d : datos.entrySet()){
            Representante representante = new Representante();
            representante.setDni(d.getKey());
            for(Map.Entry<String,Object>entry : d.getValue().entrySet()){
                if(entry.getKey().equals(columnas.get(1))){
                    String s = (String) entry.getValue();
                    representante.setNombre(s);
                }
                if(entry.getKey().equals(columnas.get(2))){
                    String s = (String) entry.getValue();
                    representante.setApellido(s);
                }
                if(entry.getKey().equals(columnas.get(3))){
                    LocalDate l= (LocalDate) entry.getValue();
                    representante.setFechaNacimiento(l);
                }
                if(entry.getKey().equals("EquiposProhibido")){
                    HashSet<Equipo>equipos = new HashSet<>();
                    for (Integer i : (HashSet<Integer>) entry.getValue()){
                        for(Equipo e : listaEquipos){
                            if(e.getId() == i){
                                equipos.add(e);
                            }
                        }
                    }
                    representante.setClubesProhibidos(equipos);
                }
                if(entry.getKey().equals("EquiposContactados")){
                    HashSet<Equipo>equipos = new HashSet<>();
                    for (Integer i : (HashSet<Integer>) entry.getValue()){
                        for(Equipo e : listaEquipos){
                            if(e.getId() == i){
                                equipos.add(e);
                            }
                        }
                    }
                    representante.setClubesContactados(equipos);
                }
                if(entry.getKey().equals("Jugadores")){
                    HashSet<Jugador>jugadores = new HashSet<>();
                    for (Map.Entry<Integer, HashMap<String, Object>> hashMapEntry: ((HashMap<Integer, HashMap<String, Object>>) entry.getValue()).entrySet()){
                        Jugador jugador = new Jugador();
                        jugador.setDni(hashMapEntry.getKey());
                        for(Map.Entry<String,Object>e : hashMapEntry.getValue().entrySet()) {
                            if (e.getKey().equals(columnasJugador.get(1))) {
                                String s = (String) e.getValue();
                                jugador.setNombre(s);
                            }
                            if (e.getKey().equals(columnasJugador.get(2))) {
                                String s = (String) e.getValue();
                                jugador.setApellido(s);
                            }
                            if (e.getKey().equals(columnasJugador.get(3))) {
                                LocalDate l = (LocalDate) e.getValue();
                                jugador.setFechaNacimiento(l);
                            }
                            if(e.getKey().equals(columnasJugador.get(4))) {
                                float f = (float) e.getValue();
                                jugador.setSalario(f);
                            }
                            if(e.getKey().equals(columnasPosicion.get(1))) {
                                Posicion p = (Posicion) e.getValue();
                                jugador.setPosicion(p);
                            }
                        }
                        jugadores.add(jugador);
                    }
                    representante.setJugadoresRepresentados(jugadores);
                }
            }
            listaManager.add(representante);
        }
    }




    public Jugador jugadorMasJovenFichado2() {
        LocalDate masJoven =  LocalDate.now();
        Jugador jugadorMasJoven=new Jugador();
        for(Fichaje fichaje:historiaFichaje) {
            if(masJoven.equals(LocalDate.now()) || fichaje.getJugador().getFechaNacimiento().compareTo(masJoven)>0) {
                masJoven=fichaje.getJugador().getFechaNacimiento();
                jugadorMasJoven=fichaje.getJugador();
            }
        }
        return jugadorMasJoven;
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
        System.out.println("EJERCICIO E (version 2)");
        System.out.println("Si establecemos la regla de que un manager no puede "+'\n'+"representar a másde 2 jugadores de un mismo club, listar todos los managers que rompen dicha regla.");
        for(Representante r: managersRepetidos){
            System.out.println("Nombre: " + r.getNombre() + " Apellido: " + r.getApellido() + " Dni: " + r.getDni() + " Fecha de Nacimiento: " + r.getFechaNacimiento());
        }
        return managersRepetidos;
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
        System.out.println("EJERCICIO F (version 2)");
        System.out.println("Los jugadores mejores pagos por posición en el sistema de fichajes.");
        return hash;
    }



    public void correccionFichaje2(){
        System.out.println("EJERCICIO H");
        System.out.println("Debido  a  una  penalidad  que  pone  la  asociación  a  los  clubes  que tienen más de 3 fichajes rechazados "+'\n'+"(esto nos está sucediendo con 2  clubes)  se  pide modificar  los  fichajes  necesarios  para  que  se consideren aceptados los clubes y así eviten la sanción.");
        HashMap <Equipo, HashSet<Fichaje>> fichajesCaidosPorEquipo=new HashMap<>();
        for(Fichaje fichaje:historiaFichaje){
            if(!fichaje.isCompletado()){
                if(fichajesCaidosPorEquipo.containsKey(fichaje.getClub())){
                    fichajesCaidosPorEquipo.get(fichaje.getClub()).add(fichaje);
                }else {
                    HashSet<Fichaje>fs = new HashSet<>();
                    fs.add(fichaje);
                    fichajesCaidosPorEquipo.put(fichaje.getClub(), fs);
                }
            }
        }
        for(Map.Entry<Equipo, HashSet<Fichaje>> equipo:fichajesCaidosPorEquipo.entrySet()){
            if(equipo.getValue().size()>3){
                for(Fichaje fichaje:equipo.getValue()){
                    if(fichaje.getClub().equals(equipo.getKey()) && !fichaje.isCompletado()){
                        this.getAccesoBase().verificarFichaje(fichaje.getIdFichaje());
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
            s1.traduccionEquipo(s1.getAccesoBase().traerEquipos());//Primero Clubes  // cambie nombre de traerEquipo2 a traduccionEquipo pq asi es mas declarativo
            s1.traduccionRepresentante(s1.getAccesoBase().traerRepresentante());//Despues Representantes // cambie nombre de traereRepresentante2 a traduccionRepresentante pq asi es mas declarativo
            s1.traerFichajes2(s1.getAccesoBase().traerFichajes());//Despues Fichajes
            s1.llenarDorsales();//Despues los Dorsales
            //Si no se pone en este Orden no funciona

            System.out.println();

            ArrayList<String> nombreCampos = s1.getAccesoBase().jugadoresPorClubPorPosicion();
            System.out.println("EJERCICIO A");
            System.out.println("Listado de jugadores por posición por club.");
            for(Posicion p:Posicion.values()) {
                if(nombreCampos.contains(p.name())) {
                    System.out.println(p);
                    for(int i = 0; i<nombreCampos.size(); i++){
                        if(nombreCampos.get(i).equals(p.name())) {
                            i++;
                            System.out.println(nombreCampos.get(i));
                            i++;
                            System.out.println(nombreCampos.get(i));
                            i++;
                            System.out.println(nombreCampos.get(i));
                            System.out.println();
                        }
                    }
                }
            }

            System.out.println();
            System.out.println();

            System.out.println("EJERCICIO B (version 2)");
            System.out.println("Jugador más joven en ser fichado por un club.");
            System.out.println("DNI: " + s1.jugadorMasJovenFichado2().getDni());
            System.out.println("Nombre: " + s1.jugadorMasJovenFichado2().getNombre());
            System.out.println("Fecha de Nacimiento: " + s1.jugadorMasJovenFichado2().getFechaNacimiento());

            System.out.println();
            System.out.println();

            nombreCampos=s1.getAccesoBase().fichajeCaidoPorPosicion();
            System.out.println("EJERCICIO C");
            System.out.println("Fichajes en  los  cuales el  club  no  aceptaba más jugadores  en  esa posición.");
            for (int i=0; i< nombreCampos.size(); i+=3){
                System.out.println("Id de Fichaje: " + nombreCampos.get(i) + " | Id de Equipo " + nombreCampos.get(i+1) + " | Id de Jugador: " + nombreCampos.get(i+2));
            }

            System.out.println();
            System.out.println();

            nombreCampos=s1.accesoBase.jugadorMalRepresentado();
            System.out.println("EJERCICIO D");
            System.out.println("Jugadores representados por managers que no corresponde.");
            for (int i=0; i< nombreCampos.size(); i+=2){
                System.out.println(nombreCampos.get(i) + " " + nombreCampos.get(i+1));
            }

            System.out.println();
            s1.managerRepetidoEnClub2(); // EJERCICIO E

            System.out.println();
            System.out.println();


            HashMap<Posicion, Jugador>hash2 = s1.mejorPagoPorPosicion2(); // EJERCICIO F
            for(Map.Entry<Posicion,Jugador>entry : hash2.entrySet()){
                System.out.println("Posicion: " + entry.getKey() + " Apellido del Jugador: " + entry.getValue().getApellido() + " Salario: " + entry.getValue().getSalario() + "\n");
            }

            System.out.println();
            System.out.println();

            nombreCampos=s1.accesoBase.clubProhibidoMasRecurrente();
            System.out.println("EJERCICIO G");
            System.out.println("Equipo  de  futbol  que  acepta  la  menor  cantidad  de  managers  para sus jugadores.");
            for(String campo:nombreCampos){
                System.out.println(campo);
            }

            System.out.println();

            s1.correccionFichaje2();

            //Update en java de lo cambiado en correccionFichaje2()

            s1.historiaFichaje = new HashSet<>();
            s1.listaEquipos = new HashSet<>();
            s1.listaManager = new HashSet<>();

            s1.getAccesoBase().traerEquipos();
//            s1.traerFichajes();
            s1.llenarDorsales();

        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
}


