package api;

import context.ResponseContext;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AuthApi {

    public static Response login (String username, String password)
    {
        Response response = given()
                .queryParam("username", username)
                .queryParam("password", password)
                .when()
                .get("/user/login");
        ResponseContext.setLastResponse(response);
        return response;
    }

    public static Response logout()
    {
        Response response = given()
                .when()
                .get("/user/logout");
        ResponseContext.setLastResponse(response);
        return response;

    }


}
