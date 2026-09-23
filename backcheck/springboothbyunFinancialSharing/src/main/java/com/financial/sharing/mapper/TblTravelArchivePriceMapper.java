package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblTravelArchivePrice;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 价格信息Mapper
 */
public interface TblTravelArchivePriceMapper extends BaseMapper<TblTravelArchivePrice> {

    /**
     * 根据档案ID查询
     */
    List<TblTravelArchivePrice> selectByArchiveId(@Param("archiveId") String archiveId);

    /**
     * 根据城市查询
     */
    List<TblTravelArchivePrice> selectByCity(@Param("city") String city);

    /**
     * 根据房型查询
     */
    List<TblTravelArchivePrice> selectByRoomType(@Param("roomType") String roomType);

    /**
     * 根据车型查询
     */
    List<TblTravelArchivePrice> selectByVehicleType(@Param("vehicleType") String vehicleType);

    /**
     * 根据是否启用查询
     */
    List<TblTravelArchivePrice> selectByIsEnabled(@Param("isEnabled") Integer isEnabled);

    /**
     * 根据档案ID和城市查询
     */
    List<TblTravelArchivePrice> selectByArchiveIdAndCity(@Param("archiveId") String archiveId, @Param("city") String city);
}
