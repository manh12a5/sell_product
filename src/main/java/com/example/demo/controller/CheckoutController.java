package com.example.demo.controller;

import com.example.demo.email.EmailSender;
import com.example.demo.form.ItemForm;
import com.example.demo.form.PlaceOrderForm;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.AppUser;
import com.example.demo.model.OrderCreation;
import com.example.demo.service.order.IOrderCreationService;
import com.example.demo.service.payment.PaymentService;
import com.example.demo.service.cart.ICartService;
import com.example.demo.service.cartItem.ICartItemService;
import com.example.demo.service.login.IAppUserService;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
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
    private IOrderCreationService orderCreationService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private EmailSender emailSender;

    private AppUser currentUser() {
        return appUserService.getCurrentUser();
    }

    @GetMapping("")
    public ModelAndView checkout(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("view/checkout");
        AppUser appUser = currentUser();

        Cart cart;
        if (appUser != null) {
            cart = cartService.findCartByAppUserId(appUser.getId());
        } else {
            cart = (Cart) session.getAttribute("cartGuest");
        }

        if (cart != null) {
            List<CartItem> cartItemList = cartItemService.findCartItemByCartId(cart.getId());

            double totalPrice = 0;
            for (CartItem cartItem : cartItemList) {
                totalPrice += (cartItem.getQuantity() * cartItem.getProduct().getPrice());
            }

            modelAndView.addObject("appUser", appUser);
            modelAndView.addObject("cart", cart);
            modelAndView.addObject("cartItems", cartItemList);
            modelAndView.addObject("totalPrice", totalPrice);
        }
        return modelAndView;
    }

    @PostMapping("/order")
    @Transactional
    public void getPlaceOrder(@ModelAttribute PlaceOrderForm placeOrderForm,
                                      HttpServletResponse response) throws PayPalRESTException, IOException {
        setShipping(placeOrderForm);

        //Create order
        Long orderID = orderCreationService.createOrderCreation(placeOrderForm);
        placeOrderForm.setOrderId(orderID);

        String url = getURLPayment(placeOrderForm);

        //Delete cart after order
        cartItemService.deleteCartItemByCartId(placeOrderForm.getCartId());

        response.sendRedirect(url);
    }

    private void sendEmailAfterPayment(PlaceOrderForm placeOrderForm) {
        Map<String, Object> templateAttributes = new LinkedHashMap<>();

        templateAttributes.put("firstName", placeOrderForm.getFirstName());
        templateAttributes.put("lastName", placeOrderForm.getLastName());
        templateAttributes.put("email", placeOrderForm.getEmail());
        templateAttributes.put("address", placeOrderForm.getAddress());
        templateAttributes.put("phoneNumber", placeOrderForm.getPhoneNumber());
        templateAttributes.put("subTotal", placeOrderForm.getSubTotal());
        templateAttributes.put("shippingCost", placeOrderForm.getShippingCost());
        templateAttributes.put("tax", placeOrderForm.getTax());
        templateAttributes.put("grandTotal", placeOrderForm.getGrandTotal());

        List<CartItem> cartItems = cartItemService.findCartItemByCartId(placeOrderForm.getCartId());
        List<ItemForm> itemFormList = new ArrayList<>();
        for (CartItem item: cartItems) {
            ItemForm itemForm = new ItemForm();
            itemForm.setName(item.getProduct().getName());
            double priceOfItem = item.getProduct().getPrice() * item.getQuantity();
            itemForm.setPrice(Double.toString(priceOfItem));
            itemForm.setQuantity(Integer.toString(item.getQuantity()));
            itemForm.setCurrency("USD");
            itemForm.setTax("0");

            itemFormList.add(itemForm);
        }
        templateAttributes.put("itemFormList", itemFormList);

        emailSender.send(placeOrderForm.getEmail(), templateAttributes,
                "email/placeOrder", "Place Order");
    }

    private String getURLPayment(PlaceOrderForm placeOrderForm) throws PayPalRESTException {
        String url;

        switch (placeOrderForm.getPaymentMethod()) {
            case "0":
                url = "/payment/paypal/review_payment";
                sendEmailAfterPayment(placeOrderForm);
                OrderCreation orderCreation = orderCreationService.findById(placeOrderForm.getOrderId());
                orderCreation.setIsBackorderAllowed(true);
                break;
            case "3":
                url = paymentService.authorizePayment(placeOrderForm);
                break;
            default:
                url = "/payment/paypal/cancel";
        }

        return url;
    }

    private void setShipping(PlaceOrderForm placeOrderForm) {
        String shippingMethod = "";
        double shippingCost = 0;

        switch (placeOrderForm.getShippingMethod()) {
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

        placeOrderForm.setShippingCost(shippingCost);
        placeOrderForm.setShippingMethod(shippingMethod);
        placeOrderForm.setGrandTotal(placeOrderForm.getGrandTotal() + shippingCost);
    }

}
