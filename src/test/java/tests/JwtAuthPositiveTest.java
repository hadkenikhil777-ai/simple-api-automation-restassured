package tests;

import base.BaseTest;
import context.AuthContext;
import api.JwtAuthApi;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pojo.AuthResponse;
import reporting.ReportLogger;
import utils.JwtTestData;

@Listeners(reporting.ExtentTestListener.class)
public class JwtAuthPositiveTest extends BaseTest {

    @BeforeMethod
    public void init() {
        useDummyJsonApi(); // setup only, no logging
    }

    @Test(priority = 1)
    public void loginWithPojo() {

        ReportLogger.info("Starting login using POJO-based request");

        Response response =
                JwtAuthApi.login(JwtTestData.validLoginRequest());

        ReportLogger.info("Login API status: " + response.getStatusCode());
        ReportLogger.info("Login API response:\n" + response.asPrettyString());

        Assert.assertEquals(response.getStatusCode(), 200);

        AuthResponse auth = response.as(AuthResponse.class);

        AuthContext.setAccessToken(auth.getAccessToken());
        AuthContext.setRefreshToken(auth.getRefreshToken());
        AuthContext.setCookies(response.getCookies());

        ReportLogger.info("Access token stored in AuthContext (masked)");
        ReportLogger.info("Cookies stored in AuthContext");

        Assert.assertNotNull(AuthContext.getAccessToken());
        Assert.assertNotNull(AuthContext.getRefreshToken());

        ReportLogger.pass("Login successful and tokens captured using POJO");
    }

    @Test(priority = 2)
    public void accessSecureApiUsingAuthContext() {

        ReportLogger.info("Calling secured API using AuthContext");

        Response response =
                JwtAuthApi.getUserProfile(
                        AuthContext.getAccessToken(),
                        AuthContext.getCookies()
                );

        ReportLogger.info("Secured API status: " + response.getStatusCode());
        ReportLogger.info("Secured API response:\n" + response.asPrettyString());

        Assert.assertEquals(response.getStatusCode(), 200);

        ReportLogger.pass("Secured API accessed successfully using JWT from AuthContext");
    }
}
