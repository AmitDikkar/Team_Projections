package com.dropbox.cmpe.Dropbox.ui.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.dropbox.cmpe.Dropbox.domain.DatabaseDetails;
import com.dropbox.cmpe.Dropbox.dto.MyMongo;
import com.dropbox.cmpe.Dropbox.ui.views.FilesForMustach;
import com.dropbox.cmpe.Dropbox.ui.views.HomeView;

@Path("/home/{username}")
@Produces(MediaType.TEXT_HTML)
public class HomeResource {
	MyMongo mymongo;
	DatabaseDetails databaseDetails;
	//List<FilesForMustach> listOfFiles = new ArrayList<FilesForMustach>();

	public HomeResource(MyMongo mymongo,DatabaseDetails databaseDetails) {
		this.mymongo=mymongo;
		this.databaseDetails=databaseDetails;
	}

	@GET
	@Path("/listoffiles")
	public HomeView getHome(@PathParam("username") String username) {
		ArrayList<FilesForMustach> listOfFiles = new ArrayList<FilesForMustach>();
		listOfFiles = getList(username);
		return new HomeView(listOfFiles);
		}

	private ArrayList<FilesForMustach> getList(String username) {
		// Auto-generated method stub
		ArrayList<FilesForMustach> listOfFiles = new ArrayList<FilesForMustach>();
		List<String> files = mymongo.getFileNames(username);
		List<String> dates =  mymongo.getFileDates(username);
		int index=0;
		for(String file: files){
			FilesForMustach element = new FilesForMustach();
			element.setFile(file);
			listOfFiles.add(index, element);
			index++;		// I am not sure whether this function name is get or find. Check it.
		}
		index=0;
		int index1=0;
		for(String date: dates){
			//FilesForMustach element = new FilesForMustach();
			//element.setDate(date);
			listOfFiles.get(index1).setDate(date);
			index1++;
			}
		return listOfFiles;
	}		
		
	}
