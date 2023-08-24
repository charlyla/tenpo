package com.tenpo.app.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NumberDTO {

    // @NotNull(message = "El campo no debe ser nulo")
    // @Pattern(regexp = "\\d+", message = "El campo debe contener solo dígitos")
    private Integer numberA;
    // @NotNull(message = "El campo no debe ser nulo")
    // @Pattern(regexp = "\\d+", message = "El campo debe contener solo dígitos")
    private Integer NumberB;
}
