package com.shensu.service.roundService.impl;

import com.ctc.wstx.util.DataUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shensu.mybatis.entity.check.Student;
import com.shensu.mybatis.entity.highStudent.HighStudCheck;
import com.shensu.mybatis.entity.highStudent.HighStudent;
import com.shensu.mybatis.entity.round.Round;
import com.shensu.mybatis.mapper.round.RoundMapper;
import com.shensu.mybatis.mapper.school.SchoolMapper;
import com.shensu.service.highStuService.HighStuService;
import com.shensu.service.roundService.RoundService;
import com.shensu.util.CheckFunction;
import com.shensu.util.DateUtils;
import com.shensu.util.StringUtils;
import io.swagger.models.auth.In;
import lombok.Data;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoundServiceImpl implements RoundService {

    @Resource
    private RoundMapper  roundMapper ;
    @Resource
    private HighStuService  stuService;
    @Resource
    private SchoolMapper  schoolMapper;

    /**
     * 查询最新轮次名称
     */
    @Override
    public String  findRoundName(String nowYear, String vaccName) {
        String roundName= roundMapper.findRoundName(nowYear,vaccName);
        if(StringUtils.isBlank(roundName)) {
            return  "第1轮";
        }
        roundName=Integer.parseInt(roundName)+1+"";

        return "第"+roundName+"轮";
    }
    /**
     * 获取最新轮次id
     * */
    @Override
    public String findMaxRoundId() {
        String currentId  =  roundMapper.findMaxRoundId();
        if(StringUtils.isBlank(currentId)){
            currentId="1";
        }else{
            currentId=Integer.parseInt(currentId)+1+"";
        }
        return currentId;
    }
    @Override
    public PageInfo<Round> findRounds(String schId ,String nowYear, String vaccName, String roundName,String state,int pageNum,int pageSize) {
        //处理关联表
        StringBuilder  build  = new StringBuilder();
        //处理一下查询是通过什么进行查询
        if("1".equals(state)){
            PageHelper.startPage(pageNum,pageSize);
        }
        List<Round> list  = roundMapper.findRounds(schId,nowYear,vaccName,roundName,build.toString(),state);
        //处理每个轮次对应的学校信息
        list.forEach(round -> {
            String  schIds  = round.getSchIds();
            String clsIds  =round.getClaIds();
            round.setRoundName("第"+round.getRoundName()+"轮");
            List<String> schName = schoolMapper.findSchoolByIds(schId);
            round.setSchNames(schName.stream().map(String::valueOf).collect(Collectors.joining(",")));
            if(StringUtils.isNotEmpty(clsIds)){
                String[] claId = clsIds.split(",");
                List<String> claName = schoolMapper.findClasByIds(claId);
                round.setClaNames(claName.stream().map(String::valueOf).collect(Collectors.joining(",")));
            }
        });
        return new PageInfo<>(list);
    }


    /**
     * 删除一个轮次的所有用户
     * */
    @Override
    public void   deleteRound(String roundId) throws Exception{
        String msg  ="";
        int  num = roundMapper.deleteRound(roundId);
        if(num>0){
            //删除这个id下面的查验记录
            num =   stuService.deleteCheckResultById(roundId);
        }else{

            throw new RuntimeException("轮次信息删除失败") ;
        }
    }

    @Override
    public List<Round> findAllRounds(String state) {
        return  roundMapper.findAllRounds(state);
    }

    @Override
    public void updateRound(String roundId) {
        roundMapper.updateRoundState(roundId);
    }

    /**
     * 添加轮次信息
     * */
    @Override
    public String addRound(String nowYear,String vaccName,String roundName,String startTime ,String endTime,String areaCode,String schId,String claId,String claLevels,String startBirth,String endBirth) throws Exception {
        String msg ="轮次信息插入成功";
        String id  =findMaxRoundId();
        int num  = roundMapper.addRound(id,nowYear,vaccName,roundName,startTime,endTime,areaCode,schId,claId,startBirth,endBirth,claLevels);
        if(num<=0){
            throw  new Exception("轮次信息插入失败");
        }

        String[] schIds  = schId.split(",");
        for(int i=0;i<schIds.length;i++){
            //添加该轮次下查询的学生信息一个学校一个学校的添加
            List<HighStudCheck>  checksList  = new ArrayList<>();
            List<HighStudent>  stusList  = stuService.findCheckStuds(schIds[i],claId,startBirth,endBirth);
            if(stusList!=null  && stusList.size()>0){
                for(int index=0;index<stusList.size();index++){
                    HighStudent  stu  = stusList.get(index);
                    HighStudCheck stuCheck  = new HighStudCheck();
                    stuCheck.setRoundId(id);
                    stuCheck.setStuNo(stu.getStuNo());
                    //将每个学生的查验结果进行保存
                    if("麻腮风疫苗".equals(vaccName)){
                        CheckFunction.checkMaZhen(stu,stuCheck,startTime,endTime);
                    }else if("水痘疫苗".equals(vaccName)){
                        CheckFunction.shuiDou(stu,stuCheck);
                    }else if("流感疫苗".equals(vaccName)){
                        CheckFunction.liuGan(stu,stuCheck);
                    }
                    checksList.add(stuCheck);
                }
            }
            //一个学校数据处理后批量插入查验结果数据库,如果添加的轮次的学校中没有学生则不需要进行插入
            if(checksList.size()>0){
             stuService.insertStuCheckResult(checksList);
            }
        }
        return msg;
    }




}
