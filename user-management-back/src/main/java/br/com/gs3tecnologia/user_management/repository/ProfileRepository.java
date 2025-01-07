package br.com.gs3tecnologia.user_management.repository;

import br.com.gs3tecnologia.user_management.model.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, String> {
    boolean existsByName(String name);
    Optional<ProfileEntity> findByName(String name);
}
