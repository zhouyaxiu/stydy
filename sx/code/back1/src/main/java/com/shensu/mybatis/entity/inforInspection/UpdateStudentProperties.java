package com.shensu.mybatis.entity.inforInspection;

/**
 * @version 1.0.0
 * @ClassName UpdateStudentProperties.java
 * @Description TODO
 * @createTime 2023年04月06日 11:06
 */
public class UpdateStudentProperties {
    public UpdateStudentProperties(){};
    public UpdateStudentProperties(String propertiesName, String propertiesChineseName, int type){
        this.propertiesName = propertiesName;
        this.propertiesChineseName = propertiesChineseName;
        this.type = type;
    };
    public UpdateStudentProperties(String propertiesName, String propertiesChineseName){
        this.propertiesName = propertiesName;
        this.propertiesChineseName = propertiesChineseName;
    };

    private String propertiesName;
    private String propertiesChineseName;
    private String beforValue;
    private String afterValue;
    private int type;//0 普通格式 1需要转换的值格式 2日期格式

    public String getPropertiesName() {
        return propertiesName;
    }
    public void setPropertiesName(String propertiesName) {
        this.propertiesName = propertiesName;
    }
    public String getPropertiesChineseName() {
        return propertiesChineseName;
    }
    public void setPropertiesChineseName(String propertiesChineseName) {
        this.propertiesChineseName = propertiesChineseName;
    }
    public String getBeforValue() {
        return beforValue;
    }
    public void setBeforValue(String beforValue) {
        this.beforValue = beforValue;
    }
    public String getAfterValue() {
        return afterValue;
    }
    public void setAfterValue(String afterValue) {
        this.afterValue = afterValue;
    }
    @Override
    public String toString() {
        String result = "";
        String tembeforValue = "";
        String temafterValue = "";
        if(beforValue != null){
            tembeforValue = beforValue;
        }
        if(afterValue != null){
            temafterValue = afterValue;
        }
        if(tembeforValue.equals(temafterValue)){
//			result = propertiesChineseName +tembeforValue+"被改为"+temafterValue+"; ";
        }else{
            if("".equals(temafterValue)){
                result = propertiesChineseName + ":被删除; ";
            }else{
                result = propertiesChineseName + ":" +tembeforValue+"被改为"+temafterValue+"; ";
            }
        }
//		result = propertiesChineseName +beforValue+"被改为"+afterValue+"; ";
        return result;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }

}
