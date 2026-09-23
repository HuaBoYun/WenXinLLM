<template>
  <div>
    <div class="img_left"></div>
    <div class="img_right"></div>
    <div class="header">
      <div class="h_title">
        <div class="h_text">功能地图</div>
      </div>
      <div class="list">
        <div class="list_d">
          <div
            v-for="(item, index) in cardList1"
            :class="index % 2 ? 'pp' : 'd'"
            :key="item + 1"
          >
            <div class="d_title">{{ item.title }}</div>
            <div class="d_content" v-for="item in item.content" :key="item">
              <div class="content_d">{{ item }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <el-row :gutter="10">
      <el-col :span="14" style="margin-top: 10px">
        <table1></table1>
      </el-col>
      <el-col :span="10" style="margin-top: 10px">
        <times></times>
      </el-col>
      <el-col :span="8" style="margin-top: 10px">
        <chart1></chart1>
      </el-col>
      <el-col :span="8" style="margin-top: 10px">
        <chart2></chart2>
      </el-col>
      <el-col :span="8" style="margin-top: 10px">
        <chart3></chart3>
      </el-col>
      <el-col :span="24" style="margin-top: 10px">
        <chart4></chart4>
      </el-col>
      <el-col :span="12" style="margin-top: 10px">
        <chart5></chart5>
      </el-col>
      <el-col :span="12" style="margin-top: 10px">
        <chart6></chart6>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import table1 from './table1.vue'
  import times from './times.vue'
  import chart1 from './chart1.vue'
  import chart2 from './chart2.vue'
  import chart3 from './chart3.vue'
  import chart4 from './chart4.vue'
  import chart5 from './chart5.vue'
  import chart6 from './chart6.vue'
  export default {
    components: {
      table1,
      times,
      chart1,
      chart2,
      chart3,
      chart4,
      chart5,
      chart6,
    },
    data() {
      return {
        cardList1: [
          {
            title: '合同起草',
            content: [
              '编辑留痕',
              '审批要点',
              '智能抽取',
              '合同比对',
              '智能合同审查',
            ],
          },
          {
            title: '合同签订',
            content: ['生成条形码水印', '电子签署', '物理用印'],
          },
          {
            title: '合同登记',
            content: ['文本归档', '合同补录', '合同比对'],
          },
          {
            title: '合同履约',
            content: ['履约阶段', '履约计划', '履约变更'],
          },
          {
            title: '合同变动',
            content: ['变更', '补充', '解除', '锁定合同'],
          },
          {
            title: '合同关闭',
            content: ['相对方评价', '合同归档', '管理建议'],
          },
        ],
      }
    },
  }
</script>

<style scoped lang="scss">
  .header {
    background: url('https://eopdemo.landray.com.cn/ui-ext/kmaps/kms_working_map/model3/images/map-bg@2x.jpg')
      no-repeat center;
    background-size: 100% 100%;
    height: 450px;
    /* position: relative; */
  }
  .h_title {
    padding-top: 19px;
    text-align: center;
  }
  .h_text {
    display: inline-block;
    padding: 6px 11px;
    background: #4285f4;
    font-size: 16px;
    color: #fff;
    border-radius: 10px;
    cursor: pointer;
  }
  .img_right {
    position: absolute;
    top: 220px;
    right: 0;
    background: url('https://eopdemo.landray.com.cn/ui-ext/kmaps/kms_working_map/model3/images/bg-female.png')
      no-repeat center;
    height: 250px;
    width: 198px;
    background-size: 100% 100%;
  }
  .img_left {
    position: absolute;
    top: 220px;
    background: url('https://eopdemo.landray.com.cn/ui-ext/kmaps/kms_working_map/model3/images/bg-man.png')
      no-repeat center;
    height: 250px;
    width: 198px;
    background-position-x: -55px !important;
    background-size: 100% 100%;
  }
  .list {
    padding-top: 25px;
    text-align: center;
    z-index: 100;
  }
  .list_d {
    display: inline-block;
    width: 90%;
    position: relative;
  }
  .d {
    vertical-align: top;
    display: inline-block;
    width: 14%;
    margin-right: 24px;
    border-radius: 4%;
    box-shadow: 0 2px 5px 0 rgb(0 12 98 / 10%);
    overflow: hidden;
    background-color: #fff;
  }
  .pp {
    vertical-align: top;
    display: inline-block;
    width: 14%;
    margin-right: 24px;
    border-radius: 4%;
    box-shadow: 0 2px 5px 0 rgb(0 12 98 / 10%);
    overflow: hidden;
    background-color: #fff;
    margin-top: 20px;
  }
  .d:last-child {
    margin-right: 0;
  }
  .d_title {
    background: url('https://eopdemo.landray.com.cn/ui-ext/kmaps/kms_working_map/model3/images/headBg.png')
      no-repeat center;
    height: 40px;
    margin-top: 0px;
    line-height: 46px;
    color: #fff;
    font-size: 16px;
  }
  .content_d {
    background: #fff;
    padding: 9px 0;
    cursor: pointer;
    &:hover {
      background: #f2f2f2;
      color: #719cf8;
    }
  }
</style>
