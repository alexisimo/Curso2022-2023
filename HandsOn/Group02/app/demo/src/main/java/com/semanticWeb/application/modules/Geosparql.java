package com.semanticWeb.application.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.geosparql.configuration.GeoSPARQLConfig;
import org.apache.jena.geosparql.spatial.SpatialIndex;
import org.apache.jena.geosparql.spatial.SpatialIndexException;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.RDFDataMgr;

import com.semanticWeb.application.types.TransportFeature;

public class Geosparql {
    Model model;
    Dataset ds;
    
    public Geosparql(String ontologyUri, String dataUri) throws SpatialIndexException{
        model = ModelFactory.createUnion(RDFDataMgr.loadModel(ontologyUri), RDFDataMgr.loadModel(dataUri)); 
        GeoSPARQLConfig.setupMemoryIndex();
        ds = SpatialIndex.wrapModel(model);
        SpatialIndex.buildSpatialIndex(ds);
    }
    
    public List<TransportFeature> getClosestFeatures(String resource, int maxDistance){
        String queryLocal =  "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
                        "PREFIX spatial: <http://jena.apache.org/spatial#>" +
                        "PREFIX geo: <http://www.opengis.net/ont/geosparql#>" +
                        "PREFIX units: <http://www.opengis.net/def/uom/OGC/1.0/>"+ 
                        "PREFIX ont: <http://smartcity.linkeddata.es/transport/ontology/>"+ 
                        "SELECT ?uris ?name "+
                        "WHERE {"+
                        "    " + resource + " geo:hasGeometry ?geo." +
                        "    ?geo geo:asWKT ?g ." +
                        "    ?c rdfs:subClassOf* geo:Feature ." +
                        "    ?uris a ?c ." +
                        "    ?uris spatial:nearbyGeom(?g " + maxDistance + " units:meter) ." +
                        "    ?uris ont:hasAddress ?ad ." +
                        "    ?ad ont:ad_name ?adname ." +
                        "    ?ad ont:ad_number ?adnumber ." +
                        "    BIND(CONCAT(STR( ?adname ), \" \", STR( ?adnumber )) AS ?name )" +
                        "}";
        List<TransportFeature> res = new ArrayList<>();
        try (QueryExecution qe = QueryExecution.create(queryLocal, ds)) {
            ResultSet rs = qe.execSelect();
            while(rs.hasNext()){
                QuerySolution s = rs.next();
                res.add(new TransportFeature(s.get("uris").toString(), s.get("name").toString()));
            }
        }
        return res;
    }

    public List<TransportFeature> getClosestMetroStations(String resource, int maxDistance){
        String queryLocal =  "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
                        "PREFIX spatial: <http://jena.apache.org/spatial#>" +
                        "PREFIX geo: <http://www.opengis.net/ont/geosparql#>" +
                        "PREFIX units: <http://www.opengis.net/def/uom/OGC/1.0/>"+ 
                        "PREFIX ont: <http://smartcity.linkeddata.es/transport/ontology/>"+ 
                        "PREFIX wd: <http://www.wikidata.org/entity/>"+ 
                        "PREFIX wdt: <http://www.wikidata.org/prop/direct/>"+ 
                        "PREFIX p: <http://www.wikidata.org/prop/>"+ 
                        "PREFIX psv: <http://www.wikidata.org/prop/statement/value/>"+ 
                        "PREFIX wikibase: <http://wikiba.se/ontology#>"+ 
                        "SELECT ?name "+
                        "WHERE {"+
                        "    {" +
                        "    SELECT DISTINCT ?lat ?lon ?name "+
                        "    WHERE {" +
                        "           SERVICE <https://query.wikidata.org/sparql> {" +
                        "               ?QNODE wdt:P31 wd:Q928830 ." +
                        "               ?QNODE wdt:P16 wd:Q191987 ." +
                        "               ?QNODE p:P625 ?COORDS ." +
                        "               ?COORDS psv:P625 ?COORDSN ." +
                        "               ?COORDSN wikibase:geoLongitude ?lat . " +
                        "               ?COORDSN wikibase:geoLatitude ?lon . " +
                        "               ?QNODE wdt:P81 ?MLINE ." +
                        "               ?MLINE wdt:P31 wd:Q15079663." +
                        "               ?QNODE rdfs:label ?name ." +
                        "               FILTER(lang(?name) = \"es\")" +
                        "           }" +
                        "    }" +
                        "    }" +
                        "    " + resource +" spatial:nearby(?lat ?lon " + maxDistance + " units:meter) ." +
                        "}";
        List<TransportFeature> res = new ArrayList<>();
        try (QueryExecution qe = QueryExecution.create(queryLocal, ds)) {
            ResultSet rs = qe.execSelect();
            while(rs.hasNext()){
                QuerySolution s = rs.next();
                res.add(new TransportFeature("a/a/a/a/a", s.get("name").asLiteral().getString()));
            }
        }
        return res;
    }

    public void printTriples(){
        StmtIterator it =  model.listStatements();
        while(it.hasNext()){
            System.out.println(it.next());
        }
    }

    public Map<String,String> getFeaturesFromType(String resourceType){
        String querybikes =  "PREFIX esbici: <http://vocab.ciudadesabiertas.es/def/transporte/bicicleta-publica#>" +
                "PREFIX dct: <http://purl.org/dc/terms/>" +
                "SELECT DISTINCT ?feature ?name "+
                "WHERE {"+
                "    ?feature a esbici:EstacionBicicleta ."+
                "    ?feature dct:identifier ?name ." +
                "}";
        String queryev =  "PREFIX ev: <https://smart-data-models.github.io/dataModel.Transportation/>" +
                "PREFIX ont: <http://smartcity.linkeddata.es/transport/ontology/>"+ 
                "SELECT DISTINCT ?feature ?name "+
                "WHERE {"+
                "    ?feature a ev:EVChargingStation ."+
                "    ?feature ont:id ?name ." +
                "}";
        Map<String,String> res = new HashMap<>();
        try (QueryExecution qe = QueryExecution.create("bikes".equals(resourceType) ? querybikes : queryev, model)) {
            ResultSet rs = qe.execSelect();
            while(rs.hasNext()){
                QuerySolution s = rs.next();
                res.put(s.get("name").toString(),s.get("feature").toString());
            }
        }
        return res;
    }
}
