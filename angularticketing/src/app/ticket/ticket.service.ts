import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Ticket } from './ticket.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TicketService {
  private apiUrl ="http://localhost:8080/api/tickets";

  constructor(private httpClient:HttpClient) { }

  createTicket(newTicket:Ticket):Observable<Ticket>{
    return this.httpClient.post<Ticket>(this.apiUrl,newTicket)
  }
  getAllTickets():Observable<Ticket[]>{
    return this.httpClient.get<Ticket[]>(this.apiUrl);
  }

  updateTicket(ticketId:number, updatedTicket:Ticket):Observable<Ticket>{
    return this.httpClient.put<Ticket>(this.apiUrl+'/'+ticketId,updatedTicket)
  }

  deleteTicket(ticketId:number){
    return this.httpClient.delete(this.apiUrl+'/'+ticketId);
  }
  startTicketingSystem(ticketReleaseRate: number, customerRetrievalRate: number, maxTicketCapacity: number): Observable<string> {
    return this.httpClient.post<string>(`${this.apiUrl}/start?ticketReleaseRate=${ticketReleaseRate}&customerRetrievalRate=${customerRetrievalRate}&maxTicketCapacity=${maxTicketCapacity}`, {});
  }

  stopTicketingSystem(): Observable<string> {
    return this.httpClient.post<string>(`${this.apiUrl}/stop`, {});
  }


}
