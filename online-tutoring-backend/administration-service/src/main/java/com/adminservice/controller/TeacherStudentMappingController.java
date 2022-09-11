package com.adminservice.controller;

import com.adminservice.model.*;
import com.adminservice.service.CourseRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mdm/mapping")
@CrossOrigin(origins = "*")
public class TeacherStudentMappingController {

    private final CourseRegistrationService courseRegistrationService;


    @PostMapping("/teacherMapping")
    @ResponseStatus(HttpStatus.CREATED)
    public void mapTeacherToSubject(@RequestBody @Valid RegisterTeacherMappingRequest registerTeacherMappingRequest) {
        courseRegistrationService.mapTeacherToSubjectAndGrade(registerTeacherMappingRequest);

    }

    @GetMapping("/teacher/{subjectId}/{gradeId}")
    public TeacherRecord getTeacherForSubjectAndGrade(@PathVariable("subjectId") long subjectId, @PathVariable("gradeId") long gradeId){
        return courseRegistrationService.getTeacherForSubjectAndGrade(subjectId,gradeId);
    }


    @GetMapping("/studentsOfTeacher")
    public List<StudentRecord> getStudentsOfTeacher(@RequestParam("teacherId") Long teacherId,@RequestParam(required = false) Long gradeId,@RequestParam(required = false) Long subjectId){
        return courseRegistrationService.getStudentsOfTeacher(teacherId,gradeId,subjectId);
    }

    @GetMapping("/gradeAndSubjectsOfTeacher")
    public List<GradeAndSubjectMappingRecord> getGradeAndSubjectsOfTeacher(@RequestParam("teacherId") long teacherId){
        return courseRegistrationService.getGradesAndSubjectsOfTeacher(teacherId);
    }

    @PostMapping("/mapTeacherToGradeAndSubject")
    @ResponseStatus(HttpStatus.CREATED)
    public void mapTeacherToGradeAndSubject(@RequestBody @Valid MapTeacherGradeSubjectRequest mapTeacherGradeSubjectRequest){

        courseRegistrationService.mapTeacherToSubjectAndGrade(mapTeacherGradeSubjectRequest);
    }


}
