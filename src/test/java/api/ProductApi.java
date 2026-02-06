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


    public static Response getProductsSorted(String sortBy, String order) {
        return given()
                .queryParam("sortBy", sortBy)
                .queryParam("order", order)
                .when()
                .get("/products");
    }

    public static Response getProductsSortedWithPagination(
            String sortBy,
            String order,
            int limit,
            int skip
    ) {
        return given()
                .queryParam("sortBy", sortBy)
                .queryParam("order", order)
                .queryParam("limit", limit)
                .queryParam("skip", skip)
                .when()
                .get("/products");
    }

    public static Response getProductsWithInvalidParam() {
        return given()
                .queryParam("invalidParam", "test")
                .when()
                .get("/products");
    }

    public static Response postProductsEndpoint() {
        return given()
                .when()
                .post("/products");
    }

    public static Response getProductsWithMalformedParams() {
        return given()
                .queryParam("limit", "invalid-number")
                .when()
                .get("/products");
    }



}
