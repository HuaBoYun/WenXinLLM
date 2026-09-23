<template>
  <div>
    <el-row :gutter="14" v-loading="loading">
      <el-form
        ref="ruleForm"
        label-width="110px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="!footer"
      >
        <el-col :span="12">
          <el-form-item label="编号" prop="no">
            <el-input
              v-model="formData.no"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入邮箱"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="姓名" prop="staffName">
            <el-input
              v-model="formData.staffName"
              clearable
              disabled
              placeholder="请输入姓名"
              style="width: 75%"
            />
            <el-button
              type="primary"
              style="margin-left: 10px"
              @click="$refs.executor.showEdit()"
              size="mini"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="所在单位" prop="thisBelongGroupName">
            <el-input
              v-model="formData.thisBelongGroupName"
              :style="{ width: '75%' }"
              disabled
              clearable
              placeholder="请选择单位"
            />
            <el-button
              type="primary"
              style="margin-left: 20px"
              @click="handleObject('dept')"
              size="mini"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="邮箱" prop="mail">
            <el-input
              v-model="formData.mail"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入邮箱"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="部门" prop="workUnitName">
            <el-input
              v-model="formData.workUnitName"
              clearable
              placeholder="请选择部门"
              :style="{ width: '100%', height: '27px' }"
              disabled
            />
            <!-- <el-button
              type="primary"
              style="margin-left: 10px"
              @click="showDept('dept')"
            >
              选择
            </el-button> -->
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="有效期限" prop="time">
            <el-date-picker
              v-model="formData.time"
              type="daterange"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="岗位" prop="post">
            <el-input
              v-model="formData.post"
              clearable
              placeholder="请输入岗位"
              :style="{ width: '100%' }"
            />
            <!-- <el-button
              type="primary"
              style="margin-left: 10px"
              @click="showDept('job')"
            >
              选择
            </el-button> -->
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="联系电话" prop="mobilePhone">
            <el-input
              v-model="formData.mobilePhone"
              clearable
              placeholder="请输入联系电话"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="员工编号" prop="staffNumber">
            <el-input
              v-model="formData.staffNumber"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入员工编号"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="服务类型" prop="serviceType">
            <el-select
              v-model="formData.serviceType"
              placeholder="请选择服务类型"
              style="width: 100%"
            >
              <el-option label="开通" value="开通"></el-option>
              <el-option label="延期" value="延期"></el-option>
              <el-option label="注销" value="注销"></el-option>
              <el-option label="权限变更" value="权限变更"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="申请日期" prop="applyTime">
            <el-date-picker
              v-model="formData.applyTime"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="选择日期"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="申请原因" prop="remark">
            <el-input
              v-model="formData.remark"
              :style="{ width: '100%' }"
              clearable
              :rows="4"
              type="textarea"
              placeholder="请输入申请原因"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" style="text-align: right" v-if="footer">
      <el-button @click="save" type="primary">确定</el-button>
      <el-button @click="ymsubmit" type="primary">提交</el-button>
    </div>
    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
    <project-manage
      @projectManage="getChildlistPro"
      ref="manage"
    ></project-manage>
    <!-- 选择人员弹窗 -->
    <executor-options ref="executor" @projectManage="handleExecutorSelected" />
    <!-- 选择部门弹窗 -->
    <department-options ref="department" @selected="handleDepartmentSelected" />
    <Company ref="audiTree" @submit="getChildlistObj"></Company>
  </div>
</template>

<script>
  import { baseURL } from '@/config'
  import store from '@/store'
  import projectManage from '@/views/audit/project/components/formComponents/projectManage.vue'
  import DepartmentOptions from '@/views/oilAudit/report/components/options/department.vue'
  import ExecutorOptions from '@/components/danxuanPerson.vue'
  import { editInfor, getInfoDetail } from '@/oapi/ypns_zhgl/vpn.js'
  import { getUserDetailInfo } from '@/oapi/ypns_zhgl/filePublic'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import Company from '@/views/oilAudit/jhlx/components/department.vue'
  const token = store.getters['user/token']

  export default {
    components: {
      projectManage,
      DepartmentOptions,
      ExecutorOptions,
      Resubmit,
      Company,
    },
    inheritAttrs: false,
    props: ['fetchData'],
    data() {
      return {
        loading: false,
        baseURL: baseURL,
        uploadApi: '/audit/fileManage/upload',
        headers: { token: token },
        tableData: [],
        formData: {
          mail: '',
          mobilePhone: '',
          serviceType: '',
          staffNumber: '',
          staffId: '',
          workUnitName: '',
          post: '',
          staffName: '',
          remark: '',
          applyTime: '',
          id: '',
          no: '',
          time: [],
          thisBelongGroupName: '',
          thisBelongGroup: '',
        },
        deptType: '',
        footer: true,
        rules: {
          mail: [
            {
              required: true,
              message: '请输入邮箱',
              trigger: 'blur',
            },
          ],
          staffName: [
            {
              required: true,
              message: '请选择姓名',
              trigger: ['blur', 'change'],
            },
          ],
          mobilePhone: [
            {
              required: true,
              message: '请输入手机号码',
              trigger: 'blur',
            },
          ],
          staffNumber: [
            {
              required: true,
              message: '请输入员工编号',
              trigger: 'blur',
            },
          ],
          post: [
            {
              required: true,
              message: '请输入岗位',
              trigger: 'blur',
            },
          ],
          serviceType: [
            {
              required: true,
              message: '请输入服务类型',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        fromId: '',
        flowtaskinfoflowid: '',
        ymFromId: '',
        status: '',
        fromIdcopy: '',
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      //提交审批
      async ymsubmit() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            this.$refs.resubmit.ymsubmit()
          }
        })
      },
      showDept(type) {
        this.deptType = type
        this.$refs.department.show()
      },
      // 选择人员
      async handleExecutorSelected(node) {
        this.$set(this.formData, 'staffName', node[0].realname)
        this.$set(this.formData, 'staffId', node[0].staffid)
        const res = await getUserDetailInfo({ id: node[0].staffid })
        this.formData.workUnitName = res.data.workUnitName
        this.$forceUpdate()
      },
      // 选择部门
      handleDepartmentSelected(node) {
        if (this.deptType === 'dept') {
          this.$set(this.formData, `sealWorkUnitName`, node.label)
          this.$set(this.formData, `sealWorkUnit`, node.id)
        } else if (this.deptType === 'job') {
          this.$set(this.formData, `auditWorkUnitName`, node.label)
          this.$set(this.formData, `auditWorkUnit`, node.id)
        }
        this.$forceUpdate()
      },
      getCurrentDate() {
        return new Date(+new Date() + 8 * 3600 * 1000)
          .toJSON()
          .substr(0, 19)
          .replace('T', ' ')
      },
      async showEdit(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status
      ) {
        if (formId) {
          const res = await getInfoDetail({ id: formId })
          this.formData.mail = res.data.mail
          this.formData.mobilePhone = res.data.mobilePhone
          this.formData.serviceType = res.data.serviceType
          this.formData.staffNumber = res.data.staffNumber
          this.formData.remark = res.data.remark
          this.formData.applyTime = res.data.applyTime
          this.formData.time = [
            res.data.effectiveStartTime,
            res.data.effectiveEndTime,
          ]
          this.formData.no = res.data.no
          this.formData.thisBelongGroupName = res.data.thisBelongGroupName
          this.formData.thisBelongGroup = res.data.thisBelongGroup
          this.formData.id = res.data.id
          this.formData.workUnitName = res.data.staff.workUnitName
          this.formData.post = res.data.post
          this.formData.staffName = res.data.staff.realName
          // this.formData = JSON.parse(JSON.stringify(row))
        }
        this.footer = isWfqdedit
        this.fromId = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
        if (title == 'detail') {
          this.footer = false
        }
      },
      close() {
        this.loading = false
        this.formData = {
          mail: '',
          mobilePhone: '',
          serviceType: '',
          staffNumber: '',
          staffId: '',
          workUnitName: '',
          post: '',
          staffName: '',
          remark: '',
          applyTime: '',
          id: '',
          no: '',
          time: [],
          thisBelongGroupName: '',
          thisBelongGroup: '',
        }
        this.$bus.$emit('updateMsg', 0)
        this.footer = true
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            // return
            const params = JSON.parse(JSON.stringify(this.formData))
            params.effectiveStartTime = this.formData.time[0]
            params.effectiveEndTime = this.formData.time[1]
            delete params.time
            const res = await editInfor(params)
            if (res && res.code == 200) {
              this.$emit('fetchData')
              this.$message({
                message: '提交成功！',
                type: 'success',
              })
            } else {
              this.$message({
                message: '提交失败',
                type: 'error',
              })
            }
          }
        })
      },
      async checkLink() {
        return
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const res = await LinkTest({
              dataBaseConnectionAddress:
                this.formData.dataBaseConnectionAddress,
              dataBasePassWord: this.formData.dataBasePassWord,
              dataBaseType: this.formData.dataBaseType,
              dataBaseUsers: this.formData.dataBaseUsers,
            })
            if (res.code == 1) {
              this.$message({
                message: res.msg,
                type: 'success',
              })
            } else {
              this.$message({
                message: res.msg,
                type: 'error',
              })
            }
          }
        })
      },
      projectManager() {
        this.$refs['manage'].showEdit()
      },
      async getChildlistPro(val) {
        this.staffid = val[0].staffid
        this.$set(this.formData, 'orgName', val[0].realname)
      },
      handleObject() {
        this.$refs['audiTree'].showEdit()
      },
      getChildlistObj(val) {
        this.$set(this.formData, 'thisBelongGroup', val.id)
        this.$set(this.formData, 'thisBelongGroupName', val.label)
      },
    },
  }
</script>
<style scoped>
  .el-form-item__contractname span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
