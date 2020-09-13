import java.sql.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Supplier {
	public static int existID2;

	public static int chooseSupplier(Statement s) {
		Scanner scan = new Scanner(System.in);
		String q;
    	ResultSet result;
		int ID = 0;
    	try {
      		q = "select sup_id Company_ID, sup_name Name from Supplier where sup_id > 1 order by sup_id";
      		result = s.executeQuery(q);
      		if(!result.next()){
        		System.out.println ("No supplier records found. ");
      		}
      		else{
				System.out.println("Here is a list of suppliers in our system. ");
        		System.out.println(String.format("%-20s", "Company_ID") + String.format("%-20s", "Name"));
        		do{
          			System.out.println (String.format("%-20s", result.getString("Company_ID")) + " " + String.format("%-20s", result.getString("Name")));
        		} while (result.next());
				System.out.println("Please choose your company from the list by providing the company ID: ");
      		}
			int badID = 0;
			do {
				if(!scan.hasNextInt()){
          			System.out.println("Please enter an integer between 2 and 100: ");
          			scan.next();
          			badID = 1;
          			continue;
        		}
        		ID = scan.nextInt();
        		if(ID < 2 || ID > 100) {
          			System.out.println("Please enter an integer between 2 and 100: ");
          			badID = 1;
         	 		continue;
        		}
				else {
					badID = 0;
					overview(s, ID);
				}
			} while(badID == 1);
        } catch(SQLException e) {
            System.out.println("Opps! Something went wrong...Please try again later. ");
        }
		return ID;
    }


	public static void overview(Statement s, int ID) {
		try {
			String q2;
        	ResultSet result2;
        	q2 = "select sup_id Company_ID, sup_name Name, comp_id Product_ID, gen_name Item, price Unit_Price, quantity In_Stock " +
          		"from supplier natural join manufactures inner join general_product on gen_id = comp_id " +
            	"where sup_id = " + ID;
        	result2 = s.executeQuery(q2);

        	if(!result2.next()){}
			else {
          		System.out.println("Here is a product overall of your company: ");
          		System.out.println(String.format("%-15s", "Company_ID") + String.format("%-15s", "Name") + String.format("%-15s", "Product_ID") + String.format("%-30s", "Item") + String.format("%-20s", "Unit_Price") + String.format("%-10s", "In_Stock"));
          		do {
            		System.out.println (String.format("%-15s", result2.getString("Company_ID")) + " " + String.format("%-15s", result2.getString("Name")) + " " + String.format("%-15s", result2.getString("Product_ID")) + " " + String.format("%-30s", result2.getString("Item")) + " "  + String.format("%-20s", result2.getString("Unit_Price")) + " " + String.format("%-10s", result2.getString("In_Stock")));
          		} while(result2.next());
			}
    	} catch(SQLException e) {
        	System.out.println("Opps! Something went wrong...Please try again later. ");
    	}
	}

	public static void makeChange(Statement s, Connection con, int supID) {
		try {
			System.out.println("Please confirm the product ID that you would like to make change on: ");
            Scanner scan6 = new Scanner (System.in);
            int badID = 0;
            do {
            	if(!scan6.hasNextInt()){
     	           System.out.println("Please enter an integer: ");
                   scan6.next();
                   badID = 1;
                   continue;
                }
                int ID = scan6.nextInt();

				int row2 = 0;
                String q5 = "select count(*) from supplier natural join manufactures where sup_id = " + supID;
                ResultSet result5 = s.executeQuery(q5);
                if(!result5.next()) {}
                else {
                	do {
                    	String row = " ";
                        row = result5.getString("count(*)");
                        row2 = Integer.parseInt(row);
                    } while(result5.next());
                }

                int count = 0;
                String q3 = "select comp_id from supplier natural join manufactures where sup_id = " + supID;
                ResultSet result3 = s.executeQuery(q3);
                if(!result3.next()) {}
                else {
                	do {
                    	int comp2 = 0;
                        String comp = " ";
                        comp = result3.getString("comp_id");
                        comp2 = Integer.parseInt(comp);
                        if(ID == comp2) {
                        	badID = 0;
                            break;
                        }
                        count++;
                    } while(result3.next());
                }

                if(count == row2) {
                	badID = 1;
                    System.out.println("Please enter an integer(product ID) that is among your products: ");
                    continue;
                }

              	badID = 0;
                updatePro(s, con, ID, supID);
            } while(badID == 1);
		} catch(SQLException e) {
            System.out.println("Opps! Something went wrong...Please try again later. ");
        }
	}


	public static void updatePro(Statement s, Connection con, int ID, int sup_id) {
		try {
			int noProduct = 0;
			do {
				String q3;
        		ResultSet result3;
        		q3 = "select gen_name from General_Product where gen_id = " + ID;
         		result3 = s.executeQuery(q3);
        		if(!result3.next()){
					noProduct = 1;
        			System.out.println ("No Product with ID " + ID);
       			}
			} while(noProduct == 1);

			System.out.println("Would you like to change the price of product#" + ID + ". (Y/N)");
			int noMatchOption = 0;
			do{
            	Scanner scan = new Scanner (System.in);
                String changePrice;
                changePrice = scan.nextLine();
                if(!changePrice.equalsIgnoreCase("Y") && !changePrice.equalsIgnoreCase("N")){
                	System.out.println("Failed to recognize option. Please re-enter.");
                    noMatchOption = 1;
                }
                else {
      		    	noMatchOption = 0;
                    if(changePrice.equalsIgnoreCase("Y")) {
           				System.out.println("Please enter the new price: ");
						Scanner scan2 = new Scanner (System.in);
                        int badNum = 0;
                        do {
                        	if(!scan2.hasNextDouble()){
                             	System.out.println("Please enter a positive number: ");
                                scan2.next();
                                badNum = 1;
                                continue;
                            }
                            double newPrice = scan2.nextDouble();
							boolean fail = BigDecimal.valueOf(newPrice).scale() > 2;

                            if(newPrice < 0 || newPrice == 0 || fail) {
                            	System.out.println("Please enter a positive number with at most two decimal places: ");
                                badNum = 1;
                                continue;
                            }
                            else {
                                badNum = 0;
								String q;
								q = "UPDATE manufactures SET price  = " + newPrice + "  WHERE comp_id = " + ID + " and sup_id = " + sup_id;
								s.executeQuery(q);
								System.out.println("The price for product#" + ID + " has been updated.");
								overview(s, sup_id);
                             }
                         } while(badNum == 1);
					}
				}
			} while(noMatchOption == 1);

			int noMatchOption2 = 0;
			System.out.println("Would you like to update the quantity of product#" + ID + "? (Y/N)");
			do{
                Scanner scan = new Scanner (System.in);
                String changeQuant;
                changeQuant = scan.nextLine();
                if(!changeQuant.equalsIgnoreCase("Y") && !changeQuant.equalsIgnoreCase("N")){
                    System.out.println("Failed to recognize option. Please re-enter.");
                    noMatchOption2 = 1;
                }
                else {
                    noMatchOption2 = 0;
                    if(changeQuant.equalsIgnoreCase("Y")) {
                        System.out.println("How many to add? ");
                        Scanner scan2 = new Scanner (System.in);
                        int badNum = 0;
                        do {
                            if(!scan2.hasNextInt()){
                                System.out.println("Please enter a postive integer: ");
                                scan2.next();
                                badNum = 1;
                                continue;
                            }
                            int newQuant = scan2.nextInt();
                            if(newQuant < 0 || newQuant == 0) {
                                System.out.println("Please enter a positive integer: ");
                                badNum = 1;
                                continue;
                            }
                            else {
                                badNum = 0;
								String add = "";
								int add2 = 0;
								String q3;
                				ResultSet result3;
                				q3 = "select quantity from manufactures where sup_id = " + sup_id + " and comp_id = " + ID;
                				result3 = s.executeQuery(q3);
                				if(!result3.next()){}
								else {
									add = result3.getString("quantity");
									add2 = Integer.parseInt(add);
									newQuant = newQuant + add2;
								}

                                String q;
                                q = "UPDATE manufactures SET quantity = " + newQuant + "  WHERE comp_id = " + ID + " and sup_id = " + sup_id;
                                s.executeQuery(q);
                                System.out.println("The quantity for product#" + ID + " has been updated.");
                                overview(s, sup_id);
                             }
                         } while(badNum == 1);
                    }
                }
			 } while(noMatchOption2 == 1);
			 con.commit();
		} catch(SQLException e) {
			try {
                con.rollback();
                System.out.println("Opps! Something went wrong...Transaction was not successfully. Please try again later. ");
            } catch (SQLException rollbackException) {
                System.out.println("Opps! Something went wrong...Please contact system manager. ");
            }
        }
	}

	public static void addProduct(Statement s, Connection con, int sup_id) {
		try {
			String lastID = "";
			int lastID2 = 0;
			String q;
            ResultSet result;
            q = "select gen_id from general_product order by gen_id desc";
            result = s.executeQuery(q);
            if(!result.next()){}
            else {
				lastID = result.getString("gen_id");
				lastID2 = Integer.parseInt(lastID);
			}

			System.out.println("Before proceeding to add a new product to the system, is it a branded product? (Y/N)");
			Scanner scanner = new Scanner(System.in);
			int noBrand = 0;
			boolean callBranded = false, shareID = false;
			do {
				String branded = scanner.nextLine();
				switch (branded) {
					case "y":
					case "Y":
						noBrand = 0;
						callBranded = branded(s, con, lastID2, sup_id);
					break;

					case "N":
					case "n" :
						noBrand = 0;
						shareID = notBranded(s, con, lastID2, sup_id);
					break;

					default:
						noBrand = 1;
						System.out.println("Failed to recognize option. Please enter 'Y' for yes, or 'N' for no. ");
					break;
				}
			} while (noBrand == 1);

			System.out.println("Would you like to update price and quantity at this point? (Y/N)");
			System.out.println("Note that you can modify both two later by choosing '2' in the Menu Bar. ");

			int noMatchOption = 0;
			do{
             	Scanner scan2 = new Scanner (System.in);
                String set;
                set = scan2.nextLine();
                if(!set.equalsIgnoreCase("Y") && !set.equalsIgnoreCase("N")){
                 	System.out.println("Failed to recognize option. Please enter 'Y' for yes, or 'N' for no.");
                    noMatchOption = 1;
                 }
                 else {
                 	noMatchOption = 0;
                    if(set.equalsIgnoreCase("Y")) {
						if(callBranded == true) {
							updatePro(s, con, (lastID2+1), sup_id);
						}
						else if (shareID == true) {
							updatePro(s, con, existID2, sup_id);
						}
						else if (shareID == false) {
							updatePro(s, con, (lastID2+1), sup_id);
						}
					}
				}
			} while(noMatchOption == 1);

            System.out.println("The new product#" + (lastID2 + 1) + " has been added.");
            overview(s, sup_id);

		} catch(SQLException e) {
            System.out.println("Opps! Something went wrong...Please try again later. ");
        }
	}

	public static boolean branded(Statement s, Connection con, int lastID2, int sup_id) {
		boolean callBranded = true;
		try {
			System.out.println("The next avaliable product ID is " + (lastID2 + 1) + ".");
    		System.out.println("Please enter the product name: ");
        	Scanner scan = new Scanner(System.in);
        	String newProName = scan.nextLine();
        	System.out.println("Please enter the product catagory: ");
        	String newProCag = scan.nextLine();

        	String q2;
			q2 = "insert into general_product (gen_id, gen_name, category, end_product_flag) values (" + (lastID2+1) + ", '" + newProName + "', '" + newProCag + "', 0)";
        	s.executeQuery(q2);

        	String q3;
			q3 = "insert into manufactures (sup_id, comp_id, price, quantity) values (" + sup_id + ", " + (lastID2+1) + ", NULL, 0)";
        	s.executeQuery(q3);

			int comp_from_id = getRandomNumberInRange(1000, (lastID2 + 1));
            int sup_from_id = getRandomNumberInRange(2, 100);
			String q4;
			q4 = "insert into made_of values (" + (lastID2+1) + ", " + comp_from_id + ", " + sup_id + ", " + sup_from_id + ")";
			s.executeQuery(q4);

			con.commit();
		} catch(SQLException e) {
			try {
                con.rollback();
				System.out.println(e);
                System.out.println("Opps! Something went wrong...Transaction was not successfully. Please try again later. ");
            } catch (SQLException rollbackException) {
                System.out.println("Opps! Something went wrong...Please contact system manager. ");
            }
        }
		return callBranded;
	}

	public static boolean notBranded(Statement s, Connection con, int lastID2, int sup_id) {
		boolean shareID = false;
		 try {
			System.out.println("Please enter the product name: ");
            Scanner scan = new Scanner(System.in);
            String newProName = scan.nextLine();
            String existID = "";
            String q1;
            ResultSet result1;
            q1 = "select gen_id from general_product where gen_name = '" + newProName + "'";
            result1 = s.executeQuery(q1);
            if(!result1.next()) {
	            System.out.println("This non-branded product has not been recorded in our system before. It will have its own new ID.");
				//System.out.println("You will be asked to re-enter the product name to confirm. ");
                //branded(s, con, lastID2, sup_id);
				System.out.println("The next avaliable product ID is " + (lastID2 + 1) + ".");
            	Scanner scan2 = new Scanner(System.in);
            	System.out.println("Please enter the product catagory: ");
           	 	String newProCag = scan2.nextLine();

            	String q2;
            	q2 = "insert into general_product (gen_id, gen_name, category, end_product_flag) values (" + (lastID2+1) + ", '" + newProName + "', '" + newProCag + "', 0)";
            	s.executeQuery(q2);

            	String q3;
            	q3 = "insert into manufactures (sup_id, comp_id, price, quantity) values (" + sup_id + ", " + (lastID2+1) + ", NULL, 0)";
            	s.executeQuery(q3);

            	int comp_from_id = getRandomNumberInRange(1000, (lastID2 + 1));
            	int sup_from_id = getRandomNumberInRange(2, 100);
            	String q4;
            	q4 = "insert into made_of values (" + (lastID2+1) + ", " + comp_from_id + ", " + sup_id + ", " + sup_from_id + ")";
            	s.executeQuery(q4);
            }
            else {
				shareID = true;
            	existID = result1.getString("gen_id");
                existID2 = Integer.parseInt(existID);
                System.out.println("This non-branded product has already been recorded in our system due to some other suppliers who also produce the same product. ");
                System.out.println("The system will provide your product with a shared ID: " + existID2);

                String q3;
                q3 = "insert into manufactures (sup_id, comp_id, price, quantity) values (" + sup_id + ", " + existID2 + ", NULL, 0)";
                s.executeQuery(q3);

				int comp_from_id = getRandomNumberInRange(1000, (lastID2 + 1));
            	int sup_from_id = getRandomNumberInRange(2, 100);
            	String q4;
            	q4 = "insert into made_of values (" + existID2 + ", " + comp_from_id + ", " + sup_id + ", " + sup_from_id + ")";
            	s.executeQuery(q4);
			}
				con.commit();
		} catch(SQLException e) {
			try {
                con.rollback();
                System.out.println("!!Unsucessful transaction!! Your company has already been manufacturing this product. ");
            } catch (SQLException rollbackException) {
                System.out.println("Opps! Something went wrong...Please contact system manager. ");
            }
        }
		return shareID;
	}


 	public static void checkOrder(Statement s, Connection con, int sup_id) {
		System.out.println("Checking for any new orders/unshiped orders......");
		try {
			String q;
            ResultSet result;
            q = "select ship_id, sup_id, sup_name, gen_id, gen_name, price, quantity, total_price from shipment natural join ship_gen " + 
				"inner join supplier using (sup_id) inner join general_product using (gen_id) where leave_time is NULL and sup_id = " + sup_id;
            result = s.executeQuery(q);
            if(!result.next()) {
                System.out.println("There is no new order that has not yet been shipped. Please check back later. ");
            }
            else {
				System.out.println("You have at lease one new order. Please see below and make a shipment as soon as possible. ");
				System.out.println(String.format("%-10s","Shipment_ID") + " " + String.format("%-10s", "Buyer") + " " + 
                        String.format("%-10s", "Supplier_ID") + " " + String.format("%-20s", "Supplier_Name") + " " + 
                        String.format("%-10s", "Product_ID") + " " + String.format("%-30s", "Product_Name") + " " + 
                        String.format("%-10s", "Unit_Price") + " " + String.format("%-10s", "Quantity") + " " + 
                        String.format("%-25s","Total_Price"));

				do {
					System.out.println(String.format("%-10s", result.getString("ship_id")) + " " + String.format("%-10s", "Regork") + " " + 
						String.format("%-10s", result.getString("sup_id")) + " " + String.format("%-20s", result.getString("sup_name")) + " " + 
                        String.format("%-10s", result.getString("gen_id")) + " " + String.format("%-30s", result.getString("gen_name")) + " " + 
                        String.format("%-10s", result.getString("price")) + " " + String.format("%-10s", result.getString("quantity")) + " " + 
						String.format("%-25s",result.getString("total_price")));
				} while(result.next());
				makeShip(s, con, sup_id);
			}

		} catch(SQLException e) {
			System.out.println("Opps! Something went wrong...Please try again later. ");
        }


    }

	public static void makeShip(Statement s, Connection con, int supID) {
		try {
			int badID = 0;
			int noMatchOption = 0;
			System.out.println("Would you like to make a shipment now? (Y/N)");
            do{
                Scanner scan = new Scanner (System.in);
				Scanner scan2 = new Scanner (System.in);
                String make;
                make = scan.nextLine();
                if(!make.equalsIgnoreCase("Y") && !make.equalsIgnoreCase("N")){
                    System.out.println("Failed to recognize option. Please enter 'Y' for yes, or 'N' for no.");
                    noMatchOption = 1;
                 }
                 else {
                    noMatchOption = 0;
                    if(make.equalsIgnoreCase("Y")) {
						System.out.println("Please enter the shipment ID that you want to ship now: ");
						do {
        					if(!scan2.hasNextInt()){
          						System.out.println("Please enter an integer(shipment ID) that is among your unshipped order: ");
          						scan2.next();
          						badID = 1;
          						continue;
        					}
        					int shipID = scan2.nextInt();


							int row2 = 0;
                    		String q11 = "select count(*) from shipment where leave_time is NULL";
                   	 		ResultSet result11 = s.executeQuery(q11);
                    		if(!result11.next()) {}
                    		else {
                        		do {
                            		String row = " ";
                            		row = result11.getString("count(*)");
                            		row2 = Integer.parseInt(row);
                        		} while(result11.next());
                    		}

                    		int count = 0;
                    		String q13 = "select ship_id from shipment where leave_time is NULL";
                    		ResultSet result13 = s.executeQuery(q13);
                    		if(!result13.next()) {}
                    		else {
                        		do {
                            		int ship2 = 0;
                            		String ship = " ";
                            		ship = result13.getString("ship_id");
                            		ship2 = Integer.parseInt(ship);
                            		if(shipID == ship2) {
                                		badID = 0;
                                		break;
                            		}
                            		count++;
                        		} while(result13.next());
                    		}

                    		if(count == row2) {
                        		badID = 1;
                        		System.out.println("Please enter an integer(shipment ID) that is among your unshipped order: ");
                        		continue;
                    		}

							int genID2 = 0;
							String q7, genID = " ";
							ResultSet result7;
							q7 = "select gen_id from ship_gen where ship_id = " + shipID;
							result7 = s.executeQuery(q7);
                            if(!result7.next()) {}
                            else {
                                genID = result7.getString("gen_id");
                                genID2 = Integer.parseInt(genID);
                            }

							int quant2 = 0;
							String quant = " ";
							String q2;
							ResultSet result2;
							q2 = "select quantity from ship_gen where ship_id = " + shipID;
							result2 = s.executeQuery(q2);
							if(!result2.next()) {}
                            else {
								quant = result2.getString("quantity");
								quant2 = Integer.parseInt(quant);
							}

							System.out.println("Processing the shipment......");
							badID = 0;
							String q;
							String date = " ";

 							String time = "select to_char(sysdate,'DD-MON-YYYY') current_time from dual";
							ResultSet resultT ;
							resultT = s.executeQuery(time);
							if(!resultT.next()) {}
							else {
								do {
									date = resultT.getString("current_time");
								} while(resultT.next());
							}

     						q = "Update shipment Set leave_time = to_date('" + date + "','DD/MM/YYYY') where ship_id = " + shipID;
      						s.executeQuery(q);

							//for-loop add to specific product table and ship_spec table
							for(int i = 0; i < quant2; i++) {
								String lastID = " ";
            					int lastID2 = 0;
            					String q3;
            					ResultSet result3;
            					q3 = "select spec_id from specific_product order by spec_id desc";
            					result3 = s.executeQuery(q3);
            					if(!result3.next()){}
            					else {
                					lastID = result3.getString("spec_id");
                					lastID2 = Integer.parseInt(lastID);
            					}

								String q5 = "insert into specific_product (spec_id) values (" + (lastID2 + 1) + ")";
								s.executeQuery(q5);

								String q4 = "insert into ship_spec (ship_id, spec_id, sup_id) " +
											"values (" + shipID + ", " + (lastID2 + 1) + ", " + supID + ")";
								s.executeQuery(q4);


								String q6 = "insert into an_instance_of (spec_id, gen_id) values (" + (lastID2 + 1) + ", " + genID2 + ")";
								s.executeQuery(q6);

								int sup_to_id2 = 0;
                            	String sup_to_id = " ";
                            	String q8;
                            	ResultSet result8;
                            	q8 = "select sup_to_id from made_of where comp_to_id = " + genID2;
                            	result8 = s.executeQuery(q8);
                            	if(!result8.next()) {}
                            	else {
                                	sup_to_id = result8.getString("sup_to_id");
                                	sup_to_id2 = Integer.parseInt(sup_to_id);
                            	}

								int comp_from_id = getRandomNumberInRange(2000, (lastID2 + 1));
								int sup_from_id = getRandomNumberInRange(2, 100);
								String q9 = "insert into compose (comp_to_id, comp_from_id, sup_to_id, sup_from_id) values (" + (lastID2 + 1) 
											+ ", " + comp_from_id + ", " + sup_to_id2 + ", " + sup_from_id + ")";
								s.executeQuery(q9);
							}

							System.out.println("Order#" + shipID + " has been shipped!");
						} while(badID == 1);
                    }
					else {
						System.out.println("Please remember to ship the order when the cargo is ready. Your buyer is waiting. ");
					}
					con.commit();
                }
            } while(noMatchOption == 1);


		} catch(SQLException e) {
			try {
				con.rollback();
				System.out.println(e);
				System.out.println("Opps! Something went wrong...Transaction was not successfully. Please try again later. ");
			} catch (SQLException rollbackException) {
				System.out.println("Opps! Something went wrong...Please contact system manager. ");
			}
        }
	}

	public static int getRandomNumberInRange(int min, int max) {
		Random r = new Random();
		int num = r.nextInt((max - min) + 1) + min;
		return num;
	}

}
