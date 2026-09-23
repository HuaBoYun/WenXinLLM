<template>
  <el-dialog
    :close-on-click-modal="false"
    v-if="dialogFormVisible"
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
      >
        <el-col :span="12">
          <el-form-item label="报告名称" label-width="140px" prop="reportname">
            <el-input
              v-model="formData.reportname"
              clearable
              placeholder="请输入报告名称"
              :style="{ width: '256px' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报告时间" label-width="140px" prop="reporttime">
            <el-date-picker
              v-model="formData.reporttime"
              placeholder="选择报告时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '256px' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报告版本" label-width="140px" prop="reporttype">
            <el-select
              v-model="formData.reporttype"
              placeholder="请选择报报告版本"
              :disabled="!footer"
              :style="{ width: '256px' }"
            >
              <el-option label="草稿" value="草稿" />
              <el-option label="终稿草稿" value="终稿草稿" />
              <el-option label="终稿" value="终稿" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报告方式" label-width="140px" prop="reportmode">
            <el-select
              v-model="formData.reportmode"
              placeholder="请选择报告方式"
              :disabled="!footer"
              :style="{ width: '256px' }"
            >
              <el-option label="定期报告" value="定期报告" />
              <el-option label="非定期报告" value="非定期报告" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="报告部门"
            label-width="140px"
            prop="reportdepartment"
          >
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
              @click="$refs.department.show()"
              :disabled="!footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报告人" label-width="140px" prop="reporter">
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
              @click="$refs.executor.show()"
              :disabled="!footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="" prop="repdesc">
            <!-- <tinymce
              v-model="formData.field108"
              :height="300"
              placeholder="请输入编辑器"
            /> -->
            <UEditor
              ref="ueditor"
              v-model="formData.repdesc"
              :height="300"
              :templates="templates"
              :disabled="!footer"
              template="nbsj_zdy"
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
  import { zdyAdd, reportFileList } from '@/api/monitor/monitorReport'
  import { download, deleteReportFile } from '@/api/audit/implement'
  import Tinymce from '@/components/Tinymce'
  import UEditor from '@/components/UEditor'
  import DepartmentOptions from './options/department.vue'
  import ExecutorOptions from './options/executor.vue'
  import store from '@/store'
  import { formatDate, formatDay } from '@/utils/index'
  const { baseURL } = require('@/config')
  export default {
    name: 'ProjectDataInfo',
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
          reportmode: undefined,
          reporttype: undefined,
          reporterid: undefined,
          reporter: undefined,
          reportdepartmentid: undefined,
          reportdepartment: undefined,
          repdesc: undefined,
        },
        tableData: [],
        templates: [],
        footer: true,
        rules: {
          reportname: [
            {
              required: true,
              message: '请输入报告名称',
              trigger: 'blur',
            },
          ],
          reporttime: [
            {
              required: true,
              message: '请输入报告时间',
              trigger: 'blur',
            },
          ],
          reportmode: [
            {
              required: true,
              message: '请选择报告方式',
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
          repdesc: [
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
          reportdepartment: [
            {
              required: true,
              message: '请输入报告部门',
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
    created() {},
    mounted() {},
    methods: {
      handleDepartmentSelected(node) {
        this.$set(this.formData, 'reportdepartment', node.name)
        this.$set(this.formData, 'reportdepartmentid', node.id)
        this.formData.reportdepartmentid = node.id
        this.formData.reportdepartment = node.name
      },
      handleExecutorSelected(node) {
        this.$set(this.formData, 'reporter', node.name)
        this.$set(this.formData, 'reporterid', node.id)
        this.formData.reporter = node.realname
        this.formData.reporterid = node.staffid
      },
      showEdit(title, row) {
        this.dialogFormVisible = true
        if (row) {
          this.formData = row.report
          this.getFileList(row.report.reportid)
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else {
          this.title = '新增'
          this.footer = true
        }
      },
      async getFileList(reportid) {
        const data = await reportFileList({ reportid })
        this.tableData = data.data.data || []
      },
      close() {
        this.formData = {}
        this.dialogFormVisible = false
        this.tableData = []
        this.footer = true
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let attids = ''
            this.tableData.map((item) => {
              attids += item.attid
              attids += ','
            })
            const { reporter, reporttime, reportdepartment, ...other } =
              this.formData
            attids = attids.substring(0, attids.length - 1)
            delete other.sendTime
            const data = await zdyAdd({
              ...other,
              attids,
              reporttime: formatDay(reporttime),
              reportdepartment: this.formData.reportdepartment,
              reporter: this.formData.reporter,
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
      // async handleDelete(row) {
      //   let list = this.tableData
      //   list = list.filter((item) => item.attid != row.attid)
      //   this.tableData = list
      //   await deleteReportFile({ attId: row.attid })
      // },
      handleDelete(row) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          //删除对应的id
          let res = await deleteReportFile({ attId: row.attid })
          if (res.msg === '成功') {
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
            let list = this.tableData
            list = list.filter((item) => item.attid != row.attid)
            this.tableData = list
          } else {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          }
        })
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
