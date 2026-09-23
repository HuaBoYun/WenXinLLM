<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="500px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-form ref="form" label-width="140px">
      <el-form-item label="用户名">
        <span>{{ companyList1.username }}</span>
      </el-form-item>
      <el-form-item label="真实姓名">
        <span>{{ companyList1.realname }}</span>
      </el-form-item>
      <el-form-item label="用户地址信息">
        <span>{{ companyList1.address }}</span>
      </el-form-item>
      <el-form-item label="所属机构">
        <span>{{ companyList1.orgName }}</span>
      </el-form-item>
      
      <el-form-item label="角色">
        <span>角色</span>
      </el-form-item>
      <el-form-item label="职位">
        <span>职位</span>
      </el-form-item>
      <el-form-item label="Email">
        <span>{{ companyList1.email }}</span>
      </el-form-item>
  
      <el-form-item label="电话号码">
        <span>{{ companyList1.fixedphone }}</span>
      </el-form-item>
      <el-form-item label="移动电话">
        <span>{{ companyList1.miblephone }}</span>
      </el-form-item>
      <el-form-item label="是否禁用">
        <span>{{ companyList1.status === 0 ? '禁用' : '启用' }}</span>
      </el-form-item>
      <el-form-item label="简短描述">
        <span>{{ companyList1.memo }}</span>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="close">关 闭</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { userInfo } from '@/api/setting/auth'
  export default {
    name: 'UserInfo',
    data() {
      return {
        title: '用户信息',
        dialogFormVisible: false,
        companyList1: [],
      }
    },
    created() {},
    methods: {
      showEdit(row) {
        this.organization(row)
        this.dialogFormVisible = true
      },
      close() {
        this.dialogFormVisible = false
      },
      async organization(row) {
        const { user } = await userInfo({ staffid: row.staffid })
        this.companyList1 = user
        console.log(this.companyList1)
        // this.$refs['leftlist'].handleNodeClick(tlist)
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
