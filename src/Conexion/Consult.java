/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexion;

import Models.Cliente.*;
import Models.Configuracion.TConfiguracion;
import Models.Ordenador.TOrdenadores;
import Models.Proveedor.TPagos_proveedor;
import Models.Proveedor.TProveedores;
import Models.Proveedor.TReportes_proveedor;
import Models.Usuario.TRoles;
import Models.Usuario.TUsuarios;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

/**
 *
 * @author antonio
 */
public class Consult extends Conexion {

    private QueryRunner QR = new QueryRunner();

    public List<TClientes> clientes() {
        List<TClientes> cliente = new ArrayList();
        try {
            cliente = (List<TClientes>) QR.query(getConn(), "SELECT * FROM tclientes",
                    new BeanListHandler(TClientes.class));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex);
        }
        return cliente;
    }

    public List<TReportes_clientes> reportesClientes() {
        String where = "";
        List<TReportes_clientes> reportes = new ArrayList();
        String condicion = " tclientes.id = treportes_clientes.idCliente ";
        String campos = " tclientes.id, tclientes.dni, tclientes.nombre,"
                + "tclientes.telefono,tclientes.email,tclientes.direccion,"
                + "treportes_clientes.idReporte, treportes_clientes.deudaActual,"
                + "treportes_clientes.fechaDeuda, treportes_clientes.ultimoPago,"
                + "treportes_clientes.fechaPago, treportes_clientes.ticket,"
                + "treportes_clientes.deuda, treportes_clientes.mensual, treportes_clientes.cambio,"
                + "treportes_clientes.fechaLimite";
        try {
            reportes = (List<TReportes_clientes>) QR.query(getConn(),
                    "SELECT" + campos + " FROM treportes_clientes INNER JOIN tclientes ON"
                    + condicion + where, new BeanListHandler(TReportes_clientes.class));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
        }
        return reportes;
    }

    public List<TReportes_clientes> reportesClientes(int idCliente) {
        String where = "WHERE tclientes.id =" + idCliente;
        List<TReportes_clientes> reportes = new ArrayList();
        String condicion1 = " tclientes.id = treportes_clientes.idCliente ";
        String condicion2 = " tclientes.id = treportes_intereses_clientes.clienteId ";
        String campos = " tclientes.id, tclientes.dni, tclientes.nombre,"
                + "treportes_clientes.idReporte, treportes_clientes.deudaActual,"
                + "treportes_clientes.fechaDeuda, treportes_clientes.ultimoPago,"
                + "treportes_clientes.fechaPago, treportes_clientes.ticket,"
                + "treportes_clientes.deuda, treportes_clientes.mensual, treportes_clientes.cambio,"
                + "treportes_clientes.fechaLimite, treportes_clientes.idCliente,"
                + "treportes_intereses_clientes.idReportInteres,"
                + "treportes_intereses_clientes.intereses,treportes_intereses_clientes.pago,"
                + "treportes_intereses_clientes.cambio_interes,treportes_intereses_clientes.cuotas,"
                + "treportes_intereses_clientes.interesFecha,treportes_intereses_clientes.ticketIntereses,"
                + "treportes_intereses_clientes.clienteId";
        try {
            reportes = (List<TReportes_clientes>) QR.query(getConn(),
                    "SELECT" + campos + " FROM tclientes INNER JOIN treportes_clientes ON"
                    + condicion1 + "INNER JOIN treportes_intereses_clientes ON"
                    + condicion2 + where, new BeanListHandler(TReportes_clientes.class));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
        }
        return reportes;
    }

    public List<TIntereses_clientes> interesesClientes() {
        List<TIntereses_clientes> intereses = new ArrayList();
        try {
            intereses = (List<TIntereses_clientes>) QR.query(getConn(),
                    "SELECT * FROM tintereses_clientes",
                    new BeanListHandler(TIntereses_clientes.class));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
        }
        return intereses;
    }

    public List<TReportes_clientes> reporteCliente() {
        List<TReportes_clientes> reporte = new ArrayList();
        try {
            reporte = (List<TReportes_clientes>) QR.query(getConn(),
                    "SELECT * FROM treportes_clientes",
                    new BeanListHandler(TReportes_clientes.class));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
        }
        return reporte;
    }

    public List<TReportes_clientes> reportes_Clientes() {
        String where = "";
        List<TReportes_clientes> reportes = new ArrayList();
        String condicion1 = " tclientes.id = treportes_clientes.idCliente ";
        String campos = " tclientes.id, tclientes.dni, tclientes.nombre,"
                + "tclientes.telefono,tclientes.email,tclientes.direccion,"
                + "treportes_clientes.idReporte, treportes_clientes.deudaActual,"
                + "treportes_clientes.fechaDeuda, treportes_clientes.ultimoPago,"
                + "treportes_clientes.fechaPago, treportes_clientes.ticket,"
                + "treportes_clientes.deuda, treportes_clientes.mensual, treportes_clientes.cambio,"
                + "treportes_clientes.fechaLimite";
        try {
            reportes = (List<TReportes_clientes>) QR.query(getConn(),
                    "SELECT" + campos + " FROM tclientes INNER JOIN treportes_clientes ON"
                    + condicion1 + where, new BeanListHandler(TReportes_clientes.class));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
        }
        return reportes;
    }

    public List<TUsuarios> usuarios() {
        List<TUsuarios> usuarios = new ArrayList();
        try {
            usuarios = (List<TUsuarios>) QR.query(getConn(), "SELECT * FROM tusuarios",
                    new BeanListHandler(TUsuarios.class));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
        }
        return usuarios;
    }

    public List<TRoles> roles() {
        List<TRoles> role = new ArrayList();
        try {
            role = (List<TRoles>) QR.query(getConn(), "SELECT * FROM troles",
                    new BeanListHandler(TRoles.class));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex);
        }
        return role;
    }

    public List<TOrdenadores> ordenadores() {
        List<TOrdenadores> ordenador = new ArrayList();
        try {
            ordenador = (List<TOrdenadores>) QR.query(getConn(), "SELECT * FROM tordenadores",
                    new BeanListHandler(TOrdenadores.class));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
        }
        return ordenador;
    }

    public List<TConfiguracion> config() {
        List<TConfiguracion> config = new ArrayList();
        try {
            config = (List<TConfiguracion>) QR.query(getConn(), "SELECT * FROM tconfiguracion",
                    new BeanListHandler(TConfiguracion.class));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
        }
        return config;
    }

    public List<TPagos_clientes> pagosClientes() {
        List<TPagos_clientes> pagos = new ArrayList();
        try {
            pagos = (List<TPagos_clientes>) QR.query(getConn(), "SELECT * FROM "
                    + "tpagos_clientes", new BeanListHandler(TPagos_clientes.class));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex);
        }
        return pagos;
    }

    public List<TPagos_reportes_intereses_clientes> pagosReportesInteresesCliente() {
        List<TPagos_reportes_intereses_clientes> pagos = new ArrayList();
        try {
            pagos = (List<TPagos_reportes_intereses_clientes>) QR.query(getConn(), "SELECT * FROM "
                    + "tpagos_reportes_intereses_clientes", new BeanListHandler(TPagos_reportes_intereses_clientes.class));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex);
        }
        return pagos;
    }

    public List<TProveedores> proveedores() {
        List<TProveedores> proveedor = new ArrayList();
        try {
            proveedor = (List<TProveedores>) QR.query(getConn(), "SELECT * FROM tproveedores",
                    new BeanListHandler(TProveedores.class));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
        }
        return proveedor;
    }
    
    public List<TReportes_proveedor> reportesProveedores(int idProveedor) {
        String where = " WHERE tproveedores.id =" + idProveedor;
        List<TReportes_proveedor> reporte = new ArrayList();
        String condicion1 = " tproveedores.id = treportes_proveedor.proveedorId"; 
        String campos = " tproveedores.id,tproveedores.proveedor,tproveedores.telefono,"
                + "treportes_proveedor.idReporte,treportes_proveedor.deudaActual,"
                + "treportes_proveedor.deuda,treportes_proveedor.mensual,"
                + "treportes_proveedor.cambio,treportes_proveedor.fechaDeuda,"
                + "treportes_proveedor.ultimoPago,treportes_proveedor.fechaPago,"
                + "treportes_proveedor.ticket";
        try {
            reporte = (List<TReportes_proveedor>) QR.query(getConn(),
                    "SELECT" + campos + " FROM tproveedores INNER JOIN treportes_proveedor ON"
                    + condicion1 + where, new BeanListHandler(TReportes_proveedor.class));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
        }
        return reporte;
    }
    
    public List<TReportes_proveedor> reporteProveedor() {
        List<TReportes_proveedor> reporte = new ArrayList();
        try {
            reporte = (List<TReportes_proveedor>) QR.query(getConn(), "SELECT * FROM treportes_proveedor",
                    new BeanListHandler(TReportes_proveedor.class));
        } catch (Exception e) {
        }
        return reporte;
    }
    
    public List<TPagos_proveedor> pagosProveedores() {
        List<TPagos_proveedor> pagos = new ArrayList();
        try {
            pagos = (List<TPagos_proveedor>) QR.query(getConn(), "SELECT * FROM "
                    + "tpagos_proveedor", new BeanListHandler(TPagos_proveedor.class));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex);
        }
        return pagos;
    }
}
