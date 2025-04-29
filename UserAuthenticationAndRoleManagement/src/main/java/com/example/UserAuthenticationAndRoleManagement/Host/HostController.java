package com.example.UserAuthenticationAndRoleManagement.Host;
import com.example.UserAuthenticationAndRoleManagement.Host.DTO.PropertyDTO;
import com.example.UserAuthenticationAndRoleManagement.User.User;
import com.example.UserAuthenticationAndRoleManagement.User.UserRepository;
import com.example.UserAuthenticationAndRoleManagement.User.UserService;
import com.example.UserAuthenticationAndRoleManagement.auth.FirebasePrincipal;
import jakarta.validation.Valid;
import org.apache.catalina.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping("/host")
public class HostController {
    private final HostService hostService;

    @Autowired
    public HostController(HostService hostService) {
        this.hostService = hostService;

    }
    @GetMapping("/properties/create")
    public String showCreatePropertyForm(Model model, Principal principal) {
String roleName = hostService.getRoleNameFromPrincipal(principal); // You need to pass the principal here
            model.addAttribute("role", roleName);
            model.addAttribute("propertyForm", new PropertyDTO());
        return "add-property-form";
    }
    @GetMapping("/properties/edit/{id}")
    public String showUpdatePropertyForm(@PathVariable("id") Integer propertyId, Model model, Principal principal) {
        PropertyDTO property = hostService.getPropertyById(propertyId);
        String roleName = hostService.getRoleNameFromPrincipal(principal);
        model.addAttribute("propertyId", propertyId);
        model.addAttribute("role", roleName);
        model.addAttribute("propertyForm", property);
        return "edit-property-form";
    }

    //#############################################################

    // End points that call the Property Service
    @PostMapping("/properties/create")
    public String createProperty(@Valid @ModelAttribute("propertyForm") PropertyDTO dto,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Principal principal) {
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
    @PostMapping("/properties/edit/{id}")
    public String updateProperty(@PathVariable("id") Integer propertyId,
                                 @Valid @ModelAttribute("propertyForm") PropertyDTO dto,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Principal principal) {
        if (result.hasErrors()) {
            return "edit-property-form";
        }

        dto.setHostId(getHostIdFromPrincipal(principal)); // enforce correct host
        hostService.updateProperty(propertyId, dto);
        redirectAttributes.addFlashAttribute("success", "Property updated successfully!");
        return "redirect:/host/properties";
    }


    @GetMapping("/properties")
    public String viewHostProperties(Model model, Principal principal) {
        String hostId = getHostIdFromPrincipal(principal); // You must implement this based on your user system
        List<PropertyDTO> properties = hostService.getPropertiesForHost(hostId);

        String roleName = hostService.getRoleNameFromPrincipal(principal); // You need to pass the principal here

        model.addAttribute("role", roleName);
        model.addAttribute("properties", properties);
        return "host-properties"; // your Thymeleaf HTML
    }
}