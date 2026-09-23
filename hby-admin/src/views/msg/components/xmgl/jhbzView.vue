<template>
  <div>
    <el-row :gutter="15">
      <el-form
        ref="form"
        :class="{ disabled: disabled }"
        label-width="140px"
        :model="formData"
        :rules="rules"
        size="medium"
        v-loading="showLoading"
        :disabled="disabled"
      >
        <el-col :span="12">
          <el-form-item label="立项单位2">
            <el-input
              v-model.trim="formData.planname"
              placeholder="请选择立项单位"
              :style="{ width: '75%' }"
              disabled
            />
            <el-button type="primary" style="margin-left: 10px">选择</el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="编制人" prop="plantype">
            <el-input
              v-model="formData.plantype"
              clearable
              placeholder="请输入编制人"
              :style="{ width: '100%' }"
              :disabled="disabled"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被审计单位" prop="planname">
            <el-input
              v-model.trim="formData.planname"
              placeholder="请输入被审计单位"
              :style="{ width: '75%' }"
              disabled
            />
            <el-button type="primary" style="margin-left: 10px">选择</el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间" prop="palnyear">
            <el-date-picker
              v-model="formData.palnyear"
              type="month"
              :style="{ width: '100%' }"
              placeholder="请选择创建时间"
              value-format="yyyy"
              :disabled="disabled"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="实施类型" prop="plantype">
            <el-select
              v-model="formData.type"
              placeholder="请选择实施类型"
              :style="{ width: '100%' }"
            >
              <el-option label="实施1" value="1" />
              <el-option label="实施2" value="2" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="境外项目" prop="plantype">
            <el-select
              v-model="formData.type"
              placeholder="请选择境外项目"
              :style="{ width: '100%' }"
            >
              <el-option label="是" value="1" />
              <el-option label="否" value="2" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="实施审计机构" prop="planname">
            <el-input
              v-model.trim="formData.planname"
              placeholder="请输入实施审计机构"
              :style="{ width: '75%' }"
              disabled
            />
            <el-button type="primary" style="margin-left: 10px">选择</el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目年度" prop="palnyear">
            <el-date-picker
              v-model="formData.palnyear"
              type="month"
              :style="{ width: '100%' }"
              placeholder="请选择项目年度"
              value-format="yyyy"
              :disabled="disabled"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目负责处（科）室" prop="planname">
            <el-input
              v-model.trim="formData.planname"
              placeholder="请输入项目负责处（科）室"
              :style="{ width: '75%' }"
              disabled
            />
            <el-button type="primary" style="margin-left: 10px">选择</el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划实施月份" prop="palnyear">
            <el-date-picker
              v-model="formData.palnyear"
              type="month"
              :style="{ width: '100%' }"
              placeholder="请选择计划实施月份"
              value-format="yyyy"
              :disabled="disabled"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计项目类型" prop="plantype">
            <el-select
              v-model="formData.type"
              placeholder="请选择审计项目类型"
              :style="{ width: '100%' }"
            >
              <el-option label="实施1" value="1" />
              <el-option label="实施2" value="2" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="立项依据" prop="plantype">
            <el-select
              v-model="formData.type"
              placeholder="请选择立项依据"
              :style="{ width: '100%' }"
            >
              <el-option label="实施1" value="1" />
              <el-option label="实施2" value="2" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计项目名称" prop="plantype">
            <el-input
              v-model="formData.plantype"
              clearable
              placeholder="请输入计划投入人日"
              :style="{ width: '100%' }"
              :disabled="disabled"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划类型" prop="plantype">
            <el-select
              v-model="formData.type"
              placeholder="请选择立项依据"
              :style="{ width: '100%' }"
            >
              <el-option label="实施1" value="1" />
              <el-option label="实施2" value="2" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划投入人日" prop="plantype">
            <el-input
              v-model="formData.plantype"
              clearable
              placeholder="请输入计划投入人日"
              :style="{ width: '100%' }"
              :disabled="disabled"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="是否对全部经营活动审计" prop="plantype">
            <el-select
              v-model="formData.type"
              placeholder="请选择立项依据"
              :style="{ width: '100%' }"
            >
              <el-option label="实施1" value="1" />
              <el-option label="实施2" value="2" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="变更原因" prop="planname">
            <el-input
              v-model.trim="formData.planname"
              placeholder="请输入变更原因"
              :style="{ width: '100%' }"
              disabled
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>{{ this.formData.plantype }}项目</el-divider>
        </el-col>
        <el-col :span="24">
          <div v-if="!disabled" style="text-align: right; margin-bottom: 5px">
            <el-button type="success" @click="handleAdd">增加一行</el-button>
            <!-- <el-button type="success" @click="handleDelCheck">删除</el-button> -->
          </div>
          <!-- 新增可编辑表格 -->
          <el-table
            border
            :data="tableDataProject"
            style="width: 100%; margin-bottom: 25px"
            :rules="rulesProject"
            :disabled="disabled"
          >
            <el-table-column
              align="center"
              label="项目编号"
              prop="projectCode"
              :render-header="addRedStar"
            ></el-table-column>
            <el-table-column
              align="center"
              label="项目名称"
              prop="prjoectName"
              :render-header="addRedStar"
            >
              <template slot-scope="scope">
                <el-input
                  v-show="scope.row.show"
                  v-model="scope.row.prjoectName"
                  size="mini"
                  style="width: 90%"
                  :disabled="disabled"
                />
                <span v-show="!scope.row.show">
                  {{ scope.row.prjoectName }}
                </span>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="被审计对象"
              prop="orgIdNames"
              min-width="180px"
            ></el-table-column>
            <!-- <el-table-column
              align="center"
              label="计划开始时间"
              prop="platformName"
              min-width="180px"
            >
              <template slot-scope="scope">
                {{ UTCformat(scope.row.startDate) }}
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="计划结束时间"
              prop="platformName"
              min-width="180px"
            >
              <template slot-scope="scope">
                {{ UTCformat(scope.row.endDate) }}
              </template>
            </el-table-column> -->
            <el-table-column
              align="center"
              label="时间安排"
              prop="sjap"
              min-width="180px"
            ></el-table-column>
            <el-table-column
              align="center"
              label="项目概述"
              prop="xmgs"
              min-width="180px"
            ></el-table-column>

            <!-- <el-table-column align="center" label="是否外委" min-width="130">
              <template slot-scope="scope">
                {{ scope.row.externAlassig === 1 ? '是' : '否' }}
              </template>
            </el-table-column> -->
            <!-- <el-table-column align="center" label="操作" min-width="130">
              <template>
                <el-button type="primary" disabled>终止</el-button>
              </template>
            </el-table-column> -->

            <el-table-column
              v-if="!disabled"
              align="center"
              label="操作"
              min-width="110"
              prop=""
            >
              <template slot-scope="scope">
                <el-button
                  type="text"
                  @click="handleDelete(scope.$index, scope.row)"
                >
                  删除
                </el-button>

                <!-- <el-button type="text" @click="scope.row.show = true">
                  编辑
                </el-button>

                <el-button type="text" @click="saveProject(scope.row)">
                  保存
                </el-button> -->
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <el-upload
            style="text-align: right; margin-bottom: 5px"
            class="upload-demo"
            :action="baseApi + api"
            :on-success="handleSuccess"
            :show-file-list="false"
            :headers="headers"
            multiple
            :file-list="fileList"
            :before-upload="handleBeforeUpload"
          >
            <div v-if="!disabled || isOnwer" style="margin-right: 10px">
              <el-button type="success">点击上传</el-button>
            </div>
          </el-upload>
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
              <template slot-scope="scope">
                <el-button
                  :disabled="false"
                  type="text"
                  @click="handleDown(scope.row)"
                >
                  下载
                </el-button>
                <el-button
                  v-if="!disabled || isOnwer"
                  type="text"
                  @click="handleDeleteFile(scope.$index, scope.row)"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>

    <!-- <div style="text-align: right; margin-top: 10px" v-if="!disabled">
      <el-button @click="close" v-if="!isedit">取 消</el-button>
      <el-button @click="close" v-if="isedit">关 闭</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div> -->
    <!-- </el-dialog> -->
    <!-- <plan-leader ref="plan" @planList="planList"></plan-leader> -->
    <Company ref="auditee" @submit="auditee"></Company>
    <!-- 选择组长组员子组件 -->
    <!-- <selectTeam ref="select" @selectTeamList="selectTeamList" /> -->
    <select-team ref="select" @selectTeamList="selectTeamList"></select-team>

    <div style="text-align: right; margin-top: 10px" v-if="!disabled">
      <el-button @click="save" type="primary">确定</el-button>
      <el-button @click="ymsubmit" type="primary">提交</el-button>
    </div>
    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />

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
    <!-- <CandidateListNew ref="candidateListNew" @selected="handSelectedNew1" /> -->
    <CandidateList ref="candidateList" @selected="handSelected" />
  </div>
</template>

<script>
  import {
    saveContract,
    ymWorkCandidates,
    ymWorkSubmit,
  } from '@/oapi/contract/manage'
  import CandidateUserSelect from '@/components/CandidateUserSelect.vue'
  import CandidateList from '@/components/CandidateList'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'

  import {
    deleteFileById,
    getAuditPlanAttInfo,
    getAuditPlanViewDetail,
    getPlanProjectListByPlanId,
    mergeAuditPlanInfo,
    mergePlanProjectManageInfoList,
    removePlanProjectInfo,
  } from '@/oapi/audit/plan'
  import { UTCformat } from '@/utils'
  // import PlanLeader from './childCom/PlanLeader.vue'
  import { download } from '@/oapi/audit/implement'
  import store from '@/store'
  const { baseURL } = require('@/config')

  export default {
    name: 'IndexEdit',
    components: {
      // PlanLeader,
      Company: () => import('@/components/Company.vue'),

      selectTeam: () =>
        import(
          '@/views/audit/project/components/formComponents/selectTeam.vue'
        ),
      CandidateUserSelect,
      CandidateList,
      Resubmit,
    },
    data() {
      return {
        baseApi: baseURL,
        api: '/audit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        disabled: false,
        value: '',
        planRow: {},
        formData: {
          plancode: '',
          planname: undefined,
          palnyear: undefined,
          plantype: undefined,
          palncost: undefined,
          spanDate: [],
          realname: '',
          isauditor: '',
          organizationName: '',
          createtime: '',
          orgname: '',
          remarks: '',
        },
        UTCformat,
        fileList: [],
        planid: '',
        createPerson: '',
        itemKey: '',
        rules: {
          plancode: [
            { required: true, message: '请输入审计项目名称', trigger: 'blur' },
          ],
          palnyear: [
            { required: true, message: '请输入计划年度', trigger: 'blur' },
          ],
          plantype: [
            { required: true, message: '请输入计划类别', trigger: 'blur' },
          ],
          // spanDate: [
          //   {
          //     required: true,
          //     message: '请选择计划时间',
          //     trigger: 'change',
          //   },
          // ],
          realname: [
            { required: true, message: '请选择计划负责人', trigger: 'blur' },
          ],
          isauditor: [
            { required: true, message: '请选择是否外审', trigger: 'blur' },
          ],
          orgname: [
            { required: true, message: '请输入审计单位', trigger: 'blur' },
          ],
          remarks: [
            { required: true, message: '请输入活动名称', trigger: 'blur' },
          ],
        },
        rulesProject: {
          projectname: [
            { required: true, message: '请输入项目名称', trigger: 'blur' },
          ],
          targetname: [
            { required: true, message: '请输入工作目标', trigger: 'change' },
          ],
        },
        fieldOptions: [
          {
            label: '是',
            value: 1,
          },
          {
            label: '否',
            value: 0,
          },
        ],
        title: '',
        isOnwer: false,
        plancode: '',
        tableData: [],
        tableDataProject: [],
        sIndex: 0,
        isSendBack: false,
        showLoading: false,
        isSubmit: false,
        //提交
        visible: false,
        fzforms: {
          branchStrs: [],
        },
        fzform1: {
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
        fzRules1: {
          branchStrs: [
            {
              required: true,
              message: '请选择分支',
              trigger: 'blur',
            },
          ],
        },
        fromId: 0,
        fromIdcopy: 0,
        flowtaskinfoflowid: '',
        chooseStatus: true,
        executorType: '',
        contractid: '',
        fzoptions: [],
        formData2: {
          transferStaffName: '',
          transferStaffId: '',
        },
        visible1: false,
        visible2: false,
        formData3: [],
        candidateData: {},
        runderList: [],
        candidateType: '',
        clearType: false,
        fromId: '',
        flowtaskinfoflowid: '',
        ymFromId: '',
        status: '',
        fromIdcopy: '',
      }
    },
    created() {},
    methods: {
      async ymsubmit() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            this.$refs.resubmit.ymsubmit()
          }
        })
      },
      reset() {
        this.formData = {
          plancode: '',
          planname: undefined,
          palnyear: undefined,
          plantype: '',
          palncost: undefined,
          spanDate: [],
          realname: '',
          isauditor: '',
          organizationName: '',
          createtime: '',
          orgname: '',
          remarks: '',
        }
        // this.disabled = false
        this.tableDataProject = []
      },
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      async showEdit(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status
      ) {
        this.reset()
        this.fromId = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
        if (title == 'detail') {
          this.disabled = true
        } else if (title == 'edit') {
          this.disabled = false
        }
        console.log('this.disabled', this.disabled)
        //查看按钮入口
        let resD = await getAuditPlanViewDetail({
          planid: formId,
        })

        const userInfo = JSON.parse(localStorage.getItem('userInfo'))
        if (resD.data.auditPlan.createStaff.staffid == userInfo.staffid) {
          this.isOnwer = true
        }

        //获取审计计划附件
        let resF = await getAuditPlanAttInfo({
          planId: formId,
        })
        this.tableData = resF.data

        this.formData = {
          plancode: resD.data.auditPlan.plancode,
          planname: resD.data.auditPlan.planname,
          palnyear: resD.data.auditPlan.palnyear,
          plantype: resD.data.auditPlan.plantype,
          palncost: resD.data.auditPlan.palncost,
          // realname: resD.data.auditPlan.principalStaff.realname,
          // isauditor: resD.data.auditPlan.isauditor,
          organizationName: resD.data.auditPlan.createStaff.realname,
          createtime: resD.data.auditPlan.createtime,
          orgname: resD.data.auditPlan.auditOrgInfo.orgname,
          remarks: resD.data.auditPlan.remarks,
          // principalid: resD.data.auditPlan.principalStaff.staffid,
          createstaffid: resD.data.auditPlan.createStaff.staffid,
          planStartTime: resD.data.auditPlan.starttime,
          planEndTime: resD.data.auditPlan.endtime,
          spanDate: [
            UTCformat(resD.data.auditPlan.starttime),
            UTCformat(resD.data.auditPlan.endtime),
          ],
          createtime: UTCformat(resD.data.auditPlan.createtime),
        }
        this.uploader = resD.data.auditPlan.createStaff.realname

        //计划项目接口
        // let res = await getPlanProjectListByPlanId({ planId: this.planid })  //旧逻辑
        let res = await getPlanProjectListByPlanId({ planId: formId }) //旧逻辑
        this.tableDataProject = res.data.planList
      },
      getQueryVariable(url, variable) {
        var query = url && url.substring(1)
        var vars = query && query.split('?')
        if (vars && vars.length > 0) {
          for (var i = 0; i < vars.length; i++) {
            var pair = vars[i].split('=')
            if (pair[0] == variable) {
              return pair[1]
            }
          }
        }
        return false
      },
      changeTime() {
        //设置key，使组件重新渲染生成
        this.itemKey = Math.random()
      },
      showObj(sIndex) {
        this.sIndex = sIndex
        this.$refs['auditee'].showEdit()
      },
      auditee(val, flag) {
        if (flag == 'right') {
          this.$set(this.tableDataProject[this.sIndex], 'bsjtype', 'ry')
          this.$set(
            this.tableDataProject[this.sIndex],
            'orgidnames',
            val[0].realname
          )
          this.$set(
            this.tableDataProject[this.sIndex],
            'orgids',
            val[0].staffid
          )
        } else {
          this.$set(this.tableDataProject[this.sIndex], 'orgidnames', val.label)
          this.$set(this.tableDataProject[this.sIndex], 'orgids', val.id)
          this.$set(this.tableDataProject[this.sIndex], 'bsjtype', 'bm')
        }
      },
      closed() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.$emit('close')
      },
      async save() {
        this.$refs['form'].validate(async (valid) => {
          const arrAttid = this.tableData.map((item) => item.attid)
          if (arrAttid && arrAttid.length) {
            this.formData.attids = arrAttid.join(',')
          } else {
            this.formData.attids = ''
          }

          if (valid) {
            //上层表单参数
            let params = {
              planid: this.formData.planid,
              plancode: this.formData.plancode,
              planname: this.formData.planname,
              palnyear: this.formData.palnyear,
              plantype: this.formData.plantype,
              palncost: this.formData.palncost,
              planStartTime: this.formData.spanDate[0],
              planEndTime: this.formData.spanDate[1],
              isauditor: this.formData.isauditor,
              createstaffid: this.formData.staffid,
              principalid: this.formData.principalid,
              attIds: this.formData.attids,
              // organizationName: this.formData.realname,
              // createtime: this.formData.createtime,
              // orgname: this.formData.orgname,
              remarks: this.formData.remarks,
            }

            // if (this.disabled) {
            //   params.opinionstatus = 0
            // }
            //审计计划上层表单提交
            let resForm = await mergeAuditPlanInfo(params)
            if (resForm.code === 1) {
              this.$baseMessage(
                resForm.msg,
                'success',
                'vab-hey-message-success'
              )
              this.closed()
              this.planid = resForm.data.auditPlan.planid
              this.formData.planid = resForm.data.auditPlan.planid
              this.tableDataProject.map((v) => {
                this.saveProject(v)
              })
              this.$emit('fetch-data')
            }
          }
        })
      },
      async add() {
        this.$refs['form'].validate(async (valid) => {
          const arrAttid = this.tableData.map((item) => item.attid)
          if (arrAttid && arrAttid.length) {
            this.formData.attids = arrAttid.join(',')
          } else {
            this.formData.attids = ''
          }

          if (valid) {
            //上层表单参数
            let params = {
              planid: this.formData.planid,
              plancode: this.formData.plancode,
              planname: this.formData.planname,
              palnyear: this.formData.palnyear,
              plantype: this.formData.plantype,
              palncost: this.formData.palncost,
              planStartTime: this.formData.spanDate[0],
              planEndTime: this.formData.spanDate[1],
              isauditor: this.formData.isauditor,
              createstaffid: this.formData.staffid,
              principalid: this.formData.principalid,
              attIds: this.formData.attids,
              // organizationName: this.formData.realname,
              // createtime: this.formData.createtime,
              // orgname: this.formData.orgname,
              remarks: this.formData.remarks,
            }

            // if (this.disabled) {
            //   params.opinionstatus = 0
            // }
            //审计计划上层表单提交
            let resForm = await mergeAuditPlanInfo(params)
            if (resForm.code === 1) {
              this.$baseMessage(
                resForm.msg,
                'success',
                'vab-hey-message-success'
              )
            }
          }
        })
      },
      // 给表头加必填符号*
      addRedStar(h, { column }) {
        return [
          h('span', { style: 'color: red' }, '*'),
          h('span', ' ' + column.label),
        ]
      },
      submitForm() {
        this.$refs['elForm'].validate((valid) => {
          if (!valid) return
          // TODO 提交表单
        })
      },
      resetForm() {
        this.$refs['elForm'].resetFields()
      },
      // 保存
      async saveProject(row) {
        if (!row.projectname || !row.targetname) {
          this.$baseMessage('请填写项目名称和工作目标', 'error')
          return
        }
        if (!this.planid) {
          this.$baseMessage('请保存表单信息后再进行当前操作', 'error')
          return
        }
        row.show = false

        const data = {
          planid: this.planid,
          // planprojectid: this.planid,
          plancode: this.formData.plancode,
          finishtime: UTCformat(row.finishtime),
          projectname: row.projectname,
          planprojectid: row.planprojectid,
          targetname: row.targetname,
          orgids: row.orgids,
          orgidnames: row.orgidnames,
          bsjtype: row.bsjtype,
          externalassig: row.externalassig,
        }
        let res = await mergePlanProjectManageInfoList(data)
      },
      // 添加点击按钮
      handleAdd() {
        // if (!this.planid) {
        //   this.$baseMessage('请保存表单信息后再进行当前操作', 'error')
        //   return
        // }
        // 000000
        this.tableDataProject.push({
          projectname: '',
          targetname: '',
          finishtime: '',
          externalassig: '',
          orgidnames: '',
          show: true,
        })
      },
      handleDeleteFile(index, row) {
        deleteFileById({ attId: row.attid }).then((res) => {
          this.$message({
            type: 'success',
            message: '删除成功!',
          })
          this.tableData.splice(index, 1)
        })
      },

      handleDelete(index, row) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(() => {
            if (row.planprojectid) {
              removePlanProjectInfo({
                planprojectid: row.planprojectid,
              }).then((res) => {
                if (res.code == 1) {
                  this.$message({
                    type: 'success',
                    message: '删除成功!',
                  })
                  this.tableDataProject.splice(index, 1)
                }
              })
            } else {
              this.$message({
                type: 'success',
                message: '删除成功!',
              })
              this.tableDataProject.splice(index, 1)
            }
          })
          .catch((err) => {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          })
      },
      showGroupLeader() {
        this.$refs['select'].showEdit('leader')
      },
      selectTeamList(val) {
        // 0000000

        if (val) {
          this.$set(this.formData, 'realname', val[0].realname)
          this.$set(this.formData, 'principalid', val[0].staffid)
        }
      },
      async handleDown(row) {
        const data = await download({ attId: row.attid })
        let filename = row.attname
        let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
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
      handleSuccess(response, file, fileList) {
        let fileObj = {}
        if (file.response.result == '200') {
          fileObj.uploader = this.uploader
          fileObj.attname = file.name
          fileObj.attsize = file.size
          fileObj.attid = response.data.attid

          this.tableData.push(fileObj)
          this.$baseMessage(file.response.msg, 'success')
        } else {
          this.$baseMessage(file.response.msg, 'error')
        }
      },
      // planLeader() {
      //   this.$refs['plan'].showEdit()
      // },
      // planList(val) {
      //   this.$set(this.formData, 'realname', val[0].realname)
      // },
      //提交
      handleCandSelect1(index, value) {
        this.formData2.transferStaffName = value
      },
      handleCandSelect(index, value) {
        // this.$set(this.formData3[index], 'transferStaffId', value)
        this.formData3[index].transferStaffId = value
      },
      close() {
        this.$refs['form'].resetFields()
        this.clearType = true
        this.$bus.$emit('updateMsg', 0)
      },
      currentClose() {
        this.visible = false
        this.form = {}
      },
      handlefzChange(e) {
        this.$refs['fzform'].clearValidate()
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
        })
        if (code == 1) {
          this.$message.success('提交成功')
          this.close4()
          this.close()
        }
      },
      handleSelect() {
        this.$refs['candidateList'].show(this.candidateData)
      },
      selectValue(e) {
        let arr = []
        this.fzoptions.forEach((res) => {
          e.forEach((res1) => {
            if (res.value == res1) {
              arr.push(res)
            }
          })
        })
        this.runderList = arr
        this.formData3 = arr.map(() => {
          return { transferStaffName: '', transferStaffId: '' }
        })
      },
      handleSelectNew(row, index) {
        this.candidateData.nodeId = row.value
        this.$refs['candidateListNew'].show(this.candidateData, index)
      },
      handSelectedNew1(data, index) {
        let name = '',
          id = ''
        data.map((item) => {
          name += item.fullName + ','
          id += item.id + ','
        })
        name = name.substring(0, name.length - 1)
        id = id.substring(0, id.length - 1)
        this.$set(this.formData3[index], 'transferStaffName', name)
        this.$set(this.formData3[index], 'transferStaffId', id)
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
        })
        if (code == 1) {
          this.$message.success('提交成功')
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
      close4() {
        this.visible = false
        this.resetINfo()
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
      handSelected(data) {
        let name = '',
          id = ''
        data.map((item) => {
          name += item.fullName + ','
          id += item.id + ','
        })
        name = name.substring(0, name.length - 1)
        id = id.substring(0, id.length - 1)
        this.formData2.transferStaffName = name
        this.formData2.transferStaffId = id
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
          ymFromId: this.fromIdcopy == -1 ? '' : this.ymFromId,
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
<style scoped lang="scss">
  ::v-deep .el-form.disabled {
    input {
      border: 0;
      background-color: #ffffff;
    }
  }
  ::v-deep .el-date-editor.el-input,
  .el-date-editor.el-input__inner {
    width: 140px;
  }
  ::v-deep .el-input--small .el-input__inner {
    width: 140px;
  }

  ::v-deep .el-form-item__content {
    line-height: 0 !important;
  }
</style>
