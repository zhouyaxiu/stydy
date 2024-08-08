package com.shensu.mybatis.entity.check;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zjj
 * @version 1.0.0
 * @ClassName VaccinInfo.java
 * @Description 疫苗信息
 * @createTime 2023年04月26日 13:34
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VaccinInfo {

    //疫苗名称
    String name;
    //剂次
    String index;
    //状态
    String status;

}
