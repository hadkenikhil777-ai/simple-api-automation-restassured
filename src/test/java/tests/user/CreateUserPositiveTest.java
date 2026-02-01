package tests.user;

import base.BaseTest;
import endpoints.UserInfo;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import reporting.ReportLogger;
import utils.TestData;

import java.util.Map;

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

        Response response = UserInfo.createUser(payload);

        ReportLogger.info("Response Status: " + response.getStatusCode());
        ReportLogger.info("Response Body:\n" + response.asPrettyString());

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertNotNull(response.jsonPath().getString("message"));

        ReportLogger.pass("User created successfully");
    }
}
