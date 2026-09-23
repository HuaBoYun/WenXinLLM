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
        <el-col :span="24">
          <el-form-item label="服务类型" prop="serviceType">
            <el-radio-group v-model="formData.serviceType">
              <el-radio label="新办证书">新办证书</el-radio>
              <el-radio label="证书冻结">证书冻结</el-radio>
              <el-radio label="证书解冻">证书解冻</el-radio>
              <el-radio label="证书更新">证书更新</el-radio>
              <el-radio label="证书注销">证书注销</el-radio>
              <el-radio label="USBKey解锁">USBKey解锁</el-radio>
              <el-radio label="USBKey丢失补办">USBKey丢失补办</el-radio>
              <el-radio label="USBKey损坏补办">USBKey损坏补办</el-radio>
              <el-radio label="授权码更新">授权码更新</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="用户姓名" prop="userName">
            <el-input
              v-model="formData.userName"
              clearable
              placeholder="请输入用户姓名"
              :style="{ width: '75%' }"
            />
            <el-button
              type="primary"
              style="margin-left: 20px"
              @click="$refs.manage.showEdit()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="电子邮箱" prop="email">
            <el-input
              v-model="formData.email"
              clearable
              placeholder="电子邮箱"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="员工编号" prop="staffCode">
            <el-input
              v-model="formData.staffCode"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入员工编号"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="组织机构名称" prop="organizationName">
            <el-input
              v-model="formData.organizationName"
              clearable
              placeholder="请输入组织机构名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="组织机构编码" prop="organizationNum">
            <el-input
              v-model="formData.organizationNum"
              clearable
              placeholder="请输入组织机构编码"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系电话" prop="contactPhone">
            <el-input
              v-model="formData.contactPhone"
              clearable
              placeholder="请输入联系电话"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="备注" :style="{ height: '28px' }">
            <el-input
              v-model="formData.remark"
              :style="{ width: '100%' }"
              placeholder="请输入备注"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否代办" prop="isAgency">
            <el-radio-group v-model="formData.isAgency">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="0">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="代办人姓名"
            prop="agencyName"
            v-if="formData.isAgency == 1"
          >
            <el-input
              v-model="formData.agencyName"
              :style="{ width: '100%' }"
              placeholder="请输入代办人姓名"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="代办人邮箱"
            prop="agencyMail"
            v-if="formData.isAgency == 1"
          >
            <el-input
              v-model="formData.agencyMail"
              :style="{ width: '100%' }"
              placeholder="请输入代办人邮箱"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="代办人单位"
            prop="agencyBelongGroup"
            v-if="formData.isAgency == 1"
          >
            <el-input
              v-model="formData.agencyBelongGroup"
              :style="{ width: '100%' }"
              placeholder="请输入代办人单位"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="代办人电话"
            prop="agencyContactPhone"
            v-if="formData.isAgency == 1"
          >
            <el-input
              v-model="formData.agencyContactPhone"
              :style="{ width: '100%' }"
              placeholder="请输入代办人电话"
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
  </div>
</template>

<script>
  import { baseURL } from '@/config'
  import store from '@/store'
  import projectManage from '@/components/danxuanPerson.vue'
  import { editInfor, getInfoDetail } from '@/oapi/ypns_zhgl/szzsgl'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'

  const token = store.getters['user/token']

  export default {
    components: { projectManage, Resubmit },
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
          id: '',
          serviceType: '',
          userName: '',
          email: '',
          staffCode: '',
          organizationName: '',
          organizationNum: '',
          contactPhone: '',
          remark: '',
          isAgency: '',
          agencyContactPhone: '',
          agencyBelongGroup: '',
          agencyMail: '',
          agencyName: '',
        },
        radio: '',
        footer: true,
        rules: {
          agencyContactPhone: [
            {
              required: true,
              message: '请输入代办人电话',
              trigger: 'blur',
            },
          ],
          agencyBelongGroup: [
            {
              required: true,
              message: '请输入代办人单位',
              trigger: 'blur',
            },
          ],
          agencyMail: [
            {
              required: true,
              message: '请输入代办人邮箱',
              trigger: 'blur',
            },
          ],
          agencyName: [
            {
              required: true,
              message: '请输入代办人姓名',
              trigger: 'blur',
            },
          ],
          staffCode: [
            {
              required: true,
              message: '请输入员工编号',
              trigger: 'blur',
            },
          ],
          email: [
            {
              required: true,
              message: '请输入电子邮件',
              trigger: 'blur',
            },
          ],
          userName: [
            {
              required: true,
              message: '请输入用户姓名',
              trigger: 'blur',
            },
          ],
          organizationName: [
            {
              required: true,
              message: '请输入组织机构名称',
              trigger: 'blur',
            },
          ],
          remark: [
            {
              required: true,
              message: '请输入备注',
              trigger: 'blur',
            },
          ],
          serviceType: [
            {
              required: true,
              message: '请选择服务类型',
              trigger: ['blur', 'change'],
            },
          ],
          isAgency: [
            {
              required: true,
              message: '请选择是否代办',
              trigger: ['blur', 'change'],
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

          this.formData.serviceType = res.data.serviceType
          this.formData.userName = res.data.userName
          this.formData.email = res.data.email
          this.formData.staffCode = res.data.staffCode
          this.formData.organizationName = res.data.organizationName
          this.formData.organizationNum = res.data.organizationNum
          this.formData.contactPhone = res.data.contactPhone
          this.formData.remark = res.data.remark
          this.formData.isAgency = res.data.isAgency
          this.formData.agencyContactPhone = res.data.agencyContactPhone
          this.formData.agencyBelongGroup = res.data.agencyBelongGroup
          this.formData.agencyMail = res.data.agencyMail
          this.formData.agencyName = res.data.agencyName
          this.formData.id = res.data.id
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
          serviceType: '',
          userName: '',
          email: '',
          staffCode: '',
          organizationName: '',
          organizationNum: '',
          contactPhone: '',
          remark: '',
          isAgency: '',
          id: '',
          agencyContactPhone: '',
          agencyBelongGroup: '',
          agencyMail: '',
          agencyName: '',
        }
        this.$bus.$emit('updateMsg', 0)
        this.footer = true
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            // return
            const params = JSON.parse(JSON.stringify(this.formData))
            const res = await editInfor(params)
            if (res && res.code == 200) {
              this.$emit('fetchData')
              this.$message({
                message: '保存成功！',
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
        this.$set(this.formData, 'userName', val[0].realname)
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
  .el-radio {
    margin-top: 10px;
    width: 110px;
  }
</style>
