package utils;

import io.restassured.response.Response;
import org.testng.Assert;
import reporting.ReportLogger;

import java.util.Arrays;

public class AssertUtils {

    private AssertUtils() {
        // prevent object creation
    }

    public static void assertStatusIn(
            Response response,
            String context,
            int... expectedStatuses
    ) {
        int actual = response.getStatusCode();
        boolean match = false;
        for (int expected : expectedStatuses) {
            if (actual == expected) {
                match = true;
                break;
            }
        }

        if (match) {
            ReportLogger.pass(context + " status: " + actual);
        } else {
            ReportLogger.fail(
                    context + " status expected " + Arrays.toString(expectedStatuses)
                            + " but was " + actual
            );
            ReportLogger.fail("Response body:\n" + response.asString());
            Assert.fail(context + " status mismatch");
        }
    }

    public static void assertStatusAtLeast(
            Response response,
            String context,
            int minStatus
    ) {
        int actual = response.getStatusCode();
        if (actual >= minStatus) {
            ReportLogger.pass(context + " status: " + actual);
        } else {
            ReportLogger.fail(
                    context + " status expected >= " + minStatus + " but was " + actual
            );
            ReportLogger.fail("Response body:\n" + response.asString());
            Assert.fail(context + " status too low");
        }
    }

    public static void assertNotNull(Object value, String context) {
        if (value != null) {
            ReportLogger.pass(context + " is present");
        } else {
            ReportLogger.fail(context + " is null");
            Assert.fail(context + " is null");
        }
    }

    public static void assertEquals(Object actual, Object expected, String context) {
        if (expected == null ? actual == null : expected.equals(actual)) {
            ReportLogger.pass(context + " equals " + expected);
        } else {
            ReportLogger.fail(
                    context + " expected " + expected + " but was " + actual
            );
            Assert.fail(context + " mismatch");
        }
    }

    public static void assertResponseTimeLessThan(
            long responseTimeMs,
            long maxMs,
            String context
    ) {
        if (responseTimeMs < maxMs) {
            ReportLogger.pass(context + " within SLA (" + responseTimeMs + " ms)");
        } else {
            ReportLogger.fail(
                    context + " exceeded SLA (" + responseTimeMs + " ms, limit " + maxMs + " ms)"
            );
            Assert.fail(context + " response time too high");
        }
    }
}
