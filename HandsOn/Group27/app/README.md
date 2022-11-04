## Public Energy Visor

This solution is a web application that provides the possibility of visualizing different types of energy consumption by public buildings in the city of Madrid.

Offers a series of query filters using a group of properties, which values are provided by a series of drop-down lists. Also, a SPARQL-endpoint is provided, that allows the final user to dive into the application graph. 

#### User's interface
![User UI](../img/view.png?raw=true)

#### Properties for filtering the map
Prop. Name | 
---|
Building name |
Building Type (in order to simplify, the only type available is "Libraries") |
District name |
Neightborhood name |
Year |

#### Information provided peer building
Info. Name | 
---|
Building name |
Building Type |
District name |
Neightborhood name |
Observed Year |
Observed Month |
Type of consumed energy |
Energy group |
Consumed quantity |
Wikidata ref (if exists) |

-----------------------
## Deployment considerations

*How to install the requirements* -> 
`$ pip install -r requierements.txt`

*How to deploy the web-app* -> 
`$ python run.py `

Env. Variables | Description
---|---
HELIO_SERVER | SPARQL-endpoint server IP:PORT 
APP_HOST | The IP to deploy the web-application 
APP_PORT | The PORT to deploy the web-application 

Important |
---|
A SPARQL-endpoint server is required, its IP-PORT has to be defined as an enviroment variable. |
The graph to be provided by the SPARQL-endpoint is located in `../rdf/consumo-energia-edificios-updated-with-links.ttl` |

-----------------------