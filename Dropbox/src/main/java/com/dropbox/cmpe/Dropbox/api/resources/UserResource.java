/**
 * 
 */
package com.dropbox.cmpe.Dropbox.api.resources;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.glacier.model.CreateVaultResult;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.dropbox.cmpe.Dropbox.domain.AmazonCommon;
import com.dropbox.cmpe.Dropbox.domain.User;
import com.dropbox.cmpe.Dropbox.dto.MyMongo;
import com.yammer.metrics.annotation.Timed;

/**
 * @author Team Projections
 * 
 */

@Path("v1/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

	private MyMongo myMongo;

	private String vaultName;

	public UserResource(MyMongo myMongo) {
		// TODO Auto-generated constructor stub
		this.myMongo = myMongo;
	}

	@POST
	@Path("/newuser")
	@Timed(name = "new-user-new-vault")
	public Response newUser(@Valid User newUser) throws Exception {
		String newUserName = myMongo.registerUser(newUser.getUsername(),
				newUser.getPassword(), newUser.getRole(), newUser.getEmailid());

		String newUserPassword = newUser.getPassword();
		String newUserRole = newUser.getRole();
		String newUserEmailID = newUser.getEmailid();

		System.out.println("Creating new vault for new user");
		this.vaultName = myMongo.getVaultName(newUserName);
		AmazonCommon common = new AmazonCommon();
		AWSCredentials credentials = common.getCredentials();
		AmazonGlacierClient client = common.getClient(credentials);
		CreateVaultResult vaultresult = common.createVault(this.vaultName,
				client);
		if (newUserName != newUser.getUsername()) {
			String responceMessage = "Your username will be : " + newUserName;
			System.out.println(newUserName);
			sentEmail(credentials,newUserName, newUserPassword, newUserRole, newUserEmailID);
			return Response.status(200).entity(responceMessage).build();
		} else {
			String responceMessage = "Your username will be : " + newUserName;
			sentEmail(credentials,newUserName, newUserPassword, newUserRole, newUserEmailID);
			return Response.status(200).entity(responceMessage).build();
		}

	}

	@POST
	@Path("/olduser")
	@Timed(name = "Login")
	public Response loginUser(@Valid User newUser) {
		String existingUser = newUser.getUsername();
		System.out.println("usename:" + newUser.getUsername());
		System.out.println("password:" + newUser.getPassword());
		if (myMongo.isUserNameExist(existingUser) == true) {
			if (myMongo.authenticateUser(newUser.getUsername(),
					newUser.getPassword())) {
				return Response.ok(200).build();
			} else {
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity("Please verify your username and password")
						.build();
			}
		} else {
			return Response
					.status(Response.Status.UNAUTHORIZED)
					.entity("Sorry!! This username doesn't exists in our system. Please sign up as new user")
					.build();
		}
	}

	public void sentEmail(AWSCredentials credentials,String newUserName, String newUserPassword, String newUserRole,String newUserEmailID){

		String FROM = "savanibharat@gmail.com";
		String TO = newUserEmailID;
		String SUBJECT = "Welcome to Amazon Glacier";
		String BODY = "This email was sent through Amazon SES by using the AWS SDK for Java.\n\n\nAmazon Glacier is an extremely low-cost storage service that provides secure and durable storage for data archiving and backup. In order to keep costs low, Amazon Glacier is optimized for data that is infrequently accessed and for which retrieval times of several hours are suitable. With Amazon Glacier, customers can reliably store large or small amounts of data for as little as $0.01 per gigabyte per month, a significant savings compared to on-premises solutions.Companies typically over-pay for data archiving. First, they're forced to make an expensive upfront payment for their archiving solution (which does not include the ongoing cost for operational expenses such as power, facilities, staffing, and maintenance). Second, since companies have to guess what their capacity requirements will be, they understandably over-provision to make sure they have enough capacity for data redundancy and unexpected growth. This set of circumstances results in under-utilized capacity and wasted money. With Amazon Glacier, you pay only for what you use. Amazon Glacier changes the game for data archiving and backup as you pay nothing upfront, pay a very low price for storage, and can scale your usage up or down as needed, while AWS handles all of the operational heavy lifting required to do data retention well. It only takes a few clicks in the AWS Management Console to set up Amazon Glacier and then you can upload any amount of data you choose."
				+ "\n\n\n\n\n You can now use the following login credentials.\n\n\n"
				+     "Username is :: "+newUserName+"\nPassword is :: " +newUserPassword;

		
		credentials.getAWSAccessKeyId();
		credentials.getAWSSecretKey();
		Destination destination = new Destination()
				.withToAddresses(new String[] { TO });
		Content subject = new Content().withData(SUBJECT);
		Content textBody = new Content().withData(BODY);
		Body body = new Body().withText(textBody);
		Message message = new Message().withSubject(subject).withBody(body);
		SendEmailRequest request = new SendEmailRequest().withSource(FROM)
				.withDestination(destination).withMessage(message);
		try {
			System.out
					.println("Attempting to send an email through AmazonSES by using the AWS SDK for Java...");
			AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient(
					credentials);
			client.sendEmail(request);
			System.out.println("Email sent!");
		} catch (Exception ex) {
			System.out.println("The email was not sent.");
			System.out.println("Error message: " + ex.getMessage());
		}
	}
		
	}
