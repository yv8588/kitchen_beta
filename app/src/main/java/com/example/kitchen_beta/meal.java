package com.example.kitchen_beta;

public class meal {
    private String name;
    private double price;
    private String image;
    String category;

    /**
     *
     * @param name meal name.
     * @param price meals price.
     * @param image meals image.
     * @param category meal category .
     */
    public void Meal(String name,double price,String image,String category){
        this.name=name;
        this.price=price;
        this.image=image;
        this.category=category;
   }

    /**
     * @return the price.
     */
    public double getPrice() {
        return price;
    }
    /**
     * @return the category.
     */
    public String getCategory() {
        return category;
    }
    /**
     * @return the image.
     */
    public String getImage() {
        return image;
    }
    /**
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * sets meal category.
     * @param category the meals category
     */
    public void setCategory(String category) {
        this.category = category;
    }
    /**
     * sets the meals image.
     * @param image meal image.
     */
    public void setImage(String image) {
        this.image = image;
    }
    /**
     * sets the meal name
     * @param name meals name.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * sets meals price.
     * @param price meals price.
     */
    public void setPrice(double price) {
        this.price = price;
    }
}
