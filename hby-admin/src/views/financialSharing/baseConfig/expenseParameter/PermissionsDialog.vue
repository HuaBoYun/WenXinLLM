<template>
  <el-dialog
    title="权限配置"
    :visible="visible"
    width="800px"
    @close="handleClose"
  >
    <div class="permissions-dialog">
      <div class="toolbar">
        <el-button type="primary" size="small" @click="handleAdd">新增权限</el-button>
      </div>

      <el-table
        :data="permissionsList"
        border
        style="width: 100%; margin-top: 10px"
      >
        <el-table-column prop="permissionType" label="权限类型" width="150">
          <template slot-scope="scope">
            <el-select
              v-if="scope.row.editing"
              v-model="scope.row.permissionType"
              size="small"
              placeholder="请选择权限类型"
              style="width: 100%"
            >
              <el-option label="查看" value="VIEW" />
              <el-option label="编辑" value="EDIT" />
              <el-option label="删除" value="DELETE" />
              <el-option label="审批" value="APPROVE" />
            </el-select>
            <span v-else>{{ getPermissionTypeName(scope.row.permissionType) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="permissionTarget" label="权限目标" width="200">
          <template slot-scope="scope">
            <el-input
              v-if="scope.row.editing"
              v-model="scope.row.permissionTarget"
              size="small"
              placeholder="请输入权限目标（角色/用户ID）"
            />
            <span v-else>{{ scope.row.permissionTarget }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="permissionValue" label="权限值" width="120">
          <template slot-scope="scope">
            <el-select
              v-if="scope.row.editing"
              v-model="scope.row.permissionValue"
              size="small"
              placeholder="请选择"
              style="width: 100%"
            >
              <el-option label="允许" :value="1" />
              <el-option label="拒绝" :value="0" />
            </el-select>
            <el-tag v-else :type="scope.row.permissionValue === 1 ? 'success' : 'danger'" size="small">
              {{ scope.row.permissionValue === 1 ? '允许' : '拒绝' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注">
          <template slot-scope="scope">
            <el-input
              v-if="scope.row.editing"
              v-model="scope.row.remark"
              size="small"
              placeholder="请输入备注"
            />
            <span v-else>{{ scope.row.remark }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template slot-scope="scope">
            <el-button
              v-if="!scope.row.editing"
              size="mini"
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            <el-button
              v-if="scope.row.editing"
              size="mini"
              type="success"
              @click="handleSaveRow(scope.row)"
            >
              保存
            </el-button>
            <el-button
              v-if="scope.row.editing"
              size="mini"
              @click="handleCancelRow(scope.row)"
            >
              取消
            </el-button>
            <el-button
              v-if="!scope.row.editing"
              size="mini"
              type="danger"
              @click="handleDeleteRow(scope.$index)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSave" :loading="saveLoading">保存全部</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { expenseParameterApi } from '@/api/financialSharing/baseConfig'

export default {
  name: 'PermissionsDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    parameterId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      permissionsList: [],
      saveLoading: false
    }
  },
  mounted() {
    console.log('PermissionsDialog mounted, visible:', this.visible, 'parameterId:', this.parameterId)
  },
  watch: {
    visible(val) {
      console.log('PermissionsDialog visible 变化:', val, 'parameterId:', this.parameterId)
      if (val && this.parameterId) {
        this.loadPermissions()
      }
    }
  },
  methods: {
    async loadPermissions() {
      console.log('loadPermissions 被调用, parameterId:', this.parameterId)
      try {
        const response = await expenseParameterApi.getPermissions(this.parameterId)
        console.log('获取权限列表响应:', response)
        if (response.code === 1) {
          this.permissionsList = (response.data || []).map(item => ({
            ...item,
            editing: false
          }))
          console.log('权限列表加载成功:', this.permissionsList)
        } else {
          this.$message.error(response.msg || '加载权限失败')
        }
      } catch (error) {
        console.error('加载权限失败:', error)
        this.$message.error('加载权限失败：' + error.message)
      }
    },
    handleAdd() {
      this.permissionsList.push({
        permissionId: null,
        parameterId: this.parameterId,
        permissionType: '',
        permissionTarget: '',
        permissionValue: 1,
        remark: '',
        editing: true
      })
    },
    handleEdit(row) {
      row.editing = true
      row._backup = { ...row }
    },
    handleSaveRow(row) {
      if (!row.permissionType || !row.permissionTarget) {
        this.$message.warning('权限类型和权限目标不能为空')
        return
      }
      row.editing = false
      delete row._backup
    },
    handleCancelRow(row) {
      if (row._backup) {
        Object.assign(row, row._backup)
        delete row._backup
      }
      row.editing = false
      if (!row.permissionId) {
        const index = this.permissionsList.indexOf(row)
        this.permissionsList.splice(index, 1)
      }
    },
    handleDeleteRow(index) {
      this.$confirm('确定要删除该权限吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.permissionsList.splice(index, 1)
      }).catch(() => {})
    },
    async handleSave() {
      // 检查是否有正在编辑的行
      const editingRow = this.permissionsList.find(item => item.editing)
      if (editingRow) {
        this.$message.warning('请先保存或取消正在编辑的行')
        return
      }

      this.saveLoading = true
      try {
        const response = await expenseParameterApi.savePermissions(
          this.parameterId,
          this.permissionsList.map(item => ({
            permissionId: item.permissionId,
            parameterId: item.parameterId,
            permissionType: item.permissionType,
            permissionTarget: item.permissionTarget,
            permissionValue: item.permissionValue,
            remark: item.remark
          }))
        )
        if (response.code === 1) {
          this.$message.success('保存成功')
          this.handleClose()
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        this.$message.error('保存失败：' + error.message)
      } finally {
        this.saveLoading = false
      }
    },
    handleClose() {
      console.log('PermissionsDialog handleClose 被调用')
      this.$emit('update:visible', false)
      this.$emit('close')
    },
    getPermissionTypeName(type) {
      const typeMap = {
        'VIEW': '查看',
        'EDIT': '编辑',
        'DELETE': '删除',
        'APPROVE': '审批'
      }
      return typeMap[type] || type
    }
  }
}
</script>

<style scoped>
.permissions-dialog {
  padding: 10px;
}

.toolbar {
  margin-bottom: 10px;
}
</style>

