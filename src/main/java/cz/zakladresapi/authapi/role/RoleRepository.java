package cz.zakladresapi.authapi.role;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cz.zakladresapi.authapi.role.domain.Role;
import cz.zakladresapi.authapi.role.domain.RoleEnum;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
  Optional<Role> findByName(RoleEnum name);
}