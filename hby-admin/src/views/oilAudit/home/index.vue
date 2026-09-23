<template>
  <div id="app" class="page">
    <!-- <div class="title">当前实施项目：{{ title }}</div> -->

    <el-row :gutter="10">
      <el-col :lg="24" :md="24" :sm="24">
        <!-- 切换模块默认页不挂载待办中心，避免一次性触发多条流程列表接口；进入消息/待办中心时再加载。 -->
        <!-- <DBXX></DBXX> -->
      </el-col>
      <!-- <el-col :lg="12" :md="12" :sm="24">
        <div class="table-responsive">
          <h5>我的工作</h5>
          <el-tabs type="border-card" v-model="activeName1">
            <el-tab-pane label="待办" name="first" style="overflow-y: scroll">
              <dbTable></dbTable>
            </el-tab-pane>
            <el-tab-pane label="已办" name="second" style="overflow-y: scroll">
              <ybTable></ybTable>
            </el-tab-pane>
            <el-tab-pane label="发起" name="three" style="overflow-y: scroll">
              <fqTable></fqTable>
            </el-tab-pane>
            <el-tab-pane label="抄送" name="four" style="overflow-y: scroll">
              <csTable></csTable>
            </el-tab-pane>
          </el-tabs>
        </div>
      </el-col> -->

      <!-- <el-col :lg="12" :md="12" :sm="24">
        <div class="table-responsive">
          <h5>汇总管理</h5>
          <el-tabs type="border-card" v-model="activeName2">
            <el-tab-pane
              label="工作小结"
              name="first"
              style="overflow-y: scroll"
            >
              <GZXJTable></GZXJTable>
            </el-tab-pane>
            <el-tab-pane
              label="疑点管理"
              name="second"
              style="overflow-y: scroll"
            >
              <YDGLTable></YDGLTable>
            </el-tab-pane>

            <el-tab-pane
              label="缺陷管理"
              name="four"
              style="overflow-y: scroll"
            >
              <QXGLTable></QXGLTable>
            </el-tab-pane>
            <el-tab-pane
              label="审计发现"
              name="five"
              style="overflow-y: scroll"
            >
              <SJFXTable></SJFXTable>
            </el-tab-pane>
            <el-tab-pane label="项目资料" name="six" style="overflow-y: scroll">
              <XMZLTable></XMZLTable>
            </el-tab-pane>
          </el-tabs>
        </div>
      </el-col>
      <el-col :lg="12" :md="12" :sm="24">
        <div class="table-responsive">
          <h5>审计模型</h5>
          <el-tabs type="border-card" v-model="activeName3">
            <el-tab-pane label="预计结果" name="first">
              <SJMXTable></SJMXTable>
            </el-tab-pane>
          </el-tabs>
        </div>
      </el-col>
      <el-col :lg="12" :md="12" :sm="24">
        <div class="table-responsive">
          <h5>资料查询</h5>
          <el-tabs type="border-card" v-model="activeName4">
            <el-tab-pane
              label="档案借阅"
              name="first"
              style="overflow-y: scroll"
            >
              <DAJYTable></DAJYTable>
            </el-tab-pane>
            <el-tab-pane
              label="审计经验库"
              name="third"
              style="overflow-y: scroll"
            >
              <SJJYKTable></SJJYKTable>
            </el-tab-pane>

            <el-tab-pane label="管理制度" name="five">
              <ZDGLTable></ZDGLTable>
            </el-tab-pane>
          </el-tabs>
        </div>
      </el-col> -->

      <el-col :lg="24" :md="24" :sm="24">
        <div class="table-responsive">
          <h5>功能导航</h5>
          <Gndh :isXmpy="true" />
        </div>
      </el-col>

      <el-col :lg="24" :md="24" :sm="24">
        <div class="table-responsive">
          <h5>消息通知</h5>
          <!-- 默认首页不加载通知流程列表，切到消息中心后再调用。 -->
          <!-- <Notice :isXmpy="true" :isHytz="true" /> -->
          <!-- <el-tabs type="border-card" v-model="activeName2">
            <el-tab-pane
              label="项目评优"
              name="first"
              style="overflow-y: scroll"
            >
              <XMPYTable></XMPYTable>
            </el-tab-pane>
          </el-tabs> -->
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  // import { getCurrentProjectData } from '@/oapi/audit/home'
  import { getCurrSsProject } from '@/oapi/audit/project'

  import GZXJTable from './components/hzgl/gzxj.vue'
  import QXGLTable from './components/hzgl/qxgl.vue'
  import SJFXTable from './components/hzgl/sjfx.vue'
  import YDGLTable from './components/hzgl/ydgl.vue'
  import YJJGTable from './components/sjyj/yjjg.vue'
  import dbTable from './components/wdgz/db.vue'
  import ybTable from './components/wdgz/yb.vue'
  import fqTable from './components/wdgz/fq.vue'
  import csTable from './components/wdgz/cs.vue'
  import DAJYTable from './components/zlcx/dajy.vue'
  import SJJYKTable from './components/zlcx/sjjyk.vue'
  import XMZLTable from './components/zlcx/xmzl.vue'
  import ZDGLTable from './components/zlcx/zdgl.vue'
  import SJMXTable from './components/sjmx/index.vue'
  import XMPYTable from './components/xmpy/index.vue'
  // import DBXX from './dbxx.vue'

  // import Notice from '@/views/homes/personal/xiafa-new.vue'
  import Gndh from '@/views/oilAudit/home/gndh.vue'
  export default {
    name: 'Download',
    components: {
      GZXJTable,
      YDGLTable,
      QXGLTable,
      SJFXTable,
      DAJYTable,
      SJJYKTable,
      XMZLTable,
      YJJGTable,
      dbTable,
      ybTable,
      fqTable,
      csTable,
      ZDGLTable,
      SJMXTable,
      XMPYTable,
      // DBXX,
      // Notice,
      Gndh,
    },
    data() {
      return {
        pageSizes: [5, 10, 15, 20, 50, 100],
        layout: 'total, prev, pager, next, jumper',
        tableData: [
          {
            date: 'XXXX',
            name: 'XXXX',
            address: 'XXXX',
          },
          {
            date: 'XXXX',
            name: 'XXXX',
            address: 'XXXX',
          },
          {
            date: 'XXXX',
            name: 'XXXX',
            address: 'XXXX',
          },
          {
            date: 'XXXX',
            name: 'XXXX',
            address: 'XXXX',
          },
        ],
        title: '',

        tableData3: [
          {
            number: 'CJTZ01',
            content: '企业增加注册资本或者资本公积时是否计提印花税',
          },
          {
            number: 'RULE-01',
            content: '发运数量不能低于50',
          },
        ], //预计结果
        activeName1: 'first',
        activeName2: 'first',
        activeName3: 'first',
        activeName4: 'first',
        activeYear: 2021,
        years: [2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019, 2020, 2021],
      }
    },

    created() {
      // this.getDefaultInfo()
    },
    methods: {
      changeActiveYear: function (year) {
        this.activeYear = year
      },

      filterHandler(value, row, column) {
        const property = column['date']
        return row[property] === value
      },
      getDefaultInfo() {
        getCurrSsProject().then((res) => {
          this.title = res.data.pj.projectName
        })
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
    },
  }
</script>
<style scoped>
  h5 {
    font-size: 18px;
    margin: 2px;
    color: #333;
  }
  .el-col > div {
    border: 1px solid #dcdfe5;
    margin-bottom: 10px;
  }

  .page {
    padding: 20px;
  }

  .table-title {
    cursor: pointer;
  }

  .table th {
    position: relative;
  }

  .table-filter {
    position: absolute;
    border: 1px solid gainsboro;
    padding: 5px;
    left: 0;
    right: 0;
    top: 40px;
    background: white;
    min-width: 160px;
  }

  .table-filter input {
    padding: 5px;
    font-size: 14px;
    margin-right: 5px;
  }

  .table-filter .form-check {
    display: flex;
    flex-direction: column;
    text-align: left;
    font-size: 14px;
    font-weight: 400;
    padding: 5px;
  }

  .table-filter button {
    font-size: 12px;
    padding: 2px 10px;
  }

  .table-responsive {
    background: white;
    padding: 20px;
    margin-bottom: 20px;
  }

  .chats > div > div {
    background: white;
    padding: 10px;
    margin-bottom: 20px;
  }

  .select-year {
    display: flex;
    background: aliceblue;
    padding: 5px;
  }

  .select-year > div {
    margin-right: 10px;
    padding: 2px 5px;
    cursor: pointer;
  }

  .select-year .active {
    background: #ffaf0f;
    border-radius: 20px;
    color: white;
  }
  h5 {
    margin: 0 0 10px 0;
    font-size: 17px;
  }
  .title {
    font-size: 24px;
    padding: 10px;
    border: 1px solid #dcdfe5;
    margin-bottom: 30px;
  }
  .el-tab-pane {
    height: 300px;
  }
</style>
