package com.jeptstore.framework;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public final class BrowserFactory {

	/**
	 * Creates a browser of specified type
	 * 
	 * @param browser
	 * @return
	 */
	public static WebDriver create(BROWSERS browser) {
		WebDriver driver;
		switch (browser) {
		case explorer:
			driver = createExplorer();
			break;
		case edge:
			driver = createEdge();
			break;
		case chrome:
			driver = createChrome();
			break;
		case firefox:
			driver = createFirefox();
			break;
		case headless:
			driver = createHeadless();
			break;
		default:
			throw new IllegalArgumentException("unsupported browser: " + browser);
		}

		prepareDriver(driver);
		return driver;
	}

	protected static void prepareDriver(WebDriver driver) {
		setWaitTimeouts(driver);
		moveToSecondDisplay(driver);
	}

	protected static void setWaitTimeouts(WebDriver driver) {
		driver.manage().timeouts().pageLoadTimeout(480, TimeUnit.SECONDS).setScriptTimeout(480, TimeUnit.SECONDS);
	}

	static WebDriver createHeadless() {
		WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("headless");
		return new ChromeDriver(options);
	}

	static WebDriver createChrome() {
		WebDriverManager.chromedriver().setup();
		ChromeOptions options = createChromeOptions();
		options.addArguments("window-size=1200x600");
		return new ChromeDriver(options);
	}

	static WebDriver createEdge() {
		WebDriverManager.edgedriver().setup();
		// EdgeOptions options = new EdgeOptions();
		// options.setCapability(capabilityName, value);
		return new EdgeDriver();
	}

	static WebDriver createExplorer() {
		WebDriverManager.iedriver().setup();
		// https://github.com/seleniumhq/selenium-google-code-issue-archive/issues/1795
		// https://stackoverflow.com/questions/12034969/internetexplorerdriver-zoom-level-error
		// https://stackoverflow.com/questions/24746777/selenium-nosuchwindowexception-in-ie-11
		// https://github.com/SeleniumHQ/selenium/wiki/InternetExplorerDriver#required-configuration
		InternetExplorerOptions ieOptions = new InternetExplorerOptions();
		ieOptions.setCapability("initialBrowserUrl", "https://www.google.com");
		ieOptions.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		ieOptions.setCapability("ignoreZoomSetting", true);
		return new InternetExplorerDriver(ieOptions);
	}

	static WebDriver createFirefox() {
		WebDriverManager.firefoxdriver().setup();
		FirefoxProfile profile = getFirefoxProfileNeverAsk();
		profile.setAcceptUntrustedCertificates(true);
		profile.setAssumeUntrustedCertificateIssuer(false);
		FirefoxOptions options = new FirefoxOptions();
		options.setProfile(profile);
		return new FirefoxDriver(options);
	}

	static FirefoxProfile getFirefoxProfileNeverAsk() {
		String downloadDir = System.getProperty("user.dir") + "\\downloads";
		File dir = new File(downloadDir);
		dir.mkdirs();
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("browser.helperApps.alwaysAsk.force", false);
		profile.setPreference("browser.download.manager.showWhenStarting", false);
		profile.setPreference("browser.download.folderList", 2);
		profile.setPreference("browser.download.dir", downloadDir);
		profile.setPreference("browser.helperApps.neverAsk.openFile", "text/csv,text/txt");
		profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
				"application/csv, text/csv, application/x-zip, application/zip, application/x-zip-compressed");
		return profile;
	}

	/**
	 * Will move the browser to the second display if such exists
	 * 
	 * @param driver
	 */
	public static void moveToSecondDisplay(WebDriver driver) {
		GraphicsDevice[] screens = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();

		for (GraphicsDevice screen : screens) {
			Rectangle rect = screen.getDefaultConfiguration().getBounds();
			if (!((rect.x == 0) && (rect.y == 0))) {
				driver.manage().window().setPosition(new Point(rect.x, rect.y));
			}
		}
		driver.manage().window().maximize();
	}

	private static ChromeOptions createChromeOptions() {
		ChromeOptions options = new ChromeOptions();
		List<String> args = new LinkedList<String>();
		args.add("--disable-infobars");
		args.add("--disable-notifications");
		args.add("--start-maximized");
		options.addArguments(args);
		return options;
	}
}