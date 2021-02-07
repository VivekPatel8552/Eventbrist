package com.test.epam.Eventbrite.Helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Effeciency {

	/**
	 * Load Property file
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public Properties loadPropertyFile(String fileName) throws IOException {
		fileName = System.getProperty("user.dir") + File.separator+"src"+File.separator+"main"+File.separator+
				"resources"+File.separator+ fileName;
		File file = new File(fileName);   
		FileInputStream fileInput = null; 
		fileInput = new FileInputStream(file);
		Properties properties= new Properties(); //load properties file 
		properties.load(fileInput);
		return properties;
	}
	
	/**
	 * readJsonElement 
	 * @param fileName
	 * @param elementName
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, String> readJsonElementInOrder(String fileName, String elementName) throws Exception {
		String filePath = null;
		filePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator
				+ "resources" + File.separator + "Eventbrite" + File.separator + fileName;
		JsonElement root = new JsonParser().parse(new FileReader(filePath));
		JsonObject jsonObject = root.getAsJsonObject();
		JsonElement some = jsonObject.get(elementName);
		JsonObject testData = some.getAsJsonObject();
		HashMap<String, String> testDataMap = new LinkedHashMap<String, String>();
		for (Map.Entry<String, JsonElement> entry : testData.entrySet()) {
			testDataMap.put(entry.getKey().toString(), entry.getValue().getAsString());
		}
		return testDataMap;
	}
}
