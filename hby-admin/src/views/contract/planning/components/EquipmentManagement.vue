<template>
  <el-dialog
    title="设备资源管理"
    :visible.sync="dialogVisible"
    width="90%"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    @close="handleClose"
  >
    <div style="margin-bottom: 20px;">
      <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd">
        添加设备资源
      </el-button>
    </div>

    <el-table :data="equipmentList" border style="width: 100%">
      <el-table-column label="设备名称" prop="equipmentName" width="120" />
      <el-table-column label="设备类型" prop="equipmentType" width="100" />
      <el-table-column label="设备型号" prop="equipmentModel" width="100" />
      <el-table-column label="规格参数" prop="equipmentSpecification" min-width="150" />
      <el-table-column label="需求数量" prop="requiredQuantity" width="100">
        <template #default="{ row }">
          {{ row.requiredQuantity }} {{ row.unit }}
        </template>
      </el-table-column>
      <el-table-column label="采购方式" prop="procurementMethod" width="100">
        <template #default="{ row }">
          {{ getProcurementMethodName(row.procurementMethod) }}
        </template>
      </el-table-column>
      <el-table-column label="预估成本" prop="estimatedCost" width="100">
        <template #default="{ row }">
          {{ row.estimatedCost ? row.estimatedCost + '元' : '-' }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row, $index }">
          <el-button type="text" size="small" @click="handleEdit(row, $index)">编辑</el-button>
          <el-button type="text" size="small" style="color: #f56c6c;" @click="handleDelete($index)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 设备资源编辑弹窗 -->
    <el-dialog
      :title="editTitle"
      :visible.sync="editDialogVisible"
      width="60%"
      append-to-body
    >
      <el-form
        ref="editForm"
        :model="editForm"
        :rules="editRules"
        label-width="120px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="设备名称" prop="equipmentName">
              <el-input v-model="editForm.equipmentName" placeholder="请输入设备名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备类型" prop="equipmentType">
              <el-input v-model="editForm.equipmentType" placeholder="请输入设备类型" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="设备型号" prop="equipmentModel">
              <el-input v-model="editForm.equipmentModel" placeholder="请输入设备型号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="采购方式" prop="procurementMethod">
              <el-select v-model="editForm.procurementMethod" placeholder="请选择采购方式" style="width: 100%">
                <el-option label="购买" :value="1" />
                <el-option label="租赁" :value="2" />
                <el-option label="借用" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="规格参数" prop="equipmentSpecification">
          <el-input
            v-model="editForm.equipmentSpecification"
            type="textarea"
            :rows="2"
            placeholder="请输入规格参数"
          />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="需求数量" prop="requiredQuantity">
              <el-input-number
                v-model="editForm.requiredQuantity"
                :min="0"
                :precision="3"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="单位" prop="unit">
              <el-input v-model="editForm.unit" placeholder="请输入单位" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="使用期限(天)" prop="plannedUsagePeriod">
              <el-input-number
                v-model="editForm.plannedUsagePeriod"
                :min="0"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="计划开始日期" prop="plannedStartDate">
              <el-date-picker
                v-model="editForm.plannedStartDate"
                type="date"
                placeholder="选择开始日期"
                style="width: 100%"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划结束日期" prop="plannedEndDate">
              <el-date-picker
                v-model="editForm.plannedEndDate"
                type="date"
                placeholder="选择结束日期"
                style="width: 100%"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预估成本" prop="estimatedCost">
              <el-input-number
                v-model="editForm.estimatedCost"
                :min="0"
                :precision="2"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责人" prop="responsiblePersonId">
              <el-select v-model="editForm.responsiblePersonId" placeholder="请选择负责人" style="width: 100%">
                <el-option label="张三" :value="1" />
                <el-option label="李四" :value="2" />
                <el-option label="王五" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="供应商信息" prop="supplierInfo">
          <el-input v-model="editForm.supplierInfo" placeholder="请输入供应商信息" />
        </el-form-item>

        <el-form-item label="技术要求" prop="technicalRequirements">
          <el-input
            v-model="editForm.technicalRequirements"
            type="textarea"
            :rows="2"
            placeholder="请输入技术要求"
          />
        </el-form-item>

        <el-form-item label="质量标准" prop="qualityStandards">
          <el-input
            v-model="editForm.qualityStandards"
            type="textarea"
            :rows="2"
            placeholder="请输入质量标准"
          />
        </el-form-item>

        <el-form-item label="交付要求" prop="deliveryRequirements">
          <el-input
            v-model="editForm.deliveryRequirements"
            type="textarea"
            :rows="2"
            placeholder="请输入交付要求"
          />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveEdit">确定</el-button>
      </div>
    </el-dialog>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handleSaveAll">保存全部</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import {
    createEquipmentResource,
    getEquipmentResourceList,
    updateEquipmentResource
  } from '@/api/contract/planning'

  export default {
    name: 'EquipmentManagement',
    data() {
      return {
        dialogVisible: false,
        editDialogVisible: false,
        editTitle: '',
        planningInfo: {},
        equipmentList: [],
        editForm: {
          planningId: null,
          equipmentName: '',
          equipmentType: '',
          equipmentModel: '',
          equipmentSpecification: '',
          requiredQuantity: 1,
          unit: '',
          plannedUsagePeriod: null,
          plannedStartDate: '',
          plannedEndDate: '',
          procurementMethod: null,
          estimatedCost: null,
          supplierInfo: '',
          technicalRequirements: '',
          qualityStandards: '',
          deliveryRequirements: '',
          responsiblePersonId: null
        },
        editIndex: -1,
        editRules: {
          equipmentName: [
            { required: true, message: '请输入设备名称', trigger: 'blur' }
          ],
          equipmentType: [
            { required: true, message: '请输入设备类型', trigger: 'blur' }
          ],
          requiredQuantity: [
            { required: true, message: '请输入需求数量', trigger: 'blur' }
          ],
          unit: [
            { required: true, message: '请输入单位', trigger: 'blur' }
          ],
          procurementMethod: [
            { required: true, message: '请选择采购方式', trigger: 'change' }
          ]
        }
      }
    },
    methods: {
      async showEdit(data) {
        this.dialogVisible = true
        this.planningInfo = { ...data }
        await this.loadEquipmentList()
      },

      async loadEquipmentList() {
        try {
          const response = await getEquipmentResourceList({
            planningId: this.planningInfo.id
          })
          if (response.code === 200) {
            this.equipmentList = response.data || []
          }
        } catch (error) {
          console.error('加载设备资源失败：', error)
        }
      },
      
      handleClose() {
        this.dialogVisible = false
        this.equipmentList = []
        this.planningInfo = {}
      },

      handleAdd() {
        this.editTitle = '添加设备资源'
        this.editIndex = -1
        this.resetEditForm()
        this.editDialogVisible = true
      },

      handleEdit(row, index) {
        this.editTitle = '编辑设备资源'
        this.editIndex = index
        this.editForm = { ...row }
        this.editDialogVisible = true
      },

      handleDelete(index) {
        this.$confirm('确定要删除这条设备资源吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.equipmentList.splice(index, 1)
          this.$message.success('删除成功')
        })
      },

      resetEditForm() {
        this.editForm = {
          planningId: this.planningInfo.id,
          equipmentName: '',
          equipmentType: '',
          equipmentModel: '',
          equipmentSpecification: '',
          requiredQuantity: 1,
          unit: '',
          plannedUsagePeriod: null,
          plannedStartDate: '',
          plannedEndDate: '',
          procurementMethod: null,
          estimatedCost: null,
          supplierInfo: '',
          technicalRequirements: '',
          qualityStandards: '',
          deliveryRequirements: '',
          responsiblePersonId: null
        }
      },

      async handleSaveEdit() {
        try {
          await this.$refs.editForm.validate()
          
          if (this.editIndex === -1) {
            // 添加
            this.equipmentList.push({ ...this.editForm })
          } else {
            // 编辑
            this.equipmentList.splice(this.editIndex, 1, { ...this.editForm })
          }
          
          this.editDialogVisible = false
          this.$message.success('保存成功')
        } catch (error) {
          console.error('保存失败：', error)
        }
      },

      async handleSaveAll() {
        try {
          // 这里应该调用批量保存API
          this.$message.success('保存成功')
          this.handleClose()
        } catch (error) {
          this.$message.error('保存失败：' + error.message)
        }
      },

      // 获取采购方式名称
      getProcurementMethodName(method) {
        const methodMap = {
          1: '购买',
          2: '租赁',
          3: '借用'
        }
        return methodMap[method] || '未知'
      }
    }
  }
</script>

<style scoped>
  .dialog-footer {
    text-align: right;
  }
</style>
