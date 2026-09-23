<template>
  <div class="marketing-container">
    <section class="first">
      <div class="my-tabs">
        <el-radio-group v-model="tabkey" style="margin-bottom: 30px">
          <el-radio-button label="1">入门应知</el-radio-button>
          <el-radio-button label="2">商机拓展</el-radio-button>
          <el-radio-button label="3">立项跟进</el-radio-button>
          <el-radio-button label="4">招投标管理</el-radio-button>
          <el-radio-button label="5">合同谈判&客户服务</el-radio-button>
        </el-radio-group>
        <div class="tab-content" id="tab-content">
          <div class="tab-item tab-one" v-if="tabkey === '1'">
            <div
              class="tab-item-card"
              v-for="item1 in cardListOne"
              :key="item1.title"
            >
              <div class="card-title">{{ item1.title }}</div>
              <ul class="card-list">
                <li v-for="ui in item1.ull" :key="ui.name">{{ ui.name }}</li>
              </ul>
              <img :src="item1.gimg" alt="" class="btm-img" />
            </div>
          </div>
          <div class="tab-item tab-two" v-if="tabkey === '2'">
            <div
              class="tab-item-card"
              v-for="item1 in cardListTwo"
              :key="item1.title"
            >
              <div class="card-title">{{ item1.title }}</div>
              <ul class="card-list">
                <li v-for="ui in item1.ull" :key="ui.name">{{ ui.name }}</li>
              </ul>
              <img :src="item1.gimg" alt="" class="btm-img" />
            </div>
          </div>
          <div class="tab-item tab-three" v-if="tabkey === '3'">
            <div
              class="tab-item-card"
              v-for="item1 in cardListThree"
              :key="item1.title"
            >
              <div class="card-title">{{ item1.title }}</div>
              <ul class="card-list">
                <li v-for="ui in item1.ull" :key="ui.name">{{ ui.name }}</li>
              </ul>
              <img :src="item1.gimg" alt="" class="btm-img" />
            </div>
          </div>
          <div class="tab-item tab-four" v-if="tabkey === '4'">
            <div
              class="tab-item-card"
              v-for="item1 in cardListFour"
              :key="item1.title"
            >
              <div class="card-title">{{ item1.title }}</div>
              <ul class="card-list">
                <li v-for="ui in item1.ull" :key="ui.name">{{ ui.name }}</li>
              </ul>
              <img :src="item1.gimg" alt="" class="btm-img" />
            </div>
          </div>
          <div class="tab-item tab-five" v-if="tabkey === '5'">
            <div
              class="tab-item-card"
              v-for="item1 in cardListFive"
              :key="item1.title"
            >
              <div class="card-title">{{ item1.title }}</div>
              <ul class="card-list">
                <li v-for="ui in item1.ull" :key="ui.name">{{ ui.name }}</li>
              </ul>
              <img :src="item1.gimg" alt="" class="btm-img" />
            </div>
          </div>
        </div>
      </div>
    </section>
    <section class="second">
      <div class="block-item">
        <div class="left"><img :src="this.img1" alt="" /></div>
        <div class="right">
          <div class="title">解决方案课件</div>
          <div class="english">THE SOLUTION</div>
        </div>
      </div>
      <div class="block-item">
        <div class="left"><img :src="this.img2" alt="" /></div>
        <div class="right">
          <div class="title">营销技巧课件</div>
          <div class="english">SALES SKILLS</div>
        </div>
      </div>
      <div class="block-item">
        <div class="left"><img :src="this.img3" alt="" /></div>
        <div class="right">
          <div class="title">客户案例</div>
          <div class="english">CUSTOMER CASE</div>
        </div>
      </div>
      <div class="block-item">
        <div class="left"><img :src="this.img4" alt="" /></div>
        <div class="right">
          <div class="title">演示方案</div>
          <div class="english">DEMO ORESENTATION</div>
        </div>
      </div>
      <div class="block-item">
        <div class="left"><img :src="this.img5" alt="" /></div>
        <div class="right">
          <div class="title">战区分享</div>
          <div class="english">THE TEAM SHOWED</div>
        </div>
      </div>
    </section>
    <section class="third">
      <div class="third-block">
        <div class="title">
          <a href="#">行业讯息</a>
          <div class="btn-g">
            <el-button size="mini">新建</el-button>
            <el-button size="mini">更多</el-button>
          </div>
        </div>
        <div class="list">
          <el-table :data="tableData1" style="width: 100%" :show-header="false">
            <el-table-column prop="name" label="title"></el-table-column>
            <el-table-column
              prop="person"
              label="person"
              width="80"
            ></el-table-column>
            <el-table-column
              prop="date"
              label="date"
              width="100"
            ></el-table-column>
          </el-table>
        </div>
      </div>

      <div class="third-block">
        <div class="title">
          <a href="#">友商对比</a>
          <div class="btn-g">
            <el-button size="mini">新建</el-button>
            <el-button size="mini">更多</el-button>
          </div>
        </div>
        <div class="list">
          <el-table :data="tableData2" style="width: 100%" :show-header="false">
            <el-table-column prop="name" label="title"></el-table-column>
            <el-table-column
              prop="person"
              label="person"
              width="80"
            ></el-table-column>
            <el-table-column
              prop="date"
              label="date"
              width="100"
            ></el-table-column>
          </el-table>
        </div>
      </div>

      <div class="third-block">
        <div class="title">
          <a href="#">解决方案 售前实战</a>
          <div class="btn-g">
            <el-button size="mini">新建</el-button>
            <el-button size="mini">更多</el-button>
          </div>
        </div>
        <div class="list">
          <el-table :data="tableData3" style="width: 100%" :show-header="false">
            <el-table-column prop="name" label="title"></el-table-column>
            <el-table-column
              prop="person"
              label="person"
              width="80"
            ></el-table-column>
            <el-table-column
              prop="date"
              label="date"
              width="100"
            ></el-table-column>
          </el-table>
        </div>
      </div>

      <div class="third-block">
        <div class="title">
          <a href="#">投标资质 投标文件模板 投标常用资料 实战投标资料</a>
          <div class="btn-g">
            <el-button size="mini">新建</el-button>
            <el-button size="mini">更多</el-button>
          </div>
        </div>
        <div class="list">
          <el-table :data="tableData4" style="width: 100%" :show-header="false">
            <el-table-column prop="name" label="title"></el-table-column>
            <el-table-column
              prop="person"
              label="person"
              width="80"
            ></el-table-column>
            <el-table-column
              prop="date"
              label="date"
              width="100"
            ></el-table-column>
          </el-table>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
  const img1 = require('./../../../assets/indexPage/marketing/01@2x.png')
  const img2 = require('./../../../assets/indexPage/marketing/04@2x.png')
  const img3 = require('./../../../assets/indexPage/marketing/03@2x.png')
  const img4 = require('./../../../assets/indexPage/marketing/02@2x.png')
  const img5 = require('./../../../assets/indexPage/marketing/05@2x.png')
  const groupImg1 = require('./../../../assets/indexPage/marketing/Group10@2x.png')
  const groupImg2 = require('./../../../assets/indexPage/marketing/Group11@2x.png')
  const groupImg3 = require('./../../../assets/indexPage/marketing/Group13@2x.png')
  const groupImg4 = require('./../../../assets/indexPage/marketing/Group14@2x.png')
  export default {
    name: 'marketing-container',
    data() {
      return {
        tabkey: '1',
        img1: img1,
        img2: img2,
        img3: img3,
        img4: img4,
        img5: img5,
        groupImg1: groupImg1,
        groupImg2: groupImg2,
        groupImg3: groupImg3,
        groupImg4: groupImg4,
        cardListOne: [
          {
            title: '产品知识',
            ull: [
              { name: '产品操作手册指南' },
              { name: '产品白皮书' },
              { name: '新功能介绍' },
              { name: '新旧功能对比' },
              { name: 'SOW说明书' },
            ],
            gimg: groupImg1,
          },
          {
            title: '营销技巧',
            ull: [
              { name: '营销策略' },
              { name: '商机挖掘话术' },
              { name: '营销标准话术' },
              { name: '顾问式销售' },
              { name: '售前能力培养' },
              { name: '方案演讲技巧' },
            ],
            gimg: groupImg2,
          },
          {
            title: '营销规范',
            ull: [
              { name: '规范管理制度' },
              { name: '产能管理制度' },
              { name: '售前管理制度' },
              { name: '标书注意事项' },
              { name: '投标操作指引' },
              { name: '投标管理规范' },
              { name: '合同管理制度' },
              { name: '合同签订指引' },
            ],
            gimg: groupImg3,
          },
          {
            title: '模板范本',
            ull: [
              { name: 'PPT设计规范' },
              { name: '第一封邮件' },
              { name: '第二封邮件' },
              { name: '名片模板' },
              { name: '电脑背景' },
            ],
            gimg: groupImg4,
          },
        ],
        cardListTwo: [
          {
            title: '发给客户资料',
            ull: [
              { name: '公司介绍PPT' },
              { name: '公司介绍视频' },
              { name: '产品体验' },
              { name: '宣传彩页' },
              { name: '标准模块介绍' },
            ],
            gimg: groupImg1,
          },
          {
            title: '初次交流方案',
            ull: [
              { name: '产品简版' },
              { name: '产品全量版' },
              { name: '产品初次交流' },
              { name: '产品战略分析' },
              { name: '合同管理' },
              { name: '英文版方案' },
              { name: '首次拜访' },
              { name: '采购' },
            ],
            gimg: groupImg2,
          },
          {
            title: '营销规范',
            ull: [
              { name: '规范管理制度' },
              { name: '产能管理制度' },
              { name: '售前管理制度' },
              { name: '标书注意事项' },
              { name: '投标操作指引' },
              { name: '投标管理规范' },
              { name: '合同管理制度' },
              { name: '合同签订指引' },
            ],
            gimg: groupImg3,
          },
          {
            title: '客户案例',
            ull: [{ name: '客户LOGO集' }, { name: '客户案例' }],
            gimg: groupImg4,
          },
          {
            title: '后台工具操作',
            ull: [{ name: '商机录入' }, { name: '拜访记录' }],
            gimg: groupImg3,
          },
        ],
        cardListThree: [
          {
            title: '立项&竞分',
            ull: [
              { name: '立项报告' },
              { name: '竞商对比报告' },
              { name: '竞品功能分析' },
              { name: '竞品用户分析' },
              { name: '竞品战略层分析' },
              { name: '竞品范围层分析' },
            ],
            gimg: groupImg1,
          },
          {
            title: '深度交流方案',
            ull: [
              { name: '产品简版' },
              { name: '深度交流版' },
              { name: '产品全量版' },
              { name: '产品初次交流' },
              { name: '产品战略分析' },
              { name: '合同管理' },
              { name: '英文版方案' },
              { name: '流程指数' },
            ],
            gimg: groupImg3,
          },
          {
            title: '专项方案',
            ull: [
              { name: '营销策略' },
              { name: '商机挖掘话术' },
              { name: '营销标准话术' },
              { name: '顾问式销售' },
              { name: '售前能力培养' },
              { name: '方案演讲技巧' },
            ],
            gimg: groupImg2,
          },
          {
            title: '营销规范',
            ull: [
              { name: '费控管理' },
              { name: '电子发票' },
              { name: '合同管理' },
              { name: '电子签章' },
              { name: '政务办公' },
              { name: '勘察设计' },
              { name: '智慧党建' },
              { name: '财务共享' },
              { name: '项目管理' },
              { name: '投资管理' },
              { name: 'BMP业务管理' },
            ],
            gimg: groupImg3,
          },
          {
            title: '产品演示',
            ull: [{ name: '演示话术' }, { name: '演示视频' }],
            gimg: groupImg4,
          },
          {
            title: 'BMP操作',
            ull: [
              { name: '销售立项' },
              { name: '拜访记录' },
              { name: '派工申请' },
              { name: '营销在线' },
            ],
            gimg: groupImg4,
          },
        ],
        cardListFour: [
          {
            title: '投标准备',
            ull: [
              { name: '报价' },
              { name: '招标需求文档' },
              { name: '评分标准' },
              { name: '投标常用资料' },
              { name: '客户感谢信' },
              { name: '中标通知书汇总' },
              { name: '实战投标文件' },
            ],
            gimg: groupImg1,
          },
          {
            title: '投标支持',
            ull: [
              { name: '平台投标方案' },
              { name: '销售投标方案' },
              { name: '合同投标方案' },
              { name: '费控投标方案' },
              { name: '人力投标方案' },
              { name: '流程指数投标方案' },
            ],
            gimg: groupImg2,
          },
          {
            title: '投标汇报',
            ull: [{ name: '投标汇报PPT' }, { name: '投标演示要点' }],
            gimg: groupImg3,
          },
          {
            title: 'BMP操作',
            ull: [
              { name: '投标登记' },
              { name: '商务与技术标书' },
              { name: '主营业务报价' },
              { name: '制造商授权书' },
              { name: '本部保证金/标书' },
              { name: '伙伴保证金/标书' },
              { name: '资质原件借阅' },
              { name: '伙伴保证金退款' },
            ],
            gimg: groupImg4,
          },
        ],
        cardListFive: [
          {
            title: '合同谈判',
            ull: [
              { name: '商务合同模板' },
              { name: '选型结果报告' },
              { name: '归档合同查询' },
              { name: 'BMP操作.合同预审' },
              { name: 'BMP操作.合同申请' },
            ],
            gimg: groupImg1,
          },
          {
            title: '客户服务方法论',
            ull: [
              { name: '如何推进服务续约' },
              { name: '更新客户信息' },
              { name: '定期上门拜访' },
              { name: '邀请参加活动' },
              { name: '推动购买代维服务' },
              { name: '推动升级服务' },
              { name: '重视满意度' },
              { name: '及时处理投诉' },
              { name: '邀请老客户培训' },
            ],
            gimg: groupImg2,
          },
          {
            title: '客户服务流程模板',
            ull: [
              { name: '客户续约流程' },
              { name: '服务延期流程' },
              { name: '客户服务报告' },
            ],
            gimg: groupImg3,
          },
        ],
        tableData1: [
          {
            name: '2.1用友 “三剑齐发” ，瞬间带你进入数智时代',
            person: '张孝昆',
            date: '2023-01-17',
          },
          {
            name: '智邦国际企业一体化管理软件：多维一体化，全员协同化',
            person: '张孝昆',
            date: '2023-01-17',
          },
          {
            name: '扩展针对Kubernetes 和云环境的勒索软件保护',
            person: '张孝昆',
            date: '2023-01-17',
          },
          {
            name: '瑞星分享卫生医疗行业解决方案',
            person: '张孝昆',
            date: '2023-01-17',
          },
          {
            name: '从数据湖发展为企业数据平台（EDP）：商业案例从未如此令人信服',
            person: '张孝昆',
            date: '2023-01-17',
          },
          {
            name: '左手AI右手RPA，软通动力助力企业财务降本增效',
            person: '张孝昆',
            date: '2023-01-17',
          },
        ],
        tableData2: [
          {
            name: '软件行业深度对比分析报告 ',
            person: '张孝昆',
            date: '2023-01-17',
          },
          {
            name: '软件外包公司对比：海辉、软通、东软集团等',
            person: '张孝昆',
            date: '2023-01-17',
          },
          {
            name: '公司软件优势',
            person: '张孝昆',
            date: '2023-01-17',
          },
          {
            name: '不同规模软件开发公司对比',
            person: '张孝昆',
            date: '2023-01-17',
          },
          {
            name: '传统软件产品与互联网产品的区别',
            person: '张孝昆',
            date: '2023-01-17',
          },
          {
            name: '极致物业管理软件与同行软件的对比分析',
            person: '张孝昆',
            date: '2023-01-17',
          },
        ],
        tableData3: [
          {
            name: '阿里商旅企业差旅整体解决方案',
            person: '张孝昆',
            date: '2023-01-17',
          },
          {
            name: '智能派工软件案例',
            person: '张孝昆',
            date: '2023-01-17',
          },
          {
            name: '企业流程管理案例',
            person: '张孝昆',
            date: '2023-01-17',
          },
          {
            name: '厂内智能物流系统案例',
            person: '张孝昆',
            date: '2023-01-17',
          },
          {
            name: '智能车间案例',
            person: '张孝昆',
            date: '2023-01-17',
          },
          {
            name: '烘焙行业案例',
            person: '张孝昆',
            date: '2023-01-17',
          },
        ],
        tableData4: [
          {
            name: '国家高新技术企业证书',
            person: '张孝昆',
            date: '2023-01-17',
          },
          {
            name: '守合同重信用企业',
            person: '张孝昆',
            date: '2023-01-17',
          },
          {
            name: 'AAA企业信用认证',
            person: '张孝昆',
            date: '2023-01-17',
          },
          {
            name: '售后服务认证',
            person: '张孝昆',
            date: '2023-01-17',
          },
          {
            name: 'ISO9001',
            person: '张孝昆',
            date: '2023-01-17',
          },
          {
            name: '北京天信企业信用等级投标资质评价体系',
            person: '张孝昆',
            date: '2023-01-17',
          },
        ],
      }
    },
  }
</script>

<style scoped>
  .marketing-container {
    /* background-color: rgba(0, 0, 0, 0.1); */
    background: #f6f8f9 !important;
    padding: 0 !important;
  }

  ul > li {
    list-style: none;
  }

  .first {
    background: url(./../../../assets/indexPage/marketing/mapnav-bg.png);
    background-repeat: no-repeat;
    /* background-size: cover; */
    background-position: top center;
    background-color: #ecf3ff;
    margin-right: auto;
    margin-left: auto;
    /* min-width: 900px; */
    max-height: 100%;
    height: 500px;
    background-color: #fff;
    overflow: hidden;
    position: relative;
  }

  .my-tabs {
    text-align: center;
    padding-top: 20px;
  }

  .tab-item {
    display: flex;
    justify-content: center;
  }

  .tab-item-card {
    background-color: #fff;
    width: 168px;
    margin: 0 20px;
    /* padding: 20px; */
    border-radius: 10px;
    height: fit-content;
    position: relative;
    box-shadow: 4px 4px 5px 2px #d4e0fe;
  }

  .tab-item-card .btm-img {
    width: 200px;
    position: absolute;
    left: 0;
    bottom: -150px;
  }

  .card-title {
    padding: 12px 0 3px 0;
    line-height: 24px;
    color: #4285f4;
    font-weight: bold;
    font-size: 16px;
    text-align: center;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .card-list {
    padding-left: 0;
    margin-bottom: 20px;
  }

  .card-list > li {
    padding-left: 10px;
    position: relative;
    width: auto;
    margin: 5px 10px 5px 20px;
    white-space: normal;
    text-align: left;
  }

  .second {
    display: flex;
    justify-content: space-between;
    margin: 20px 0;
  }

  .block-item {
    background-color: #fff;
    width: 19.5%;
    height: 100px;
    display: flex;
    align-items: center;
    padding: 0 15px;
    box-shadow: 0 2px 8px -2px rgb(0 0 0 / 13%);
    border-radius: 4px;
    cursor: pointer;
  }

  .block-item .left img {
    height: 48px;
    width: 48px;
    margin-right: 10px;
  }

  .block-item .right {
    display: flex;
    align-items: flex-start;
    flex-direction: column;
    flex: 1;
  }

  .block-item .title {
    font-size: 18px;
    color: #333;
    white-space: nowrap;
    text-overflow: ellipsis;
    -o-text-overflow: ellipsis;
    overflow: hidden;
  }

  .block-item .english {
    color: #999;
    font-size: 12px;
    line-height: 1.8;
    white-space: nowrap;
    text-overflow: ellipsis;
    -o-text-overflow: ellipsis;
    overflow: hidden;
  }

  .third {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
    margin-bottom: 20px;
  }

  .third-block {
    width: 49.5%;
  }

  .third-block .title {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 12px;
    margin-bottom: 12px;
  }

  .third-block .title a {
    font-size: 16px;
  }
</style>
