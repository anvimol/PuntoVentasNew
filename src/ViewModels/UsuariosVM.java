/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ViewModels;

import Conexion.Consult;
import Libreria.*;
import Models.Usuario.TRoles;
import Models.Usuario.TUsuarios;
import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
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
 * @author avice
 */
public class UsuariosVM extends Consult {

    private static TUsuarios _dataUsuario;
    private static JLabel _nombrePerfil;
    private static JLabel _picturePerfil;
    private JLabel _imagePicture;
    private List<JLabel> _label;
    private List<JTextField> _textField;
    private JCheckBox _checkBoxState;
    private JComboBox _comboBoxRole;
    private JTable _tableUser;
    private JSpinner _spinnerUsers;
    private String _accion = "insert";
    private UploadImage _uploadimage = new UploadImage();
    private TextFieldEvent evento = new TextFieldEvent();
    private DefaultTableModel modelo1;
    private int _reg_por_pagina = 10, _num_pagina = 1, _seccion = 1;
    private Paginador<TUsuarios> _paginadorUsuarios;
    private List<TUsuarios> listUsuarios;
    private int _idUsuario = 0;

    public UsuariosVM(TUsuarios dataUsuario, Object[] perfil) {
        _dataUsuario = dataUsuario;
        _nombrePerfil = (JLabel) perfil[0];
        _picturePerfil = (JLabel) perfil[1];
        perfil();
    }

    public UsuariosVM(Object[] objects, List<JTextField> textField, List<JLabel> label) {
        _textField = textField;
        _label = label;
        _imagePicture = (JLabel) objects[0];
        _checkBoxState = (JCheckBox) objects[1];
        _tableUser = (JTable) objects[2];
        _spinnerUsers = (JSpinner) objects[3];
        _comboBoxRole = (JComboBox) objects[4];
        restablecer();
    }

    private void perfil() {
        _nombrePerfil.setText(_dataUsuario.getNombre());
        if (null != _dataUsuario.getImagen()) {
            _uploadimage.byteImage(_picturePerfil, _dataUsuario.getImagen(), 43, 55);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="CÓDIGO REGISTRAR USUARIOS">
    public void registrarUsuario() {
        if (validarDatos()) {
            int count;
            var listEmail = usuarios().stream()
                    .filter(u -> u.getEmail().equals(_textField.get(3).getText()))
                    .collect(Collectors.toList());
            count = listEmail.size();
            var listDni = usuarios().stream()
                    .filter(u -> u.getDni().equals(_textField.get(0).getText()))
                    .collect(Collectors.toList());
            count += listDni.size();
            try {
                switch (_accion) {
                    case "insert":
                        if (count == 0) {
                            saveData();
                        } else {
                            if (!listEmail.isEmpty()) { // 0 < listEmail.size()
                                _label.get(3).setText("El email ya esta registrado");
                                _label.get(3).setForeground(Color.RED);
                                _textField.get(3).requestFocus();
                            }
                            if (!listDni.isEmpty()) {
                                _label.get(0).setText("El DNI ya esta registrado");
                                _label.get(0).setForeground(Color.RED);
                                _textField.get(0).requestFocus();
                            }
                        }
                        break;
                    case "update":
                        if (count == 2) {
                            if (listEmail.get(0).getIdUsuario() == _idUsuario
                                    && listDni.get(0).getIdUsuario() == _idUsuario) {
                                saveData();
                            } else {
                                if (listDni.get(0).getIdUsuario() != _idUsuario) {
                                    _label.get(0).setText("El DNI ya esta registrado");
                                    _label.get(0).setForeground(Color.RED);
                                    _textField.get(0).requestFocus();
                                }
                                if (listEmail.get(0).getIdUsuario() != _idUsuario) {
                                    _label.get(3).setText("El email ya esta registrado");
                                    _label.get(3).setForeground(Color.RED);
                                    _textField.get(3).requestFocus();
                                }
                            }
                        } else {
                            if (count == 0) {
                                saveData();
                            } else {
                                if (!listDni.isEmpty()) {
                                    if (listDni.get(0).getIdUsuario() == _idUsuario) {
                                        saveData();
                                    } else {
                                        _label.get(0).setText("El DNI ya esta registrado");
                                        _label.get(0).setForeground(Color.RED);
                                        _textField.get(0).requestFocus();
                                    }
                                }
                                if (!listEmail.isEmpty()) {
                                    if (listEmail.get(0).getIdUsuario() == _idUsuario) {
                                        saveData();
                                    } else {
                                        _label.get(3).setText("El email ya esta registrado");
                                        _label.get(3).setForeground(Color.RED);
                                        _textField.get(3).requestFocus();
                                    }
                                }
                            }
                        }
                        break;
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    private void saveData() throws SQLException {
        try {
            final QueryRunner qr = new QueryRunner(true);
            getConn().setAutoCommit(false);
            byte[] image = UploadImage.getImageByte();
            if (image == null) {
                image = Objetos.uploadImage.getTransFoto(_imagePicture);
            }
            var role = (TRoles) _comboBoxRole.getSelectedItem();
            switch (_accion) {
                case "insert":
                    String sqlUser1 = "INSERT INTO tusuarios(dni,nombre,email,"
                            + "telefono,direccion,usuario,password,role,imagen,"
                            + "isActive,estado,fecha) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
                    
                    Object[] dataUser1 = {
                        _textField.get(0).getText(),
                        _textField.get(1).getText(),
                        _textField.get(2).getText(),
                        _textField.get(3).getText(),
                        _textField.get(4).getText(),
                        _textField.get(5).getText(),
                        Encriptar.encrypt(_textField.get(6).getText()),
                        "Admin",//role.getRole(),
                        image,
                        true,
                        _checkBoxState.isSelected(),
                        new Date()
                    };
                    qr.insert(getConn(), sqlUser1, new ColumnListHandler(), dataUser1);
                    break;
                case "update":
                    String sqlUser2 = "UPDATE tusuarios SET dni=?,nombre=?,"
                            + "email=?,telefono=?,direccion=?,usuario=?,role=?,"
                            + "imagen=?,estado=? WHERE idUsuario=" + _idUsuario;
                    Object[] dataUser2 = {
                        _textField.get(0).getText(),
                        _textField.get(1).getText(),
                        _textField.get(2).getText(),
                        _textField.get(3).getText(),
                        _textField.get(4).getText(),
                        _textField.get(5).getText(),
                        //Encriptar.encrypt(_textField.get(6).getText()),
                        role.getRole(),
                        image,
                        _checkBoxState.isSelected()
                    };
                    qr.update(getConn(), sqlUser2, dataUser2);
                    break;
            }
            getConn().commit();
            restablecer();
        } catch (Exception e) {
            getConn().rollback();
            System.out.println(e.getMessage());
        }
    }

    public void searchUsuarios(String campo) {
        List<TUsuarios> usuariosFilter;
        String[] titulos = {"Id", "Dni", "Nombre", "Email", "Teléfono",
            "Dirección", "Usuario", "Role", "Estado", "Image"};
        modelo1 = new DefaultTableModel(null, titulos);
        int inicio = (_num_pagina - 1) * _reg_por_pagina;
        if (campo.isEmpty()) {
            usuariosFilter = usuarios().stream()
                    .skip(inicio).limit(_reg_por_pagina)
                    .collect(Collectors.toList());
        } else {
            usuariosFilter = usuarios().stream()
                    .filter(u -> u.getDni().startsWith(campo)
                    || u.getNombre().startsWith(campo))
                    .skip(inicio).limit(_reg_por_pagina)
                    .collect(Collectors.toList());
        }
        if (!usuariosFilter.isEmpty()) {
            usuariosFilter.forEach(item -> {
                Object[] registros = {
                    item.getIdUsuario(),
                    item.getDni(),
                    item.getNombre(),
                    item.getEmail(),
                    item.getTelefono(),
                    item.getDireccion(),
                    item.getUsuario(),
                    item.getRole(),
                    item.isEstado(),
                    item.getImagen()
                };
                modelo1.addRow(registros);
            });
        }
        _tableUser.setModel(modelo1);
        _tableUser.setRowHeight(30);
        _tableUser.getColumnModel().getColumn(0).setMaxWidth(0);
        _tableUser.getColumnModel().getColumn(0).setMinWidth(0);
        _tableUser.getColumnModel().getColumn(0).setPreferredWidth(0);
        _tableUser.getColumnModel().getColumn(9).setMaxWidth(0);
        _tableUser.getColumnModel().getColumn(9).setMinWidth(0);
        _tableUser.getColumnModel().getColumn(9).setPreferredWidth(0);
        _tableUser.getColumnModel().getColumn(8).setCellRenderer(new Render_CheckBox());
    }

    public void getUsuario() {
        _accion = "update";
        int fila = _tableUser.getSelectedRow();
        _idUsuario = (Integer) modelo1.getValueAt(fila, 0);
        _textField.get(0).setText((String) modelo1.getValueAt(fila, 1));
        _textField.get(1).setText((String) modelo1.getValueAt(fila, 2));
        _textField.get(2).setText((String) modelo1.getValueAt(fila, 3));
        _textField.get(3).setText((String) modelo1.getValueAt(fila, 4));
        _textField.get(4).setText((String) modelo1.getValueAt(fila, 5));
        _textField.get(5).setText((String) modelo1.getValueAt(fila, 6));
        _textField.get(6).setText("************");
        _textField.get(6).setEnabled(false);
        var model = new DefaultComboBoxModel();
        var role = new TRoles();
        var rol = (String) modelo1.getValueAt(fila, 7);
        role.setRole(rol);
        model.addElement(role);
        roles().forEach(item -> {
            if (!rol.equals(item.getRole())) {
                model.addElement(item);
            }
        });
        _comboBoxRole.setModel(model);
        _checkBoxState.setSelected((Boolean) modelo1.getValueAt(fila, 8));
        byte[] image = (byte[]) modelo1.getValueAt(fila, 9);
        if (image != null) {
            Objetos.uploadImage.byteImage(_imagePicture, image, 255, 329);
        }
    }

    public void restablecer() {
        _seccion = 1;
        _accion = "insert";
        for (int i = 0; i < _textField.size(); i++) {
            _textField.get(i).setText("");
        }
        _textField.get(6).setEnabled(true);
        _checkBoxState.setSelected(false);
        _comboBoxRole.setForeground(new Color(70, 106, 124));
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
        _label.get(5).setText("Usuario");
        _label.get(5).setForeground(new Color(70, 106, 124));
        _label.get(6).setText("Contraseña");
        _label.get(6).setForeground(new Color(70, 106, 124));
        _imagePicture.setIcon(new ImageIcon(getClass().getClassLoader()
                .getResource("Resources/iconos-clientes-png.png")));
        
        getRoles();
        
        listUsuarios = usuarios();
        if (!listUsuarios.isEmpty()) {
            _paginadorUsuarios = new Paginador<>(listUsuarios,
                    _label.get(8), _reg_por_pagina);
        }
        searchUsuarios("");
        
        SpinnerNumberModel model = new SpinnerNumberModel(
                10.0, // Dato visualizado al inicio del spinner
                1.0, // Limite inferior
                100.0, // Limite superior
                1.0); //incremento-decremento
//        crearTabla();
        _spinnerUsers.setModel(model);
    }

    public void getRoles() {
        var model = new DefaultComboBoxModel();
        roles().forEach(item -> {
            model.addElement(item);
        });
        _comboBoxRole.setModel(model);
    }

    public void Paginador(String metodo) {
        switch (metodo) {
            case "Primero":
                switch (_seccion) {
                    case 1:
                        if (!listUsuarios.isEmpty()) {
                            _num_pagina = _paginadorUsuarios.primero();
                        }
                        break;
                }
                break;
            case "Anterior":
                switch (_seccion) {
                    case 1:
                        if (!listUsuarios.isEmpty()) {
                            _num_pagina = _paginadorUsuarios.anterior();
                        }
                        break;
                }
                break;
            case "Siguiente":
                switch (_seccion) {
                    case 1:
                        if (!listUsuarios.isEmpty()) {
                            _num_pagina = _paginadorUsuarios.siguiente();
                        }
                        break;
                }
                break;
            case "Ultimo":
                switch (_seccion) {
                    case 1:
                        if (!listUsuarios.isEmpty()) {
                            _num_pagina = _paginadorUsuarios.ultimo();
                        }
                        break;
                }
                break;
        }
        switch (_seccion) {
            case 1:
                searchUsuarios("");
                break;
        }
    }

    public void registroPaginas() {
        _num_pagina = 1;
        Number value = (Number) _spinnerUsers.getValue();
        _reg_por_pagina = value.intValue();
        switch (_seccion) {
            case 1:
                if (!listUsuarios.isEmpty()) {
                    _paginadorUsuarios = new Paginador<>(listUsuarios,
                            _label.get(8), _reg_por_pagina);
                }
                searchUsuarios("");
                break;
        }
    }

    private boolean validarDatos() {
        if (_textField.get(0).getText().isEmpty()) {
            _label.get(0).setText("Ingrese el DNI del usuario");
            _label.get(0).setForeground(Color.RED);
            _textField.get(0).requestFocus();
            return false;
        } else if (_textField.get(1).getText().isEmpty()) {
            _label.get(1).setText("Ingrese el nombre del usuario");
            _label.get(1).setForeground(Color.RED);
            _textField.get(1).requestFocus();
            return false;
        } else if (_textField.get(2).getText().isEmpty()) {
            _label.get(2).setText("Ingrese el email del usuario");
            _label.get(2).setForeground(Color.RED);
            _textField.get(2).requestFocus();
            return false;
        } else if (!Objetos.eventos.isEmail(_textField.get(2).getText())) {
            _label.get(2).setText("El email no es correcto");
            _label.get(2).setForeground(Color.RED);
            _textField.get(2).requestFocus();
            return false;
        } else if (_textField.get(3).getText().isEmpty()) {
            _label.get(3).setText("Ingrese el teléfono del usuario");
            _label.get(3).setForeground(Color.RED);
            _textField.get(3).requestFocus();
            return false;
        } else if (_textField.get(4).getText().isEmpty()) {
            _label.get(4).setText("Ingrese la dirección del usuario");
            _label.get(4).setForeground(Color.RED);
            _textField.get(4).requestFocus();
            return false;
        } else if (_textField.get(5).getText().isEmpty()) {
            _label.get(5).setText("Ingrese el login del usuario");
            _label.get(5).setForeground(Color.RED);
            _textField.get(5).requestFocus();
            return false;
        } else if (_textField.get(6).getText().isEmpty()) {
            _label.get(6).setText("Ingrese la contraseña del usuario");
            _label.get(6).setForeground(Color.RED);
            _textField.get(6).requestFocus();
            return false;
        }
        return true;
    }

//    public void reportUsuario() throws IOException, JRException {
//        InputStream rutaInforme = getClass().getResourceAsStream("/Reportes/usuarios.jasper");
//        //String rutaInforme = "src\\Reportes\\usuarios.jasper";
//        
//        JasperPrint print = JasperFillManager.fillReport(rutaInforme, null, getConn());
//        JasperViewer ver = new JasperViewer(print, false);
//        ver.setTitle("Listado de usuarios");
//        ver.setVisible(true);
//    }
    // </editor-fold> 

}
