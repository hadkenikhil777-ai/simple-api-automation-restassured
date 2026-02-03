package tests;

import base.BaseTest;
import api.JwtAuthApi;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import reporting.ReportLogger;
import utils.JwtTestData;

import java.util.Map;

@Listeners(reporting.ExtentTestListener.class)
public class JwtAuthFlowTest extends BaseTest {

    private static String accessToken;
    private static Map<String, String> authCookies;

    @BeforeMethod
    public void init() {
        useDummyJsonApi(); // setup only
    }

    @Test(priority = 1)
    public void loginAndGetJwtToken() {

        ReportLogger.info("DummyJSON API selected");
        ReportLogger.info("Starting JWT login flow");

        ReportLogger.info("Calling Login API");

        Response response = JwtAuthApi.login(
                JwtTestData.validLoginRequest()
        );

        ReportLogger.info("Login API Status Code: " + response.getStatusCode());
        ReportLogger.info("Login API Response:\n" + response.asPrettyString());

        Assert.assertEquals(response.getStatusCode(), 200);

        accessToken = response.jsonPath().getString("accessToken");
        authCookies = response.getCookies();

        ReportLogger.info("Access Token captured (masked): "
                + accessToken.substring(0, 20) + "...");

        ReportLogger.info("Auth Cookies captured: " + authCookies.keySet());

        Assert.assertNotNull(accessToken, "Access token should not be null");
        Assert.assertFalse(authCookies.isEmpty(), "Auth cookies should not be empty");

        ReportLogger.pass("JWT token and cookies captured successfully");
    }

    @Test(priority = 2)
    public void accessSecuredApiWithJwt() {

        ReportLogger.info("Accessing secured API using valid JWT and cookies");

        Response response =
                JwtAuthApi.getUserProfile(accessToken, authCookies);

        ReportLogger.info("Secured API Status Code: " + response.getStatusCode());
        ReportLogger.info("Secured API Response:\n" + response.asPrettyString());

        Assert.assertEquals(response.getStatusCode(), 200);

        Assert.assertEquals(
                response.jsonPath().getString("username"),
                "emilys"
        );

        ReportLogger.pass("Secured API accessed successfully with valid JWT");
    }

    @Test(priority = 3)
    public void accessApiWithInvalidJwtWithoutCookies() {

        ReportLogger.info("Accessing secured API with INVALID JWT and NO cookies");

        Response response =
                JwtAuthApi.getUserProfileWithTokenOnly("invalid.token.value");

        int statusCode = response.getStatusCode();

        ReportLogger.info("Invalid JWT Status Code: " + statusCode);
        ReportLogger.info("Invalid JWT Response:\n" + response.asPrettyString());

        /*
         * DummyJSON quirk:
         * - Returns 500 for malformed JWT instead of 401
         */
        Assert.assertTrue(
                statusCode == 401 || statusCode == 403 || statusCode == 500,
                "Expected authentication failure but got: " + statusCode
        );

        ReportLogger.pass(
                "Invalid JWT correctly rejected (DummyJSON returned " + statusCode + ")"
        );
    }
}
