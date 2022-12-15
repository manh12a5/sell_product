package com.example.demo.service.warehouse;

import com.example.demo.form.WarehouseForm;
import com.example.demo.model.Warehouse;
import com.example.demo.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseService implements IWarehouseService {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Warehouse> findAll() {
        return warehouseRepository.findAll();
    }

    @Override
    public Warehouse findById(Long id) {
        return warehouseRepository.findById(id).orElse(null);
    }

    @Override
    public Warehouse save(Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }

    @Override
    public void remove(Long id) {
        warehouseRepository.deleteById(id);
    }

    @Override
    public List<WarehouseForm> findWarehouseByProductId(Long productId) {
        String sql = "SELECT warehouse.warehouse_name, warehouse.stock "
                + "FROM product "
                + "LEFT JOIN product_warehouse ON product_warehouse.product_id = product.id "
                + "LEFT JOIN warehouse ON warehouse.id = product_warehouse.warehouse_id "
                + "WHERE product.id = :productId "
                + "AND product_warehouse.is_main_warehouse = 1 ";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("productId", productId);

        List<Object[]> results = query.getResultList();

        return results.stream().map(
                row -> WarehouseForm.builder()
                        .name((String) row[0])
                        .stock((Integer) row[1]).build())
                .collect(Collectors.toList());
    }

}
