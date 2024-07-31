package com.project.logiware.order.dto;

import com.project.logiware.order.model.OrderLineItems;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long id;

    private String orderNumber;

    private List<OrderLineItems> orderLineItemsList;
}
