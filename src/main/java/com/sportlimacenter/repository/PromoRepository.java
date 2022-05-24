package com.sportlimacenter.repository;

import com.sportlimacenter.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromoRepository extends JpaRepository<Promotion, Long> {
}
