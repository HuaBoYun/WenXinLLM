<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1100px"
    @close="close"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="140px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="formDisabled"
        :label-position="'top'"
      >
        <el-col :span="12">
          <el-form-item
            label="审计报告定稿"
            prop="sjbgdgTitle"
            :rules="{
              required: true,
              message: '请选择审计报告定稿',
              trigger: 'change',
            }"
          >
            <el-input
              v-model="formData.sjbgdgTitle"
              :style="{ width: '75%' }"
              disabled
              placeholder="审计报告定稿"
            />
            <el-button
              @click="handlePlan"
              style="margin-left: 15px"
              type="primary"
              size="mini"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="在报告中的对应编号"
            prop="issueNumber"
            :rules="{
              required: true,
              message: '请输入在报告中的对应编号',
              trigger: 'blur',
            }"
          >
            <el-input
              v-model="formData.issueNumber"
              clearable
              placeholder="请输入在报告中的对应编号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="定性"
            prop="qualitative"
            :rules="{ required: true, message: '请输入定性', trigger: 'blur' }"
          >
            <el-input
              v-model="formData.qualitative"
              clearable
              placeholder="请输入定性"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="问题所属单位名称"
            prop="unitOrgId"
            :rules="{
              required: true,
              message: '请选择问题所属单位名称',
              trigger: 'change',
            }"
          >
            <el-select
              v-model="formData.unitOrgId"
              placeholder="请选择问题所属单位名称"
              :style="{ width: '100%' }"
              @change="$forceUpdate()"
            >
              <el-option
                v-for="(item, index) in unitNameOptions"
                :key="index"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="问题涉及企业的管理层级"
            prop="wtsjglcj"
            :rules="{
              required: true,
              message: '请选择问题涉及企业的管理层级',
              trigger: 'blur',
            }"
          >
            <el-input
              v-model="formData.wtsjglcj"
              clearable
              placeholder="请输入问题涉及企业的管理层级"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item
            label="关联底稿"
            prop="draft"
            :rules="{
              required: true,
              message: '请选择关联底稿',
              trigger: 'change',
            }"
          >
            <el-input
              v-model="formData.draft"
              clearable
              placeholder="请选择关联底稿"
              :style="{ width: '90%' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="handleDraft"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="money">
            <div slot="label">
              问题金额（元）
              <span class="color-red f13">
                (提示:录入除工程合同,物采,招投标审减额外的发现问题金额)
              </span>
            </div>
            <el-input
              type="number"
              v-model="formData.money"
              clearable
              placeholder="请输入问题金额（元）"
              :style="{ width: '100%' }"
              @input="inputMoney($event, 'money')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="reviewMoney">
            <div slot="label">
              审减金额（元）
              <span class="color-red f13">
                (注:只填写产生资产损失的金额,不涉及资产损失不填写)
              </span>
            </div>
            <el-input
              type="number"
              v-model="formData.reviewMoney"
              clearable
              placeholder="请输入审减金额（元）"
              :style="{ width: '100%' }"
              @input="inputMoney($event, 'reviewMoney')"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item prop="assetLoss">
            <div slot="label">
              资产损失（
              <span class="color-red">万元</span>
              ）
              <span class="color-red f13">
                (提示:仅录入工程合同,物采,招投标的审减金额)
              </span>
            </div>
            <el-input
              type="number"
              v-model="formData.assetLoss"
              clearable
              placeholder="资产损失（元）"
              :style="{ width: '100%' }"
              @input="inputMoney($event, 'assetLoss')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="风险程度" prop="riskLevel">
            <el-input
              v-model="formData.riskLevel"
              clearable
              placeholder="请输入风险程度"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item
            prop="issueDetail"
            　:rules="{ required: true, message: '请输入事实表述', trigger: 'blur' }"
          >
            <span slot="label" class="color-red">事实表述</span>
            <el-input
              v-model="formData.issueDetail"
              clearable
              placeholder="请输入事实表述"
              :style="{ width: '100%' }"
              type="textarea"
              rows="4"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item
            prop="problemQualitative"
            :rules="{
              required: true,
              message: '请输入问题定性',
              trigger: 'blur',
            }"
          >
            <span slot="label" class="color-red">问题定性</span>

            <el-input
              v-model="formData.problemQualitative"
              clearable
              placeholder="请输入问题定性"
              :style="{ width: '100%' }"
              type="textarea"
              rows="4"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            prop="qualitativeRule"
            :rules="{
              required: true,
              message: '请输入定性法规依据',
              trigger: 'blur',
            }"
          >
            <span slot="label" class="color-red">定性法规依据</span>
            <el-input
              v-model="formData.qualitativeRule"
              clearable
              placeholder="请输入定性法规依据"
              :style="{ width: '100%' }"
              type="textarea"
              rows="4"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            prop="correctPropose"
            :rules="{
              required: true,
              message: '请输入处理意见或整改建议',
              trigger: 'blur',
            }"
          >
            <span slot="label" class="color-red">处理意见或整改建议</span>
            <el-input
              v-model="formData.correctPropose"
              clearable
              placeholder="请输入处理意见或整改建议"
              :style="{ width: '100%' }"
              type="textarea"
              rows="4"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item
            label="问题发生年度"
            prop="problemYear"
            :rules="{ required: true, message: '请选择问题发生年度' }"
          >
            <div class="year-select-wrapper">
              <el-date-picker
                v-model="tempYear"
                placeholder="请选择年度"
                style="width: 120px"
                type="year"
                format="yyyy"
                value-format="yyyy"
                @change="handleYearSelect"
              />
              <div class="year-tags" v-if="problemYearList.length">
                <el-tag
                  v-for="year in problemYearList"
                  :key="year"
                  closable
                  size="small"
                  @close="handleYearRemove(year)"
                  style="margin-left: 5px"
                >
                  {{ year }}
                </el-tag>
              </div>
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="整改时限"
            prop="timeLimit"
            :rules="{ required: true, message: '请选择整改时限' }"
          >
            <el-input
              v-model="formData.timeLimit"
              clearable
              placeholder="请输入整改时限"
              :style="{ width: '100%' }"
            />
            <!-- <el-select v-model="formData.timeLimit" placeholder="请选择整改时限" :style="{ width: '100%' }" >
              <el-option label="发审计意见及决定书之日起一个月内" value="发审计意见及决定书之日起一个月内"></el-option>
            </el-select> -->
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item
            label="整改督促牵头部门或单位"
            prop="urgeDepartment"
            :rules="{ required: true, message: '请输入整改督促牵头部门或单位' }"
          >
            <el-input
              v-model="formData.urgeDepartment"
              clearable
              placeholder="请输入整改督促牵头部门或单位"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item
            label="整改分类"
            prop="rectClass"
            :rules="{ required: true, message: '请选择整改分类' }"
          >
            <el-select
              v-model="formData.rectClass"
              placeholder="请选择整改分类"
              :style="{ width: '100%' }"
            >
              <el-option label="立行立改" value="立行立改"></el-option>
              <el-option label="分阶段整改" value="分阶段整改"></el-option>
              <el-option label="持续整改" value="持续整改"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="整改人" prop="rectPerName">
            <el-input v-model="formData.rectPerName" :style="{ width: '75%' }" clearable placeholder="整改人" disabled />
            <el-button @click="openPerson('rectPerName')" style="margin-left: 10px; height: 30px" type="primary">
              选择
            </el-button>
          </el-form-item>
        </el-col> -->
        <!-- <el-col :span="12">
          <el-form-item label="整改责任人" prop="head">
            <el-input v-model="formData.head" clearable placeholder="请输入整改责任人" :style="{ width: '100%' }" />
          </el-form-item>
        </el-col> -->
        <!-- <el-col :span="12">
          <el-form-item label="创建人" prop="operator">
            <el-input v-model="formData.operator" disabled placeholder="请输入创建人" :style="{ width: '100%' }" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间" prop="addTime">
            <el-date-picker v-model="formData.addTime" placeholder="请输入创建时间" type="date" format="yyyy-MM-dd"
              value-format="yyyy-MM-dd" :style="{ width: '100%' }" disabled />
          </el-form-item>
        </el-col> -->

        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div
            style="text-align: right; margin-bottom: 5px"
            v-if="!formDisabled"
          >
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
                  v-if="!formDisabled"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <!-- <el-col :span="24" style="margin-top: 20px;" v-if="title == '详情'">
          <el-divider>审计建议</el-divider>
        </el-col>
        <el-col :span="24" v-if="title == '详情'">
          <el-table :data="proList">
            <el-table-column align="center" label="建议标题" prop="title" show-overflow-tooltip >
              <template #default="{ row }">
              <el-button type="text" @click="handleDetails(row)" :disabled="false">{{ row.title }}</el-button>
            </template>
            </el-table-column>
            <el-table-column align="center" label="建议涉及业务类型" prop="businessType" show-overflow-tooltip />
            <el-table-column align="center" label="建议层级类型" prop="hierarchyType" show-overflow-tooltip />
            <el-table-column align="center" label="建议描述" prop="details" show-overflow-tooltip />
          </el-table>
        </el-col> -->
      </el-form>
    </el-row>
    <div slot="footer" v-if="!formDisabled">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
      <el-button
        @click="handleApproval"
        type="primary"
        :disabled="!this.editId"
      >
        提交审批
      </el-button>
    </div>

    <suggestModal ref="suggestModal" @selected="handlePlanSelected" />
    <myDraftModal ref="myDraftModal" @selected="handlePlanSelecteds" />
    <wtdaPerson @projectManage="getChildlistPro" ref="manage" />
    <Score ref="edit"></Score>
    <ProcessList ref="process" @fetchData="close" />
  </el-dialog>
</template>

<script>
  import { download } from '@/oapi/audit/report'
  import suggestModal from '@/views/oilAudit/rectify/components/suggestModal'
  import wtdaPerson from '@/views/oilAudit/rectify/components/wtdaPerson.vue'
  import myDraftModal from '@/views/oilAudit/rectify/components/myDraftModal2.vue'
  import Score from '@/views/oilAudit/report/components/sjjyView.vue'
  import store from '@/store'
  import {
    editInfo,
    getDetailInfo,
    deleteFileInfo,
    getAuditOrgList,
  } from '@/oapi/yqns_sjzg/wtqd'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  const { baseURL } = require('@/config')
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    name: 'wtdaEdit',
    inheritAttrs: false,
    components: { suggestModal, wtdaPerson, myDraftModal, Score, ProcessList },
    props: [],
    data() {
      return {
        // baseApi: baseURL,
        // api: '/oiaudit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        fileList: [],
        tempYear: '', // 临时年份选择
        problemYearList: [], // 已选年份列表
        baseApi: baseURL,
        importApi: '/audit/auditPlan/mergePlanProjectManageInfoImport',
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        formData: {
          sjbgdgTitle: '',
          sjbgdgid: '',
          unitName: '',
          unitOrgId: '',
          issueNumber: '',
          rectPerName: '',
          rectPerson: '',
          issueDetail: '',
          money: '',
          reviewMoney: '',
          qualitative: '',
          problemQualitative: '',
          qualitativeRule: '',
          correctPropose: '',
          problemYear: '',
          timeLimit: '发审计意见及决定书之日起一个月内',
          rectClass: '',
          urgeDepartment: '',
          head: '',
          operator: '',
          operatorId: '',
          addTime: '',
          draft: '',
          draftIdStrs: '',
          fileIds: [],
          riskLevel: '',
          assetLoss: '',
          wtsjglcj: '',
          projectId: '',
          id: '',
        },
        formDisabled: true,
        tableData: [],
        proList: [],
        rules: {},
        dialogFormVisible: false,
        title: '新增',
        personType: '',
        unitNameOptions: [],
        editId: '',
      }
    },
    methods: {
      // 选择年份
      handleYearSelect(year) {
        if (year && !this.problemYearList.includes(year)) {
          this.problemYearList.push(year)
          this.formData.problemYear = this.problemYearList.join(',')
        }
        this.tempYear = '' // 清空临时选择
      },
      // 移除年份
      handleYearRemove(year) {
        const index = this.problemYearList.indexOf(year)
        if (index > -1) {
          this.problemYearList.splice(index, 1)
          this.formData.problemYear = this.problemYearList.join(',')
        }
      },
      // 初始化年份列表（用于反显）
      initProblemYearList() {
        if (this.formData.problemYear) {
          this.problemYearList = this.formData.problemYear
            .split(',')
            .filter((y) => y)
        } else {
          this.problemYearList = []
        }
      },
      //金额输入
      inputMoney(value, key) {
        // 移除非数字字符和小数点
        let sanitizedValue = value.replace(/[^0-9.]/g, '')
        // 如果输入的是小数点，确保只有一个小数点
        if (sanitizedValue.indexOf('.') !== sanitizedValue.lastIndexOf('.')) {
          sanitizedValue = sanitizedValue.slice(
            0,
            sanitizedValue.lastIndexOf('.')
          )
        }
        // 如果输入的是0开头且后面有其他数字，去掉开头的0
        if (
          sanitizedValue.startsWith('0') &&
          sanitizedValue.length > 1 &&
          sanitizedValue[1] !== '.'
        ) {
          sanitizedValue = sanitizedValue.slice(1)
        }
        // 如果输入的是小数点开头，前面加0
        if (sanitizedValue.startsWith('.')) {
          sanitizedValue = '0' + sanitizedValue
        }
        // 如果输入的是负数，去掉负号
        if (sanitizedValue.startsWith('-')) {
          sanitizedValue = sanitizedValue.slice(1)
        }
        // 如果输入的是空字符串或0，设置为空字符串
        if (sanitizedValue === '' || sanitizedValue === '0') {
          sanitizedValue = ''
        }
        // 更新输入框的值
        this.formData[key] = sanitizedValue
      },
      openPerson(type) {
        if (!this.formData.unitOrgId) {
          this.$baseMessage(
            '请选择审计项目！',
            'error',
            'vab-hey-message-error'
          )
          return false
        }
        this.personType = type
        this.$refs.manage.showEdit(this.formData.unitOrgId)
      },
      getChildlistPro(val) {
        this.formData.rectPerName = val[0].realname
        this.formData.rectPerson = val[0].staffid
      },
      handlePlan() {
        this.$refs['suggestModal'].showEdit()
      },
      handleDraft() {
        if (!this.formData.projectId) {
          this.$message({
            message: '请先选择审计报告定稿！',
            type: 'error',
          })
          return
        }
        this.$refs['myDraftModal'].showEdit(this.formData.projectId)
      },
      async handlePlanSelected(val) {
        this.formData.sjbgdgTitle = val[0].title
        this.formData.sjbgdgid = val[0].sjbgdgid
        this.formData.projectId = val[0].projectId
        let res = await getAuditOrgList({ sjbgdgid: val[0].sjbgdgid })
        if (res.code == 1) {
          let list = []
          res.data.map((item) => {
            list.push({
              value: item.orgid,
              label: item.orgname,
            })
          })
          this.unitNameOptions = list
          this.formData.unitOrgId = res.data[0].orgid
          this.$forceUpdate()
          this.formData.unitName = res.data[0].orgname
          this.formData.rectPerName = ''
          this.formData.rectPerson = ''
        }
      },
      handlePlanSelecteds(e) {
        this.$set(
          this.formData,
          'draft',
          e.map((item) => item.draftName).join(',')
        )
        this.$set(
          this.formData,
          'draftIdStrs',
          e.map((item) => item.id).join(',')
        )
      },
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
      async showEdit(row, disabled) {
        this.dialogFormVisible = true
        if (disabled == 'details') {
          this.formDisabled = true
          this.title = '详情'
        } else if (disabled == 'edit') {
          this.formDisabled = false
          this.title = '编辑'
        } else {
          this.formDisabled = false
          this.title = '新增'
        }

        if (!row) {
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.operator = userInfo.realname
          // this.formData.addTime = new Date()

          const year = new Date().getFullYear() //得到年份
          const month = new Date().getMonth() //得到月份
          const date = new Date().getDate() //得到日期
          this.formData.addTime = `${year}-${
            month < 10 ? '0' + month : month
          }-${date < 10 ? '0' + date : date}`
          return
        } else {
          this.editId = row.id
          const res = await getDetailInfo({ issueId: row.id })
          this.formData.sjbgdgTitle = res.data.baseData.sjbgdgTitle
          this.formData.sjbgdgid = res.data.baseData.sjbgdgid
          this.formData.unitName = res.data.baseData.unitName
          this.formData.unitOrgId = res.data.baseData.unitOrgId

          let list = []
          list.push({
            value: res.data.baseData.unitOrgId,
            label: res.data.baseData.unitName,
          })
          this.unitNameOptions = list
          this.formData.issueNumber = res.data.baseData.issueNumber
          this.formData.rectPerName = res.data.baseData.rectPerName
          this.formData.rectPerson = res.data.baseData.rectPerson
          this.formData.issueDetail = res.data.baseData.issueDetail
          this.formData.money = res.data.baseData.money
          this.formData.projectId = res.data.baseData.projectId
          this.formData.reviewMoney = res.data.baseData.reviewMoney
          this.formData.qualitative = res.data.baseData.qualitative
          this.formData.problemQualitative =
            res.data.baseData.problemQualitative
          this.formData.qualitativeRule = res.data.baseData.qualitativeRule
          this.formData.correctPropose = res.data.baseData.correctPropose
          this.formData.problemYear = res.data.baseData.problemYear
          this.initProblemYearList() // 反显年份标签
          this.formData.timeLimit = res.data.baseData.timeLimit
          this.formData.rectClass = res.data.baseData.rectClass
          this.formData.urgeDepartment = res.data.baseData.urgeDepartment
          this.formData.head = res.data.baseData.head
          this.formData.operator = res.data.baseData.operator
          this.formData.operatorId = res.data.baseData.operatorId
          this.formData.addTime = res.data.baseData.addTime
          this.formData.draft = res.data.baseData.draft
          this.formData.draftIdStrs = res.data.baseData.draftIdStrs
          this.formData.fileIds = res.data.baseData.fileIds
          this.formData.id = res.data.baseData.id
          this.tableData = res.data.fileData
          this.proList = res.data.baseData.proList
          this.formData.riskLevel = res.data.baseData.riskLevel
          this.formData.assetLoss = res.data.baseData.assetLoss
          this.formData.wtsjglcj = res.data.baseData.wtsjglcj
        }
      },
      close() {
        this.formData = {
          sjbgdgTitle: '',
          sjbgdgid: '',
          unitName: '',
          unitOrgId: '',
          issueNumber: '',
          rectPerName: '',
          rectPerson: '',
          projectId: '',
          issueDetail: '',
          money: '',
          reviewMoney: '',
          qualitative: '',
          problemQualitative: '',
          qualitativeRule: '',
          correctPropose: '',
          problemYear: '',
          timeLimit: '发审计意见及决定书之日起一个月内',
          rectClass: '',
          urgeDepartment: '',
          head: '',
          operator: '',
          operatorId: '',
          addTime: '',
          riskLevel: '',
          assetLoss: '',
          wtsjglcj: '',
          draft: '',
          draftIdStrs: '',
          fileIds: [],
          id: '',
        }
        this.problemYearList = [] // 清空年份列表
        this.tempYear = ''
        this.dialogFormVisible = false
        this.tableData = []
        this.formDisabled = true
        this.$refs['ruleForm'].resetFields()
        this.editId = ''
        this.$emit('fetchData')
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let fileIds = ''
            this.tableData.map((item) => {
              fileIds += item.attid
              fileIds += ','
            })
            fileIds = fileIds.substring(0, fileIds.length - 1)
            const data = await editInfo({
              ...this.formData,
              fileIds,
            })
            if (data && (data.code == 200 || data.code == 1)) {
              this.editId = data.data.id
              this.formData.id = data.data.id
              this.$message({
                message: '提交成功！',
                type: 'success',
              })
               this.$emit('fetchData')
            } else {
              this.$message({
                message: '提交失败',
                type: 'error',
              })
            }
          } else {
            return false
          }
        })
      },
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
      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        this.tableData = list
        await deleteFileInfo({ fileIds: row.attid, issueId: this.formData.id })
      },
      // handlePreview(file) {},
      // handleSuccess(file) {
      //   if (file.result == '200') {
      //     let list = this.tableData
      //     list.push(file.data)
      //     this.tableData = list
      //     this.$baseMessage(file.msg, 'success')
      //   } else {
      //     this.$baseMessage(file.msg, 'error')
      //   }
      // },
      handleDetails(row) {
        this.$refs['edit'].showEdit(row, 'details')
      },
      handleApproval() {
        //提交审批
        this.$refs['process'].save(193, this.editId)
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
        // 调用自定义上传函数
        new Promise((resolve, reject) => {
          customUpload({
            baseApi: this.baseApi,
            api: this.api,
            key: window.key,
            iv: window.iv,
            headers: this.headers,
            fileList: fileList, // 使用 fileList 而不是 file
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
<style scoped>
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
  .color-red {
    color: red;
  }
  .f13 {
    font-size: 13px;
  }
  .year-select-wrapper {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    gap: 5px;
  }
  .year-tags {
    display: flex;
    flex-wrap: wrap;
    gap: 5px;
  }
</style>
