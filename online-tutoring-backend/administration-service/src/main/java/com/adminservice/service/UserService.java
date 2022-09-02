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

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private CognitoIdentityProviderClient identityProviderClient;
    // private UserRepository userRepo;
    @Value("${cognito.app.clientid}")
    private String clientId;
    @Value("${jwt.aws.userPoolId}")
    private String userPoolId;
    @Value("${student.group.name}")
    private String studentGroup;
    @Value("${teacher.group.name}")
    private String teacherGroup;

    private GradeRepository gradeRepo;
    private StudentRepository studentRepo;

    private SubjectRepository subjectRepo;
    private TeacherRepository teacherRepository;
    private TeacherSubjectGradeRepository teacherSubjectGradeRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(CognitoIdentityProviderClient identityProviderClient, StudentRepository studentRepo, GradeRepository gradeRepo, SubjectRepository subjectRepo,TeacherRepository teacherRepository,TeacherSubjectGradeRepository teacherSubjectGradeRepository) {
        this.identityProviderClient = identityProviderClient;
        this.studentRepo = studentRepo;
        this.gradeRepo = gradeRepo;
        this.subjectRepo = subjectRepo;
        this.teacherRepository = teacherRepository;
        this.teacherSubjectGradeRepository=teacherSubjectGradeRepository;
    }

    private String createCognitoUser(String userName,String password,String emailId, Roles role) {
        AttributeType attributeType = AttributeType.builder().name("email").value(emailId)
                .build();

        List<AttributeType> attrs = new ArrayList<>();
        attrs.add(attributeType);
        SignUpRequest signUpRequest = SignUpRequest.builder().userAttributes(attrs)
                .username(userName).clientId(clientId).password(password)
                .build();

        SignUpResponse response = identityProviderClient.signUp(signUpRequest);
        logger.debug("user confirmed=={}", response);

        String groupName = Roles.TEACHER == role ? teacherGroup : studentGroup;
        AdminAddUserToGroupRequest adminGroupRequest = AdminAddUserToGroupRequest.builder()
                .username(userName).userPoolId(userPoolId).groupName(groupName).build();
        identityProviderClient.adminAddUserToGroup(adminGroupRequest);
        return response.userSub();

    }

    @Transactional
    public void createStudent(CreateStudentRequest createStudentRequest) {

        String cognitoId = createCognitoUser(createStudentRequest.getUserName(),createStudentRequest.getPassword(),createStudentRequest.getEmailId(),Roles.STUDENT);
        // Need to refactor
        Grade grade = gradeRepo.findById(createStudentRequest.getGrade()).orElseThrow();
        logger.debug("Getting subjects!!");
        List<Subject> subjects = subjectRepo.findByIdIn(createStudentRequest.getSubjects());
        logger.debug("subjects:{}", subjects);
        logger.debug("Creating student!!");
        Student student = Student.builder().name(createStudentRequest.getUserName()).email(createStudentRequest.getEmailId())
                .cognitoId(cognitoId).phoneNo(createStudentRequest.getEmailId()).grade(grade).parentName(createStudentRequest.getParentName())
                .address(createStudentRequest.getAddress()).build();
        studentRepo.save(student);
        logger.debug("Setting subject for student!!");
        subjects.forEach(subject -> subject.addStudent(student));
        subjectRepo.saveAll(subjects);


    }

    @Transactional
    public void createTeacher(CreateTeacherRequest createTeacherRequest) {

        String cognitoId = createCognitoUser(createTeacherRequest.getUserName(),createTeacherRequest.getPassword(),createTeacherRequest.getEmailId(),Roles.TEACHER);
        // Need to refactor

        Teacher teacher = Teacher.builder().name(createTeacherRequest.getUserName()).email(createTeacherRequest.getEmailId())
                .cognitoId(cognitoId).phoneNo(createTeacherRequest.getEmailId())
                .address(createTeacherRequest.getAddress()).build();
        teacherRepository.save(teacher);
    }

    @Transactional
    public void mapTeacherToSubjectAndGrade(RegisterTeacherMappingRequest registerTeacherMappingRequest){
        Grade grade = gradeRepo.findById(registerTeacherMappingRequest.getGrade()).orElseThrow();
        Subject subject = subjectRepo.findById(registerTeacherMappingRequest.getSubject()).orElseThrow();
        Teacher teacher = teacherRepository.findByName(registerTeacherMappingRequest.getUserName());
        TeacherSubjectGradeId teacherSubjectGradeId = new TeacherSubjectGradeId(teacher.getId(), grade.getId(), subject.getId());
        TeacherSubjectGradeMap teacherSubjectGradeMap=TeacherSubjectGradeMap.builder()
                .id(teacherSubjectGradeId)
                .teacher(teacher)
                .grade(grade)
                .subject(subject)
                .build();
        logger.debug("teacherSubjectGradeMap:{}",teacherSubjectGradeMap);
        teacherSubjectGradeRepository.save(teacherSubjectGradeMap);

    }
}
