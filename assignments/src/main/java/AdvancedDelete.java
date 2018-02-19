
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.context.FhirContext;

/**
 * This class contains methods for removing resoruces from the FHIR server.
 */
public class AdvancedDelete {

    private IGenericClient client = null;

    public AdvancedDelete(String baseUrl) {
        FhirContext ctx = FhirContext.forDstu3();
        client = ctx.newRestfulGenericClient(baseUrl);
    }
    public static void main(String[] args){
        AdvancedDelete ad = new AdvancedDelete("http://fhirtest.uhn.ca/baseDstu3");
        ad.deletePatient("271262");
    }
    /**
     * Delete the patient with the given ID from the FHIR server.
     */
    public void deletePatient(String patientId) {
        //Place your code here
//        IBaseOperationOutcome resp = client.delete().resourceById(new IdDt("Patient", patientId)).execute();
        client.delete()
                .resourceConditionalByType("Patient")
                .where(Patient.IDENTIFIER.exactly().code(patientId))
                .execute();
    }

    /**
     * Delete the observation with the given ID from the FHIR server.
     */
    public void deleteObservation(String observationId) {
        //Place your code here

        client.delete().resourceById(new IdDt("Observation", observationId)).execute();
//        if (resp != null) {
//            OperationOutcome outcome = (OperationOutcome) resp;
//            System.out.println(outcome.getIssueFirstRep().getDetailsElement().getValue());
//        }
    }

}