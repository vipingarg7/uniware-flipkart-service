package com.uniware.integrations.client.dto.uniware;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import static javax.persistence.GenerationType.IDENTITY;

public class BundleItemType {

    private static final long serialVersionUID = 5252303980748499623L;
    private Integer           id;
    //TODO take decision on below fields
//    private ItemType          itemType;
//    private Bundle            bundle;
    private int               quantity;
    private BigDecimal price;
    private BigDecimal        priceRatio;
    private Date created;
    private Date              updated;

    public BundleItemType() {
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "item_type_id", nullable = false)
//    public ItemType getItemType() {
//        return this.itemType;
//    }
//
//    public void setItemType(ItemType itemType) {
//        this.itemType = itemType;
//    }
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "bundle_id", nullable = false)
//    public Bundle getBundle() {
//        return bundle;
//    }
//
//    public void setBundle(Bundle bundle) {
//        this.bundle = bundle;
//    }

    @Column(name = "price", nullable = false, precision = 12)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Column(name = "price_ratio", nullable = false, precision = 12)
    public BigDecimal getPriceRatio() {
        return priceRatio;
    }

    public void setPriceRatio(BigDecimal priceRatio) {
        this.priceRatio = priceRatio;
    }

    @Column(name = "quantity", nullable = false)
    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", nullable = false, length = 19)
    public Date getCreated() {
        return this.created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated", nullable = false, length = 19, insertable = false, updatable = false)
    public Date getUpdated() {
        return this.updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
//        result = prime * result + ((itemType == null) ? 0 : itemType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BundleItemType other = (BundleItemType) obj;
//        if (itemType == null) {
//            if (other.itemType != null)
//                return false;
//        } else if (!itemType.getSkuCode().equals(other.itemType.getSkuCode()))
//            return false;
        return true;
    }

}
