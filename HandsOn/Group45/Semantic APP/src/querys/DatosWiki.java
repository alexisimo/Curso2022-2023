package querys;

import Clases.Bibliotecas;
import Clases.Eventos;
import org.wikidata.wdtk.datamodel.helpers.Datamodel;
import org.wikidata.wdtk.datamodel.interfaces.EntityDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.Statement;
import org.wikidata.wdtk.examples.ExampleHelpers;
import org.wikidata.wdtk.wikibaseapi.ApiConnection;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import static org.wikidata.wdtk.examples.FetchOnlineDataExample.printDocumentation;

public class DatosWiki {

    public DatosWiki() {
    }

    public static ArrayList<Bibliotecas> DatosMadrid(ArrayList<Bibliotecas> list) throws IOException, MediaWikiApiErrorException {
        ExampleHelpers.configureLogging();
        printDocumentation();

        WikibaseDataFetcher wbdf = new WikibaseDataFetcher(
                ApiConnection.getWikidataApiConnection(),
                Datamodel.SITE_WIKIDATA);
        wbdf.getFilter().setLanguageFilter(Collections.singleton("es"));
        //---------------
        EntityDocument madrid = wbdf.getEntityDocument("Q2807");
        Statement stat=  ((ItemDocument) madrid).findStatement("P2044");
        String respuesta= stat.getClaim().getValue().toString().split(" ")[0];
        list.forEach(n->n.setNivelDeMar(respuesta));
        return list;
    }
    public static ArrayList<Eventos> DatosEventos(ArrayList<Eventos> eventos) throws IOException, MediaWikiApiErrorException {
        if (eventos.size()==0)
            return eventos;
        ExampleHelpers.configureLogging();
        printDocumentation();

        WikibaseDataFetcher wbdf = new WikibaseDataFetcher(
                ApiConnection.getWikidataApiConnection(),
                Datamodel.SITE_WIKIDATA);
        wbdf.getFilter().setLanguageFilter(Collections.singleton("es"));
        //---------------
        String id=eventos.get(0).getWikidataEvento();
        EntityDocument madrid = wbdf.getEntityDocument(id);
        Statement Calle=  ((ItemDocument) madrid).findStatement("P6375");
        Statement Coordenadas=  ((ItemDocument) madrid).findStatement("P625");
        String calle= Calle.getClaim().getValue().toString().split("\"")[1];
        String coord=Coordenadas.getClaim().getValue().toString().split("\\(")[0];

        for (int i=0;i<eventos.size();i++){
            eventos.get(i).setWikiCoor(coord);
            eventos.get(i).setWikiStreet(calle);
        }
        return eventos;
    }
    public static void main(String[] args) throws IOException, MediaWikiApiErrorException {

    }
}

