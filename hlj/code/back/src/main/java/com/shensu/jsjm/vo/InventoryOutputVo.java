package com.shensu.jsjm.vo;

import com.shensu.jsjm.model.Inventory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
@Data
public class InventoryOutputVo {

    @ApiModelProperty("实时库存集合")
    private List<Inventory> inventoryList;

}
