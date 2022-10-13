package com.studentassessment.api.mdm;

import com.studentassessment.feign.FeignConfig;
import com.studentassessment.model.mdm.ListStudentsResponse;
import com.studentassessment.model.mdm.TeacherDetails;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "iex",
        url = "${restapi.mdm.url}",
        configuration = {FeignConfig.class})
public interface MdmClient {
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/mdm/mapping/studentsOfTeacher",
            consumes = "application/json")
    ListStudentsResponse getStudents(@RequestParam("teacherId") Long teacherId, @RequestParam("gradeId")Long gradeId, @RequestParam("subjectId") Long subjectId);


    @RequestMapping(
            method = RequestMethod.GET,
            value = "/mdm/admin/teacher",
            consumes = "application/json")
    TeacherDetails getTeacherById(@RequestParam("id") Long teacherId);
}
