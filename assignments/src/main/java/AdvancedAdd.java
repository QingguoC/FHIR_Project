import ca.uhn.fhir.model.primitive.IdDt;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Observation.ObservationStatus;
import org.hl7.fhir.dstu3.model.Quantity;
import org.hl7.fhir.dstu3.model.Reference;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.context.FhirContext;

/**
 * This class contains methods for adding resoruces to the FHIR server.
 */
public class AdvancedAdd {

    private IGenericClient client = null;


    public static void main(String[] args){
        AdvancedAdd ad = new AdvancedAdd("http://hapi.fhir.org/baseDstu3");
        //System.out.println(ad.addObservation("Patient/270957","8867-4","Heart Rate",78,"/min","88"));
        System.out.println(ad.addPatient("Big","John"));
    }
    public AdvancedAdd(String baseUrl) {
        FhirContext ctx = FhirContext.forDstu3();
        client = ctx.newRestfulGenericClient(baseUrl);
    }

    /**
     * Add a new patient to the FHIR server with the given first and last name.
     * Return the ID of the newly created patient.
     */
    public String addPatient(String firstName, String lastName) {
        //Place your code here
        Patient patient = new Patient();
        patient.addName().setFamily(lastName).addGiven(firstName);
        MethodOutcome outcome = client.create()
                .resource(patient)
                .prettyPrint()
                .encodedJson()
                .execute();

        return outcome.getId().getValue();
    }

    /**
     * Add a new observation to the FHIR server with a reference to the specified patient ID.
     * Assume the patient already exists in the FHIR server.
     * The observation will have a loinc code and display name, a unit of measure value code,
     * units, and value for the observation.
     * Return the ID of the newly created observation.
     */
    public String addObservation(String patientId, String loincCode, String loincDisplayName,
                                 double value, String valueUnit, String valueCode) {
        //Place your code here

        Observation ob = new Observation();
        ob.getCode()
                .addCoding()
                .setSystem("http://loinc.org")
                .setCode(loincCode)
                .setDisplay(loincDisplayName);
        ob.setSubject(new Reference(patientId));
        ob.setValue(
                new Quantity()
                        .setValue(value)
                        .setUnit(valueUnit)
                        .setCode(valueCode));
        MethodOutcome outcome = client.create()
                .resource(ob)
                .prettyPrint()
                .encodedJson()
                .execute();
        return outcome.getId().getValue();
    }

}