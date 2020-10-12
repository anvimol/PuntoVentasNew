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
public class TIntereses_clientes {
    private int idInteresesCliente;
    private String fechaInicial;
    private double deuda;
    private double mensual;
    private double intereses;
    private String fecha;
    private boolean cancelado;
    private int idCliente;

    public TIntereses_clientes() {
    }

    public int getIdInteresesCliente() {
        return idInteresesCliente;
    }

    public void setIdInteresesCliente(int idInteresesCliente) {
        this.idInteresesCliente = idInteresesCliente;
    }

    public String getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(String fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public double getDeuda() {
        return deuda;
    }

    public void setDeuda(double deuda) {
        this.deuda = deuda;
    }

    public double getMensual() {
        return mensual;
    }

    public void setMensual(double mensual) {
        this.mensual = mensual;
    }

    public double getIntereses() {
        return intereses;
    }

    public void setIntereses(double intereses) {
        this.intereses = intereses;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public boolean isCancelado() {
        return cancelado;
    }

    public void setCancelado(boolean cancelado) {
        this.cancelado = cancelado;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
    
    
}
