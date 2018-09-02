package app;

import org.apache.derby.tools.sysinfo;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.AddressDt;
import ca.uhn.fhir.model.dstu2.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;

public class HL7Provider {
	
	
	
	public static void main(String[] args) {
	
		System.out.println("Starting...");
	
		
		insertPatient(null);
		
	}
	
	public static String searchPatient(String name, String surname) {
		
		String result = "";
		
		// We're connecting to a DSTU1 compliant server in this example
		FhirContext ctx = FhirContext.forDstu2();
		String serverBase = "http://fhirtest.uhn.ca/baseDstu2";
		 
		IGenericClient client = ctx.newRestfulGenericClient(serverBase);
		 
		// Perform a search
		Bundle results = client.search()
			      .forResource(Patient.class)
			      .where(Patient.GIVEN.matches().value(name)).and(Patient.FAMILY.matches().value(surname))
			      .returnBundle(Bundle.class)
			      .execute();
		 
//		System.out.println("Found " + results.getEntry().size() + " patients named 'Smith'");
		result += "Found " + results.getEntry().size() + " patients named "+name+" "+surname+System.lineSeparator();
		

		for (Entry entry : results.getEntry()) {
			
			String jsonEncoded = ctx.newJsonParser().encodeResourceToString(entry.getResource());

			System.out.println("-------------------------------------------------------------------");
			result+= "-------------------------------------------------------------------"+System.lineSeparator();
			
			System.out.println(jsonEncoded);
			
			Patient patient = FhirContext.forDstu2().newJsonParser().parseResource(Patient.class, jsonEncoded);
			
			System.out.println(patient.getName().toString());
			System.out.println(patient.getGender());
			System.out.println(patient.getBirthDate());
			System.out.println(patient.getAddress());
			
			String formatedName = patient.getNameFirstRep().getGivenAsSingleString()+" "+patient.getNameFirstRep().getFamilyAsSingleString();
			
			String formatedAdderess = 
					patient.getAddressFirstRep().getCountry()+ ", "+
					patient.getAddressFirstRep().getCity()+ " "+
					"("+patient.getAddressFirstRep().getPostalCode()+")";

			String formatedID = patient.getIdentifierFirstRep().getValue();	
			
			
			result+= "ID: "+formatedID+System.lineSeparator();
			result+= "Active: "+patient.getActive()+System.lineSeparator();
			result+= "Name: "+formatedName+System.lineSeparator();
			result+= "Birth Date: "+patient.getBirthDate()+System.lineSeparator();
			result+= "Aderess: "+formatedAdderess+System.lineSeparator();	
		}
		
		return result;
		
	}
	
	
	public static void insertPatient(Patient patientTest) {
		
		FhirContext ctx = FhirContext.forDstu2();
		String serverBase = "http://fhirtest.uhn.ca/baseDstu2";
		 
		IGenericClient client = ctx.newRestfulGenericClient(serverBase);
		
		Patient p_ = new Patient();
		Patient patient = new Patient();
		// ..populate the patient object..
		patient.addIdentifier().setSystem("urn:system").setValue("789456123");
		patient.addName().addFamily("João").addGiven("Dias");
		 
		// Invoke the server create method (and send pretty-printed JSON
		// encoding to the server
		// instead of the default which is non-pretty printed XML)
		MethodOutcome outcome = client.create()
		   .resource(patient)
		   .prettyPrint()
		   .encodedJson()
		   .execute();
		 
		// The MethodOutcome object will contain information about the
		// response from the server, including the ID of the created
		// resource, the OperationOutcome response, etc. (assuming that
		// any of these things were provided by the server! They may not
		// always be)
		IdDt id = (IdDt) outcome.getId();
		System.out.println("Got ID: " + id.getValue());
		
	}

}
