package com.ticket.ticketing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.Scanner;

@Component
public class TicketingCommandLineRunner implements CommandLineRunner {
    @Autowired
    private TicketService ticketService;
    private Thread vendorThread;
    private Thread customerThread;

    public void run(String... args) throws Exception{
        Scanner scanner = new Scanner(System.in);
        String command;
        System.out.println("Enter 'start' to start the ticketing system, 'stop' to stop, or 'exit' to quit.");

        while (true) {
            System.out.print("Command: ");
            command = scanner.nextLine().trim().toLowerCase();

            if (command.equals("start")) {
                try {
                    // Validate and get ticket release rate
                    int ticketReleaseRate = getValidatedInput("Enter ticket release rate (positive integer): ", scanner);

                    // Validate and get customer retrieval rate
                    int customerRetrievalRate = getValidatedInput("Enter customer retrieval rate (positive integer): ", scanner);

                    // Validate and get max ticket capacity
                    int maxTicketCapacity = getValidatedInput("Enter maximum ticket capacity (positive integer): ", scanner);

                    // Start the ticketing system
                    startTicketingSystem(ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else if (command.equals("stop")) {
                try {
                    stopTicketingSystem();
                } catch (InterruptedException e) {
                    System.out.println("Failed to stop the ticketing system: " + e.getMessage());
                }
            } else if (command.equals("exit")) {
                System.out.println("Exiting the ticketing system.");
                break;
            } else {
                System.out.println("Invalid command. Use 'start', 'stop', or 'exit'.");
            }
        }
        scanner.close();
    }

    // Method to get validated positive integer input from the user
    private int getValidatedInput(String prompt, Scanner scanner) {
        int value;
        while (true) {
            System.out.print(prompt);
            try {
                value = Integer.parseInt(scanner.nextLine());
                if (value <= 0) {
                    throw new InputMismatchException("Value must be a positive integer.");
                }
                break; // Valid input, exit loop
            } catch (NumberFormatException | InputMismatchException e) {
                System.out.println("Invalid input. " + e.getMessage());
            }
        }
        return value;
    }

    public void startTicketingSystem(int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity) throws InterruptedException {
        // Initialize ticket pool
        ticketService.initializeTicketPool(0, maxTicketCapacity);

        // Create Vendor and Customer with dynamic rates
        Vendor vendor = new Vendor(ticketService, ticketReleaseRate);
        Customer customer = new Customer(ticketService, customerRetrievalRate);

        // Start Vendor and Customer threads
        vendorThread = new Thread(vendor);
        customerThread = new Thread(customer);

        vendorThread.start();
        customerThread.start();
        System.out.println("Ticketing system started.");


        // Allow the system to run for 10 seconds
        Thread.sleep(10000);

        // Stop the threads after running
        stopTicketingSystem();
    }

    public void stopTicketingSystem() throws InterruptedException {
        if (vendorThread != null && customerThread != null) {
            vendorThread.interrupt();
            customerThread.interrupt();

            vendorThread.join();
            customerThread.join();

            System.out.println("Ticketing system stopped.");

        }else{
            System.out.println("Ticketing system is not running.");
        }
    }

}
