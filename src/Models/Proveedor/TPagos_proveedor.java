
package Models.Proveedor;

import java.util.Date;

/**
 *
 * @author anvimol
 */
public class TPagos_proveedor {
    private int idPagosProve;
    private int idProveedor;
    private Double deuda;
    private Double saldo;
    private Double pago;
    private Double cambio;
    private Date fecha;
    private String ticket;
    private int idUsuario;
    private String usuario;
    private Date fechaDeuda;
    private Double mensual;

    public TPagos_proveedor() {
    }

    public int getIdPagosProve() {
        return idPagosProve;
    }

    public void setIdPagosProve(int idPagosProve) {
        this.idPagosProve = idPagosProve;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public Double getDeuda() {
        return deuda;
    }

    public void setDeuda(Double deuda) {
        this.deuda = deuda;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
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

    public Date getFechaDeuda() {
        return fechaDeuda;
    }

    public void setFechaDeuda(Date fechaDeuda) {
        this.fechaDeuda = fechaDeuda;
    }

    public Double getMensual() {
        return mensual;
    }

    public void setMensual(Double mensual) {
        this.mensual = mensual;
    }
    
    
}
