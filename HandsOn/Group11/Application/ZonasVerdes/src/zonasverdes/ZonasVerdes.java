package zonasverdes;

import java.util.ArrayList;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

/**
 *
 * @author alvpe
 */
public class ZonasVerdes {

//    public static ArrayList<String> toList(ResultSet result, String column) {
//        ArrayList<String> aux = new ArrayList<String>();
//
//        do {
//            aux.add(result.next().get(column).toString());
//        }while(result.hasNext());
//        
//        return aux;
//        }

  
    public static ArrayList<String> getZonaVerde_Distrito(String distrito1){ //ArrayList<String> String distrito1
        Model model = ModelFactory.createDefaultModel();
        model.read("output-with-links.nt"); 
        String querystr1 = "SELECT ?nombreParque WHERE { "
				+ "?parque <https://zonas_verdes_madrid.com/ontology/ZonasVerdes#belongsDistrict> <https://zonas_verdes_madrid.com/Distrito#"+distrito1+">.\n ?parque <https://zonas_verdes_madrid.com/ontology/ZonasVerdes#Nombre_ZonaVerde> ?nombreParque.}";

        Query query = QueryFactory.create(querystr1);
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet distrito = qe.execSelect();
        ArrayList<String> getZonaVerdeD=new ArrayList<>();
        while(distrito.hasNext()){
            getZonaVerdeD.add(distrito.next().get("nombreParque").toString());
        }

        qe.close();
        return getZonaVerdeD;
    }
    
        public static ArrayList<String> getZonaVerde_Barrio(String barrio1){ //ArrayList<String>
        Model model = ModelFactory.createDefaultModel();
        model.read("output-with-links.nt"); 
        //String barrio1="CENTRO";
        String querystr1 = "SELECT ?nombreParque WHERE { "
				+ "?parque <https://zonas_verdes_madrid.com/ontology/ZonasVerdes#belongsNeighborhood> <https://zonas_verdes_madrid.com/Barrio#"+barrio1+">.\n ?parque <https://zonas_verdes_madrid.com/ontology/ZonasVerdes#Nombre_ZonaVerde> ?nombreParque.}";

        Query query = QueryFactory.create(querystr1);
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet distrito = qe.execSelect();
        ArrayList<String> getZonaVerdeB=new ArrayList<>();
        while(distrito.hasNext()){
            getZonaVerdeB.add(distrito.next().get("nombreParque").toString());//distrito.next().toString()
        }

        qe.close();
        return getZonaVerdeB;
    }
    
    public static void main(String[] args) {
        //System.out.println(getDistrito());
        //getID_Distrito();
        //System.out.println(getBarrio());
        //System.out.println(getM2());
       //System.out.println(getZonaVerde_Barrio("CORTES"));
       System.out.println(getZonaVerde_Distrito("CENTRO"));
       //getZonaVerde_Barrio();
       //getZonaVerde_Barrio("");
       //getZonaVerde_Barrio("CORTES");
    }
    
   
    
}
