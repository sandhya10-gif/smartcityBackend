package com.example.SmartCity.Repository;

import com.example.SmartCity.model.College;
import com.example.SmartCity.model.CollegeCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollegeRepository extends JpaRepository<College, Long> {

    // Find colleges by city
    List<College> findByCityIgnoreCase(String city);

    // Search colleges by name
    List<College> findByNameContainingIgnoreCase(String name);
    List<College> findByCityIgnoreCaseAndNameContainingIgnoreCase(String city, String name);
    List<College> findAllByOrderByHostelFeeAsc();

    List<College> findByHostelAvailableTrue();

    List<College> findByTypeIgnoreCase(String type);
    List<College> findByCategory(CollegeCategory category);


}
