package com.example.SmartCity.Repository;
import com.example.SmartCity.model.Role;
import com.example.SmartCity.model.Status;
import com.example.SmartCity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByRole(Role role);

    List<User> findByStatus(Status status);

    // ✅ FIXED
    List<User> findByEmailContainingIgnoreCase(String email);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
