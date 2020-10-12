/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Libreria;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 *
 * @author avice
 */
public class Ordenador {

    public static String getSerialNumber(char drive) throws Exception {
        String line = null;
        String serial = null;
        String[] cmd = {"/bin/bash","-c","echo password| sudo -S ls"};
        Process process = Runtime.getRuntime().exec("cmd /c vol " + drive + ":");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(process.getInputStream()));
        while ((line = in.readLine()) != null) {
            String[] strings = line.split(" ");
            serial = strings[strings.length - 1];
        }
        in.close();
        return serial;
    }
}
