package com.sportlimacenter.repository;

import com.sportlimacenter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEnabledTrueAndDni(String dni);
    Optional<User> findByDni(String dni);
    List<User> findByBranchId(long branchId);
}
