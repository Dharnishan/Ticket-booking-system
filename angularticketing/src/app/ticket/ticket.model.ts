export interface Ticket {
    id?:number;
    totalTickets:number;
    ticketReleaseRate:number;
    customerRetrievalRate:number;
    maxTicketCapacity:number;
    description:string;
    buyQuantity?:number;
   
}
