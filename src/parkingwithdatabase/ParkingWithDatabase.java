/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parkingwithdatabase;

import java.sql.SQLException;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;

public class ParkingWithDatabase {

    public static void main(String[] args) {
        ParkingGUI parking = new ParkingGUI();
        parking.setVisible(true);

//            String s = "2017-11-05 00:00:00";
//            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try {
//            Date d = df.parse(s);
//            s = new TableParking().dataCounter(d);
//            System.out.println(s);
//        } catch (ParseException ex) {
//            Logger.getLogger(ParkingGUI.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        TableParking tp = new TableParking();
//        CachedRowSet rs = tp.cachedRowSet;
//        try {
//            rs.absolute(1);
//        System.out.println(rs.getString(tp.getArrivalDate()));
//        } catch (SQLException ex) {
//            Logger.getLogger(ParkingWithDatabase.class.getName()).log(Level.SEVERE, null, ex);
//        }
//String arriv = "2017-11-07 13:30:00:0";
////String dep = "2017-12-11 00:00:00";
//String dep = null;
//String st = new Billing().countProfit(arriv, dep);
//        System.out.println(st);
    }

}
