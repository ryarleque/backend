package com.sportlimacenter.service;

import com.sportlimacenter.entity.Promotion;
import com.sportlimacenter.entity.Referral;
import com.sportlimacenter.entity.User;
import com.sportlimacenter.mapper.PromotionMapper;
import com.sportlimacenter.mapper.UserMapper;
import com.sportlimacenter.model.request.RegisterUserRequest;
import com.sportlimacenter.model.response.LoginResponse;
import com.sportlimacenter.model.response.UserResponse;
import com.sportlimacenter.repository.PromoRepository;
import com.sportlimacenter.repository.ReferralRepository;
import com.sportlimacenter.repository.UserRepository;
import com.sportlimacenter.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PromoRepository promoRepository;
    private final ReferralRepository referralRepository;

    private final UserMapper userMapper;
    private final PromotionMapper promoMapper;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    private final int SESSIONS_PER_WEEK = 2;

    public LoginResponse validateLogin(String user, String password) {
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(user);
        authenticate(user, password);

        final String token = jwtUtil.generateToken(userDetails);
        return LoginResponse.builder().token(token).build();
    }

    private void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User disabled");
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }

    @Transactional
    public void registerUser(RegisterUserRequest registerUserRequest) {

        Promotion promotion = promoMapper.mapFromRegisterUserRequest(registerUserRequest);
        promoRepository.save(promotion);

        User user = userMapper.mapFromRegisterUserRequest(registerUserRequest);
        user.setEnabled(true);
        user.setStartDate(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));
        user.setReferred(registerUserRequest.getReferralCode() != null);
        user.setPromotion(promotion);

        User userSaved = userRepository.save(user);

        if (registerUserRequest.getReferralCode() != null) {
            User userReferral = userRepository.findByDni(registerUserRequest.getReferralCode())
                    .orElseThrow(() ->  new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid referral code"));

            Referral referral = Referral.builder()
                    .parentUserId(userReferral.getId())
                    .childUserId(userSaved.getId())
                    .build();
            referralRepository.save(referral);
        }
    }

    public List<UserResponse> getUsersByBranchId(String branchId) {
        List<User> users = userRepository.findByBranchId(Long.valueOf(branchId));
        return users.stream()
                .map(u -> toUserResponse(u))
                .collect(Collectors.toList());
    }

    public UserResponse getUserByDni(String dni) {
        User user = userRepository.findByEnabledTrueAndDni(dni)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return toUserResponse(user);
    }


    private UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getName() + " " + user.getLastname())
                .dni(user.getDni())
                .email(user.getEmail())
                .startDate(user.getStartDate().toLocalDate())
                .phone(user.getPhone())
                .remainingDays(getRemainingDaysSubscription(user))
                .build();
    }

    private int getRemainingDaysSubscription(User user) {
        int numberOfSessions = user.getPromotion().getMonths_subscription()*4*SESSIONS_PER_WEEK;
        long daysBetween = ChronoUnit.DAYS.between(user.getStartDate().toLocalDate(), LocalDate.now());
        long numberOfSessionsUntilNow = Stream.iterate(user.getStartDate(), date -> date.plusDays(1))
                .limit(daysBetween)
                .filter(d -> d.getDayOfWeek() == DayOfWeek.MONDAY || d.getDayOfWeek() == DayOfWeek.WEDNESDAY)
                .count();
        return numberOfSessions - (int) numberOfSessionsUntilNow;
    }


}
