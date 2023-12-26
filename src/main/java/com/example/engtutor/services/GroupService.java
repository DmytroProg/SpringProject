package com.example.engtutor.services;

import com.example.engtutor.helpers.Output;
import com.example.engtutor.helpers.StringHelper;
import com.example.engtutor.models.Group;
import com.example.engtutor.repository.GroupRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@org.springframework.stereotype.Service
@CacheConfig(cacheNames = "groups")
public class GroupService implements Service<Group> {

    private final GroupRepository repository;

    @Autowired
    public GroupService(GroupRepository repository) {
        this.repository = repository;
    }

    @Override
    @CachePut(key = "#entity.id")
    public Group add(Group entity) {
        validate(entity);
        return repository.save(entity);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(key = "'allGroups'"),
            @CacheEvict(key = "#id")
    })
    public void remove(Long id) {
        var item = repository.findById(id);
        if(item.isEmpty())
            throw new IllegalArgumentException("group" + Output.VALUE_NOT_EXIST);
        repository.deleteById(id);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(key = "'allGroups'"),
            @CacheEvict(key = "#id")
    })
    public Group update(Long id, Group entity) {
        Group group = repository.findById(id)
                .orElseThrow(() -> new IllegalStateException("group does not exist"));

        if(entity.getName() != null && !entity.getName().isEmpty() &&
                !Objects.equals(entity.getName(), group.getName())){
            group.setName(entity.getName());
        }

        return group;
    }

    @Override
    @Cacheable(key = "'allGroups'")
    public List<Group> getAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(key = "'allGroups'")
    public List<Group> getPaged(int limit, int offset) {
        return repository.findAll(PageRequest.of(offset, limit)).stream().toList();
    }

    @Override
    @Cacheable(key = "#id")
    public Optional<Group> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void validate(Group group) {
        if(StringHelper.isEmpty(group.getName()))
            throw new IllegalArgumentException("name" + Output.EMPTY_VALUE);
    }
}
