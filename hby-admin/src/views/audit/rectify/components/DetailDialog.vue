<template>
  <el-dialog
    title="问题整改详情"
    :visible.sync="dialogVisible"
    width="60%"
    :before-close="handleClose"
    class="detail-dialog"
  >
    <div v-loading="loading">
      <el-form label-width="150px" class="detail-form">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="一级单位">
              <span>{{ detailData.firstorg }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="具体责任单位">
              <span>{{ detailData.specificdept }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="问题来源">
              <span>{{ detailData.sourceproblem }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="审计报告出具年份">
              <span>{{ detailData.issuanceyear }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="问题类别">
              <span>{{ detailData.problemtype }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="整改类型">
              <span>{{ detailData.cortype }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">
          问题在审计报告中的序号及表述
        </el-divider>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="一级标题">
              <span>{{ detailData.firsttitle }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="二级标题">
              <span>{{ detailData.secondtitle }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="三级标题">
              <span>{{ detailData.thirdtitle }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="在审计报告中的表述">
              <span>{{ detailData.auditreportdesc }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">整改责任清单</el-divider>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="具体责任单位">
              <span>{{ detailData.specificdeptlist }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="具体问题表述">
              <span>{{ detailData.problemdesc }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="问题金额（万元）">
              <span>{{ detailData.problemmoney }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负有监督管理责任的主管部门">
              <span>{{ detailData.supermanagdept }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">整改目标清单</el-divider>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="法规政策依据">
              <span>{{ detailData.lawspocor }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="整改要求">
              <span>{{ detailData.correquire }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="整改时限">
              <span>{{ detailData.cortime }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="整改完成标准">
              <span>{{ detailData.corstandard }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="细化的整改措施">
              <span>{{ detailData.cormeasure }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="对应的完成时间">
              <span>{{ detailData.completiontime }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">具体责任单位整改责任人</el-divider>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="整改第一责任人">
              <span>{{ detailData.firstperson }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="协助整改工作的领导">
              <span>{{ detailData.assistleader }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="牵头整改部门责任人">
              <span>{{ detailData.leaderperson }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="配合整改部门责任人">
              <span>{{ detailData.cooperson }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="审计部门责任人">
              <span>{{ detailData.auditperson }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="已采取的整改措施">
              <span>{{ detailData.takemeasure }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">具体责任单位整改情况</el-divider>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="项目数（个）">
              <span>{{ detailData.itemquantity }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="问题整改金额（万元）">
              <span>{{ detailData.procormoney }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="追缴资金（万元）">
              <span>{{ detailData.recomoney }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="归还原渠道（万元）">
              <span>{{ detailData.returnmoney }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="统筹盘活（万元）">
              <span>{{ detailData.overallamount }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="加快拨付（万元）">
              <span>{{ detailData.accdisbur }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="退抵税费（万元）">
              <span>{{ detailData.refundtaxes }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="调整账表（万元）">
              <span>{{ detailData.adjuststatement }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="调整金融业务服务（万元）">
              <span>{{ detailData.adjustmoney }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="补办手续等（万元）">
              <span>{{ detailData.suppamount }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="其他方式">
              <span>{{ detailData.way }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="其他金额（万元）">
              <span>{{ detailData.amount }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="土地面积（公顷）">
              <span>{{ detailData.landarea }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="矿产资源（万吨）">
              <span>{{ detailData.mineresource }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="单位（个）">
              <span>{{ detailData.unit }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="家庭（户）">
              <span>{{ detailData.family }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="人数（人）">
              <span>{{ detailData.numberpeople }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="住房（套）">
              <span>{{ detailData.housing }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="追责问题情形">
              <span>{{ detailData.clearform }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="追责人数">
              <span>{{ detailData.accnumberpeo }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="完善制度数量（个）">
              <span>{{ detailData.accquantity }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="文件名称">
              <span>{{ detailData.filename }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="未整改到位问题原因">
              <span>{{ detailData.notrectreason }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="是否已完成整改">
              <span>{{ detailData.corstatus }}</span>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关 闭</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    name: 'DetailDialog',
    props: {
      visible: {
        type: Boolean,
        default: false,
      },
      detailData: {
        type: Object,
        default: () => ({}),
      },
    },
    data() {
      return {
        loading: false,
        dialogVisible: false,
      }
    },
    watch: {
      visible(val) {
        this.dialogVisible = val
      },
      dialogVisible(val) {
        if (!val) {
          this.$emit('update:visible', false)
        }
      },
    },
    methods: {
      handleClose() {
        this.dialogVisible = false
      },
    },
  }
</script>

<style lang="scss" scoped></style>
