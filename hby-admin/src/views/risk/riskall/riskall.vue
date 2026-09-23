<template>
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
                v-model="queryForm.impRiskName"
                clearable
                v-if="item.name === '风险名称'"
                placeholder="风险名称"
              />
              <el-input
                v-model="queryForm.orgName"
                clearable
                v-if="item.name === '上报单位'"
                placeholder="上报单位"
              />
              <el-select
                v-model="queryForm.jd"
                clearable
                v-if="item.name === '季度'"
                placeholder="请选择季度"
              >
                <el-option label="全部" value="" />
                <el-option label="一季度" value="一季度" />
                <el-option label="二季度" value="二季度" />
                <el-option label="三季度" value="三季度" />
                <el-option label="四季度" value="四季度" />
              </el-select>
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
        </vab-query-form-top-panel>
      </el-card>
    </vab-query-form>
    <el-card shadow="never">
      <vab-query-form class="margin-b0">
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
          <el-button type="primary" v-if="isUEditor" @click="handleCancel">
            取消
          </el-button>
          <el-button type="primary" v-if="isUEditor" @click="handleConfirm">
            确定
          </el-button>
          <el-button type="success" v-if="!isUEditor" @click="handleExport()">
            导出
          </el-button>
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-table
        v-loading="listLoading"
        :data="list"
        @select-all="handleSelectAll"
        @select="handleSelection"
        ref="multipleTable"
        @selection-change="handleSelectionChange"
      >
        <el-table-column
          width="48"
          type="selection"
          :reserve-selection="true"
        ></el-table-column>
        <el-table-column
          align="center"
          label="上报单位"
          prop="impLssuedUnitName"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="季度"
          prop="jd"
          show-overflow-tooltip
        />

        <el-table-column
          align="center"
          label="风险名称"
          prop="impRiskName"
          #default="{ row }"
          width="200"
        >
          <el-button type="text" @click="handleEdit(row, 'detail')">
            {{ row.impRiskName }}
          </el-button>
        </el-table-column>
        <el-table-column width="1" />

        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="上报日期"
            prop="toreportdate"
            width="120"
            v-if="item.name === '上报日期'"
          />
          <el-table-column
            v-if="item.name === '牵头领导'"
            align="center"
            label="牵头领导"
            prop="impWayStaffName"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '牵头责任部门'"
            align="center"
            label="牵头责任部门"
            prop="impWayDeptName"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '公司相关责任单位'"
            align="center"
            label="公司相关责任单位"
            prop="impDutyUnitName"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '重点事项'"
            align="center"
            label="重点事项"
            prop="impKeyIssues"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '风险描述'"
            align="center"
            label="风险描述"
            prop="impRiskDetails"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '本季度风险防控情况'"
            align="center"
            label="本季度风险防控情况"
            prop="impThisControl"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '已发生的风险事件及应对处置情况'"
            align="center"
            label="已发生的风险事件及应对处置情况"
            prop="impSolutions"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '需要提示的问题和风险'"
            align="center"
            label="需要提示的问题和风险"
            prop="impTips"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '下季度主要风险研判及相应防控措施'"
            align="center"
            label="下季度主要风险研判及相应防控措施"
            prop="impTextControl"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '其他需要说明的情况'"
            align="center"
            label="其他需要说明的情况"
            prop="impOther"
            show-overflow-tooltip
          />
        </div>
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
    <el-dialog
      :close-on-click-modal="false"
      :append-to-body="true"
      :title="dialogTitle"
      :visible.sync="riskfillEdit2Visable"
      width="1000px"
      @close="riskfillEdit2Visable = false"
    >
      <riskfillEdit2
        v-if="riskfillEdit2Visable"
        :curRow="curRow"
        @fetchData="fetchData"
        @close="riskfillEdit2Visable = false"
      />
    </el-dialog>
  </div>
</template>

<script>
  import lxjybSelectModal from '@/components/selectPerson'
  import riskfillEdit from '@/views/risk/riskfill/riskfillEdit'
  // import valueList from '@/views/risk/riskvalue/valueList'
  import { formatDate } from '@/utils/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import {
    issuedImplementSummary,
    riskReportingDelete,
    exportIssuedImplementSummary,
  } from '@/api/risk/riskfill'
  import { baseURL } from '@/config'
  import store from '@/store'
  const token = store.getters['user/token']
  import { xiafaListNew } from '@/oapi/audit/preparation'
  import riskfillEdit2 from '@/views/risk/riskfill/riskfillEdit2'

  export default {
    name: 'Download',
    components: {
      lxjybSelectModal,
      filterSearch,
      filterTable,
      valueList: () => import('@/views/risk/riskvalue/valueList'),

      riskfillEdit2,
    },
    mixins: [searchTableMixis],
    props: {
      isUEditor: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      return {
        baseApi: baseURL,
        api: '/oiaudit/plan/require/suggestion/import?isCover=1',
        headers: { token },
        select: [],
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        isCover: 0,
        queryForm: {
          impRiskName: '',
          pageNumber: 1,
          pageSize: 20,
          reportingld: '',
          orgName: '',
          jd: '',
        },
        filedAll: [
          { name: '风险名称' },
          { name: '上报单位' },
          { name: '季度' },
          { name: '上报日期' },
          { name: '牵头领导' },
          { name: '牵头责任部门' },
          { name: '公司相关责任单位' },
          { name: '重点事项' },
          { name: '风险描述' },
          { name: '本季度风险防控情况' },
          { name: '已发生的风险事件及应对处置情况' },
          { name: '需要提示的问题和风险' },
          { name: '下季度主要风险研判及相应防控措施' },
          { name: '其他需要说明的情况' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-fxgk-riskvalue-search',
        tableKey: 'oilAudit-fxgk-riskvalue-list',
        searchMore: true,
        dialogTitle: '新增',
        riskfillEdit2Visable: false,
        curRow: null,
        multipleSelection: [],
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
      handleSelectionChange(val) {
        this.multipleSelection = val
      },
      handleSuccess(response) {
        if (response.code == 1) {
          this.fetchData()
          this.$baseMessage('导入成功', 'success')
        } else {
          this.$baseMessage(response.msg, 'error')
        }
      },
      handlePush(row) {
        this.curRow = JSON.parse(JSON.stringify(row))
        this.riskfillEdit2Visable = true
      },
      getFiled() {
        return [
          { name: '风险名称', key: 'impRiskName' },
          { name: '上报单位', key: 'orgName' },
          { name: '季度', key: 'jd' },
          // { name: '牵头领导', key: 'impWayStaffName' },
          // { name: '牵头责任部门', key: 'impWayDeptName' },
          // { name: '风险描述', key: 'impRiskDetails' },
          // { name: '本季度风险防控情况', key: 'impThisControl' },
          // { name: '上报年度', key: 'nd' },
          // { name: '季度', key: 'jd' },
        ]
      },
      resetQueryForm() {
        this.queryForm = {
          impRiskName: '',
          pageNumber: 1,
          pageSize: 20,
          reportingld: '',
          orgName: '',
          jd: '',
        }
      },
      resetSearch() {
        this.resetQueryForm()
        // 清空已选择的数据
        this.select = []
        // 清空表格选择状态
        if (this.$refs.multipleTable) {
          this.$refs.multipleTable.clearSelection()
        }
        this.fetchData()
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
      },
      queryData() {
        this.queryForm.pageNumber = 1
        // 清空已选择的数据
        this.select = []
        // 清空表格选择状态
        if (this.$refs.multipleTable) {
          this.$refs.multipleTable.clearSelection()
        }
        this.fetchData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },

      async handleEdit(row, disabled) {
        this.curRow = JSON.parse(JSON.stringify(row))
        if (row && disabled) {
          this.dialogTitle = '详情'
          this.curRow.disabled = true
        } else if (row && !disabled) {
          this.dialogTitle = '修改'
        } else {
          this.dialogTitle = '新增'
        }
        this.riskfillEdit2Visable = true
      },
      async fetchData() {
        this.listLoading = true
        issuedImplementSummary(this.queryForm)
          .then((res) => {
            if (
              res &&
              res.data &&
              res.data.data &&
              res.data.data.pageInfo &&
              res.data.data.pageInfo.tlist
            ) {
              this.list = res.data.data.pageInfo.tlist
              this.total = res.data.data.pageInfo.totalRecord
            }
            this.setCheckedRows()
          })
          .finally(() => {
            this.listLoading = false
          })
      },
      // 翻页的时候回显已勾选的数据
      setCheckedRows() {
        this.$nextTick(() => {
          this.select.forEach((row) => {
            this.$refs.multipleTable.toggleRowSelection(
              this.list.find((item) => {
                return row.id == item.id
              }),
              true
            )
          })
        })
      },
      handleSelection(val, row) {
        const i = this.select.findIndex((x) => x.id == row.id)
        if (i < 0) {
          this.select.push(row)
        } else {
          this.select.splice(i, 1)
        }
      },
      handleSelectAll(val) {
        const curSelected = val.filter((x) => !!x)
        if (curSelected && curSelected.length) {
          curSelected.map((row) => {
            if (row && !this.select.some((x) => x.id == row.id)) {
              this.select.push(row)
            }
          })
        } else {
          this.list.map((row) => {
            const i = this.select.findIndex((x) => x.id == row.id)
            if (i >= 0) {
              this.select.splice(i, 1)
            }
          })
        }
      },
      //导出
      async handleExport(val) {
        const ids = this.select.map((res) => res.id)
        const data = await exportIssuedImplementSummary({
          id: ids.toString(),
          ...this.queryForm,
        })
        let fileName = '重大风险汇总'
        let blob = new Blob([data], {
          type: 'application/vnd.ms-excel',
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
      // 选择变化时触发
      handleSelectionChange(val) {
        this.multipleSelection = val
      },
      // 取消按钮
      handleCancel() {
        this.$emit('close')
      },

      // 确认按钮
      handleConfirm() {
        // 构建选中数据的文本
        this.$emit('submit', this.multipleSelection)
      },
      clearSelection() {
        this.$refs.multipleTable.clearSelection()
        this.multipleSelection = []
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
