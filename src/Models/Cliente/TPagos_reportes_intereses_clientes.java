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
public class TPagos_reportes_intereses_clientes {

    private int idPagosReporInteClien;
    private Double intereses;
    private Double pago;
    private Double cambio;
    private int cuotas;
    private String fecha;
    private String ticket;
    private int idUsuario;
    private String usuario;
    private int idCliente;

    public TPagos_reportes_intereses_clientes() {
    }

    public int getIdPagosReporInteClien() {
        return idPagosReporInteClien;
    }

    public void setIdPagosReporInteClien(int idPagosReporInteClien) {
        this.idPagosReporInteClien = idPagosReporInteClien;
    }

    public Double getIntereses() {
        return intereses;
    }

    public void setIntereses(Double intereses) {
        this.intereses = intereses;
    }

    public Double getPago() {
        return pago;
    }

    public void setPago(Double pago) {
        this.pago = pago;
    }

    public Double getCambio() {
        return cambio;
    }

    public void setCambio(Double cambio) {
        this.cambio = cambio;
    }

    public int getCuotas() {
        return cuotas;
    }

    public void setCuotas(int cuotas) {
        this.cuotas = cuotas;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

}
