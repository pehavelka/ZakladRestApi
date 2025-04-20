package cz.zakladresapi.authapi.role.domain;

import java.io.Serializable;

import cz.zakladresapi.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Table(name = "roles")
@Entity
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 5583246587431972827L;

  @Enumerated(EnumType.STRING)
  @Column(name = "rolename")
  private RoleEnum name;

  @Column(name = "roledescription")
  private String description;
}
