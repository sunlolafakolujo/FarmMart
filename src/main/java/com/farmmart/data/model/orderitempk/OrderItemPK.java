package com.farmmart.data.model.orderitempk;

import com.farmmart.data.model.order.CustomerOrder;
import com.farmmart.data.model.product.Product;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;
@Embeddable
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "customerOrder")
public class OrderItemPK implements Serializable {

    private static final long serialVersionUID=476151177562655457L;

    @ManyToOne
    private CustomerOrder customerOrder;

    @ManyToOne
    private Product product;

    public CustomerOrder getCustomerOrder() {
        return customerOrder;
    }

    public void setCustomerOrder(CustomerOrder customerOrder) {
        this.customerOrder = customerOrder;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + ((customerOrder.getId() == null)
                ? 0
                : customerOrder
                .getId()
                .hashCode());
        result = prime * result + ((product.getId() == null)
                ? 0
                : product
                .getId()
                .hashCode());

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        OrderItemPK other = (OrderItemPK) obj;
        if (customerOrder == null) {
            if (other.customerOrder != null) {
                return false;
            }
        } else if (!customerOrder.equals(other.customerOrder)) {
            return false;
        }

        if (product == null) {
            if (other.product != null) {
                return false;
            }
        } else if (!product.equals(other.product)) {
            return false;
        }

        return true;
    }
}
