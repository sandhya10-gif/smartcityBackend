package com.example.SmartCity.Repository;

import com.example.SmartCity.model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {

    // ✅ Filter by city only
    List<Hospital> findByCityIgnoreCase(String city);

    // ✅ Search by hospital name only
    List<Hospital> findByNameContainingIgnoreCase(String name);

    // ✅ Filter by city + search
    List<Hospital> findByCityIgnoreCaseAndNameContainingIgnoreCase(
            String city,
            String name
    );

    List<Hospital> findByEmergencyAvailableTrue();

    List<Hospital> findByTypeIgnoreCase(String type);
}
