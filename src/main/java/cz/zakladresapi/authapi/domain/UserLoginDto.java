package cz.zakladresapi.authapi.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Údaje přihlášení
 */
@Data
public class UserLoginDto {
  @Schema(description = "Email uživatele", example = "user@example.com")
  private String email;
  
  @Schema(description = "Heslo uživatele", example = "password123")
  private String password;
}