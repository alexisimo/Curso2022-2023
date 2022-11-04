5
    Analysis
    Ontology
        - The ontology base namespace cannot be only a domain name.
        - Accessibility is not a number; it is the accessibility types.
        - The domain is missing in some of the properties.
        - Some of the properties defined with numbers as ranges are not numbers.
        - URLs are not strings.
        - The classes and properties under the "time" namespace do not exist. The same happens with other namespaces that you are using.
        - The example file is not correct.
    RDF data
        - The RDF file is not syntactically correct.
        - The namespaces are wrong.
        - URLs are encoded as strings.
        - The values of xds:time are not properly encoded.
        - Some fields with range 0-1 are not integers, they are booleans.
        - Verify that the class and property names used in the RDF data are the same as those used in the ontology.
        - Some entities could be encoded as instances and not strings. You will need them for linking.
