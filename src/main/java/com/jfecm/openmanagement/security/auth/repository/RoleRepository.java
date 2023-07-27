package com.jfecm.openmanagement.security.auth.repository;

import com.jfecm.openmanagement.security.auth.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

}
