package com.sportlimacenter.mapper;

import com.sportlimacenter.entity.User;
import com.sportlimacenter.model.request.RegisterUserRequest;
import com.sportlimacenter.model.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mappings({
            @Mapping(target = "dni", source = "user"),
            @Mapping(target = "lastname", source = "lastName")
    })
    User mapFromRegisterUserRequest(RegisterUserRequest request);
}
