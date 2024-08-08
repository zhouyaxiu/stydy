package com.shensu.jsjm.vo;

import com.shensu.jsjm.model.WisdomInventory;
import lombok.Data;

import java.util.List;

/**
 * @description:todo
 * @author：GJ
 * @date：2023/11/29 10:05
 */
@Data
public class WisdomInventoryOutputVo {
    /**
     * 免规
     * */
    List<WisdomInventory> NIPwisdomInventoryList;
    /**
     * 非免规
     * */
    List<WisdomInventory> wisdomInventoryList;

}
