<template>
  <div>
    <el-row :gutter="4">
      <el-form
        ref="ruleForm"
        label-width="140px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="formDisabled"
        style="display: flex; flex-wrap: wrap"
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
              disabled
              v-model="formData.secrectLevelId"
              clearable
              placeholder="密级"
              style="width: 100%"
              :disabled="formDisabled"
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
              disabled
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24" v-if="showMJ">
          <el-divider></el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="责任部门" prop="unitDeptidName">
            <el-input
              v-model="formData.unitDeptidName"
              clearable
              placeholder="请选择责任部门"
              :style="{ width: '80%' }"
              disabled
            />
            <el-button
              type="primary"
              @click="$refs['department1'].show(false, [])"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="配合单位或部门" prop="cooperateOrgName">
            <el-input
              v-model="formData.cooperateOrgName"
              clearable
              placeholder="请输入配合单位或部门"
              disabled
            />
            <!-- <el-button type="primary" disabled>选择</el-button> -->
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="公司责任领导" prop="leadershipName">
            <el-input
              v-model="formData.leadershipName"
              clearable
              placeholder="请输入公司责任领导"
              disabled
            />
            <!-- <el-button type="primary" disabled>选择</el-button> -->
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="关联风险点" prop="risknumber">
            <el-input
              v-model="formData.risknumber"
              :style="{ width: '100%' }"
              readonly
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="一级风险" prop="oneRisk">
            <el-input
              v-model="formData.oneRisk"
              clearable
              placeholder="请输入一级风险"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="二级风险" prop="twoRisk">
            <el-input
              v-model="formData.twoRisk"
              clearable
              placeholder="请输入二级风险"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="三级风险" prop="threeRisk">
            <el-input
              v-model="formData.threeRisk"
              clearable
              placeholder="请输入三级风险"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="四级风险" prop="levelFourRisk">
            <el-input
              v-model="formData.levelFourRisk"
              clearable
              placeholder="请输入四级风险"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="风险描述" prop="detailRisk">
            <el-input
              v-model="formData.detailRisk"
              clearable
              placeholder="请输入风险描述"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item
            label="风险源分析（导致风险发生的潜在因素）"
            prop="analysisRisk"
          >
            <el-input
              v-model="formData.analysisRisk"
              clearable
              placeholder="请输入风险源分析（导致风险发生的潜在因素）"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="现有应对措施" prop="analysisSol">
            <el-input
              v-model="formData.analysisSol"
              clearable
              placeholder="请输入现有应对措施"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="相关制度和规程索引" prop="analysisRel">
            <el-input
              v-model="formData.analysisRel"
              clearable
              placeholder="请输入相关制度和规程索引"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="典型风险事件描述" prop="analysisEve">
            <el-input
              v-model="formData.analysisEve"
              clearable
              placeholder="请输入典型风险事件描述"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="风险发生可能性评价标准" prop="assessStan">
            <el-input
              v-model="formData.assessStan"
              clearable
              placeholder="请输入风险发生可能性评价标准"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="对应分值" prop="assessScoreOne">
            <el-input
              v-model="formData.assessScoreOne"
              clearable
              placeholder="请输入对应分值"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="风险影响程度评价标准" prop="assessInf">
            <el-input
              v-model="formData.assessInf"
              clearable
              placeholder="请输入风险影响程度评价标准"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="对应分值" prop="assessScoreTwo">
            <el-input
              v-model="formData.assessScoreTwo"
              clearable
              placeholder="请输入对应分值"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="风险评分" prop="assessSco">
            <el-input
              v-model="formData.assessSco"
              clearable
              placeholder="请输入风险评分"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="风险等级" prop="assessGrade">
            <el-select
              v-model="formData.assessGrade"
              placeholder="请选择风险等级"
              :style="{ width: '100%' }"
            >
              <el-option label="极低" value="1" />
              <el-option label="低" value="2" />
              <el-option label="中" value="3" />
              <el-option label="高" value="4" />
              <el-option label="极高" value="5" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>一体化管控措施</el-divider>
        </el-col>
        <el-col :span="24" style="margin-bottom: 20px">
          <el-table :data="controls">
            <el-table-column
              align="center"
              label="一体化管控措施编号"
              prop="controlnumber"
              show-overflow-tooltip
            >
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="showControlDetail(row)"
                  :disabled="false"
                >
                  {{ row.controlnumber }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="一体化控制目标"
              prop="controldes"
              show-overflow-tooltip
            />
            <el-table-column align="center" label="控制措施" prop="conkzcs">
              <template #default="{ row }">
                <div
                  v-html="row.conkzcs ? row.conkzcs.replace(/\n/g, '<br>') : ''"
                ></div>
              </template>
            </el-table-column>
            <el-table-column align="center" label="操作" v-if="!formDisabled">
              <template #default="{ row }">
                <el-button type="text" @click="handleEditControl(row)">
                  修改
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <!-- <el-col :span="24">
          <el-form-item label="本月风险管控措施及实施情况" prop="monthMea">
            <el-input
              v-model="formData.monthMea"
              clearable
              placeholder="请输入本月风险管控措施及实施情况"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="管控措施是否逾期" prop="isoverdue">
            <el-select
              v-model="formData.isoverdue"
              placeholder="请选择管控措施是否预期"
              :style="{ width: '100%' }"
            >
              <el-option label="是" value="是" />
              <el-option label="否" value="否" />
            </el-select>
          </el-form-item>
        </el-col>
        -->

        <el-col :span="24">
          <el-form-item label="风险变化趋势" prop="riskChange">
            <el-select
              v-model="formData.riskChange"
              placeholder="风险变化趋势"
              :style="{ width: '100%' }"
            >
              <el-option label="升高" value="1" />
              <el-option label="持平" value="2" />
              <el-option label="下降" value="3" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="上升/下降原因" prop="reason">
            <el-input
              v-model="formData.reason"
              clearable
              placeholder="请输入管控措施是否预期"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="24">
          <el-form-item label="下月风险管控措施" prop="nextMonthMea">
            <el-input
              v-model="formData.nextMonthMea"
              clearable
              placeholder="请输入下月风险管控措施"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
            />
          </el-form-item>
        </el-col> -->
        <el-col :span="12">
          <el-form-item label="是否新增风险" prop="isNewRisk">
            <el-select
              v-model="formData.isNewRisk"
              placeholder="请选择是否新增风险"
              :style="{ width: '100%' }"
            >
              <el-option label="新增风险" :value="1" />
              <el-option label="已有风险" :value="2" />
              <el-option label="关闭风险" :value="3" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="上报月份" prop="month">
            <el-select
              v-model="formData.month"
              placeholder="请选择上报月份"
              :style="{ width: '100%' }"
              disabled
            >
              <el-option label="一月" value="1" />
              <el-option label="二月" value="2" />
              <el-option label="三月" value="3" />
              <el-option label="四月" value="4" />
              <el-option label="五月" value="5" />
              <el-option label="六月" value="6" />
              <el-option label="七月" value="7" />
              <el-option label="八月" value="8" />
              <el-option label="九月" value="9" />
              <el-option label="十月" value="10" />
              <el-option label="十一月" value="11" />
              <el-option label="十二月" value="12" />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-divider>附件</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-top: 5px" v-if="!formDisabled">
            <!-- <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-success="handleSuccess"
              :data="{
                moduleType: 'YDPG',
                moduleId: this.formData.id,
              }"
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
              <el-button
                type="success"
                :disabled="showMJ && !formData.secrectLevelId"
              >
                上传
              </el-button>
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
      </el-form>
    </el-row>
    <div slot="footer" style="text-align: right" v-if="!formDisabled">
      <el-button @click="add" type="primary" v-loading="loading">
        确定
      </el-button>
      <el-button @click="ymsubmit" type="primary">提交</el-button>
    </div>
    <CompanyTreeModel1 ref="department1" @selected="handleSelectDepartment1" />
    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
    <controlMeasures ref="controlMeasures" @fetch-data="fetchData" />
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </div>
</template>

<script>
  import {
    evaluationInsertOrUpdate,
    monthlyEvaluationDetails,
    getMonthlyEvaluationAttInfo,
  } from '@/api/risk/riskfill'
  import store from '@/store'
  import { formatDay } from '@/utils/index'
  const { baseURL } = require('@/config')
  import CompanyTreeModel1 from '@/components/CompanyTreeModel'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import { riskAnalysisDetail } from '@/api/risk'
  import controlMeasures from '@/views/risk/treatment/controlMeasures.vue'
  import { downFieldById, getGroupPlanCode } from '@/api/risk/riskEvents'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import ZXPerson from '@/components/selectPerson.vue'
  import { getSPMJ } from '@/api/setting/mjsz'
  import { couldMJ } from '@/utils'

  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    name: 'xqjybEdit',
    inheritAttrs: false,
    components: { CompanyTreeModel1, Resubmit, controlMeasures, ZXPerson },
    data() {
      return {
        loading: false,
        formData: {
          isoverdue: '',
          reason: '',
          id: '',
          unitDeptid: '',
          unitDeptidName: '',
          oneRisk: '',
          twoRisk: '',
          threeRisk: '',
          detailRisk: '',
          analysisRisk: '',
          analysisSol: '',
          analysisRel: '',
          analysisEve: '',
          assessStan: '',
          assessScoreOne: '',
          assessInf: '',
          assessScoreTwo: '',
          assessSco: '',
          assessGrade: '',
          monthMea: '',
          riskChange: '',
          nextMonthMea: '',
          isNewRisk: '',
          month: new Date().getMonth() + 1 + '',
          risknumber: '',
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
        },
        controls: [],
        formDisabled: false,
        rules: {
          unitDeptidName: [
            { required: true, message: '请选择责任部门', trigger: 'blur' },
          ],
          oneRisk: [
            { required: true, message: '请输入一级风险', trigger: 'blur' },
          ],
          twoRisk: [
            { required: true, message: '请输入二级风险', trigger: 'blur' },
          ],
          threeRisk: [
            { required: true, message: '请输入三级风险', trigger: 'blur' },
          ],
          detailRisk: [
            { required: true, message: '请输入风险描述', trigger: 'blur' },
          ],
          analysisRisk: [
            {
              required: true,
              message: '请输入风险源分析（导致风险发生的潜在因素）',
              trigger: 'blur',
            },
          ],
          analysisSol: [
            { required: true, message: '请输入现有应对措施', trigger: 'blur' },
          ],
          analysisRel: [
            {
              required: true,
              message: '请输入相关制度和规程索引',
              trigger: 'blur',
            },
          ],
          analysisEve: [
            {
              required: true,
              message: '请输入典型风险事件描述',
              trigger: 'blur',
            },
          ],
          assessStan: [
            {
              required: true,
              message: '请输入风险发生可能性评价标准',
              trigger: 'blur',
            },
          ],
          assessScoreOne: [
            { required: true, message: '请输入对应分值', trigger: 'blur' },
          ],
          assessInf: [
            {
              required: true,
              message: '请输入风险影响程度评价标准',
              trigger: 'blur',
            },
          ],
          assessScoreTwo: [
            { required: true, message: '请输入对应分值', trigger: 'blur' },
          ],
          assessSco: [
            { required: true, message: '请输入风险评分', trigger: 'blur' },
          ],
          assessGrade: [
            { required: true, message: '请输入风险等级', trigger: 'blur' },
          ],
          monthMea: [
            {
              required: true,
              message: '请输入本月风险管控措施及实施情况',
              trigger: 'blur',
            },
          ],
          riskChange: [
            { required: true, message: '请输入风险变化趋势', trigger: 'blur' },
          ],
          nextMonthMea: [
            {
              required: true,
              message: '请输入下月风险管控措施',
              trigger: 'blur',
            },
          ],
          isNewRisk: [
            { required: true, message: '请选择是否新增风险', trigger: 'blur' },
          ],
          month: [
            { required: true, message: '请选择上报月份', trigger: 'blur' },
          ],
          reason: [
            { required: true, message: '请输入上升/下降原因', trigger: 'blur' },
          ],
          isoverdue: [{ required: true, message: '请选择', trigger: 'blur' }],
        },
        baseApi: baseURL,
        // api: '/riskcontrol/attachment/uploadFileAttInfo',
        // api: '/oiaudit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        fileList: [],
        importApi: '/audit/auditPlan/mergePlanProjectManageInfoImport',
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        removeIds: [],
        tableDataFile: [],
        fileIdList: [],
        uploadLoading: false,
        fromId: '',
        flowtaskinfoflowid: '',
        ymFromId: '',
        status: '',
        fromIdcopy: '',
        footer: true,
        riskcopingid: '',
        riskid: '',
        MJoption: [],
        showMJ: false,
        menuId: '',
      }
    },
    mounted() {
      // if (this.curRow && this.curRow.id) {
      //   this.fetchData()
      // }
      // if (this.$route.query.risknumber) {
      //   this.formData.risknumber = this.$route.query.risknumber
      //   this.formData.riskid = this.$route.query.riskid
      // }
    },
    methods: {
      async getMJData(flowType) {
        this.showMJ = couldMJ()
        if (this.showMJ) {
          // 请求密级下拉数据
          const res2 = await getSPMJ({ flowType: flowType })
          this.MJoption = res2.data
        }
      },
      handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.formData.staffScopeIds = ids
        this.formData.staffScopeNames = names
      },
      changeMJ(selectedValue) {
        // 切换密级时清空知悉范围
        this.formData.staffScopeIds = ''
        this.formData.staffScopeNames = ''
        // 切换密级时清空附件列表
        this.tableDataFile = []
        this.fileIdList = []
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
      async showEdit(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status,
        nextNodeName,
        flowType
      ) {
        console.log('🚀 ~ showEdit ~ formId:', formId)
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详情'
          this.formDisabled = true
        } else if (title == 'add') {
          this.title = '新增'
        }
        if (flowType) {
          this.getMJData(flowType)
        }
        if (formId) {
          this.riskid = formId
          await this.fetchData()
        }
        this.fromId = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
      },
      async fetchData() {
        const res = await monthlyEvaluationDetails({ id: this.riskid })
        const fileRes = await getMonthlyEvaluationAttInfo({
          id: this.riskid,
        })
        this.tableDataFile = fileRes.data || []
        console.log('🚀 ~ fetchData ~  this.tableDataFile:', this.tableDataFile)
        if (res && res.data && res.data.data && res.data.data.pageInfo) {
          const formData = res.data.data.pageInfo
          Object.assign(this.formData, formData)
          this.getData(formData.riskid)
        }
      },
      async getData(id) {
        const { data, code } = await riskAnalysisDetail({
          riskid: id,
        })
        if (code == 1) {
          let params = {
            leadershipName: data.risk?.leadershipName,
            leadership: data.risk?.leadership,
            cooperateOrgName: data.risk?.cooperateOrgName,
            cooperateOrg: data.risk?.cooperateOrg,
            riskHopeValue: data.copings?.riskhopevalue,
            risknumber: data.risk.risknumber || undefined,
            levelFourRisk: data.risk.levelFourRisk,
          }
          this.riskcopingid = data.copings?.riskcopingid

          this.controls = data.controls || []
          this.formData = { ...this.formData, ...params }
        }
      },
      handleSelectDepartment1(checked) {
        console.log('checked', checked.id)
        console.log('checked', checked.name)
        this.formData.unitDeptid = checked.id
        this.formData.unitDeptidName = checked.name
      },
      close() {
        this.$refs['ruleForm'].resetFields()
        this.formData = {}
        this.$bus.$emit('updateMsg', 0)
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            // 添加检查确保 tableDataFile 存在
            const attIds =
              this.tableDataFile && this.tableDataFile.length > 0
                ? this.tableDataFile.map((x) => x.attid).join(',')
                : ''
            let params = {
              ...this.formData,
              implementId: this.formId,
            }
            // 调试日志：检查密级信息
            console.log('保存参数 params:', params)
            console.log('密级ID:', params.secrectLevelId)
            console.log(
              '知悉范围:',
              params.staffScopeNames,
              params.staffScopeIds
            )

            const res = await evaluationInsertOrUpdate(params, {
              attIds: attIds,
            })
            this.loading = false
            if (res && res.code == 1) {
              this.$message.success('保存成功！')
              this.$emit('fetchData')
            } else {
              this.$message.error(res.msg || '保存失败！')
            }
          }
        })
      },
      handleObject() {
        this.$refs['audiTree'].showEdit()
      },
      getDepartmentInfo(val) {
        this.formData.organizationId = val.id
        this.formData.organizationName = val.name
      },
      handleObject() {
        this.$refs['audiTree'].showEdit()
      },
      getDepartmentInfo(val) {
        this.formData.organizationId = val.id
        this.formData.organizationName = val.name
      },
      /**
       * @description: 下载
       * @return {*}
       */
      // async handleDownload(row) {
      //   console.log('downlaod', row)
      //   const res = await downFieldById({ id: row.attid })
      //   console.log(res)
      //   if (!res) return
      //   let filename = row.attname
      //   let blob = new Blob([res]) //res即为blob数据，请注意自己的数据形式
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
       * @description: 删除附件
       * @return {*}
       */
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, () => {
          const deleteId = row.attid
          this.removeIds.push(deleteId)
          //删除formData要返回给后端的id
          this.fileIdList = this.fileIdList.filter((item) => {
            return item != deleteId
          })
          //删除tableDataFile，假删除
          this.tableDataFile = this.tableDataFile.filter((item) => {
            return item != row
          })
          this.$baseMessage('删除成功', 'success', 'vab-hey-message-success')
          // await this.fetchData()
        })
      },
      /**
       * @description: 上传成功
       * @return {*}
       */
      // handleSuccess(e) {
      //   console.log(e, 'hengheng')
      //   if (e.code === 200) {
      //     let attInfo = {}
      //     attInfo = e.data
      //     this.fileIdList.push(attInfo.attid)
      //     this.tableDataFile.push(attInfo)
      //     this.$baseMessage('上传成功', 'success', 'vab-hey-message-success')
      //   } else {
      //     this.$baseMessage('上传失败', 'error', 'vab-hey-message-error')
      //   }
      //   this.uploadLoading = false
      // },
      /**
       * @description: 预览
       * @return {*}
       */
      // async handlePreviewFile(row) {
      //   const { data } = await getPrivewAttInfo({
      //     //此接口通用
      //     attId: row.attid,
      //     attType: 2,
      //   })

      //   window.open(
      //     data.previewurl +
      //       '?url=' +
      //       encodeURIComponent(Base64.encode(data.ftpUrl))
      //   )
      // },
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
          this.tableDataFile = [...this.tableDataFile, ...file.data]
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

      async ymsubmit() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.$refs.resubmit.ymsubmit()
          }
        })
      },
      /**
       * @description: 一体化 打开详情
       * @return {*}
       */
      showControlDetail(row) {
        this.$refs.controlMeasures.showEdit(
          row,
          {
            riskcopingid: this.riskcopingid,
            riskHopeValue: this.formData.riskHopeValue,
            risknumber: this.formData.risknumber,
            type: 'ydpg',
          },
          true
        )
      },
      handleEditControl(row) {
        this.$refs.controlMeasures.showEdit(row, {
          riskcopingid: this.riskcopingid,
          riskHopeValue: this.formData.riskHopeValue,
          riskid: this.formData.riskid,
          type: 'ydpg',
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
