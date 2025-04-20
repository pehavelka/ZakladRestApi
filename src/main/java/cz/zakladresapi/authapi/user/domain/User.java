package cz.zakladresapi.authapi.user.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cz.zakladresapi.authapi.role.domain.RoleUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Uživatelé
 */
@Data
@Entity
@Table(name = "users")
public class User implements UserDetails, Serializable {
  private static final long serialVersionUID = 9196890370927109071L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id; 

  @Column(name = "full_name")
  private String fullName;

  @Column(name = "email")
  private String email;

  @Column(name = "userpassword")
  private String password;

  @JsonIgnore
  @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
  private List<RoleUser> roles;

  @Column(name = "zmenacas", insertable = false, updatable = false)
  private LocalDateTime zmenaCas;

  @Column(name = "zmenauzivatel", insertable = false, updatable = false)
  private String zmenaUzivatel;

  @Column(name = "platnost")
  private boolean platnost;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {

    List<SimpleGrantedAuthority> result = new ArrayList<>();
    if (roles != null) {
      for (RoleUser r : roles) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + r.getRole().getName());
        result.add(authority);
      }
    }

    return result;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public String getUsername() {
    return email;
  }
}