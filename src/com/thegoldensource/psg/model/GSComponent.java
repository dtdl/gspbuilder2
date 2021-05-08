package com.thegoldensource.psg.model;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.thegoldensource.util.YamlConfig;


/**
 * 
 * @author David Tao
 *
 */
public class GSComponent implements Comparator<GSComponent> {

	private static Map<String, String> cmptTypMap;
	private static final Logger logger = Logger.getLogger(GSComponent.class);

	private String cmptTyp = "UNKNOWN";
	private boolean active = true;
	private String cmptShortName;
	private String cmptName;
	private String cmptFullPath;
	private String cmptPath;
	private String cmptFolder;


	private String cmptOrchPath;
	
	static {
		cmptTypMap = new HashMap<String, String>();
		
		// load component path from config.yaml
		YamlConfig yamlConfig = new YamlConfig();
		Map<String, Object> tmpMap = yamlConfig.getConfigMap("generator.componentpath");
		
		// reverse key/value for easier parsing later
		for (String key : tmpMap.keySet()) {
			cmptTypMap.put(tmpMap.get(key).toString(), key);
		}
	}
	
	public GSComponent() {
	}
	
	/**
	 * Component expect a linux style path, meaning "/", instead of "\"
	 * @param cmptFullPath, the full path of the component 
	 * @param rootFolder, basically the "configuration" folder
	 */
	public GSComponent(String cmptFullPath, String rootFolder, boolean active) {
		logger.debug("new GSComponent with: " + cmptFullPath );
		
		this.cmptFullPath = cmptFullPath;
		this.active = active;
		this.cmptPath = cmptFullPath.replace(rootFolder, "");
		
		//TODO handle "\"
		this.cmptName = cmptFullPath.substring(cmptFullPath.lastIndexOf("/")+1);
		this.cmptShortName = cmptName.substring(0, cmptName.lastIndexOf("."));
		this.cmptFolder = this.cmptPath.replace(this.cmptName, "");
		//		logger.debug("cmptPath:" + this.cmptPath);
//		logger.debug("cmptName:" + this.cmptName);
		
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
	public String getCmptFolder() {
		return cmptFolder;
	}
	public void setCmptFolder(String cmptFolder) {
		this.cmptFolder = cmptFolder;
	}
	public String getCmptShortName() {
		return cmptShortName;
	}
	public void setCmptShortName(String cmptShortName) {
		this.cmptShortName = cmptShortName;
	}
	
	/**
	 * @return String
	 */
	public String toString() {
		String str = "{" + this.cmptTyp + ", isActive=" + this.active +
				", cmptName=" + this.cmptName +
				", cmptShortName=" + this.cmptShortName +
				", cmptPath=" + this.cmptPath + 
				", cmptFullPath=" + this.cmptFullPath +
				", cmptFolder=" + this.cmptFolder + 
				", cmptOrchPath=" + this.cmptOrchPath + "}";
		return str;
	}

	
	public static void main(String[] args) {
		
		GSComponent c = new GSComponent("D:\\Projects\\Nikko\\E41-svn\\trunk\\customgc\\configuration\\vendordefinitions\\NIKKO_CSV_LINEBYLINE.gsp".replace("\\", "/"), "D:\\Projects\\Nikko\\E41-svn\\trunk\\customgc\\configuration".replace("\\", "/"), true);
		System.out.println(c);

		GSComponent c1 = new GSComponent("D:\\Projects\\Nikko\\E41-svn\\trunk\\customgc\\configuration\\resources\\mapping\\NIKKO\\BNP\\GC_BNP_ACCT.mdx".replace("\\", "/"), "D:\\Projects\\Nikko\\E41-svn\\trunk\\customgc\\configuration".replace("\\", "/"), true);
		System.out.println(c1);
	}


	@Override
	public int compare(GSComponent c1, GSComponent c2) {
		return c1.getCmptName().compareTo(c2.getCmptName());
//		return c1.getCmptFullPath().compareTo(c2.getCmptFullPath());
	}
}
