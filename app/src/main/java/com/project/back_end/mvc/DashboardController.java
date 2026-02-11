package com.project.back_end.mvc;

import com.project.back_end.services.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DashboardController {

    private final Service sharedService;

    @Autowired
    public DashboardController(Service sharedService) {
        this.sharedService = sharedService;
    }

    @GetMapping("/adminDashboard/{token}")
    public String adminDashboard(
        @PathVariable String token,
        RedirectAttributes redirectAttributes
    ) {
        var result = sharedService.validateToken(token, "admin");

        if (result) {
            redirectAttributes.addFlashAttribute(
                "error",
                "Invalid admin token"
            );
            return "redirect:/";
        }

        return "admin/adminDashboard";
    }

    @GetMapping("/doctorDashboard/{token}")
    public String doctorDashboard(
        @PathVariable String token,
        RedirectAttributes redirectAttributes
    ) {
        var result = sharedService.validateToken(token, "doctor");

        if (result) {
            redirectAttributes.addFlashAttribute(
                "error",
                "Invalid doctor token"
            );
            return "redirect:/";
        }

        return "doctor/doctorDashboard";
    }
}
