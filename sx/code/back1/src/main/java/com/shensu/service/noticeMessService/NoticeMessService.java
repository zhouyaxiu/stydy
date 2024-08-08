package com.shensu.service.noticeMessService;

import com.shensu.mybatis.entity.notice.NoticeMess;
import com.shensu.mybatis.mapper.noticeMapper.NoticeMessMapper;
import com.shensu.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
public class NoticeMessService {
    private static final Logger logger = LoggerFactory.getLogger(NoticeMessService.class);

    @Resource
    private NoticeMessMapper  noticeMessMapper;

    public List<NoticeMess> findNoticeMess(String  schId,String noticeType) {
        String[] noticeTypes=null;
        if(StringUtils.isNotEmpty(noticeType)){
            noticeTypes= noticeType.split(",");
        }
        if(StringUtils.isEmpty(schId)){
            List<NoticeMess> list = new ArrayList<>();
            if("0".equals(noticeTypes[0])){
                NoticeMess mess1  = new NoticeMess(schId,"0","各位家长到医院补打疫苗时请携带“儿童预防接种证”！如有问题请及时联系社区卫生服务中心接种门诊。");
                list.add(mess1);
            }else if("1".equals(noticeTypes[0])){
                NoticeMess mess2  = new NoticeMess(schId,"1","经仔细查验发现该学生无接种证，请您携带孩子及有效证件凭此通知单到居住地所在社区卫生服务中心办理补证手续。");
                list.add(mess2);
            }else if("2".equals(noticeTypes[0])){
                NoticeMess mess3  = new NoticeMess(schId,"2","经仔细查验发现该学生无接种证，请您携带孩子及有效证件凭此通知单到居住地所在社区卫生服务中心办理补证手续。");
                list.add(mess3);
            }
            return list;
        }
        List<NoticeMess> list= noticeMessMapper.queryNoticeBySchIdAndType(schId,noticeTypes);
        if(list ==null || list.size()==0){
            //说明还没有保存过这个信息，需要自己封装固定信息，并且保存到数据库后再返回给前台
            NoticeMess mess1  = new NoticeMess(schId,"0","各位家长到医院补打疫苗时请携带“儿童预防接种证”！如有问题请及时联系社区卫生服务中心接种门诊");
            NoticeMess mess2  = new NoticeMess(schId,"1","经仔细查验发现该学生无接种证，请您携带孩子及有效证件凭此通知单到居住地所在社区卫生服务中心办理补证手续");
            NoticeMess mess3  = new NoticeMess(schId,"2","经仔细查验发现该学生无接种证，请您携带孩子及有效证件凭此通知单到居住地所在社区卫生服务中心办理补证手续");
            //保存三个信息到数据库
            noticeMessMapper.saveNoticeMess(mess1);
            noticeMessMapper.saveNoticeMess(mess2);
            noticeMessMapper.saveNoticeMess(mess3);

            if(noticeTypes != null){
                for(int i=0;i<noticeTypes.length;i++){
                    if("0".equals(noticeTypes[0])){
                        list.add(mess1);
                    }else if("1".equals(noticeTypes[0])){
                        list.add(mess2);
                    }else if("2".equals(noticeTypes[0])){
                        list.add(mess3);
                    }
                }
            }
        }
        return  list ;
    }
    public String updateNotify(NoticeMess noticeMess) {
        String msg = "修改失败！";
        Integer num  =noticeMessMapper.updateNoticeMess(noticeMess);
        if(num>0){
            msg="修改成功";
        }
        return msg;
    }


}
