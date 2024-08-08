package com.shensu.jsjm.model;

import lombok.Data;

/**
 * @description:todo
 * @author：GJ
 * @date：2023/11/30 13:59
 */
@Data
public class ResponseInventory {
    private String GBCODELEVEL ;
    private String GOODSTYPE ;
    private String GOODSSORTCODE ;
    private int    STORENUM ;
    private int    OVERDUENum ;
    private int    ONEMONTHNum ;
    private int    THREEMONTHNum ;

}
