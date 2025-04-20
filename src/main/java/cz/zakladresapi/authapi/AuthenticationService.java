package cz.zakladresapi.authapi;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cz.zakladresapi.authapi.domain.LoginResponseDto;
import cz.zakladresapi.authapi.domain.UserLoginDto;
import cz.zakladresapi.authapi.domain.UserSignUpDto;
import cz.zakladresapi.authapi.role.RoleRepository;
import cz.zakladresapi.authapi.role.RoleUserRepository;
import cz.zakladresapi.authapi.role.domain.Role;
import cz.zakladresapi.authapi.role.domain.RoleEnum;
import cz.zakladresapi.authapi.role.domain.RoleUser;
import cz.zakladresapi.authapi.user.UserRepository;
import cz.zakladresapi.authapi.user.domain.User;
import cz.zakladresapi.authapi.user.domain.UserDto;
import cz.zakladresapi.core.ErrorCollector;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class AuthenticationService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleUserRepository roleUserRepository;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private RoleRepository roleRepository;

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtService jwtService;

  /**
   * Příhlášení do systému
   * 
   * @param dto
   * @return
   */
  public LoginResponseDto authenticate(UserLoginDto dto) {
    log.debug("AUTHAPI - přihlášení");
    
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));

    User authenticatedUser = userRepository.findByEmail(dto.getEmail()).orElseThrow();
    String jwtToken = jwtService.generateToken(authenticatedUser);

    LoginResponseDto loginResponse = LoginResponseDto.builder().token(jwtToken)
        .expiresIn(jwtService.getExpirationTime()).build();

    return loginResponse;
  }

  /**
   * Přihlášení - kontroly
   * 
   * @param dto
   * @param errs
   */
  public void authenticateKontroly(UserLoginDto dto, final ErrorCollector errs) {
    log.debug("AUTHAPI - kontroly před přihlášením");
    
    if (StringUtils.isBlank(dto.getEmail())) {
      errs.AddChyba("Údaj E-mail není zadán.");
    }
    if (StringUtils.isBlank(dto.getPassword())) {
      errs.AddChyba("Údaj heslo není zadán.");
    }
  }

  /**
   * Registrace nového uživatele, role USER
   * 
   * @param dto
   * @param role
   * @return
   */
  public UserDto signUp(UserSignUpDto dto, RoleEnum role) {
    log.debug("AUTHAPI - registrace nového uživatele");
    
    Optional<Role> optionalRole = roleRepository.findByName(role);

    if (optionalRole.isEmpty()) {
      return null;
    }

    User ent = new User();
    ent.setFullName(dto.getFullName());
    ent.setEmail(dto.getEmail());
    ent.setPassword(passwordEncoder.encode(dto.getPassword()));
    ent.setPlatnost(true);
    ent = userRepository.save(ent);

    RoleUser ru = new RoleUser();
    ru.setRole(optionalRole.get());
    ru.setUser(ent);
    roleUserRepository.save(ru);

    entityManager.refresh(ent);
    entityManager.refresh(ru);

    return userRepository.findById(ent.getId()).map(UserDto::of).get();
  }

  /**
   * Registrace nového uživatele, role USER - kontroly
   * 
   * @param dto
   * @param errs
   */
  public void signUpKontroly(UserSignUpDto dto, final ErrorCollector errs) {
    log.debug("AUTHAPI - kontroly při registraci nového uživatele");
    
    if (StringUtils.isBlank(dto.getEmail())) {
      errs.AddChyba("Údaj E-mail není zadán.");
    } else {
      if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
        errs.AddChyba(String.format("Uživatel s E-mail %s již existuje.", dto.getEmail()));
      }
    }
    if (StringUtils.isBlank(dto.getFullName())) {
      errs.AddChyba("Údaj Celé jméno není zadán.");
    }
    if (StringUtils.isBlank(dto.getPassword())) {
      errs.AddChyba("Údaj heslo není zadán.");
    }
  }

  /**
   * Detail přihlášeného uživatele
   * 
   * @return
   */
  public UserDto authenticatedUser() {
    log.debug("AUTHAPI - detail přihlášeného uživatele");
    
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    User currentUser = (User) authentication.getPrincipal();

    return UserDto.builder()
        .email(currentUser.getEmail())
        .fullName(currentUser.getFullName())
        .id(currentUser.getId())
        .platnost(currentUser.isPlatnost())
        .zmenaCas(currentUser.getZmenaCas())
        .zmenaUzivatel(currentUser.getZmenaUzivatel())
        .build();
  }
}