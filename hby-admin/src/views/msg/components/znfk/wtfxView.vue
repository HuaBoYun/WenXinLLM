<template>
  <div>
    <el-row :gutter="14">
      <el-form ref="form" label-width="120px" :model="form" :rules="rules">
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
              v-model="form.staffScopeNames"
              readonly
              placeholder="请选择知悉范围"
              :style="{ width: '80%' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.ZXPerson.showEdit(form.secrectLevelId)"
              :disabled="!form.secrectLevelId || !footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24" v-if="showMJ">
          <el-divider>基本信息</el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="一级流程" prop="oneprocess">
            <el-input
              :disabled="!footer"
              v-model="form.oneprocess"
              placeholder="请输入一级流程"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="二级流程" prop="problemtype">
            <el-input
              :disabled="!footer"
              v-model="form.problemtype"
              placeholder="请输入二级流程"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 50px">
          <el-form-item label="风险编号" prop="risknumber">
            <el-input
              v-model="form.risknumber"
              :style="{ width: '216px' }"
              placeholder="请选择风险"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              :disabled="!footer"
              @click="$refs.riskList.show()"
            >
              选择
            </el-button>
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              v-if="form.risknumberid"
              @click="handleRisknumber"
            >
              详细
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 50px">
          <el-form-item label="缺陷等级" prop="defectlevel">
            <el-select
              v-model="form.defectlevel"
              clearable
              :disabled="!footer"
              placeholder="请选择缺陷等级"
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
        <el-col :span="12" style="height: 50px">
          <el-form-item label="主责部门" prop="orgname">
            <el-input
              v-model="form.orgname"
              :style="{ width: '256px' }"
              placeholder="请选择主责部门"
              disabled
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
        <el-col :span="12" style="height: 50px">
          <el-form-item label="落实整改人" prop="realname">
            <el-input
              v-model="form.realname"
              :style="{ width: '256px' }"
              placeholder="请选择落实整改人"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              :disabled="!footer"
              @click="$refs.executor.showEdit()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="预计完成时间" prop="estfinishdate">
            <el-date-picker
              v-model="form.estfinishdate"
              placeholder="请选择预计完成时间"
              format="yyyy-MM-dd"
              :style="{ width: '100%' }"
              type="date"
              :disabled="!footer"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="缺陷类型" prop="defecttype">
            <el-select
              v-model="form.defecttype"
              placeholder="请选择缺陷类型"
              :style="{ width: '100%' }"
              :disabled="!footer"
            >
              <el-option
                v-for="item in defecttypeList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="内控评价年度" prop="testyear">
            <el-date-picker
              v-model="form.testyear"
              placeholder="请选择内控评价年度"
              format="yyyy"
              :style="{ width: '100%' }"
              type="year"
              :disabled="!footer"
              value-format="yyyy"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="缺陷概述" prop="problemmemo">
            <el-input
              v-model="form.problemmemo"
              placeholder="请输入缺陷概述"
              type="textarea"
              rows="4"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="缺陷具体描述" prop="defectmemo">
            <el-input
              v-model="form.defectmemo"
              placeholder="请输入缺陷具体描述"
              type="textarea"
              rows="4"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="定性依据" prop="quabasis">
            <el-input
              v-model="form.quabasis"
              placeholder="请输入定性依据"
              type="textarea"
              rows="4"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="整改措施" prop="measures">
            <el-input
              v-model="form.measures"
              placeholder="请输入整改措施"
              type="textarea"
              rows="4"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="整改时限" prop="deadline">
            <el-date-picker
              v-model="form.Date"
              clearable
              start-placeholder="开始时间"
              end-placeholder="结束时间"
              format="yyyy-MM-dd"
              range-separator="-"
              type="daterange"
              value-format="yyyy-MM-dd"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="整改进展情况" prop="rectification">
            <el-input
              v-model="form.rectification"
              placeholder="请输入整改进展情况"
              type="textarea"
              rows="4"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="完成状态" prop="rectificationStatus">
            <el-select
              v-model="form.rectificationStatus"
              placeholder="请选择完成状态"
              :style="{ width: '100%' }"
              :disabled="!footer"
            >
              <el-option
                v-for="item in rectificationStatusList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="反馈意见" prop="feedback">
            <el-input
              v-model="form.feedback"
              placeholder="请输入反馈意见"
              type="textarea"
              rows="4"
              disabled
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="24" >
          <el-form-item label="整改计划" prop="reformplan">
            <el-input
              v-model ="form.reformplan"
              placeholder="请输入整改计划"
              type="textarea"
              rows="4"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col> -->

        <el-col :span="24">
          <UEditor
            ref="ueditor"
            v-model="form.content"
            :height="300"
            :templates="templates"
            template="nbsj"
            :disabled="!footer"
            style="width: 100%"
          />
        </el-col>

        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
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
                  :disable="false"
                >
                  下载
                </el-button>
                <el-button
                  type="text"
                  :disabled="false"
                  @click="handlePreviewFile(row)"
                >
                  预览
                </el-button>
                <el-button v-if="footer" type="text" @click="handleDelete(row)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <div v-if="footer" style="text-align: right; margin-top: 10px">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
      <el-button @click="ymsubmit" type="primary">提交</el-button>
    </div>
    <department ref="department" @selected="handleDepartmentSelected" />
    <executor ref="executor" @projectManage="handleExecutorSelected" />
    <riskList ref="riskList" @selected="handleRiskList" />
    <RiskEdit ref="read" :fromNK="true" />
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
  import department from '@/views/internal/question/components/options/department.vue'
  import executor from '@/components/danxuanPerson.vue'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import {
    testtaskProfindSave,
    testtaskProfindDetail,
    getFjListByFind,
    delFjByTypeAndId,
    getWTFXDetail,
  } from '@/api/internal/tack'
  import CandidateUserSelect from '@/components/CandidateUserSelect'
  const { baseURL } = require('@/config')
  import store from '@/store'
  // import UEditor from '@/components/UEditor'
  import { download } from '@/api/internal/score'
  import riskList from '@/views/internal/internalTest/components/riskList.vue'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import RiskEdit from '@/views/risk/identify/creation/components/RiskEdit.vue'
  import ZXPerson from '@/components/selectPerson.vue'
  import { getSPMJ } from '@/api/setting/mjsz'
  import { hasMJ, couldMJ } from '@/utils'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    name: 'issueView',
    components: {
      department,
      executor,
      CandidateUserSelect,
      UEditor: () => import('@/components/UEditor'),
      riskList,
      Resubmit,
      RiskEdit,
      ZXPerson,
    },
    data() {
      return {
        baseApi: baseURL,
        fileList: [],
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        headers: { token: store.getters['user/token'] },
        form: {
          oneprocess: '',
          problemtype: '',
          defectlevel: '',
          mainorg: '',
          orgname: '',
          reformstaffid: '',
          realname: '',
          estfinishdate: '',
          problemmemo: '',
          defectmemo: '',
          reformplan: '',
          feedback: '',
          quabasis: '',
          content: '',
          testyear: '',
          defecttype: '',
          risknumberid: '',
          risknumber: '',
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
          measures: '',
          Date: [],
          deadlineStart: '',
          deadlineEnd: '',
          rectification: '',
          rectificationStatus: ''
        },
        rules: {
          oneprocess: [
            { required: true, message: '请输入一级流程', trigger: 'blur' },
          ],
          defectlevel: [
            { required: true, message: '请选择缺陷等级', trigger: 'blur' },
          ],
          problemtype: [
            { required: true, message: '请输入二级流程', trigger: 'blur' },
          ],
          risknumber: [
            { required: false, message: '请选择风险', trigger: 'blur' },
          ],
          orgname: [{ required: true, message: '请选择', trigger: 'blur' }],
          realname: [{ required: true, message: '请选择', trigger: 'blur' }],
        },
        field103Options: [
          {
            label: '一般',
            value: '一般',
          },
          {
            label: '重要',
            value: '重要',
          },
          {
            label: '重大',
            value: '重大',
          },
        ],
        title: '',
        dialogFormVisible: false,
        renderData: [],
        footer: true,
        //提交
        testtaskid: undefined,

        status: 0,
        jurisdictionCode: 0,
        templates: [],
        tableData: [],
        defecttypeList: [
          {
            value: '执行缺陷',
            label: '执行缺陷',
          },
          {
            value: '设计缺陷',
            label: '设计缺陷',
          },
        ],
        rectificationStatusList: [
          {
            value: 0,
            label: '未完成',
          },
          {
            value: 1,
            label: '已完成',
          },
        ],
        //提交
        ymFromId: 0,
        flowId: 0,
        fromId: 0,
        fromIdcopy: 0,
        flowtaskinfoflowid: '',
        status: 0,
        MJoption: [],
        menuId: 0,
        showMJ: false,
      }
    },
    watch: {
      'form.content'(val) {
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
            if (this.form[i[1]]) {
              s = s.replace(i[0], this.form[i[1]])
            }
          })
          this.form.content = s
        }
      },
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
      async show(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status,
        flowType
      ) {
        // 拿到类型传给getMJData获取审批的密级的下拉数据
        if (flowType) {
          this.getMJData(flowType)
        }
        this.footer = title === 'edit'
        // 流程相关
        this.fromId = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
        // const curRow = JSON.parse(JSON.stringify(row))
        // if (testtaskid) this.testtaskid = testtaskid

        // this.form = Object.assign(this.form, curRow)
        // this.form.testyear = String(curRow.testYear)

        const info = await getWTFXDetail({ findid: formId })
        this.form = Object.assign(this.form, info.data.profind)
        if (info.data.profind?.secrectLevelId) {
          localStorage.setItem(
            'SPsecrectLevelId',
            info.data.profind.secrectLevelId
          )
        }

        if (this.form.deadlineStart && this.form.deadlineEnd) {
          this.form.Date = [this.form.deadlineStart, this.form.deadlineEnd]
        }

        // this.form.testyear = String(info.data.testYear)
        const res = await getFjListByFind({ findId: formId })
        if (res && res.data && res.data.fjlist) {
          this.tableData = res.data.fjlist
        }
      },
      handleRisknumber() {
        this.$refs['read'].showEdit(
          { riskid: this.form.risknumberid },
          '',
          true
        )
      },
      async getInfo(findid) {
        const res = await testtaskProfindDetail({ findid })
        this.form = res.data
        this.$forceUpdate()
      },
      handleRiskList(node) {
        this.$set(this.form, 'risknumberid', node.riskid)
        this.$set(this.form, 'risknumber', node.risknumber)
        console.log(node)
      },
      handleDepartmentSelected(node) {
        this.form.mainorg = node.id
        this.$set(this.form, 'orgname', node.name)
        this.$forceUpdate()
      },
      handleExecutorSelected(node) {
        this.form.reformstaffid = node[0].staffid
        this.$set(this.form, 'realname', node[0].realname)
        this.$forceUpdate()
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const attids = this.tableData.map((x) => x.attid).join(',')
            if (this.form.Date && this.form.Date.length) {
              this.form.deadlineStart = this.form.Date[0]
              this.form.deadlineEnd = this.form.Date[1]
            }
            const data = await testtaskProfindSave({
              ...this.form,
              testyear: Number(this.form.testyear),
              attids,
              testtaskid: this.testtaskid,
            })
            if (data.code == 200) {
              this.$baseMessage('保存成功', 'success')
              this.$emit('fetch-data')
            } else {
              this.$baseMessage(data.msg, 'error')
            }
          }
        })
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.footer = true
        this.dialogFormVisible = false
        this.$bus.$emit('updateMsg', 0)
      },

      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        this.tableData = list
        const res = await delFjByTypeAndId({
          type: 'wtfx',
          attid: row.attid,
        })
        if (res && res.code === 200) {
          this.$message.success('成功')
        } else {
          this.$message.error(res.msg)
        }
      },
      //提交
      async ymsubmit() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            this.$refs.resubmit.ymsubmit()
          }
        })
      },
      /**
       * @description: 文件预览
       * @param {*} row 文件信息
       * @return {*}
       */ async handlePreviewFile(row) {
        const { data } = await getPrivewAttInfo({
          attId: row.attid,

          attType: 2,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url }) // iframe弹框预览形式
      },
      handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.form.staffScopeIds = ids
        this.form.staffScopeNames = names
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
