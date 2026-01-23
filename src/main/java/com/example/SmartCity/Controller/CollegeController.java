package com.example.SmartCity.Controller;

import com.example.SmartCity.model.College;
import com.example.SmartCity.model.CollegeCategory;
import com.example.SmartCity.Repository.CollegeRepository;
import com.example.SmartCity.Util.GeoUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/colleges")

public class CollegeController {

    private final CollegeRepository collegeRepository;

    public CollegeController(CollegeRepository collegeRepository) {
        this.collegeRepository = collegeRepository;
    }

    @GetMapping()
    public List<College> getColleges(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) CollegeCategory category) {

        city = (city != null) ? city.trim() : null;
        search = (search != null) ? search.trim() : null;


        if (category != null) {
            return collegeRepository.findByCategory(category);
        }
        // 🧠 PRIORITY BASED DECISION SUPPORT
        if (priority != null) {
            switch (priority) {
                case "LOW_FEES":
                    return collegeRepository.findAllByOrderByHostelFeeAsc();

                case "HOSTEL":
                    return collegeRepository.findByHostelAvailableTrue();

                case "GOVERNMENT":
                    return collegeRepository.findByTypeIgnoreCase("Government");
            }
        }

        // ✅ BOTH city + search
        if (city != null && !city.isEmpty() &&
                search != null && !search.isEmpty()) {

            return collegeRepository
                    .findByCityIgnoreCaseAndNameContainingIgnoreCase(city, search);
        }

        // ✅ ONLY city
        if (city != null && !city.isEmpty()) {
            return collegeRepository.findByCityIgnoreCase(city);
        }

        // ✅ ONLY search
        if (search != null && !search.isEmpty()) {
            return collegeRepository.findByNameContainingIgnoreCase(search);
        }

        // ✅ NO filters
        return collegeRepository.findAll();
    }
    // 🔴 ADMIN ONLY
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public College addCollege(@RequestBody College college) {
        return collegeRepository.save(college);
    }

    // 🔴 ADMIN ONLY
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public College updateCollege(@PathVariable Long id,
                                 @RequestBody College college) {
        college.setId(id);
        return collegeRepository.save(college);
    }

    // 🔴 ADMIN ONLY
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCollege(@PathVariable Long id) {
        collegeRepository.deleteById(id);
    }


    @GetMapping("/nearest")
    public List<Map<String, Object>> getNearestColleges(
            @RequestParam double lat,
            @RequestParam double lon) {

        List<College> colleges = collegeRepository.findAll();

        return colleges.stream()
                .map(college -> {
                    double dist = GeoUtil.distance(
                            lat, lon,
                            college.getLatitude(),
                            college.getLongitude()
                    );

                    Map<String, Object> map = new HashMap<>();
                    map.put("college", college);
                    map.put("distance", Math.round(dist * 10.0) / 10.0); // 1 decimal km

                    return map;
                })
                .sorted((a, b) ->
                        Double.compare(
                                (double) a.get("distance"),
                                (double) b.get("distance")
                        )
                )
                .toList();
    }




}
