package com.thegoldensource.psg.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.thegoldensource.util.YamlConfig;


/**
 * 
 * @author David Tao
 *
 */
public class GSComponent {

	private static Map<String, String> cmptTypMap;
//	private static Properties properties = new Properties();
	private static final Logger logger = Logger.getLogger(GSComponent.class);

	private String cmptTyp = "UNKNOWN";
	private boolean active = true;
	private String cmptName;
	private String cmptFullPath;
	private String cmptPath;
	private String cmptOrchPath;
	
	
	static {
		cmptTypMap = new HashMap<String, String>();

			
		YamlConfig yamlConfig = new YamlConfig();
		Map<String, Object> tmpMap = yamlConfig.getConfigMap("generator.componentpath");
		
		// reverse key/value for easier parsing later
		for (String key : tmpMap.keySet()) {
			cmptTypMap.put(tmpMap.get(key).toString(), key);
		}
		
		
			
//		try {
//			BufferedReader bufferedReader;
//			bufferedReader = new BufferedReader(new FileReader("build.properties"));
//			properties.load(bufferedReader);
//			
//			String cmptPaths = properties.get("component.paths").toString();
//			logger.debug(cmptPaths);
//			
//			String[] cmptPathArray = cmptPaths.split(";");	
//			for (String cmptPath : cmptPathArray) {
//				String cmpt = cmptPath.split(":")[0];
//				String path = cmptPath.split(":")[1];
//				cmptTypMap.put(path, cmpt);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}		
	}
	
	
	public GSComponent() {
	}
	
	/**
	 * 
	 * @param cmptPath
	 */
	public GSComponent(String cmptFullPath, String rootFolder) {
		
		logger.debug("new GSComponent with: " + cmptFullPath );
		
		
		this.cmptFullPath = cmptFullPath;
		this.active = true;
		this.cmptPath = cmptFullPath.replace(rootFolder, "");
		
		//TODO handle "\"
		this.cmptName = cmptFullPath.substring(cmptFullPath.lastIndexOf("/")+1);
		logger.debug("cmptPath:" + this.cmptPath);
		logger.debug("cmptName:" + this.cmptName);
		
		// determine component type based on its folder
		for (String key: GSComponent.cmptTypMap.keySet()) {
			if (cmptFullPath.contains(key)) {
				this.cmptTyp = GSComponent.cmptTypMap.get(key).toString();
				this.cmptOrchPath = this.cmptPath.replace(this.cmptName, "").replace(key, "");
				
				logger.info("new GSComponent:" + this.cmptTyp + ":" + this.cmptPath);
				break;
			}
		}
	}
	
	
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
				", cmptPath=" + this.cmptPath + ", cmptName=" + this.cmptName +
				", cmptOrchPath=" + this.cmptOrchPath + "}";
//		logger.debug("GSComponent.toString: " + str);
		
		return str;
	}

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GSComponent c = new GSComponent("D:\\Projects\\Nikko\\E41-svn\\trunk\\customgc\\configuration\\vendordefinitions\\NIKKO_CSV_LINEBYLINE.gsp".replace("\\", "/"), "D:\\Projects\\Nikko\\E41-svn\\trunk\\customgc\\configuration".replace("\\", "/"));
		System.out.println(c);

		GSComponent c1 = new GSComponent("D:\\Projects\\Nikko\\E41-svn\\trunk\\customgc\\configuration\\resources\\mapping\\NIKKO\\BNP\\GC_BNP_ACCT.mdx".replace("\\", "/"), "D:\\Projects\\Nikko\\E41-svn\\trunk\\customgc\\configuration".replace("\\", "/"));
		System.out.println(c1);
		
//		D:\Projects\Nikko\E41-svn\trunk\customgc\configuration\resources\mapping\NIKKO\BNP\GC_BNP_ISSU_NZFA_Valuation.mdx
//		D:\\Projects\\Nikko\\E41-svn\\trunk\\customgc
		
	}

}
