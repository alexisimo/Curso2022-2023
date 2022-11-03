10
    Analysis
        - The analysis.html file does not contain the license of the dataset to be generated.
    Ontology
        - Correct "Turist" -> "Tourist".
        - The resource naming strategy os not correctly used in all ontology terms.
        - In OWL, there are object properties (where value of the property is a resource) and datatype properties (where the value of the property is a string literal, usually typed). The ontology only contains datatype properties when some of them should be object properties.
        - Domains are missing in properties.
    RDF data
        - It could happen that two individuals from different classes have the same URI because the naming strategy does not ensure uniqueness.
        - Not every string with numbers is a number (e.g., postal code).
