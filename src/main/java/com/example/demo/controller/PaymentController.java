package com.example.demo.controller;

import com.example.demo.email.EmailSender;
import com.example.demo.form.ItemForm;
import com.example.demo.model.Cart;
import com.example.demo.model.OrderCreation;
import com.example.demo.service.cart.ICartService;
import com.example.demo.service.cartItem.ICartItemService;
import com.example.demo.service.order.IOrderCreationService;
import com.example.demo.service.payment.PaymentService;
import com.paypal.api.payments.*;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ICartItemService cartItemService;

    @Autowired
    private ICartService cartService;

    @Autowired
    private IOrderCreationService orderCreationService;

    @GetMapping("/paypal/cancel")
    public ModelAndView getViewCancelPaypal() {
        return new ModelAndView("payment/cancel");
    }

    @GetMapping("/paypal/review_payment")
    public ModelAndView getReviewPaymentPaypal(HttpServletRequest request)
            throws PayPalRESTException {
        ModelAndView modelAndView = new ModelAndView("payment/review_payment");
        String paymentId = request.getParameter("paymentId") != null
                ? request.getParameter("paymentId") : null;

        if (paymentId != null) {
            Payment payment = paymentService.getPaymentDetails(paymentId);
            OrderCreation orderCreation = orderCreationService.findById(Long.parseLong(payment.getNoteToPayer()));
            orderCreation.setIsBackorderAllowed(true);
            sendEmailAfterPaypal(payment);
        }

        return modelAndView;
    }

    private void sendEmailAfterPaypal(Payment payment) {
        Map<String, Object> templateAttributes = new LinkedHashMap<>();
        templateAttributes.put("firstName", payment.getPayer().getPayerInfo().getFirstName());
        templateAttributes.put("lastName", payment.getPayer().getPayerInfo().getLastName());
        templateAttributes.put("email", payment.getPayer().getPayerInfo().getEmail());
        templateAttributes.put("address", payment.getPayer().getPayerInfo().getShippingAddress().getCity());
        templateAttributes.put("phoneNumber",
                payment.getPayer().getPayerInfo().getPhone() != null ? payment.getPayer().getPayerInfo().getPhone() : "");
        templateAttributes.put("subTotal", payment.getTransactions().get(0).getAmount().getDetails().getSubtotal());
        templateAttributes.put("shippingCost", payment.getTransactions().get(0).getAmount().getDetails().getShipping());
        templateAttributes.put("tax", payment.getTransactions().get(0).getAmount().getDetails().getTax());
        templateAttributes.put("grandTotal", payment.getTransactions().get(0).getAmount().getTotal());

        List<Item> items = payment.getTransactions().get(0).getItemList().getItems();
        List<ItemForm> itemFormList = new ArrayList<>();
        for (Item item: items) {
           ItemForm itemForm = new ItemForm();
           itemForm.setName(item.getName());
           double priceOfItem = Double.parseDouble(item.getPrice()) * Double.parseDouble(item.getQuantity());
           itemForm.setPrice(Double.toString(priceOfItem));
           itemForm.setQuantity(item.getQuantity());
           itemForm.setCurrency(item.getCurrency());
           itemForm.setTax(item.getTax());

           itemFormList.add(itemForm);
        }
        templateAttributes.put("itemFormList", itemFormList);

        emailSender.send(payment.getPayer().getPayerInfo().getEmail(), templateAttributes,
                "email/placeOrder", "Place Order with Paypal");

        Cart cart = cartService.findCartByEmailOfAppUser(payment.getPayer().getPayerInfo().getEmail());
        cartItemService.deleteCartItemByCartId(cart.getId());
    }

}
