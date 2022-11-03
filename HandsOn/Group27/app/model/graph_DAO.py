from rdflib.plugins.sparql import prepareQuery
from datetime import datetime
from SPARQLWrapper import SPARQLWrapper, JSON
from settings import HELIO_SERVER

sparql = SPARQLWrapper(HELIO_SERVER)

c_building_inf = dict()
c_years = list()
c_months = dict()

def get_build_inf(build_id):
    if build_id not in c_building_inf.keys():
        get_buildings_inf()
    
    return c_building_inf[build_id]

def get_buildings_inf():

    global c_building_inf

    buildings = set()
    building_type = set()
    neighborhood = set()
    district = set()


    query = """
        PREFIX ec:<http://www.semanticweb_g27.org/ontology/EnergyCons#> 
        PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> 
        PREFIX schema: <http://schema.org/> 
        PREFIX owl: <http://www.w3.org/2002/07/owl#> 

        SELECT DISTINCT ?building_id ?building_name ?structure_type ?structure_neighborhood ?structure_district ?latitude ?longitude ?wikidata
        WHERE 
        {
            ?building_id a schema:CivicStructure ;
                rdfs:label ?building_name ;
                schema:geo ?structure_geo ;
                ec:buildingType ?structure_type ;
                ec:district ?structure_district ;
                ec:neighborhood ?structure_neighborhood .
            OPTIONAL {?building_id owl:sameAs ?wikidata .}
            ?structure_geo a schema:GeoCoordinates ;
                schema:latitude ?latitude ;
                schema:longitude ?longitude .
        }
    """

    results = exec_sparql_query(query)

    for row in results["results"]["bindings"]:

        building_name = row.get("building_name", {}).get("value")
        structure_type = row.get("structure_type", {}).get("value")
        structure_neighborhood = row.get("structure_neighborhood", {}).get("value")
        structure_district = row.get("structure_district").get("value")
        wikidata_ref = row.get("wikidata", {}).get("value")

        buildings.add(building_name)
        building_type.add(structure_type)
        neighborhood.add(structure_neighborhood)
        district.add(structure_district)
        
        # Building info cache
        c_building_inf[row["building_id"]["value"]] = {

            "location": (row.get("latitude",{}).get("value"), row.get("longitude").get("value")),
            "name":building_name,
            "type": structure_type,
            "neighborhood": structure_neighborhood,
            "district": structure_district,
            "wikidata": wikidata_ref if wikidata_ref is not None else f"https://www.wikidata.org/w/index.php?go=Go&search={row.get('building_name', {}).get('value')}"
        }

    buildings = list(buildings)
    buildings.sort()

    building_type = list(building_type)
    building_type.sort()

    neighborhood = list(neighborhood)
    neighborhood.sort()

    district = list(district)
    district.sort()

    return buildings, building_type, neighborhood, district

def get_years_months():

    global c_years, c_months
    c_years = list()
    c_months = dict()
    years = set()
    months = dict()

    query = """
        PREFIX ssn: <http://www.w3.org/2005/Incubator/ssn/ssnx/ssn#>  

        SELECT DISTINCT (year(?date) as ?year) (month(?date) as ?month)
        WHERE 
        {
            ?ob_id a ssn:Observation ;
                ssn:resultTime ?date .
        }
    """

    results = exec_sparql_query(query)
    for row in results["results"]["bindings"]:
        year, month = int(row.get("year", {}).get("value")), int(row.get("month", {}).get("value"))
        date = datetime(year, month, 1)
        years.add(date.strftime("%Y"))
        months[date.strftime("%B")] = month
    
    c_months = {str_:f"{value:02}"for str_, value in months.items()}
    years = list(years)
    c_years = years

    return years, sorted(list(months.keys()), key=lambda x: months[x])

def get_latest_observations():
    year = datetime.now().strftime("%Y") 
    return observation_lookup({"year": year,})

def observation_lookup(conditions: dict) -> list:

    latest_obs = dict()
    query = """
        PREFIX ec:<http://www.semanticweb_g27.org/ontology/EnergyCons#> 
        PREFIX ssn: <http://www.w3.org/2005/Incubator/ssn/ssnx/ssn#> 
        PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> 
        PREFIX schema: <http://schema.org/> 

        SELECT DISTINCT ?ob_id ?building_id ?ob_unit_text ?ob_value ?ob_ec_energy_group ?ob_ec_type_of_energy (year(?date) as ?year) (month(?date) as ?month)
        {
            ?ob_id a ssn:Observation ;
                ssn:resultTime ?date ;
                ssn:hasFeatureOfInterest ?building_id ;
                schema:unitText ?ob_unit_text ;
                schema:value ?ob_value .
                OPTIONAL{
                    ?ob_id ec:energyGroup ?ob_ec_energy_group ;
                        ec:typeOfEnergy ?ob_ec_type_of_energy .
                }
            ?building_id a schema:CivicStructure ;
                rdfs:label ?building_name ;
                schema:geo ?structure_geo ;
                ec:buildingType ?structure_type ;
                ec:district ?structure_district ;
                ec:neighborhood ?structure_neighborhood .
                
        """+ build_filters(conditions) +"""
        } ORDER BY DESC(?date) 
    """

    results = exec_sparql_query(query)
    
    for row in results["results"]["bindings"]:
        # Get latest observations for each building.
        building_id = row["building_id"]["value"]
        
        if building_id not in latest_obs.keys():
            group = row.get("ob_ec_energy_group", {}).get("value")
            type_ = row.get("ob_ec_type_of_energy",{}).get("value")
            latest_obs[building_id] = {
                "building": building_id,
                "year": row.get("year", {}).get("value"),
                "month": datetime(1998, int(row.get("month").get("value")), 1).strftime("%B"),
                "units": row.get("ob_unit_text", {}).get("value"),
                "value": row.get("ob_value",{}).get("value"),
                "group": group if group is not None else "Not Defined",
                "type":  type_ if type_ is not None else "Not Defined",
            }
    
    return latest_obs

def build_filters(conditions):
    
    filter_pattern = "FILTER(contains(xsd:string({}),xsd:string({}))) .\n"
    filters = []

    if len(c_months)==0:
        get_years_months()

    if "building_name" in conditions:
        filters.append(filter_pattern.format("?building_name", "\""+ conditions["building_name"] + "\""))
        
    if "building_type" in conditions:
        filters.append(filter_pattern.format("?structure_type", "\""+ conditions["building_type"] + "\""))

    if "district" in conditions:
        filters.append(filter_pattern.format("?structure_district", "\"" + conditions["district"] + "\""))

    if "neighborhood" in conditions:
        filters.append(filter_pattern.format("?structure_neighborhood", "\"" + conditions["neighborhood"] + "\""))
    
    if "year"  in conditions and "month" in conditions:
        year = conditions["year"]
        month = c_months[conditions["month"]]
        filters.append("FILTER(?date = {})".format(f"\"{year}-{month}-01\"^^xsd:date"))

    elif "year" in conditions:
        filters.append(filter_pattern.format("year(?date)", conditions["year"] ))

    elif "month" in conditions:
        filters.append("FILTER(xsd:integer(month(?date)) = xsd:integer({})).".format(c_months[conditions["month"]]))

    return "\n".join(filters)

def exec_sparql_query(query):
    sparql.setQuery(query)
    sparql.setReturnFormat(JSON)
    return sparql.query().convert()