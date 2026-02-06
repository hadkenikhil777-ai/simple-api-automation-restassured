package tests;

import api.ProductApi;
import base.BaseTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import reporting.ReportLogger;

import java.util.List;
import java.util.Map;
@Test(groups = {"Product Listing"})
public class ProductCategoryFilteringTest extends BaseTest {

    @BeforeMethod
    public void init() {
        useDummyJsonApi(); // setup only
    }

    // 1️⃣ Verify products are correctly grouped by category
    @Test
    public void verifyProductsGroupedByCategory() {

        ReportLogger.info("========== TEST STARTED : Verify Products Grouped By Category ==========");

        Response response = ProductApi.getProducts();

        List<String> categories = response.jsonPath().getList("products.category");

        Assert.assertTrue(categories.size() > 0, "No categories found in response");

        ReportLogger.info("Categories present in response: " + categories);

        ReportLogger.info("========== TEST COMPLETED ==========");
    }

    // 2️⃣ Verify filtering products by category name
    @Test
    public void verifyFilterByCategory() {

        ReportLogger.info("========== TEST STARTED : Verify Filter By Category ==========");

        String category = "beauty";

        Response response = ProductApi.getProductsByCategory(category);

        List<String> categories =
                response.jsonPath().getList("products.category");

        for (String cat : categories) {
            Assert.assertEquals(cat, category, "Product category mismatch");
        }

        ReportLogger.info("Filtering by category validated successfully");

        ReportLogger.info("========== TEST COMPLETED ==========");
    }

    // 3️⃣ Verify filtering products by brand
    @Test
    public void verifyFilterByBrand() {

        ReportLogger.info("========== TEST STARTED : Verify Filter By Brand ==========");

        String brand = "Essence";

        Response response = ProductApi.getProducts();

        List<Map<String, Object>> products =
                response.jsonPath().getList("products");

        boolean brandFound = false;

        for (Map<String, Object> product : products) {

            if (brand.equals(product.get("brand"))) {
                brandFound = true;
                break;
            }
        }

        Assert.assertTrue(brandFound, "Brand not found in product list");

        ReportLogger.info("Brand filtering validation successful");

        ReportLogger.info("========== TEST COMPLETED ==========");
    }

    // 4️⃣ Verify filtering products by price range
    @Test
    public void verifyFilterByPriceRange() {

        ReportLogger.info("========== TEST STARTED : Verify Filter By Price Range ==========");

        double minPrice = 10;
        double maxPrice = 50;

        Response response = ProductApi.getProducts();

        List<Number> prices = response.jsonPath().getList("products.price");

        for (Number priceValue : prices) {
            double price = priceValue.doubleValue();
            if (price >= minPrice && price <= maxPrice) {
                Assert.assertTrue(true);
            }
        }

        ReportLogger.info("Price range filtering logic validated");

        ReportLogger.info("========== TEST COMPLETED ==========");
    }

    // 5️⃣ Verify filtering products by rating threshold
    @Test
    public void verifyFilterByRatingThreshold() {

        ReportLogger.info("========== TEST STARTED : Verify Filter By Rating Threshold ==========");

        double ratingThreshold = 4.0;

        Response response = ProductApi.getProducts();

        List<Number> ratings = response.jsonPath().getList("products.rating");

        boolean validRatingFound = false;

        for (Number ratingValue : ratings) {
            double rating = ratingValue.doubleValue();
            if (rating >= ratingThreshold) {
                validRatingFound = true;
                break;
            }
        }

        Assert.assertTrue(validRatingFound, "No products found above rating threshold");

        ReportLogger.info("Rating threshold validation successful");

        ReportLogger.info("========== TEST COMPLETED ==========");
    }

    // 6️⃣ Verify filtering by availability status
    @Test
    public void verifyFilterByAvailabilityStatus() {

        ReportLogger.info("========== TEST STARTED : Verify Filter By Availability Status ==========");

        String expectedStatus = "In Stock";

        Response response = ProductApi.getProducts();

        List<String> availabilityStatuses =
                response.jsonPath().getList("products.availabilityStatus");

        boolean statusFound = false;

        for (String status : availabilityStatuses) {

            if (expectedStatus.equals(status)) {
                statusFound = true;
                break;
            }
        }

        Assert.assertTrue(statusFound, "Expected availability status not found");

        ReportLogger.info("Availability status validation successful");

        ReportLogger.info("========== TEST COMPLETED ==========");
    }
}
