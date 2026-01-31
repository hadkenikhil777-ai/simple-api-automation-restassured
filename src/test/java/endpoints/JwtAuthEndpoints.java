package endpoints;

import io.restassured.response.Response;
import pojo.LoginRequest;
import pojo.RefreshTokenRequest;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class JwtAuthEndpoints {

    public static Response login (LoginRequest request){

        return given()
                .log().ifValidationFails()
                .header("Content-Type", "application/json")
                .body(request)

                .when()
                .log().ifValidationFails()
                .post("/auth/login");

    }

    public static Response getUserProfile(String token, Map<String, String> cookies){

        return given()
                .log().ifValidationFails()
                .cookies(cookies)
                .header("Autherization", "Bearer " + token)
                .when()
                .log().ifValidationFails()
                .get("/auth/me");
    }


    public static Response getUserProfileWithTokenOnly(String token) {

        return given()
                .log().ifValidationFails()
                .header("Authorization", "Bearer " + token)
                .when()
                .log().ifValidationFails()
                .get("/auth/me");
    }

    public static Response refreshToken(String refreshToken, Map<String, String> cookies){

        return given()
                .log().ifValidationFails()
                .header("Content-Type", "application/json")
                .cookies(cookies)
                .body(
                        new RefreshTokenRequest(refreshToken, 30)
                )
                .when()
                .log().ifValidationFails()
                .post("/auth/refresh");
    }

}
