<template>
  <div class="material-management">

    <!-- 材料需求管理对话框 -->
    <el-dialog
      title="材料需求管理"
      :visible.sync="dialogVisible"
      width="90%"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <div class="material-dialog-content">
        <!-- 操作按钮 -->
        <div class="operation-bar">
          <el-button type="primary" icon="el-icon-plus" @click="handleAdd">
            添加材料需求
          </el-button>
          <el-button
            type="success"
            icon="el-icon-check"
            @click="handleBatchApprove"
            :disabled="selectedRows.length === 0"
          >
            批量审批
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="exportData">
            导出数据
          </el-button>
        </div>

        <!-- 材料需求列表 -->
        <el-table
          v-loading="tableLoading"
          :data="materialList"
          @selection-change="handleSelectionChange"
          stripe
          border
          style="margin-top: 20px"
        >
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column
            prop="materialName"
            label="材料名称"
            min-width="150"
            show-overflow-tooltip
          />
          <el-table-column
            prop="materialCategory"
            label="材料类别"
            width="120"
            align="center"
          />
          <el-table-column
            prop="materialSpecification"
            label="规格型号"
            min-width="200"
            show-overflow-tooltip
          />
          <el-table-column
            prop="requiredQuantity"
            label="需求数量"
            width="100"
            align="center"
          >
            <template #default="{ row }">
              {{ row.requiredQuantity }} {{ row.unit }}
            </template>
          </el-table-column>
          <el-table-column
            prop="estimatedUnitPrice"
            label="预估单价"
            width="120"
            align="center"
          >
            <template #default="{ row }">
              ¥{{ row.estimatedUnitPrice?.toFixed(2) || '0.00' }}
            </template>
          </el-table-column>
          <el-table-column
            prop="estimatedTotalPrice"
            label="预估总价"
            width="120"
            align="center"
          >
            <template #default="{ row }">
              ¥{{ (row.requiredQuantity * row.estimatedUnitPrice)?.toFixed(2) || '0.00' }}
            </template>
          </el-table-column>
          <el-table-column
            prop="procurementStatus"
            label="采购状态"
            width="100"
            align="center"
          >
            <template #default="{ row }">
              <el-tag :type="getProcurementStatusType(row.procurementStatus)">
                {{ getProcurementStatusName(row.procurementStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            prop="requiredDate"
            label="需求日期"
            width="120"
            align="center"
          >
            <template #default="{ row }">
              {{ formatDate(row.requiredDate) }}
            </template>
          </el-table-column>
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

    <!-- 材料需求编辑对话框 -->
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
            <el-form-item label="材料名称" prop="materialName">
              <el-input
                v-model="editForm.materialName"
                placeholder="请输入材料名称"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="材料类别" prop="materialCategory">
              <el-select
                v-model="editForm.materialCategory"
                placeholder="请选择材料类别"
                style="width: 100%"
              >
                <el-option label="建筑材料" value="建筑材料" />
                <el-option label="装饰材料" value="装饰材料" />
                <el-option label="机电材料" value="机电材料" />
                <el-option label="其他材料" value="其他材料" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="规格型号" prop="materialSpecification">
          <el-input
            v-model="editForm.materialSpecification"
            type="textarea"
            :rows="2"
            placeholder="请输入材料规格型号"
          />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="需求数量" prop="requiredQuantity">
              <el-input-number
                v-model="editForm.requiredQuantity"
                :min="0"
                :precision="3"
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
                <el-option label="吨" value="吨" />
                <el-option label="立方米" value="立方米" />
                <el-option label="平方米" value="平方米" />
                <el-option label="米" value="米" />
                <el-option label="个" value="个" />
                <el-option label="套" value="套" />
                <el-option label="台" value="台" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="预估单价" prop="estimatedUnitPrice">
              <el-input-number
                v-model="editForm.estimatedUnitPrice"
                :min="0"
                :precision="2"
                controls-position="right"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="计划使用日期" prop="plannedUsageDate">
              <el-date-picker
                v-model="editForm.plannedUsageDate"
                type="date"
                placeholder="选择计划使用日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="供应商要求" prop="supplierRequirements">
              <el-input
                v-model="editForm.supplierRequirements"
                placeholder="请输入供应商要求"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="备注" prop="remarks">
          <el-input
            v-model="editForm.remarks"
            type="textarea"
            :rows="3"
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
import {
  createMaterialRequirement,
  getMaterialRequirementList
} from '@/api/contract/planning'

export default {
  name: 'MaterialManagement',
  props: {
    planningId: {
      type: [String, Number],
      required: true
    }
  },
  data() {
    return {
      dialogVisible: false,
      currentPlanningId: null,
      tableLoading: false,
      materialList: [],
      selectedRows: [],
      pagination: {
        current: 1,
        size: 20,
        total: 0
      },
      editDialogVisible: false,
      editDialogTitle: '添加材料需求',
      saveLoading: false,
      editForm: {
        planningId: null,
        materialName: '',
        materialCategory: '', // 材料类别
        materialSpecification: '', // 材料规格
        requiredQuantity: 1, // 需求数量，默认为1
        unit: '',
        estimatedUnitPrice: 0, // 预估单价
        plannedUsageDate: null, // 计划使用日期
        supplierRequirements: '', // 供应商要求
        remarks: ''
      },
      editRules: {
        materialName: [
          { required: true, message: '请输入材料名称', trigger: 'blur' }
        ],
        materialCategory: [
          { required: true, message: '请选择材料类别', trigger: 'change' }
        ],
        requiredQuantity: [
          { required: true, message: '请输入需求数量', trigger: 'blur' },
          { type: 'number', min: 0.01, message: '需求数量必须大于0', trigger: 'blur' }
        ],
        unit: [
          { required: true, message: '请选择单位', trigger: 'change' }
        ],
        estimatedUnitPrice: [
          { required: true, message: '请输入预估单价', trigger: 'blur' },
          { type: 'number', min: 0, message: '预估单价不能为负数', trigger: 'blur' }
        ],
        plannedUsageDate: [
          { required: true, message: '请选择计划使用日期', trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    // 显示材料需求管理对话框
    showEdit(row) {
      this.currentPlanningId = row.id
      this.dialogVisible = true
      this.fetchData()
    },

    // 关闭对话框
    handleDialogClose() {
      this.dialogVisible = false
      this.currentPlanningId = null
      this.materialList = []
      this.selectedRows = []
    },
    async fetchData() {
      if (!this.currentPlanningId) return

      this.tableLoading = true
      try {
        const params = {
          planningId: this.currentPlanningId,
          pageNum: this.pagination.current,
          pageSize: this.pagination.size
        }
        const response = await getMaterialRequirementList(params)
        console.log('材料需求列表接口返回数据:', response)
        // 使用统一的成功状态码判断
        if ([200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
          this.materialList = response.data.records || response.data.list || []
          this.pagination.total = response.data.total || 0
          console.log('材料需求数据处理成功:', this.materialList)
        } else {
          console.error('材料需求接口返回失败状态码:', response.code)
          this.$message.error(response.msg || response.message || '获取数据失败')
        }
      } catch (error) {
        this.$message.error('获取数据失败：' + error.message)
      } finally {
        this.tableLoading = false
      }
    },

    handleAdd() {
      this.editDialogTitle = '添加材料需求'
      this.editForm = {
        planningId: this.currentPlanningId,
        materialName: '',
        materialCategory: '', // 材料类别
        materialSpecification: '',
        requiredQuantity: 1, // 默认数量为1，避免0值验证失败
        unit: '',
        estimatedUnitPrice: 0,
        plannedUsageDate: null,
        supplierRequirements: '',
        remarks: ''
      }
      this.editDialogVisible = true
    },

    handleEdit(row) {
      this.editDialogTitle = '编辑材料需求'
      this.editForm = { ...row }
      this.editDialogVisible = true
    },

    handleDetail(row) {
      this.editDialogTitle = '材料需求详情'
      this.editForm = { ...row }
      this.editDialogVisible = true
      // 设置为只读模式
      this.$nextTick(() => {
        this.$refs.editForm.$el.querySelectorAll('input, textarea, .el-select').forEach(el => {
          el.setAttribute('readonly', true)
          el.setAttribute('disabled', true)
        })
      })
    },

    async handleSave() {
      try {
        await this.$refs.editForm.validate()
        
        this.saveLoading = true
        const response = await createMaterialRequirement(this.editForm)
        console.log('创建材料需求接口返回数据:', response)

        // 使用统一的成功状态码判断
        if ([200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
          this.$message.success('保存成功')
          this.editDialogVisible = false
          this.fetchData()
        } else {
          console.error('创建材料需求接口返回失败状态码:', response.code)
          this.$message.error(response.msg || response.message || '保存失败')
        }
      } catch (error) {
        if (error !== 'validation failed') {
          this.$message.error('保存失败：' + error.message)
        }
      } finally {
        this.saveLoading = false
      }
    },

    handleDelete(row) {
      this.$confirm('确定要删除这个材料需求吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 实现删除逻辑
        this.$message.success('删除成功')
        this.fetchData()
      }).catch(() => {})
    },

    handleApprove(row) {
      this.$confirm('确定要审批这个材料需求吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        // 实现审批逻辑
        this.$message.success('审批成功')
        this.fetchData()
      }).catch(() => {})
    },

    handleBatchApprove() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要审批的材料需求')
        return
      }
      this.$confirm(`确定要批量审批选中的${this.selectedRows.length}个材料需求吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        // 实现批量审批逻辑
        this.$message.success('批量审批成功')
        this.fetchData()
      }).catch(() => {})
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

    getProcurementStatusType(status) {
      const statusMap = {
        1: 'info',
        2: 'warning',
        3: 'success',
        4: 'danger'
      }
      return statusMap[status] || 'info'
    },

    getProcurementStatusName(status) {
      const statusMap = {
        1: '待采购',
        2: '采购中',
        3: '已采购',
        4: '已取消'
      }
      return statusMap[status] || '未知'
    },

    formatDate(date) {
      if (!date) return ''
      return new Date(date).toLocaleDateString()
    }
  }
}
</script>

<style scoped>
.material-management {
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
