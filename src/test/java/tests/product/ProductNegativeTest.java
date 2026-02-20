package tests.product;

import api.ProductApi;
import base.BaseTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import reporting.ReportLogger;
import utils.AssertUtils;


@Test(groups = {"Product Listing"})
public class ProductNegativeTest extends BaseTest {

    @BeforeMethod
    public void init() {
        useDummyJsonApi(); // setup only
    }

    // 1️⃣ Verify API behavior when limit = 0
    @Test
    public void verifyLimitZero() {

        ReportLogger.info("========== TEST STARTED : Verify limit = 0 ==========");

        Response response = ProductApi.getProductsWithLimit(0);

        AssertUtils.assertStatusIn(response, "Limit zero request", 200);

        int productCount = response.jsonPath().getList("products").size();
        int responseLimit = response.jsonPath().getInt("limit");
        int total = response.jsonPath().getInt("total");

        // DummyJSON may normalize limit=0 to default/full dataset instead of returning empty list.
        Assert.assertTrue(
                (responseLimit == 0 && productCount == 0)
                        || (responseLimit > 0 && productCount == responseLimit)
                        || (responseLimit == total && productCount == total),
                "Unexpected normalization behavior for limit=0"
        );

        ReportLogger.info("Limit zero scenario validated");
        ReportLogger.info("========== TEST COMPLETED ==========");
    }

    // 2️⃣ Verify API behavior when skip exceeds total count
    @Test
    public void verifySkipBeyondTotal() {

        ReportLogger.info("========== TEST STARTED : Verify skip beyond total ==========");

        Response response = ProductApi.getProductsWithPagination(10, 10000);

        AssertUtils.assertStatusIn(response, "Skip beyond total", 200);

        int productCount = response.jsonPath().getList("products").size();

        Assert.assertEquals(productCount, 0, "Products should be empty when skip exceeds total");

        ReportLogger.info("Skip beyond total scenario validated");
        ReportLogger.info("========== TEST COMPLETED ==========");
    }

    // 3️⃣ Verify API behavior with invalid query parameters
    @Test
    public void verifyInvalidQueryParameter() {

        ReportLogger.info("========== TEST STARTED : Verify invalid query parameter ==========");

        Response response = ProductApi.getProductsWithInvalidParam();

        int statusCode = response.getStatusCode();

        ReportLogger.info("Response status code: " + statusCode);

        Assert.assertTrue(
                statusCode == 400 || statusCode == 422 || statusCode == 200,
                "Unexpected status code for invalid parameter"
        );

        ReportLogger.info("Invalid query parameter scenario validated");
        ReportLogger.info("========== TEST COMPLETED ==========");
    }

    // 4️⃣ Verify API returns proper error for unsupported HTTP method
    @Test
    public void verifyUnsupportedHttpMethod() {

        ReportLogger.info("========== TEST STARTED : Verify unsupported HTTP method ==========");

        Response response = ProductApi.postProductsEndpoint();

        int statusCode = response.getStatusCode();

        ReportLogger.info("Response status code: " + statusCode);

        Assert.assertTrue(
                statusCode >= 400,
                "Expected non-success status for unsupported method"
        );

        ReportLogger.info("Unsupported HTTP method scenario validated");
        ReportLogger.info("========== TEST COMPLETED ==========");
    }

    // 5️⃣ Verify API response when no products match filters
    @Test
    public void verifyNoMatchingProducts() {

        ReportLogger.info("========== TEST STARTED : Verify no matching products ==========");

        Response response = ProductApi.getProductsByCategory("nonexistent-category");

        AssertUtils.assertStatusIn(response, "No matching products", 200);

        int productCount = response.jsonPath().getList("products").size();

        Assert.assertEquals(productCount, 0, "Expected no products for invalid category");

        ReportLogger.info("No matching products scenario validated");
        ReportLogger.info("========== TEST COMPLETED ==========");
    }
}
