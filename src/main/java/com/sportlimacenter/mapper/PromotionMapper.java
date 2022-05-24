package com.sportlimacenter.mapper;

import com.sportlimacenter.entity.Promotion;
import com.sportlimacenter.model.request.RegisterUserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PromotionMapper {

    @Mappings({
            @Mapping(target = "description", source = "promo.description"),
            @Mapping(target = "value", source = "promo.value"),
            @Mapping(target = "unit", source = "promo.unit"),
            @Mapping(target = "user", ignore = true)
    })
    Promotion mapFromRegisterUserRequest(RegisterUserRequest request);
}
