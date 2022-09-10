package com.adminservice.service;

import com.adminservice.entity.*;
import com.adminservice.model.*;
import com.adminservice.repo.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseRegistrationService {
    private final  GradeRepository gradeRepo;
    private final  SubjectRepository subjectRepo;
    private final  TeacherRepository teacherRepository;

    private final  StudentRepository studentRepository;
    private final  TeacherSubjectGradeRepository teacherSubjectGradeRepository;
    private static final Logger logger = LoggerFactory.getLogger(CourseRegistrationService.class);



    @Transactional
    public void mapTeacherToSubjectAndGrade(RegisterTeacherMappingRequest registerTeacherMappingRequest){
        // even though we pass the id, added these lines to check whether id actually exists in db
        Grade grade = gradeRepo.findById(registerTeacherMappingRequest.getGrade()).orElseThrow();
        Subject subject = subjectRepo.findById(registerTeacherMappingRequest.getSubject()).orElseThrow();
        Teacher teacher = teacherRepository.findByUserName(registerTeacherMappingRequest.getUserName());
        TeacherSubjectGradeId teacherSubjectGradeId = new TeacherSubjectGradeId(teacher.getId(), grade.getId(), subject.getId());
        logger.debug("teacher id=={}",teacher.getId());
        logger.debug("grade id=={}",grade.getId());
        logger.debug("teacher id=={}",teacher.getId());
        TeacherSubjectGradeMap teacherSubjectGradeMap= TeacherSubjectGradeMap.builder()
                .id(teacherSubjectGradeId)
                .subject(subject)
                .grade(grade)
                .teacher(teacher)
                .build();

        logger.debug("Saving!!!!!!!!!!");
        teacherSubjectGradeRepository.persistAndFlush(teacherSubjectGradeMap);

    }
    @Transactional(readOnly = true)
    public TeacherRecord getTeacherForSubjectAndGrade(long subjectId, long gradeId){
       /* long teacherId = teacherSubjectGradeRepository.findBySubjectIdAndGradeId(subjectId,gradeId)
                            .orElseThrow();
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow();*/
        TeacherRecord teacherRecord = teacherSubjectGradeRepository.getTeacherForGradeAndSubject(subjectId, gradeId);
        logger.debug("teacher id=={}",teacherRecord.name());
        return teacherRecord;

    }


    @Transactional(readOnly = true)
    public List<StudentRecord> getStudentsOfTeacher(Long teacherId,Long gradeId, Long subjectId){

        List<StudentRecord> studentRecords = studentRepository.getAllStudentsOfTeacher(teacherId,gradeId,subjectId);
        return studentRecords;

    }
    @Transactional(readOnly = true)
    public List<DropdownRecord> getAllSubjects(){

        List<DropdownRecord> subjects = subjectRepo.getAllSubjects();
        return subjects;

    }

    @Transactional(readOnly = true)
    public List<DropdownRecord> getAllGrades(){

        List<DropdownRecord> grades = gradeRepo.getAllGrades();
        return grades;

    }
    @Transactional(readOnly = true)
    public List<GradeAndSubjectMappingRecord> getGradesAndSubjectsOfTeacher(long teacherId){

        List<GradeAndSubjectMappingRecord> gradeAndSubjectMappingForTeacher = teacherSubjectGradeRepository.getGradeAndSubjectMappingForTeacher(teacherId);
        return gradeAndSubjectMappingForTeacher;

    }


}
