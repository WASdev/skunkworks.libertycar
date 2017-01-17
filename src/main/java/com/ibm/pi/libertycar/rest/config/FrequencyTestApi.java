package com.ibm.pi.libertycar.rest.config;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ibm.pi.libertycar.config.Globals;

@Path("/frequencyTest")
public class FrequencyTestApi {


    @Path("steering")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public void testSteering(@QueryParam("testFrequency") int steeringTestFrequency) {
        Globals.getController().testSteering(steeringTestFrequency, 5);
    }
    
    @Path("speed")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public void testSpeed(@QueryParam("testFrequency") int speedTestFrequency) {
        Globals.getController().testSpeed(speedTestFrequency, 5);
    }

}
