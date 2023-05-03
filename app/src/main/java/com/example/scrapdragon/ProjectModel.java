package com.example.scrapdragon;

public class ProjectModel {

   private String Product,City,MobileNumber,ProductDescription,ProductPrice,SellersName,SellersEmail,nodekey;
   private String ImageUrl;

    public ProjectModel() {
    }

    public ProjectModel(String product, String city, String mobileNumber, String productDescription, String productPrice, String sellersName, String sellersEmail, String nodekey, String imageUrl) {
        Product = product;
        City = city;
        MobileNumber = mobileNumber;
        ProductDescription = productDescription;
        ProductPrice = productPrice;
        SellersName = sellersName;
        SellersEmail = sellersEmail;
        this.nodekey = nodekey;
        ImageUrl = imageUrl;
    }

    public String getNodekey() {
        return nodekey;
    }

    public void setNodekey(String nodekey) {
        this.nodekey = nodekey;
    }

    public String getProduct() {
        return Product;
    }

    public void setProduct(String product) {
        Product = product;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public String getProductDescription() {
        return ProductDescription;
    }

    public void setProductDescription(String productDescription) {
        ProductDescription = productDescription;
    }

    public String getSellersEmail() {
        return SellersEmail;
    }

    public void setSellersEmail(String sellersEmail) {
        SellersEmail = sellersEmail;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getSellersName() {
        return SellersName;
    }

    public void setSellersName(String sellersName) {
        SellersName = sellersName;
    }
}