package Zoho.TestCases;

import java.io.IOException;
import java.util.Hashtable;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.relevantcodes.extentreports.LogStatus;

import zohoDDF.Base.BaseTest;
import zohoDDF.Util.DataUtil;
import zohoDDF.Util.Xls_Reader;

public class LeadTest extends BaseTest {

	Xls_Reader xls;
	SoftAssert softassert;
	
	
	@BeforeMethod
	public void initializaion(){
		
		softassert = new SoftAssert();
	}
	
	
	@Test(priority=1,dataProvider="getData")
	
	public void createLeadTest(Hashtable<String, String>table) throws IOException{
		
		test = rep.startTest("createLeadTest");
		
		if(!DataUtil.isRunnable("CreateLeadTest", xls) ||table.get("RunMode").equals("N")){
			test.log(LogStatus.INFO, "Skipping the test as RunMode is N");
			throw new SkipException("Skipping the Test");}
		
		
		openBrowser(table.get("Browser"));
		test.log(LogStatus.INFO, "Browser Open Sucessfully");
		navigate("url");
		test.log(LogStatus.INFO, "Open Site Sucessfully");
		doLogin(prop.getProperty("username"), prop.getProperty("pwd"));
		test.log(LogStatus.INFO, "Login Sucessfully");
		click("crm_xpath");
		click("createNew_xpath");
		click("AddNewLead_xpath");
		//click("leads_xpath");
		//click("createLead_xpath");
		type("leadCompany_xpath", table.get("Company"));
		type("leadLastName_xpath", table.get("LastName"));
		wait(1);
		click("saveLeadButton_xpath");
		
		wait(4);
		click("leads_xpath");
		
		int rNum = getLeadRowCount(table.get("LastName"));
		if(rNum==-1)
			reportFailure("Lead not created" + table.get("LastName"));
		else
			Reportpass("Lead Created Sucessfully" + table.get("LastName"));
		
		
	
	}

	@Test(priority=2,dataProvider="getData")
	public void convertLeadTest(Hashtable<String, String>table) throws IOException{
		test= rep.startTest("convertLeadTest");
		if(!DataUtil.isRunnable("ConvertLeadTest", xls) ||table.get("RunMode").equals("N")){
			test.log(LogStatus.INFO, "Skipping the test as RunMode is N");
			throw new SkipException("Skipping the Test");}
		
		openBrowser(table.get("Browser"));
		navigate("url");
		doLogin(prop.getProperty("username"), prop.getProperty("pwd"));
		click("crm_xpath");
		click("leads_xpath");
		clickonLead(table.get("LastName"));
		click("convertButton_xpath");
		click("convertButtonSave_xpath");
		click("leads_xpath");
		int rowNumber = getLeadRowCount(table.get("LastName"));
		
		if(rowNumber==-1)
			System.out.println("Lead Convert Sucessfully");
		test.log(LogStatus.INFO, "Lead Convert Sucessfully");
		
		click("Accounts_xpath");
		
			//Validate
		
	}
	
	@Test(priority=3,dataProvider="deleteData")
	public void deleteLeadTest(Hashtable<String, String>table) throws IOException{
		test= rep.startTest("deleteLeadTest");
		
		if(!DataUtil.isRunnable("DeleteLeadAccountTest", xls) ||table.get("RunMode").equals("N")){
			test.log(LogStatus.INFO, "Skipping the test as RunMode is N");
			throw new SkipException("Skipping the Test");}
					
		openBrowser(table.get("Browser"));
		navigate("url");
		doLogin(prop.getProperty("username"), prop.getProperty("pwd"));
		click("crm_xpath");
		click("leads_xpath");
		clickonLead(table.get("LeadName"));		
		click("customizeForDeleteLead_xpath");		
		actionClass("deleteLeadButton_xpath");
		actionClass("deletePopup_xpath");
		wait(3);
		click("leads_xpath");
		
		
		int rowNumber = getLeadRowCount(table.get("LeadCompany"));
		if(rowNumber==-1)
			System.out.println("Lead Deleted Sucessfully");
		test.log(LogStatus.INFO, "Lead Deleted Sucessfully");
			
	
	}
	
	
	@DataProvider 
	public Object[][] getData(){
		Init();
		
		xls = new Xls_Reader(prop.getProperty("xlspath"));
		
		return DataUtil.getTestData(xls,"CreateLeadTest");
		
	}
	
	@DataProvider
	public Object[][]deleteData(){
		
		Init();
		xls=new Xls_Reader(prop.getProperty("xlspath"));
		return DataUtil.getTestData(xls, "DeleteLeadAccountTest");
			
	}
	
	
	
	
	@AfterMethod
	public void quit(){
		try{
		softassert.assertAll();}
		catch(Error e){
			test.log(LogStatus.FAIL, e.getMessage());
		}
		
		
		if(rep!=null){
			rep.endTest(test);
			rep.flush();
		}
		if(driver!=null)
			driver.quit();
		
	
	}
	

}
