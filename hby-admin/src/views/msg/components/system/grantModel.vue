<template>
  <div>
    <el-form label-width="100px" ref="elForm">
      <el-form-item
        label="授权角色"
        prop="operationData.rname"
      >
        <el-input v-model="operationData.rname" disabled></el-input>
      </el-form-item>
      <el-form-item
        label="用户"
        v-if="primaryInfo.operationType == 7 || primaryInfo.operationType == 9"
        prop="operationData.realnames"
      >
        <div style="display: flex; align-items: center">
          <el-input
            v-model="operationData.realnames"
            disabled
            style="min-width: 400px; margin-right: 10px"
          ></el-input>
          <el-button
            type="primary"
            @click="handleSubmit"
            v-if="primaryInfo.status == 2"
          >
            选择
          </el-button>
        </div>
      </el-form-item>
      <el-form-item
        label="公司名称"
        v-if="primaryInfo.operationType == 8 || primaryInfo.operationType == 10"
        prop="operationData.orgnames"
      >
        <div style="display: flex; align-items: center">
          <el-input
            v-model="operationData.orgnames"
            disabled
            style="min-width: 400px; margin-right: 10px"
          ></el-input>
          <el-button
            type="primary"
            @click="handleSubmit"
            v-if="primaryInfo.status == 2"
          >
            选择
          </el-button>
        </div>
      </el-form-item>
      <el-form-item label="描述">
        <el-input v-model="primaryInfo.operationMemo" disabled></el-input>
      </el-form-item>
    </el-form>
    <CompanyRoleTree ref="CompanyRoleTree" @selected="handCompanyRoleSubmit" />
    <RemoveCompanyRoleTree
      ref="RemoveCompanyRoleTree"
      :fromType="1"
      @selected="handCompanySubmit"
    />
    <UserTable ref="UserTable" @selected="handUserTableSubmit" />
    <RemoveUser
      ref="RemoveUser"
      :fromType="1"
      @selected="handUserTableSubmit"
    />
    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
    <div slot="footer" style="text-align: right" v-if="primaryInfo.status == 2">
      <el-button @click="save" type="primary">确定</el-button>
      <el-button @click="ymsubmit" type="primary">提交</el-button>
    </div>
  </div>
</template>
<script>
  import { rqorg, rquser, qxuser, delrqorg } from '@/api/setting/auth'
  import UserTable from '@/views/setting/auth/components/UserTable'
  import RemoveUser from '@/views/setting/auth/components/RemoveUser'
  import CompanyRoleTree from '@/views/setting/auth/components/CompanyRoleTree.vue'
  import RemoveCompanyRoleTree from '@/views/setting/auth/components/RemoveCompanyRoleTree'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'

  export default {
    name: 'grantModel',
    components: {
      CompanyRoleTree,
      UserTable,
      RemoveUser,
      RemoveCompanyRoleTree,
      Resubmit,
    },
    data() {
      return {
        flowtaskinfoflowid: '',
        fromId: '',
        ymFromId: '',
        isWfqdedit: '',
        status: '',
        fromIdcopy: '',
        primaryInfo: {},
        operationData: {},
      }
    },
    methods: {
      showEdit(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status,
        primaryInfo
      ) {
        console.log('🚀 ~ primaryInfo:', primaryInfo)
        this.fromId = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.isWfqdedit = isWfqdedit
        this.status = status
        this.primaryInfo = primaryInfo
        this.operationData = JSON.parse(primaryInfo.operationData)
      },
      handleSubmit() {
        if (this.primaryInfo.operationType == 7) {
          this.$refs['UserTable'].show(this.primaryInfo.targetId)
        } else if (this.primaryInfo.operationType == 8) {
          this.$refs['CompanyRoleTree'].showEdit(this.primaryInfo.targetId)
        } else if (this.primaryInfo.operationType == 9) {
          this.$refs['RemoveUser'].showEdit({ rid: this.primaryInfo.targetId })
        } else if (this.primaryInfo.operationType == 10) {
          this.$refs['RemoveCompanyRoleTree'].showEdit({
            rid: this.primaryInfo.targetId,
          })
        }
      },
      close() {
        this.$bus.$emit('updateMsg', 0)
      },
      async handCompanyRoleSubmit(val) {
        this.companyRoleList = val
        this.$set(
          this.operationData,
          'orgnames',
          val.map((item) => item.label).join(',')
        )
      },
      async handCompanySubmit(val) {
        this.companyList = val
        this.$set(
          this.operationData,
          'orgnames',
          val.map((item) => item.orgname).join(',')
        )
      },
      async handUserTableSubmit(val) {
        console.log('🚀 ~ val:', val)
        this.userList = val
        this.$set(
          this.operationData,
          'realnames',
          val.map((item) => item.realname).join(',')
        )
      },
      async save() {
        if (this.primaryInfo.operationType == 7) {
          const { code, data } = await rquser({
            roleid: this.primaryInfo.targetId,
            staffids: this.userList
              .map((item) => {
                return item.staffid
              })
              .join(','),
          })
          if (code == 1) {
            this.$message.success('修改成功')
          }
        }
        if (this.primaryInfo.operationType == 8) {
          const { code, data } = await rqorg({
            roleid: this.primaryInfo.targetId,
            orgids: this.companyRoleList.map((item) => item.id).join(','),
          })
          if (code == 1) {
            this.$message.success('修改成功')
          }
        }
        if (this.primaryInfo.operationType == 9) {
          const { code, data } = await qxuser({
            roleid: this.primaryInfo.targetId,
            staffids: this.userList
              .map((item) => {
                return item.staffid
              })
              .join(','),
          })
          if (code == 1) {
            this.$message.success('修改成功')
          }
        }
        if (this.primaryInfo.operationType == 10) {
          const { msg, data, code } = await delrqorg({
            roleid: this.primaryInfo.targetId,
            orgids: this.companyList.map((item) => item.orgid).join(','),
          })
          if (code == 1) {
            this.$message.success('修改成功')
          }
        }
      },
      async ymsubmit() {
        this.$refs.resubmit.ymsubmit()
      },
    },
  }
</script>
<style scoped></style>
