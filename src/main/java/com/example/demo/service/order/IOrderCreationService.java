package com.example.demo.service.order;

import com.example.demo.form.PlaceOrderForm;
import com.example.demo.model.OrderCreation;
import com.example.demo.service.IService;
import org.springframework.transaction.annotation.Transactional;

public interface IOrderCreationService extends IService<OrderCreation> {
    @Transactional
    Long createOrderCreation(PlaceOrderForm placeOrderForm);
}
