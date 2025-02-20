package com.ticket.ticketing;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;


@Service // Marks this class as a Spring service component, allowing it to be injected into other components.
public class TicketService {
    private int totalTickets;
    private int maxTicketCapacity;

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }
    // Initializes the ticket pool with the total number of tickets and the maximum ticket capacity.
    // This method is synchronized to ensure thread safety.
    public synchronized void initializeTicketPool(int totalTickets, int maxTicketCapacity){
        this.totalTickets=totalTickets;
        this.maxTicketCapacity=maxTicketCapacity;
    }

    // Adds a specified number of tickets to the pool.
    // Synchronized to handle concurrent access by multiple threads.
    public synchronized void addTickets(int count){
        while(totalTickets>=maxTicketCapacity){
            try {
                System.out.println("Ticket pool is full.");
                return;
                //wait();
            }catch (Exception e){
                Thread.currentThread().interrupt();
            }
        }

        totalTickets += count;

        System.out.println("Vendor added "+ count +" tickets. Total tickets: "+totalTickets);
        saveToFile();
        notifyAll();
    }

    // Method for purchasing a ticket.
    // Synchronized to prevent race conditions when accessing the ticket pool.
    public synchronized void purchaseTicket(){
        while (totalTickets == 0){
            try {
                System.out.println("No tickets available.");
                wait();
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
        totalTickets--;
        System.out.println("Customer purchased a ticket . Remaining tickets: "+totalTickets);
        saveToFile();
        notifyAll();
   }
    // Saves the ticket data (total tickets and max capacity) to a JSON file.
    private void saveToFile(){
        ObjectMapper objectMapper = new ObjectMapper();
        TicketData ticketData = new TicketData(totalTickets,maxTicketCapacity);
        try {
            objectMapper.writeValue(new File("ticketsdetails.json"),ticketData);
            System.out.println("Ticket data saved to ticketsdetails.json");
        }catch (IOException e){
            System.out.println("Failed to save ticket data: "+ e.getMessage());
        }
    }

    //inner class for json structure
    static class TicketData{
       private int totalTickets;
       private int maxTicketCapacity;

        public TicketData(int totalTickets, int maxTicketCapacity) {
            this.maxTicketCapacity = maxTicketCapacity;
            this.totalTickets = totalTickets;
        }

        public int getTotalTickets() {
            return totalTickets;
        }

        public void setTotalTickets(int totalTickets) {
            this.totalTickets = totalTickets;
        }

        public int getMaxTicketCapacity() {
            return maxTicketCapacity;
        }

        public void setMaxTicketCapacity(int maxTicketCapacity) {
            this.maxTicketCapacity = maxTicketCapacity;
        }
    }

}
