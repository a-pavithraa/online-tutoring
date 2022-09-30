package com.adminservice.service;

import java.util.List;

import com.adminservice.entity.*;
import com.adminservice.model.CreateTeacherRequest;
import com.adminservice.model.TeacherRecord;
import com.adminservice.repo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.adminservice.model.CreateStudentRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final StudentRepository studentRepo;
    private final  GradeRepository gradeRepo;
    private final  SubjectRepository subjectRepo;
    private final  TeacherRepository teacherRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Transactional
    public void createStudent(CreateStudentRequest createStudentRequest, String cognitoId) {


        // Need to refactor
        Grade grade = gradeRepo.findById(createStudentRequest.getGrade()).orElseThrow();
        logger.debug("Getting subjects!!");
        List<Subject> subjects = subjectRepo.findByIdIn(createStudentRequest.getSubjects());
        logger.debug("subjects:{}", subjects);
        logger.debug("Creating student!!");
        Student student = Student.builder().userName(createStudentRequest.getUserName()).fullName(createStudentRequest.getFullName()).email(createStudentRequest.getEmailId())
                .cognitoId(cognitoId).phoneNo(createStudentRequest.getPhoneNo()).grade(grade).parentName(createStudentRequest.getParentName())
                .address(createStudentRequest.getAddress()).build();
        studentRepo.persist(student);
        logger.debug("Setting subject for student!!");
        subjects.forEach(subject -> subject.addStudent(student));
        subjectRepo.persistAll(subjects);


    }

    @Transactional
    public void createTeacher(CreateTeacherRequest createTeacherRequest, String cognitoId) {

        Teacher teacher = Teacher.builder().userName(createTeacherRequest.getUserName()).fullName(createTeacherRequest.getFullName()).email(createTeacherRequest.getEmailId())
                .cognitoId(cognitoId).phoneNo(createTeacherRequest.getPhoneNo())
                .address(createTeacherRequest.getAddress()).build();
        teacherRepository.persist(teacher);
    }

    @Transactional
    public void deleteTeacher(String userName){
        Long teacherId = teacherRepository.findIdByUserName(userName).orElseThrow();
        teacherRepository.deleteById(teacherId);

    }

    @Transactional
    public void deleteStudent(String userName){
        Long studentId = studentRepo.findIdByName(userName).orElseThrow();
        studentRepo.deleteById(studentId);

    }

    public List<TeacherRecord> getAllTeachers(){
        return teacherRepository.getAllTeachers();
    }
    public TeacherRecord getTeacherById(long id){
        return  teacherRepository.findById(id).orElseThrow();
    }

    public TeacherRecord getTeacherDetailsByName(String name){
        return teacherRepository.findByName(name).orElseThrow();
    }


}
