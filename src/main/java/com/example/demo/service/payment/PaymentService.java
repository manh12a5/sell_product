package com.example.demo.service.payment;

import com.example.demo.form.PlaceOrderForm;
import com.example.demo.model.AppUser;
import com.example.demo.model.CartItem;
import com.example.demo.service.cartItem.ICartItemService;
import com.example.demo.service.login.IAppUserService;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {

    @Value("${payment.client.id}")
    private String clientId;

    @Value("${payment.client.secret}")
    private String clientSecret;

    private static final String MODE = "sandbox"; //Test payment
//    private static final String MODE = "live"; //Pay real money

    @Autowired
    private IAppUserService appUserService;

    @Autowired
    private ICartItemService cartItemService;

    private AppUser currentUser() {
        return appUserService.getCurrentUser();
    }

    public String authorizePayment(PlaceOrderForm placeOrderForm) throws PayPalRESTException {
        Payer payer = getPayerInformation();
        RedirectUrls redirectUrls = getRedirectUrls();
        List<Transaction> transactions = getTransactionInformation(placeOrderForm);

        Payment requestPayment = new Payment();
        requestPayment.setPayer(payer)
                .setRedirectUrls(redirectUrls)
                .setTransactions(transactions)
                .setIntent("authorize")
                .setNoteToPayer(Long.toString(placeOrderForm.getOrderId()));

        APIContext apiContext = new APIContext(clientId, clientSecret, MODE);
        Payment approvedPayment = requestPayment.create(apiContext);

        return getApprovedLink(approvedPayment);
    }

    public Payment getPaymentDetails(String paymentId) throws PayPalRESTException {
        APIContext apiContext = new APIContext(clientId, clientSecret, MODE);
        return Payment.get(apiContext, paymentId);
    }

    private String getApprovedLink(Payment payment) {
        List<Links> links = payment.getLinks();
        String approvedLink = null;

        for (Links link: links) {
            if (link.getRel().equalsIgnoreCase("approval_url")) {
                approvedLink = link.getHref();
            }
        }

        return approvedLink;
    }

    private List<Transaction> getTransactionInformation(PlaceOrderForm placeOrderForm) {
        Details details = new Details();
        details.setShipping(Double.toString(placeOrderForm.getShippingCost()))
                .setSubtotal(Double.toString(placeOrderForm.getSubTotal()))
                .setTax(Double.toString(placeOrderForm.getTax()));

        Amount amount = new Amount();
        amount.setCurrency("USD")
                .setTotal(Double.toString(placeOrderForm.getGrandTotal()))
                .setDetails(details);

        List<CartItem> cartItems = cartItemService.findCartItemByCartId(placeOrderForm.getCartId());
        List<Transaction> transactions = new ArrayList<>();

        Transaction transaction = new Transaction();
        transaction.setAmount(amount)
                .setDescription("Paypal");

        ItemList itemList = new ItemList();
        List<Item> items = new ArrayList<>();

        for (CartItem cartItem: cartItems) {
            Item item = new Item();
            item.setCurrency("USD")
                    .setName(cartItem.getProduct().getName())
                    .setPrice(Double.toString(cartItem.getProduct().getPrice()))
                    .setTax(Double.toString(placeOrderForm.getTax()))
                    .setQuantity(Integer.toString(cartItem.getQuantity()));

            items.add(item);
        }

        itemList.setItems(items);
        transaction.setItemList(itemList);
        transactions.add(transaction);

        return transactions;
    }

    private RedirectUrls getRedirectUrls() {
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://localhost:8080/payment/paypal/cancel");
        redirectUrls.setReturnUrl("http://localhost:8080/payment/paypal/review_payment");

        return redirectUrls;
    }

    private Payer getPayerInformation() {
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        PayerInfo payerInfo = new PayerInfo();
        payerInfo.setFirstName(currentUser().getFirstName())
                .setLastName(currentUser().getLastName())
                .setEmail(currentUser().getEmail());

        payer.setPayerInfo(payerInfo);

        return payer;
    }

}
