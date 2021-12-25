package carsharing;

import java.sql.*;
import java.util.Scanner;

public class Main {

    static final String USER = "";
    static final String PASS = "";

    static Connection connection = null;
    static Statement stmt = null;
    static String databaseFileName;

    public static void main(String[] args) {

        if (args.length>0 && args[0].equals('-' + "databaseFileName"))
            databaseFileName = "./src/carsharing/db/"+args[1];
        else
            databaseFileName = "./src/carsharing/db/mine";
            databaseFileName = "/Users/kau/IdeaProjects/Car Sharing/Car Sharing/task/src/carsharing/db/carsharing";
        openConnection();

        CompanyDaoImp allCompanies = new CompanyDaoImp(stmt);
        CarDaoImp allCars = new CarDaoImp(stmt);
        CustomerDaoImp allCustomers = new CustomerDaoImp(stmt);

        Scanner scan = new Scanner(System.in);

        int choice1, choice2, choice3, choice4, choice5;

        do {
            System.out.println("1. Log in as a manager");
            System.out.println("2. Log in as a customer");
            System.out.println("3. Create a customer");
            System.out.println("0. Exit");
            System.out.print("> ");
            choice1 = scan.nextInt();
            System.out.println();
            if (choice1 == 1){
                do {
                    allCompanies.updateCompanies();
                    System.out.println("1. Company list");
                    System.out.println("2. Create a company");
                    System.out.println("0. Back");
                    System.out.print("> ");
                    choice2 = scan.nextInt();
                    System.out.println();

                    if (choice2 == 1) {
                        if (allCompanies.size()==0) {
                            System.out.println("The company list is empty!");
                        } else {
                            System.out.println("Choose the company:");
                            System.out.print(allCompanies);
                            System.out.println("0. Back");
                            System.out.print("> ");
                            choice3 = scan.nextInt();
                            System.out.println();

                            if (choice3>0 & choice3 <= allCompanies.size()) {
                                Company company = allCompanies.get(choice3);
                                System.out.println("'" + company.getName() + "' company");

                                do {
                                    allCars.updateCars(company);
                                    System.out.println("1. Car list");
                                    System.out.println("2. Create a car");
                                    System.out.println("0. Back");
                                    System.out.print("> ");
                                    choice4 = scan.nextInt();
                                    System.out.println();

                                    if (choice4 == 1) {
                                        if (allCars.size() == 0) {
                                            System.out.println("The car list is empty!");
                                        } else {
                                            System.out.println("Car list:");
                                            System.out.println(allCars);
                                        }
                                    } else if (choice4 == 2) {
                                        scan.nextLine();
                                        System.out.println("Enter the car name:");
                                        System.out.print("> ");
                                        allCars.addCar(scan.nextLine());
                                    }
                                } while (choice4 != 0);
                            }
                        }
                    } else if (choice2 == 2) {
                        scan.nextLine();
                        System.out.println("Enter the company name:");
                        System.out.print("> ");
                        allCompanies.addCompany(scan.nextLine());
                    }
                } while (choice2 != 0);
            } else if (choice1 == 2) {
                allCustomers.updateCustomer();
                System.out.println("Customer list:");
                if (allCustomers.size()==0) {
                    System.out.println("The customer list is empty!");
                } else {
                    System.out.print(allCustomers);
                    System.out.println("0. Back");
                    System.out.print("> ");
                    choice2 = scan.nextInt();
                    System.out.println();

                    choice3 = 9;
                    do {
                        if (choice2 > 0 & choice2 <= allCustomers.size()) {
                            allCompanies.updateCompanies();
                            allCustomers.updateCustomer();
                            allCars.updateCars();

                            Customer customer = allCustomers.get(choice2);

                            System.out.println("1. Rent a car");
                            System.out.println("2. Return a rented car");
                            System.out.println("3. My rented car");
                            System.out.println("0. Back");
                            System.out.print("> ");
                            choice3 = scan.nextInt();
                            System.out.println();
                            int currentCarId = customer.getCarId();

                            if (choice3 == 1) {
                                if (currentCarId>0) {
                                    System.out.println("You've already rented a car!");
                                    System.out.println();
                                }
                                else {
                                    System.out.println("Choose a company:");
                                    System.out.print(allCompanies);
                                    System.out.println("0. Back");
                                    System.out.print("> ");
                                    choice4 = scan.nextInt();
                                    System.out.println();

                                    if (choice4 > 0 & choice4 <= allCompanies.size()) {
                                        Company company = allCompanies.get(choice4);
                                        allCars.updateCarsNonrented(company);
                                        if (allCars.size() == 0) {
                                            System.out.println("The car list is empty!");
                                        } else {
                                            System.out.println("Choose a car:");
                                            System.out.print(allCars);
                                            System.out.println("0. Back");
                                            System.out.print("> ");
                                            choice5 = scan.nextInt();
                                            System.out.println();

                                            if (choice5>0 & choice5 <= allCars.size()) {
                                                Car car = allCars.get(choice5);
                                                allCustomers.rentCar(customer, car);
                                                System.out.println();
                                            }
                                        }
                                    }
                                }
                            } else if (choice3 == 2) {
                                if (currentCarId > 0) {
                                    allCustomers.returnCar(customer);
                                    System.out.println();
                                } else {
                                    System.out.println("You didn't rent a car!");
                                    System.out.println();
                                }
                            } else if (choice3 == 3) {
                                if (currentCarId > 0) {
                                    Car currentCar = allCars.get(currentCarId);
                                    Company currentCompany = allCompanies.get(currentCarId);
                                    System.out.println("Your rented car:");
                                    System.out.println(currentCar);
                                    System.out.println("Company:");
                                    System.out.println(currentCompany);
                                    System.out.println();
                                } else {
                                    System.out.println("You didn't rent a car!");
                                    System.out.println();
                                }
                            }
                        }
                    } while (choice3 != 0);
                }
            } else if (choice1 == 3) {
                scan.nextLine();
                System.out.println("Enter the customer name:");
                System.out.print("> ");
                allCustomers.addCustomer(scan.nextLine());
            }
        } while (choice1 != 0);

        closeConnection();
    }

    static void openConnection(){
        try {
            connection = DriverManager.getConnection("jdbc:h2:" + databaseFileName, USER, PASS);
            connection.setAutoCommit(true);
            System.out.println("Connected to database.");
            stmt = connection.createStatement();
        } catch(SQLException se) {
            se.printStackTrace();
        }
    }

    static void closeConnection(){
        try{
            if(stmt!=null)
                stmt.close();
        } catch(SQLException se2) {
            se2.printStackTrace();
        }

        try {
            if(connection!=null)
                connection.close();
        } catch(SQLException se){
            se.printStackTrace();
        }
    }

}