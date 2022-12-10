package com.resitplatform.infrastructure.db.jpa;

import com.resitplatform.domain.model.Resit;
import com.resitplatform.domain.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface DataResitRepository extends CrudRepository<Resit, UUID> {

    List<Resit> findByResponsibleTeacherUsernameContainingAndNameContaining(String teacherName, String name, Pageable pageable);

    Optional<Resit> findBySlug(String slug);

    Optional<Resit> findByName(String name);

    List<Resit> findByParticipantsIn(Set<User> participants, Pageable pageable);

}
