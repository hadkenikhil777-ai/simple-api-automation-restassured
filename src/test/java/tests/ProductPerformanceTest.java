package tests;

import api.ProductApi;
import base.BaseTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import reporting.ReportLogger;
import utils.AssertUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ProductPerformanceTest extends BaseTest {

    @BeforeMethod
    public void init() {
        useDummyJsonApi(); // setup only
    }

    // 1️⃣ Verify API response time is within acceptable SLA
    @Test
    public void verifyResponseTimeWithinSla() {

        ReportLogger.info("========== TEST STARTED : Verify Response Time SLA ==========");

        Response response = ProductApi.getProducts();

        long responseTime = response.time();

        ReportLogger.info("Response time: " + responseTime + " ms");

        // SLA: 2000 ms
        AssertUtils.assertResponseTimeLessThan(
                responseTime,
                2000,
                "Product listing response time"
        );

        ReportLogger.info("========== TEST COMPLETED ==========");
    }

    // 2️⃣ Verify API handles large product datasets efficiently
    @Test
    public void verifyLargeDatasetHandling() {

        ReportLogger.info("========== TEST STARTED : Verify Large Dataset Handling ==========");

        int largeLimit = 100;

        Response response = ProductApi.getProductsWithLimit(largeLimit);

        AssertUtils.assertStatusIn(response, "Large dataset request", 200);

        int productCount = response.jsonPath().getList("products").size();

        ReportLogger.info("Returned product count: " + productCount);

        Assert.assertTrue(productCount > 0, "No products returned for large dataset");

        // Response time check for larger payload
        long responseTime = response.time();
        ReportLogger.info("Response time for large dataset: " + responseTime + " ms");

        AssertUtils.assertResponseTimeLessThan(
                responseTime,
                3000,
                "Large dataset response time"
        );

        ReportLogger.info("========== TEST COMPLETED ==========");
    }

    // 3️⃣ Verify consistent response under multiple parallel requests
    @Test
    public void verifyParallelRequestsConsistency() throws InterruptedException, ExecutionException {

        ReportLogger.info("========== TEST STARTED : Parallel Requests Consistency ==========");

        int numberOfRequests = 5;

        ExecutorService executor = Executors.newFixedThreadPool(numberOfRequests);
        List<Future<Response>> futures = new ArrayList<>();

        for (int i = 0; i < numberOfRequests; i++) {
            futures.add(executor.submit(() -> ProductApi.getProducts()));
        }

        for (Future<Response> future : futures) {
            Response response = future.get();

            AssertUtils.assertStatusIn(response, "Parallel request", 200);

            int productCount = response.jsonPath().getList("products").size();

            Assert.assertTrue(productCount > 0, "Invalid response in parallel execution");
        }

        executor.shutdown();

        ReportLogger.info("Parallel request consistency validated");
        ReportLogger.info("========== TEST COMPLETED ==========");
    }
}
