import java.sql.*;
import java.util.Scanner;

public class StoreManager {

  public static void productOnShelf(Statement s) {
    String q;
    ResultSet result;
    try {
      q = "select gen_id ID, gen_name Name, category Department from General_Product where End_Product_Flag = 1";
      result = s.executeQuery(q);
      if(!result.next()){
        System.out.println ("No records found. Regork is currently selling nothing. ");
      }
      else{
	System.out.println("Here is the list of products that Regork currently sells: ");
        System.out.println(String.format("%-8s", "ID") + String.format("%-32s", "Name") + String.format("%-24s", "Department"));
        do{
          System.out.println (String.format("%-8s", result.getString("ID")) + " " + String.format("%-30s", result.getString("Name")) + " " + String.format("%-20s", result.getString("Department")));
        } while (result.next());
      }
    } catch(SQLException e) {
        System.out.println("Opps! Something went wrong...Please try again later. ");
    }
  }

 public static void allProducts(Statement s) {
    String q;
    ResultSet result;
    try {
      q = "select gen_id ID, gen_name Name, category Department from General_Product order by gen_id";
      result = s.executeQuery(q);
      if(!result.next()){
        System.out.println ("No records found. No available products. ");
      }
      else{
    	System.out.println("Here is the list of avaliable products from all suppliers: ");
        System.out.println(String.format("%-8s", "ID") + String.format("%-32s", "Name") + String.format("%-24s", "Department"));
        do{
          System.out.println (String.format("%-8s", result.getString("ID")) + " " + String.format("%-30s", result.getString("Name")) + " " + String.format("%-30s", result.getString("Department")));
        } while (result.next());
      }
    } catch(SQLException e) {
        System.out.println("Opps! Something went wrong...Please try again later. ");
    }
  }

  public static void searchById(Statement s) {
	Scanner scan = new Scanner(System.in);
	try {
	  int badID = 1;
	  System.out.println("Please enter the Supplier ID (between 1 and 100) that you want to contact: ");
	  do {
        if(!scan.hasNextInt()){
          System.out.println("Please enter an integer between 1 and 100: ");
          scan.next();
          badID = 1;
          continue;
        }
        int ID = scan.nextInt();
        if(ID < 1 || ID > 100) {
          System.out.println("Please enter an integer between 1 and 100: ");
          badID = 1;
          continue;
        }
        String q2;
        ResultSet result2;
   	    q2 = "select supplier.sup_id ID, sup_name Name, phone_num Phone#, address.street Street, address.city City, address.state State, zip Zip " +
		     "from supplier inner join located_at on supplier.sup_id = located_at.sup_id inner join phone_number on supplier.sup_id = phone_number.sup_id, address " +
		     "where supplier.sup_id = " + ID + " and located_at.street = address.street and located_at.city = address.city and located_at.state = address.state";
        result2 = s.executeQuery(q2);

		if(!result2.next()){
          String q3;
          ResultSet result3;
          q3 = "select sup_name from Supplier where id = " + ID;
          result3 = s.executeQuery(q3);
          if(!result3.next()){
            System.out.println ("No Supplier with ID " + ID);
          }
          badID = 0;
        }
        else {
          badID = 0;
          System.out.println("Contact information for Supplier " + ID + " " + result2.getString("Name") + ": ");
          System.out.println(String.format("%-8s", "ID") + String.format("%-24s", "Name") + String.format("%-18s", "Phone#") + String.format("%-26s", "Street") + String.format("%-10s", "City") + String.format("%-10s", "State") + String.format("%-8s", "Zip"));
          do {
            System.out.println (String.format("%-8s", result2.getString("ID")) + " " + String.format("%-20s", result2.getString("Name")) + " " + String.format("%-15s", result2.getString("Phone#")) + " " + String.format("%-30s", result2.getString("Street")) + " " + String.format("%-10s", result2.getString("City")) + " " + String.format("%-10s", result2.getString("State")) + " " + String.format("%-8s", result2.getString("Zip")));
          }while(result2.next());
        }
	  } while(badID == 1);
 	} catch(SQLException e) {
		System.out.println(e);
        System.out.println("Opps! Something went wrong...Please try again later. ");
    }
  }

  public static void searchByName(Statement s) {
	System.out.println("Input name search substring: ");
    int noMatchName =0;
	Scanner scan = new Scanner(System.in);

    try{
      do{
        String substring;
        substring = scan.nextLine();
        if(!substring.matches("[a-zA-Z]*")){
          System.out.println("Special characters or numbers are not allowed in a search string. Please re-enter.");
          noMatchName = 1;
        }
        else{
          String q;
          ResultSet result;
          q = "select supplier.sup_id ID, sup_name Name, phone_num Phone#, address.street Street, address.city City, address.state State, zip Zip " + 
		   	"from supplier inner join located_at on supplier.sup_id = located_at.sup_id inner join phone_number on supplier.sup_id = phone_number.sup_id, address " +
			"where supplier.sup_name like '%" + substring + "%' and located_at.street = address.street and located_at.city = address.city and located_at.street = address.street";
          result = s.executeQuery(q);

          if(!result.next()) {
             System.out.println ("No matches. Please re-enter: ");
             noMatchName = 1;
          }
          else{
            System.out.println("Here is a list of all matching suppliers");
 	     	System.out.println(String.format("%-8s", "ID") + String.format("%-24s", "Name") + String.format("%-18s", "Phone#") + String.format("%-26s", "Street") + String.format("%-10s", "City") + String.format("%-10s", "State") + String.format("%-8s", "Zip")); 
            do{
			  System.out.println (String.format("%-8s", result.getString("ID")) + " " + String.format("%-20s", result.getString("Name")) + " " + String.format("%-15s", result.getString("Phone#")) + " " + String.format("%-30s", result.getString("Street")) + " " + String.format("%-10s", result.getString("City")) + " " + String.format("%-10s", result.getString("State")) + " " + String.format("%-8s", result.getString("Zip")));
              noMatchName = 0;
            } while (result.next());
          }
        }
      } while(noMatchName == 1);
    } catch(SQLException e) {
        System.out.println(e);
        System.out.println("Opps! Something went wrong...Please try again later. ");
    }
  }

  public static void searchByProduct(Statement s) {
	Scanner scan = new Scanner(System.in);
    try {
		String lastID = "";
        int lastID2 = 0;
        String qID;
        ResultSet resultID;
        qID = "select gen_id from General_Product order by gen_id desc";
        resultID = s.executeQuery(qID);
        if(!resultID.next()){}
        else {
          lastID = resultID.getString("gen_id");
          lastID2 = Integer.parseInt(lastID);
        }


      int badID = 1;
      System.out.println("Please enter the product ID (between 1000 and " + lastID2 + ") that you want to check for suppliers: ");
      do {
        if(!scan.hasNextInt()){
          System.out.println("Please enter an integer between 1000 and " + lastID2 + ": ");
          scan.next();
          badID = 1;
          continue;
        }
        int ID = scan.nextInt();
        if(ID < 1000 || ID > lastID2) {
          System.out.println("Please enter an integer between 1000 and " + lastID2 + ": ");
          badID = 1;
          continue;
        }

		String q2;
        ResultSet result2;
		q2 = "select supplier.sup_id ID, sup_name Name, phone_num Phone#, address.street Street, address.city City, address.state State, zip Zip " +
			"from supplier inner join located_at on supplier.sup_id = located_at.sup_id inner join phone_number on supplier.sup_id = phone_number.sup_id inner join manufactures on supplier.sup_id = manufactures.sup_id, address " + 
			"where comp_id = " + ID + " and located_at.street = address.street and located_at.city = address.city and located_at.street = address.street";
        result2 = s.executeQuery(q2);

        if(!result2.next()){
          String q3;
          ResultSet result3;
          q3 = "select sup_name from Supplier natural join Manufactures where comp_id = " + ID;
          result3 = s.executeQuery(q3);
          if(!result3.next()){
            System.out.println ("No product with ID " + ID);
          }
          badID = 0;
        }
        else {
          badID = 0;
          System.out.println("Contact information for Supplier suppling product " + ID + " " + ": ");
          System.out.println(String.format("%-8s", "ID") + String.format("%-24s", "Name") + String.format("%-18s", "Phone#") + String.format("%-26s", "Street") + String.format("%-10s", "City") + String.format("%-10s", "State") + String.format("%-8s", "Zip"));
          do {
            System.out.println (String.format("%-8s", result2.getString("ID")) + " " + String.format("%-20s", result2.getString("Name")) + " " + String.format("%-15s", result2.getString("Phone#")) + " " + String.format("%-30s", result2.getString("Street")) + " " + String.format("%-10s", result2.getString("City")) + " " + String.format("%-10s", result2.getString("State")) + " " + String.format("%-8s", result2.getString("Zip")));
          }while(result2.next());
        }
      } while(badID == 1);
    } catch(SQLException e) {
        System.out.println(e);
        System.out.println("Opps! Something went wrong...Please try again later. ");
    }
  }

  public static void bestSupplier(Statement s) {
	try {
		String lastID = "";
        int lastID2 = 0;
        String qID;
        ResultSet resultID;
        qID = "select gen_id from General_Product order by gen_id desc";
        resultID = s.executeQuery(qID);
        if(!resultID.next()){}
        else {
          lastID = resultID.getString("gen_id");
          lastID2 = Integer.parseInt(lastID);
        }


	  Scanner scan = new Scanner(System.in);
	  int badID = 1;
      System.out.println("Please enter the product ID (between 1000 and " + lastID2 + ") that you want to check for suppliers: ");
      do {
        if(!scan.hasNextInt()){
          System.out.println("Please enter an integer between 1000 and " + lastID2 + ": ");
          scan.next();
          badID = 1;
          continue;
        }
        int ID = scan.nextInt();
        if(ID < 1000 || ID > lastID2) {
          System.out.println("Please enter an integer between 1000 and " + lastID2 + ": ");
          badID = 1;
          continue;
        }

        String q2;
        ResultSet result2;
		q2 = "select supplier.sup_id ID, sup_name Name, price Price, rank() over (order by price) Rank " +
			 "from supplier inner join manufactures on supplier.sup_id = manufactures.sup_id " +
			 "where comp_id = " + ID;
		result2 = s.executeQuery(q2);

        if(!result2.next()){
          String q3;
          ResultSet result3;
          q3 = "select sup_name from Supplier natural join Manufactures where comp_id = " + ID;
          result3 = s.executeQuery(q3);
          if(!result3.next()){
            System.out.println ("No product with ID " + ID);
          }
          badID = 0;
        }
        else {
          badID = 0;
          System.out.println("Here is a list of suppliers suppling product " + ID  + ". The results are ranked based on price from lowest to highest.");
          System.out.println(String.format("%-8s", "ID") + String.format("%-15s", "Name") + String.format("%-10s", "Price") + String.format("%-5s", "Rank"));
          do {
            System.out.println (String.format("%-8s", result2.getString("ID")) + " " + String.format("%-15s", result2.getString("Name")) + " " + String.format("%-10s", result2.getString("Price")) + String.format("%-5s", result2.getString("Rank")));
          }while(result2.next());
        }
      } while(badID == 1);
	} catch(SQLException e) {
        System.out.println("Opps! Something went wrong...Please try again later. ");
    }
  }


  public static int makeOrder(Statement s, Connection con) {
	 int isAlreadyEndPro = 0;
	 try {
	   String lastID = "";
       int lastID2 = 0;
       String qID;
       ResultSet resultID;
       qID = "select gen_id from General_Product order by gen_id desc";
       resultID = s.executeQuery(qID);
       if(!resultID.next()){}
       else {
         lastID = resultID.getString("gen_id");
         lastID2 = Integer.parseInt(lastID);
       }

      Scanner scan = new Scanner(System.in);
      int badID = 1;
      System.out.println("Please enter the product ID (between 1000 and " + lastID2 +") that you want to order: ");
      do {
        if(!scan.hasNextInt()){
          System.out.println("Please enter an integer between 1000 and " + lastID2 + ": ");
          scan.next();
          badID = 1;
          continue;
        }
        int ID = scan.nextInt();
        if(ID < 1000 || ID > lastID2) {
          System.out.println("Please enter an integer between 1000 and " + lastID2 + ": ");
          badID = 1;
          continue;
        }

		String q2;
        ResultSet result2;
		q2 = "select sup_id Supplier#, sup_name Name, comp_id Product#, price Unit_Price, quantity Avaliability " +
			"from Manufactures natural join Supplier " +
			"where quantity > 0 and comp_id = " + ID;
        result2 = s.executeQuery(q2);

		String supName = " " , uPrice = " " , proID = " " ;
		double uPrice2 = 0;
		String q3 = "select sup_id from Supplier natural join Manufactures where comp_id = " + ID;

        if(!result2.next()){
          ResultSet result3;
          result3 = s.executeQuery(q3);
          if(!result3.next()){
            System.out.println ("No product with ID " + ID);
          }
		  System.out.println("Unfortunately, the product you chose is currently out of stock from all the suppliers.");
          badID = 0;
        }
        else {
          badID = 0;
          System.out.println("Here is a list of suppliers for the product you chose (the system has intentionally omitted the supplier(s) whose product you chose is currently out of stock): ");
          System.out.println(String.format("%-8s", "Supplier#") + String.format("%-15s", "Name") + String.format("%-10s", "Product#") + String.format("%-20s", "Unit_Price") + String.format("%-10s", "Avaliability"));
          do {
			proID = result2.getString("Product#");
    		int proID2 = Integer.parseInt(proID);
            System.out.println (String.format("%-8s", result2.getString("Supplier#")) + " " + String.format("%-15s", result2.getString("Name")) + " " + String.format("%-10s", result2.getString("Product#")) + " " + String.format("%-20s", result2.getString("Unit_Price")) + " " + String.format("%-10s", result2.getString("Avaliability")));
			if(proID2==ID) {
				isAlreadyEndPro = 1;
			}
          }while(result2.next());

		  System.out.println("Which supplier do you want to make order from? Please enter the supplier ID. ");
		  int badID2 = 0;
		  int supID = 0;
		  Scanner scan2 = new Scanner (System.in);
		  do {
	        if(!scan2.hasNextInt()){
          	  System.out.println("Please enter an integer between 1 and 100: ");
              scan2.next();
              badID2 = 1;
              continue;
            }
			supID = scan2.nextInt();

			int row2 = 0;
            String q5 = "select count(*) from Supplier natural join Manufactures where comp_id = " + ID;
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
			String q6 = q3;
            ResultSet result6 = s.executeQuery(q6);
            if(!result6.next()) {}
            else {
              do {
                int sup2 = 0;
                String sup = " ";
                sup = result6.getString("sup_id");
                sup2 = Integer.parseInt(sup);
                if(supID == sup2) {
                	badID2 = 0;
                    break;
                }
                count++;
              } while(result6.next());
            }

            if(count == row2) {
              badID2 = 1;
              System.out.println("The number you entered does not in the list above. Please re-enter: ");
              continue;
            }

			  String q4;
          	  ResultSet result4;
          	  q4 = "select sup_name, price from Supplier natural join Manufactures where comp_id = " + ID + " and sup_id = " + supID;
              result4 = s.executeQuery(q4);
          	  if(!result4.next()){}
			  else {
				do {
					supName = result4.getString("sup_name");
            		uPrice = result4.getString("price");
            		uPrice2 = Double.parseDouble(uPrice);
				} while(result4.next());
			  }
			  makeOrder(s, con, supID, ID, supName, uPrice2);
		  } while (badID2 == 1);
        }
      } while(badID == 1);
    } catch(SQLException e) {
		System.out.println(e);
        System.out.println("Opps! Something went wrong...Please try again later. ");
    }
	return isAlreadyEndPro;
  }


  public static void makeOrder(Statement s, Connection con, int supID, int proID, String supName, double uPrice) {
    try {
      System.out.println("How many product#" + proID + " do you want to order? ");
  	  Scanner scan = new Scanner (System.in);
      String q2, quant = "";
      int quant2 = 0;
      ResultSet result2;
	  q2 = "select quantity Quantity from Manufactures where comp_id = " + proID + " and sup_id = " + supID;
      result2 = s.executeQuery(q2);
	  if(!result2.next()) {}
	  else {
        do {
	      quant = result2.getString("Quantity");
		  quant2 = Integer.parseInt(quant);
	    } while(result2.next());
	  }

	  int quantity = 0;
      int badQuant = 0;
      do {
	    if(!scan.hasNextInt()) {
          System.out.println("Please enter an integer: ");
          scan.next();
          badQuant = 1;
          continue;
        }
	    quantity = scan.nextInt();
        if(quantity > quant2 || quantity <= 0) {
          System.out.println("Number is not within the range of avaliability. Please re-enter an integer between 0 and " + quant2 + ": ");
          badQuant = 1;
          continue;
        }
		else { badQuant = 0;}
	  } while(badQuant == 1);

	  String lastID = "";
      int lastID2 = 0;
      String q;
      ResultSet result;
      q = "select ship_id from shipment order by ship_id desc";
      result = s.executeQuery(q);
      if(!result.next()){}
      else {
      	lastID = result.getString("ship_id");
        lastID2 = Integer.parseInt(lastID);
      }

	  String proName = " " ;
	  String qP;
      ResultSet resultP;
      qP = "select gen_name from general_product where gen_id = " + proID;
      resultP = s.executeQuery(qP);
      if(!resultP.next()){}
      else { proName = resultP.getString("gen_name");}

	  System.out.println("Processing your order...");
	  String q1;
	  q1 = "insert into SHIPMENT (SHIP_ID, leave_time, arrive_time) values (" + (lastID2+1) + ", NULL, NULL)";
      s.executeQuery(q1);

	  String q3;
	  q3 = "Update manufactures Set quantity = " + (quant2 - quantity) + " Where sup_id = " + supID + " and comp_id = " + proID;
      s.executeQuery(q3);

	  String q5;
	  q5 = "insert into SHIP_GEN (ship_id, gen_id, sup_id, price, quantity) values (" + (lastID2+1) + ", " + proID + ", " +
			supID + ", " + uPrice + ", " + quantity + ")";
	  s.executeQuery(q5);

	  String des_city = " ", des_street = " ", des_state = " ";
	  String q7 = "select street, city, state from located_at natural join Supplier where sup_name = 'Regork'";
	  ResultSet result7;
	  result7 = s.executeQuery(q7);
	  if(!result7.next()){}
	  else {
		do {
			des_street = result7.getString("street");
			des_city = result7.getString("city");
			des_state = result7.getString("state");
		} while(result7.next());
	  }


	  String q6;
	  q6 = "insert into DESTINATION (ship_id, street, city, state) values (" + (lastID2+1) + ", '" + des_street + "', '" + des_city + "', '" + 
			des_state + "')";
	  s.executeQuery(q6);

	  String src_city = " ", src_street = " ", src_state = " ";
      String q9 = "select street, city, state from located_at natural join supplier where sup_id = " + supID;
      ResultSet result9;
      result9 = s.executeQuery(q9);
      if(!result9.next()){}
      else {
        do {
            src_street = result9.getString("street");
            src_city = result9.getString("city");
            src_state = result9.getString("state");
        } while(result9.next());
      }

	  String q8;
      q8 = "insert into SOURCE (ship_id, street, city, state) values (" + (lastID2+1) + ", '" + src_street + "', '" + src_city + "', '" + 
            src_state + "')";
      s.executeQuery(q8);

	  System.out.println("The supplier#" + supID + " has received your order. ");
	  System.out.println("Here is your order summary: ");
	  System.out.println(String.format("%-15s", "Supplier_ID") + " " + String.format("%-30s", "Supplier_Name") + " " + 
						String.format("%-15s", "Product_ID") + " " + String.format("%-40s", "Product_Name") + " " + 
						String.format("%-10s", "Unit_Price") + " " + String.format("%-10s", "Quantity") + " " + String.format("%-25s","Total_Price"));
	  System.out.println(String.format("%-15s", supID) + " " + String.format("%-30s", supName) + " " + String.format("%-15s", proID) + " " + 
						String.format("%-40s", proName) + " " + String.format("%-10s", uPrice) + " " + String.format("%-10s", quantity) + " " + 
						String.format("%-25s", round(uPrice*quantity, 2)));
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
  }


  public static void recall(Statement s, Statement s2, Connection con) {
	try {
		String lastID = "";
        int lastID2 = 0;
        String qID;
        ResultSet resultID;
        qID = "select spec_id from Specific_Product order by spec_id desc";
        resultID = s.executeQuery(qID);
        if(!resultID.next()){}
        else {
          lastID = resultID.getString("spec_id");
          lastID2 = Integer.parseInt(lastID);
        }


      Scanner scan = new Scanner(System.in);
      int badID = 0;
      int isAlreadyEndPro = 0;
      System.out.println("Please enter the product ID (between 2000 and " + lastID2 + ") that you want to recall: ");
      do {
        if(!scan.hasNextInt()){
          System.out.println("Please enter an integer between 2000 and " + lastID2 + ": ");
          scan.next();
          badID = 1;
          continue;
        }
        int ID = scan.nextInt();
        if(ID < 2000 || ID > lastID2) {
          System.out.println("Please enter an integer between 2000 and " + lastID2 + ": ");
          badID = 1;
          continue;
        }

	    badID = 0;

		//get genID
                String genID = " ";
                int genID2 = 0;
                String q3 = "select gen_id from specific_product natural join an_instance_of where spec_id = " + ID;
                ResultSet result3 = s.executeQuery(q3);
                if(!result3.next()){}
                else {
                    do {
                        genID = result3.getString("gen_id");
                        genID2 = Integer.parseInt(genID);
                    } while (result3.next());
                }

		System.out.println("Here is the production chain of product#" + ID + ". It is being traced down from itself to its root as indicated below from top to bottom. ");

		String q;
		ResultSet result;
		q = "select comp_to_id Component_ID, gen_name Component_Name, sup_to_id Supplier_ID, sup_name Supplier_Name " +
			"from compose inner join supplier on compose.sup_to_id = supplier.sup_id " +
			"inner join specific_product on compose.comp_to_id = specific_product.spec_id " +
            "inner join an_instance_of using (spec_id) inner join general_product using(gen_id) " +
            "where comp_to_id = " + ID;
		result = s.executeQuery(q);
        if(!result.next()){}
        else {
			System.out.println(String.format("%-20s","Component_ID") + " " + String.format("%-45s","Component_Name") + " " + String.format("%-20s","Supplier_ID") + " " + String.format("%-30s","Supplier_Name"));
			//do {
				String specID2 = result.getString("Component_ID");
				int specID3 = Integer.parseInt(specID2);

                String comp_name = result.getString("Component_Name");

                String supID = result.getString("Supplier_ID");
                int supID2 = Integer.parseInt(supID);

                String sup_name = result.getString("Supplier_Name");

				//set end_pro_flag to 0 for comp_to_id
                String q2 = "Update General_Product SET end_product_flag = 0 Where gen_id = " + genID2;
                s.executeQuery(q2);

				System.out.println(String.format("%-20s", specID2) + " " + String.format("%-50s", comp_name) + " " + String.format("%-20s", supID2) + " " + String.format("%-20s", sup_name));
			//} while (result.next());
		}

 	    recursiveRecall(s, s2, con, ID);
		System.out.println("Removing them from store shelves...... ");
		System.out.println("They are removed from store shelves along with other same products. ");
	  } while(badID == 1);
		productOnShelf(s);
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

  public static void recursiveRecall(Statement s, Statement s2, Connection con, int specID) {
	try {
		//get genID
                String genID = " ";
                int genID2 = 0;
                String q3 = "select gen_id from specific_product natural join an_instance_of where spec_id = " + specID;
                ResultSet result3 = s.executeQuery(q3);
                if(!result3.next()){}
                else {
                    do {
                        genID = result3.getString("gen_id");
                        genID2 = Integer.parseInt(genID);
                    } while(result3.next());
                }

		String specID2 = " ", supID = " ";
		int specID3 = 0, supID2 = 0;
		String comp_name = " ", sup_name = " ";
		String q;
		ResultSet result;
		q = "select comp_from_id Component_ID, gen_name Component_Name, sup_from_id Supplier_ID, sup_name Supplier_Name " +
			"from compose inner join supplier on compose.sup_from_id = supplier.sup_id " +
			"inner join specific_product on compose.comp_from_id = specific_product.spec_id " +
			"inner join an_instance_of using (spec_id) inner join general_product using(gen_id) " +
			"where comp_to_id = " + specID;
		result = s.executeQuery(q);
		if(!result.next()){}
		else {
			do {
				specID2 = result.getString("Component_ID");
    			specID3 = Integer.parseInt(specID2);

                //set end_pro_flag to 0 for comp_to_id
                String q2 = "Update General_Product SET end_product_flag = 0 Where gen_id = " + genID2;
                s2.executeQuery(q2);

				comp_name = result.getString("Component_Name");

				supID = result.getString("Supplier_ID");
				supID2 = Integer.parseInt(supID);

				sup_name = result.getString("Supplier_Name");
			} while (result.next());
		}
		if(specID3 == specID) {
			return;
		}
		else {
		  specID = specID3;
		  System.out.println(String.format("%-20s", specID2) + " " + String.format("%-50s", comp_name) + " " + String.format("%-20s", supID2) + " " + String.format("%-20s", sup_name));
		  recursiveRecall(s, s2, con, specID3);
		}
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

  public static void confirm(Statement s, Connection con, int isAlreadyEndPro) {
	try {
		String q;
		ResultSet result;
		q = "select ship_id, sup_id, sup_name, gen_id, gen_name, total_price, arrive_time, leave_time " + 
			"from shipment natural join ship_gen inner join supplier using (sup_id) inner join general_product using(gen_id) " +
			"where arrive_time is NULL and leave_time is not NULL";
		result = s.executeQuery(q);
		if(!result.next()) {
			System.out.println("There is no new order that has not yet been confirmed. Please check back later. ");
        }
        else {
            System.out.println("You have at lease one unconfirmed order. Please see below and confirm as soon as possible. ");
            System.out.println(String.format("%-10s","Shipment_ID") + " " + 
                        String.format("%-10s", "Supplier_ID") + " " + String.format("%-20s", "Supplier_Name") + " " + 
                        String.format("%-10s", "Product_ID") + " " + String.format("%-35s", "Product_Name") + " " + 
                        String.format("%-10s","Total_Price") + " " + String.format("%-15s", "Leave_Time") + 
						String.format("%-25s", "Arrive_Time"));
			do {
				System.out.println(String.format("%-10s", result.getString("ship_id")) + " " + 
                        String.format("%-15s", result.getString("sup_id")) + " " + String.format("%-25s", result.getString("sup_name")) + " " +
                        String.format("%-6s", result.getString("gen_id")) + " " + String.format("%-30s", result.getString("gen_name")) + " " +
                        String.format("%-10s",result.getString("total_price")) + " " + String.format("%-20s",result.getString("leave_time")) + " " +
						String.format("%-25s",result.getString("arrive_time")));
			} while(result.next());
			setArrive(s, con, isAlreadyEndPro);
        }
	} catch(SQLException e) {
        System.out.println("Opps! Something went wrong...Please try again later. ");
    }
  }

  public static void setArrive(Statement s, Connection con, int isAlreadyEndPro) {
 	try {
		int badID = 0;
        int noMatchOption = 0;
        System.out.println("Would you like to confirm a shipment now? (Y/N)");
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
                 System.out.println("Please enter the shipment ID that you want to confirm now: ");
                 do {
                    if(!scan2.hasNextInt()){
                      System.out.println("Please enter an integer(shipment ID) that is among your unconfirmed order: ");
                      scan2.next();
                      badID = 1;
                      continue;
                    }
                    int shipID = scan2.nextInt();

					int row2 = 0;
					String q5 = "select count(*) from shipment where arrive_time is NULL and leave_time is not NULL";
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
					String q3 = "select ship_id from shipment where arrive_time is NULL and leave_time is not NULL";
					ResultSet result3 = s.executeQuery(q3);
					if(!result3.next()) {}
					else {
						do {
							int ship2 = 0;
							String ship = " ";
							ship = result3.getString("ship_id");
							ship2 = Integer.parseInt(ship);
							if(shipID == ship2) {
								badID = 0;
								break;
							}
							count++;
						} while(result3.next());
					}

					if(count == row2) {
						badID = 1;
						System.out.println("Please enter an integer(shipment ID) that is among your unconfirmed order: ");
						continue;
					}


                    System.out.println("Processing the confirmation......");
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

                    q = "Update shipment Set arrive_time = to_date('" + date + "','DD/MM/YYYY') where ship_id = " + shipID;
                    s.executeQuery(q);

					int proID2 = 0;
					String proID = " ";
					String q2 = "select gen_id from ship_gen where ship_id = " + shipID;
					ResultSet result2 = s.executeQuery(q2);
					if(!result2.next()) {}
                    else {
                      do {
							proID = result2.getString("gen_id");
          					proID2 = Integer.parseInt(proID);
                      } while(result2.next());
                    }

					if(isAlreadyEndPro == 0) {
        				String q4;
        				q4 = "Update general_product Set end_product_flag = 1 Where gen_id = " + proID2 ;
        				s. executeQuery(q4);
      				}

                    System.out.println("Order#" + shipID + " has been confirmed for the receipt. Happy transaction!");
                 } while(badID == 1);
              }
              else {
                    System.out.println("Please remember to confirm the order when the cargo is received. Your supplier is waiting. ");
              }
              con.commit();
           }
        } while(noMatchOption == 1);
	} catch(SQLException e) {
      try {
         con.rollback();
         System.out.println("Opps! Something went wrong...Transaction was not successfully. Please try again later. ");
      } catch (SQLException rollbackException) {
         System.out.println("Opps! Something went wrong...Please contact system manager. ");
      }
    }
  }


  public static double round(double value, int places) {
    double scale = Math.pow(10, places);
    return Math.round(value * scale) / scale;
  }


}
