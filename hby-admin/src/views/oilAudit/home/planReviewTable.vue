<template>
  <!-- 工程项目造价中间表 -->
  <div class="system-log-container">
    <vab-query-form class="margin-b0">
      <el-card shadow="never">
        <vab-query-form-top-panel>
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item
              :prop="item.key"
              v-for="(item, index) in searchItem"
              :key="index"
            >
              <el-input
                v-model="queryForm.projectName"
                clearable
                placeholder="项目名称"
                v-if="item.name === '项目名称'"
              />
              <el-input
                v-model="queryForm.projectOrderName"
                clearable
                placeholder="项目负责人"
                v-if="item.name === '项目负责人'"
                :style="{ width: '256px' }"
                disabled
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="$refs.executor.show()"
                v-if="item.name === '项目负责人'"
              >
                选择
              </el-button>

              <el-date-picker
                clearable
                v-model="queryForm.Date"
                end-placeholder="结束日期"
                format="yyyy-MM-dd"
                range-separator="-"
                start-placeholder="开始日期"
                :style="{ width: '360px' }"
                type="daterange"
                value-format="yyyy-MM-dd"
                v-if="item.name === '项目日期'"
              />
            </el-form-item>
            <el-form-item>
              <el-button
                icon="el-icon-search"
                type="primary"
                @click="fetchData"
              >
                查询
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-button
                native-type="submit"
                type="primary"
                @click="resetSearch"
              >
                重置
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-tooltip
                class="item"
                effect="dark"
                content="搜索筛选"
                placement="top"
              >
                <el-popover placement="left" trigger="click">
                  <filter-search
                    v-if="true"
                    :list="searchAll"
                    :name="localKey"
                    @updateSearchShow="initSearch"
                  />
                  <el-button slot="reference" style="height: 32px">
                    <vab-icon icon="filter" :is-custom-svg="true" />
                  </el-button>
                </el-popover>
              </el-tooltip>
            </el-form-item>
          </el-form>
        </vab-query-form-top-panel>
      </el-card>
    </vab-query-form>
    <el-card shadow="never">
      <vab-query-form-right-panel :span="24">
        <el-tooltip
          class="item"
          effect="dark"
          content="表格筛选"
          placement="top"
        >
          <el-popover placement="right" trigger="click">
            <filter-table
              :list="filedAll"
              :name="tableKey"
              @updateTableShow="initTable"
            />
            <el-button
              slot="reference"
              icon="el-icon-s-grid"
              class="biaoge"
              style="margin-bottom: 10px; margin-right: 10px"
            ></el-button>
          </el-popover>
        </el-tooltip>
        <!-- <el-button type="success" @click="handleEdit(null)">新增</el-button>
        <el-upload
          class="upload-demo"
          :show-file-list="false"
          :action="baseApi + api"
          :headers="headers"
          :on-success="handleSuccess"
        >
          <el-button type="success">导入</el-button>
        </el-upload>
        <el-button type="success" @click="handleExport">导出</el-button> -->
      </vab-query-form-right-panel>

      <el-table v-loading="listLoading" :data="list">
        <!-- <el-table-column align="center" label="序号" prop="gcxmzjZjbNo" /> -->
        <!-- <el-table-column align="center" label="合同编号" prop="htbh">
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.htbh }}
            </el-button>
          </template>
        </el-table-column> -->
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            v-if="item.name === '项目名称'"
            align="center"
            label="项目名称"
            prop="projectName"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleEdit(row, true)">
                {{ row.projectName }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            v-if="item.name === '组长'"
            align="center"
            label="组长"
            prop="leaderName"
          ></el-table-column>
          <el-table-column
            v-if="item.name === '副组长'"
            align="center"
            label="副组长"
            prop="fzzName"
          ></el-table-column>
          <el-table-column
            v-if="item.name === '主审'"
            align="center"
            label="主审"
            prop="zsname"
          ></el-table-column>
          <el-table-column
            v-if="item.name === '专业科室人员'"
            align="center"
            label="专业科室人员"
            prop="zyksAppStaffName"
          ></el-table-column>
          <el-table-column
            v-if="item.name === '上报时间'"
            align="center"
            label="上报时间"
            prop="startAppDate"
            :formatter="formatDate"
          ></el-table-column>
          <el-table-column
            v-if="item.name === '修改时间'"
            align="center"
            label="修改时间"
            prop=" updatedate"
          >
            <template #default="{ row }">
              {{ row.updatedate || row.createdate }}
            </template>
          </el-table-column>
          <el-table-column
            v-if="item.name === '定稿时间'"
            align="center"
            label="定稿时间"
            prop="endAppDate"
            :formatter="formatDate"
          ></el-table-column>
          <el-table-column
            v-if="item.name === '业务分管副主任'"
            align="center"
            label="业务分管副主任"
            prop="ywfgAppStaffName"
          ></el-table-column>
          <el-table-column
            v-if="item.name === '审批意见'"
            align="center"
            label="审批意见"
            prop="commont"
          ></el-table-column>
        </div>
        <el-table-column width="1"></el-table-column>
      </el-table>
    </el-card>

    <el-pagination
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />

    <Views ref="edit" @fetch-data="fetchData"></Views>

    <implementationAnalysisModel ref="xmnum" />
    <jhlistEdit ref="jhlistEdit" />

    <XMQDModel ref="XMQDModel" />
    <DGModel ref="DGModel" />
    <FAModel ref="FAModel" />
    <SSModel ref="SSModel" />

    <executor-options ref="executor" @selected="handleExecutorSelected" />
  </div>
</template>

<script>
  import { getReviewStatusList } from '@/oapi/audit/plan'
  import Views from '@/views/oilAudit/project/components/IndexEdit.vue'
  import { formatDate } from '@/utils/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import { baseURL } from '@/config'
  import store from '@/store'
  const token = store.getters['user/token']
  import implementationAnalysisModel from './components/implementationAnalysisModel'
  import ExecutorOptions from '@/views/oilAudit/implement/components/options/executor.vue'
  import jhlistEdit from './components/jhlistEdit'
  import YearPicker from './components/YearPicker'
  import XMQDModel from './components/XMQDModel'
  import DGModel from './components/DGModel'
  import FAModel from './components/FAModel'
  import SSModel from './components/SSModel'

  export default {
    name: 'gcxmzjzjb',
    components: {
      filterSearch,
      filterTable,
      implementationAnalysisModel,
      jhlistEdit,
      YearPicker,
      XMQDModel,
      DGModel,
      FAModel,
      SSModel,
      Views,
      ExecutorOptions,
    },
    mixins: [searchTableMixis],
    data() {
      return {
        baseApi: baseURL,
        api: '/oiaudit/gcxmzjzjb/importData',
        headers: { token },
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          projectName: '',
          projectOrderName: '',
          projectOrderId: '',
          Date: [],
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          // { name: '项目年度' },
          { name: '项目名称' },
          { name: '组长' },
          { name: '副组长' },
          { name: '主审' },
          { name: '专业科室人员' },
          { name: '上报时间' },
          { name: '修改时间' },
          { name: '定稿时间' },
          { name: '业务分管副主任' },
          { name: '审批意见' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-gcgl-planReviewTable-search',
        tableKey: 'oilAudit-gcgl-planReviewTable-list',
        searchMore: true,
        dateValue2: {},
      }
    },
    created() {
      this.fetchData()
      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
      },
      handleExecutorSelected(node) {
        this.queryForm.projectOrderName = node.realname
        this.queryForm.projectOrderId = node.staffid
      },
      handleworkPlanCount(row) {
        this.$refs['FAModel'].showEdit(row)
      },
      handleimplSuperCount(row) {
        this.$refs['SSModel'].showEdit(row)
      },
      handleaoverCount(row) {},
      handleworkSheetQuCount(row) {
        this.$refs['DGModel'].showEdit(row, 1)
      },
      handleworkSheetFhCount(row) {
        this.$refs['DGModel'].showEdit(row, 0)
      },
      handleitemCount(row) {
        this.$refs['XMQDModel'].showEdit(row)
      },
      updateStatisticYear(startYear, endYear) {
        console.log('看看回调：', startYear, endYear)
      },
      async handleExport() {
        const data = await engineeringCostCenterTableExport({
          'tblYqnsGcxmzj.gcmc': this.queryForm.tblYqnsGcxmzj.gcmc,
          'tblYqnsGcxmzj.htbh': this.queryForm.tblYqnsGcxmzj.htbh,
          createYear: this.queryForm.createYear,
          pageNumber: this.queryForm.pageNumber,
          pageSize: this.queryForm.pageSize,
        })
        let fileName = '工程项目造价中间表'
        let blob = new Blob([data], {
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        })
        if (window.navigator.msSaveOrOpenBlob) {
          navigator.msSaveBlob(blob, fileName)
        } else {
          let link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = fileName
          link.click()
          // 释放内存
          window.URL.revokeObjectURL(link.href)
        }
      },
      handleSuccess(response) {
        if (response.code == 1) {
          this.fetchData()
          this.$baseMessage('导入成功', 'success')
        } else {
          this.$baseMessage(response.msg, 'error')
        }
      },
      handleZ(row) {
        this.$refs['jhlistEdit'].showEdit(row, true)
      },
      getFiled() {
        return [
          { name: '项目负责人', key: 'projectOrderId' },
          { name: '项目名称', key: 'projectName' },
          { name: '项目日期', key: 'Date' },
        ]
      },
      resetQueryForm() {
        // this.queryForm = this.$options.data().queryForm;
        this.queryForm = {
          projectName: '',
          projectOrderName: '',
          projectOrderId: '',
          Date: [],
          pageNumber: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        let { Date, ...other } = this.queryForm
        let planStarttime = ''
        let planEndtime = ''
        if (Date) {
          planStarttime = Date[0]
          planEndtime = Date[1]
        }
        const {
          data: { tlist, totalRecord },
        } = await getReviewStatusList({
          ...other,
          planStarttime,
          planEndtime,
        })
        this.listLoading = false
        this.list = tlist
        this.total = totalRecord || 0
      },
      async handleDetail(row, type) {
        await this.$refs['xmnum'].showEdit(row, type)
      },
      handleEdit(row, disabled) {
        this.$refs['edit'].showEdit(row, disabled, this.planNum)
      },
      async handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          this.listLoading = true
          const res = await engineeringCostCenterTableDelete({
            ids: row.gcxmzjzjbid,
          })
          this.listLoading = false
          if (res && res.code == 1) {
            this.$message.success('操作成功！')
            this.fetchData()
          } else {
            this.$message.error(res.msg || '操作失败！')
          }
        })
      },
    },
  }
</script>

<style scoped lang="scss">
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }
  .margin-b0 {
    margin-bottom: 0;
  }
  .upload-demo {
    display: inline-block;
    margin: 0 10px;
  }
</style>
