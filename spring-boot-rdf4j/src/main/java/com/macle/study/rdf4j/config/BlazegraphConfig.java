package com.macle.study.rdf4j.config;

import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BlazegraphConfig {

    @Bean
    public Repository blazegraphRepository() {
        HTTPRepository repository = new HTTPRepository("http://localhost:9999/bigdata/sparql");
        //repository.initialize();
        return repository;
    }
}