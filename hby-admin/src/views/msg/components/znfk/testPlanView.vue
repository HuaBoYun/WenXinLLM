<template>
  <div>
    <el-row :gutter="14">
      <el-form
        ref="form"
        label-width="120px"
        :model="form"
        :rules="rules"
        :disabled="disabled"
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
              v-model="form.secrectLevelId"
              clearable
              placeholder="密级"
              style="width: 100%"
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
              v-model="form.staffScopeNames"
              readonly
              placeholder="请选择知悉范围"
              :style="{ width: '80%' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.ZXPerson.showEdit(form.secrectLevelId)"
              :disabled="!form.secrectLevelId || disabled"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24" v-if="showMJ">
          <el-divider>基本信息</el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划编号" prop="plannumber">
            <el-input v-model.trim="form.plannumber" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划名称" prop="planname">
            <el-input
              v-model.trim="form.planname"
              placeholder="请输入计划名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 47px">
          <el-form-item label="计划年度" prop="planyear">
            <el-date-picker
              v-model="form.planyear"
              format="yyyy"
              placeholder="请选择计划年度"
              :style="{ width: '100%' }"
              type="year"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="测试类型" prop="testtype">
            <el-select
              v-model="form.testtype"
              clearable
              placeholder="请选择测试类型"
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="item in field103Options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12" style="height: 47px">
          <el-form-item label="计划时间" prop="time">
            <el-date-picker
              v-model="form.time"
              end-placeholder="结束日期"
              format="yyyy-MM-dd"
              range-separator="-"
              start-placeholder="开始日期"
              :style="{ width: '100%' }"
              type="daterange"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="开展费用(元)" prop="planfee">
            <el-input
              v-model.trim="form.planfee"
              placeholder="请输入开展费用"
              type="number"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 47px">
          <el-form-item label="负责人" prop="staffid">
            <el-input
              v-model.trim="form.planleader"
              :style="{ width: '256px' }"
              placeholder="请选择负责人"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.executor.showEdit()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="投入人力" prop="numberofpeople">
            <el-input
              v-model.trim="form.numberofpeople"
              placeholder="请输入投入人力"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被测试公司" prop="testedorgs">
            <el-input
              v-model.trim="form.testedorgs"
              placeholder="请选择被测试公司"
              :style="{ width: '256px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.companyTree.showEdit(true)"
            >
              多选
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="测试模板" prop="testtemid">
            <el-input
              v-model.trim="form.templename"
              :style="{ width: '256px' }"
              placeholder="请选择测试模板"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.template.show(form.secrectLevelId)"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="说明情况" prop="situationDes">
            <el-input
              v-model.trim="form.situationDes"
              type="textarea"
              :rows="6"
              placeholder="请输入说明情况"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>模板明细</el-divider>
        </el-col>
        <el-col :span="24" v-if="!disabled">
          <div style="text-align: right; margin-bottom: 10px">
            <el-button
              type="success"
              @click="handleSelectExecutor"
              :disabled="selectedTemplateRows.length === 0"
            >
              选择下发人员
            </el-button>
          </div>
        </el-col>
        <el-table
          :data="testtemData"
          @selection-change="handleTemplateSelectionChange"
          ref="templateTable"
        >
          <el-table-column
            type="selection"
            width="55"
            align="center"
            v-if="!disabled"
          />
          <el-table-column align="center" label="编号" prop="typecode" />
          <el-table-column align="center" label="模板类型" prop="typename" />
          <el-table-column align="center" label="下发人员" prop="realname" />
        </el-table>
        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="!disabled">
            <!-- <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-success="handleSuccess"
              :file-list="tableData"
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
                <el-button
                  type="text"
                  @click="handleDelete(row)"
                  v-if="!disabled"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <div style="text-align: right; margdivin-top: 10px" v-if="!disabled">
      <el-button type="primary" @click="save">确 定</el-button>
      <el-button @click="ymsubmit" type="primary" :disabled="btnLoading">
        提交
      </el-button>
    </div>

    <test-template ref="template" @selected="handleTemplateSelected" />
    <Department
      ref="companyTree"
      @submit="handleChooseCompany"
      :multiple="true"
    />
    <!-- <select-company
      ref="companyTree"
      @handleChooseCompany="handleChooseCompany"
      :multiple="true"
    /> -->
    <!-- <CompanySelectUserByTree
      ref="executor"
      @selected="handleExecutorSelected"
    /> -->
    <executor-options
      ref="executor"
      @projectManage="handleExecutorSelection"
      :secrectLevelId="form.secrectLevelId"
    />
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
  </div>
</template>

<script>
  import {
    groupFindAutoNumber,
    groupSave,
    groupModifySave,
    groupDetail,
    delGroupTestPlanAtt,
    saveOrUpdate,
  } from '@/api/internal/plan'
  import { typeList } from '@/api/internal/testTemplate'

  import { getDefaultRenderData } from '@/api/internal/project'
  import TestTemplate from '@/views/internal/internalTest/components/options/TestTemplate.vue'
  import CompanyTreeModal from '@/components/CompanyTreeModal'
  import CompanySelectUserByTree from '@/components/CompanySelectUserByTree'
  import CustormForm from '@/components/customForm/index.vue'
  import ZXPerson from '@/components/selectPerson.vue'
  import Department from '@/components/departmentSelect.vue'

  import { getSPMJ } from '@/api/setting/mjsz'
  import { hasMJ, couldMJ } from '@/utils'
  const { baseURL } = require('@/config')
  import { download } from '@/oapi/audit/report'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import store from '@/store'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import selectCompany from '@/views/internal/internalTest/components/ChooseCompanys.vue'
  import ExecutorOptions from '@/components/danxuanPerson.vue'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  const token = store.getters['user/token']
  export default {
    name: 'PlanView',
    components: {
      TestTemplate,
      CompanySelectUserByTree,
      CustormForm,
      ZXPerson,
      Resubmit,
      selectCompany,
      ExecutorOptions,
      Department,
    },
    data() {
      const validator = (_rule, value, callback) => {
        if (value !== '' && isNaN(value)) {
          callback(new Error('请输入数字值'))
        } else {
          callback()
        }
      }
      return {
        baseApi: baseURL,
        // api: '/nkhg/nbkz/pjbg/addupload',
        // headers: {
        //   token: store.getters['user/token'],
        // },
        headers: { token: token },
        fileList: [],
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        tableData: [],
        selectedTemplateRows: [], // 选中的模板明细行
        executorSelectionMode: null, // 人员选择模式：'template' 或 null
        form: {
          plannumber: '',
          planname: '',
          planyear: '',
          testtype: '',
          situationDes: '',
          testedorgs: '',
          testedorgIds: '',
          time: '',
          planfee: '',
          staffid: '',
          planleader: '',
          testtemid: '',
          numberofpeople: '',
          assessname: '',
          assessid: '',
          assid: '',
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
        },
        rules: {
          plannumber: [
            { required: true, trigger: 'blur', message: '请输入计划编号' },
          ],
          planname: [
            { required: true, trigger: 'blur', message: '请输入计划名称' },
          ],
          assessname: [
            { required: true, trigger: 'blur', message: '评价计划编号' },
          ],
          planyear: [
            { required: true, trigger: 'blur', message: '请选择计划年度' },
          ],
          staffid: [
            { required: true, trigger: 'blur', message: '请选择负责人' },
          ],

          testtemid: [
            { required: true, trigger: 'blur', message: '请选择测试模板' },
          ],
          testtemid: [
            { required: true, trigger: 'blur', message: '请选择测试模板' },
          ],
          testedorgs: [
            { required: true, trigger: 'blur', message: '请选择被测试部门' },
          ],
          planfee: [
            {
              validator,
              trigger: 'blur',
            },
          ],
        },
        field103Options: [
          {
            label: '穿行测试',
            value: '穿行测试',
          },
          {
            label: '控制测试',
            value: '控制测试',
          },
        ],
        title: '',
        dialogFormVisible: false,
        renderData: [],
        disabled: true,
        MJoption: [],
        menuId: 0,
        showMJ: false,
        btnLoading: false,
        //提交
        ymFromId: 0,
        flowId: 0,
        fromId: 0,
        fromIdcopy: 0,
        flowtaskinfoflowid: '',
        status: 0,
        testtemData: [],
      }
    },
    computed: {
      getFormLevel() {
        if (!this.form.secrectLevelId) return ''
        const level = this.MJoption.find(
          (item) => item.levelId == this.form.secrectLevelId
        )
        return level ? level.levelName : ''
      },
    },
    methods: {
      changeMJ(selectedValue) {
        const selectedItem = this.MJoption.find(
          (item) => item.levelId === selectedValue
        )
        if (selectedItem) {
          const label = selectedItem.levelName
          if (label == '非密' || label == '公开') {
            this.form.staffScopeNames = '全部人员'
            this.form.staffScopeIds = ''
          } else {
            this.form.staffScopeIds = ''
            this.form.staffScopeNames = ''
            this.form.staffid = ''
            this.form.planleader = ''
            this.form.testtemid = ''
            this.form.templename = ''
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
      showEdit(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status,
        nextNodeName,
        flowType
      ) {
        if (flowType) {
          this.getMJData(flowType)
        }
        this.btnLoading = false
        this.fromId = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
        this.disabled = title === 'detail'
        this.getInfo(formId)
        this.title = '集团测试计划'
        this.dialogFormVisible = true
      },
      async getInfo(row) {
        const { data, code, msg } = await groupDetail({
          selectProjectid: Number(row),
        })
        if (code == 1) {
          const {
            id,
            planleader,
            planmadedep,
            orgid,
            numberofpeople,
            starttime,
            endtime,
            testtemple,
            planfee,
            ...other
          } = data.test

          this.form = {
            ...other,
            id,
            time:
              starttime && endtime
                ? [starttime.slice(0, 10), endtime.slice(0, 10)]
                : [],
            planleader: planleader,
            oid2: orgid,
            numberofpeople: numberofpeople,
            planfee: planfee,
            templename: testtemple.templename,
            testtemid: testtemple.testtemid,
          }
          this.tableData = data.fjList
          this.testtemData = data.test?.detailsList
          delete this.form.creatid
          delete this.form.linkdeptid
          delete this.form.createtime
          delete this.form.starttime
          delete this.form.endtime
          // await this.getTestType(testtemple?.testtemid)
          if (data.test?.secrectLevelId) {
            localStorage.setItem('SPsecrectLevelId', data.test.secrectLevelId)
          }
        }
      },
      close() {
        this.tableData = []
        this.testtemData = [] // 清空模板明细数据
        this.selectedTemplateRows = [] // 清空选中的模板明细行
        this.executorSelectionMode = null // 重置人员选择模式
        this.form = {
          plannumber: '',
          planname: '',
          planyear: '',
          testtype: '',
          testedorgs: '',
          time: '',
          planfee: '',
          staffid: '',
          planleader: '',
          testtemid: '',
          numberofpeople: '',
          assessname: '',
          assessid: '',
          assid: '',
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
        }
        this.dialogFormVisible = false
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { time, planyear, ...other } = this.form
            let attIds = ''
            this.tableData.map((item) => {
              attIds += item.attid
              attIds += ','
            })
            attIds = attIds.substring(0, attIds.length - 1)

            // 构建detailsList数据结构
            const detailsList = this.testtemData.map((item) => ({
              staffid: item.staffid || '',
              testtempletaid: item.testtempletaid || item.id || '',
              typeid: item.typeid || item.typecode || '',
              groupid: item.groupid || '',
            }))

            const { msg, code, data } = await saveOrUpdate({
              ...other,
              planyear:
                planyear instanceof Date ? planyear.getFullYear() : planyear,
              starttime: time ? time[0] : '',
              endtime: time ? time[1] : '',
              attids: attIds,
              detailsList: detailsList,
            })
            if (code == 1) {
              this.$baseMessage(
                '保存成功',
                'success',
                'vab-hey-message-success'
              )
              this.$emit('fetch-data')
            }
          }
        })
      },
      async getNumber() {
        const { msg, code, data } = await groupFindAutoNumber({
          column: 'PLANNUMBER',
          noId: '327',
          orgCol: 'ORGID',
          tblName: 'TBL_GROUP_TESTPLAN',
        })
        if (code == 1) {
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.form.plannumber = data
        } else {
          this.$baseMessage(msg, 'error')
        }
      },

      // 处理模板明细多选
      handleTemplateSelectionChange(selection) {
        this.selectedTemplateRows = selection
      },
      // 选择下发人员
      handleSelectExecutor() {
        if (this.selectedTemplateRows.length === 0) {
          this.$message.warning('请先选择模板明细')
          return
        }
        this.executorSelectionMode = 'template'
        this.$refs.executor.showEdit()
      },
      handleExecutorSelection(val) {
        if (this.executorSelectionMode === 'template') {
          // 为模板明细分配下发人员
          this.handleTemplateExecutorSelected(val)
        } else {
          // 为计划负责人分配
          this.handleExecutor(val)
        }
      },

      handleExecutor(val) {
        this.$set(this.form, 'planleader', val[0].realname)
        this.$set(this.form, 'staffid', val[0].staffid)
      },
      // 处理模板明细下发人员选择
      handleTemplateExecutorSelected(val) {
        if (val && val.length > 0) {
          // 为选中的模板明细行分配下发人员，支持多对多分配
          this.selectedTemplateRows.forEach((row, index) => {
            row.staffid = val[0].staffid
            row.realname = val[0].realname
          })
          // 更新表格数据
          this.$forceUpdate()
          // 清空表格选中状态
          this.$refs.templateTable.clearSelection()
          this.selectedTemplateRows = []
          this.$message.success('下发人员分配成功')
        }
        this.executorSelectionMode = null
      },
      handleExecutorSelected(node) {
        const { planleader, staffid, ...other } = this.form
        let form = {
          ...other,
          staffid: node.staffid,
          planleader: node.realname,
        }
        this.form = form
      },
      async handleTemplateSelected(node) {
        const { testtemid, templename, ...other } = this.form
        let form = {
          ...other,
          templename: node.templename,
          testtemid: node.testtemid,
        }
        this.form = form
        await this.getTestType(node.testtemid)
      },
      async getTestType(id) {
        const { code, data } = await typeList({ testtempletaid: id })
        this.testtemData = data
      },
      handleChooseCompany(node) {
        this.$set(
          this.form,
          'testedorgs',
          node.map((item) => item.label).join(',')
        )
        this.$set(
          this.form,
          'testedorgIds',
          node.map((item) => item.id).join(',')
        )
      },

      handlePlanSelected(val) {
        if (val && val[0]) {
          this.$set(this.form, 'assessname', val[0].assessname)
          this.$set(this.form, 'assessid', val[0].assessid)
          this.$set(this.form, 'assid', val[0].assid)
        }
      },
      handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.form.staffScopeIds = ids
        this.form.staffScopeNames = names
      },

      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      async handleDelete(row) {
        let res = await delGroupTestPlanAtt({ attid: row.attid })
        if (res.code == 200) {
          this.$message({
            type: 'success',
            message: '删除成功!',
          })
          let list = this.tableData
          list = list.filter((item) => item.attid != row.attid)
          this.tableData = list
        }
      },
      /**
       * @description: 下载文件
       * @param {*} row
       * @return {*}
       */
      // async handleDown(row) {
      //   const data = await download({ attId: row.attid })
      //   let filename = row.attname
      //   let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
      //   let url = window.URL.createObjectURL(blob, {
      //     type: 'application/vnd.ms-excel',
      //   })
      //   const link = document.createElement('a')
      //   link.style.display = 'none'
      //   link.href = url
      //   link.setAttribute('download', filename)
      //   document.documentElement.appendChild(link)
      //   link.click()
      //   document.documentElement.removeChild(link)
      // },
      /**
       * @description: 文件预览
       * @param {*} row 文件信息
       * @return {*}
       */
      // async handlePreviewFile(row) {
      //   const { data } = await getPrivewAttInfo({
      //     attId: row.attid,
      //     attType: 2,
      //   })

      //   const url =
      //     data.previewurl +
      //     '?url=' +
      //     encodeURIComponent(Base64.encode(data.ftpUrl))
      //   this.$iFrameDialog({ iframeUrl: url })
      // },

      // handleSuccess(file) {
      //   if (file.code == '200') {
      //     let list = this.tableData
      //     list.push(file.data.Attachment)
      //     this.tableData = list
      //     this.$baseMessage(file.msg, 'success')
      //   } else {
      //     this.$baseMessage(file.msg, 'error')
      //   }
      // },
      //提交
      async ymsubmit() {
        try {
          this.$refs['form'].validate(async (valid) => {
            if (valid) {
              this.btnLoading = true
              this.$refs.resubmit.ymsubmit()
            }
          })
        } catch (error) {
          this.btnLoading = false
        }
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
          // this.fileList = [...this.fileList, ...file.data]
          this.tableData = [...this.tableData, ...file.data]
          // this.tableData = list
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
