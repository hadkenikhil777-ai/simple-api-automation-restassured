package api;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ProductApi {

    public static Response getProducts() {
        return given()
                .when()
                .get("/products");
    }

    public static Response getProductsWithLimit(int limit) {
        return given()
                .queryParam("limit", limit)
                .when()
                .get("/products");
    }

    public static Response getProductsWithPagination(int limit, int skip) {
        return given()
                .queryParam("limit", limit)
                .queryParam("skip", skip)
                .when()
                .get("/products");
    }

    public static Response getProductsByCategory(String category) {
        return given()
                .when()
                .get("/products/category/" + category);
    }

}
