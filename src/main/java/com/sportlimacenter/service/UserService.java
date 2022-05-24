package com.sportlimacenter.service;

import com.sportlimacenter.entity.Promotion;
import com.sportlimacenter.entity.Referral;
import com.sportlimacenter.entity.User;
import com.sportlimacenter.mapper.PromotionMapper;
import com.sportlimacenter.mapper.UserMapper;
import com.sportlimacenter.model.request.RegisterUserRequest;
import com.sportlimacenter.model.response.LoginResponse;
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

import java.time.LocalDateTime;


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

    public void registerUser(RegisterUserRequest registerUserRequest) {
        User user = userMapper.mapFromRegisterUserRequest(registerUserRequest);
        user.setEnabled(true);
        user.setStartDate(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));
        user.setReferred(registerUserRequest.getReferralCode() != null);
        User userSaved = userRepository.save(user);

        Promotion promotion = promoMapper.mapFromRegisterUserRequest(registerUserRequest);
        promotion.setUser(userSaved);
        promoRepository.save(promotion);

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


}
