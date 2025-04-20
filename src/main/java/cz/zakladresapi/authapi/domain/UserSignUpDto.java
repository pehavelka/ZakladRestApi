package cz.zakladresapi.authapi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pro registraci nového uživatele
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpDto {
  private String email;
  private String password;
  private String fullName;
}