<template>
  <el-dialog
    title="选项配置"
    :visible="visible"
    width="800px"
    @close="handleClose"
  >
    <div class="options-dialog">
      <div class="toolbar">
        <el-button type="primary" size="small" @click="handleAdd">新增选项</el-button>
      </div>

      <el-table
        :data="optionsList"
        border
        style="width: 100%; margin-top: 10px"
      >
        <el-table-column prop="optionValue" label="选项值" width="150">
          <template slot-scope="scope">
            <el-input
              v-if="scope.row.editing"
              v-model="scope.row.optionValue"
              size="small"
              placeholder="请输入选项值"
            />
            <span v-else>{{ scope.row.optionValue }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="optionName" label="选项名称" width="150">
          <template slot-scope="scope">
            <el-input
              v-if="scope.row.editing"
              v-model="scope.row.optionName"
              size="small"
              placeholder="请输入选项名称"
            />
            <span v-else>{{ scope.row.optionName }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="sortOrder" label="排序号" width="100">
          <template slot-scope="scope">
            <el-input-number
              v-if="scope.row.editing"
              v-model="scope.row.sortOrder"
              size="small"
              :min="0"
              controls-position="right"
            />
            <span v-else>{{ scope.row.sortOrder }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="isEnabled" label="状态" width="80">
          <template slot-scope="scope">
            <el-switch
              v-if="scope.row.editing"
              v-model="scope.row.isEnabled"
              :active-value="1"
              :inactive-value="0"
            />
            <el-tag v-else :type="scope.row.isEnabled === 1 ? 'success' : 'danger'" size="small">
              {{ scope.row.isEnabled === 1 ? '启用' : '禁用' }}
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
  name: 'OptionsDialog',
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
      optionsList: [],
      saveLoading: false,
      originalData: []
    }
  },
  watch: {
    visible(val) {
      if (val && this.parameterId) {
        this.loadOptions()
      }
    }
  },
  methods: {
    async loadOptions() {
      try {
        const response = await expenseParameterApi.getOptions(this.parameterId)
        if (response.code === 1) {
          this.optionsList = (response.data || []).map(item => ({
            ...item,
            editing: false
          }))
          this.originalData = JSON.parse(JSON.stringify(this.optionsList))
        } else {
          this.$message.error(response.msg || '加载选项失败')
        }
      } catch (error) {
        this.$message.error('加载选项失败：' + error.message)
      }
    },
    handleAdd() {
      this.optionsList.push({
        optionId: null,
        parameterId: this.parameterId,
        optionValue: '',
        optionName: '',
        sortOrder: this.optionsList.length + 1,
        isEnabled: 1,
        remark: '',
        editing: true
      })
    },
    handleEdit(row) {
      row.editing = true
      row._backup = { ...row }
    },
    handleSaveRow(row) {
      if (!row.optionValue || !row.optionName) {
        this.$message.warning('选项值和选项名称不能为空')
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
      if (!row.optionId) {
        const index = this.optionsList.indexOf(row)
        this.optionsList.splice(index, 1)
      }
    },
    handleDeleteRow(index) {
      this.$confirm('确定要删除该选项吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.optionsList.splice(index, 1)
      }).catch(() => {})
    },
    async handleSave() {
      // 检查是否有正在编辑的行
      const editingRow = this.optionsList.find(item => item.editing)
      if (editingRow) {
        this.$message.warning('请先保存或取消正在编辑的行')
        return
      }

      this.saveLoading = true
      try {
        const response = await expenseParameterApi.saveOptions(
          this.parameterId,
          this.optionsList.map(item => ({
            optionId: item.optionId,
            parameterId: item.parameterId,
            optionValue: item.optionValue,
            optionName: item.optionName,
            sortOrder: item.sortOrder,
            isEnabled: item.isEnabled,
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
      this.$emit('update:visible', false)
      this.$emit('close')
    }
  }
}
</script>

