import os
from rdflib.plugins.sparql import prepareQuery
from SPARQLWrapper import SPARQLWrapper, JSON

HELIO = "http://localhost:9000/sparql"
wikidata = "https://query.wikidata.org/"
sparql = SPARQLWrapper(HELIO)

cs_data = {}
    
def getChargingStationsData():
    
    global cs_data
    global cn_data
    
    owners = set()
    connectorsType = set()
    bookables = set()
    
    query = """
        PREFIX ns:<http://HO-G1.linkeddata.es/ontology/Chargingstation#> 
        PREFIX schema: <http://schema.org/>
        PREFIX uiote:<http://www.w3id.org/urban-iot/uiote#>
        PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
        PREFIX owl: <http://www.w3.org/2002/07/owl#>
        PREFIX mv: <http://schema.mobivoc.org/#>

        SELECT DISTINCT ?cs ?owner ?bookable ?city ?wikidata ?address ?lat ?long ?cn ?connectorType ?numberOfConnectors
        WHERE 
        {
            ?cs a uiote:ChargingStation ;
                ns:owner ?owner ;
                schema:City ?city ;
                schema:address ?address .
            ?gc a schema:GeoCoordinates ;
                schema:latitude ?lat ;
                schema:longitude ?long .
            
            OPTIONAL{?cs ns:bookable ?bookable .}
            OPTIONAL{?cs owl:sameAs ?wikidata .}
            OPTIONAL{?cn ns:numberOfConnectors ?numberOfConnectors .}
            OPTIONAL{?cn mv:plugType ?connectorType .}
            ?cs ns:hasConnector ?cn .
            ?cs ns:hasGeocoordinates ?gc .
            ?cn a uiote:Connector .
        }
    """
    
    sparql.setQuery(query)
    sparql.setReturnFormat(JSON)
    res = sparql.query().convert()
    
    for result in res["results"]["bindings"]:
        
        cs = result.get("cs", {}).get("value")
        owner = result.get("owner", {}).get("value")
        bookable = result.get("bookable", {}).get("value")
        city = result.get("city", {}).get("value")
        address = result.get("address", {}).get("value")
        connectorType = result.get("connectorType", {}).get("value")
        numberOfConnectors = result.get("numberOfConnectors", {}).get("value")
        latitude = result.get("lat", {}).get("value")
        longitude = result.get("long", {}).get("value")
        wikidata = result.get("wikidata", {}).get("value")

        owners.add(owner)
        connectorsType.add(connectorType)
        bookables.add(bookable)
        
        cs_data[result["cs"]["value"]] = {
            "owner": owner,
            "bookable": bookable,
            "address": address,
            "city": city,
            "latitude": latitude,
            "longitude": longitude,
            "connectorType": connectorType,
            "numberOfConnectors": numberOfConnectors,
            "wikidata": wikidata
        }
        
        
    # then we sort them in alphabetical order
    
    owners = list(owners)
    owners.sort()
    connectorsType =  list(connectorsType)
    connectorsType = [i for i in connectorsType if i is not None]
    connectorsType.sort()
    
    bookables = list(bookables)
    bookables = [i for i in bookables if i is not None]
    bookables.sort()
    
    # print(cs_data)
    

    return owners, connectorsType, bookables

def filterQuery(filterVariables):
    global cs_data
    
    query = """
        PREFIX ns:<http://HO-G1.linkeddata.es/ontology/Chargingstation#> 
        PREFIX schema: <http://schema.org/>
        PREFIX uiote:<http://www.w3id.org/urban-iot/uiote#>
        PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
        PREFIX owl: <http://www.w3.org/2002/07/owl#>
        PREFIX mv: <http://schema.mobivoc.org/#>

        SELECT DISTINCT ?cs ?owner ?bookable ?city ?wikidata ?address ?lat ?long ?cn ?connectorType ?numberOfConnectors
        WHERE 
        {
            ?cs a uiote:ChargingStation ;
                ns:owner ?owner ;
                schema:City ?city ;
                schema:address ?address .
            ?gc a schema:GeoCoordinates ;
                schema:latitude ?lat ;
                schema:longitude ?long .
            
            OPTIONAL{?cs ns:bookable ?bookable .}
            OPTIONAL{?cs owl:sameAs ?wikidata .}
            OPTIONAL{?cn ns:numberOfConnectors ?numberOfConnectors .}
            OPTIONAL{?cn mv:plugType ?connectorType .}
            ?cs ns:hasConnector ?cn .
            ?cs ns:hasGeocoordinates ?gc .
            ?cn a uiote:Connector .
    """
    print(filterVariables)
    
    if "owner" in filterVariables and filterVariables["owner"] != "Todos":
        query += "\nFILTER (contains(?owner, \"" + filterVariables["owner"] + "\"))"
    if "bookable" in filterVariables and filterVariables["bookable"] != "Todos":
        query += "\nFILTER (contains(?bookable, \"" + filterVariables["bookable"] + "\"))"
    if "connectorType" in filterVariables and filterVariables["connectorType"] != "Todos":
        query += "\nFILTER (contains(?connectorType, \"" + filterVariables["connectorType"] + "\"))"
    if "address" in filterVariables and filterVariables["address"] != "":
        query += "\nFILTER (contains(?address, \"" + filterVariables["address"] + "\"))"
        
    query += "\n}"
    print("query: " + query)
    
    sparql.setQuery(query)
    sparql.setReturnFormat(JSON)
    res = sparql.query().convert()
    cs_data = {}
    print("cs_data antes de introducir post values: " + str(cs_data))
    for result in res["results"]["bindings"]:
        
        cs = result.get("cs", {}).get("value")
        owner = result.get("owner", {}).get("value")
        bookable = result.get("bookable", {}).get("value")
        city = result.get("city", {}).get("value")
        address = result.get("address", {}).get("value")
        connectorType = result.get("connectorType", {}).get("value")
        numberOfConnectors = result.get("numberOfConnectors", {}).get("value")
        latitude = result.get("lat", {}).get("value")
        longitude = result.get("long", {}).get("value")
        wikidata = result.get("wikidata", {}).get("value")
        
        
        cs_data[result["cs"]["value"]] = {
            "owner": owner,
            "bookable": bookable,
            "address": address,
            "city": city,
            "latitude": latitude,
            "longitude": longitude,
            "connectorType": connectorType,
            "numberOfConnectors": numberOfConnectors,
            "wikidata": wikidata
        }
        
    print("cs_data despues de introducir post values: " + str(cs_data))
    
    return