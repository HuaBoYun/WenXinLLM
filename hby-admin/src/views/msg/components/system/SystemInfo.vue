<template>
  <div>
    <h2 style="margin: 0 auto; padding: 0; text-align: center">
      {{ title }}
    </h2>
    <el-divider />
    <el-form label-width="100px" :class="{ disabled: true }">
      <el-row>
        <el-col :span="12">
          <el-form-item label="创建人">
            <el-input v-model="username" disabled style="border: none" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间">
            <el-input v-model="createTime" disabled style="border: none" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <el-divider />
    <!-- 用户流程 -->
    <div v-if="targetType == 'user' && !isResetPassword">
      <UserFlow ref="userFlow" />
    </div>
    <div v-if="isResetPassword && targetType == 'user'">
      <ResetPassword ref="resetPassword" />
    </div>
    <div v-if="targetType == 'company' && !isDelete">
      <Company ref="company" />
    </div>
    <div v-if="targetType == 'company' && isDelete">
      <ResetPassword ref="resetPassword" />
    </div>
    <div v-if="targetType == 'dept' && !isDelete">
      <Department ref="department" />
    </div>
    <div v-if="targetType == 'dept' && isDelete">
      <ResetPassword ref="resetPassword" />
    </div>
    <div v-if="targetType == 'secrect' && !isDelete">
      <Miji ref="miji" />
    </div>
    <div v-if="targetType == 'secrect' && isDelete">
      <ResetPassword ref="resetPassword" />
    </div>
    <div v-if="targetType == 'right' && !isDelete">
      <Right ref="right" />
    </div>
    <div v-if="targetType == 'right' && isDelete">
      <ResetPassword ref="resetPassword" />
    </div>
    <div v-if="targetType == 'commonflow' && isDelete">
      <ResetPassword ref="resetPassword" />
    </div>
    <div v-if="targetType == 'contractflow' && isDelete">
      <ResetPassword ref="resetPassword" />
    </div>
    <div v-if="targetType == 'grant' && operationTypeNum == 11">
      <MemuSQ ref="MemuSQ" />
    </div>
    <div v-if="targetType == 'grant' && !isDelete && operationTypeNum != 11">
      <grantModel ref="grant" />
    </div>

    <div v-if="targetType == 'role' && !isDelete">
      <Role ref="role" />
    </div>
    <div v-if="targetType == 'role' && isDelete">
      <ResetPassword ref="resetPassword" />
    </div>
  </div>
</template>

<script>
  import { getSystemInfo } from '@/api/setting/auth'
  import UserFlow from '@/views/msg/components/system/user.vue'
  import ResetPassword from '@/views/msg/components/system/resetPassword.vue'
  import Company from '@/views/msg/components/system/company.vue'
  import Department from '@/views/msg/components/system/department.vue'
  import Miji from '@/views/msg/components/system/miji.vue'
  import Right from '@/views/msg/components/system/menu.vue'
  import GrantModel from '@/views/msg/components/system/grantModel.vue'
  import Role from '@/views/msg/components/system/role.vue'
  import MemuSQ from '@/views/msg/components/system/memuSQ.vue'
  export default {
    name: 'SystemInfo',
    components: {
      UserFlow,
      ResetPassword,
      Company,
      Department,
      Miji,
      Right,
      GrantModel,
      Role,
      MemuSQ,
    },
    data() {
      return {
        formData: {
          name: '',
          targetType: '',
        },
        rules: {},
        fromId: '',
        flowtaskinfoflowid: '',
        ymFromId: '',
        status: '',
        fromIdcopy: '',
        targetType: '', // 流程类型
        title: '', // 流程标题
        operationType: {
          1: '新增',
          2: '修改',
          3: '删除',
          4: '启用',
          5: '弃用',
          6: '密码重置',
          7: '用户授权',
          8: '公司授权',
          9: '用户取消授权',
          10: '公司取消授权',
          11: '菜单授权',
          12: '数据授权',
          13: '取消数据授权',
        }, // 操作类型
        createTime: '',
        username: '',
        isResetPassword: false, //是否是重置密码流程
        isDelete: false, //是否是删除流程
        operationTypeNum: 0,
      }
    },
    methods: {
      handleSave() {
        console.log(this.form)
      },
      showEdit(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status
      ) {
        console.log("🚀 ~ formId:", formId)
        // 查询当前流程信息，是哪个类型的流程
        getSystemInfo({
          recordId: formId,
        }).then((res) => {
          this.targetType = res.data.primaryInfo.targetType
          this.username = res.data.primaryInfo.creatorName
          this.createTime = res.data.primaryInfo.creationTime
          this.isResetPassword = res.data.primaryInfo.operationType == 6 //是否是重置密码
          this.isDelete =
            res.data.primaryInfo.operationType == 3 ||
            res.data.primaryInfo.operationType == 4 ||
            res.data.primaryInfo.operationType == 5 //是否是删除，是否启用，是否弃用

          const oldData = res.data.beforeData || {}
          const newData = JSON.parse(res.data.primaryInfo.operationData) || {}
          this.operationTypeNum = res.data.primaryInfo.operationType
          // 用户类型
          if (
            this.targetType == 'user' &&
            res.data.primaryInfo.operationType != 6
          ) {
            this.title =
              '用户信息' +
              this.operationType[res.data.primaryInfo.operationType]
            this.$nextTick(() => {
              this.$refs['userFlow'].showEdit(
                title,
                formId,
                flowtaskinfoflowid,
                ymFromId,
                isWfqdedit,
                status,
                oldData,
                newData,
                this.operationType[res.data.primaryInfo.operationType]
              )
            })
          }
          if (
            this.targetType == 'user' &&
            res.data.primaryInfo.operationType == 6
          ) {
            this.title = '用户重置密码'
            this.$nextTick(() => {
              this.$refs['resetPassword'].showEdit(
                title,
                formId,
                flowtaskinfoflowid,
                ymFromId,
                isWfqdedit,
                status,
                res.data.primaryInfo.operationMemo
              )
            })
          }
          if (this.targetType == 'company') {
            this.title =
              '公司信息' +
              this.operationType[res.data.primaryInfo.operationType]
            this.$nextTick(() => {
              this.$refs['company'].showEdit(
                title,
                formId,
                flowtaskinfoflowid,
                ymFromId,
                isWfqdedit,
                status,
                oldData,
                newData,
                this.operationType[res.data.primaryInfo.operationType]
              )
            })
          }
          if (
            this.targetType == 'company' &&
            (res.data.primaryInfo.operationType == 4 ||
              res.data.primaryInfo.operationType == 5 ||
              res.data.primaryInfo.operationType == 3)
          ) {
            this.title =
              '公司信息' +
              this.operationType[res.data.primaryInfo.operationType]
            this.$nextTick(() => {
              // 删除流程,走纯展示的内容页，也就是重置密码的页面
              this.$refs['resetPassword'].showEdit(
                title,
                formId,
                flowtaskinfoflowid,
                ymFromId,
                isWfqdedit,
                status,
                res.data.primaryInfo.operationMemo
              )
            })
          }
          if (this.targetType == 'dept') {
            this.title =
              '部门信息' +
              this.operationType[res.data.primaryInfo.operationType]
            this.$nextTick(() => {
              this.$refs['department'].showEdit(
                title,
                formId,
                flowtaskinfoflowid,
                ymFromId,
                isWfqdedit,
                status,
                oldData,
                newData,
                this.operationType[res.data.primaryInfo.operationType]
              )
            })
          }
          if (
            this.targetType == 'dept' &&
            (res.data.primaryInfo.operationType == 3 ||
              res.data.primaryInfo.operationType == 4 ||
              res.data.primaryInfo.operationType == 5)
          ) {
            this.title =
              '部门信息' +
              this.operationType[res.data.primaryInfo.operationType]
            this.$nextTick(() => {
              // 删除流程,走纯展示的内容页，也就是重置密码的页面
              this.$refs['resetPassword'].showEdit(
                title,
                formId,
                flowtaskinfoflowid,
                ymFromId,
                isWfqdedit,
                status,
                res.data.primaryInfo.operationMemo
              )
            })
          }
          if (this.targetType == 'secrect') {
            this.title =
              '密级信息' +
              this.operationType[res.data.primaryInfo.operationType]
            this.$nextTick(() => {
              this.$refs['miji'].showEdit(
                title,
                formId,
                flowtaskinfoflowid,
                ymFromId,
                isWfqdedit,
                status,
                oldData,
                newData,
                this.operationType[res.data.primaryInfo.operationType]
              )
            })
          }
          if (
            this.targetType == 'secrect' &&
            res.data.primaryInfo.operationType == 3
          ) {
            this.title =
              '密级信息' +
              this.operationType[res.data.primaryInfo.operationType]
            this.$nextTick(() => {
              // 删除流程,走纯展示的内容页，也就是重置密码的页面
              this.$refs['resetPassword'].showEdit(
                title,
                formId,
                flowtaskinfoflowid,
                ymFromId,
                isWfqdedit,
                status,
                res.data.primaryInfo.operationMemo
              )
            })
          }
          if (this.targetType == 'right') {
            this.title =
              '菜单' + this.operationType[res.data.primaryInfo.operationType]
            this.$nextTick(() => {
              this.$refs['right'].showEdit(
                title,
                formId,
                flowtaskinfoflowid,
                ymFromId,
                isWfqdedit,
                status,
                oldData,
                newData,
                this.operationType[res.data.primaryInfo.operationType]
              )
            })
          }
          if (
            this.targetType == 'right' &&
            (res.data.primaryInfo.operationType == 4 ||
              res.data.primaryInfo.operationType == 5 ||
              res.data.primaryInfo.operationType == 3)
          ) {
            this.title =
              '菜单' + this.operationType[res.data.primaryInfo.operationType]
            this.$nextTick(() => {
              // 删除流程,走纯展示的内容页，也就是重置密码的页面
              this.$refs['resetPassword'].showEdit(
                title,
                formId,
                flowtaskinfoflowid,
                ymFromId,
                isWfqdedit,
                status,
                res.data.primaryInfo.operationMemo
              )
            })
          }
          if (
            this.targetType == 'commonflow' &&
            (res.data.primaryInfo.operationType == 4 ||
              res.data.primaryInfo.operationType == 5 ||
              res.data.primaryInfo.operationType == 3)
          ) {
            this.title =
              '通用流程' +
              this.operationType[res.data.primaryInfo.operationType]
            this.$nextTick(() => {
              // 删除流程,走纯展示的内容页，也就是重置密码的页面
              this.$refs['resetPassword'].showEdit(
                title,
                formId,
                flowtaskinfoflowid,
                ymFromId,
                isWfqdedit,
                status,
                res.data.primaryInfo.operationMemo
              )
            })
          }
          if (
            this.targetType == 'contractflow' &&
            (res.data.primaryInfo.operationType == 4 ||
              res.data.primaryInfo.operationType == 5 ||
              res.data.primaryInfo.operationType == 3)
          ) {
            this.title =
              '合同流程' +
              this.operationType[res.data.primaryInfo.operationType]
            this.$nextTick(() => {
              // 删除流程,走纯展示的内容页，也就是重置密码的页面
              this.$refs['resetPassword'].showEdit(
                title,
                formId,
                flowtaskinfoflowid,
                ymFromId,
                isWfqdedit,
                status,
                res.data.primaryInfo.operationMemo
              )
            })
          }
          if (
            this.targetType == 'grant' &&
            (res.data.primaryInfo.operationType == 7 ||
              res.data.primaryInfo.operationType == 8 ||
              res.data.primaryInfo.operationType == 9 ||
              res.data.primaryInfo.operationType == 10)
          ) {
            this.title = this.operationType[res.data.primaryInfo.operationType]
            this.$nextTick(() => {
              this.$refs['grant'].showEdit(
                title,
                formId,
                flowtaskinfoflowid,
                ymFromId,
                isWfqdedit,
                status,
                res.data.primaryInfo
              )
            })
          }
          if (this.targetType == 'role') {
            this.title =
              '角色' + this.operationType[res.data.primaryInfo.operationType]
            this.$nextTick(() => {
              this.$refs['role'].showEdit(
                title,
                formId,
                flowtaskinfoflowid,
                ymFromId,
                isWfqdedit,
                status,
                oldData,
                newData,
                this.operationType[res.data.primaryInfo.operationType],
                res.data.primaryInfo.operationMemo
              )
            })
          }
          if (
            this.targetType == 'role' &&
            (res.data.primaryInfo.operationType == 4 ||
              res.data.primaryInfo.operationType == 5 ||
              res.data.primaryInfo.operationType == 3)
          ) {
            this.title =
              '角色' + this.operationType[res.data.primaryInfo.operationType]
            this.$nextTick(() => {
              // 删除流程,走纯展示的内容页，也就是重置密码的页面
              this.$refs['resetPassword'].showEdit(
                title,
                formId,
                flowtaskinfoflowid,
                ymFromId,
                isWfqdedit,
                status,
                res.data.primaryInfo.operationMemo
              )
            })
          }
          if (
            this.targetType == 'grant' &&
            res.data.primaryInfo.operationType == 11
          ) {
            this.title =
              '角色' + this.operationType[res.data.primaryInfo.operationType]
            this.$nextTick(() => {
              this.$refs['MemuSQ'].showEdit(
                title,
                formId,
                flowtaskinfoflowid,
                ymFromId,
                isWfqdedit,
                status,
                res.data.primaryInfo
              )
            })
          }
          if (
            this.targetType == 'grant' &&
            res.data.primaryInfo.operationType == 12
          ) {
          }
        })
      },
    },
  }
</script>

<style lang="scss" scoped>
  /* 如果你的 style 是 scoped 的 */
  ::v-deep .el-input__inner {
    border: none !important;
    box-shadow: none !important;
  }

  /* 如果不是 scoped，可以直接 */
  .el-input__inner {
    border: none !important;
    box-shadow: none !important;
  }
</style>
