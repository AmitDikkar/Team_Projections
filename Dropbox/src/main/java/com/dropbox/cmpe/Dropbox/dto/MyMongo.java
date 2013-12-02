/**
 * 
 */
package com.dropbox.cmpe.Dropbox.dto;

import java.net.UnknownHostException;

import java.util.Date;
import java.util.List;

import org.jongo.Jongo;
import org.jongo.MongoCollection;

import com.dropbox.cmpe.Dropbox.config.ConfigElements;
import com.dropbox.cmpe.Dropbox.domain.DatabaseDetails;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

/**
 * @author Team projections
 * 
 */
public class MyMongo {

	public static MongoCollection collection;

	public MyMongo() {
		// TODO Auto-generated constructor stub
		MongoClient client = null;
		try {
			client = new MongoClient(new ServerAddress(
					ConfigElements.getDatabaseAddress(),
					ConfigElements.getDatabasePort()));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("!!!!Error in connection");
		}
		DB database = client.getDB(ConfigElements.getDbName());
		String username = "projection";
		String pwd = "projection";
		char[] password = pwd.toCharArray();
		boolean auth = database.authenticate(username, password);
		Jongo jongo = new Jongo(database);
		MyMongo.collection = jongo.getCollection(ConfigElements
				.getDbCollection());
	}

	/**
	 * @param dbDetails
	 *            pass the database object and it will directly save into
	 *            Database
	 */
	public void insert(DatabaseDetails dbDetails) {
		collection.insert(dbDetails);
	}

	/**
	 * @return the collection
	 */
	public MongoCollection getCollection() {
		return collection;
	}

	/**
	 * @param collection
	 *            the collection to set
	 */
	public void setCollection(MongoCollection collection) {
		this.collection = collection;
	}

	public Boolean isUserNameExist(String username) {
		String query = "{userName:'" + username + "'}";
		DatabaseDetails dbDetails = collection.findOne(query).as(
				DatabaseDetails.class);

		if (dbDetails == null) {
			return false;
		} else {
			return true;
		}
	}

	public String getArchiveID(String userName, String fileName) {
		String query = "{userName:'" + userName + "'}";
		DatabaseDetails dbDetails = MyMongo.collection.findOne(query).as(
				DatabaseDetails.class);
		int index = dbDetails.getFileIndex(fileName);
		return (dbDetails.getArchiveId(index));
	}

	public Boolean checkFileSizeToUpload(String userName, long fileSize) {
		String query = "{userName:'" + userName + "'}";
		long storageLeft = MyMongo.collection.findOne(query)
				.as(DatabaseDetails.class).getTotalStorageLeft();
		if (storageLeft >= fileSize) {
			return true;
		} else {
			return false;
		}
	}

	public void deleteFileDetails(String userName, String fileName) {
		moveToTrash(userName, fileName, "N"); // N stands for normal file delete
		String query = "{userName:'" + userName + "'}";
		DatabaseDetails dbDetails = MyMongo.collection.findOne(query).as(
				DatabaseDetails.class);
		int index = dbDetails.getFileIndex(fileName);
		dbDetails.removefileArichiveId(index);
		dbDetails.removeFilePath(index);
		long fileSize = dbDetails.getFileSizeFile(index);
		dbDetails.setTotalStorageLeft(dbDetails.getTotalStorageLeft()
				+ fileSize);
		dbDetails.removefileSize(index);
		dbDetails.removeFileFromList(index);
		dbDetails.removeFileUploadingDate(index);
		collection.update(query).merge(dbDetails);
	}

	public void shareFile(String userName, String sharedWith, String fileName) {
		String query = "{userName:'" + userName + "'}";
		DatabaseDetails userDbDetails = MyMongo.collection.findOne(query).as(
				DatabaseDetails.class);
		String query2 = "{userName:'" + sharedWith + "'}";
		DatabaseDetails sharedDbDetails = MyMongo.collection.findOne(query2)
				.as(DatabaseDetails.class);
		sharedDbDetails.addSharedFileName(fileName);
		int index = sharedDbDetails.getSharedFileIndex(fileName);
		sharedDbDetails.addSharedVaultName(index, userDbDetails.getVaultName());
		sharedDbDetails.addSharedArchiveId(index,
				userDbDetails.getArchiveIDByFileName(fileName));
		sharedDbDetails.addSharedFileSize(index,
				userDbDetails.getFileSizeByFileName(fileName));
		sharedDbDetails.addSharedFileDates(index, new Date().toGMTString());
		collection.update(query2).merge(sharedDbDetails);
		System.out.println("File shared");
	}

	// below 2 functions will be used for downloading and deleting the shared
	// file
	public String getSharedFileVaultName(String userName, String fileName) {
		String query = "{userName:'" + userName + "'}";
		DatabaseDetails dbDetails = MyMongo.collection.findOne(query).as(
				DatabaseDetails.class);
		int index = dbDetails.getSharedFileIndex(fileName);
		return dbDetails.getSharedFileVaultName(index);
	}

	public String getSharedFileArchivalId(String userName, String fileName) {
		String query = "{userName:'" + userName + "'}";
		DatabaseDetails dbDetails = MyMongo.collection.findOne(query).as(
				DatabaseDetails.class);
		int index = dbDetails.getSharedFileIndex(fileName);
		return dbDetails.getSharedFileArchivalId(index);
	}

	public void deleteSharedFileDetails(String userName, String fileName) {
		moveToTrash(userName, fileName, "S"); // S stands for shared file
		String query = "{userName:'" + userName + "'}";
		DatabaseDetails dbDetails = MyMongo.collection.findOne(query).as(
				DatabaseDetails.class);
		int index = dbDetails.getSharedFileIndex(fileName);
		dbDetails.removeSharedFileDetails(index);
		collection.update(query).merge(dbDetails);
	}

	public void deleteFileFromTrash(String userName, String fileName) {
		String query = "{userName:'" + userName + "'}";
		DatabaseDetails dbDetails = MyMongo.collection.findOne(query).as(
				DatabaseDetails.class);
		int index = dbDetails.getTrashFileIndex(fileName);
		dbDetails.removeFromTrash(index);
		collection.update(query).merge(dbDetails);
	}

	/**
	 * @param userName
	 * @param fileName
	 * @param s
	 *            Move files to trash.. irrespective of whether file is deleted
	 *            from Normal list or from SharedFiles No need to maintain the
	 *            size of deleted shared files
	 */
	private void moveToTrash(String userName, String fileName, String s) {
		// TODO - Auto-generated method stub
		System.out.println("Inside Move to trash");
		String query = "{userName:'" + userName + "'}";
		DatabaseDetails dbDetails = MyMongo.collection.findOne(query).as(
				DatabaseDetails.class);

		if (s.equalsIgnoreCase("N")) {
			System.out.println("Normal Delete");
			int index1 = dbDetails.getFileIndex(fileName);
			dbDetails.addTrashListOfFiles(fileName);
			int index2 = dbDetails.getTrashFileIndex(fileName);
			dbDetails.addTrashVaultName(index2, dbDetails.getVaultName());
			dbDetails.addtrashFileArichiveIds(index2,
					dbDetails.getArchiveId(index1));
			dbDetails.addTrashFileSizes(index2,
					dbDetails.getFileSizeFile(index1));
			dbDetails
					.addTrashFileDeletionDate(index2, new Date().toGMTString());
			dbDetails.addTrashCameFrom(index2, s);
		} else {
			System.out.println("shared Delete");
			int index1 = dbDetails.getSharedFileIndex(fileName);
			dbDetails.addTrashListOfFiles(fileName);
			int index2 = dbDetails.getTrashFileIndex(fileName);
			dbDetails.addTrashVaultName(index2,
					dbDetails.getSharedFileVaultName(index1));
			dbDetails.addtrashFileArichiveIds(index2,
					dbDetails.getSharedFileArchivalId(index1));
			dbDetails.addTrashFileSizes(index2,
					dbDetails.getSharedFileSize(index1));
			dbDetails
					.addTrashFileDeletionDate(index2, new Date().toGMTString());
			dbDetails.addTrashCameFrom(index2, s);
		}
		collection.update(query).merge(dbDetails);
		System.out.println("Moved to trash");
	}

	public Boolean authenticateUser(String userName, String password) {
		String query = "{userName:'" + userName + "'}";
		DatabaseDetails dbDetails = MyMongo.collection.findOne(query).as(
				DatabaseDetails.class);
		System.out.println("database pwd: " + dbDetails.getPassword());
		if (dbDetails.getPassword().equals(password)) {
			return true;
		} else {
			return false;
		}
	}

	public String getTrashArchivalId(String userName, String fileName) {
		String query = "{userName:'" + userName + "'}";
		DatabaseDetails dbDetails = MyMongo.collection.findOne(query).as(
				DatabaseDetails.class);
		int index = dbDetails.getTrashFileIndex(fileName);
		return dbDetails.getTrashFileArchiveIds(index);
	}

	public String getTrashFileVaultName(String userName, String fileName) {
		String query = "{userName:'" + userName + "'}";
		DatabaseDetails dbDetails = MyMongo.collection.findOne(query).as(
				DatabaseDetails.class);
		int index = dbDetails.getTrashFileIndex(fileName);
		return dbDetails.getTrashFileVaultName(index);
	}

	// Below methods will be used for Views

	public List<String> getFileNames(String userName) {
		String query = "{userName:'" + userName + "'}";
		DatabaseDetails dbDetails = MyMongo.collection.findOne(query).as(
				DatabaseDetails.class);

		return dbDetails.getListOfFiles();
	}

	public List<String> getSharedFileNames(String userName) {
		String query = "{userName:'" + userName + "'}";
		DatabaseDetails dbDetails = MyMongo.collection.findOne(query).as(
				DatabaseDetails.class);

		return dbDetails.getSharedFileNames();
	}

	public List<String> getTrashFileNames(String userName) {
		String query = "{userName:'" + userName + "'}";
		DatabaseDetails dbDetails = MyMongo.collection.findOne(query).as(
				DatabaseDetails.class);

		return dbDetails.getTrashListOfFiles();
	}

	public List<String> getTrashFileDates(String userName) {
		// TODO Auto-generated method stub
		String query = "{userName:'" + userName + "'}";
		DatabaseDetails dbDetails = MyMongo.collection.findOne(query).as(
				DatabaseDetails.class);
		return dbDetails.getTrashListOfDates();
	}

	public List<String> getFileDates(String userName) {
		// TODO Auto-generated method stub
		String query = "{userName:'" + userName + "'}";
		DatabaseDetails dbDetails = MyMongo.collection.findOne(query).as(
				DatabaseDetails.class);
		return dbDetails.getListOfDate();
	}
	public String getVaultName(String userName){
		String query = "{userName:'"+userName+"'}";
		String vaultName = MyMongo.collection.findOne(query).as(DatabaseDetails.class).getVaultName();
		return (vaultName);
	}

	/**
	 * @param userName
	 * @param fileName
	 * @param filePath
	 * @param fileArchiveId
	 * @param fileZize
	 * This function will add new file details in the database
	 * 1. Automatically update the StorageLeft
	 * 2. add File name, File size archival ID, 
	 */
	public void addNewFileDetails(String userName, String fileName, String filePath, String fileArchiveId, long fileZize){
		String query = "{userName:'"+userName+"'}";
		DatabaseDetails dbDetails = MyMongo.collection.findOne(query).as(DatabaseDetails.class);
		dbDetails.addFile(fileName);
		int index = dbDetails.getFileIndex(fileName);
		dbDetails.addFilePath(index, filePath);
		dbDetails.addfileArichiveId(index, fileArchiveId);
		dbDetails.addfileSize(index, fileZize);
		dbDetails.addFileUploadingDate(index, new Date().toGMTString());
		dbDetails.setTotalStorageLeft(dbDetails.getTotalStorageLeft() - fileZize);
		collection.update(query).merge(dbDetails);
		System.out.println("updated fields");
	}

public void recoverFile(String userName, String fileName){
		System.out.println("Inside Move to trash");
		String query = "{userName:'"+userName+"'}";
		DatabaseDetails dbDetails = MyMongo.collection.findOne(query).as(DatabaseDetails.class);
		
		int trashIndex = dbDetails.getTrashFileIndex(fileName);
		
		if(dbDetails.getTrashCameFrom(trashIndex).equalsIgnoreCase("N")){  // copy file details to normal file list
			dbDetails.addFile(fileName);
			int fileIndex = dbDetails.getFileIndex(fileName);
			dbDetails.addfileArichiveId(fileIndex, dbDetails.getTrashFileArchiveIds(trashIndex));
			dbDetails.addfileSize(fileIndex, dbDetails.getTrashFileSize(trashIndex));
			dbDetails.addFileUploadingDate(fileIndex, new Date().toGMTString());
			long fileSize = dbDetails.getTrashFileSize(trashIndex);
			dbDetails.setTotalStorageLeft(dbDetails.getTotalStorageLeft() - fileSize);
		}
		else{ // copy file details to shared File list
			dbDetails.addSharedFileName(fileName);
			int sharedFileIndex = dbDetails.getSharedFileIndex(fileName);
			dbDetails.addSharedArchiveId(sharedFileIndex, dbDetails.getTrashFileArchiveIds(trashIndex));
			dbDetails.addSharedFileSize(sharedFileIndex, dbDetails.getTrashFileSize(trashIndex));
			dbDetails.addSharedFileDates(sharedFileIndex, new Date().toGMTString());
		}
		dbDetails.removeFromTrash(trashIndex);
		collection.update(query).merge(dbDetails);
	}


	public String registerUser(String userName, String password, String role,String emailid){
		DatabaseDetails dbDetails = new DatabaseDetails();
		if(isUserNameExist(userName) == false){
			dbDetails.setUserName(userName);
			dbDetails.setRole(role);
		}
		else{
			System.out.println("This is existing username");
			dbDetails.setUserNameWithNewVaultName(userName); //Created unique username and vaultname also
			dbDetails.setRole(role);
		}
		dbDetails.setPassword(password);
		dbDetails.setEmailid(emailid);
		insert(dbDetails);
		return dbDetails.getUserName();
	}
}