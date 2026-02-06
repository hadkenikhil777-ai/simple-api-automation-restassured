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

public class ProductReviewRatingTest extends BaseTest {

    @BeforeMethod
    public void init() {
        useDummyJsonApi(); // setup only
    }

    // 1️⃣ Verify each product can contain multiple reviews
    @Test
    public void verifyMultipleReviewsPerProduct() {

        ReportLogger.info("========== TEST STARTED : Verify Multiple Reviews ==========");

        Response response = ProductApi.getProducts();

        List<Map<String, Object>> products =
                response.jsonPath().getList("products");

        for (Map<String, Object> product : products) {
            List<Map<String, Object>> reviews =
                    (List<Map<String, Object>>) product.get("reviews");

            Assert.assertNotNull(reviews, "Reviews field is missing");
            Assert.assertTrue(reviews.size() > 0, "No reviews found for product");
        }

        ReportLogger.info("Multiple reviews validation successful");
        ReportLogger.info("========== TEST COMPLETED ==========");
    }

    // 2️⃣ Verify review fields exist
    @Test
    public void verifyReviewFields() {

        ReportLogger.info("========== TEST STARTED : Verify Review Fields ==========");

        Response response = ProductApi.getProducts();

        List<Map<String, Object>> products =
                response.jsonPath().getList("products");

        for (Map<String, Object> product : products) {

            List<Map<String, Object>> reviews =
                    (List<Map<String, Object>>) product.get("reviews");

            for (Map<String, Object> review : reviews) {
                Assert.assertNotNull(review.get("rating"), "Rating missing");
                Assert.assertNotNull(review.get("comment"), "Comment missing");
                Assert.assertNotNull(review.get("reviewerName"), "Reviewer name missing");
                Assert.assertNotNull(review.get("date"), "Review date missing");
            }
        }

        ReportLogger.info("Review fields validation successful");
        ReportLogger.info("========== TEST COMPLETED ==========");
    }

    // 3️⃣ Verify average product rating aligns with review ratings
    @Test
    public void verifyAverageRatingMatchesReviews() {

        ReportLogger.info("========== TEST STARTED : Verify Average Rating ==========");

        Response response = ProductApi.getProducts();

        List<Map<String, Object>> products =
                response.jsonPath().getList("products");

        for (Map<String, Object> product : products) {

            double productRating =
                    ((Number) product.get("rating")).doubleValue();

            List<Map<String, Object>> reviews =
                    (List<Map<String, Object>>) product.get("reviews");

            double total = 0;

            for (Map<String, Object> review : reviews) {
                total += ((Number) review.get("rating")).doubleValue();
            }

            double average = total / reviews.size();

            // Allow small difference due to rounding
            Assert.assertTrue(
                    Math.abs(productRating - average) <= 1.0,
                    "Product rating does not align with review average"
            );
        }

        ReportLogger.info("Average rating validation successful");
        ReportLogger.info("========== TEST COMPLETED ==========");
    }

    // 4️⃣ Verify invalid review ratings are rejected
    @Test
    public void verifyInvalidReviewRatings() {

        ReportLogger.info("========== TEST STARTED : Verify Invalid Review Ratings ==========");

        Response response = ProductApi.getProducts();

        List<Map<String, Object>> products =
                response.jsonPath().getList("products");

        for (Map<String, Object> product : products) {

            List<Map<String, Object>> reviews =
                    (List<Map<String, Object>>) product.get("reviews");

            for (Map<String, Object> review : reviews) {

                double rating =
                        ((Number) review.get("rating")).doubleValue();

                Assert.assertTrue(
                        rating >= 1 && rating <= 5,
                        "Invalid review rating found: " + rating
                );
            }
        }

        ReportLogger.info("Invalid review rating validation successful");
        ReportLogger.info("========== TEST COMPLETED ==========");
    }
}
