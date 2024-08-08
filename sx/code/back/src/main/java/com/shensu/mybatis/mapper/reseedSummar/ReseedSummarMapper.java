package com.shensu.mybatis.mapper.reseedSummar;

import com.shensu.mybatis.entity.reseedSummar.StaticInforInspectionBean;
import com.shensu.mybatis.entity.reseedSummar.StaticInforInspectionBeanOne;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zcy
 * @ClassName ReseedSummarMapper
 * @Description 补种汇总表
 * @createTime 2023/4/19
 */
@Mapper
public interface ReseedSummarMapper {
    List<StaticInforInspectionBean> queryList(@Param("claIds") List<String> claIds,@Param("roundId") String roundId);

    List<String> getClaIdBySchIdAndYearAndLevel(@Param("schId") String schId,@Param("year") String year,@Param("level") String level);

    List<StaticInforInspectionBeanOne> queryListOne(@Param("areaSql") String areaSql, @Param("selectNameSql") String selectNameSql, @Param("groupBySql") String groupBysql,
                                                    @Param("schId") String schId, @Param("year") String year, @Param("grade") String grade,
                                                    @Param("round") String round, @Param("className") String className, @Param("schType") String schType);

    List<StaticInforInspectionBeanOne> querySchNumOne(@Param("areaSql") String areaSql, @Param("selectNameSql") String selectNameSql, @Param("groupBySql") String groupBysql,
                                                @Param("schId") String schId, @Param("year") String year, @Param("grade") String grade,
                                                @Param("round") String round, @Param("className") String className,@Param("schType") String schType);
}
