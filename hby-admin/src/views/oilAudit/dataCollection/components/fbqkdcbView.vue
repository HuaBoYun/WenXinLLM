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
          <el-form-item label="分包项目名称" prop="projectName">
            <el-input
              v-model="formData.projectName"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入分包项目名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="对应的总包含合同编号" prop="generalContractNo">
            <el-input
              v-model="formData.generalContractNo"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入对应的总包含合同编号"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="分包合同编号" prop="subContractNo">
            <el-input
              v-model="formData.subContractNo"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入分包合同编号"
              @input="inputMoney($event, 'subContractNo')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="分包单位" prop="subOrgName">
            <el-input
              v-model="formData.subOrgName"
              clearable
              placeholder="请选择分包单位"
              :style="{ width: '266px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="openDep('subOrg')"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="分包形式" prop="subType">
            <el-input
              v-model="formData.subType"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入分包形式"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="分包合同金额" prop="subContractAmount">
            <el-input
              v-model="formData.subContractAmount"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入分包合同金额"
              @input="inputMoney($event, 'subContractAmount')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="分包结算金额" prop="subSettlementAmount">
            <el-input
              v-model="formData.subSettlementAmount"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入分包结算金额"
              @input="inputMoney($event, 'subSettlementAmount')"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="额度" prop="quota">
            <el-input
              v-model="formData.quota"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入额度"
              @input="inputMoney($event, 'quota')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目所在地" prop="projectAddress">
            <el-input
              v-model="formData.projectAddress"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入项目所在地"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="分包选商方式" prop="subCheckMode">
            <el-select style="width: 100%" v-model="formData.subCheckMode">
              <el-option
                v-for="item in typeList"
                :key="item.id"
                :label="item.name"
                :value="item.name"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否涉及法律诉讼" prop="hasProceedings">
            <el-radio-group v-model="formData.hasProceedings">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="0">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="建设单位" prop="buildOrgName">
            <el-input
              v-model="formData.buildOrgName"
              clearable
              placeholder="请输入建设单位"
              :style="{ width: '266px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="openDep('buildOrg')"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="总包单位" prop="generalOrgName">
            <el-input
              v-model="formData.generalOrgName"
              clearable
              placeholder="请输入总包单位"
              :style="{ width: '266px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="openDep('generalOrg')"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计人员" prop="auditPersonName">
            <el-input
              v-model="formData.auditPersonName"
              clearable
              placeholder="请选择审计人员"
              style="width: 266px; height: 30px"
              disabled
            />
            <el-button
              @click="projectManager('auditPerson')"
              style="margin-left: 10px; height: 30px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
    </div>

    <!-- 人员 -->
    <!-- <project-manage @projectManage="getChildlistPro" ref="manage" /> -->
    <project-manage
      @projectManage="getChildlistPro"
      ref="manage"
      :multiple="false"
    ></project-manage>
    <!-- 公司 -->

    <SelectDepartment ref="company" @submit="selectedCompany" />
  </el-dialog>
</template>

<script>
  import { baseURL } from '@/config'
  import store from '@/store'
  import {
    subContractDetail,
    subContractSaveOrUpdate,
  } from '@/oapi/audit/information'
  import { download, deleteFile } from '@/oapi/audit/report'
  // import projectManage from '@/views/audit/project/components/formComponents/projectManage.vue'
  const token = store.getters['user/token']
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import projectManage from '@/components/selectPerson.vue'

  export default {
    components: { projectManage, SelectDepartment },
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
          projectName: '',
          auditPersonId: '',
          auditPersonName: '',
          generalContractNo: '',
          subContractNo: '',
          subOrgName: '',
          subOrgId: '',
          subType: '',
          subContractAmount: '',
          subSettlementAmount: '',
          quota: '',
          projectAddress: '',
          subCheckMode: '',
          hasProceedings: '',
          buildOrgName: '',
          buildOrgId: '',
          generalOrgName: '',
          generalOrgId: '',
          auditPersonName: '',
          auditPersonId: '',
        },
        radio: '',
        footer: true,
        rules: {
          projectName: [
            {
              required: true,
              message: '请输入分包项目名称',
              trigger: 'blur',
            },
          ],
          generalContractNo: [
            {
              required: true,
              message: '请输入对应的总包含合同编号',
              trigger: 'blur',
            },
          ],
          subContractNo: [
            {
              required: true,
              message: '请输入分包合同编号',
              trigger: 'blur',
            },
          ],
          subOrgName: [
            {
              required: true,
              message: '请选择分包单位',
              trigger: 'blur',
            },
          ],
          subType: [
            {
              required: true,
              message: '请输入分包形式',
              trigger: 'blur',
            },
          ],
          subContractAmount: [
            {
              required: true,
              message: '请输入分包合同金额',
              trigger: 'blur',
            },
          ],
          buildOrgName: [
            {
              required: true,
              message: '请输入建设单位',
              trigger: 'blur',
            },
          ],
          generalOrgName: [
            {
              required: true,
              message: '请输入总包单位',
              trigger: 'blur',
            },
          ],
          auditPersonName: [
            {
              required: true,
              message: '请选择审计人员',
              trigger: 'blur',
            },
          ],
          subSettlementAmount: [
            {
              required: true,
              message: '请输入分包结算金额',
              trigger: 'blur',
            },
          ],
          quota: [
            {
              required: true,
              message: '请输入额度',
              trigger: 'blur',
            },
          ],
          projectAddress: [
            {
              required: true,
              message: '请输入项目所在地',
              trigger: 'blur',
            },
          ],
          subCheckMode: [
            {
              required: true,
              message: '请选择分包选商方式',
              trigger: 'blur',
            },
          ],
          hasProceedings: [
            {
              required: true,
              message: '请选择是否涉及法律诉讼',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        depType: '',
        typeList: [
          { id: 0, name: '公开招标' },
          { id: 1, name: '邀请招标' },
          { id: 2, name: '公开竞争性谈判' },
          { id: 3, name: '邀请竞争性谈判' },
          { id: 4, name: '谈判评审' },
          { id: 5, name: '单一' },
        ],
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      //金额输入
      inputMoney(value, key) {
        // 移除非数字字符和小数点
        let sanitizedValue = value.replace(/[^0-9.]/g, '')
        // 如果输入的是小数点，确保只有一个小数点
        if (sanitizedValue.indexOf('.') !== sanitizedValue.lastIndexOf('.')) {
          sanitizedValue = sanitizedValue.slice(
            0,
            sanitizedValue.lastIndexOf('.')
          )
        }
        // 如果输入的是0开头且后面有其他数字，去掉开头的0
        if (
          sanitizedValue.startsWith('0') &&
          sanitizedValue.length > 1 &&
          sanitizedValue[1] !== '.'
        ) {
          sanitizedValue = sanitizedValue.slice(1)
        }
        // 如果输入的是小数点开头，前面加0
        if (sanitizedValue.startsWith('.')) {
          sanitizedValue = '0' + sanitizedValue
        }
        // 如果输入的是负数，去掉负号
        if (sanitizedValue.startsWith('-')) {
          sanitizedValue = sanitizedValue.slice(1)
        }
        // 如果输入的是空字符串或0，设置为空字符串
        if (sanitizedValue === '' || sanitizedValue === '0') {
          sanitizedValue = ''
        }
        // 更新输入框的值
        const keys = key.split('.')
        let formDataRef = this.formData
        for (let i = 0; i < keys.length - 1; i++) {
          formDataRef = formDataRef[keys[i]]
        }
        formDataRef[keys[keys.length - 1]] = sanitizedValue
      },
      getCurrentDate() {
        return new Date(+new Date() + 8 * 3600 * 1000)
          .toJSON()
          .substr(0, 19)
          .replace('T', ' ')
      },
      openDep(type) {
        this.depType = type
        this.$refs.company.showEdit()
      },

      selectedCompany(node) {
        this.$set(this.formData, this.depType + 'Name', node.label)
        this.$set(this.formData, this.depType + 'Id', node.id)
      },
      projectManager(type) {
        // this.proType = type
        this.$refs['manage'].showEdit()
      },
      getChildlistPro(val) {
        this.$set(this.formData, 'auditPersonName', val[0].realname)
        this.$set(this.formData, 'auditPersonId', val[0].staffid)
      },
      async showEdit(row, title) {
        this.dialogFormVisible = true
        if (row) {
          const res = await subContractDetail({ id: row.id })
          Object.assign(this.formData, res.data.data)

          this.formData.subOrgName = res.data.data.subOrg?.orgname || ''
          this.formData.buildOrgName = res.data.data.buildOrg?.orgname || ''
          this.formData.generalOrgName = res.data.data.generalOrg?.orgname || ''
          this.formData.auditPersonName =
            res.data.data.auditPerson?.realname || ''

          this.formData.subOrgId = res.data.data.subOrg?.orgid || ''
          this.formData.buildOrgId = res.data.data.buildOrg?.orgid || ''
          this.formData.generalOrgId = res.data.data.generalOrg?.orgid || ''
          this.formData.auditPersonId = res.data.data.auditPerson?.staffid || ''
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
        this.formData = {
          id: '',
          projectName: '',
          auditPersonId: '',
          auditPersonName: '',
          generalContractNo: '',
          subContractNo: '',
          subOrgName: '',
          subOrgId: '',
          subType: '',
          subContractAmount: '',
          subSettlementAmount: '',
          quota: '',
          projectAddress: '',
          subCheckMode: '',
          hasProceedings: '',
          buildOrgName: '',
          buildOrgId: '',
          generalOrgName: '',
          generalOrgId: '',
          auditPersonName: '',
          auditPersonId: '',
        }
        this.tableData = []
        this.dialogFormVisible = false
        this.footer = true
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let params = { ...this.formData }

            const data = await subContractSaveOrUpdate({
              ...params,
            })
            if (data.code == 1) {
              this.$baseMessage(data.msg, 'success')
              this.$emit('fetchData')
              this.close()
            } else {
              this.$baseMessage(data.msg, 'error')
            }
          }
        })
      },
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      async handleDown(row) {
        const data = await download({ attId: row.attid })
        let filename = row.attname
        let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
        let url = window.URL.createObjectURL(blob, {
          type: 'application/vnd.ms-excel',
        })
        const link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', filename)
        document.documentElement.appendChild(link)
        link.click()
        document.documentElement.removeChild(link)
      },
      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        this.tableData = list
        await deleteFile({ attId: row.attid })
      },
      handlePreview(file) {},
      handleSuccess(file) {
        if (file.result == '200') {
          let list = this.tableData
          list.push(file.data)
          this.tableData = list
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
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
