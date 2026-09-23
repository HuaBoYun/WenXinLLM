<template>
  <div>
    <el-dialog
      v-if="dialogFormVisible"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close(initialTable)"
      :close-on-click-modal="false"
    >
      <el-row :gutter="15">
        <el-form
          ref="form"
          :class="{ disabled: disabled }"
          label-width="125px"
          :model="formData"
        >
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
        <el-table-column
          align="center"
          prop="amount2"
          label="审计组长(主审)评分"
        >
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
      <!--  -->
      <template v-if="!disabled" #footer>
        <el-button @click="close(initialTable)">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
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
    <personsModal ref="person" @handlePersonInfo="handlePersonInfo" />
    <projectModal
      ref="project"
      @handleProjectInfo="handleProjectInfo"
      :personId="this.formData"
    />
    <PersonnelCheckModal ref="personCheck" />
    <!-- 提交 -->
    <el-dialog
      @close="currentClose"
      title="选择分支"
      :visible="visible"
      :append-to-body="true"
      :close-on-click-modal="false"
    >
      <el-form
        label-width="100px"
        ref="fzforms"
        :modal="fzforms"
        :rules="fzRules"
      >
        <el-form-item label="分支选择" prop="branchStrs">
          <el-select
            style="width: 100%"
            v-model="fzforms.branchStrs"
            @change="handlefzChange"
            multiple
          >
            <el-option
              v-for="item in fzoptions"
              :label="item.nodeName"
              :value="item.nodeId"
              :key="item.nodeId"
            ></el-option>
          </el-select>
        </el-form-item>
        <div v-for="(item, index) in this.runderList" :key="item.value">
          <el-form-item
            :label="item.label"
            prop="transferStaffName"
            v-if="item.hasCandidates"
          >
            <!-- <el-input
              disabled
              placeholder="请选择候选人"
              v-model="formData3[index].transferStaffName"
              style="width: 79%; margin-right: 8px"
            ></el-input>
            <el-button type="primary" @click="handleSelectNew(item, index)">
              请选择
            </el-button> -->
            <CandidateUserSelect
              :clearType="clearType"
              @selected="handleCandSelect"
              :index="index"
              :nodeId="item.nodeId"
              :candidateData="candidateData"
              multiple
              placeholder="请选择候选人"
            />
          </el-form-item>
        </div>
      </el-form>
      <div slot="footer">
        <el-button @click="currentClose">取消</el-button>
        <el-button type="primary" @click="save4">提交</el-button>
      </div>
    </el-dialog>
    <el-dialog
      @close="close1"
      title="选择分支"
      :visible="visible1"
      :append-to-body="true"
      :close-on-click-modal="false"
      v-if="visible1"
    >
      <el-form
        :modal="fzform1"
        label-width="100px"
        ref="fzform1"
        :rules="fzRules1"
      >
        <el-form-item label="分支选择" prop="branchStrs">
          <el-select
            style="width: 100%"
            v-model="fzform1.branchStrs"
            @change="selectValue"
            multiple
          >
            <el-option
              v-for="item in fzoptions"
              :key="item.nodeId"
              :label="item.nodeName"
              :value="item.nodeId"
            ></el-option>
          </el-select>
        </el-form-item>
        <div v-for="(item, index) in this.runderList" :key="item.value">
          <el-form-item
            :label="item.label"
            prop="transferStaffName"
            v-if="item.hasCandidates"
          >
            <!-- <el-input
              disabled
              placeholder="请选择候选人"
              v-model="formData3[index].transferStaffName"
              style="width: 79%; margin-right: 8px"
            ></el-input>
            <el-button type="primary" @click="handleSelectNew(item, index)">
              请选择
            </el-button> -->
            <CandidateUserSelect
              :clearType="clearType"
              @selected="handleCandSelect"
              :index="index"
              :nodeId="item.nodeId"
              :candidateData="candidateData"
              multiple
              placeholder="请选择候选人"
            />
          </el-form-item>
        </div>
      </el-form>
      <div slot="footer">
        <el-button @click="close1">取消</el-button>
        <el-button type="primary" @click="save1">提交</el-button>
      </div>
    </el-dialog>
    <el-dialog
      title="选择候选人"
      :visible.sync="visible2"
      :close-on-click-modal="false"
      width="40%"
      :modal="false"
      @close="close2"
      v-if="visible2"
    >
      <el-form
        :model="formData2"
        :rules="rules2"
        ref="ruleForm2"
        label-width="80px"
      >
        <el-form-item label="候选人" prop="transferStaffName">
          <!-- <el-input
            disabled
            placeholder="请选择候选人"
            v-model="formData2.transferStaffName"
            style="width: 79%; margin-right: 8px"
          ></el-input>
          <el-button type="primary" @click="handleSelect">请选择</el-button> -->
          <CandidateUserSelect
            :clearType="clearType"
            @selected="handleCandSelect1"
            :index="0"
            :nodeId="candidateData.nodeId"
            :candidateData="candidateData"
            multiple
            placeholder="请选择候选人"
          />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="close2">取 消</el-button>
        <el-button type="primary" @click="save2">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
  import {
    createData,
    editPersonData,
    getDefaultPersonInfo,
  } from '@/oapi/audit/structure'
  import PersonnelCheckModal from '@/views/oilAudit/structure/components/personnelEdit.vue'
  import personsModal from './components/selectPersonModal.vue'
  import projectModal from './components/selectProjectModal.vue'
  import {
    getFlowTaskInfo,
    ymWorkCandidates,
    ymWorkSubmit,
  } from '@/oapi/contract/manage'
  import { getFaqiInfo } from '@/oapi/setting/msg'
  import CandidateUserSelect from '@/components/CandidateUserSelect'

  export default {
    name: 'xxxx',
    components: {
      personsModal,
      projectModal,
      PersonnelCheckModal,
      CandidateUserSelect,
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
        fzforms: {
          branchStrs: [],
        },
        fzRules: {
          branchStrs: [
            {
              required: true,
              message: '请选择分支',
              trigger: 'blur',
            },
          ],
        },
        fzform1: {
          branchStrs: [],
        },
        fzRules1: {
          branchStrs: [
            {
              required: true,
              message: '请选择分支',
              trigger: 'blur',
            },
          ],
        },
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
      }
    },
    methods: {
      close(value) {
        this.$refs['form'].resetFields()
        this.clearType = true
        this.$emit('handleReload')
        this.dialogFormVisible = false
        this.tableData = value
        this.disabled = false
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
              this.dialogFormVisible = false
              this.$emit('handleReload')
            }
          })
        }
      },
      showEdit(a, b) {
        this.dialogFormVisible = true
        if (b) {
          this.personEditId = b.staffScoreid
          getDefaultPersonInfo({
            staffScoreid: b.staffScoreid,
          }).then(async (res) => {
            // if (res.data.score.status == 2 || res.data.score.status == 3) {
            //   const res2 = await getFlowTaskInfo({
            //     tableId: 15,
            //     formId: b.staffScoreid,
            //   })
            //   this.jurisdictionCode = res2.data.isFlowInfo
            //   if (res2.data.isFlowInfo) {
            //     this.flowtaskinfoflowid = res2.data.flowId
            //     this.fromId = b.staffScoreid
            //     this.ymFromId = res2.data.id

            //     const res3 = await getFaqiInfo({
            //       id: res2.data.id,
            //       flowId: res2.data.flowId,
            //     })
            //     if (res3.code == 1) {
            //       this.status = res3.data.dataJson.flowTaskInfo.status
            //     }
            //   }
            // }

            this.formData = {
              auditProjectName: res.data.score.auditProjectName,
              realname: res.data.score.auditor.realname,
              status: res.data.score.status,
            }
            this.staffid = res.data.score.auditor.staffid
            const data = res.data.score.tblNbsjStaffscoreDetails.map((res) => {
              return {
                project: res.project,
                consideration: res.consideration,
                grading: res.grading,
                auditTeamLeaderScore: res.auditTeamLeaderScore,
                remark: res.remark,
                staffScore_details_id: res.staffScore_details_id,
              }
            })
            this.tableData = data
          })
        }
        if (a === '新增') {
          this.title = '新增'
          this.$refs['form'].resetFields()
          this.tableData = value
          his.disabled = false
        }
        if (a === '编辑') {
          this.title = '编辑'
          this.disabled = false
        } else {
          this.title = '查看'
          this.disabled = true
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
        if (!this.formData.realname) {
          this.$message.error('请先选择人员')
          return
        }
        this.$refs['project'].showEdit()
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
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const res = await ymWorkCandidates({
              flowId: this.flowtaskinfoflowid,
              fromId: this.fromId,
              flowTaskOperatorId: '',
              id: '',
            })
            this.candidateType = res.data.candidateType
            if (res.data.candidateType == 1) {
              this.fzoptions = res.data.list
              this.visible = true
              let list = []
              res.data.list.map((item) => {
                list.push({
                  value: item.nodeId,
                  label: item.nodeName,
                  hasCandidates: item.hasCandidates,
                })
              })
              this.options = list
              //保存请求人员列表的信息
              let candidateData = {
                // tableId: tableId,
                fromId: this.fromId,
              }
              this.candidateData = candidateData
            } else if (res.data.candidateType == 2) {
              let candidateData = {
                // tableId: tableId,
                fromId: this.fromId,
                nodeId: res.data.list[0].nodeId,
              }
              this.candidateData = candidateData
              this.visible2 = true
            } else {
              const wordres = await ymWorkSubmit({
                flowId: this.flowtaskinfoflowid,
                fromId: this.fromId,
                branchStrs: this.fzforms.branchStrs
                  ? this.fzforms.branchStrs.join(',')
                  : '',
                candidateType: res.data.candidateType,
                ymFromId: this.fromIdcopy == -1 ? '' : this.ymFromId,
                status: this.status,
              })
              if (wordres.code === 1) {
                this.$message.success(wordres.msg)
                this.close()
                this.visible = false
              }
            }
          }
        })
      },
      currentClose() {
        this.visible = false
        this.form = {}
      },
      handlefzChange(e) {
        this.$refs['fzform'].clearValidate()
      },
      handleCandSelect1(index, value) {
        this.formData2.transferStaffName = value
      },
      handleCandSelect(index, value) {
        // this.$set(this.formData3[index], 'transferStaffId', value)
        this.formData3[index].transferStaffId = value
      },
      async save4() {
        if (this.fzforms.branchStrs.length == 0) {
          this.$message.warning('请选择分支')
          return
        }
        let arr = []
        if (this.formData3.length > 0) {
          this.formData3.map((res) => {
            let str = []
            res.transferStaffId.map((item) => {
              str.push(item.id)
            })
            arr.push(str)
          })
        }

        let list = []
        arr.map((item) => {
          let str = item.join(',')
          if (str) {
            list.push(str)
          }
        })
        let candidateList = list.join('~')

        const { data, code } = await ymWorkSubmit({
          // tableId: this.tableId,
          flowId: this.flowtaskinfoflowid,
          fromId: this.fromId,
          branchStrs: this.fzforms.branchStrs
            ? this.fzforms.branchStrs.join(',')
            : '',
          candidateList: candidateList || '',
          ymFromId: this.fromIdcopy == -1 ? '' : this.ymFromId,
          candidateType: this.candidateType,
          status: this.status,
        })
        if (code == 1) {
          this.$message.success('提交成功')
          this.close4()
          this.close()
        }
        // } else {
        //
        //   return false
        // }
        // })
      },
      selectValue(e) {
        let arr = []
        this.fzoptions.forEach((res) => {
          if (res.nodeId == e.split('~')[0]) {
            arr.push(res)
          }
        })
        this.runderList = arr
        this.formData3 = arr.map(() => {
          return { transferStaffName: '', transferStaffId: '' }
        })
      },

      resetINfo() {
        this.flowId = ''
        this.fromId = ''
        this.runderList = []
        this.formData = {
          value: [],
        }
        this.formData2 = {
          transferStaffName: '',
          transferStaffId: '',
        }
      },
      async save1() {
        let branchStrs = ''
        this.fzform1.branchStrs.map((item) => {
          branchStrs = branchStrs + item + ','
        })
        branchStrs = branchStrs.substring(0, branchStrs.length - 1)
        let arr = []
        if (this.formData3.length > 0) {
          this.formData3 &&
            this.formData3.map((res) => {
              let str = []
              res.transferStaffId &&
                res.transferStaffId.map((item) => {
                  str.push(item.id)
                })
              arr.push(str)
            })
        }

        let list = []
        arr.map((item) => {
          let str = item.join(',')
          if (str) {
            list.push(str)
          }
        })
        let candidateList = list.join('~')

        const { data, code } = await ymWorkSubmit({
          flowId: this.flowtaskinfoflowid,
          fromId: this.fromId,
          branchStrs,
          candidateType: this.candidateType,
          candidateList: candidateList || '',
          ymFromId: this.fromIdcopy == -1 ? '' : this.ymFromId,
          status: this.status,
        })
        if (code == 1) {
          this.$message.success('提交成功')
          this.close()
          this.visible1 = false
        }
      },
      close1() {
        this.visible1 = false
        this.resetINfo()
      },
      close2() {
        this.visible2 = false
        this.resetINfo()
      },
      async save2() {
        if (!this.formData2.transferStaffName) {
          this.$message.error('请选择候选人')
          return
        }

        let list = []
        this.formData2.transferStaffName.map((item) => {
          list.push(item.id)
        })
        const { data, code } = await ymWorkSubmit({
          flowId: this.flowtaskinfoflowid,
          fromId: this.fromId,
          candidateList: list.join(','),
          nodeCode: this.candidateData.nodeId,
          candidateType: this.candidateType,
          status: this.status,
        })
        if (code == 1) {
          this.$message.success('提交成功')
          this.close2()
          this.close()
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
