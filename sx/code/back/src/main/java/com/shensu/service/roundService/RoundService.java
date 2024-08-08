package com.shensu.service.roundService;

import com.github.pagehelper.PageInfo;
import com.shensu.mybatis.entity.round.Round;

import java.util.List;

public interface RoundService {
    public String  findRoundName(String nowYear, String vaccName );
    public String findMaxRoundId();
    public  String addRound(String nowYear,String vaccName,String roundName,String startTime ,String endTime,String areaCode,String schId,String claId,String claLevels,String startBirth,String endBirth) throws Exception;

    PageInfo<Round> findRounds(String areaCode,String areaLevel ,String nowYear, String vaccName, String roundName,String   state,int pageNum,int pageSize);

    public  void   deleteRound (String roundId) throws Exception;

    List<Round> findAllRounds(String state);

    void updateRound(String roundId);
}
