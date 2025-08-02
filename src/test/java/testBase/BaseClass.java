package testBase;
 



import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
//import java.util.Properties;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

public class BaseClass {
	public static WebDriver driver;
	public Logger logger;//log4j
    public Properties p;
	
	@BeforeClass(groups= {"Sanity", "Regression", "Master"})
	@Parameters({"os","browser"})
	public void setup(String os, String br) throws IOException
	{
		//Loading congif.properties file
		FileReader file = new FileReader("./src//test//resources//config.properties"); // .//--representing current project location
		p=new Properties();
		p.load(file);
		
		logger = LogManager.getLogger(this.getClass());
		
		System.setProperty("webdriver.edge.driver", "C:/Users/2413569/Downloads/edgedriver_win64 (1)/msedgedriver.exe");
		//if the environment is remote
		   if(p.getProperty("execution_env").equalsIgnoreCase("remote"))
		   {
			   DesiredCapabilities cap = new DesiredCapabilities();
				
			   if(os.equalsIgnoreCase("windows")) 
			   {
				cap.setPlatform(Platform.WIN11); 
			   }
			   else if(os.equalsIgnoreCase("windows"))
			   {
				 cap.setPlatform(Platform.MAC);  
			   }
			   else
			   {
				   System.out.println("No matching os");
				   return;
			   }
			   
			   //browser
		
			   switch(br.toLowerCase())
			   {
			   case "chrome" : cap.setBrowserName("chrome"); break;
			   case "edge" : cap.setBrowserName("MicrosoftEdge"); break;
			   default: System.out.println("No matching browser"); break;
			   }
			   
				driver = new RemoteWebDriver(new URL("http://10.232.4.81:4444/wd/hub"), cap);
		   }
		
		   if(p.getProperty("execution_env").equalsIgnoreCase("local"))
		   {   
			   switch(br.toLowerCase())
				{
				case "chrome" : driver=new ChromeDriver(); break;
				case "edge" : driver=new EdgeDriver(); break;
				case "firefox" : driver=new FirefoxDriver(); break;
				default: System.out.println("Invalid browser name...."); return;
		       }
			   
			  
  }
		
	driver.manage().deleteAllCookies();
	driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	driver.get(p.getProperty("appURL"));//reading url from properties file
	driver.manage().window().maximize();
	}
	

	@AfterClass(groups= {"Sanity", "Regression", "Master"})
	public void tearDown()
	{
		driver.quit();
	}
		
	public String randomeString() {
		String generatedStr = RandomStringUtils.randomAlphabetic(5);
		return generatedStr;
	}
	
	public String randomeNum() {
		String generatedNum = RandomStringUtils.randomNumeric(10);
		return generatedNum;
	}
	
	public String randomeAlphaNumeric() {
		String generatedNum = RandomStringUtils.randomNumeric(3);
		String generatedStr = RandomStringUtils.randomAlphabetic(3);
		return (generatedStr+"@" + generatedNum);
	}
	
	public String captureScreen(String tname) throws IOException {

		String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
				
		TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
		File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
		
		String targetFilePath=System.getProperty("user.dir")+"\\screenshots\\" + tname + "_" + timeStamp + ".png";
		File targetFile=new File(targetFilePath);
		
		sourceFile.renameTo(targetFile);
			
		return targetFilePath;

	}
	
	
	
 
}


