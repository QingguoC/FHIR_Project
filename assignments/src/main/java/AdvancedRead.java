import java.util.HashSet;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import ca.uhn.fhir.rest.client.api.IGenericClient;

/**
 * This class contains methods for reading resoruces from the FHIR server.
 */
public class AdvancedRead {

    private IGenericClient client = null;

    public static void main(String[] args){
        AdvancedRead ar = new AdvancedRead("http://hapi.fhir.org/baseDstu3");
        System.out.println(ar.getTotalNumPatientsByObservation("8867-4"));
    }
    public AdvancedRead(String baseUrl) {
        FhirContext ctx = FhirContext.forDstu3();
        client = ctx.newRestfulGenericClient(baseUrl);
    }

    /**
     * Find all observations with the givin loinc code and return the total
     * number of patients referenced.  Note that patients may have multiple
     * observations, so the number of observations >= number of patients.
     */
    public int getTotalNumPatientsByObservation(String loincCode) {
        //Place your code here
        HashSet<String> res = new HashSet<String>();
        Bundle response = client.search().forResource(Observation.class).where(Observation.CODE.exactly().systemAndCode("http://loinc.org", loincCode)).returnBundle(Bundle.class).execute();

        do{
            for(BundleEntryComponent bec : response.getEntry()){
                Observation ob = (Observation) bec.getResource();
                if (ob.hasSubject()){
                    String p = ob.getSubject().getReference();
                    if (!p.equals("Patient/undefined")){
                        res.add(p);

                    }
                }

            }
            if (response.getLink(Bundle.LINK_NEXT) == null){
                break;
            }
            response = client.loadPage().next(response).execute();
        } while (true);

        return res.size();
    }

}