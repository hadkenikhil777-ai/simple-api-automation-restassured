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

public class ProductBusinessRuleTest extends BaseTest {

    @BeforeMethod
    public void init() {
        useDummyJsonApi(); // setup only
    }

    // 1️⃣ Verify minimumOrderQuantity is respected
    @Test
    public void verifyMinimumOrderQuantity() {

        ReportLogger.info("========== TEST STARTED : Verify Minimum Order Quantity ==========");

        Response response = ProductApi.getProducts();
        List<Map<String, Object>> products =
                response.jsonPath().getList("products");

        for (Map<String, Object> product : products) {

            int minQty =
                    ((Number) product.get("minimumOrderQuantity")).intValue();

            Assert.assertTrue(
                    minQty > 0,
                    "Invalid minimum order quantity"
            );
        }

        ReportLogger.info("Minimum order quantity validated successfully");
        ReportLogger.info("========== TEST COMPLETED ==========");
    }

    // 2️⃣ Verify products marked Low Stock have limited quantity
    @Test
    public void verifyLowStockProducts() {

        ReportLogger.info("========== TEST STARTED : Verify Low Stock Products ==========");

        Response response = ProductApi.getProducts();
        List<Map<String, Object>> products =
                response.jsonPath().getList("products");

        for (Map<String, Object> product : products) {

            String status = product.get("availabilityStatus").toString();
            int stock = ((Number) product.get("stock")).intValue();

            if ("Low Stock".equals(status)) {
                Assert.assertTrue(
                        stock <= 5,
                        "Low stock product has higher quantity than expected"
                );
            }
        }

        ReportLogger.info("Low stock validation successful");
        ReportLogger.info("========== TEST COMPLETED ==========");
    }

    // 3️⃣ Verify products with No return policy do not allow returns
    @Test
    public void verifyNoReturnPolicyProducts() {

        ReportLogger.info("========== TEST STARTED : Verify No Return Policy ==========");

        Response response = ProductApi.getProducts();
        List<Map<String, Object>> products =
                response.jsonPath().getList("products");

        for (Map<String, Object> product : products) {

            String returnPolicy = product.get("returnPolicy").toString();

            if ("No return policy".equalsIgnoreCase(returnPolicy)) {
                Assert.assertEquals(
                        returnPolicy,
                        "No return policy",
                        "Return policy mismatch"
                );
            }
        }

        ReportLogger.info("No return policy validation successful");
        ReportLogger.info("========== TEST COMPLETED ==========");
    }

    // 4️⃣ Verify warranty information is displayed correctly
    @Test
    public void verifyWarrantyInformation() {

        ReportLogger.info("========== TEST STARTED : Verify Warranty Information ==========");

        Response response = ProductApi.getProducts();
        List<String> warranties =
                response.jsonPath().getList("products.warrantyInformation");

        for (String warranty : warranties) {
            Assert.assertNotNull(warranty, "Warranty information missing");
            Assert.assertFalse(
                    warranty.trim().isEmpty(),
                    "Warranty information is empty"
            );
        }

        ReportLogger.info("Warranty information validation successful");
        ReportLogger.info("========== TEST COMPLETED ==========");
    }

    // 5️⃣ Verify shipping information matches product category
    @Test
    public void verifyShippingInformationByCategory() {

        ReportLogger.info("========== TEST STARTED : Verify Shipping Information By Category ==========");

        Response response = ProductApi.getProducts();
        List<Map<String, Object>> products =
                response.jsonPath().getList("products");

        for (Map<String, Object> product : products) {

            String category = product.get("category").toString();
            String shipping = product.get("shippingInformation").toString();

            Assert.assertNotNull(shipping, "Shipping information missing");

            // Example logical check: perishable goods should not ship in 1 month
            if ("groceries".equalsIgnoreCase(category)) {
                Assert.assertFalse(
                        shipping.toLowerCase().contains("1 month"),
                        "Groceries should not have long shipping time"
                );
            }
        }

        ReportLogger.info("Shipping information validation successful");
        ReportLogger.info("========== TEST COMPLETED ==========");
    }
}
