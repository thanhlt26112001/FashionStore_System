package com.example.fashionstore_system.controller;

import com.example.fashionstore_system.entity.Product;
import com.example.fashionstore_system.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;
import java.util.Optional;

//@RequestMapping("/product")
//@RestController
@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String viewHomePage(Model model) {

        return findPaginated(1, "id", "asc", model);
    }


    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable (value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir,
                                Model model) {
        int pageSize = 6;

        Page<Product> page = productService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<Product> listproduct = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("listproduct", listproduct);
        return "shop";
    }


    
//    @GetMapping("/list/page")
//    public String paginate(Model model, @RequestParam("p") Optional<Integer> p){
//        Pageable pageable = PageRequest.of(p.orElse(0),6);
//        Page<Product> page = productService.findAll(pageable);
//        model.addAttribute("listproduct", page);
//        return "shop";
//    }

    @GetMapping("/Productdetail/{id}")
    public String showProductDetail(Model model, @PathVariable(name = "id") int id) {

        Product product = productService.getProduct(id);
        model.addAttribute("product", product);

        return "product-details";
    }
}
