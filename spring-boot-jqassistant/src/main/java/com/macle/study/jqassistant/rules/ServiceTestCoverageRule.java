package com.macle.study.jqassistant.rules;

import com.buschmais.jqassistant.core.rule.api.model.Concept;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;

@Concept("example:ServiceTestCoverage")
public class ServiceTestCoverageRule {
    public void validate(TypeDescriptor type) {
        String cypher = """            MATCH (service:Type)            WHERE service.name =~ '.*Service$'            WITH service            OPTIONAL MATCH (test:Type)            WHERE test.name = service.name + 'Test'            RETURN service.name as Service, 
                   CASE WHEN test IS NULL 
                        THEN 'Missing Test' 
                        ELSE 'Has Test' 
                   END as TestStatus        """;
    }
}