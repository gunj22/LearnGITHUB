package Zoho.TestCases;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.util.Hashtable;

import com.relevantcodes.extentreports.LogStatus;
import zohoDDF.Base.BaseTest;
import zohoDDF.Util.DataUtil;
import zohoDDF.Util.Xls_Reader;

public class LoginTest extends BaseTest {

	String testCaseName ="LoginTest";
	SoftAssert softAssert;
	Xls_Reader xls;
	
	
	
	@BeforeMethod
	public void init(){
		
		softAssert=new SoftAssert();
	}

	@Test(dataProvider="getData")
	public void doLoginTest(Hashtable<String, String>data) throws IOException{
		
		test =rep.startTest("LoginTest");
		test.log(LogStatus.INFO, "Starting the Test");
		
		if(!DataUtil.isRunnable(testCaseName, xls) ||  data.get("RunMode").equals("N")){
			test.log(LogStatus.SKIP, "Skipping the test as runmode is N");
			throw new SkipException("Skipping the test as runmode is N");
		}
		
		test.log(LogStatus.INFO, "Details");
		openBrowser(data.get("Browser"));
		navigate("url");
	boolean actualResult = doLogin(prop.getProperty("username"), prop.getProperty("pwd"));
	boolean expectedResult = false;
	
	if(data.get("ExpectedResult").equals("Y"))
		expectedResult=true;
	else
		expectedResult=false;
	
	if(actualResult!=expectedResult){
		reportFailure("Login Failed,Credentials not match");
	}else{
		Reportpass("Login sucessfully");
	}
		
	}


	@AfterMethod
	public void quit(){
		
		try{
		softAssert.assertAll();}
		catch(Exception ex){
			test.log(LogStatus.FAIL, ex.getMessage());
		}
		
		rep.endTest(test);
		rep.flush();
		if(driver!=null)
			driver.quit();
		
		
	}
	
	
	@DataProvider
	public Object[][] getData(){		
		super.Init();	
		xls = new Xls_Reader(prop.getProperty("xlspath"));
		return DataUtil.getTestData(xls, testCaseName);
		
	}

}
