<template>
  <div>
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="100px"
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
              disabled
              placeholder="请选择知悉范围"
              :style="{ width: '76%' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.ZXPerson.showEdit(formData.secrectLevelId)"
              :disabled="!formData.secrectLevelId || !footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider></el-divider>
        </el-col>

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
              <el-option label="审计专报" value="审计专报" />
              <el-option label="审计移送书" value="审计移送书" />
              <el-option label="其他" value="其他" />
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
              @click="$refs.executor.showEdit()"
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
              :disabled="!footer"
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

        <el-col :span="24">
          <el-form-item label="" prop="repdesc">
            <UEditor
              ref="ueditor"
              v-model="formData.repdesc"
              :height="300"
              :templates="templates"
              :disabled="!footer"
              template="SJJGWS"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>初稿上传</el-divider>
        </el-col>
        <el-col :span="24" style="margin-bottom: 20px">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-upload
              style="text-align: right; margin-bottom: 5px"
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
          <el-table :data="tableDataFile">
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
              width="120"
            >
              <template slot-scope="scope">
                <el-button
                  :disabled="false"
                  type="text"
                  @click="handleDowns(scope.row)"
                >
                  下载
                </el-button>
                <el-button type="text" @click="handlePreviewFile(scope.row)">
                  预览
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

        <el-col :span="24">
          <el-divider>复核意见稿上传</el-divider>
        </el-col>
        <el-col :span="24" style="margin-bottom: 20px">
          <div
            style="text-align: right; margin-bottom: 5px"
            v-if="nextNodeName == '审计法务部人员'"
          >
            <el-upload
              style="text-align: right; margin-bottom: 5px"
              class="upload-demo"
              :show-file-list="false"
              action=""
              :headers="headers"
              :on-preview="handlePreview"
              :on-success="handleOpinionSuccess"
              :file-list="fileList"
              :before-upload="handleBeforeUpload"
              :multiple="true"
            >
              <div style="margin-right: 10px">
                <el-button type="success">点击上传</el-button>
              </div>
            </el-upload>
          </div>
          <el-table :data="opinionFile">
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
              width="120"
            >
              <template slot-scope="scope">
                <el-button
                  :disabled="false"
                  type="text"
                  @click="handleDowns(scope.row)"
                >
                  下载
                </el-button>
                <el-button type="text" @click="handlePreviewFile(scope.row)">
                  预览
                </el-button>
                <el-button
                  v-if="nextNodeName == '审计法务部人员'"
                  type="text"
                  @click="handleDeleteFile(scope.$index, scope.row, 'opinion')"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24" style="margin-bottom: 20px">
          <el-divider>终稿上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div
            style="text-align: right; margin-bottom: 5px"
            v-if="nextNodeName == 'end'"
          >
            <!-- <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-success="handleFinalSuccess"
              :file-list="fileList"
              :before-upload="handleBeforeUpload"
            >
              <el-button type="success">上传</el-button>
            </el-upload> -->

            <el-upload
              style="text-align: right; margin-bottom: 5px"
              class="upload-demo"
              :show-file-list="false"
              action=""
              :headers="headers"
              :on-preview="handlePreview"
              :on-success="handleFinalSuccess"
              :file-list="fileList"
              :before-upload="handleBeforeUpload"
              :multiple="true"
            >
              <div style="margin-right: 10px">
                <el-button type="success">点击上传</el-button>
              </div>
            </el-upload>
          </div>
          <el-table :data="finalFile">
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
              width="120"
            >
              <template slot-scope="scope">
                <el-button
                  :disabled="false"
                  type="text"
                  @click="handleDowns(scope.row)"
                >
                  下载
                </el-button>
                <el-button type="text" @click="handlePreviewFile(scope.row)">
                  预览
                </el-button>

                <!-- 终稿表格的删除按钮 -->
                <el-button
                  v-if="footer || nextStepId == 'end'"
                  type="text"
                  @click="handleDeleteFile(scope.$index, scope.row, 'final')"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <div style="text-align: right; margin-top: 10px">
      <el-button
        v-if="nextNodeName == '审计法务部人员' || nextStepId == 'end'"
        @click="saveFile"
        type="primary"
      >
        保存附件
      </el-button>
      <el-button type="primary" @click="add" v-if="footer">确 定</el-button>
      <el-button
        type="primary"
        @click="ymsubmit"
        :disabled="btnLoading"
        v-if="footer"
      >
        提 交
      </el-button>
    </div>
    <department-options ref="department" @selected="handleDepartmentSelected" />
    <executor-options ref="executor" @projectManage="handleExecutorSelected" />
    <!-- 复核人,征求一见人选择 -->
    <projectManage ref="manage" @reviewTypeSelect="reviewTypeSelect" />
    <projectManage1 ref="manage1" @reviewTypeSelect="reviewTypeSelect1" />
    <oaList ref="oaList" @selected="handleOA" />
    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />

    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </div>
</template>

<script>
  import { getOaurl } from '@/api/contract/manage'
  import { deleteReportFile, download } from '@/api/audit/implement'
  import {
    reportAdd,
    reportFileList,
    getOpinionList,
    getFinalList,
    saveFinal,
    saveOpinion,
  } from '@/api/audit/report'
  import Tinymce from '@/components/Tinymce'
  import UEditor from '@/components/UEditor'
  import store from '@/store'
  import { formatDay } from '@/utils/index'
  import {
    default as projectManage,
    default as projectManage1,
  } from '@/views/audit/project/components/formComponents/projectManage.vue'
  import oaList from '@/views/audit/prepare/components/oaList.vue'
  import DepartmentOptions from '@/views/audit/report/components/options/department.vue'
  import ExecutorOptions from '@/components/danxuanPerson.vue'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import { reportDetail } from '@/api/audit/report'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import ZXPerson from '@/components/selectPerson.vue'
  import { getMJ } from '@/api/setting/mjsz'
  import { hasMJ, couldMJ } from '@/utils'
  import { getSPMJ } from '@/api/setting/mjsz'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
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
      Resubmit,
      ZXPerson,
    },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        baseApi: baseURL,
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
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
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
        },
        fileList: [],
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
        fromId: '',
        flowtaskinfoflowid: '',
        ymFromId: '',
        status: '',
        fromIdcopy: '',
        btnLoading: false,
        opinionFileIds: [],
        opinionFile: [],
        finalFileIds: [],
        finalFile: [],
        nextNodeName: '',
        nextStepId: '',
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
    // async created() {
    //   this.showMJ = couldMJ()
    //   if (this.showMJ) {
    //     // 获取密级,菜单id
    //     const res = await hasMJ('ReportIndex')
    //     this.menuId = res[0].menuid
    //     // 请求密级下拉数据
    //     const res2 = await getMJ({ rightId: res[0].menuid })
    //     this.MJoption = res2.data
    //   }
    // },
    mounted() {},
    methods: {
      changeMJ(selectedValue) {
        const selectedItem = this.MJoption.find(
          (item) => item.levelId === selectedValue
        )
        if (selectedItem) {
          const label = selectedItem.levelName
          if (label == '非密' || label == '公开') {
            this.formData.staffScopeNames = '全部人员'
            this.formData.staffScopeIds = ''
          } else {
            this.formData.staffScopeIds = ''
            this.formData.staffScopeNames = ''
          }
        }
      },
      async getMJData(id) {
        this.showMJ = couldMJ()
        if (this.showMJ) {
          const res2 = await getSPMJ({ flowType: id })
          this.MJoption = res2.data
        }
      },
      handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.formData.staffScopeIds = ids
        this.formData.staffScopeNames = names
      },
      //提取数据
      handleFileData(fileData) {
        const files = fileData.map((item) => ({
          ...item,
          name: item.attname,
          size: item.attsize,
          createPerson: item.uploader,
        }))
        const fileIds = fileData.map((item) => item.attid)
        return { files, fileIds }
      },
      //复核附件
      async getOpinionList(id) {
        try {
          const { data } = await getOpinionList({ reportid: id })
          const { files, fileIds } = this.handleFileData(data.data)
          this.opinionFileIds = fileIds
          this.opinionFile = files
        } catch (err) {
          console.error('获取复核意见稿失败:', err)
          this.$message.error('获取复核意见稿失败')
        }
      },
      //最终附件
      async getFinalList(id) {
        try {
          const { data } = await getFinalList({ reportid: id })
          const { files, fileIds } = this.handleFileData(data.data)
          this.finalFileIds = fileIds
          this.finalFile = files
        } catch (err) {
          console.error('获取终稿失败:', err)
          this.$message.error('获取终稿失败')
        }
      },

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
        this.$set(this.formData, `reporter`, node[0].realname)
        //保存名称对应的ID
        this.$set(this.formData, `reporterid`, node[0].staffid)
      },
      async showEdit(
        title,
        fromId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status,
        nextNodeName,
        flowType,
        nextStepId
      ) {
        console.log('🚀 ~ nextStepId:', nextStepId)
        console.log('🚀 ~ nextNodeName:', nextNodeName)
        this.dialogFormVisible = true
        // 流程相关
        this.nextNodeName = nextNodeName
        this.nextStepId = nextStepId
        this.fromId = fromId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
        if (flowType) {
          this.getMJData(flowType)
        }
        if (fromId) {
          const data = await reportDetail({ reportid: fromId })
          // 保存审批用的密级id
          if (data.data.report.secrectLevelId) {
            localStorage.setItem(
              'SPsecrectLevelId',
              data.data.report.secrectLevelId
            )
          }
          Object.assign(this.formData, data.data.report)
          this.getFileList(fromId)
          this.getOpinionList(fromId)
          this.getFinalList(fromId)
        }
        if (title == 'edit') {
          this.title = '编辑'
          this.fileIds = []
          this.tableDataFile = []
          this.fileList = []
          this.footer = true
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
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

      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
      close() {
        this.dialogFormVisible = false
        this.tableData = []
        this.footer = true
        this.$bus.$emit('updateMsg', 0)
      },
      async saveOpinionList() {
        await saveOpinion({
          reportid: this.fromId,
          attids: this.opinionFileIds.join(','),
        })
      },
      async saveFinalList() {
        await saveFinal({
          reportid: this.fromId,
          attids: this.finalFileIds.join(','),
        })
      },

      async saveFile() {
        try {
          if (this.nextNodeName === '审计法务部人员') {
            // 复核意见稿保存
            await this.saveOpinionList()
            this.$baseMessage('复核意见稿保存成功', 'success')
          } else if (this.nextStepId === 'end') {
            // 终稿保存
            await this.saveFinalList()
            this.$baseMessage('终稿保存成功', 'success')
          }
        } catch (error) {
          console.error('保存附件失败:', error)
          this.$baseMessage('保存附件失败，请重试', 'error')
        }
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
            delete other.tblReporttemple
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
              this.$emit('fetchData')
            } else {
              this.$baseMessage(data.msg, 'error')
            }
          } else {
            return false
          }
        })
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

      // 处理初稿上传
      handleDraftSuccess(response, file, fileList) {
        if (response.result == '200') {
          file.createPerson = JSON.parse(
            localStorage.getItem('userInfo')
          ).realname
          let arr = response.data
          const fileData = {
            name: arr.attname,
            size: arr.attsize,
            createPerson: arr.uploader,
            attid: arr.attid,
          }
          this.tableDataFile.push(fileData)
          this.fileIds.push(arr.attid)
          this.$baseMessage(response.msg, 'success')
        } else {
          this.$baseMessage(response.msg, 'error')
        }
      },

      async handleDeleteFile(index, row, fileType = 'draft') {
        let res = await deleteReportFile({ attId: row.attid })
        if (res.msg === '成功') {
          this.$message({
            type: 'success',
            message: '删除成功!',
          })
          if (fileType == 'draft') {
            this.fileIds.splice(index, 1)
            this.tableDataFile.splice(index, 1)
          } else if (fileType == 'opinion') {
            this.opinionFileIds.splice(index, 1)
            this.opinionFile.splice(index, 1)
          } else if (fileType == 'final') {
            this.finalFileIds.splice(index, 1)
            this.finalFile.splice(index, 1)
          }
        }
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
      // 流程相关-提交
      async ymsubmit() {
        try {
          this.$refs['ruleForm'].validate(async (valid) => {
            if (valid) {
              this.btnLoading = true
              this.$refs.resubmit.ymsubmit()
            }
          })
        } catch (error) {
          this.btnLoading = false
        }
      },
      // 处理复核意见稿上传
      handleOpinionSuccess(file) {
        if (file.code == 200) {
          this.opinionFile = [...this.opinionFile, ...file.data]
          this.opinionFileIds = [
            ...this.opinionFileIds,
            ...file.data.map((item) => item.attid),
          ]
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },

      // 处理终稿上传
      handleFinalSuccess(file) {
        if (file.code == 200) {
          this.finalFile = [...this.finalFile, ...file.data]
          this.finalFileIds = [
            ...this.finalFileIds,
            ...file.data.map((item) => item.attid),
          ]
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
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
          this.fileList = [...this.fileList, ...file.data]
          this.tableDataFile = [...this.tableDataFile, ...file.data]
          this.fileIds = [
            ...this.fileIds,
            ...file.data.map((item) => item.attid),
          ]
          // this.tableData = list
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
</style>
