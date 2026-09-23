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
                v-model="queryForm.assessmentNo"
                clearable
                placeholder="评估编号"
                v-if="item.name === '评估编号'"
              />
              <el-input
                v-model="queryForm.assessmentName"
                clearable
                placeholder="评估名称"
                v-if="item.name === '评估名称'"
              />
              <el-select
                v-model="queryForm.assessmentType"
                clearable
                placeholder="评估类型"
                v-if="item.name === '评估类型'"
              >
                <el-option label="承接前" :value="1" />
                <el-option label="执行中" :value="2" />
                <el-option label="结项后" :value="3" />
              </el-select>
              <el-select
                v-model="queryForm.assessmentStatus"
                clearable
                placeholder="评估状态"
                v-if="item.name === '评估状态'"
              >
                <el-option label="待评估" :value="1" />
                <el-option label="评估中" :value="2" />
                <el-option label="已完成" :value="3" />
              </el-select>
              <el-select
                v-model="queryForm.riskLevel"
                clearable
                placeholder="风险等级"
                v-if="item.name === '风险等级'"
              >
                <el-option label="低" :value="1" />
                <el-option label="中" :value="2" />
                <el-option label="高" :value="3" />
                <el-option label="极高" :value="4" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button
                icon="el-icon-search"
                native-type="submit"
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
            <el-button
              slot="reference"
              icon="el-icon-s-grid"
              class="biaoge"
              style="margin-bottom: 10px; margin-right: 10px"
            ></el-button>
          </el-popover>
        </el-tooltip>
        <el-button type="success" @click="handleAdd">新建评估</el-button>
        <el-button
          type="danger"
          icon="el-icon-delete"
          @click="handleBatchDelete"
          :disabled="multipleSelection.length === 0"
        >
          批量删除
        </el-button>
        <el-button type="warning" icon="el-icon-warning" @click="showRiskWarning">
          风险预警
        </el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <template v-for="(item, index) in filedNow">
          <el-table-column
            :key="`assessmentNo-${index}`"
            align="center"
            label="评估编号"
            prop="assessmentNo"
            width="150"
            v-if="item.name === '评估编号'"
          />
          <el-table-column
            :key="`assessmentName-${index}`"
            align="center"
            label="评估名称"
            prop="assessmentName"
            min-width="200"
            v-if="item.name === '评估名称'"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleDetail(row)">
                {{ row.assessmentName }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            :key="`counterpartName-${index}`"
            align="center"
            label="相对方"
            prop="counterpartName"
            min-width="180"
            v-if="item.name === '相对方'"
          />
          <el-table-column
            :key="`assessmentType-${index}`"
            align="center"
            label="评估类型"
            prop="assessmentType"
            width="100"
            v-if="item.name === '评估类型'"
          >
            <template #default="{ row }">
              <el-tag :type="getAssessmentTypeType(row.assessmentType)">
                {{ getAssessmentTypeName(row.assessmentType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            :key="`riskLevel-${index}`"
            align="center"
            label="风险等级"
            prop="riskLevel"
            width="100"
            v-if="item.name === '风险等级'"
          >
            <template #default="{ row }">
              <el-tag :type="getRiskLevelType(row.riskLevel)">
                {{ getRiskLevelName(row.riskLevel) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            :key="`totalScore-${index}`"
            align="center"
            label="总评分"
            prop="totalScore"
            width="100"
            v-if="item.name === '总评分'"
          />
          <el-table-column
            :key="`assessmentStatus-${index}`"
            align="center"
            label="评估状态"
            prop="assessmentStatus"
            width="100"
            v-if="item.name === '评估状态'"
          >
            <template #default="{ row }">
              <el-tag :type="getAssessmentStatusType(row.assessmentStatus)">
                {{ getAssessmentStatusName(row.assessmentStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            :key="`assessorName-${index}`"
            align="center"
            label="评估人"
            prop="assessorName"
            width="100"
            v-if="item.name === '评估人'"
          />
          <el-table-column
            :key="`assessmentDate-${index}`"
            align="center"
            label="评估日期"
            prop="assessmentDate"
            width="150"
            :formatter="formatDate"
            v-if="item.name === '评估日期'"
          />
        </template>
        <el-table-column align="center" label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="text" size="small" @click="handleDetail(row)">详情</el-button>
            <el-button type="text" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button
              type="text"
              size="small"
              @click="handleApprove(row)"
              v-if="row.assessmentStatus === 2"
            >
              审批
            </el-button>
            <el-button type="text" size="small" style="color: #f56c6c;" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-pagination
      background
      class="pagination"
      :current-page="queryForm.pageNum"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <RiskAssessmentEdit ref="edit" @fetch-data="fetchData" />

    <!-- 风险预警管理对话框 -->
    <el-dialog
      title="风险预警管理"
      :visible.sync="riskWarningVisible"
      width="95%"
      :close-on-click-modal="false"
      custom-class="risk-warning-dialog"
    >
      <RiskWarningManagement ref="riskWarningManagement" />
    </el-dialog>
  </div>
</template>

<script>
  import {
    getRiskAssessmentList,
    getRiskAssessmentById,
    approveRiskAssessment,
    deleteRiskAssessment,
    batchDeleteRiskAssessment
  } from '@/api/contract/riskAssessment'
  import RiskAssessmentEdit from './components/RiskAssessmentEdit'
  import RiskWarningManagement from './components/RiskWarningManagement'
  import { formatDate } from '@/utils/index'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'

  export default {
    name: 'RiskAssessment',
    components: { RiskAssessmentEdit, RiskWarningManagement, filterTable, filterSearch },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          assessmentNo: '',
          assessmentName: '',
          assessmentType: null,
          assessmentStatus: null,
          riskLevel: null,
          pageNum: 1,
          pageSize: 20,
        },
        multipleSelection: [], // 多选数据
        riskWarningVisible: false, // 风险预警对话框显示状态

        localKey: 'contract-risk-assessment-search',
        tableKey: 'contract-risk-assessment-list',
        searchNow: [],
        searchMore: true,
        searchItem: [],
        searchAll: this.getFiled(), //所有搜索项
        filedNow: [],
        filedAll: [
          { name: '评估编号' },
          { name: '评估名称' },
          { name: '相对方' },
          { name: '评估类型' },
          { name: '风险等级' },
          { name: '总评分' },
          { name: '评估状态' },
          { name: '评估人' },
          { name: '评估日期' },
        ],
      }
    },
    created() {
      //初始化表格&筛选
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initTable()
      this.initSearch()
      // 在表格初始化完成后再获取数据
      this.fetchData()
    },
    methods: {
      // 动态筛选 动态表格 初始化数据&相关方法
      showMore() {
        this.searchMore = !this.searchMore

        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
      },
      getFiled() {
        let fields = [
          { name: '评估编号', key: 'assessmentNo' },
          { name: '评估名称', key: 'assessmentName' },
          { name: '评估类型', key: 'assessmentType' },
          { name: '评估状态', key: 'assessmentStatus' },
          { name: '风险等级', key: 'riskLevel' },
        ]
        return fields
      },
      initSearch() {
        let self = this
        this.$nextTick(function () {
          let data = localStorage.getItem(self.localKey)
          if (data) {
            data = JSON.parse(data)
            let tempArr = []
            for (let i = 0; i < data.length; i++) {
              if (data[i].show) {
                tempArr.push(data[i])
              }
            }
            this.searchNow = tempArr
          } else {
            this.searchNow = this.searchAll
          }
          // 重置非展示搜索项
          this.searchAll.forEach((x) => {
            if (!this.searchNow.some((y) => y.key === x.key)) {
              if (Array.isArray(this.queryForm[x.key])) {
                this.queryForm[x.key] = []
              } else if (this.queryForm[x.key] instanceof Object) {
                this.queryForm[x.key] = {}
              } else {
                this.queryForm[x.key] = null
              }
            }
          })

          if (this.searchMore) {
            this.searchItem = this.searchNow
          } else {
            this.searchItem = this.searchNow.slice(0, 4)
          }
        })
      },
      // 动态表格开始
      initTable() {
        let self = this
        this.$nextTick(function () {
          let data = localStorage.getItem(self.tableKey)

          if (data) {
            data = JSON.parse(data)
            let tempArr = []
            for (let i = 0; i < data.length; i++) {
              if (data[i].show) {
                tempArr.push(data[i])
              }
            }
            self.filedNow = tempArr
          } else {
            // 参考项目信息登记模块，直接使用 filedAll
            self.filedNow = self.filedAll
          }
        })
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
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
        this.queryForm.pageNum = val
        this.fetchData()
      },
      resetQueryForm() {
        this.queryForm = {
          assessmentNo: '',
          assessmentName: '',
          assessmentType: null,
          assessmentStatus: null,
          riskLevel: null,
          pageNum: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNum = 1
        this.fetchData()
      },
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        try {
          const response = await getRiskAssessmentList(this.queryForm)
          console.log('风险评估列表接口返回数据:', response)

          // 参考项目信息登记模块的处理方式
          if (response.code === 1) {
            this.list = response.data.list || []
            this.total = response.data.total || 0
            console.log('风险评估列表数据:', this.list.length, '条记录')
          } else {
            this.$message.error(response.msg || '查询失败')
            this.list = []
            this.total = 0
          }
        } catch (error) {
          console.error('获取风险评估列表失败:', error)
          this.$message.error('获取数据失败：' + error.message)
          this.list = []
          this.total = 0
        }
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit('add', null)
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选择的行数据
       * @return {*}
       */
      async handleDetail(row) {
        try {
          const response = await getRiskAssessmentById(row.id)
          console.log('风险评估详情接口返回数据:', response)

          // 检查多种成功状态码：200, 0, '200', '0', '1', 1, 2
          const successCodes = [200, 0, '200', '0', '1', 1, 2]
          if (successCodes.includes(response.code)) {
            await this.$refs['edit'].showEdit('detail', response.data)
          } else {
            this.$message.error(response.msg || response.message || '获取详情失败')
          }
        } catch (error) {
          this.$message.error('获取详情失败：' + error.message)
        }
      },
      async handleEdit(row) {
        try {
          const response = await getRiskAssessmentById(row.id)
          console.log('风险评估编辑接口返回数据:', response)

          // 检查多种成功状态码：200, 0, '200', '0', '1', 1, 2
          const successCodes = [200, 0, '200', '0', '1', 1, 2]
          if (successCodes.includes(response.code)) {
            await this.$refs['edit'].showEdit('edit', response.data)
          } else {
            this.$message.error(response.msg || response.message || '获取详情失败')
          }
        } catch (error) {
          this.$message.error('获取详情失败：' + error.message)
        }
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          try {
            const response = await deleteRiskAssessment(row.id)
            console.log('删除风险评估接口返回数据:', response)

            // 检查多种成功状态码：200, 0, '200', '0', '1', 1, 2
            const successCodes = [200, 0, '200', '0', '1', 1, 2]
            if (successCodes.includes(response.code)) {
              this.$baseMessage('删除成功', 'success')
              // 刷新列表
              this.fetchData()
            } else {
              this.$baseMessage(response.msg || response.message || '删除失败', 'error')
            }
          } catch (error) {
            this.$baseMessage('删除失败：' + error.message, 'error')
          }
        })
      },
      // 多选处理
      handleSelectionChange(selection) {
        this.multipleSelection = selection
      },
      // 批量删除处理
      handleBatchDelete() {
        if (this.multipleSelection.length === 0) {
          this.$baseMessage('请选择要删除的数据', 'warning')
          return
        }

        const ids = this.multipleSelection.map(item => item.id)
        const count = this.multipleSelection.length

        this.$baseConfirm(`你确定要删除选中的 ${count} 项数据吗？`, null, async () => {
          try {
            const response = await batchDeleteRiskAssessment(ids)
            console.log('批量删除风险评估接口返回数据:', response)

            // 检查多种成功状态码：200, 0, '200', '0', '1', 1, 2
            const successCodes = [200, 0, '200', '0', '1', 1, 2]
            if (successCodes.includes(response.code)) {
              this.$baseMessage(`成功删除 ${count} 项数据`, 'success')
              // 刷新列表
              this.fetchData()
              // 清空选择
              this.multipleSelection = []
            } else {
              this.$baseMessage(response.msg || response.message || '批量删除失败', 'error')
            }
          } catch (error) {
            this.$baseMessage('批量删除失败：' + error.message, 'error')
          }
        })
      },
      // 审批处理
      handleApprove(row) {
        this.$baseConfirm('确认审批通过此风险评估吗？', null, async () => {
          try {
            const response = await approveRiskAssessment({
              id: row.id,
              approvalStatus: 2,
              approvalComments: '审批通过'
            })
            console.log('风险评估审批响应:', response)

            // 检查多种成功状态码：200, 0, '200', '0', '1', 1, 2
            const successCodes = [200, 0, '200', '0', '1', 1, 2]
            if (successCodes.includes(response.code)) {
              this.$baseMessage('审批成功', 'success')
              await this.fetchData()
            } else {
              this.$baseMessage(response.message || response.msg || '审批失败', 'error')
            }
          } catch (error) {
            console.error('风险评估审批错误:', error)
            this.$baseMessage('审批失败：' + error.message, 'error')
          }
        })
      },
      // 获取评估类型名称
      getAssessmentTypeName(type) {
        const typeMap = {
          1: '承接前',
          2: '执行中',
          3: '结项后'
        }
        return typeMap[type] || '未知'
      },
      // 获取评估类型样式
      getAssessmentTypeType(type) {
        const typeMap = {
          1: 'info',
          2: 'warning',
          3: 'success'
        }
        return typeMap[type] || 'info'
      },
      // 获取风险等级名称
      getRiskLevelName(level) {
        const levelMap = {
          1: '低',
          2: '中',
          3: '高',
          4: '极高'
        }
        return levelMap[level] || '未知'
      },
      // 获取风险等级样式
      getRiskLevelType(level) {
        const typeMap = {
          1: 'success',
          2: 'warning',
          3: 'danger',
          4: 'danger'
        }
        return typeMap[level] || 'info'
      },
      // 获取评估状态名称
      getAssessmentStatusName(status) {
        const statusMap = {
          1: '待评估',
          2: '评估中',
          3: '已完成'
        }
        return statusMap[status] || '未知'
      },
      // 获取评估状态样式
      getAssessmentStatusType(status) {
        const typeMap = {
          1: 'info',
          2: 'warning',
          3: 'success'
        }
        return typeMap[status] || 'info'
      },
      // 显示风险预警管理
      showRiskWarning() {
        this.riskWarningVisible = true
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

<style>
  .risk-warning-dialog .el-dialog__body {
    padding: 10px 20px;
  }

  .risk-warning-dialog .el-dialog {
    margin-top: 5vh !important;
  }
</style>
