package utilities;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
//import java.net.URL;
import java.net.URL;

//Extent report 5.x...//version

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import testBase.BaseClass;

public class ExtentReportManager implements ITestListener {
	public ExtentSparkReporter sparkReporter;
	public ExtentReports extent;
	public ExtentTest test;

	String repName;//represent report name

	public void onStart(ITestContext testContext) {
		
		//generate timestamp way
		/*1)SimpleDateFormat df=new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
		Date dt=new Date();
		String currentdatetimestamp=df.format(dt);
		*/
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());// time stamp
		
		//generate report with time stamp
		repName = "Test-Report-" + timeStamp + ".html";
		sparkReporter = new ExtentSparkReporter(".\\reports\\" + repName);// specify location of the report

		sparkReporter.config().setDocumentTitle("opencart Automation Report"); // Title of report
		sparkReporter.config().setReportName("opencart Functional Testing"); // name of the report
		sparkReporter.config().setTheme(Theme.DARK);
		
		extent = new ExtentReports();
		extent.attachReporter(sparkReporter);
		extent.setSystemInfo("Application", "opencart");
		extent.setSystemInfo("Module", "Admin");
		extent.setSystemInfo("Sub Module", "Customers");
		extent.setSystemInfo("User Name", System.getProperty("user.name"));//
		extent.setSystemInfo("Environemnt", "QA");
		
		String os = testContext.getCurrentXmlTest().getParameter("os");
		extent.setSystemInfo("Operating System", os);
		
		String browser = testContext.getCurrentXmlTest().getParameter("browser");
		extent.setSystemInfo("Browser", browser);
		
		List<String> includedGroups = testContext.getCurrentXmlTest().getIncludedGroups();
		if(!includedGroups.isEmpty()) {
		extent.setSystemInfo("Groups", includedGroups.toString());
		}
	}

	public void onTestSuccess(ITestResult result) {
	
		test = extent.createTest(result.getTestClass().getName());
		test.assignCategory(result.getMethod().getGroups()); // to display groups in report
		test.log(Status.PASS,result.getName()+" got successfully executed");
		
	}
    
	//whenever the test method will fail then OnTestFailure mathod will trigger
	public void onTestFailure(ITestResult result) {
		test = extent.createTest(result.getTestClass().getName());//So result from the result it is getting the class and from the class it is getting the name of the class.And with that name it is creating the new test entry in the report.
		test.assignCategory(result.getMethod().getGroups());//From the result it is getting the test method.Whichever we executed and from the test method it is getting the groups.In which group the test method is belongs to.It is capturing those groups, and those groups will be attached to the report as a category wise.
		
		test.log(Status.FAIL,result.getName()+" got failed");// upgrading the test cases failure information.
		test.log(Status.INFO, result.getThrowable().getMessage());//Printing the error message.
		
		//attaching screenshot to the report
		try {
			String imgPath = new BaseClass().captureScreen(result.getName());//screnshot method will trigger we are attaching the screenshot from the base class.We are calling a screenshot method by passing the name of the test, and it will return the image path.
			test.addScreenCaptureFromPath(imgPath);//stmt will attach ss to the report and And with that name it is creating the new test entry in the report.it will return the image path.
			
		} catch (IOException e1) {//if ss not available
			e1.printStackTrace();
		}
	}

	public void onTestSkipped(ITestResult result) {//it skip whenever the test method skip
		test = extent.createTest(result.getTestClass().getName());
		test.assignCategory(result.getMethod().getGroups());
		test.log(Status.SKIP, result.getName()+" got skipped");
		test.log(Status.INFO, result.getThrowable().getMessage());
	}

	//if you include this code, we don't need to open the report manually.It will automatically opens.
	public void onFinish(ITestContext testContext) {
		
		extent.flush();//consolidate all the information from the report.
		
		String pathOfExtentReport = System.getProperty("user.dir")+"\\reports\\"+repName;
		File extentReport = new File(pathOfExtentReport);
		
		try {
			Desktop.getDesktop().browse(extentReport.toURI());// this particular method will open this report on the browser automatically.We no need to write.We no need to open the report manually.It will automatically open the report on browser.
		} catch (IOException e) {//if this extend report is not available this will throw some exception.
			e.printStackTrace();
		}

		
		/*  you want to send that report to your team automatically as soon as your automation tests are completed,
            execution immediately you want to send the report, then you can enable this piece of code.
		 *  this piece of code we can use to send automated email to the team.
		 *  try {
			  URL url = new  URL("file:///"+System.getProperty("user.dir")+"\\reports\\"+repName);
		  
		  // Create the email message 
		  ImageHtmlEmail email = new ImageHtmlEmail();
		  email.setDataSourceResolver(new DataSourceUrlResolver(url));
		  email.setHostName("smtp.googlemail.com"); 
		  email.setSmtpPort(465);
		  email.setAuthenticator(new DefaultAuthenticator("pavanoltraining@gmail.com","password")); 
		  email.setSSLOnConnect(true);
		  email.setFrom("pavanoltraining@gmail.com"); //Sender
		  email.setSubject("Test Results");
		  email.setMsg("Please find Attached Report....");
		  email.addTo("pavankumar.busyqa@gmail.com"); //Receiver 
		  email.attach(url, "extent report", "please check report..."); 
		  email.send(); // send the email 
		  }
		  catch(Exception e) 
		  { 
			  e.printStackTrace(); 
			  }
		 */ 
		 
	}

}
