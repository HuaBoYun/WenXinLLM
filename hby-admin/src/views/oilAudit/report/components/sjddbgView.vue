<template>
  <!-- 审计督导报告 edit -->
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
        style="display: flex; flex-wrap: wrap"
      >
        <el-col :span="12">
          <el-form-item label="报告名称" prop="reportName">
            <el-input
              v-model="formData.reportName"
              clearable
              placeholder="请输入报告名称"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报告时间" prop="reportTime">
            <el-date-picker
              v-model="formData.reportTime"
              type="date"
              format="yyyy-MM-dd"
              :disabled="!footer"
              value-format="yyyy-MM-dd"
              placeholder="选择报告时间"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报告类型" prop="reportType">
            <el-select
              v-model="formData.reportType"
              clearable
              placeholder="请选择报告类型"
              :style="{ width: '100%' }"
              :disabled="!footer"
            >
              <el-option
                v-for="item in reportTypeOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报告方式" prop="reportWay">
            <el-select
              v-model="formData.reportWay"
              clearable
              placeholder="请选择报告方式"
              :style="{ width: '100%' }"
              :disabled="!footer"
            >
              <el-option
                v-for="item in reportWayOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报告部门" prop="reportDept">
            <el-input
              v-model="formData.reportDept"
              clearable
              placeholder="请输入报告部门"
              :style="{ width: '256px' }"
              :disabled="true"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              :disabled="!footer"
              @click="$refs.department.show()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报告人" prop="reporter">
            <el-input
              v-model="formData.reporter"
              clearable
              placeholder="请输入报告人"
              :style="{ width: '256px' }"
              :disabled="true"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              :disabled="!footer"
              @click="$refs.executor.show()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="编辑器" prop="reportContent">
            <!-- <tinymce
              v-model="formData.repdesc"
              :height="300"
              placeholder="请输入编辑器"
            />   -->
            <UEditor
              ref="ueditor"
              v-model="formData.reportContent"
              :height="300"
              :disabled="!footer"
              :templates="templates"
              template="gzrz"
              style="margin-left: -100px"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + (formData.reportid ? api1 : api)"
              :data="{ reportid: formData.reportid }"
              :headers="headers"
              :on-preview="handlePreview"
              :on-success="handleSuccess"
              :file-list="tableData"
            >
              <el-button type="success">上传</el-button>
            </el-upload>
          </div>
          <el-table :data="tableData">
            <el-table-column align="center" label="附件名称" prop="attname" />
            <el-table-column
              align="center"
              label="文件大小(KB)"
              prop="attsize"
            />
            <el-table-column align="center" label="创建人" prop="uploader" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="200"
            >
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="handleDown(row)"
                  :disabled="false"
                >
                  下载
                </el-button>
                <el-button
                  type="text"
                  @click="handlePreviewFile(row)"
                  :disabled="false"
                >
                  预览
                </el-button>
                <el-button type="text" @click="handleDelete(row)" v-if="footer">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </div>
    <template #footer v-if="footer">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </template>

    <department-options ref="department" @selected="handleDepartmentSelected" />
    <executor-options ref="executor" @selected="handleExecutorSelected" />
  </el-dialog>
</template>

<script>
import Tinymce from '@/components/Tinymce'
import UEditor from '@/components/UEditor'
import { formatDay } from '@/utils/index'
import store from '@/store'
import DepartmentOptions from './options/department.vue'
import ExecutorOptions from './options/executor.vue'
const { baseURL } = require('@/config')
import { download } from '@/api/internal/score'
import { getOne, auitSupervisorReportUpdate } from '@/oapi/audit/implement'
import { getPrivewAttInfo } from '@/api/contract/manage'
  import { spDelFj, deleteFile } from '@/api/internal/report'

export default {
  name: 'IndexView',
  components: { Tinymce, DepartmentOptions, ExecutorOptions, UEditor },
  inheritAttrs: false,
  props: [],
  data() {
    return {
      baseApi: baseURL,
      api: '/nkhg/nbkz/pjbg/addupload',
      api1: '/nkhg/nbkz/pjbg/updateupload',
      headers: {
        token: store.getters['user/token'],
      },
      formData: {
        id: '',
        reportName: '',
        reportTime: '',
        reportType: '', // 报告类型
        reportWay: '', // 报告方式
        reportDept: '', // 报告部门
        reporter: '', // 报告人
        reportContent: '',
      },
      rules: {
        reportName: [
          {
            required: true,
            message: '请输入报告名称',
            trigger: 'blur',
          },
        ],
        reportTime: [
          {
            required: true,
            message: '请选择报告时间',
            trigger: 'blur',
          },
        ],
        reportType: [
          {
            required: true,
            message: '请选择报告类型',
            trigger: 'blur',
          },
        ],
        reportWay: [
          {
            required: true,
            message: '请选择报告方式',
            trigger: 'blur',
          },
        ],
        reportContent: [
          {
            required: true,
            message: '请输入编辑器',
            trigger: 'blur',
          },
        ],
        reporter: [
          {
            required: true,
            message: '请输入报告人',
            trigger: 'blur',
          },
        ],
        reportDept: [
          {
            required: true,
            message: '请输入报告部门',
            trigger: 'blur',
          },
        ],
      },
      dialogFormVisible: false,
      title: '新增',
      footer: true,
      tableData: [],
      templates: [],
      reportTypeOptions: [
        {
          label: '对内报告',
          value: '对内报告',
        },
        {
          label: '对外报告',
          value: '对外报告',
        },
      ],
      reportWayOptions: [
        {
          label: '定期报告',
          value: '定期报告',
        },
        {
          label: '非定期报告',
          value: '非定期报告',
        },
      ],
    }
  },
  computed: {},
  watch: {
    'formData.repdesc'(val) {
      //
      //
      if (this.$refs['ueditor'].editor.openTemplate) {
        this.$refs['ueditor'].editor.openTemplate = false
        let s = val
        const arr = [
          ['$[contract.contractno]', 'contractno'],
          ['$[contract.contractname]', 'contractname'],
          ['$[contract.contractamount]', 'contractmoney'],
          ['$[contract.contractItem]', 'contractitem'],
          ['$[contract.executor]', 'realname'],
          ['$[contract.rmbinwords]', 'hzsumowing'],

          ['$[counterpart.coupersion]', 'counterpartcode'],
          ['$[counterpart.personincharge]', 'contractbd'],
          ['$[counterpart.counterpartHank]', 'bankkhyh'],
          ['$[counterpart.counumber]', 'counterpartno'],
          ['$[counterpart.couname]', 'budgetname'],
          ['$[counterpart.couaddress]', 'counterpartaddress'],
          ['$[counterpart.coupersion]', 'contacts'],
          ['$[counterpart.contactsPhone]', 'contactsphone'],
          ['$[counterpart.counterpartHankAccount]', 'bankaccount'],
          ['$[counterpart.legarepresentative]', 'contacts'],
          ['$[counterpart.pctelephonenumber]', 'contractzd'],
          // ['$[counterpart.taxpayeridentification]', 'hzsumowing'], //纳税人识别号
        ]
        arr.forEach((i) => {
          if (this.formData[i[1]]) {
            s = s.replace(i[0], this.formData[i[1]])
          }
        })
        this.formData.repdesc = s
      }
    },
  },
  created() {},
  mounted() {},
  methods: {
    // onchangeReportTime(val) { // 选择报告时间范围
    //   if (this.formData.reportTime.length) {
    //     this.formData.reportTime = val[0] + '~' + val[1]
    //   }
    // },
    handleDepartmentSelected(node) {
      // 选择部门
      this.$nextTick(() => {
        this.$refs['ruleForm'].clearValidate()
      })
      this.$set(this.formData, 'reportDeptid', node.id)
      this.$set(this.formData, 'reportDept', node.label)
      // this.formData.reportdepartmentid = node.id
      // this.formData.reportdepartment = node.name
    },
    handleExecutorSelected(node) {
      // 选择报告人
      this.$nextTick(() => {
        this.$refs['ruleForm'].clearValidate()
      })

      this.$set(this.formData, 'reporter', node.realname)
      this.$set(this.formData, 'reporterid', node.staffid)
      // this.formData.reporter = node.realname
      // this.formData.reporterid = node.staffid
    },
    async showEdit(row, type) {
      if (type == 'add') {
        this.title = '添加'
      } else if (type == 'edit') {
        this.getInfo(row)
        this.title = '编辑'
      } else {
        this.title = '查看'
        this.getInfo(row)
        this.footer = false
      }
      this.dialogFormVisible = true
    },
    async getInfo(row) {
      const res = await getOne({ id: row.id })
      Object.assign(this.formData, res.data.data)
      this.tableData = res.data.data.attachments || []
    },
    close() {
      this.title = ''
      this.formData = {
        id: '',
        reportName: '',
        reportTime: '', // 报告时间
        reportType: '', // 报告类型
        reportWay: '', // 报告方式
        reportDept: '', // 报告部门
        reporter: '', // 报告人
        reportContent: '',
      }
      this.tableData = []
      this.footer = true
      this.dialogFormVisible = false
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
      if (!this.formData.reportid) {
        await deleteFile({ attid: row.attid })
      } else {
        await spDelFj({ attid: row.attid, reportid: this.formData.reportid })
      }
    },
    handlePreview(file) {},
    handleSuccess(file) {
      if (file.code == '200') {
        let list = this.tableData
        list.push(file.data.Attachment)
        this.tableData = list
        this.$baseMessage('上传成功', 'success')
      } else {
        this.$baseMessage(file.msg, 'error')
      }
    },
    add() {
      this.$refs['ruleForm'].validate(async (valid) => {
        if (valid) {
          if (this.formData.id) {
            // 编辑
            let attids = []
            this.tableData.map((item) => {
              attids.push(item.attid)
            })
            const { attachments, ...other } = this.formData

            const data = await auitSupervisorReportUpdate({
              ...other,
              attIds: attids,
            })
            if (data.code === 1) {
              this.$baseMessage('修改成功', 'success')
            } else {
              this.$baseMessage(data.msg, 'error')
            }
            this.$emit('fetch-data')
            this.close()
          } else {
            // 新增
            let attids = []
            this.tableData.map((item) => {
              attids.push(item.attid)
            })
            const { attachments, ...other } = this.formData
            const data = await auitSupervisorReportUpdate({
              ...other,
              attIds: attids,
            })
            if (data.code === 1) {
              this.$baseMessage('保存成功', 'success')
            } else {
              this.$baseMessage(data.msg, 'error')
            }
            this.$emit('fetch-data')
            this.close()
          }
        } else {
          return false
        }
      })
    },
    /**
     * @description: 文件预览
     * @param {*} row 文件信息
     * @return {*}
     */
    async handlePreviewFile(row) {
      const { data } = await getPrivewAttInfo({
        attId: row.attid,
        attType: 2,
      })

      const url =
        data.previewurl +
        '?url=' +
        encodeURIComponent(Base64.encode(data.ftpUrl))
      this.$iFrameDialog({ iframeUrl: url })
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
