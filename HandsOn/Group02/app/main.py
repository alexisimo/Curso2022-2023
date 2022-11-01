from time import sleep
import plotly.express as px
from rdflib import Graph, Namespace, Literal, URIRef
from rdflib.namespace import RDF, RDFS
from rdflib.plugins.sparql import prepareQuery
mapbox_access_token =open("app/.mapbox_token").read()

g = Graph()

base = Namespace("http://smartcity.linkeddata.es/transport/")
dc = Namespace("http://purl.org/dc/elements/1.1/")
esbici = Namespace("http://vocab.ciudadesabiertas.es/def/transporte/bicicleta-publica#")
ev = Namespace("https://smart-data-models.github.io/dataModel.Transportation/")
geosparql = Namespace("http://www.opengis.net/ont/geosparql#")
ont = Namespace("http://smartcity.linkeddata.es/transport/ontology/")
owl = Namespace("http://www.w3.org/2002/07/owl#")
rdfs = Namespace("http://www.w3.org/2000/01/rdf-schema#")
xsd = Namespace("http://www.w3.org/2001/XMLSchema#")
dct = Namespace("http://purl.org/dc/terms/")
wdt = Namespace("http://www.wikidata.org/prop/")

"""
g.namespace_manager.bind('base', Namespace("http://smartcity.linkeddata.es/transport/"), override=False)
g.namespace_manager.bind('dc', Namespace("http://purl.org/dc/elements/1.1/"), override=False)
g.namespace_manager.bind('esbici', Namespace("http://vocab.ciudadesabiertas.es/def/transporte/bicicleta-publica#"), override=False)
g.namespace_manager.bind('ev', Namespace("https://smart-data-models.github.io/dataModel.Transportation/EVChargingStation/model.yaml#/"), override=False)
g.namespace_manager.bind('geosparql', Namespace("http://www.opengis.net/ont/geosparql#"), override=False)
g.namespace_manager.bind('ont', Namespace("http://smartcity.linkeddata.es/transport/ontology/"), override=False)
g.namespace_manager.bind('owl', Namespace("http://www.w3.org/2002/07/owl#"), override=False)
g.namespace_manager.bind('rdfs', Namespace("http://www.w3.org/2000/01/rdf-schema#"), override=False)
g.namespace_manager.bind('xsd', Namespace("http://www.w3.org/2001/XMLSchema#"), override=False)
g.namespace_manager.bind('dct', Namespace("http://purl.org/dc/terms/"), override=False)
"""

#For local deployment
g.parse("ontology/ontology_v2.ttl", format="ttl")
g.parse("rdf/RDF-with-links-v2.ttl", format="ttl")


default = prepareQuery('''
    SELECT ?sn ?c ?cap ?cp 
    WHERE {
        ?c rdfs:subClassOf* geosparql:Feature .
        ?s a ?c .
        OPTIONAL{
            ?s dct:identifier ?sn .
            ?s ont:capacity ?cap } .
        OPTIONAL{
            ?s ont:id ?sn .
            ?s ev:capacity ?cap } .
        ?s geosparql:hasGeometry ?g .
        ?g geosparql:asWKT ?cp
    }
    ''',
    initNs={"geosparql": geosparql, "dct": dct, "rdfs": rdfs, "ont": ont, "ev": ev}
)

districts = prepareQuery('''
    SELECT ?district ?name
    WHERE {
        ?district a ont:District .
        ?district ont:name_id ?name
    }
    ''',
    initNs={"ont": ont}
)

neighbourhoods = prepareQuery('''
    SELECT ?neigh ?name
    WHERE {
        ?neigh a ont:Neighbourhood .
        ?neigh ont:hasDistrict ?district .
        ?neigh ont:name_id ?name
    }
    ''',
    initNs={"ont": ont}
)

operators = prepareQuery('''
    SELECT ?op ?name ?logo
    WHERE {
        ?op a ont:Operator .
        ?op ont:name_id ?name .
        OPTIONAL{
            ?op owl:sameAs ?qnode .
            SERVICE <https://query.wikidata.org/sparql> {
                ?qnode wdt:P154 ?logo
            }
        }
    }
    ''',
    initNs={"ont": ont, "owl": owl, "wdt": wdt}
)

for results in g.query(neighbourhoods, initBindings={'district': URIRef("http://smartcity.linkeddata.es/transport/data/district/14")}):
    print(results.neigh, results.name)
    
#Parse data to list
points = []
for results in g.query(default):
    id = str(results.sn)
    
    #type split to get only the last part of the URI
    type = str(results.c).split('/')[-1]
    if type[0] == 'b' :
        type = type.split('#')[1]
    
    point = results.cp
    point = point.replace("POINT(", "")
    point = point.replace(")", "")
    point = point.split(" ")
    point = [float(i) for i in point]
    
    capacity = str(results.cap)
    
    if(type == "EstacionBicicleta"):
        color = "Bike Station"
    elif(type == "EVChargingStation"):
        color = "Charging Station"
    else :
        color = "Others"
    # Merge point and type
    points.append([type +": "+ id] + point + [capacity] + [color])
  
#PRint points
#for point in points:
#    print(point)  
    
#Points to dict
points_dict = {
    "name": [i[0] for i in points],
    "Latitude": [i[1] for i in points],
    "Longitude": [i[2] for i in points],
    "Capacity": [i[3] for i in points],
    "Type": [i[4] for i in points]
}

fig = px.scatter_mapbox(points_dict, 
                        lat="Latitude", 
                        lon="Longitude", 
                        hover_name="name", 
                        hover_data=["Capacity"],
                        color = "Type",
                        height=800
                        )

fig.update_layout(
    mapbox=dict(
        center=dict(lat=40.41831, lon= -3.70275),
        accesstoken=mapbox_access_token,
        zoom=12,
    ),
    mapbox_style="mapbox://styles/rcn41/cl9y1jy8s003g14plahpakd40"
)


import pandas as pd
import dash
from dash import dcc, html, Output, Input

#Create dic from districts
district = []
for results in g.query(districts):
    district.append([results.name])
    
district_dict = {
    "district" : [i[0] for i in district]
}

#Create dic from neighbourhoods
neighbourhood = []
for results in g.query(neighbourhoods):
    neighbourhood.append([results.name.title()])
    
neighbourhood_dict = {
    "neighbourhood" : [i[0] for i in neighbourhood]
}
    

disf = pd.DataFrame(district_dict)
neighf = pd.DataFrame(neighbourhood_dict)

app = dash.Dash()
app.layout = html.Div([
    html.Div([
    dcc.Dropdown(
        disf['district'].unique(),
        'Districts',
        id='district-dropdown'
        ),],
    style={'width': '49%', 'display': 'inline-block'}),
    html.Div([
    dcc.Dropdown(
        neighf['neighbourhood'].unique(),
        'Neighbourhoods',
        id='neighbourhood-dropdown'
        ),],
    style={'width': '49%', 'float': 'right', 'display': 'inline-block'}),
    
    dcc.Graph(
        id='MapPlot',
        figure=fig
    )
])

app.run_server() 