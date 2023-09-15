package com.teamAlpha.bookHub.catalog.entity;

import com.teamAlpha.bookHub.common.entity.Schemas;
import com.teamAlpha.bookHub.communication.entity.Attachment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.Date;

import static javax.persistence.GenerationType.*;

@Entity
@Table(name = "product", schema = Schemas.CATALOG)
public class Product {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "product_id")
    private Integer productId;
    @NotNull
    @Size(min = 2, message = "Product name should have at least 2 characters")
    @Column(name = "product_name")
    private String productName;
    @NotNull
    @Column(name = "description")
    private String description;
    @NotNull
    @Column(name = "price")
    private Integer price;
    @NotNull
    @OneToOne()
    @JoinColumn(name = "image_id", referencedColumnName = "a_id")
    private Attachment attachment;
    @NotNull
    @Column(name = "available_count")
    private Integer availableCount;

    @NotNull
    @Column(name = "shop_id")
    private Integer shopId;

//    @JsonIgnore
    @ManyToOne()
    //name can be anything but ref name should be @Colum name or id
    @JoinColumn(name = "product_category_id", referencedColumnName = "category_id")
    private ProductCategory productCategory;
    @CreationTimestamp
    @Column(name= "created_on")
    private Date createdOn;
    @UpdateTimestamp
    @Column(name = "updated_on")
    private Date updatedOn;


    public ProductCategory getProductCategory() {
        return productCategory;
    }


    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }


    public Integer getAvailableCount() {
        return availableCount;
    }

    public void setAvailableCount(Integer availableCount) {
        this.availableCount = availableCount;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", attachment=" + attachment +
                ", availableCount=" + availableCount +
                ", shopId=" + shopId +
                ", productCategory=" + productCategory +
                ", createdOn=" + createdOn +
                ", updatedOn=" + updatedOn +
                '}';
    }
}
