package reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

    private static ExtentReports extent;

    public static ExtentReports getExtent() {
        if (extent == null) {
            ExtentSparkReporter reporter =
                    new ExtentSparkReporter("target/extent-report.html");

            reporter.config().setReportName("API Automation Report");
            reporter.config().setDocumentTitle("JWT Automation Results");

            extent = new ExtentReports();
            extent.attachReporter(reporter);
        }
        return extent;
    }
}
