package zohoDDF.Base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import zohoDDF.Util.ExtentManager;
import zohoDDF.Util.Xls_Reader;

public class BaseTest {

	public WebDriver driver=null;
	public Properties prop=null;
	public ExtentReports rep = ExtentManager.getInstance();
	public ExtentTest test;
	//public Xls_Reader xl=null;
	
	
	
	public void Init() {
		
		if(prop==null){
		prop= new Properties();
		try{
		FileInputStream fs = new FileInputStream(System.getProperty("user.dir")+"//src//test//resources//projectconfig.properties");
		prop.load(fs);
		}
		catch(Exception ex){
			ex.getStackTrace();
		}
		}
		
	}
	
	
	public void openBrowser(String bType) throws IOException{
		test.log(LogStatus.INFO, "Opening the Browser" + bType);
		if(bType.equals("Mozilla"))
			driver=new FirefoxDriver();
		else if(bType.equals("Chrome")){
			System.setProperty("webdriver.chrome.driver", prop.getProperty("chromedriver_exe"));
			driver= new ChromeDriver();
		}
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		test.log(LogStatus.INFO, "Browser Opened Sucessfully" +bType);
		

	}

	public void navigate(String locatorKey){
		test.log(LogStatus.INFO, "Navigate to -- " +prop.getProperty(locatorKey));
		driver.get(prop.getProperty(locatorKey));
		
	}
	
	public void click(String XpathKey){
		driver.findElement(By.xpath(prop.getProperty(XpathKey))).click();
	}

	public void type(String XpatKey,String ValueKey){
		getElement(XpatKey).sendKeys(ValueKey);
	
	
	}
	
	public WebElement getElement(String XpathKey){ 
		WebElement e = null;
		try{
		if(XpathKey.endsWith("_xpath"))
		e=	driver.findElement(By.xpath(prop.getProperty(XpathKey)));
		else if(XpathKey.endsWith("_id"))
		e=	driver.findElement(By.id(prop.getProperty(XpathKey)));
		else if(XpathKey.endsWith("_name"))
		e=	driver.findElement(By.name(prop.getProperty(XpathKey)));
		else{reportFailure("Xpath not correct --" +XpathKey);
		 Assert.fail("Xpath not correct --" +XpathKey);
		}
		}catch(Exception ex){
			reportFailure(ex.getMessage());
			ex.printStackTrace();
			Assert.fail("Failed the Test -- " +ex.getMessage());
		}
		return e;
	}
	
	
	public void wait(int timetoWait) {
		try {
			Thread.sleep(timetoWait * 1000);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
	}
	
	
	public int getLeadRowCount(String leadName){
		test.log(LogStatus.INFO, "Finding the lead" + leadName);
		List<WebElement> leadNames = driver.findElements(By.xpath(prop.getProperty("leadnames_xpath")));
		
		for(int i=0;i<leadNames.size();i++){
			
			if(leadNames.get(i).getText().equals(leadName))
				System.out.println(leadNames.get(i).getText());
				test.log(LogStatus.INFO, "Lead found on the row" + i);
				return (i+1);
		
		}
		return -1;
	}
	
	
	public void clickonLead(String leadName){
		test.log(LogStatus.INFO, "Clicking of Lead" + leadName);
		int rNum= getLeadRowCount(leadName);
		driver.findElement(By.xpath(prop.getProperty("leadPart1_xpath")+rNum+prop.getProperty("leadPart2_xpath"))).click();
		
	}
	
	
	public String getText(String locatorKey){
		test.log(LogStatus.INFO, "Getting the Text " + locatorKey);
		
	return	getElement(locatorKey).getText();
		
	}
	
	
	
	/************************************App Functions *************************************/
	
	public void waitforPageload(){
		
		JavascriptExecutor js = (JavascriptExecutor)driver;
		String state = (String)js.executeScript("return document.readyState");
		while(!state.equals("complete"))
		wait(2);
		state = (String)js.executeScript("return document.readyState");
			
		}

	
	public boolean doLogin(String username,String pwd){
			
		click("loginButton_xpath");
		wait(1);
		//waitforPageload();
		driver.switchTo().frame(0);
		type("username_xpath", username);
		type("pwd_xpath", pwd);
		//click("mangeSession_xpath");
		click("SignIn_button_xpath");
		click("skipWarning_xpath");
		click("continueUrl_xpath");
		
		if(isElementPresent("crm_xpath")){
			test.log(LogStatus.INFO, "Login Sucessfull");
			return true;}
		else {
			test.log(LogStatus.INFO, "Login UnSucessfull");
			return false;}
		
	}
	
	
	public void actionClass(String LocatorKey)
	{
		WebElement deleteButton =  getElement(LocatorKey);
		Actions act = new Actions(driver);
		act.moveToElement(deleteButton).click();
		act.build().perform();
		
		
	}
	
	

        /************************Validation Function************************************/

	
	public boolean verifyTitle(String locatorKey,String expectedResult){
		String actualText=getElement(locatorKey).getText().trim();
		String expectedTest=prop.getProperty("signinText");
		if(actualText.equals(expectedTest))
		  return true;
		else
		  return false;
		
	}
	
	
	public boolean isElementPresent(String locatorKey){
		List<WebElement> elementList=null;
		
		if(locatorKey.endsWith("_xpath"))
			elementList = driver.findElements(By.xpath(prop.getProperty(locatorKey)));
		else if(locatorKey.endsWith("_id"))
			elementList = driver.findElements(By.id(prop.getProperty(locatorKey)));
		else if(locatorKey.endsWith("_name"))
			elementList= driver.findElements(By.name(prop.getProperty(locatorKey)));
		
		
		else{
			reportFailure("Locaor is not correct --- "+locatorKey);
			Assert.fail("Locaor is not correct --- "+locatorKey);
		}
		
		if(elementList.size()==0){
			return false;
		}
		
	return true;
		
	}
	
	public void selectDate(String d){
		test.log(LogStatus.INFO, "Selecting the Date - " + d);
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		
		try {
			Date dateTobeSelected = sdf.parse(d);
			Date currentDate = new Date();
			sdf= new SimpleDateFormat("MMMM");
			String monthToBeSelected = sdf.format(dateTobeSelected);
			sdf = new SimpleDateFormat("yyyy");
			String yearToBeSelected = sdf.format(dateTobeSelected);
			sdf = new SimpleDateFormat("d");
			String dayToBeSelected = sdf.format(dateTobeSelected);
			
			String monthYearToBeSelected = monthToBeSelected+" "+yearToBeSelected;
			
		 while(true){
			 
			 if(currentDate.compareTo(dateTobeSelected)==1)
				 click("claenderBackbutton_xpath");
			 else if(currentDate.compareTo(dateTobeSelected)==-1)
				 click("calenderNextbutton_xpath");
			 		 
			 if(monthYearToBeSelected.equals(getText("calenderCurrentdateMonth_xpath"))){
			 break;
			 }
		 }
		 driver.findElement(By.xpath("//td[text()= '"+dayToBeSelected+"']")).click();
			test.log(LogStatus.INFO, "Date selection sucessfull" + d);
		
		
		
		
		} 
		
			catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	
	
	  /************************Reporting Function************************************/
	
	public void Reportpass(String msg){
		test.log(LogStatus.INFO, msg);
		
	}
	
	public void reportFailure(String msg){
		test.log(LogStatus.FAIL, msg);
		takeScreenShot();
		Assert.fail(msg);
	}

	public void takeScreenShot(){
		// fileName of the screenshot
		Date d=new Date();
		String screenshotFile=d.toString().replace(":", "_").replace(" ", "_")+".png";
		// store screenshot in that file
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir")+"//TakeScreenShots//"+screenshotFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//put screenshot file in reports
		test.log(LogStatus.INFO,"Screenshot-> "+ test.addScreenCapture(System.getProperty("user.dir")+"//TakeScreenShots//"+screenshotFile));
		
	}

	
}
