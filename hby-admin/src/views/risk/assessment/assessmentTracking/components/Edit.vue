<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
      :close-on-press-escape="false"
      :close-on-click-modal="false"
    >
      <el-row :gutter="15" v-loading="loading">
        <el-form
          :disabled="alldisabled"
          ref="elForm"
          label-width="140px"
          :model="formData"
          :rules="rules"
          size="medium"
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
                :style="{ width: '75%' }"
                disabled
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="
                  fromType = ''
                  $refs.ZXPerson.showEdit(formData.secrectLevelId)
                "
                :disabled="!formData.secrectLevelId || alldisabled"
              >
                选择
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="showMJ">
            <el-divider>基本信息</el-divider>
          </el-col>
          <el-col :span="12">
            <el-form-item label="评估计划编号" prop="plancode">
              <el-input
                v-model="formData.plancode"
                readonly
                placeholder="请输入评估计划编号"
                :style="{ width: '100%' }"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="评估计划名称" prop="planName">
              <el-input
                v-model="formData.planName"
                clearable
                placeholder="请输入评估计划名称"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划开始时间" prop="startDate">
              <el-date-picker
                v-model="formData.startDate"
                clearable
                format="yyyy-MM-dd"
                placeholder="请选择计划开始时间"
                :style="{ width: '100%' }"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划结束时间" prop="endDate">
              <el-date-picker
                v-model="formData.endDate"
                clearable
                format="yyyy-MM-dd"
                placeholder="请选择计划结束时间"
                :style="{ width: '100%' }"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="评估标准" prop="assname">
              <el-input
                placeholder="请选择评估标准"
                readonly
                v-model="formData.assname"
                @click.native="!alldisabled && handleSetAss()"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划类型" prop="planType">
              <el-select
                v-model="formData.planType"
                clearable
                placeholder="请选择计划类型"
                :style="{ width: '100%' }"
              >
                <el-option
                  v-for="(item, index) in typeOptions"
                  :key="index"
                  :disabled="item.disabled"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="制定机构" prop="orgname">
              <el-input
                v-model="formData.orgname"
                disabled
                clearable
                placeholder="请输入制定机构"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="录入人员" prop="realname">
              <el-input
                v-model="formData.realname"
                clearable
                disabled
                placeholder="请输入录入人员"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="评估计划描述" prop="planDes">
              <el-input
                v-model="formData.planDes"
                :autosize="{ minRows: 4, maxRows: 8 }"
                placeholder="请输入评估计划描述"
                :style="{ width: '100%' }"
                type="textarea"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-divider>评估任务明细</el-divider>
          </el-col>
          <el-col :span="24">
            <el-table :data="tableList">
              <el-table-column
                align="center"
                label="风险点编号"
                prop="risknumber"
              >
                <template #default="{ row }">
                  <el-button
                    type="text"
                    @click="showRiskDetail(row)"
                    :disabled="false"
                  >
                    {{ row.risknumber }}
                  </el-button>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="风险名称"
                prop="riskname"
                show-overflow-tooltip
              />
              <el-table-column
                align="center"
                label="风险点描述"
                prop="riskdes"
                show-overflow-tooltip
              />
              <el-table-column
                align="center"
                label="评估人员"
                prop="realname"
                show-overflow-tooltip
              />
              <el-table-column
                align="center"
                label="状态"
                prop="asssatus"
                width="120"
              >
                <template #default="{ row }">
                  {{
                    row.asssatus == 0
                      ? '未评估'
                      : row.asssatus == 1
                      ? '已保存'
                      : row.asssatus == 2
                      ? '已评估'
                      : ''
                  }}
                </template>
              </el-table-column>
              <el-table-column align="center" label="发生频率" prop="frequency">
                <template #default="{ row }">
                  <span :style="{ color: getColorByValue(row.frequency) }">
                    {{ getLabelByValue(row.frequency, 'frequency') }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column align="center" label="严重程度" prop="severity">
                <template #default="{ row }">
                  <span :style="{ color: getColorByValue(row.severity) }">
                    {{ getLabelByValue(row.severity, 'severity') }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column align="center" label="风险等级" prop="risklevel">
                <template #default="{ row }">
                  <span :style="{ color: getColorByValue(row.risklevel) }">
                    {{ getLabelByValue(row.risklevel, 'risklevel') }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column align="center" label="评估时间" prop="assdate" />
            </el-table>
          </el-col>
          <el-col :span="24">
            <UEditor
              ref="ueditor"
              v-model="formData.content"
              :height="300"
              :templates="templates"
              template="PGJH"
            />
          </el-col>

          <el-col :span="24">
            <el-divider>附件</el-divider>
          </el-col>
          <el-col :span="24">
            <div style="text-align: right; margin-bottom: 5px">
              <el-upload
                style="text-align: right; margin-bottom: 5px"
                class="upload-demo"
                :action="baseApi + api"
                :data="{
                  moduleType: 'PGJH',
                  moduleId: this.formData.id,
                }"
                :headers="headers"
                :on-success="handleSuccess"
                :show-file-list="false"
                multiple
                :file-list="fileList"
              >
                <div style="margin-right: 10px">
                  <el-button type="success">上传</el-button>
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
                <template #default="{ row }">
                  <el-button
                    type="text"
                    @click="handleDownload(row)"
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
                    @click="handleDeleteAttach(row)"
                    v-if="!alldisabled"
                  >
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-form>
      </el-row>
      <template #footer>
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </template>
    </el-dialog>

    <standard ref="setStandard" @handelStandard="handelStandard" />
    <riskList ref="setRisk" @setRisk="setRisk" />
    <Proportion ref="setProportion" @setProportion="setProportion" />
    <CompanySelectUserByTree
      ref="userTreeRef"
      isUserName
      @selected="handleExecutorSelected"
    />
    <RiskRead ref="riskRead" />
    <RiskEdit ref="read" />
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </div>
</template>
<script>
  import RiskRead from '@/views/risk/identify/creation/components/RiskRead.vue'
  import RiskEdit from '@/views/risk/identify/creation/components/RiskEdit.vue'

  import UEditor from '@/components/UEditor'
  import {
    addRiskInfo,
    delPgglByRisk,
    riPlanAdd,
    riPlanAddPage,
    riPlanInfo,
    riTaskInfo,
    saveUsers,
    saveFile,
    downloadFile,
    trackItemList,
  } from '@/api/systemLog'
  import {
    deleteFieldById,
    downFieldById,
    createFindAutoNumber,
    createFindAutoNumberNew,
  } from '@/api/risk/riskEvents'
  import CompanySelectUserByTree from '@/components/CompanySelectUserByTree'
  import store from '@/store'
  const { baseURL } = require('@/config')
  import {
    getFlowTaskInfo,
    ymWorkCandidates,
    ymWorkSubmit,
  } from '@/api/contract/manage'
  import CandidateUserSelect from '@/components/CandidateUserSelect'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import ZXPerson from '@/components/selectPerson.vue'
  import { hasMJ, couldMJ } from '@/utils'
  import { getMJ } from '@/api/setting/mjsz'
  import { UTCformat } from '@/utils'
  export default {
    name: 'PlanEdit',
    components: {
      standard: () => import('../../components/standard.vue'),
      riskList: () => import('../../components/riskList.vue'),
      Proportion: () => import('../../components/Proportion.vue'),
      CompanySelectUserByTree,
      CandidateUserSelect,
      UEditor,
      RiskRead,
      ZXPerson,
      RiskEdit,
    },
    data() {
      return {
        title: '',
        baseApi: baseURL,
        api: '/riskcontrol/attachment/uploadFileAttInfo',
        headers: {
          token: store.getters['user/token'],
        },
        isAdd: '',
        loading: false,
        dialogFormVisible: false,
        fileList: [],
        assrisk: {},
        formData: {
          assId: undefined,
          assname: undefined,
          plancode: undefined,
          planName: undefined,
          startDate: null,
          endDate: null,
          planType: undefined,
          orgname: undefined,
          realname: undefined,
          planDes: undefined,
          content: undefined,
          id: undefined,
          secrectLevelId: undefined,
          staffScopeNames: undefined,
          staffScopeIds: undefined,
        },
        fileIdList: [],
        templates: [],
        removeIds: [],
        list: [],
        tableList: [],
        showRow: {},
        tableData: [],
        tableDataFile: [],
        rules: {
          plancode: [
            {
              required: true,
              message: '请输入评估计划编号',
              trigger: 'blur',
            },
          ],
          planName: [
            {
              required: true,
              message: '请输入评估计划名称',
              trigger: 'blur',
            },
          ],
          startDate: [
            {
              required: true,
              message: '请选择计划开始时间',
              trigger: 'blur',
            },
          ],
          endDate: [
            {
              required: true,
              message: '请选择计划结束时间',
              trigger: 'blur',
            },
          ],
          assname: [
            {
              required: true,
              message: '请选择评估标准',
              trigger: 'blur',
            },
          ],
          planType: [
            {
              required: true,
              message: '请选择计划类型',
              trigger: 'blur',
            },
          ],
          orgname: [],
          realname: [],
          planDes: [
            {
              required: true,
              message: '请输入计划描述',
              trigger: 'blur',
            },
          ],
        },
        typeOptions: [
          {
            label: '年度计划',
            value: '1',
          },
          {
            label: '临时性计划',
            value: '2',
          },
        ],
        showRow: {},
        showMJ: false,
        MJoption: [],
        alldisabled: false,
        fromType: '',
        frequencyOptions: [
          {
            label: '很低',
            value: 1,
            color: '#52FFB7',
          },
          {
            label: '较低',
            value: 2,
            color: '#33D73B',
          },
          {
            label: '中等',
            value: 3,
            color: '#FFB500',
          },
          {
            label: '较高',
            value: 4,
            color: '#FF7F00',
          },
          {
            label: '很高',
            value: 5,
            color: '#E92129',
          },
        ],
        severityOptions: [
          {
            label: '很低',
            value: '1',
            color: '#52FFB7',
          },
          {
            label: '较低',
            value: '2',
            color: '#33D73B',
          },
          {
            label: '中等',
            value: '3',
            color: '#FFB500',
          },
          {
            label: '较高',
            value: '4',
            color: '#FF7F00',
          },
          {
            label: '很高',
            value: '5',
            color: '#E92129',
          },
        ],
        footer: undefined,
        color: ['', '#52FFB7', '#33D73B', '#FFB500', '#FF7F00', '#E92129'],
      }
    },
    computed: {},
    watch: {
      'formData.content'(val) {
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
          ]
          arr.forEach((i) => {
            if (this.formData[i[1]]) {
              s = s.replace(i[0], this.formData[i[1]])
            }
          })
          this.formData.content = s
        }
      },
    },
    async created() {
      this.showMJ = couldMJ()
      if (this.showMJ) {
        const res = await hasMJ('AssessmentPlan')
        this.menuId = res[0].menuid
        const res2 = await getMJ({ rightId: res[0].menuid })
        this.MJoption = res2.data
      }
    },
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
            this.formData.staffScopeNames = ''
            this.formData.staffScopeIds = ''
            this.formData.assId = undefined
            this.formData.assname = undefined
          }
        }
      },
      async handleZXPersonSelected(val) {
        if (this.fromType == '') {
          const ids = val.map((res) => res.staffid).toString()
          const names = val.map((res) => res.realname).toString()
          this.formData.staffScopeIds = ids
          this.formData.staffScopeNames = names
        } else {
          this.handleExecutorSelected(val)
        }
      },
      beforUpload() {
        this.uploadLoading = true
      },
      handleExecutorSelected(e) {
        const data = {
          assriskid: this.assrisk.assriskid,
          users: e.map((v) => v.staffid).join(','),
        }
        saveUsers(data).then(() => {
          const currentRisk = this.list.find(
            (item) => item.assriskid === this.assrisk.assriskid
          )
          if (currentRisk) {
            currentRisk.tblRiskRiskMarking = e.map((user) => ({
              staff: {
                realname: user.realname,
                staffid: user.staffid,
              },
              assweight: 0,
            }))
          }
        })
      },
      handleSuccess(e) {
        if (e.code === 200) {
          let attInfo = {}
          attInfo = e.data
          this.fileIdList.push(attInfo.attid)
          this.tableDataFile.push(attInfo)
          this.$baseMessage('上传成功', 'success', 'vab-hey-message-success')
        } else {
          this.$baseMessage('上传失败', 'error', 'vab-hey-message-error')
        }
        this.uploadLoading = false
      },
      handleSetAss() {
        this.$refs['setStandard'].showEdit(this.formData.secrectLevelId)
      },
      save() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            let content = this.formData.content || ''
            const info = {
              ...this.formData,
              content,
              reporteds: this.fileIdList.toString(),
              removeReporteds: this.removeIds.toString(),
            }
            if (this.isAdd === 'add') {
              console.log(info, 'formData')
              riPlanAdd(info).then((res) => {
                if (res.code == 1) {
                  this.$baseMessage(
                    '新增成功',
                    'success',
                    'vab-hey-message-success'
                  )
                  this.$set(this.formData, 'id', res.data.assplanid)
                  this.$set(this.formData, 'assplanid', res.data.assplanid)
                  this.showRow = {
                    assplanid: res.data.assplanid,
                    ...this.formData,
                  }
                  this.$emit('fetchData')
                  this.getTableList()
                }
              })
            } else {
              console.log(info, 'formData')
              riPlanAdd(info).then((res) => {
                if (res.code == 1) {
                  this.$baseMessage(
                    '编辑成功',
                    'success',
                    'vab-hey-message-success'
                  )
                  this.$emit('fetchData')
                }
              })
            }
          }
        })
      },
      async showEdit(row, type) {
        this.loading = true
        this.alldisabled = false
        if (!row) {
          this.isAdd = 'add'
          this.title = '添加'
          createFindAutoNumberNew().then((res) => {
            this.$set(this.formData, 'plancode', res.data)
          })
          riPlanAddPage().then((res) => {
            this.$set(this.formData, 'realname', res.data.Staff.realname)
            this.$set(this.formData, 'orgname', res.data.orgByUser.orgname)
            this.$set(this.formData, 'unit', res.data.orgByUser.orgid)

            this.loading = false
          })
        } else {
          if (type == 'detail') {
            this.isAdd = 'detail'
            this.title = '详情'
            this.alldisabled = true
          } else {
            this.isAdd = 'edit'
            this.title = '编辑'
            this.alldisabled = false
          }
          let data = {
            planId: row.assplanid,
          }
          this.showRow = row
          await riTaskInfo(data).then(async (res) => {
            console.log(res, 'aaa')

            this.$set(this, 'formData', {
              assplanid: row.assplanid,
              plancode: res.data.RiskAssplan.plancode,
              planName: res.data.RiskAssplan.planName,
              startDate: res.data.RiskAssplan.startDate,
              endDate: res.data.RiskAssplan.endDate,
              assname: res.data.RiskAssplan.assessmentstd.assname,
              planType: res.data.RiskAssplan.planType,
              orgname: res.data.RiskAssplan.organization.memo,
              planDes: res.data.RiskAssplan.plandes,
              realname: res.data.RiskAssplan.recorder,
              id: res.data.RiskAssplan.assplanid,
              assId: res.data.RiskAssplan.assessmentstd.assstdid,
              unit: res.data.RiskAssplan.organization.orgid,
              status: res.data.RiskAssplan.status,
              content: res.data.RiskAssplan.content || '',
              secrectLevelId: res.data.RiskAssplan.secrectLevelId,
              staffScopeNames: res.data.RiskAssplan.staffScopeNames,
              staffScopeIds: res.data.RiskAssplan.staffScopeIds,
            })
            this.tableDataFile = res.data.RiskAssplan.tblAttachments || []
            this.fileIdList = []
            res.data.RiskAssplan.tblAttachments.forEach((item) => {
              this.fileIdList.push(item.attid)
            })
            this.getTableList()
            this.removeIds = []
            let list = res.data.RiskAssplan.riskAssplanRiskList || []
            let tList = []
            list.map((item) => {
              if (item.risk) {
                tList.push(item)
              }
            })
            this.list = tList
            console.log(this.list, 'this.list')
            this.loading = false
          })
        }
        this.dialogFormVisible = true
      },

      async getTableList() {
        const response = await trackItemList({ planId: this.formData.id })
        if (
          response.data &&
          response.data.pageBean &&
          response.data.pageBean.records
        ) {
          const records = response.data.pageBean.records
          this.tableList = records.map((v, index) => {
            v.index = index
            if (v.assdate) {
              v.assdate = UTCformat(v.assdate)
            }
            return v
          })
        } else {
          this.tableList = []
          console.error('获取评估任务明细数据结构不符合预期', response)
        }
      },
      handelStandard(row) {
        this.$set(this.formData, 'assId', row.assstdid)
        this.$set(this.formData, 'assname', row.assname)
        this.$refs['elForm'].clearValidate()
      },
      uniqueFunc(arr, uniId) {
        const res = new Map()
        return arr.filter(
          (item) => !res.has(item[uniId]) && res.set(item[uniId], 1)
        )
      },
      setRisk(row) {
        let list = this.list.concat(row || [])
        this.list = list
        console.log(list, 'this.list')
        const data = {
          assplanid: this.formData.id,
          selectList: row.map((v) => v.riskid).join(','),
        }
        addRiskInfo(data).then((res) => {
          this.$baseMessage(res.msg, 'success')
          riTaskInfo({ planId: this.formData.id }).then((res2) => {
            console.log('res2', res2)

            let list = res2.data.RiskAssplan.riskAssplanRiskList || []
            let tList = []
            list.map((item) => {
              if (item.risk) {
                tList.push(item)
              }
            })
            this.list = tList
            this.loading = false
          })
        })
      },
      setProportion() {
        riTaskInfo({ planId: this.formData.id }).then((res) => {
          this.list = res.data.RiskAssplan.riskAssplanRiskList || []
          this.loading = false
        })
        this.$forceUpdate()
      },
      close() {
        this.dialogFormVisible = false
        this.list = []
        this.tableDataFile = []

        this.formData = {
          plancode: undefined,
          planName: undefined,
          startDate: null,
          endDate: null,
          planType: undefined,
          orgname: undefined,
          realname: undefined,
          planDes: undefined,
          content: undefined,
          id: undefined,
          secrectLevelId: undefined,
          staffScopeNames: undefined,
          staffScopeIds: undefined,
          assId: undefined,
          assname: undefined,
        }
        this.fileIdList = []
        this.removeIds = []
      },
      handleSetStaff(row) {
        console.log('🚀 ~ handleSetStaff ~ row:', row)
        this.assrisk = row
        if (
          this.formData.secrectLevelId &&
          this.formData.secrectLevelId != ''
        ) {
          this.fromType = 'selectPerson'
          this.$refs.ZXPerson.showEdit(this.formData.secrectLevelId)
        } else {
          let list = row.tblRiskRiskMarking.map((x) => {
            return {
              orgName: x.staff.orgName,
              orgid: x.staff.orgid,
              realname: x.staff.realname,
              rn: x.staff.rn,
              staffid: x.staff.staffid,
              username: x.staff.username,
            }
          })
          this.$refs['userTreeRef'].show(list)
        }
      },
      handleSetWeight(row) {
        console.log('set weight', row)
        this.$refs['setProportion'].showEdit(row)
      },
      async handleDownload(row) {
        const res = await downFieldById({ id: row.attid })
        console.log(res)
        if (!res) return
        let filename = row.attname
        let blob = new Blob([res])
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
      downloadFileByBlob(blob, fileName = 'file') {
        let blobUrl = window.URL.createObjectURL(blob)
        let link = document.createElement('a')
        link.download = fileName || 'defaultName'
        link.style.display = 'none'
        link.href = blobUrl
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
      },
      handleDeleteAttach(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, () => {
          const deleteId = row.attid
          this.removeIds.push(deleteId)
          this.fileIdList = this.fileIdList.filter((item) => {
            return item != deleteId
          })
          this.tableDataFile = this.tableDataFile.filter((item) => {
            return item != row
          })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
        })
      },
      handleDeleteRisk(row, index) {
        console.log(row, 'riskid=====')
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          this.list.splice(index, 1)
          const data = {
            planid: this.formData.id,
            riskid: row.risk.riskid,
          }
          const { msg } = await delPgglByRisk(data)
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
        })
      },
      async handlePreviewFile(row) {
        console.log('row', row)
        const { data } = await getPrivewAttInfo({
          attId: row.attid,
          attType: 2,
        })

        window.open(
          data.previewurl +
            '?url=' +
            encodeURIComponent(Base64.encode(data.ftpUrl))
        )
      },
      hanAddRisk() {
        if (!this.formData.assplanid)
          return this.$message.error('请先保存基本信息')
        this.$refs['setRisk'].showEdit(this.formData.secrectLevelId)
      },
      showRiskDetail(row) {
        console.log(row)
        this.$refs['read'].showEdit(row, '', true)
      },
      getLabelByValue(value, type) {
        if (!value) return '未评估'

        const options =
          type == 'risklevel' ? this.severityOptions : this.frequencyOptions
        const option = options.find((item) => item.value == value)
        return option ? option.label : value
      },

      getColorByValue(value) {
        if (!value) return ''

        return this.color[value] || ''
      },
    },
  }
</script>
<style></style>
