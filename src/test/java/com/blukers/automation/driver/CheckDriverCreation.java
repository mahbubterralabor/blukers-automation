package com.blukers.automation.driver;

public class CheckDriverCreation {
    public static void main(String[] args) {

        try {
            DriverService.start();
            System.out.println("Driver started successfully");
        } finally {
            DriverService.stop();
            System.out.println("Driver stopped successfully");
        }
    }

}
