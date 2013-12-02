package com.dropbox.cmpe.Dropbox;

import com.dropbox.cmpe.Dropbox.api.resources.DocumentResource;
import com.dropbox.cmpe.Dropbox.api.resources.RootResource;
import com.dropbox.cmpe.Dropbox.api.resources.UserResource;
import com.dropbox.cmpe.Dropbox.config.ConfigElements;
import com.dropbox.cmpe.Dropbox.config.DropboxServiceConfiguration;
import com.dropbox.cmpe.Dropbox.domain.DatabaseDetails;
import com.dropbox.cmpe.Dropbox.dto.MyMongo;
import com.dropbox.cmpe.Dropbox.ui.resources.DeletedFileResource;
import com.dropbox.cmpe.Dropbox.ui.resources.HomeResource;
import com.dropbox.cmpe.Dropbox.ui.resources.LoginResource;
import com.dropbox.cmpe.Dropbox.ui.resources.RegisterResource;
import com.dropbox.cmpe.Dropbox.ui.resources.SharedFileResource;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.assets.AssetsBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.views.ViewBundle;

import de.spinscale.dropwizard.jobs.JobsBundle;

/**
 * CMPE 273 - Dropbox using amazon Glacier Author: Team Projections
 * 
 */

public class DropboxService extends Service<DropboxServiceConfiguration> {
	public static void main(String[] args) throws Exception {
		new DropboxService().run(args);
	}

	@Override
	public void initialize(Bootstrap<DropboxServiceConfiguration> bootstrap) {
		bootstrap.setName("dropbox-service");
		bootstrap.addBundle(new ViewBundle());
		bootstrap.addBundle(new AssetsBundle());
		bootstrap.addBundle(new JobsBundle("com.dropbox.cmpe.Dropbox"));
	}

	@Override
	public void run(DropboxServiceConfiguration configuration,
			Environment environment) throws Exception {

		// Configuring elements in the static function
		ConfigElements.setAmazonTimeZone(configuration.getAmazonTimeZone());
    	ConfigElements.setAmazonUsername(configuration.getAmazonUsername());
    	ConfigElements.setAmazonPassword(configuration.getAmazonPassword());
    	ConfigElements.setDatabaseAddress(configuration.getDatabaseAddress());
    	ConfigElements.setDatabasePassword(configuration.getDatabasePassword());
    	ConfigElements.setDatabasePort(configuration.getDatabasePort());
    	ConfigElements.setDatabaseUsername(configuration.getDatabaseUsername());
    	ConfigElements.setDbCollection(configuration.getDbCollection());
    	ConfigElements.setDbName(configuration.getDbName());
		// Added root resource - kept nothing as of now.
		MyMongo myMongo = new MyMongo();
		environment.addResource(RootResource.class);
		DatabaseDetails databaseDetails=new DatabaseDetails();
		// Added Document resource API to handle file uploading
		environment.addResource(new DocumentResource(myMongo));
		environment.addResource(new UserResource(myMongo));
		environment.addResource(new LoginResource());
    	environment.addResource(new HomeResource(myMongo,databaseDetails));
    	environment.addResource(new DeletedFileResource(myMongo));
    	environment.addResource(new SharedFileResource(myMongo));
    	environment.addResource(new RegisterResource());
	}
}