package org.launchcode.liftoffproject.controllers;

import  org.launchcode.liftoffproject.models.Product;
import org.launchcode.liftoffproject.models.ProductData;
import org.launchcode.liftoffproject.models.User;
import org.launchcode.liftoffproject.models.Vendor;
import org.launchcode.liftoffproject.models.data.ProductRepository;
import org.launchcode.liftoffproject.models.data.UserRepository;
import org.launchcode.liftoffproject.models.data.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Controller
public class ProductController {


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VendorRepository vendorRepository;


    //METHOD TO GET USER FROM SESSION
    public User getUserFromSession(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("user");
        if (userId == null) {
            return null;
        }

        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            return null;
        }

        return user.get();
    }

    @GetMapping("products/add")
    public String displayAddProductForm(Model model, HttpServletRequest request){
        model.addAttribute(new Product());
        HttpSession session = request.getSession(false);

        if (session != null) {
            User user = getUserFromSession(session);
            model.addAttribute("user", user);
        }

        model.addAttribute("title", "ADD PRODUCT");

        return "products/add";

    }

    @PostMapping("products/add")
    public String processAddProductForm(@ModelAttribute @Valid Product newProduct, Errors errors, Model model, HttpServletRequest request) {

        if (errors.hasErrors()) {
            return "products/add";
        }

        // Get user from session.
        HttpSession session = request.getSession(false);
        User user = getUserFromSession(session);
        model.addAttribute("user", user);

        //Set the current users as the "owner" of the product.
        Vendor vendor = user.getVendor();
        model.addAttribute("vendor", vendor);
        newProduct.setVendor(vendor);
        productRepository.save(newProduct);

        model.addAttribute("title", "ADD PRODUCT");

        return "redirect:/vendor/profile";
    }

    @GetMapping("products/edit/{productId}")
    public String editProduct(@ModelAttribute @PathVariable int productId, Model model) {

        Optional<Product> optionalProduct = productRepository.findById(productId);
        Product product = (Product) optionalProduct.get();

        model.addAttribute("title", "EDIT PRODUCT");
        model.addAttribute("product", product);

        return "products/edit";
        }

    @PostMapping("products/edit/{productId}")
    public String processEditProductForm(@PathVariable int productId, @ModelAttribute @Valid @RequestParam String name,
                                         @RequestParam String photo, @RequestParam String type, @RequestParam String description,
                                         @RequestParam boolean organic) {

        Optional<Product> optionalProduct = productRepository.findById(productId);
        Product product = (Product) optionalProduct.get();

        product.setName(name);
        product.setPhoto(photo);
        product.setType(type);
        product.setDescription(description);
        product.setOrganic(organic);

        productRepository.save(product);

        return "redirect:/vendor/profile";
    }
}
