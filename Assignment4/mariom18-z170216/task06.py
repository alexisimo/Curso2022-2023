github_storage = "https://raw.githubusercontent.com/FacultadInformatica-LinkedData/Curso2021-2022/master/Assignment4/course_materials"
from rdflib import Graph, Namespace, Literal, XSD
from rdflib.namespace import RDF, RDFS

g = Graph()
g.namespace_manager.bind('ns', Namespace("http://somewhere#"), override=False)
g.namespace_manager.bind('vcard', Namespace("http://www.w3.org/2001/vcard-rdf/3.0#"), override=False)
g.parse(github_storage + "/rdf/example5.rdf", format="xml")

ns = Namespace("http://somewhere#")
g.add((ns.Researcher, RDF.type, RDFS.Class))
# for s, p, o in g:
#  print(s,p,o)

g.add((ns.University, RDF.type, RDFS.Class))
#for s, p, o in g:
#  print(s,p,o)

g.add((ns.Researcher, RDFS.subClassOf, ns.Person))
#for s, p, o in g:
#  print(s,p,o)

g.add((ns.JaneSmith, RDF.type, ns.Researcher))
#for s, p, o in g:
#  print(s,p,o)

fn=Literal("Jane Smith", datatype=XSD.string)
family=Literal("Smith", datatype=XSD.string)
given=Literal("JaneSmith", datatype=XSD.string)

VCARD = Namespace("http://www.w3.org/2001/vcard-rdf/3.0#")
g.add((ns.JaneSmith, VCARD.FN, fn))
g.add((ns.JaneSmith, VCARD.Family, family))
g.add((ns.JaneSmith, VCARD.Given, given))
#for s, p, o in g:
# print(s,p,o)


g.add((ns.UPM, RDF.type, ns.University))

g.add((ns.worksAt, RDF.type, RDF.Property))

g.add((ns.JohnSmith, ns.worksAt, ns.UPM))

for s, p, o in g:
  print(s,p,o)
