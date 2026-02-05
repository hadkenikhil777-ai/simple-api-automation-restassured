package tests;

import base.BaseTest;
import context.AuthContext;
import api.JwtAuthApi;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import reporting.ReportLogger;
import utils.JwtTestData;

@Listeners(reporting.ExtentTestListener.class)
@Test(groups = {"JWT Fresh Token"})
public class JwtRefreshTokenTest extends BaseTest {

    @BeforeMethod
    public void init() {
        useDummyJsonApi(); // setup only
    }

    @Test(priority = 1)
    public void loginAndCaptureToken() {

        ReportLogger.info("Starting login to capture access & refresh tokens");

        Response response = JwtAuthApi.login(
                JwtTestData.validLoginRequest()
        );

        ReportLogger.info("Login Status Code: " + response.getStatusCode());
        ReportLogger.info("Login Response:\n" + response.asPrettyString());

        Assert.assertEquals(response.getStatusCode(), 200);

        AuthContext.setAccessToken(
                response.jsonPath().getString("accessToken")
        );
        AuthContext.setRefreshToken(
                response.jsonPath().getString("refreshToken")
        );
        AuthContext.setCookies(response.getCookies());

        ReportLogger.info("Access token stored in AuthContext (masked)");
        ReportLogger.info("Refresh token stored in AuthContext");
        ReportLogger.info("Cookies stored in AuthContext");

        Assert.assertNotNull(AuthContext.getAccessToken());
        Assert.assertNotNull(AuthContext.getRefreshToken());

        ReportLogger.pass("Login successful and tokens captured");
    }

    @Test(priority = 2)
    public void accessApiWithValidAccessToken() {

        ReportLogger.info("Accessing secured API using valid access token");

        Response response =
                JwtAuthApi.getUserProfile(
                        AuthContext.getAccessToken(),
                        AuthContext.getCookies()
                );

        ReportLogger.info("Secured API Status Code: " + response.getStatusCode());
        ReportLogger.info("Secured API Response:\n" + response.asPrettyString());

        Assert.assertEquals(response.getStatusCode(), 200);

        ReportLogger.pass("Secured API accessed successfully with valid access token");
    }

    @Test(priority = 3)
    public void refreshAccessToken() {

        ReportLogger.info("Refreshing access token using refresh token");

        Response response =
                JwtAuthApi.refreshToken(
                        AuthContext.getRefreshToken(),
                        AuthContext.getCookies()
                );

        ReportLogger.info("Refresh Token Status Code: " + response.getStatusCode());
        ReportLogger.info("Refresh Token Response:\n" + response.asPrettyString());

        Assert.assertEquals(response.getStatusCode(), 200);

        AuthContext.setAccessToken(
                response.jsonPath().getString("accessToken")
        );
        AuthContext.setRefreshToken(
                response.jsonPath().getString("refreshToken")
        );
        AuthContext.setCookies(response.getCookies());

        Assert.assertNotNull(AuthContext.getAccessToken());

        ReportLogger.pass("Access token refreshed and updated in AuthContext");
    }

    @Test(priority = 4)
    public void accessApiWithNewAccessToken() {

        ReportLogger.info("Accessing secured API using refreshed access token");

        Response response =
                JwtAuthApi.getUserProfile(
                        AuthContext.getAccessToken(),
                        AuthContext.getCookies()
                );

        ReportLogger.info("Secured API Status Code: " + response.getStatusCode());
        ReportLogger.info("Secured API Response:\n" + response.asPrettyString());

        Assert.assertEquals(response.getStatusCode(), 200);

        ReportLogger.pass("Secured API accessed successfully with refreshed token");
    }
}
