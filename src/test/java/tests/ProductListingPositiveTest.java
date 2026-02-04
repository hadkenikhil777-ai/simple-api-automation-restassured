package tests;

import api.ProductApi;
import base.BaseTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import reporting.ReportLogger;
import utils.AssertUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Test(groups = {"Product Listing"})
public class ProductListingPositiveTest extends BaseTest {

    @BeforeMethod
    public void init() {
        useDummyJsonApi(); // Setup only (no logging here)
    }

    // 1️⃣ Verify API returns 200 OK for valid request
    @Test
    public void verifyStatusCode200() {

        ReportLogger.info("========== TEST STARTED : Verify Status Code 200 ==========");

        Response response = ProductApi.getProducts();

        ReportLogger.info("Response Status Code: " + response.getStatusCode());
        ReportLogger.info("Response Body:\n" + response.asPrettyString());

        AssertUtils.assertStatusIn(response, "Get products", 200);

        ReportLogger.info("========== TEST COMPLETED ==========");
    }

    // 2️⃣ Verify response contains products, total, skip, limit fields
    @Test
    public void verifyResponseStructure() {

        ReportLogger.info("========== TEST STARTED : Verify Response Structure ==========");

        Response response = ProductApi.getProducts();

        AssertUtils.assertNotNull(response.jsonPath().get("products"), "products field");
        AssertUtils.assertNotNull(response.jsonPath().get("total"), "total field");
        AssertUtils.assertNotNull(response.jsonPath().get("skip"), "skip field");
        AssertUtils.assertNotNull(response.jsonPath().get("limit"), "limit field");

        ReportLogger.info("Response structure validated successfully");

        ReportLogger.info("========== TEST COMPLETED ==========");
    }

    // 3️⃣ Verify products array is not empty when data exists
    @Test
    public void verifyProductsArrayNotEmpty() {

        ReportLogger.info("========== TEST STARTED : Verify Products Array Not Empty ==========");

        Response response = ProductApi.getProducts();

        int productCount = response.jsonPath().getList("products").size();

        ReportLogger.info("Product count: " + productCount);

        Assert.assertTrue(productCount > 0, "Products list is empty");

        ReportLogger.info("========== TEST COMPLETED ==========");
    }

    // 4️⃣ Verify each product has a unique id
    @Test
    public void verifyUniqueProductIds() {

        ReportLogger.info("========== TEST STARTED : Verify Unique Product IDs ==========");

        Response response = ProductApi.getProducts();

        List<Integer> productIds = response.jsonPath().getList("products.id");

        Set<Integer> uniqueIds = new HashSet<>(productIds);

        Assert.assertEquals(
                productIds.size(),
                uniqueIds.size(),
                "Duplicate product IDs found"
        );

        ReportLogger.info("All product IDs are unique");

        ReportLogger.info("========== TEST COMPLETED ==========");
    }

    // 5️⃣ Verify products are returned according to limit value
    @Test
    public void verifyProductsCountAsPerLimit() {

        ReportLogger.info("========== TEST STARTED : Verify Products Count As Per Limit ==========");

        int limit = 10;

        Response response = ProductApi.getProductsWithLimit(limit);

        int productCount = response.jsonPath().getList("products").size();

        ReportLogger.info("Expected limit: " + limit);
        ReportLogger.info("Actual product count: " + productCount);

        Assert.assertEquals(productCount, limit, "Product count does not match limit");

        ReportLogger.info("========== TEST COMPLETED ==========");
    }

    // 6️⃣ Verify pagination using skip & limit works correctly
    @Test
    public void verifyPaginationUsingSkipAndLimit() {

        ReportLogger.info("========== TEST STARTED : Verify Pagination ==========");

        int limit = 5;
        int skip = 5;

        Response response = ProductApi.getProductsWithPagination(limit, skip);

        AssertUtils.assertStatusIn(response, "Get products with pagination", 200);

        int productCount = response.jsonPath().getList("products").size();
        int firstProductId = response.jsonPath().getInt("products[0].id");

        ReportLogger.info("Product count: " + productCount);
        ReportLogger.info("First product ID: " + firstProductId);

        Assert.assertEquals(productCount, limit, "Pagination limit mismatch");
        Assert.assertTrue(firstProductId > skip, "Pagination skip not applied correctly");

        ReportLogger.info("========== TEST COMPLETED ==========");
    }
}
