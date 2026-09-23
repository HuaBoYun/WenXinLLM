<template>
  <el-dialog
    title="复制表达式规则"
    :visible.sync="visible"
    width="600px"
    :before-close="handleClose"
    append-to-body
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
      size="small"
    >
      <el-form-item label="新规则名称" prop="ruleName">
        <el-input v-model="form.ruleName" placeholder="请输入新规则名称" />
      </el-form-item>
      
      <el-form-item label="新规则编码" prop="ruleCode">
        <el-input v-model="form.ruleCode" placeholder="请输入新规则编码" />
      </el-form-item>
      
      <el-form-item label="复制选项">
        <el-checkbox-group v-model="copyOptions">
          <el-checkbox label="description">复制描述</el-checkbox>
          <el-checkbox label="expression">复制表达式内容</el-checkbox>
          <el-checkbox label="tags">复制标签</el-checkbox>
          <el-checkbox label="priority">复制优先级</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      
      <el-form-item label="目标状态">
        <el-radio-group v-model="form.status">
          <el-radio label="DRAFT">草稿</el-radio>
          <el-radio label="ACTIVE">启用</el-radio>
          <el-radio label="INACTIVE">禁用</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="loading">确定复制</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveExpressionRule } from '@/api/mxgl'

export default {
  name: 'ExpressionCopyDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    data: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      copyOptions: ['description', 'expression', 'tags'],
      form: {
        ruleName: '',
        ruleCode: '',
        status: 'DRAFT'
      },
      rules: {
        ruleName: [
          { required: true, message: '请输入新规则名称', trigger: 'blur' }
        ],
        ruleCode: [
          { required: true, message: '请输入新规则编码', trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.initForm()
      }
    }
  },
  methods: {
    initForm() {
      this.form = {
        ruleName: this.data.ruleName + '_副本',
        ruleCode: this.data.ruleCode + '_copy',
        status: 'DRAFT'
      }
      this.copyOptions = ['description', 'expression', 'tags']
    },
    
    handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          this.loading = true
          try {
            const newRule = {
              ruleName: this.form.ruleName,
              ruleCode: this.form.ruleCode,
              ruleType: this.data.ruleType,
              status: this.form.status,
              isPublic: false
            }
            
            // 根据复制选项添加字段
            if (this.copyOptions.includes('description')) {
              newRule.description = this.data.description
            }
            if (this.copyOptions.includes('expression')) {
              newRule.expression = this.data.expression
            }
            if (this.copyOptions.includes('tags')) {
              newRule.tags = [...(this.data.tags || [])]
            }
            if (this.copyOptions.includes('priority')) {
              newRule.priority = this.data.priority
            } else {
              newRule.priority = 1
            }
            
            const response = await saveExpressionRule(newRule)
            if (response.code === 1) {
              this.$message.success('复制成功')
              this.$emit('success')
              this.handleClose()
            } else {
              this.$message.error(response.msg || '复制失败')
            }
          } catch (error) {
            this.$message.error('复制失败')
          } finally {
            this.loading = false
          }
        }
      })
    },
    
    handleClose() {
      this.$emit('update:visible', false)
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>
