package endpoints;

import io.restassured.response.Response;
import pojo.LoginRequest;
import pojo.RefreshTokenRequest;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class JwtAuthEndpoints {

    public static Response login (LoginRequest request){

        return given()
                .header("Content-Type", "application/json")
                .body(request)

                .when()
                .post("/auth/login");

    }

    public static Response getUserProfile(String token, Map<String, String> cookies){

        return given()
                .cookies(cookies)
                .header("Autherization", "Bearer " + token)
                .when()
                .get("/auth/me");
    }


    public static Response getUserProfileWithTokenOnly(String token) {

        return given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/auth/me");
    }

    public static Response refreshToken(String refreshToken, Map<String, String> cookies){

        return given()
                .header("Content-Type", "application/json")
                .cookies(cookies)
                .body(
                        new RefreshTokenRequest(refreshToken, 30)
                )
                .when()
                .post("/auth/refresh");
    }

}
