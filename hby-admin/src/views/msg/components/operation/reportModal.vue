<template>
  <el-dialog
    v-if="dialogFormVisible"
    :append-to-body="true"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-tabs v-show="dialogFormVisible" v-model="activeName" type="card">
      <el-tab-pane label="基本信息" name="first">
        <h3>审计报告-审批</h3>
        <el-row :gutter="14">
          <el-form
            ref="ruleForm"
            label-width="100px"
            :model="formData"
            :rules="rules"
            size="mini"
          >
            <el-col :span="12">
              <el-form-item
                label="报告名称"
                label-width="140px"
                prop="reportname"
              >
                <el-input
                  v-model="formData.reportname"
                  clearable
                  placeholder="请输入报告名称"
                  :style="{ width: '256px' }"
                  :disabled="!cloudEdit"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="报告时间"
                label-width="140px"
                prop="reporttime"
              >
                <el-date-picker
                  v-model="formData.reporttime"
                  placeholder="选择报告时间"
                  type="date"
                  format="yyyy-MM-dd"
                  value-format="yyyy-MM-dd"
                  :style="{ width: '256px' }"
                  :disabled="!cloudEdit"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="报告版本"
                label-width="140px"
                prop="reporttype"
              >
                <el-select
                  v-model="formData.reporttype"
                  placeholder="请选择报报告版本"
                  :disabled="!cloudEdit"
                  :style="{ width: '256px' }"
                >
                  <el-option label="草稿" value="草稿" />
                  <el-option label="终稿草稿" value="终稿草稿" />
                  <el-option label="终稿" value="终稿" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="报告方式"
                label-width="140px"
                prop="reportmode"
              >
                <el-select
                  v-model="formData.reportmode"
                  placeholder="请选择报告方式"
                  :disabled="!cloudEdit"
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
                  :disabled="!cloudEdit"
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
                  :disabled="!cloudEdit"
                >
                  选择
                </el-button>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="复核人"
                label-width="140px"
                prop="fhstaffname"
              >
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
                  :disabled="!cloudEdit"
                >
                  选择
                </el-button>
              </el-form-item>
            </el-col>
            <el-col :span="12">
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
                  :disabled="!cloudEdit"
                >
                  选择
                </el-button>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="编辑器" prop="repdesc">
                <UEditor
                  ref="ueditor"
                  v-model="formData.repdesc"
                  :height="300"
                  :templates="templates"
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
                  :disabled="!cloudEdit"
                  :before-upload="handleBeforeUpload"
                >
                  <el-button type="success" :disabled="!cloudEdit">
                    上传
                  </el-button>
                </el-upload>
              </div>
              <el-table :data="tableDataFile">
                <el-table-column align="center" label="附件名称" prop="name" />
                <el-table-column
                  align="center"
                  label="文件大小(KB)"
                  prop="size"
                />
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
                      :disabled="!cloudEdit"
                      type="text"
                      @click="handleDeleteFile(scope.$index, scope.row)"
                    >
                      删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-col>
            <el-col :span="24">
              <el-divider>审批记录</el-divider>
            </el-col>
            <el-col
              :span="24"
              v-if="aoptionList.length"
              style="margin-bottom: 16px"
            >
              <el-table :data="aoptionList">
                <el-table-column
                  align="center"
                  label="审批人"
                  width="200px"
                  prop="staffidName"
                />
                <el-table-column align="center" label="意见" prop="optDesc" />
                <el-table-column align="center" label="时间" prop="createDate">
                  <template slot-scope="scope">
                    <div>{{ formatDay(scope.row.createDate) }}</div>
                  </template>
                </el-table-column>

                <el-table-column align="center" label="结果" prop="optState" />
              </el-table>
            </el-col>

            <el-col :span="24">
              <el-divider>审批意见</el-divider>
            </el-col>
            <el-col :span="24">
              <el-form-item label="审批意见" prop="optDesc">
                <el-input
                  type="textarea"
                  :rows="2"
                  placeholder="请输入审批意见"
                  v-model="formData.optDesc"
                  :disabled="hiddenButton"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <div v-if="!hiddenButton">
                <el-button @click="add" type="primary">保存</el-button>
                <el-button
                  type="primary"
                  v-for="(value, index) in buttonList"
                  :key="index"
                  @click="handleSubmit(index)"
                >
                  {{ value ? value : '提交' }}
                </el-button>
              </div>
            </el-col>
          </el-form>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="审批查看" name="second">
        <el-row :gutter="24">
          <el-col :span="24">
            <img
              alt="审批图"
              :src="imgurl"
              style="margin-bottom: 20px; width: 100%"
            />
          </el-col>
        </el-row>
      </el-tab-pane>
    </el-tabs>

    <!-- <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </div> -->

    <department-options ref="department" @selected="handleDepartmentSelected" />
    <executor-options ref="executor" @selected="handleExecutorSelected" />
    <!-- 复核人选择 -->
    <projectManage ref="manage" @reviewTypeSelect="reviewTypeSelect" />
    <projectManage1 ref="manage1" @reviewTypeSelect="reviewTypeSelect1" />
  </el-dialog>
</template>

<script>
  import { deleteReportFile, download } from '@/api/audit/implement'
  import { reportAdd, reportFileList } from '@/api/audit/report'
  import {
    handleReport,
    handleReportButtonClick,
  } from '@/api/workbench/auditTools'
  import Tinymce from '@/components/Tinymce'
  import UEditor from '@/components/UEditor'
  import { baseURL } from '@/config'
  import store from '@/store'
  import { formatDay } from '@/utils/index'
  import {
    default as projectManage,
    default as projectManage1,
  } from '@/views/audit/project/components/formComponents/projectManage.vue'
  import DepartmentOptions from '@/views/audit/report/components/options/department.vue'
  import ExecutorOptions from '@/views/audit/report/components/options/executor.vue'
  export default {
    name: 'ProjectDataInfo',
    components: {
      Tinymce,
      UEditor,
      DepartmentOptions,
      ExecutorOptions,
      projectManage,
      projectManage1,
    },
    inheritAttrs: false,
    props: ['UEditorCloudEdit'],
    data() {
      return {
        baseApi: baseURL,
        api: '/fileManage/upload',
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
          optDesc: undefined,
          zqyjstaffname: undefined,
          zqyjstaffid: undefined,
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
              trigger: 'blur',
            },
          ],
          optDesc: [
            {
              required: true,
              message: '请输入审批意见',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        activeName: 'first',
        buttonList: [],
        rowsData: {},
        handleData: {},
        imgurl: '',
        cloudEdit: false,
        fileList: [],
        tableDataFile: [],
        fileIds: [],
        aoptionList: [],
        formatDay,
        hiddenButton: false,
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
      async showEdit(row, names) {
        if (names === '查看') {
          this.hiddenButton = true
          this.cloudEdit = false
        }
        this.dialogFormVisible = true
        const ids = row.cyurl.split('=')[1]
        await this.getFileList(ids)
        let res = await handleReport({
          cyId: row.cyid,
          reportid: ids,
          taskId: row.taskid,
        })
        this.formData = {
          ...res.data.report,
        }

        this.imgurl =
          baseURL + `/audit/nbsjapproval/picture?taskId=` + row.taskid
        //处理除了富文本框之外的按钮是否禁用
        let userInfo = JSON.parse(localStorage.getItem('userInfo'))
        // 当viewOppsiteProcessInfo接口中的cystaffid与获取用户信息中的staffid相等且cystate等于需调整 基本信息改成可编辑 编号不可编辑
        if (
          row.cystaffid == userInfo.staffid &&
          res.data.cy.cystate == '需调整'
        ) {
          this.cloudEdit = true
        } else {
          this.cloudEdit = false
        }
        this.aoptionList = res.data.aoptionList || []

        this.buttonList = res.data.btnList ? res.data.btnList : ['提交']
        this.handleData = res.data.report
        this.rowsData = row
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
        this.dialogFormVisible = false
        this.tableData = []
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
              this.$message({
                type: 'success',
                message: '保存成功',
              })
            } else {
              this.$message({
                type: 'error',
                message: '保存失败',
              })
            }
          }
        })
      },
      async handleDownload(row) {
        const res = await download({ attid: row.attid })
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
      handleSubmit(index) {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const params = {
              reportid: this.handleData.reportid,
              optDesc: this.formData.optDesc,
              cyId: this.rowsData.cyid,
              taskId: this.rowsData.taskid,
              transition: this.buttonList[index] || '提交',
            }
            let { code } = await handleReportButtonClick({
              ...params,
            })
            if (code === 1) {
              this.$message.success('办理成功')
              this.dialogFormVisible = false
              this.cloudEdit = false
              this.$emit('reload-data')
            } else {
              this.$message.success('办理失败')
              this.cloudEdit = false
              this.$emit('reload-data')
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
