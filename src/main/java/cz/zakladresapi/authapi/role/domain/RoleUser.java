package cz.zakladresapi.authapi.role.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cz.zakladresapi.authapi.user.domain.User;
import cz.zakladresapi.core.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Vazba mezi uživatelem a rolí
 */
@Data
@Table(name = "roleuser")
@Entity
@EqualsAndHashCode(callSuper = true)
public class RoleUser extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 5655231614750145410L;

  @JsonIgnore
  @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
  @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
  private Role role;

  @JsonIgnore
  @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
  private User user;

}
