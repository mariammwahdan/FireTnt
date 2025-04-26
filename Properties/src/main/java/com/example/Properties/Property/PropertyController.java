package com.example.Properties.Property;

import com.example.Properties.Property.DTO.*;
import com.example.Properties.Property.Model.Property;
import com.example.Properties.Property.Model.Review;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
@RequestMapping("/api/properties")
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping("/create")
    public String showAddForm(Model model) {
        model.addAttribute("propertyForm", new CreatePropertyDTO());
    return "add-property-form"    ;
    }
    @PostMapping("/create")
    public String createProperty(@Valid @ModelAttribute("propertyForm") CreatePropertyDTO dto,
                                                   BindingResult result,
                                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "add-property-form";
        }
        propertyService.createProperty(dto);
        redirectAttributes.addFlashAttribute("success", "Property created successfully!");
        return "redirect:/api/properties/all";
    }

    @GetMapping("/all")
    public String getAllProperties(Model model) {
        List<Property> propertyList = propertyService.getAllProperties();
        model.addAttribute("properties", propertyList);
        return "properties-list";
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<Property> getPropertyById(@PathVariable Integer id) {
//        Property property = propertyService.getPropertyById(id);
//        if (property != null) {
//            return new ResponseEntity<>(property,HttpStatus.OK);
//        }
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }

    @GetMapping("/host/{hostId}")
    @ResponseBody
    public List<CreatePropertyDTO> getPropertiesByHostId(@PathVariable Long hostId) {
        List<Property> properties = propertyService.getPropertiesByHostId(hostId);
        return properties.stream()
                .map(p -> new CreatePropertyDTO(
                        p.getPropertyId(),
                        p.getTitle(),
                        p.getDescription(),
                        p.getPricePerNight(),
                        p.isBooked(),
                        p.getHostId()
                ))
                .toList();
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Property property = propertyService.getPropertyById(id);
        if (property == null) {
            return "redirect:api/properties/all"; // Or show a 404 page
        }
        UpdatePropertyDTO dto = new UpdatePropertyDTO();
        dto.setTitle(property.getTitle());
        dto.setDescription(property.getDescription());
        dto.setPricePerNight(property.getPricePerNight());
        dto.setHostId(property.getHostId()); // Assuming you have a getHost().getId()
        dto.setIsBooked(property.isBooked());
        model.addAttribute("propertyId", id);
        model.addAttribute("propertyForm", dto);
        return "edit-property-form";
    }
    @PostMapping("/edit/{id}")
    public String updateProperty(
            @PathVariable Integer id,
            @ModelAttribute("propertyForm") @Valid UpdatePropertyDTO dto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "edit-property-form";
        }

        Property updated = propertyService.updateProperty(id, dto);
        long pID=updated.getPropertyId();
        if (updated != null) {
            redirectAttributes.addFlashAttribute("successMessage", "Property updated successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Property not found.");
        }
        return "redirect:/api/properties/"+pID;
    }

    @GetMapping("/{id}")
    public String viewPropertyDetails(@PathVariable Integer id, Model model) {
        Property property = propertyService.getPropertyById(id);
        if (property == null) {
            return "redirect:/api/properties/all";
        }
        model.addAttribute("property", property);
        return "property-details";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Integer id) {
      propertyService.deleteProperty(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/isBooked")
    public ResponseEntity<Boolean> isBooked(@PathVariable Integer id) {
        return ResponseEntity.ok(propertyService.isBooked(id));
    }


    //#############################################################


    // End points that call the Review Service
    @GetMapping("/reviews")
    @ResponseBody
    public ResponseEntity<List<Review>> getAllReviews() {
        List<Review> reviews = propertyService.getAllReviewsFromReviewService();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<Review>> getReviewsForProperty(@PathVariable("id") Integer propertyId) {
        List<Review> reviews = propertyService.getReviewsForProperty(propertyId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/{propertyId}/reviews")
    public ResponseEntity<Review> createReviewForProperty(
            @PathVariable Integer propertyId,
            @RequestBody CreateReviewWithPropertyIdDTO createReviewDTO) {

        Review createdReview = propertyService.createReviewForProperty(propertyId, createReviewDTO);
        return ResponseEntity.ok(createdReview);
    }

    // Update Review Text for Property by reviewId
    @PutMapping("/{propertyId}/reviews/{reviewId}/text")
    public ResponseEntity<Review> updateReviewTextForProperty(
            @PathVariable Integer propertyId,
            @PathVariable long reviewId,
            @RequestBody UpdateReviewTextDTO dto) {

        Review updatedReview = propertyService.updateReviewTextForProperty(propertyId, reviewId, dto);
        return ResponseEntity.ok(updatedReview);
    }

    // Update Review Rating for Property by reviewId
    @PutMapping("/{propertyId}/reviews/{reviewId}/rating")
    public ResponseEntity<Review> updateReviewRatingForProperty(
            @PathVariable Integer propertyId,
            @PathVariable long reviewId,
            @RequestBody UpdateReviewRatingDTO dto) {

        Review updatedReview = propertyService.updateReviewRatingForProperty(propertyId, reviewId, dto);
        return ResponseEntity.ok(updatedReview);
    }


}
