package cz.zakladresapi.authapi.domain;

import lombok.Builder;
import lombok.Data;

/**
 * Odpověď přihlášení
 */
@Data
@Builder
public class LoginResponseDto {
  private String token;
  private long expiresIn;
}