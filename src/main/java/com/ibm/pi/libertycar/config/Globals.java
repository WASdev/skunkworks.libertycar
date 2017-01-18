package com.ibm.pi.libertycar.config;

import com.ibm.pi.libertycar.control.CarControllerInterface;

public class Globals {
    
    private static CarControllerInterface controller = null;

    public static CarControllerInterface getController() {
        return controller;
    }

    public static void setController(CarControllerInterface controller) {
        Globals.controller = controller;
    }

}
