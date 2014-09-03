package com.ibm.pi.libertycar.webapp;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;


@Path("/ipIdentifierEndpoint/{sessionId}")
public class IpIdentifierEndpoint  {
	
	@POST
	public String postIp(@Context HttpServletRequest req, @PathParam(value="sessionId") String sessionId){
		System.out.println("IM IN");
		String id = CarAdmin.getIdFromIp(req.getRemoteAddr());
		CarControlEndpoint.ipSessionMap.put(sessionId, id); 

		System.out.println("GOOD SESSION: "+sessionId);
		return "im only here because im testing if produces is needed";
	}
}
