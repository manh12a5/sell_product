package com.example.demo.service.cart;

import com.example.demo.form.CartForm;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Product;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    private EntityManager entityManager;

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
        boolean checkDuplicateCart = false;

        CartItem cartItem = null;
        if (cartItemList.isEmpty()) {
            addNewCartItem(cartForm, cart, product);
        } else {
            for (CartItem cartItemForm: cartItemList) {
                if (cartItemForm.getProduct().getId().equals(product.getId())
                        && (cartItemForm.getSize() == cartForm.getSize())) {
                    checkDuplicateCart = true;
                    cartItem = cartItemForm;
                } else {
                    addNewCartItem(cartForm, cart, product);
                }
                break;
            }
        }

        if (checkDuplicateCart) {
            cartItem.setQuantity(cartItem.getQuantity() + cartForm.getQuantity());
            cartItemRepository.save(cartItem);
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

    @Override
    @Transactional
    public void removeGuestCart() {
        List<Cart> carts = cartRepository.findGuestCart();

        for (Cart cart: carts) {
            cartItemRepository.deleteCartItemByCartId(cart.getId());
            cartRepository.delete(cart);
        }
    }

    @Override
    public Cart findCartByEmailOfAppUser(String email) {
        return cartRepository.findCartByEmailOfAppUser(email.trim());
    }

}
