package com.thegoldensource.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import com.thegoldensource.psg.pkgfile.BasePkgFileGenerator;

/**
 * 
 * 
 * @author David Tao
 *
 */
public class YamlConfig {
	
	static private final Logger logger = Logger.getLogger(YamlConfig.class);
	static private final String configFile = "./config.yaml";

	/**
	 * @deprecated for testing purpose only
	 * @param map
	 * @param count
	 */
	private void printMap(Map map, int count){
        Set set = map.keySet();
        for(Object key: set){

            Object value = map.get(key);

            for(int i=0; i<count; i++){
                System.out.print("===");
            }

            if(value instanceof Map) {

                System.out.println(key+":");
                printMap((Map)value, count+1);//嵌套
            }else if(value instanceof List){

                System.out.println(key+":");
                for(Object obj: (List)value){
                    for(int i=0; i<count; i++){
                        System.out.print("    ");
                    }
                    System.out.println("    - "+obj.toString());
                }
            }else{

                System.out.println(key + ": " + value);
            }
        }
    }
	
	/**
	 * 
	 * @return yaml config as a Map<String, Map/String>
	 */
	public Map<String, Object> loadConfig () {
		
		logger.info("loading config");
		
		FileInputStream fileInputStream = null;
		Map<String, Object> map = null;
		
		try {
		    Yaml yaml = new Yaml();
		    File file = new File(configFile);
		    fileInputStream = new FileInputStream(file);
		    map = yaml.loadAs(fileInputStream, Map.class);
		}catch(FileNotFoundException e) {
			logger.error("File Not Found:" + configFile);
		    e.printStackTrace();
		}finally {
		    try {
		        if(fileInputStream!=null)  fileInputStream.close();
		    }catch (IOException e){
		        e.printStackTrace();
		    }
		}
		
		logger.info("loaded config: " + map.toString());
		return map;
	}
	
	public static void main(String[] args) {

		YamlConfig y = new YamlConfig();
		y.loadConfig();
	}

}
