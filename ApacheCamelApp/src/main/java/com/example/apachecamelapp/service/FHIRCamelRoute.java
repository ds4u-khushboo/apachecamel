package com.example.apachecamelapp.service;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class FHIRCamelRoute extends RouteBuilder {

    @Autowired
    private FHIRService fhirService;

    @Autowired
    private XmlToJson xmlToJsons;

    @Value("${serverUrl}")
    private String serverUrll;

    @Override
    public void configure() {
        from("file:{{input}}?noop=true")
                .log("Processing file: ${header.input}")
             .convertBodyTo(String.class)
//                to("direct:xmlToJsons").
//              //  .bean(xmlToJson, "xmlToJson");
//        from("direct:xmlToJsons")
//                .setHeader("Content-Type", constant("application/json"))
                .to(serverUrll)// Configure the correct HAPI FHIR server URL
                .log("Sent to HAPI FHIR server: ${body}");
    }

    @Bean
    public FHIRService fhirService() {
        return new FHIRService();
    }


}
