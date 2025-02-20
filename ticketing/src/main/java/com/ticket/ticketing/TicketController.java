package com.ticket.ticketing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin // Allows cross-origin requests to this controller (useful for frontend-backend communication).
@RequestMapping("/api/tickets") // Base URL for all endpoints related to tickets.
public class TicketController {
    @Autowired
    private TicketRepository ticketRepository;

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    // Endpoint to create a new ticket.
    @PostMapping
    public Ticket createTicket(@RequestBody Ticket ticket){
        ticketRepository.save(ticket);
        return ticket;


    }
    // Endpoint to retrieve all tickets from the database.
    @GetMapping
    public List<Ticket> getAllTickets(){
        return ticketRepository.findAll();
    }
    // Endpoint to update a specific ticket based on its ID.
    @PutMapping("/{id}")
    public Ticket updateTicket(@PathVariable Long id, @RequestBody Ticket ticket){
        ticket.setId(id);
        return ticketRepository.save(ticket);

    }
    // Endpoint to delete a ticket based on its ID.
    @DeleteMapping("/{id}")
    public void deleteTicket(@PathVariable Long id){
        ticketRepository.deleteById(id);
    }

    // Autowire the TicketingCommandLineRunner, which controls the ticketing system logic.
    @Autowired
    private TicketingCommandLineRunner commandLineRunner;

    // Endpoint to start the ticketing system with specific parameters: ticket release rate, customer retrieval rate, and max ticket capacity.
    @PostMapping("/start")
    public String startTicketingSystem(@RequestParam int ticketReleaseRate,@RequestParam int customerRetrievalRate, @RequestParam int maxTicketCapacity){
        try{
            commandLineRunner.startTicketingSystem(ticketReleaseRate,customerRetrievalRate,maxTicketCapacity);
            return "Ticketing system started with release rate: "+ ticketReleaseRate +" and retrival rate: "+ customerRetrievalRate + ", and max capacity: "+ maxTicketCapacity;
        }catch (InterruptedException e){
            return "Error: "+ e.getMessage();
        }
    }

    // Endpoint to stop the ticketing system.
    @PostMapping("/stop")
    public String stopTicketingSystem(){
        try {
            commandLineRunner.stopTicketingSystem();
            return "Ticketing system stoped.";
        }catch (InterruptedException e){
            return "Error: "+e.getMessage();
        }
    }

}
