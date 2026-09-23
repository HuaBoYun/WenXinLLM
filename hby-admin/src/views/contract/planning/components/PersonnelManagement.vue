<template>
  <el-dialog
    title="人员配置管理"
    :visible.sync="dialogVisible"
    width="90%"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    @close="handleClose"
  >
    <div style="margin-bottom: 20px;">
      <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd">
        添加人员配置
      </el-button>
    </div>

    <el-table :data="personnelList" border style="width: 100%">
      <el-table-column label="角色名称" prop="roleName" width="120" />
      <el-table-column label="角色描述" prop="roleDescription" min-width="150" />
      <el-table-column label="技能要求" prop="requiredSkills" min-width="150" />
      <el-table-column label="经验要求" prop="experienceRequirements" width="120" />
      <el-table-column label="分配人员" prop="allocatedPersonName" width="100" />
      <el-table-column label="工作量(%)" prop="workloadPercentage" width="100" />
      <el-table-column label="日费率" prop="dailyRate" width="100">
        <template #default="{ row }">
          {{ row.dailyRate ? row.dailyRate + '元' : '-' }}
        </template>
      </el-table-column>
      <el-table-column label="配置状态" prop="allocationStatus" width="100">
        <template #default="{ row }">
          <el-tag :type="getAllocationStatusType(row.allocationStatus)">
            {{ getAllocationStatusName(row.allocationStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row, $index }">
          <el-button type="text" size="small" @click="handleEdit(row, $index)">编辑</el-button>
          <el-button type="text" size="small" style="color: #f56c6c;" @click="handleDelete($index)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 人员配置编辑弹窗 -->
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
            <el-form-item label="角色名称" prop="roleName">
              <el-input v-model="editForm.roleName" placeholder="请输入角色名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分配人员" prop="allocatedPersonId">
              <el-select v-model="editForm.allocatedPersonId" placeholder="请选择人员" style="width: 100%">
                <el-option label="张三" :value="1" />
                <el-option label="李四" :value="2" />
                <el-option label="王五" :value="3" />
                <el-option label="赵六" :value="4" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="角色描述" prop="roleDescription">
          <el-input
            v-model="editForm.roleDescription"
            type="textarea"
            :rows="2"
            placeholder="请输入角色描述"
          />
        </el-form-item>

        <el-form-item label="技能要求" prop="requiredSkills">
          <el-input
            v-model="editForm.requiredSkills"
            type="textarea"
            :rows="2"
            placeholder="请输入技能要求"
          />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="经验要求" prop="experienceRequirements">
              <el-input v-model="editForm.experienceRequirements" placeholder="请输入经验要求" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学历要求" prop="educationRequirements">
              <el-input v-model="editForm.educationRequirements" placeholder="请输入学历要求" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="工作量(%)" prop="workloadPercentage">
              <el-input-number
                v-model="editForm.workloadPercentage"
                :min="0"
                :max="100"
                :precision="2"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="日费率" prop="dailyRate">
              <el-input-number
                v-model="editForm.dailyRate"
                :min="0"
                :precision="2"
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

        <el-form-item label="配置状态" prop="allocationStatus">
          <el-select v-model="editForm.allocationStatus" placeholder="请选择配置状态" style="width: 100%">
            <el-option label="待分配" :value="1" />
            <el-option label="已分配" :value="2" />
            <el-option label="已确认" :value="3" />
            <el-option label="已取消" :value="4" />
          </el-select>
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
    createPersonnelAllocation,
    getPersonnelAllocationList,
    updatePersonnelAllocation
  } from '@/api/contract/planning'

  export default {
    name: 'PersonnelManagement',
    data() {
      return {
        dialogVisible: false,
        editDialogVisible: false,
        editTitle: '',
        planningInfo: {},
        personnelList: [],
        editForm: {
          planningId: null,
          roleName: '',
          roleDescription: '',
          requiredSkills: '',
          experienceRequirements: '',
          educationRequirements: '',
          allocatedPersonId: null,
          workloadPercentage: 100,
          dailyRate: null,
          plannedStartDate: '',
          plannedEndDate: '',
          allocationStatus: 1
        },
        editIndex: -1,
        editRules: {
          roleName: [
            { required: true, message: '请输入角色名称', trigger: 'blur' }
          ],
          allocatedPersonId: [
            { required: true, message: '请选择分配人员', trigger: 'change' }
          ],
          workloadPercentage: [
            { required: true, message: '请输入工作量', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      async showEdit(data) {
        this.dialogVisible = true
        this.planningInfo = { ...data }
        await this.loadPersonnelList()
      },

      async loadPersonnelList() {
        try {
          const response = await getPersonnelAllocationList({
            planningId: this.planningInfo.id
          })
          if (response.code === 200) {
            this.personnelList = response.data || []
          }
        } catch (error) {
          console.error('加载人员配置失败：', error)
        }
      },
      
      handleClose() {
        this.dialogVisible = false
        this.personnelList = []
        this.planningInfo = {}
      },

      handleAdd() {
        this.editTitle = '添加人员配置'
        this.editIndex = -1
        this.resetEditForm()
        this.editDialogVisible = true
      },

      handleEdit(row, index) {
        this.editTitle = '编辑人员配置'
        this.editIndex = index
        this.editForm = { ...row }
        this.editDialogVisible = true
      },

      handleDelete(index) {
        this.$confirm('确定要删除这条人员配置吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.personnelList.splice(index, 1)
          this.$message.success('删除成功')
        })
      },

      resetEditForm() {
        this.editForm = {
          planningId: this.planningInfo.id,
          roleName: '',
          roleDescription: '',
          requiredSkills: '',
          experienceRequirements: '',
          educationRequirements: '',
          allocatedPersonId: null,
          workloadPercentage: 100,
          dailyRate: null,
          plannedStartDate: '',
          plannedEndDate: '',
          allocationStatus: 1
        }
      },

      async handleSaveEdit() {
        try {
          await this.$refs.editForm.validate()
          
          if (this.editIndex === -1) {
            // 添加
            this.personnelList.push({ ...this.editForm })
          } else {
            // 编辑
            this.personnelList.splice(this.editIndex, 1, { ...this.editForm })
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

      // 获取配置状态名称
      getAllocationStatusName(status) {
        const statusMap = {
          1: '待分配',
          2: '已分配',
          3: '已确认',
          4: '已取消'
        }
        return statusMap[status] || '未知'
      },

      // 获取配置状态样式
      getAllocationStatusType(status) {
        const typeMap = {
          1: 'info',
          2: 'warning',
          3: 'success',
          4: 'danger'
        }
        return typeMap[status] || 'info'
      }
    }
  }
</script>

<style scoped>
  .dialog-footer {
    text-align: right;
  }
</style>
