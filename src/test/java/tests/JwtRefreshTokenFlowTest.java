package tests;

import base.BaseTest;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import endpoints.JwtAuthEndpoints;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.JwtTestData;

import java.util.Map;

public class JwtRefreshTokenFlowTest extends BaseTest {

    private static String accessToken;
    private static String refreshToken;
    private static Map<String, String> cookies;

    @Test(priority = 1)
    public void loginAndCaptureToken(){

        Response response = JwtAuthEndpoints.login(
                JwtTestData.loginPayload()
        );

        Assert.assertEquals(response.getStatusCode(), 200);

        accessToken = response.jsonPath().getString("accessToken");
        refreshToken = response.jsonPath().getString("refreshToken");
        cookies = response.getCookies();

        System.out.println(accessToken);
        System.out.println(refreshToken);
    }

    @Test(priority = 2)
    public void accessApiWithValidAccessToken(){

        Response response = JwtAuthEndpoints.getUserProfile(accessToken, cookies);

        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 3)
    public void refreshAccessTokenUsingRefreshToken(){

        Response response = JwtAuthEndpoints.refreshToken(refreshToken, cookies);

        Assert.assertEquals(response.getStatusCode(), 200);

        accessToken = response.jsonPath().getString("accessToken");
        refreshToken = response.jsonPath().getString("refreshToken");

        System.out.println("New Access Token: " + accessToken);
        System.out.println("New Regresh Token: " + refreshToken);

        Assert.assertNotNull(accessToken);
        Assert.assertNotNull(refreshToken);
    }

    @Test(priority = 4)
    public void accessApiWithNewAccessToken(){

        Response response = JwtAuthEndpoints.getUserProfile(accessToken, cookies);

        Assert.assertEquals(response.getStatusCode(), 200);
    }
}
