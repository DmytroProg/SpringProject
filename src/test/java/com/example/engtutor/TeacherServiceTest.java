package com.example.engtutor;

import com.example.engtutor.models.Teacher;
import com.example.engtutor.repository.TeacherRepository;
import com.example.engtutor.services.TeacherService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TeacherServiceTest {

    @Mock
    private TeacherRepository repository;

    @InjectMocks
    private TeacherService service;

    public TeacherServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    // Test cases for add method
    @Test
    void testAdd() {
        // Given
        Teacher teacher = new Teacher("John", "Doe", LocalDate.of(1990, 1, 1), "English Teacher", 50000);
        when(repository.save(teacher)).thenReturn(teacher);

        // When
        Teacher result = service.add(teacher);

        // Then
        assertEquals(teacher, result);
        verify(repository, times(1)).save(teacher);
    }

    // Test cases for remove method
    @Test
    void testRemove() {
        // Given
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.of(new Teacher()));

        // When
        service.remove(id);

        // Then
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void testRemoveNonExistentTeacher() {
        // Given
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // When/Then
        // Verify that IllegalArgumentException is thrown for a non-existent teacher
        assertThrows(IllegalArgumentException.class, () -> service.remove(id));

        // Verify that repository.deleteById is not called
        verify(repository, never()).deleteById(id);
    }

    // Test cases for update method
    @Test
    void testUpdate() {
        // Given
        Long id = 1L;
        Teacher existingTeacher = new Teacher("John", "Doe", LocalDate.of(1990, 1, 1), "English Teacher", 50000);
        Teacher updatedTeacher = new Teacher("UpdatedFirstName", "UpdatedLastName", LocalDate.of(1995, 6, 15), "UpdatedDescription", 60000);

        when(repository.findById(id)).thenReturn(Optional.of(existingTeacher));
        when(repository.save(existingTeacher)).thenReturn(existingTeacher);

        // When
        Teacher result = service.update(id, updatedTeacher);

        // Then
        assertEquals(updatedTeacher.getFirstName(), result.getFirstName());
        assertEquals(updatedTeacher.getLastName(), result.getLastName());
        assertEquals(updatedTeacher.getDateOfBirth(), result.getDateOfBirth());
        assertEquals(updatedTeacher.getDescription(), result.getDescription());
        assertEquals(updatedTeacher.getSalary(), result.getSalary());
        verify(repository, times(1)).findById(id);
        //verify(repository, times(1)).save(existingTeacher);
    }

    // Test cases for getAll method
    @Test
    void testGetAll() {
        // Given
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(new Teacher("John", "Doe", LocalDate.of(1990, 1, 1), "English Teacher", 50000));
        teachers.add(new Teacher("Jane", "Smith", LocalDate.of(1985, 5, 10), "Math Teacher", 60000));

        when(repository.findAll()).thenReturn(teachers);

        // When
        List<Teacher> result = service.getAll();

        // Then
        assertEquals(teachers, result);
        verify(repository, times(1)).findAll();
    }

    // Add more test methods for other service methods as needed
}
