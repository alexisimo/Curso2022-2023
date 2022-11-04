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
wdt_metro = Namespace("http://www.wikidata.org/prop/direct/")
wd = Namespace("http://www.wikidata.org/entity/")

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

selectedFeatures = prepareQuery('''
    SELECT ?sn ?c ?cap ?cp 
    WHERE {
        ?c rdfs:subClassOf* geosparql:Feature .
        ?s a ?c .
        ?s ont:hasNeighbourhood ?neigh .
        ?neigh ont:hasDistrict ?district .
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

metro = prepareQuery('''
    SELECT ?QNODE ?NAME (GROUP_CONCAT(?MLINENUMBER ; separator="|") as ?MLINENUMBERS) ?COORDS 
    WHERE{
        SERVICE <https://query.wikidata.org/sparql> {
            ?QNODE wdt:P31 wd:Q928830 .
            ?QNODE wdt:P16 wd:Q191987 .
            ?QNODE wdt:P625 ?COORDS .
            ?QNODE wdt:P81 ?MLINE .
            ?MLINE wdt:P31 wd:Q15079663.
            ?MLINE wdt:P1671 ?MLINENUMBER .
            ?QNODE rdfs:label ?NAME .
            FILTER(lang(?NAME) = "es")
        }
    }GROUP BY ?QNODE ?NAME ?COORDS LIMIT 1000
    ''',
    initNs={"wdt": wdt_metro, "wd":wd, "rdfs": rdfs}
)
    
#Parse data to list
points = []
for results in g.query(default):
    id = str(results.sn)
    
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

#Parse metro data to points
for results in g.query(metro):
    id = str(results.MLINENUMBERS)
    
    type = str(results.NAME)
    
    point = results.COORDS
    point = point = point.replace("Point(", "")
    point = point.replace(")", "")
    point = point.split(" ")
    point = [float(point[1]), float(point[0])]
    
    points.append([type +": "+ id] + point + ["None"] + ["Metro"])
    
    
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
    district.append([str(results.name)]+ [str(results.district)])
    
district_dict = {
    "district" : [i[0] for i in district],
    "uri" : [i[1] for i in district]
}
    
disf = pd.DataFrame(district_dict)

#Create dic from neighbourhoods
neighbourhood = []
for results in g.query(neighbourhoods):
    neighbourhood.append([str(results.name.title())]+ [str(results.neigh)])
    
neighbourhood_dict = {
    "neighbourhood" : [i[0] for i in neighbourhood],
    "uri" : [i[1] for i in neighbourhood]
}

neighf = pd.DataFrame(neighbourhood_dict)

#-----APP-----
app = dash.Dash()
app.layout = html.Div([
    html.Div([
        dcc.Link(
        html.Button('GeoSparql', id='button'),
        href='https://www.youtube.com/watch?v=HEtKZH38jV8', refresh=True),],
    style={'display': 'inline-block'}),
    html.Div([
    dcc.Dropdown(
        disf['district'].unique(),
        '',
        id='district-dropdown'
        ),],
    style={'width': '46%', 'float': 'right', 'display': 'center'}),
    html.Div([
    dcc.Dropdown(
        [],
        'Neighbourhoods',
        placeholder="Select a district",
        id='neighbourhood-dropdown'
        ),],
    style={'width': '48%', 'float': 'right', 'display': 'inline-block'}),
    html.Div([
    dcc.Graph(
        id='MapPlot',
        figure=fig
    ),],
    style={'width': '100%', 'float': 'right', 'display': 'inline-block'})
])

@app.callback(
    Output('neighbourhood-dropdown', 'options'),
    Output('MapPlot', 'figure'),
    Input('district-dropdown', 'value'),
    Input('neighbourhood-dropdown', 'value'))
def display_district(district_value, neighbourhood_value):    
    if(district_value == None):
        return [], fig
    
    if(len(district_value) < 1):
        return [], fig
    
    uri_name= disf[disf["district"] == district_value]['uri'].values[0]
    
    neighbourhood_dis= []
    for results in g.query(neighbourhoods, initBindings={'district': URIRef(uri_name)}):
        neighbourhood_dis.append([str(results.name.title())] + [str(results.neigh)])

    neighbourhood_dict_dis = {
        "neighbourhood" : [i[0] for i in neighbourhood_dis],
        "uri" : [i[1] for i in neighbourhood_dis]
    }
    
    neigh_dis = pd.DataFrame(neighbourhood_dict_dis)
        
    dis_points = []
    uri_type = 'district'
    
    if(neighbourhood_value != "Neighbourhoods" and neighbourhood_value is not None and neigh_dis[neigh_dis["neighbourhood"] == neighbourhood_value]['uri'].values.size > 0):
        uri_name= neigh_dis[neigh_dis["neighbourhood"] == neighbourhood_value]['uri'].values[0]
        uri_type = 'neigh'
        
    for results in g.query(selectedFeatures, initBindings={uri_type: URIRef(uri_name)}):
        id_dis = str(results.sn)
        
        type_dis = str(results.c).split('/')[-1]
        if type_dis[0] == 'b' :
            type_dis = type_dis.split('#')[1]
        
        point = results.cp
        point = point.replace("POINT(", "")
        point = point.replace(")", "")
        point = point.split(" ")
        point = [float(i) for i in point]
        
        capacity = str(results.cap)
        
        if(type_dis == "EstacionBicicleta"):
            color = "Bike Station"
        elif(type_dis == "EVChargingStation"):
            color = "Charging Station"
        else :
            color = "Others"
        # Merge point and type
        dis_points.append([type_dis +": "+ id_dis] + point + [capacity] + [color])
    
    points_dict_dis = {
        "name": [i[0] for i in dis_points],
        "Latitude": [i[1] for i in dis_points],
        "Longitude": [i[2] for i in dis_points],
        "Capacity": [i[3] for i in dis_points],
        "Type": [i[4] for i in dis_points]
    }

    fig_d = px.scatter_mapbox(points_dict_dis, 
                            lat="Latitude", 
                            lon="Longitude", 
                            hover_name="name", 
                            hover_data=["Capacity"],
                            color = "Type",
                            height=800
                            )

    fig_d.update_layout(
        mapbox=dict(
            center=dict(lat=40.41831, lon= -3.70275),
            accesstoken=mapbox_access_token,
            zoom=12,
        ),
        mapbox_style="mapbox://styles/rcn41/cl9y1jy8s003g14plahpakd40"
    )
    
    return neigh_dis['neighbourhood'].unique(), fig_d

app.run_server()