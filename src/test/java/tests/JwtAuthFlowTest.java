package tests;

import base.BaseTest;
import endpoints.JwtAuthEndpoints;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.JwtTestData;

import java.util.Map;

public class JwtAuthFlowTest extends BaseTest {

    private static String accessToken;
    private static Map<String, String> authCookies;

    @Test(priority = 1)
    public void loginAndGetJwtToken() {

        Response response = JwtAuthEndpoints.login(
                JwtTestData.validLoginRequest()
        );

        Assert.assertEquals(response.getStatusCode(), 200);

        accessToken = response.jsonPath().getString("accessToken");
        authCookies = response.getCookies();

        System.out.println("JWT Token: " + accessToken);
        System.out.println("Cookies: " + authCookies);

        Assert.assertNotNull(accessToken, "Access token should not be null");
        Assert.assertFalse(authCookies.isEmpty());
    }

    @Test(priority = 2)
    public void accessSecuredApiWithJwt() {

        Response response = JwtAuthEndpoints.getUserProfile(accessToken, authCookies);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(
                response.jsonPath().getString("username"),
                "emily"
        );
    }

    @Test(priority = 3)
    public void accessApiWithInvalidJwtWithoutCookies() {

        Response response =
                JwtAuthEndpoints.getUserProfileWithTokenOnly("invalid.token.value");

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);
        System.out.println("Response: " + response.asString());

        Assert.assertEquals(response.getStatusCode(), 500,
                "DummyJSON returns 500 for malformed JWT instead of 401");


        Assert.assertTrue(
                statusCode == 401 || statusCode == 403 || statusCode == 500,
                "Expected authentication failure but got: " + statusCode
        );
    }

}
