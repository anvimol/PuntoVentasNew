/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Libreria;

/**
 *
 * @author avice
 */
public class Codigos {
    private String tickets = "";

    public String codesTickets(String ticket) {
        if (ticket.equals("9999999999")) {
            tickets = "0000000001";
        } else {
            int num = Integer.valueOf(ticket);
            num++;
            tickets = String.format("%010d", num);
        }
        return tickets;
    }

}
