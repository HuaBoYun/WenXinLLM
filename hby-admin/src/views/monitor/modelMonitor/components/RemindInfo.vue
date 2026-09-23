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
          <el-form-item label="方案编号">
            <span>{{ detail.solutioncode }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="方案名称">
            <span>{{ detail.solutionname }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="方案状态">
            <span>{{ detail.solutionstatus }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人">
            <span>{{ detail.realname }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建日期">
            <span>{{ detail.createdate }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注">
            <span>{{ detail.memo }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-card shadow="never" body-style="padding: 0">
            <div slot="header" class="clearfix">
              <span>模型</span>
            </div>
            <el-table>
              <el-table-column label="ID"></el-table-column>
              <el-table-column label="模型编号"></el-table-column>
              <el-table-column label="模型名称"></el-table-column>
              <el-table-column label="操作"></el-table-column>
            </el-table>
          </el-card>
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
    name: 'RemindInfo',
    data() {
      return {
        title: '模型预警 - 详细信息',
        dialogFormVisible: false,
        detail: {},
      }
    },
    created() {},
    methods: {
      showEdit(data) {
        const {
          organization: { orgmeno },
          user: { realname },
          ...row
        } = data
        this.detail = {
          realname,
          orgname: orgmeno,
          ...row,
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
