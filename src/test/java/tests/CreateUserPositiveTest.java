package tests;

import base.BaseTest;
import api.UserApi;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import reporting.ReportLogger;
import utils.AssertUtils;
import utils.TestData;

import java.util.Map;

@Test(groups = {"Create User"})
public class CreateUserPositiveTest extends BaseTest {

    @BeforeMethod
    public void init() {
        usePetStoreApi(); // ✅ setup only, NO logging
    }

    @Test
    public void createNewUser() {

        ReportLogger.info("PetStore API selected");
        ReportLogger.info("Preparing Create User payload");

        Map<String, Object> payload = TestData.createUserPayload();
        ReportLogger.info("Payload: " + payload);

        ReportLogger.info("Calling Create User API");

        Response response = UserApi.createUser(payload);

        ReportLogger.info("Response Status: " + response.getStatusCode());
        ReportLogger.info("Response Body:\n" + response.asPrettyString());

        AssertUtils.assertStatusIn(response, "Create user", 200);
        AssertUtils.assertNotNull(
                response.jsonPath().getString("message"),
                "Create user response message"
        );
    }
}
