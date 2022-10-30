package Clases;

import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;
import querys.DatosWiki;
import java.io.IOException;

public class Bibliotecas {
    private String pk;
    private String nombre;
    private String Descripcion;
    private String Horario;
    private String Telefono;
    private String Direccion;
    private String Url;
    private String nivelDeMar;

    public Bibliotecas() throws IOException, MediaWikiApiErrorException {
    }

    public String getPk() {
        return this.pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.Descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.Descripcion = descripcion;
    }

    public String getHorario() {
        return this.Horario;
    }

    public void setHorario(String horario) {
        this.Horario = horario;
    }

    public String getTelefono() {
        return this.Telefono;
    }

    public void setTelefono(String telefono) {
        this.Telefono = telefono;
    }

    public String getDireccion() {
        return this.Direccion;
    }

    public void setDireccion(String direccion) {
        this.Direccion = direccion;
    }

    public String getUrl() {
        return this.Url;
    }

    public void setUrl(String url) {
        this.Url = url;
    }
    public String getNivelDeMar() {
        return this.nivelDeMar;
    }
    public void setNivelDeMar(String nivelDeMar) {
        this.nivelDeMar = nivelDeMar;
    }

}
