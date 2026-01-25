package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(name = "products_table")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Transient
    private String button;
    @Column(name = "product_name")
    private String name;
    @Column(name = "description")
    private String details;
    @Column(name = "price", nullable = false)
    private double price;
    @Column(name = "product_image")
    private String image;

    @Column(name = "category", nullable = false)
    private String category;
    @Column(name = "color")
    private String color;
    @Column(name = "is_available", nullable = false)
    private boolean isAvailable = true;

    // Extra fields for enhanced catalog functionality (not in current table)
    @Transient
    private String sku;  // Stock Keeping Unit
    @Transient
    private boolean isPromotion;  // Is this product on sale?
    @Transient
    private double discountPercent;  // Discount percentage (0-100)
    @Transient
    private boolean isCustomProduct;  // Is this a custom order?
    @Transient
    private String customType;  // Type if custom: "Bridal Bouquet", "Flowering Pot", etc.
    @Transient
    private double priceRangeMin;  // Minimum price for custom products
    @Transient
    private double priceRangeMax;  // Maximum price for custom products
    @Transient
    private String greetingCard;  // Optional greeting card text
   /* @ManyToMany (mappedBy = "products")
    private List<Order> orders;
*/
    public Product(int id, String button, String name, String details, double price) {
        this(name, details, price, null, null, null, true);
        this.button = button;
        this.id = id;
    }

    public Product(String name, String details, double price, String image, String category, String color,
                   boolean isAvailable) {
        this.name = name;
        this.details = details;
        this.price = price;
        this.image = image;
        this.category = category;
        this.color = color;
        this.isAvailable = isAvailable;
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
    public void setID(int newid){
        setId(newid);
    }
    public  int getID(){

        return getId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
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
        return normalizeDiscountPercent(discountPercent);
    }

    public double getNormalizedDiscountPercent() {
        return normalizeDiscountPercent(discountPercent);
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = normalizeDiscountPercent(discountPercent);
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
            return roundCurrency(calculateDiscountedPrice(basePrice, discountPercent));
        }
        return roundCurrency(basePrice);
    }

    public static double normalizeDiscountPercent(double discountPercent) {
        if (discountPercent > 0 && discountPercent <= 1) {
            return discountPercent * 100.0;
        }
        return discountPercent;
    }

    public static double calculateDiscountedPrice(double basePrice, double discountPercent) {
        double normalizedDiscount = normalizeDiscountPercent(discountPercent);
        return basePrice * (1 - normalizedDiscount / 100.0);
    }

    public static double roundCurrency(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
