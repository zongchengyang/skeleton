package api;

import controllers.netidController;
import io.dropwizard.jersey.validation.Validators;
import org.junit.Test;
import javax.validation.Validator;
import static junit.framework.TestCase.assertEquals;

public class AssignmentTest {
    private final Validator validator = Validators.newValidator();

    String myNetid = "zy338";
    @Test
    public void testnetidController() {
        netidController c = new netidController();
        String controllerNetid = c.getnetid();
        assertEquals(myNetid, controllerNetid);
    }
}