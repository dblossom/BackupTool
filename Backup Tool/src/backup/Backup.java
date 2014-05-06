package backup;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class Backup {
	
	//folder location hard coded (for now)
	final protected String FOLDER_DEST = "/home/user/test/";
	
	public Backup(){}
	
	public static void main(String[] args){
		
		Backup b = new Backup();
		
		//store the hard_coded root folder in the args[0]
		//this is just temp, will add support for ANY root
		//ugh...bandaid, should have just left it hard coded
		args = new String[1];
		args[0] = b.FOLDER_DEST;
		
		b.deleteAllFilesFolders(args[0]);
		
		b.copyAllFilesFolders("/home/user/Documents", b.FOLDER_DEST);
	}
	
	/**
	 * Method takes in a string "root folder dest" and deletes
	 * all files & folders including itself.
	 * @param rootFolder - the root folder to delete
	 */
	protected void deleteAllFilesFolders(String rootFolder){
		//create a file/folder from the root folder
		File file = new File(rootFolder);
		
		//oh boy, we need to get outta here.
		if(!file.exists()){
			return;
		}
			
		//if this "file" is actually a folder
		//we need to work on deleting those contents
		//as per java doc ".delete()" will not delete
		//a folder unless it is empty
		if(file.isDirectory())
			new Thread(new Inner(file)).start();
		
		//now delete the root directory
		file.delete();
	}
	
	/**
	 * Method moves all files and folders from one location to another
	 * @param source what to move
	 * @param destination where to move it.
	 */
	protected void copyAllFilesFolders(String source, String destination){
		
		File fileSource = new File(source);
		File fileDestination = new File(destination);
		
		try {
			FileUtils.copyDirectory(fileSource, fileDestination);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Inner class that is the "worker thread"
	 * which will delete all files and folders
	 * @author user
	 *
	 */
	static class Inner implements Runnable{
		
		private File[] fileList;
		
		public Inner(File folder){
			this.fileList = folder.listFiles();
		}

		@Override
		public void run() {
			for(File file: fileList){
				if(file.isDirectory()){
					new Thread(new Inner(file)).start();
					//once we return delete root
					//we should probably invoke wait here
					file.delete();
				}else{
					//just a file delete.
					file.delete();
				}
			}
			
		}
		
	}

}
