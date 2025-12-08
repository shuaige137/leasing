package com.example.leasing_spring;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class LeasingController {

    @Autowired
    private LeasingService service;

    @RequestMapping("/")
    public String viewHomePage(Model model, @Param("keyword") String keyword) {
        List<LeasingEntity> listEntities = service.listAll(keyword);
        model.addAttribute("listEntities", listEntities);
        model.addAttribute("keyword", keyword);
        return "index";
    }

    @RequestMapping("/new")
    public String newLeasingPage(Model model) {
        LeasingEntity leasing = new LeasingEntity();
        model.addAttribute("leasing", leasing);
        return "newLeasing";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveLeasing(@ModelAttribute("leasing") LeasingEntity leasing){
        service.saveLeasing(leasing);
        return "redirect:/";
    }

    @RequestMapping("/edit/{id}")
    public ModelAndView showEditLeasingForm(@PathVariable(name="id") Long id){
        ModelAndView mav = new ModelAndView("editLeasing");
        LeasingEntity leasing = service.get(id);
        mav.addObject("leasing", leasing);
        return mav;
    }

    @RequestMapping("/view/{id}")
    public ModelAndView viewLeasing(@PathVariable(name="id") Long id){
        ModelAndView mav = new ModelAndView("viewLeasing");
        LeasingEntity leasing = service.get(id);
        mav.addObject("leasing", leasing);
        return mav;
    }

    @RequestMapping("/delete/{id}")
    public String deleteLeasing(@PathVariable(name="id") Long id){
        service.deleteLeasing(id);
        return "redirect:/";
    }

    @RequestMapping("/about")
    public String aboutPage() {
        return "about";
    }

}

