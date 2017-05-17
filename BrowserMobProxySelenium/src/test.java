
import java.io.File;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;

import org.junit.*;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.filters.RequestFilter;
import net.lightbody.bmp.proxy.CaptureType;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

public class test {

	String profilePath = "/Users/cqx931/adnauseamTestingProfile/";

	public WebDriver driver;
	public BrowserMobProxy proxy;

	public HttpHeaders adVisitHeaders;
	public HttpHeaders normalVisitHeaders;

	public boolean log = true;

	@Before
	public void setUp() {

		// start the proxy
		proxy = new BrowserMobProxyServer();
		proxy.start(0);

		// get the Selenium proxy object
		Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);

		// Set desired capability for using proxy Server.
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);

		// open chrome with adnauseam profile
		ChromeOptions options = new ChromeOptions();
		options.addArguments("user-data-dir=" + profilePath);
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);

		// Start the Browser with desired features.
		driver = new ChromeDriver(capabilities);

		// enable more detailed HAR capture, if desired (see CaptureType for the
		// complete list)
		proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);

		// create a new HAR with label
		proxy.newHar("test.html");

	}

	@Test
	public void testCaseOne() {

		testHeaders("http://rednoise.org/adntest/simple.html", "http://www.bonhams.com/departments/JWL/");
		Assert.assertTrue(compareHeaders(adVisitHeaders, normalVisitHeaders));
	}

	@After
	public void tearDown() {

		// get the HAR data
		Har har = proxy.getHar();

		// Write the HAR Data in a File
		File arFile = new File("sample.har");
		try {
			har.writeTo(arFile);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		clearAdVault();

		if (driver != null) {
			// Stop the BrowserMob Proxy Server
			proxy.stop();

			// Close the browser
			driver.quit();
		}

	}

	public void testHeaders(String visitUrl, String adUrl) {

		proxy.addRequestFilter(new RequestFilter() {

			@Override
			public HttpResponse filterRequest(io.netty.handler.codec.http.HttpRequest request, HttpMessageContents contents, HttpMessageInfo messageInfo) {

				// System.out.println(request.uri());
				if (request.uri().equals(adUrl)) {

					if (adVisitHeaders == null) {
						adVisitHeaders = request.headers();
						if (log) {
							System.out.println("--------- Ad Visit ----------");
							printAllHeaders(adVisitHeaders);
						}
						visitTheAdUrl(adUrl);
					}
					else {
						
						normalVisitHeaders = request.headers();
						if (log) {
							System.out.println("--------- Normal Visit ----------");
							printAllHeaders(normalVisitHeaders);
							System.out.println("---------------------------------");
						}

					}

				}
				return null;
			}

		});

		driver.get(visitUrl);

		try {
			Thread.sleep(15000);
		}
		catch (InterruptedException e) {
			System.out.println("got interrupted!");
		}

	}

	public void visitTheAdUrl(String url) {
		((JavascriptExecutor) driver).executeScript("window.open('" + url + "', '_blank');");
	}

	public void clearAdVault() {
		driver.get("chrome-extension://jflohihlppclmnhmjghiljidceocbbce/vault.html");
		driver.findElement(By.id("reset")).click();
		Alert alert = driver.switchTo().alert();
		alert.accept();
	}

	public boolean compareHeaders(HttpHeaders adVisit, HttpHeaders normalVisit) {

		if (log) System.out.println("Compare Headers: " + normalVisit.size() + " " + adVisit.size());

		if (normalVisit.size() == adVisit.size()) {
			boolean result = true;
			Iterator<Map.Entry<String, String>> itr = adVisit.iteratorAsString();

			while (itr.hasNext()) {
				Map.Entry<String, String> header = itr.next();
				String key = header.getKey();
				if (!normalVisit.get(key).equals(header.getValue())) {
					if (log) {
						System.out.println("Key:" + key + "; ");
						System.out.println("Ad Visit: " + header.getValue());
						System.out.println("Normal Visit: " + normalVisit.get(key));
					}
					result = false;
				}
			}
			if (result == true && log) System.out.println("Same Headers");
			return result;

		} else return false;
	}

	public void printAllHeaders(HttpHeaders headers) {

		Iterator<Map.Entry<String, String>> itr = headers.iteratorAsString();

		while (itr.hasNext()) {
			Map.Entry<String, String> header = itr.next();
			String key = header.getKey();
			System.out.println(key + ":" + header.getValue());

		}
	}

}