package extentreports;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import io.github.bonigarcia.wdm.WebDriverManager;

public class GoogleTest {

	String SCREENSHOTS = System.getProperty("user.dir") + "\\screenshots\\";
	public ExtentTest logger;
	public ExtentReports report = ExtentReportManager.getReportInstance();
	WebDriver driver;

	@BeforeMethod
	public void startDriver() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.get("https://www.google.com");
	}

	@Test(enabled = false)
	public void googleTest() throws IOException {
		logger = report.createTest("Google Test");
		driver.findElement(By.name("q")).sendKeys("Selenium");
		logger.log(Status.PASS, "TestCasePassed");
	}

	@Test()
	public void googleTitleTest() throws IOException {
		logger = report.createTest("Google Title Test");
		logger.log(Status.INFO, "title: " + driver.getTitle());
		logger.log(Status.FAIL, "TestCasePassed");
		Assert.assertEquals(driver.getTitle(), "Google");
		logger.log(Status.PASS, "TestCasePassed");
		logger.addScreenCaptureFromPath(takeScreenShot("Google Title Test"));
	}

	@AfterMethod
	public void closeDriver() {
		if (driver != null) {
			driver.close();
		}
		report.flush();
	}

	String takeScreenShot(String screenShotName) {
		// screen shot using takeScreenShot interface
		TakesScreenshot screenShot = (TakesScreenshot) driver;
		File screenshotFile = screenShot.getScreenshotAs(OutputType.FILE);
		// dynamic file name

		String screenShotExtension = new Date().toString().replace(" ", "_").replace(":", "_") + screenShotName
				+ ".png";
		String ScreenShotLocation = SCREENSHOTS + screenShotExtension;
		File iamgeFile = new File(ScreenShotLocation);
		try {
			FileUtils.copyFile(screenshotFile, iamgeFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ScreenShotLocation;
	}
}
