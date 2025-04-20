package cz.zakladresapi.authapi.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;

import cz.zakladresapi.authapi.user.domain.QUser;
import cz.zakladresapi.authapi.user.domain.User;
import cz.zakladresapi.authapi.user.domain.UserDto;
import cz.zakladresapi.authapi.user.domain.UserSearchDto;
import cz.zakladresapi.core.ErrorCollector;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @PersistenceContext
  private EntityManager entityManager;

  public List<UserDto> seznam(UserSearchDto dto) {
    log.debug("Uživatelé - seznam");
    
    if (dto == null) {
      return new ArrayList<UserDto>();
    }
    
    JPAQuery<User> query = new JPAQuery<>(entityManager);
    QUser user = QUser.user;

    BooleanBuilder predicate = new BooleanBuilder();

    if (dto.getId() != null) {
      predicate.and(user.id.eq(dto.getId()));
    }

    if (!StringUtils.isBlank(dto.getEmail())) {
      predicate.and(user.email.containsIgnoreCase(dto.getEmail()));
    }
    if (!StringUtils.isBlank(dto.getFullName())) {
      predicate.and(user.fullName.containsIgnoreCase(dto.getFullName()));
    }

    return query.from(user).where(predicate).fetch().stream().map(UserDto::of).toList();
  }

  /**
   * Kontroly před uložením
   * 
   * @param dto
   * @param errs
   * @param novy
   */
  public void kontroly(final UserDto dto, final ErrorCollector errs, final boolean novy) {
    log.debug("Uživatelé - kontroly před uloženm");
    
    if (novy) {
      if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
        errs.AddChyba(String.format("Uživatel s E-mail %s již existuje.", dto.getEmail()));
      }
    } else {
      if (dto.getId() == null) {
        errs.AddChyba("Údaj ID není zadán.");
      } else if (userRepository.findById(dto.getId()).isEmpty()) {
        errs.AddChyba(String.format("Záznam s id %s nenalezen.", dto.getId()));
      }
    }

    if (StringUtils.isBlank(dto.getEmail())) {
      errs.AddChyba("Údaj E-mail není zadán.");
    }

    if (StringUtils.isBlank(dto.getEmail())) {
      errs.AddChyba("Údaj Celé jméno není zadán.");
    }
  }

  public UserDto novy(UserDto dto) {
    log.debug("Uživatelé - vytvořit nový");
    
    User ent = new User();
    dto.setPlatnost(true);
    dtoToEntity(dto, ent);

    ent = userRepository.save(ent);
    entityManager.flush();
    entityManager.refresh(ent);
    
    return UserDto.of(ent);
  }

  public UserDto zmena(UserDto dto) {
    log.debug("Uživatelé - změna");
    
    User ent = userRepository.findById(dto.getId())
        .orElseThrow(() -> new EntityNotFoundException("Uživatel nenalezen. Id: " + dto.getId()));
    
    dtoToEntity(dto, ent);

    ent = userRepository.save(ent);
    entityManager.flush();
    entityManager.refresh(ent);
    
    return UserDto.of(ent);
  }

  public Optional<UserDto> detail(final Integer id) {
    log.debug("Uživatelé - detail");
    
    return userRepository.findById(id).map(UserDto::of);
  }

  private void dtoToEntity(final UserDto dto, final User ent) {
    ent.setFullName(dto.getFullName());
    ent.setEmail(dto.getEmail());
    ent.setPlatnost(dto.isPlatnost());
  }
}