<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row>
      <el-col :span="24">
        <h3>基本信息</h3>
      </el-col>
      <el-col :span="24" :push="16">
        <p style="margin-bottom: 15px">加入监控日期：2022-03-19 19:53:37</p>
      </el-col>
      <el-col :span="12" :push="1">
        <p style="margin-bottom: 5px">统一社会信用代码： XXXX</p>
      </el-col>
      <el-col :span="12">
        <p style="margin-bottom: 5px">公司类型： XXXX</p>
      </el-col>
      <el-col :span="12" :push="1">
        <p style="margin-bottom: 5px">法人代表： XXXX</p>
      </el-col>
      <el-col :span="12">
        <p style="margin-bottom: 5px">注册资本： XXXX</p>
      </el-col>
      <el-col :span="12" :push="1">
        <p style="margin-bottom: 5px">成立时间： XXXX</p>
      </el-col>
      <el-col :span="12">
        <p style="margin-bottom: 5px">企业地址： XXXX</p>
      </el-col>
      <el-col :span="12" :push="1">
        <p style="margin-bottom: 5px">行业：XXXX</p>
      </el-col>

      <el-col :span="24">
        <h3>监控总览</h3>
      </el-col>
      <el-col :span="22" :push="1">
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane label="风险分析" name="first">
            <div style="margin-bottom: 15px">
              涉诉：
              <el-tag type="warning">裁判文书（其他）86条</el-tag>
              <el-tag type="danger">裁判文书（金融）13条</el-tag>
              <el-tag type="warning">裁判文书（非金融）11条</el-tag>
              <el-tag type="danger">被执行人76条</el-tag>
              <el-tag type="danger">失信被执行人8条</el-tag>
              <el-tag type="warning">开庭公告27条</el-tag>
              <el-tag type="warning">法院公告12条</el-tag>
            </div>
            <div style="margin-bottom: 15px">
              经营：
              <el-tag>经营数据1条</el-tag>
              <el-tag type="warning">营业收入下降较快</el-tag>
              <el-tag>亏损大幅收窄</el-tag>
              <el-tag type="warning">资产负债率偏高</el-tag>
              <el-tag type="warning">运营能力过低</el-tag>
              <el-tag type="warning">资不抵债，需要股东注资</el-tag>
            </div>
            <div style="margin-bottom: 15px">
              舆情：
              <el-tag type="danger">停业破产 253条</el-tag>
              <el-tag type="danger">涉诉 52条</el-tag>
              <el-tag type="danger">信用风险 19条</el-tag>
              <el-tag type="danger">债务风险 105</el-tag>
              <el-tag type="danger">条财务风险 2条</el-tag>
              <el-tag type="danger">安全事件 1条</el-tag>
              <el-tag type="danger">造假欺诈 1条</el-tag>
              <el-tag type="danger">质量风险 1条</el-tag>
              <el-tag type="danger">高管负面 4条</el-tag>
              <el-tag type="danger">亏损 21条</el-tag>
              <el-tag type="danger">市场风险 2条</el-tag>
              <el-tag type="danger">合同纠纷 15条</el-tag>
              <el-tag type="warning">收购重组 95条</el-tag>
              <el-tag type="warning">投资融资 13条</el-tag>
              <el-tag type="warning">增持减持 5条</el-tag>
              <el-tag type="warning">人事变动 38条</el-tag>
              <el-tag type="warning">股权变动 9条</el-tag>

              <el-tag>合作经营 4条</el-tag>
              <el-tag>成果奖项 1条</el-tag>
              <el-tag>产品与服务信息 4条</el-tag>
              <el-tag>新品发布 3条</el-tag>
              <el-tag>其他 251条</el-tag>
            </div>
            <div style="margin-bottom: 15px">工商：无数据</div>
            <div style="margin-bottom: 15px">投融资：无数据</div>
            <div style="margin-bottom: 15px">违规：无数据</div>
            <div style="margin-bottom: 15px">税务：无数据</div>
          </el-tab-pane>
        </el-tabs>
      </el-col>
      <el-col :span="24">
        <h3>监控事件</h3>
      </el-col>

      <el-col :span="22" :push="1">
        <el-tabs v-model="activeName2" @tab-click="handleClick2">
          <el-tab-pane label="风险分析" name="first"></el-tab-pane>
          <el-tab-pane label="法院公告" name="second"></el-tab-pane>
          <el-tab-pane label="行政处罚" name="three"></el-tab-pane>
        </el-tabs>
      </el-col>
    </el-row>
  </el-dialog>
</template>

<script>
  export default {
    name: 'Company',
    inheritAttrs: false,
    props: [],
    data() {
      return {
        dialogFormVisible: false,
        title: '天津国兴资本',
        activeName: 'first',
        activeName2: 'first',
      }
    },
    methods: {
      handleClick() {},
      handleClick2() {},
      showEdit(row) {
        this.dialogFormVisible = true
      },
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>

<style>
  .yuan {
    width: 200px;
    height: 200px;
    background-color: red;
    border-top-left-radius: 50% 20px;
    border-top-right-radius: 50% 20px;
  }
</style>
