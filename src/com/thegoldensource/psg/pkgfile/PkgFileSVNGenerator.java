package com.thegoldensource.psg.pkgfile;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.thegoldensource.psg.model.GSComponent;

import org.apache.log4j.Logger;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class PkgFileSVNGenerator extends BasePkgFileGenerator {

	static private final Logger logger = Logger.getLogger(PkgFileLocalGenerator.class);
	
	private SVNRepository repository;
	private String user;
	private String pass;
	private int versionFrom = 0;
//	private int versionTo = -1;
	private String svnurl;

	protected PkgFileSVNGenerator() {
		typ = GeneratorType.svn;
		this.user = yamlConfig.getConfig("svn.user");
	    this.pass = yamlConfig.getConfig("svn.pass");
	    

	}
	
	/**
	 * 
	 */
    private void initialSVNRepository() {

//		String svnurl = yamlConfig.getConfig("target.gc.svnurl");
//		logger.debug("svnurl: " + svnurl);
		
        // initializes the library (it must be done before ever using the library itself)
		//For using over http:// and https://
        DAVRepositoryFactory.setup();
        //For using over svn:// and svn+xxx://
        SVNRepositoryFactoryImpl.setup();
        //For using over file:///
        FSRepositoryFactory.setup();

        this.repository = null;
        try {
            /*
             * Creates an instance of SVNRepository to work with the repository.
             * All user's requests to the repository are relative to the
             * repository location used to create this SVNRepository.
             * SVNURL is a wrapper for URL strings that refer to repository locations.
             */
            this.repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(svnurl));
            ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(user, pass);
            repository.setAuthenticationManager(authManager);
        } catch (SVNException svne) {
        	logger.error("error while creating an SVNRepository for location '" + svnurl + "': " + svne.getMessage());
        }
        
        try {
            /*
             * Checks up if the specified path/to/repository part of the URL
             * really corresponds to a directory. If doesn't the program exits.
             * SVNNodeKind is that one who says what is located at a path in a
             * revision. -1 means the latest revision.
             */
            SVNNodeKind nodeKind = repository.checkPath("", -1);
            if (nodeKind == SVNNodeKind.NONE) {
                System.err.println("There is no entry at '" + svnurl + "'.");
                System.exit(1);
            } else if (nodeKind == SVNNodeKind.FILE) {
                System.err.println("The entry at '" + svnurl + "' is a file while a directory was expected.");
                System.exit(1);
            }
            /*
             * getRepositoryRoot() returns the actual root directory where the
             * repository was created. 'true' forces to connect to the repository 
             * if the root url is not cached yet. 
             */
            //System.out.println("Repository Root: " + repository.getRepositoryRoot(true));
            /*
             * getRepositoryUUID() returns Universal Unique IDentifier (UUID) of the 
             * repository. 'true' forces to connect to the repository 
             * if the UUID is not cached yet.
             */
            //System.out.println("Repository UUID: " + repository.getRepositoryUUID(true));
            //System.out.println("");

            /*
             * Displays the repository tree at the current path - "" (what means
             * the path/to/repository directory)
             */
        } catch (SVNException svne) {
        	logger.error("error while listing entries: " + svne.getMessage());
        }
        /*
         * Gets the latest revision number of the repository
         */
        long latestRevision = -1;
        try {
            latestRevision = repository.getLatestRevision();
        } catch (SVNException svne) {
        	logger.error("error while fetching the latest repository revision: " + svne.getMessage());
        }
        logger.info("---------------------------------------------");
        logger.info("Repository latest revision: " + latestRevision);
    }
    
	@Override
	protected List<GSComponent> getComponentList() {
		logger.info("getComponentList start");

//	    if (this.target = "gc")
	    this.versionFrom = Integer.parseInt(yamlConfig.getConfig("target."+this.target+".svnversionfrom"));
	    this.svnurl = yamlConfig.getConfig("target."+this.target+".svnurl");
	    
	    logger.debug("user: " + user);
	    logger.debug("pass: " + pass);
	    logger.debug("versionFrom: " + versionFrom);
	    logger.debug("svnurl: " + svnurl);
		this.initialSVNRepository();
		
        List<GSComponent> cmptList = new ArrayList<GSComponent>();
        try {
			cmptList = this.getCmptList("");
		} catch (SVNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // debug
        for (GSComponent c: cmptList) {
        	logger.debug("GSComponent:" + c);
        }
		
		logger.info("getComponentList end");
		return cmptList;
	}
	
	/**
	 * 
	 * @param path
	 * @return
	 * @throws SVNException
	 */
	private List<GSComponent> getCmptList(String path) throws SVNException {
		List<GSComponent> cmptList = new ArrayList<GSComponent>();
		 
		Collection entries = repository.getDir(path, -1, null, (Collection) null);
		Iterator iterator = entries.iterator();
		while (iterator.hasNext()) {
		    SVNDirEntry entry = (SVNDirEntry) iterator.next();
		    
		    logger.debug("/" + (path.equals("") ? "" : path + "/")
		            + entry.getName() + " (author: '" + entry.getAuthor()
		            + "'; revision: " + entry.getRevision() + "; date: " + entry.getDate() + ")");
		    if (entry.getKind() == SVNNodeKind.FILE) {
		        cmptList.add(new GSComponent("/"+path+"/"+entry.getName(), "", entry.getRevision()>=this.versionFrom));
		    }
		    //Checking up if the entry is a directory.
		    if (entry.getKind() == SVNNodeKind.DIR) {
		    	cmptList.addAll(this.getCmptList((path.equals("")) ? entry.getName(): path + "/" + entry.getName()));
		    }
		}
		
		cmptList.sort(new GSComponent());
	        
		return cmptList;
	}
	
//	/**
//	 * get the full file list of the folder path
//	 * @param path
//	 * @return
//	 */
//	private List<String> getFileList(String path) throws SVNException {
//		
//        List<String> fileList = new ArrayList<String>();
//       
//
//        logger.debug("path:" + path);
////        path = "./";
//        
//        Collection entries = repository.getDir(path, -1, null, (Collection) null);
//        Iterator iterator = entries.iterator();
//        while (iterator.hasNext()) {
//            SVNDirEntry entry = (SVNDirEntry) iterator.next();
//            System.out.println("/" + (path.equals("") ? "" : path + "/")
//                    + entry.getName() + " (author: '" + entry.getAuthor()
//                    + "'; revision: " + entry.getRevision() + "; date: " + entry.getDate() + ")");
//            if (entry.getKind() == SVNNodeKind.FILE) {
////                fileList.add("/" + (path.equals("") ? "" : path + "/") + entry.getName());
////                cmptList.add(new GSComponent(entry.getPath(), entry.getName(), entry.getRevision()>0));
//            }
//            /*
//             * Checking up if the entry is a directory.
//             */
//            if (entry.getKind() == SVNNodeKind.DIR) {
////                fileList.addAll(getFileList((path.equals("")) ? entry.getName(): path + "/" + entry.getName()));
////            	cmptList.add(new GSComponent(entry.getPath(), entry.getName(), entry.getRevision()>0));
//            }
//        }
//
//
//        try {
//            /*
//             * Checks up if the specified path/to/repository part of the URL
//             * really corresponds to a directory. If doesn't the program exits.
//             * SVNNodeKind is that one who says what is located at a path in a
//             * revision. -1 means the latest revision.
//             */
//            SVNNodeKind nodeKind = repository.checkPath("", -1);
//            if (nodeKind == SVNNodeKind.NONE) {
//                System.err.println("There is no entry at '" + path + "'.");
//                System.exit(1);
//            } else if (nodeKind == SVNNodeKind.FILE) {
//                System.err.println("The entry at '" + path + "' is a file while a directory was expected.");
//                System.exit(1);
//            }
//            /*
//             * getRepositoryRoot() returns the actual root directory where the
//             * repository was created. 'true' forces to connect to the repository 
//             * if the root url is not cached yet. 
//             */
//            //System.out.println("Repository Root: " + repository.getRepositoryRoot(true));
//            /*
//             * getRepositoryUUID() returns Universal Unique IDentifier (UUID) of the 
//             * repository. 'true' forces to connect to the repository 
//             * if the UUID is not cached yet.
//             */
//            //System.out.println("Repository UUID: " + repository.getRepositoryUUID(true));
//            //System.out.println("");
//
//            /*
//             * Displays the repository tree at the current path - "" (what means
//             * the path/to/repository directory)
//             */
//        } catch (SVNException svne) {
//            System.err.println("error while listing entries: "
//                    + svne.getMessage());
//            System.exit(1);
//        }
//        
////        File file = new File(path);
////        File[] tempList = file.listFiles();
//////        logger.debug("tempList.length:" + tempList.length);
////
////        for (int i = 0; i < tempList.length; i++) {
//////        	logger.debug("tempList[i]:" + tempList[i].toString());
////            if (tempList[i].isFile()) {
////                fileList.add(tempList[i].toString());
////                //String fileName = tempList[i].getName();
////            }
////            if (tempList[i].isDirectory()) {
////            	fileList.addAll(this.getFileList(tempList[i].toString()));
////            }
////        }
//        
//        return fileList;
//	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		PkgFileSVNGenerator pfg = new PkgFileSVNGenerator();
		pfg.generate();
	}

}
