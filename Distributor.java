package com.company;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

public class Distributor {
    private Connection connection = null;
    private Statement statement = null;
    private ResultSet result = null;
    private int distributorID = 0;
    private boolean continueLoop = true;
    private HashSet<Integer> validOrderID = new HashSet<>();
    public Distributor(Connection c,Statement s, ResultSet r) throws SQLException {
        this.connection = c;
        this.statement = s;
        this.result = r;
        if (validateDistributor()) {
            findAllValidOrderID();
            options();
        }
    }
    public void findAllValidOrderID(){
        try {
            result = this.statement.executeQuery("SELECT OrderID FROM Orders WHERE DistributorID = "+distributorID);
            while (result.next()) {
                this.validOrderID.add(result.getInt("OrderID"));
            }
            result = this.statement.executeQuery("SELECT OrderID FROM OrderPayment");
            while (result.next()) {
                int orderIdentity = result.getInt("OrderID");
                if(this.validOrderID.contains(orderIdentity)){
                    this.validOrderID.remove(orderIdentity);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean validateDistributor() {
        try {
            System.out.println("Enter Distributor ID: ");
            Scanner sc = new Scanner(System.in);
            distributorID = sc.nextInt();
            result = this.statement.executeQuery("SELECT DistributorID FROM Distributor");
            boolean distributorExists = false;
            while (result.next()) {
                if (result.getInt("DistributorID") == distributorID) {
                    distributorExists = true;
                    break;
                }
            }
            if (!distributorExists) {
                System.out.println("Incorrect Distributor ID");
                return false;
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void options() throws SQLException {

        while (continueLoop) {
            System.out.println("Select one of the options to perform a task");
            System.out.println("1. Show orders for Distributor");
            System.out.println("2. Find a book by topic.");
            System.out.println("3. Find a book by date.");
            System.out.println("4. Find periodic publication by topic.");
            System.out.println("5. Find a periodic publication by date.");
            System.out.println("6. Find a book by title.");
            System.out.println("7. Find periodic publication by title.");
            System.out.println("8. Find a periodic publication by author Name.");
            System.out.println("9. Find a book by author Name.");
            System.out.println("10. Make order, for a book edition or an issue of a publication per distributor");
            System.out.println("11. Make Payment for the order.");
            System.out.println("12. Exit");

            Scanner sc = new Scanner(System.in);
            int optionSelected = sc.nextInt();

            switch (optionSelected) {
                case 1:
                    showOrders();
                    break;
                case 2:
                    findBookByTopic();
                    break;
                case 3:
                    findBookByDate();
                    break;
                case 4:
                    findPPByTopic();
                    break;
                case 5:
                    findPPByDate();
                    break;
                case 6:
                    findBookByTitle();
                    break;
                case 7:
                    findPPByTitle();
                    break;
                case 8:
                    findPPByAuthorName();
                    break;
                case 9:
                    findBookByAuthorName();
                    break;
                case 10:
                    makeAnOrder();
                    break;
                case 11:
                    makePayment();
                    break;
                case 12:
                    System.out.println("Thank you please visit us again");
                    return;
                default:
                    System.out.println("Please enter valid Input");
                    break;
            }
        }
    }

    private void makePayment() {
        try {

            // Switching off autocommit to start a transaction 
            connection.setAutoCommit(false);

            Scanner sc = new Scanner(System.in);
            if(this.distributorID == 0){
                System.out.println("Please enter a distributor ID :");
                this.distributorID = sc.nextInt();
            }

            findAllValidOrderID();
            if(this.validOrderID.size() > 0) {
                System.out.println("The OrderID that are pending for your payment:");
                Iterator it = validOrderID.iterator();
                while (it.hasNext()) {
                    System.out.println("OrderID : " + it.next());
                    System.out.println();
                }
                System.out.println("Enter the OrderID to make payment.");
                int orderIndentify = sc.nextInt();

                int currentBalance = 0;
                result = this.statement.executeQuery("select BalanceAmount from Distributor where DistributorID = " + distributorID);

                while (result.next()) {
                    currentBalance = result.getInt("BalanceAmount");
                }

                int pubID = 0;
                int isbn = 0;
                int noOfCopies = 0;
                result = this.statement.executeQuery("select PubID,ISBN,NoOfCopies from Orders where OrderID = " + orderIndentify);
                while (result.next()) {
                    pubID = result.getInt("PubID");
                    isbn = result.getInt("ISBN");
                    noOfCopies = result.getInt("NoOfCopies");
                }

                int eachCopyCost = 0;
                result = this.statement.executeQuery("select Cost from Publication where PubID = " + pubID + " and " + "ISBN = " + isbn);

                while (result.next()) {
                    eachCopyCost = result.getInt("Cost");
                }

                int updatedBalance = currentBalance - (eachCopyCost * noOfCopies);

                result = this.statement.executeQuery("UPDATE Distributor SET BalanceAmount = " + updatedBalance + " WHERE DistributorID = " + distributorID);

                System.out.println("Enter the new OrderPayID to record payment for the order");
                int orderPayID = sc.nextInt();
                System.out.println("Enter the date to record payment for the order");
                String dateOfPayForOrder = sc.next();

                result = this.statement.executeQuery("INSERT INTO  OrderPayment(OrderPayID, OrderID, Amount, DateOfPay) " +
                        "VALUES (" + orderPayID + " , " + orderIndentify + " , "
                        + (eachCopyCost * noOfCopies) + " , '" + dateOfPayForOrder + "')");

                connection.commit();

            }else{
                System.out.println("You don't have any pending orders to make payment. Thank you!!");
            }

            connection.setAutoCommit(true);

        } catch (SQLException e) {
			if (connection != null) {
				try {
					connection.rollback(); // =========>
					connection.setAutoCommit(true);
					// =========>
				} catch (SQLException e1) {

					e.printStackTrace();
				}
			}
		}
    }

    private void makeAnOrder() {
        try {
            connection.setAutoCommit(false);

            Scanner sc = new Scanner(System.in);
            System.out.println("Enter the new OrderID");
            int orderID = sc.nextInt();
            System.out.println("Enter the PubID");
            int pubID = sc.nextInt();
            System.out.println("Enter the ISBN");
            int isbn = sc.nextInt();
            System.out.println("Enter the Shipping Cost");
            int shippingCost = sc.nextInt();
            System.out.println("Enter the OrderDate");
            String orderDate = sc.next();

            System.out.println("Enter the NoOfCopies");
            int noOfCopies = sc.nextInt();
            System.out.println("Enter the DeliveryDate");
            String deliveryDate = sc.next();



            //INSERT INTO Orders(OrderID, DistributorID, PubID, ISBN, ShippingCost, OrderDate, DeliveryDate, NoOfCopies)
            // VALUES(4005, 2001, 1002, 54322, 35, '2022-01-30', '2022-02-10',20);

            result = this.statement.executeQuery("INSERT INTO  Orders(OrderID, DistributorID, PubID, ISBN, " +
                    "ShippingCost, OrderDate, DeliveryDate, NoOfCopies) VALUES ("+ orderID + " , "+ distributorID+" , "
                    + pubID + " , " + isbn + " , "
                    + shippingCost + " , '" + orderDate + "' , '" + deliveryDate + "' , " + noOfCopies + ")");

            int currentBalance = 0;
            result = this.statement.executeQuery("select BalanceAmount from Distributor where DistributorID = "+distributorID);

            while (result.next()) {
                currentBalance = result.getInt("BalanceAmount");
            }

            int eachCopyCost = 0;
            result = this.statement.executeQuery("select Cost from Publication where PubID = "+pubID+" and "+"ISBN = "+isbn);

            while (result.next()) {
                eachCopyCost = result.getInt("Cost");
            }

            int updatedBalance = currentBalance + (eachCopyCost*noOfCopies);

            result = this.statement.executeQuery("UPDATE Distributor SET BalanceAmount = "+updatedBalance+" WHERE DistributorID = "+distributorID);

            connection.commit(); 
            connection.setAutoCommit(true);
        } catch (SQLException e) {
			if (connection != null) {
				try {
					connection.rollback(); // =========>
					connection.setAutoCommit(true);
					// =========>
				} catch (SQLException e1) {

					e.printStackTrace();
				}
			}
		}
    }

    private void findBookByAuthorName() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter the author name of the book to search");
            String authorName = sc.nextLine();
            result = this.statement.executeQuery("select DISTINCT P.PersonName,B.Edition,Pub.Title,Pub.Type," +
                    "Pub.ISBN,Pub.PubID from Person P,WritesChapters WC,Publication Pub,Book B where " +
                    "P.PersonName = '"+authorName+"' and P.PersonID = WC.PersonID and WC.PubID = B.PubID " +
                    "and WC.ISBN = B.ISBN and B.PubID = Pub.PubID and B.ISBN = Pub.ISBN");
            while (result.next()) {
                System.out.println("PersonName: " + result.getString("PersonName"));
                System.out.println("Edition: " + result.getInt("Edition"));
                System.out.println("Title: " + result.getString("Title"));
                System.out.println("Type: " + result.getString("Type"));
                System.out.println("ISBN: " + result.getInt("ISBN"));
                System.out.println("Publication ID: " + result.getInt("PubID"));
                System.out.println();
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void findPPByAuthorName() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter the author name of the periodic publication to search");
            String authorName = sc.nextLine();
            result = this.statement.executeQuery("select DISTINCT P.PersonName, PP.Periodicity, " +
                    "Pub.Title,Pub.Type,Pub.ISBN,Pub.PubID from Person P,WritesArticles WA,Publication Pub," +
                    "PeriodicPublication PP where P.PersonName = '"+authorName+"' and " +
                    "P.PersonID = WA.PersonID and WA.PubID = PP.PubID and WA.ISBN = PP.ISBN and " +
                    "PP.PubID = Pub.PubID and PP.ISBN = Pub.ISBN");
            while (result.next()) {
                System.out.println("PersonName: " + result.getString("PersonName"));
                System.out.println("Periodicity: " + result.getInt("Periodicity"));
                System.out.println("Title: " + result.getString("Title"));
                System.out.println("Type: " + result.getString("Type"));
                System.out.println("ISBN: " + result.getInt("ISBN"));
                System.out.println("Publication ID: " + result.getInt("PubID"));
                System.out.println();
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void findPPByTitle() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter the title of the periodic publication to search");
            String title = sc.nextLine();
            result = this.statement.executeQuery("SELECT * FROM Publication NATURAL JOIN PeriodicPublication WHERE Title = '"+title+"'");
            while (result.next()) {
                System.out.println("Publication ID: " + result.getInt("PubID"));
                System.out.println("ISBN: " + result.getInt("ISBN"));
                System.out.println("Title: " + result.getString("Title"));
                System.out.println("Type: " + result.getString("Type"));
                System.out.println("Topic: " + result.getString("Topic"));
                System.out.println("Publication Date: " + result.getString("PublicationDate"));
                System.out.println("Cost: " + result.getInt("Cost"));
                System.out.println("Periodicity: " + result.getInt("Periodicity"));
                System.out.println();
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void findBookByTitle() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter the title of the book to search");
            String title = sc.nextLine();
            result = this.statement.executeQuery("SELECT * FROM Publication NATURAL JOIN Book WHERE Title = '"+title+"'");
            while (result.next()) {
                System.out.println("Publication ID: " + result.getInt("PubID"));
                System.out.println("ISBN: " + result.getInt("ISBN"));
                System.out.println("Title: " + result.getString("Title"));
                System.out.println("Type: " + result.getString("Type"));
                System.out.println("Topic: " + result.getString("Topic"));
                System.out.println("Publication Date: " + result.getString("PublicationDate"));
                System.out.println("Cost: " + result.getInt("Cost"));
                System.out.println("Edition: " + result.getInt("Edition"));
                System.out.println();
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void findPPByDate() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter the date of the periodic publication to search");
            String date = sc.nextLine();
            result = this.statement.executeQuery("SELECT * FROM Publication NATURAL JOIN PeriodicPublication WHERE PublicationDate = '"+date+"'");
            while (result.next()) {
                System.out.println("Publication ID: " + result.getInt("PubID"));
                System.out.println("ISBN: " + result.getInt("ISBN"));
                System.out.println("Title: " + result.getString("Title"));
                System.out.println("Type: " + result.getString("Type"));
                System.out.println("Topic: " + result.getString("Topic"));
                System.out.println("Publication Date: " + result.getString("PublicationDate"));
                System.out.println("Cost: " + result.getInt("Cost"));
                System.out.println("Periodicity: " + result.getInt("Periodicity"));
                System.out.println();
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void findPPByTopic() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter the topic of the periodic publication to search");
            String topic = sc.nextLine();
            result = this.statement.executeQuery("SELECT * FROM Publication NATURAL JOIN PeriodicPublication WHERE Topic = '"+topic+"'");
            while (result.next()) {
                System.out.println("Publication ID: " + result.getInt("PubID"));
                System.out.println("ISBN: " + result.getInt("ISBN"));
                System.out.println("Title: " + result.getString("Title"));
                System.out.println("Type: " + result.getString("Type"));
                System.out.println("Topic: " + result.getString("Topic"));
                System.out.println("Publication Date: " + result.getString("PublicationDate"));
                System.out.println("Cost: " + result.getInt("Cost"));
                System.out.println("Periodicity: " + result.getInt("Periodicity"));
                System.out.println();
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void findBookByDate() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter the date of the book to search");
            String date = sc.nextLine();
            result = this.statement.executeQuery("SELECT * FROM Publication NATURAL JOIN Book Where PublicationDate = '"+date+"'");
            while (result.next()) {
                System.out.println("Publication ID: " + result.getInt("PubID"));
                System.out.println("ISBN: " + result.getInt("ISBN"));
                System.out.println("Title: " + result.getString("Title"));
                System.out.println("Type: " + result.getString("Type"));
                System.out.println("Topic: " + result.getString("Topic"));
                System.out.println("Publication Date: " + result.getString("PublicationDate"));
                System.out.println("Cost: " + result.getInt("Cost"));
                System.out.println("Edition: " + result.getInt("Edition"));
                System.out.println();
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void findBookByTopic() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter the topic of the book to search");
            String topic = sc.nextLine();
            result = this.statement.executeQuery("SELECT * FROM Publication NATURAL JOIN Book WHERE Topic = '"+topic+"'");
            while (result.next()) {
                System.out.println("Publication ID: " + result.getInt("PubID"));
                System.out.println("ISBN: " + result.getInt("ISBN"));
                System.out.println("Title: " + result.getString("Title"));
                System.out.println("Type: " + result.getString("Type"));
                System.out.println("Topic: " + result.getString("Topic"));
                System.out.println("Publication Date: " + result.getString("PublicationDate"));
                System.out.println("Cost: " + result.getInt("Cost"));
                System.out.println("Edition: " + result.getInt("Edition"));
                System.out.println();
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showOrders() {
        try {

            result = this.statement.executeQuery("SELECT * FROM Orders WHERE DistributorID = "+distributorID);
            System.out.println("Please find your previous orders");
            System.out.println();
            while (result.next()) {
                System.out.println("OrderID: " + result.getInt("OrderID"));
                System.out.println("DistributorID: " + result.getInt("DistributorID"));
                System.out.println("Publication ID: " + result.getInt("PubID"));
                System.out.println("ISBN: " + result.getInt("ISBN"));
                System.out.println("Shipping Cost: " + result.getInt("ShippingCost"));
                System.out.println("Order Date: " + result.getString("OrderDate"));
                System.out.println("Delivery Date: " + result.getString("DeliveryDate"));
                System.out.println("No Of Copies: " + result.getInt("NoOfCopies"));
                System.out.println();
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
