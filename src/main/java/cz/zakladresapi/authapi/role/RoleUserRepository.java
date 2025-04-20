package cz.zakladresapi.authapi.role;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cz.zakladresapi.authapi.role.domain.RoleUser;

@Repository
public interface RoleUserRepository extends CrudRepository<RoleUser, Integer> {

}