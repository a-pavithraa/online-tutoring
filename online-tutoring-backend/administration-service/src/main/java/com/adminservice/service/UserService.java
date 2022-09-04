package com.adminservice.service;

import java.util.ArrayList;
import java.util.List;

import com.adminservice.entity.*;
import com.adminservice.model.CreateTeacherRequest;
import com.adminservice.model.RegisterTeacherMappingRequest;
import com.adminservice.repo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.adminservice.model.CreateStudentRequest;
import com.adminservice.util.Utilties.Roles;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminAddUserToGroupRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpResponse;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
@RequiredArgsConstructor
public class UserService {
    private StudentRepository studentRepo;
    private GradeRepository gradeRepo;
    private SubjectRepository subjectRepo;
    private TeacherRepository teacherRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    @Autowired
    public UserService(StudentRepository studentRepo, GradeRepository gradeRepo, SubjectRepository subjectRepo,TeacherRepository teacherRepository) {

        this.studentRepo = studentRepo;
        this.gradeRepo = gradeRepo;
        this.subjectRepo = subjectRepo;
        this.teacherRepository = teacherRepository;

    }



    @Transactional
    public void createStudent(CreateStudentRequest createStudentRequest, String cognitoId) {


        // Need to refactor
        Grade grade = gradeRepo.findById(createStudentRequest.getGrade()).orElseThrow();
        logger.debug("Getting subjects!!");
        List<Subject> subjects = subjectRepo.findByIdIn(createStudentRequest.getSubjects());
        logger.debug("subjects:{}", subjects);
        logger.debug("Creating student!!");
        Student student = Student.builder().name(createStudentRequest.getUserName()).email(createStudentRequest.getEmailId())
                .cognitoId(cognitoId).phoneNo(createStudentRequest.getEmailId()).grade(grade).parentName(createStudentRequest.getParentName())
                .address(createStudentRequest.getAddress()).build();
        studentRepo.persist(student);
        logger.debug("Setting subject for student!!");
        subjects.forEach(subject -> subject.addStudent(student));
        subjectRepo.persistAll(subjects);


    }

    @Transactional
    public void createTeacher(CreateTeacherRequest createTeacherRequest, String cognitoId) {

        Teacher teacher = Teacher.builder().name(createTeacherRequest.getUserName()).email(createTeacherRequest.getEmailId())
                .cognitoId(cognitoId).phoneNo(createTeacherRequest.getEmailId())
                .address(createTeacherRequest.getAddress()).build();
        teacherRepository.persist(teacher);
    }

    @Transactional
    public void deleteTeacher(String userName){
        Long teacherId = teacherRepository.findIdByName(userName).orElseThrow();
        teacherRepository.deleteById(teacherId);

    }

    @Transactional
    public void deleteStudent(String userName){
        Long studentId = studentRepo.findIdByName(userName).orElseThrow();
        studentRepo.deleteById(studentId);

    }



}
