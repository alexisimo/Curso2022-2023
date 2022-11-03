#!/usr/bin/env python
# coding: utf-8

# **Task 07: Querying RDF(s)**

# In[8]:


get_ipython().system('pip install rdflib')
github_storage = "https://raw.githubusercontent.com/FacultadInformatica-LinkedData/Curso2021-2022/master/Assignment4/course_materials"


# Leemos el fichero RDF de la forma que lo hemos venido haciendo

# In[9]:


from rdflib import Graph, Namespace, Literal
from rdflib.namespace import RDF, RDFS
g = Graph()
g.namespace_manager.bind('ns', Namespace("http://somewhere#"), override=False)
g.namespace_manager.bind('vcard', Namespace("http://www.w3.org/2001/vcard-rdf/3.0#"), override=False)
g.parse(github_storage+"/rdf/example6.rdf", format="xml")


# **TASK 7.1: List all subclasses of "Person" with RDFLib and SPARQL**

# In[10]:


# TO DO
from rdflib.plugins.sparql import prepareQuery

NS = Namespace("http://somewhere#")
VCARD = Namespace("http://www.w3.org/2001/vcard-rdf/3.0#")

q1 = prepareQuery('''
  SELECT ?subject WHERE {
      ?subject rdfs:subClassOf <http://somewhere#Person>.
  }
  ''',
  initNs = { "rdfs": RDFS}
)
# Visualize the results

#In SPARQL
for r in g.query(q1):
    print(r)
    
print("----------")
    
#In RDFLib
for s, p, o in g.triples((None, RDFS.subClassOf, NS.Person)):
    print(s)


# **TASK 7.2: List all individuals of "Person" with RDFLib and SPARQL (remember the subClasses)**
# 

# In[12]:


# TO DO
q2 = prepareQuery('''
  SELECT ?subject WHERE {
  {
      ?subject rdf:type <http://somewhere#Person>.
  }
  UNION
  {
      ?subclass rdfs:subClassOf* <http://somewhere#Person>.
      ?subject rdf:type ?subclass.
  }
  }
  ''',
  initNs = { "rdfs": RDFS, "rdf": RDF}
)
# Visualize the results

#In SPARQL
for r in g.query(q2):
    print(r)
    
print("----------")
    
#In RDFLib
for s, p, o in g.triples((None, RDF.type, NS.Person)):
    print(s)
    
for s, p, o in g.triples((None, RDFS.subClassOf, NS.Person)):
    for a, b, c in g.triples((None, RDF.type, s)):
        print(a)


# **TASK 7.3: List all individuals of "Person" and all their properties including their class with RDFLib and SPARQL**
# 

# In[14]:


# TO DO
q3 = prepareQuery('''
  SELECT ?subject ?prop ?value WHERE {
  {
      ?subject rdf:type <http://somewhere#Person>;
               ?property ?value.
  }
  UNION
  {
      ?subclass rdfs:subClassOf* <http://somewhere#Person>.
      ?subject rdf:type ?subclass;
               ?property ?value.
  }
  }
  ''',
  initNs = { "rdfs": RDFS, "rdf": RDF}
)
# Visualize the results

#In SPARQL
for r in g.query(q3):
    print(r)
    
print("----------")
    
#In RDFLib
for s, p, o in g.triples((None, RDF.type, NS.Person)):
    print(s)
    for a, b, c in g.triples((s, None, None)):
        print(b)
    
for a, b, c in g.triples((None, RDFS.subClassOf, NS.Person)):
    for s, p, o in g.triples((None, RDF.type, a)):
        print(s)
        for d, e, f in g.triples((s, None, None)):
            print(e)


# In[ ]:




