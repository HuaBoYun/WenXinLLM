<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      :modal="false"
      @close="close"
      :close-on-click-modal="false"
      v-if="dialogFormVisible"
    >
      <el-row :gutter="15">
        <el-form
          ref="elForm"
          :disabled="allDisabled"
          label-width="125px"
          :model="formData"
          :rules="rules"
        >
          <el-col :span="12">
            <el-form-item label="人员类别" prop="personType">
              <el-select
                v-model="formData.personType"
                placeholder="请选择人员类别"
                :style="{ width: '348px', height: '30px' }"
                @change="changePersonType"
              >
                <el-option label="审计中心人员" value="0" />
                <el-option label="外聘人员" value="1" />
                <el-option label="临时借调人员" value="2" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="人员" prop="realname">
              <el-input
                v-model="formData.realname"
                clearable
                placeholder="请选择人员"
                style="width: 280px; height: 30px"
                disabled
              />
              <el-button
                @click="projectManager"
                style="margin-left: 10px; height: 30px"
                type="primary"
              >
                选择
              </el-button>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <!-- <el-input
                v-model="formData.gender"
                clearable
                placeholder="请填写性别"
                :style="{ width: '100%', height: '30px' }"
                :disabled="disabledEdit && !outPersonCloudEdit"
              /> -->
              <el-select
                v-model="formData.gender"
                placeholder="请填写性别"
                :style="{ width: '348px' }"
                :disabled="disabledEdit && !outPersonCloudEdit"
              >
                <el-option label="男" value="1" />
                <el-option label="女" value="0" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出生年月" prop="birthday">
              <el-date-picker
                :disabled="disabledEdit && !outPersonCloudEdit"
                v-model="formData.birthday"
                type="date"
                placeholder="选择日期"
                value-format="yyyy-MM-dd"
                style="width: 348px"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="政治面貌" prop="politicaloutlook">
              <el-input
                v-model="formData.politicaloutlook"
                clearable
                placeholder="请填写政治面貌"
                :style="{ width: '100%' }"
                :disabled="disabledEdit && !outPersonCloudEdit"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学历/学位" prop="education">
              <el-input
                v-model="formData.education"
                clearable
                placeholder="请填写学历/学位"
                :style="{ width: '100%' }"
                :disabled="disabledEdit && !outPersonCloudEdit"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="专业" prop="major">
              <el-input
                v-model="formData.major"
                clearable
                :disabled="lookDisablue"
                placeholder="请填写专业"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="毕业院校" prop="school">
              <el-input
                v-model="formData.school"
                clearable
                :disabled="lookDisablue"
                placeholder="请填写毕业院校"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="参加工作时间" prop="worktime">
              <el-date-picker
                :disabled="disabledEdit && !outPersonCloudEdit"
                v-model="formData.worktime"
                type="date"
                placeholder="选择日期"
                value-format="yyyy-MM-dd"
                style="width: 348px"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所在单位" prop="orgname">
              <el-input
                v-model="formData.orgname"
                clearable
                placeholder="请填写所在单位"
                :style="{ width: '100%' }"
                :disabled="disabledEdit"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所在部门及职位" prop="jobname">
              <el-input
                v-model="formData.jobname"
                clearable
                placeholder="请填写所在单位"
                :style="{ width: '100%' }"
                :disabled="disabledEdit"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="办公电话" prop="officephone">
              <el-input
                v-model="formData.officephone"
                clearable
                :disabled="lookDisablue"
                placeholder="请填写所在单位"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机" prop="miblephone">
              <el-input
                v-model="formData.miblephone"
                clearable
                :disabled="lookDisablue"
                placeholder="请填写手机号码"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="职称" prop="title">
              <el-input
                v-model="formData.title"
                clearable
                placeholder="请填写所在单位"
                :style="{ width: '100%' }"
                :disabled="disabledEdit && !outPersonCloudEdit"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执业资格" prop="qualification">
              <el-input
                v-model="formData.qualification"
                clearable
                :disabled="lookDisablue"
                placeholder="请填写执业资格"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="主要工作经历">
              <el-input
                type="textarea"
                v-model="formData.jobexperiences"
                clearable
                placeholder="请填写工作经历"
                :disabled="lookDisablue"
                :style="{ width: '100%' }"
              ></el-input>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="24">
            <el-form-item label="参加审计检查情况" prop="situation">
              <el-input
                type="textarea"
                v-model="formData.situation"
                clearable
                placeholder="请填写审计检查情况"
                :disabled="lookDisablue"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col> -->
          <el-col :span="24">
            <el-form-item label="其他有关内容" prop="memo">
              <el-input
                type="textarea"
                v-model="formData.memo"
                clearable
                :disabled="lookDisablue"
                placeholder="请填写其他有关内容"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
        </el-form>
        <el-col :span="24">
          <el-divider>个人培训信息</el-divider>
        </el-col>
        <el-col :span="24">
          <div v-if="!disabled" style="text-align: right; margin-bottom: 5px">
            <el-button
              type="success"
              :disabled="lookDisablue"
              @click="handleAdd"
            >
              增加一行
            </el-button>
            <!-- <el-button type="success" @click="handleDelCheck">删除</el-button> -->
          </div>
          <!-- 新增可编辑表格 -->
          <el-table border :data="tableDataProject">
            <el-table-column
              align="center"
              label="培训时间"
              prop="traintime"
              width="188"
            >
              <template slot-scope="scope">
                <el-date-picker
                  :disabled="!scope.row.show"
                  v-model="scope.row.traintime"
                  type="date"
                  placeholder="选择日期"
                  value-format="yyyy-MM-dd"
                ></el-date-picker>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="培训地点"
              prop="trainlocation"
            >
              <template slot-scope="scope">
                <el-input
                  v-show="scope.row.show"
                  v-model="scope.row.trainlocation"
                  size="mini"
                  style="width: 90%"
                />
                <span v-show="!scope.row.show">
                  {{ scope.row.trainlocation }}
                </span>
              </template>
            </el-table-column>

            <el-table-column
              align="center"
              label="培训证明材料"
              prop="trainevidence"
              min-width="300px"
            >
              <template slot-scope="scope">
                <div style="display: flex">
                  <el-upload
                    :action="baseApi + api"
                    :on-success="
                      (response, file, fileList) =>
                        handleSuccess1(response, file, fileList, scope.$index)
                    "
                    :headers="headers"
                    :file-list="fileList"
                    :on-change="handleChange"
                    :show-file-list="false"
                    :disabled="lookDisablue"
                    :before-upload="handleBeforeUpload"
                  >
                    <div style="display: flex; justify-content: space-between">
                      <span style="width: 200px">
                        {{ tableDataProject[scope.$index].trainevidence }}
                      </span>
                      <el-button :disabled="lookDisablue" type="success">
                        上传
                      </el-button>
                    </div>
                  </el-upload>
                  <el-button type="text" @click="handleDownload(scope.row)">
                    下载
                  </el-button>
                  <el-button type="text" @click="handlePreviewFile(scope.row)">
                    预览
                  </el-button>
                </div>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="培训证明人"
              prop="trainwitness"
              min-width="180px"
            >
              <template slot-scope="scope">
                <el-input
                  v-show="scope.row.show"
                  v-model="scope.row.trainwitness"
                  size="mini"
                  style="width: 90%"
                />
                <span v-show="!scope.row.show">
                  {{ scope.row.trainwitness }}
                </span>
              </template>
            </el-table-column>

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
                  :disabled="lookDisablue"
                >
                  删除
                </el-button>

                <el-button
                  :disabled="lookDisablue"
                  type="text"
                  @click="scope.row.show = true"
                >
                  编辑
                </el-button>

                <el-button
                  :disabled="lookDisablue"
                  type="text"
                  @click="saveProject(scope.row)"
                >
                  保存
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24">
          <el-divider>资质文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <el-upload
            :action="baseApi + api"
            :on-success="handleSuccess"
            :show-file-list="false"
            :headers="headers"
            multiple
            :file-list="fileList"
            :disabled="lookDisablue"
            :before-upload="handleBeforeUpload"
            style="text-align: right; margin-bottom: 5px"
          >
            <div v-if="!disabled">
              <el-button :disabled="lookDisablue" type="success">
                点击上传
              </el-button>
            </div>
            <!-- <el-button type="success" slot="tip" @click="handleDelFile">
              删除
            </el-button> -->
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
                  @click="handleDownload(scope.row)"
                >
                  下载
                </el-button>
                <el-button
                  v-if="!disabled"
                  type="text"
                  @click="handleDeletePerson(scope.row)"
                  :disabled="lookDisablue"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24">
          <el-divider>项目</el-divider>
        </el-col>
        <el-col :span="24">
          <el-table :data="projectTableData">
            <el-table-column
              align="center"
              label="项目编号"
              prop="projectCode"
              width="100"
            >
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="handleEdit(row, true)"
                  :style="`color:${
                    row.projectId == currProjectId ? '#10d06d' : ''
                  }`"
                >
                  {{ row.projectCode }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="项目名称"
              prop="prjoectName"
            />
            <el-table-column
              align="center"
              label="项目来源"
              prop="projectSource"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="项目经理"
              prop="pmStaff.realname"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="项目目前状态"
              prop="status"
              show-overflow-tooltip
            >
              <template #default="{ row }">
                {{
                  row.status == '0'
                    ? '未启动'
                    : row.status == '1'
                    ? '启动'
                    : row.status == '2'
                    ? '实施 '
                    : row.status == '3'
                    ? '完成'
                    : '归档'
                }}
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24">
          <el-divider>评价</el-divider>
        </el-col>
        <el-col :span="24">
          <el-table :data="evaluationTableData">
            <el-table-column
              align="center"
              label="审计人员"
              width="100"
              prop="auditor.realname"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleDetail('查看', row)">
                  {{ row.auditor.realname }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="参与审计项目名称"
              prop="auditProjectName"
            />

            <el-table-column
              align="center"
              label="总分"
              prop="totalScore"
              show-overflow-tooltip
              width="120"
            />
            <el-table-column
              align="center"
              label="考核结果"
              prop="totalScore"
              show-overflow-tooltip
            >
              <template slot-scope="scope">
                <div v-if="scope.row.totalScore > 90">{{ '优秀' }}</div>
                <div
                  v-if="scope.row.totalScore < 90 && scope.row.totalScore > 80"
                >
                  {{ '良好' }}
                </div>
                <div
                  v-if="scope.row.totalScore < 80 && scope.row.totalScore > 60"
                >
                  {{ '合格' }}
                </div>
                <div v-if="scope.row.totalScore < 60">{{ '不合格' }}</div>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>

      <!-- lookDisablue  判断为true的时候不显示 -->
      <template v-if="!lookDisablue" #footer>
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <!-- <el-button
          v-if="
            (formData.aprStatus == 2 || formData.aprStatus == 3) &&
            jurisdictionCode == 1
          "
          @click="ymsubmit"
          type="primary"
        >
          提交
        </el-button> -->
      </template>
    </el-dialog>
    <project-manage
      @projectManage="getChildlistPro"
      ref="manage"
    ></project-manage>
    <evaluationEdit ref="edit" />
    <IndexEdit ref="edit" />
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
  import { download } from '@/oapi/audit/implement'
  import { projectList } from '@/oapi/audit/rectify'
  import {
    deletePersonFile,
    loadEvaluationData,
    personFileList,
  } from '@/oapi/audit/structure'
  import IndexEdit from '@/views/oilAudit/project/components/IndexEdit.vue'
  import evaluationEdit from '@/views/oilAudit/structure/components/evaluationEdit.vue'
  import {
    getFlowTaskInfo,
    ymWorkCandidates,
    ymWorkSubmit,
  } from '@/oapi/contract/manage'
  import { getFaqiInfo } from '@/oapi/setting/msg'
  import CandidateUserSelect from '@/components/CandidateUserSelect'

  import {
    GetStaffInfo,
    GetusrDetail,
    MergePlanProjectManageInfo,
    RemoveTrain,
    SaveOrUpdateStaff,
  } from '@/oapi/setting/personnel'
  import store from '@/store'
  import projectManage from '../../../audit/project/components/formComponents/projectManage.vue'
  const { baseURL } = require('@/config')
  export default {
    name: 'xxxx',
    components: {
      projectManage,
      evaluationEdit,
      IndexEdit,
      CandidateUserSelect,
    },
    data() {
      return {
        baseApi: baseURL,
        api: '/audit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        disabledEdit: true,
        title: '新增',
        dialogFormVisible: false,
        tableDataProject: [],
        tableData: [],
        fileList: [],
        disabled: false,
        staffid: '',
        formData: {
          realName: '',
          gender: '',
          birthday: '',
          politicaloutlook: '',
          education: '',
          major: '',
          school: '',
          worktime: '',
          officephone: '',
          title: '',
          qualification: '',
          jobexperiences: '',
          situation: '',
          miblephone: '',
          memo: '',
          orgname: '',
          jobname: '',
          personType: '',
          aprStatus: '',
        },
        rules: {
          planname: [
            {
              message: '请选择所属审计计划',
              required: true,
              trigger: 'change',
            },
          ],
        },
        disabledBtn: false,
        lookDisablue: false,
        outPersonCloudEdit: false,
        projectTableData: [],
        evaluationTableData: [],
        allDisabled: false,
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
    watch: {
      staffid: {
        async handler() {
          let a = await projectList({ pmId: this.staffid })
          this.projectTableData = a.data.pageInfo.tlist
          let b = await loadEvaluationData({ staffid: this.staffid })
          this.evaluationTableData = b.data.pageInfo.tlist
        },
      },
    },
    methods: {
      close() {
        // this.form = this.$options.data().form
        this.dialogFormVisible = false
        this.clearType = true
        this.$emit('fetchData')
        this.allDisabled = false
        this.projectTableData = []
        this.evaluationTableData = []
        this.staffid = ''
      },
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      submitForm() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            let attids = ''
            this.tableData.map((item) => {
              attids += item.attid
              attids += ','
            })
            attids = attids.substring(0, attids.length - 1)
            let data = {
              realName: this.formData.realName,
              gender: this.formData.gender,
              politicaloutlook: this.formData.politicaloutlook,
              education: this.formData.education,
              major: this.formData.major,
              school: this.formData.school,
              staffid: this.staffid,
              officephone: this.formData.officephone,
              title: this.formData.title,
              qualification: this.formData.qualification,
              jobexperiences: this.formData.jobexperiences,
              situation: this.formData.situation,
              miblephone: this.formData.miblephone,
              memo: this.formData.memo,
              // orgname: this.formData.orgname,
              // jobname: this.formData.jobname,

              personType: this.formData.personType,
            }
            const params = {
              workDate: this.formData.worktime
                ? this.formData.worktime.split('T')[0]
                : '',
              birth: this.formData.birthday
                ? this.formData.birthday.split('T')[0]
                : '',
            }

            SaveOrUpdateStaff({
              data: JSON.stringify(data),
              workDate: params.workDate || '',
              birth: params.birth || '',
              attids,
            }).then((res) => {
              if (res.code == 1) {
                this.dialogFormVisible = false
                this.$message({
                  type: 'success',
                  message: '提交成功',
                })
                this.$emit('fetchData')
              } else {
                this.message({
                  type: 'success',
                  message: '操作失败',
                })
              }
            })
          }
        })
      },
      async showEdit(row, disabled, planNum) {
        this.dialogFormVisible = true
        if (this.staffid) {
          let a = await projectList({ pmId: this.staffid })
          this.projectTableData = a.data.pageInfo.tlist
          let b = await loadEvaluationData({ staffid: this.staffid })
          this.evaluationTableData = b.data.pageInfo.tlist
        } else {
          this.projectTableData = []
          this.evaluationTableData = []
        }

        // let a = await projectList({ pmId: this.staffid })
        // this.disabledEdit = true // 显示不用填写

        if (disabled == '修改') {
          this.getPersonFileList(row.staffid)
          //
          GetStaffInfo({ staffId: row.staffid }).then(async (res) => {
            // if (res.data.staff.aprStatus == 2 || res.data.staff.aprStatus == 3) {
            //   const res2 = await getFlowTaskInfo({
            //     tableId: 14,
            //     formId: row.staffid,
            //   })
            //   this.jurisdictionCode = res2.data.isFlowInfo
            //   if (res2.data.isFlowInfo) {
            //     this.flowtaskinfoflowid = res2.data.flowId
            //     this.fromId = row.staffid
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

            const staff = res.data.staff
            const trainList = res.data.trainList
            this.tableDataProject = trainList
            if (staff.personType === '1') {
              this.outPersonCloudEdit = true
            } else {
              this.outPersonCloudEdit = false
            }
            this.formData = {
              realname: staff.realname,
              gender: staff.gender,
              birthday: staff.birthday,
              politicaloutlook: staff.politicaloutlook,
              education: staff.education,
              major: staff.major,
              school: staff.school,
              worktime: staff.worktime,
              officephone: staff.officephone,
              title: staff.title,
              qualification: staff.qualification,
              jobexperiences: staff.jobexperiences,
              situation: staff.situation,
              miblephone: staff.miblephone,
              memo: staff.memo,
              orgname: staff.currentOrg ? staff.currentOrg.orgname : '',
              jobname: staff.linkDetp ? staff.linkDetp.orgname : '',
              personType: staff.personType,
              aprStatus: staff.aprStatus,
            }
          })
          this.staffid = row.staffid
          this.title = '修改'
          this.lookDisablue = false
          this.disabledBtn = true
        } else if (disabled == '新建') {
          // this.disabledEdit = false
          this.formData = {}
          this.tableDataProject = []
          this.title = '新增'
          this.lookDisablue = false
          this.disabledBtn = false
        } else if (disabled == '查看') {
          this.getPersonFileList(row.staffid)
          this.allDisabled = true
          GetStaffInfo({ staffId: row.staffid }).then((res) => {
            const staff = res.data.staff
            const trainList = res.data.trainList
            this.tableDataProject = trainList
            if (staff.personType === '1') {
              this.outPersonCloudEdit = true
            } else {
              this.outPersonCloudEdit = false
            }
            this.formData = {
              realname: staff.realname,
              gender: staff.gender,
              birthday: staff.birthday,
              politicaloutlook: staff.politicaloutlook,
              education: staff.education,
              major: staff.major,
              school: staff.school,
              worktime: staff.worktime,
              officephone: staff.officephone,
              title: staff.title,
              qualification: staff.qualification,
              jobexperiences: staff.jobexperiences,
              situation: staff.situation,
              miblephone: staff.miblephone,
              memo: staff.memo,
              orgname: staff.currentOrg ? staff.currentOrg.orgname : '',
              jobname: staff.linkDetp ? staff.linkDetp.orgname : '',
              personType: staff.personType,
            }
          })
          this.title = '查看'
          this.disabledBtn = true
          this.lookDisablue = true
        }
      },
      async getChildlistPro(val) {
        let res = await GetusrDetail({
          staffid: val[0].staffid,
        })

        const data = res.data.usrInfo
        // this.formData = {
        //   realname: data.realname,
        //   memo: data.memo,
        //   // orgname: data.orgName,
        //   miblephone: data.miblephone,
        //   education: data.education,
        //   jobexperiences: data.jobexperiences,
        //   major: data.major,
        //   officephone: data.officephone,
        //   politicaloutlook: data.politicaloutlook,
        //   qualification: data.qualification,
        //   school: data.school,
        //   situation: data.situation,
        //   title: data.title,
        //   jobname: data.linkDetp.orgname,
        //   orgname: data.linkOrg.orgname,
        // }
        this.formData.realname = data.realname
        this.formData.memo = data.memo
        this.formData.miblephone = data.miblephone
        this.formData.education = data.education
        this.formData.jobexperiences = data.jobexperiences
        this.formData.major = data.major
        this.formData.officephone = data.officephone
        this.formData.politicaloutlook = data.politicaloutlook
        this.formData.qualification = data.qualification
        this.formData.school = data.school
        this.formData.situation = data.situation
        this.formData.title = data.title
        this.formData.jobname = data.linkDetp.orgname
        this.formData.orgname = data.linkOrg.orgname

        this.staffid = val[0].staffid
        this.$set(this.formData, 'realname', val[0].realname)
      },
      // 添加点击按钮
      handleAdd() {
        //
        this.tableDataProject.push({
          trainevidence: '',
          trainlocation: '',
          traintime: '',
          trainwitness: '',
          show: true,
        })
      },
      handleChange(file, fileList) {
        this.fileList = fileList.slice(-1)
      },
      saveProject(row) {
        if (this.staffid) {
          row.show = false
          const data = {
            trainevidence: row.trainevidence,
            trainlocation: row.trainlocation,
            trainwitness: row.trainwitness,
          }
          const obj = {
            data: JSON.stringify(data),
            staffId: this.staffid,
            trainattIds: row.attid,
            traindate: row.traintime,
          }
          MergePlanProjectManageInfo(obj).then((res) => {
            this.$message({
              type: 'success',
              message: '保存成功',
            })
          })
        } else {
          this.$message({
            type: 'error',
            message: '请选择人员',
          })
        }
      },
      projectManager() {
        this.$refs['manage'].showEdit()
      },
      handleDelete(index, row) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(async () => {
            this.tableDataProject.splice(index, 1)
            await RemoveTrain({
              trainId: row.trainid,
            })
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          })
      },
      // handleSuccess(response, file, fileList) {
      //
      //   if (file.response.result == '200') {
      //     file.createPerson = this.createPerson
      //     this.tableData.push(file)
      //     this.$baseMessage(file.response.msg, 'success')
      //   } else {
      //     this.$baseMessage(file.response.msg, 'error')
      //   }
      // },
      handleSuccess(file) {
        if (file.result == '200') {
          let list = this.tableData
          list.push(file.data)
          this.tableData = list
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      handleSuccess1(res, file, fileList, index) {
        if (file.response.result == '200') {
          this.tableDataProject[index].trainevidence = res.data.attname
          this.tableDataProject[index].attid = res.data.attid
        }

        // switch (flag) {
        // //找到第一组 往进push 下面同理
        //   case 1:
        //     this.tableData[index].uploadFilePath1.push(aaaa);
        //     break;
        //   case 2:
        //     this.tableData[index].uploadFilePath2.push(aaaa);
        //     break;
        //   case 3:
        //     this.tableData[index].uploadFilePath3.push(aaaa);
        //     break;
        // }
      },
      handleEdit2(index, row) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(async () => {
            this.tableData.splice(index, 1)
            // await deleteFileById({})
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          })
      },
      changePersonType(e) {
        if (e === '1') {
          this.outPersonCloudEdit = true
        } else {
          this.outPersonCloudEdit = false
        }
      },
      handleDetail(a, b) {
        this.$refs['edit'].showEdit(a, b)
      },
      handleEdit(row, disabled) {
        this.$refs['edit'].showEdit(row, disabled, this.planNum)
      },
      handleDeletePerson(row) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          //删除对应的id
          let res = await deletePersonFile({ attId: row.attid })
          if (res.msg === '成功') {
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
            let list = this.tableData
            list = list.filter((item) => item.attid != row.attid)
            this.tableData = list
          } else {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          }
        })
      },
      async getPersonFileList(staffId) {
        const data = await personFileList({ staffId })
        this.tableData = data.data || []
      },
      // async handleDownload(row) {
      //   const res = await download({ attId: row.attid })
      //   this.downloadFileByBlob(res, row.name)
      // },

      // downloadFileByBlob(blob, fileName = 'file') {
      //   // let blobUrl = window.URL.createObjectURL(blob)
      //   let blobUrl = window.URL.createObjectURL(blob, {
      //     type: 'application/vnd.ms-excel',
      //   })
      //   let link = document.createElement('a')
      //   link.download = fileName || 'defaultName'
      //   link.style.display = 'none'
      //   link.href = blobUrl
      //   // 触发点击
      //   document.body.appendChild(link)
      //   link.click()
      //   // 移除
      //   document.body.removeChild(link)
      // },
      async handleDownload(row) {
        const data = await download({ attId: row.attid })
        let filename = row.attname || row.trainevidence
        let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
        let url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', filename)
        document.documentElement.appendChild(link)
        link.click()
        document.documentElement.removeChild(link)
      },
      //提交
      async ymsubmit() {
        this.$refs['elForm'].validate(async (valid) => {
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
<style scoped lang="scss">
  // ::v-deep .el-table td.el-table__cell div {
  //   display: flex;
  // }
  // ::v-deep .el-upload-list__item:first-child {
  //   margin-top: 5px;
  // }
</style>
