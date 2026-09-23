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
          <!-- <el-col :span="12" v-if="showMJ">
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
                :disabled="lookDisablue"
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
                :disabled="!formData.secrectLevelId || lookDisablue"
              >
                选择
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-divider></el-divider>
          </el-col> -->
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
          <el-col :span="12">
            <el-form-item label="年龄" prop="yearsold">
              <el-input
                v-model="formData.yearsold"
                clearable
                :disabled="lookDisablue"
                placeholder="请填写年龄"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系方式" prop="contactinfo">
              <el-input
                v-model="formData.contactinfo"
                clearable
                :disabled="lookDisablue"
                placeholder="请填写联系方式"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="职务" prop="zhiwu">
              <el-input
                v-model="formData.zhiwu"
                clearable
                :disabled="lookDisablue"
                placeholder="请填写职务"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="职级" prop="zhiwulevel">
              <el-input
                v-model="formData.zhiwulevel"
                clearable
                :disabled="lookDisablue"
                placeholder="请填写职级"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最高学历" prop="topmajor">
              <el-input
                v-model="formData.topmajor"
                clearable
                :disabled="lookDisablue"
                placeholder="请填写最高学历"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="研究生毕业院校" prop="gradschool">
              <el-input
                v-model="formData.gradschool"
                clearable
                :disabled="lookDisablue"
                placeholder="请填写研究生毕业院校"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="本科毕业院校" prop="school">
              <el-input
                v-model="formData.school"
                clearable
                :disabled="lookDisablue"
                placeholder="请填写本科毕业院校"
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
                    action=""
                    :on-success="
                      (response, file, fileList) =>
                        handleSuccess1(response, file, fileList, scope.$index)
                    "
                    :headers="headers"
                    :file-list="fileList"
                    :on-change="handleChange"
                    :show-file-list="false"
                    :disabled="lookDisablue"
                    :before-upload="
                      (file) => handleBeforeUploadTrain(file, scope.$index)
                    "
                  >
                    <div style="display: flex; justify-content: space-between">
                      <span
                        style="width: 200px"
                        v-if="tableDataProject[scope.$index].trainevidence"
                        @click="handlePreviewFile(scope.row)"
                      >
                        {{ tableDataProject[scope.$index].trainevidence }}
                      </span>
                      <div v-else style="width: 200px" />

                      <el-button :disabled="lookDisablue" type="success">
                        上传
                      </el-button>
                    </div>
                  </el-upload>
                  <el-button type="text" @click="handleDowns(scope.row)">
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
            style="text-align: right; margin-bottom: 5px"
            class="upload-demo"
            :show-file-list="false"
            action=""
            :headers="headers"
            :on-preview="handlePreview"
            :on-success="handleSuccess"
            :file-list="fileList2"
            :before-upload="handleBeforeUpload"
            :multiple="true"
          >
            <div v-if="!disabled" style="margin-right: 10px">
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
                  @click="handleDowns(scope.row)"
                >
                  下载
                </el-button>
                <el-button
                  type="text"
                  :disabled="false"
                  @click="handlePreviewFile(scope.row)"
                >
                  预览
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
    <!-- <project-manage
      @projectManage="getChildlistPro"
      ref="manage"
    ></project-manage> -->
    <select-team ref="select" @selectTeamList="selectTeamList"></select-team>

    <evaluationEdit ref="edit" />
    <IndexEdit ref="edit" />
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </div>
</template>
<script>
  import { download } from '@/api/audit/implement'
  import { projectList } from '@/api/audit/rectify'
  import {
    deletePersonFile,
    loadEvaluationData,
    personFileList,
  } from '@/api/audit/structure'
  import IndexEdit from '@/views/audit/project/components/IndexEdit.vue'
  import evaluationEdit from '@/views/audit/structure/components/evaluationEdit.vue'
  import {
    getFlowTaskInfo,
    ymWorkCandidates,
    ymWorkSubmit,
    getPrivewAttInfo,
  } from '@/api/contract/manage'
  import { getFaqiInfo } from '@/api/setting/msg'
  import CandidateUserSelect from '@/components/CandidateUserSelect'
  import {
    GetStaffInfo,
    GetusrDetail,
    MergePlanProjectManageInfo,
    RemoveTrain,
    SaveOrUpdateStaff,
  } from '@/api/setting/personnel'
  import store from '@/store'
  import projectManage from '../../../audit/project/components/formComponents/projectManage.vue'
  const { baseURL } = require('@/config')
  import selectTeam from '@/views/audit/plan/components/selectTeam.vue'
  import ZXPerson from '@/components/selectPerson.vue'
  import { getMJ } from '@/api/setting/mjsz'
  import { hasMJ, couldMJ } from '@/utils'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    name: '324',
    components: {
      projectManage,
      evaluationEdit,
      IndexEdit,
      CandidateUserSelect,
      selectTeam,
      ZXPerson,
    },
    data() {
      return {
        baseApi: baseURL,
        api2: '/audit/fileManage/upload',
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        headers: {
          token: store.getters['user/token'],
        },
        disabledEdit: true,
        title: '新增',
        dialogFormVisible: false,
        tableDataProject: [],
        tableData: [],
        fileList: [],
        fileList2: [],
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
          yearsold: '',
          contactinfo: '',
          zhiwu: '',
          zhiwulevel: '',
          topmajor: '',
          gradschool: '',
          school: '',
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
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
        mjId: '', // 密级id
        MJoption: [],
        showMJ: false,
      }
    },
    // watch: {
    //   staffid: {
    //     async handler() {
    //       let a = await projectList({ pmId: this.staffid })
    //       this.projectTableData = a.data.pageInfo.tlist
    //       let b = await loadEvaluationData({ staffid: this.staffid })
    //       this.evaluationTableData = b.data.pageInfo.tlist
    //     },
    //   },
    // },
    // async created() {
    //   this.showMJ = couldMJ()
    //   if (this.showMJ) {
    //     // 获取密级,菜单id
    //     const res = await hasMJ('Personnel')
    //     this.menuId = res[0].menuid
    //     // 请求密级下拉数据
    //     const res2 = await getMJ({ rightId: res[0].menuid })
    //     this.MJoption = res2.data
    //   }
    // },
    computed: {
      getFormLevel() {
        if (!this.formData.secrectLevelId) return ''
        const level = this.MJoption.find(
          (item) => item.levelId == this.formData.secrectLevelId
        )
        return level ? level.levelName : ''
      },
    },
    methods: {
      async getMJ() {
        this.showMJ = couldMJ()
        if (this.showMJ) {
          // 获取密级,菜单id
          const res = await hasMJ('Personnel')
          this.menuId = res[0].menuid
          // 请求密级下拉数据
          const res2 = await getMJ({ rightId: res[0].menuid })
          this.MJoption = res2.data
        }
      },
      handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.formData.staffScopeIds = ids
        this.formData.staffScopeNames = names
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
      close() {
        // this.form = this.$options.data().form
        // this.formData = this.$options.data().formData
        this.formData = {
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
          yearsold: '',
          contactinfo: '',
          zhiwu: '',
          zhiwulevel: '',
          topmajor: '',
          gradschool: '',
          school: '',
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
        }
        this.dialogFormVisible = false
        this.clearType = true
        this.$emit('fetchData')
        this.allDisabled = false
        this.projectTableData = []
        this.evaluationTableData = []
        this.fileList2 = []
        this.tableData = []
        this.staffid = ''
      },
      handleBeforeUpload2(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      handleBeforeUploadTrain(file, index) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 100MB!')
          return false
        }
        // 调用自定义上传逻辑
        this.customUploadWrapperTrain({ file, index })
        return false // 停止默认上传行为
      },
      async handleDownload(row) {
        try {
          // 调用 handleDown 并传递自定义的下载接口
          await handleDown(row, this.headers, this.lodeapi)
        } catch (error) {
          console.error('自定义下载失败:', error)
        }
      },

      downloadFileByBlob(blob, fileName = 'file') {
        // let blobUrl = window.URL.createObjectURL(blob)
        let blobUrl = window.URL.createObjectURL(blob, {
          type: 'application/vnd.ms-excel',
        })
        let link = document.createElement('a')
        link.download = fileName || 'defaultName'
        link.style.display = 'none'
        link.href = blobUrl
        // 触发点击
        document.body.appendChild(link)
        link.click()
        // 移除
        document.body.removeChild(link)
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
              yearsold: this.formData.yearsold,
              contactinfo: this.formData.contactinfo,
              zhiwu: this.formData.zhiwu,
              zhiwulevel: this.formData.zhiwulevel,
              topmajor: this.formData.topmajor,
              gradschool: this.formData.gradschool,
              school: this.formData.school,
              // orgname: this.formData.orgname,
              // jobname: this.formData.jobname,

              personType: this.formData.personType,
              secrectLevelId: this.formData.secrectLevelId,
              staffScopeNames: this.formData.staffScopeNames,
              staffScopeIds: this.formData.staffScopeIds,
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
              attIds: attids,
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
      /**
       * @description: 外部打开内部dialog
       * @param {*} title 表单类型
       * @param {*} row 编辑、详情带入的数据
       * @return {*}
       */
      async showEdit(row, disabled) {
        this.dialogFormVisible = true
        // this.getMJ()

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
            this.tableDataProject = trainList.map((k) => {
              k.show = false
              return k
            })
            if (staff.personType === '1') {
              this.outPersonCloudEdit = true
            } else {
              this.outPersonCloudEdit = false
            }
            console.log('🚀 ~ showEdit ~ staff:', staff)
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

              yearsold: staff.yearsold,
              contactinfo: staff.contactinfo,
              zhiwu: staff.zhiwu,
              zhiwulevel: staff.zhiwulevel,
              topmajor: staff.topmajor,
              gradschool: staff.gradschool,
              school: staff.school,
              secrectLevelId: staff.secrectLevelId,
              staffScopeNames: staff.staffScopeNames,
              staffScopeIds: staff.staffScopeIds,
            }
          })
          this.staffid = row.staffid
          this.title = '修改'
          this.lookDisablue = false
          this.disabledBtn = true
        } else if (disabled == '新建') {
          // this.disabledEdit = false
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
            this.tableDataProject = trainList.map((k) => {
              k.show = false
              return k
            })
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

              yearsold: staff.yearsold,
              contactinfo: staff.contactinfo,
              zhiwu: staff.zhiwu,
              zhiwulevel: staff.zhiwulevel,
              topmajor: staff.topmajor,
              gradschool: staff.gradschool,
              school: staff.school,
              secrectLevelId: +staff.secrectLevelId,
              staffScopeNames: staff.staffScopeNames,
              staffScopeIds: staff.staffScopeIds,
            }
          })
          this.title = '查看'
          this.disabledBtn = true
          this.lookDisablue = true
        }
      },
      async selectTeamList(val) {
        let res = await GetusrDetail({
          staffid: val[0].staffid,
        })

        const data = res.data.usrInfo

        // 修改为逐个设置属性，确保响应式更新
        this.$set(this.formData, 'realname', data.realname || '')
        this.$set(this.formData, 'memo', data.memo || '')
        this.$set(this.formData, 'miblephone', data.miblephone || '')
        this.$set(this.formData, 'education', data.education || '')
        this.$set(this.formData, 'jobexperiences', data.jobexperiences || '')
        this.$set(this.formData, 'major', data.major || '')
        this.$set(this.formData, 'officephone', data.officephone || '')
        this.$set(
          this.formData,
          'politicaloutlook',
          data.politicaloutlook || ''
        )
        this.$set(this.formData, 'qualification', data.qualification || '')
        this.$set(this.formData, 'school', data.school || '')
        this.$set(this.formData, 'situation', data.situation || '')
        this.$set(this.formData, 'title', data.title || '')
        this.$set(this.formData, 'jobname', data.linkDetp?.orgname || '')
        this.$set(this.formData, 'orgname', data.linkOrg?.orgname || '')

        // 重置禁用状态
        this.disabledEdit = false
        this.lookDisablue = false

        this.staffid = val[0].staffid
      },
      // 添加点击按钮
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */
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
            trainid: row.trainid,
          }
          const obj = {
            data: JSON.stringify(data),
            staffId: this.staffid,
            trainattIds: row.attid,
            traindate: row.traintime,
            trainid: row.trainid || '',
          }
          MergePlanProjectManageInfo(obj).then((res) => {
            if (res.data.code == 0) return this.$message.error(res.data.msg)
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
        // this.$refs['manage'].showEdit()
        this.$refs['select'].showEdit('leader')
      },
      async handleDelete(index, row) {
        this.tableDataProject.splice(index, 1)
        // 如果有trainid则调用接口删除，否则只进行前端删除
        if (row.trainid) {
          await RemoveTrain({
            trainId: row.trainid,
          })
        }
        this.$message({
          type: 'success',
          message: '删除成功!',
        })
      },
      handleSuccess1(res, file, fileList, index) {
        console.log('handleSuccess1 called with:', {
          res,
          file,
          fileList,
          index,
        })
        console.log(
          'tableDataProject before update:',
          this.tableDataProject[index]
        )

        // 处理原始上传方式的响应
        if (file && file.response && file.response.result == '200') {
          this.$set(
            this.tableDataProject[index],
            'trainevidence',
            file.response.data.fileName
          )
          this.$set(
            this.tableDataProject[index],
            'attid',
            file.response.data.attId
          )
          console.log(
            'Updated via original upload:',
            file.response.data.fileName
          )
        }
        // 处理customUpload的响应
        else if (res && res.code == 200) {
          if (res.data && res.data.length > 0) {
            const fileData = res.data[0]
            // 将res.data[0]中的所有数据都赋值给tableDataProject[index]
            Object.keys(fileData).forEach((key) => {
              this.$set(this.tableDataProject[index], key, fileData[key])
            })
            // 确保trainevidence字段正确设置
            this.$set(
              this.tableDataProject[index],
              'trainevidence',
              fileData.attname
            )
            console.log('Updated via customUpload:', fileData.attname)
          }
          this.$baseMessage(res.msg, 'success')
        } else {
          console.error('Upload failed:', res)
          this.$baseMessage(res?.msg || '上传失败', 'error')
        }

        console.log(
          'tableDataProject after update:',
          this.tableDataProject[index]
        )
        this.$forceUpdate()
      },
      customUploadWrapperTrain(options) {
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
            fileList: fileList,
            formData: formData,
            onProgress: this.handleProgress,
            onSuccess: (response) => {
              this.handleSuccess1(response, null, null, options.index)
              resolve(response)
            },
            onError: (error) => {
              reject(error)
            },
          })
        })
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
      async handleDeletePerson(row) {
        let res = await deletePersonFile({ attId: row.attid })
        if (res.msg === '成功') {
          this.$message({
            type: 'success',
            message: '删除成功!',
          })
          let list = this.tableData
          list = list.filter((item) => item.attid != row.attid)
          this.tableData = list
        }
      },
      async getPersonFileList(staffId) {
        const data = await personFileList({ staffId })
        this.tableData = data.data || []
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
          this.fileList2 = [...this.fileList2, ...file.data]
          this.tableData = [...this.tableData, ...file.data]
          // this.tableData = list
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      //下载公共方法调用
      async handleDowns(row) {
        console.log('🚀 ~ row:', row)
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
<style scoped lang="scss">
  // ::v-deep .el-table td.el-table__cell div {
  //   display: flex;
  // }
  // ::v-deep .el-upload-list__item:first-child {
  //   margin-top: 5px;
  // }
</style>
