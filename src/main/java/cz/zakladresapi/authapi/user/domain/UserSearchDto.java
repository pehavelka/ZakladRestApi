package cz.zakladresapi.authapi.user.domain;

import lombok.Data;

@Data
public class UserSearchDto {
  private Integer id;
  private String email;
  private String fullName;
}