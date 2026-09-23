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
            <UEditor
              ref="ueditor"
              v-model="formData.content"
              :height="300"
              :templates="templates"
              template="PGJH"
            />
          </el-col>
          <el-col :span="24">
            <el-divider>评估的风险信息</el-divider>
          </el-col>
          <div
            style="text-align: right; margin-bottom: 5px; margin-right: 10px"
          >
            <el-button type="success" @click="hanAddRisk">选择风险</el-button>
          </div>
          <el-col :span="24">
            <el-table :data="list">
              <el-table-column
                align="center"
                label="风险点编号"
                prop="risk.risknumber"
                show-overflow-tooltip
              >
                <template #default="{ row }">
                  <el-button
                    type="text"
                    @click="showRiskDetail(row.risk)"
                    :disabled="false"
                  >
                    {{ row.risk.risknumber }}
                  </el-button>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="风险点描述"
                prop="risk.riskdes"
                show-overflow-tooltip
              />
              <el-table-column
                align="center"
                label="评估人员"
                prop="reporter"
                show-overflow-tooltip
              >
                <template #default="{ row }">
                  <div v-for="user in row.tblRiskRiskMarking" :key="user.id">
                    {{
                      user.staff.realname + '(' + (user.assweight || 0) + '%)'
                    }}
                  </div>
                </template>
              </el-table-column>
              <el-table-column align="center" label="操作">
                <template #default="{ row, $index }">
                  <el-button type="text" @click="handleDeleteRisk(row, $index)">
                    删除风险
                  </el-button>
                  <el-button type="text" @click="handleSetStaff(row)">
                    设置评估人员
                  </el-button>
                  <el-button type="text" @click="handleSetWeight(row)">
                    设置权重
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
          <el-col :span="24">
            <el-divider>附件</el-divider>
          </el-col>
          <el-col :span="24">
            <div style="text-align: right; margin-bottom: 5px">
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
        <!-- <el-button
          v-if="
            (formData.status == 2 || formData.status == 3) &&
            jurisdictionCode == 1
          "
          @click="ymsubmit"
          type="primary"
        >
          提交
        </el-button> -->
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
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    name: 'PlanEdit',
    components: {
      standard: () => import('../../components/standard.vue'),
      riskList: () => import('../../components/riskList.vue'),
      Proportion: () => import('../../components/Proportion.vue'),
      CompanySelectUserByTree,
      CandidateUserSelect,
      UEditor: () => import('@/components/UEditor'),
      RiskRead,
      ZXPerson,
      RiskEdit,
    },
    data() {
      return {
        title: '',
        baseApi: baseURL,
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        fileList: [],
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
      'formData.content'(val) {
        if (
          this.$refs['ueditor'] &&
          this.$refs['ueditor'].editor &&
          this.$refs['ueditor'].editor.openTemplate
        ) {
          this.$refs['ueditor'].editor.openTemplate = false
          this.formData.content = this.replaceTemplateVariables(val)
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
      replaceTemplateVariables(content) {
        if (!content) return content

        const templateMap = new Map([
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
          ['$[counterpart.contactsPhone]', 'contactsphone'],
          ['$[counterpart.counterpartHankAccount]', 'bankaccount'],
          ['$[counterpart.legarepresentative]', 'contacts'],
          ['$[counterpart.pctelephonenumber]', 'contractzd'],
        ])

        let result = content
        for (const [template, field] of templateMap) {
          if (this.formData[field]) {
            result = result.replace(
              new RegExp(template.replace(/[.*+?^${}()|[\]\\]/g, '\\$&'), 'g'),
              this.formData[field]
            )
          }
        }

        return result
      },
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
      /**
       * @description: 上传校验
       * @return {*}
       */
      beforUpload() {
        this.uploadLoading = true
        // if (!this.formData.id) {
        //   this.$baseMessage('请先保存基本信息', 'error', 'vab-hey-message-error')
        //   return false
        // } else {
        //   this.uploadLoading = true
        // }
      },
      // 设置评估人员
      handleExecutorSelected(e) {
        const data = {
          // assplanid: this.assrisk.assplanid,
          assriskid: this.assrisk.assriskid,
          users: e.map((v) => v.staffid).join(','),
        }
        // saveUsers(data).then((res) => {
        //   this.$message({ type: 'success', message: '设置成功' })
        //   this.list = []
        //   riTaskInfo({ planId: this.formData.id }).then((res2) => {
        //     console.log('res2', res2)

        //     let list = res2.data.RiskAssplan.riskAssplanRiskList || []
        //     let tList = []
        //     list.map((item) => {
        //       if (item.risk) {
        //         tList.push(item)
        //       }
        //     })
        //     this.list = tList
        //     this.loading = false
        //   })
        // })
        saveUsers(data).then(() => {
          // 直接更新当前行的评估人员数据
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
        // this.$set(this.formData, 'reporter', e.realname)
        // this.$set(this.formData, 'reporterid', e.staffid)
      },
      handleSetAss() {
        this.$refs['setStandard'].showEdit(this.formData.secrectLevelId)
      },
      /**
       * @description: 保存
       * @return {*}
       */
      save() {
        // this.formData.startDate = this.formData.startDate
        // this.formData.endDate = this.formData.endDate

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
                  // this.formData.id = res.data.assplanid
                  this.$set(this.formData, 'id', res.data.assplanid)
                  this.$set(this.formData, 'assplanid', res.data.assplanid)
                  this.showRow = {
                    assplanid: res.data.assplanid,
                    ...this.formData,
                  }
                  this.$emit('fetchData')
                  // this.close()
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
                  // this.close()
                }
              })
            }
          }
        })
      },
      /**
       * @description: 初始化，获取数据
       * @return {*}
       */
      async showEdit(row, type) {
        this.loading = true
        this.alldisabled = false
        if (!row) {
          this.isAdd = 'add'
          this.title = '添加'
          // {
          //   tblName: 'TBL_RISK_ASSPLAN',
          //   column: 'PLANCODE',
          //   orgCol: 'UNIT',
          //   noId: 259,
          // }
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
            //这块是点击确定按钮，保存时候要传的reporteds
            this.fileIdList = []
            res.data.RiskAssplan.tblAttachments.forEach((item) => {
              this.fileIdList.push(item.attid)
            })
            //删除列表为空
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
      /**
       * @description: 选择成功 回调
       * @return {*}
       */
      handelStandard(row) {
        this.$set(this.formData, 'assId', row.assstdid)
        this.$set(this.formData, 'assname', row.assname)
        this.$refs['elForm'].clearValidate()
      },
      /**
       * @description: 去重
       * @return {*}
       */
      uniqueFunc(arr, uniId) {
        const res = new Map()
        return arr.filter(
          (item) => !res.has(item[uniId]) && res.set(item[uniId], 1)
        )
      },
      /**
       * @description: 保存风险
       * @return {*}
       */
      setRisk(row) {
        let list = this.list.concat(row || [])
        // list = this.uniqueFunc(list, 'riskid')
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
      /**
       * @description: 关闭页面
       * @return {*}
       */
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
      /**
       * @description: 打开选择评估人员页面
       * @return {*}
       */
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
          // console.log('set staff', row)
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
      /**
       * @description: 打开设置权重页面
       * @return {*}
       */
      handleSetWeight(row) {
        console.log('set weight', row)
        this.$refs['setProportion'].showEdit(row)
      },
      /**
       * @description: 下载
       * @return {*}
       */
      async handleDownload(row) {
        const res = await downFieldById({ id: row.attid })
        console.log(res)
        if (!res) return
        let filename = row.attname
        let blob = new Blob([res]) //res即为blob数据，请注意自己的数据形式
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
      /**
       * @description: 删除附件
       * @return {*}
       */
      handleDeleteAttach(row) {
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
          // console.log(this.tableDataFile, 'this.fileIdList')
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          // await this.fetchData()
        })
      },
      /**
       * @description: 删除风险
       * @return {*}
       */
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
          // await this.fetchData()
        })
      },
      /**
       * @description: 选择风险前校验
       * @return {*}
       */
      hanAddRisk() {
        if (!this.formData.assplanid)
          return this.$message.error('请先保存基本信息')
        this.$refs['setRisk'].showEdit(this.formData.secrectLevelId)
      },
      /**
       * @description: 风险详细
       * @return {*}
       */
      showRiskDetail(row) {
        console.log(row)
        // this.$refs['riskRead'].showRead(row, row.riskcatid)
        this.$refs['read'].showEdit(row, '', true)
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
          // 确保 file.data 是数组格式
          const uploadedFiles = Array.isArray(file.data)
            ? file.data
            : [file.data]

          // 更新文件列表
          this.fileList = [...this.fileList, ...uploadedFiles]
          this.tableDataFile = [...this.tableDataFile, ...uploadedFiles]

          // 保存上传文件的attid到fileIdList
          uploadedFiles.forEach((fileItem) => {
            if (fileItem.attid) {
              this.fileIdList.push(fileItem.attid)
            }
          })

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
<style></style>
