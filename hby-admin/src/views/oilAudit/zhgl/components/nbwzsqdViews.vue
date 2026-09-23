<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    v-if="dialogFormVisible"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="135px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="!footer"
      >
        <el-col :span="12">
          <el-form-item label="信息标题" prop="infoTitle">
            <el-input
              v-model="formData.infoTitle"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入信息标题"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="提供单位负责人" prop="providerPeopleName">
            <el-input
              v-model="formData.providerPeopleName"
              :style="{ width: '75%' }"
              clearable
              disabled
              placeholder="选择提供单位负责人"
            />
            <el-button
              type="primary"
              style="margin-left: 20px"
              @click="selectPeople('provider')"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系人" prop="contactName">
            <el-input
              v-model="formData.contactName"
              :style="{ width: '75%' }"
              clearable
              disabled
              placeholder="请选联系人"
            />
            <el-button
              type="primary"
              style="margin-left: 20px"
              @click="selectPeople('contact')"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="电话/传真" prop="fax">
            <el-input
              v-model="formData.fax"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入电话/传真"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="邮件地址" prop="mailAddress">
            <el-input
              v-model="formData.mailAddress"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入邮件地址"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="信息发布栏目" prop="infoReleaseColumn">
            <el-input
              v-model="formData.infoReleaseColumn"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入信息发布栏目"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="预期上网时间" prop="expectOnlineTime">
            <el-date-picker
              v-model="formData.expectOnlineTime"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="选择预期上网时间"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预期失效时间" prop="failureTime">
            <el-date-picker
              v-model="formData.failureTime"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="选择期失效时间"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="内容摘要" prop="contentAbstract">
            <el-input
              v-model="formData.contentAbstract"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入内容摘要"
              type="textarea"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary" :loading="loading">确定</el-button>
      <el-button
        @click="handleApproval"
        type="primary"
        :disabled="!this.editId"
      >
        提交审批
      </el-button>
    </div>
    <!-- 申请人 -->
    <executor-options ref="executor" @projectManage="handleExecutorSelected" />
    <!-- 部门 -->
    <department-options ref="department" @selected="handleDepartmentSelected" />
    <ProcessList ref="process" @fetchData="close" />
  </el-dialog>
</template>

<script>
  import { baseURL } from '@/config'
  import store from '@/store'
  import { editIp, getInfoDetail } from '@/oapi/ypns_zhgl/nbwzsqd'
  import DepartmentOptions from '@/views/oilAudit/report/components/options/department.vue'
  import ExecutorOptions from '@/components/danxuanPerson.vue'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'

  const token = store.getters['user/token']

  export default {
    components: {
      DepartmentOptions,
      ExecutorOptions,
      ProcessList,
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
          infoTitle: '',
          infoProviderWorkUnit: '',
          // infoProviderWorkUnitName: '',
          providerPeople: '',
          providerPeopleName: '',
          contact: '',
          contactName: '',
          fax: '',
          mailAddress: '',
          infoReleaseColumn: '',
          expectOnlineTime: '',
          failureTime: '',
          contentAbstract: '',
          id: '',
        },
        peopleType: '',
        radio: '',
        footer: true,
        rules: {
          infoTitle: [
            {
              required: true,
              message: '请输入信息标题',
              trigger: 'blur',
            },
          ],
          infoProviderWorkUnitName: [
            {
              required: true,
              message: '请选择信息提供单位',
              trigger: ['blur', 'change'],
            },
          ],
          providerPeopleName: [
            {
              required: true,
              message: '请选供单位负责人',
              trigger: ['blur', 'change'],
            },
          ],
          contactName: [
            {
              required: true,
              message: '请选联系人',
              trigger: ['blur', 'change'],
            },
          ],
          fax: [
            {
              required: true,
              message: '请输入电话/传真',
              trigger: 'blur',
            },
          ],
          // mailAddress: [
          //   {
          //     required: true,
          //     message: '请输入邮件地址',
          //     trigger: 'blur',
          //   },
          // ],
          infoReleaseColumn: [
            {
              required: true,
              message: '请输入信息发布栏目',
              trigger: 'blur',
            },
          ],
          expectOnlineTime: [
            {
              required: true,
              message: '选择预期上网时间',
              trigger: ['blur', 'change'],
            },
          ],
          failureTime: [
            {
              required: true,
              message: '选择期失效时间',
              trigger: ['blur', 'change'],
            },
          ],
          contentAbstract: [
            {
              required: true,
              message: '请输入内容摘要',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        editId: '',
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      selectPeople(type) {
        this.peopleType = type
        this.$refs.executor.showEdit()
      },
      handleExecutorSelected(node) {
        console.log(node, this.peopleType)
        if (this.peopleType === 'provider') {
          this.$set(this.formData, 'providerPeopleName', node[0].realname)
          this.$set(this.formData, 'providerPeople', node[0].staffid)
        } else if (this.peopleType === 'contact') {
          this.$set(this.formData, 'contactName', node[0].realname)
          this.$set(this.formData, 'contact', node[0].staffid)
        }
        this.$forceUpdate()
      },
      handleDepartmentSelected(node) {
        //保存名称
        this.$set(this.formData, `infoProviderWorkUnitName`, node.label)
        //保存名称对应的ID
        this.$set(this.formData, `infoProviderWorkUnit`, node.id)
      },
      getCurrentDate() {
        return new Date(+new Date() + 8 * 3600 * 1000)
          .toJSON()
          .substr(0, 19)
          .replace('T', ' ')
      },
      async showEdit(row, title) {
        this.dialogFormVisible = true
        if (row) {
          this.editId = row.id
          const res = await getInfoDetail({ id: row.id })
          this.formData.infoTitle = res.data.infoTitle
          this.formData.infoProviderWorkUnit = res.data.infoProviderWorkUnit
          // this.formData.infoProviderWorkUnitName =
          //   res.data.infoProviderWorkUnitName
          this.formData.providerPeople = res.data.providerPeople
          this.formData.providerPeopleName = res.data.providerPeopleName
          this.formData.contact = res.data.contact
          this.formData.contactName = res.data.contactName
          this.formData.fax = res.data.fax
          this.formData.mailAddress = res.data.mailAddress
          this.formData.infoReleaseColumn = res.data.infoReleaseColumn
          this.formData.expectOnlineTime = res.data.expectOnlineTime
          this.formData.failureTime = res.data.failureTime
          this.formData.contentAbstract = res.data.contentAbstract
          this.formData.id = res.data.id
          // this.formData = JSON.parse(JSON.stringify(row))
        }

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详情'
          this.footer = false
        } else if (title == 'add') {
          this.title = '新增'
          let resL = JSON.parse(localStorage.getItem('userInfo')).realname
          this.formData = {
            ...this.formData,
            createdUser: resL,
            createdTime: this.getCurrentDate(),
          }
        }
      },
      close() {
        this.formData = {
          infoTitle: '',
          infoProviderWorkUnit: '',
          // infoProviderWorkUnitName: '',
          providerPeople: '',
          providerPeopleName: '',
          contact: '',
          contactName: '',
          fax: '',
          mailAddress: '',
          infoReleaseColumn: '',
          expectOnlineTime: '',
          failureTime: '',
          contentAbstract: '',
          id: '',
        }
        this.editId = ''
        this.dialogFormVisible = false
        this.loading = false
        this.footer = true
        this.$emit('fetchData')
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            const params = JSON.parse(JSON.stringify(this.formData))
            const res = await editIp(params)
            if (res && res.code == 200) {
              this.editId = res.data.id
              this.formData.id = res.data.id
              this.$message({
                message: '保存成功！',
                type: 'success',
              })
            } else {
              this.$message({
                message: '保存失败',
                type: 'error',
              })
            }
            this.loading = false
          }
        })
      },
      handleApproval() {
        //提交审批
        this.$refs['process'].save(134, this.editId)
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
