package extentreports;

import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;

public class ExtentReportManager {

	private static ExtentReports report;
	static String reportName = "report_" + new Date().toString().replaceAll(":", "_").replaceAll(" ", "_")
			+ ".html";

	public static ExtentReports getReportInstance() {

		if (report == null) {

			ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(
					System.getProperty("user.dir") + "/reports/" + reportName);
			report = new ExtentReports();
			report.attachReporter(htmlReporter);

			report.setSystemInfo("OS", "Windows 10");
			report.setSystemInfo("Browser", "Chrome");

			htmlReporter.config().setDocumentTitle("Automation Test Suite");
			htmlReporter.config().setReportName("Test Report");
			htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
			htmlReporter.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");
		}

		return report;
	}
}