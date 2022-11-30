package com.example.demo.controller;

import com.example.demo.email.EmailSender;
import com.example.demo.form.PlaceOrderForm;
import com.example.demo.model.cart.Cart;
import com.example.demo.model.cart.CartItem;
import com.example.demo.model.login.AppUser;
import com.example.demo.service.cart.ICartService;
import com.example.demo.service.cartItem.ICartItemService;
import com.example.demo.service.login.IAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private IAppUserService appUserService;

    @Autowired
    private ICartService cartService;

    @Autowired
    private ICartItemService cartItemService;

    @Autowired
    private EmailSender emailSender;

    private AppUser currentUser() {
        return appUserService.getCurrentUser();
    }

    @GetMapping("")
    public ModelAndView checkout() {
        ModelAndView modelAndView = new ModelAndView("view/checkout");
        AppUser appUser = currentUser();
        Cart cart = cartService.findCartByAppUserId(appUser.getId());
        List<CartItem> cartItemList = cartItemService.findCartItemByCartId(cart.getId());

        double totalPrice = 0;
        for (CartItem cartItem: cartItemList) {
            totalPrice += (cartItem.getQuantity() * cartItem.getProduct().getPrice());
        }

        modelAndView.addObject("appUser", appUser);
        modelAndView.addObject("cart", cart);
        modelAndView.addObject("cartItems", cartItemList);
        modelAndView.addObject("totalPrice", totalPrice);
        return modelAndView;
    }

    @PostMapping("/order")
    public ModelAndView getPlaceOrder(@ModelAttribute PlaceOrderForm placeOrderForm) {
        ModelAndView modelAndView = new ModelAndView("redirect:/");

        Map<String, Object> templateAttributes = new LinkedHashMap<>();
        templateAttributes.put("firstName", placeOrderForm.getFirstName());
        templateAttributes.put("lastName", placeOrderForm.getFirstName());
        templateAttributes.put("email", placeOrderForm.getEmail());
        templateAttributes.put("address", placeOrderForm.getAddress());
        templateAttributes.put("phoneNumber", placeOrderForm.getPhoneNumber());
        templateAttributes.put("typePayment", placeOrderForm.getTypePayment());

        List<CartItem> cartItems = cartItemService.findCartItemByCartId(placeOrderForm.getCartId());
        templateAttributes.put("cartItems", cartItems);

        templateAttributes.put("subTotal", placeOrderForm.getSubTotal());
        templateAttributes.put("discount", placeOrderForm.getDiscount());
        getShipping(placeOrderForm.getShippingMethod(), templateAttributes);
        templateAttributes.put("grandTotal", placeOrderForm.getGrandTotal());

        emailSender.send(placeOrderForm.getEmail(), templateAttributes, "email/placeOrder");

        cartItemService.deleteCartItemByCartId(placeOrderForm.getCartId());

        return modelAndView;
    }

    private void getShipping(String shipping, Map<String, Object> templateAttributes) {
        String shippingMethod = "";
        double shippingCost = 0;

        switch (shipping) {
            case "0":
                shippingCost = 0;
                shippingMethod = "Standard Delivery";
                break;
            case "10":
                shippingCost = 10;
                shippingMethod = "Express Delivery";
                break;
            case "20":
                shippingCost = 20;
                shippingMethod = "Next Business day";
                break;
        }

        templateAttributes.put("shippingCost", shippingCost);
        templateAttributes.put("shippingMethod", shippingMethod);
    }

}
