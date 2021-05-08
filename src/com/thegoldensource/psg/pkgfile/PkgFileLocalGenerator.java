package com.thegoldensource.psg.pkgfile;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import com.thegoldensource.psg.model.GSComponent;

/**
 * To produce a component list from a local "configuration" folder.
 * 
 * 
 * @author David Tao
 */
public class PkgFileLocalGenerator extends BasePkgFileGenerator {

	static private final Logger logger = Logger.getLogger(PkgFileLocalGenerator.class);
	
	/**
	 * 
	 */
	protected PkgFileLocalGenerator() {
		typ = GeneratorType.local;
	}
	
	/**
	 * 
	 */
	@Override
	protected List<GSComponent> getComponentList() {
		logger.info("getComponentList start");

		// get the local component directory
		String root = yamlConfig.getConfig("local.folder");
		logger.debug("folder: " + root);
		
        // get file list from local directory
        List<String> fileList = this.getFileList(root);
        for (String f: fileList) {
        	logger.debug("file:" + f);
        }
        
        // convert to component list
        List<GSComponent> cmptList = new ArrayList<GSComponent>();
        for (String f: fileList) {
        	logger.debug("PkgFileLocalGenerator.getComponentList f" + f);
        	logger.debug("PkgFileLocalGenerator.getComponentList root" + root);
        	// change into linux folder format
        	cmptList.add(new GSComponent(f.replace("\\", "/"), root.replace("\\", "/"), true));
        }
//        for (GSComponent c: cmptList) {
//        	logger.debug("GSComponent:" + c);
//        }
        
		// sort component by name
        cmptList.sort(new GSComponent());
        
        
		logger.info("getComponentList end");
		return cmptList;
	}
	
	/**
	 * get the full file list of the folder path
	 * @param path
	 * @return
	 */
	private List<String> getFileList(String path) {
		
        List<String> fileList = new ArrayList<String>();
        
        File file = new File(path);
        File[] tempList = file.listFiles();
//        logger.debug("tempList.length:" + tempList.length);

        for (int i = 0; i < tempList.length; i++) {
//        	logger.debug("tempList[i]:" + tempList[i].toString());
            if (tempList[i].isFile()) {
                fileList.add(tempList[i].toString());
                //String fileName = tempList[i].getName();
            }
            if (tempList[i].isDirectory()) {
            	fileList.addAll(this.getFileList(tempList[i].toString()));
            }
        }
        
        return fileList;
	}

	public static void main(String[] args) {
		
		PkgFileLocalGenerator gen = new PkgFileLocalGenerator();
		gen.generate();
	}

}
