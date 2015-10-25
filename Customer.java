import java.util.*;
/**
 * The Customer class is used to represent a customer that keeps track of its 
 * username and a wish list of products.
 * 
 * @author Beck Hasti, CS 367 instructor, copyright 2014
 */
public class Customer {
    private String username;          // the customer's username      
    private List<String> wishlist;    // the wish list of products
    
    /**
     * Constructs a customer with the given username and an empty wish list.
     * 
     * @param name the username of this customer
     */
    public Customer(String name)     {
        username = name;
        wishlist = new ArrayList<String>();
    }
    
    /**
     * Return the username of this customer.
     * 
     * @return the username of the customer
     */
    public String getUsername() { 
        return username;
    }
    
    /**
     * Return the wish list of products for this customer.
     * 
     * @return the wish list of products
     */
    public List<String> getWishlist() {
        return wishlist;
    }
}

