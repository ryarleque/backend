package com.sportlimacenter.model.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginResponse {

    private String token;
}
