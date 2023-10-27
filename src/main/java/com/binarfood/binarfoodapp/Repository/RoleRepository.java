package com.binarfood.binarfoodapp.Repository;

import com.binarfood.binarfoodapp.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRoleName(String name);

}
