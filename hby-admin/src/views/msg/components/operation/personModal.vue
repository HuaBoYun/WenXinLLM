<template>
  <div>
    <el-dialog
      v-if="dialogFormVisible"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
      :close-on-click-modal="false"
    >
      <el-tabs v-show="dialogFormVisible" v-model="activeName" type="card">
        <el-tab-pane label="基本信息" name="first">
          <h3>审计人员-审批</h3>
          <el-row :gutter="15">
            <el-form
              ref="elForm"
              :disabled="!cloudEdit"
              label-width="125px"
              :model="formData"
              :rules="rules"
            >
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
                <el-form-item label="人员类别" prop="personType">
                  <el-select
                    v-model="formData.personType"
                    placeholder="请选择人员类别"
                    :style="{ width: '348px' }"
                    @change="changePersonType"
                  >
                    <el-option label="审计中心人员" value="0" />
                    <el-option label="外聘人员" value="1" />
                    <el-option label="临时借调人员" value="2" />
                  </el-select>
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
              <div
                v-if="!disabled"
                style="text-align: right; margin-bottom: 5px"
              >
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
                      <div
                        style="display: flex; justify-content: space-between"
                      >
                        <span style="width: 200px">
                          {{ tableDataProject[scope.$index].trainevidence }}
                        </span>
                        <el-button :disabled="lookDisablue" type="success">
                          点击上传
                        </el-button>
                      </div>
                    </el-upload>
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
                <el-table-column
                  align="center"
                  label="附件名称"
                  prop="attname"
                />
                <el-table-column
                  align="center"
                  label="文件大小(KB)"
                  prop="attsize"
                />
                <el-table-column
                  align="center"
                  label="创建人"
                  prop="uploader"
                />
                <el-table-column
                  align="center"
                  label="操作"
                  show-overflow-tooltip
                  width="120"
                >
                  <template slot-scope="scope">
                    <el-button
                      :disabled="false"
                      type="text"
                      @click="handleEdit2(row)"
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
                      v-if="
                        scope.row.totalScore < 90 && scope.row.totalScore > 80
                      "
                    >
                      {{ '良好' }}
                    </div>
                    <div
                      v-if="
                        scope.row.totalScore < 80 && scope.row.totalScore > 60
                      "
                    >
                      {{ '合格' }}
                    </div>
                    <div v-if="scope.row.totalScore < 60">{{ '不合格' }}</div>
                  </template>
                </el-table-column>
              </el-table>
            </el-col>
            <el-form
              label-width="125px"
              :model="formData1"
              :rules="rules1"
              ref="elForm1"
              v-if="!hideDescInput"
            >
              <el-col :span="24">
                <el-divider>审批意见</el-divider>
              </el-col>
              <el-col :span="24">
                <el-form-item label="审批意见" prop="optDesc">
                  <el-input
                    type="textarea"
                    :rows="2"
                    placeholder="请输入审批意见"
                    v-model="formData1.optDesc"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <div>
                  <el-button
                    v-if="cloudEdit"
                    @click="submitForm"
                    type="primary"
                  >
                    保存
                  </el-button>
                  <el-button
                    type="primary"
                    v-for="(value, index) in buttonList"
                    :key="index"
                    @click="handleSubmit(index)"
                  >
                    {{ value ? value : '提交' }}
                  </el-button>
                </div>
              </el-col>
            </el-form>
          </el-row>
        </el-tab-pane>
        <el-tab-pane label="审批查看" name="second">
          <el-row :gutter="24">
            <el-col :span="24">
              <img
                alt="审批图"
                :src="imgurl"
                style="margin-bottom: 20px; width: 100%"
              />
            </el-col>
            <el-col :span="24">
              <el-table :data="personTableData">
                <el-table-column
                  align="center"
                  label="流程ID"
                  width="100"
                  prop="processName"
                ></el-table-column>
                <el-table-column
                  align="center"
                  label="办理人"
                  prop="approver"
                />

                <el-table-column
                  align="center"
                  label="办理角色"
                  prop="approvalrole"
                  show-overflow-tooltip
                  width="150"
                />
                <el-table-column
                  align="center"
                  label="办理结果"
                  prop="result"
                  show-overflow-tooltip
                  width="120"
                />
                <el-table-column
                  align="center"
                  label="办理意见"
                  prop="examination"
                  show-overflow-tooltip
                  width="120"
                />
                <el-table-column
                  align="center"
                  label="办理时间"
                  prop="approvaldate"
                  show-overflow-tooltip
                  width="120"
                  :formatter="formatDate"
                />
                <el-table-column
                  align="center"
                  label="下一步:办理人/办理角色"
                  prop="handle"
                  show-overflow-tooltip
                  width="120"
                />
              </el-table>
            </el-col>
          </el-row>
        </el-tab-pane>
      </el-tabs>

      <!-- lookDisablue  判断为true的时候不显示 -->
      <!-- <template v-if="lookDisablue" #footer>
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </template> -->
    </el-dialog>
    <project-manage
      @projectManage="getChildlistPro"
      ref="manage"
    ></project-manage>
    <evaluationEdit ref="edit" />
    <IndexEdit ref="edit" />
  </div>
</template>
<script>
  import { projectList } from '@/api/audit/rectify'
  import {
    deletePersonFile,
    getBLDetail,
    getPersonImgData,
    handlePersonButtonClick,
    loadEvaluationData,
    personFileList,
  } from '@/api/audit/structure'
  import {
    GetStaffInfo,
    GetusrDetail,
    MergePlanProjectManageInfo,
    RemoveTrain,
    SaveOrUpdateStaff,
  } from '@/api/setting/personnel'
  import { baseURL } from '@/config'
  import store from '@/store'
  import { formatDay } from '@/utils/index'
  import projectManage from '@/views/audit/project/components/formComponents/projectManage.vue'
  import IndexEdit from '@/views/audit/project/components/IndexEdit.vue'
  import evaluationEdit from '@/views/audit/structure/components/evaluationEdit.vue'
  export default {
    name: 'xxxx',
    components: {
      projectManage,
      evaluationEdit,
      IndexEdit,
    },
    data() {
      return {
        baseApi: baseURL,
        api: '/fileManage/upload',
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
        },
        formData1: {
          optDesc: '',
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
        rules1: {
          optDesc: [
            {
              message: '请填写审批意见',
              required: true,
              trigger: 'blur',
            },
          ],
        },
        disabledBtn: false,
        lookDisablue: false,
        outPersonCloudEdit: false,
        projectTableData: [],
        evaluationTableData: [],
        allDisabled: false,
        buttonList: [],
        processDefinitionId: '',
        processInstanceId: '',
        taskid: '',
        cyId: '',
        staffID: '',
        cloudEdit: false,
        activeName: 'first',
        imgurl: '',
        personTableData: [],
        hideDescInput: false, //控制是否隐藏审批意见输入
      }
    },
    watch: {
      staffid: {
        async handler() {
          let a = await projectList({ pmId: this.staffid })
          this.projectTableData = a.data.pageInfo.tlist
          let b = await loadEvaluationData({ pmId: this.staffid })
          this.evaluationTableData = b.data.pageInfo.tlist
        },
      },
    },
    methods: {
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
      },
      close() {
        // this.form = this.$options.data().form
        this.dialogFormVisible = false
        this.allDisabled = false
        this.formData1 = {}
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
                ? this.formData.worktime.split('T')[0]
                : '',
            }
            //

            SaveOrUpdateStaff({
              data: JSON.stringify(data),
              workDate: JSON.stringify(params.workDate),
              birth: JSON.stringify(params.birth),
              attids,
            }).then((res) => {
              if (res.code == 1) {
                // this.dialogFormVisible = false
                this.$message({
                  type: 'success',
                  message: '提交成功',
                })
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
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      async showEdit(row, disabled, planNum) {
        const ids = row.cyurl.split('=')[1]
        this.staffid = ids
        this.dialogFormVisible = true
        if (this.staffid) {
          let a = await projectList({ pmId: this.staffid })
          this.projectTableData = a.data.pageInfo.tlist
          let b = await loadEvaluationData({ pmId: this.staffid })
          this.evaluationTableData = b.data.pageInfo.tlist
        } else {
          this.projectTableData = []
          this.evaluationTableData = []
        }

        if (disabled == '查看' || disabled == '传阅查看') {
          this.title = '查看'
          if (disabled == '传阅查看') {
            this.hideDescInput = true
          }
          this.disabledBtn = true
          this.lookDisablue = true
          this.getPersonFileList(ids)
          let res = await getBLDetail({
            cyId: row.cyid,
            staffid: ids,
          })
          //处理除了富文本框之外的按钮是否禁用
          let userInfo = JSON.parse(localStorage.getItem('userInfo'))
          // 当viewOppsiteProcessInfo接口中的cystaffid与获取用户信息中的staffid相等且cystate等于需调整 基本信息改成可编辑 编号不可编辑
          if (
            +row.cystaffid === userInfo.staffid &&
            res.data.cy.cystate == '需调整'
          ) {
            this.lookDisablue = false
            this.cloudEdit = true
          } else {
            this.cloudEdit = false
            this.lookDisablue = true
          }
          //保存数据用于通过或者驳回按钮
          this.staffID = res.data.cy.taskid
          this.taskId = res.data.task ? res.data.task.taskId : ''
          this.cyId = res.data.cy.cyid
          let res1 = await getPersonImgData({
            staffid: res.data.cy.taskid,
            taskId: res.data.cy.businesskey,
          })
          this.imgurl = res1.data.url
          this.personTableData = res1.data.taskList
          //保存参数，用于审批
          this.processDefinitionId = res.data.task
            ? res.data.task.processDefinitionId
            : ''
          this.processInstanceId = res.data.task
            ? res.data.task.processInstanceId
            : ''
          this.formData = {
            ...res.data.spstaff,
          }
          this.buttonList = res.data.btnList ? res.data.btnList : ['提交']
          // this.allDisabled = true
          GetStaffInfo({ staffId: ids }).then((res) => {
            const staff = res.data.staff
            const trainList = res.data.trainList
            this.tableDataProject = trainList
            if (staff.personType === '1') {
              this.outPersonCloudEdit = true
            } else {
              this.outPersonCloudEdit = false
            }
          })
        }
      },
      async getChildlistPro(val) {
        let res = await GetusrDetail({
          staffid: val[0].staffid,
        })
        const data = res.data.usrInfo
        this.formData = {
          realname: data.realname,
          memo: data.memo,
          // orgname: data.orgName,
          miblephone: data.miblephone,
          education: data.education,
          jobexperiences: data.jobexperiences,
          major: data.major,
          officephone: data.officephone,
          politicaloutlook: data.politicaloutlook,
          qualification: data.qualification,
          school: data.school,
          situation: data.situation,
          title: data.title,
          jobname: data.linkDetp.orgname,
          orgname: data.linkOrg.orgname,
        }
        // this.disabledEdit = true

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
      async handleDownload(row) {
        const res = await download({ attid: row.attid })
        this.downloadFileByBlob(res, row.name)
      },
      downloadFileByBlob(blob, fileName = 'file') {
        let blobUrl = window.URL.createObjectURL(blob)
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
      handleSubmit(index) {
        this.$refs['elForm1'].validate(async (valid) => {
          if (valid) {
            const params = {
              examination: this.formData1.optDesc,
              processDefinitionId: this.processDefinitionId,
              processInstanceId: +this.processInstanceId,
              staffid: +this.staffID,
              cyId: +this.cyId,
              taskId: this.taskId,
              transitionName: this.buttonList[index] || '提交',
            }
            let { code } = await handlePersonButtonClick({
              ...params,
            })
            if (code === 1) {
              this.$message.success('办理成功')
              this.dialogFormVisible = false
              this.$emit('fetch-data')
            } else {
              this.$message.success('办理失败')
              this.$emit('fetch-data')
            }
          }
        })
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
