package com.huabo.monitor.vo.param;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblBugCriterionEntityParam {
	
    @Schema(name = "主键ID,自动增长")
    private Integer bugcriid;
	
    @Schema(name = "缺陷级别")
    private String bugcrilevel;
	
    @Schema(name = "缺陷定义")
    private String bugcridefine;
	
    @Schema(name = "定量标准")
    private String bugcriration;
	
    @Schema(name = "定性标准")
    private String bugcristability;
	
    @Schema(name = "缺陷状态")
    private Integer status;
    
    
    @Schema(name = "主键ID,自动增长")
    private List<Integer> bugcriids;
 
	
}
