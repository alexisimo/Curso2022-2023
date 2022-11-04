package com.example.group02;

import org.apache.jena.geosparql.spatial.SpatialIndexException;

import com.example.group02.modules.Geosparql;

public class App {
    
    private Geosparql geo;
    
    public App() throws SpatialIndexException{
        String ont = "/home/raul/Escritorio/ontology_v2.ttl";
        String data = "/home/raul/Escritorio/RDF-with-links-v2.ttl";
        geo = new Geosparql(ont, data);
    }
    
    public Geosparql getGeoSparql(){
        return geo;
    }
    public static void main(String[] args) {
        try{
            App a = new App();
            for (String res : a.getGeoSparql().getClosestFeatures("<http://smartcity.linkeddata.es/transport/data/bikeStation/178>", 1000)) {
                System.out.println(res);
            }
            //a.getGeoSparql().printTriples();
            /**GatewayServer gatewayServer = new GatewayServer(new App());
            gatewayServer.start();
            System.out.println("Gateway Server Started");*/
        }catch(Exception e){
            e.printStackTrace();
            System.err.println("Error main");
        }
    }
}
