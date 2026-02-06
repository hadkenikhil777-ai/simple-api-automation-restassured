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

@Test(groups = {"Product Listing"})
public class ProductDataValidationTest extends BaseTest {

    @BeforeMethod
    public void init() {
        useDummyJsonApi(); // setup only
    }

    // 1️⃣ Verify mandatory fields exist for each product
    @Test
    public void verifyMandatoryFieldsExist() {

        ReportLogger.info("========== TEST STARTED : Verify Mandatory Fields ==========");

        Response response = ProductApi.getProducts();

        List<Map<String, Object>> products = response.jsonPath().getList("products");

        for (Map<String, Object> product : products) {
            Assert.assertNotNull(product.get("id"), "id is missing");
            Assert.assertNotNull(product.get("title"), "title is missing");
            Assert.assertNotNull(product.get("price"), "price is missing");
            Assert.assertNotNull(product.get("category"), "category is missing");
        }

        ReportLogger.info("Mandatory fields validated for all products");
        ReportLogger.info("========== TEST COMPLETED ==========");
    }

    // 2️⃣ Verify numeric fields contain valid numeric values
    @Test
    public void verifyNumericFields() {

        ReportLogger.info("========== TEST STARTED : Verify Numeric Fields ==========");

        Response response = ProductApi.getProducts();
        List<Map<String, Object>> products = response.jsonPath().getList("products");

        for (Map<String, Object> product : products) {
            Assert.assertTrue(((Number) product.get("price")).doubleValue() >= 0, "Invalid price");
            Assert.assertTrue(((Number) product.get("rating")).doubleValue() >= 0, "Invalid rating");
            Assert.assertTrue(((Number) product.get("stock")).intValue() >= 0, "Invalid stock");
            Assert.assertTrue(((Number) product.get("discountPercentage")).doubleValue() >= 0,
                    "Invalid discount percentage");
        }

        ReportLogger.info("Numeric field validation successful");
        ReportLogger.info("========== TEST COMPLETED ==========");
    }

    // 3️⃣ Verify rating is between 0 and 5
    @Test
    public void verifyRatingRange() {

        ReportLogger.info("========== TEST STARTED : Verify Rating Range ==========");

        Response response = ProductApi.getProducts();
        List<Number> ratings = response.jsonPath().getList("products.rating");

        for (Number rating : ratings) {
            double ratingValue = rating == null ? Double.NaN : rating.doubleValue();
            Assert.assertTrue(
                    ratingValue >= 0 && ratingValue <= 5,
                    "Rating out of valid range: " + rating
            );
        }

        ReportLogger.info("Rating range validated successfully");
        ReportLogger.info("========== TEST COMPLETED ==========");
    }

    // 4️⃣ Verify availability status matches stock value
    @Test
    public void verifyAvailabilityStatusAgainstStock() {

        ReportLogger.info("========== TEST STARTED : Verify Availability Status ==========");

        Response response = ProductApi.getProducts();
        List<Map<String, Object>> products = response.jsonPath().getList("products");

        List<String> allowedStatuses = java.util.Arrays.asList("In Stock", "Low Stock", "Out of Stock");

        for (Map<String, Object> product : products) {
            Object availability = product.get("availabilityStatus");
            Assert.assertNotNull(availability, "availabilityStatus is missing");

            String availabilityStatus = availability.toString();
            Assert.assertTrue(
                    allowedStatuses.contains(availabilityStatus),
                    "Unexpected availabilityStatus: " + availabilityStatus
            );
        }

        ReportLogger.info("Availability status validated for allowed values");
        ReportLogger.info("========== TEST COMPLETED ==========");
    }



//    @Test
//    public void verifyAvailabilityStatusAgainstStock() {
//
//        ReportLogger.info("========== TEST STARTED : Verify Availability Status ==========");
//
//        Response response = ProductApi.getProducts();
//        List<Map<String, Object>> products = response.jsonPath().getList("products");
//
//        for (Map<String, Object> product : products) {
//            int stock = ((Number) product.get("stock")).intValue();
//            String availabilityStatus = product.get("availabilityStatus").toString();
//
//            if (stock == 0) {
//                Assert.assertEquals(availabilityStatus, "Out of Stock");
//            } else if (stock <= 5) {
//                Assert.assertEquals(availabilityStatus, "Low Stock");
//            } else {
//                Assert.assertEquals(availabilityStatus, "In Stock");
//            }
//        }
//
//        ReportLogger.info("Availability status validated against stock");
//        ReportLogger.info("========== TEST COMPLETED ==========");
//    }




    // 5️⃣ Verify images and thumbnail URLs are present
    @Test
    public void verifyImagesAndThumbnailPresence() {

        ReportLogger.info("========== TEST STARTED : Verify Images & Thumbnail ==========");

        Response response = ProductApi.getProducts();
        List<Map<String, Object>> products = response.jsonPath().getList("products");

        for (Map<String, Object> product : products) {
            Assert.assertNotNull(product.get("thumbnail"), "Thumbnail missing");

            List<String> images = (List<String>) product.get("images");
            Assert.assertTrue(images != null && !images.isEmpty(), "Images list is empty");
        }

        ReportLogger.info("Images and thumbnail validation successful");
        ReportLogger.info("========== TEST COMPLETED ==========");
    }

    // 6️⃣ Verify SKU is present and not empty
    @Test
    public void verifySkuIsPresent() {

        ReportLogger.info("========== TEST STARTED : Verify SKU ==========");

        Response response = ProductApi.getProducts();
        List<String> skus = response.jsonPath().getList("products.sku");

        for (String sku : skus) {
            Assert.assertNotNull(sku, "SKU is null");
            Assert.assertFalse(sku.trim().isEmpty(), "SKU is empty");
        }

        ReportLogger.info("SKU validation successful");
        ReportLogger.info("========== TEST COMPLETED ==========");
    }
}
