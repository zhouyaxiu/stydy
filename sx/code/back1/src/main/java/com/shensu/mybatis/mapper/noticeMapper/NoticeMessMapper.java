package com.shensu.mybatis.mapper.noticeMapper;


import com.shensu.mybatis.entity.notice.NoticeMess;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeMessMapper {
    public List<NoticeMess>  queryNoticeBySchIdAndType(String schId,String[] noticeTypes);
    public void saveNoticeMess(NoticeMess notice);
    public Integer updateNoticeMess(NoticeMess notice);
}
