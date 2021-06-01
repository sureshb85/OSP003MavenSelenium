package com.jeptstore.framework;

import org.openqa.selenium.WebDriver;

public class TestCase {
	protected WebDriver driver;

	private WebDriver startWebDriver(String browser) {
		// The browser may have been started from outside
		String msg = ">>> DRIVER : %s TestCase %s";
		if (driver == null) {
			// driver is not started so we start it
			driver = BrowserFactory.create(BROWSERS.valueOf(browser));

			if (driver != null)
				System.out.println(String.format(msg, driver.getWindowHandle(), this));
		}
		return driver;
	}

	public static void main(String[] args) {
		TestCase t = new TestCase();
		WebDriver driver = t.startWebDriver("edge");
		driver.get("https://www.google.com");
		System.err.println(driver.getTitle() + "title");
	}
}
