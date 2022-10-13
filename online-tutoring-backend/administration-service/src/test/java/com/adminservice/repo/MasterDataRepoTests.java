package com.adminservice.repo;

import com.adminservice.entity.Teacher;
import com.adminservice.entity.TeacherSubjectGradeId;
import com.adminservice.entity.TeacherSubjectGradeMap;
import com.adminservice.model.GradeAndSubjectMappingDetails;
import com.adminservice.model.TeacherDetails;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.persistence.EntityManager;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers

public class MasterDataRepoTests {

    @Container
    static MySQLContainer<?> mysqlContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.0.30"));


    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
    }



    @Autowired
    private TeacherSubjectGradeRepository teacherSubjectGradeRepository;

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        teacherSubjectGradeRepository.deleteAll();

        Teacher teacher =Teacher.builder().userName("Test").userName("Test").email("test@gmail.com").cognitoId("test").fullName("Test").build();
        entityManager.persist(teacher);
        //entityManager.persist(Teacher.builder().id(2L).userName("Test2").userName("Test2").build());

        TeacherSubjectGradeId teacherSubjectGradeId = new TeacherSubjectGradeId(1L,1L,1L);
        TeacherSubjectGradeMap teacherSubjectGradeMap = TeacherSubjectGradeMap.builder()
                .id(teacherSubjectGradeId)
                .subject(subjectRepository.findById(1L).get())
                .grade(gradeRepository.findById(1L).get())
                .teacher(teacher)
                .build();



        entityManager.persist(teacherSubjectGradeMap);
       // entityManager.persist(TeacherSubjectGradeMap.builder().id(new TeacherSubjectGradeId(2l,1l,2L)).build());
    }

    @Test
    void getTeacherForGradeAndSubject(){
        TeacherDetails teacherRecord =teacherSubjectGradeRepository.getTeacherForGradeAndSubject(1L,1L);
        Assertions.assertThat(teacherRecord).isNotNull();

    }
    @Test
    void shouldGetMappedGradeAndSubjects() {
        List<GradeAndSubjectMappingDetails> mappings = teacherSubjectGradeRepository.getGradeAndSubjectMappingForTeacher(1L);
        Assertions.assertThat(mappings).hasSize(1);
        Assertions.assertThat(mappings.get(0).gradeId()).isEqualTo(1L);

    }

}
