<template>
  <div>
    <el-card>
      <div class="img_left"></div>
      <div class="img_right"></div>
      <div class="header">
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
    </el-card>
    <el-card>
      <div class="content111">
        <span class="icon_contain" v-for="item in icons" :key="item + 1">
          <img :src="item.url" alt="" />
          <div>{{ item.title }}</div>
        </span>
      </div>
    </el-card>
    <el-card>
      <el-row :gutter="20">
        <el-col :lg="16" class="magTop">
          <div class="title">
            <card-title first="财务制度" second="财税政策" class="card" />
          </div>
        </el-col>
        <el-col :lg="8">
          <person first="财务会计" second="管理会计" class="card" />
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
  import icon1 from '../../../assets/selfHelp/icon1.png'
  import icon2 from '../../../assets/selfHelp/icon2.png'
  import icon3 from '../../../assets/selfHelp/icon3.png'
  import icon4 from '../../../assets/selfHelp/icon4.png'
  import icon5 from '../../../assets/selfHelp/icon5.png'
  import icon6 from '../../../assets/selfHelp/icon6.png'
  import icon7 from '../../../assets/selfHelp/icon7.png'
  import icon8 from '../../../assets/selfHelp/icon8.png'
  import CardTitle from './components/CardTitle'
  import person from './components/person'
  export default {
    components: { CardTitle, person },
    data() {
      return {
        cardList1: [
          {
            title: '预算管理',
            content: [
              '预算编制',
              '预算数据',
              '预算调整',
              '预算追加',
              '预算查询',
            ],
          },
          {
            title: '费用申请',
            content: [
              '差旅费申请',
              '营销费用申情',
              '业务招待费申请',
              '人力专项费申请',
              '行政专项费申请',
              '借款申请',
            ],
          },
          {
            title: '费用报销',
            content: [
              '差旅费报销',
              '通讯费报销',
              '营销费报销',
              '其他费用报销',
              '业务招待费报销',
              '行政专项费报销',
              '人力专项费报销',
            ],
          },
          {
            title: '财务作业',
            content: [
              '单据审核',
              '出纳付款',
              '财务制证',
              '凭证审核',
              '财务调账',
              '票据管理',
            ],
          },
          {
            title: '费用统计',
            content: ['部门费用表', '科目费用表', '专项费用表', '预算执行表'],
          },
        ],
        icons: [
          { url: icon1, title: '我的未报费用' },
          { url: icon2, title: '我的费用申请' },
          { url: icon3, title: '我的借款' },
          { url: icon4, title: '我的还款' },
          { url: icon5, title: '我的报销' },
          { url: icon6, title: '我的付款申请' },
          { url: icon7, title: '我的回款' },
          { url: icon8, title: '我的发票明细' },
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
  .content111 {
    display: flex;
    justify-content: space-around;
    flex-direction: row;
    // display: inline-block;
    .icon_contain {
      display: inline-block;
      width: 12.5%;
      padding: 10px 20px;
      text-align: center;
      &:hover {
        cursor: pointer;
        background: #f2f2f2;
      }
      img {
        display: inline-block;
      }
    }
    .magTop {
      margin-top: 30px;
    }
  }
</style>
