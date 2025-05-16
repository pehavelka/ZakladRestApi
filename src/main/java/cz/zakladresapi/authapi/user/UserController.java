package cz.zakladresapi.authapi.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cz.zakladresapi.authapi.user.domain.UserDto;
import cz.zakladresapi.authapi.user.domain.UserSearchDto;
import cz.zakladresapi.core.ErrorCollector;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Správa uživatelů
 * 
 */
@Tag(name = "UserController", description = "Správa uživatelů")
@RequestMapping("/users")
@RestController
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping
  @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'SUPER_ADMIN')")
  public ResponseEntity<List<UserDto>> getSeznam(@ModelAttribute UserSearchDto dto) {
    return ResponseEntity.ok(userService.seznam(dto));
  }

  @GetMapping(path = "/{id}")
  @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'SUPER_ADMIN')")
  public ResponseEntity<Object> detail(@PathVariable Integer id) {

    Optional<UserDto> dto = userService.detail(id);
    if (dto.isEmpty()) {
      return ResponseEntity.badRequest().body(String.format("Záznam s id %s nenalezen.", id));
    }

    return ResponseEntity.ok(dto);
  }

  @PostMapping
  @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'SUPER_ADMIN')")
  public ResponseEntity<Object> novy(@RequestBody UserDto dto) {
    ErrorCollector errs = new ErrorCollector();
    userService.kontroly(dto, errs, true);
    if (errs.konec400(false)) {
      return ResponseEntity.badRequest().body(errs.getErrors());
    }

    return ResponseEntity.ok(userService.novy(dto));
  }

  @PutMapping
  @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'SUPER_ADMIN')")
  public ResponseEntity<Object> zmena(@RequestBody UserDto dto) {
    ErrorCollector errs = new ErrorCollector();
    userService.kontroly(dto, errs, false);
    if (errs.konec400(false)) {
      return ResponseEntity.badRequest().body(errs.getErrors());
    }

    return ResponseEntity.ok(userService.zmena(dto));
  }
}