<template>
  <el-row>
    <el-col span="10" push="7">
      <h2 class="title">合同审批单</h2>
      <div class="view1">
        <div>公司名称：中国XXX集团</div>
      </div>
      <div class="view2">
        <div>填单日期：{{ date1 }}</div>
        <div>单据编号：{{ receiptCode }}</div>
      </div>
      <div class="view3">
        <div class="view3_lump1">合同名称：</div>
        <div class="view3_lump1">{{ contractName }}</div>
        <div class="view3_lump1">承办部门：</div>
        <div class="view3_lump1">{{}}</div>
        <div class="view3_lump2">合同编号：</div>
        <div class="view3_lump2">客户AXX号</div>
        <div class="view3_lump2">经办人</div>
        <div class="view3_lump2">陈X</div>
        <div class="view3_lump3">合同等级</div>
        <div class="view3_lump3">
          <el-checkbox v-model="checked1">一般</el-checkbox>
          <el-checkbox v-model="checked2">重要</el-checkbox>
          <el-checkbox v-model="checked3">重大</el-checkbox>
        </div>
        <div class="view3_lump3">承办部门负责人</div>
        <div class="view3_lump3">XXXX</div>
      </div>

      <div class="view4">审查意见</div>
      <div class="view5">
        <div class="view5_lump1">会审部门（如有）</div>
        <div class="view5_lump1">2</div>
        <div class="view5_lump2">保密管理部门</div>
        <div class="view5_lump2">4</div>
        <div class="view5_lump2">财务管理部门</div>
        <div class="view5_lump2">6</div>
        <div class="view5_lump3">法律管理部门</div>
        <div class="view5_lump3">8</div>
        <div class="view5_lump3">承办部门意见反馈</div>
        <div class="view5_lump3">10</div>
        <div class="view5_lump4">审核部门确认</div>
        <div class="view5_lump4">12</div>
        <div class="view5_lump5">相关材料上传</div>
        <div class="view5_lump5">14</div>
      </div>
    </el-col>
  </el-row>
</template>

<script>
  export default {
    props: {
      form: {
        type: Object,
        default: () => {},
      },
      cy: {
        type: Object,
        default: () => {},
      },
    },
    data() {
      return {
        formData: [],
        checked1: true,
        checked2: false,
        checked3: false,
        date1: '',
        contractName: '',
        receiptCode: '',
      }
    },
    created() {
      this.formData = this.form.taskList

      if (this.cy) {
        this.date1 = this.cy.cydate
        this.contractName = this.cy.contractName
        this.receiptCode = this.cy.cycode
      }
    },
    methods: {
      //提交保存
      submitForm() {
        this.$refs.form.validate((valid) => {
          if (valid) {
            this.$message({
              message: '提交成功',
              type: 'success',
            })
          } else {
            this.$message({
              message: '提交失败',
              type: 'error',
            })
          }
        })
      },
    },
  }
</script>

<style scoped lang="scss">
  .el-row {
    margin-bottom: 20px;
  }
  .title {
    text-align: center;
  }
  .view1 {
    margin-left: 15px;
  }
  .view2 {
    display: flex;
    margin-top: 25px;
    justify-content: space-between;
    margin-left: 15px;
  }
  .view3 {
    width: 100%;
    height: 300px;
    display: grid;
    grid-template-columns: 16% 34% 16% 34%;
    grid-template-rows: 90px 60px 150px;
    margin-top: 30px;
  }
  .view3_lump1 {
    border: 1px solid #111;
    text-align: center;
    line-height: 90px;
  }
  .view3_lump2 {
    border: 1px solid #111;
    text-align: center;
    line-height: 60px;
  }
  .view3_lump3 {
    border: 1px solid #111;
    text-align: center;
    line-height: 150px;
  }
  .view4 {
    text-align: center;
    border: 1px solid #111;
    height: 50px;
    line-height: 50px;
  }
  .view5 {
    width: 100%;
    height: 930px;
    display: grid;
    grid-template-columns: 16% 84%;
    grid-template-rows: 90px 150px 150px 200px 200px 90px 70px;
  }
  .view5_lump1 {
    border: 1px solid #111;
    text-align: center;
    line-height: 90px;
  }
  .view5_lump2 {
    border: 1px solid #111;
    text-align: center;
    line-height: 150px;
  }
  .view5_lump3 {
    border: 1px solid #111;
    text-align: center;
    line-height: 200px;
  }
  .view5_lump4 {
    border: 1px solid #111;
    text-align: center;
    line-height: 90px;
  }
  .view5_lump5 {
    border: 1px solid #111;
    text-align: center;
    line-height: 70px;
  }
</style>
