<template>
  <div>
    <el-row :gutter="14" v-loading="loading">
      <el-form
        ref="ruleForm"
        label-width="135px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="!footer"
      >
        <el-col :span="12">
          <el-form-item label="审计结果确认单" prop="resultcode">
            <el-input
              v-model="formData.resultcode"
              :style="{ width: '266px' }"
              clearable
              placeholder="请选择审计结果确认单"
              disabled
            />
            <el-button
              type="primary"
              style="margin-left: 10px"
              @click="handleChoicePlan"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计项目名称" prop="projectName">
            <el-input
              v-model="formData.projectName"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入审计项目名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被审计单位" prop="auditOrgName">
            <el-input
              v-model="formData.auditOrgName"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入审计项目名称"
              disabled
            />

            <!-- <el-input
              v-model="formData.auditOrgName"
              clearable
              placeholder="请选择分包单位"
              :style="{ width: '266px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="openDep('auditOrgName')"
              size="small"
            >
              选择
            </el-button> -->
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="追款金额（万元）" prop="money">
            <el-input
              v-model="formData.money"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入追款金额（万元）"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="追款相对方" prop="zkOrgName">
            <el-input
              v-model="formData.zkOrgName"
              :style="{ width: '80%' }"
              disabled
              clearable
              placeholder="请输入追款相对方"
            />
            <el-button
              type="primary"
              style="margin-left: 10px"
              @click="$refs.department.show()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="追款事由（逐项填写）" prop="reason">
            <el-input
              v-model="formData.reason"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入追款事由（逐项填写）"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="remark">
            <el-input
              v-model="formData.remark"
              :style="{ width: '100%' }"
              clearable
              placeholder="备注"
              type="textarea"
              :rows="6"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <!-- <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
    </div> -->
    <div style="text-align: right; margin-top: 10px" v-if="footer">
      <!-- <el-button @click="close">取 消</el-button> -->
      <el-button type="primary" @click="save">确 定</el-button>
      <el-button type="primary" @click="ymsubmit">提 交</el-button>
    </div>

    <!-- 计划项目子组件 -->
    <auditResult ref="result" @resultList="getChildlist" />

    <!-- 部门 -->
    <DepartmentOptions ref="department" @selected="handleDepartmentSelected" />

    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
  </div>
</template>

<script>
import { baseURL } from '@/config'
import store from '@/store'
import {
  auditProjectZkDetail,
  auditProjectZkSaveOrUpdate,
} from '@/oapi/audit/report'
// import { resultList } from '@/oapi/audit/implement'
import DepartmentOptions from '@/views/oilAudit/report/components/options/department.vue'
import auditResult from '@/views/oilAudit/report/components/options/auditResult.vue'
import Resubmit from '@/views/msg/components/options/Resubmit.vue'
const token = store.getters['user/token']

export default {
  components: { DepartmentOptions, auditResult, Resubmit },
  inheritAttrs: false,
  props: ['fetchData'],
  data() {
    return {
      loading: false,
      baseApi: baseURL,
      api: '/oiaudit/fileManage/upload',
      headers: { token: token },
      tableData: [],
      formData: {
        id: '',
        resultcode: '',
        projectName: '',
        auditOrgName: '',
        auditOrgName: '',
        reason: '',
        money: '',
        zkOrgId: '',
        remark: '',
        resultId: '',
      },
      radio: '',
      footer: true,
      rules: {
        resultcode: [
          {
            required: true,
            message: '请选择审计结果确认单',
            trigger: 'blur',
          },
        ],
        projectName: [
          {
            required: true,
            message: '请输入审计项目名称',
            trigger: 'blur',
          },
        ],
        auditOrgName: [
          {
            required: true,
            message: '请选择被审计单位',
            trigger: 'blur',
          },
        ],
        reason: [
          {
            required: true,
            message: '请输入追款事由',
            trigger: 'blur',
          },
        ],
        money: [
          {
            required: true,
            message: '请输入追款金额',
            trigger: 'blur',
          },
        ],
        zkOrgId: [
          {
            required: true,
            message: '请输入追款相对方',
            trigger: 'blur',
          },
        ],
      },
      dialogFormVisible: false,
      title: '新增',
      depType: '',
      // 流程相关
      fromId: '',
      fromIdcopy: '',
      flowtaskinfoflowid: '',
      ymFromId: '',
      status: '',
    }
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  methods: {
    async ymsubmit() {
      this.$refs['ruleForm'].validate(async (valid) => {
        if (valid) {
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
    openDep(type) {
      this.depType = type
      this.$refs.department.show()
    },
    handleDepartmentSelected(node) {
      this.$set(this.formData, this.depType, node.name)
      this.$set(this.formData, `auditOrgId`, node.id)
    },
    async handleChoicePlan() {
      this.$refs['result'].showEdit()
    },
    getChildlist(val) {
      this.formData.resultId = val[0].projectid || ''
      this.formData.auditOrgName = val[0].orgidnames || ''
      this.formData.resultcode = val[0].projectname || ''
      this.formData.auditOrgId = val[0].orgids || ''
      this.formData.reason = val[0].overview || ''
    },
    async showEdit(
      title,
      formId,
      flowtaskinfoflowid,
      ymFromId,
      isWfqdedit,
      status
    ) {
      this.dialogFormVisible = true
      // 流程相关
      this.fromId = formId
      this.fromIdcopy = formId
      this.flowtaskinfoflowid = flowtaskinfoflowid
      this.ymFromId = ymFromId
      this.status = status
      if (formId) {
        const res = await auditProjectZkDetail({ id: formId })
        Object.assign(this.formData, res.data.data)
        this.formData.auditOrgName = res.data.data?.auditOrg?.orgname || ''
        this.formData.resultcode = res.data.data?.result?.resultcode || ''
        this.formData.resultId = res.data.data?.resultId || ''
      }

      if (title == 'edit') {
        this.title = '编辑'
      } else if (title == 'detail') {
        this.title = '详细'
        this.footer = false
      } else if (title == 'add') {
        this.title = '新增'
        let resL = JSON.parse(localStorage.getItem('userInfo')).realname
        this.formData = {
          ...this.formData,
          // createdUser: resL,
          // createdTime: this.getCurrentDate(),
        }
      }
    },
    close() {
      this.$bus.$emit('updateMsg', 0)
      this.formData = {
        id: '',
        resultcode: '',
        projectName: '',
        projectId: '',
        auditOrgName: '',
        auditOrgName: '',
        reason: '',
        money: '',
        zkOrgId: '',
        remark: '',
      }
      this.tableData = []
      this.dialogFormVisible = false
      this.footer = true
    },
    async save() {
      this.$refs['ruleForm'].validate(async (valid) => {
        if (valid) {
          let params = { ...this.formData }
          console.log(params)
          const data = await auditProjectZkSaveOrUpdate({
            ...params,
          })
          if (data.code == 1) {
            this.$baseMessage(data.msg, 'success')
            this.$emit('fetchData')
            // this.close()
          } else {
            this.$baseMessage(data.msg, 'error')
          }
        }
      })
    },
    async checkLink() {
      return
      this.$refs['ruleForm'].validate(async (valid) => {
        if (valid) {
          const res = await LinkTest({
            dataBaseConnectionAddress: this.formData.dataBaseConnectionAddress,
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
