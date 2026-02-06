package tests;

import api.ProductApi;
import base.BaseTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import reporting.ReportLogger;
import utils.AssertUtils;

import java.util.List;
import java.util.Map;

public class ProductSecurityTest extends BaseTest {

    @BeforeMethod
    public void init() {
        useDummyJsonApi(); // setup only
    }

    // 1️⃣ Verify API does not expose sensitive internal fields
    @Test
    public void verifyNoSensitiveFieldsExposed() {

        ReportLogger.info("========== TEST STARTED : Verify No Sensitive Fields ==========");

        Response response = ProductApi.getProducts();

        List<Map<String, Object>> products =
                response.jsonPath().getList("products");

        for (Map<String, Object> product : products) {

            Assert.assertFalse(product.containsKey("internalId"),
                    "Sensitive field exposed: internalId");

            Assert.assertFalse(product.containsKey("costPrice"),
                    "Sensitive field exposed: costPrice");

            Assert.assertFalse(product.containsKey("supplierInfo"),
                    "Sensitive field exposed: supplierInfo");
        }

        ReportLogger.info("Sensitive fields validation successful");
        ReportLogger.info("========== TEST COMPLETED ==========");
    }

    // 2️⃣ Verify API prevents SQL / injection-based query abuse
    @Test
    public void verifySqlInjectionProtection() {

        ReportLogger.info("========== TEST STARTED : Verify SQL Injection Protection ==========");

        String maliciousInput = "' OR 1=1 --";

        Response response = ProductApi.getProductsByCategory(maliciousInput);

        int statusCode = response.getStatusCode();

        ReportLogger.info("Response status code: " + statusCode);
        ReportLogger.info("Response body:\n" + response.asPrettyString());

        // Expect either error or empty result
        Assert.assertTrue(
                statusCode == 400 || statusCode == 404 || statusCode == 200,
                "Unexpected status code for injection attempt"
        );

        if (statusCode == 200) {
            int productCount = response.jsonPath().getList("products").size();
            Assert.assertEquals(productCount, 0,
                    "Injection attempt returned unexpected data");
        }

        ReportLogger.info("SQL injection protection validated");
        ReportLogger.info("========== TEST COMPLETED ==========");
    }

    // 3️⃣ Verify malformed requests do not crash the API
    @Test
    public void verifyMalformedRequestHandling() {

        ReportLogger.info("========== TEST STARTED : Verify Malformed Request Handling ==========");

        Response response = ProductApi.getProductsWithMalformedParams();

        int statusCode = response.getStatusCode();

        ReportLogger.info("Response status code: " + statusCode);
        ReportLogger.info("Response body:\n" + response.asPrettyString());

        Assert.assertTrue(
                statusCode >= 400 && statusCode < 500,
                "Malformed request should return client error"
        );

        ReportLogger.info("Malformed request handling validated");
        ReportLogger.info("========== TEST COMPLETED ==========");
    }
}
