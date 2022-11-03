1
    Analysis
        - Spatial coordinates are numerical values, not an entity that can be linked. That is, even if in the application you relate entities by their spatial distance, the datasets won't be linked.
        - The analysis.html file does not contain the license of the dataset to be generated.
    Ontology
        - The namespace xsd1 is not correctly defined.
        - You are creating a class XMLSchemaxsd.
        - The ranges of the datatype properties are incorrect (xsd1:xsd).
        - Some properties do not have a domain.
        - If you are only going to define a string for the address; there is no need to have the address as a class, just use a datatype property.
        - You will need to create links between entities in your dataset (i.e., instances of the classes) and the same entities in other datasets. It is not clear which types of entities (i.e., classes) will be linked.
        - Errors in the ontology also appear in the example file.
    RDF data
        - The datatype xsd:string is not properly written.
        - Datatypes are missing.
        - Peripheral resources are not described.
        - City resources are not described.
