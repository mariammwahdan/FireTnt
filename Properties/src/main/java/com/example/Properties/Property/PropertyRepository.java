package com.example.Properties.Property;

import com.example.Properties.Property.Model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Integer> {
    Property findByTitle(String title);
    Property findByDescription(String description);
    Property findByPricePerNight(double pricePerNight);
    List<Property> findByIsBooked(boolean isBooked);
    List<Property> findByHostId(Long hostId);
}