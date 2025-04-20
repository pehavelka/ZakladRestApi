package cz.zakladresapi.authapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cz.zakladresapi.authapi.domain.UserLoginDto;
import cz.zakladresapi.authapi.domain.UserSignUpDto;
import cz.zakladresapi.authapi.role.domain.RoleEnum;
import cz.zakladresapi.core.ErrorCollector;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {

  @Autowired
  private AuthenticationService authenticationService;

  /**
   * Příhlášení do systému
   * 
   * @param dto
   * @return
   */
  @PostMapping("/login")
  public ResponseEntity<Object> authenticate(@RequestBody UserLoginDto dto) {
    ErrorCollector errs = new ErrorCollector();
    authenticationService.authenticateKontroly(dto, errs);
    if (errs.konec400(false)) {
      return ResponseEntity.badRequest().body(errs.getErrors());
    }

    return ResponseEntity.ok(authenticationService.authenticate(dto));
  }

  /**
   * Registrace nového uživatele, role USER
   * 
   * @param dto
   * @return
   */
  @PostMapping("signup")
  public ResponseEntity<Object> signup(@RequestBody UserSignUpDto dto) {
    ErrorCollector errs = new ErrorCollector();
    authenticationService.signUpKontroly(dto, errs);
    if (errs.konec400(false)) {
      return ResponseEntity.badRequest().body(errs.getErrors());
    }

    return ResponseEntity.ok(authenticationService.signUp(dto, RoleEnum.USER));
  }

  /**
   * Detail přihlášeného uživatele
   * 
   * @return
   */
  @GetMapping("/me")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Object> authenticatedUser() {
    return ResponseEntity.ok(authenticationService.authenticatedUser());
  }
}
