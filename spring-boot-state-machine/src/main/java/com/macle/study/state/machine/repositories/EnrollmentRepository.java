package com.macle.study.state.machine.repositories;

import com.macle.study.state.machine.entities.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    Optional<Enrollment> findEnrollmentByEmployeeIdAndCourseId(long employeeId, long courseId);
}