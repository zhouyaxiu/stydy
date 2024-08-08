package com.shensu.mybatis.mapper.round;

import com.shensu.mybatis.entity.round.Round;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoundMapper {
    String  findRoundName(String nowYear, String vaccName);
    String  findMaxRoundId();
    public  int  addRound(String id ,String nowYear,String vaccName,String roundName,String startTime ,String endTime,String areaCode,String schId,String claId,String startBirth,String endBirth,String claLevels);
    List<Round>  findRounds(String schId, String nowYear, String vaccName, String roundName, String temp,String state);

    int deleteRound(String roundId);

    List<Round> findAllRounds(String state);

    void updateRoundState(String roundId);

    Round getRoundById(@Param("id") String roundId);

    Round findRound(String nowYear, String vaccName, String roundName);

}


