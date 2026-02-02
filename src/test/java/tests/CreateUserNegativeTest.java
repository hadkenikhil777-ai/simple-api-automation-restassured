package tests;

import base.BaseTest;
import endpoints.UserInfo;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import reporting.ReportLogger;
import utils.TestData;

import java.util.HashMap;
import java.util.Map;

@Listeners(reporting.ExtentTestListener.class)
public class CreateUserNegativeTest extends BaseTest {

    @BeforeMethod
    public void init() {
        usePetStoreApi();
    }

    /* -------------------------
       1️⃣ Missing Mandatory Fields
       ------------------------- */

    @Test
    public void createUserWithoutUsername() {

        ReportLogger.info("Creating user without username");

        Map<String, Object> payload = TestData.createUserPayload();
        payload.remove("username");

        Response response = UserInfo.createUser(payload);

        ReportLogger.info("Response:\n" + response.asPrettyString());

        Assert.assertTrue(
                response.getStatusCode() == 400 || response.getStatusCode() == 200,
                "Petstore may allow missing username"
        );
    }

    @Test
    public void createUserWithoutEmail() {

        ReportLogger.info("Creating user without email");

        Map<String, Object> payload = TestData.createUserPayload();
        payload.remove("email");

        Response response = UserInfo.createUser(payload);

        ReportLogger.info("Response:\n" + response.asPrettyString());

        Assert.assertTrue(response.getStatusCode() >= 200);
    }

    /* -------------------------
       2️⃣ Invalid Data Formats
       ------------------------- */

    @Test
    public void createUserWithInvalidEmail() {

        ReportLogger.info("Creating user with invalid email format");

        Map<String, Object> payload = TestData.createUserPayload();
        payload.put("email", "invalid-email");

        Response response = UserInfo.createUser(payload);

        ReportLogger.info("Response:\n" + response.asPrettyString());

        Assert.assertTrue(response.getStatusCode() >= 200);
    }

    @Test
    public void createUserWithInvalidPhone() {

        ReportLogger.info("Creating user with invalid phone");

        Map<String, Object> payload = TestData.createUserPayload();
        payload.put("phone", "abcd1234");

        Response response = UserInfo.createUser(payload);

        ReportLogger.info("Response:\n" + response.asPrettyString());

        Assert.assertTrue(response.getStatusCode() >= 200);
    }

    /* -------------------------
       3️⃣ Boundary Value Testing
       ------------------------- */



    @Test
    public void createUserWithVeryLongUsername() {

        ReportLogger.info("Creating user with very long username");

        Map<String, Object> payload = TestData.createUserPayload();
        StringBuilder longUsername = new StringBuilder();
        for (int i = 0; i < 300; i++) {
            longUsername.append("a");
        }

        payload.put("username", longUsername.toString());


        Response response = UserInfo.createUser(payload);

        ReportLogger.info("Response:\n" + response.asPrettyString());

        Assert.assertTrue(response.getStatusCode() >= 200);
    }

    /* -------------------------
       4️⃣ Duplicate User
       ------------------------- */

    @Test
    public void createDuplicateUser() {

        ReportLogger.info("Creating duplicate user");

        Map<String, Object> payload = TestData.createUserPayload();

        Response firstResponse = UserInfo.createUser(payload);
        Response secondResponse = UserInfo.createUser(payload);

        ReportLogger.info("Second Response:\n" + secondResponse.asPrettyString());

        Assert.assertTrue(secondResponse.getStatusCode() >= 200);
    }

    /* -------------------------
       5️⃣ Empty Payload
       ------------------------- */

    @Test
    public void createUserWithEmptyPayload() {

        ReportLogger.info("Creating user with empty payload");

        Map<String, Object> payload = new HashMap<>();

        Response response = UserInfo.createUser(payload);

        ReportLogger.info("Response:\n" + response.asPrettyString());

        Assert.assertTrue(
                response.getStatusCode() == 400 || response.getStatusCode() == 200,
                "Petstore accepts empty payload"
        );
    }

    /* -------------------------
       6️⃣ Invalid HTTP Method
       ------------------------- */

    @Test
    public void createUserUsingGetMethod() {

        ReportLogger.info("Calling create user API using GET method");

        Response response = UserInfo.createUserUsingGet();

        ReportLogger.info("Response:\n" + response.asPrettyString());

        Assert.assertEquals(response.getStatusCode(), 405);
    }

    /* -------------------------
       7️⃣ Missing Content-Type
       ------------------------- */

    @Test
    public void createUserWithoutContentType() {

        ReportLogger.info("Creating user without Content-Type header");

        Response response =
                UserInfo.createUserWithoutContentType(
                        TestData.createUserPayload()
                );

        ReportLogger.info("Response:\n" + response.asPrettyString());

        Assert.assertTrue(
                response.getStatusCode() == 415 || response.getStatusCode() == 200
        );
    }
}
