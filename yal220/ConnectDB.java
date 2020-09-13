import java.sql.*;
import java.util.Scanner;

public class ConnectDB {
  public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);
    int flag = 0;
    do{
      try {
		System.out.println("Enter Oracle user id: ");
        String userid = scan.nextLine();
        System.out.println("Enter Oracle password for " + userid +": ");
        String password = scan.nextLine();

        Connection con = DriverManager.getConnection("jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241", userid, password);
        Statement s = con.createStatement();
		Statement s2 = con.createStatement();
		con.setAutoCommit(false);
        System.out.println("connection successfully made.");
        flag = 0;

		System.out.println("---------------------------------");
		System.out.println("| Welcome to Regork Supermarket! |");
		System.out.println("---------------------------------");

		System.out.println("Please select your role from Store Manager or Supplier.");
        int noMatchRole =0;
        do{
          String role;
          role = scan.nextLine();
          if(!role.equalsIgnoreCase("Store Manager") && !role.equalsIgnoreCase("Supplier")){
            System.out.println("Failed to recognize role. Please re-enter.");
            noMatchRole = 1;
          }
          else{
			noMatchRole = 0;
			if(role.equalsIgnoreCase("Store Manager")) {
				System.out.println("Hello, Store Manager " + userid + ". ");

			  while(true) {
				System.out.println("-----------");
        	    System.out.println("| Menu Bar |");
        		System.out.println("-----------");

				System.out.println("1) check Regork products & make an order");
				System.out.println("2) find the best supplier for a product");
				System.out.println("3) contact a supplier");
				System.out.println("4) recall a product");
				System.out.println("5) confirm the receipt of an order");
				System.out.println("Please select a number");

				int noSuchThing = 1;
				do {
					if(!scan.hasNextInt()){
                        System.out.println("Please enter an integer among 1, 2, 3, 4, and 5: ");
                        scan.next();
                    	noSuchThing = 1;
                        continue;
                    }

					int toDo = scan.nextInt();
					if(toDo != 1 && toDo != 2 && toDo != 3 && toDo != 4 && toDo != 5) {
						noSuchThing = 1;
						System.out.println("Invalid selection. Please select 1, 2, 3, 4 or 5.");
					}
					else {
						noSuchThing = 0;
						int isAlreadyEndPro = 0;
						if(toDo == 1) {
							System.out.println("Pulling out the list of products that Regork sells......");
							StoreManager.productOnShelf(s);
							System.out.println("Would you like to place an order from a supplier? (Y/N)");

							int noOrder = 0;
                            do{
                                Scanner scan3 = new Scanner (System.in);
                                String order;
                                order = scan3.nextLine();
                                if(!order.equalsIgnoreCase("Y") && !order.equalsIgnoreCase("N")){
                                    System.out.println("Failed to recognize option. Please re-enter.");
                                    noOrder = 1;
                                }
                                else {
                                    noOrder = 0;
                                    if(order.equalsIgnoreCase("Y")) {
										StoreManager.allProducts(s);
										isAlreadyEndPro = StoreManager.makeOrder(s, con);
                                    }
                                }
                            } while(noOrder == 1);
						}

						else if(toDo == 2) {
							StoreManager.bestSupplier(s);
						}
						if(toDo == 3) {
							System.out.println("Get suppliers' contact by 1)id, 2)name(or substring), or 3) product");
							int noSuchWay = 1;
							do {
								if(!scan.hasNextInt()){
                    				System.out.println("Please enter an integer among 1, 2, and 3: ");
                    				scan.next();
                   					noSuchWay = 1;
                    				continue;
                				}

								int byWhatWay = scan.nextInt();
								if(byWhatWay!= 1 && byWhatWay != 2 && byWhatWay != 3) {
                        			noSuchWay = 1;
                        			System.out.println("Invalid selection. Please select 1, 2, or 3.");
                    			}
								else {
									noSuchWay = 0;

									if(byWhatWay == 1) {
										StoreManager.searchById(s);
									}
									else if(byWhatWay == 2) {
										StoreManager.searchByName(s);
									}
									else if(byWhatWay == 3) {
										StoreManager.allProducts(s);
										StoreManager.searchByProduct(s);
									}
								}
							} while (noSuchWay == 1);

						}

						if(toDo == 4) {
							StoreManager.recall(s, s2, con);
						}
						if(toDo == 5) {
							StoreManager.confirm(s, con, isAlreadyEndPro);
						}
					}
        		}while(noSuchThing == 1);

				System.out.println("Do you want to quit now (Y/N)? If you select 'N', you will be directed back to the menu.");
				int quit = 0;
				do{
                	Scanner scan4 = new Scanner (System.in);
                    String quitSys = scan4.nextLine();
                    if(!quitSys.equalsIgnoreCase("Y") && !quitSys.equalsIgnoreCase("N")){
                    	System.out.println("Failed to recognize option. Please re-enter.");
                        quit = 1;
                    }
                    else {
						quit = 0;
                        if(quitSys.equalsIgnoreCase("Y")) {
							System.out.println("Logging out from Regork System......");
                            s.close();
                            con.close();
                            System.exit(0);
                        }
                        else if(quitSys.equalsIgnoreCase("N")) {
                            System.out.println("Redirecting to the menu bar......");
                        }
                    }
                 } while(quit == 1);
			  }
			}//end of manager role

			else if(role.equalsIgnoreCase("Supplier")) {
				System.out.println("Hello, Supplier " + userid + ". ");
				int sup_id = Supplier.chooseSupplier(s);

			  while(true) {
			 	System.out.println("-----------");
                System.out.println("| Menu Bar |");
                System.out.println("-----------");

                System.out.println("1) check new orders & make a shipment");
                System.out.println("2) update price/in-stock information for an existing product");
				System.out.println("3) add a new product");
                System.out.println("Please select a number");

				Scanner scan4 = new Scanner(System.in);

				int noSuchThing = 1;
                do {
					if(!scan4.hasNextInt()){
                        System.out.println("Please enter an integer among 1, 2, and 3: ");
                        scan4.next();
                        noSuchThing = 1;
                        continue;
                    }
                    int toDo = scan4.nextInt();
                    if(toDo != 1 && toDo != 2 && toDo != 3) {
                        noSuchThing = 1;
                        System.out.println("Invalid selection. Please select 1, 2 or 3.");
                    }

                   else {
                        noSuchThing = 0;
                        if(toDo == 1) {
							Supplier.checkOrder(s, con, sup_id);
						}

						if(toDo == 2) {
							System.out.println("Would you like to update the price/quantity for your products? (Y/N)");
							System.out.println("Note that you can only add quantity to include newly manufactured items.");
							System.out.println("The decrease in number of items casued by transaction/shipment will be handled automatically when making a shipment. ");
							int noMatchOption = 0;
                            do{
                                Scanner scan5 = new Scanner (System.in);
                                String update;
                                update = scan5.nextLine();
                                if(!update.equalsIgnoreCase("Y") && !update.equalsIgnoreCase("N")){
                                    System.out.println("Failed to recognize option. Please re-enter.");
                                    noMatchOption = 1;
                                }
                                else {
                                    noMatchOption = 0;
                                    if(update.equalsIgnoreCase("Y")) {
										Supplier.makeChange(s, con, sup_id);
                                    }
                                }
                            } while(noMatchOption == 1);
                        }

						if(toDo == 3) {
							Supplier.addProduct(s, con, sup_id);
						}
					}
				 } while(noSuchThing == 1);
				 System.out.println("Do you want to quit now (Y/N)? If you select 'N', you will be directed back to the menu.");
                int quit = 0;
                do{
                    Scanner scan7 = new Scanner (System.in);
                    String quitSys = scan7.nextLine();
                    if(!quitSys.equalsIgnoreCase("Y") && !quitSys.equalsIgnoreCase("N")){
                        System.out.println("Failed to recognize option. Please re-enter.");
                        quit = 1;
                    }
                    else {
                        quit = 0;
                        if(quitSys.equalsIgnoreCase("Y")) {
                            System.out.println("Logging out from Regork System......");
                            s.close();
                            con.close();
                            System.exit(0);
                        }
                        else if(quitSys.equalsIgnoreCase("N")) {
                            System.out.println("Redirecting to the menu bar......");
                        }
                    }
                 } while(quit == 1);
              }
			}
          }//end of supplier role
        } while(noMatchRole == 1);

		s.close();
        con.close();
        System.exit(0);

		} catch (SQLException e) {
        System.out.println("Wrong login info Please re-enter again. ");
        flag = 1;
      }
    } while(flag == 1);
  }
}


