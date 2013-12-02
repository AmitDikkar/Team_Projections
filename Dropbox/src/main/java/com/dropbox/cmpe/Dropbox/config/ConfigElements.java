/**
 * 
 */
package com.dropbox.cmpe.Dropbox.config;

/**
 * @author Team Projections
 *
 */
public class ConfigElements {
	
	private static String amazonTimeZone;
	
	private static String amazonUsername;
	
	private static String amazonPassword;
	
	private static String dbName="dropbox";
	
    private static String dbCollection="dropdetails";
    
    private static String databaseUsername="teamprojections";
    
    private static String databasePassword="teamprojections";

	private static String databaseAddress="ds053788.mongolab.com";

	private static int databasePort=53788;

	/**
	 * @return the amazonTimeZone
	 */
	public static String getAmazonTimeZone() {
		return amazonTimeZone;
	}

	/**
	 * @param amazonTimeZone the amazonTimeZone to set
	 */
	public static void setAmazonTimeZone(String amazonTimeZone) {
		ConfigElements.amazonTimeZone = amazonTimeZone;
	}

	/**
	 * @return the amazonUsername
	 */
	public static String getAmazonUsername() {
		return amazonUsername;
	}

	/**
	 * @param amazonUsername the amazonUsername to set
	 */
	public static void setAmazonUsername(String amazonUsername) {
		ConfigElements.amazonUsername = amazonUsername;
	}

	/**
	 * @return the amazonPassword
	 */
	public static String getAmazonPassword() {
		return amazonPassword;
	}

	/**
	 * @param amazonPassword the amazonPassword to set
	 */
	public static void setAmazonPassword(String amazonPassword) {
		ConfigElements.amazonPassword = amazonPassword;
	}

	/**
	 * @return the dbName
	 */
	public static String getDbName() {
		return dbName;
	}

	/**
	 * @param dbName the dbName to set
	 */
	public static void setDbName(String dbName) {
		ConfigElements.dbName = dbName;
	}

	/**
	 * @return the dbCollection
	 */
	public static String getDbCollection() {
		return dbCollection;
	}

	/**
	 * @param dbCollection the dbCollection to set
	 */
	public static void setDbCollection(String dbCollection) {
		ConfigElements.dbCollection = dbCollection;
	}

	/**
	 * @return the databaseUsername
	 */
	public static String getDatabaseUsername() {
		return databaseUsername;
	}

	/**
	 * @param databaseUsername the databaseUsername to set
	 */
	public static void setDatabaseUsername(String databaseUsername) {
		ConfigElements.databaseUsername = databaseUsername;
	}

	/**
	 * @return the databasePassword
	 */
	public static String getDatabasePassword() {
		return databasePassword;
	}

	/**
	 * @param databasePassword the databasePassword to set
	 */
	public static void setDatabasePassword(String databasePassword) {
		ConfigElements.databasePassword = databasePassword;
	}

	/**
	 * @return the databaseAddress
	 */
	public static String getDatabaseAddress() {
		return databaseAddress;
	}

	/**
	 * @param databaseAddress the databaseAddress to set
	 */
	public static void setDatabaseAddress(String databaseAddress) {
		ConfigElements.databaseAddress = databaseAddress;
	}

	/**
	 * @return the databasePort
	 */
	public static int getDatabasePort() {
		return databasePort;
	}

	/**
	 * @param databasePort the databasePort to set
	 */
	public static void setDatabasePort(int databasePort) {
		ConfigElements.databasePort = databasePort;
	}
}
