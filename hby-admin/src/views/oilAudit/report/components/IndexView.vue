<template>
  <el-dialog
    v-if="dialogFormVisible"
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
          <el-form-item label="报告类型" label-width="140px" prop="reporttype">
            <el-select
              v-model="formData.reporttype"
              placeholder="请选择报告类型"
              :disabled="!footer"
              :style="{ width: '256px' }"
            >
              <el-option label="管理建议书" value="管理建议书" />
              <el-option label="审计报告" value="审计报告" />
              <!-- <el-option label="审计专报" value="审计专报" />
              <el-option label="审计移送书" value="审计移送书" />
              <el-option label="其他" value="其他" /> -->
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
              disabled
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
              disabled
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
          <el-form-item label="关联OA" label-width="140px" prop="title">
            <el-input
              v-model="formData.title"
              clearable
              placeholder="请选择OA信息"
              :style="{ width: '80%' }"
              disabled
            />
            <el-button
              style="margin-left: 20px"
              type="primary"
              @click="openOA()"
            >
              选择
            </el-button>
            <el-button
              v-if="formData.url"
              style="margin-left: 20px"
              type="primary"
              @click="detail()"
            >
              详情
            </el-button>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="复核人" label-width="140px" prop="fhstaffname">
            <el-input
              v-model="formData.fhstaffname"
              clearable
              placeholder="请选择复核人"
              style="width: 256px"
              disabled
            />
            <el-button
              @click="projectManager1"
              style="margin-left: 10px"
              type="primary"
              :disabled="!footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col> -->
        <!-- <el-col :span="12">
          <el-form-item
            label="征求意见人"
            label-width="140px"
            prop="zqyjstaffname"
          >
            <el-input
              v-model="formData.zqyjstaffname"
              clearable
              placeholder="请选择征求意见人"
              style="width: 256px"
              disabled
            />
            <el-button
              @click="projectManager2"
              style="margin-left: 10px"
              type="primary"
              :disabled="!footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col> -->
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
              template="nbsj"
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
              :on-success="handleSuccess"
              :file-list="fileList"
              :before-upload="handleBeforeUpload"
            >
              <el-button type="success">上传</el-button>
            </el-upload>
          </div>
          <el-table :data="tableDataFile">
            <el-table-column align="center" label="附件名称" prop="name" />
            <el-table-column align="center" label="文件大小(KB)" prop="size" />
            <el-table-column
              align="center"
              label="创建人"
              prop="createPerson"
            />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template slot-scope="scope">
                <el-button
                  :disabled="false"
                  type="text"
                  @click="handleDownload(scope.row)"
                >
                  下载
                </el-button>
                <el-button
                  v-if="footer"
                  type="text"
                  @click="handleDeleteFile(scope.$index, scope.row)"
                >
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
    <!-- 复核人,征求一见人选择 -->
    <projectManage ref="manage" @reviewTypeSelect="reviewTypeSelect" />
    <projectManage1 ref="manage1" @reviewTypeSelect="reviewTypeSelect1" />
    <oaList ref="oaList" @selected="handleOA" />
  </el-dialog>
</template>

<script>
  import { getOaurl } from '@/oapi/contract/manage'
  import { deleteReportFile, download } from '@/oapi/audit/implement'
  import { reportAdd, reportFileList } from '@/oapi/audit/report'
  import Tinymce from '@/components/Tinymce'
  import UEditor from '@/components/UEditor'
  import store from '@/store'
  import { formatDay } from '@/utils/index'
  import {
    default as projectManage,
    default as projectManage1,
  } from '@/views/oilAudit/project/components/formComponents/projectManage.vue'
  import oaList from '@/views/oilAudit/prepare/components/oaList.vue'
  import DepartmentOptions from './options/department.vue'
  import ExecutorOptions from './options/executor.vue'
  const { baseURL } = require('@/config')
  export default {
    name: 'ProjectDataInfo',
    components: {
      Tinymce,
      UEditor,
      DepartmentOptions,
      ExecutorOptions,
      projectManage,
      projectManage1,
      oaList,
    },
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
          fhstaffname: undefined,
          zqyjstaffname: undefined,
          zqyjstaffid: undefined,
          title: '',
          oaid: '',
          h5url: '',
          url: '',
        },
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
          // reportmode: [
          //   {
          //     required: true,
          //     message: '请选择报告方式',
          //     trigger: 'blur',
          //   },
          // ],
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
              trigger: 'change',
            },
          ],
          reporter: [
            {
              required: true,
              message: '请输入报告人',
              trigger: 'blur',
            },
          ],
          fhstaffname: [
            {
              required: true,
              message: '请输入复核人',
              trigger: 'blur',
            },
          ],
          reportdepartment: [
            {
              required: true,
              message: '请输入报告部门',
              trigger: 'change',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        disabled: false,
        fileList: [],
        tableDataFile: [],
        fileIds: [],
      }
    },
    computed: {},
    watch: {
      'formData.repdesc'(val) {
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
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      handleDepartmentSelected(node) {
        //保存名称
        this.$set(this.formData, `reportdepartment`, node.name)
        //保存名称对应的ID
        this.$set(this.formData, `reportdepartmentid`, node.id)
      },
      handleExecutorSelected(node) {
        //保存名称
        this.$set(this.formData, `reporter`, node.realname)
        //保存名称对应的ID
        this.$set(this.formData, `reporterid`, node.staffid)
      },
      showEdit(title, row) {
        this.dialogFormVisible = true
        if (row) {
          this.formData = row.report
          this.getFileList(row.report.reportid)
        }
        if (title == 'edit') {
          this.title = '编辑'
          this.fileIds = []
          this.tableDataFile = []
          this.fileList = []
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else {
          this.title = '新增'
          this.footer = true
          this.formData = {}
          this.tableDataFile = []
          this.fileIds = []
          this.fileList = []
        }
      },
      async getFileList(reportid) {
        const res = await reportFileList({ reportid })
        //回填上传文件表格
        const arr = res.data.data
        const arr1 = arr.map((item) => {
          return {
            ...item,
            name: item.attname,
            size: item.attsize,
            createPerson: item.uploader,
          }
        })
        const arr2 = arr.map((res) => {
          return res.attid
        })
        //收集id
        this.fileIds = arr2
        this.tableDataFile = arr1
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
            const {
              reporter,
              reporttime,
              reportdepartment,
              fhstaffid,
              fhstaffname,
              zqyjstaffname,
              zqyjstaffid,
              ...other
            } = this.formData
            delete other.sendTime
            const data = await reportAdd({
              ...other,
              attids: this.fileIds.toString() || '',
              reporttime: formatDay(reporttime),
              fhstaffid,
              fhstaffname,
              sendTime: formatDay(reporttime),
              reportdepartment,
              reporter,
              zqyjstaffname,
              zqyjstaffid,
            })
            if (data.code == 1) {
              this.$baseMessage('保存成功', 'success')
              this.close()
              this.$emit('fetch-data')
            } else {
              this.$baseMessage(data.msg, 'error')
            }
          } else {
            return false
          }
        })
      },

      async handleDownload(row) {
        const res = await download({ attId: row.attid })
        this.downloadFileByBlob(res, row.name)
      },
      downloadFileByBlob(blob, fileName = 'file') {
        let blobUrl = window.URL.createObjectURL(blob)
        let link = document.createElement('a')
        link.download = fileName || 'defaultName'
        link.style.display = 'none'
        link.href = blobUrl
        // 触发点击
        document.body.appendChild(link)
        link.click()
        // 移除
        document.body.removeChild(link)
      },

      handleSuccess(response, file, fileList) {
        if (file.response.result == '200') {
          file.createPerson = JSON.parse(
            localStorage.getItem('userInfo')
          ).realname
          let arr = file.response.data
          const arr1 = {
            name: arr.attname,
            size: arr.attsize,
            createPerson: arr.uploader,
            attid: arr.attid,
          }
          this.tableDataFile.push(arr1)

          this.fileList = fileList
          let fileArr = []
          this.fileList.forEach((item) => {
            fileArr.push(item.response.data.attid)
          })
          this.fileIds = [...this.fileIds, ...fileArr]
          this.$baseMessage(file.response.msg, 'success')
        } else {
          this.$baseMessage(file.response.msg, 'error')
        }
      },
      handleDeleteFile(index, row) {
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
            this.fileIds.splice(index, 1)
            this.tableDataFile.splice(index, 1)
          } else {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          }
        })
      },
      projectManager1() {
        this.$refs['manage'].showEdit('fhstaff')
      },
      projectManager2() {
        this.$refs['manage1'].showEdit('zqyjstaff')
      },
      reviewTypeSelect(e) {
        //保存名称
        this.$set(this.formData, `${e.reviewType}name`, e.id[0].realname)
        //保存名称对应的ID
        this.$set(this.formData, `${e.reviewType}id`, e.id[0].staffid)
      },
      reviewTypeSelect1(e) {
        //保存名称
        this.$set(this.formData, `${e.reviewType}name`, e.id[0].realname)
        //保存名称对应的ID
        this.$set(this.formData, `${e.reviewType}id`, e.id[0].staffid)
      },
      openOA() {
        this.$refs.oaList.show()
      },
      handleOA(e) {
        this.$set(this.formData, `title`, e[0].subject)
        this.$set(this.formData, `oaid`, e[0].id)
        this.$set(this.formData, `h5url`, e[0].h5Url)
        this.$set(this.formData, `url`, e[0].url)
        // this.formData.title = e[0].subject
        // this.formData.oaid = e[0].id
        // this.formData.h5url = e[0].h5Url
        // this.formData.url = e[0].url
      },
      async detail() {
        const res = await getOaurl()
        this.ticket = res.data.ticket
        this.oaurl = res.data.oaurl.substring(0, res.data.oaurl.length - 1)
        window.open(`${this.oaurl}${this.formData.url}&ticket=${this.ticket}`)
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
