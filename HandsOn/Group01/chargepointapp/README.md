# Table of contents
- [Table of contents](#table-of-contents)
- [Quick demo](#quick-demo)
- [Charging Point App guide](#charging-point-app-guide)
  - [How to use](#how-to-use)
    - [Running helio server](#running-helio-server)
    - [Preparing virtual environment and running flask server](#preparing-virtual-environment-and-running-flask-server)
    - [Opening app in browser](#opening-app-in-browser)

# Quick demo

[![demo-2.png](https://i.postimg.cc/K87S1KMS/demo-2.png)]
[![demo3.png](https://i.postimg.cc/L6L6V81v/demo3.png)]
[![demo1.png](https://i.postimg.cc/fb5Gv6C8/demo1.png)]

# Charging Point App guide

## How to use

### Running helio server

You'll need the latest version of helio found in [Helio](https://github.com/oeg-upm/helio/releases)(we currently have it working on v0.3.13) and Java 8 runtime. Then, in the app root directory (Group01/chargepointapp) run the following command:

`java -jar publisher-0.3.13.jar --server.port=9000 --mappings=mappings`

### Preparing virtual environment and running flask server

To run the backend of the app we'll use a framework called flask (much more lightweight than django), and to have it running correctly we'll make a virtual environment where we'll install all the necessary dependencies in the project.

To do so, we open a terminal on the app root directory and run the following commands:

`python -m venv flask_application`

then we get into the virtual environment in the terminal using:

`flask_application\Scripts\activate`

then we install the dependencies:

`pip install flask python-dotenv folium rdflib SPARQLWrapper`

and finally run flask server with either one of these commands inside the virtual environment:

`flask run` or `python -m flask run`

### Opening app in browser

To see the app interface we just need to navigate to [http://localhost:8000](http://localhost:8000).
Notice we have the helio server running under [http://localhost:9000](http://localhost:9000) and therefore there it is the SPARQL endpoint.