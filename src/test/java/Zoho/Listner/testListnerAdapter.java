package Zoho.Listner;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class testListnerAdapter extends TestListenerAdapter {

	
	public void onTestFailure(ITestResult tr){
		System.out.println("Fail  -> " + tr.getName());
	
		
	}
	
	public void onTestSkipped(ITestResult tr){
		System.out.println("Skipped-> " + tr.getName());
		
	}
	
	public void onTestSuccess(ITestResult tr){
		System.out.println("Success-> " + tr.getName());
		
	}
	
	
}
