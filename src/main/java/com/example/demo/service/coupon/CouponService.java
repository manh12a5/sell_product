package com.example.demo.service.coupon;

import com.example.demo.model.Coupon;
import com.example.demo.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Service
public class CouponService implements ICouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Override
    public List<Coupon> findAll() {
        return couponRepository.findAll();
    }

    @Override
    public Coupon findById(Long id) {
        return couponRepository.findById(id).orElse(null);
    }

    @Override
    public Coupon save(Coupon coupon) {
        coupon.setExpiredDate(new Date(System.currentTimeMillis()));
        return couponRepository.save(coupon);
    }

    @Override
    public void remove(Long id) {
        couponRepository.deleteById(id);
    }

    @Override
    public List<Coupon> findCouponByCode(String code) {
        return couponRepository.findCouponByCode(code);
    }

}
