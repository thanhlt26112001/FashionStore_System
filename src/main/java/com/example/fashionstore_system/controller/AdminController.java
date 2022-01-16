package com.example.fashionstore_system.controller;

import com.example.fashionstore_system.entity.*;
import com.example.fashionstore_system.service.CategoriesService;
import com.example.fashionstore_system.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;
@RequestMapping("/admin")
@Controller
public class AdminController {
    @Autowired
    private PromotionService promotionService;

    @Autowired
    private CategoriesService categoriesService;

    //Promotions
    @GetMapping("/listPromotions")
    public String viewCourse(Model model,
                             @RequestParam(value = "keyword", defaultValue = "") String keyword,
                             @RequestParam(value = "sortField", defaultValue = "id") String sortField,
                             @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) {
        return listByPages(1, sortField, sortDir, keyword, model);
    }

    @GetMapping("/listPromotions/add")
    public String addPromotion(Model model){
        Promotion promotion = new Promotion();
        model.addAttribute("PROMOTION", promotion);
        model.addAttribute("addpromotions", "/admin/listPromotions/saveOrUpdate");
        return "add_promotion_Admin";
    }

    @PostMapping ("/listPromotions/saveOrUpdate")
    public RedirectView saveAdd(@ModelAttribute("PROMOTION") Promotion promotion,
                                @RequestParam(name = "Monday", required=false) String Monday,
                                @RequestParam(name = "Tuesday",required=false) String Tuesday,
                                @RequestParam(name = "Wednesday",required=false) String Wednesday,
                                @RequestParam(name = "Thursday",required=false) String Thursday,
                                @RequestParam(name = "Friday",required=false) String Friday,
                                @RequestParam(name = "Saturday",required=false) String Saturday,
                                @RequestParam(name = "Sunday",required=false) String Sunday,
                                @RequestParam(name = "AllWeek",required=false) String AllWeek,
                                RedirectAttributes model) {
        Promotion promotion1 = promotionService.findByName(promotion.getName());
        if(promotion1 != null){
            model.addFlashAttribute("alert","Promotion name is existed!!!");
            return new RedirectView("/admin/listPromotions/add");
        }
        if(promotion.getDiscount() > 100 || promotion.getDiscount() < 0){
            model.addFlashAttribute("alert","Discount must from 0 to 100!!!");
            return new RedirectView("/admin/listPromotions/add");
        }
        if(promotion.getStartDate().after(promotion.getEndDate())){
            model.addFlashAttribute("alert","Start Date must before End Date!!!");
            return new RedirectView("/admin/listPromotions/add");
        }
        String applyDay = "";
        if(Monday != null && Tuesday != null && Wednesday != null && Thursday != null &&
                Friday != null && Saturday != null && Sunday != null || AllWeek != null){
            promotion.setApplyDay("AllWeek");
            promotionService.save(promotion);
            return new RedirectView("/admin/listPromotions");
        }
        if(Monday != null){
            applyDay= applyDay + Monday + " ";
        }
        if(Tuesday != null){
            applyDay= applyDay + Tuesday + " ";
        }
        if(Wednesday != null){
            applyDay= applyDay + Wednesday + " ";
        }
        if(Thursday != null){
            applyDay= applyDay + Thursday + " ";
        }
        if(Friday != null){
            applyDay= applyDay + Friday + " ";
        }
        if(Saturday != null){
            applyDay= applyDay + Saturday + " ";
        }
        if(Sunday != null){
            applyDay= applyDay + Sunday + " ";
        }
        promotion.setApplyDay(applyDay);
        promotionService.save(promotion);
        return new RedirectView("/admin/listPromotions");
    }

    @RequestMapping("/edit/{id}")
    public ModelAndView showEditPage(@PathVariable(name = "id") int id) {
        ModelAndView mav = new ModelAndView("edit_promotion_Admin");
        Promotion promotion = promotionService.getById(id);
        String applyDay = promotion.getApplyDay();
        if(applyDay.equals("AllWeek")){
            mav.addObject("AllWeek", true);
        }
        if(applyDay.contains("Tuesday")){
            mav.addObject("Tuesday", true);
        }
        if(applyDay.contains("Wednesday")){
            mav.addObject("Wednesday", true);
        }
        if(applyDay.contains("Thursday")){
            mav.addObject("Thursday", true);
        }
        if(applyDay.contains("Friday")){
            mav.addObject("Friday", true);
        }
        if(applyDay.contains("Saturday")){
            mav.addObject("Saturday", true);
        }
        if(applyDay.contains("Sunday")){
            mav.addObject("Sunday", true);
        }
        mav.addObject("promotion", promotion);
        return mav;
    }

    @PostMapping ("/listPromotions/editAndSave")
    public RedirectView saveEdit(@ModelAttribute("PROMOTION") Promotion promotion,
                           @RequestParam(name = "Monday", required=false) String Monday,
                           @RequestParam(name = "Tuesday",required=false) String Tuesday,
                           @RequestParam(name = "Wednesday",required=false) String Wednesday,
                           @RequestParam(name = "Thursday",required=false) String Thursday,
                           @RequestParam(name = "Friday",required=false) String Friday,
                           @RequestParam(name = "Saturday",required=false) String Saturday,
                           @RequestParam(name = "Sunday",required=false) String Sunday,
                           @RequestParam(name = "AllWeek",required=false) String AllWeek,
                           RedirectAttributes model) {
        if(promotion.getDiscount() > 100 || promotion.getDiscount() < 0){
            model.addFlashAttribute("alert","Discount must from 0 to 100!!!");
            return new RedirectView("/admin/edit/"+promotion.getId());
        }
        if(promotion.getStartDate().after(promotion.getEndDate())){
            model.addFlashAttribute("alert","Start Date must before End Date!!!");
            return new RedirectView("/admin/edit/"+promotion.getId());
        }
        String applyDay = "";
        if(Monday != null && Tuesday != null && Wednesday != null && Thursday != null &&
                Friday != null && Saturday != null && Sunday != null || AllWeek != null){
            promotion.setApplyDay("AllWeek");
            promotionService.save(promotion);
            return new RedirectView("/admin/listPromotions");
        }
        if(Monday != null){
            applyDay= applyDay + Monday + " ";
        }
        if(Tuesday != null){
            applyDay= applyDay + Tuesday + " ";
        }
        if(Wednesday != null){
            applyDay= applyDay + Wednesday + " ";
        }
        if(Thursday != null){
            applyDay= applyDay + Thursday + " ";
        }
        if(Friday != null){
            applyDay= applyDay + Friday + " ";
        }
        if(Saturday != null){
            applyDay= applyDay + Saturday + " ";
        }
        if(Sunday != null){
            applyDay= applyDay + Sunday + " ";
        }
        promotion.setApplyDay(applyDay);
        promotionService.save(promotion);
        return new RedirectView("/admin/listPromotions");
    }

    @RequestMapping("/delete/{id}")
    public String deletePromotion(@PathVariable(name = "id") int id) {
        promotionService.delete(id);
        return "redirect:/admin/listPromotions";
    }

    @GetMapping("/listPromotions/{pageNumber}")
    public String listByPages(@PathVariable(name = "pageNumber") int currentPage,
                              @RequestParam(value = "sortField", defaultValue = "id") String sortField,
                              @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
                              @RequestParam(value = "keyword", defaultValue = "") String keyword,
                              Model model) {
        Page<Promotion> page = promotionService.listAllPromotion(currentPage, sortField, sortDir, keyword);
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();
        List<Promotion> listpromotions = page.getContent();
        model.addAttribute("listpromotions", listpromotions);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("keyword", keyword);
        model.addAttribute("query", "?sortField=" + sortField + "&sortDir="
                + sortDir + "&keyword=" + keyword);
        return "list_promotions_Admin";
    }

    //Promotions

    //Category
    @GetMapping("/category/listCategory")
    public String viewCategory(Model model,
                             @RequestParam(value = "keyword", defaultValue = "") String keyword,
                             @RequestParam(value = "sortField", defaultValue = "id") String sortField,
                             @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) {
        return listByPagesCategory(1, sortField, sortDir, keyword, model);
    }

    @GetMapping("/category/listCategory/add")
    public String addCategory(Model model){
        Category category = new Category();
        model.addAttribute("category", category);
        model.addAttribute("addcategory", "/admin/category/listCategory/save");
        return "add_category_Admin";
    }

    @PostMapping ("/category/listCategory/save")
    public RedirectView saveAddCategory(@ModelAttribute("category") Category category,
                                RedirectAttributes model) {
        Category category1 = categoriesService.findByName(category.getName());
        if(category1 != null){
            model.addFlashAttribute("alert","Category name is existed!!!");
            return new RedirectView("/admin/category/listCategory");
        }
        categoriesService.save(category);
        return new RedirectView("/admin/category/listCategory");
    }

    @RequestMapping("/category/edit/{id}")
    public ModelAndView showEditCategory(@PathVariable(name = "id") int id) {
        ModelAndView mav = new ModelAndView("edit_category_Admin");
        Category category = categoriesService.getById(id);
        mav.addObject("category", category);
        return mav;
    }

    @PostMapping ("/category/listCategory/editAndSave")
    public RedirectView saveEditCategory(@ModelAttribute("category") Category category,
                                 RedirectAttributes model) {

        categoriesService.save(category);
        return new RedirectView("/admin/category/listCategory");
    }

    @RequestMapping("/category/delete/{id}")
    public String deleteCategory(@PathVariable(name = "id") int id) {
        categoriesService.delete(id);
        return "redirect:/admin/category/listCategory";
    }

    @GetMapping("/category/listCategory/{pageNumber}")
    public String listByPagesCategory(@PathVariable(name = "pageNumber") int currentPage,
                              @RequestParam(value = "sortField", defaultValue = "id") String sortField,
                              @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
                              @RequestParam(value = "keyword", defaultValue = "") String keyword,
                              Model model) {
        Page<Category> page = categoriesService.listAllCategory(currentPage, sortField, sortDir, keyword);
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();
        List<Category> listcategory = page.getContent();
        model.addAttribute("listcategory", listcategory);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("keyword", keyword);
        model.addAttribute("query", "?sortField=" + sortField + "&sortDir="
                + sortDir + "&keyword=" + keyword);
        return "list_category_Admin";
    }
    // category

}
