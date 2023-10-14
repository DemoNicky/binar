package com.binarfood.binarfoodapp.Repository;

import com.binarfood.binarfoodapp.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<List<User>> findByUsername(String username);

    Optional<User> findByUserCode(String code);

    @Query("SELECT p FROM User p WHERE LOWER(p.username) LIKE LOWER(CONCAT('%', :nama, '%')) AND p.deleted = false")
    Optional<List<User>> findUser(@Param("nama") String nama);
}
