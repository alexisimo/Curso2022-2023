#!/usr/bin/env python
# coding: utf-8

# **Task 07: Querying RDF(s)**

# In[1]:


get_ipython().system('pip install rdflib')
github_storage = "https://raw.githubusercontent.com/FacultadInformatica-LinkedData/Curso2021-2022/master/Assignment4/course_materials"


# Leemos el fichero RDF de la forma que lo hemos venido haciendo
 
# In[2]:


from rdflib import Graph, Namespace, Literal
from rdflib.namespace import RDF, RDFS
g = Graph()
g.namespace_manager.bind('ns', Namespace("http://somewhere#"), override=False)
g.namespace_manager.bind('vcard', Namespace("http://www.w3.org/2001/vcard-rdf/3.0#"), override=False)
g.parse(github_storage+"/rdf/example6.rdf", format="xml")


# In[4]:


# Add the namespace and import the usage of SPARQL
ns = Namespace("http://somewhere#")
from rdflib.plugins.sparql import prepareQuery


# **TASK 7.1: List all subclasses of "Person" with RDFLib and SPARQL**

# In[7]:


# Visualize the results
for s,p,o in g.triples((None,RDFS.subClassOf,ns.Person)):
  print(s)
  for s1,p1,o1 in g.triples((None,RDFS.subClassOf,s)):
      print(s1)
print('\n')

q1 = prepareQuery('''
  SELECT 
    ?s
  WHERE { 
    ?s (rdfs:subClassOf) ns:Person.  
  }
  ''',
  initNs = { "rdfs": RDFS, "ns": ns}
)
for r in g.query(q1):
  print(r)


# **TASK 7.2: List all individuals of "Person" with RDFLib and SPARQL (remember the subClasses)**
# 

# In[11]:


for s, p, o in g.triples((None, RDF.type, ns.Person)):
  print(s, "Person.")
for s, p, o in g.triples((None, RDFS.subClassOf, ns.Person)):
  for s2, p2, o2 in g.triples((None, RDF.type, s)):
    print(s2, "subclass of", s)

print('\n')
q2 = prepareQuery('''
  SELECT distinct ?Person
  WHERE {
    {?PClass (rdfs:subClassOf/rdfs:subClassOf*) ns:Person}
    {?Person rdf:type ?PClass}
    UNION
    {?Person rdf:type ns:Person}
  }
  ''',
  initNs = { "ns": ns}
)
# Visualize the results
for r in g.query(q2):
  print(r)


# **TASK 7.3: List all individuals of "Person" and all their properties including their class with RDFLib and SPARQL**
# 

# In[12]:


# Visualize the results
for s,p,o in g.triples((None,RDF.type,ns.Person)):
  for s,p,o in g.triples((s,None,None)):
    print(s,p,o)
for s,p,o in g.triples((None,RDFS.subClassOf,ns.Person)):
  for(s,p,o) in g.triples((None,RDF.type,s)):
    for(s,p,o) in g.triples((s,None,None)):
      print(s,p,o)
print('\n')

q3 = prepareQuery('''
  SELECT distinct ?Person ?P ?O
  WHERE {
    {?PClass (rdfs:subClassOf) ns:Person}
    {?Person rdf:type ?PClass}
    UNION
    {?Person rdf:type ns:Person}
    {?Person ?P ?O}
  }
  ''',
  initNs = { "ns": ns}
)

for r in g.query(q3):
  print(r)


# In[ ]:




