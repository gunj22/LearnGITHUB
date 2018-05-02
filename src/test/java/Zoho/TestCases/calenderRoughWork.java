package Zoho.TestCases;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.annotations.Test;

public class calenderRoughWork {


	@Test
	public void calenderFunctionality() throws ParseException{
		
		Date d = new Date();
		System.out.println(d);
		
		String date = "10/10/2016";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date d1 = sdf.parse(date);
		
		System.out.println(d.compareTo(d1));
		//System.out.println("The selecting date is -- > " +sdf.format(d1));
	
		
		/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		System.out.println(sdf.format(d));*/
		
		
		
		
	}





}
