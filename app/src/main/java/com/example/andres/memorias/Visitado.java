package com.example.andres.memorias;

/**
 * Created by Andres on 13/07/2016.
 */
public class Visitado {
    private String link;
    private String propietario;
    private String defuncion;

    public Visitado(String link,String propietario,String defuncion){
        this.setLink(link);
        this.setPropietario(propietario);
        this.setDefuncion(defuncion);
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }

    public String getDefuncion() {
        return defuncion;
    }

    public void setDefuncion(String defuncion) {
        this.defuncion = defuncion;
    }

    @Override
    public String toString() {
        return propietario+","+defuncion;
    }
}
