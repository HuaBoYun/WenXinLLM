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
          <el-form-item label="申请人" prop="applyName">
            <el-input
              v-model="formData.applyName"
              :style="{ width: '75%' }"
              disabled
              placeholder="请选择申请人"
            />
            <el-button
              type="primary"
              style="margin-left: 20px"
              @click="$refs.executor.showEdit()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="部门" prop="applyWorkUnitName">
            <el-input
              v-model="formData.applyWorkUnitName"
              :style="{ width: '100%' }"
              disabled
              placeholder="请选择部门"
            />
            <!-- <el-button type="primary" style="margin-left: 20px">选择</el-button> -->
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="申请角色" prop="applyRole">
            <el-input
              v-model="formData.applyRole"
              :style="{ width: '100%' }"
              placeholder="请输入申请角色"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="申请时间" prop="applyTime">
            <el-input
              v-model="formData.applyTime"
              :style="{ width: '100%' }"
              disabled
              placeholder="请选择申请时间"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="情况说明" prop="remark">
            <el-input
              v-model="formData.remark"
              :style="{ width: '100%' }"
              placeholder="请输入情况说明"
              type="textarea"
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
    <!-- 人 -->
    <executor-options ref="executor" @projectManage="handleExecutorSelected" />
    <!-- 部门 -->
    <department-options ref="department" @selected="handleDepartmentSelected" />
  </div>
</template>

<script>
  import { baseURL } from '@/config'
  import store from '@/store'
  import DepartmentOptions from '@/views/oilAudit/report/components/options/department.vue'
  import ExecutorOptions from '@/components/danxuanPerson.vue'
  import { editInfor, getInfoDetail } from '@/oapi/ypns_zhgl/qxsqd.js'
  import { getUserDetailInfo } from '@/oapi/ypns_zhgl/filePublic'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'

  const token = store.getters['user/token']

  export default {
    components: { DepartmentOptions, ExecutorOptions, Resubmit },
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
          applyId: '',
          applyRole: '',
          applyName: '',
          applyTime: '',
          applyWorkUnit: '',
          applyWorkUnitName: '',
          remark: '',
          id: '',
        },
        footer: true,
        rules: {
          applyName: [
            {
              required: true,
              message: '请选择申请人',
              trigger: ['blur', 'change'],
            },
          ],
          applyRole: [
            {
              required: true,
              message: '请输入角色',
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
      // 人员选择
      async handleExecutorSelected(node) {
        this.$set(this.formData, 'applyName', node[0].realname)
        this.$set(this.formData, 'applyId', node[0].staffid)
        const res = await getUserDetailInfo({ id: node[0].staffid })
        this.formData.applyWorkUnitName = res.data.workUnitName
        this.$forceUpdate()
      },
      // 部门选择
      handleDepartmentSelected(node) {
        this.$set(this.formData, `applyWorkUnitName`, node.label)
        this.$set(this.formData, `applyWorkUnit`, node.id)
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
          this.formData.id = res.data.id
          this.formData.applyId = res.data.applyId
          this.formData.applyRole = res.data.applyRole
          this.formData.applyName = res.data.applyName
          this.formData.applyTime = res.data.applyTime
          this.formData.applyWorkUnit = res.data.applyWorkUnit
          this.formData.applyWorkUnitName = res.data.applyWorkUnitName
          this.formData.remark = res.data.remark
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
          applyId: '',
          applyRole: '',
          applyName: '',
          applyTime: '',
          applyWorkUnit: '',
          applyWorkUnitName: '',
          remark: '',
          id: '',
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
