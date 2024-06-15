package Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class Assement1 {
	WebDriver driver;

	@BeforeTest
	public void Navigate_to_url() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.get("https://www.amazon.in/");
		driver.manage().window().maximize();
	}

//	Web Automation Using Sele

	@Test
	public void Search_the_product() throws InterruptedException, IOException, TesseractException {
		driver.findElement(By.id("twotabsearchtextbox")).sendKeys("Hindi books", Keys.ENTER);
		driver.findElement(By.xpath(
				"//span[text()='Premchand ki Anmol Kahaniya (Hindi) [Paperback] Munshi Premchand [Paperback] Munshi Premchand']"))
				.click();
		String currentUrl = driver.getCurrentUrl();
		Set<String> windowHandles = driver.getWindowHandles();
		Iterator<String> iterator = windowHandles.iterator();
		String parent = iterator.next();
		String child = iterator.next();
		driver.switchTo().window(child);

		// Get user reviews and store String value
		WebElement userratings = driver.findElement(By.id("acrCustomerReviewText"));
		String Userretingstext = userratings.getText();
		System.out.println("Userratings : " + Userretingstext);

		// Get product name and store String value
		WebElement producttitle = driver.findElement(By.id("productTitle"));
		String producttitletext = producttitle.getText();
		System.out.println("Name : " + producttitletext);

		// Get product price and store String value
		WebElement productamout = driver.findElement(By.xpath("//span[text()='179']"));
		String productamouttext = productamout.getText();
		System.out.println("Price : " + productamouttext);

		System.out.println(
				"=========================================================================================================");
		// Screenshot the page
		TakesScreenshot screenshot = (TakesScreenshot) (driver);
		File screenshotAs = screenshot.getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screenshotAs,
				new File("C:\\Users\\yugendran\\Desktop\\Workspace\\Assement\\Screenshot_Folder\\Screenshot.png"));

		// Convert to json format
		JSONArray bookList = new JSONArray();
		JSONObject bookDetails = new JSONObject();
		bookDetails.put("name", producttitletext);
		bookDetails.put("price", productamouttext);
		bookDetails.put("rating", Userretingstext);
		bookList.add(bookDetails);
		System.out.println(bookList);
		ITesseract instance = new Tesseract();

		// Text Extraction with OCR:
		// ============================

		try {
			instance.setDatapath("C:\\Users\\yugendran\\Downloads\\Tess4J-3.4.8-src\\Tess4J\\tessdata");
			String result = instance.doOCR(new File(
					"C:\\\\Users\\\\yugendran\\\\Desktop\\\\Workspace\\\\Assement\\\\Screenshot_Folder\\\\Screenshot.png"));
			System.out.println(result);
			// Store to  txt file get the string value
			FileWriter filewriter = new FileWriter("D:\\File\\file.txt");
			BufferedWriter bufferwriter = new BufferedWriter(filewriter);
			bufferwriter.write(result);
			
			// Data Comparison and Report Generation:

			// To get the product prices from the automation script and compare them with the prices extracted from the screenshot
			if (result.contains(productamouttext)) {
				System.out.println("Products with matching prices.");
			} else {
				System.out.println("Products with discrepancies in prices");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
