package com.example.demo.service.order;

import com.example.demo.form.PlaceOrderForm;
import com.example.demo.model.*;
import com.example.demo.repository.OrderCreationRepository;
import com.example.demo.service.cartItem.ICartItemService;
import com.example.demo.service.login.IAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
public class OrderCreationService implements IOrderCreationService {

    @Autowired
    private OrderCreationRepository orderCreationRepository;

    @Autowired
    private IAppUserService appUserService;

    @Autowired
    private ICartItemService cartItemService;

    @Autowired
    private IOrderLineService orderLineService;

    private AppUser currentUser() {
        return appUserService.getCurrentUser();
    }

    @Override
    public List<OrderCreation> findAll() {
        return orderCreationRepository.findAll();
    }

    @Override
    public OrderCreation findById(Long id) {
        return orderCreationRepository.findById(id).orElse(null);
    }

    @Override
    public OrderCreation save(OrderCreation orderCreation) {
        return orderCreationRepository.save(orderCreation);
    }

    @Override
    public void remove(Long id) {
        orderCreationRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Long createOrderCreation(PlaceOrderForm placeOrderForm) {
        OrderCreation orderCreation = new OrderCreation();
        orderCreation.setAppUser(currentUser());
        orderCreation.setCreatedOn(new Timestamp(System.currentTimeMillis()));
        this.save(orderCreation);

        List<CartItem> cartItems = cartItemService.findCartItemByCartId(placeOrderForm.getCartId());
        for (CartItem cartItem: cartItems) {
            OrderLine orderLine = new OrderLine();
            orderLine.setOrderCreation(orderCreation);
            orderLine.setProduct(cartItem.getProduct());
            orderLine.setQuantity(cartItem.getQuantity());
            orderLine.setPrice(cartItem.getProduct().getPrice());

            orderLineService.save(orderLine);
        }

        return orderCreation.getId();
    }

}
