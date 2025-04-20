package cz.zakladresapi.authapi.domain;

import lombok.Data;

/**
 * Údaje přihlášení
 */
@Data
public class UserLoginDto {
  private String email;
  private String password;
}