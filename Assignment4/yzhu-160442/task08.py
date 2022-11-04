from rdflib.plugins.sparql import prepareQuery
from rdflib.namespace import RDF, RDFS

VCARD = Namespace("http://www.w3.org/2001/vcard-rdf/3.0#")

print("ORIGINAL GRAPH 1")
for s, p, o in g1.triples((None, RDF.type, VCARD.Person)):
    print(s,p,o)

query1 = prepareQuery('''
    SELECT DISTINCT ?Subject ?GivenName ?FamilyName ?Email WHERE {
        ?Subject rdf:type ns:Person.
        OPTIONAL{?Subject vcard:Given ?GivenName}.
        OPTIONAL{?Subject vcard:Family ?FamilyName}.
        OPTIONAL{?Subject vcard:EMAIL ?Email}
    }
    ''',
    initNs={"ns":Namespace("http://data.org#"),"vcard":VCARD, "rdf":RDF, "rdfs": RDFS}
    ) 
for r1 in g1.query(query1):
  print(r1)