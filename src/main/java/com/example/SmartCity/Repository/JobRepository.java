package com.example.SmartCity.Repository;

import com.example.SmartCity.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByLocationIgnoreCase(String location);

    List<Job> findByRoleContainingIgnoreCase(String role);

    List<Job> findByLocationIgnoreCaseAndRoleContainingIgnoreCase(
            String location, String role
    );
}
