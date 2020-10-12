/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models.Cliente;

/**
 *
 * @author avice
 */
public class TReportes_intereses_clientes {

    private int idReportInteres;
    private double intereses;
    private double pago;
    private double cambio_interes;
    private int cuotas;
    private String interesFecha;
    private String ticketIntereses;
    private int clienteId;

    public TReportes_intereses_clientes() {
    }

    public int getIdReportInteres() {
        return idReportInteres;
    }

    public void setIdReportInteres(int idReportInteres) {
        this.idReportInteres = idReportInteres;
    }

    public double getIntereses() {
        return intereses;
    }

    public void setIntereses(double intereses) {
        this.intereses = intereses;
    }

    public double getPago() {
        return pago;
    }

    public void setPago(double pago) {
        this.pago = pago;
    }

    public double getCambio_interes() {
        return cambio_interes;
    }

    public void setCambio_interes(double cambio_interes) {
        this.cambio_interes = cambio_interes;
    }

    public int getCuotas() {
        return cuotas;
    }

    public void setCuotas(int cuotas) {
        this.cuotas = cuotas;
    }

    public String getInteresFecha() {
        return interesFecha;
    }

    public void setInteresFecha(String interesFecha) {
        this.interesFecha = interesFecha;
    }

    public String getTicketIntereses() {
        return ticketIntereses;
    }

    public void setTicketIntereses(String ticketIntereses) {
        this.ticketIntereses = ticketIntereses;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

}
