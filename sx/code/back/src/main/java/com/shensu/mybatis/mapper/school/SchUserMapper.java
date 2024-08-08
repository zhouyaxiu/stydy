package com.shensu.mybatis.mapper.school;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.shensu.mybatis.entity.school.SchUserBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zcy
 * @ClassName SchUserMapper
 * @Description
 * @createTime 2023/4/6
 */
@Mapper
public interface SchUserMapper {
    List<SchUserBean> getSchUserBySchId(String schId);

    Integer getSchUserNumBySchId(String schId);

    void insertOneSchUser(@Param("schId") String schId, @Param("loginName") String loginName, @Param("password") String password);

    void resetSchUser(@Param("id") String id, @Param("password") String password);

    void delSchUserByIds(List<SchUserBean> beans);

    void batchInsertSChUSer(List<SchUserBean> beans);

    @DS("ningbo")
    List<SchUserBean> getSchUserBySchIdFromNingBo(String schId);
}
