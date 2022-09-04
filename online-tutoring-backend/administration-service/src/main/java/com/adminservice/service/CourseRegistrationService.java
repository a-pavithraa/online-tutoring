package com.adminservice.service;

import com.adminservice.entity.*;
import com.adminservice.model.RegisterTeacherMappingRequest;
import com.adminservice.model.TeacherRecord;
import com.adminservice.repo.GradeRepository;
import com.adminservice.repo.SubjectRepository;
import com.adminservice.repo.TeacherRepository;
import com.adminservice.repo.TeacherSubjectGradeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseRegistrationService {
    private GradeRepository gradeRepo;
    private SubjectRepository subjectRepo;
    private TeacherRepository teacherRepository;
    private TeacherSubjectGradeRepository teacherSubjectGradeRepository;
    private static final Logger logger = LoggerFactory.getLogger(CourseRegistrationService.class);

    public CourseRegistrationService(GradeRepository gradeRepo, SubjectRepository subjectRepo, TeacherRepository teacherRepository, TeacherSubjectGradeRepository teacherSubjectGradeRepository) {
        this.gradeRepo = gradeRepo;
        this.subjectRepo = subjectRepo;
        this.teacherRepository = teacherRepository;
        this.teacherSubjectGradeRepository = teacherSubjectGradeRepository;
    }

    @Transactional
    public void mapTeacherToSubjectAndGrade(RegisterTeacherMappingRequest registerTeacherMappingRequest){
        // even though we pass the id, added these lines to check whether id actually exists in db
        Grade grade = gradeRepo.findById(registerTeacherMappingRequest.getGrade()).orElseThrow();
        Subject subject = subjectRepo.findById(registerTeacherMappingRequest.getSubject()).orElseThrow();
        Teacher teacher = teacherRepository.findByName(registerTeacherMappingRequest.getUserName());
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
}
