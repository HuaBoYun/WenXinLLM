<template>
  <div class="data-validation-container">
    <!-- 页面标题 -->
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span class="title-text">数据验证</span>
      </div>

      <!-- 查询表单 -->
      <el-form :model="queryForm" label-width="100px" class="query-form">
        <el-row :gutter="20">
          <el-col :lg="6" :md="12" :sm="24">
            <el-form-item label="采集任务ID">
              <el-input v-model="queryForm.collectionTaskId" placeholder="请输入采集任务ID" />
            </el-form-item>
          </el-col>
          <el-col :lg="6" :md="12" :sm="24">
            <el-form-item label="验证类型">
              <el-select v-model="queryForm.validationType" placeholder="请选择验证类型">
                <el-option label="全部" value="" />
                <el-option label="完整性验证" value="COMPLETENESS" />
                <el-option label="一致性验证" value="CONSISTENCY" />
                <el-option label="准确性验证" value="ACCURACY" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :lg="12" :md="24" :sm="24">
            <el-form-item>
              <el-button type="primary" @click="handleQuery">查询</el-button>
              <el-button @click="handleReset">重置</el-button>
              <el-button type="success" @click="handleCreate">新建验证任务</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <!-- 验证报告列表 -->
      <el-table
        :data="tableData"
        style="width: 100%"
        :loading="loading"
        stripe
        border
        :default-sort="{ prop: 'createTime', order: 'descending' }"
      >
        <el-table-column prop="reportId" label="报告ID" width="120" align="center" />

        <el-table-column prop="collectionTaskId" label="采集任务ID" width="130" align="center" />

        <el-table-column prop="validationType" label="验证类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag type="info">{{ getValidationTypeText(scope.row.validationType) }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="status" label="验证状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)" effect="dark">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="totalRecords" label="总记录数" width="110" align="right">
          <template slot-scope="scope">
            <span style="font-weight: 500; color: #409EFF;">{{ scope.row.totalRecords | formatNumber }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="passRecords" label="通过记录数" width="120" align="right">
          <template slot-scope="scope">
            <span style="font-weight: 500; color: #67C23A;">{{ scope.row.passRecords | formatNumber }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="failRecords" label="失败记录数" width="120" align="right">
          <template slot-scope="scope">
            <span style="font-weight: 500; color: #F56C6C;">{{ scope.row.failRecords | formatNumber }}</span>
          </template>
        </el-table-column>

        <el-table-column label="通过率" width="100" align="center">
          <template slot-scope="scope">
            <el-progress
              :percentage="Math.round((scope.row.passRecords / scope.row.totalRecords) * 100)"
              :color="getPassRateColor(scope.row.passRecords, scope.row.totalRecords)"
              style="margin: 0;"
            />
          </template>
        </el-table-column>

        <el-table-column prop="createTime" label="创建时间" width="160" align="center" sortable />

        <el-table-column label="操作" width="180" fixed="right" align="center">
          <template slot-scope="scope">
            <div style="display: flex; gap: 4px; justify-content: center; flex-wrap: wrap;">
              <el-button
                size="mini"
                type="primary"
                @click="handleViewReport(scope.row)"
              >
                查看报告
              </el-button>
              <el-button
                size="mini"
                type="danger"
                plain
                @click="handleDelete(scope.row)"
              >
                删除
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        :current-page="pageNumber"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handlePageSizeChange"
        @current-change="handlePageChange"
        style="margin-top: 20px; text-align: right"
      />
    </el-card>

    <!-- 新建验证任务对话框 -->
    <el-dialog
      title="新建验证任务"
      :visible.sync="createDialogVisible"
      width="600px"
      @close="handleCreateDialogClose"
    >
      <el-form :model="createForm" label-width="100px" :rules="createFormRules" ref="createFormRef">
        <el-form-item label="采集任务ID" prop="collectionTaskId">
          <el-input v-model="createForm.collectionTaskId" placeholder="请输入采集任务ID" />
        </el-form-item>
        <el-form-item label="验证类型" prop="validationType">
          <el-select v-model="createForm.validationType" placeholder="请选择验证类型">
            <el-option label="完整性验证" value="COMPLETENESS" />
            <el-option label="一致性验证" value="CONSISTENCY" />
            <el-option label="准确性验证" value="ACCURACY" />
          </el-select>
        </el-form-item>
        <el-form-item label="验证规则">
          <el-input v-model="createForm.validationRule" type="textarea" rows="3" placeholder="请输入验证规则" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="createForm.remark" type="textarea" rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreateSubmit">确定</el-button>
      </span>
    </el-dialog>

    <!-- 验证报告详情对话框 -->
    <el-dialog
      title="验证报告详情"
      :visible.sync="reportDialogVisible"
      width="800px"
    >
      <el-descriptions :column="2" border v-if="reportData">
        <el-descriptions-item label="报告ID">{{ reportData.reportId }}</el-descriptions-item>
        <el-descriptions-item label="采集任务ID">{{ reportData.collectionTaskId }}</el-descriptions-item>
        <el-descriptions-item label="验证类型">
          {{ getValidationTypeText(reportData.validationType) }}
        </el-descriptions-item>
        <el-descriptions-item label="验证状态">
          <el-tag :type="getStatusType(reportData.status)">
            {{ getStatusText(reportData.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="总记录数">{{ reportData.totalRecords }}</el-descriptions-item>
        <el-descriptions-item label="通过记录数">{{ reportData.passRecords }}</el-descriptions-item>
        <el-descriptions-item label="失败记录数">{{ reportData.failRecords }}</el-descriptions-item>
        <el-descriptions-item label="通过率">
          {{ ((reportData.passRecords / reportData.totalRecords) * 100).toFixed(2) }}%
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ reportData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="完成时间">{{ reportData.completeTime || '进行中' }}</el-descriptions-item>
        <el-descriptions-item label="验证规则" :span="2">
          {{ reportData.validationRule }}
        </el-descriptions-item>
        <el-descriptions-item label="验证结果" :span="2">
          {{ reportData.validationResult }}
        </el-descriptions-item>
      </el-descriptions>
      <span slot="footer" class="dialog-footer">
        <el-button @click="reportDialogVisible = false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getValidationReportList, startValidation, deleteValidationReport, getValidationReport } from '@/api/finance/dataValidation'

export default {
  name: 'DataValidation',
  filters: {
    formatNumber(value) {
      if (!value) return '0'
      return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
    }
  },
  data() {
    return {
      queryForm: {
        collectionTaskId: '',
        validationType: ''
      },
      tableData: [],
      loading: false,
      pageNumber: 1,
      pageSize: 20,
      total: 0,
      createDialogVisible: false,
      reportDialogVisible: false,
      reportData: null,
      createForm: {
        collectionTaskId: '',
        validationType: 'COMPLETENESS',
        validationRule: '',
        remark: ''
      },
      createFormRules: {
        collectionTaskId: [
          { required: true, message: '请输入采集任务ID', trigger: 'blur' }
        ],
        validationType: [
          { required: true, message: '请选择验证类型', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.handleQuery()
  },
  methods: {
    handleQuery() {
      this.pageNumber = 1
      this.loadData()
    },
    handleReset() {
      this.queryForm = {
        collectionTaskId: '',
        validationType: ''
      }
      this.pageNumber = 1
      this.loadData()
    },
    loadData() {
      this.loading = true
      getValidationReportList(this.pageNumber, this.pageSize).then(res => {
        if (res.code === 1) {
          this.tableData = res.data.list || []
          this.total = res.data.total || 0
        } else {
          this.$message.error(res.msg || '加载数据失败')
          // 接口失败时使用 mock 数据
          this.loadMockData()
        }
      }).catch(err => {
        this.$message.warning('接口暂不可用，已加载示例数据')
        console.error(err)
        // 接口异常时使用 mock 数据
        this.loadMockData()
      }).finally(() => {
        this.loading = false
      })
    },
    loadMockData() {
      // Mock 数据 - 当后端接口报404或异常时使用
      const mockDataList = [
        {
          reportId: 'REPORT001',
          collectionTaskId: 'TASK001',
          validationType: 'COMPLETENESS',
          status: 'SUCCESS',
          totalRecords: 15000,
          passRecords: 14850,
          failRecords: 150,
          createTime: '2025-01-21 10:30:00',
          completeTime: '2025-01-21 10:45:00',
          validationRule: 'SELECT COUNT(*) FROM SOURCE WHERE ID IS NOT NULL',
          validationResult: '数据完整性验证通过，缺失记录数为150条'
        },
        {
          reportId: 'REPORT002',
          collectionTaskId: 'TASK002',
          validationType: 'CONSISTENCY',
          status: 'SUCCESS',
          totalRecords: 8500,
          passRecords: 8500,
          failRecords: 0,
          createTime: '2025-01-20 14:00:00',
          completeTime: '2025-01-20 14:20:00',
          validationRule: 'SELECT * FROM SOURCE WHERE AMOUNT = CALC_AMOUNT',
          validationResult: '数据一致性验证通过，所有记录数据一致'
        },
        {
          reportId: 'REPORT003',
          collectionTaskId: 'TASK003',
          validationType: 'ACCURACY',
          status: 'FAILED',
          totalRecords: 5200,
          passRecords: 4950,
          failRecords: 250,
          createTime: '2025-01-21 09:15:00',
          completeTime: '2025-01-21 09:35:00',
          validationRule: 'SELECT * FROM SOURCE WHERE AMOUNT > 0 AND AMOUNT < 1000000',
          validationResult: '数据准确性验证失败，发现250条异常数据'
        },
        {
          reportId: 'REPORT004',
          collectionTaskId: 'TASK004',
          validationType: 'COMPLETENESS',
          status: 'RUNNING',
          totalRecords: 3200,
          passRecords: 2100,
          failRecords: 0,
          createTime: '2025-01-21 11:00:00',
          completeTime: null,
          validationRule: 'SELECT COUNT(*) FROM SOURCE WHERE STATUS IN ("ACTIVE", "PENDING")',
          validationResult: '验证进行中，已验证2100条记录'
        },
        {
          reportId: 'REPORT005',
          collectionTaskId: 'TASK005',
          validationType: 'CONSISTENCY',
          status: 'SUCCESS',
          totalRecords: 12000,
          passRecords: 11980,
          failRecords: 20,
          createTime: '2025-01-18 08:00:00',
          completeTime: '2025-01-18 08:30:00',
          validationRule: 'SELECT * FROM SOURCE WHERE DEPT_ID IN (SELECT ID FROM DEPT)',
          validationResult: '数据一致性验证通过，发现20条部门ID不匹配'
        },
        {
          reportId: 'REPORT006',
          collectionTaskId: 'TASK006',
          validationType: 'ACCURACY',
          status: 'SUCCESS',
          totalRecords: 22000,
          passRecords: 21890,
          failRecords: 110,
          createTime: '2025-01-21 08:30:00',
          completeTime: '2025-01-21 09:00:00',
          validationRule: 'SELECT * FROM SOURCE WHERE PRICE > 0 AND QUANTITY > 0',
          validationResult: '数据准确性验证通过，发现110条价格或数量异常'
        },
        {
          reportId: 'REPORT007',
          collectionTaskId: 'TASK007',
          validationType: 'COMPLETENESS',
          status: 'SUCCESS',
          totalRecords: 4500,
          passRecords: 4500,
          failRecords: 0,
          createTime: '2025-01-17 15:00:00',
          completeTime: '2025-01-17 15:15:00',
          validationRule: 'SELECT COUNT(*) FROM SOURCE WHERE REQUIRED_FIELD IS NOT NULL',
          validationResult: '数据完整性验证通过，所有必填字段均已填写'
        },
        {
          reportId: 'REPORT008',
          collectionTaskId: 'TASK008',
          validationType: 'CONSISTENCY',
          status: 'SUCCESS',
          totalRecords: 18500,
          passRecords: 18450,
          failRecords: 50,
          createTime: '2025-01-16 10:00:00',
          completeTime: '2025-01-16 10:40:00',
          validationRule: 'SELECT * FROM SOURCE WHERE DEBIT_AMOUNT = CREDIT_AMOUNT',
          validationResult: '数据一致性验证通过，发现50条借贷不平衡记录'
        }
      ]

      // 根据分页参数返回对应的数据
      const startIndex = (this.pageNumber - 1) * this.pageSize
      const endIndex = startIndex + this.pageSize
      this.tableData = mockDataList.slice(startIndex, endIndex)
      this.total = mockDataList.length
    },
    handlePageChange(pageNumber) {
      this.pageNumber = pageNumber
      this.loadData()
    },
    handlePageSizeChange(pageSize) {
      this.pageSize = pageSize
      this.pageNumber = 1
      this.loadData()
    },
    handleCreate() {
      this.createForm = {
        collectionTaskId: '',
        validationType: 'COMPLETENESS',
        validationRule: '',
        remark: ''
      }
      this.createDialogVisible = true
    },
    handleCreateSubmit() {
      this.$refs.createFormRef.validate(valid => {
        if (valid) {
          startValidation(this.createForm).then(res => {
            if (res.code === 1) {
              this.$message.success('验证任务创建成功')
              this.createDialogVisible = false
              this.loadData()
            } else {
              this.$message.error(res.msg || '创建失败')
            }
          }).catch(err => {
            this.$message.error('创建失败')
            console.error(err)
          })
        }
      })
    },
    handleCreateDialogClose() {
      this.$refs.createFormRef.clearValidate()
    },
    handleViewReport(row) {
      getValidationReport(row.reportId).then(res => {
        if (res.code === 1) {
          this.reportData = res.data
          this.reportDialogVisible = true
        } else {
          this.$message.warning('报告接口暂不可用，已加载示例数据')
          // 接口失败时使用当前行数据作为报告
          this.reportData = row
          this.reportDialogVisible = true
        }
      }).catch(err => {
        this.$message.warning('报告接口暂不可用，已加载示例数据')
        console.error(err)
        // 接口异常时使用当前行数据作为报告
        this.reportData = row
        this.reportDialogVisible = true
      })
    },
    handleDelete(row) {
      this.$confirm('确定要删除该验证报告吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteValidationReport(row.reportId).then(res => {
          if (res.code === 1) {
            this.$message.success('删除成功')
            this.loadData()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        }).catch(err => {
          this.$message.error('删除失败')
          console.error(err)
        })
      }).catch(() => {})
    },
    getStatusType(status) {
      const statusMap = {
        'SUCCESS': 'success',
        'FAILED': 'danger',
        'RUNNING': 'warning'
      }
      return statusMap[status] || 'info'
    },
    getStatusText(status) {
      const statusMap = {
        'SUCCESS': '成功',
        'FAILED': '失败',
        'RUNNING': '进行中'
      }
      return statusMap[status] || status
    },
    getValidationTypeText(type) {
      const typeMap = {
        'COMPLETENESS': '完整性验证',
        'CONSISTENCY': '一致性验证',
        'ACCURACY': '准确性验证'
      }
      return typeMap[type] || type
    },
    getPassRateColor(passRecords, totalRecords) {
      const rate = (passRecords / totalRecords) * 100
      if (rate >= 95) {
        return '#67C23A'
      } else if (rate >= 80) {
        return '#409EFF'
      } else if (rate >= 60) {
        return '#E6A23C'
      } else {
        return '#F56C6C'
      }
    }
  }
}
</script>

<style scoped>
.data-validation-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.box-card {
  margin-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 4px;
}

.title-text {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
  letter-spacing: 0.5px;
}

.query-form {
  margin-bottom: 20px;
  padding: 10px 0;
}

.clearfix:after {
  content: "";
  display: table;
  clear: both;
}

/* 表格样式优化 */
/deep/ .el-table {
  background-color: #fff;
  border-radius: 4px;
  overflow: hidden;
}

/deep/ .el-table__header {
  background-color: #f5f7fa;
}

/deep/ .el-table__header th {
  background-color: #f5f7fa;
  color: #303133;
  font-weight: 600;
  border-bottom: 2px solid #dfe6e9;
}

/deep/ .el-table__body tr:hover > td {
  background-color: #f0f9ff !important;
}

/deep/ .el-table__body td {
  padding: 12px 0;
  border-bottom: 1px solid #ebeef5;
}

/deep/ .el-table__row {
  height: 50px;
}

/deep/ .el-progress {
  width: 100%;
}

/deep/ .el-button--mini {
  padding: 5px 10px;
  font-size: 12px;
}

/deep/ .el-tag {
  padding: 4px 12px;
  border-radius: 3px;
  font-weight: 500;
}

/* 分页样式 */
/deep/ .el-pagination {
  text-align: right;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
}
</style>

