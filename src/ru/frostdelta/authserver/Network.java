package ru.frostdelta.authserver;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Network {

    static HashMap<String, PreparedStatement> preparedStatements = new HashMap<String, PreparedStatement>();
    private Connection connection;
    private static final String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8"; private static final String user = "root"; private static final String password = "";


    public void openConnection() throws SQLException, ClassNotFoundException {
        if (connection != null && !connection.isClosed()) {
            return;
        }

        synchronized (this) {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);

            preparedStatements.put("keyAuth", connection.prepareStatement("SELECT * FROM `users_key` WHERE `name`=?"));

            preparedStatements.put("passAuth", connection.prepareStatement("SELECT * FROM `dle_users` WHERE `name`=?"));

            preparedStatements.put("passChange", connection.prepareStatement("UPDATE `dle_users` SET `password`=? WHERE `name`=?"));

            preparedStatements.put("eMailChange", connection.prepareStatement("UPDATE `dle_users` SET `email`=? WHERE `name`=?"));

            preparedStatements.put("sendMsg", connection.prepareStatement("INSERT INTO `tickets`(`name`, `text`, `theme`) VALUES (?, ? , ?)"));

            preparedStatements.put("addKey", connection.prepareStatement("INSERT INTO `users_key`(`name`, `key`) VALUES (?, ?)"));
        }
    }




    public void addKey(String player, String key) {
        PreparedStatement addKey = preparedStatements.get("addKey");

        try {
            addKey.setString(1, player);
            addKey.setString(2, key);



            addKey.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendMsg(String player, String msg, String theme) {
        PreparedStatement sendMsg = preparedStatements.get("sendMsg");

        try {
            sendMsg.setString(1, player);
            sendMsg.setString(2, msg);
            sendMsg.setString(3, theme);


            sendMsg.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void eMailChange(String player, String email) {
        PreparedStatement eMailChange = preparedStatements.get("eMailChange");

        try {
            eMailChange.setString(1, email);
            eMailChange.setString(2, player);

            eMailChange.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void passChange(String player, String hash) {
        PreparedStatement passChange = preparedStatements.get("passChange");

        try {
            passChange.setString(1, hash);
            passChange.setString(2, player);

            passChange.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String keyAuth(String name){
        PreparedStatement keyAuth = preparedStatements.get("keyAuth");
        ResultSet rs = null;
        try {

            keyAuth.setString(1, name);

            rs = keyAuth.executeQuery();
            while (rs.next()) {

                return rs.getString("key");

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return "false";
    }

    public String passAuth(String name){
        PreparedStatement passAuth = preparedStatements.get("passAuth");
        ResultSet rs = null;
        try {

            passAuth.setString(1, name);

            rs = passAuth.executeQuery();
            while (rs.next()) {

                return rs.getString("password");

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return "";
    }


    public String getServer(String name, String type){
        PreparedStatement getServer = preparedStatements.get("getServer");
        ResultSet rs = null;
        try {

            getServer.setString(1, name);

            rs = getServer.executeQuery();
            while (rs.next()) {

                return rs.getString(type);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return "";
    }







}
