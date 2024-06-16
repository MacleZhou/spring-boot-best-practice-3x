package com.macle.study.state.machine.services;

import com.macle.study.state.machine.events.EnrollmentEvent;
import com.macle.study.state.machine.repositories.EnrollmentRepository;
import com.macle.study.state.machine.state.EnrollmentState;
import com.macle.study.state.machine.commands.CompleteCommand;
import com.macle.study.state.machine.commands.EnrollCommand;
import com.macle.study.state.machine.dto.EnrollmentDto;
import com.macle.study.state.machine.entities.Enrollment;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class EnrollmentService {

    private EnrollmentRepository enrollmentRepository;

    private StateMachineFactory<EnrollmentState, EnrollmentEvent>  stateMachineFactory;

    @Transactional
    public EnrollmentDto enroll(EnrollCommand enrollCommand) {
        boolean isPresented = enrollmentRepository.findEnrollmentByEmployeeIdAndCourseId(enrollCommand.employeeId(), enrollCommand.courseId())
                .isPresent();

        if(isPresented){
            throw new IllegalArgumentException("Employee already exists");
        }

        var enrollment = new Enrollment(enrollCommand.employeeId(), enrollCommand.courseId());

        enrollmentRepository.save(enrollment);

        var stateMachine = initStateMachine(enrollment.getId(), null);
        log.info("New state: {}", stateMachine.getState().getId());
        enrollment.setEnrollmentState(stateMachine.getState().getId());

        return toDto(enrollment);
    }

    @Transactional
    public void complete(CompleteCommand completeCommand) {
        var enrollment = enrollmentRepository.findEnrollmentByEmployeeIdAndCourseId(completeCommand.employeeId(), completeCommand.courseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        var stateMachine = initStateMachine(enrollment.getId(), enrollment.getEnrollmentState());
        stateMachine.sendEvent(Mono.just(new GenericMessage<>(EnrollmentEvent.COMPLETE))).subscribe();
        enrollment.setEnrollmentState(stateMachine.getState().getId());
    }

    public EnrollmentDto findById(long id) {
        var enrollment = enrollmentRepository.findById(id).orElseThrow();
        log.info("Get: {}", enrollment.getEnrollmentState());
        return toDto(enrollment);
    }

    public Enrollment findEnrollmentByEmployeeIdAndCourseId(long employeeId, long courseId) {
        var enrollment = enrollmentRepository.findEnrollmentByEmployeeIdAndCourseId(employeeId, courseId).orElseThrow();
        log.info("Get: {}", enrollment.getEnrollmentState());
        return enrollment;
    }

    private EnrollmentDto toDto(Enrollment enrollment) {
        return new EnrollmentDto(enrollment.getId(), enrollment.getEmployeeId(), enrollment.getCourseId(), enrollment.getEnrollmentState());
    }

    private StateMachine<EnrollmentState, EnrollmentEvent> initStateMachine(long id, EnrollmentState enrollmentState) {
        var stateMachine = stateMachineFactory.getStateMachine(Long.toString(id));
        if (enrollmentState != null) {
            stateMachine.getStateMachineAccessor().doWithAllRegions(access ->
                    access.resetStateMachineReactively(new DefaultStateMachineContext<>(enrollmentState, null, null, null)).subscribe());
        }
        stateMachine.startReactively().subscribe();

        return stateMachine;
    }
}