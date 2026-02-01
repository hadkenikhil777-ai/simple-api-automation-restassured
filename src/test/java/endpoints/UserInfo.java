package endpoints;

import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.patch;

public class UserInfo {

    public static Response createUser(Map<String, Object> payload){

        return given()
                .header("Content-Type", "application/json")
                .body(payload)
                .when()
                .post("/user");
    }
    public static Response login(String username, String password){

        return given()
                .queryParam("username", username)
                .queryParam("password", password)
                .when()
                .get("/user/login");

    }
    public static Response createUserUsingGet() {
        return given()
                .when()
                .get("/user")
                .then()
                .extract().response();
    }

    public static Response createUserWithoutContentType(Map<String, Object> payload) {
        return given()
                .body(payload)
                .when()
                .post("/user")
                .then()
                .extract().response();
    }



}
