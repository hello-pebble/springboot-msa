package springboot_sns.controller.dto;

public record LoginRequest(
        String username,
        String password
) {
}
