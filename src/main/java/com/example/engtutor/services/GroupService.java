package com.example.engtutor.services;

import com.example.engtutor.models.Student;
import com.example.engtutor.models.StudentsGroup;
import com.example.engtutor.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@org.springframework.stereotype.Service
public class GroupService implements Service<StudentsGroup> {

    private final GroupRepository groupRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository) {

        this.groupRepository = groupRepository;
    }

    @Override
    public StudentsGroup add(StudentsGroup entity) {
        if(!isValid(entity)) throw new IllegalArgumentException();
        return groupRepository.save(entity);
    }

    @Override
    public void remove(Long id) {
        groupRepository.deleteById(id);
    }

    @Override
    public StudentsGroup update(Long id, StudentsGroup entity) {
        StudentsGroup group = groupRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("group does not exist"));

        if(entity.getName() != null && !entity.getName().isEmpty() &&
                !Objects.equals(entity.getName(), group.getName())){
            group.setName(entity.getName());
        }

        return group;
    }

    @Override
    public List<StudentsGroup> getAll() {
        return groupRepository.findAll();
    }

    @Override
    public Optional<StudentsGroup> getById(Long id) {

        return groupRepository.findById(id);
    }

    @Override
    public boolean isValid(StudentsGroup group) {
        if (group.getName() == null || group.getName().trim().isEmpty())
            return false;

        return true;
    }
}
