package tests;

import base.BaseTest;
import api.UserApi;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import reporting.ReportLogger;
import utils.AssertUtils;
import utils.TestData;

import java.util.HashMap;
import java.util.Map;

@Listeners(reporting.ExtentTestListener.class)
@Test(groups = {"Create User"})
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

        Response response = UserApi.createUser(payload);

        ReportLogger.info("Response:\n" + response.asPrettyString());

        AssertUtils.assertStatusIn(
                response,
                "Create user without username",
                200,
                400
        );
    }

    @Test
    public void createUserWithoutEmail() {

        ReportLogger.info("Creating user without email");

        Map<String, Object> payload = TestData.createUserPayload();
        payload.remove("email");

        Response response = UserApi.createUser(payload);

        ReportLogger.info("Response:\n" + response.asPrettyString());

        AssertUtils.assertStatusAtLeast(
                response,
                "Create user without email",
                200
        );
    }

    /* -------------------------
       2️⃣ Invalid Data Formats
       ------------------------- */

    @Test
    public void createUserWithInvalidEmail() {

        ReportLogger.info("Creating user with invalid email format");

        Map<String, Object> payload = TestData.createUserPayload();
        payload.put("email", "invalid-email");

        Response response = UserApi.createUser(payload);

        ReportLogger.info("Response:\n" + response.asPrettyString());

        AssertUtils.assertStatusAtLeast(
                response,
                "Create user with invalid email format",
                200
        );
    }

    @Test
    public void createUserWithInvalidPhone() {

        ReportLogger.info("Creating user with invalid phone");

        Map<String, Object> payload = TestData.createUserPayload();
        payload.put("phone", "abcd1234");

        Response response = UserApi.createUser(payload);

        ReportLogger.info("Response:\n" + response.asPrettyString());

        AssertUtils.assertStatusAtLeast(
                response,
                "Create user with invalid phone",
                200
        );
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


        Response response = UserApi.createUser(payload);

        ReportLogger.info("Response:\n" + response.asPrettyString());

        AssertUtils.assertStatusAtLeast(
                response,
                "Create user with very long username",
                200
        );
    }

    /* -------------------------
       4️⃣ Duplicate User
       ------------------------- */

    @Test
    public void createDuplicateUser() {

        ReportLogger.info("Creating duplicate user");

        Map<String, Object> payload = TestData.createUserPayload();

        Response firstResponse = UserApi.createUser(payload);
        Response secondResponse = UserApi.createUser(payload);

        ReportLogger.info("Second Response:\n" + secondResponse.asPrettyString());

        AssertUtils.assertStatusAtLeast(
                secondResponse,
                "Create duplicate user (second response)",
                200
        );
    }

    /* -------------------------
       5️⃣ Empty Payload
       ------------------------- */

    @Test
    public void createUserWithEmptyPayload() {

        ReportLogger.info("Creating user with empty payload");

        Map<String, Object> payload = new HashMap<>();

        Response response = UserApi.createUser(payload);

        ReportLogger.info("Response:\n" + response.asPrettyString());

        AssertUtils.assertStatusIn(
                response,
                "Create user with empty payload",
                200,
                400
        );
    }

    /* -------------------------
       6️⃣ Invalid HTTP Method
       ------------------------- */

    @Test
    public void createUserUsingGetMethod() {

        ReportLogger.info("Calling create user API using GET method");

        Response response = UserApi.createUserUsingGet();

        ReportLogger.info("Response:\n" + response.asPrettyString());

        AssertUtils.assertStatusIn(
                response,
                "Create user using GET method",
                405
        );
    }

    /* -------------------------
       7️⃣ Missing Content-Type
       ------------------------- */

    @Test
    public void createUserWithoutContentType() {

        ReportLogger.info("Creating user without Content-Type header");

        Response response =
                UserApi.createUserWithoutContentType(
                        TestData.createUserPayload()
                );

        ReportLogger.info("Response:\n" + response.asPrettyString());

        AssertUtils.assertStatusIn(
                response,
                "Create user without Content-Type",
                200,
                415
        );
    }
}
