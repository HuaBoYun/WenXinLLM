<template>
  <el-dialog
    title="审核流程配置"
    :visible.sync="dialogVisible"
    width="900px"
    @close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item label="流程名称" prop="workflowName">
        <el-input v-model="form.workflowName" placeholder="请输入流程名称"></el-input>
      </el-form-item>
      
      <el-form-item label="流程描述" prop="workflowDesc">
        <el-input
          v-model="form.workflowDesc"
          type="textarea"
          :rows="3"
          placeholder="请输入流程描述"
        ></el-input>
      </el-form-item>
      
      <el-form-item label="审核节点">
        <el-button type="primary" size="small" @click="addNode">添加节点</el-button>
        <el-table :data="form.nodes" border style="margin-top: 10px;">
          <el-table-column label="序号" width="80">
            <template slot-scope="scope">
              {{ scope.$index + 1 }}
            </template>
          </el-table-column>
          <el-table-column label="节点名称">
            <template slot-scope="scope">
              <el-input v-model="scope.row.nodeName" size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="审核人">
            <template slot-scope="scope">
              <el-select v-model="scope.row.auditor" size="small" placeholder="选择审核人">
                <el-option label="张三" value="zhangsan"></el-option>
                <el-option label="李四" value="lisi"></el-option>
                <el-option label="王五" value="wangwu"></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="审核类型">
            <template slot-scope="scope">
              <el-select v-model="scope.row.auditType" size="small" placeholder="选择类型">
                <el-option label="必须审核" value="required"></el-option>
                <el-option label="可选审核" value="optional"></el-option>
                <el-option label="会签审核" value="countersign"></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="超时时间(小时)">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.timeout" size="small" :min="1" :max="168"></el-input-number>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100">
            <template slot-scope="scope">
              <el-button type="danger" size="mini" @click="removeNode(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form-item>
      
      <el-form-item label="流程设置">
        <el-checkbox v-model="form.autoStart">自动启动</el-checkbox>
        <el-checkbox v-model="form.allowSkip">允许跳过</el-checkbox>
        <el-checkbox v-model="form.allowReturn">允许退回</el-checkbox>
        <el-checkbox v-model="form.notifyEmail">邮件通知</el-checkbox>
      </el-form-item>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleConfirm" :loading="loading">保存</el-button>
    </div>
  </el-dialog>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'AuditWorkflowDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    workflowData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      form: {
        workflowName: '',
        workflowDesc: '',
        nodes: [],
        autoStart: false,
        allowSkip: false,
        allowReturn: true,
        notifyEmail: true
      },
      rules: {
        workflowName: [
          { required: true, message: '请输入流程名称', trigger: 'blur' }
        ],
        workflowDesc: [
          { required: true, message: '请输入流程描述', trigger: 'blur' }
        ]
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
        workflowName: this.workflowData.workflowName || '',
        workflowDesc: this.workflowData.workflowDesc || '',
        nodes: this.workflowData.nodes || [
          {
            nodeName: '初审',
            auditor: '',
            auditType: 'required',
            timeout: 24
          }
        ],
        autoStart: this.workflowData.autoStart || false,
        allowSkip: this.workflowData.allowSkip || false,
        allowReturn: this.workflowData.allowReturn || true,
        notifyEmail: this.workflowData.notifyEmail || true
      }
    },
    addNode() {
      this.form.nodes.push({
        nodeName: '',
        auditor: '',
        auditType: 'required',
        timeout: 24
      })
    },
    removeNode(index) {
      this.form.nodes.splice(index, 1)
    },
    handleClose() {
      this.dialogVisible = false
      this.$refs.form.resetFields()
    },
    handleConfirm() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          if (this.form.nodes.length === 0) {
            this.$message.warning('请至少添加一个审核节点')
            return
          }
          this.loading = true
          try {
            await request({
              url: '/monitor/v1/enterprise/data/quality/workflow/save',
              method: 'post',
              headers: { 'Content-Type': 'application/json;charset=UTF-8' },
              data: this.form
            })
            this.$message.success('流程配置保存成功')
            this.handleClose()
            this.$emit('refresh')
          } catch (error) {
            this.$message.error(error.message || '流程配置保存失败')
          } finally {
            this.loading = false
          }
        }
      })
    }
  }
}
</script>
