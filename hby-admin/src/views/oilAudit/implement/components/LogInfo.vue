<template>
  <el-dialog
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="日志名称" prop="reportname">
            <el-input
              v-model="formData.reportname"
              clearable
              placeholder="请输入日志名称"
              :style="{ width: '256px' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="日志时间" prop="reporttime">
            <el-date-picker
              v-model="formData.reporttime"
              placeholder="选择日志时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :disabled="!footer"
              :style="{ width: '256px' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报告类型" prop="reporttype">
            <el-select
              v-model="formData.reporttype"
              placeholder="请选择报告类型"
              :disabled="!footer"
              :style="{ width: '256px' }"
            >
              <el-option label="日报" value="日报" />
              <el-option label="周报" value="周报" />
              <el-option label="总结报告" value="总结报告" />
              <!-- <el-option label="月报" value="月报" />
              <el-option label="季报" value="季报" />
              <el-option label="年报" value="年报" />
              <el-option label="综合" value="综合" /> -->
            </el-select>
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
        <el-col :span="12" v-if="false">
          <el-form-item label="报告部门" prop="reportdepartment">
            <el-input
              v-model="formData.reportdepartment"
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
        <el-col :span="24">
          <el-form-item label="" prop="memo">
            <!-- <tinymce
              v-model="formData.repdesc"
              :height="300"
              placeholder="请输入编辑器"
            /> -->
            <UEditor
              ref="ueditor"
              v-model="formData.memo"
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
              :action="baseApi + api"
              :headers="headers"
              :on-preview="handlePreview"
              :on-success="handleSuccess"
              :file-list="tableData"
              :before-upload="handleBeforeUpload"
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
                <el-button type="text" @click="handleDown(row)">下载</el-button>
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
  import {
    deleteFile,
    download,
    workReportFileList,
    workReportSave,
  } from '@/oapi/audit/implement'
  import Tinymce from '@/components/Tinymce'
  import UEditor from '@/components/UEditor'
  import store from '@/store'
  import { formatDay } from '@/utils/index'
  import DepartmentOptions from './options/department.vue'
  import ExecutorOptions from './options/executor.vue'
  const { baseURL } = require('@/config')
  export default {
    name: 'LoginInfo',
    components: { Tinymce, UEditor, DepartmentOptions, ExecutorOptions },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        baseApi: baseURL,
        api: '/audit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        formData: {
          reportname: undefined,
          reporttime: undefined,
          reporttype: '日报',
          memo: undefined,
          repdesc: undefined,
          realname: undefined,
          reporter: undefined,
          reporterid: undefined,
          reportdepartment: undefined,
          reportdepartmentid: undefined,
        },
        footer: true,
        tableData: [],
        templates: [],
        rules: {
          reportname: [
            {
              required: true,
              message: '请输入日志名称',
              trigger: 'blur',
            },
          ],
          reporttime: [
            {
              required: true,
              message: '请选择日志时间',
              trigger: 'blur',
            },
          ],
          reporttype: [
            {
              required: true,
              message: '请选择报告类型',
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
          reportdepartment: [
            {
              required: true,
              message: '请输入报告部门',
              trigger: 'blur',
            },
          ],
          memo: [
            {
              required: true,
              message: '请输入编辑器',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
      }
    },
    computed: {},
    watch: {
      'formData.describe'(val) {
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
    created() {
      const info = localStorage.getItem('userInfo')
      const aa = JSON.parse(info)
      this.$set(this.formData, 'reporter', aa.realname)
      this.$set(this.formData, 'reporterid', aa.staffid)
      this.$set(this.formData, 'reportdepartment', aa.linkDetp.orgname)
      this.$set(this.formData, 'reportdepartmentid', aa.linkDetp.orgid)
      this.formData.reporter = aa.realname
      this.formData.reporterid = aa.staffid
      this.formData.reportdepartment = aa.linkDetp.orgname
      this.formData.reportdepartmentid = aa.linkDetp.orgid
    },
    mounted() {},
    methods: {
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      handleDepartmentSelected(node) {
        this.$nextTick(() => {
          this.$refs['ruleForm'].clearValidate()
        })
        this.formData.reportdepartmentid = node.id
        this.formData.reportdepartment = node.name
      },
      handleExecutorSelected(node) {
        this.$nextTick(() => {
          this.$refs['ruleForm'].clearValidate()
        })
        this.formData.reporter = node.realname
        this.formData.reporterid = node.staffid
      },
      showEdit(title, row) {
        this.dialogFormVisible = true
        if (row) {
          const { reportdepartment, reporter, ...other } = row.wr
          this.formData = {
            reportdepartment: reportdepartment.orgname,
            reporter: reporter.realname,
            ...other,
          }
          this.getFileList(row.wr.reportid)
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        }
      },
      async getFileList(reportid) {
        const data = await workReportFileList({ reportid })
        this.tableData = data.data.data || []
      },
      close() {
        this.dialogFormVisible = false
        this.formData = {}
        this.tableData = []
        this.footer = true
        this.$emit('close')
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let attids = ''
            this.tableData.map((item) => {
              attids += item.attid
              attids += ','
            })
            attids = attids.substring(0, attids.length - 1)
            const { reporter, reportdepartment, reporttime, ...other } =
              this.formData
            const data = await workReportSave({
              reporttime: formatDay(reporttime),
              ...other,
              attids,
            })
            if (data.code == 1) {
              this.$baseMessage('保存成功', 'success')
            } else {
              this.$baseMessage(data.msg, 'error')
            }
            this.$emit('fetch-data')
            this.close()
          } else {
            return false
          }
        })
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
        this.$message.success('删除成功')
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
