package com.sportlimacenter.repository;

import com.sportlimacenter.entity.Referral;
import com.sportlimacenter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferralRepository extends JpaRepository<Referral, Long> {
}
