package com.dropbox.cmpe.Dropbox.ui.views;

import java.util.ArrayList;

import java.util.List;

import com.yammer.dropwizard.views.View;

public class HomeView extends View{
	
	//private List<String> filenames;
	List<FilesForMustach> listOfFiles =new ArrayList<FilesForMustach>();
	/**
	 * @return the listOfFiles
	 */
	public List<FilesForMustach> getListOfFiles() {
		return listOfFiles;
	}
	/**
	 * @param listOfFiles the listOfFiles to set
	 */
	public void setListOfFiles(List<FilesForMustach> listOfFiles) {
		this.listOfFiles = listOfFiles;
	}
	
	public HomeView(List<FilesForMustach> listOfFiles) {
		super("home.mustache");
		this.listOfFiles = listOfFiles;
	}
	
}

