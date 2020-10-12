package ViewModels;

import Conexion.Consult;
import Libreria.*;
import Models.Cliente.*;
import Models.Usuario.TUsuarios;
import datechooser.beans.DateChooserCombo;
import java.awt.Color;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

/**
 *
 * @author anvimol
 */
public class ClientesVM extends Consult {

    private String _accion = "insert", _mony;
    private ArrayList<JLabel> _label;
    private ArrayList<JTextField> _textfield;
    private JCheckBox _checkBoxCredito;
    private JTable _tablaCliente, _tablaClientePagos, _tablaReporDeudaCliente;
    private JTable _tablaPagosCuotas, _tablaPagosIntereses;
    private DefaultTableModel modelo1, modelo2, modelo3, modelo4, modelo5;
    private JSpinner _spinnerPaginas;
    private JRadioButton _radioCuota, _radioInteres;
    private int _idCliente = 0, _idReport, _idClienteRport, _idReportIntereses;
    private int _num_pagina = 1, _reg_por_pagina = 10, _idHistorial, _idHistorial1;
    public int seccion, _interesCuotas = 0, cuotas = 0, _seccion1;
    private Double _intereses = 0.0, _deudaActual = 0.0, _interesPago = 0.0;
    private Double _pago = 0.0, _mensual = 0.0, _deudaActualCliente = 0.0, _deuda = 0.0;
    private Double _interesesPagos = 0.0, _cambio = 0.0, _interesesCliente = 0.0;
    private String nameCliente, _ticketCuota, _ticketIntereses, tickets = "";
    private Codigos _codigos;
    private Paginador<TClientes> _paginadorClientes;
    private Paginador<TClientes> _paginadorReportes;
    private Paginador<TReportes_clientes> _paginadorReportesDeuda;
    private Paginador<TPagos_clientes> _paginadorPagos;
    private Paginador<TPagos_reportes_intereses_clientes> _paginadorPagosIntereses;
    private List<TClientes> listClientes;
    private List<TClientes> listReportesDeuda;
    private List<TReportes_clientes> listReportes;
    private List<TIntereses_clientes> _listIntereses;
    private List<TPagos_clientes> listPagos = new ArrayList<>();
    private List<TPagos_reportes_intereses_clientes> listPagosIntereses = new ArrayList<>();
    private FormatDecimal _format;
    private SimpleDateFormat formateador;
    private DateChooserCombo _dateChooser, _dateChooser1, _dateChooser2;
    private JCheckBox _chekBox_dia;
    private static TUsuarios _dataUsuario;
    private Ticket ticket1 = new Ticket();

    public ClientesVM() {
    }

    public ClientesVM(TUsuarios dataUsuarios) {
        _dataUsuario = dataUsuarios;
        formateador = new SimpleDateFormat("yyyy/MM/dd");
    }

    public ClientesVM(Object[] objects, ArrayList<JLabel> label, ArrayList<JTextField> textfield) {
        _textfield = textfield;
        _label = label;
        _checkBoxCredito = (JCheckBox) objects[0];
        _tablaCliente = (JTable) objects[1];
        _spinnerPaginas = (JSpinner) objects[2];
        _tablaClientePagos = (JTable) objects[3];
        _radioCuota = (JRadioButton) objects[4];
        _radioInteres = (JRadioButton) objects[5];
        _tablaReporDeudaCliente = (JTable) objects[6];
        _dateChooser = (DateChooserCombo) objects[7];
        _chekBox_dia = (JCheckBox) objects[8];
        _dateChooser1 = (DateChooserCombo) objects[9];
        _dateChooser2 = (DateChooserCombo) objects[10];
        _tablaPagosCuotas = (JTable) objects[11];
        _tablaPagosIntereses = (JTable) objects[12];
        _format = new FormatDecimal();
        _codigos = new Codigos();
        _mony = ConfiguracionVM.Mony;
        formateador = new SimpleDateFormat("yyyy/MM/dd");
        restablecer();
        restablecerReportCliente();
        resetReportDeuda();
    }

    // <editor-fold defaultstate="collapsed" desc="CÓDIGO REGISTRO CLIENTE">
    public void registrarCliente() {
        if (validarDatos()) {
            int count;
            List<TClientes> listEmail = clientes().stream()
                    .filter(u -> u.getEmail().equals(_textfield.get(3).getText()))
                    .collect(Collectors.toList());
            count = listEmail.size();
            List<TClientes> listDni = clientes().stream()
                    .filter(u -> u.getDni().equals(_textfield.get(0).getText()))
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
                            if (listEmail.get(0).getId() == _idCliente
                                    && listDni.get(0).getId() == _idCliente) {
                                saveData();
                            } else {
                                if (listDni.get(0).getId() != _idCliente) {
                                    _label.get(0).setText("El DNI ya esta registrado");
                                    _label.get(0).setForeground(Color.RED);
                                    _textfield.get(0).requestFocus();
                                }
                                if (listEmail.get(0).getId() != _idCliente) {
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
                                    if (listDni.get(0).getId() == _idCliente) {
                                        saveData();
                                    } else {
                                        _label.get(0).setText("El DNI ya esta registrado");
                                        _label.get(0).setForeground(Color.RED);
                                        _textfield.get(0).requestFocus();
                                    }
                                }
                                if (!listEmail.isEmpty()) {
                                    if (listEmail.get(0).getId() == _idCliente) {
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
                image = Objetos.uploadImage.getTransFoto(_label.get(6));
            }
            switch (_accion) {
                case "insert":
                    String sqlCliente = "INSERT INTO tclientes(dni,nombre,"
                            + "email,telefono,direccion,credito,fecha,imagen) "
                            + "VALUES(?,?,?,?,?,?,?,?)";
                    Object[] dataCliente = {
                        _textfield.get(0).getText(),
                        _textfield.get(1).getText(),
                        _textfield.get(2).getText(),
                        _textfield.get(3).getText(),
                        _textfield.get(4).getText(),
                        _checkBoxCredito.isSelected(),//tynyint
                        new Calendario().getFecha(),
                        image
                    };

                    qr.insert(getConn(), sqlCliente, new ColumnListHandler(), dataCliente);

                    String sqlReport = "INSERT INTO treportes_clientes (deuda,"
                            + "mensual,cambio,ultimoPago,fechaPago,deudaActual,"
                            + "fechaDeuda,ticket,fechaLimite,idCliente) "
                            + "VALUES (?,?,?,?,?,?,?,?,?,?)";

                    List<TClientes> cliente = clientes();

                    Object[] dataReport = {
                        0,
                        0,
                        0,
                        0,
                        "--/--/----",
                        0,
                        "--/--/----",
                        "00000000000",
                        "--/--/----",
                        cliente.get(cliente.size() - 1).getId() // Ultimo cliente registrado
                    };
                    qr.insert(getConn(), sqlReport, new ColumnListHandler(), dataReport);

                    String sqlReportInteres = "INSERT INTO treportes_intereses_clientes "
                            + "(intereses,pago,cambio_interes,cuotas,interesFecha,ticketIntereses,"
                            + "clienteId) VALUES (?,?,?,?,?,?,?)";
                    Object[] dataReportInteres = {
                        0,
                        0,
                        0,
                        0,
                        "--/--/----",
                        "00000000000",
                        cliente.get(cliente.size() - 1).getId(), // Ultimo cliente registrado
                    };
                    qr.insert(getConn(), sqlReportInteres, new ColumnListHandler(),
                            dataReportInteres);
                    break;
                case "update":
                    Object[] dataCliente2 = {
                        _textfield.get(0).getText(),
                        _textfield.get(1).getText(),
                        _textfield.get(2).getText(),
                        _textfield.get(3).getText(),
                        _textfield.get(4).getText(),
                        _checkBoxCredito.isSelected(),//tynyint
                        image
                    };
                    String sqlCliente2 = "UPDATE tclientes SET dni=?, nombre=?,"
                            + "email=?, telefono=?, direccion=?,"
                            + "credito=?, imagen=? WHERE id = " + _idCliente;
                    qr.update(getConn(), sqlCliente2, dataCliente2);
                    break;
            }

            getConn().commit();
            restablecer();
        } catch (SQLException e) {
            getConn().rollback();
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void searchCliente(String campo) {
        List<TClientes> clienteFilter;
        String[] titulos = {"Id", "Dni", "Nombre", "Email", "Teléfono",
            "Dirección", "Credito", "Imagen"};
        modelo1 = new DefaultTableModel(null, titulos);

        int inicio = (_num_pagina - 1) * _reg_por_pagina;

        if (campo.isEmpty()) {
            clienteFilter = clientes().stream()
                    .skip(inicio).limit(_reg_por_pagina)
                    .collect(Collectors.toList());
        } else {
            clienteFilter = clientes().stream()
                    .filter(C -> C.getDni().startsWith(campo)
                    || C.getNombre().startsWith(campo))
                    .skip(inicio).limit(_reg_por_pagina)
                    .collect(Collectors.toList());
        }

        if (!clienteFilter.isEmpty()) {
            clienteFilter.forEach(item -> {
                Object[] registros = {
                    item.getId(),
                    item.getDni(),
                    item.getNombre(),
                    item.getEmail(),
                    item.getTelefono(),
                    item.getDireccion(),
                    item.isCredito(),
                    item.getImagen()
                };
                modelo1.addRow(registros);
            });
        }
        _tablaCliente.setModel(modelo1);
        _tablaCliente.setRowHeight(30);
        _tablaCliente.getColumnModel().getColumn(0).setMaxWidth(0);
        _tablaCliente.getColumnModel().getColumn(0).setMinWidth(0);
        _tablaCliente.getColumnModel().getColumn(0).setPreferredWidth(0);
        _tablaCliente.getColumnModel().getColumn(7).setMaxWidth(0);
        _tablaCliente.getColumnModel().getColumn(7).setMinWidth(0);
        _tablaCliente.getColumnModel().getColumn(7).setPreferredWidth(0);
        _tablaCliente.getColumnModel().getColumn(6).setCellRenderer(new Render_CheckBox());
    }

    public void getCliente() {
        _accion = "update";
        int fila = _tablaCliente.getSelectedRow();
        _idCliente = (Integer) modelo1.getValueAt(fila, 0);
        _textfield.get(0).setText((String) modelo1.getValueAt(fila, 1));
        _textfield.get(1).setText((String) modelo1.getValueAt(fila, 2));
        _textfield.get(2).setText((String) modelo1.getValueAt(fila, 3));
        _textfield.get(3).setText((String) modelo1.getValueAt(fila, 4));
        _textfield.get(4).setText((String) modelo1.getValueAt(fila, 5));
        _checkBoxCredito.setSelected((Boolean) modelo1.getValueAt(fila, 6));
        Objetos.uploadImage.byteImage(_label.get(5), (byte[]) modelo1.getValueAt(fila, 7), 179, 209); //168, 195
    }

    public final void restablecer() {
        seccion = 1;
        _accion = "insert";
        _textfield.get(0).setText("");
        _textfield.get(1).setText("");
        _textfield.get(2).setText("");
        _textfield.get(3).setText("");
        _textfield.get(4).setText("");
        _checkBoxCredito.setSelected(false);
        _checkBoxCredito.setForeground(new Color(70, 106, 124));
        _label.get(0).setText("DNI");
        _label.get(0).setForeground(new Color(70, 106, 124));
        _label.get(1).setText("Nombre");
        _label.get(1).setForeground(new Color(70, 106, 124));
        _label.get(2).setText("Email");
        _label.get(2).setForeground(new Color(70, 106, 124));
        _label.get(3).setText("Teléfono");
        _label.get(3).setForeground(new Color(70, 106, 124));
        _label.get(4).setText("Dirección");
        _label.get(4).setForeground(new Color(70, 106, 124));
        _label.get(5).setIcon(new ImageIcon(getClass().getClassLoader()
                .getResource("Resources/iconos-clientes-png.png")));
        listClientes = clientes();
        if (!listClientes.isEmpty()) {
            _paginadorClientes = new Paginador<>(listClientes,
                    _label.get(6), _reg_por_pagina);
        }
        SpinnerNumberModel model = new SpinnerNumberModel(
                10.0, // Dato visualizado al inicio del spinner
                1.0, //Limite inferior
                100.0, // Limite Superior
                1.0 // Incremento - decremento
        );
        _spinnerPaginas.setModel(model);
        searchCliente("");
        registroPaginas();
    }

    private boolean validarDatos() {
        if (_textfield.get(0).getText().isEmpty()) {
            _label.get(0).setText("Ingrese el DNI");
            _label.get(0).setForeground(Color.RED);
            _textfield.get(0).requestFocus();
            return false;
        } else if (_textfield.get(1).getText().isEmpty()) {
            _label.get(1).setText("Ingrese el Nombre");
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
    public void searchReportes(String campo) {
        List<TClientes> clienteFilter;
        String[] titulos = {"Id", "Dni", "Nombre", "Email", "Teléfono",
            "Dirección"};
        modelo2 = new DefaultTableModel(null, titulos);
        int inicio = (_num_pagina - 1) * _reg_por_pagina;
        if (campo.equals("")) {
            clienteFilter = clientes().stream()
                    .skip(inicio).limit(_reg_por_pagina)
                    .collect(Collectors.toList());
        } else {
            clienteFilter = clientes().stream()
                    .filter(C -> C.getDni().startsWith(campo) || C.getNombre().startsWith(campo))
                    .skip(inicio).limit(_reg_por_pagina)
                    .collect(Collectors.toList());
        }
        if (!clienteFilter.isEmpty()) {
            clienteFilter.forEach(item -> {
                Object[] registros = {
                    item.getId(),
                    item.getDni(),
                    item.getNombre(),
                    item.getEmail(),
                    item.getTelefono(),
                    item.getDireccion()
                };
                modelo2.addRow(registros);
            });
        }
        _tablaClientePagos.setModel(modelo2);
        _tablaClientePagos.setRowHeight(30);
        _tablaClientePagos.getColumnModel().getColumn(0).setMaxWidth(0);
        _tablaClientePagos.getColumnModel().getColumn(0).setMinWidth(0);
        _tablaClientePagos.getColumnModel().getColumn(0).setPreferredWidth(0);
    }

    public void getReportCliente() {
        int fila = _tablaClientePagos.getSelectedRow();
        _idClienteRport = (Integer) modelo2.getValueAt(fila, 0);
        List<TReportes_clientes> clienteFilter = reportesClientes(_idClienteRport);
        if (!clienteFilter.isEmpty()) {
            TReportes_clientes cliente = clienteFilter.get(0);
            _idReport = cliente.getIdReporte();
            _idReportIntereses = cliente.getIdReportInteres();
            nameCliente = cliente.getNombre();
            _label.get(7).setText(nameCliente);
            _deudaActual = (Double) cliente.getDeudaActual();
            _deuda = (Double) cliente.getDeuda();
            _label.get(8).setText(_format.decimal(_deudaActual) + _mony);
            _label.get(9).setText(_format.decimal((Double) cliente.getUltimoPago()) + _mony);
            _ticketCuota = cliente.getTicket();
            _label.get(10).setText(_ticketCuota);
            _label.get(11).setText(cliente.getFechaPago());
            _label.get(12).setText(_format.decimal((Double) cliente.getMensual()) + _mony);
            _listIntereses = interesesClientes().stream()
                    .filter(u -> u.getIdCliente() == _idClienteRport
                    && u.isCancelado() == false)
                    .collect(Collectors.toList());
            if (_listIntereses.isEmpty()) {
                _label.get(13).setText("0,00" + _mony);
                _label.get(14).setText("0");
                _label.get(15).setText("0000000000");
                _label.get(16).setText("--/--/----");
            } else {
                _interesCuotas = 0;
                _intereses = 0.0;
                _listIntereses.forEach(item -> {
                    _intereses += item.getIntereses();
                    _interesCuotas++;
                });
                _label.get(13).setText(_format.decimal(_intereses) + _mony);
                _label.get(14).setText(String.valueOf(_interesCuotas));
                _ticketIntereses = cliente.getTicketIntereses();
                _label.get(15).setText(_ticketIntereses);
                _label.get(16).setText(cliente.getInteresFecha());
            }
            historialPagos(false);
            historialIntereses(false);
        }
    }

    public void pagos() {
        if (!_textfield.get(6).getText().isEmpty()) {
            _label.get(19).setText("Ingrese el pago");
            if (_idReport == 0) {
                _label.get(18).setText("Seleccione un cliente");
            } else {
                if (_radioInteres.isSelected()) {
                    if (!_textfield.get(5).getText().isEmpty()) {
                        int cantCuotas = Integer.valueOf(_textfield.get(6).getText());
                        if (cantCuotas <= _interesCuotas) {
                            if (!_textfield.get(6).getText().isEmpty()) {
                                _interesPago = _format.reconstruir(_textfield.get(7).getText());
                                if (_interesPago >= _interesesPagos) {
                                    _cambio = _interesPago - _interesesPagos;
                                    _label.get(18).setText("Cambio para el cliente "
                                            + _format.decimal(_cambio) + _mony); //String.format("%.2f", _cambio) + _mony);
                                    _interesesCliente = _intereses - _interesesPagos;
                                    _label.get(13).setText(_format.decimal(_interesesCliente) + _mony);
                                } else {
                                    _label.get(18).setText("El pago debe ser "
                                            + _format.decimal(_interesesPagos) + _mony);
                                    _interesesCliente = _intereses - _interesesPagos;
                                    _label.get(13).setText(_format.decimal(_interesesCliente) + _mony);
                                }
                            }
                        } else {
                            _label.get(18).setText("Cuotas inválidas");
                        }
                    } else {
                        _label.get(19).setText("Ingrese el número de cuotas");
                        _textfield.get(5).requestFocus();
                    }
                } else if (_radioCuota.isSelected()) {
                    if (!_textfield.get(6).getText().isEmpty()) {
                        _pago = _format.reconstruir(_textfield.get(6).getText());
                        TReportes_clientes dataReport = reporteCliente().stream()
                                .filter(u -> u.getIdReporte() == _idReport)
                                .collect(Collectors.toList()).get(0);
                        _mensual = dataReport.getMensual();
                        if (_pago > _mensual) {
                            if (Objects.equals(_pago, _deudaActual) || _pago > _deudaActual) {
                                _cambio = _pago - _deudaActual;
                                _label.get(18).setText("Cambio para el cliente "
                                        + _format.decimal(_cambio) + _mony);
                                _label.get(8).setText("0.00" + _mony);
                                _deudaActual = 0.00;
                                _deudaActualCliente = 0.00;
                            } else {
                                _cambio = _pago - _mensual;
                                _label.get(18).setText("Cambio para el cliente "
                                        + _format.decimal(_cambio) + _mony);
                                _deudaActualCliente = _deudaActual - _mensual;
                                _label.get(8).setText(_format.decimal(_deudaActualCliente) + _mony);
                            }
                        } else if (Objects.equals(_pago, _mensual)) {
                            _deudaActualCliente = _deudaActual - _mensual;
                            _label.get(8).setText(_format.decimal(_deudaActualCliente) + _mony);
                        }
                    }
                }
            }
        } else {
            _label.get(18).setText("Ingrese el pago");
            _label.get(8).setText(_format.decimal(_deudaActual) + _mony);
            _label.get(13).setText(_format.decimal(_intereses) + _mony);
        }
    }

    public void cuotasIntereses() {
        if (Objects.equals(_idReport, 0)) { // _idReport == 0
            _label.get(19).setText("Seleccione un cliente");
        } else {
            if (_deudaActual > 0 || _intereses > 0) {
                _label.get(18).setText("Ingrese el pago");
                if (_textfield.get(5) != null) {
                    if (_textfield.get(5).getText().isEmpty()) {
                        _label.get(13).setText(_format.decimal(_intereses) + _mony);
                        _label.get(14).setText(String.valueOf(_interesCuotas));
                        _label.get(17).setText("0,00" + _mony);
                        _label.get(18).setText("Ingrese el pago");
                    } else {
                        _label.get(17).setText("0,00" + _mony);
                        int cantCuotas = Integer.valueOf(_textfield.get(5).getText());
                        if (cantCuotas <= _interesCuotas) {
                            _label.get(18).setText("Ingrese el pago");
                            if (!_listIntereses.isEmpty()) {
                                _interesesPagos = 0.00;
                                for (int i = 0; i < cantCuotas; i++) {
                                    _interesesPagos += _listIntereses.get(i).getIntereses();
                                }
                                cuotas = _interesCuotas - cantCuotas;
                                Double intereses = _intereses - _interesesPagos;
                                _label.get(13).setText(_format.decimal(intereses)
                                        + _mony);
                                _label.get(14).setText(String.valueOf(cuotas));
                                _label.get(17).setText(_format.decimal(_interesesPagos)
                                        + _mony);
                            }
                        } else {
                            _label.get(18).setText("Cuotas invalidas");
                            _textfield.get(5).requestFocus();
                        }
                    }
                }
                pagos();
            } else {
                _label.get(18).setText("El cliente no contiene deuda");
            }

        }
    }

    public void ejecutarPago() throws SQLException {
        final QueryRunner qr = new QueryRunner(true);
        if (Objects.equals(_idReport, 0)) {
            _label.get(18).setText("Seleccione un cliente");
        } else {
            if (_textfield.get(6).getText().isEmpty()) {
                _label.get(18).setText("Ingrese el pago");
                _textfield.get(6).requestFocus();
            } else {
                String fecha = new Calendario().getFecha();
                //Realizar consulta al usuario que inicia sesión
                var usuario = _dataUsuario.getNombre();
                if (_radioCuota.isSelected()) {
                    if (!_deuda.equals(0) || !_deuda.equals(0.0) || !_deuda.equals(0.00)) {
                        if (_pago >= _mensual) {
                            try {
                                getConn().setAutoCommit(false);
                                String dateNow = new Calendario().addMes(1);
                                String _fechalimite = Objects.equals(_deudaActual, 0) ? new Calendario().getFecha() : dateNow;
                                String ticket = _codigos.codesTickets(_ticketCuota);

                                String query1 = "INSERT INTO tpagos_clientes(deuda,saldo,pago,cambio,"
                                        + "fecha,fechaLimite,ticket,idUsuario,usuario,idCliente,fechaDeuda,mensual) "
                                        + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";

                                var dataReport = reporteCliente().stream()
                                        .filter(u -> u.getIdCliente() == _idClienteRport)
                                        .collect(Collectors.toList()).get(0);

                                Object[] data1 = {
                                    _deuda,
                                    _deudaActualCliente,
                                    _pago,
                                    _cambio,
                                    formateador.parse(fecha),
                                    formateador.parse(_fechalimite),
                                    ticket,
                                    _dataUsuario.getIdUsuario(),
                                    usuario,
                                    _idClienteRport,
                                    formateador.parse(dataReport.getFechaDeuda()),
                                    dataReport.getMensual()
                                };
                                qr.insert(getConn(), query1, new ColumnListHandler(), data1);

                                if (_deudaActualCliente.equals(0.00)) {
                                    String query2 = "UPDATE treportes_clientes SET deuda=?, "
                                            + "mensual=?, fechaDeuda=?, deudaActual=?,"
                                            + "ultimoPago=?, cambio=?, fechaPago=?, ticket=?,"
                                            + "fechaLimite=? WHERE idReporte =" + _idReport;
                                    Object[] data2 = {
                                        0.00,
                                        0.00,
                                        "--/--/----",
                                        0.00,
                                        0.00,
                                        0.00,
                                        "--/--/----",
                                        "0000000000",
                                        "--/--/----",};
                                    qr.update(getConn(), query2, data2);
                                } else {
                                    String query2 = "UPDATE treportes_clientes SET deuda=?, "
                                            + "mensual=?, fechaDeuda=?, deudaActual=?,"
                                            + "ultimoPago=?, cambio=?, fechaPago=?, ticket=?,"
                                            + "fechaLimite=? WHERE idReporte =" + _idReport;
                                    Object[] data2 = {
                                        _deuda, dataReport.getMensual(),
                                        fecha, _deudaActualCliente, _pago,
                                        _cambio, fecha, ticket, _fechalimite
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
                                ticket1.textoIzquierda("Cliente: " + nameCliente);
                                ticket1.textoIzquierda("Fecha:   " + fecha);
                                ticket1.textoIzquierda("Usuario: " + usuario);
                                ticket1.lineasGuion();
                                ticket1.textoCentro("Su credito: " + _format.decimal(_deuda) + _mony);
                                ticket1.textoExtremo("Cuotas por 12 meses:", _format.decimal(_mensual) + _mony);
                                ticket1.textoExtremo("Deuda anterior:", _format.decimal(_deudaActual) + _mony);
                                ticket1.textoExtremo("Pago:", _format.decimal(_pago) + _mony);
                                ticket1.textoExtremo("Cambio:", _format.decimal(_cambio) + _mony);
                                ticket1.textoExtremo("Deuda actual:", _format.decimal(_deudaActualCliente) + _mony);
                                ticket1.textoExtremo("Próximo pago:", _fechalimite);
                                ticket1.lineasVacia();
                                ticket1.textoCentro("@anvimol");
                                ticket1.print();
                                getConn().commit();
                                restablecerReportCliente();
                            } catch (SQLException e) {
                                getConn().rollback();
                                JOptionPane.showMessageDialog(null, e);
                            } catch (ParseException ex) {
                                Logger.getLogger(ClientesVM.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } else {
                        _label.get(18).setText("El cliente no tiene deuda");
                    }
                } else if (_radioInteres.isSelected()) {
                    if (!_intereses.equals(0)) {
                        if (!_textfield.get(5).getText().equals("")) {
                            try {
                                Integer cantCoutas = Integer.valueOf(_textfield.get(5).getText());
                                if (cantCoutas <= _interesCuotas) {
                                    if (_interesPago >= _interesesPagos) {
                                        getConn().setAutoCommit(false);
                                        if (!_listIntereses.isEmpty()) {
                                            Object[] data1 = {true};
                                            for (int i = 0; i < cantCoutas; i++) {
                                                String query1 = "UPDATE tintereses_clientes SET cancelado=?"
                                                        + " WHERE idInteresesCliente=" + _listIntereses.get(i).getIdInteresesCliente() + " AND "
                                                        + " idCliente=" + _idClienteRport;
                                                qr.update(getConn(), query1, data1);
                                            }

                                            String ticket = _codigos.codesTickets(_ticketIntereses);
                                            String query2 = "INSERT INTO tpagos_reportes_intereses_clientes (intereses,pago,"
                                                    + "cambio,cuotas,fecha,ticket,idUsuario,usuario,idCliente) VALUES (?,?,?,?,?,?,?,?,?)";
                                            Object[] data2 = {
                                                _intereses,
                                                _interesPago,
                                                _cambio,
                                                cantCoutas,
                                                fecha,
                                                ticket,
                                                _dataUsuario.getIdUsuario(),
                                                usuario,
                                                _idClienteRport
                                            };
                                            qr.insert(getConn(), query2, new ColumnListHandler(), data2);

                                            String query3 = "UPDATE treportes_intereses_clientes SET intereses=?,pago=?,"
                                                    + "cambio_interes=?,cuotas=?,interesFecha=?,ticketIntereses=?"
                                                    + " WHERE idReportInteres=" + _idReportIntereses + " AND "
                                                    + "clienteId=" + _idClienteRport;
                                            if (cuotas == 0) {
                                                Object[] data3 = {
                                                    0.00,
                                                    0.00,
                                                    0.00,
                                                    0,
                                                    fecha,
                                                    "0000000000"
                                                };
                                                qr.update(getConn(), query3, data3);
                                            } else {
                                                Object[] data3 = {
                                                    _interesesCliente,
                                                    _interesPago,
                                                    _cambio,
                                                    cantCoutas,
                                                    fecha,
                                                    ticket
                                                };
                                                qr.update(getConn(), query3, data3);
                                            }

                                            ticket1.textoCentro("Sistema de ventas AVM");
                                            ticket1.lineasVacia();
                                            ticket1.textoIzquierda("Direccion");
                                            ticket1.textoIzquierda("Avda La Paz, 3 2ºB");
                                            ticket1.textoIzquierda("Tel 665151716");
                                            ticket1.lineasGuion();
                                            ticket1.textoCentro("FACTURA DE PAGOS DE INTERESES");
                                            ticket1.lineasGuion();
                                            ticket1.textoIzquierda("Factura: " + ticket);
                                            ticket1.textoIzquierda("Cliente: " + nameCliente);
                                            ticket1.textoIzquierda("Fecha: " + fecha);
                                            ticket1.textoIzquierda("Usuario: " + usuario);
                                            ticket1.lineasGuion();
                                            ticket1.textoCentro("Intereses " + _format.decimal(_intereses) + _mony);
                                            ticket1.textoExtremo("Cuotas", cantCoutas.toString());
                                            ticket1.textoExtremo("Pago:", _format.decimal(_interesPago) + _mony);
                                            ticket1.textoExtremo("Cambio:", _format.decimal(_cambio) + _mony);
                                            ticket1.lineasVacia();
                                            ticket1.textoCentro("@anvimol");
                                            ticket1.print();
                                            getConn().commit();
                                            restablecerReportCliente();
                                        }
                                    } else {
                                        _label.get(18).setText("El pago debe ser " + _format.decimal(_interesesPagos) + _mony);
                                    }
                                } else {
                                    _label.get(18).setText("Cuotas inválidas");
                                }
                            } catch (Exception e) {
                                getConn().rollback();
                                JOptionPane.showMessageDialog(null, e);
                            }
                        } else {
                            _label.get(18).setText("Ingrese el número de cuotas");
                        }
                    } else {
                        _label.get(18).setText("El cliente no tiene deuda");
                    }
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
            //formateador = new SimpleDateFormat("yyyy/MM/dd");
            var cal = new Calendario();
            String[] titulos = {"Id", "Deuda", "Saldo", "Pago", "Cambio",
                "Fecha", "Ticket", "Fecha deuda", "Mensual", "Fecha limite"};
            modelo4 = new DefaultTableModel(null, titulos);
            var date1 = formateador.parse(_dateChooser1.getSelectedPeriodSet().toString());
            var date2 = formateador.parse(_dateChooser2.getSelectedPeriodSet().toString());
            if (filtrar) {
                if (date2.after(date1) || date2.equals(date1)) {
                    var listPagos1 = new ArrayList<TPagos_clientes>();
                    var pagos = pagosClientes().stream()
                            .filter(u -> u.getIdCliente() == _idClienteRport)
                            .collect(Collectors.toList());

                    var date3 = _dateChooser1.getCurrent();
                    var date4 = formateador.parse(cal.getFecha(date3));

                    for (TPagos_clientes pago : pagos) {
                        var data = pago.getFecha().toString().replace('-', '/');
                        var date5 = formateador.parse(data);
                        if (date4.equals(date5) || date4.before(date5)) {
                            listPagos1.add(pago);
                        }
                    }
                    var date6 = _dateChooser2.getCurrent();
                    var date7 = formateador.parse(cal.getFecha(date6));
                    for (TPagos_clientes pago : listPagos1) {
                        var data = pago.getFecha().toString().replace('-', '/');
                        var date8 = formateador.parse(data);
                        if (date7.equals(date8) || date7.after(date8)) {
                            Object[] registros = {
                                pago.getId(),
                                _format.decimal(pago.getDeuda()),
                                _format.decimal(pago.getSaldo()),
                                _format.decimal(pago.getPago()),
                                _format.decimal(pago.getCambio()),
                                pago.getFecha(),
                                pago.getTicket(),
                                pago.getFechaDeuda(),
                                _format.decimal(pago.getMensual()),
                                pago.getFechaLimite()
                            };
                            modelo4.addRow(registros);
                            listPagos.add(pago);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "La fecha final debe ser "
                            + "mayor a la fecha inicial");
                }
            } else {
                int inicio = (_num_pagina - 1) * _reg_por_pagina;
                var pagos = pagosClientes().stream()
                        .filter(u -> u.getIdCliente() == _idClienteRport)
                        .collect(Collectors.toList());
                listPagos = pagos;
                var data = pagos.stream()
                        .skip(inicio).limit(_reg_por_pagina)
                        .collect(Collectors.toList());

                Collections.reverse(listPagos);
                Collections.reverse(data);
                for (TPagos_clientes pago : data) {
                    Object[] registros = {
                        pago.getId(),
                        _format.decimal(pago.getDeuda()),
                        _format.decimal(pago.getSaldo()),
                        _format.decimal(pago.getPago()),
                        _format.decimal(pago.getCambio()),
                        pago.getFecha(),
                        pago.getTicket(),
                        pago.getFechaDeuda(),
                        _format.decimal(pago.getMensual()),
                        pago.getFechaLimite()
                    };
                    modelo4.addRow(registros);
                }
            }
        } catch (Exception ex) {
            var data = ex.getMessage();
            System.out.println(data);
        }
        _tablaPagosCuotas.setModel(modelo4);
        _tablaPagosCuotas.setRowHeight(30);
        _tablaPagosCuotas.getColumnModel().getColumn(0).setMaxWidth(0);
        _tablaPagosCuotas.getColumnModel().getColumn(0).setMinWidth(0);
        _tablaPagosCuotas.getColumnModel().getColumn(0).setPreferredWidth(0);
        _tablaPagosCuotas.getColumnModel().getColumn(6).setMaxWidth(0);
        _tablaPagosCuotas.getColumnModel().getColumn(6).setMinWidth(0);
        _tablaPagosCuotas.getColumnModel().getColumn(6).setPreferredWidth(0);
        _tablaPagosCuotas.getColumnModel().getColumn(7).setMaxWidth(0);
        _tablaPagosCuotas.getColumnModel().getColumn(7).setMinWidth(0);
        _tablaPagosCuotas.getColumnModel().getColumn(7).setPreferredWidth(0);
        _tablaPagosCuotas.getColumnModel().getColumn(8).setMaxWidth(0);
        _tablaPagosCuotas.getColumnModel().getColumn(8).setMinWidth(0);
        _tablaPagosCuotas.getColumnModel().getColumn(8).setPreferredWidth(0);
    }

    public void getHistorialPago() {
        formateador = new SimpleDateFormat("yyyy/MM/dd");
        int fila = _tablaPagosCuotas.getSelectedRow();
        _idHistorial = (Integer) modelo4.getValueAt(fila, 0);
        var deuda = (String) modelo4.getValueAt(fila, 1) + _mony;
        _label.get(21).setText(deuda);
        var saldo = (String) modelo4.getValueAt(fila, 2) + _mony;
        _label.get(22).setText(saldo);
        var fechaDeuda = (Date) modelo4.getValueAt(fila, 7);
        _label.get(23).setText(fechaDeuda.toString());
        var ticket = (String) modelo4.getValueAt(fila, 6);
        _label.get(24).setText(ticket);
        var pago = (String) modelo4.getValueAt(fila, 3) + _mony;
        _label.get(25).setText(pago);
        var mensual = (String) modelo4.getValueAt(fila, 8) + _mony;
        _label.get(26).setText(mensual);
        var fechaPago = (Date) modelo4.getValueAt(fila, 5);
        _label.get(27).setText(fechaPago.toString());
        var fechaLimite = (Date) modelo4.getValueAt(fila, 9);
        _label.get(28).setText(fechaLimite.toString());
        var cambio = (String) modelo4.getValueAt(fila, 4) + _mony;
        var usuario = _dataUsuario.getNombre();

        ticket1.textoCentro("Sistema de ventas AVM");
        ticket1.lineasVacia();
        ticket1.textoIzquierda("Direccion");
        ticket1.textoIzquierda("Avda La Paz, 3 2ºB");
        ticket1.textoIzquierda("Tel 665151716");
        ticket1.lineasGuion();
        ticket1.textoCentro("FACTURA");
        ticket1.lineasGuion();
        ticket1.textoIzquierda("Factura: " + ticket);
        ticket1.textoIzquierda("Cliente: " + nameCliente);
        ticket1.textoIzquierda("Fecha: " + fechaPago);
        ticket1.textoIzquierda("Usuario: " + usuario);
        ticket1.lineasGuion();
        ticket1.textoCentro("Su crédito " + deuda);
        ticket1.textoExtremo("Cuotas por mes", mensual);
        ticket1.textoExtremo("Pago:", pago);
        ticket1.textoExtremo("Cambio:", cambio);
        ticket1.textoExtremo("Deuda actual:", saldo);
        ticket1.textoExtremo("Próximo pago:", fechaLimite.toString());
        ticket1.lineasVacia();
        ticket1.textoCentro("@anvimol");
    }

    public void ticketDeuda() {
        switch (_seccion1) {
            case 1:
                if (_idHistorial == 0) {
                    JOptionPane.showMessageDialog(null, "Seleccione un historial de pago");
                } else {
                    ticket1.print();
                }
                break;
            case 2:
                if (_idHistorial1 == 0) {
                    JOptionPane.showMessageDialog(null, "Seleccione un historial de pago de intereses");
                } else {
                    ticket1.print();
                }
                break;
        }
    }

    public void historialIntereses(boolean filtrar) {
        listPagosIntereses = new ArrayList<>();
        try {
            _dateChooser1.setFormat(3);
            _dateChooser2.setFormat(3);
            formateador = new SimpleDateFormat("yyyy/MM/dd");
            var cal = new Calendario();
            String[] titulos = {"Id", "Intereses", "Pago", "Cambio",
                "Cuota", "Fecha", "Ticket"};
            modelo5 = new DefaultTableModel(null, titulos);
            var date1 = formateador.parse(_dateChooser1.getSelectedPeriodSet().toString());
            var date2 = formateador.parse(_dateChooser2.getSelectedPeriodSet().toString());
            if (filtrar) {
                if (date2.after(date1) || date2.equals(date1)) {
                    var listPagos1 = new ArrayList<TPagos_reportes_intereses_clientes>();
                    var pagos = pagosReportesInteresesCliente().stream()
                            .filter(u -> u.getIdCliente() == _idClienteRport)
                            .collect(Collectors.toList());

                    var date3 = _dateChooser1.getCurrent();
                    var date4 = formateador.parse(cal.getFecha(date3));

                    for (TPagos_reportes_intereses_clientes pago : pagos) {
                        var data = pago.getFecha().toString().replace('-', '/');
//                        var array = data.split("/");
//                        var date5 = formateador.parse(array[2] + "/" + array[1] + "/" + array[0]);
                        var date5 = formateador.parse(data);
                        if (date4.equals(date5) || date4.before(date5)) {
                            listPagos1.add(pago);
                        }
                    }
                    var date6 = _dateChooser2.getCurrent();
                    var date7 = formateador.parse(cal.getFecha(date6));
                    for (TPagos_reportes_intereses_clientes pago : listPagos1) {
                        var data = pago.getFecha().toString().replace('-', '/');
                        var date8 = formateador.parse(data);
                        if (date7.equals(date8) || date7.after(date8)) {
                            Object[] registros = {
                                pago.getIdPagosReporInteClien(),
                                _format.decimal(pago.getIntereses()),
                                _format.decimal(pago.getPago()),
                                _format.decimal(pago.getCambio()),
                                pago.getCuotas(),
                                pago.getFecha(),
                                pago.getTicket()
                            };
                            modelo5.addRow(registros);
                            listPagosIntereses.add(pago);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "La fecha final debe ser "
                            + "mayor a la fecha inicial");
                }
            } else {
                int inicio = (_num_pagina - 1) * _reg_por_pagina;
                var pagos = pagosReportesInteresesCliente().stream()
                        .filter(u -> u.getIdCliente() == _idClienteRport)
                        .collect(Collectors.toList());

                listPagosIntereses = pagos;
                var data = pagos.stream()
                        .skip(inicio).limit(_reg_por_pagina)
                        .collect(Collectors.toList());
                Collections.reverse(listPagosIntereses);
                Collections.reverse(data);

                for (TPagos_reportes_intereses_clientes pago : data) {
                    Object[] registros = {
                        pago.getIdPagosReporInteClien(),
                        _format.decimal(pago.getIntereses()),
                        _format.decimal(pago.getPago()),
                        _format.decimal(pago.getCambio()),
                        pago.getCuotas(),
                        pago.getFecha(),
                        pago.getTicket()
                    };
                    modelo5.addRow(registros);

                }
            }
        } catch (Exception ex) {
            var data = ex.getMessage();
            System.out.println(data);
        }
        _tablaPagosIntereses.setModel(modelo5);
        _tablaPagosIntereses.setRowHeight(30);
        _tablaPagosIntereses.getColumnModel().getColumn(0).setMaxWidth(0);
        _tablaPagosIntereses.getColumnModel().getColumn(0).setMinWidth(0);
        _tablaPagosIntereses.getColumnModel().getColumn(0).setPreferredWidth(0);
    }

    public void getHistorialIntereses() {
        formateador = new SimpleDateFormat("yyyy/MM/dd");
        int fila = _tablaPagosIntereses.getSelectedRow();
        _idHistorial1 = (Integer) modelo5.getValueAt(fila, 0);
        var intereses = (String) modelo5.getValueAt(fila, 1) + _mony;
        _label.get(29).setText(intereses);
        var ticket = (String) modelo5.getValueAt(fila, 6);
        _label.get(30).setText(ticket);
        var fecha = (String) modelo5.getValueAt(fila, 5);
        _label.get(31).setText(fecha);
        var cambio = (String) modelo5.getValueAt(fila, 3) + _mony;
        _label.get(32).setText(cambio);
        var pago = (String) modelo5.getValueAt(fila, 2) + _mony;
        _label.get(33).setText(pago);
        var cuota = (int) modelo5.getValueAt(fila, 4);
        _label.get(34).setText(cuota + "");

        var usuario = _dataUsuario.getNombre();

        ticket1.textoCentro("Sistema de ventas AVM");
        ticket1.lineasVacia();
        ticket1.textoIzquierda("Direccion");
        ticket1.textoIzquierda("Avda La Paz, 3 2ºB");
        ticket1.textoIzquierda("Tel 665151716");
        ticket1.lineasGuion();
        ticket1.textoCentro("FACTURA DE PAGO DE INTERESES");
        ticket1.lineasGuion();
        ticket1.textoIzquierda("Factura: " + ticket);
        ticket1.textoIzquierda("Cliente: " + nameCliente);
        ticket1.textoIzquierda("Fecha: " + fecha);
        ticket1.textoIzquierda("Usuario: " + usuario);
        ticket1.lineasGuion();
        ticket1.textoCentro("Intereses: " + intereses);
        ticket1.textoExtremo("Cuotas:", cuota + "");
        ticket1.textoExtremo("Pago:", pago);
        ticket1.textoExtremo("Cambio:", cambio);
        ticket1.lineasVacia();
        ticket1.textoCentro("@anvimol");
    }

    public final void restablecerReportCliente() {
        _idReport = 0;
        _interesCuotas = 0;
        _intereses = 0.0;
        _interesPago = 0.0;
        _deudaActual = 0.0;
        _interesesPagos = 0.0;
        _interesesCliente = 0.0;
        _cambio = 0.0;
        _pago = 0.0;
        _mensual = 0.0;
        _deudaActualCliente = 0.0;
        _ticketCuota = "0000000000";
        _ticketIntereses = "0000000000";
        _label.get(7).setText("Nombre del Cliente");
        _label.get(8).setText("0,00" + _mony);
        _label.get(9).setText("0,00" + _mony);
        _label.get(10).setText("0000000000");
        _label.get(11).setText("--/--/----");
        _label.get(12).setText("0,00" + _mony);
        _label.get(13).setText("0,00" + _mony);
        _label.get(14).setText("0");
        _label.get(15).setText("0000000000");
        _label.get(16).setText("--/--/----");
        _label.get(17).setText("0,00" + _mony);
        _label.get(18).setText("Ingrese el pago");
        _textfield.get(5).setText("");
        _textfield.get(6).setText("");
        listReportesDeuda = clientes();
        if (!listReportesDeuda.isEmpty()) {
            _paginadorReportes = new Paginador<>(listReportesDeuda,
                    _label.get(6), _reg_por_pagina);
        }
        searchReportes("");
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="CÓDIGO DEUDAS Y REPORTES">
    private List<TReportes_clientes> _list = new ArrayList<>();

    public void getReportesDeuda(String valor) {
        _list = new ArrayList<>();
        String[] titulos = {"Id", "DNI", "Nombre", "Apellidos", "Email",
            "Teléfono", "idReporte", "Fecha Limite"};
        modelo3 = new DefaultTableModel(null, titulos);
        var inicio = (_num_pagina - 1) * _reg_por_pagina;
        List<TReportes_clientes> list = new ArrayList();
        if (valor.equals("")) {
            list = reportes_Clientes().stream()
                    .skip(inicio).limit(_reg_por_pagina)
                    .collect(Collectors.toList());
            if (!list.isEmpty()) {
                for (TReportes_clientes item : list) {
                    if (!item.getFechaLimite().equals("--/--/----")) {
                        try {
                            Date date1 = formateador.parse(item.getFechaLimite());
                            Date date2 = formateador.parse(new Calendario().getFecha());
                            long time = date1.getTime() - date2.getTime();
                            long days = time / (1000 * 60 * 60 * 24);
                            if (3 >= days) { // Cuando al cliente le falten 3 dias para que le venza la fecha limite
                                Object[] registros = {
                                    item.getId(),
                                    item.getDni(),
                                    item.getNombre(),
                                    item.getEmail(),
                                    item.getTelefono(),
                                    item.getIdReporte(),
                                    item.getFechaLimite(),
                                    item.getFechaPago(),
                                    item.getMensual(),
                                    item.getDeuda()
                                };
                                modelo3.addRow(registros);
                                if (0 > days) {
                                    interesMora(registros, days);
                                }
                                _list.add(item);
                            }
                        } catch (ParseException ex) {

                        }
                    }
                }
            }
        } else {
            list = reportes_Clientes().stream()
                    .filter(C -> C.getDni().startsWith(valor)
                    || C.getNombre().startsWith(valor))
                    .skip(inicio).limit(_reg_por_pagina)
                    .collect(Collectors.toList());
            if (!list.isEmpty()) {
                for (TReportes_clientes item : list) {
                    if (!item.getFechaLimite().equals("--/--/----")) {
                        try {
                            Date date1 = formateador.parse(item.getFechaLimite());
                            Date date2 = formateador.parse(new Calendario().getFecha());
                            long time = date1.getTime() - date2.getTime();
                            long days = time / (1000 * 60 * 60 * 24);
                            if (3 >= days) {
                                Object[] registros = {
                                    item.getId(),
                                    item.getDni(),
                                    item.getNombre(),
                                    item.getEmail(),
                                    item.getTelefono(),
                                    item.getIdReporte(),
                                    item.getFechaLimite(),
                                    item.getFechaPago(),
                                    item.getMensual(),
                                    item.getDeuda()
                                };
                                modelo3.addRow(registros);
                                _list.add(item);
                            }
                        } catch (ParseException ex) {

                        }
                    }
                }
            }
        }
        if (_tablaReporDeudaCliente != null) {
            _tablaReporDeudaCliente.setModel(modelo3);
            _tablaReporDeudaCliente.setRowHeight(30);
            _tablaReporDeudaCliente.getColumnModel().getColumn(0).setMaxWidth(0);
            _tablaReporDeudaCliente.getColumnModel().getColumn(0).setMinWidth(0);
            _tablaReporDeudaCliente.getColumnModel().getColumn(0).setPreferredWidth(0);
            _tablaReporDeudaCliente.getColumnModel().getColumn(6).setMaxWidth(0);
            _tablaReporDeudaCliente.getColumnModel().getColumn(6).setMinWidth(0);
            _tablaReporDeudaCliente.getColumnModel().getColumn(6).setPreferredWidth(0);
        }

    }

    private long diasMora;
    private DefaultTableModel _selectedCliente;
    private Date _fechaLimite;
    private Integer _idReporte;

    public void getReporteDeuda(DefaultTableModel selected, int fila) {
        if (selected != null) {
            Calendar calendar = Calendar.getInstance();
            try {
                var nombre = (String) selected.getValueAt(fila, 2);
                var apellidos = (String) selected.getValueAt(fila, 3);
                _label.get(20).setText(nombre + " " + apellidos);
                _idReporte = (Integer) selected.getValueAt(fila, 6);
                _fechaLimite = formateador.parse((String) selected.getValueAt(fila, 7));
                calendar.setTime(_fechaLimite);
                _dateChooser.setSelectedDate(calendar);
                Date date = formateador.parse(new Calendario().getFecha());
                long time = _fechaLimite.getTime() - date.getTime();
                diasMora = time / (1000 * 60 * 60 * 24);
                if (0 < diasMora) {
                    if (Math.abs(diasMora) == 1) {
                        _label.get(21).setText(diasMora + " día restante");
                    } else {
                        _label.get(21).setText(diasMora + " días restantes");
                    }
                } else {
                    if (Math.abs(diasMora) == 1) {
                        _label.get(21).setText("Se ha sobrepasado " + Math.abs(diasMora)
                                + " día");
                    } else {
                        _label.get(21).setText("Se ha sobrepasado " + Math.abs(diasMora)
                                + " días");
                    }
                }
                _selectedCliente = selected;
            } catch (Exception e) {
            }
        }
    }

    public void extenderDias() {
        if (_selectedCliente != null) {
            if (0 <= diasMora) {
                if (_chekBox_dia.isSelected()) {
                    try {
                        _dateChooser.setFormat(3); // formato del DateFormat del dateChooserCombo
                        var date1 = formateador.parse(new Calendario().getFecha());
                        var date2 = _dateChooser.getSelectedDate().getTime();
                        if (date1.before(date2) && _fechaLimite.before(date2)) {
                            final QueryRunner qr = new QueryRunner(true);
                            String query = "UPDATE treportes_clientes SET fechaLimite=?"
                                    + " WHERE idReporte = " + _idReporte;
                            Object[] data = {formateador.format(date2)};
                            qr.update(getConn(), query, data);
                            resetReportDeuda();
                        } else {
                            var fechaLimite = !date1.before(date2) ? new Calendario().getFecha() : formateador.format(_fechaLimite);
                            JOptionPane.showConfirmDialog(null, "Seleccione una fecha mayor a la fecha: " + fechaLimite
                                    + "\npara extender los dias de pago al cliente",
                                    "Fecha para extender dias", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        }
                        _dateChooser.setFormat(0);
                    } catch (Exception e) {
                    }

                } else {
                    JOptionPane.showConfirmDialog(null, "Seleccione la casilla para "
                            + "verificar que va a extender \n los dias de pago al cliente",
                            "Extender dias", JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE);
                }
            } else {
                JOptionPane.showConfirmDialog(null, "Al cliente no se le pueden extender los dias",
                        "Extender dias", JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE);
            }
        } else {
            JOptionPane.showConfirmDialog(null, "Seleccione un cliente de la lista",
                    "Extender dias", JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE);
        }
    }

    private void interesMora(Object[] cliente, long days) {
        var intereses = ConfiguracionVM.intereses;
        var id = (Integer) cliente[0];
        var fechaPago = (String) cliente[7];
        var mensual = (Double) cliente[8];
        if (!intereses.equals(0.0) || !intereses.equals(0.00)) {
            var clientesInteres1 = interesesClientes().stream()
                    .filter(i -> i.getIdCliente() == id
                    && i.getFechaInicial().equals(fechaPago))
                    .collect(Collectors.toList());

            var clientesInteres2 = interesesClientes().stream()
                    .filter(i -> i.getIdCliente() == id
                    && i.getFechaInicial().equals(fechaPago)
                    && i.isCancelado() == false)
                    .collect(Collectors.toList());
            long dias = Math.abs(days);
            Double porcentaje = intereses / 100;
            Double moratorio = mensual * porcentaje;
            //Double moratorioDia = moratorioMensual / 30;
            //Double interes = moratorioDia * dias;
            int count1 = clientesInteres1.size();
            int count2 = clientesInteres2.size();
            int pos = count2;
            pos--; // Obtengo el ultimo registro de clientesInteres2.size()
            if (count2 == 0) {
                for (int i = 1; i <= dias; i++) {
                    insert(cliente, new TIntereses_clientes(), i, false, moratorio);
                }
            } else {
                if (count1 <= dias) {
                    if (count2 <= dias) {
                        long interesDias = dias - count1;
                        for (int i = 1; i <= interesDias; i++) {
                            insert(cliente, clientesInteres2.get(pos), i, true, moratorio);
                        }
                    }
                }

            }
        } else {
            
        }
    }

    private void insert(Object[] cliente, TIntereses_clientes clientesInteres,
            int day, boolean value, Double interes) {
        Date fecha = null;
        final QueryRunner qr = new QueryRunner(true);
        var id = (Integer) cliente[0];
        var fechaLimite = (String) cliente[6];
        var fechaPago = (String) cliente[7];
        var mensual = (Double) cliente[8];
        var deuda = (Double) cliente[9];
        
        try {
            getConn().setAutoCommit(false);
            if (value) {
                fecha = formateador.parse(clientesInteres.getFecha());
            } else {
                fecha = formateador.parse(fechaLimite);
            }
            var nowDate = new Calendario().addDay(fecha, day);
            String query = "INSERT INTO tintereses_clientes(fechaInicial,deuda,"
                    + "mensual,intereses,fecha,cancelado,idCliente) "
                    + "VALUES(?,?,?,?,?,?,?)";
            Object[] data = {
                fechaPago,
                deuda,
                mensual,
                interes,
                nowDate,
                false,
                id
            };
            qr.insert(getConn(), query, new ColumnListHandler(), data);
            getConn().commit();
        } catch (Exception e) {
            try {
                getConn().rollback();
            } catch (SQLException ex) {
            }
        }
    }

    private void resetReportDeuda() {
        diasMora = 0;
        _label.get(20).setText("Cliente");
        _label.get(21).setText("Días");
        Calendar c = new GregorianCalendar();
        _dateChooser.setSelectedDate(c);
        getReportesDeuda("");
        if (!_list.isEmpty()) {
            _paginadorReportesDeuda = new Paginador<>(_list, _label.get(7), _reg_por_pagina);
        }
    }

    // </editor-fold>
    public void paginador(String metodo) {
        switch (metodo) {
            case "Primero":
                switch (seccion) {
                    case 1:
                        if (!listClientes.isEmpty()) {
                            _num_pagina = _paginadorClientes.primero();
                        }
                        break;
                    case 2:
                        switch (_seccion1) {
                            case 0:
                                if (!listReportesDeuda.isEmpty()) {
                                    _num_pagina = _paginadorReportes.primero();
                                }
                                break;
                            case 1:
                                if (!listPagos.isEmpty()) {
                                    _num_pagina = _paginadorPagos.primero();
                                }
                                break;
                            case 2:
                                if (!listPagosIntereses.isEmpty()) {
                                    _num_pagina = _paginadorPagosIntereses.primero();
                                }
                                break;
                        }
                        break;
                    case 3:
                        if (!_list.isEmpty()) {
                            _num_pagina = _paginadorReportesDeuda.primero();
                        }
                        break;
                }
                break;
            case "Anterior":
                switch (seccion) {
                    case 1:
                        if (!listClientes.isEmpty()) {
                            _num_pagina = _paginadorClientes.anterior();
                        }
                        break;
                    case 2:
                        switch (_seccion1) {
                            case 0:
                                if (!listReportesDeuda.isEmpty()) {
                                    _num_pagina = _paginadorReportes.anterior();
                                }
                                break;
                            case 1:
                                if (!listPagos.isEmpty()) {
                                    _num_pagina = _paginadorPagos.anterior();
                                }
                                break;
                            case 2:
                                if (!listPagosIntereses.isEmpty()) {
                                    _num_pagina = _paginadorPagosIntereses.anterior();
                                }
                                break;
                        }
                        break;
                    case 3:
                        if (!_list.isEmpty()) {
                            _num_pagina = _paginadorReportesDeuda.anterior();
                        }
                        break;
                }
                break;
            case "Siguiente":
                switch (seccion) {
                    case 1:
                        if (!listClientes.isEmpty()) {
                            _num_pagina = _paginadorClientes.siguiente();
                        }
                        break;
                    case 2:
                        switch (_seccion1) {
                            case 0:
                                if (!listReportesDeuda.isEmpty()) {
                                    _num_pagina = _paginadorReportes.siguiente();
                                }
                                break;
                            case 1:
                                if (!listPagos.isEmpty()) {
                                    _num_pagina = _paginadorPagos.siguiente();
                                }
                                break;
                            case 2:
                                if (!listPagosIntereses.isEmpty()) {
                                    _num_pagina = _paginadorPagosIntereses.siguiente();
                                }
                                break;
                        }
                        break;
                    case 3:
                        if (!_list.isEmpty()) {
                            _num_pagina = _paginadorReportesDeuda.siguiente();
                        }
                        break;
                }
                break;
            case "Ultimo":
                switch (seccion) {
                    case 1:
                        if (!listClientes.isEmpty()) {
                            _num_pagina = _paginadorClientes.ultimo();
                        }
                        break;
                    case 2:
                        switch (_seccion1) {
                            case 0:
                                if (!listReportesDeuda.isEmpty()) {
                                    _num_pagina = _paginadorReportes.ultimo();
                                }
                                break;
                            case 1:
                                if (!listPagos.isEmpty()) {
                                    _num_pagina = _paginadorPagos.ultimo();
                                }
                                break;
                            case 2:
                                if (!listPagosIntereses.isEmpty()) {
                                    _num_pagina = _paginadorPagosIntereses.ultimo();
                                }
                                break;
                        }
                        break;
                    case 3:
                        if (!_list.isEmpty()) {
                            _num_pagina = _paginadorReportesDeuda.ultimo();
                        }
                        break;
                }
                break;
        }

        switch (seccion) {
            case 1:
                searchCliente("");
                break;
            case 2:
                switch (_seccion1) {
                    case 0:
                        searchReportes("");
                        break;
                    case 1:
                        historialPagos(false);
                        break;
                    case 2:
                        historialIntereses(false);
                        break;
                }
                break;
            case 3:
                getReportesDeuda("");
                break;
        }
    }

    public void registroPaginas() {
        _num_pagina = 1;
        Number caja = (Number) _spinnerPaginas.getValue();
        _reg_por_pagina = caja.intValue();
        switch (seccion) {
            case 1:
                if (!listClientes.isEmpty()) {
                    _paginadorClientes = new Paginador<>(listClientes,
                            _label.get(6), _reg_por_pagina);
                }
                searchCliente("");
                break;
            case 2:
                switch (_seccion1) {
                    case 0:
                        if (!listReportesDeuda.isEmpty()) {
                            _paginadorReportes = new Paginador<>(listReportesDeuda,
                                    _label.get(6), _reg_por_pagina);
                        }
                        searchReportes("");
                        break;
                    case 1:
                        if (!listPagos.isEmpty()) {
                            _paginadorPagos = new Paginador<>(listPagos,
                                    _label.get(6), _reg_por_pagina);
                        }
                        historialPagos(false);
                        break;
                    case 2:
                        if (!listPagosIntereses.isEmpty()) {
                            _paginadorPagosIntereses = new Paginador<>(listPagosIntereses,
                                    _label.get(6), _reg_por_pagina);
                        }
                        historialIntereses(false);
                        break;
                }
                break;
            case 3:
                if (!_list.isEmpty()) {
                    _paginadorReportesDeuda = new Paginador<>(_list,
                            _label.get(6), _reg_por_pagina);
                }
                getReportesDeuda("");
                break;
        }
    }

}
