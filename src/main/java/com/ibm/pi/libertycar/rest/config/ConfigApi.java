package com.ibm.pi.libertycar.rest.config;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ibm.pi.libertycar.config.FrequencySettings;

@Path("/config")
public class ConfigApi {
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String helloWorld() {
        return "Hello world2";
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FrequencySettings setNewFrequencies(FrequencySettings settings) {
        settings.setLeftMax(settings.getLeftMax() + 1);
        return settings;
    }

}
