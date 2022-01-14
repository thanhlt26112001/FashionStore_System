package com.example.fashionstore_system.controller;

import com.example.fashionstore_system.entity.Category;
import com.example.fashionstore_system.entity.Product;
import com.example.fashionstore_system.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.List;
import java.util.Optional;

@RequestMapping("/product")
@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/listproducts")
    public String viewCourse(Model model,
                             @RequestParam(value = "keyword", defaultValue = "") String keyword,
                             @RequestParam(value = "categoryId", defaultValue = "-1") Integer categoryId,
                             @RequestParam(value = "sortField", defaultValue = "id") String sortField,
                             @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) {
        return listByPages(1, sortField, sortDir, keyword, categoryId, model);
    }

    //phân trang
    @GetMapping("/listproducts/{pageNumber}")
    public String listByPages(@PathVariable(name = "pageNumber") int currentPage,
                              @RequestParam("sortField") String sortField,
                              @RequestParam("sortDir") String sortDir,
                              @RequestParam("keyword") String keyword,
                              @RequestParam(value = "categoryId") Integer categoryId,
                              Model model) {
        Page<Product> page = productService.listAll(currentPage, sortField, sortDir, keyword, categoryId);
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();
        List<Product> listproduct = page.getContent();
        List<Product> listproductRegister = new ArrayList<>();
        model.addAttribute("listproductRegister", listproductRegister);
        model.addAttribute("listproduct", listproduct);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("query", "?sortField=" + sortField + "&sortDir="
                + sortDir + "&keyword=" + keyword + "&categoryId=" + categoryId);
        model.addAttribute("categoryList", productService.getCategoryList());
        return "shop";
    }

    @GetMapping("/productdetail/{id}")
    public String showProductDetail(Model model, @PathVariable(name = "id") int id) {
        Product product = productService.getProduct(id);
        model.addAttribute("product", product);
        return "product-details";
    }
}