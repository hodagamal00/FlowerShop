package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Entity
@Table(name = "products_table")
public class Product implements Serializable {

    @Id
    private int id;
    private String button;
    @Column(name = "product_name")
    private String name;
    @Column(name = "product_details")
    private String details;
    @Column(name = "product_price")
    private double price;
    @Column(name = "product_image")
    private String image;
    
    // New fields for enhanced catalog functionality
    @Column(name = "sku")
    private String sku;  // Stock Keeping Unit
    @Column(name = "category")
    private String category;  // e.g., "Bouquet", "Arrangement", "Flowering Pot", "Bridal Bouquet"
    @Column(name = "color")
    private String color;  // e.g., "Red", "White", "Mixed"
    @Column(name = "is_promotion")
    private boolean isPromotion;  // Is this product on sale?
    @Column(name = "discount_percent")
    private double discountPercent;  // Discount percentage (0-100)
    @Column(name = "is_custom_product")
    private boolean isCustomProduct;  // Is this a custom order?
    @Column(name = "custom_type")
    private String customType;  // Type if custom: "Bridal Bouquet", "Flowering Pot", etc.
    @Column(name = "price_range_min")
    private double priceRangeMin;  // Minimum price for custom products
    @Column(name = "price_range_max")
    private double priceRangeMax;  // Maximum price for custom products
    @Column(name = "greeting_card")
    private String greetingCard;  // Optional greeting card text
   /* @ManyToMany (mappedBy = "products")
    private List<Order> orders;
*/
    public Product(int id, String button, String name,String details, double price) {
        super();
        this.button = button;
        this.name = name;
        this.details = details;
        this.price = price;
        this.id = id;

    }

    public Product(){

    }

    // SETTER & GETTERS
    public void setButton(String new_button){
        this.button = new_button;
    }
    public String getButton(){
        return this.button;
    }
    public void setName(String new_name){
        this.name = new_name;
    }
    public String getName(){
        return this.name;
    }
    public void setPrice(double new_price){
        this.price = new_price;
    }
    public double getPrice(){
        return this.price;
    }
    public void setDetails(String new_details){
        this.details = new_details;
    }
    public String getDetails(){
        return this.details;
    }
    public void setImage(String new_image){
        this.image = new_image;
    }
    public String getImage(){
        return this.image;
    }
    public int setID(int newid){
        this.id = newid;
        return  this.id;
    }
    public  int getID(){

        return this.id;
    }

    public void updateid() {
        this.id=id-1;
    }

    // Getters and Setters for new fields
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isPromotion() {
        return isPromotion;
    }

    public void setPromotion(boolean promotion) {
        isPromotion = promotion;
    }

    public boolean hasActivePromotion() {
        return isPromotion && discountPercent > 0;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public boolean isCustomProduct() {
        return isCustomProduct;
    }

    public void setCustomProduct(boolean customProduct) {
        isCustomProduct = customProduct;
    }

    public String getCustomType() {
        return customType;
    }

    public void setCustomType(String customType) {
        this.customType = customType;
    }

    public double getPriceRangeMin() {
        return priceRangeMin;
    }

    public void setPriceRangeMin(double priceRangeMin) {
        this.priceRangeMin = priceRangeMin;
    }

    public double getPriceRangeMax() {
        return priceRangeMax;
    }

    public void setPriceRangeMax(double priceRangeMax) {
        this.priceRangeMax = priceRangeMax;
    }

    public String getGreetingCard() {
        return greetingCard;
    }

    public void setGreetingCard(String greetingCard) {
        this.greetingCard = greetingCard;
    }

    // Helper method to calculate actual price after discount
    public double getActualPrice() {
        double basePrice = price;
        if (hasActivePromotion()) {
            return basePrice * (1 - discountPercent / 100.0);
        }
        return basePrice;
    }
}
