<template>
  <!--  <div id="abc" class="system-log-container"></div>-->
  <div id="app" class="page">
    <Xiafa></Xiafa>
    <!-- <Table></Table> -->
    <div style="margin-bottom: 20px">
      <el-form ref="form" :inline="true" label-width="0" :model="queryForm">
        <el-form-item>
          <el-select v-model="queryForm.year" placeholder="请选择">
            <el-option
              v-for="item in yearInquiryData"
              :key="item"
              :label="item"
              :value="item"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button icon="el-icon-search" type="primary" @click="fetchData">
            查询
          </el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table
        ref="filterTable"
        border
        :data="projectSituationTable"
        style="width: 100%"
        v-loading="listLoading"
      >
        <el-table-column label="序号" type="index" width="50" />
        <el-table-column label="被审计单位" prop="auditOrgName" />
        <el-table-column label="项目名称" prop="prjoectName" />
        <el-table-column label="计划年度" prop="planYear" />
        <el-table-column label="审计类型" prop="auditType" />
        <el-table-column label="项目经理" prop="realname" />
        <el-table-column label="项目状态" prop="examineTypes" />
        <el-table-column label="项目来源" prop="projectSource" />
        <el-table-column label="项目费用估算" prop="costs" />
      </el-table>
      <el-pagination
        background
        :current-page="queryForm.pageNumber"
        :layout="layout"
        :page-size="queryForm.pageSize"
        :total="total"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
        :page-sizes="[5, 10]"
      />
    </div>
    <el-row :gutter="10">
      <!-- <el-col :lg="24" :md="8" :sm="24">
        <Table></Table>
      </el-col> -->
      <el-col :lg="12" :md="12" :sm="24">
        <div id="chats-1" style="width: 100%; height: 300px"></div>
      </el-col>
      <el-col :lg="12" :md="12" :sm="24">
        <div id="chats-2" style="width: 100%; height: 300px"></div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import * as echarts from 'echarts'
  import {
    entryNumber,
    projectType,
    projectSituation,
    yearInquiry,
  } from '@/oapi/audit/sjfx'
  import Table from './huiyiTable.vue'
  import Xiafa from './xiafa.vue'

  export default {
    name: 'Download',
    components: { Table, Xiafa },
    data() {
      return {
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
        queryForm: {
          year: '',
          pageNumber: 1,
          pageSize: 5,
        },
        total: 0,
        layout: 'total, sizes, prev, pager, next, jumper',
        activeYear: 2021,
        yearInquiryData: [],
        entryNumberData: [],
        projectTypeData: [],
        projectSituationTable: [],
        listLoading: false,
      }
    },
    mounted() {},
    created() {
      this.getYearInquiry()
      this.getEntryNumber()
      this.getProjectType()
      this.getProjectSituation()
    },
    methods: {
      changeActiveYear: function (year) {
        this.activeYear = year
      },
      filterHandler(value, row, column) {
        const property = column['date']
        return row[property] === value
      },
      getYearInquiry() {
        yearInquiry().then((res) => {
          this.yearInquiryData = res.data.list
        })
      },
      getProjectSituation() {
        this.listLoading = true
        projectSituation(this.queryForm).then((res) => {
          this.projectSituationTable = res.data.pageInfo.tlist
          this.total = res.data.pageInfo.totalRecord
          this.listLoading = false
        })
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getProjectSituation()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getProjectSituation()
      },
      getEntryNumber() {
        entryNumber(this.queryForm).then((res) => {
          this.entryNumberData = res.data.data
          this.chats1()
        })
      },
      chats1() {
        const chats1 = echarts.init(document.getElementById('chats-1'))
        // 指定图表的配置项和数据
        chats1.setOption({
          title: {
            text: '各公司审计项目数',
            left: 'left',
          },
          tooltip: {
            trigger: 'item',
          },
          series: [
            {
              name: '项目数量',
              type: 'pie',
              top: '15%',
              radius: ['30%', '60%'],
              data: this.entryNumberData,
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)',
                },
              },
            },
          ],
        })
      },
      getProjectType() {
        projectType(this.queryForm).then((res) => {
          this.projectTypeData = res.data.data
          this.chats2()
        })
      },
      chats2() {
        const chats2 = echarts.init(document.getElementById('chats-2'))

        // 指定图表的配置项和数据
        chats2.setOption({
          title: {
            text: '审计项目类型',
            left: 'left',
          },
          tooltip: {
            trigger: 'item',
          },
          series: [
            {
              name: '项目数量',
              type: 'pie',
              top: '15%',
              radius: ['30%', '60%'],
              data: this.projectTypeData,
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)',
                },
              },
            },
          ],
        })
      },
      fetchData() {
        this.getEntryNumber()
        this.getProjectType()
        this.getProjectSituation()
      },
      resetSearch() {
        this.queryForm.year = ''
        this.getEntryNumber()
        this.getProjectType()
        this.getProjectSituation()
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
</style>
