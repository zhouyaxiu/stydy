package com.shensu.mybatis.entity.check;

import lombok.Data;

/**
 * @author zjj
 * @version 1.0.0
 * @ClassName VaccinInfo.java
 * @Description 特定疫苗编码
 * @createTime 2023年09月07日 17:04
 */
@Data
public class VaccinInfo {

    private String FBactID;

    private String FBactAlias;

    private String FBactSeq;

    private String FBactType;

    private String FBactName;

    private String FBactShortName;

    private String FBactEnFullName;

    private String FBactEnName;

    private String FBactShowEnName;

    private String FEnabled;

    private String FBactKind;

    private String FShotSpace;

    private String FCountryCode;


}
