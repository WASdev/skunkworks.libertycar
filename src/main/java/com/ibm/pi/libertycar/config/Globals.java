package com.ibm.pi.libertycar.config;

import com.ibm.pi.libertycar.control.CarController;

public class Globals {
    
    private static CarController controller = null;

    public static CarController getController() {
        return controller;
    }

    public static void setController(CarController controller) {
        Globals.controller = controller;
    }

}
