package com.example.demo.controller;

import com.example.demo.form.CartForm;
import com.example.demo.form.CartItemForm;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.AppUser;
import com.example.demo.model.Product;
import com.example.demo.service.cart.ICartService;
import com.example.demo.service.cartItem.ICartItemService;
import com.example.demo.service.login.IAppUserService;
import com.example.demo.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private IAppUserService appUserService;

    @Autowired
    private ICartService cartService;

    @Autowired
    private ICartItemService cartItemService;

    @Autowired
    private IProductService productService;

    private AppUser currentUser() {
        return appUserService.getCurrentUser();
    }

    @RequestMapping(value="", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView showCart(@ModelAttribute CartForm cartForm, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("view/cart");

        List<CartItemForm> cartItemForms = cartForm.getCartItemFormList();

        if(!cartItemForms.isEmpty()) {
            for (CartItemForm cartItemForm : cartItemForms) {
                Long cartItemId = cartItemForm.getCartItemId();
                CartItem cartItem = cartItemService.findById(cartItemId);
                cartItem.setQuantity(cartItemForm.getQuantity());

                cartItemService.save(cartItem);
            }
        }

        cartView(modelAndView, session);
        return modelAndView;
    }

    private void cartView(ModelAndView modelAndView, HttpSession session) {
        CartForm cartForm = new CartForm();
        double totalPriceCart = 0;

        Cart cart = null;
        if (currentUser() != null) {
            cart = cartService.findCartByAppUserId(currentUser().getId());
        } else {
            cart = (Cart) session.getAttribute("cartGuest");
        }

        if (cart != null) {
            List<CartItem> cartItems = cartItemService.findCartItemByCartId(cart.getId());

            cartForm.setSize(cartItems.size());

            for (CartItem item : cartItems) {
                CartItemForm cartItemForm = new CartItemForm();

                cartItemForm.setCartItemId(item.getId());
                cartItemForm.setQuantity(item.getQuantity());
                cartItemForm.setSize(item.getSize());
                cartItemForm.setProduct(item.getProduct());
                cartItemForm.setTotal(item.getQuantity()
                        * cartItemForm.getProduct().getPrice());

                totalPriceCart += cartItemForm.getTotal();
                cartForm.getCartItemFormList().add(cartItemForm);
            }
        }

        modelAndView.addObject("cartForm", cartForm);
        modelAndView.addObject("totalPriceCart", totalPriceCart);
    }

    @PostMapping("/addToCart")
    public String addToCart(@ModelAttribute CartForm cartForm, Model model, HttpSession session) {
        AppUser appUser = currentUser();
        Product product = productService.findById(cartForm.getProductId());

        Cart cart;
        if (appUser != null) {
            cart = cartService.findCartByAppUserId(appUser.getId());
        } else {
            if (session.getAttribute("cartGuest") == null) {
                cart = new Cart();
                cartService.save(cart);
                session.setAttribute("cartGuest", cart);
            } else {
                cart = (Cart) session.getAttribute("cartGuest");
            }
        }

        if (cart != null) {
            cartService.addToCart(cartForm, cart, product);
            model.addAttribute("addToCart", "Added To Cart");
        }

        return "redirect:/products/view/" + product.getId();
    }

    @GetMapping("/deleteCartItem/{id}")
    public String deleteCartItem(@PathVariable Long id) {
        cartItemService.remove(id);
        return "redirect:/cart";
    }

}
