import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.api.MethodOutcome;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointUse;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Quantity;
import org.hl7.fhir.exceptions.FHIRException;

import java.util.List;

/**
 * This class contains methods for updating resources in the FHIR server.
 */
public class AdvancedUpdate {

    private IGenericClient client = null;

    public AdvancedUpdate(String baseUrl) {
        FhirContext ctx = FhirContext.forDstu3();
        client = ctx.newRestfulGenericClient(baseUrl);
    }

    public static void main(String[] args){
            AdvancedUpdate au = new AdvancedUpdate("http://hapi.fhir.org/baseDstu3");
            System.out.println(au.updatePatientHomePhone("209016","888-888-8888"));
        //au.updateObservationValue("270543",200);
    }

    /**
     * Find the patient with the given ID and update the home phone number.  If the
     * patient does not already have a home phone number, add it.  Return the ID
     * of the updated resource.
     */
    public String updatePatientHomePhone(String patientId, String homePhoneNumber) {
        //Place your code here
        Patient patient = client.read().resource(Patient.class).withId(patientId).execute();
        List<ContactPoint> cps = patient.getTelecom();
        boolean hasHomePhone = false;
        for(ContactPoint cp: cps){
//            if (cp.hasSystem())
//            System.out.println(cp.getValue() + "  "  +cp.getSystem().toCode());

            if (cp.hasSystem() && cp.getSystem().toCode().toLowerCase().equals("phone") && cp.hasUse() && cp.getUse().toCode().toLowerCase().equals("home")){
//                cp.setValue(homePhoneNumber);
                hasHomePhone = true;
                cp.setValue(homePhoneNumber);
            }
        }
        if (!hasHomePhone){

            cps.add(new ContactPoint().setUse(ContactPointUse.HOME).setSystem(ContactPoint.ContactPointSystem.PHONE).setValue(homePhoneNumber));
            patient.setTelecom(cps);
        }

        MethodOutcome outcome =  client.update()
                .resource(patient)
                .execute();

        return outcome.getId().getValue();
    }

    /**
     * Find the observation with the given ID and update the value.  Recall that
     * observations have a unit of measure value code, units, and a value - this
     * method only updates the value.  Return the ID of the updated resource.
     */
    public String updateObservationValue(String observationId, double value) {
        //Place your code here
        Observation observation = client.read().resource(Observation.class).withId(observationId).execute();


        try {
            observation.getValueQuantity().setValue(value);

//            System.out.println(observation.getValueQuantity().getValue().toString());
        } catch (FHIRException e) {
            e.printStackTrace();
        }
        MethodOutcome outcome =  client.update()
                .resource(observation)
                .execute();

//        FhirContext ctx = FhirContext.forDstu3();
//        System.out.println(ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(client.read().resource(Observation.class).withId(observationId).execute()));
        return outcome.getId().getValue();


    }

}