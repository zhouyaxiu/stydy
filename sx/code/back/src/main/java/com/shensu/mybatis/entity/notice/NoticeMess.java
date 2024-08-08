package com.shensu.mybatis.entity.notice;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE)
@ApiModel("通知模板类")
public class NoticeMess {

    private  String schId;//学校id
    private  String  noticeType;// 消息类型：0补种通知单   1：补证通知单  2：建档通知单
    private String noticeContent;//变更消息内容
}
