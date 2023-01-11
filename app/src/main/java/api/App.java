package api;

import java.util.Scanner;

public class App {
    public static void main( String[] args ) {
    	Scanner in = new Scanner(System.in);
    	String location;
    	String startString;
    	String endString;
    	String unitString;
    	String answer;
    	
        System.out.println( "~~Welcome to Weather App~~");
        System.out.println("What location would you like ot check? (ex. London, UK)");
        location = in.nextLine();
        
        System.out.println("Would you like to select a range of dates? (y or n)");
        answer = in.nextLine();
        if(answer.equals("y")) {
        	System.out.println("What would you like the start date to be? (yyyy-mm-dd ex. 2021-08-25)");
        	startString = in.nextLine();
        	System.out.println("Would you like to select a end date? (y or n)");
        	answer = in.nextLine();
        	if(answer.equals("y")) {
        		System.out.println("What would you like the end date to be? (yyyy-mm-dd ex. 2021-08-25)");
        		endString = in.nextLine();
        	} else {
        		endString = null;
        	}
        } else {
        	startString = null;
        	endString = null;
        }
        
        System.out.println("What type of metrics would you like us to use? (us, metric, uk)");
        unitString = in.nextLine();
        System.out.println("\n\n");
        WeatherAPI weatherAPI = new WeatherAPI(location, unitString, startString, endString);
        
        try {
			weatherAPI.request();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        in.close();
    }
}

