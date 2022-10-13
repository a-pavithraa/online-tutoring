package com.adminservice.service;

import com.adminservice.entity.*;
import com.adminservice.exception.MappingAlreadyExistsException;
import com.adminservice.model.*;
import com.adminservice.repo.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseRegistrationService {
    private final GradeRepository gradeRepo;
    private final SubjectRepository subjectRepo;
    private final TeacherRepository teacherRepository;

    private final StudentRepository studentRepository;
    private final TeacherSubjectGradeRepository teacherSubjectGradeRepository;
    private static final Logger logger = LoggerFactory.getLogger(CourseRegistrationService.class);


    @Transactional
    public void mapTeacherToSubjectAndGrade(RegisterTeacherMappingRequest registerTeacherMappingRequest) {
        // even though we pass the id, added these lines to check whether id actually exists in db
        Grade grade = gradeRepo.findById(registerTeacherMappingRequest.getGrade()).orElseThrow();
        Subject subject = subjectRepo.findById(registerTeacherMappingRequest.getSubject()).orElseThrow();
        Teacher teacher = teacherRepository.findByUserName(registerTeacherMappingRequest.getUserName());
        TeacherSubjectGradeId teacherSubjectGradeId = new TeacherSubjectGradeId(teacher.getId(), grade.getId(), subject.getId());
        logger.debug("teacher id=={}", teacher.getId());
        logger.debug("grade id=={}", grade.getId());
        logger.debug("teacher id=={}", teacher.getId());
        TeacherSubjectGradeMap teacherSubjectGradeMap = TeacherSubjectGradeMap.builder()
                .id(teacherSubjectGradeId)
                .subject(subject)
                .grade(grade)
                .teacher(teacher)
                .build();

        logger.debug("Saving!!!!!!!!!!");
        teacherSubjectGradeRepository.persistAndFlush(teacherSubjectGradeMap);

    }

    @Transactional(readOnly = true)
    public TeacherDetails getTeacherForSubjectAndGrade(long subjectId, long gradeId) {
       /* long teacherId = teacherSubjectGradeRepository.findBySubjectIdAndGradeId(subjectId,gradeId)
                            .orElseThrow();
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow();*/
        TeacherDetails teacherRecord = teacherSubjectGradeRepository.getTeacherForGradeAndSubject(subjectId, gradeId);
        logger.debug("teacher id=={}", teacherRecord.name());
        return teacherRecord;

    }


    @Transactional(readOnly = true)
    public ListStudentsResponse getStudentsOfTeacher(Long teacherId, Long gradeId, Long subjectId) {

        List<StudentDetails> studentRecords = studentRepository.getAllStudentsOfTeacher(teacherId, gradeId, subjectId);
        ListStudentsResponse listStudentsResponse = new ListStudentsResponse(studentRecords);
        return listStudentsResponse;
    }

    @Transactional(readOnly = true)
    public List<Dropdown> getAllSubjects() {

        List<Dropdown> subjects = subjectRepo.getAllSubjects();
        return subjects;

    }

    @Transactional(readOnly = true)
    public List<Dropdown> getAllGrades() {

        List<Dropdown> grades = gradeRepo.getAllGrades();
        return grades;

    }

    @Transactional(readOnly = true)
    public GradeAndSubjectMappingResponse getGradesAndSubjectsOfTeacher(long teacherId) {

        List<GradeAndSubjectMappingDetails> gradeAndSubjectMappingForTeacher = teacherSubjectGradeRepository.getGradeAndSubjectMappingForTeacher(teacherId);
        GradeAndSubjectMappingResponse gradeAndSubjectMappingResponse = new GradeAndSubjectMappingResponse(gradeAndSubjectMappingForTeacher);
        return gradeAndSubjectMappingResponse;

    }

    @Transactional
    public void mapTeacherToSubjectAndGrade(MapTeacherGradeSubjectRequest mapTeacherGradeSubjectRequestList) {
        checkMappingAlreadyExists(mapTeacherGradeSubjectRequestList);
        teacherSubjectGradeRepository.deleteByTeacherId(mapTeacherGradeSubjectRequestList.getTeacherId());
        List<GradeAndSubjectMappingRequest> gradeAndSubjectMappingRequestList = mapTeacherGradeSubjectRequestList.getGradeAndSubjectMappingRequestList();
        List<TeacherSubjectGradeMap> teacherSubjectGradeMapList = new ArrayList<>();
        Teacher teacher = teacherRepository.findById(mapTeacherGradeSubjectRequestList.getTeacherId()).orElseThrow();
        for (GradeAndSubjectMappingRequest gradeAndSubjectMappingRequest : gradeAndSubjectMappingRequestList) {
            TeacherSubjectGradeId teacherSubjectGradeId = new TeacherSubjectGradeId(mapTeacherGradeSubjectRequestList.getTeacherId(), gradeAndSubjectMappingRequest.getSubjectId(), gradeAndSubjectMappingRequest.getGradeId());
            Grade grade = gradeRepo.findById(gradeAndSubjectMappingRequest.getGradeId()).orElseThrow();
            Subject subject = subjectRepo.findById(gradeAndSubjectMappingRequest.getSubjectId()).orElseThrow();

            TeacherSubjectGradeMap teacherSubjectGradeMap = TeacherSubjectGradeMap.builder()
                    .id(teacherSubjectGradeId).grade(grade).subject(subject).teacher(teacher).build();
            teacherSubjectGradeMapList.add(teacherSubjectGradeMap);
        }
        teacherSubjectGradeRepository.persistAll(teacherSubjectGradeMapList);

    }
    @Transactional(readOnly = true)
    public void checkMappingAlreadyExists(MapTeacherGradeSubjectRequest mapTeacherGradeSubjectRequestList){
        for (GradeAndSubjectMappingRequest gradeAndSubjectMappingRequest : mapTeacherGradeSubjectRequestList.getGradeAndSubjectMappingRequestList())  {
            if(teacherSubjectGradeRepository.checkMappingExistsForGradeAndSubject(gradeAndSubjectMappingRequest.getSubjectId(), gradeAndSubjectMappingRequest.getGradeId(), mapTeacherGradeSubjectRequestList.getTeacherId()))
                throw new MappingAlreadyExistsException(gradeAndSubjectMappingRequest.getGradeId(), gradeAndSubjectMappingRequest.getSubjectId());

        }

    }


}
