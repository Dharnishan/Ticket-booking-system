package com.ticket.ticketing;

public class Vendor implements Runnable {
    private final TicketService ticketService;
    private final int ticketReleaseRate;

    public Vendor(TicketService ticketService, int ticketReleaseRate){
        this.ticketService = ticketService;
        this.ticketReleaseRate = ticketReleaseRate;
    }
    @Override
    public void run(){
        while (!Thread.currentThread().isInterrupted()){
            ticketService.addTickets(ticketReleaseRate);
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }


    }
}
