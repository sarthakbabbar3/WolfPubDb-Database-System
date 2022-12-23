package com.company;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    static final String jdbcURL = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/sarora22";
    // Put your oracle ID and password here

    public  Connection getConnection() {
        return connection;
    }

    public  Statement getStatement() {
        return statement;
    }

    public  ResultSet getResult() {
        return result;
    }


    private  Connection connection = null;
    private  Statement statement = null;
    private  ResultSet result = null;

    public DBConnection() {

        initialize();

        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                    connection.setAutoCommit(true);
                } catch (SQLException e1) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean checkAbilityToStudy(String studentName) {
        try {
            result = statement
                    .executeQuery("SELECT (FundingReceived+Income) AS TotalIncome, (TuitonFees+LivingExpenses) AS "
                            + "TotalFees FROM Students, Schools WHERE Students.School = Schools.Name AND Students.Name "
                            + "LIKE '" + studentName + "%'");

            if (result.next()) {
                return (result.getInt("TotalIncome") > result.getInt("TotalFees"));
            }
            throw new RuntimeException(studentName + " cannot be found.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void initialize() {
        try {
            connectToDatabase();

//            statement.executeUpdate("CREATE TABLE Students (Name VARCHAR(20), "
//                    + "School VARCHAR(10), Age INTEGER, FundingReceived INTEGER, Income INTEGER, Sex CHAR(1))");
//
//            statement.executeUpdate("INSERT INTO Students VALUES ('Todd', 'NC State'," + " 18, 16000, 30000, 'M')");
//            statement.executeUpdate("INSERT INTO Students VALUES ('Max', 'Stanford'," + " 21, 20000, 70000, 'M')");
//            statement.executeUpdate("INSERT INTO Students VALUES ('Alex', 'UNC'," + " 19, 8000, 40000, 'M')");
//            statement.executeUpdate("INSERT INTO Students VALUES ('Natasha', 'Harvard'," + " 22, 15000, 75000, 'F')");
//            statement.executeUpdate("INSERT INTO Students VALUES ('Kelly', 'UCLA'," + " 23, 2000, 50000, 'F')");
//            statement.executeUpdate("INSERT INTO Students VALUES ('Angela', 'NYU'," + "18, 8000, 45000, 'F')");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void connectToDatabase() throws ClassNotFoundException, SQLException {
        Class.forName("org.mariadb.jdbc.Driver");

        String user = "sarora22";
        String password = "200418000";

        connection = DriverManager.getConnection(jdbcURL, user, password);
        statement = connection.createStatement();
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (result != null) {
            try {
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
