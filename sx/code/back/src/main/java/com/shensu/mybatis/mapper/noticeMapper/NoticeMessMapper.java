package com.shensu.mybatis.mapper.noticeMapper;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.shensu.mybatis.entity.notice.NoticeMess;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeMessMapper {
    public List<NoticeMess>  queryNoticeBySchIdAndType(String schId,String[] noticeTypes);
    public void saveNoticeMess(NoticeMess notice);
    public Integer updateNoticeMess(NoticeMess notice);
    //从宁波平台查询数据
    @DS("ningbo")
    public List<NoticeMess>  queryNoticeBySchId(String schId);

    //批量插入
    public  void   bachInsertNotice(List<NoticeMess> list );
}
