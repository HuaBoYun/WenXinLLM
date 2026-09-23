<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="500px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-form ref="form" label-width="140px">
      <el-form-item label="机构编码">
        <span>{{ info.orgnumber }}</span>
      </el-form-item>
      <el-form-item label="机构名称">
        <span>{{ info.orgname }}</span>
      </el-form-item>
      <!-- <el-form-item label="是否是主责部门">
        <span>是</span>
      </el-form-item> -->
      <el-form-item label="机构简介">
        <span>
          {{ info.orgmeno }}
        </span>
      </el-form-item>
      <el-form-item label="备注">
        <span>
          {{ info.memo }}
        </span>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="close">关 闭</el-button>
    </template>
  </el-dialog>
</template>

<script>
  export default {
    name: 'CompanyInfo',
    props: {
      title: {
        type: String,
        default: '公司信息',
      },
    },
    data() {
      return {
        dialogFormVisible: false,
        info: [],
      }
    },
    created() {},
    methods: {
      showEdit(row) {
        this.dialogFormVisible = true
        this.info = row
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
