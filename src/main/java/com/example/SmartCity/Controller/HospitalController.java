package com.example.SmartCity.Controller;

import com.example.SmartCity.model.Hospital;
import com.example.SmartCity.Repository.HospitalRepository;
import com.example.SmartCity.Util.GeoUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hospitals")
public class HospitalController {

    private final HospitalRepository hospitalRepository;

    public HospitalController(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    @GetMapping
    public List<Hospital> getHospitals(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Boolean emergency) {

        // 🔥 PRIORITY FILTERS FIRST

        if (emergency != null && emergency) {
            return hospitalRepository.findByEmergencyAvailableTrue();
        }

        if (type != null && !type.isEmpty()) {
            return hospitalRepository.findByTypeIgnoreCase(type);
        }

        // ✅ CITY + SEARCH
        if (city != null && !city.isEmpty()
                && search != null && !search.isEmpty()) {
            return hospitalRepository
                    .findByCityIgnoreCaseAndNameContainingIgnoreCase(city, search);
        }

        // ✅ ONLY CITY
        if (city != null && !city.isEmpty()) {
            return hospitalRepository.findByCityIgnoreCase(city);
        }

        // ✅ ONLY SEARCH
        if (search != null && !search.isEmpty()) {
            return hospitalRepository.findByNameContainingIgnoreCase(search);
        }

        return hospitalRepository.findAll();
    }

    // 🔴 ADMIN ONLY
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Hospital add(@RequestBody Hospital hospital) {
        return hospitalRepository.save(hospital);
    }

    // 🔴 ADMIN ONLY
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Hospital update(@PathVariable Long id, @RequestBody Hospital hospital) {
        hospital.setId(id);
        return hospitalRepository.save(hospital);
    }

    // 🔴 ADMIN ONLY
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        hospitalRepository.deleteById(id);
    }

    @GetMapping("/nearest")
    public List<Map<String, Object>> getNearestHospitals(
            @RequestParam double lat,
            @RequestParam double lon) {

        return hospitalRepository.findAll().stream()
                .filter(h -> h.getLatitude() != null && h.getLongitude() != null)
                .map(hospital -> {
                    double dist = GeoUtil.distance(
                            lat, lon,
                            hospital.getLatitude(),
                            hospital.getLongitude()
                    );

                    Map<String, Object> map = new HashMap<>();
                    map.put("hospital", hospital);
                    map.put("distance", Math.round(dist * 10.0) / 10.0);
                    return map;
                })
                .sorted(Comparator.comparingDouble(
                        m -> (double) m.get("distance")
                ))
                .toList();
    }


}
