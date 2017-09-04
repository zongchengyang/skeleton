package controllers;


import javax.ws.rs.GET;
import javax.ws.rs.Path;


@Path("/netid")
public class netidController {
    @GET
    public String getnetid() {
        return "zy338";
    }
}
