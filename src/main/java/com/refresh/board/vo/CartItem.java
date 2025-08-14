package com.refresh.board.vo;

public class CartItem {
    private String productId;
    private String name;
    private String image;
    private int basePrice;
    private int optionPrice; // ← 이게 없으면 오류
    private int price;
    private int quantity;
    private String option;

    // 반드시 기본 생성자 + Getter/Setter 포함!
    public CartItem() {}

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public int getBasePrice() { return basePrice; }
    public void setBasePrice(int basePrice) { this.basePrice = basePrice; }

    public int getOptionPrice() { return optionPrice; }
    public void setOptionPrice(int optionPrice) { this.optionPrice = optionPrice; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getOption() { return option; }
    public void setOption(String option) { this.option = option; }
}
