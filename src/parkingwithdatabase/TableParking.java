/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parkingwithdatabase;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;

public class TableParking {

    private String name = "parking";
    //columns
    private String id;
    private String cellStatus;
    private String model;
    private String carNumber;
    private String color;
    private String ownerName;
    private String ownerSurname;
    private String arrivalDate;
    private String departureDate;
    private String downtime;
    private String mustPay;

    CachedRowSet cachedRowSet;
    CarDatabase carDatabase;
    ParkingGUI gui;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getCellStatus() {
        return cellStatus;
    }

    public String getModel() {
        return model;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public String getColor() {
        return color;
    }

    public String getOwnerSurname() {
        return ownerSurname;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public String getDowntime() {
        return downtime;
    }

    public String getMustPay() {
        return mustPay;
    }

    public TableParking() {
        try {
            initCachedRowSet();

            ResultSetMetaData resultSetMetaData = cachedRowSet.getMetaData();

            id = resultSetMetaData.getColumnLabel(1);
            cellStatus = resultSetMetaData.getColumnLabel(2);
            model = resultSetMetaData.getColumnLabel(3);
            carNumber = resultSetMetaData.getColumnLabel(4);
            color = resultSetMetaData.getColumnLabel(5);
            ownerName = resultSetMetaData.getColumnLabel(6);
            ownerSurname = resultSetMetaData.getColumnLabel(7);
            arrivalDate = resultSetMetaData.getColumnLabel(8);
            departureDate = resultSetMetaData.getColumnLabel(9);
            downtime = resultSetMetaData.getColumnLabel(10);
            mustPay = resultSetMetaData.getColumnLabel(11);
        } catch (SQLException ex) {
            Logger.getLogger(TableParking.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initCachedRowSet() {
        try {
            carDatabase = new CarDatabase();
            cachedRowSet = (CachedRowSet) carDatabase.getData();
            cachedRowSet.setUsername(CarDatabase.getUserName());
            cachedRowSet.setPassword(CarDatabase.getPassword());
            cachedRowSet.setUrl(CarDatabase.getURLname());
            cachedRowSet.setTableName(CarDatabase.getTableName());
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(TableParking.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getCell() {
        int cell = 0;
        try {
            initCachedRowSet();
            int[] arr = new int[cachedRowSet.size()];
            System.out.println("cachedRowSet has " + cachedRowSet.size() + "rows");
            int j = 0;
            while (cachedRowSet.next()) {
                arr[j] = cachedRowSet.getInt(id);
                j++;
            }
            for (int i = 0; i < arr.length; i++) {
                System.out.print(arr[i] + " ");
            }
            System.out.println();
            java.util.Arrays.sort(arr);

            for (int i = 0; i < arr.length; i++) {
                System.out.print(arr[i] + " ");
            }
            System.out.println();

            j = 0;
            for (int i = 1; i <= arr.length; i++) {
                if (arr[j] != i) {
                    cell = i;
                    break;
                } else {
                    j++;
                }
            }
            if (j == arr.length) {
                cell = ++j;
            }

        } catch (SQLException ex) {
            System.out.println("cell haven't initialize");
            Logger.getLogger(TableParking.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("cell: " + cell);
        return cell;
    }

    public String dataCounter(String dataAriving) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(dataAriving);
        String s = null;

        try {
            long timeUp = format.parse(dataAriving).getTime();
            long diff = System.currentTimeMillis() - timeUp;

            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            StringBuilder sb = new StringBuilder();
            sb.append(diffDays + " д., ");
            sb.append(diffHours + " ч., ");
            sb.append(diffMinutes + " м., ");
            s = sb.toString();
            System.out.println(s);
        } catch (ParseException ex) {
            Logger.getLogger(TableParking.class.getName()).log(Level.SEVERE, null, ex);
        }
        return s;
    }

    public void updateDatabase() {
        try {
            initCachedRowSet();
            carDatabase = new CarDatabase();
            try {
                carDatabase.getConection();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(TableParking.class.getName()).log(Level.SEVERE, null, ex);
            }
            Statement statement = carDatabase.getStatement();

            while (cachedRowSet.next()) {
                String dateArive = cachedRowSet.getString(arrivalDate);
                String dateDeparture = cachedRowSet.getString(departureDate);
                String mustPayString = countProfit(dateArive, dateDeparture);
                String downtime = dataCounter(dateArive);
                statement.executeUpdate("update parking set downtime = '" + downtime + "',must_pay = '" + mustPayString + "' where id = '" + cachedRowSet.getInt(id) + "' and car_namber = '" + cachedRowSet.getString(carNumber) + "'");
            }
        } catch (SQLException ex) {
            Logger.getLogger(TableParking.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String countProfit(String arrivalDate, String departureDate) {
        int prisePerHour = 1;
        int prisePerDay = 20;
        int prisePerMonth = 400;

        String mustPay = null;
        long mustPayLong = 0;
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long arrival = 0;
        long departure = 0;
        long diff = 0;

        try {
            arrival = formater.parse(arrivalDate).getTime();
            if (departureDate != null) {
                departure = formater.parse(departureDate).getTime();
            }
        } catch (ParseException ex) {
            System.out.println("couldn't able to parse arrival date");
        }

        if (departureDate == null) {
            diff = System.currentTimeMillis() - arrival;
        } else {
            diff = System.currentTimeMillis() - departure;
            System.out.println("diff " + diff);
        }
        if (diff == 0) {
            return "fail";
        }
        if (diff < 0) {
            return "ok";
        }

        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000) % 30;
        //here you may create method which would spesifiez it will be 31 or 30 months days;
        long diffMonth = (diff / (30 * 24 * 60 * 60 * 1000)) * -1;
        System.out.println("diffMonth " + diffMonth);
        System.out.println(diffMonth + " month" + diffDays + " days" + diffHours + " hour" + diffMinutes + " minute");

        if (diffMonth > 0) {
            long i = prisePerMonth * diffMonth;
            mustPayLong += i;
            //System.out.println("month" + i);
        }

        if (diffDays > 0) {
            long i = prisePerDay * diffDays;
            mustPayLong += i;
            //System.out.println("day" + i);
        }

        if (diffHours > 0) {
            long i = prisePerHour * diffHours;
            mustPayLong += i;
            //System.out.println("hour" + i);
        }

        if (diffMinutes > 30) {
            mustPayLong += prisePerHour;
        }

        System.out.println(mustPayLong);
        int i = (int) mustPayLong;

        mustPay = Integer.toString(i);

        return mustPay + " hrn";
    }
}
