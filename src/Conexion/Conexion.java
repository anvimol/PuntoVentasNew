/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author antonio
 */
public class Conexion {

    private String db = "punto_de_ventas";
    private String user = "root";
    private String password = "";
    private String url = "jdbc:mysql://localhost/" + db + "?&useSSL=false";// ?SslMode=none
    private Connection conn = null;

    public Conexion() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(this.url, this.user, this.password);
            if (conn != null) {
                System.out.println("Conexion a la base de datos " + this.db + ".... Listo ");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error: " + e);
        }
    }

    public Connection getConn() {
        return conn;
    }

    public void desconectar() {
        try {
            conn.close();
        } catch (SQLException ex) {
        }
    }
}
