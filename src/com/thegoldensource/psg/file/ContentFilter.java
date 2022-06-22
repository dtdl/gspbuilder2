package com.thegoldensource.psg.file;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ContentFilter {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		try {
			FileWriter writer = new FileWriter("./data/GSadhGDFExp_20220407-070120.R73.2022.P73");
			BufferedReader bufferedReader = new BufferedReader(new FileReader("./data/GSadhGDFExp_20220407-070120.R73.1.P73"));
//			bufferedReader.readLine();
			String line = null;
			while((line = bufferedReader.readLine()) != null) {
//				if(line.startsWith("SVR") || line.startsWith("VEIL-R")) {
//				if(line.startsWith("VF1") && !line.startsWith("VF1-Plan")) {
//				if(!line.contains("20211231\tT")) {
				if(line.startsWith("DCVEUF") && !line.contains("20211231\t")) {
//				if(line.startsWith("CTBC") && line.contains("20220211\tT")) {
//				if(line.startsWith("CTBC")) {
//				if(line.startsWith("KBVFF")) {
//				if(line.startsWith("VDEF-B\t")) {
//				if(line.contains("20220128\t")) {
					System.out.println(line);
					writer.write(line + "\n");
				}
			}
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
