package com.example.apachecamelapp.service;

import org.apache.camel.CamelContext;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelContext;

public class Context {


    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        PropertiesComponent propertiesComponent = new PropertiesComponent();
        propertiesComponent.setLocation("classpath:application.properties");
        context.addComponent("properties", propertiesComponent);
        context.addRoutes(new FHIRCamelRoute());
       // context.register(new FhirContextConfig());

        context.start();
        Thread.sleep(Long.MAX_VALUE);
    }
}
