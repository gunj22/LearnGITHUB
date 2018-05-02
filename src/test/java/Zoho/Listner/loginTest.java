package Zoho.Listner;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import zohoDDF.Base.BaseTest;

public class loginTest extends BaseTest{
	public SoftAssert asrt;

	@BeforeTest
	public void start(){
		
		asrt = new SoftAssert();
	}
	
	
	
	@Test
	public void login() throws IOException{
		openBrowser("Chrome");
		navigate("site");
		
	

	
	}
	
	
	
	@AfterTest
	public void quit(){
	
		asrt.assertAll();
		
	}

}
