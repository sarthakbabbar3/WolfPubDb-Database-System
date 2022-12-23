package com.company;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Scanner;

public class Reports {
    private Connection connection = null;
    private Statement statement = null;
    private ResultSet result = null;

    private boolean continueLoop = true;
    public Reports(Connection c,Statement s, ResultSet r) throws SQLException {
        this.connection = c;
        this.statement = s;
        this.result = r;
        options();
    }

    public void options() throws SQLException {
        while (continueLoop) {
            System.out.println("Select one of the options to perform a task");
            System.out.println("1. Calculate the total current number of distributors.");
            System.out.println("2. Calculate Total Revenue Per City.");
            System.out.println("3. Calculate Total Revenue Per Distributor.");
            System.out.println("4. Calculate Total Revenue Per Location.");
            System.out.println("5. Calculate total payment to author/editor per time period and per work type.");
            System.out.println("6. Total Revenue of Publishing House.");
            System.out.println("7. Total Expenses of Publication House.");
            System.out.println("8. Number and total price of copies of each publication bought per distributor per month.");
            System.out.println("9. Exit");

            Scanner sc = new Scanner(System.in);
            int optionSelected = sc.nextInt();

            switch (optionSelected) {
                case 1:
                    totalNumberOfDistributors();
                    break;
                case 2:
                    totalRevenuePerCity();
                    break;
                case 3:
                    totalRevenuePerDistributor();
                    break;
                case 4:
                    totalRevenuePerLocation();
                    break;
                case 5:
                    totalPaymentToPerson();
                    break;
                case 6:
                    totalRevenueOfPublicationHouse();
                    break;
                case 7:
                    totalExpensesOfPublicationHouse();
                    break;
                case 8:
                    numberAndPriceOfCopies();
                    break;
                case 9:
                    System.out.println("Thank you please visit us again");
                    return;
                default:
                    System.out.println("Please enter valid Input");
                    break;
            }
        }
    }


    private void numberAndPriceOfCopies() {
        try {
            result = this.statement.executeQuery("Select D.DistributorID, YEAR(O.OrderDate) As year, " +
                    "MONTH(O.OrderDate) AS month, SUM(P.Cost*O.NoOfCopies) AS Total_Price, O.NoOfCopies " +
                    "from Orders O, Publication P, Distributor D Where O.PubID = P.PubID " +
                    "AND D.DistributorID = O.DistributorID group by D.DistributorID, " +
                    "YEAR(O.OrderDate), MONTH(O.OrderDate)");
            while (result.next()) {
                System.out.println();
                System.out.println("Distributor ID: " + result.getInt("DistributorID"));
                System.out.println("Year: " + result.getInt("Year"));
                System.out.println("Month: " + result.getInt("month"));
                System.out.println("Total Price: " + result.getInt("Total_Price"));
                System.out.println("No Of Copies: " + result.getInt("NoOfCopies"));
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void totalExpensesOfPublicationHouse() {
        try {
            result = this.statement.executeQuery("Select (Select SUM(ShippingCost) From Orders) + (Select SUM(Amount) From Payment) As Expenses");
            while (result.next()) {
                System.out.println();
                System.out.println("Total Expenses of Publication House: " + result.getInt("Expenses"));
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void totalRevenueOfPublicationHouse() {
        try {
            result = this.statement.executeQuery("Select SUM(P.Cost*O.NoOfCopies) AS Total_Revenue from Orders O, Publication P Where O.PubID = P.PubID");
            while (result.next()) {
                System.out.println();
                System.out.println("Total Revenue of Publication House: " + result.getInt("Total_Revenue"));
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void totalPaymentToPerson() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter the time period range");
            System.out.println("Enter from Date");
            String fromDate = sc.nextLine();
            System.out.println("Enter to Date");
            String toDate = sc.nextLine();

            result = this.statement.executeQuery("select Payment.WorkType, sum(Payment.Amount) AS Total_Payments " +
                    "from Payment JOIN Person on Payment.PersonID = Person.PersonID " +
                    "where DateOfPay Between '"+fromDate+"' and '"+toDate+"' " +
                    "group by Payment.WorkType");
            while (result.next()) {
                System.out.println();
                System.out.println("WorkType: " + result.getString("WorkType"));
                System.out.println("Total Payments: " + result.getInt("Total_Payments"));
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void totalRevenuePerLocation() {
        try {
            result = this.statement.executeQuery("Select D.StreetAddress, SUM(P.Cost*O.NoOfCopies) AS Revenue\n" +
                    "from Orders O, Publication P, Distributor D\n" +
                    "Where O.PubID = P.PubID AND D.DistributorID = O.DistributorID\n" +
                    "Group By D.StreetAddress");
            while (result.next()) {
                System.out.println();
                System.out.println("StreetAddress: " + result.getString("StreetAddress"));
                System.out.println("Total Revenue: " + result.getInt("Revenue"));
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void totalRevenuePerDistributor() {
        try {
            result = this.statement.executeQuery("Select D.DistributorName, SUM(P.Cost*O.NoOfCopies) AS Revenue\n" +
                    "from Orders O, Publication P, Distributor D\n" +
                    "Where O.PubID = P.PubID AND D.DistributorID = O.DistributorID\n" +
                    "Group By D.DistributorName");
            while (result.next()) {
                System.out.println();
                System.out.println("DistributorName: " + result.getString("DistributorName"));
                System.out.println("Total Revenue: " + result.getInt("Revenue"));
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void totalRevenuePerCity() {
        try {
            result = this.statement.executeQuery("Select D.City, SUM(P.Cost*O.NoOfCopies) AS Revenue from Orders O, Publication P, Distributor D Where O.PubID = P.PubID AND D.DistributorID = O.DistributorID Group By D.City");
            while (result.next()) {
                System.out.println();
                System.out.println("City: " + result.getString("City"));
                System.out.println("Total Revenue: " + result.getInt("Revenue"));
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void totalNumberOfDistributors() {
        try {
            result = this.statement.executeQuery("SELECT COUNT(DistributorID) AS TotalCount FROM Distributor");
            while (result.next()) {
                System.out.println();
                System.out.println("Total Number of Distributors: " + result.getInt("TotalCount"));
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
