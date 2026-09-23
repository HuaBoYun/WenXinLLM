<template>
  <div class="system-log-container">
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
        <el-button
          type="success"
          @click="handleEdit(null)"
          v-if="!formDisabled && closestatus != 6"
        >
          新增
        </el-button>
        <el-button type="success" @click="handleExport()">导出</el-button>
      </vab-query-form-right-panel>

      <el-table
        v-loading="listLoading"
        :data="list"
        @select-all="handleSelectAll"
        @select="handleSelection"
        ref="multipleTable"
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
            v-if="item.name === '责任部门'"
            align="center"
            label="责任部门"
            prop="unitDeptidName"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '上报月份'"
            align="center"
            label="上报月份"
            prop="month"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              {{ formatMonth(row.month) }}
            </template>
          </el-table-column>
          <el-table-column
            v-if="item.name === '关联风险点'"
            align="center"
            label="关联风险点"
            prop="risknumber"
            show-overflow-tooltip
            width="130"
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
          <!-- <el-table-column v-if="item.name === '本月风险管控措施及实施情况'" align="center" label="本月风险管控措施及实施情况" prop="monthMea"
            show-overflow-tooltip /> -->
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
          <!-- <el-table-column v-if="item.name === '下月风险管控措施'" align="center" label="下月风险管控措施" prop="nextMonthMea"
            show-overflow-tooltip /> -->
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
            align="center"
            label="审批状态"
            prop="status"
            v-if="item.name === '审批状态'"
          >
            <template #default="{ row }">
              {{
                row.status == 1
                  ? '审批中'
                  : row.status == 2
                  ? '需调整'
                  : row.status == 3
                  ? '已撤销'
                  : row.status == 4
                  ? '已终止'
                  : row.status == 5
                  ? '已跟踪'
                  : row.status == 6
                  ? '已完成'
                  : '未审批'
              }}
            </template>
          </el-table-column>
        </div>
        <el-table-column
          label="操作"
          #default="{ row }"
          fixed="right"
          align="center"
          width="120"
        >
          <template>
            <el-button
              type="text"
              @click="handleEdit(row)"
              :disabled="!!+row.status || createId != row.createStaffid"
              v-if="closestatus != 6"
            >
              修改
            </el-button>
            <el-dropdown style="margin-left: 10px" v-if="closestatus != 6">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button type="text" @click.native="handleEdit(row, true)">
                    详情
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click.native="handleManage(row)"
                    :disabled="!+row.status"
                  >
                    办理
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="handleApproval(row)"
                    :disabled="!!+row.status || createId != row.createStaffid"
                  >
                    上报审批
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click.native="handleDelete(row)"
                    :disabled="!!+row.status || createId != row.createStaffid"
                  >
                    删除
                  </el-button>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
            <el-button
              type="text"
              @click.native="handleEdit(row, true)"
              v-if="closestatus == 6"
            >
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
        :createData="createData"
        :controlList="controlList"
        :newRisk="newRisk"
      />
    </el-dialog>
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqddeal" />
  </div>
</template>

<script>
  import {
    queryMonthlyEvaluationList,
    monthlyEvaluationADelete,
    exportPGList,
    monthlyEvaluationSubmit,
  } from '@/api/risk/riskfill'
  import valueEdit from '@/views/risk/riskvalue/valueEdit'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import { getFlowPkInfo } from '@/api/contract/manage'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import { riskAnalysisDetail } from '@/api/risk'
  import { riPlanInfo } from '@/api/systemLog'

  export default {
    name: 'valueList',
    components: { valueEdit, filterTable, WfqdDeal, ProcessList },
    mixins: [searchTableMixis],
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
          riskid: '',
        },
        total: 0,
        curRow: null,
        filedAll: [
          { name: '责任部门' },
          { name: '上报月份' },
          { name: '关联风险点' },
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
          { name: '风险变化趋势' },
          { name: '是否新增风险' },
          { name: '审批状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-fxgk-riskvalueList-search',
        tableKey: 'oilAudit-fxgk-riskvalueList-list',
        searchMore: true,
        select: [],
        closestatus: 0,
        createData: {},
        controlList: [],
        newRisk: false,
        createId: JSON.parse(localStorage.getItem('userInfo')).staffid,
      }
    },
    created() {
      console.log('this.$route', this.$route)
      if (this.$route.query.riskid) {
        this.queryForm.riskid = this.$route.query.riskid
        this.closestatus = this.$route.query.closestatus
        this.fetchData()
      }

      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
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
      // 月份数字转中文月份
      formatMonth(month) {
        if (!month) return ''
        const monthNum = parseInt(month)
        const chineseMonths = [
          '一月',
          '二月',
          '三月',
          '四月',
          '五月',
          '六月',
          '七月',
          '八月',
          '九月',
          '十月',
          '十一月',
          '十二月',
        ]
        return monthNum >= 1 && monthNum <= 12
          ? chineseMonths[monthNum - 1]
          : month
      },
      async handleApproval(row) {
        // if (this.createId != row.createStaffid) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        await monthlyEvaluationSubmit({ id: row.id })
        //提交审批
        this.$refs['process'].save(205, row.id)
      },
      async handleManage(row) {
        const res = await getFlowPkInfo({
          formId: row.id,
          tableId: 205,
        })

        this.$refs.wfqddeal.show(res.data, false)
      },
      getFiled() {
        return [{ name: '风险名称', key: 'impRiskName' }]
      },
      async fetchData() {
        this.formDisabled = this.$route.query.disabled
        this.listLoading = true
        queryMonthlyEvaluationList(this.queryForm)
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

      close() {
        this.$emit('close')
      },
      async handleEdit(row, disabled) {
        this.curRow = row ? JSON.parse(JSON.stringify(row)) : {}
        this.curRow.riskid = this.$route.query.riskid

        if (row && disabled) {
          this.createData = {}
          this.controlList = []
          this.dialogTitle = '详情'
          this.newRisk = false
          this.curRow.disabled = true
        } else if (row && !disabled) {
          this.createData = {}
          this.controlList = []
          this.dialogTitle = '修改'
          this.newRisk = false
        } else {
          this.dialogTitle = '新增'
          this.getRiskInfo()
          this.newRisk = true
          // 从列表最后一项获取相关字段
          if (this.list && this.list.length > 0) {
            const firstItem = this.list[this.list.length - 1]
            this.curRow.analysisRel = firstItem.analysisRel
            this.curRow.analysisEve = firstItem.analysisEve
            this.curRow.assessStan = firstItem.assessStan
            this.curRow.assessScoreOne = firstItem.assessScoreOne
            this.curRow.assessInf = firstItem.assessInf
            this.curRow.assessScoreTwo = firstItem.assessScoreTwo
            this.curRow.assessSco = firstItem.assessSco
            this.curRow.assessGrade = firstItem.assessGrade
          }
        }
        // 调用风险分析详情接口
        this.valueEditVisable = true
      },
      //获取详情和一体化管控措施
      async getRiskInfo() {
        try {
          this.loading = true
          // 获取风险分析详情
          const { data } = await riskAnalysisDetail({
            riskid: this.$route.query.riskid,
          })
          this.createData = data

          // 获取一体化管控措施
          const res = await riPlanInfo({
            riskid: this.$route.query.riskid,
          })
          this.controlList = res.data?.controls || []
        } catch (error) {
          console.error('获取风险信息失败:', error)
          this.$message.error('获取风险信息失败')
        } finally {
          this.loading = false
        }
      },
      async handleDelete(row) {
        // if (this.createId != row.createStaffid) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          this.listLoading = true
          const res = await monthlyEvaluationADelete({ id: row.id })
          this.listLoading = false
          if (res && res.code == 1) {
            this.$message.success('操作成功！')
            this.fetchData()
          } else {
            this.$message.error(res.msg || '操作失败！')
          }
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
      //导出
      async handleExport() {
        const ids = this.select.map((res) => res.id)
        const data = await exportPGList({
          id: ids.toString(),
          ...this.queryForm,
        })
        let fileName = '单位月度风险评估表'
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
