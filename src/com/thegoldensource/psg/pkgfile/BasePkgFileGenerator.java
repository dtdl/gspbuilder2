package com.thegoldensource.psg.pkgfile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.thegoldensource.psg.model.GSComponent;
import com.thegoldensource.util.YamlConfig;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * Abstract generator which defines the general process of generating packing required packagedescription.xml and build.xml:
 * Also a static factory of actual children generators.
 * 
 * 
 * @author David Tao
 *
 */
public abstract class BasePkgFileGenerator {

	static private final Logger logger = Logger.getLogger(BasePkgFileGenerator.class);
	static protected GeneratorType typ = GeneratorType.local;

	// load configuration
	static protected YamlConfig yamlConfig = new YamlConfig();

	static {
		// set the configured type
		typ = GeneratorType.valueOf(yamlConfig.getConfig("generator.typ"));
		logger.debug("config: " + yamlConfig);
		
	}
	
	/**
	 * 
	 */
	public void generate() {
		
		logger.info("BasePkgFileGenerator.generate start");
		
		// get component list (defer to children generator)
		List<GSComponent> cmpList = this.getComponentList();
		
		// call templates to actually create package configuration files
		this.generateFiles(cmpList);
		
		logger.info("BasePkgFileGenerator.generate end");
	}
	
	/**
	 * 
	 */
	protected abstract List<GSComponent> getComponentList();
	
	
	/**
	 * 
	 * @param cmpList
	 */
	private void generateFiles(List<GSComponent> cmpList) {
		
		logger.info("BasePkgFileGenerator.generateFiles start");
		

		
		// initial freemarker
		// Create your Configuration instance, and specify if up to what FreeMarker
		// version (here 2.3.29) do you want to apply the fixes that are not 100%
		// backward-compatible. See the Configuration JavaDoc for details.
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
		// Set the preferred charset template files are stored in. UTF-8 is
		// a good choice in most applications:
		cfg.setDefaultEncoding("UTF-8");
		// Sets how errors will appear.
		// During web page *development* TemplateExceptionHandler.HTML_DEBUG_HANDLER is better.
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		// Don't log exceptions inside FreeMarker that it will thrown at you anyway:
		cfg.setLogTemplateExceptions(false);
		// Wrap unchecked exceptions thrown during template processing into TemplateException-s:
		cfg.setWrapUncheckedExceptions(true);
		// Do not fall back to higher scopes when reading a null loop variable:
		cfg.setFallbackOnNullLoopVariable(false);

		
		
		// Specify the source where the template files come from. Here I set a
		// plain directory for it, but non-file-system sources are possible too:
		try {
			cfg.setDirectoryForTemplateLoading(new File("./template"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// prepare all the variables
		Map<String, Object> templateVars = new HashMap<String, Object>();
		templateVars.put("componentList", cmpList);
		templateVars.put("yamlConfig", yamlConfig.getConfigMap());
		
		//parse yaml to see what needs to be generated
		Map<String, Object> filesMap = yamlConfig.getConfigMap("generator.file");
		logger.debug("files to be generated: " + filesMap);

		//
		try {
			for (Object fileObj : filesMap.values()) {

				// parse the file name and template
				Map<String, String> fileMap = (Map<String, String>) fileObj;
				String fileName = fileMap.get("name");
				String fileTemplate = fileMap.get("template");
				
				// get fm template  
				Template temp = cfg.getTemplate(fileTemplate);
				// target file
				Writer out = new FileWriter(new File(fileName));
//				Writer out = new OutputStreamWriter(System.out);
				
				// process to generate out
				temp.process(templateVars, out);

				logger.debug("file generated: " + fileName + " by " + fileTemplate);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.info("BasePkgFileGenerator.generateFiles end");
		
	}



	/**
	 * 
	 * @return an instance of Generator based on the configured property
	 */
	public static BasePkgFileGenerator getInstance() {
		
		
		BasePkgFileGenerator gen = null; 
		
		switch(typ){
    	case local:
    		gen = new PkgFileLocalGenerator();
    	case svn:
    		break;
    	case git:
    		break;
		default:
			break;
		}
		
		return gen;
	}
	
	/**
	 * 
	 * @param type
	 * @return an instance of Generator
	 */
	public static BasePkgFileGenerator getInstance(GeneratorType type) {
		
		BasePkgFileGenerator gen = null; 
		
		switch(type){
        	case local:
        		gen = new PkgFileLocalGenerator();
        	case svn:
        		break;
        	case git:
        		break;
        	case auto:
        		gen = BasePkgFileGenerator.getInstance();
		}
		
		return gen;
	}
	
	public static void main(String[] args) {
		
		BasePkgFileGenerator gen = BasePkgFileGenerator.getInstance();
		gen.generate();

	}

}
