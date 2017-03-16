package com.ibm.pi.libertycar.config;

import com.ibm.pi.libertycar.control.CarControllerInterface;
import com.ibm.pi.libertycar.security.UserIdManager;

public class Globals {
    
    private static CarControllerInterface controller = null;
    private static UserIdManager uim = null;

    public static CarControllerInterface getController() {
        return controller;
    }

    public static void setController(CarControllerInterface controller) {
        Globals.controller = controller;
    }

    public static void setUserIdManager(UserIdManager uim) {
      Globals.uim = uim;
      
    }

    public static UserIdManager getUserIdManager() {
      return uim;
    }


}
