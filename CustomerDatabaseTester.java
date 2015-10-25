import java.util.*;

public class CustomerDatabaseTester {

	public static void main(String[] args) {
		testEmpty();
		testOne();
		testSmallAddRemove();
		testSmallProduct();
		testLarge();
		testExceptions();

	}
	private static void testEmpty() {
		CustomerDatabase database = new CustomerDatabase();
		
		if (database.size() != 0)			
			System.out.println("size incorrect for empty database");
		
		try {
			if (database.containsProduct("test")) 
				System.out.println("containsProduct incorrect for empty database");
		} catch (Exception e) {
			System.out.println("containsProduct threw exception on empty database");
		}
		
		try {
			if (database.containsCustomer("test")) 
				System.out.println("containsCustomer incorrect for empty database");
		} catch (Exception e) {
			System.out.println("containsCustomer threw exception on empty database");
		}		
		
		try {
			if (database.removeCustomer("test")) 
				System.out.println("removeCustomer incorrect for empty database");
		} catch (Exception e) {
			System.out.println("removeCustomer threw exception on empty database");
		}
		
		try {
			if (database.removeProduct("test")) 
				System.out.println("removeProduct incorrect for empty database");
		} catch (Exception e) {
			System.out.println("removeProduct threw exception on empty database");
		}				
		
		try {
			if (database.hasProduct("test", "test")) 
				System.out.println("hasProduct incorrect for empty database");
		} catch (Exception e) {
			System.out.println("hasProduct threw exception on empty database");
		}
		
		try {
			if (database.getProducts("test") != null) 
				System.out.println("getProducts incorrect for empty database");
		} catch (Exception e) {
			System.out.println("getProducts threw exception on empty database");
		}
	
		try {
			if (database.getCustomers("test") != null) 
				System.out.println("getCustomers incorrect for empty database");
		} catch (Exception e) {
			System.out.println("getCustomers threw exception on empty database");
		}
		
		try {
			Iterator iter = database.iterator();
			if (iter.hasNext())
				System.out.println("iterator has next on empty database");
		} catch (Exception e) {
			System.out.println("iterator threw exception on empty database");
		}
		
		try {
			database.addProduct("test", "test");
			System.out.println("addProduct did not throw exception on empty database");
		} catch (IllegalArgumentException e) {
			// expected
		} catch (Exception e) {
			System.out.println("addProduct threw wrong exception on empty database");
		}
	}

	
	private static void testOne() {
		CustomerDatabase database = new CustomerDatabase();
		try {
			database.addCustomer("customer1");
			if (database.size() != 1) System.out.println("size incorrect when 1 customer");
			
			if (!database.containsCustomer("customer1"))
				System.out.println("containsCustomer incorrect when customer is in database with 1 customer");
			if (database.containsCustomer("customer2"))
				System.out.println("containsCustomer incorrect when customer isn't in database with 1 customer");
			
			try {
			if (database.hasProduct("customer1", "test"))
				System.out.println("hasProduct incorrect with one customer, no products");
			} catch (Exception e) {
				System.out.println("hasProduct threw exception with one customer, no products");
			}
			
			try {
				if (database.getProducts("customer1").size() != 0) 
					System.out.println("getProducts incorrect for customer with no products in database");
			} catch (Exception e) {
				System.out.println("getProducts threw exception on customer with no products in database");
			}
			
			try {
				database.removeCustomer("customer1");
				if (database.size() != 0) System.out.println("size incorrect when only customer removed");
				
				if (database.containsCustomer("customer1"))
					System.out.println("only customer not removed OR containsCustomer incorrect when only customer removed");
			} catch (Exception e) {
				System.out.println("removeCustomer threw unexpected exception");
			}
			
			try {
				if (database.containsProduct("test")) 
					System.out.println("containsProduct incorrect for database with one customer, no products");
			} catch (Exception e) {
				System.out.println("containsProduct threw exception on database with one customer, no products");
			}
			
		} catch (Exception e) {
			System.out.println("addCustomer threw unexpected exception");
		}
	}
	
	private static void testSmallAddRemove() {
		CustomerDatabase database = new CustomerDatabase();
		for (int i = 0; i < 10; i++)
			database.addCustomer("customer" + i);
		
		if (database.size() != 10) System.out.println("size wrong on small-sized database");
		
		try {
			Iterator iter = database.iterator();
			int count = 0;
			while (iter.hasNext()) {
				Customer curr = (Customer)(iter.next());
				if (!curr.getUsername().equalsIgnoreCase("customer" + count))
					System.out.println("items not iterated over in same order as added");
				if (curr.getWishlist().size() != 0) 
					System.out.println("added customer doesn't have 0 products as expected");
				count++;
			}
		} catch (Exception e) {
			System.out.println("unexpected exception while testing iterator");
		}
		
		for (int i = 0; i < 10; i++) {
			if (!database.containsCustomer("customer"+i))
				System.out.println("contains wrong if customer is in small-sized database");
		}
		if (database.containsCustomer("no customer"))
			System.out.println("contains wrong if customer is not in small-sized database");		

		int size, sizeNew;
		try {
			size = database.size();
			database.removeCustomer("customer4"); // remove middle item
			sizeNew = database.size();
			if (size != sizeNew+1)
				System.out.println("size incorrect after removing customer");
			try {
				Iterator iter = database.iterator();
				while (iter.hasNext()) {
					Customer curr = (Customer)(iter.next());
					if (curr.getUsername().equalsIgnoreCase("customer4"))
						System.out.println("removeCustomer did not remove correct customer");
				}	
			} catch (Exception e) {
				System.out.println("unexpected exception while testing iterator");
			}
		} catch (ConcurrentModificationException e) {
			System.out.println("concurrent modification exception while testing remove");
		}


		try {
			database.removeCustomer("customer0"); // remove first item
			try {
				Iterator iter = database.iterator();
				while (iter.hasNext()) {
					Customer curr = (Customer)(iter.next());
					if (curr.getUsername().equalsIgnoreCase("customer0"))
						System.out.println("removeCustomer did not remove correct customer");
				}
			} catch (Exception e) {
			System.out.println("unexpected exception while testing iterator");
			}
		} catch (ConcurrentModificationException e) {
			System.out.println("concurrent modification exception while testing cancel");
		}

		try {
			database.removeCustomer("customer9"); // remove last item
			try {
				Iterator iter = database.iterator();
				while (iter.hasNext()) {
					Customer curr = (Customer)(iter.next());
					if (curr.getUsername().equalsIgnoreCase("customer9"))
						System.out.println("removeCustomer did not remove correct customer");
				}
			} catch (Exception e) {
				System.out.println("unexpected exception while testing iterator");
			}
		} catch (ConcurrentModificationException e) {
			System.out.println("concurrent modification exception while testing remove");
		}

		database = new CustomerDatabase();
		database.addCustomer("new customer");
		size = database.size();
		database.addCustomer("new customer");
		sizeNew = database.size();
		if (size != sizeNew)
			System.out.println("addCustomer if customer already in database changed size");
		try {
			Iterator iter = database.iterator();
		    Customer customer1 = (Customer)(iter.next());
		    Customer customer2 = (Customer)(iter.next());
			if (customer1.getUsername().equalsIgnoreCase(customer2.getUsername()))
					System.out.println("added duplicate customer");
		} catch (NoSuchElementException e) { 
		} catch (Exception e) {
			System.out.println("unexpected exception while testing iterator");
		}
		
		database = new CustomerDatabase();
		for (int i = 0; i < 10; i++) 
			database.addCustomer("customer" + i);
		size = database.size();
		try {
			database.removeCustomer("new customer");
			sizeNew = database.size();
			if (size != sizeNew)
				System.out.println("removeCustomer if customer not in database changed size");
		} catch (Exception e) {
			System.out.println("removeCustomer if customer not in database threw exception");
		}
	}
	
	private static void testSmallProduct() {
		CustomerDatabase database = new CustomerDatabase();
		for (int i = 0; i < 10; i++)
			database.addCustomer("customer" + i);
		
		try {
			for (int i = 0; i < 10; i++) {
				database.addProduct("customer"+i, "prod0");
				database.addProduct("customer"+i, "prod1");
				database.addProduct("customer"+i, "prod2");
			}
		} catch (Exception e) {
			System.out.println("addProduct threw unexpected exception");
		}
		
		for (int i = 0; i < 3; i++) {
			if (!database.containsProduct("prod"+i))
				System.out.println("contains wrong if product is in small-sized database");
		}
		if (database.containsProduct("no prod"))
			System.out.println("contains wrong if product is not in small-sized database");
			
			
		try {  // add product to customer not in database
			database.addProduct("no customer", "prod1");
			System.out.println("addProduct did not throw exception when customer not in database");
		} catch (IllegalArgumentException e) {
			// expected
		} catch (Exception e) {
			System.out.println("addProduct threw wrong exception when customer not in database");
		}
		
		try {
			for (int i = 0; i < 10; i++) {
				List prods = database.getProducts("customer"+i);
				if (prods.size() != 3) 
					System.out.println("expected 3 items in products - addProduct or getProducts incorrect");
				for (int j = 0; j < prods.size(); j++) {
					if (!prods.contains("prod"+j) && !prods.contains("PROD"+j))
						System.out.println("expected item not in products - addProduct or getProducts incorrect");
				}
			}
		} catch (Exception e) {
			System.out.println("getProducts threw unexpected exception");
		}
		
		try {
			Iterator iter = database.iterator();
			while (iter.hasNext()) {
				Customer customer = (Customer) iter.next();
				if (customer.getWishlist() != database.getProducts(customer.getUsername()))
					System.out.println("addProduct incorrect - doesn't add to customer objects");
			}
		} catch (Exception e) {
			System.out.println("iterator/getProducts threw unexpected exception");
		}
		
		try {
			for (int i = 0; i < 10; i++)
				if (!database.hasProduct("customer"+i, "prod2"))
					System.out.println("hasProduct doesn't return true if it does contain a customer");
		} catch (Exception e) {
			System.out.println("hasProduct threw unexpected exception");
		}
		
		if (database.hasProduct("customer3", "noprod1"))
			System.out.println("hasProduct doesn't return false if it does not contain a customer");
			
		try { // remove product
			boolean result = database.removeProduct("no prod");
			if (result)
				System.out.println("removeProduct returned true when non-existent product removed");
			result = database.removeProduct("prod1");
			if (!result)
				System.out.println("removeProduct didn't return true when existing product removed - addProduct or removeProduct incorrect");
			for (int i = 0; i < database.size(); i++) {
				if (database.getProducts("customer"+i).contains("prod1"))
					System.out.println("removeProduct did not remove product from customer");
			}
		} catch (Exception e) {
			System.out.println("removeProduct threw unexpected exception");
		}
		
		try {
			database = new CustomerDatabase();
			database.addCustomer("customer0");
			database.addProduct("customer0", "prod0");
			database.addCustomer("customer1");
			database.addProduct("customer1", "prod0");
			int size = database.size();
			database.removeProduct("prod0");
			if (size != database.size())
				System.out.println("removeProduct removed customer as well");
			if (!database.containsCustomer("customer0"))
				System.out.println("removeProduct removed customer as well");
		} catch (Exception e) {
			System.out.println("removing all products threw unexpected exception");
		}

	}
	
	private static void testLarge() {
		CustomerDatabase database = new CustomerDatabase();
		int bigSize = 10000;
		int i = 0;
		
		// add 10,000 customers
		try {
			for (i = 0; i < bigSize; i++)
				database.addCustomer("P"+i);	
			if (database.size() != bigSize) 
				System.out.println("size incorrect for large database");
		} catch (Exception e) {
			System.out.println("addCustomer threw exception when adding item " + i);
		}
		
		try {
			for (i = 0; i < bigSize; i++)
				if (!database.containsCustomer("P"+i))	 
					System.out.println("contains incorrect for customer in large database");
		} catch (Exception e) {
			System.out.println("contains threw exception when checking item " + i);
		}
		
		// remove 10,000 customers
		try {
			for (i = 0; i < bigSize; i++)
				database.removeCustomer("P"+i);	
			if (database.size() != 0) 
				System.out.println("size incorrect for large database");
		} catch (ConcurrentModificationException e) {
			System.out.println("removeCustomer threw concurrent modification exception");
		} catch (Exception e) {
			System.out.println("removeCustomer threw unexpected exception when removing item " + i);
		}
	}
	
	private static void testExceptions() {
		CustomerDatabase database = new CustomerDatabase();
		for (int i = 0; i < 5; i++)
			database.addCustomer("customer" + i);

		try {
			database.containsProduct(null);
			System.out.println("containsProduct(null) didn't throw exception");
		} catch (IllegalArgumentException e) {	
		} catch (Exception e) {
			System.out.println("containsProduct(null) threw wrong exception type");
		}
		
		try {
			database.containsCustomer(null);
			System.out.println("containsCustomer(null) didn't throw exception");
		} catch (IllegalArgumentException e) {	
		} catch (Exception e) {
			System.out.println("containsCustomer(null) threw wrong exception type");
		}
		
		try {
			database.addCustomer(null);
			System.out.println("addCustomer(null) didn't throw exception");
		} catch (IllegalArgumentException e) {	
		} catch (Exception e) {
			System.out.println("addCustomer(null) threw wrong exception type");
		}
		
		try {
			database.removeCustomer(null);
			System.out.println("removeCustomer(null) didn't throw exception");
		} catch (IllegalArgumentException e) {	
		} catch (Exception e) {
			System.out.println("removeCustomer(null) threw wrong exception type");
		}
		
		try {
			database.addProduct("customer1", null);
			System.out.println("addProduct(c, null) didn't throw exception");
		} catch (IllegalArgumentException e) {	
		} catch (Exception e) {
			System.out.println("addProduct(c, null) threw wrong exception type");
		}
		try {
			database.addProduct(null, "C1");
			System.out.println("addProduct(null, c) didn't throw exception");
		} catch (IllegalArgumentException e) {	
		} catch (Exception e) {
			System.out.println("addProduct(null, c) threw wrong exception type");
		}	
		try {
			database.addProduct("T1", "P1");
			System.out.println("addProduct(c, p), c not in database, didn't throw exception");
		} catch (IllegalArgumentException e) {	
		} catch (Exception e) {
			System.out.println("addProduct(c, p), c not in database, threw wrong exception type");
		}
		
		try {
			database.hasProduct("customer1", null);
			System.out.println("hasProduct(c, null) didn't throw exception");
		} catch (IllegalArgumentException e) {	
		} catch (Exception e) {
			System.out.println("hasProduct(c, null) threw wrong exception type");
		}
		try {
			database.hasProduct(null, "P1");
			System.out.println("hasProduct(null, p) didn't throw exception");
		} catch (IllegalArgumentException e) {	
		} catch (Exception e) {
			System.out.println("hasProduct(null, p) threw wrong exception type");
		}	
		
		try {
			database.getProducts(null);
			System.out.println("getProducts(null) didn't throw exception");
		} catch (IllegalArgumentException e) {	
		} catch (Exception e) {
			System.out.println("getProducts(null) threw wrong exception type");
		}
		try {
			database.getCustomers(null);
			System.out.println("getCustomers(null) didn't throw exception");
		} catch (IllegalArgumentException e) {	
		} catch (Exception e) {
			System.out.println("getCustomers(null) threw wrong exception type");
		}
		
		try {
			database.removeProduct(null);
			System.out.println("removeProduct(null) didn't throw exception");
		} catch (IllegalArgumentException e) {	
		} catch (Exception e) {
			System.out.println("removeProduct(null) threw wrong exception type");
		}
	}

}
