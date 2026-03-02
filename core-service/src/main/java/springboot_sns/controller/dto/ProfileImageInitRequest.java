package springboot_sns.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProfileImageInitRequest(
        @NotNull @Positive Long fileSize,
        @NotBlank String fileName
) {
}
