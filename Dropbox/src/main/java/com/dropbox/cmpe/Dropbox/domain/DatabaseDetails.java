/**
 * 
 */
package com.dropbox.cmpe.Dropbox.domain;

import java.io.File;
import java.nio.file.spi.FileTypeDetector;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.jongo.Find;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Team_Projections This class contains every field related to database
 *         storage Purpose of this class: When we will use Jongo to store into
 *         MongoDB, obejct of this class will be created and then stored in the
 *         mongoDB
 */
public class DatabaseDetails {

	private String userId;

	// this should be unique
	private String userName;

	private String password;

	private List<String> listOfFiles = new ArrayList<String>();

	private List<Long> fileSize = new ArrayList<Long>();

	private List<String> listOfDate = new ArrayList<String>();

	private List<String> fileArichiveId = new ArrayList<String>();;

	private List<String> filePaths = new ArrayList<String>();

	private String vaultName;

	private String role;

	private long TotalStorage;

	private long TotalStorageLeft;

	private String emailid;

	@JsonIgnore
	private int random = 0;

	private List<String> sharedVaultNames = new ArrayList<String>();

	private List<String> sharedFileNames = new ArrayList<String>();

	private List<String> sharedArchiveIDs = new ArrayList<String>();

	private List<Long> sharedFileSizes = new ArrayList<Long>();

	private List<String> sharedFileDates = new ArrayList<String>();

	private List<String> trashListOfFiles = new ArrayList<String>();

	private List<String> trashVaultNames = new ArrayList<String>();

	private List<Long> trashFileSizes = new ArrayList<Long>();

	private List<String> trashListOfDates = new ArrayList<String>();

	private List<String> trashFileArichiveIds = new ArrayList<String>();

	private List<String> trashCameFrom = new ArrayList<String>();

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
		this.vaultName = userName;
	}

	public void setUserNameWithNewVaultName(String username) {
		random++;
		this.userName = username + random;
		this.vaultName = username + random;
		// System.out.println("new user name is :"+);
	}

	/**
	 * @return the listOfFiles
	 */
	public List<String> getListOfFiles() {
		return listOfFiles;
	}

	/**
	 * @param listOfFiles
	 *            the listOfFiles to set
	 */
	public void setListOfFiles(List<String> listOfFiles) {
		this.listOfFiles = listOfFiles;
	}

	/**
	 * @return the vaultName
	 */
	public String getVaultName() {
		return vaultName;
	}

	/**
	 * @param vaultName
	 *            the vaultName to set
	 */
	public void setVaultName(String vaultName) {
		this.vaultName = vaultName;
	}

	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public void setRole(String role) {
		this.role = role;
		if (role.equalsIgnoreCase("manager")) {
			this.TotalStorage = 5120;
			this.TotalStorageLeft = 5120;
		} else {
			this.TotalStorage = 1024;
		}
	}

	/**
	 * @return the totalStorage
	 */
	public long getTotalStorage() {
		return TotalStorage;
	}

	/**
	 * @param totalStorage
	 *            the totalStorage to set
	 */
	public void setTotalStorage(long totalStorage) {
		TotalStorage = totalStorage;
	}

	/**
	 * @return the fileSize
	 */
	public List<Long> getFileSize() {
		return fileSize;
	}

	/**
	 * @param fileSize
	 *            the fileSize to set
	 */
	public void setFileSize(List<Long> fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * @return the fileArichiveId
	 */
	public List<String> getFileArichiveId() {
		return fileArichiveId;
	}

	/**
	 * @param fileArichiveId
	 *            the fileArichiveId to set
	 */
	public void setFileArichiveId(List<String> fileArichiveId) {
		this.fileArichiveId = fileArichiveId;
	}

	/**
	 * @return the filePaths
	 */
	public List<String> getFilePaths() {
		return filePaths;
	}

	/**
	 * @param filePaths
	 *            the filePaths to set
	 */
	public void setFilePaths(List<String> filePaths) {
		this.filePaths = filePaths;
	}

	/**
	 * @param fileName
	 *            add file to the list
	 */
	public void addFile(String fileName) {
		listOfFiles.add(fileName);
	}

	public String getArchiveId(int index) {
		String str = fileArichiveId.get(index);
		System.out.println("returning archive id");
		return (str);
	}

	public List<String> getListOfDate() {
		return listOfDate;
	}

	public void setListOfDate(List<String> listOfDate) {
		this.listOfDate = listOfDate;
	}

	/**
	 * @param size
	 *            remove file size from the list
	 */
	public void removefileSize(int index) {
		fileSize.remove(index);
	}

	/**
	 * @param archiveId
	 *            remove archival id from the list
	 */
	public void removefileArichiveId(int index) {
		fileArichiveId.remove(index);
	}

	/**
	 * @param filePath
	 *            remove file path from the list
	 */
	public void removeFilePath(int index) {
		filePaths.remove(index);
	}

	/**
	 * @param index
	 *            remove file uploading date from the list
	 */
	public void removeFileUploadingDate(int index) {
		listOfDate.remove(index);
	}

	public void removeFileFromList(int index) {
		listOfFiles.remove(index);
	}

	public long getFileSizeFile(int index) {
		// TODO Auto-generated method stub
		return fileSize.get(index);
	}

	/**
	 * @return the sharedVaultNames
	 */
	public List<String> getSharedVaultNames() {
		return sharedVaultNames;
	}

	/**
	 * @param sharedVaultNames
	 *            the sharedVaultNames to set
	 */
	public void setSharedVaultNames(List<String> sharedVaultNames) {
		this.sharedVaultNames = sharedVaultNames;
	}

	/**
	 * @return the sharedFileNames
	 */
	public List<String> getSharedFileNames() {
		return sharedFileNames;
	}

	/**
	 * @param sharedFileNames
	 *            the sharedFileNames to set
	 */
	public void setSharedFileNames(List<String> sharedFileNames) {
		this.sharedFileNames = sharedFileNames;
	}

	/**
	 * @return the sharedArchiveIDs
	 */
	public List<String> getSharedArchiveIDs() {
		return sharedArchiveIDs;
	}

	/**
	 * @param sharedArchiveIDs
	 *            the sharedArchiveIDs to set
	 */
	public void setSharedArchiveIDs(List<String> sharedArchiveIDs) {
		this.sharedArchiveIDs = sharedArchiveIDs;
	}

	public void addSharedVaultName(int index, String vaultName) {
		sharedVaultNames.add(index, vaultName);
	}

	public void addSharedFileName(String fileName) {
		sharedFileNames.add(fileName);
	}

	public void addSharedArchiveId(int index, String ArchiveId) {
		sharedArchiveIDs.add(index, ArchiveId);
	}

	public String getArchiveIDByFileName(String fileName) {
		int index = listOfFiles.indexOf(fileName);
		return fileArichiveId.get(index);
	}

	public String getSharedFileVaultName(int index) {
		return sharedVaultNames.get(index);
	}

	public String getSharedFileArchivalId(int index) {
		return sharedArchiveIDs.get(index);
	}

	public long getSharedFileSize(int index) {
		return sharedFileSizes.get(index);
	}

	/**
	 * @return the sharedFileSizes
	 */
	public List<Long> getSharedFileSizes() {
		return sharedFileSizes;
	}

	/**
	 * @param sharedFileSizes
	 *            the sharedFileSizes to set
	 */
	public void setSharedFileSizes(List<Long> sharedFileSizes) {
		this.sharedFileSizes = sharedFileSizes;
	}

	public void addSharedFileSize(int index, Long fileSize) {
		sharedFileSizes.add(index, fileSize);
	}

	public void addSharedFileDates(int index, String date) {
		sharedFileDates.add(index, date);
	}

	public long getFileSizeByFileName(String fileName) {
		int index = listOfFiles.indexOf(fileName);
		return fileSize.get(index);
	}

	public void removeSharedFileDetails(int index) {
		sharedArchiveIDs.remove(index);
		sharedFileNames.remove(index);
		sharedFileSizes.remove(index);
		sharedVaultNames.remove(index);
		sharedFileDates.remove(index);
	}

	/**
	 * @return the trashListOfFiles
	 */
	public List<String> getTrashListOfFiles() {
		return trashListOfFiles;
	}

	/**
	 * @param trashListOfFiles
	 *            the trashListOfFiles to set
	 */
	public void setTrashListOfFiles(List<String> trashListOfFiles) {
		this.trashListOfFiles = trashListOfFiles;
	}

	/**
	 * @return the trashFileSizes
	 */
	public List<Long> getTrashFileSizes() {
		return trashFileSizes;
	}

	/**
	 * @param trashFileSizes
	 *            the trashFileSizes to set
	 */
	public void setTrashFileSizes(List<Long> trashFileSizes) {
		this.trashFileSizes = trashFileSizes;
	}

	/**
	 * @return the trashListOfDates
	 */
	public List<String> getTrashListOfDates() {
		return trashListOfDates;
	}

	/**
	 * @param trashListOfDates
	 *            the trashListOfDates to set
	 */
	public void setTrashListOfDates(List<String> trashListOfDates) {
		this.trashListOfDates = trashListOfDates;
	}

	/**
	 * @return the trashFileArichiveIds
	 */
	public List<String> getTrashFileArichiveIds() {
		return trashFileArichiveIds;
	}

	/**
	 * @param trashFileArichiveIds
	 *            the trashFileArichiveIds to set
	 */
	public void setTrashFileArichiveIds(List<String> trashFileArichiveIds) {
		this.trashFileArichiveIds = trashFileArichiveIds;
	}

	public void addTrashListOfFiles(String fileName) {
		trashListOfFiles.add(fileName);
	}

	public void addTrashFileSizes(int index, long fileSize) {
		trashFileSizes.add(index, fileSize);
	}

	public void addtrashFileArichiveIds(int index, String ArichiveId) {
		trashFileArichiveIds.add(index, ArichiveId);
	}

	public String getTrashFileVaultName(int index) {
		return trashVaultNames.get(index);
	}

	public int getTrashFileIndex(String fileName) {
		return trashListOfFiles.indexOf(fileName);
	}

	public String getTrashCameFrom(int index) {
		return trashCameFrom.get(index);
	}

	public void addTrashVaultName(int index, String vaultName) {
		trashVaultNames.add(index, vaultName);
	}

	public void addTrashFileDeletionDate(int index, String date) {
		trashListOfDates.add(index, date);
	}

	public void addTrashCameFrom(int index, String status) {
		trashCameFrom.add(index, status);
	}

	/**
	 * @return the trashVaultNames
	 */
	public List<String> getTrashVaultNames() {
		return trashVaultNames;
	}

	/**
	 * @param trashVaultNames
	 *            the trashVaultNames to set
	 */
	public void setTrashVaultNames(List<String> trashVaultNames) {
		this.trashVaultNames = trashVaultNames;
	}

	/**
	 * @return the trashCameFrom
	 */
	public List<String> getTrashCameFrom() {
		return trashCameFrom;
	}

	/**
	 * @param trashCameFrom
	 *            the trashCameFrom to set
	 */
	public void setTrashCameFrom(List<String> trashCameFrom) {
		this.trashCameFrom = trashCameFrom;
	}

	/**
	 * @return the sharedFileDates
	 */
	public List<String> getSharedFileDates() {
		return sharedFileDates;
	}

	/**
	 * @param sharedFileDates
	 *            the sharedFileDates to set
	 */
	public void setSharedFileDates(List<String> sharedFileDates) {
		this.sharedFileDates = sharedFileDates;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the emailid
	 */
	public String getEmailid() {
		return emailid;
	}

	/**
	 * @param emailid
	 *            the emailid to set
	 */
	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}
}