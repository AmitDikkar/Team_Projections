package com.dropbox.cmpe.Dropbox.ui.resources;

//import javax.ws.rs.GET;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.dropbox.cmpe.Dropbox.ui.views.LoginView;
import com.dropbox.cmpe.Dropbox.ui.views.RegisterView;



@Path("/register")
@Produces(MediaType.TEXT_HTML)
public class RegisterResource {
	

	    public RegisterResource() {
		
	    }

	    @GET

	    public RegisterView getRegisteration() {

	    	return new RegisterView();

	    }
	
}


