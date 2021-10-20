/**
 * 
 */
package com.thegoldensource.psg.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.thegoldensource.util.YamlConfig;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * @author DTao
 *
 */
public class GeneralFileGenerator {
	
	private static final Logger logger = Logger.getLogger(GeneralFileGenerator.class);
	private static final String pnConfig = "-Dconfig";
	protected static String configFile = null;
	
// load configuration
//	protected YamlConfig yamlConfig = new YamlConfig(configFile);
	protected YamlConfig yamlConfig = new YamlConfig();

	public List<Map<String, String>> readExcel(String fileName) {
		List<Map<String, String>> schedulesList = new ArrayList<Map<String, String>>();
		try {

//			 obtaining input bytes from a file
			FileInputStream fis = new FileInputStream(new File(fileName));
			// creating workbook instance that refers to .xls file
			XSSFWorkbook wb = new XSSFWorkbook(fis);
			// creating a Sheet object to retrieve the object
			XSSFSheet sheet = wb.getSheetAt(0);
			// evaluating cell type
			FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();

			// read the first row as header.
			Row headerRow = sheet.getRow(0);
			List<String> headerList = new ArrayList<String>();

			for (Cell cell : headerRow) {
				switch (formulaEvaluator.evaluateInCell(cell).getCellType()) {
				case Cell.CELL_TYPE_NUMERIC: // field that represents numeric cell type
					// getting the value of the cell as a number
//						System.out.print(cell.getNumericCellValue() + ",");
					headerList.add(cell.getStringCellValue().toString());
					break;
				case Cell.CELL_TYPE_STRING: // field that represents string cell type
					// getting the value of the cell as a string
//						System.out.print(cell.getStringCellValue() + ",");
					headerList.add(cell.getStringCellValue());
					break;
				}
			}
			logger.debug(headerList);

			// read the content
			for (int j = 1; j < sheet.getPhysicalNumberOfRows(); j++) { // iteration over row using for each loop
				Row row = sheet.getRow(j);
				Map<String, String> map = new HashMap<String, String>();

				for (int i = 0; i < headerList.size(); i++) {
					Cell cell = row.getCell(i);
//					System.out.println("key=" + headerList.get(i));
//					System.out.println(" value=" + cell);
					
					if (cell == null) {
						map.put(headerList.get(i), cell!=null? cell.getStringCellValue():"");
					} else {

						switch (formulaEvaluator.evaluateInCell(cell).getCellType()) {
						case Cell.CELL_TYPE_NUMERIC: // field that represents numeric cell type
							// getting the value of the cell as a number
//							System.out.print(cell.getNumericCellValue() + ",");
							map.put(headerList.get(i), String.valueOf(cell.getNumericCellValue()));
							break;
						case Cell.CELL_TYPE_STRING: // field that represents string cell type
							// getting the value of the cell as a string
	//							System.out.print(cell.getStringCellValue() + ",");
							map.put(headerList.get(i), cell!=null? cell.getStringCellValue():"");
							break;
						}
					}
				}
//				System.out.println(map);
				schedulesList.add(map);
			}
//			System.out.print(schedulesList);
			
 

		} catch (Exception e) {
			e.printStackTrace();
		}
		return schedulesList;
	}

	public void generateFiles() {
		logger.info("GeneralFileGenerator.generateFiles start");
		
		
		
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
			e.printStackTrace();
		}

		
		//read yaml
//		logger.debug(this.yamlConfig.getConfig("configExcel"));
		
		// initial vars.
		// read config excel
		List<Map<String, String>> schedules = readExcel(this.yamlConfig.getConfig("configExcel"));
//		logger.debug(this.yamlConfig.getConfig("targetFiles"));

		//parse yaml to see what needs to be generated
		Map<String, Object> filesMap = yamlConfig.getConfigMap("targetFiles");
		logger.debug("files to be generated: " + filesMap);
		
		

		// generate each file
		try {
			
			for (Object fileObj : filesMap.values()) {

				// parse the file name and template
				Map<String, String> fileMap = (Map<String, String>) fileObj;
				String fileName = fileMap.get("name");
				String fileTemplate = fileMap.get("template");
				
				// get fm template  
				Template temp = cfg.getTemplate(fileTemplate);
				
				
				// generate file for each schedule
				for (Map<String, String> s : schedules) {

					
					Map<String, Object> templateVars = new HashMap<String, Object>();
					templateVars.put("schedule", s);
					

					// target out file
					String fileName2 = fileName.replace("<title>", s.get("title"));
					Writer out = new FileWriter(new File(fileName2));
					
					// process to generate out
					temp.process(templateVars, out);
					


					logger.debug("file generated: " + fileName2 + " by " + fileTemplate);
				}
				

			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		
		logger.info("GeneralFileGenerator.generateFiles end");
	}

	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		logger.debug(args);
		for (String arg : args) { 
			if (arg.contains(pnConfig)) {
				configFile = arg.split("=")[1];
				logger.debug(pnConfig + ": " + arg.split("=")[1]);
			}
		}
		
		if (configFile == null) {
			configFile = "./config/config.yaml";
		}
		
		
		GeneralFileGenerator jsg = new GeneralFileGenerator();
		jsg.generateFiles();
	
		}
}
