/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Libreria;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author antonio
 */
public class Calendario {

    private DateFormat dateFormat;
    private Date date = new Date();
    private Calendar c = new GregorianCalendar();
    private final String Fecha;
    private final String Dia;
    private final String Mes;
    private final String Year;
    private final String Hora;
    private String am_pm;

    public Calendario() {
        switch (c.get(Calendar.AM_PM)) {
            case 0:
                am_pm = "am";
                break;
            case 1:
                am_pm = "pm";
                break;
        }
        dateFormat = new SimpleDateFormat("dd");
        Dia = dateFormat.format(date);
        dateFormat = new SimpleDateFormat("MM");
        Mes = dateFormat.format(date);
        dateFormat = new SimpleDateFormat("yyyy");
        Year = dateFormat.format(date);
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Fecha = dateFormat.format(date);
        dateFormat = new SimpleDateFormat("hh:mm:ss");
        Hora = dateFormat.format(date) + " " + am_pm;
    }

    public String getFecha() {
        return Fecha;
    }

    public String getDia() {
        return Dia;
    }

    public String getMes() {
        return Mes;
    }

    public String getYear() {
        return Year;
    }

    public String getHora() {
        return Hora;
    }

    public String addMes(int mes) {
        c.setTime(date);
        c.add(Calendar.MONTH, mes);
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(c.getTime());
    }
    
    public String addDay(Date date,int day) {
        c.setTime(date);
        c.add(Calendar.DAY_OF_WEEK, day);
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(c.getTime());
    }
    
    public String getFecha(Calendar date) {
        var dia = date.get(Calendar.DAY_OF_MONTH);
        var mes = date.get(Calendar.MONTH) + 1;
        var year = date.get(Calendar.YEAR);
        var fecha = String.valueOf(year + "/" + mes + "/" + dia);
        return fecha;
    }
}
