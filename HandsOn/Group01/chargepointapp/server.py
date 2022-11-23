from app_scripts import obtain_inf
from flask import Flask, render_template, request, redirect
import folium
import flask
import re

app=Flask(__name__)

startingCoordinates = (40.54746, -3.64197) # Alcobendas coordinates for starting map

with open('.env') as f:
    encontrado=False
    while not encontrado:
        line=f.readline()
        listaVariables=re.split("^FLASK_RUN_PORT=*", line)
        if len(listaVariables)>1:
            port=listaVariables[1]
            encontrado=True

@app.route('/',methods=['GET', 'POST'])
def index():
    
    owners, connectorsType, bookables = obtain_inf.getChargingStationsData()
    
    address = ""
    owner = "Todos"
    connectorType = "Todos"
    bookable = "Todos"

    if request.method=='GET':
        
        folium_map = folium.Map(location=startingCoordinates, zoom_start=14)
        
        for cs in obtain_inf.cs_data:
            csAux = obtain_inf.cs_data[cs]
            if (csAux["bookable"] is None):
                csAux["bookable"] = "No hay información"
            if (csAux["connectorType"] is None):
                csAux["connectorType"] = "No hay información"
            if (csAux["wikidata"] is None):
                csAux["wikidata"] = "-"
            
            iframe = folium.IFrame("<div class='map-popup' style='font-family:IBM Plex Sans'><center><h4 class='titulo'><b>Charging Station</b></h4></center>" + "<b>Dirección: </b>" + csAux["address"] + ", " + csAux["city"] + "<br>" + "<b>Operador: </b>" + csAux["owner"] + "<br>" + "<b>Reservable: </b>" + csAux["bookable"] +  "<br>" + "<b>Conectores: </b>" + csAux["numberOfConnectors"] + "<br>" + "<b>Tipo de conector: </b>" + csAux["connectorType"] + "<br><a target='_blank' href='" + csAux['wikidata'] + "'>Ver más</a>" + "</div>", width=300, height=200)
            popup = folium.Popup(iframe)
            folium.Marker(location=[obtain_inf.cs_data[cs]["latitude"], obtain_inf.cs_data[cs]["longitude"]], icon=folium.Icon(color="red", icon='plug', prefix='fa'), popup=popup).add_to(folium_map)
        
        folium_map.save('templates/foliumMap.html')
        return render_template('index.html',
                            owners=owners,
                            connectorsType=connectorsType,
                            bookables=bookables,
                            folium_map=folium_map,
                            defaultAddress=address,
                            defaultOwner=owner,
                            defaultConnectorType=connectorType,
                            defaultBookable=bookable)
        
    else:
        # aqui se tratan los datos del formulario
        address = request.form.get('address')
        owner = request.form.get('owner')
        connectorType = request.form.get('connectorType')
        bookable = request.form.get('bookable')
        
        folium_map = folium.Map(location=startingCoordinates, zoom_start=14)

        filterVariables = {}
        if address != "":
            filterVariables["address"] = address
        if owner != "Todos":
            filterVariables["owner"] = owner
        if connectorType != "Todos":
            filterVariables["connectorType"] = connectorType
        if bookable != "Todos":
            filterVariables["bookable"] = bookable
        
        obtain_inf.filterQuery(filterVariables)
        
        for cs in obtain_inf.cs_data:
            csAux = obtain_inf.cs_data[cs]
            if (csAux["bookable"] is None):
                csAux["bookable"] = "No hay información"
            if (csAux["connectorType"] is None):
                csAux["connectorType"] = "No hay información"
            if (csAux["wikidata"] is None):
                csAux["wikidata"] = "-"
            
            iframe = folium.IFrame("<div class='map-popup' style='font-family:IBM Plex Sans'><center><h4 class='titulo'><b>Charging Station</b></h4></center>" + "<b>Dirección: </b>" + csAux["address"] + ", " + csAux["city"] + "<br>" + "<b>Operador: </b>" + csAux["owner"] + "<br>" + "<b>Reservable: </b>" + csAux["bookable"] +  "<br>" + "<b>Conectores: </b>" + csAux["numberOfConnectors"] + "<br>" + "<b>Tipo de conector: </b>" + csAux["connectorType"] + "<br><a target='_blank' href='" + csAux['wikidata'] + "'>Ver más</a>" + "</div>", width=300, height=200)
            popup = folium.Popup(iframe)
            folium.Marker(location=[obtain_inf.cs_data[cs]["latitude"], obtain_inf.cs_data[cs]["longitude"]], icon=folium.Icon(color="red", icon='plug', prefix='fa'), popup=popup).add_to(folium_map)
        
        folium_map.save('templates/foliumMap.html')
        
        return render_template("index.html",
                            owners=owners,
                            connectorsType=connectorsType,
                            bookables=bookables,
                            folium_map=folium_map,
                            defaultAddress=address,
                            defaultOwner=owner,
                            defaultConnectorType=connectorType,
                            defaultBookable=bookable)

@app.route('/query',methods=['GET', 'POST'])
def query():
    return render_template('query.html')

@app.route('/about',methods=['GET'])
def about():
    return render_template('about.html')

@app.route('/map',methods=['GET'])
def map():
    return render_template('map.html')

@app.route('/foliumMap',methods=['GET'])
def foliumMap():
    return render_template('foliumMap.html')

@app.route('/sparql',methods=['GET'])
def sparql():
    return render_template('sparql.html')

def cleanVariables():
    address = ""
    owner = "Todos"
    connectorType = "Todos"
    bookable = "Todos"


if __name__ == '__main__':
    app.run()