package com.logicgate.event;


import com.logicgate.order.model.Order;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class InvoiceEvent extends ApplicationEvent {
    private String applicationUrl;
    private Order order;

    public InvoiceEvent(Order order, String applicationUrl) {
        super(order);
        this.order=order;
        this.applicationUrl=applicationUrl;
    }
}
