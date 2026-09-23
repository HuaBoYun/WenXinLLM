<template>
  <div>
    <el-row :gutter="15">
      <el-form
        ref="form"
        :class="{ disabled: disabled }"
        label-width="125px"
        :model="formData"
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
              :disabled="disabled"
              @change="changeMJ"
              :style="{ width: '100%' }"
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
              :style="{ width: '78%' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.ZXPerson.showEdit(formData.secrectLevelId)"
              :disabled="!formData.secrectLevelId || disabled"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider></el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计人员" prop="realname">
            <el-input
              v-model="formData.realname"
              clearable
              style="width: 180px"
              placeholder="请选择人员"
              disabled
            />
            <el-button
              @click="openPersonModal"
              style="margin-left: 10px"
              type="primary"
              :disabled="disabled"
            >
              选择
            </el-button>
            <el-button
              @click="checkPersonInfo"
              style="margin-left: 10px"
              type="primary"
            >
              人员详情
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计项目名称" prop="auditProjectName">
            <el-input
              v-model="formData.auditProjectName"
              clearable
              style="width: 266px"
              placeholder="请选择项目"
              disabled
            />
            <el-button
              @click="openProjectModal"
              style="margin-left: 10px"
              type="primary"
              :disabled="disabled"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <el-table
      :data="tableData"
      border
      show-summary
      :summary-method="getSummaries"
      style="width: 100%"
    >
      <el-table-column
        align="center"
        prop="project"
        label="项目"
        width="180"
      ></el-table-column>
      <el-table-column align="center" prop="name" label="参考因素">
        <template slot-scope="scope">
          <el-input
            v-model="scope.row.consideration"
            @input="handleInput(scope.$index, scope.row)"
            size="mini"
            style="width: 90%"
            :disabled="disabled"
          />
        </template>
      </el-table-column>
      <el-table-column align="center" prop="amount1" label="评分标准">
        <template slot-scope="scope">
          <el-input
            @input="handleInput(scope.$index, scope.row)"
            v-model="scope.row.grading"
            size="mini"
            style="width: 90%"
            :disabled="disabled"
          />
        </template>
      </el-table-column>
      <el-table-column align="center" prop="amount2" label="审计组长(主审)评分">
        <template slot-scope="scope">
          <el-input
            @input="handleInput(scope.$index, scope.row)"
            v-model="scope.row.auditTeamLeaderScore"
            size="mini"
            style="width: 90%"
            :disabled="disabled"
            type="number"
          />
        </template>
      </el-table-column>
      <el-table-column align="center" prop="amount3" label="备注">
        <template slot-scope="scope">
          <el-input
            @input="handleInput(scope.$index, scope.row)"
            v-model="scope.row.remark"
            size="mini"
            style="width: 90%"
            :disabled="disabled"
          />
        </template>
      </el-table-column>
    </el-table>
    <div style="text-align: right; margin-top: 10px">
      <el-button @click="close()">取 消</el-button>
      <el-button type="primary" @click="submitForm" v-if="!disabled">
        确 定
      </el-button>
      <el-button
        @click="ymsubmit"
        type="primary"
        :disabled="btnLoading"
        v-if="!disabled"
      >
        提交
      </el-button>
    </div>
    <personsModal ref="person" @handlePersonInfo="handlePersonInfo" />
    <projectModal
      ref="project"
      @handleProjectInfo="handleProjectInfo"
      :personId="this.formData"
    />
    <PersonnelCheckModal ref="personCheck" />
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
    createData,
    editPersonData,
    getDefaultPersonInfo,
  } from '@/api/audit/structure'
  import PersonnelCheckModal from '@/views/audit/structure/components/personnelEdit.vue'
  import personsModal from '@/views/audit/structure/components/components/selectPersonModal.vue'
  import projectModal from '@/views/audit/structure/components/components/selectProjectModal.vue'
  import {
    getFlowTaskInfo,
    ymWorkCandidates,
    ymWorkSubmit,
  } from '@/api/contract/manage'
  import { getFaqiInfo } from '@/api/setting/msg'
  import CandidateUserSelect from '@/components/CandidateUserSelect'
  import { getSPMJ } from '@/api/setting/mjsz'
  import { hasMJ, couldMJ } from '@/utils'
  import ZXPerson from '@/components/selectPerson.vue'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  export default {
    name: 'xxxx',
    components: {
      personsModal,
      projectModal,
      PersonnelCheckModal,
      CandidateUserSelect,
      ZXPerson,
      Resubmit,
    },
    data() {
      return {
        title: '新增',
        dialogFormVisible: false,
        disabled: false,
        formData: {
          auditProjectName: '',
          realname: '',
          status: '',
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
        },
        staffid: '',
        disabled: false,
        tableData: [
          {
            project: '参与情况',
            consideration: '',
            grading: '',
            auditTeamLeaderScore: '',
            remark: '',
            staffScore_details_id: '',
          },
          {
            project: '工作态度',
            consideration: '',
            grading: '',
            auditTeamLeaderScore: '',
            remark: '',
            staffScore_details_id: '',
          },
          {
            project: '专业技术',
            consideration: '',
            grading: '',
            auditTeamLeaderScore: '',
            remark: '',
            staffScore_details_id: '',
          },
          {
            project: '敏感性',
            consideration: '',
            grading: '',
            auditTeamLeaderScore: '',
            remark: '',
            staffScore_details_id: '',
          },
          {
            project: '文字能力',
            consideration: '',
            grading: '',
            auditTeamLeaderScore: '',
            remark: '',
            staffScore_details_id: '',
          },
          {
            project: '沟通能力',
            consideration: '',
            grading: '',
            auditTeamLeaderScore: '',
            remark: '',
            staffScore_details_id: '',
          },
        ],
        initialTable: [
          {
            project: '参与情况',
            consideration: '',
            grading: '',
            auditTeamLeaderScore: '',
            remark: '',
            staffScore_details_id: '',
          },
          {
            project: '工作态度',
            consideration: '',
            grading: '',
            auditTeamLeaderScore: '',
            remark: '',
            staffScore_details_id: '',
          },
          {
            project: '专业技术',
            consideration: '',
            grading: '',
            auditTeamLeaderScore: '',
            remark: '',
            staffScore_details_id: '',
          },
          {
            project: '敏感性',
            consideration: '',
            grading: '',
            auditTeamLeaderScore: '',
            remark: '',
            staffScore_details_id: '',
          },
          {
            project: '文字能力',
            consideration: '',
            grading: '',
            auditTeamLeaderScore: '',
            remark: '',
            staffScore_details_id: '',
          },
          {
            project: '沟通能力',
            consideration: '',
            grading: '',
            auditTeamLeaderScore: '',
            remark: '',
            staffScore_details_id: '',
          },
        ],
        totalscore: 0,
        personEditId: undefined,
        //提交
        visible: false,

        fzoptions: [],
        runderList: [],
        candidateData: {},
        flowId: 0,
        fromId: 0,
        fromIdcopy: 0,
        flowtaskinfoflowid: '',
        visible1: false,
        visible2: false,
        formData2: {
          transferStaffName: '',
          transferStaffId: '',
        },
        status: 0,
        jurisdictionCode: 0,
        clearType: false,
        //提交
        ymFromId: 0,
        flowId: 0,
        fromId: 0,
        fromIdcopy: 0,
        flowtaskinfoflowid: '',
        MJoption: [],
        menuId: 0,
        showMJ: false,
        btnLoading: false,
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
      close(value) {
        this.formData = {
          auditProjectName: '',
          realname: '',
          status: '',
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
        }
        this.tableData = []
        this.clearType = true
        this.dialogFormVisible = false
        this.tableData = value
        this.disabled = false
        this.$bus.$emit('updateMsg', 0)
      },
      submitForm() {
        if (this.totalscore > 100) {
          this.$message.error('总分不能大于100')
          return
        }
        const info = {
          ...this.formData,
          detailListJson: JSON.stringify(this.tableData),
          totalScore: this.totalscore,
        }
        if (this.title === '新增') {
          createData({
            ...info,
          }).then((res) => {
            if (res.code == 1) {
              this.$message.success('提交成功')
              this.dialogFormVisible = false
              this.$emit('handleReload')
            }
          })
        } else {
          editPersonData({
            ...info,
            staffScoreid: this.personEditId,
          }).then((res) => {
            if (res.code == 1) {
              this.$message.success('提交成功')
            }
          })
        }
      },
      showEdit(title, formId, flowtaskinfoflowid, ymFromId, status, flowType) {
        // 先重置表单数据和表格数据
        this.formData = {
          auditProjectName: '',
          realname: '',
          status: '',
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
        }
        this.staffid = ''
        this.tableData = JSON.parse(JSON.stringify(this.initialTable))
        
        // 重置表单验证
        this.$nextTick(() => {
          if (this.$refs.form) {
            this.$refs.form.clearValidate()
          }
        })
        
        this.disabled = title === 'detail'
        this.dialogFormVisible = true
        // 流程相关
        this.fromId = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
        // 拿到类型传给getMJData获取审批的密级的下拉数据
        if (flowType) {
          this.getMJData(flowType)
        }
        if (formId) {
          getDefaultPersonInfo({
            staffScoreid: formId,
          }).then(async (res) => {
            this.formData = {
              auditProjectName: res.data.score.auditProjectName,
              realname: res.data.score.auditor.realname,
              status: res.data.score.status,
              secrectLevelId: res.data.score.secrectLevelId,
              staffScopeIds: res.data.score.staffScopeIds,
              staffScopeNames: res.data.score.staffScopeNames,
            }
            if (res.data.score.secrectLevelId) {
              localStorage.setItem(
                'SPsecrectLevelId',
                res.data.score.secrectLevelId
              )
            }
            this.staffid = res.data.score.auditor.staffid
            const data = res.data.score.tblNbsjStaffscoreDetails.map((res) => {
              console.log(res)
              return {
                project: res.project,
                consideration: res.consideration,
                grading: res.grading,
                auditTeamLeaderScore: res.auditTeamLeaderScore,
                remark: res.remark,
                staffScore_details_id: res.staffScore_details_id,
              }
            })
            console.log(data)
            this.tableData = data
          })
        }
      },
      handleInput(a, b) {
        //a是索引
        this.tableData[a] = b
      },
      openPersonModal() {
        this.$refs['person'].showEdit()
      },
      openProjectModal() {
        console.log(this.staffid)
        if (!this.formData.realname) {
          this.$message.error('请先选择人员')
          return
        }
        this.$refs['project'].showEdit(this.staffid)
      },
      handlePersonInfo(v) {
        this.$set(this.formData, `auditors`, v[0].staffid)
        this.$set(this.formData, `realname`, v[0].realname)
        this.staffid = v[0].staffid
      },
      handleProjectInfo(v) {
        this.$set(this.formData, `projectid`, v[0].projectId)
        this.$set(this.formData, `auditProjectName`, v[0].prjoectName)
      },
      getSummaries(param) {
        const { columns, data } = param
        let aaScore = 0
        for (let i = 0; i < data.length; i++) {
          aaScore += +data[i].auditTeamLeaderScore
        }
        this.totalscore = aaScore
        const sums = []
        columns.forEach((column, index) => {
          if (index === 0) {
            sums[index] = '合计'
            return
          }
          if (index === 1) {
            sums[index] = '/'
            return
          }
          if (index === 2) {
            sums[index] = '/'
            return
          }
          if (index === 3) {
            sums[index] = this.totalscore + '分'
            return
          }
          if (index === 4) {
            sums[index] = '/'
            return
          }
        })
        return sums
      },
      checkPersonInfo() {
        const info = { staffid: this.staffid }
        this.$refs['personCheck'].showEdit(info, '查看')
      },

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
    },
  }
</script>

<style scoped>
  ::v-deep input::-webkit-outer-spin-button,
  ::v-deep input::-webkit-inner-spin-button {
    -webkit-appearance: none !important;
  }
  ::v-deep input[type='number'] {
    -moz-appearance: textfield;
  }
</style>
