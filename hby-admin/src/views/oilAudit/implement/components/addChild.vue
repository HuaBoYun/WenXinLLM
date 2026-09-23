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
        <el-col :span="12" style="height: 29px">
          <el-form-item label="编号" prop="resultcode">
            <el-input
              v-model="formData.resultcode"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入编号"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计项目名称" prop="projectname">
            <el-input
              v-model="formData.projectname"
              clearable
              placeholder="请输入审计项目名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 29px">
          <el-form-item label="被审计单位名称" prop="orgidnames">
            <el-input
              v-model="formData.orgidnames"
              clearable
              placeholder="请输入被审计单位名称"
              :style="{ width: '266px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="openDep('orgidnames')"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同编号" prop="contractcode">
            <el-input
              v-model="formData.contractcode"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入合同编号"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="合同名称" prop="contractname">
            <el-input
              v-model="formData.contractname"
              clearable
              placeholder="请输入合同名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="核减人" prop="hjstaffname">
            <el-input
              v-model="formData.hjstaffname"
              clearable
              placeholder="请输入核减人"
              readonly
              :style="{ width: '266px' }"
            />
            <el-button
              @click="$refs['manage'].showEdit()"
              style="margin-left: 10px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 29px">
          <el-form-item label="施工单位" prop="sgorgname">
            <el-input
              v-model="formData.sgorgname"
              clearable
              placeholder="请输入施工单位"
              :style="{ width: '266px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="openDep('sgorgname')"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同金额(元)" prop="contractmoney">
            <el-input
              v-model="formData.contractmoney"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入合同金额(元)"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="核增金额(元)" prop="hzmoney">
            <el-input
              v-model="formData.hzmoney"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入核增金额(元)"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="核减金额(元)" prop="hjmoney">
            <el-input
              v-model="formData.hjmoney"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入核减金额(元)"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计认定金额(元)" prop="sdmoney">
            <el-input
              v-model="formData.sdmoney"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入审计认定金额(元)"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="问题描述" label-width="140px" prop="overview">
            <el-input
              type="textarea"
              placeholder="请输入问题描述"
              :rows="5"
              v-model="formData.overview"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="审计意见"
            label-width="140px"
            prop="auditopinion"
          >
            <el-input
              type="textarea"
              placeholder="请输入审计意见"
              :rows="5"
              v-model="formData.auditopinion"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>

    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
    </div>

    <!-- 部门 -->
    <DepartmentOptions ref="department" @selected="handleDepartmentSelected" />
    <project-manage
      @projectManage="getChildlistPro"
      ref="manage"
      :multiple="false"
    ></project-manage>
  </el-dialog>
</template>

<script>
import { baseURL } from '@/config'
import store from '@/store'
import { resultGetone, resultSaveOrUpdate } from '@/oapi/audit/implement'
import { deleteFile, download } from '@/oapi/audit/report'
import DepartmentOptions from '@/views/oilAudit/report/components/options/department.vue'
import projectManage from '@/components/selectPerson.vue'

const token = store.getters['user/token']

export default {
  components: { DepartmentOptions, projectManage },
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
        scoreItems: [],
        resultcode: '',
        projectname: '',
        orgidnames: '',
        contractcode: '',
        contractname: '',
        sgorgname: '',
        contractmoney: '',
        hzmoney: '',
        hjmoney: '',
        sdmoney: '',
        auditopinion: '',
        hjstaffname: '',
        hjstaffid: '',
        overview: '',
      },
      footer: true,
      rules: {
        resultcode: [
          {
            required: true,
            message: '请输入编号',
            trigger: 'blur',
          },
        ],
        orgidnames: [
          {
            required: true,
            message: '请输入被审计单位名称',
            trigger: 'blur',
          },
        ],
        contractcode: [
          {
            required: true,
            message: '请输入合同编号',
            trigger: 'blur',
          },
        ],
        projectname: [
          {
            required: true,
            message: '请输入审计项目名称',
            trigger: 'blur',
          },
        ],
        contractname: [
          {
            required: true,
            message: '请输入合同名称',
            trigger: 'blur',
          },
        ],
        sgorgname: [
          {
            required: true,
            message: '请输入施工单位',
            trigger: 'blur',
          },
        ],
        contractmoney: [
          {
            required: true,
            message: '请输入合同金额(元)',
            trigger: 'blur',
          },
        ],
        hzmoney: [
          {
            required: true,
            message: '请输入核增金额(元)',
            trigger: 'blur',
          },
        ],
        hjmoney: [
          {
            required: true,
            message: '请输入核减金额(元)',
            trigger: 'blur',
          },
        ],
        sdmoney: [
          {
            required: true,
            message: '请输入审计认定金额(元)',
            trigger: 'blur',
          },
        ],
      },
      dialogFormVisible: false,
      title: '新增',
      depType: '',
    }
  },
  methods: {
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
      this.$set(this.formData, this.depType, node.label)
      if (this.depType == 'orgidnames') {
        this.$set(this.formData, `orgids`, node.id)
      } else if (this.depType == 'sgorgname') {
        this.$set(this.formData, `sgorgid`, node.id)
      }
    },
    async showEdit(row, title) {
      this.footer = true
      if (title == 'add') {
        this.title = '新增'
      } else if (title == 'edit') {
        this.title = '修改'
        Object.assign(this.formData, row)
      } else if (title == 'detail') {
        Object.assign(this.formData, row)
        this.title = '详情'
        this.footer = false
      }
      this.dialogFormVisible = true
    },
    async getChildlistPro(val) {
      const ids = val.map((res) => res.staffid)
      const names = val.map((res) => res.realname)
      this.$set(this.formData, 'hjstaffid', ids.toString())
      this.$set(this.formData, 'hjstaffname', names.toString())
    },
    close() {
      this.formData.id = ''
      this.formData.scoreItems = []
      this.formData.resultcode = ''
      this.formData.projectname = ''
      this.formData.orgidnames = ''
      this.formData.contractcode = ''
      this.formData.contractname = ''
      this.formData.sgorgname = ''
      this.formData.contractmoney = ''
      this.formData.hzmoney = ''
      this.formData.hjmoney = ''
      this.formData.sdmoney = ''
      this.formData.auditopinion = ''
      this.formData.hjstaffname = ''
      this.formData.hjstaffid = ''
      this.formData.overview = ''
      this.dialogFormVisible = false
      this.footer = true
    },
    async save() {
      this.$refs['ruleForm'].validate(async (valid) => {
        if (valid) {
          let attids = ''
          this.tableData.map((item) => {
            attids += item.attid
            attids += ','
          })
          attids = attids.substring(0, attids.length - 1)

          let params = { ...this.formData }
          delete params.realname
          const { data, code, msg } = await resultSaveOrUpdate({
            ...params,
            attids,
          })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
            this.$emit('AddChild', data.data)
            this.close()
          } else {
            this.$baseMessage(msg, 'error')
          }
        }
      })
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
