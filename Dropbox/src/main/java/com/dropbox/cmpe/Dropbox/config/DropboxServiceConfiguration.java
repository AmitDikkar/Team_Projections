/**

 * 
 */
package com.dropbox.cmpe.Dropbox.config;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;

/**
 * @author Team Projections
 * 
 */
public class DropboxServiceConfiguration extends Configuration {

	@NotEmpty
	@JsonProperty
	private String amazonTimeZone;

	@NotEmpty
	@JsonProperty
	private String amazonUsername;

	@NotEmpty
	@JsonProperty
	private String amazonPassword;

	@NotEmpty
	@JsonProperty
	private String dbName;

	@NotEmpty
	@JsonProperty
	private String dbCollection;

	@NotEmpty
	@JsonProperty
	private String databaseUsername;

	@NotEmpty
	@JsonProperty
	private String databasePassword;

	@NotEmpty
	@JsonProperty
	private String databaseAddress;

	@JsonProperty
	private int databasePort;

	/**
	 * @return the amazonTimeZone
	 */
	public String getAmazonTimeZone() {
		return amazonTimeZone;
	}

	/**
	 * @param amazonTimeZone
	 *            the amazonTimeZone to set
	 */
	public void setAmazonTimeZone(String amazonTimeZone) {
		this.amazonTimeZone = amazonTimeZone;
	}

	/**
	 * @return the amazonUsername
	 */
	public String getAmazonUsername() {
		return amazonUsername;
	}

	/**
	 * @param amazonUsername
	 *            the amazonUsername to set
	 */
	public void setAmazonUsername(String amazonUsername) {
		this.amazonUsername = amazonUsername;
	}

	/**
	 * @return the amazonPassword
	 */
	public String getAmazonPassword() {
		return amazonPassword;
	}

	/**
	 * @param amazonPassword
	 *            the amazonPassword to set
	 */
	public void setAmazonPassword(String amazonPassword) {
		this.amazonPassword = amazonPassword;
	}

	/**
	 * @return the dbName
	 */
	public String getDbName() {
		return dbName;
	}

	/**
	 * @param dbName
	 *            the dbName to set
	 */
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	/**
	 * @return the dbCollection
	 */
	public String getDbCollection() {
		return dbCollection;
	}

	/**
	 * @param dbCollection
	 *            the dbCollection to set
	 */
	public void setDbCollection(String dbCollection) {
		this.dbCollection = dbCollection;
	}

	/**
	 * @return the databaseUsername
	 */
	public String getDatabaseUsername() {
		return databaseUsername;
	}

	/**
	 * @param databaseUsername
	 *            the databaseUsername to set
	 */
	public void setDatabaseUsername(String databaseUsername) {
		this.databaseUsername = databaseUsername;
	}

	/**
	 * @return the databasePassword
	 */
	public String getDatabasePassword() {
		return databasePassword;
	}

	/**
	 * @param databasePassword
	 *            the databasePassword to set
	 */
	public void setDatabasePassword(String databasePassword) {
		this.databasePassword = databasePassword;
	}

	/**
	 * @return the databaseAddress
	 */
	public String getDatabaseAddress() {
		return databaseAddress;
	}

	/**
	 * @param databaseAddress
	 *            the databaseAddress to set
	 */
	public void setDatabaseAddress(String databaseAddress) {
		this.databaseAddress = databaseAddress;
	}

	/**
	 * @return the databasePort
	 */
	public int getDatabasePort() {
		return databasePort;
	}

	/**
	 * @param databasePort
	 *            the databasePort to set
	 */
	public void setDatabasePort(int databasePort) {
		this.databasePort = databasePort;
	}

}
