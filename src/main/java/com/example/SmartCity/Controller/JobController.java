package com.example.SmartCity.Controller;

import com.example.SmartCity.model.Job;
import com.example.SmartCity.Repository.JobRepository;
import com.example.SmartCity.Util.GeoUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")

public class JobController {

    private final JobRepository jobRepository;

    public JobController(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @GetMapping
    public List<Job> getJobs(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String search) {

        city = (city != null) ? city.trim() : null;
        search = (search != null) ? search.trim() : null;

        // city + search
        if (city != null && !city.isEmpty()
                && search != null && !search.isEmpty()) {
            return jobRepository
                    .findByLocationIgnoreCaseAndRoleContainingIgnoreCase(city, search);
        }

        // only city
        if (city != null && !city.isEmpty()) {
            return jobRepository.findByLocationIgnoreCase(city);
        }

        // only search
        if (search != null && !search.isEmpty()) {
            return jobRepository.findByRoleContainingIgnoreCase(search);
        }

        // no filter
        return jobRepository.findAll();
    }
    // 🔐 ADMIN ONLY
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Job addJob(@RequestBody Job job) {
        return jobRepository.save(job);
    }

    // 🔐 ADMIN ONLY
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Job updateJob(@PathVariable Long id, @RequestBody Job job) {
        job.setId(id);
        return jobRepository.save(job);
    }

    // 🔐 ADMIN ONLY
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteJob(@PathVariable Long id) {
        jobRepository.deleteById(id);
    }

    @GetMapping("/nearest")
    public List<Map<String, Object>> getNearestJobs(
            @RequestParam double lat,
            @RequestParam double lon) {

        return jobRepository.findAll().stream()
                .filter(j -> j.getLatitude() != null && j.getLongitude() != null)
                .map(job -> {
                    double dist = GeoUtil.distance(
                            lat, lon,
                            job.getLatitude(),
                            job.getLongitude()
                    );

                    Map<String, Object> map = new HashMap<>();
                    map.put("job", job);
                    map.put("distance", Math.round(dist * 10.0) / 10.0);
                    return map;
                })
                .sorted(Comparator.comparingDouble(
                        m -> (double) m.get("distance")
                ))
                .toList();
    }

}
