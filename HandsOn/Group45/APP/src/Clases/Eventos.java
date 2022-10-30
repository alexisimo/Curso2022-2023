package Clases;

public class Eventos {
    private String NombreEvento;
    private String FechaInicio;
    private String FechaFin;
    private String HoraEmpiezo;
    private String NombreIntalacion;
    private String WikidataEvento;
    private String WikiStreet;
    private String WikiCoor;

    public String getWikiStreet() {
        return WikiStreet;
    }

    public void setWikiStreet(String wikiStreet) {
        WikiStreet = wikiStreet;
    }

    public String getWikiCoor() {
        return WikiCoor;
    }

    public void setWikiCoor(String wikiCoor) {
        WikiCoor = wikiCoor;
    }

    public String getWikidataEvento() {
        return this.WikidataEvento;
    }

    public void setWikidataEvento(String wikidataEvento) {
        this.WikidataEvento = wikidataEvento;
    }

    public String getNombreEvento() {
        return this.NombreEvento;
    }

    public void setNombreEvento(String nombreEvento) {
        this.NombreEvento = nombreEvento;
    }
    public String getFechaInicio() {
        return this.FechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.FechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return this.FechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.FechaFin = fechaFin;
    }

    public String getHoraEmpiezo() {
        return this.HoraEmpiezo;
    }

    public void setHoraEmpiezo(String horaEmpiezo) {
        this.HoraEmpiezo = horaEmpiezo;
    }

    public String getNombreIntalacion() {
        return this.NombreIntalacion;
    }

    public void setNombreIntalacion(String nombreIntalacion) {
        this.NombreIntalacion = nombreIntalacion;
    }

    public Eventos() {
    }
}
