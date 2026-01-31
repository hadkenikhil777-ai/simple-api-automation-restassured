package tests;

import base.BaseTest;
import context.AuthContext;
import endpoints.JwtAuthEndpoints;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import pojo.AuthResponse;
import utils.JwtTestData;
import java.util.Map;

public class JwtAuthPojoFlowTest extends BaseTest {


    @Test(priority = 1)
    public void loginWithPojo() {

        Response response =
                JwtAuthEndpoints.login(JwtTestData.validLoginRequest());

        Assert.assertEquals(response.getStatusCode(), 200);

        AuthResponse auth = response.as(AuthResponse.class);

        AuthContext.setAccessToken(auth.getAccessToken());
        AuthContext.setRefreshToken(auth.getRefreshToken());
        AuthContext.setCookies(response.getCookies());

        Assert.assertNotNull(AuthContext.getAccessToken());
        Assert.assertNotNull(AuthContext.getRefreshToken());
        Assert.assertFalse(AuthContext.getCookies().isEmpty());
    }

        @Test(priority = 2)
        public void accessSecureApiWithPojoToken(){

            Response response =
                    JwtAuthEndpoints.getUserProfile(AuthContext.getAccessToken(), AuthContext.getCookies());

            Assert.assertEquals(response.getStatusCode(), 200);


    }
}
