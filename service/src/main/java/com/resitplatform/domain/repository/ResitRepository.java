package com.resitplatform.domain.repository;

import com.resitplatform.domain.model.Resit;
import com.resitplatform.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ResitRepository {

    Optional<Resit> findBySlug(String slug);

    Optional<Resit> findByName(String name);

    List<Resit> findByParticipants(Set<User> participants, Integer limit, Integer offset);

    List<Resit> findByFilters(String name, String teacherName, Integer limit, Integer offset);

    void cancel(Resit resit);

    Resit save(Resit resit);
}
