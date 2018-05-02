package Zoho.TestCases;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.sun.corba.se.impl.encoding.TypeCodeOutputStream;

import zohoDDF.Base.BaseTest;
import zohoDDF.Util.DataUtil;
import zohoDDF.Util.ExtentManager;
import zohoDDF.Util.Xls_Reader;


public class PotentialTest extends BaseTest{
	
	Xls_Reader xls;
	SoftAssert softassert;
	
	
	@Test(dataProvider="createDealData")
	public void createDeal(Hashtable<String, String>table) throws IOException{
		
		test= rep.startTest("createDeal");
		if(!DataUtil.isRunnable("CreateDealTest", xls) ||table.get("RunMode").equals("N")){
			test.log(LogStatus.INFO, "Skipping the test as RunMode is N");
			throw new SkipException("Skipping the Test");}
		
		openBrowser(table.get("Browser"));
		navigate("url");
		doLogin(prop.getProperty("username"), prop.getProperty("pwd"));
		click("crm_xpath");
		click("createNew_xpath");
		click("Dealbutton_xpath");
		type("dealNameText_xpath", table.get("DealName"));
		type("accountNameText_xpath", table.get("AccountName"));
		click("calenderControl_xpath");
		selectDate(table.get("ClosingDate"));
		click("stage_xpath");
		
		WebElement list = getElement("allItemsinList_xpath");
		List<WebElement> clickList = list.findElements(By.tagName("li"));
		for(int i =0 ;i<clickList.size();i++){
			if(clickList.get(i).getText().equals(table.get("Stage"))){
				clickList.get(i).click();
				break;}
		}
		click("potentialSavebutton_xpath");
		wait(1);
		click("Dealbuttontop_xpath");
		
		List<WebElement> result = driver.findElements(By.xpath("//div[@class='stageview-each-container']/div[2]/a"));
		
		for(int i =0; i<result.size();i++){
			if(result.get(i).getText().equals(table.get("DealName"))){
				takeScreenShot();
				test.log(LogStatus.INFO, "Deal Createrd sucessfully");
			System.out.println("Deal Createrd sucessfully");
			break;
			}
	
		}

	}

	
	@Test(dataProvider="deleteDeal")
	public void removeDeal(Hashtable<String, String>table) throws IOException{
		
		test= rep.startTest("removeDeal");
		if(!DataUtil.isRunnable("DeleteDealAccountTest", xls) ||table.get("RunMode").equals("N")){
			test.log(LogStatus.INFO, "Skipping the test as RunMode is N");
			throw new SkipException("Skipping the Test");}
	
		
		openBrowser(table.get("Browser"));
		navigate("url");
		doLogin(prop.getProperty("username"), prop.getProperty("pwd"));
		click("crm_xpath");
		click("Dealbuttontop_xpath");
		
		
		List<WebElement> result = driver.findElements(By.xpath("//div[@class='stageview-each-container']/div[2]/a"));
		
		for(int i =0; i<result.size();i++){
			
			if(result.get(i).getText().equals(table.get("DealName"))){
				takeScreenShot();
				result.get(i).click();
				click("customize_xpath");
				takeScreenShot();
				click("deleteDeal_xpath");
				takeScreenShot();
				click("deletePopup_xpath");
				test.log(LogStatus.INFO, "Deal Deleted Sucessfully");
				Reportpass("Deal Deleted Sucessfully");
				wait(1);
				click("Dealbuttontop_xpath");
				takeScreenShot();
			break;
			}
			else{
				test.log(LogStatus.INFO, "Deal is not present");
			//break;
			}
			
	
		}
		
}
	
	@DataProvider
	public Object [][] createDealData(){
		
		Init();
		 xls = new Xls_Reader(prop.getProperty("xlspath"));
		 return DataUtil.getTestData(xls, "CreateDealTest");
	}

	@DataProvider
	public Object [][] deleteDeal(){
		Init();
		xls= new Xls_Reader(prop.getProperty("xlspath"));
		return DataUtil.getTestData(xls, "DeleteDealAccountTest");
		
	} 

	@BeforeMethod
	public void initializaion(){
		
		softassert = new SoftAssert();
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
		/*if(driver!=null)
			driver.quit();*/
	}

}