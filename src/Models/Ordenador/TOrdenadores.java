
package Models.Ordenador;

import java.util.Date;

/**
 *
 * @author avice
 */
public class TOrdenadores {
    private int idOrdenador;
    private String ordenador;
    private boolean isActive;
    private String usuario;
    private Date inFecha;
    private Date outFecha;
    private int usuario_id;

    public TOrdenadores() {
    }

    public int getIdOrdenador() {
        return idOrdenador;
    }

    public void setIdOrdenador(int idOrdenador) {
        this.idOrdenador = idOrdenador;
    }

    public String getOrdenador() {
        return ordenador;
    }

    public void setOrdenador(String ordenador) {
        this.ordenador = ordenador;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Date getInFecha() {
        return inFecha;
    }

    public void setInFecha(Date inFecha) {
        this.inFecha = inFecha;
    }

    public Date getOutFecha() {
        return outFecha;
    }

    public void setOutFecha(Date outFecha) {
        this.outFecha = outFecha;
    }

    public int getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(int usuario_id) {
        this.usuario_id = usuario_id;
    }
    
    
}
