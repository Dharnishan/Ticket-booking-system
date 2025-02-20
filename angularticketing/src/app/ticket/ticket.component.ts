import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { TicketService } from './ticket.service';
import { Ticket } from './ticket.model';
import {FormsModule} from '@angular/forms';



@Component({
  selector: 'app-ticket',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './ticket.component.html',
  styleUrls: ['./ticket.component.css']
})
export class TicketComponent implements OnInit {
  constructor(private ticketService:TicketService ){}

  newTicket:Ticket = {totalTickets:0,ticketReleaseRate:0,customerRetrievalRate:0,maxTicketCapacity:0,description:""}
  tickets:Ticket[] = []
  editingTicket:Ticket|null =null;
  updatedTicket: Ticket ={totalTickets:0,ticketReleaseRate:0,customerRetrievalRate:0,maxTicketCapacity:0,description:""}

  ticketReleaseRate: number = 5;
  customerRetrievalRate: number = 3;
  maxTicketCapacity: number = 20;

  
  
  ngOnInit(): void {

    this.getAllTickets();
  }

 
  


 

  createTicket():void{
    this.ticketService.createTicket(this.newTicket).subscribe((createdTicket)=>{
      this.newTicket={totalTickets:0,ticketReleaseRate:0,customerRetrievalRate:0,maxTicketCapacity:0,description:""}
      this.tickets.push(createdTicket);
    })
  }

  getAllTickets(){
    this.ticketService.getAllTickets().subscribe((tickets)=>{
      this.tickets = tickets;
      this.tickets.forEach(ticket => ticket.buyQuantity = 1);
      
    });
  }

  editTicket(ticket:Ticket){
    this.editingTicket = ticket;
    this.updatedTicket = {...ticket};//create a copy for editing
  }

  updateTicket():void{
    if(this.editingTicket){
      this.ticketService.updateTicket(this.editingTicket.id!,this.updatedTicket)
      .subscribe((result)=>{
        const index = this.tickets.findIndex((ticket)=>ticket.id == this.editingTicket!.id)
        if(index!==-1){
          this.tickets[index] = result;
          this.cancelEdit()
        }
      })
    }
  }

  cancelEdit(){
    this.editingTicket = null;
    this.updatedTicket = {totalTickets:0,ticketReleaseRate:0,customerRetrievalRate:0,maxTicketCapacity:0,description:""};
  }

  deleteTicket(ticketId:any){
    if(confirm('Are You sure want to Delete ? :('))
    this.ticketService.deleteTicket(ticketId)
    .subscribe(()=>{
      this.tickets=this.tickets.filter((ticket)=>ticket.id!==ticketId)
      if(this.editingTicket && this.editingTicket.id == ticketId){
        this.cancelEdit();
      }
    })
  }
  

  startTicketingSystem(): void{
    if(this.ticketReleaseRate > 0 && this.customerRetrievalRate > 0 && this.maxTicketCapacity > 0 ){
      this.ticketService.startTicketingSystem(this.ticketReleaseRate,this.customerRetrievalRate,this.maxTicketCapacity).subscribe(response =>{
        alert(response);
      },error => {
        alert("Failed to start the ticketing system.");
      });
    }else{
      alert("Please enter valid release and retrieval rates.");
    }
  }

  stopTicketingSystem(): void{
    this.ticketService.stopTicketingSystem().subscribe(response=>{
      alert(response);
    },error =>{
      alert("Failed to stop the ticketing system")
    })
  }
  startTicketingSystemForTicket(ticket: Ticket): void {
    if (ticket.ticketReleaseRate > 0 && ticket.customerRetrievalRate > 0 && ticket.maxTicketCapacity > 0) {
      this.ticketService
        .startTicketingSystem(ticket.ticketReleaseRate, ticket.customerRetrievalRate, ticket.maxTicketCapacity)
        .subscribe(
          (response) => {
            alert(`Started ticketing system for: ${ticket.description}`);
          },
          (error) => {
            alert('Error starting the ticketing system.');
          }
        );
    } else {
      alert('Please enter valid rates for the ticket.');
    }
  }

  // Stop the ticketing system for a specific ticket
  stopTicketingSystemForTicket(ticket: Ticket): void {
    this.ticketService.stopTicketingSystem().subscribe(
      (response) => {
        alert(`Stopped ticketing system for: ${ticket.description}`);
      },
      (error) => {
        alert('Error stopping the ticketing system.');
      }
    );
  }


  
  



}
