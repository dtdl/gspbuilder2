package com.thegoldensource.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @deprecated
 * @author GoldenSource
 *
 */
public class PropertiesConfig {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Properties properties = new Properties();

//		InputStream in = PropertiesMain.class.getClassLoader().getResourceAsStream("build.properties");
//		properties.load(in);
		
		BufferedReader bufferedReader = new BufferedReader(new FileReader("build.properties"));
		properties.load(bufferedReader);

		properties.getProperty("ttt");
		System.out.println(properties.getProperty("ttt"));
		
		
	}

}
