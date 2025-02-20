package com.ticket.ticketing;
// The Customer class implements the Runnable interface, representing a customer that purchases tickets.
public class Customer implements Runnable{
    private final TicketService ticketService;
    private final int customerRetrievalRate;

    public Customer(TicketService ticketService, int customerRetrievalRate){
        this.ticketService = ticketService;
        this.customerRetrievalRate = customerRetrievalRate;
    }
    // The run() method contains the code that will be executed when this customer thread starts.
    @Override
    public void run(){
        while (!Thread.currentThread().isInterrupted()){ // Loop continues until the thread is interrupted.
            for (int i = 0; i < customerRetrievalRate; i++) {
                ticketService.purchaseTicket(); // Calls the ticket service to purchase a ticket.
            }
            try {
                Thread.sleep(1000);  // Simulate customer purchasing every second
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }
}
