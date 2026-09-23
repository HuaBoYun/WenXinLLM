<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="15">
      <el-form ref="elForm" label-width="140px" size="medium">
        <el-col :span="12">
          <el-form-item label="模型名称">
            <span>{{ detail.name1 }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="适用行业">
            <span>{{ detail.name2 }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="导出平台">
            <span>{{ detail.content }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="元数据存储路径">
            <span>{{ detail.name3 }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="目标数据库">
            <span>{{ detail.name4 }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="目标表">
            <span>{{ detail.name5 }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="模型状态">
            <span>{{ detail.status == 1 ? '启用' : '停用' }}</span>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <template #footer>
      <el-button @click="close">关 闭</el-button>
    </template>
  </el-dialog>
</template>

<script>
  export default {
    name: 'sjmxglView',
    data() {
      return {
        title: '评估模型管理 - 详细信息',
        dialogFormVisible: false,
        detail: {}, //详情
      }
    },
    created() {},
    methods: {
      //显示
      showEdit(data) {
        this.detail = data
        this.dialogFormVisible = true
      },
      //关闭
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style scoped>
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
