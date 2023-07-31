package com.restAssured.stepdef;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.yaml.snakeyaml.Yaml;

import com.rexel.utils.TestUtils;

public class TestData {
	
	public String getTestData(String type, String dataName) {
		String data = null;
		String path= System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator
		+ "resources" + File.separator + "TestData.yaml";
		try {
			Yaml yaml = new Yaml();
			InputStream inputStream = new FileInputStream(path);
			Map<String, Object> map = yaml.load(inputStream);
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (value instanceof Map) {
					Map<String, Object> subMap = (Map<String, Object>) value;
					for (Map.Entry<String, Object> subEntry : subMap.entrySet()) {
						String subKey = subEntry.getKey();
						Object subValue = subEntry.getValue();
						//System.out.println(key + "." + subKey + ": " + subValue);
						String test= key+"."+subKey;
						if(test.equalsIgnoreCase(type+"."+dataName)){
							data=(String) subValue.toString();
							break;
						}
					}
				} else {
					System.out.println(key + ": " + value);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return data;
	}
}
