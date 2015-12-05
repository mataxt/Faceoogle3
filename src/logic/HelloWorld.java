package logic;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("hello")
public class HelloWorld {
 @GET
 @Path("message")
 @Produces("text/plain")
 public String getMessage() {
	 return "Yo!";
 }
}