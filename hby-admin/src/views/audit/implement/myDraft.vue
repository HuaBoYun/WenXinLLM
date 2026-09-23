<template>
  <div class="system-log-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-top-panel>
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item v-for="(item, index) in searchItem" :key="index">
              <el-input
                v-model="queryForm.sheetcode"
                clearable
                placeholder="底稿编号"
                v-if="item.name === '底稿编号'"
              />
              <el-input
                v-model="queryForm.sheetname"
                clearable
                placeholder="底稿名称"
                v-if="item.name === '底稿名称'"
              />
              <el-input
                v-model="queryForm.realname"
                placeholder="拟稿人"
                v-if="item.name === '拟稿人'"
                :style="{ width: '256px' }"
                disabled
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                v-if="item.name === '拟稿人'"
                @click="$refs.executor.showEdit()"
              >
                选择
              </el-button>
              <el-select
                v-model="queryForm.status"
                placeholder="审核状态"
                v-if="item.name === '审核状态'"
              >
                <el-option label="审批中" value="1" />
                <el-option label="已退回" value="2" />
                <el-option label="已撤回" value="3" />
                <!-- <el-option label="已终止" value="4" />
                <el-option label="已跟踪" value="5" /> -->
                <el-option label="已完成" value="6" />
                <el-option label="未审批" value="0" />
              </el-select>
              <el-select
                v-model="queryForm.internalType"
                clearable
                placeholder="问题类型"
                v-if="item.name === '问题类型'"
              >
                <el-option
                  v-for="option in problemTypeOptions"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                />
              </el-select>
              <el-date-picker
                v-model="queryForm.dateRange"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                v-if="item.name === '时间范围'"
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
            <el-form-item>
              <span
                :class="searchMore ? 'search-more is-opened' : 'search-more'"
                @click="showMore"
              >
                <span>{{ searchMore ? '收起' : '展开' }}</span>
                <i class="el-icon-arrow-down"></i>
              </span>
            </el-form-item>
          </el-form>
        </vab-query-form-top-panel>
      </el-card>
    </vab-query-form>

    <el-card shadow="never" class="secondCard">
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
            <!-- <i class="el-icon-delete" slot="reference"></i> -->
            <el-button
              slot="reference"
              icon="el-icon-s-grid"
              class="biaoge"
              style="margin-bottom: 10px; margin-right: 10px"
            ></el-button>
          </el-popover>
        </el-tooltip>
        <el-button type="success" @click="handleAllExport()">导出</el-button>
        <el-button type="success" @click="handleAdd">新建</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="底稿编号"
          prop="sheetCode"
          width="170"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.sheetCode }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="底稿名称"
            v-if="item.name === '底稿名称'"
            prop="sheetName"
            show-overflow-tooltip
          />
          <!-- <el-table-column
            align="center"
            label="审计目标"
            prop="sheetTarget"
            show-overflow-tooltip
          /> -->
          <el-table-column
            align="center"
            label="被审计对象"
            v-if="item.name === '被审计对象'"
            prop="orgIdNames"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            align="center"
            label="拟稿人"
            v-if="item.name === '拟稿人'"
            prop="realname"
            show-overflow-tooltip
          />
          <!-- <el-table-column
            align="center"
            label="审批人"
            prop="approver"
            show-overflow-tooltip
          >
            <template slot-scope="{ row }">
              {{ row.yjfh && row.ejfh ? row.yjfh + ',' + row.ejfh : '' }}
            </template>
          </el-table-column> -->
          <el-table-column
            align="center"
            label="拟稿日期"
            v-if="item.name === '拟稿日期'"
            prop="createTime"
            show-overflow-tooltip
            :formatter="formatDates"
          />
          <el-table-column
            align="center"
            label="状态"
            v-if="item.name === '状态'"
            prop="state"
            show-overflow-tooltip
          >
            <!-- <template #default="{ row }">
              {{
                row.state == '1'
                  ? '未复核'
                  : row.state == '2'
                  ? '复核中'
                  : row.state == '3'
                  ? '复核终止'
                  : row.state == '4'
                  ? '复核通过'
                  : '已退回'
              }}
            </template> -->
            <template #default="{ row }">
              {{
                row.state == 1
                  ? '审批中'
                  : row.state == 2
                  ? '已退回'
                  : row.state == 3
                  ? '已撤回'
                  : row.state == 4
                  ? '已终止'
                  : row.state == 5
                  ? '已跟踪'
                  : row.state == 6
                  ? '已完成'
                  : '未审批'
              }}
            </template>
          </el-table-column>
        </div>

        <!-- <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="220"
        >
          <template #default="{ row }">
            <el-button
              type="text"
              :disabled="row.state != 0"
              @click="handleEdit(row)"
            >
              修改
            </el-button>
            <el-button
              :disabled="row.state"
              type="text"
              @click="handleReview(row)"
            >
              提交审批
            </el-button>
            <el-button
              type="text"
              @click="handleDelete(row)"
              :disabled="row.state"
            >
              删除
            </el-button>
          </template>
        </el-table-column> -->
        <el-table-column label="操作" fixed="right" align="center">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row)"
              :disabled="!!row.state || createId != row.createstaffid"
            >
              修改
            </el-button>
            <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button
                    type="text"
                    :disabled="!row.state"
                    @click.native="handleDeal(row)"
                  >
                    办理
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    :disabled="
                      !!row.state || btnLoading || createId != row.createstaffid
                    "
                    @click.native="handleReview(row)"
                  >
                    提交审批
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button type="text" @click.native="handleExport(row)">
                    导出
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    :disabled="!!row.state || createId != row.createstaffid"
                    @click.native="handleDelete(row)"
                  >
                    删除
                  </el-button>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
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
    <MyDraftInfo ref="edit" @fetch-data="fetchData" />
    <executor-options ref="executor" @projectManage="handleExecutorSelected" />
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqddeal" />
  </div>
</template>

<script>
  import {
    doDelete,
    myDraftDetail,
    myDraftExport,
    myDraftAllExport,
    myDraftList,
    saveDraft,
  } from '@/api/audit/implement'
  import { formatDay } from '@/utils/index'
  import MyDraftInfo from './components/myDraftInfo'
  import ExecutorOptions from '@/components/danxuanPerson.vue'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import { getFlowPkInfo } from '@/api/contract/manage'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'Download',
    mixins: [searchTableMixis],
    components: {
      filterTable,
      filterSearch,
      MyDraftInfo,
      ExecutorOptions,
      ProcessList,
      WfqdDeal,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          sheetname: '',
          sheetcode: '',
          realname: '',
          status: '',
          staffid: '',
          internalType: '',
          dateRange: [],
          pageNumber: 1,
          pageSize: 20,
        },
        projectInfo: {},
        // 筛选、表格头自定义
        filedAll: [
          { name: '底稿名称' },
          { name: '被审计对象' },
          { name: '拟稿人' },
          { name: '拟稿日期' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'audit-implement-myDraft-search',
        tableKey: 'audit-implement-myDraft-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        btnLoading: false,
        createId: JSON.parse(localStorage.getItem('userInfo')).staffid,
        problemTypeOptions: [
          {
            label: '贯彻国家重大决策部署及国资委监管任务',
            value: '贯彻国家重大决策部署及国资委监管任务',
          },
          {
            label: '公司治理（战略执行、改革改制）',
            value: '公司治理（战略执行、改革改制）',
          },
          { label: '财务资金与会计核算', value: '财务资金与会计核算' },
          { label: '投资管理', value: '投资管理' },
          { label: '采购及销售管理', value: '采购及销售管理' },
          { label: '金融业务', value: '金融业务' },
          { label: '信息化管理', value: '信息化管理' },
          { label: '境外国有资产管理', value: '境外国有资产管理' },
          { label: '八项规定和廉洁从业', value: '八项规定和廉洁从业' },
          { label: '其他', value: '其他' },
        ],
      }
    },
    async created() {
      this.fetchData()
      this.initTable() //初始化表格
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    methods: {
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '底稿编号', key: 'sheetcode' },
          { name: '底稿名称', key: 'sheetname' },
          { name: '拟稿人', key: 'realname' },
          { name: '审核状态', key: 'status' },
          { name: '问题类型', key: 'internalType' },
          { name: '时间范围', key: 'dateRange' },
        ]
        return fields
      },
      handleExecutorSelected(node) {
        this.queryForm.realname = node[0].realname
        this.queryForm.staffid = node[0].staffid
      },
      formatDates(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
      },
      resetQueryForm() {
        this.queryForm = {
          sheetname: '',
          sheetcode: '',
          realname: '',
          status: '',
          staffid: '',
          internalType: '',
          dateRange: [],
          pageNumber: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        const { dateRange, ...otherParams } = this.queryForm
        const [startDate, endDate] = dateRange || []
        const {
          data: {
            project: project,
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await myDraftList({
          ...otherParams,
          startDate: startDate || '',
          endDate: endDate || '',
        })
        this.projectInfo = project
        this.list = list
        this.total = total
        this.listLoading = false
      },
      /**
       * @description: 导出
       * @return {*}
       */
      async handleExport(row) {
        const data = await myDraftExport({ sheetid: row.sheetId })
        let fileName = '我的底稿.docx'
        let blob = new Blob([data], {
          type: '',
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
      /**
       * @description: 全量导出
       * @return {*}
       */
      async handleAllExport(row) {
        const { dateRange, ...otherParams } = this.queryForm
        const [startDate, endDate] = dateRange || []
        let params = {
          sheetName: this.queryForm.sheetname,
          sheetCode: this.queryForm.sheetcode,
          status: this.queryForm.status,
          createstaff: this.queryForm.staffid,
          internalType: this.queryForm.internalType,
          startDate: startDate || '',
          endDate: endDate || '',
          pageNumber: 1,
          pageSize: 20,
        }
        const data = await myDraftAllExport(params)
        let fileName = '我的底稿.xlsx'
        let blob = new Blob([data], {
          type: '',
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
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */
      handleAdd() {
        this.$refs['edit'].showEdit('add', null)
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */
      async handleDetail(row) {
        const data = await myDraftDetail({ sheetid: row.sheetId })
        this.$refs['edit'].showEdit('detail', data.data)
      },
      /**
       * @description: 打开编辑表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */
      async handleEdit(row) {
        const data = await myDraftDetail({ sheetid: row.sheetId })
        this.$refs['edit'].showEdit('edit', data.data)
      },
      handleReview(row) {
        try {
          this.$baseConfirm('你确定要提交当前项吗', null, async () => {
            this.btnLoading = true
            const tableId = 10
            const fromId = row.sheetId
            this.$refs['process'].save(tableId, fromId)
          })
        } catch (error) {
          this.btnLoading = false
        }
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await doDelete({ sheetid: row.sheetId })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
          await this.fetchData()
        })
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      async handleDeal(row) {
        const res = await getFlowPkInfo({
          formId: row.sheetId,
          tableId: 10,
        })

        this.$refs.wfqddeal.show(res.data, false)
      },
    },
  }
</script>

<style scoped lang="scss">
  .system-log-container {
    background: #f6f8f9 !important;
    padding: 0 !important;
  }
  .secondCard {
    margin-top: -5px !important;
  }
  .pagination {
    margin-bottom: 20px !important;
  }
</style>
