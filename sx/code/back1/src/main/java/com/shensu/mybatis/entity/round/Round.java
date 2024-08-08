package com.shensu.mybatis.entity.round;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Round {
    private String roundId ;
    private String roundName;
    private String roundVaccName;
    private String roundYear;
    private String  ifTask;
    private String startTime;
    private String endTime;
    private String areaCode;
    private String schIds;
    private String schNames;
    private String claIds;
    private String claNames;
    private String startBirth;
    private String endBirth;
    private String deleteState;
    private String  addtime;
    private String claLevels;

}
