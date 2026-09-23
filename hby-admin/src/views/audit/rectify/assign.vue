<template>
  <div class="system-log-container">
    <vab-query-form class="margin-b0">
      <el-card shadow="never">
        <vab-query-form-left-panel :span="24">
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
                v-if="item.name === '通知编号'"
                v-model="queryForm.planCode"
                clearable
                placeholder="通知编号"
              />

              <el-input
                v-if="item.name === '通知名称'"
                v-model="queryForm.planName"
                clearable
                placeholder="通知名称"
              />

              <el-select
                v-if="item.name === '通知类别'"
                v-model="queryForm.planType"
                placeholder="请选择通知类别"
                clearable
              >
                <el-option label="审计" value="1" />
                <el-option label="内控" value="2" />
                <el-option label="非系统实施" value="3" />
                <el-option label="外部审计" value="4" />
              </el-select>

              <el-select
                v-if="item.name === '状态'"
                v-model="queryForm.status"
                placeholder="请选择状态"
              >
                <el-option label="未审批" value="0" />
                <el-option label="审批中" value="1" />
                <el-option label="已退回" value="2" />
                <el-option label="已撤销" value="3" />
                <el-option label="未下发" value="6" />
                <el-option label="未分派" value="7" />
                <el-option label="未启动" value="8" />
                <el-option label="开始整改" value="9" />
                <el-option label="整改完成" value="10" />
              </el-select>

              <el-date-picker
                v-if="item.name === '创建日期'"
                v-model="queryForm.Date"
                clearable
                end-placeholder="创建结束日期"
                format="yyyy-MM-dd"
                range-separator="-"
                start-placeholder="创建开始日期"
                :style="{ width: '300px' }"
                type="daterange"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
            <el-form-item>
              <el-button
                icon="el-icon-search"
                native-type="submit"
                type="primary"
                @click="queryData"
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
        </vab-query-form-left-panel>
        <!-- <vab-query-form-right-panel>
        <el-button type="success" @click="handleAdd">新建</el-button>
      </vab-query-form-right-panel> -->
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
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="通知编号"
          prop="planCode"
          width="100"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.planCode }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            v-if="item.name === '通知名称'"
            align="center"
            label="通知名称"
            prop="planName"
          />
          <el-table-column
            v-if="item.name === '通知类别'"
            align="center"
            label="通知类别"
            prop="planType"
            #default="{ row }"
          >
            {{
              ['审计', '内控', '非系统实施', '外部审计'][
                Number(row.planType) - 1
              ]
            }}
          </el-table-column>
          <el-table-column
            align="center"
            label="创建人"
            v-if="item.name === '创建人'"
            prop="createStaffName"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            v-if="item.name === '创建日期'"
            label="创建日期"
            prop="createTime"
            show-overflow-tooltip
            :formatter="formatDate"
          />
          <el-table-column
            align="center"
            label="截止日期"
            v-if="item.name === '截止日期'"
            prop="deadlineTime"
            show-overflow-tooltip
            :formatter="formatDate"
          />
          <el-table-column
            align="center"
            v-if="item.name === '整改责任人'"
            label="整改责任人"
            prop="zrrRealName"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            v-if="item.name === '总分派记录数'"
            label="总分派记录数"
            prop="totalRecord"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            v-if="item.name === '已分派记录数'"
            label="已分派记录数"
            prop="allocatedRecord"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            v-if="item.name === '未分派记录数'"
            label="未分派记录数"
            prop="un"
            show-overflow-tooltip
            #default="{ row }"
          >
            {{ Number(row.totalRecord) - Number(row.allocatedRecord) }}
          </el-table-column>
          <el-table-column
            align="center"
            v-if="item.name === '状态'"
            label="状态"
            prop="status"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              {{
                {
                  '0': '未审批',
                  '1': '审批中',
                  '2': '已退回',
                  '3': '已撤销',
                  '6': '未下发',
                  '7': '未分派',
                  '8': '未启动',
                  '9': '开始整改',
                  '10': '整改完成',
                  '11': '关闭',
                }[row.status] || '未审批'
              }}
            </template>
          </el-table-column>
        </div>

        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="120"
        >
          <template #default="{ row }">
            <el-button
              v-if="row.status != '2' && row.status != '3'"
              type="text"
              @click="assign(row)"
            >
              分派
            </el-button>
          </template>
        </el-table-column>
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
    <scheme-info
      v-if="showDialog"
      @closeDialog="closeDialog"
      ref="edit"
      @fetch-data="fetchData"
    />
    <assignTable ref="table" @fetch="fetchData" />
  </div>
</template>

<script>
  import {
    getRectificationPlanList,
    getRectificationPlanDetail,
  } from '@/api/zgzz/index.js'
  import SchemeInfo from './components/SchemeInfo'
  import { formatDay } from '@/utils/index'
  import assignTable from './components/table/assignTable'

  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  export default {
    name: 'Download',
    components: { SchemeInfo, assignTable, filterSearch, filterTable },
    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          planCode: '',
          planName: '',
          createStaffName: '',
          createStaff: '',
          status: '',
          Date: '',
          createTimeStart: '',
          createTimeEnd: '',
          planType: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '通知名称' },
          { name: '通知类别' },
          { name: '创建人' },
          { name: '创建日期' },
          { name: '截止日期' },
          { name: '整改责任人' },
          { name: '总分派记录数' },
          { name: '已分派记录数' },
          { name: '未分派记录数' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'audit-rectify-assign-search',
        tableKey: 'audit-rectify-assign-list',
        searchMore: true,
        showDialog: false,
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
      getFiled() {
        return [
          { name: '通知编号', key: 'planCode' },
          { name: '通知名称', key: 'planName' },
          { name: '通知类别', key: 'planType' },
          { name: '创建人', key: 'createStaffName' },
          { name: '状态', key: 'status' },
          { name: '创建日期', key: 'Date' },
        ]
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      resetQueryForm() {
        // this.queryForm = this.$options.data().queryForm;
        this.queryForm = {
          planCode: '',
          planName: '',
          createStaff: '',
          status: '',
          Date: '',
          createTimeStart: '',
          createTimeEnd: '',
          planType: '',
          pageNumber: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const obj = { ...this.queryForm }
        obj.createTimeStart = obj.Date[0]
        obj.createTimeEnd = obj.Date[1]
        delete obj.Date
        const {
          data: { tlist, totalRecord },
        } = await getRectificationPlanList({ ...obj, selectType: 2 })
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      async handleDetail(row) {
        this.showDialog = true
        this.$nextTick(async () => {
          const res = await getRectificationPlanDetail({ planId: row.planId })
          await this.$refs['edit'].showEdit('detail', res.data)
        })
      },
      closeDialog() {
        this.showDialog = false
      },
      async assign(row) {
        // if (row.status != "7") {
        //   this.$baseMessage("该项目已开始整改或整改完成，无法再分派人员", "error");
        //   return;
        // }
        await this.$refs['table'].showEdit(row)
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await doDelete({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
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
</style>
