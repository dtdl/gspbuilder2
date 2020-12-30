package com.thegoldensource.psg.pkgfile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.thegoldensource.psg.model.GSComponent;

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
	static protected GeneratorType typ = GeneratorType.LOCAL;
	static protected Properties properties = new Properties();
	
	// load properties
	static {

		BufferedReader bufferedReader;
		try {
			bufferedReader = new BufferedReader(new FileReader("build.properties"));
			properties.load(bufferedReader);
			typ = GeneratorType.valueOf(properties.getProperty("generator.type"));
		} catch (Exception e) {
			e.printStackTrace();
		}		
//		logger.debug("generator.type===" + typ);
		
	}
	
	/**
	 * 
	 */
	public void generate() {
		
		logger.debug("BasePkgFileGenerator.generate start");
		
		// get component list (defer to children generator)
		List<GSComponent> cmpList = this.getComponentList();
		
		// call templates to actually create package configuration files
		this.generateFiles(cmpList);
		
		logger.debug("BasePkgFileGenerator.generate end");
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
		
		logger.debug("BasePkgFileGenerator.generateFiles start");
		//TODO parse yaml to see what needs to be generated
		
		//TODO generate files for each
		
		// Create your Configuration instance, and specify if up to what FreeMarker
		// version (here 2.3.29) do you want to apply the fixes that are not 100%
		// backward-compatible. See the Configuration JavaDoc for details.
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_30);

		// Specify the source where the template files come from. Here I set a
		// plain directory for it, but non-file-system sources are possible too:
		try {
			cfg.setDirectoryForTemplateLoading(new File("./template"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// From here we will set the settings recommended for new projects. These
		// aren't the defaults for backward compatibilty.

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
		
		
		
		try {
			Template temp = cfg.getTemplate("PackageDescription.ftl");
			Map<String, Object> gcDescription = new HashMap<String, Object>();
			gcDescription.put("componentList", cmpList);
			Writer out = new OutputStreamWriter(System.out);
			temp.process(gcDescription, out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		logger.debug("BasePkgFileGenerator.generateFiles end");
		
	}



	/**
	 * 
	 * @return an instance of Generator based on the configured property
	 */
	public static BasePkgFileGenerator getInstance() {
		
		
		BasePkgFileGenerator gen = null; 
		
		switch(typ){
    	case LOCAL:
    		gen = new PkgFileLocalGenerator();
    	case SVN:
    		break;
    	case GIT:
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
        	case LOCAL:
        		gen = new PkgFileLocalGenerator();
        	case SVN:
        		break;
        	case GIT:
        		break;
        	case AUTO:
        		gen = BasePkgFileGenerator.getInstance();
		}
		
		return gen;
	}
	
	public static void main(String[] args) {
		
		BasePkgFileGenerator gen = BasePkgFileGenerator.getInstance();
		gen.generate();

	}

}
