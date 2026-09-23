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
          :disabled="addDisabled"
        >
          <el-col :span="12">
            <el-form-item label="所属集团" prop="belongGroupName">
              <el-input
                v-model.trim="formData.belongGroupName"
                placeholder="请选择所属集团"
                disabled
              />
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

          <el-col :span="12">
            <el-form-item label="服务团队名称" prop="serviceTeamName">
              <el-input
                v-model="formData.serviceTeamName"
                clearable
                placeholder="请输入服务团队名称"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="团队负责人" prop="leaderName">
              <el-input
                v-model="formData.leaderName"
                clearable
                placeholder="请输入团队负责人"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="联系方式" prop="leaderContact">
              <el-input
                v-model="formData.leaderContact"
                clearable
                placeholder="请输入联系方式"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="服务项目类型" prop="serviceItemType">
              <el-select
                v-model="formData.serviceItemType"
                :style="{ width: '100%' }"
                placeholder="请选择服务项目类型"
                :disabled="!footer"
              >
                <el-option label="基金及股权投资" :value="1" />
                <el-option label="债权投资（含融资租赁等业务）" :value="2" />
                <el-option label="不良资产投资" :value="3" />
                <el-option
                  label="专业产业投资（含盐业及不动产领域等）"
                  :value="4"
                />
                <el-option label="诉讼代理" :value="5" />
              </el-select>
            </el-form-item>
          </el-col>

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

          <!-- <el-col :span="24" style="margin-top: 20px">
            <el-divider>工作记录</el-divider>
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
              />
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
              />
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
                  <el-button type="text" @click="handleEdit2(row)">
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
          </el-col> -->

          <el-col :span="24" style="margin-top: 20px">
            <el-divider>服务登记</el-divider>
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
                label="服务项目"
                prop="serviceProject"
              >
                <template #default="{ row }">
                  <el-button
                    type="text"
                    @click="handleEdit3(row, 'detail')"
                    :disabled="false"
                  >
                    {{ row.serviceProject }}
                  </el-button>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="服务方式"
                prop="isCollaboration"
              >
                <template #default="{ row }">
                  {{ row.isCollaboration == 1 ? '提供服务' : '参与报价' }}
                </template>
              </el-table-column>
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
            <el-divider>考核表</el-divider>
          </el-col>
          <el-col :span="24">
            <div style="text-align: right; margin-bottom: 5px" v-if="footer">
              <el-button type="success" @click="handleAdd4">新建</el-button>
            </div>
          </el-col>
          <el-col :span="24">
            <el-table :data="examineIdList">
              <el-table-column align="center" label="序号" type="index" />
              <el-table-column align="center" label="团队名称" prop="teamName">
                <template #default="{ row }">
                  <el-button
                    type="text"
                    @click="handleEdit4(row, 'detail')"
                    :disabled="false"
                  >
                    {{ row.teamName }}
                  </el-button>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="有无合作"
                prop="isCollaboration"
              />
              <el-table-column
                align="center"
                label="委托主体"
                prop="entrustSubject"
              />
              <el-table-column
                align="center"
                label="服务项目"
                prop="serviceProject"
              />
              <el-table-column
                align="center"
                label="专业能力（60分）"
                prop="professionalAbilityGrade"
              />
              <el-table-column
                align="center"
                label="响应效率（30分）"
                prop="reactionEfficiencyGrade"
              />
              <el-table-column
                align="center"
                label="增值服务（10分）"
                prop="appreciationServiceGrade"
              />
              <el-table-column
                align="center"
                label="合计"
                prop="total"
              ></el-table-column>
              <el-table-column
                align="center"
                label="平均分"
                prop="average"
              ></el-table-column>
              <el-table-column align="center" label="备注" prop="remark" />
              <el-table-column
                align="center"
                label="操作"
                show-overflow-tooltip
                width="120"
              >
                <template #default="{ row }">
                  <el-button type="text" @click="handleEdit4(row, 'edit')">
                    编辑
                  </el-button>
                  <el-button
                    type="text"
                    @click="
                      handleDelete(
                        'examineId',
                        row,
                        legalServiceExamineDoDelete
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
        </el-form>
      </el-row>
      <template #footer>
        <el-button @click="close">关 闭</el-button>
        <el-button @click="save" type="primary" v-if="!addDisabled">
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
    <assessView ref="assessView" @on-save-success="onSaveSuccess" />

    <serveView ref="serveView" @on-save-success="onSaveSuccess" />
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
  import assessView from './assessView.vue'
  import serveView from './serveView.vue'
  import CompanySelectModal from '@/components/CampanySelectModal'
  import projectManage from '@/views/audit/project/components/formComponents/projectManage.vue'
  import {
    legalService,
    fetchApi,
    legalServiceLawyer,
    legalServiceWorkRecord,
    legalServiceWorkReport,
    legalServiceExamine,
  } from '@/api/fwgl/api'

  const { saveOrUpdate, detail } = legalService
  const { doDelete: legalServiceLawyerDoDelete } = legalServiceLawyer
  const { doDelete: legalServiceWorkRecordDoDelete } = legalServiceWorkRecord
  const { doDelete: legalServiceWorkReportDoDelete } = legalServiceWorkReport
  const { doDelete: legalServiceExamineDoDelete } = legalServiceExamine

  let _that = null

  export default {
    props: [],
    components: {
      experienceView,
      workView,
      workStatementView,
      assessView,
      serveView,
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
        examineIdList: [], // 考核
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
          serviceTeamName: [
            {
              required: true,
              message: '请输入服务团队名称',
              trigger: 'blur',
            },
          ],
          serviceItemType: [
            {
              required: true,
              message: '请选择服务项目类型',
              trigger: 'blur',
            },
          ],
        },
        legalServiceLawyerDoDelete: legalServiceLawyerDoDelete,
        legalServiceWorkRecordDoDelete: legalServiceWorkRecordDoDelete,
        legalServiceWorkReportDoDelete: legalServiceWorkReportDoDelete,
        legalServiceExamineDoDelete: legalServiceExamineDoDelete,
        addDisabled: false,
      }
    },
    mounted() {
      _that = this
    },
    methods: {
      /**
       * @description: 外部打开dialog
       * @param {*} title 类型
       * @param {*} row 行数据
       * @return {*}
       */      
      async showEdit(title, row) {
        const userInfo = JSON.parse(localStorage.getItem('userInfo'))
        this.dialogFormVisible = true
        this.footer = true
        this.lawyerIdList = []
        this.workRecordIdList = []
        this.workReportIdList = []
        this.examineIdList = []
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
          this.addDisabled = true
        } else {
          this.title = '新增'

          this.formData.creatorName = userInfo.realname
          this.formData.createdTime = new Date()
        }
        if (row) {
          this.loading = true
          const res = await fetchApi(detail, { id: row.id })
          this.loading = false
          if (res && res.code == 200 && res.data) {
            this.formData = res.data.lawService
            this.formData.belongGroupName = userInfo.currentOrg.orgname
            this.formData.isAwardOfContract = res.data.lawService
              .isAwardOfContract
              ? res.data.lawService.isAwardOfContract.toString()
              : ''
            this.formData.employmentTermType = res.data.lawService
              .employmentTermType
              ? res.data.lawService.employmentTermType.toString()
              : ''
            this.lawyerIdList = res.data.lawServiceLawyer

            this.workRecordIdList = res.data.lawServiceWorkRecord
            this.workReportIdList = res.data.lawServiceWorkReport
            this.examineIdList = res.data.lawServiceExamine
          }
        }

        // this.formData.fillInPersonId = userInfo.staffid
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
          const id = cbData.rowItem[cbData.key]
          const i = oldArr.findIndex((x) => x[cbData.key] === id)
          if (i !== -1) {
            oldArr.splice(i, 1, cbData.rowItem)
          } else oldArr.push(cbData.rowItem)
          this[cbData.key + 'List'] = oldArr
        }
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
      handleSelectPerson(data) {
        this.curSelectPersonId = data.id
        this.curSelectPersonName = data.name
        this.$refs['manage'].showEdit()
      },
      /**
       * @description: 公司部门选择回调
       * @param {*} val 已选数据
       * @return {*}
       */      
      handleCompanyTreeSelected(val) {
        this.$set(this.formData, val.idKey, val.id)
        this.$set(this.formData, val.labelKey, val.label)
      },
      /**
       * @description: 打开新增主办律师信息弹框
       * @return {*}
       */      
      handleAdd() {
        this.$refs['experienceView'].showEdit('add', null)
      },
      /**
       * @description: 打开新增工作记录弹框
       * @return {*}
       */      
      handleAdd2() {
        this.$refs['workView'].showEdit('add', null)
      },
      /**
       * @description: 打开新增服务登记弹框
       * @return {*}
       */      
      handleAdd3() {
        this.$refs['serveView'].showEdit('add', null)
      },
      /**
       * @description: 打开新增考核表弹框
       * @return {*}
       */      
      handleAdd4() {
        this.$refs['assessView'].showEdit('add', null, {
          workUnitName: this.formData.workUnitName,
          serviceTeamName: this.formData.serviceTeamName,
          serviceItemType: this.formData.serviceItemType,
        })
      },
      /**
       * @description: 打开编辑主办律师信息弹框
       * @return {*}
       */      
      async handleEdit(row, title) {
        await this.$refs['experienceView'].showEdit(title, {
          id: row.lawyerId,
        })
      },
      /**
       * @description: 打开编辑工作记录弹框
       * @return {*}
       */      
      async handleEdit2(row) {
        await this.$refs['workView'].showEdit('edit', {
          id: row.workRecordId,
        })
      },
      /**
       * @description: 打开编辑服务登记弹框
       * @return {*}
       */      
      async handleEdit3(row, title) {
        await this.$refs['serveView'].showEdit(title, {
          id: row.workReportId,
        })
      },
      /**
       * @description: 打开编辑考核表弹框
       * @return {*}
       */      
      async handleEdit4(row, title) {
        await this.$refs['assessView'].showEdit(title, {
          id: row.examineId,
        })
      },
      /**
       * @description: 删除子表数据
       * @param {*} key 子表类型
       * @param {*} row 当前选择数据
       * @param {*} api 删除接口
       * @return {*}
       */      
      handleDelete(key, row, api) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const res = await fetchApi(api, { id: row[key] })
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
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.formData = {}
        this.dialogFormVisible = false
        this.addDisabled = false
      },
      /**
       * @description: 保存表单
       * @return {*}
       */      
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.formData.state = Number(this.formData.state)
            this.formData.isAwardOfContract = Number(
              this.formData.isAwardOfContract
            )
            this.formData.lawServiceType = 2

            const idKeys = [
              'lawyerId',
              'workRecordId',
              'workReportId',
              'examineId',
            ]
            idKeys.forEach((id) => {
              const arr = this[id + 'List']
              if (arr && arr.length) {
                this.formData[id] = arr.map((x) => x[id]).join(',')
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
    },
  }
</script>
