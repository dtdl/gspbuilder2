/**
 * 
 */
package com.thegoldensource.psg.pkgfile;

/**
 * @author GoldenSource
 *
 */
public enum GeneratorType {
	
	LOCAL("LOCAL"), SVN("SVN"), GIT("GIT"), AUTO("AUTO");
	
	private String type;
	
	private GeneratorType(String typ) {
		type = typ;
    }
	
	public String getGeneratorType() {
        return type;
    }
	
	@Override
    public String toString() {
        return "Generator Type: '" + type + "'";
    }
	
}
