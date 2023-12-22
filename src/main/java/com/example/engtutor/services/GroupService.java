package com.example.engtutor.services;

import com.example.engtutor.models.Student;
import com.example.engtutor.models.StudentsGroup;
import com.example.engtutor.repository.GroupRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@org.springframework.stereotype.Service
@CacheConfig(cacheNames = "groups")
public class GroupService extends Service<StudentsGroup> {

    @Autowired
    public GroupService(GroupRepository groupRepository) {
        super(groupRepository);
    }

    @Override
    @Transactional
    @CachePut
    public StudentsGroup update(Long id, StudentsGroup entity) {
        StudentsGroup group = repository.findById(id)
                .orElseThrow(() -> new IllegalStateException("group does not exist"));

        if(entity.getName() != null && !entity.getName().isEmpty() &&
                !Objects.equals(entity.getName(), group.getName())){
            group.setName(entity.getName());
        }

        return group;
    }

    @Override
    @Cacheable
    public List<StudentsGroup> getAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable
    public List<StudentsGroup> getPaged(int limit, int offset) {
        return repository.findAll(PageRequest.of(offset, limit)).stream().toList();
    }

    @Override
    @Cacheable
    public Optional<StudentsGroup> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public boolean isValid(StudentsGroup group) {
        if (group.getName() == null || group.getName().trim().isEmpty())
            return false;

        return true;
    }
}
