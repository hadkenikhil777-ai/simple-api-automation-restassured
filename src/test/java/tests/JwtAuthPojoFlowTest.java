package tests;

import base.BaseTest;
import endpoints.JwtAuthEndpoints;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import pojo.AuthResponse;
import utils.JwtTestData;
import java.util.Map;

public class JwtAuthPojoFlowTest extends BaseTest {

    private static String accessToken;
    private  static String refreshToken;
    private  static Map<String, String> cookies;

    @Test(priority = 1)
    public void loginWithPojo() {

        Response response = JwtAuthEndpoints.login(JwtTestData.validLoginRequest());

        Assert.assertEquals(response.getStatusCode(), 200);

        AuthResponse auth = response.as(AuthResponse.class);

        accessToken = auth.getAccessToken();
        refreshToken = auth.getRefreshToken();
        cookies = response.getCookies();

        Assert.assertNotNull(accessToken);
        Assert.assertNotNull(refreshToken);
        Assert.assertEquals(auth.getUsername(), "emilys");
    }

        @Test(priority = 2)
        public void accessSecureApiWithPojoToken(){

            Response response =
                    JwtAuthEndpoints.getUserProfile(accessToken, cookies);

            Assert.assertEquals(response.getStatusCode(), 200);


    }
}
