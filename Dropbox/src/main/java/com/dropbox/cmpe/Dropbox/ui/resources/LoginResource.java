package com.dropbox.cmpe.Dropbox.ui.resources;

//import javax.ws.rs.GET;
import javax.ws.rs.GET;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.dropbox.cmpe.Dropbox.ui.views.LoginView;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class LoginResource {
	 //private final DocumentRepository documentRepository;

	    public LoginResource() {
		//this.documentRepository=documentRepository;
	    }

	    @GET
	    public LoginView getHome() {

	    	return new LoginView();

	    }
}


