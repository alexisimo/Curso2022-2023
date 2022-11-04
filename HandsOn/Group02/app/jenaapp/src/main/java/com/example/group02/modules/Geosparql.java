package com.example.group02.modules;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.geosparql.configuration.GeoSPARQLConfig;
import org.apache.jena.geosparql.spatial.SpatialIndex;
import org.apache.jena.geosparql.spatial.SpatialIndexException;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.RDFDataMgr;


public class Geosparql {
    Model model;
    Dataset ds;
    
    public Geosparql(String ontologyUri, String dataUri) throws SpatialIndexException{
        model = ModelFactory.createUnion(RDFDataMgr.loadModel(ontologyUri), RDFDataMgr.loadModel(dataUri)); 
        GeoSPARQLConfig.setupMemoryIndex();
        ds = SpatialIndex.wrapModel(model);
        SpatialIndex.buildSpatialIndex(ds);
    }
    
    public List<String> getClosestFeatures(String resource, int maxDistance){
        String queryLocal =  "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
                        "PREFIX spatial: <http://jena.apache.org/spatial#>" +
                        "PREFIX geo: <http://www.opengis.net/ont/geosparql#>" +
                        "PREFIX units: <http://www.opengis.net/def/uom/OGC/1.0/>"+ 
                        "SELECT ?uris "+
                        "WHERE {"+
                        "    " + resource + " geo:hasGeometry ?geo." +
                        "    ?geo geo:asWKT ?g ." +
                        "    ?c rdfs:subClassOf* geo:Feature ." +
                        "    ?uris a ?c ." +
                        "    ?uris spatial:nearbyGeom(?g " + maxDistance + " units:meter 10)" +
                        "}";
        List<String> res = new ArrayList<>();
        try (QueryExecution qe = QueryExecution.create(queryLocal, ds)) {
            ResultSet rs = qe.execSelect();
            while(rs.hasNext()){
                QuerySolution s = rs.next();
                res.add(s.get("uris").toString());
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
}
