package com.example.engtutor.repository;

import com.example.engtutor.models.StudentsGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<StudentsGroup, Long> {
}
