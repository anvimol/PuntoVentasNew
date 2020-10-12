/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ViewModels;

import Conexion.Consult;
import Libreria.FormatDecimal;
import Models.Configuracion.TConfiguracion;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

/**
 *
 * @author AJPDHN
 */
public class ConfiguracionVM extends Consult {

    public static String Mony;
    public static Double intereses;
    private static List<JRadioButton> _radio;
    private List<JTextField> _textField;
    private List<JLabel> _label;
    private FormatDecimal _format;
    private String sqlConfig;

    public ConfiguracionVM() {
        TypeMoney();
    }

    public ConfiguracionVM(List<JRadioButton> radio) {
        _radio = radio;
        RadioEvent();
        TypeMoney();
        restablecer();
    }

    public ConfiguracionVM(List<JRadioButton> radio, List<JTextField> texfield,
            List<JLabel> label) {
        _radio = radio;
        _textField = texfield;
        _label = label;
        _format = new FormatDecimal();
        RadioEvent();
        TypeMoney();
        restablecer();
    }

    private void RadioEvent() {
        _radio.get(0).addActionListener((ActionEvent e) -> {
            TypeMoney("€", _radio.get(0).isSelected());
        });
        _radio.get(1).addActionListener((ActionEvent e) -> {
            TypeMoney("$", _radio.get(1).isSelected());
        });
    }

    private void TypeMoney() {
        sqlConfig = "INSERT INTO tconfiguracion(typeMoney) VALUES(?)";

        List<TConfiguracion> config = config();
        final QueryRunner qr = new QueryRunner(true);

        if (config.isEmpty()) {
            Mony = "€";
            Object[] dataConfig = {Mony};
            try {
                qr.insert(getConn(), sqlConfig, new ColumnListHandler(), dataConfig);
            } catch (SQLException ex) {
            }
        } else {
            TConfiguracion data = config.get(0);
            Mony = data.getTypeMoney();
            switch (Mony) {
                case "€":
                    _radio.get(0).setSelected(true);
                    break;
                case "$":
                    _radio.get(1).setSelected(true);
                    break;
            }
        }
    }

    private void TypeMoney(String typeMoney, boolean valor) {
        final QueryRunner qr = new QueryRunner(true);
        if (valor) {
            try {
                List<TConfiguracion> config = config();
                if (config.isEmpty()) {
                    sqlConfig = "INSERT INTO tconfiguracion(typeMoney) VALUES(?)";
                    Mony = typeMoney;
                    Object[] dataConfig = {Mony};

                    qr.insert(getConn(), sqlConfig, new ColumnListHandler(), dataConfig);

                } else {
                    TConfiguracion data = config.get(0);
                    sqlConfig = "UPDATE tconfiguracion SET typeMoney = ? WHERE id =" + data.getId();

                    if (data.getTypeMoney().equals(typeMoney)) {
                        Mony = typeMoney;
                    } else {
                        Mony = typeMoney;
                        Object[] dataConfig = {Mony};
                        qr.update(getConn(), sqlConfig, dataConfig);
                    }
                }
            } catch (SQLException e) {
            }
        }
    }

    public void registrarIntereses() {
        if (_textField.get(0).getText().equals("")) {
            _label.get(0).setText("Ingrese el interés");
            _label.get(0).setForeground(Color.RED);
            _textField.get(0).requestFocus();
        } else {
            if (_radio.get(2).isSelected()) {
                try {
                    final QueryRunner qr = new QueryRunner(true);
                    var _tconfiguration = config();
                    if (_tconfiguration.isEmpty()) {
                        sqlConfig = "INSERT INTO tconfiguracion(typeMoney,intereses) VALUES(?,?)";
                        Object[] dataConfig = {"€", _format.reconstruir(_textField.get(0).getText())};
                        qr.insert(getConn(), sqlConfig, new ColumnListHandler(), dataConfig);
                    } else {
                        var data = _tconfiguration.get(0);
                        sqlConfig = "UPDATE tconfiguracion SET typeMoney = ?,"
                                + "intereses = ? WHERE id =" + data.getId();
                        Object[] dataConfig = {
                            data.getTypeMoney(),
                            _format.reconstruir(_textField.get(0).getText())
                        };
                        qr.update(getConn(), sqlConfig, dataConfig);
                    }
                    restablecer();
                } catch (Exception e) {
                }
            } else {
                _label.get(0).setText("Seleccione la opcion intereses");
                _label.get(0).setForeground(Color.RED);
            }
        }
    }

    private void restablecer() {
        var _tconfiguration = config();
        if (!_tconfiguration.isEmpty()) {
            var data = _tconfiguration.get(0);
            intereses = data.getIntereses();
            if (_label != null) {
                var interes = data.getIntereses() == 0.00 ? "0.00%" : data.getIntereses() + "%";
                _label.get(1).setText(interes);
                _textField.get(0).setText("");
                _label.get(0).setText("");
                _radio.get(0).setSelected(false);
            }
        }
    }
}
