package endpoints;

import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class JwtAuthEndpoints {

    public static Response login (Object payload){

        return given()
                .header("Content-Type", "application/json")
                .body(payload)

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
                        Map.of(
                                "refreshToken", refreshToken,
                                "expiresInMins", 30
                        )
                )
                .when()
                .post("/auth/refresh");
    }

}
