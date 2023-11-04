package com.example.apachecamelapp.service;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.dstu3.model.Bundle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class XmlToJson {

    @Value("${input}")
    public String input;


    public static void main(String[] args) throws IOException {
        xmlToJson();
    }

    public static void xmlToJson() throws IOException {
        String xmlInput = new String(Files.readAllBytes(Paths.get("F:/Khushboo/apacheCamelApplication/fhir/xml/DischargeNote_Example.xml")));
        FhirContext ctx = FhirContext.forDstu3(); // Replace 'forR4' with the FHIR version you are using (R4, R5, etc.)
        Bundle bundle = (Bundle) ctx.newXmlParser().parseResource(xmlInput);
        String jsonOutput = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle);
        Files.write(Paths.get("/fhir/json/file.json"), jsonOutput.getBytes(StandardCharsets.UTF_8));
    }
}
