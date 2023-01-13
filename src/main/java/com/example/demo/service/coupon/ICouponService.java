package com.example.demo.service.coupon;

import com.example.demo.model.Coupon;
import com.example.demo.service.IService;

import java.util.List;

public interface ICouponService extends IService<Coupon> {

    List<Coupon> findCouponByCode(String code);
}
