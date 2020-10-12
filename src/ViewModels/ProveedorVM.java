package ViewModels;

import Conexion.Consult;
import Libreria.Calendario;
import Libreria.Codigos;
import Libreria.FormatDecimal;
import Libreria.Objetos;
import Libreria.Paginador;
import Libreria.Render_CheckBox;
import Libreria.Ticket;
import Models.Proveedor.TPagos_proveedor;
import Models.Proveedor.TProveedores;
import Models.Proveedor.TReportes_proveedor;
import Models.Usuario.TUsuarios;
import datechooser.beans.DateChooserCombo;
import java.awt.Color;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

/**
 *
 * @author anvimol
 */
public class ProveedorVM extends Consult {

    private String _accion = "insert", _mony;
    private ArrayList<JLabel> _label;
    private ArrayList<JTextField> _textfield;
    private JTable _tableProveedor, _tableReporte, _tablePagoCuotas;
    private JSpinner _spinnerPaginas;
    private DefaultTableModel modelo1, modelo2, modelo3;
    public int seccion, _reg_por_pagina = 10, _num_pagina = 1;
    private Paginador<TProveedores> _paginadorProveedor, _paginadorReporProve;
    private Paginador<TPagos_proveedor> _paginadorPagosProve;
    private FormatDecimal _format;
    private Codigos _codigos;
    private static TUsuarios _dataUsuario;
    private SimpleDateFormat formateador;
    private DateChooserCombo _dateChooser1, _dateChooser2;
    private List<TProveedores> proveedorFilter;
    private List<TPagos_proveedor> listPagos;
    private Ticket ticket1 = new Ticket();
    private int _idProveedor = 0, _idHistorial;

    public ProveedorVM(TUsuarios dataUsuario) {
        _dataUsuario = dataUsuario;
        formateador = new SimpleDateFormat("yyyy/MM/dd");
    }

    public ProveedorVM(Object[] objects, ArrayList<JLabel> label,
            ArrayList<JTextField> textfield) {
        _textfield = textfield;
        _label = label;
        _tableProveedor = (JTable) objects[0];
        _spinnerPaginas = (JSpinner) objects[1];
        _tableReporte = (JTable) objects[2];
        _dateChooser1 = (DateChooserCombo) objects[3];
        _dateChooser2 = (DateChooserCombo) objects[4];
        _tablePagoCuotas = (JTable) objects[5];
        _mony = ConfiguracionVM.Mony;
        _format = new FormatDecimal();
        _codigos = new Codigos();
        formateador = new SimpleDateFormat("dd/MM/yyyy");
        restablecer();
        restablecerReport();
    }

    // <editor-fold defaultstate="collapsed" desc="CÓDIGO REGISTRO PROVEEDOR">
    public void registrarProveedor() {
        if (validarDatos()) {
            int count;
            List<TProveedores> listEmail = proveedores().stream()
                    .filter(u -> u.getEmail().equals(_textfield.get(2).getText()))
                    .collect(Collectors.toList());
            count = listEmail.size();
            List<TProveedores> listDni = proveedores().stream()
                    .filter(u -> u.getDni_cif().equals(_textfield.get(0).getText()))
                    .collect(Collectors.toList());
            count += listDni.size();
            try {
                switch (_accion) {
                    case "insert":
                        if (count == 0) {
                            saveData();
                        } else {
                            if (!listEmail.isEmpty()) { // 0 < listEmail.size()
                                _label.get(2).setText("El email ya esta registrado");
                                _label.get(2).setForeground(Color.RED);
                                _textfield.get(2).requestFocus();
                            }
                            if (!listDni.isEmpty()) {
                                _label.get(0).setText("El DNI ya esta registrado");
                                _label.get(0).setForeground(Color.RED);
                                _textfield.get(0).requestFocus();
                            }
                        }
                        break;
                    case "update":
                        if (count == 2) {
                            if (listEmail.get(0).getId() == _idProveedor
                                    && listDni.get(0).getId() == _idProveedor) {
                                saveData();
                            } else {
                                if (listDni.get(0).getId() != _idProveedor) {
                                    _label.get(0).setText("El DNI ya esta registrado");
                                    _label.get(0).setForeground(Color.RED);
                                    _textfield.get(0).requestFocus();
                                }
                                if (listEmail.get(0).getId() != _idProveedor) {
                                    _label.get(2).setText("El email ya esta registrado");
                                    _label.get(2).setForeground(Color.RED);
                                    _textfield.get(2).requestFocus();
                                }
                            }
                        } else {
                            if (count == 0) {
                                saveData();
                            } else {
                                if (!listDni.isEmpty()) {
                                    if (listDni.get(0).getId() == _idProveedor) {
                                        saveData();
                                    } else {
                                        _label.get(0).setText("El DNI ya esta registrado");
                                        _label.get(0).setForeground(Color.RED);
                                        _textfield.get(0).requestFocus();
                                    }
                                }
                                if (!listEmail.isEmpty()) {
                                    if (listEmail.get(0).getId() == _idProveedor) {
                                        saveData();
                                    } else {
                                        _label.get(2).setText("El email ya esta registrado");
                                        _label.get(2).setForeground(Color.RED);
                                        _textfield.get(2).requestFocus();
                                    }
                                }
                            }
                        }
                        break;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    private void saveData() throws SQLException {
        try {
            final QueryRunner qr = new QueryRunner(true);
            getConn().setAutoCommit(false);
            byte[] image = Objetos.uploadImage.getImageByte();
            if (image == null) {
                image = Objetos.uploadImage.getTransFoto(_label.get(5));
            }
            switch (_accion) {
                case "insert":
                    String sqlProveedor = "INSERT INTO tproveedores(dni_cif,proveedor,"
                            + "email,direccion,telefono,fecha,imagen) "
                            + "VALUES(?,?,?,?,?,?,?)";
                    Object[] dataProveedor = {
                        _textfield.get(0).getText(),
                        _textfield.get(1).getText(),
                        _textfield.get(2).getText(),
                        _textfield.get(3).getText(),
                        _textfield.get(4).getText(),
                        new Date(),
                        image
                    };

                    qr.insert(getConn(), sqlProveedor, new ColumnListHandler(), dataProveedor);

                    String sqlReport = "INSERT INTO treportes_proveedor(deudaActual,"
                            + "deuda,mensual,cambio,fechaDeuda,ultimoPago,"
                            + "fechaPago,ticket,proveedorId) "
                            + "VALUES (?,?,?,?,?,?,?,?,?)";

                    List<TProveedores> proveedor = proveedores();

                    Object[] dataReport = {
                        0,
                        0,
                        0,
                        0,
                        null,
                        0,
                        null,
                        "00000000000",
                        proveedor.get(proveedor.size() - 1).getId() // Ultimo cliente registrado
                    };
                    qr.insert(getConn(), sqlReport, new ColumnListHandler(), dataReport);

                    break;
                case "update":
                    Object[] dataProveedor2 = {
                        _textfield.get(0).getText(),
                        _textfield.get(1).getText(),
                        _textfield.get(2).getText(),
                        _textfield.get(3).getText(),
                        _textfield.get(4).getText(),
                        new Date(),
                        image
                    };
                    String sqlProveedor2 = "UPDATE tproveedores SET dni_cif=?, proveedor=?,"
                            + "email=?, direccion=?, telefono=?, fecha=?, imagen=? "
                            + "WHERE id = " + _idProveedor;
                    qr.update(getConn(), sqlProveedor2, dataProveedor2);
                    break;
            }

            getConn().commit();
            restablecer();
        } catch (Exception e) {
            getConn().rollback();
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void searchProveedores(String campo) {
        String[] titulos = {"Id", "DNI/CIF", "Proveedor", "Email", "Dirección",
            "Teléfono", "Imagen"};
        modelo1 = new DefaultTableModel(null, titulos);

        int inicio = (_num_pagina - 1) * _reg_por_pagina;

        if (campo.isEmpty()) {
//            proveedorFilter = proveedores().stream()
//                    .skip(inicio).limit(_reg_por_pagina)
//                    .collect(Collectors.toList());
            proveedorFilter = proveedores();
        } else {
            proveedorFilter = proveedores().stream()
                    .filter(C -> C.getDni_cif().startsWith(campo) || C.getEmail().startsWith(campo)
                    || C.getProveedor().startsWith(campo))
                    .collect(Collectors.toList());
        }
        var data = proveedorFilter;
        var list = data.stream()
                .skip(inicio).limit(_reg_por_pagina)
                .collect(Collectors.toList());
        if (!list.isEmpty()) {
            list.forEach(item -> {
                Object[] registros = {
                    item.getId(),
                    item.getDni_cif(),
                    item.getProveedor(),
                    item.getEmail(),
                    item.getDireccion(),
                    item.getTelefono(),
                    item.getImagen()
                };
                modelo1.addRow(registros);
            });
        }
        _tableProveedor.setModel(modelo1);
        _tableProveedor.setRowHeight(30);
        _tableProveedor.getColumnModel().getColumn(0).setMaxWidth(0);
        _tableProveedor.getColumnModel().getColumn(0).setMinWidth(0);
        _tableProveedor.getColumnModel().getColumn(0).setPreferredWidth(0);
        _tableProveedor.getColumnModel().getColumn(6).setMaxWidth(0);
        _tableProveedor.getColumnModel().getColumn(6).setMinWidth(0);
        _tableProveedor.getColumnModel().getColumn(6).setPreferredWidth(0);
    }

    public void getProveedor() {
        _accion = "update";
        int fila = _tableProveedor.getSelectedRow();
        _idProveedor = (Integer) modelo1.getValueAt(fila, 0);
        _textfield.get(0).setText((String) modelo1.getValueAt(fila, 1));
        _textfield.get(1).setText((String) modelo1.getValueAt(fila, 2));
        _textfield.get(2).setText((String) modelo1.getValueAt(fila, 3));
        _textfield.get(3).setText((String) modelo1.getValueAt(fila, 4));
        _textfield.get(4).setText((String) modelo1.getValueAt(fila, 5));
        Objetos.uploadImage.byteImage(_label.get(5), (byte[]) modelo1.getValueAt(fila, 6), 179, 209);

        _label.get(0).setText("DNI o CIF");
        _label.get(0).setForeground(new Color(0, 153, 51));
        _label.get(1).setText("Proveedor:");
        _label.get(1).setForeground(new Color(0, 153, 51));
        _label.get(2).setText("Email:");
        _label.get(2).setForeground(new Color(0, 153, 51));
        _label.get(3).setText("Dirección:");
        _label.get(3).setForeground(new Color(0, 153, 51));
        _label.get(4).setText("Teléfono:");
        _label.get(4).setForeground(new Color(0, 153, 51));
    }

    public void restablecer() {
        seccion = 0;
        _accion = "insert";
        _textfield.get(0).setText("");
        _textfield.get(1).setText("");
        _textfield.get(2).setText("");
        _textfield.get(3).setText("");
        _textfield.get(4).setText("");
        _label.get(0).setText("DNI / CIF:");
        _label.get(0).setForeground(new Color(70, 106, 124));
        _label.get(1).setText("Proveedor:");
        _label.get(1).setForeground(new Color(70, 106, 124));
        _label.get(2).setText("Email:");
        _label.get(2).setForeground(new Color(70, 106, 124));
        _label.get(3).setText("Dirección:");
        _label.get(3).setForeground(new Color(70, 106, 124));
        _label.get(4).setText("Teléfono:");
        _label.get(4).setForeground(new Color(70, 106, 124));
        _label.get(5).setIcon(new ImageIcon(getClass().getClassLoader()
                .getResource("Resources/iconos-clientes-png.png")));
        SpinnerNumberModel model = new SpinnerNumberModel(
                10.0, // Dato visualizado al inicio del spinner
                1.0, //Limite inferior
                100.0, // Limite Superior
                1.0 // Incremento - decremento
        );
        _spinnerPaginas.setModel(model);

        proveedorFilter = proveedores();
        if (!proveedorFilter.isEmpty()) {
            _paginadorProveedor = new Paginador<>(proveedorFilter,
                    _label.get(6), _reg_por_pagina);
        }
        searchProveedores("");
    }

    private boolean validarDatos() {
        if (_textfield.get(0).getText().isEmpty()) {
            _label.get(0).setText("Ingrese el DNI o CIF");
            _label.get(0).setForeground(Color.RED);
            _textfield.get(0).requestFocus();
            return false;
        } else if (_textfield.get(1).getText().isEmpty()) {
            _label.get(1).setText("Ingrese el Nombre del Proveedor");
            _label.get(1).setForeground(Color.RED);
            _textfield.get(1).requestFocus();
            return false;
        } else if (_textfield.get(2).getText().isEmpty()) {
            _label.get(2).setText("Ingrese el Email");
            _label.get(2).setForeground(Color.RED);
            _textfield.get(2).requestFocus();
            return false;
        } else if (!Objetos.eventos.isEmail(_textfield.get(2).getText())) {
            _label.get(2).setText("Ingrese un Email valido");
            _label.get(2).setForeground(Color.RED);
            _textfield.get(2).requestFocus();
            return false;
        } else if (_textfield.get(3).getText().isEmpty()) {
            _label.get(3).setText("Ingrese el Teléfono");
            _label.get(3).setForeground(Color.RED);
            _textfield.get(3).requestFocus();
            return false;
        } else if (_textfield.get(4).getText().isEmpty()) {
            _label.get(4).setText("Ingrese la Dirección");
            _label.get(4).setForeground(Color.RED);
            _textfield.get(4).requestFocus();
            return false;
        }
        return true;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="CÓDIGO PAGOS Y REPORTES">
    private int _idReport, _idProveedorReport;
    public int _seccion1 = 0;
    private Double _deudaActual = 0.0, _cambio = 0.0;
    private Double _pago = 0.0, _mensual = 0.0, _deudaActualProvee = 0.0, _deuda;
    private String _ticketCuota, nameProveedor;
    private List<TProveedores> reporteFilter;

    public void searchReportes(String valor) {
        String[] titulos = {"Id", "Proveedor", "Email", "Dirección", "Teléfono"};
        modelo2 = new DefaultTableModel(null, titulos);
        int inicio = (_num_pagina - 1) * _reg_por_pagina;
        if (valor.isEmpty()) {
            reporteFilter = proveedores();
        } else {
            reporteFilter = proveedores().stream()
                    .filter(P -> P.getProveedor().startsWith(valor)
                    || P.getEmail().startsWith(valor))
                    .collect(Collectors.toList());
        }
        var data = reporteFilter;
        var list = data.stream()
                .skip(inicio).limit(_reg_por_pagina)
                .collect(Collectors.toList());
        if (!list.isEmpty()) {
            list.forEach(item -> {
                Object[] registros = {
                    item.getId(),
                    item.getProveedor(),
                    item.getEmail(),
                    item.getDireccion(),
                    item.getTelefono()
                };
                modelo2.addRow(registros);
            });
        }
        _tableReporte.setModel(modelo2);
        _tableReporte.setRowHeight(30);
        _tableReporte.getColumnModel().getColumn(0).setMaxWidth(0);
        _tableReporte.getColumnModel().getColumn(0).setMinWidth(0);
        _tableReporte.getColumnModel().getColumn(0).setPreferredWidth(0);
    }

    public void getReportProveedor() {
        int fila = _tableReporte.getSelectedRow();
        _idProveedorReport = (Integer) modelo2.getValueAt(fila, 0);
        var proveedorFilter = reportesProveedores(_idProveedorReport);
        if (!proveedorFilter.isEmpty()) {
            var proveedor = proveedorFilter.get(0);
            _idReport = proveedor.getIdReporte();
            nameProveedor = proveedor.getProveedor();
            _label.get(7).setText(nameProveedor);
            _deudaActual = (Double) proveedor.getDeudaActual();
            _deuda = (Double) proveedor.getDeuda();
            _label.get(8).setText(_format.decimal(_deudaActual) + _mony);
            _label.get(9).setText(_format.decimal((Double) proveedor.getUltimoPago()) + _mony);
            _ticketCuota = proveedor.getTicket();
            _label.get(10).setText(_ticketCuota);
            if (null != proveedor.getFechaPago()) {
                _label.get(11).setText(proveedor.getFechaPago().toString());
            }

            _label.get(12).setText(_format.decimal((Double) proveedor.getMensual()) + _mony);
            pagos();
            historialPagos(false);
        }
    }

    public void pagos() {
        if (!_textfield.get(5).getText().isEmpty()) {
            _label.get(13).setText("Ingrese el pago");
            _label.get(13).setForeground(Color.RED);
            if (_idReport == 0) {
                _label.get(13).setText("Seleccione un proveedor");
                _label.get(13).setForeground(Color.RED);
            } else {
                if (!_textfield.get(5).getText().isEmpty()) {
                    _pago = _format.reconstruir(_textfield.get(5).getText());
                    var dataReport = reporteProveedor().stream()
                            .filter(u -> u.getIdReporte() == _idReport)
                            .collect(Collectors.toList()).get(0);
                    _mensual = dataReport.getMensual();
                    if (_pago > _mensual) {
                        if (Objects.equals(_pago, _deudaActual) || _pago > _deudaActual) {
                            _cambio = _pago - _deudaActual;
                            _label.get(13).setText("Cambio para el sistema "
                                    + _format.decimal(_cambio) + _mony);
                            _label.get(13).setForeground(Color.RED);
                            _label.get(8).setText("0.00" + _mony);
                            _deudaActual = 0.0;
                            _deudaActualProvee = 0.0;
                        } else {
                            _cambio = _pago - _mensual;
                            _label.get(13).setText("Cambio para el sistema "
                                    + _format.decimal(_cambio) + _mony);
                            _label.get(13).setForeground(Color.RED);
                            _deudaActualProvee = _deudaActual - _mensual;
                            _label.get(8).setText(_format.decimal(_deudaActualProvee) + _mony);
                        }
                    } else if (Objects.equals(_pago, _mensual)) {
                        _deudaActualProvee = _deudaActual - _mensual;
                        _label.get(8).setText(_format.decimal(_deudaActualProvee) + _mony);
                    } else {
                        _cambio = 0.0;
                        var deuda = _mensual - _pago;
                        _label.get(13).setText("Importe faltante " + _format.decimal(deuda) + _mony);
                        _label.get(13).setForeground(Color.RED);
                    }
                }
            }
        } else {
            _label.get(13).setText("Ingresar el pago");
            _label.get(8).setText(_format.decimal(_deudaActual) + _mony);
        }
    }

    public void ejecutarPago() throws SQLException {
        final QueryRunner qr = new QueryRunner(true);
        if (Objects.equals(_idReport, 0)) {
            _label.get(13).setText("Seleccione un proveedor");
        } else {
            if (_textfield.get(5).getText().isEmpty()) {
                _label.get(13).setText("Ingrese el pago");
                _textfield.get(5).requestFocus();
            } else {
                String fecha = new Calendario().getFecha();
                // Realizar consulta del usuario que inicia sesión
                var usuario = _dataUsuario.getNombre();
                if (!_deuda.equals(0) || !_deuda.equals(0.0) || !_deuda.equals(0.00)) {
                    if (_pago >= _mensual) {
                        try {
                            getConn().setAutoCommit(false);
                            String ticket = _codigos.codesTickets(_ticketCuota);
                            String query1 = "INSERT INTO tpagos_proveedor(deuda,saldo,pago,cambio,"
                                    + "fecha,ticket,idUsuario,usuario,fechaDeuda,mensual,idProveedor) "
                                    + "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
                            var dataReport = reporteProveedor().stream()
                                    .filter(u -> u.getIdReporte() == _idProveedorReport)
                                    .collect(Collectors.toList()).get(0);
                            Object[] data1 = {
                                _deuda,
                                _deudaActualProvee,
                                _pago,
                                _cambio,
                                fecha,
                                ticket,
                                _dataUsuario.getIdUsuario(),
                                usuario,
                                dataReport.getFechaDeuda(),
                                dataReport.getMensual(),
                                _idProveedorReport
                            };
                            qr.insert(getConn(), query1, new ColumnListHandler(), data1);
                            if (_deudaActualProvee.equals(0.0)) {
                                String query2 = "UPDATE treportes_proveedor SET deudaActual=?, "
                                        + "deuda=?, mensual=?, cambio=?, fechaDeuda=?, "
                                        + "ultimoPago=?, fechaPago=?, ticket=?,"
                                        + "fechaLimite=? WHERE idReporte =" + _idReport;
                                Object[] data2 = {
                                    0.00,
                                    0.00,
                                    0.00,
                                    0.00,
                                    null,
                                    0.00,
                                    null,
                                    "0000000000"
                                };
                                qr.update(getConn(), query2, data2);
                            } else {
                                String query2 = "UPDATE treportes_proveedor SET deudaActual=?, "
                                        + "cambio=?, ultimoPago=?, fechaPago=?, ticket=?"
                                        + " WHERE idReporte =" + _idReport;
                                Object[] data2 = {
                                    _deudaActualProvee,
                                    _cambio,
                                    _pago,
                                    new Date(),
                                    ticket
                                };
                                qr.update(getConn(), query2, data2);
                            }
                            ticket1.textoCentro("Sistema de ventas Anvimol"); // imprime en el centro
                            ticket1.lineasVacia();
                            ticket1.textoIzquierda("Direccion");
                            ticket1.textoIzquierda("Avda La Paz 3, Onil");
                            ticket1.textoIzquierda("Tel 665151716");
                            ticket1.lineasGuion();
                            ticket1.textoCentro("FACTURA"); // imprime en el centro 
                            ticket1.lineasGuion();
                            ticket1.textoIzquierda("Factura: " + ticket);
                            ticket1.textoIzquierda("Proveedor: " + nameProveedor);
                            ticket1.textoIzquierda("Fecha:   " + fecha);
                            ticket1.textoIzquierda("Usuario: " + usuario);
                            ticket1.lineasGuion();
                            ticket1.textoCentro("Su credito: " + _format.decimal(_deuda) + _mony);
                            ticket1.textoExtremo("Cuotas por mes:", _format.decimal(_mensual) + _mony);
                            ticket1.textoExtremo("Deuda anterior:", _format.decimal(_deudaActual) + _mony);
                            ticket1.textoExtremo("Pago:", _format.decimal(_pago) + _mony);
                            ticket1.textoExtremo("Cambio:", _format.decimal(_cambio) + _mony);
                            ticket1.textoExtremo("Deuda actual:", _format.decimal(_deudaActualProvee) + _mony);
                            ticket1.lineasVacia();
                            ticket1.textoCentro("@anvimol");
                            ticket1.print();
                            getConn().commit();
                            restablecerReport();
                        } catch (Exception e) {
                            getConn().rollback();
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(null, e);
                        }
                    }
                } else {
                    _label.get(13).setText("El Sistema no tiene deuda");
                }
            }
        }
    }

    public void historialPagos(boolean filtrar) {
        formateador = new SimpleDateFormat("dd/MM/yyyy");
        listPagos = new ArrayList<>();
        try {
            _dateChooser1.setFormat(3);
            _dateChooser2.setFormat(3);
            var cal = new Calendario();
            String[] titulos = {"Id", "Deuda", "Saldo", "Pago", "Cambio", "Fecha",
                "Ticket", "Fecha deuda", "Mensual"};
            modelo3 = new DefaultTableModel(null, titulos);
            var date1 = formateador.parse(_dateChooser1.getSelectedPeriodSet().toString());
            var date2 = formateador.parse(_dateChooser2.getSelectedPeriodSet().toString());
            if (filtrar) {
                if (date2.after(date1) || date1.equals(date2)) {
                    var listPagos1 = new ArrayList<TPagos_proveedor>();
                    var pagos = pagosProveedores().stream()
                            .filter(u -> u.getIdProveedor() == _idProveedorReport)
                            .collect(Collectors.toList());
                    formateador = new SimpleDateFormat("yyyy/MM/dd");
                    _dateChooser1.setFormat(3);
                    var date3 = _dateChooser1.getCurrent();
                    var date4 = formateador.parse(cal.getFecha(date3));
                    for (TPagos_proveedor pago : pagos) {
                        var date5 = pago.getFecha();
                        if (date4.equals(date5) || date4.before(date5)) {
                            listPagos1.add(pago);
                        }
                    }
                    _dateChooser2.setFormat(3);
                    var date6 = _dateChooser2.getCurrent();
                    var date7 = formateador.parse(cal.getFecha(date6));
                    for (TPagos_proveedor pago : listPagos1) {
                        var date8 = pago.getFecha();
                        if (date7.equals(date8) || date7.after(date8)) {
                            Object[] registros = {
                                pago.getIdPagosProve(),
                                _format.decimal(pago.getDeuda()) + _mony,
                                _format.decimal(pago.getSaldo()) + _mony,
                                _format.decimal(pago.getPago()) + _mony,
                                _format.decimal(pago.getCambio()) + _mony,
                                pago.getFecha(),
                                pago.getTicket(),
                                pago.getFechaDeuda(),
                                _mony + _format.decimal(pago.getMensual()),};
                            modelo3.addRow(registros);
                            listPagos.add(pago);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "La fecha final debe ser mayor a la fecha inicial ");
                }
            } else {
                int inicio = (_num_pagina - 1) * _reg_por_pagina;
                var pagos = pagosProveedores().stream()
                        .filter(u -> u.getIdProveedor() == _idProveedorReport)
                        .collect(Collectors.toList());
                listPagos = pagos;
                var data = pagos.stream()
                        .skip(inicio).limit(_reg_por_pagina)
                        .collect(Collectors.toList());
                Collections.reverse(listPagos);
                Collections.reverse(data);
                for (TPagos_proveedor pago : data) {
                    Object[] registros = {
                        pago.getIdPagosProve(),
                        _format.decimal(pago.getDeuda()) + _mony,
                        _format.decimal(pago.getSaldo()) + _mony,
                        _format.decimal(pago.getPago()) + _mony,
                        _format.decimal(pago.getCambio()) + _mony,
                        pago.getFecha(),
                        pago.getTicket(),
                        pago.getFechaDeuda(),
                        _format.decimal(pago.getMensual()) + _mony
                    };
                    modelo3.addRow(registros);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        _tablePagoCuotas.setModel(modelo3);
        _tablePagoCuotas.setRowHeight(30);
        _tablePagoCuotas.getColumnModel().getColumn(0).setMaxWidth(0);
        _tablePagoCuotas.getColumnModel().getColumn(0).setMinWidth(0);
        _tablePagoCuotas.getColumnModel().getColumn(0).setPreferredWidth(0);
        _tablePagoCuotas.getColumnModel().getColumn(7).setMaxWidth(0);
        _tablePagoCuotas.getColumnModel().getColumn(7).setMinWidth(0);
        _tablePagoCuotas.getColumnModel().getColumn(7).setPreferredWidth(0);
        _tablePagoCuotas.getColumnModel().getColumn(8).setMaxWidth(0);
        _tablePagoCuotas.getColumnModel().getColumn(8).setMinWidth(0);
        _tablePagoCuotas.getColumnModel().getColumn(8).setPreferredWidth(0);
    }

    public void getHistorialPago() {
        formateador = new SimpleDateFormat("dd/MM/yyyy");
        int fila = _tablePagoCuotas.getSelectedRow();
        _idHistorial = (Integer) modelo3.getValueAt(fila, 0);
        var deuda = (String) modelo3.getValueAt(fila, 1);
        _label.get(14).setText(deuda);
        var saldo = (String) modelo3.getValueAt(fila, 2);
        _label.get(15).setText(saldo);
        var fechaDeuda = (Date) modelo3.getValueAt(fila, 7);
        if (fechaDeuda != null) {
            _label.get(16).setText(fechaDeuda.toString());
        }
        var ticket = (String) modelo3.getValueAt(fila, 6);
        _label.get(17).setText(ticket);
        var pago = (String) modelo3.getValueAt(fila, 3);
        _label.get(18).setText(pago);
        var mensual = (String) modelo3.getValueAt(fila, 8);
        _label.get(19).setText(mensual);
        var fechaPago = (Date) modelo3.getValueAt(fila, 5);
        if (fechaPago != null) {
            _label.get(20).setText(fechaPago.toString());
        }
        var cambio = (String) modelo3.getValueAt(fila, 4);
        _label.get(21).setText(cambio);

        var usuario = _dataUsuario.getNombre();
        ticket1.textoCentro("Sistema de ventas Anvimol"); // imprime en el centro
        ticket1.lineasVacia();
        ticket1.textoIzquierda("Direccion");
        ticket1.textoIzquierda("Avda La Paz 3, Onil");
        ticket1.textoIzquierda("Tel 665151716");
        ticket1.lineasGuion();
        ticket1.textoCentro("FACTURA"); // imprime en el centro 
        ticket1.lineasGuion();
        ticket1.textoIzquierda("Factura: " + ticket);
        ticket1.textoIzquierda("Proveedor: " + nameProveedor);
        ticket1.textoIzquierda("Fecha:   " + fechaPago);
        ticket1.textoIzquierda("Usuario: " + usuario);
        ticket1.lineasGuion();
        ticket1.textoCentro("Su credito: " + deuda);
        ticket1.textoExtremo("Cuotas por mes:", mensual);
        ticket1.textoExtremo("Deuda anterior:", _format.decimal(_deudaActual) + _mony);
        ticket1.textoExtremo("Pago:", pago);
        ticket1.textoExtremo("Cambio:", cambio);
        ticket1.textoExtremo("Deuda actual:", saldo);
        ticket1.lineasVacia();
        ticket1.textoCentro("@anvimol");
    }

    public void ticketDeuda() {
        switch (_seccion1) {
            case 1:
                if (_idHistorial == 0) {
                    JOptionPane.showMessageDialog(null, "Seleccione un historial de pago ");
                } else {
                    ticket1.print();
                }
                break;
        }
    }

    public final void restablecerReport() {
        _idReport = 0;
        _deudaActual = 0.0;
        _cambio = 0.0;
        _pago = 0.0;
        _mensual = 0.0;
        _label.get(7).setText("Nombre del Proveedor");
        _label.get(8).setText("0.00" + _mony);
        _label.get(9).setText("0.00" + _mony);
        _label.get(10).setText("0000000000");
        _label.get(11).setText("--/--/----");
        _label.get(12).setText("0.00" + _mony);
        _label.get(13).setText("Ingrese el pago");
        _textfield.get(5).setText("");
        searchReportes("");
        if (!reporteFilter.isEmpty()) {
            _paginadorReporProve = new Paginador<>(reporteFilter,
                    _label.get(6), _reg_por_pagina);
        }
    }

    // </editor-fold>
    public void paginador(String metodo) {
        switch (metodo) {
            case "Primero":
                switch (seccion) {
                    case 0:
                        if (!proveedorFilter.isEmpty()) {
                            _num_pagina = _paginadorProveedor.primero();
                        }
                        break;
                    case 1:
                        switch (_seccion1) {
                            case 0:
                                if (!reporteFilter.isEmpty()) {
                                    _num_pagina = _paginadorReporProve.primero();
                                }
                                break;
                            case 1:
                                if (!listPagos.isEmpty()) {
                                    _num_pagina = _paginadorPagosProve.primero();
                                }
                                break;
                        }
                }
                break;
            case "Anterior":
                switch (seccion) {
                    case 0:
                        if (!proveedorFilter.isEmpty()) {
                            _num_pagina = _paginadorProveedor.anterior();
                        }
                        break;
                    case 1:
                        switch (_seccion1) {
                            case 0:
                                if (!reporteFilter.isEmpty()) {
                                    _num_pagina = _paginadorReporProve.anterior();
                                }
                                break;
                            case 1:
                                if (!listPagos.isEmpty()) {
                                    _num_pagina = _paginadorPagosProve.anterior();
                                }
                                break;
                        }
                }
                break;
            case "Siguiente":
                switch (seccion) {
                    case 0:
                        if (!proveedorFilter.isEmpty()) {
                            _num_pagina = _paginadorProveedor.siguiente();
                        }
                        break;
                    case 1:
                        switch (_seccion1) {
                            case 0:
                                if (!reporteFilter.isEmpty()) {
                                    _num_pagina = _paginadorReporProve.siguiente();
                                }
                                break;
                            case 1:
                                if (!listPagos.isEmpty()) {
                                    _num_pagina = _paginadorPagosProve.siguiente();
                                }
                                break;
                        }
                }
                break;
            case "Ultimo":
                switch (seccion) {
                    case 0:
                        if (!proveedorFilter.isEmpty()) {
                            _num_pagina = _paginadorProveedor.ultimo();
                        }
                        break;
                    case 1:
                        switch (_seccion1) {
                            case 0:
                                if (!reporteFilter.isEmpty()) {
                                    _num_pagina = _paginadorReporProve.ultimo();
                                }
                                break;
                            case 1:
                                if (!listPagos.isEmpty()) {
                                    _num_pagina = _paginadorPagosProve.ultimo();
                                }
                                break;
                        }
                }
                break;
        }

        switch (seccion) {
            case 0:
                searchProveedores("");
                break;
            case 1:
                switch (_seccion1) {
                    case 0:
                        searchReportes("");
                        break;
                    case 1:
                        historialPagos(false);
                        break;
                }
                break;
        }
    }

    public void registroPaginas() {
        _num_pagina = 1;
        Number value = (Number) _spinnerPaginas.getValue();
        _reg_por_pagina = value.intValue();
        switch (seccion) {
            case 0:
                if (!proveedorFilter.isEmpty()) {
                    _paginadorProveedor = new Paginador<>(proveedorFilter,
                            _label.get(6), _reg_por_pagina);
                }
                searchProveedores("");
                break;
            case 1:
                switch (_seccion1) {
                    case 0:
                        if (!reporteFilter.isEmpty()) {
                            _paginadorReporProve = new Paginador<>(reporteFilter,
                                    _label.get(6), _reg_por_pagina);
                        }
                        searchReportes("");
                        break;
                    case 1:
                        if (!listPagos.isEmpty()) {
                            _paginadorPagosProve = new Paginador<>(listPagos,
                                    _label.get(6), _reg_por_pagina);
                        }
                        historialPagos(false);
                        break;
                }
                break;
        }
    }
}
