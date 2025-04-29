package com.example.UserAuthenticationAndRoleManagement.Host;
import com.example.UserAuthenticationAndRoleManagement.Host.DTO.PropertyDTO;
import com.example.UserAuthenticationAndRoleManagement.User.UserRepository;
import com.example.UserAuthenticationAndRoleManagement.User.UserService;
import com.example.UserAuthenticationAndRoleManagement.auth.FirebasePrincipal;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping("/host")
public class HostController {

    private final HostService hostService;
   // private final UserRepository userRepo;

    @Autowired
    public HostController(HostService hostService) {
        this.hostService = hostService;
    }


    @GetMapping("/properties/create")
    public String showCreatePropertyForm(Model model) {
        model.addAttribute("propertyForm", new PropertyDTO());
        return "add-property-form";
    }

    @PostMapping("/properties/create")
    public String createProperty(@Valid @ModelAttribute("propertyForm") PropertyDTO dto,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 RedirectAttributes redirectAttribute,Principal principal) {
//        if (result.hasErrors()) {
//            return "add-property-form";
//        }
        System.out.println("CREATED NEW PROPP: "+ dto);
        String hostId = getHostIdFromPrincipal(principal);
        System.out.println("Host ID: " + hostId);
        dto.setHostId(hostId); // ➡️ Important! Set hostId here, not from form.
        hostService.createProperty(dto);
        redirectAttributes.addFlashAttribute("success", "Property created successfully!");
        return "redirect:/host/properties";
    }
    private String getHostIdFromPrincipal(Principal principal) {
        // Extract user ID from FirebasePrincipal or session
        return hostService.getUidFromPrincipal(principal); // Example for testing
    }

    //#############################################################


    // End points that call the Property Service
    @GetMapping("/properties")
    public String viewHostProperties(Model model, Principal principal) {
        String hostId = getHostIdFromPrincipal(principal); // You must implement this based on your user system
        List<PropertyDTO> properties = hostService.getPropertiesForHost(hostId);
        model.addAttribute("properties", properties);
        return "host-properties"; // your Thymeleaf HTML
    }
}