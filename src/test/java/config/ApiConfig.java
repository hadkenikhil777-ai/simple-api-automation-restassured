package config;

import io.restassured.RestAssured;

import java.util.ResourceBundle;

public class ApiConfig {

    private static final ResourceBundle config =
            ResourceBundle.getBundle("config");

    public enum ApiType {
        PETSTORE,
        DUMMYJSON
    }

    public static void setApi(ApiType apiType) {

        switch (apiType) {
            case PETSTORE:
                RestAssured.baseURI = getRequiredUrl("petstore.base.url");
                break;

            case DUMMYJSON:
                RestAssured.baseURI = getRequiredUrl("dummyjson.base.url");
                break;

            default:
                throw new IllegalArgumentException("Unsupported API type: " + apiType);
        }
    }

    private static String getRequiredUrl(String key) {
        String url = config.getString(key);
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalStateException("Missing or empty config value for key: " + key);
        }
        return url.trim();
    }
}
