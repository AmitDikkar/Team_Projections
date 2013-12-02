//DocumentResource
package com.dropbox.cmpe.Dropbox.api.resources;

import java.io.File;
import java.io.IOException;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.glacier.model.CreateVaultResult;
import com.amazonaws.services.glacier.model.DeleteArchiveRequest;
import com.amazonaws.services.glacier.transfer.ArchiveTransferManager;
import com.amazonaws.services.glacier.transfer.UploadResult;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.util.ImmutableMapParameter.Builder;
import com.dropbox.cmpe.Dropbox.domain.AmazonCommon;
import com.dropbox.cmpe.Dropbox.domain.User;
import com.dropbox.cmpe.Dropbox.dto.MyMongo;
import com.yammer.metrics.annotation.Timed;

/**
 * @author Team Projection
 * 
 */
@Path("v1/documents")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DocumentResource {

	AmazonGlacierClient client;
	private MyMongo myMongo;

	// -------new user so new vault..
	public DocumentResource(MyMongo myMongo) {
		this.myMongo = myMongo;
	}

	@GET
	@Path("/old/{existinguser}/download")
	@Timed(name = "inventory-details")
	public Response getArchive(@QueryParam("filepath") String filePath,
			@PathParam("existinguser") String existingUser,
			@QueryParam("fileName") String fileName) {

		String vaultName;
		System.out.println("inside invetory url hit");
		System.out.println("file name retrieved: " + fileName);
		vaultName = myMongo.getVaultName(existingUser);
		System.out.println("Vault Name is: " + vaultName);
		String archiveId = myMongo.getArchiveID(existingUser, fileName);
		System.out.println("Archive ID is: " + archiveId);
		AmazonGlacierClient glacierClient;
		AmazonSQSClient sqsClient;
		AmazonSNSClient snsClient;
		AmazonCommon common = new AmazonCommon();
		AWSCredentials credentials = common.getCredentials();

		glacierClient = new AmazonGlacierClient(credentials);
		sqsClient = new AmazonSQSClient(credentials);
		snsClient = new AmazonSNSClient(credentials);

		glacierClient.setEndpoint("https://glacier.us-west-1.amazonaws.com/");
		sqsClient.setEndpoint("https://sqs.us-west-1.amazonaws.com");
		snsClient.setEndpoint("https://sns.us-west-1.amazonaws.com");

		try {
			ArchiveTransferManager atm = new ArchiveTransferManager(
					glacierClient, sqsClient, snsClient);
			System.out.println("going to download");
			atm.download(vaultName, archiveId, new File(filePath));
			System.out.println("finished download");
		} catch (Exception e) {
			System.out.println("In catch");
			e.printStackTrace();
			System.err.println(e);
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Check the path you have entered").build();
		}
		System.out.println("Leaving inventory");
		return Response.ok(200).build();
	}

	@DELETE
	@Path("/old/{existinguser}/delete")
	@Timed(name = "delete-file")
	public Response deleteFile(@PathParam("existinguser") String existingUser,
			@QueryParam("filename") String fileName) {
		myMongo.deleteFileDetails(existingUser, fileName);
		return Response.status(200).build();
	}

	@POST
	@Path("/old/{existinguser}/share")
	@Timed(name = "share-file")
	public Response shareFile(@PathParam("existinguser") String existingUser,
			@QueryParam("filename") String fileName,
			@QueryParam("sharewith") String sharewith) {
		if (myMongo.isUserNameExist(sharewith)) {
			myMongo.shareFile(existingUser, sharewith, fileName);
			return Response.status(200).build();
		} else {
			return Response
					.status(Response.Status.BAD_REQUEST)
					.entity("Sorry we couldn't find the username you have provided")
					.build();
		}
	}

	// delete shared file
	@DELETE
	@Path("/old/{existinguser}/share/delete")
	@Timed(name = "delete-shared-file")
	public Response deleteSharedFile(
			@PathParam("existinguser") String existingUser,
			@QueryParam("fileName") String fileName) {
		myMongo.deleteSharedFileDetails(existingUser, fileName);
		return Response.status(200).build();
	}

	@GET
	@Path("/old/{existinguser}/share/download")
	@Timed(name = "download-shared-file")
	public Response downloadSharedFile(
			@PathParam("existinguser") String existingUser,
			@QueryParam("fileName") String fileName,
			@QueryParam("filePath") String filePath) {
		String vaultName = myMongo.getSharedFileVaultName(existingUser,
				fileName);
		String archiveID = myMongo.getSharedFileArchivalId(existingUser,
				fileName);
		AmazonGlacierClient glacierClient;
		AmazonSQSClient sqsClient;
		AmazonSNSClient snsClient;
		AmazonCommon common = new AmazonCommon();
		AWSCredentials credentials = common.getCredentials();
		glacierClient = new AmazonGlacierClient(credentials);
		sqsClient = new AmazonSQSClient(credentials);
		snsClient = new AmazonSNSClient(credentials);
		glacierClient.setEndpoint("https://glacier.us-west-1.amazonaws.com/");
		sqsClient.setEndpoint("https://sqs.us-west-1.amazonaws.com");
		snsClient.setEndpoint("https://sns.us-west-1.amazonaws.com");
		try {
			ArchiveTransferManager atm = new ArchiveTransferManager(
					glacierClient, sqsClient, snsClient);
			System.out.println("going to download");
			atm.download(vaultName, archiveID, new File(filePath));
			System.out.println("finished download");
			return Response.status(200).build();

		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	// delete file from trash*/
	@DELETE
	@Path("/old/{existinguser}/trash/delete")
	@Timed(name = "delete-file-from-trash")
	public Response deleteFileFromTrash(
			@PathParam("existinguser") String existingUser,
			@QueryParam("fileName") String fileName) {

		String vaultName = myMongo
				.getTrashFileVaultName(existingUser, fileName);
		String archiveId = myMongo.getTrashArchivalId(existingUser, fileName);
		String msg = "Deleting " + archiveId + " from Glacier vault "
				+ vaultName;

		System.out.println(msg);
		AmazonCommon common = new AmazonCommon();
		AWSCredentials credentials = common.getCredentials();
		AmazonGlacierClient client = common.getClient(credentials);

		try {
			client.deleteArchive(new DeleteArchiveRequest().withVaultName(
					vaultName).withArchiveId(archiveId));
			myMongo.deleteFileFromTrash(existingUser, fileName);
			System.out.println("Deleted archive: " + archiveId);
			return Response.status(200).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Sorry!!! Couldn't delete the file").build();
		}
	}

	private String getFileNameFromPath(String filePath) {
		// TODO Auto-generated method stub
		String fileName=null;
		String newstr=filePath;
		String[] str=newstr.split("\\\\");
		for (String string : str) {
			fileName=string;
			System.out.println(string);
		}
		System.out.println("update is "+fileName);
		//compute the fileName
		return(fileName);
	}


	private long getFileSize(String filePath) {
		// TODO Auto-generated method stub
		File file = new File(filePath);
		long fileSize = file.length() / 1048786;
		System.out.println("File size is+"+fileSize);
		return fileSize;
	}

	@POST
	@Path("/old/{existinguser}/upload")
	@Timed(name="upload-file")
	public Response uploadFile(@PathParam("existinguser") String existingUser, @QueryParam("filepath") String filePath) throws IOException
	{
		//to test
		String vaultName;
		System.out.println("Inside file upload method");
		System.out.println("FilePath is : "+filePath);
		//TODO: implement below two functions
		String fileName = getFileNameFromPath(filePath);
		long fileSize = getFileSize(filePath);
		//String vaultName="myvault1";
		//String filePathUpload=filePath;
		/*
		 * ensured that the file is on your classpath, then you should have everything correct.
		 */
		Boolean allow = myMongo.checkFileSizeToUpload(existingUser,fileSize);
		
		if(allow == true){
			vaultName = myMongo.getVaultName(existingUser);
			System.out.println("Vault Name is: "+vaultName);
			AmazonCommon common = new AmazonCommon();
			AWSCredentials credentials = common.getCredentials();
			AmazonGlacierClient client = common.getClient(credentials);

			//Create this vault only if user is new
			//CreateVaultResult vaultresult = common.createVault(vaultName, client);

			boolean result = uploadFileOnGlacier(existingUser,client,credentials,fileName, filePath,vaultName,fileSize);

			if(result==true){
				//Display message that file is uploaded
				System.out.println("Operation Successful");
				return Response.ok().build();
			}
			else{
				//Display message that file is not uploaded
				System.out.println("OPERATION UNSUCCESSFUL");
				return Response
						.status(Response.Status.BAD_REQUEST)
						.entity("Sorry we couldn't upload your file")
						.build();
			}
		}
		else{
			//return failure message: saying size is not available
			return Response
					.status(Response.Status.UNAUTHORIZED)
					.entity("Sorry!!! this file has bigger size")
					.build();
		}
	}

//recover file from trash
	@POST
	@Path("/old/{existinguser}/trash/recover")
	@Timed(name="delete-file-from-trash")
	public Response recoverFileFromTrash(@PathParam("existinguser") String existingUser, @QueryParam("fileName") String fileName){
		myMongo.recoverFile(existingUser, fileName);
		return Response.status(200).build();
	}
	/**
	 * @param client
	 * @param credentials
	 * @param filePathUpload
	 * @param vaultName
	 * @return true if successful,
	 * false if unsuccessful
	 */
	
	private boolean uploadFileOnGlacier(String existingUser, AmazonGlacierClient client, AWSCredentials credentials, String fileName, String filePathUpload, String vaultName, long fileSize){
	
		try {
		
			ArchiveTransferManager atm = new ArchiveTransferManager(client,credentials);
			
			System.out.println("in try");
			System.out.println("File to be uploaded is "+filePathUpload);

			UploadResult result1 = atm.upload(vaultName, filePathUpload, new File(filePathUpload));
			
			System.out.println(result1);
			System.out.println("Archive ID: " + result1.getArchiveId());
			if(result1.getArchiveId() != null){
				myMongo.addNewFileDetails(existingUser, fileName, filePathUpload, result1.getArchiveId(), fileSize);
				return true;
			}
			else{
				return false;
			}
		} 
		catch (Exception e) 
		{
			
			System.out.println("in catch");
			System.err.println(e);
		
			return false;
		
		}
	}

}