package tests;

import base.BaseTest;
import api.JwtAuthApi;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pojo.LoginRequest;
import reporting.ReportLogger;
import utils.JwtTestData;

@Listeners(reporting.ExtentTestListener.class)
@Test(groups = {"JWT Auth"})
public class JwtAuthNegativeTest extends BaseTest {

    @BeforeMethod
    public void init() {
        useDummyJsonApi();
    }

    /* -------------------------
       1️⃣ Login Negative Tests
       ------------------------- */

    @Test
    public void loginWithInvalidUsername() {

        ReportLogger.info("Login with invalid username");

        LoginRequest request = LoginRequest.builder()
                .username("invalidUser")
                .password("validPass123")
                .expiresInMins(30)
                .build();

        Response response = JwtAuthApi.login(request);

        ReportLogger.info("Response:\n" + response.asPrettyString());

        Assert.assertTrue(
                response.getStatusCode() == 400
                        || response.getStatusCode() == 401
                        || response.getStatusCode() == 500,
                "Expected authentication failure"
        );
    }

    @Test
    public void loginWithInvalidPassword() {

        ReportLogger.info("Login with invalid password");

        LoginRequest request = JwtTestData.validLoginRequest()
                .toBuilder()
                .password("wrongPassword123")
                .build();


        Response response = JwtAuthApi.login(request);

        ReportLogger.info("Response:\n" + response.asPrettyString());

        Assert.assertTrue(
                response.getStatusCode() == 400 || response.getStatusCode() == 401,
                "Expected auth failure"
        );
    }

    @Test
    public void loginWithoutPassword() {

        ReportLogger.info("Login without password");

        LoginRequest request = JwtTestData.validLoginRequest()
                .toBuilder()
                .password(null)
                .build();


        Response response = JwtAuthApi.login(request);

        ReportLogger.info("Response:\n" + response.asPrettyString());

        Assert.assertTrue(
                response.getStatusCode() == 400 || response.getStatusCode() == 401,
                "Expected validation failure"
        );
    }

    @Test
    public void loginWithoutUsername() {

        ReportLogger.info("Login without username");

        LoginRequest request = JwtTestData.validLoginRequest()
                .toBuilder()
                .username(null)
                .build();


        Response response = JwtAuthApi.login(request);

        ReportLogger.info("Response:\n" + response.asPrettyString());

        Assert.assertTrue(
                response.getStatusCode() == 400 || response.getStatusCode() == 401
        );
    }

    @Test
    public void loginWithEmptyPayload() {

        ReportLogger.info("Login with empty payload");

        LoginRequest request = new LoginRequest();

        Response response = JwtAuthApi.login(request);

        ReportLogger.info("Response:\n" + response.asPrettyString());

        Assert.assertEquals(response.getStatusCode(), 400);
    }

    @Test
    public void loginWithSqlInjectionAttempt() {

        ReportLogger.info("Login with SQL injection attempt");

        LoginRequest request = JwtTestData.validLoginRequest();
        request.setUsername("' OR 1=1 --");


        Response response = JwtAuthApi.login(request);

        ReportLogger.info("Response:\n" + response.asPrettyString());

        Assert.assertEquals(response.getStatusCode(), 400);
    }

    @Test
    public void loginWithXssAttempt() {

        ReportLogger.info("Login with XSS attempt");

        LoginRequest request = JwtTestData.validLoginRequest();
        request.setUsername("<script>alert(1)</script>");

        Response response = JwtAuthApi.login(request);

        ReportLogger.info("Response:\n" + response.asPrettyString());

        Assert.assertEquals(response.getStatusCode(), 400);
    }

    /* -------------------------
       2️⃣ JWT Token Negative Tests
       ------------------------- */

    @Test
    public void accessApiWithRandomStringToken() {

        ReportLogger.info("Access API with random string token");

        Response response =
                JwtAuthApi.getUserProfileWithTokenOnly("random-string");

        ReportLogger.info("Response:\n" + response.asPrettyString());

        Assert.assertTrue(
                response.getStatusCode() == 401 ||
                        response.getStatusCode() == 403 ||
                        response.getStatusCode() == 500
        );
    }

    @Test
    public void accessApiWithCorruptedJwt() {

        ReportLogger.info("Access API with corrupted JWT");

        String corruptedJwt =
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.invalid.payload";

        Response response =
                JwtAuthApi.getUserProfileWithTokenOnly(corruptedJwt);

        ReportLogger.info("Response:\n" + response.asPrettyString());

        Assert.assertTrue(
                response.getStatusCode() == 401 ||
                        response.getStatusCode() == 500
        );
    }

    /* -------------------------
       3️⃣ Refresh Token Negatives
       ------------------------- */

    @Test
    public void refreshWithInvalidRefreshToken() {

        ReportLogger.info("Refreshing with invalid refresh token");

        Response response =
                JwtAuthApi.refreshToken(
                        "invalid.refresh.token",
                        null
                );

        ReportLogger.info("Response:\n" + response.asPrettyString());

        Assert.assertTrue(
                response.getStatusCode() == 401 ||
                        response.getStatusCode() == 403 ||
                        response.getStatusCode() == 500
        );
    }

    @Test
    public void refreshWithoutRefreshToken() {

        ReportLogger.info("Refreshing without refresh token");

        Response response =
                JwtAuthApi.refreshToken(null, null);

        ReportLogger.info("Response:\n" + response.asPrettyString());

        Assert.assertTrue(
                response.getStatusCode() == 400 ||
                        response.getStatusCode() == 401
        );
    }
}
