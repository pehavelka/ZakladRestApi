package cz.zakladresapi.authapi.user.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserSearchDto {
  @Schema(description = "Email uživatele", example = "null")
  private Integer id;
  
  @Schema(description = "Email uživatele", example = "null")
  private String email;
  
  @Schema(description = "Celé jméno", example = "null")
  private String fullName;
}