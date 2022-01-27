package com.example.fashionstore_system.controller;

import com.example.fashionstore_system.entity.*;
import com.example.fashionstore_system.service.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.*;
import java.util.List;

@RequestMapping("/product")
@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private CartService cartService;


    @GetMapping("/listproducts")
    public String viewProduct(Model model,
                              @RequestParam(value = "keyword", defaultValue = "") String keyword,
                              @RequestParam(value = "categoryId", defaultValue = "-1") Integer categoryId,
                              @RequestParam(value = "sortField", defaultValue = "id") String sortField,
                              @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) {
        return listProductByPages(1, sortField, sortDir, keyword, categoryId, model);
    }

    //phân trang
    @GetMapping("/listproducts/{pageNumber}")
    public String listProductByPages(@PathVariable(name = "pageNumber") int currentPage,
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
        model.addAttribute("size_carts", cartService.getCartSize());
        return "shop";
    }

    //function view feedbackList
    @GetMapping("/productdetail/{id}")
    public String viewProductDetail(Model model,
                                    @PathVariable(name = "id") int currentProduct,
                                    @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir) {
        model.addAttribute("categoryList", productService.getCategoryList());
        return productDetail(1, sortDir, currentProduct, model);
    }

    @GetMapping("/productdetail/{id}/{pageNumber}")
    public String productDetail(@PathVariable(name = "pageNumber") int currentPage,
                                @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
                                @PathVariable(name = "id") int currentProduct,
                                Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Product product = productService.getProduct(currentProduct);
        model.addAttribute("product", product);
        Page<Feedback> page = feedbackService.listAllFeedback(product.getId(), currentPage, sortDir);
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();
        List<Feedback> feedbackList = page.getContent();
        boolean flag = false;
        if (authentication != null) {
            User user = userService.findByUsername(authentication.getName());
            Set<OrderDetail> orderDetailSet = orderDetailService.getOrderDetailsByProductId(currentProduct);
            for (OrderDetail orderDetail : orderDetailSet) {
                if (orderDetail.getOrder().getCustomer().getUser().equals(user) && orderDetail.getOrder().getStatus() == 2) {
                    flag = true;
                }
            }
            model.addAttribute("userCurent", user);
        }

        Feedback feedback = new Feedback();
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("feedback", feedback);
        model.addAttribute("checkBought", flag);
        model.addAttribute("feedbackList", feedbackList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("categoryList", productService.getCategoryList());
        model.addAttribute("query", "?sortDir="
                + sortDir);
        model.addAttribute("size_carts", cartService.getCartSize());
        return "product-details";
    }

    //function saveFeedback
    @PostMapping("/feedback/saveFeedback")
    public RedirectView saveFeedback(@ModelAttribute(name = "feedback") Feedback feedback,
                                     @RequestParam(value = "feedbackImage", required = false) MultipartFile image,
                                     @RequestParam("productId") int productId,
                                     RedirectAttributes model) throws IOException {

        String fileName = StringUtils.cleanPath(image.getOriginalFilename());
        if ((!(fileName.toLowerCase().endsWith("jpeg") || fileName.toLowerCase().endsWith("png") || fileName.toLowerCase().endsWith("jpg"))) && !fileName.equals("")) {
            model.addFlashAttribute("alert", "Wrong picture format! (.jpeg or .png or  .jpg)");
            return new RedirectView("/product/productdetail/" + productId);
        }
        if(fileName.equals("") && (feedback.getContent().equals("") || feedback.getContent()==null)){
            model.addFlashAttribute("alert", "Enter your feedback or upload an image!!!");
            return new RedirectView("/product/productdetail/" + productId);
        }
        if(!fileName.equals("")){
            feedback.setImage(fileName);
        }
        feedback.setContent(feedback.getContent());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = userService.findByUsername(authentication.getName()).getCustomer();
        feedback.setCustomer(customer);
        feedback.setProduct(productService.getProduct(productId));
        Feedback feedbackSave = feedbackService.saveFeedback(feedback);
        if(!fileName.equals("")){
            Path uploadPath = Paths.get("feedback_image/" + feedbackSave.getId());
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            try (InputStream inputStream = image.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new IOException("Could not save Image: " + fileName);
            }
        }
        return new RedirectView("/product/productdetail/" + productId);
    }

    @RequestMapping("/deleteFeedback/{id}")
    public String deleteFeedback(@PathVariable(name = "id") int id) throws IOException {
        boolean flag = true;
        Feedback feedback = feedbackService.getFeedback(id);
        if (feedback.getImage() == null) {
            flag = false;
        }
        String nameImg = feedback.getFeedbackImage();
        feedbackService.deleteFeedback(feedback);
        File fileImg = new File("feedback_image/" + id);
        if (flag) {
            deleteDirectoryRecursionJava6(fileImg);
        }
        return "redirect:/product/productdetail/" + feedback.getProduct().getId();
    }

    void deleteDirectoryRecursionJava6(File file) throws IOException {
        if (file.isDirectory()) {
            File[] entries = file.listFiles();
            if (entries != null) {
                for (File entry : entries) {
                    deleteDirectoryRecursionJava6(entry);
                }
            }
        }
        if (!file.delete()) {
            throw new IOException("Failed to delete " + file);
        }
    }
}