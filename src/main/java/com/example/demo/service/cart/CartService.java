package com.example.demo.service.cart;

import com.example.demo.form.CartForm;
import com.example.demo.model.cart.Cart;
import com.example.demo.model.cart.CartItem;
import com.example.demo.model.login.AppUser;
import com.example.demo.model.product.Product;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.service.login.IAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartService implements ICartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public Cart save(Cart cart){
        return cartRepository.save(cart);
    }

    @Override
    public List<Cart> findAll() {
        return cartRepository.findAll();
    }

    @Override
    public Cart findById(Long id) {
        return cartRepository.findById(id).get();
    }

    @Override
    public void remove(Long id) {
        cartRepository.deleteById(id);
    }

    @Override
    public Cart findCartByAppUserId(Long id) {
        return cartRepository.findCartByAppUserId(id);
    }

    @Override
    @Transactional
    public void addToCart(CartForm cartForm, Cart cart, Product product) {
        List<CartItem> cartItemList = cartItemRepository.findCartItemByCartId(cart.getId());

        if (cartItemList.isEmpty()) {
            addNewCartItem(cartForm, cart, product);
        } else {
            for (CartItem cartItem: cartItemList) {
                if (cartItem.getProduct().getId().equals(product.getId())
                        && (cartItem.getSize() == cartForm.getSize())) {
                    cartItem.setQuantity(cartItem.getQuantity() + cartForm.getQuantity());

                    cartItemRepository.save(cartItem);
                } else {
                    addNewCartItem(cartForm, cart, product);
                }
            }
        }

    }

    private void addNewCartItem(CartForm cartForm, Cart cart, Product product) {
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(cartForm.getQuantity());
        cartItem.setSize(cartForm.getSize());
        cartItem.setProduct(product);
        cartItem.setCart(cart);

        cartItemRepository.save(cartItem);
    }

}
