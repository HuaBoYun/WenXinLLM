<template>
  <div class="procurement-management">
    <!-- 对外采购计划管理对话框 -->
    <el-dialog
      title="对外采购计划"
      :visible.sync="dialogVisible"
      width="90%"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <div class="procurement-dialog-content">
        <!-- 操作按钮 -->
        <div class="operation-bar">
          <el-button type="primary" icon="el-icon-plus" @click="handleAdd">
            添加采购计划
          </el-button>
          <el-button
            type="success"
            icon="el-icon-check"
            @click="handleBatchApprove"
            :disabled="selectedRows.length === 0"
          >
            批量审批
          </el-button>
          <el-button type="warning" icon="el-icon-s-order" @click="generatePurchaseOrder">
            生成采购订单
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="exportData">
            导出数据
          </el-button>
        </div>

        <!-- 采购计划列表 -->
        <el-table
          v-loading="tableLoading"
          :data="procurementList"
          @selection-change="handleSelectionChange"
          stripe
          border
          style="margin-top: 20px"
        >
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column
            prop="procurementName"
            label="采购项目"
            min-width="200"
            show-overflow-tooltip
          />
          <el-table-column
            prop="procurementCategory"
            label="采购类别"
            width="120"
            align="center"
          >
            <template #default="{ row }">
              <el-tag :type="getCategoryColor(row.procurementCategory)">
                {{ getCategoryName(row.procurementCategory) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            prop="estimatedAmount"
            label="预估金额"
            width="120"
            align="center"
          >
            <template #default="{ row }">
              ¥{{ row.estimatedAmount?.toFixed(2) || '0.00' }}
            </template>
          </el-table-column>
          <el-table-column
            prop="plannedProcurementDate"
            label="计划采购时间"
            width="130"
            align="center"
          >
            <template #default="{ row }">
              {{ formatDate(row.plannedProcurementDate) }}
            </template>
          </el-table-column>
          <el-table-column
            prop="expectedDeliveryDate"
            label="预期交付时间"
            width="130"
            align="center"
          >
            <template #default="{ row }">
              {{ formatDate(row.expectedDeliveryDate) }}
            </template>
          </el-table-column>
          <el-table-column
            prop="supplierName"
            label="推荐供应商"
            width="150"
            show-overflow-tooltip
          />
          <el-table-column
            prop="procurementStatus"
            label="采购状态"
            width="100"
            align="center"
          >
            <template #default="{ row }">
              <el-tag :type="getStatusColor(row.procurementStatus)">
                {{ getStatusName(row.procurementStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            prop="responsiblePerson"
            label="负责人"
            width="100"
            align="center"
          />
          <el-table-column label="操作" width="200" align="center" fixed="right">
            <template #default="{ row }">
              <el-button
                type="text"
                size="small"
                icon="el-icon-view"
                @click="handleDetail(row)"
              >
                详情
              </el-button>
              <el-button
                type="text"
                size="small"
                icon="el-icon-edit"
                @click="handleEdit(row)"
              >
                编辑
              </el-button>
              <el-button
                v-if="row.procurementStatus === 1"
                type="text"
                size="small"
                icon="el-icon-check"
                @click="handleApprove(row)"
              >
                审批
              </el-button>
              <el-button
                type="text"
                size="small"
                icon="el-icon-delete"
                style="color: #f56c6c"
                @click="handleDelete(row)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <div class="pagination-container">
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="pagination.current"
            :page-sizes="[10, 20, 50, 100]"
            :page-size="pagination.size"
            layout="total, sizes, prev, pager, next, jumper"
            :total="pagination.total"
          />
        </div>
      </div>
    </el-dialog>

    <!-- 采购计划编辑对话框 -->
    <el-dialog
      :title="editDialogTitle"
      :visible.sync="editDialogVisible"
      width="60%"
      :close-on-click-modal="false"
    >
      <el-form
        ref="editForm"
        :model="editForm"
        :rules="editRules"
        label-width="120px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="采购项目" prop="procurementName">
              <el-input
                v-model="editForm.procurementName"
                placeholder="请输入采购项目名称"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="采购类别" prop="procurementCategory">
              <el-select
                v-model="editForm.procurementCategory"
                placeholder="请选择采购类别"
                style="width: 100%"
              >
                <el-option label="设备采购" :value="1" />
                <el-option label="材料采购" :value="2" />
                <el-option label="服务采购" :value="3" />
                <el-option label="外包服务" :value="4" />
                <el-option label="其他" :value="5" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="采购描述" prop="procurementDescription">
          <el-input
            v-model="editForm.procurementDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入采购项目详细描述"
          />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="预估金额" prop="estimatedAmount">
              <el-input-number
                v-model="editForm.estimatedAmount"
                :min="0"
                :precision="2"
                controls-position="right"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="数量" prop="quantity">
              <el-input-number
                v-model="editForm.quantity"
                :min="1"
                controls-position="right"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="单位" prop="unit">
              <el-select
                v-model="editForm.unit"
                placeholder="请选择单位"
                style="width: 100%"
              >
                <el-option label="台" value="台" />
                <el-option label="套" value="套" />
                <el-option label="个" value="个" />
                <el-option label="批" value="批" />
                <el-option label="项" value="项" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="计划采购时间" prop="plannedProcurementDate">
              <el-date-picker
                v-model="editForm.plannedProcurementDate"
                type="date"
                placeholder="选择计划采购时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预期交付时间" prop="expectedDeliveryDate">
              <el-date-picker
                v-model="editForm.expectedDeliveryDate"
                type="date"
                placeholder="选择预期交付时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="推荐供应商" prop="supplierName">
              <el-input
                v-model="editForm.supplierName"
                placeholder="请输入推荐供应商"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责人" prop="responsiblePerson">
              <el-select
                v-model="editForm.responsiblePerson"
                placeholder="请选择负责人"
                style="width: 100%"
                filterable
              >
                <el-option label="张三" value="张三" />
                <el-option label="李四" value="李四" />
                <el-option label="王五" value="王五" />
                <el-option label="赵六" value="赵六" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="采购方式" prop="procurementMethod">
              <el-select
                v-model="editForm.procurementMethod"
                placeholder="请选择采购方式"
                style="width: 100%"
              >
                <el-option label="公开招标" value="公开招标" />
                <el-option label="邀请招标" value="邀请招标" />
                <el-option label="竞争性谈判" value="竞争性谈判" />
                <el-option label="单一来源" value="单一来源" />
                <el-option label="询价采购" value="询价采购" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="紧急程度" prop="urgencyLevel">
              <el-select
                v-model="editForm.urgencyLevel"
                placeholder="请选择紧急程度"
                style="width: 100%"
              >
                <el-option label="紧急" :value="1" />
                <el-option label="一般" :value="2" />
                <el-option label="不急" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="技术要求" prop="technicalRequirements">
          <el-input
            v-model="editForm.technicalRequirements"
            type="textarea"
            :rows="3"
            placeholder="请输入技术要求和规格说明"
          />
        </el-form-item>

        <el-form-item label="备注" prop="remarks">
          <el-input
            v-model="editForm.remarks"
            type="textarea"
            :rows="2"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="saveLoading">
          保存
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'ProcurementManagement',
  data() {
    return {
      dialogVisible: false,
      currentPlanningId: null,
      tableLoading: false,
      procurementList: [],
      selectedRows: [],
      pagination: {
        current: 1,
        size: 20,
        total: 0
      },
      editDialogVisible: false,
      editDialogTitle: '添加采购计划',
      saveLoading: false,
      editForm: {
        planningId: null,
        procurementName: '',
        procurementCategory: null,
        procurementDescription: '',
        estimatedAmount: 0,
        quantity: 1,
        unit: '',
        plannedProcurementDate: null,
        expectedDeliveryDate: null,
        supplierName: '',
        responsiblePerson: '',
        procurementMethod: '',
        urgencyLevel: 2,
        technicalRequirements: '',
        remarks: ''
      },
      editRules: {
        procurementName: [
          { required: true, message: '请输入采购项目名称', trigger: 'blur' }
        ],
        procurementCategory: [
          { required: true, message: '请选择采购类别', trigger: 'change' }
        ],
        estimatedAmount: [
          { required: true, message: '请输入预估金额', trigger: 'blur' }
        ],
        plannedProcurementDate: [
          { required: true, message: '请选择计划采购时间', trigger: 'change' }
        ],
        expectedDeliveryDate: [
          { required: true, message: '请选择预期交付时间', trigger: 'change' }
        ],
        responsiblePerson: [
          { required: true, message: '请选择负责人', trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    // 显示对外采购计划管理对话框
    showEdit(row) {
      this.currentPlanningId = row.id
      this.dialogVisible = true
      this.fetchData()
    },

    // 关闭对话框
    handleDialogClose() {
      this.dialogVisible = false
      this.currentPlanningId = null
      this.procurementList = []
      this.selectedRows = []
    },

    async fetchData() {
      if (!this.currentPlanningId) return
      
      this.tableLoading = true
      try {
        // 模拟数据，实际应该调用API
        const mockData = [
          {
            id: 1,
            procurementName: '施工设备采购',
            procurementCategory: 1,
            estimatedAmount: 500000,
            plannedProcurementDate: '2025-02-01',
            expectedDeliveryDate: '2025-02-15',
            supplierName: '华建设备有限公司',
            procurementStatus: 1,
            responsiblePerson: '张三'
          },
          {
            id: 2,
            procurementName: '建筑材料采购',
            procurementCategory: 2,
            estimatedAmount: 300000,
            plannedProcurementDate: '2025-02-10',
            expectedDeliveryDate: '2025-02-20',
            supplierName: '建材供应商',
            procurementStatus: 2,
            responsiblePerson: '李四'
          }
        ]
        
        this.procurementList = mockData
        this.pagination.total = mockData.length
      } catch (error) {
        this.$message.error('获取数据失败：' + error.message)
      } finally {
        this.tableLoading = false
      }
    },

    handleAdd() {
      this.editDialogTitle = '添加采购计划'
      this.editForm = {
        planningId: this.currentPlanningId,
        procurementName: '',
        procurementCategory: null,
        procurementDescription: '',
        estimatedAmount: 0,
        quantity: 1,
        unit: '',
        plannedProcurementDate: null,
        expectedDeliveryDate: null,
        supplierName: '',
        responsiblePerson: '',
        procurementMethod: '',
        urgencyLevel: 2,
        technicalRequirements: '',
        remarks: ''
      }
      this.editDialogVisible = true
    },

    handleEdit(row) {
      this.editDialogTitle = '编辑采购计划'
      this.editForm = { ...row }
      this.editDialogVisible = true
    },

    handleDetail(row) {
      this.editDialogTitle = '采购计划详情'
      this.editForm = { ...row }
      this.editDialogVisible = true
    },

    async handleSave() {
      try {
        await this.$refs.editForm.validate()
        
        this.saveLoading = true
        // 这里应该调用API保存数据
        await new Promise(resolve => setTimeout(resolve, 1000))
        
        this.$message.success('保存成功')
        this.editDialogVisible = false
        this.fetchData()
      } catch (error) {
        if (error !== 'validation failed') {
          this.$message.error('保存失败：' + error.message)
        }
      } finally {
        this.saveLoading = false
      }
    },

    handleDelete(row) {
      this.$confirm('确定要删除这个采购计划吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
        this.fetchData()
      }).catch(() => {})
    },

    handleApprove(row) {
      this.$confirm('确定要审批这个采购计划吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        this.$message.success('审批成功')
        this.fetchData()
      }).catch(() => {})
    },

    handleBatchApprove() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要审批的采购计划')
        return
      }
      this.$confirm(`确定要批量审批选中的${this.selectedRows.length}个采购计划吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        this.$message.success('批量审批成功')
        this.fetchData()
      }).catch(() => {})
    },

    generatePurchaseOrder() {
      this.$message.info('生成采购订单功能开发中...')
    },

    exportData() {
      this.$message.info('导出功能开发中...')
    },

    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    handleSizeChange(val) {
      this.pagination.size = val
      this.pagination.current = 1
      this.fetchData()
    },

    handleCurrentChange(val) {
      this.pagination.current = val
      this.fetchData()
    },

    getCategoryColor(category) {
      const colorMap = {
        1: 'primary',
        2: 'success',
        3: 'warning',
        4: 'info',
        5: 'danger'
      }
      return colorMap[category] || 'info'
    },

    getCategoryName(category) {
      const nameMap = {
        1: '设备采购',
        2: '材料采购',
        3: '服务采购',
        4: '外包服务',
        5: '其他'
      }
      return nameMap[category] || '未知'
    },

    getStatusColor(status) {
      const colorMap = {
        1: 'info',
        2: 'warning',
        3: 'success',
        4: 'danger'
      }
      return colorMap[status] || 'info'
    },

    getStatusName(status) {
      const nameMap = {
        1: '待审批',
        2: '采购中',
        3: '已完成',
        4: '已取消'
      }
      return nameMap[status] || '未知'
    },

    formatDate(date) {
      if (!date) return ''
      return new Date(date).toLocaleDateString()
    }
  }
}
</script>

<style scoped>
.procurement-management {
  padding: 20px;
}

.operation-bar {
  margin-bottom: 20px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.dialog-footer {
  text-align: right;
}
</style>
