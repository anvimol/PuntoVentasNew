package Models.Proveedor;

import java.util.Date;

/**
 *
 * @author anvimol
 */
public class TProveedores {

    private int id;
    private String dni_cif;
    private String proveedor;
    private String email;
    private String direccion;
    private String telefono;
    private Date fecha;
    private byte[] imagen;

    public TProveedores() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDni_cif() {
        return dni_cif;
    }

    public void setDni_cif(String dni_cif) {
        this.dni_cif = dni_cif;
    }


    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

}
