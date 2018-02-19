import java.util.List;
import java.util.ArrayList;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;

/**
 * This class contains methods for reading resources from the FHIR server.
 */
public class SimpleRead {

    IGenericClient client = null;


    public static void main(String[] args){
        SimpleRead sr = new SimpleRead("http://fhirtest.uhn.ca/baseDstu3");
        FhirContext ctx = FhirContext.forDstu3();
        String name = sr.getNameByPatientID("271262");
        System.out.println(name);
//        List<String> res = sr.getIDByPatientName("Fanny");
//        for(String id : res){
//
//            System.out.println(id);
//        }

        //System.out.println(ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(res));
    }
    public SimpleRead(String baseUrl) {
        FhirContext ctx = FhirContext.forDstu3();
        client = ctx.newRestfulGenericClient(baseUrl);
    }

    /**
     * Find the patient with the given ID and return the full name as a
     * single string.
     */
    public String getNameByPatientID(String id) {
        // Hint, there is a method that will return the full name including
        // prefix, first, last, and suffix
        //Place your code here

        Patient patient = client.read().resource(Patient.class).withId(id).execute();

//        FhirContext ctx = FhirContext.forDstu3();
//        System.out.println(ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient));

        String res = patient.getNameFirstRep().getNameAsSingleString();
        return res;
    }

    /**
     * Find all the patients that have the provided name and return a list
     * of the IDs for those patients.  The search should include matches
     * where any part of the patient name (family, given, prefix, etc.)
     * matches the method 'name' parameter.
     */
    public List<String> getIDByPatientName(String name) {
        //Place your code here
        ArrayList<String> res = new ArrayList<String>();
        FhirContext ctx = FhirContext.forDstu3();
        Bundle responses = client.search().forResource(Patient.class).where(Patient.NAME.matches().values(name)).returnBundle(Bundle.class).execute();
        for(BundleEntryComponent bec : responses.getEntry()){
            //String pm = ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(bec.getResource());
            //Patient p = ctx.newXmlParser().parseResource(Patient.class,pm);
            Patient p = (Patient) bec.getResource();

            res.add(p.getId());
        }
        return res;

    }

//    public Bundle getIDByPatientName(String name) {
//        //Place your code here
//        FhirContext ctx = FhirContext.forDstu3();
//        Bundle responses = client.search().forResource(Patient.class).where(Patient.NAME.matches().values(name)).returnBundle(Bundle.class).execute();
//
//        return responses;
//
//    }

}