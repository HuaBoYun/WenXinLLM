<template>
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
        ref="elForm"
        label-width="110px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12" v-if="showMJ">
          <el-form-item
            label="密级"
            prop="secrectLevelId"
            :rules="[
              { required: true, trigger: 'change', message: '请选择密级' },
            ]"
          >
            <el-select
              v-model="formData.secrectLevelId"
              clearable
              placeholder="密级"
              style="width: 100%"
              :disabled="!footer"
              @change="changeMJ"
            >
              <el-option
                v-for="item in MJoption"
                :key="item.levelId"
                :label="item.levelName"
                :value="item.levelId"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="showMJ">
          <el-form-item label="知悉范围" prop="staffScopeNames">
            <el-input
              v-model="formData.staffScopeNames"
              readonly
              placeholder="请选择知悉范围"
              :style="{ width: '76%' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.ZXPerson.showEdit(formData.secrectLevelId)"
              :disabled="!footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider></el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="整改报告编号" prop="reportcode">
            <el-input
              v-model="formData.reportcode"
              clearable
              placeholder="请输入整改报告编号"
              :style="{ width: '256px' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="整改报告名称" prop="reportname">
            <el-input
              v-model="formData.reportname"
              clearable
              placeholder="请输入整改报告名称"
              :style="{ width: '256px' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="整改报告类型" prop="reporttype">
            <el-select
              style="width: 100%"
              v-model="formData.reporttype"
              placeholder="选择整改报告类型"
              :style="{ width: '256px' }"
              @change="typeChange"
              :disabled="!footer"
            >
              <el-option label="整改方案报告" :value="1" />
              <el-option label="整改落实报告" :value="2" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="整改方案" prop="planStrsName">
            <el-input
              v-model="formData.planStrsName"
              clearable
              placeholder="请选择整改方案"
              :style="{ width: '256px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              :disabled="!footer"
              @click="$refs.planTable.show({ type: formData.reporttype })"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报告编制人" prop="createStaffName">
            <el-input
              v-model="formData.createStaffName"
              clearable
              placeholder="请输入报告编制人"
              :style="{ width: '256px' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报告编制部门" prop="linkdept">
            <el-input
              v-model="formData.linkdept"
              clearable
              placeholder="请输入报告编制部门"
              :style="{ width: '256px' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报告编制时间" prop="createdate">
            <el-date-picker
              v-model="formData.createdate"
              value-format="yyyy-MM-dd"
              :style="{ width: '256px' }"
              type="date"
              disabled
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-divider>整改清单</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <el-button
              type="success"
              v-if="footer"
              :disabled="!formData.planId"
              @click="openSelectContentList"
            >
              选择
            </el-button>
          </div>
          <el-table :data="issuesList" :key="tableFlag">
            <el-table-column
              align="center"
              label="问题编号"
              prop="issuesCode"
              width="100"
            >
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="handleDetailContent(row, 'detail')"
                  :disabled="false"
                >
                  {{ row.issuesCode }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="问题标题"
              prop="issuesTitle"
              #default="{ row }"
            >
              <el-tooltip placement="top">
                <div
                  slot="content"
                  style="max-width: 600px; white-space: pre-wrap"
                >
                  {{ row.issuesTitle }}
                </div>
                <div class="showOverFlow">
                  {{ row.issuesTitle }}
                </div>
              </el-tooltip>
            </el-table-column>
            <el-table-column
              align="center"
              label="整改方案"
              prop="questionMemo"
              #default="{ row }"
            >
              <el-tooltip placement="top">
                <div
                  slot="content"
                  style="max-width: 600px; white-space: pre-wrap"
                >
                  {{ row.rectificationPlan }}
                </div>
                <div class="showOverFlow">
                  {{ row.rectificationPlan }}
                </div>
              </el-tooltip>
              <!-- <el-tooltip placement="top" v-if="formData.reporttype == 1">
                <div
                  slot="content"
                  style="max-width: 600px; white-space: pre-wrap"
                >
                  {{ row.rectificationPlan }}
                </div>
                <div class="showOverFlow">
                  {{ row.rectificationPlan }}
                </div>
              </el-tooltip>
              <el-tooltip placement="top" v-if="formData.reporttype == 2">
                <div
                  slot="content"
                  style="max-width: 600px; white-space: pre-wrap"
                >
                  {{ row.situationoverView }}
                </div>
                <div class="showOverFlow">
                  {{ row.situationoverView }}
                </div>
              </el-tooltip> -->
            </el-table-column>
            <el-table-column
              align="center"
              label="拟稿人"
              prop="createStaffName"
            />
            <el-table-column
              align="center"
              label="拟稿日期"
              prop="createTime"
              :formatter="formatDate"
            />
            <el-table-column align="center" label="操作" v-if="footer">
              <template #default="{ row }">
                <el-button type="text" @click="handleDeleteContent(row)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>

        <!-- <el-col :span="24" style="margin: 20px 0">
          <UEditor
            ref="ueditor"
            v-model="formData.reportcontect"
            :height="300"
            :templates="templates"
          />
        </el-col> -->

        <el-col :span="24" style="margin-top: 10px">
          <el-divider>整改报告</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              action=""
              :headers="headers"
              :on-preview="handlePreview"
              :on-success="handleSuccess"
              :file-list="fileList"
              :before-upload="handleBeforeUpload"
              :multiple="true"
            >
              <div style="margin-right: 10px">
                <el-button type="success">点击上传</el-button>
              </div>
            </el-upload>
          </div>
          <el-table :data="attList">
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
                  @click="handleDowns(row)"
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

        <!-- <el-col :span="24" style="margin-top: 10px">
          <el-divider>会议文件</el-divider>
        </el-col>
        <el-col :span="24">
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
                  @click="handleDowns(row)"
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
              </template>
            </el-table-column>
          </el-table>
        </el-col> -->
      </el-form>
    </el-row>
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </div>
    <planTable ref="planTable" @selected="planSelected" />
    <reportTable ref="reportTable" @selected="handleContentSelected" />
    <executor-options ref="executor" @selected="handleExecutorSelected" />
    <contentEdit
      v-if="showContentEdit"
      ref="contentEdit"
      @closeDialog="closeDialog"
      :local="true"
    />
    <practicableForm
      v-if="showPracticableForm"
      ref="practicableForm"
      @closeDialog="closeDialog"
      :local="true"
    />
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </el-dialog>
</template>

<script>
  import { download } from '@/api/audit/implement'
  import {
    getIssuesAllDetailInfo,
    saveReport,
    getIssuesAllDetailInfoByPlanIssuesId,
    getReportMeetFileList,
    findAutoNumber,
    delReportFile,
  } from '@/api/zgzz/index.js'
  import store from '@/store'
  import planTable from '@/views/audit/rectify/components/table/planTable'
  import contentEdit from '@/views/audit/rectify/components/options/contentEdit'
  import reportTable from '@/views/audit/rectify/components/table/reportTable'
  import ExecutorOptions from '@/views/audit/rectify/components/options/executor.vue'
  const { baseURL } = require('@/config')
  import * as dayjs from 'dayjs'
  import practicableForm from '@/views/audit/rectify/components/form/practicableForm.vue'
  import UEditor from '@/components/UEditor'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import ZXPerson from '@/components/selectPerson.vue'
  import { getMJ } from '@/api/setting/mjsz'
  import { hasMJ, couldMJ } from '@/utils'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    name: 'ReportInfo',
    components: {
      planTable,
      reportTable,
      ExecutorOptions,
      contentEdit,
      UEditor,
      practicableForm,
      ZXPerson,
    },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        baseApi: baseURL,
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        fileList: [],
        headers: {
          token: store.getters['user/token'],
        },
        templates: [],
        formData: {
          attIds: '', // 当前页面新保存的附件主键数组
          attList: [], // 附件列表
          linkdept: '', // 报告编制部门
          linkorg: '', // 所属公司
          planStrs: '', // 选中的方案和落实显示，jsonArrray
          reportcode: '', // 整改整改报告编号
          reportcontect: '', // 报告内容
          reportid: '', // 整改报告主键
          reportname: '', // 整改报告名称
          reporttype: '', // 报告类型
          // status: '', // 审批状态
          issuesList: [], // 整改清单列表
          createStaffName: '', // 创建人
          planStrsName: '',
          planId: '',
          secrectLevelId: '',
          staffScopeIds: '',
          staffScopeNames: '',
        },
        footer: true,
        tableFlag: false,
        rules: {
          reportcode: [
            {
              required: true,
              message: '请输入整改报告编号',
              trigger: 'blur',
            },
          ],
          reportname: [
            {
              required: true,
              message: '请输入整改报告名称',
              trigger: 'blur',
            },
          ],
          planStrs: [
            {
              required: true,
              message: '请选择整改方案',
              trigger: 'blur',
            },
          ],
          createStaffName: [
            {
              required: true,
              message: '请输入报告编制人',
              trigger: 'blur',
            },
          ],
          createdate: [
            {
              required: true,
              message: '请输入报告编制时间',
              trigger: 'blur',
            },
          ],
          planMemo: [
            {
              required: false,
              message: '请输入备注',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        staffid: '',
        arr: [],
        attList: [],
        issuesList: [],
        showContentEdit: false,
        showPracticableForm: false,
        tableData: [],
        mjId: '', // 密级id
        MJoption: [],
        showMJ: false,
      }
    },
    computed: {
      getFormLevel() {
        if (!this.formData.secrectLevelId) return ''
        const level = this.MJoption.find(
          (item) => item.levelId == this.formData.secrectLevelId
        )
        return level ? level.levelName : ''
      },
    },
    watch: {},
    async created() {
      this.showMJ = couldMJ()
      if (this.showMJ) {
        // 获取密级,菜单id
        const res = await hasMJ('RectifyReport')
        this.menuId = res[0].menuid
        // 请求密级下拉数据
        const res2 = await getMJ({ rightId: res[0].menuid })
        this.MJoption = res2.data
      }
    },
    mounted() {},
    methods: {
      changeMJ(selectedValue) {
        // selectedValue 就是选中的 value（即 levelId）
        const selectedItem = this.MJoption.find(
          (item) => item.levelId === selectedValue
        )
        if (selectedItem) {
          const label = selectedItem.levelName
          if (label == '非密' || label == '公开') {
            this.formData.staffScopeNames = '全部人员'
            this.formData.staffScopeIds = ''
          } else {
            this.formData.staffScopeNames = ''
            this.formData.staffScopeIds = ''
          }
        }
      },
      handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.formData.staffScopeIds = ids
        this.formData.staffScopeNames = names
      },
      async handleDetailContent(row, type) {
        if (this.formData.reporttype == 1) {
          // 方案详情
          this.showContentEdit = true
          if (row.planId && row.issuesId) {
            this.$nextTick(async () => {
              const res = await getIssuesAllDetailInfoByPlanIssuesId({
                planId: row.planId,
                issuesId: row.issuesId,
              })
              if (res && res.data) {
                this.$refs.contentEdit.showEdit(type, res.data)
              }
            })
          } else {
            this.$message.error('主键缺失')
          }
          return
        }

        if (this.formData.reporttype == 2) {
          // 落实详情
          this.showPracticableForm = true
          this.$nextTick(async () => {
            if (row.planId && row.issuesId) {
              const res = await getIssuesAllDetailInfoByPlanIssuesId({
                planId: row.planId,
                issuesId: row.issuesId,
              })
              if (res && res.data) {
                this.$refs.practicableForm.showEdit(type, res.data)
              }
            }
          })
          return
        }
      },
      handleDeleteContent(row) {
        const i = this.issuesList.findIndex((x) => x.issuesId === row.issuesId)
        this.issuesList.splice(i, 1)
      },
      closeDialog() {
        this.showContentEdit = false
        this.showPracticableForm = false
      },
      typeChange(e) {
        this.formData.reporttype = e
        this.formData.planStrs = ''
        this.formData.planStrsName = ''
        this.issuesList = []
      },
      handleContentSelected(node) {
        console.log('node222', node)
        this.issuesList = node
        this.tableFlag = true
      },
      openSelectContentList() {
        this.$refs.reportTable.show({
          reporttype: this.formData.reporttype,
          planIdStrs: this.formData.planId,
          selected: this.issuesList,
        })
      },
      planSelected(node) {
        console.log('node', node)
        if (!node || !node.length) return
        this.issuesList = []
        node.map((x) => {
          if (x.issuesList && x.issuesList.length) {
            const issuesId = x.issuesList
              .map((y) => {
                this.issuesList.push({
                  ...y,
                  ...y.issues,
                  planId: x.planId,
                  rectificationPlan: y.rectificationPlan,
                })
                return y.issues.issuesId
              })
              .join(',')
          }
        })

        this.formData.planStrsName = node.map((x) => x.planName).join(',')
        this.formData.planId = node.map((x) => x.planId).join(',')
      },
      showEdit(title, row) {
        console.log('🚀 ~ showEdit ~ row:', row)
        this.dialogFormVisible = true
        if (row) {
          Object.assign(this.formData, row)
          this.attList = JSON.parse(JSON.stringify(row.attList))
          this.issuesList = row.issuesList
          console.log('this.formData', this.formData)
          this.formData.createdate = dayjs(this.formData.createdate).format(
            'YYYY-MM-DD'
          )
          this.formData.planStrsName = row.planStrs
          this.formData.planId = row.planIdStrs
          getReportMeetFileList({ reportid: row.reportid }).then((res) => {
            this.tableData = res.data.list
          })

          const planStrs = []
          if (row.planIdStrs && row.issuesList && row.issuesList.length) {
            const planIdStrs = row.planIdStrs.split(',')
            planIdStrs.map((x) => {
              planStrs.push({
                planId: x,
                issuesId: row.issuesList
                  .filter((y) => y.planId === x)
                  .map((j) => j.issuesId)
                  .join(','),
              })
            })
          }
          this.formData.planStrs = JSON.stringify(planStrs)
        } else {
          this.formData = {
            attIds: '',
            attList: [],
            linkdept: '',
            linkorg: '',
            planStrs: '',
            reportcode: '',
            reportcontect: '',
            reportid: '',
            reportname: '',
            reporttype: '',
            issuesList: [],
            createStaffName: '',
            planStrsName: '',
            planId: '',
            secrectLevelId: '',
            staffScopeIds: '',
            staffScopeNames: '',
          }
          this.attList = []
          this.issuesList = []
          this.tableData = []
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else {
          this.title = '新增'
          this.getFindAutoNumber()
          this.formData.reporttype = 1
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.createStaffName = userInfo.realname
          this.formData.linkdept = userInfo.linkDetp.orgname
          // this.formData.linkorg = userInfo.linkDetp.orgid
          this.formData.createdate = new Date()
        }
      },
      getFindAutoNumber() {
        findAutoNumber({
          column: 'REPORTCODE',
          noId: '329',
          orgCol: 'ORGID',
          tblName: 'TBL_ZGZZ_REPORT',
        }).then((res) => {
          this.formData.reportcode = res.data
        })
      },
      close() {
        this.$emit('closeDialog')
      },
      add() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            const attListArr = []
            this.attList.map((item) => {
              if (this.formData.attList.some((x) => x.attid === item.attid)) {
              } else attListArr.push(item.attid)
            })
            const attIds = attListArr.join(',')

            // 整改清单整合
            const planStrs = [],
              tempObj = {}
            this.issuesList.map((x) => {
              if (!tempObj[x.planId]) {
                tempObj[x.planId] = [x.issuesId]
              } else {
                tempObj[x.planId].push(x.issuesId)
              }
            })

            Object.keys(tempObj).forEach((key) => {
              planStrs.push({
                planId: key,
                issuesId: tempObj[key].join(','),
              })
            })

            const reqData = {
              attIds,
              planStrs: JSON.stringify(planStrs),
              reportcode: this.formData.reportcode,
              reportcontect: this.formData.reportcontect,
              reportid: this.formData.reportid,
              reportname: this.formData.reportname,
              reporttype: this.formData.reporttype,
              planId: this.formData.planId,
              secrectLevelId: this.formData.secrectLevelId,
              staffScopeIds: this.formData.staffScopeIds,
              staffScopeNames: this.formData.staffScopeNames,
            }

            const res = await saveReport(reqData)

            if (res.code == 1) {
              this.$baseMessage('保存成功', 'success')
              this.$emit('fetch-data')
              this.close()
            }
          } else {
            console.log('error submit!!')
            return false
          }
        })
      },

      async handleDelete(row) {
        let list = this.attList || []
        list = list.filter((item) => item.attid != row.attid)
        await delReportFile({
          attId: String(row.attid),
          reportid: this.formData.reportid,
        })
        this.$message.success('删除成功')
        this.attList = list
      },

      handleExecutorSelected(node, type) {
        if (type === 'createstaff') {
          this.formData.createstaff = node.realname
          this.formData.handlerId = node.staffid
        } else if (type === 'zrrRealName') {
          this.formData.zrrRealName = node.realname
          this.formData.response = node.staffid
        }
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return dayjs(data).format('YYYY-MM-DD')
      },

      handleBeforeUpload(file, fileList) {
        const isLt2M = file.size / 1024 / 1024 < 100 // 检查文件大小是否小于100MB
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 100MB!')
          return false // 返回false停止上传
        }
        // 如果文件大小合适，则调用自定义上传逻辑
        this.customUploadWrapper({ file })
        return false // 停止默认上传行为
      },
      customUploadWrapper(options) {
        if (
          !this.baseApi ||
          !this.api ||
          !this.headers ||
          !window.key ||
          !window.iv
        ) {
          return
        }
        // 确保 fileList 是一个数组
        const fileList = Array.isArray(options.file)
          ? options.file
          : [options.file]

        // 获取 el-upload 的 data 参数
        const formData = {
          formlevel: this.getFormLevel,
        }

        // 调用自定义上传函数
        new Promise((resolve, reject) => {
          customUpload({
            baseApi: this.baseApi,
            api: this.api,
            key: window.key,
            iv: window.iv,
            headers: this.headers,
            fileList: fileList, // 使用 fileList 而不是 file
            formData: formData, // 传递额外的表单数据
            onProgress: this.handleProgress,
            onSuccess: (response) => {
              this.handleSuccess(response)
              resolve(response) // 成功时调用 resolve
            },
            onError: (error) => {
              // this.handleError(error)
              reject(error) // 失败时调用 reject
            },
          })
        })
      },
      handleSuccess(file) {
        if (file.code == 200) {
          // 更新文件列表
          this.fileList = [...this.fileList, ...file.data]
          this.attList = [...this.attList, ...file.data]
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      //下载公共方法调用
      async handleDowns(row) {
        try {
          // 调用 handleDown 并传递自定义的下载接口
          await handleDown(row, this.headers, this.lodeapi)
        } catch (error) {
          console.error('自定义下载失败:', error)
        }
      },
      handlePreviewFile(row) {
        if (row.isEncrypted === '1') {
          // 当文件是加密状态时，使用指定的在线预览链接
          const previewUrl = row.previewUrl
          window.open(previewUrl, '_blank')
        } else {
          this.$iFrameDialog({ attid: row.attid })
        }
      },
      async handlePreview(row) {
        const { data } = await getPrivewAttInfo({
          attId: row.attid,
          attType: 0,
        })
        let url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        if (
          data.ftpUrl.includes('.pdf') ||
          data.ftpUrl.includes('.doc') ||
          data.ftpUrl.includes('.docx')
        ) {
          url = url + '&officePreviewType=pdf'
        }
        window.open(url)
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
  .showOverFlow {
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    text-overflow: ellipsis;
    line-height: 1.5em;
    max-height: 3em;
    white-space: pre-wrap;
  }
</style>
