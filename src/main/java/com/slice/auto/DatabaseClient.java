package com.slice.auto;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseClient extends DatabaseClientHelpers {

    private static final String dbUsername = System.getenv("DBUSERNAME");
    private static final String dbPassword = System.getenv("DBPASSWORD");
    private static final String databaseURL = System.getenv("DBURL");

    // Returns a string value
    static String executeSQLQuery(String sqlQuery) throws SQLException {

        Connection connection = null;
        String resultValue = "";
        ResultSet rs;

        try {
            connection = DriverManager.getConnection(databaseURL, dbUsername, dbPassword);
            Statement stmt = connection.createStatement();
            rs = stmt.executeQuery(sqlQuery);

            if (rs != null) {                                                                   // If query returned something, i.e. the ResultSet is not blank/null
                while (rs.next()) {                                                             // Iterate through the rows
                    resultValue = rs.getString(1);                                  // Get the value of each row
                }
            } else {
                System.out.println("No records obtained for the specified query.");
            }
        } catch (SQLException sqlEx) {
            System.out.println("SQL Exception: " + sqlEx.getMessage());
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return resultValue;
    }

    // Returns a list of values
    static ArrayList<String> executeSQLQueryList(String sqlQuery) throws SQLException {

        Connection connection = null;
        ArrayList<String> resultValue = new ArrayList<>();
        ResultSet resultSet;

        try {
            connection = DriverManager.getConnection(databaseURL, dbUsername, dbPassword);
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(sqlQuery);

            if (resultSet != null) {
                while (resultSet.next()) {                                                      // While there exists a next row
                    int columnCount = resultSet.getMetaData().getColumnCount();                 // Get number of columns in the row
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int iCounter = 1; iCounter <= columnCount; iCounter++) {               // For each column in the list of columns
                        stringBuilder.append(resultSet.getString(iCounter).trim()).append(" "); // Build a string containing the value of each column, separated by spaces
                    }
                    String reqValue = stringBuilder.substring(0, stringBuilder.length() - 1);   // Substring the first value from the string generated above
                    resultValue.add(reqValue);                                                  //  Put the value obtained above in the "resultValue" list e.g. "Ameer Kabob, Art of Pizza on State Street, Atino's Pizza"
                }
            } else {
                System.out.println("No records obtained for the specified query.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return resultValue;
    }
}