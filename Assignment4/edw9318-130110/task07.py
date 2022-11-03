# -*- coding: utf-8 -*-

from rdflib import Graph, Namespace, Literal
from rdflib.namespace import RDF, RDFS, FOAF
from rdflib.plugins.sparql import prepareQuery

if __name__ == '__main__':

  github_storage = "https://raw.githubusercontent.com/FacultadInformatica-LinkedData/Curso2021-2022/master/Assignment4/course_materials/rdf/"
  g = Graph()
  g.namespace_manager.bind('ns', Namespace("http://somewhere#"), override=False)
  g.namespace_manager.bind('vcard', Namespace("http://www.w3.org/2001/vcard-rdf/3.0#"), override=False)
  g.parse(github_storage+"example6.rdf", format="xml")
  vcard = Namespace("http://www.w3.org/2001/vcard-rdf/3.0/")
  np = Namespace("http://newPropertiesSpace#")
  ns = Namespace("http://somewhere#")
  rdfs = Namespace("http://www.w3.org/2000/01/rdf-schema#")

#TASK 7.1: List all subclasses of "Person" with RDFLib and SPARQL
print("7.1 RDFLIB----------")
for s, p, o in g.triples((None, RDFS.subClassOf, ns.Person)):
    print(f"{s} is a person")

print("7.1 SPARQL----------")
query = prepareQuery('''
  SELECT ?Subject WHERE { 
    ?Subject RDFS:subClassOf ns:Person
  } 
  ''',initNs= {"RDFS": rdfs, "ns":ns})
for result in g.query(query):
  print(result.Subject)

#TASK 7.2: List all individuals of "Person" with RDFLib and SPARQL (remember the subClasses)
print("7.2 RDFLIB----------")
for s, p, o in g.triples((None, RDF.type, ns.Person)):
    print(f"{s} is a person")
for s, p, o in g.triples((None, RDFS.subClassOf, ns.Person)):
        for s, p, o in g.triples((None, RDF.type, s)):
            print(f"{s} is a person")

print("7.2 SPARQL----------")
query = prepareQuery('''
  SELECT DISTINCT ?s WHERE { 
    ?Subject RDFS:subClassOf* ns:Person .
    ?s <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?Subject
  } 
  ''',initNs = {"ns":ns, "RDFS":RDFS})
for result in g.query(query) :
  print(result.s)

#TASK 7.3: List all individuals of "Person" and all their properties including their class with RDFLib and SPARQL
print("7.3 RDFLIB----------")
for s, p, o in g.triples((None, RDF.type, ns.Person)):
    for s2, p2, o2 in g.triples((s, None, None)):
        print(f"Property of {s} is {o2}")
for s, p, o in g.triples((None, RDFS.subClassOf, ns.Person)):
        for s2, p2, o2 in g.triples((None, RDF.type, s)):
            for s3, p3, o3 in g.triples((s2, RDF.type, None)):
                print(f"Property of {s2} is {o3}")

print("7.3 SPARQL----------")
query = prepareQuery('''
  SELECT DISTINCT ?s ?p ?o WHERE { 
    ?subclass RDFS:subClassOf* ns:Person .
    ?s <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?subclass.
    ?s ?p ?o.
  } 
  ''',initNs = {"ns":ns, "RDFS":RDFS})
for result in g.query(query):
  print(result.s, result.p, result.o)