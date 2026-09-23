<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-row :gutter="14">
      <el-form ref="form" label-width="120px" :model="form" :rules="rules">
        <el-col :span="12">
          <el-form-item label="疑点编号" prop="number">
            <span>NK-MA-08</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="疑点名称" prop="number">
            <span>111</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="编制人" prop="number">
            <span>张孝昆</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="编制时间" prop="number">
            <span>2020-11-12</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="疑点描述" prop="number">
            <span>111111111111111</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="测试结果" prop="number">
            <span>1111111111111111</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注">
            <span>111111111111</span>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <el-col :span="24">
      <el-divider>附件</el-divider>
    </el-col>
    <el-col :span="24">
      <el-table>
        <el-table-column align="center" label="附件名称" prop="name" />
        <el-table-column align="center" label="文件大小(KB)" prop="name" />
        <el-table-column align="center" label="创建人" prop="name" />
        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="120"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit2(row)">下载</el-button>
            <el-button type="text" @click="handleEdit2(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-col>
    <template #footer>
      <el-button @click="close">取 消</el-button>
    </template>
  </el-dialog>
</template>

<script>
  export default {
    name: 'DraftInfo',
    data() {
      return {
        form: {},
        rules: {
          number: [{ required: true, trigger: 'blur', message: '请输入' }],
        },
        field103Options: [
          {
            label: '穿行测试',
            value: 1,
          },
          {
            label: '控制测试',
            value: 2,
          },
        ],
        title: '详情',
        dialogFormVisible: false,
      }
    },
    created() {},
    methods: {
      showEdit() {
        this.dialogFormVisible = true
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
    },
  }
</script>
