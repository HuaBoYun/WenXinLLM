<template>
  <div>
    <el-card>
      <div class="content">
        <span class="icon_contain1" v-for="item in icons1" :key="item + 1">
          <img :src="item.url" alt="" />
          <div>
            <div class="text1">{{ item.title1 }}</div>
            <div class="text2">{{ item.title2 }}</div>
          </div>
        </span>
      </div>
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
      <div class="content">
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
            <card-title
              first="制度规范"
              second="服务公告"
              third="服务问答"
              class="card"
            />
          </div>
        </el-col>
        <el-col :lg="8">
          <person class="card" first="专家" />
        </el-col>
      </el-row>
    </el-card>
    <el-card>
      <el-row :gutter="20">
        <el-col :lg="24" class="magTop">
          <div class="title">
            <card2
              first="文化生活"
              second="俱乐部"
              third="爱关怀"
              class="card"
            />
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
  import icon11 from '../../../assets/selfHelp/icon1.png'
  import icon22 from '../../../assets/selfHelp/icon2.png'
  import icon33 from '../../../assets/selfHelp/icon3.png'
  import icon44 from '../../../assets/selfHelp/icon4.png'
  import icon55 from '../../../assets/selfHelp/icon5.png'
  import icon1 from '../../../assets/personManage/p1.png'
  import icon2 from '../../../assets/personManage/p2.png'
  import icon3 from '../../../assets/personManage/p3.png'
  import icon4 from '../../../assets/personManage/p4.png'
  import icon5 from '../../../assets/personManage/p5.png'
  import icon6 from '../../../assets/personManage/p6.png'
  import icon7 from '../../../assets/personManage/p7.png'
  import CardTitle from './components/CardTitle'
  import card2 from './components/card2'
  import person from './components/person'
  export default {
    components: { CardTitle, person, card2 },
    data() {
      return {
        cardList1: [
          {
            title: '人员招聘',
            content: [
              '人力需求申请',
              '校园招聘需求申请',
              '人才引进指标申请',
              '人才推荐申请',
              '退休返聘申请',
              '面试测评流程',
            ],
          },
          {
            title: '新人入职',
            content: [
              '新员工入职通知',
              '试用期考核评分',
              '员工转正薪酬沟通',
              '代职期薪酬沟通',
              '过渡期结束申请',
            ],
          },
          {
            title: '人员异动',
            content: [
              '员工异动申请',
              '岗位调整流程',
              '离职申请流程',
              '离职人员工作交接',
              '实习生离职流程',
              '合同续签流程',
            ],
          },
          {
            title: '薪酬福利',
            content: [
              '员工调薪沟通',
              '通讯津贴申请',
              '优秀员工申报',
              '奖惩申请流程',
              '外训申请流程',
              '档案借阅申请',
            ],
          },
          {
            title: '请假加班',
            content: [
              '请休假申请',
              '年假延期申请',
              '销假申请',
              '加班申请',
              '补卡流程',
            ],
          },
        ],
        icons: [
          { url: icon1, title: '招聘管理' },
          { url: icon2, title: '人事流程' },
          { url: icon3, title: '员工档案' },
          { url: icon4, title: '考试管理' },
          { url: icon5, title: '考勤管理' },
          { url: icon6, title: '绩效管理' },
          { url: icon7, title: '薪酬管理' },
        ],
        icons1: [
          { url: icon11, title1: '83', title2: '发起的沟通' },
          { url: icon22, title1: '245', title2: '参与的会议' },
          { url: icon33, title1: '400', title2: '发起的流程' },
          { url: icon44, title1: '287', title2: '负责的任务' },
          { url: icon55, title1: '93', title2: '我的假期管理' },
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
  .content {
    display: flex;
    justify-content: space-around;
    flex-direction: row;
    // display: inline-block;
    .icon_contain {
      display: inline-block;
      width: 12.5%;
      padding: 30px 20px;
      text-align: center;
      &:hover {
        cursor: pointer;
        background: #f2f2f2;
      }
      img {
        display: inline-block;
        width: 30%;
      }
    }
    .magTop {
      margin-top: 30px;
    }
  }
  .icon_contain1 {
    display: flex;
    flex-direction: row;
    justify-content: space-around;
    align-items: center;
    padding: 20px 10px;
    width: 20%;
    &:hover {
      cursor: pointer;
      background: #f2f2f2;
    }
    img {
      /* width: 50%; */
    }
    .text1 {
      color: #4285f4;
      font-weight: bold;
      font-size: 24px;
    }
    .text2 {
      color: #666666;
    }
  }
</style>
