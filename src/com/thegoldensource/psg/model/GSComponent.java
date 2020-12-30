package com.thegoldensource.psg.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;


/**
 * 
 * @author GoldenSource
 *
 */
public class GSComponent {

	private static Map<String, String> cmptTypMap;
	private static Properties properties = new Properties();
	private static final Logger logger = Logger.getLogger(GSComponent.class);

	private String cmptTyp = "UNKNOWN";
	private boolean active = true;
	private String cmptName;
	private String cmptFullPath;
	private String cmptPath;
	private String cmptOrchPath;
	
	
	static {
		cmptTypMap = new HashMap<String, String>();

		BufferedReader bufferedReader;
		try {
			bufferedReader = new BufferedReader(new FileReader("build.properties"));
			properties.load(bufferedReader);
			
			String cmptPaths = properties.get("component.paths").toString();
			logger.debug(cmptPaths);
			
			String[] cmptPathArray = cmptPaths.split(";");	
			for (String cmptPath : cmptPathArray) {
				String cmpt = cmptPath.split(":")[0];
				String path = cmptPath.split(":")[1];

				cmptTypMap.put(path, cmpt);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	
	public GSComponent() {
	}
	
	/**
	 * 
	 * @param cmptPath
	 */
	public GSComponent(String cmptFullPath, String rootFolder) {
		
//		logger.debug("GSComponent start");
		
		
		this.cmptFullPath = cmptFullPath;
		this.active = true;
		this.cmptPath = cmptFullPath.replace(rootFolder, "");
		
		//TODO handle "\"
		this.cmptName = cmptFullPath.substring(cmptFullPath.lastIndexOf("/")+1);
		
		for (String key: GSComponent.cmptTypMap.keySet()) {
			if (cmptFullPath.contains(key)) {
				this.cmptTyp = GSComponent.cmptTypMap.get(key);
				this.cmptOrchPath = this.cmptPath.replace(this.cmptName, "").replace(key, "");
				
//				logger.debug(this.cmptTyp + ":" + this.cmptPath);
				break;
			}
		}
	}
	
	
	/**
	 * TODO to be replaced by yaml parser
	 * @return
	 */
//	private static Map<String, ComponentType> initCmptTypMap() {
//		Map<String, ComponentType> cmptTypMap = new HashMap<String, ComponentType>();
//		cmptTypMap.put("\\resources\\mapping\\", ComponentType.WORKFLOW);
//		cmptTypMap.put("\\vendordefinitions\\", ComponentType.VENDORDEFINITIOIN);
//		return cmptTypMap;
//	}


	
	public String getCmptPath() {
		return cmptPath;
	}
	
	public void setCmptPath(String cmptPath) {
		this.cmptPath = cmptPath;
	}
	
	public String getCmptTyp() {
		return cmptTyp;
	}

	public void setCmptTyp(String cmptTyp) {
		this.cmptTyp = cmptTyp;
	}

	public String getCmptName() {
		return cmptName;
	}

	public void setCmptName(String cmptName) {
		this.cmptName = cmptName;
	}

	public String getCmptFullPath() {
		return cmptFullPath;
	}

	public void setCmptFullPath(String cmptFullPath) {
		this.cmptFullPath = cmptFullPath;
	}

	public String getCmptOrchPath() {
		return cmptOrchPath;
	}

	public void setCmptOrchPath(String cmptOrchPath) {
		this.cmptOrchPath = cmptOrchPath;
	}
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	/**
	 * 
	 */
	public String toString() {
		
		String str = "{" + this.cmptTyp + ", isActive=" + this.active + 
				", path=" + this.cmptPath + ", name=" + this.cmptName +
				", cmptOrchPath=" + this.cmptOrchPath + "}";
//		logger.debug("GSComponent.toString: " + str);
		
		return str;
	}

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GSComponent c = new GSComponent("D:\\Projects\\Nikko\\E41-svn\\trunk\\customgc\\configuration\\vendordefinitions\\NIKKO_CSV_LINEBYLINE.gsp", "D:\\Projects\\Nikko\\E41-svn\\trunk\\customgc\\configuration");
		GSComponent c1 = new GSComponent("D:\\Projects\\Nikko\\E41-svn\\trunk\\customgc\\configuration\\resources\\mapping\\NIKKO\\BNP\\GC_BNP_ACCT.mdx", "D:\\Projects\\Nikko\\E41-svn\\trunk\\customgc\\configuration");
		
		System.out.println(c);
		System.out.println(c1);
		
		
		
	}

}
