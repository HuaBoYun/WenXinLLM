package com.huabo.legal.exam.modules.paper.dto.response;

import com.huabo.legal.exam.modules.paper.dto.PaperDTO;
import com.huabo.legal.exam.modules.paper.dto.PaperQuDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Calendar;
import java.util.List;

@Data
@Schema(name="考试详情", description="考试详情")
public class ExamDetailRespDTO extends PaperDTO {


    @Schema(name = "单选题列表", required = true)
    private List<PaperQuDTO> radioList;

    @Schema(name = "多选题列表", required = true)
    private List<PaperQuDTO> multiList;

    @Schema(name = "判断题", required = true)
    private List<PaperQuDTO> judgeList;


    @Schema(name = "剩余结束秒数", required = true)
    public Long getLeftSeconds(){

        // 结束时间
        Calendar cl = Calendar.getInstance();
        cl.setTime(this.getCreateTime());
        cl.add(Calendar.MINUTE, getTotalTime());

        return (cl.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }

}
