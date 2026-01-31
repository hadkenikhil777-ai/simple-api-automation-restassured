package tests;

import base.BaseTest;
import context.AuthContext;
import endpoints.JwtAuthEndpoints;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import pojo.AuthResponse;
import reporting.ExtentTestListener;
import utils.JwtTestData;
import java.util.Map;

public class JwtAuthPojoFlowTest extends BaseTest {


    @Test(priority = 1)
    public void loginWithPojo() {

        ExtentTestListener.getTest().info("Logging in to get access and refresh tokens");

        Response response =
                JwtAuthEndpoints.login(JwtTestData.validLoginRequest());

        Assert.assertEquals(response.getStatusCode(), 200);

        AuthResponse auth = response.as(AuthResponse.class);

        AuthContext.setAccessToken(auth.getAccessToken());
        AuthContext.setRefreshToken(auth.getRefreshToken());
        AuthContext.setCookies(response.getCookies());

        ExtentTestListener.getTest().info("Access token stored in AuthContext");
    }


    @Test(priority = 2)
    public void accessSecureApiUsingAuthContext() {

        ExtentTestListener.getTest().info("Calling secured API with JWT token");

        Response response =
                JwtAuthEndpoints.getUserProfile(
                        AuthContext.getAccessToken(),
                        AuthContext.getCookies()
                );

        Assert.assertEquals(response.getStatusCode(), 200);

        ExtentTestListener.getTest().pass("Secured API accessed successfully");
    }

}
