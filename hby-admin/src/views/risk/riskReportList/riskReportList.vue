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
                v-model="queryForm.createUnitidName"
                clearable
                v-if="item.name === '单位'"
                placeholder="单位"
              />

              <el-input
                v-model="queryForm.deptName"
                clearable
                v-if="item.name === '部门'"
                placeholder="部门"
              />
              <el-date-picker
                v-model="queryForm.year"
                v-if="item.name === '年度'"
                type="year"
                value-format="yyyy"
                placeholder="选择年度"
              ></el-date-picker>
              <el-input
                v-model="queryForm.oneRisk"
                clearable
                v-if="item.name === '一级风险'"
                placeholder="一级风险"
              />
              <el-input
                v-model="queryForm.twoRisk"
                clearable
                v-if="item.name === '二级风险'"
                placeholder="二级风险"
              />
              <el-input
                v-model="queryForm.threeRisk"
                clearable
                v-if="item.name === '三级风险'"
                placeholder="三级风险"
              />
              <el-input
                v-model="queryForm.risknumber"
                clearable
                v-if="item.name === '风险编号'"
                placeholder="风险编号"
              />
              <el-select
                v-if="item.name === '上报开始月份'"
                v-model="queryForm.monthL"
                placeholder="请选择开始上报月份"
                @change="handleMonthLChange"
              >
                <el-option
                  v-for="month in 12"
                  :key="month"
                  :label="getMonthLabel(month)"
                  :value="month.toString()"
                />
              </el-select>

              <el-select
                v-if="item.name === '上报结束月份'"
                v-model="queryForm.monthR"
                placeholder="请选择结束上报月份"
                @change="handleMonthRChange"
              >
                <el-option
                  v-for="month in 12"
                  :key="month"
                  :label="getMonthLabel(month)"
                  :value="month.toString()"
                  :disabled="isMonthDisabled(month)"
                />
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
          label="序号"
          width="100"
          type="index"
        ></el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            v-if="item.name === '上报单位'"
            align="center"
            label="上报单位"
            prop="createUnitidName"
            show-overflow-tooltip
            width="220"
            fixed="left"
          />
          <el-table-column
            v-if="item.name === '关联风险点'"
            align="center"
            label="关联风险点"
            prop="risknumber"
            show-overflow-tooltip
            #default="{ row }"
          >
            <el-button type="text" @click="handleRead(row)">
              {{ row.risknumber }}
            </el-button>
          </el-table-column>
          <el-table-column
            v-if="item.name === '责任部门'"
            align="center"
            label="责任部门"
            prop="unitDeptidName"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '一级风险'"
            align="center"
            label="一级风险"
            prop="oneRisk"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '二级风险'"
            align="center"
            label="二级风险"
            prop="twoRisk"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '三级风险'"
            align="center"
            label="三级风险"
            prop="threeRisk"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '风险描述'"
            align="center"
            label="风险描述"
            prop="detailRisk"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '风险源分析（导致风险发生的潜在因素）'"
            align="center"
            label="风险源分析（导致风险发生的潜在因素）"
            prop="analysisRisk"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '现有应对措施'"
            align="center"
            label="现有应对措施"
            prop="analysisSol"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '相关制度和规程索引'"
            align="center"
            label="相关制度和规程索引"
            prop="analysisRel"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '典型风险事件描述'"
            align="center"
            label="典型风险事件描述"
            prop="analysisEve"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '风险发生可能性评价标准'"
            align="center"
            label="风险发生可能性评价标准"
            prop="assessStan"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '对应分值1'"
            align="center"
            label="对应分值"
            prop="assessScoreOne"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '风险影响程度评价标准'"
            align="center"
            label="风险影响程度评价标准"
            prop="assessInf"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '对应分值2'"
            align="center"
            label="对应分值"
            prop="assessScoreTwo"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '风险评分'"
            align="center"
            label="风险评分"
            prop="assessSco"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '风险等级'"
            align="center"
            label="风险等级"
            prop="assessGrade"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              {{ getRiskLevel(row.assessGrade) }}
            </template>
          </el-table-column>

          <el-table-column
            v-if="item.name === '风险变化趋势'"
            align="center"
            label="风险变化趋势"
            prop="riskChange"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              {{ getRiskChange(row.riskChange) }}
            </template>
          </el-table-column>
          <el-table-column
            v-if="item.name === '是否新增风险'"
            align="center"
            label="是否新增风险"
            prop="isNewRisk"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              {{ getRiskStatus(row.isNewRisk) }}
            </template>
          </el-table-column>
          <el-table-column
            v-if="item.name === '上报月份'"
            align="center"
            label="上报月份"
            prop="month"
          >
            <template #default="{ row }">
              {{ row.month ? getMonthLabel(Number(row.month)) : '' }}
            </template>
          </el-table-column>
          <el-table-column
            v-if="item.name === '创建日期'"
            align="center"
            label="创建日期"
            prop="createTime"
            width="150"
          />
        </div>

        <el-table-column
          label="操作"
          #default="{ row }"
          fixed="right"
          align="center"
          width="120"
        >
          <template>
            <el-button type="text" @click="handleEdit(row, true)">
              详情
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
    <el-dialog
      :close-on-click-modal="false"
      :append-to-body="true"
      :title="dialogTitle"
      :visible.sync="valueEditVisable"
      width="1000px"
      @close="valueEditVisable = false"
    >
      <valueEdit
        v-if="valueEditVisable"
        :curRow="curRow"
        @fetchData="fetchData"
        @close="valueEditVisable = false"
      />
    </el-dialog>
    <RiskEdit ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import {
    queryRiskAssessmentSummaryList,
    exportMonthlyEvaluationSummary,
  } from '@/api/risk/riskfill'
  import valueEdit from '@/views/risk/riskvalue/valueEdit'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import RiskEdit from '@/views/risk/identify/creation/components/RiskEdit.vue'

  export default {
    name: 'valueList',
    components: { valueEdit, filterSearch, filterTable, RiskEdit },
    mixins: [searchTableMixis],
    props: {
      isUEditor: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      return {
        listLoading: false,
        dialogTitle: '',
        valueEditVisable: false,
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        formDisabled: false,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          implementId: '',
          createUnitidName: '',
          deptName: '',
          oneRisk: '',
          twoRisk: '',
          threeRisk: '',
          risknumber: '',
          month: null,
          year: '',
        },
        total: 0,
        curRow: null,
        filedAll: [
          { name: '上报单位' },
          { name: '关联风险点' },
          { name: '责任部门' },
          { name: '一级风险' },
          { name: '二级风险' },
          { name: '三级风险' },
          { name: '风险描述' },
          { name: '风险源分析（导致风险发生的潜在因素）' },
          { name: '现有应对措施' },
          { name: '相关制度和规程索引' },
          { name: '典型风险事件描述' },
          { name: '风险发生可能性评价标准' },
          { name: '对应分值1' },
          { name: '风险影响程度评价标准' },
          { name: '对应分值2' },
          { name: '风险评分' },
          { name: '风险等级' },
          { name: '本月风险管控措施及实施情况' },
          { name: '风险变化趋势' },
          { name: '下月风险管控措施' },
          { name: '是否新增风险' },
          { name: '上报月份' },
          { name: '创建日期' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-fxgk-riskReporteList-search',
        tableKey: 'oilAudit-fxgk-riskReporteList-list',
        searchMore: true,
        select: [],
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
      // 获取月份标签
      getMonthLabel(month) {
        const monthLabels = [
          '一',
          '二',
          '三',
          '四',
          '五',
          '六',
          '七',
          '八',
          '九',
          '十',
          '十一',
          '十二',
        ]
        return `${monthLabels[month - 1]}月`
      },

      // 判断月份是否禁用
      isMonthDisabled(month) {
        if (!this.queryForm.monthL) return false
        return Number(month) < Number(this.queryForm.monthL)
      },

      // 开始月份变化
      handleMonthLChange(val) {
        if (
          this.queryForm.monthR &&
          Number(this.queryForm.monthR) < Number(val)
        ) {
          this.queryForm.monthR = ''
        }
      },

      // 结束月份变化
      handleMonthRChange() {
        if (!this.queryForm.monthL) {
          this.$message.warning('请先选择开始月份')
          this.queryForm.monthR = ''
        }
      },
      //风险等级
      getRiskLevel(level) {
        const levelMap = {
          1: '极低',
          2: '低',
          3: '中',
          4: '高',
          5: '极高',
        }
        return levelMap[level] || level
      },
      //风险变化趋势
      getRiskChange(level) {
        const levelMap = {
          1: '升高',
          2: '持平',
          3: '下降',
        }
        return levelMap[level] || level
      },
      //	是否风险
      getRiskStatus(status) {
        const statusMap = {
          1: '新增风险',
          2: '已有风险',
          3: '关闭风险',
        }
        return statusMap[status] || '未知状态'
      },
      getFiled() {
        return [
          { name: '单位', key: 'createUnitidName' },
          { name: '部门', key: 'deptName' },
          { name: '年度', key: 'year' },
          { name: '一级风险', key: 'oneRisk' },
          { name: '二级风险', key: 'twoRisk' },
          { name: '三级风险', key: 'threeRisk' },
          { name: '风险编号', key: 'risknumber' },
          { name: '上报开始月份', key: 'monthL' },
          { name: '上报结束月份', key: 'monthR' },
        ]
      },
      resetSearch() {
        this.queryForm.pageNumber = 1
        this.queryForm.pageSize = 20
        this.queryForm.implementId = ''
        this.queryForm.createUnitidName = ''
        this.queryForm.deptName = ''
        this.queryForm.oneRisk = ''
        this.queryForm.twoRisk = ''
        this.queryForm.threeRisk = ''
        this.queryForm.risknumber = ''
        this.queryForm.monthL = ''
        this.queryForm.monthR = ''
        this.queryForm.year = ''
        this.fetchData()
      },
      // 修改查询方法
      queryData() {
        // 月份校验
        if (
          (this.queryForm.monthL && !this.queryForm.monthR) ||
          (!this.queryForm.monthL && this.queryForm.monthR)
        ) {
          this.$message.warning('请同时选择开始和结束月份')
          return
        }

        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        let params = {
          ...this.queryForm,
        }
        queryRiskAssessmentSummaryList(params)
          .then((res) => {
            if (
              res &&
              res.data &&
              res.data.data &&
              res.data.data.pageInfo &&
              res.data.data.pageInfo.tlist
            ) {
              this.list = res.data.data.pageInfo.tlist.map((v) => {
                if (v.createTime) {
                  v.createTime = v.createTime.slice(0, 10)
                }
                return v
              })
              this.total = res.data.data.pageInfo.totalRecord
            }
            this.setCheckedRows()
          })
          .finally(() => {
            this.listLoading = false
          })
      },
      async handleEdit(row, disabled) {
        this.curRow = row ? JSON.parse(JSON.stringify(row)) : {}
        this.curRow.implementId = this.$route.query.implementId
        if (row && disabled) {
          this.dialogTitle = '详情'
          this.curRow.disabled = true
        } else if (row && !disabled) {
          this.dialogTitle = '修改'
        } else {
          this.dialogTitle = '新增'
        }
        this.valueEditVisable = true
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      //导出
      async handleExport() {
        const ids = this.select.map((res) => res.id)
        const data = await exportMonthlyEvaluationSummary({
          id: ids.toString(),
          ...this.queryForm,
        })
        let fileName = '月度评估汇总'
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
      /**
       * @description: 打开详情
       * @return {*}
       */
      handleRead(row) {
        this.$refs['edit'].showEdit(row, '', true)
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
<style scoped>
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
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
