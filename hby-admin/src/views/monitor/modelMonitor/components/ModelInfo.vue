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
          <el-form-item label="模型编号">
            <span>{{ detail.modelcode }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="模型名称">
            <span>{{ detail.modelname }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="模型所属机构/部门">
            <span>{{ detail.orgname }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="模型分类">
            <span>{{ detail.modelcategory }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="模型状态">
            <span>{{ detail.modelstatus }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人">
            <span>{{ detail.realname }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="提醒方式">
            <span>{{ detail.modelreminder }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数据连接字符串">
            <span>{{ detail.connectionstrings }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="模型描述">
            <span>{{ detail.modeldes }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="步骤1">
            <span>{{ detail.modelstep1 }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="步骤2">
            <span>{{ detail.modelstep2 }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="步骤3">
            <span>{{ detail.modelstep3 }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="步骤4">
            <span>{{ detail.modelstep4 }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="步骤5">
            <span>{{ detail.modelstep5 }}</span>
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
    name: 'ModelInfo',
    data() {
      return {
        title: '模型管理 - 详细信息',
        dialogFormVisible: false,
        detail: {},
      }
    },
    created() {},
    methods: {
      showEdit(data) {
        const {
          books,
          organization: { orgmeno },
          user: { realname },
          ...row
        } = data
        this.detail = {
          realname,
          orgname: orgmeno,
          ...row,
          connectionstrings: books.find(
            (v) => v.bookid === row.connectionstrings
          ).bookname,
        }
        this.dialogFormVisible = true
      },
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
