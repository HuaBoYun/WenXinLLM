<template>
  <div>
    <div>
      <el-row :gutter="15">
        <el-form
          ref="elForm"
          :disabled="allDisabled"
          label-width="125px"
          :model="formData"
          :rules="rules"
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
                :disabled="lookDisablue"
                :style="{ width: '100%' }"
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
          </el-col>
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
      <!-- <template v-if="!lookDisablue" #footer>
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </template> -->
    </div>
    <div
      style="text-align: right; margin-top: 10px; margin-right: 10px"
      v-if="!lookDisablue"
    >
      <!-- <el-button @click="close">取 消</el-button> -->
      <el-button type="primary" @click="add">确 定</el-button>

      <el-button type="primary" @click="ymsubmit">提 交</el-button>
    </div>

    <project-manage
      @projectManage="getChildlistPro"
      ref="manage"
    ></project-manage>
    <evaluationEdit ref="edit" />
    <IndexEdit ref="edit" />
    <!-- 提交 -->
    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
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
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import {
    GetStaffInfo,
    GetusrDetail,
    MergePlanProjectManageInfo,
    RemoveTrain,
    SaveOrUpdateStaff,
  } from '@/api/setting/personnel'
  import store from '@/store'
  import projectManage from '@/views/audit/project/components/formComponents/projectManage.vue'
  import ZXPerson from '@/components/selectPerson.vue'
  import { hasMJ, couldMJ } from '@/utils'
  import { getSPMJ } from '@/api/setting/mjsz'
  export default {
    name: 'xxxx',
    components: {
      projectManage,
      evaluationEdit,
      IndexEdit,
      Resubmit,
      ZXPerson,
    },
    data() {
      return {
        baseApi:
          process.env.NODE_ENV === 'development'
            ? '/vab-mock-server/audit'
            : process.env.VUE_APP_BASE_API,
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
        ymFromId: 0,
        fromId: 0,
        fromIdcopy: 0,
        flowtaskinfoflowid: '',
        status: '',
        mjId: '', // 密级id
        MJoption: [],
        showMJ: false,
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
      handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.formData.staffScopeIds = ids
        this.formData.staffScopeNames = names
      },
      async getMJData(id) {
        this.showMJ = couldMJ()
        if (this.showMJ) {
          const res2 = await getSPMJ({ flowType: id })
          this.MJoption = res2.data
        }
      },
      close() {
        // this.form = this.$options.data().form
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
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
        }
        this.$bus.$emit('updateMsg', 0)
        this.dialogFormVisible = false
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
      add() {
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
              orgname: this.formData.orgname,
              jobname: this.formData.jobname,
              secrectLevelId: this.formData.secrectLevelId,
              staffScopeNames: this.formData.staffScopeNames,
              staffScopeIds: this.formData.staffScopeIds,
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
                // this.dialogFormVisible = false
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
      async showEdit(
        row,
        disabled,
        planNum,
        fromId,
        flowtaskinfoflowid,
        ymFromId,
        status,
        flowType
      ) {
        // this.dialogFormVisible = true
        if (flowType) {
          this.getMJData(flowType)
        }
        if (fromId) {
          this.fromId = fromId
          this.fromIdcopy = fromId // fromId为-1时，拷贝一份
        }
        if (flowtaskinfoflowid) {
          this.flowtaskinfoflowid = flowtaskinfoflowid
        }
        if (ymFromId) {
          this.ymFromId = ymFromId
        }

        this.status = status

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
              secrectLevelId: staff.secrectLevelId,
              staffScopeNames: staff.staffScopeNames,
              staffScopeIds: staff.staffScopeIds,
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
      async handleDownload(row) {
        const res = await download({ attId: row.attid })
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
      //提交
      async ymsubmit() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            this.$refs.resubmit.ymsubmit()
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
