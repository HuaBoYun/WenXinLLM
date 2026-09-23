<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :append-to-body="true"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
    >
      <el-row :gutter="24" v-loading="loading">
        <el-form
          ref="ruleForm"
          label-width="140px"
          :model="formData"
          :rules="rules"
          size="mini"
          :disabled="allDisabled"
        >
          <el-col :span="12">
            <el-form-item label="所属集团" prop="belongGroupName">
              <el-input
                v-model.trim="formData.belongGroupName"
                placeholder="请选择所属集团"
                disabled
              />
              <!-- <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="showGroupLeader('group')"
              >
                选择
              </el-button> -->
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单位" prop="workUnitName">
              <el-input
                v-model.trim="formData.workUnitName"
                placeholder="请选择单位"
                style="width: 75%"
                disabled
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="showGroupLeader('unit')"
              >
                选择
              </el-button>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="法律顾问机构名称" prop="organizationName">
              <el-input
                v-model="formData.organizationName"
                clearable
                placeholder="请输入法律顾问机构名称"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>

          <!-- <el-col :span="12">
            <el-form-item label="是否签订合同" prop="isAwardOfContract">
              <el-select
                :style="{ width: '100%' }"
                v-model="formData.isAwardOfContract"
                placeholder="是否签订合同"
                :disabled="!footer"
              >
                <el-option label="是" value="1" />
                <el-option label="否" value="0" />
              </el-select>
            </el-form-item>
          </el-col> -->

          <el-col :span="12">
            <el-form-item label="费用(万元)" prop="expense">
              <el-input
                v-model="formData.expense"
                clearable
                placeholder="请输入顾问费用（万元）"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="注册地址" prop="registerAddress">
              <el-input
                v-model="formData.registerAddress"
                clearable
                placeholder="请输入注册地址"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="是否有过往合作经历"
              prop="hasCooperationExperience"
            >
              <el-select
                clearable
                :disabled="!footer"
                :style="{ width: '100%' }"
                v-model="formData.hasCooperationExperience"
              >
                <el-option value="1" label="是"></el-option>
                <el-option value="0" label="否"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="formData.hasCooperationExperience === '1'">
            <el-form-item label="过往合作经历" prop="pastCooperationExperience">
              <el-input
                v-model="formData.pastCooperationExperience"
                clearable
                placeholder="请输入过往合作经历"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="聘期开始年月" prop="employmentTermStartTime">
              <el-date-picker
                style="width: 100%"
                v-model="formData.employmentTermStartTime"
                placeholder="选择聘期开始年月"
                type="date"
                :disabled="!footer"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="聘期结束年月" prop="employmentTermEndTime">
              <el-date-picker
                style="width: 100%"
                v-model="formData.employmentTermEndTime"
                placeholder="选择聘期结束年月"
                type="date"
                :disabled="!footer"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="填报人" prop="creatorName">
              <el-input
                v-model="formData.creatorName"
                clearable
                placeholder="请选择填报人"
                style="width: 100%"
                disabled
              />
              <!-- <el-button
                @click="
                  handleSelectPerson({
                    id: 'fillInPersonId',
                    name: 'fillInPerson',
                  })
                "
                style="margin-left: 10px"
                type="primary"
                :disabled="!footer"
              >
                选择
              </el-button> -->
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="填报时间" prop="createdTime">
              <el-date-picker
                style="width: 100%"
                v-model="formData.createdTime"
                placeholder="选择填报时间"
                type="datetime"
                disabled
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
          <!-- <el-col :span="12">
            <el-form-item label="审核人" prop="auditPersonName">
              <el-input
                v-model="formData.auditPersonName"
                clearable
                placeholder="请选择审核人"
                style="width: 85%"
                disabled
              />
              <el-button
                @click="
                  handleSelectPerson({
                    id: 'auditPerson',
                    name: 'auditPersonName',
                  })
                "
                style="margin-left: 10px"
                type="primary"
                :disabled="!footer"
              >
                选择
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="审核时间" prop="auditTime">
              <el-date-picker
                style="width: 100%"
                v-model="formData.auditTime"
                placeholder="选择审核时间"
                type="datetime"
                :disabled="!footer"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col> -->
          <!-- <el-col :span="12">
            <el-form-item label="聘用类型" prop="employmentTermType">
              <el-select
                :style="{ width: '100%' }"
                v-model="formData.employmentTermType"
                placeholder="聘用类型"
                :disabled="!footer"
              >
                <el-option label="常年法律顾问" :value="1" />
                <el-option label="专项法律顾问" :value="2" />
              </el-select>
            </el-form-item>
          </el-col> -->
          <!-- <el-col :span="12">
            <el-form-item label="单据状态" prop="state">
              <el-select
                :style="{ width: '100%' }"
                v-model="formData.state"
                placeholder="是否签订合同"
                :disabled="!footer"
              >
                <el-option label="是" :value="1" />
                <el-option label="否" :value="0" />
              </el-select>
            </el-form-item>
          </el-col> -->
          <el-col :span="24">
            <el-form-item label="服务范围" prop="scopeOfServices">
              <el-input
                v-model="formData.scopeOfServices"
                clearable
                type="textarea"
                row="3"
                placeholder="请输入合同约定的服务范围"
                :disabled="!footer"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="收费标准及支付说明" prop="chargeExplain">
              <el-input
                v-model="formData.chargeExplain"
                clearable
                placeholder="请输入收费标准及支付说明"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>

          <el-col :span="24" style="margin-top: 20px">
            <el-divider>主办律师信息</el-divider>
          </el-col>
          <el-col :span="24">
            <div
              style="text-align: right; margin-bottom: 5px"
              v-if="footer"
            >
              <el-button type="success" @click="handleAdd">新建</el-button>
            </div>
          </el-col>
          <el-col :span="24">
            <el-table :data="lawyerIdList">
              <el-table-column align="center" label="姓名" prop="lawyerName">
                <template #default="{ row }">
                  <el-button
                    type="text"
                    @click="handleEdit(row, 'detail')"
                    :disabled="false"
                  >
                    {{ row.lawyerName }}
                  </el-button>
                </template>
              </el-table-column>
              <el-table-column align="center" label="性别" prop="sex">
                <template #default="{ row }">
                  {{ row.sex == 1 ? '男' : '女' }}
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="身份证号"
                prop="identityCard"
              />
              <el-table-column align="center" label="联系电话" prop="phone" />
              <el-table-column align="center" label="职务" prop="position" />
              <el-table-column align="center" label="学历" prop="education" />
              <el-table-column
                align="center"
                label="操作"
                show-overflow-tooltip
                width="120"
              >
                <template #default="{ row }">
                  <el-button type="text" @click="handleEdit(row, 'edit')">
                    编辑
                  </el-button>
                  <el-button
                    type="text"
                    @click="
                      handleDelete('lawyerId', row, legalServiceLawyerDoDelete)
                    "
                    v-if="footer"
                  >
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>

          <el-col :span="24" style="margin-top: 20px">
            <el-divider>另行收费法律服务项目工作记录</el-divider>
          </el-col>
          <el-col :span="24">
            <div style="text-align: right; margin-bottom: 5px" v-if="footer">
              <el-button type="success" @click="handleAdd2">新建</el-button>
            </div>
          </el-col>
          <el-col :span="24">
            <el-table :data="workRecordIdList">
              <el-table-column
                align="center"
                label="法律服务名称"
                prop="lawServiceName"
              >
                <template #default="{ row }">
                  <el-button
                    type="text"
                    @click="handleEdit2(row, 'detail')"
                    :disabled="false"
                  >
                    {{ row.lawServiceName }}
                  </el-button>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="服务项目类型"
                prop="serviceProjectType"
              />
              <el-table-column
                align="center"
                label="涉及标的金额（万元）"
                prop="money"
              />
              <el-table-column
                align="center"
                label="承办律所"
                prop="undertakeLawOffice"
              />
              <el-table-column
                align="center"
                label="主办律师"
                prop="hostLawOffice"
                #default="{ row }"
              >
                {{
                  lawyerIdList.find(
                    (x) => x.lawyerId === Number(row.hostLawOffice)
                  ).lawyerName
                }}
              </el-table-column>
              <el-table-column
                align="center"
                label="法律服务效果及评价"
                prop="serviceEffectivenessOfEvaluate"
              />
              <el-table-column
                align="center"
                label="操作"
                show-overflow-tooltip
                width="120"
              >
                <template #default="{ row }">
                  <el-button type="text" @click="handleEdit2(row, 'edit')">
                    编辑
                  </el-button>
                  <el-button
                    type="text"
                    @click="
                      handleDelete(
                        'workRecordId',
                        row,
                        legalServiceWorkRecordDoDelete
                      )
                    "
                    v-if="footer"
                  >
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>

          <el-col :span="24" style="margin-top: 20px">
            <el-divider>工作报告</el-divider>
          </el-col>
          <el-col :span="24">
            <div style="text-align: right; margin-bottom: 5px" v-if="footer">
              <el-button type="success" @click="handleAdd3">新建</el-button>
            </div>
          </el-col>
          <el-col :span="24">
            <el-table :data="workReportIdList">
              <el-table-column align="center" label="序号" type="index" />
              <el-table-column
                align="center"
                label="事务所名称"
                prop="businessPremisesName"
              >
                <template #default="{ row }">
                  <el-button
                    type="text"
                    @click="handleEdit3(row, 'detail')"
                    :disabled="false"
                  >
                    {{ row.businessPremisesName }}
                  </el-button>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="报告日期"
                prop="reportTime"
                :formatter="formatDate"
              />
              <el-table-column
                align="center"
                label="操作"
                show-overflow-tooltip
                width="120"
              >
                <template #default="{ row }">
                  <el-button type="text" @click="handleEdit3(row, 'edit')">
                    编辑
                  </el-button>
                  <el-button
                    type="text"
                    @click="
                      handleDelete(
                        'workReportId',
                        row,
                        legalServiceWorkReportDoDelete
                      )
                    "
                    v-if="footer"
                  >
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>

          <el-col :span="24" style="margin-top: 20px">
            <el-divider>评价表</el-divider>
          </el-col>
          <el-col :span="24">
            <div style="text-align: right; margin-bottom: 5px" v-if="footer">
              <el-button type="success" @click="handleAdd4">新建</el-button>
            </div>
          </el-col>
          <el-col :span="24">
            <el-table :data="gradeIdList">
              <el-table-column
                align="center"
                label="聘用单位"
                prop="hireUnitName"
              />
              <el-table-column align="center" label="服务质量" prop="item1" />
              <el-table-column align="center" label="沟通合作" prop="item2" />
              <el-table-column align="center" label="增值服务" prop="item3" />
              <el-table-column align="center" label="总分" prop="totalScore" />
              <el-table-column
                align="center"
                label="考核结果"
                prop="examineGrade"
              />
              <el-table-column
                align="center"
                label="其他意见或建议"
                prop="opinions"
              />
              <el-table-column
                align="center"
                label="操作"
                show-overflow-tooltip
                width="120"
              >
                <template #default="{ row }">
                  <el-button type="text" @click="handleEdit4(row)">
                    编辑
                  </el-button>
                  <el-button
                    type="text"
                    @click="
                      handleDelete('gradeId', row, legalServiceGradeDoDelete)
                    "
                    v-if="footer"
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
        <el-button @click="close">关 闭</el-button>
        <el-button @click="save" type="primary" v-if="!allDisabled">
          确定
        </el-button>
      </template>
    </el-dialog>

    <experienceView ref="experienceView" @on-save-success="onSaveSuccess" />
    <workView ref="workView" @on-save-success="onSaveSuccess" />
    <workStatementView
      ref="workStatementView"
      @on-save-success="onSaveSuccess"
    />
    <evaluationEdit ref="evaluationEdit" @on-save-success="onSaveSuccess" />
    <company-select-modal
      ref="companySelect"
      @selected="handleCompanyTreeSelected"
    />
    <project-manage @projectManage="getChildlistPro" ref="manage" />
  </div>
</template>
<script>
  import experienceView from './experienceView.vue'
  import workView from './workView.vue'
  import workStatementView from './workStatementView.vue'
  // import scoreView from './scoreView.vue'
  import evaluationEdit from './evaluationEdit.vue'
  import CompanySelectModal from '@/components/CampanySelectModal'
  import projectManage from '@/views/audit/project/components/formComponents/projectManage.vue'
  import { formatDay } from '@/utils/index'

  import {
    legalService,
    legalServiceLawyer,
    legalServiceWorkRecord,
    legalServiceWorkReport,
    legalServiceGrade,
    fetchApi,
  } from '@/api/fwgl/api'

  const { saveOrUpdate, detail } = legalService
  const { doDelete: legalServiceLawyerDoDelete } = legalServiceLawyer
  const { doDelete: legalServiceWorkRecordDoDelete } = legalServiceWorkRecord
  const { doDelete: legalServiceWorkReportDoDelete } = legalServiceWorkReport
  const { doDelete: legalServiceGradeDoDelete } = legalServiceGrade

  export default {
    props: [],
    components: {
      experienceView,
      workView,
      workStatementView,
      // scoreView,
      evaluationEdit,
      CompanySelectModal,
      projectManage,
    },
    data() {
      return {
        loading: false,
        dialogFormVisible: false,
        title: '新增',
        footer: true,
        formData: {},
        lawyerIdList: [], // 律师信息
        workRecordIdList: [], // 工作记录
        workReportIdList: [], // 工作报告
        gradeIdList: [], // 评分
        rules: {
          organizationName: [
            {
              required: true,
              message: '请输入法律顾问机构名称',
              trigger: 'blur',
            },
          ],
          workUnitName: [
            {
              required: true,
              message: '请输入工作单位名称',
              trigger: 'blur',
            },
          ],
        },
        legalServiceLawyerDoDelete: legalServiceLawyerDoDelete,
        legalServiceWorkRecordDoDelete: legalServiceWorkRecordDoDelete,
        legalServiceWorkReportDoDelete: legalServiceWorkReportDoDelete,
        legalServiceGradeDoDelete: legalServiceGradeDoDelete,
        allDisabled: false,
      }
    },
    methods: {   
      /**
       * @description: 外部打开dialog
       * @param {*} title 类型
       * @param {*} row 行数据
       * @return {*}
       */      
      async showEdit(title, row) {
        this.footer = true
        this.dialogFormVisible = true
        this.lawyerIdList = []
        this.workRecordIdList = []
        this.workReportIdList = []
        this.gradeIdList = []
        const userInfo = JSON.parse(localStorage.getItem('userInfo'))
        this.formData.belongGroupName = userInfo.currentOrg.orgname
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
          this.allDisabled = true
        } else {
          this.title = '新增'
          this.formData.creatorName = userInfo.realname
          this.formData.createdTime = new Date()
        }

        if (row) {
          this.loading = true
          const res = await fetchApi(detail, { id: row.id })
          this.loading = false
          if (res && res.code === 200 && res.data) {
            if (res.data.lawService.pastCooperationExperience) {
              res.data.lawService.hasCooperationExperience = '1'
            } else {
              res.data.lawService.hasCooperationExperience = '0'
            }
            this.formData = res.data.lawService
            this.formData.belongGroupName = userInfo.currentOrg.orgname
            // this.formData.isAwardOfContract =
            //   res.data.lawService.isAwardOfContract.toString()
            this.formData.employmentTermType = res.data.lawService
              .employmentTermType
              ? res.data.lawService.employmentTermType.toString()
              : ''
            this.lawyerIdList = res.data.lawServiceLawyer
            this.workRecordIdList = res.data.lawServiceWorkRecord
            this.workReportIdList = res.data.lawServiceWorkReport

            this.gradeIdList = (res.data.lawServiceEvaluate || []).map((x) => {
              const tableData = JSON.parse(x.detailListJson)
              const xObj = {
                item1: tableData[0].score,
                item2: tableData[1].score,
                item3: tableData[2].score,
                ...x,
              }
              return xObj
            })
          }
        }
      },
      /**
       * @description: 单位选择
       * @param {*} type 类型
       * @return {*}
       */      
      showGroupLeader(type) {
        if (type === 'group') {
          this.$refs.companySelect.show({
            labelKey: 'belongGroupName',
            idKey: 'belongGroupId',
            title: '所属集团',
          })
        }
        if (type === 'unit') {
          this.$refs.companySelect.show({
            labelKey: 'workUnitName',
            idKey: 'workUnitId',
            title: '工作单位',
          })
        }
      },
      /**
       * @description: 子表单保存回调
       * @param {*} cbData 回调数据
       * @return {*}
       */      
      onSaveSuccess(cbData) {
        if (cbData && cbData.rowItem) {
          const oldArr = this[cbData.key + 'List'] || []
          const id =
            cbData.rowItem[cbData.key === 'gradeId' ? 'id' : cbData.key]
          const i = oldArr.findIndex(
            (x) => x[cbData.key === 'gradeId' ? 'id' : cbData.key] === id
          )
          if (i !== -1) {
            oldArr.splice(i, 1, cbData.rowItem)
          } else oldArr.push(cbData.rowItem)

          if (cbData.key === 'gradeId') {
            this[cbData.key + 'List'] = oldArr.map((x) => {
              const tableData = JSON.parse(x.detailListJson)
              const xObj = {
                item1: tableData[0].score,
                item2: tableData[1].score,
                item3: tableData[2].score,
                ...x,
              }
              return xObj
            })
          } else {
            this[cbData.key + 'List'] = oldArr
          }
        }
      },
      /**
       * @description: 打开新增主办律师信息弹窗
       * @return {*}
       */      
      handleAdd() {
        this.$refs['experienceView'].showEdit('add', null)
      },
      /**
       * @description: 打开新增另行收费法律服务项目工作记录弹窗
       * @return {*}
       */      
      handleAdd2() {
        this.$refs['workView'].showEdit('add', null, {
          organizationName: this.formData.organizationName,
          lawyerIdList: this.lawyerIdList,
        })
      },
      /**
       * @description: 打开新增工作报告弹窗
       * @return {*}
       */      
      handleAdd3() {
        this.$refs['workStatementView'].showEdit('add', null)
      },
      /**
       * @description: 打开新增评价表弹窗
       * @return {*}
       */      
      handleAdd4() {
        let alertText = null
        if (!this.formData.organizationName) {
          alertText = '请输入法律顾问机构名称'
        } else if (!this.formData.employmentTermStartTime) {
          alertText = '请输入聘期开始年月'
        } else if (!this.formData.employmentTermEndTime) {
          alertText = '请输入聘期结束年月'
        }

        if (alertText) {
          return this.$alert(alertText)
        }
        this.$refs['evaluationEdit'].showEdit('new', null, this.formData)
      },
      /**
       * @description: 打开编辑主办律师信息弹窗
       * @return {*}
       */
      async handleEdit(row, title) {
        await this.$refs['experienceView'].showEdit(title, {
          id: row.lawyerId,
        })
      },
      /**
       * @description: 打开编辑另行收费法律服务项目工作记录弹窗
       * @return {*}
       */
      async handleEdit2(row, title) {
        await this.$refs['workView'].showEdit(
          title,
          {
            id: row.workRecordId,
          },
          {
            organizationName: this.formData.organizationName,
            lawyerIdList: this.lawyerIdList,
          }
        )
      },
      /**
       * @description: 打开编辑工作报告弹窗
       * @return {*}
       */
      async handleEdit3(row, title) {
        await this.$refs['workStatementView'].showEdit(title, {
          id: row.workReportId,
        })
      },
      /**
       * @description: 打开编辑评价表弹窗
       * @return {*}
       */
      async handleEdit4(row) {
        let alertText = null
        if (!this.formData.organizationName) {
          alertText = '请输入法律顾问机构名称'
        } else if (!this.formData.employmentTermStartTime) {
          alertText = '请输入聘期开始年月'
        } else if (!this.formData.employmentTermEndTime) {
          alertText = '请输入聘期结束年月'
        }

        if (alertText) {
          return this.$alert(alertText)
        }

        await this.$refs['evaluationEdit'].showEdit(
          'edit',
          {
            id: row.id,
          },
          this.formData
        )
      },
      /**
       * @description: 选择组件回调
       * @param {*} val 已选数据
       * @return {*}
       */      
      getChildlistPro(val) {
        this.$set(this.formData, this.curSelectPersonId, val[0].staffid)
        this.$set(this.formData, this.curSelectPersonName, val[0].realname)
      },
      /**
       * @description: 打开选择组件
       * @param {*} data 
       * @return {*}
       */      
      handleSelectPerson(data) {
        this.curSelectPersonId = data.id
        this.curSelectPersonName = data.name
        this.$refs['manage'].showEdit()
      },
      /**
       * @description: 选择组件回调
       * @param {*} val 已选数据
       * @return {*}
       */      
      handleCompanyTreeSelected(val) {
        this.$set(this.formData, val.idKey, val.id)
        this.$set(this.formData, val.labelKey, val.label)
      },
      /**
       * @description: 删除子表数据
       * @param {*} key 子表类型
       * @param {*} row 选中数
       * @param {*} api 调用的删除接口
       * @return {*}
       */      
      handleDelete(key, row, api) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const res = await fetchApi(api, {
            id: row[key === 'gradeId' ? 'id' : key],
          })
          if (res && res.code === 200) {
            this[key + 'List'].splice(
              this[key + 'List'].findIndex((x) => x[key] === row[key]),
              1
            )
            this.$baseMessage('操作成功', 'success', 'vab-hey-message-success')
          } else {
            this.$baseMessage('操作失败', 'error', 'vab-hey-message-success')
          }
        })
      },
      /**
       * @description: 关闭弹框并清理缓存数据
       * @return {*}
       */      
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.formData = {}
        this.dialogFormVisible = false
        this.allDisabled = false
      }, 
      /**
       * @description: 保存表单
       * @return {*}
       */      
      /**
       * @description: 保存表单
       * @return {*}
       */      
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.formData.state = Number(this.formData.state)
            // this.formData.isAwardOfContract = Number(
            //   this.formData.isAwardOfContract
            // )
            this.formData.lawServiceType = 1

            if (this.formData.hasCooperationExperience === '0') {
              this.formData.pastCooperationExperience = ''
            }

            const idKeys = [
              'lawyerId',
              'workRecordId',
              'workReportId',
              'gradeId',
            ]

            const idKeys2 = ['lawyerId', 'workRecordId', 'workReportId', 'id']

            idKeys.forEach((id, i) => {
              const arr = this[id + 'List']
              if (arr && arr.length) {
                this.formData[id] = arr.map((x) => x[idKeys2[i]]).join(',')
              }
            })

            this.loading = true
            const { createdTime, ...other } = this.formData
            const res = await fetchApi(saveOrUpdate, { ...other })
            this.loading = false
            if (res && res.code === 200) {
              this.$baseMessage('成功', 'success', 'vab-hey-message-success')
              this.$emit('fetch-data')
              this.close()
            }
          }
        })
      },
      /**
       * @description: 格式化日期
       * @param {*} row 当前行
       * @param {*} column 当前列
       * @return {*}
       */      
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
      },
    },
  }
</script>
