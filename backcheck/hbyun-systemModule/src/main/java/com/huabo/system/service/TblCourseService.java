package com.huabo.system.service;


import java.util.List;
import java.util.Map;

import com.huabo.system.entity.TblCourse;

public interface TblCourseService {


    void savetblCourse(TblCourse tblCourse);

    void updatetblCourse(TblCourse tblCourse);

    TblCourse geTblCourse(String id);

    List<TblCourse> findByFatherid(String pid);

    Map<String, Object> courseList(Integer pageNumber, Integer pageSize, String token, String staffId, String coursename1, String coursetype1);

    Map<String, Object> deletetblCourse(String id);
}
