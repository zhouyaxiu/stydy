package com.shensu.mybatis.entity.systemmanage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysLog {
  private  String  operaType;
  private String  modelName;
  private String loginName;
  private String accountName;
  private String logTime;
  private String modelObject;
}
