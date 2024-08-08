package com.shensu.service.check;

import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageInfo;
import com.shensu.mybatis.entity.check.AreaCdcUserVacData;
import com.shensu.mybatis.entity.check.ReplantVacStatBean;
import com.shensu.mybatis.entity.check.Student;
import com.shensu.mybatis.entity.check.StudentBase;
import com.shensu.mybatis.entity.inforInspection.InforReplantMess;
import com.shensu.mybatis.entity.school.SchoolManageBean;
import com.shensu.util.ExcelUtil;

import java.util.List;

/**
 * @author zjj
 * @version 1.0.0
 * @ClassName CheckInfoService.java
 * @Description TODO
 * @createTime 2023年04月07日 08:54
 */
public interface CheckInfoService {

    /**
     * 查询查验结果信息列表
     * @param schId
     * @param year
     * @param rounds
     * @param grade
     * @param clazz
     * @param isjz
     * @param status
     * @param name
     * @param idcard
     * @param code
     * @return
     */
    PageInfo<StudentBase> getList(String schId, String year, String rounds, String grade, String clazz,
                                  String isjz, String status, String name, String idcard, String code, Integer pageNum, Integer pageSize);

    /**
     * 根据主键获得学生信息
     * @param rowkey
     * @return
     */
    StudentBase getOneById(String rowkey);

    /**
     * 根据个案编码批量查验
     * @param childnoList
     * @return
     */
    int checkByChildno(List<String> childnoList);

    /**
     * 根据主键批量查验
     * @param rowkeyList
     * @return
     */
    int checkByRowkey(List<Long> rowkeyList);

    /**
     * 查验
     * @param childList
     * @return
     */
    Integer checkChildInfo(List<Student> childList);

    /**
     * 查验
     * @param child
     * @return
     */
    Integer checkChildInfo(Student child);

    /**
     * 查验核心方法
     * @param student 学生信息
     * @param isSaas 是否有saas信息的学生
     * @return
     */
    Student excuteCheck(Student student, boolean isSaas);

    /**
     * 根据主键批量删除
     * @param rowkeyList
     * @return
     */
    int batchDeleteById(List<String> rowkeyList);

    /**
     * 批量更新
     * @param rowkeyList
     * @param year
     * @param grade
     * @param clazz
     * @param rounds
     * @return
     */
    int batchUpdate(List<String> rowkeyList, String year, String grade, String clazz, String rounds);

    /**
     * 需补种名单列表
     * 选择地区，学校类型，学校名称，学年，年级，轮次
     * @param schId
     * @param year
     * @param rounds
     * @param grade
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<Student> getReplantRosterPage(String schId, String year, String rounds, String grade, String className,Integer pageNum, Integer pageSize);

    /**
     * 补种疫苗统计表
     * 选择地区（采用树图，最低到接种单位，接种单位默认本单位），学校类型，学年，轮次，出生日期，补种日期，是否完成补种
     * @param level
     * @param areaCode
     * @param schType
     * @param year
     * @param rounds
     * @param birthStart
     * @param birthEnd
     * @param replantStart
     * @param replantEnd
     * @param isFull
     * @return
     */
    PageInfo<JSONObject> getReplantVaccinumStatistics(Integer level, String areaCode, String schType, String year, String rounds, String birthStart, String birthEnd, String replantStart, String replantEnd, String isFull);

    /**
     * 补种疫苗统计表
     * 选择地区（采用树图，最低到接种单位，接种单位默认本单位），学校类型，学年，轮次，出生日期，补种日期，是否完成补种
     * @param level
     * @param areaCode
     * @param schType
     * @param year
     * @param rounds
     * @param birthStart
     * @param birthEnd
     * @param replantStart
     * @param replantEnd
     * @param isFull
     * @return
     */
    ReplantVacStatBean getReplantVaccinumStat(Integer level, String areaCode, String schType, String year, String rounds, String birthStart, String birthEnd, String replantStart, String replantEnd, String isFull);

    /**
     * 接种证查验汇总表
     * 选择地区，学校类型，学年，轮次
     * @param level
     * @param areaCode
     * @param schType
     * @param year
     * @param rounds
     * @return
     */
    PageInfo<AreaCdcUserVacData> getVacCertCheckStat(Integer level, String areaCode, String schType, String year, String rounds);

    void outPut(ExcelUtil excelUtil, JSONObject info, List<StudentBase> list);

    void outPutReplantRoster(ExcelUtil excelUtil, JSONObject info, List<Student> list);

    void outputReplantVacStat(ExcelUtil excelUtil, JSONObject info, List<ReplantVacStatBean> list);

    void outputVacCertCheckStat(ExcelUtil excelUtil, JSONObject info, List<AreaCdcUserVacData> list);

    /**
     * 打印补种通知单
     * @param schId
     * @param year
     * @param rounds
     * @param grade
     * @param clazz
     * @param isjz
     * @param status
     * @param name
     * @param idcard
     * @param code
     * @return
     */
    PageInfo<InforReplantMess> queryReplateMess(String schId, String year, String rounds, String grade, String clazz, String isjz, String isjzz, String status, String name, String idcard, String code, List<String> rowkeyList);

    /**
     * 打印补证通知单
     * @param schId
     * @param year
     * @param rounds
     * @param grade
     * @param clazz
     * @param isjzz
     * @param status
     * @param name
     * @param idcard
     * @param code
     * @return
     */
    PageInfo<InforReplantMess> queryReplantCard(String schId, String year, String rounds, String grade, String clazz, String isjz, String isjzz, String status, String name, String idcard, String code, List<String> rowkeyList);

    /**
     * 打印建档通知单
     * @param schId
     * @param year
     * @param rounds
     * @param grade
     * @param clazz
     * @param isjz
     * @param isjzz
     * @param status
     * @param name
     * @param idcard
     * @param code
     * @return
     */
    PageInfo<InforReplantMess> queryNoArchive(String schId, String year, String rounds, String grade, String clazz, String isjz, String isjzz, String status, String name, String idcard, String code, List<String> rowkeyList);

    /**
     * 打印查验证明
     * @param schId
     * @param year
     * @param rounds
     * @param grade
     * @param clazz
     * @param isjz
     * @param isjzz
     * @param status
     * @param name
     * @param idcard
     * @param code
     * @param rowkeyList
     * @return
     */
    PageInfo<InforReplantMess> queryCheckCert(String schId, String year, String rounds, String grade, String clazz, String isjz, String isjzz, String status, String name, String idcard, String code, List<String> rowkeyList);

    List<SchoolManageBean> getAllSchoolList();

    List<Student> queryStudentBySchIdAnySource(String schId);
}
