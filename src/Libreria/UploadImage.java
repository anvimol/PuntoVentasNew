/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Libreria;

import Conexion.Conexion;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author antonio
 */
public class UploadImage extends javax.swing.JFrame {

    private File archivo;
    private JFileChooser abrirArchivo;
    private static String urlOrigen = null;
    private static byte[] imageByte = null;

    public static byte[] getImageByte() {
        return imageByte;
    }

    public void cargarImagen(JLabel label, int ancho, int alto) {
        abrirArchivo = new JFileChooser();
        abrirArchivo.setFileFilter(new FileNameExtensionFilter("Archivos de Imagen", "jpg", "jpeg", "png", "gif"));
        int respuesta = abrirArchivo.showOpenDialog(this);
        if (respuesta == JFileChooser.APPROVE_OPTION) {
            archivo = abrirArchivo.getSelectedFile();
            urlOrigen = archivo.getAbsolutePath();
            Image foto = getToolkit().getImage(urlOrigen);
            foto = foto.getScaledInstance(ancho, alto, 1);
            label.setIcon(new ImageIcon(foto));
            try {
                BufferedImage bImage = ImageIO.read(archivo);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ImageIO.write(bImage, "png", bos);
                imageByte = bos.toByteArray();
            } catch (IOException ex) {

            }
        }
    }

    public byte[] getTransFoto(JLabel label) {
        ByteArrayOutputStream baos = null;
        try {
            Icon ico = label.getIcon();
            // Create a buffered image
            BufferedImage bufferedImage = new BufferedImage(ico.getIconWidth(),
                    ico.getIconHeight(), BufferedImage.TYPE_INT_RGB);
            baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
        } catch (IOException e) {
        }
        return baos.toByteArray();
    }

    public void byteImage(JLabel label, byte[] imgFoto, int ancho, int alto) {
        try {
            Image foto;
            BufferedImage image;
            ByteArrayInputStream bis = new ByteArrayInputStream(imgFoto);
            image = ImageIO.read(bis);
            foto = new ImageIcon(image).getImage();
            foto = foto.getScaledInstance(ancho, alto, 1); // 220, 276,
            label.setIcon(new ImageIcon(foto));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

//    public static InputStream reportImagen() {
////        String bd = "sisventas";
////        String url = "jdbc:mysql://localhost/" + bd;
//        Conexion db = new Conexion();
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//        //String query = "SELECT * FROM tclientes WHERE id = 1";
//        String query = "SELECT * FROM tclientes";
//        InputStream inp = null;
//        try {
//            ps = db.getConn().prepareStatement(query);
//            rs = ps.executeQuery();
//            while (rs.next()) {
//
//                inp = rs.getBinaryStream(10);
//            }
//        } catch (SQLException ex) {
//            System.out.println(ex.getMessage());
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
//        } finally {
//            try {
//                rs.close();
//                ps.close();
//                db.desconectar();
//            } catch (SQLException ex) {
//            }
//        }
//        return inp;
//    }
}

