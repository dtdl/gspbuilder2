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

/**
 * the yaml configuration
 * 
 * @author David Tao
 *
 */
public class YamlConfig {
	
	static private final Logger logger = Logger.getLogger(YamlConfig.class);
	static private final String configFile = "./config.yaml";
	static private Map<String, Object> configMap = null;

	static {
		logger.info("loading config");
		
		FileInputStream fileInputStream = null;
		try {
		    Yaml yaml = new Yaml();
		    File file = new File(configFile);
		    fileInputStream = new FileInputStream(file);
		    configMap = (Map<String, Object>) yaml.loadAs(fileInputStream, Map.class);
		}catch(FileNotFoundException e) {
			logger.error("File not found: " + configFile);
		    e.printStackTrace();
		}finally {
		    try {
		        if(fileInputStream!=null)  fileInputStream.close();
		    }catch (IOException e){
		        e.printStackTrace();
		    }
		}
		
		logger.info("loaded config: " + configMap);
	}
	
	public YamlConfig() {
		
	}
	
	/**
	 * @deprecated for testing purpose only
	 * @param map
	 * @param count
	 */
	private void printConfig(Map map, int count){
        Set set = map.keySet();
        for(Object key: set){

            Object value = map.get(key);

            for(int i=0; i<count; i++){
                logger.debug("  ");
            }

            if(value instanceof Map) {

                logger.debug(key+":");
                printConfig((Map)value, count+1);
            }else if(value instanceof List){

                logger.debug(key+":");
                for(Object obj: (List)value){
                    for(int i=0; i<count; i++){
                        logger.debug("    ");
                    }
                    logger.debug("    - "+obj.toString());
                }
            }else{

                logger.debug(key + ": " + value);
            }
        }
    }
	


	/**
	 * fetch the value from nested Map.
	 * e.g. use getConfig("svn.user") to get below config.
	 * -------------------
	 * ## Yaml
	 * svn:
	 *   user: david
	 * -------------------
	 * @param string
	 * @return
	 */
	public String getConfig(String keys) {
		logger.debug("fetching config: " + keys);
		Map<String, Object> tmpMap = configMap;
		
		// need to be documented that "." is hard coded here.
		String[] keyArray = keys.split("\\.");	
		logger.debug("nested level: " + keyArray.length);
		
		for (int i=0; i<keyArray.length-1; i++) {
			tmpMap = this.getMiddleMap(tmpMap, keyArray[i]);
			logger.debug("fetched config " + keyArray[i] + ": " + tmpMap);
		}
		
		String val = tmpMap.get(keyArray[keyArray.length-1]).toString();
		
		logger.info("fetched config " + keys + "= " + val);
		
		return val;
	}
	

	private Map<String, Object> getMiddleMap(Map<String, Object> tmpMap, String key) {
		return (Map<String, Object>) tmpMap.get(key);
	}

	/**
	 * 
	 * @return the full config in Map<String, Map/String>
	 */
	public Map<String, Object> getConfigMap() {
		return configMap;
	}
	
	public static void main(String[] args) {

		YamlConfig y = new YamlConfig();
		System.out.println(y.getConfig("svn.user"));
		System.out.println(y.getConfig("build.gc.base.version"));
	}

}
