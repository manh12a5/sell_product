package com.example.demo.service.cartItem;

import com.example.demo.model.CartItem;
import com.example.demo.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class CartItemService implements ICartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public List<CartItem> findAll() {
        return cartItemRepository.findAll();
    }

    @Override
    public CartItem findById(Long id) {
        return cartItemRepository.findById(id).orElse(null);
    }

    @Override
    public CartItem save(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    @Override
    public void remove(Long id) {
        cartItemRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteCartItemByCartId(Long cartId) {
        cartItemRepository.deleteCartItemByCartId(cartId);
    }

    @Override
    public List<CartItem> findCartItemByCartId(Long cartId) {
        return cartItemRepository.findCartItemByCartId(cartId);
    }

    @Override
    public void createCartItem(CartItem cartItem) {
        List<CartItem> cartItemList = this.findAll();
        boolean check = false;
        int index = 0;
        int quantity = 0;
        if (cartItemList.isEmpty()) {
            this.save(cartItem);
        } else {
            for (int i = 0; i < cartItemList.size(); i++) {
                if
                ((cartItemList.get(i).getProduct().getId().equals(cartItem.getProduct().getId())) && (cartItemList.get(i).getSize() == cartItem.getSize())) {
                    int quantity_before = cartItemList.get(i).getQuantity();
                    int quantity_after = cartItem.getQuantity();
                    int quantity_present = quantity_before + quantity_after;
                    index = i;
                    quantity = quantity_present;
                    check = true;
                    break;
                }
            }

            if (check == false) {
                this.save(cartItem);
            } else {
                CartItem cartItem1 = cartItemList.get(index);
                cartItem1.setQuantity(quantity);
                this.save(cartItem1);
            }
        }
    }

}
