<template>
  <el-dialog
    title="批量配置合并范围"
    :visible.sync="dialogVisible"
    width="900px"
    :before-close="handleClose"
    :close-on-click-modal="false"
  >
    <el-form ref="form" :model="form" label-width="140px">
      <el-form-item label="合并方法">
        <el-select v-model="defaultConsolidationMethod" placeholder="请选择默认合并方法">
          <el-option label="完全合并" value="FULL" />
          <el-option label="比例合并" value="PROPORTIONAL" />
          <el-option label="权益法" value="EQUITY" />
        </el-select>
      </el-form-item>

      <el-form-item label="生效起始期间">
        <el-input v-model="defaultStartPeriod" placeholder="如:202401" maxlength="20" />
      </el-form-item>

      <el-form-item label="生效终止期间">
        <el-input v-model="defaultEndPeriod" placeholder="如:202412" maxlength="20" />
      </el-form-item>

      <el-form-item label="组织列表">
        <el-button type="primary" size="small" @click="handleAddOrg">添加组织</el-button>
      </el-form-item>

      <!-- 组织列表表格 -->\n      <el-table :data="orgList" border max-height="400">
        <el-table-column label="序号" type="index" width="50" align="center" />
        <el-table-column label="组织ID" prop="orgId" width="150">
          <template slot-scope="scope">
            <el-input v-model="scope.row.orgId" placeholder="请输入组织ID" size="small" />
          </template>
        </el-table-column>
        <el-table-column label="组织名称" prop="orgName" width="200">
          <template slot-scope="scope">
            <el-input v-model="scope.row.orgName" placeholder="请输入组织名称" size="small" />
          </template>
        </el-table-column>
        <el-table-column label="合并方法" prop="consolidationMethod" width="150">
          <template slot-scope="scope">
            <el-select v-model="scope.row.consolidationMethod" placeholder="请选择" size="small">
              <el-option label="完全合并" value="FULL" />
              <el-option label="比例合并" value="PROPORTIONAL" />
              <el-option label="权益法" value="EQUITY" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="生效起始期间" prop="effectiveStartPeriod" width="120">
          <template slot-scope="scope">
            <el-input v-model="scope.row.effectiveStartPeriod" placeholder="202401" size="small" />
          </template>
        </el-table-column>
        <el-table-column label="生效终止期间" prop="effectiveEndPeriod" width="120">
          <template slot-scope="scope">
            <el-input v-model="scope.row.effectiveEndPeriod" placeholder="202412" size="small" />
          </template>
        </el-table-column>
        <el-table-column label="排序号" prop="sortOrder" width="80">
          <template slot-scope="scope">
            <el-input-number v-model="scope.row.sortOrder" :min="0" :max="9999" size="small" />
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="80" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-delete" @click="handleRemoveOrg(scope.$index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
        确定
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { batchSaveScope } from '@/api/financialSharing/consolidationReport/consolidationScope'

export default {
  name: 'BatchConfigDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    modelId: {
      type: String,
      default: null
    }
  },
  data() {
    return {
      dialogVisible: false,
      submitLoading: false,
      defaultConsolidationMethod: 'FULL',
      defaultStartPeriod: '',
      defaultEndPeriod: '',
      orgList: []
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
      if (!val) {
        this.resetForm()
      }
    }
  },
  methods: {
    /** 添加组织 */
    handleAddOrg() {
      this.orgList.push({
        orgId: '',
        orgName: '',
        consolidationMethod: this.defaultConsolidationMethod,
        effectiveStartPeriod: this.defaultStartPeriod,
        effectiveEndPeriod: this.defaultEndPeriod,
        sortOrder: this.orgList.length,
        isActive: 'Y'
      })
    },
    /** 删除组织 */
    handleRemoveOrg(index) {
      this.orgList.splice(index, 1)
    },
    /** 提交表单 */
    handleSubmit() {
      if (this.orgList.length === 0) {
        this.$message.warning('请至少添加一个组织')
        return
      }

      // 验证必填项
      for (let i = 0; i < this.orgList.length; i++) {
        const org = this.orgList[i]
        if (!org.orgId || !org.orgName || !org.consolidationMethod) {
          this.$message.warning(`第${i + 1}行数据不完整,请检查`)
          return
        }
      }

      this.submitLoading = true
      batchSaveScope({
        modelId: this.modelId,
        scopeList: this.orgList
      }).then(res => {
        this.submitLoading = false
        if (res.code === 200) {
          this.$message.success('批量配置成功')
          this.$emit('success')
          this.handleClose()
        } else {
          this.$message.error(res.msg || '批量配置失败')
        }
      }).catch(() => {
        this.submitLoading = false
        this.$message.error('批量配置失败')
      })
    },
    /** 关闭对话框 */
    handleClose() {
      this.dialogVisible = false
    },
    /** 重置表单 */
    resetForm() {
      this.defaultConsolidationMethod = 'FULL'
      this.defaultStartPeriod = ''
      this.defaultEndPeriod = ''
      this.orgList = []
    }
  }
}
</script>

<style scoped>
</style>

