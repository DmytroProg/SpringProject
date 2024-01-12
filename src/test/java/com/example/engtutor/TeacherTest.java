package com.example.engtutor;

import com.example.engtutor.models.Teacher;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TeacherTest {

    @Test
    void testGetId() {
        Teacher teacher = new Teacher();

        teacher.setId(1L);

        assertEquals(1L, teacher.getId());
    }

    @Test
    void testGetFirstName() {
        // Given
        Teacher teacher = new Teacher("John", "Doe", LocalDate.of(1990, 1, 1), "English Teacher", 50000);

        // Then
        assertEquals("John", teacher.getFirstName());
    }

    @Test
    void testGetLastName() {
        // Given
        Teacher teacher = new Teacher("John", "Doe", LocalDate.of(1990, 1, 1), "English Teacher", 50000);

        // Then
        assertEquals("Doe", teacher.getLastName());
    }

    @Test
    void testGetDateOfBirth() {
        // Given
        LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);
        Teacher teacher = new Teacher("John", "Doe", dateOfBirth, "English Teacher", 50000);

        // Then
        assertEquals(dateOfBirth, teacher.getDateOfBirth());
    }

    @Test
    void testGetDescription() {
        // Given
        Teacher teacher = new Teacher("John", "Doe", LocalDate.of(1990, 1, 1), "English Teacher", 50000);

        // Then
        assertEquals("English Teacher", teacher.getDescription());
    }

    @Test
    void testGetSalary() {
        // Given
        Teacher teacher = new Teacher("John", "Doe", LocalDate.of(1990, 1, 1), "English Teacher", 50000);

        // Then
        assertEquals(50000, teacher.getSalary());
    }

    @Test
    void testSetId() {
        // Given
        Teacher teacher = new Teacher();

        // When
        teacher.setId(2L);

        // Then
        assertEquals(2L, teacher.getId());
    }

    @Test
    void testSetFirstName() {
        // Given
        Teacher teacher = new Teacher();

        // When
        teacher.setFirstName("UpdatedFirstName");

        // Then
        assertEquals("UpdatedFirstName", teacher.getFirstName());
    }

    @Test
    void testSetLastName() {
        // Given
        Teacher teacher = new Teacher();

        // When
        teacher.setLastName("UpdatedLastName");

        // Then
        assertEquals("UpdatedLastName", teacher.getLastName());
    }

    @Test
    void testSetDateOfBirth() {
        // Given
        Teacher teacher = new Teacher();
        LocalDate newDateOfBirth = LocalDate.of(1995, 6, 15);

        // When
        teacher.setDateOfBirth(newDateOfBirth);

        // Then
        assertEquals(newDateOfBirth, teacher.getDateOfBirth());
    }

    @Test
    void testSetDescription() {
        // Given
        Teacher teacher = new Teacher();

        // When
        teacher.setDescription("UpdatedDescription");

        // Then
        assertEquals("UpdatedDescription", teacher.getDescription());
    }

    @Test
    void testSetSalary() {
        // Given
        Teacher teacher = new Teacher();

        // When
        teacher.setSalary(60000);

        // Then
        assertEquals(60000, teacher.getSalary());
    }
}
