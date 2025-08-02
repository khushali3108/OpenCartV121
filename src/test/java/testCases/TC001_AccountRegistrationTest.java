package testCases;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObjects.AccountRegistrationPage;
import pageObjects.HomePage;
import testBase.BaseClass;
 

public class TC001_AccountRegistrationTest extends BaseClass {

	@Test(groups={"Regression", "Master"})
    public void verify_account_registration()
	{
		logger.info("****Starting Account Registration****");
		
		try {
    	HomePage hp = new HomePage(driver);
    	hp.clickMyAccount();
		logger.info("****Clicked on MyAccount link****");
		
    	hp.clickRegister();
    	logger.info("****Clicked on Register link****");
    	
    	AccountRegistrationPage regpage = new AccountRegistrationPage(driver);
    	logger.info("****Providing customer details****");
    	
    	regpage.setFirstName(randomeString().toUpperCase());
    	regpage.setLastName(randomeString().toUpperCase());
    	regpage.setEmail(randomeString() + "@gmail.com");
    	regpage.setTelephone(randomeNum());
    	String password = randomeAlphaNumeric();
    	
    	regpage.setPassword(password);
    	regpage.setConfirmPassword(password);
    	
    	regpage.setPrivacyPolicy();
    	regpage.clickContinue();
    	
    	logger.info("****Validating expected msg****");
    	String confirmMsg = regpage.getConfirmationMsg();
    	Assert.assertEquals(confirmMsg,"Your Account Has Been Created!");
		}catch(Exception e) {
			logger.error("Test failed....");
			logger.debug("Debug logs.....");
			Assert.fail();
		}
		logger.info("****Finished Account Registration****");
 
}
}
