package reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import context.ResponseContext;
import io.restassured.response.Response;
import org.testng.*;

public class ExtentTestListener implements ITestListener {

    private static ExtentReports extent = ExtentManager.getExtent();
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        String className = result.getTestClass().getRealClass().getSimpleName();
        String methodName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();

        String testName = className + " :: " + methodName;
        ExtentTest extentTest = description == null
                ? extent.createTest(testName)
                : extent.createTest(testName, description);

        String[] groups = result.getMethod().getGroups();
        if (groups != null && groups.length > 0) {
            for (String group : groups) {
                extentTest.assignCategory(group);
            }
            extentTest.info("Groups: " + String.join(", ", groups));
        }

        extentTest.info("Suite: " + result.getTestContext().getSuite().getName());
        extentTest.info("Test: " + result.getTestContext().getName());
        extentTest.info("Class: " + result.getTestClass().getName());
        test.set(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().pass("Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.get().fail(result.getThrowable());

        Response lastResponse = ResponseContext.getLastResponse();
        if (lastResponse != null) {
            test.get().info("Last response status: " + lastResponse.getStatusCode());
            test.get().info("Last response headers: " + lastResponse.getHeaders());
            test.get().info("Last response body:\n" + lastResponse.asString());
        } else {
            test.get().info("No response captured for this test.");
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.get().skip(result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
        ReportPostProcessor.addStatusBadges(
                java.nio.file.Paths.get("target", "extent-report.html"));
    }

    public static ExtentTest getTest() {
        if (test.get() == null) {
            throw new IllegalStateException(
                    "ExtentTest is null. Ensure ExtentTestListener is registered."
            );
        }
        return test.get();
    }
}
