package com.beingAbroad.eduManage.repository;

import com.beingAbroad.eduManage.entity.Institute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;
import java.util.UUID;
@Repository
public interface InstituteRepository extends JpaRepository<Institute, UUID> {
    Optional<Institute> findById(UUID id);

    boolean existsByInstituteCode(String instituteCode);

    boolean existsByEmail(String email);
}
