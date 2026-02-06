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

@Test(groups = {"Product Listing"})
public class ProductSortingTest extends BaseTest {

    @BeforeMethod
    public void init() {
        useDummyJsonApi(); // setup only
    }

    // 1️⃣ Verify products sorted by price ascending
    @Test
    public void verifySortByPriceAscending() {

        ReportLogger.info("========== TEST STARTED : Sort By Price Ascending ==========");

        Response response = ProductApi.getProductsSorted("price", "asc");

        AssertUtils.assertStatusIn(response, "Sort by price asc", 200);

        List<Number> prices = response.jsonPath().getList("products.price");

        for (int i = 0; i < prices.size() - 1; i++) {
            Assert.assertTrue(
                    prices.get(i).doubleValue() <= prices.get(i + 1).doubleValue(),
                    "Prices are not sorted in ascending order"
            );
        }

        ReportLogger.info("Price ascending sort validated");
        ReportLogger.info("========== TEST COMPLETED ==========");
    }

    // 2️⃣ Verify products sorted by price descending
    @Test
    public void verifySortByPriceDescending() {

        ReportLogger.info("========== TEST STARTED : Sort By Price Descending ==========");

        Response response = ProductApi.getProductsSorted("price", "desc");

        AssertUtils.assertStatusIn(response, "Sort by price desc", 200);

        List<Number> prices = response.jsonPath().getList("products.price");

        for (int i = 0; i < prices.size() - 1; i++) {
            Assert.assertTrue(
                    prices.get(i).doubleValue() >= prices.get(i + 1).doubleValue(),
                    "Prices are not sorted in descending order"
            );
        }

        ReportLogger.info("Price descending sort validated");
        ReportLogger.info("========== TEST COMPLETED ==========");
    }

    // 3️⃣ Verify products sorted by rating
    @Test
    public void verifySortByRatingDescending() {

        ReportLogger.info("========== TEST STARTED : Sort By Rating Descending ==========");

        Response response = ProductApi.getProductsSorted("rating", "desc");

        AssertUtils.assertStatusIn(response, "Sort by rating desc", 200);

        List<Number> ratings = response.jsonPath().getList("products.rating");

        for (int i = 0; i < ratings.size() - 1; i++) {
            Assert.assertTrue(
                    ratings.get(i).doubleValue() >= ratings.get(i + 1).doubleValue(),
                    "Ratings are not sorted correctly"
            );
        }

        ReportLogger.info("Rating sort validated");
        ReportLogger.info("========== TEST COMPLETED ==========");
    }

    // 4️⃣ Verify products sorted by discountPercentage
    @Test
    public void verifySortByDiscountDescending() {

        ReportLogger.info("========== TEST STARTED : Sort By Discount Descending ==========");

        Response response = ProductApi.getProductsSorted("discountPercentage", "desc");

        AssertUtils.assertStatusIn(response, "Sort by discount desc", 200);

        List<Number> discounts = response.jsonPath().getList("products.discountPercentage");

        for (int i = 0; i < discounts.size() - 1; i++) {
            Assert.assertTrue(
                    discounts.get(i).doubleValue() >= discounts.get(i + 1).doubleValue(),
                    "Discounts are not sorted correctly"
            );
        }

        ReportLogger.info("Discount sort validated");
        ReportLogger.info("========== TEST COMPLETED ==========");
    }

    // 5️⃣ Verify sorting maintains correct pagination
    @Test
    public void verifySortingWithPagination() {

        ReportLogger.info("========== TEST STARTED : Sort With Pagination ==========");

        int limit = 5;
        int skip = 5;

        Response response =
                ProductApi.getProductsSortedWithPagination("price", "asc", limit, skip);

        AssertUtils.assertStatusIn(response, "Sort with pagination", 200);

        List<Number> prices = response.jsonPath().getList("products.price");

        // Validate size
        Assert.assertEquals(prices.size(), limit, "Pagination limit mismatch");

        // Validate sorting
        for (int i = 0; i < prices.size() - 1; i++) {
            Assert.assertTrue(
                    prices.get(i).doubleValue() <= prices.get(i + 1).doubleValue(),
                    "Prices not sorted correctly with pagination"
            );
        }

        ReportLogger.info("Sorting with pagination validated");
        ReportLogger.info("========== TEST COMPLETED ==========");
    }
}
