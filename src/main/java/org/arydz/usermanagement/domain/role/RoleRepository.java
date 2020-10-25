package org.arydz.usermanagement.domain.role;

import org.arydz.usermanagement.repository.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByRoleName(String roleName);

    @Query("select COUNT(u.id) from UserEntity u WHERE u.roleEntity = :role")
    Long countUsedRoles(@Param("role") RoleEntity role);
}
