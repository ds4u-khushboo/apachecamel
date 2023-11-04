package com.example.apachecamelapp.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.apache.camel.ProducerTemplate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FHIRService {

    private ProducerTemplate producerTemplate;

    @Value("${input}")
    private static String input;

    public void sendXMLToHAPIFHIR(Patient patient, Practitioner practitioner) {
        FhirContext ctx = FhirContext.forR5();
        IGenericClient client = ctx.newRestfulGenericClient("http://localhost:8080/hapi/fhir");
//
//        Patient patient = new Patient();
//        patient.setId("1");
//        // Set patient attributes as needed
//
//        // Send the patient resource to the FHIR server
//        ICreateTyped create = client.create().resource(patient);
//        create.execute();

//        String surname = pid.getPatientName()[0].getFamilyName().getFn1_Surname().getValue();
//        String name = pid.getPatientName()[0].getGivenName().getValue();
//        String patientId = msg.getPATIENT_RESULT().getPATIENT().getPID().getPatientID().getCx1_ID().getValue();
//        Patient patient = new Patient();
        patient.addName().addGiven("name");
        patient.getNameFirstRep().setFamily("surname");
        patient.setId("123");
        patient.addName().setFamily("PATIENT");

        convertToJSON();
        // now convert the resource to JSON
        String output = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
        System.out.println(output);
        Bundle bundle = new Bundle();
        bundle.addEntry().setResource(patient);
        bundle.addEntry().setResource(practitioner);

        // Call the Camel route to compose the Composition
        Bundle bundles = producerTemplate.requestBody("direct:Patient", bundle, Bundle.class);


        // Send the Composition resource to the FHIR server
        producerTemplate.sendBody("direct:Patient", bundles);
    }
    public static String convertToJSON(){
        FhirContext context=FhirContext.forR4();
        IParser source   = context.newXmlParser();                         // new XML parser
        IBaseResource resource = source.parseResource(input);                // parse the resource
        IParser       target   = context.newJsonParser();
        System.out.println("resource "+target.setPrettyPrint( true ).encodeResourceToString( resource ));// new JSON parser
        return target.setPrettyPrint( true ).encodeResourceToString( resource );
    }

    public static void main(String[] args){
        convertToJSON();
    }
}
