public class Posicion {
    private String rol;

    public Posicion(String rol) {
        this.rol = rol;
    }
    public Posicion(){rol = "delantero";}

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
