package com.example.demo.service.warehouse;

import com.example.demo.form.WarehouseForm;
import com.example.demo.model.Warehouse;
import com.example.demo.service.IService;

import java.util.List;

public interface IWarehouseService extends IService<Warehouse> {
    List<WarehouseForm> findWarehouseFormByProductId(Long productId);
}
