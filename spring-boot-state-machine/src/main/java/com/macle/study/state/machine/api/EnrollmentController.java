package com.macle.study.state.machine.api;

import com.macle.study.state.machine.commands.CompleteCommand;
import com.macle.study.state.machine.commands.EnrollCommand;
import com.macle.study.state.machine.services.EnrollmentService;
import com.macle.study.state.machine.state.EnrollmentState;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enrollment")
public class EnrollmentController {
    @Resource
    private EnrollmentService enrollmentService;

    @PostMapping("/enroll")
    EnrollmentState enroll(@RequestBody EnrollCommand enrollCommand) {
        var enrollment = enrollmentService.enroll(enrollCommand);
        var result = enrollmentService.findById(enrollment.id());
        return result.enrollmentState();
    }

    @PostMapping("/complete")
    EnrollmentState complete(@RequestBody CompleteCommand enrollCommand) {
        var enrollment = enrollmentService.findEnrollmentByEmployeeIdAndCourseId(enrollCommand.employeeId(), enrollCommand.courseId());
        enrollmentService.complete(enrollCommand);
        var result = enrollmentService.findById(enrollment.getId());
        return result.enrollmentState();
    }
}
