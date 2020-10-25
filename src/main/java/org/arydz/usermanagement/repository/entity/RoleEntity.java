package org.arydz.usermanagement.repository.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.arydz.usermanagement.domain.role.model.PermissionType;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Table(name = "ROLES")
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    Long id;

    @Column(name = "ROLE_NAME")
    String roleName;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "PERMISSION_ROLES", joinColumns = @JoinColumn(name = "FK_ROLE_ID"))
    @Enumerated(EnumType.STRING)
    @Column(name = "PERMISSION_NAME")
    @Builder.Default
    Set<PermissionType> permissionTypes = new HashSet<>();
}
