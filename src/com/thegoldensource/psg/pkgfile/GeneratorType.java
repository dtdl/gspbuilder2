/**
 * 
 */
package com.thegoldensource.psg.pkgfile;

/**
 * @author David Tao
 *
 */
public enum GeneratorType {
	
	local("local"), svn("svn"), git("git"), auto("auto");
	
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
