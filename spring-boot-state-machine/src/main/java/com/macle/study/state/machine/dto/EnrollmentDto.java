package com.macle.study.state.machine.dto;

import com.macle.study.state.machine.state.EnrollmentState;

public record EnrollmentDto(Long id, long employeeId, long courseId, EnrollmentState enrollmentState) {
}