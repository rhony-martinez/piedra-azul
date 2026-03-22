/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.piedrazul.infrastructure.persistence.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author asus
 */
public class ConnectionFactory {
    private static final String URL = "jdbc:postgresql://localhost:5432/piedrazul";
    private static final String USER = "piedrazul";
    private static final String PASSWORD = "db_piedrazul";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
