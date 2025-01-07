package br.com.gs3tecnologia.user_management.repository;

import br.com.gs3tecnologia.user_management.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsById(String id);

    Optional<UserEntity> findByUsername(String username);
}
