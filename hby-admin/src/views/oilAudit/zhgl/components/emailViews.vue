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
        label-width="210px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="!footer"
      >
        <el-col :span="12">
          <el-form-item label="申请人姓名" prop="applyName">
            <el-input
              v-model="formData.applyName"
              clearable
              disabled
              placeholder="请选择申请人姓名"
              style="width: 190px"
            />
            <el-button
              type="primary"
              style="margin-left: 10px"
              @click="$refs.executor.showEdit()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="员工编号" prop="staffCode">
            <el-input
              v-model="formData.staffCode"
              clearable
              placeholder="请输入员工编号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="申请人单位" prop="applyBelongGroupName">
            <el-input
              v-model="formData.applyBelongGroupName"
              clearable
              placeholder="请选择申请人单位"
              style="width: 190px"
              disabled
            />
            <el-button
              @click="$refs.audiTree.showEdit()"
              style="margin-left: 10px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="申请人部门" prop="applyWorkUnitName">
            <el-input
              v-model="formData.applyWorkUnitName"
              clearable
              placeholder="请选择申请人部门"
              style="width: 100%"
              disabled
            />
            <!-- <el-button
              @click="projectManager('dept')"
              style="margin-left: 10px; height: 30px"
              type="primary"
            >
              选择
            </el-button> -->
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="服务类型" prop="serviceType">
            <el-select style="width: 100%" v-model="formData.serviceType">
              <el-option
                v-for="item in typeList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              ></el-option>
            </el-select>
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
          <el-form-item label="邮箱后缀" prop="mailSuffix">
            <el-select style="width: 100%" v-model="formData.mailSuffix">
              <el-option label="petrochina.com.cn" :value="0"></el-option>
              <el-option label="cnpc.com.cn" :value="1"></el-option>
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="首选登录名" prop="loginName">
            <el-input
              v-model="formData.loginName"
              clearable
              placeholder="请输入首选登录名"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="备选登录名1" prop="loginNameOne">
            <el-input
              v-model="formData.loginNameOne"
              clearable
              placeholder="请输入备选登录名1"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="备选登录名2" prop="loginNameTwo">
            <el-input
              v-model="formData.loginNameTwo"
              clearable
              placeholder="请输入备选登录名2"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="备选登录名3" prop="loginNameThree">
            <el-input
              v-model="formData.loginNameThree"
              clearable
              placeholder="请输入备选登录名3"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="变更类型" prop="changeType">
            <el-select style="width: 100%" v-model="formData.changeType">
              <el-option
                v-for="item in changeType"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="申请人邮箱" prop="applyMail">
            <el-input
              v-model="formData.applyMail"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入申请人邮箱"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="变更内容" prop="changeContent">
            <el-input
              v-model="formData.changeContent"
              :style="{ width: '100%' }"
              clearable
              :rows="4"
              type="textarea"
              placeholder="请输入变更内容"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="注销原因" prop="logoutReason">
            <el-input
              v-model="formData.logoutReason"
              :style="{ width: '100%' }"
              clearable
              :rows="4"
              type="textarea"
              placeholder="请输入注销原因"
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

    <!-- 选择人员弹窗 -->
    <executor-options ref="executor" @projectManage="handleExecutorSelected" />
    <!-- 选择部门弹窗 -->
    <department-options ref="department" @selected="handleDepartmentSelected" />
    <!-- 选择单位 -->
    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />
    <ProcessList ref="process" @fetchData="close" />
  </el-dialog>
</template>

<script>
  import { baseURL } from '@/config'
  import store from '@/store'
  import { editInfor, getInfoDetail } from '@/oapi/ypns_zhgl/zsyyxgl'
  import DepartmentOptions from '@/views/oilAudit/report/components/options/department.vue'
  import ExecutorOptions from '@/components/danxuanPerson.vue'
  import { getUserDetailInfo } from '@/oapi/ypns_zhgl/filePublic'
  import SelectDepartment from '../../jhlx/components/department.vue'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'

  const token = store.getters['user/token']

  export default {
    components: {
      DepartmentOptions,
      ExecutorOptions,
      SelectDepartment,
      ProcessList,
    },
    inheritAttrs: false,
    props: ['fetchData'],
    data() {
      return {
        loading: false,
        baseURL: baseURL,
        api: '/audit/fileManage/upload',
        headers: { token: token },
        tableData: [],
        formData: {
          id: '',
          applyId: '',
          applyName: '',
          staffCode: '',
          applyBelongGroup: '',
          applyBelongGroupName: '',
          applyWorkUnit: '',
          applyWorkUnitName: '',
          serviceType: '',
          contactPhone: '',
          mailSuffix: '',
          loginName: '',
          loginNameOne: '',
          loginNameTwo: '',
          loginNameThree: '',
          changeType: '',
          applyMail: '',
          changeContent: '',
          logoutReason: '',
        },
        typeList: [
          { id: 0, name: '开通邮箱' },
          { id: 1, name: '信息变更' },
          { id: 2, name: '注销邮箱' },
        ],
        changeType: [
          { id: 0, name: '密码' },
          { id: 1, name: '组织机构' },
          { id: 2, name: '其他' },
        ],
        footer: true,
        rules: {
          applyMail: [
            {
              required: true,
              message: '请输入邮箱',
              trigger: 'blur',
            },
          ],
          // applyWorkUnitName: [
          //   {
          //     required: true,
          //     message: '请选择申请人部门',
          //     trigger: ['blur', 'change'],
          //   },
          // ],
          applyBelongGroupName: [
            {
              required: true,
              message: '请选择申请人单位',
              trigger: ['blur', 'change'],
            },
          ],
          applyName: [
            {
              required: true,
              message: '请选择申请人姓名',
              trigger: ['blur', 'change'],
            },
          ],
          contactPhone: [
            {
              required: true,
              message: '请输入联系电话',
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
          serviceType: [
            {
              required: true,
              message: '请输入服务类型',
              trigger: 'blur',
            },
          ],
        },
        deptType: '',
        dialogFormVisible: false,
        title: '新增',
        editId: '',
      }
    },
    methods: {
      // 选择单位
      getDepartmentInfo(node) {
        this.$set(this.formData, 'applyBelongGroupName', node.label)
        this.$set(this.formData, 'applyBelongGroup', node.id)
      },
      // 选择经办人
      async handleExecutorSelected(node) {
        this.$set(this.formData, 'applyName', node[0].realname)
        this.$set(this.formData, 'applyId', node[0].staffid)
        const res = await getUserDetailInfo({ id: node[0].staffid })
        this.formData.applyWorkUnitName = res.data.workUnitName
        this.$forceUpdate()
      },
      // 选择部门
      handleDepartmentSelected(node) {
        if (this.deptType === 'unit') {
          this.$set(this.formData, `applyBelongGroupName`, node.name)
          this.$set(this.formData, `applyBelongGroup`, node.id)
        } else if (this.deptType === 'dept') {
          this.$set(this.formData, `applyWorkUnitName`, node.name)
          this.$set(this.formData, `applyWorkUnit`, node.id)
        }
        this.$forceUpdate()
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
          this.formData.applyId = res.data.applyId
          this.formData.applyName = res.data.staff.realName
          this.formData.staffCode = res.data.staffCode
          this.formData.applyBelongGroup = res.data.applyBelongGroup
          this.formData.applyBelongGroupName = res.data.applyBelongGroupName
          this.formData.applyWorkUnit = res.data.applyWorkUnit
          this.formData.applyWorkUnitName = res.data.staff.workUnitName
          this.formData.serviceType = Number(res.data.serviceType)
          this.formData.contactPhone = res.data.contactPhone
          this.formData.mailSuffix = Number(res.data.mailSuffix)
          this.formData.loginName = res.data.loginName
          this.formData.loginNameOne = res.data.loginNameOne
          this.formData.loginNameTwo = res.data.loginNameTwo
          this.formData.loginNameThree = res.data.loginNameThree
          this.formData.changeType = Number(res.data.changeType)
          this.formData.applyMail = res.data.applyMail
          this.formData.changeContent = res.data.changeContent
          this.formData.logoutReason = res.data.logoutReason
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
          applyId: '',
          applyName: '',
          staffCode: '',
          applyBelongGroup: '',
          applyBelongGroupName: '',
          applyWorkUnit: '',
          applyWorkUnitName: '',
          serviceType: '',
          contactPhone: '',
          mailSuffix: '',
          loginName: '',
          loginNameOne: '',
          loginNameTwo: '',
          loginNameThree: '',
          changeType: '',
          applyMail: '',
          changeContent: '',
          logoutReason: '',
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
            const res = await editInfor(params)
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
      projectManager(type) {
        this.deptType = type
        this.$refs['department'].show()
      },
      async getChildlistPro(val) {
        this.staffid = val[0].staffid
        this.$set(this.formData, 'orgName', val[0].realname)
      },
      handleApproval() {
        //提交审批
        this.$refs['process'].save(143, this.editId)
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
