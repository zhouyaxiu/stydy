package com.shensu.jsjm.mapper;

import com.shensu.jsjm.model.Inventory;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface InventoryMapper {

    void insert(@Param("list") List<Inventory> list);

    List<Inventory> getInventory(@Param("areaCode") String areaCode, @Param("vaccineId") String vaccineId);
}
