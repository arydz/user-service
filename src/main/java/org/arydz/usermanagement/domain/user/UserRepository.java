package org.arydz.usermanagement.domain.user;

import org.arydz.usermanagement.repository.entity.RoleEntity;
import org.arydz.usermanagement.repository.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUserNameAndPassword(String userName, String hashedPassword);

    Optional<UserEntity> findByUserName(String userName);

    Optional<UserEntity> findByIdAndUserName(Long id, String userName);

    Long countAllByRoleEntity(RoleEntity roleEntity);
}
