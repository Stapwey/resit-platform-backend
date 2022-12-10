package com.resitplatform.infrastructure.db.jpa;

import com.resitplatform.domain.model.Resit;
import com.resitplatform.domain.model.User;
import com.resitplatform.domain.repository.ResitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class JpaResitRepositoryAdapter implements ResitRepository {

    private final DataResitRepository repository;

    @Override
    public Optional<Resit> findBySlug(String slug) {
        return repository.findBySlug(slug);
    }

    @Override
    public Optional<Resit> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public List<Resit> findByParticipants(Set<User> participants, Integer limit, Integer offset) {
        Pageable pageable = new OffsetBasedPageRequest(limit, offset, Sort.by(Sort.Order.desc("name")));
        return repository.findByParticipantsIn(participants, pageable);
    }

    @Override
    public List<Resit> findByFilters(String name, String teacherName, Integer limit, Integer offset) {
        Pageable pageable = new OffsetBasedPageRequest(limit, offset, Sort.by(Sort.Order.desc("name")));
        return repository.findByResponsibleTeacherUsernameContainingAndNameContaining(teacherName, name, pageable);
    }

    @Override
    public void cancel(Resit resit) {
        repository.delete(resit);
    }

    @Override
    public Resit save(Resit flower) {
        return repository.save(flower);
    }

}
