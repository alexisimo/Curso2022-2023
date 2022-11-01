4
    Analysis
        - The analysis.html file does not contain the license of the dataset to be generated.
    Ontology
        - The ontology base namespace cannot be only a domain name.
        - The ontology file is not syntactically correct. Do no create ontologies by hand; use tools that will ensure that this does not happen.
        - Different names are used in different places to refer to the same term.
        - In OWL, having multiple domains (or ranges) means that the domain (or range) is the intersection of all the classes.  The current definitions of properties with multiple domains are wrong.
        - Replace xsd:Integer with xsd:integer.
        - The definition of some datatype properties is wrong.
        - Longitude and latitude are numbers.
    RDF data
        - The value property does not have the correct namespace.
        - You are creating different URIs for the same resource.
        - Longitude and latitude are not typed.
