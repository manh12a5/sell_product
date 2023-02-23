package com.example.demo.service.order;

import com.example.demo.model.OrderLine;
import com.example.demo.repository.OrderLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderLineService implements IOrderLineService {

    @Autowired
    private OrderLineRepository orderLineRepository;

    @Override
    public List<OrderLine> findAll() {
        return null;
    }

    @Override
    public OrderLine findById(Long id) {
        return null;
    }

    @Override
    public OrderLine save(OrderLine orderLine) {
        return orderLineRepository.save(orderLine);
    }

    @Override
    public void remove(Long id) {

    }
}
