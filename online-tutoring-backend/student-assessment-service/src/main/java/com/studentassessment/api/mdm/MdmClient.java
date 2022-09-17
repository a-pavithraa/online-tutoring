package com.studentassessment.api.mdm;

import com.studentassessment.feign.FeignConfig;
import com.studentassessment.model.StudentRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(
        name = "iex",
        url = "${restapi.mdm.url}",
        configuration = {FeignConfig.class})
public interface MdmClient {
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/mdm/mapping/studentsOfTeacher",
            consumes = "application/json")
    List<StudentRecord> getStudents(Long teacherId, Long gradeId, Long subjectId);
}
