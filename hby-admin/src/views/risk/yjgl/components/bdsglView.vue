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
          <el-form-item label="指标名称">
            <span>{{ detail.name1 }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="表达式名称">
            <span>{{ detail.name2 }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="表达式内容">
            <span>{{ detail.content }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="备注">
            <span>{{ detail.remark }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态">
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
    name: 'bdsglView',
    data() {
      return {
        title: '表达式管理 - 详细信息',
        dialogFormVisible: false,
        detail: {}, //详情
      }
    },
    created() {},
    methods: {
      //显示
      showEdit(data) {
        console.log(1111111111111)
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
