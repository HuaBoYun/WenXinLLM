package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;
/*
   通用select分页
 */
@Mapper
public interface YhrPageMapper {


     /**
      * 分页查询
      * @param start  开始条 (当前页-1)*条数
      * @param end    结束条 当前页*条数
      * @param sql    完整sql select * from xxx where id=1 and name='xx'
      * @return
      */
     @Select(
             "SELECT * FROM (SELECT A.*, ROWNUM RN FROM "+
                     "  (${sql} ) A "+
                     "       WHERE ROWNUM <= #{end}) WHERE RN > #{start}")
     public List<Map<String,Object>> queryList(@Param("start") long start, @Param("end") long end, @Param("sql") String sql);

     /**
      * 分页带#条件查询
      * @param start 开始条 (当前页-1)*条数
      * @param end   结束条 当前页*条数
      * @param sql   select *from xxx where id=#{ent.id} and name=#{ent.name}  ent是前缀固定后接实体属性名
      * @param obj   实体类entity
      * @return
      */
     @Select(
             "SELECT * FROM (SELECT A.*, ROWNUM RN FROM "+
                     "  (${sql} ) A "+
                     "       WHERE ROWNUM <= #{end}) WHERE RN > #{start}")
     public List<Map<String,Object>> queryListByEntity(@Param("start") long start, @Param("end") long end, @Param("sql") String sql,@Param("ent") Object obj);

     /**
      *  查条数sql
      * @param sqlcount   完整countsql  select count(*) from xxx where id=1 and name='xx'
      * @return
      */
     @Select("${sqlcount}")
     public Long queryCount(@Param("sqlcount") String sqlcount);

     /**
      *  查条数sql 实体封装参数
      * @param sqlcount  select count(*) from xxx where  id=#{ent.id} and name=#{ent.name}  ent是前缀固定后接实体属性名
      * @param obj   实体类entity
      * @return
      */
     @Select("${sqlcount}")
     public Long queryCountByEntity(@Param("sqlcount") String sqlcount,@Param("ent") Object obj);


     @Select("${sql}")
     public Map<String, Object> queryBySql(@Param("sql") String sql);


     @Select("${sql}")
     IPage<Map<String,Object>>  getPage(IPage<Map<String,Object>> page, @Param("sql") String sql);

     @Select("${sql}")
     public String querySqlRetnString(@Param("sql") String sql);
}
