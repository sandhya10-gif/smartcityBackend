package com.example.SmartCity.Controller;


import com.example.SmartCity.Repository.CollegeRepository;
import com.example.SmartCity.Repository.HospitalRepository;
import com.example.SmartCity.Repository.JobRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")

public class DashboardController {

    private final CollegeRepository collegeRepo;
    private final HospitalRepository hospitalRepo;
    private final JobRepository jobRepo;

    public DashboardController(
            CollegeRepository collegeRepo,
            HospitalRepository hospitalRepo,
            JobRepository jobRepo) {
        this.collegeRepo = collegeRepo;
        this.hospitalRepo = hospitalRepo;
        this.jobRepo = jobRepo;
    }

    @GetMapping("/stats")
    public Map<String, Long> getStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("colleges", collegeRepo.count());
        stats.put("hospitals", hospitalRepo.count());
        stats.put("jobs", jobRepo.count());
        return stats;
    }
}
