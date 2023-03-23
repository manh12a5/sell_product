package com.example.demo.service.interceptor;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.AppUser;
import com.example.demo.model.Product;
import com.example.demo.service.cart.ICartService;
import com.example.demo.service.cartItem.ICartItemService;
import com.example.demo.service.login.IAppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@RequiredArgsConstructor
public class HttpRequestInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private IAppUserService appUserService;

    @Autowired
    private ICartService cartService;

    @Autowired
    private ICartItemService cartItemService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        AppUser appUser = appUserService.getCurrentUser();

        Cart cart = null;
        if (appUser != null) {
            cart = cartService.findById(appUser.getId());
        } else {
            if (session.getAttribute("cartGuest") != null) {
                cart = (Cart) session.getAttribute("cartGuest");
            }
        }

        if (cart != null) {
            List<CartItem> cartItems = cartItemService.findCartItemByCartId(cart.getId());

            int size = 0;
            double totalPrice = 0;
            if (cartItems != null && !cartItems.isEmpty()) {
                size = cartItems.size();

                for (CartItem cartItem : cartItems) {
                    Product product = cartItem.getProduct();
                    totalPrice += (product.getPrice() * cartItem.getQuantity());
                }
            }

            session.setAttribute("cartItems", cartItems);
            session.setAttribute("cartItemSize", size);
            session.setAttribute("totalPrice", totalPrice);
        }

        return super.preHandle(request, response, handler);
    }

}
