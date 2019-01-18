package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class CSVParser {
	
	public static ArrayList<String[]> parseCSVFile(String pDir) {
		BufferedReader reader;
		String actualLine = "";
		ArrayList<String[]> parsedFile = new ArrayList<String[]>();
		try {
			reader = new BufferedReader(new FileReader(pDir));
			while((actualLine = reader.readLine())!=null) {
				parsedFile.add(actualLine.split(","));
			}
			reader.close();
		} catch (Exception e) {
			System.out.println("No pude leer el archivo");
		}
		return parsedFile;
	}
	
}
