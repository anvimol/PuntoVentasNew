/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ViewModels;

import Conexion.Consult;
import Libreria.*;
import Models.Usuario.TUsuarios;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.sql.SQLException;
import java.util.Date;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

/**
 *
 * @author avice
 */
public class LoginVM extends Consult {

    private ArrayList<JLabel> _label;
    private ArrayList<JTextField> _textfield;
    private List<TUsuarios> listUsuarios;

    public LoginVM(ArrayList<JLabel> label, ArrayList<JTextField> textfield) {
        _label = label;
        _textfield = textfield;
    }

    public LoginVM() {
    }

    public Object[] login() throws SQLException {
        if (_textfield.get(0).getText().isEmpty()) {
            _label.get(0).setText("Ingrese el Usuario");
            _label.get(0).setForeground(Color.RED);
            _textfield.get(0).requestFocus();
        } else if (!Objetos.eventos.isEmail(_textfield.get(0).getText())) {
            _label.get(0).setText("Ingrese un email valido");
            _label.get(0).setForeground(Color.RED);
            _textfield.get(0).requestFocus();
        } else if (_textfield.get(1).getText().isEmpty()) {
            _label.get(1).setText("Ingrese la Contraseña");
            _label.get(1).setForeground(Color.RED);
            _textfield.get(1).requestFocus();
        } else {
            listUsuarios = usuarios().stream()
                    .filter(u -> u.getEmail().equals(_textfield.get(0).getText()))
                    .collect(Collectors.toList());
            if (!listUsuarios.isEmpty()) {
                try {
                    if (listUsuarios.get(0).isEstado()) {
                        var password = Encriptar.decrypt(listUsuarios.get(0).getPassword());
                        if (password.equals(_textfield.get(1).getText())) {
                            final QueryRunner qr = new QueryRunner(true);

                            Object[] usuario = {true};
                            String sql1 = "UPDATE tusuarios SET isActive=? WHERE "
                                    + "idUsuario=" + listUsuarios.get(0).getIdUsuario();
                            qr.update(getConn(), sql1, usuario);

                        } else {
                            _label.get(1).setText("Contraseña incorrecta");
                            _label.get(1).setForeground(Color.RED);
                            _textfield.get(1).requestFocus();
                            listUsuarios.clear();
                        }
                    } else {
                        listUsuarios.clear();
                        JOptionPane.showConfirmDialog(null, "El usuario no esta"
                                + " disponible", "Estado", JOptionPane.YES_OPTION,
                                JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            } else {
                _label.get(0).setText("El email no esta registrado");
                _label.get(0).setForeground(Color.RED);
                _textfield.get(0).requestFocus();
                listUsuarios.clear();
            }
        }
        Object[] objects = {listUsuarios};
        return objects;
    }

}
