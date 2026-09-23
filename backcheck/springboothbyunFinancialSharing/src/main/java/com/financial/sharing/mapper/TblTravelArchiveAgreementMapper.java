package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblTravelArchiveAgreement;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 合作协议Mapper
 */
public interface TblTravelArchiveAgreementMapper extends BaseMapper<TblTravelArchiveAgreement> {

    /**
     * 根据档案ID查询
     */
    List<TblTravelArchiveAgreement> selectByArchiveId(@Param("archiveId") String archiveId);

    /**
     * 根据协议名称模糊查询
     */
    List<TblTravelArchiveAgreement> selectByAgreementNameLike(@Param("agreementName") String agreementName);

    /**
     * 根据是否启用查询
     */
    List<TblTravelArchiveAgreement> selectByIsEnabled(@Param("isEnabled") Integer isEnabled);

    /**
     * 根据文件类型查询
     */
    List<TblTravelArchiveAgreement> selectByContentType(@Param("contentType") String contentType);

    /**
     * 根据档案ID和是否启用查询
     */
    List<TblTravelArchiveAgreement> selectByArchiveIdAndIsEnabled(@Param("archiveId") String archiveId, @Param("isEnabled") Integer isEnabled);
}
