<template>
  <el-dialog
    title="资产验证"
    :visible.sync="dialogVisible"
    width="60%"
    :before-close="handleClose"
  >
    <el-form :model="form" label-width="120px">
      <el-form-item label="验证类型">
        <el-select v-model="form.validationType">
          <el-option label="数据完整性验证" value="completeness"></el-option>
          <el-option label="数据准确性验证" value="accuracy"></el-option>
          <el-option label="映射关系验证" value="mapping"></el-option>
        </el-select>
      </el-form-item>
      
      <el-form-item label="验证结果">
        <el-tag :type="getValidationResultType(form.result)">
          {{ getValidationResultText(form.result) }}
        </el-tag>
      </el-form-item>
      
      <el-form-item label="验证详情">
        <el-input type="textarea" v-model="form.details" :rows="4"></el-input>
      </el-form-item>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="saveValidation">保存验证</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'AssetValidationDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      form: {
        validationType: 'completeness',
        result: 'passed',
        details: '资产数据验证通过，所有必填字段完整，映射关系正确。'
      }
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    }
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
    },
    getValidationResultType(result) {
      const typeMap = {
        'passed': 'success',
        'failed': 'danger',
        'warning': 'warning'
      }
      return typeMap[result] || 'info'
    },
    getValidationResultText(result) {
      const textMap = {
        'passed': '验证通过',
        'failed': '验证失败',
        'warning': '验证警告'
      }
      return textMap[result] || result
    },
    saveValidation() {
      this.$message.success('保存验证结果成功')
      this.handleClose()
    }
  }
}
</script>
