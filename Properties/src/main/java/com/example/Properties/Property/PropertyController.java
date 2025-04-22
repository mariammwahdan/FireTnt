package com.example.Properties.Property;

import com.example.Properties.Property.DTO.CreatePropertyDTO;
import com.example.Properties.Property.DTO.UpdatePropertyDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @PostMapping
    public ResponseEntity<Property> createProperty(@Valid @RequestBody CreatePropertyDTO dto) {
        Property property = propertyService.createProperty(dto);
        return new ResponseEntity<>(property,HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Property>> getAllProperties() {
        List<Property> propertyList = propertyService.getAllProperties();
        return new ResponseEntity<>(propertyList,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Property> getPropertyById(@PathVariable Integer id) {
        Property property = propertyService.getPropertyById(id);
        if (property != null) {
            return new ResponseEntity<>(property,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/host/{hostId}")
    public ResponseEntity<List<Property>> getPropertiesByHostId(@PathVariable Long hostId) {
        List<Property> properties = propertyService.getPropertiesByHostId(hostId);
        if (properties != null) {
            return new ResponseEntity<>(properties,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Property> updateProperty(
            @PathVariable Integer id,
            @RequestBody UpdatePropertyDTO dto) {
        Property updated = propertyService.updateProperty(id, dto);
        if (updated != null) {
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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
}
