package com.example.fashionstore_system.controller;

import com.example.fashionstore_system.entity.Product;
import com.example.fashionstore_system.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RequestMapping("/admin")
@Controller
public class AdminController {
    //anhht made this
    //product management
    //show list product
    @Autowired
    private ProductService productService;

    @RequestMapping("/product")
    public String viewProductAdmin(Model model, @Param("keyword") String keyword) {
        List<Product> listProducts = productService.listAll(keyword);
        model.addAttribute("listProducts", listProducts);
        model.addAttribute("keyword", keyword);
        return "listProductAdmin";
    }

    @RequestMapping("/product/new")
    public String showNewProductPage(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "createProductAdmin";
    }

    @RequestMapping(value = "/product/save", method = RequestMethod.POST)
    public String saveProduct(@ModelAttribute("product") Product product) {
        productService.saveProduct(product);
        return "redirect:/";
    }

    @RequestMapping("/product/edit/{id}")
    public ModelAndView showEditProductPage(@PathVariable(name = "id") int id) {
        ModelAndView mav = new ModelAndView("editProductAdmin");
        Product product = productService.getProduct(id);
        mav.addObject("product", product);
        return mav;
    }

    @RequestMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") int id) {
        productService.deleteProduct(id);
        return "redirect:/";
    }
}
