<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14">
      <el-col :span="24">
        <el-divider>基本信息</el-divider>
      </el-col>
      <el-form ref="ruleForm" label-width="100px" :model="formData" size="mini">
        <el-col :span="12">
          <el-form-item label="人员" prop="realname">
            <el-input
              v-model="formData.realname"
              clearable
              placeholder="请选择人员"
              style="width: 80%"
              disabled
            />
            <el-button
              @click="projectManager"
              style="margin-left: 10px"
              type="primary"
              :disabled="!footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="年龄" prop="age">
            <el-input
              v-model="formData.age"
              clearable
              :disabled="!footer"
              placeholder="年龄"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="身份证号" prop="identityCard">
            <el-input
              v-model="formData.identityCard"
              clearable
              :disabled="!footer"
              placeholder="身份证号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="资格证书号" prop="certificationNumber">
            <el-input
              v-model="formData.certificationNumber"
              clearable
              :disabled="!footer"
              placeholder="律师资格或法律职业资格证书号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="公司律师执业证号" prop="certificationNumber">
            <el-input
              v-model="formData.certificateNumberFileIds"
              clearable
              :disabled="!footer"
              placeholder="公司律师执业证号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="毕业学校" prop="schooloFgraduation">
            <el-input
              v-model="formData.schooloFgraduation"
              clearable
              :disabled="!footer"
              placeholder="毕业学校"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="专业" prop="specialty">
            <el-input
              v-model="formData.specialty"
              clearable
              :disabled="!footer"
              placeholder="专业"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="具体工作部门" prop="workDepartment">
            <el-input
              v-model="formData.workDepartment"
              clearable
              :disabled="!footer"
              placeholder="具体工作部门"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="邮编" prop="postalCode">
            <el-input
              v-model="formData.postalCode"
              clearable
              :disabled="!footer"
              placeholder="邮编"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="姓名" prop="practiceApplyName">
            <el-input
              v-model="formData.practiceApplyName"
              clearable
              :disabled="!footer"
              placeholder="姓名"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="性别" prop="sex">
            <el-select
              :style="{ width: '100%' }"
              v-model="formData.sex"
              placeholder="性别"
              :disabled="!footer"
            >
              <el-option label="男" value="1" />
              <el-option label="女" value="0" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="民族" prop="nation">
            <el-input
              v-model="formData.nation"
              clearable
              :disabled="!footer"
              placeholder="民族"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="政治面貌" prop="politicsStatus">
            <el-input
              v-model="formData.politicsStatus"
              clearable
              :disabled="!footer"
              placeholder="政治面貌"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="住所地址" prop="homeAddress">
            <el-input
              v-model="formData.homeAddress"
              clearable
              :disabled="!footer"
              placeholder="住所地址"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="最高学历" prop="highestEducation">
            <el-select
              :style="{ width: '100%' }"
              v-model="formData.highestEducation"
              placeholder="最高学历"
              :disabled="!footer"
            >
              <el-option label="博士研究生" value="1" />
              <el-option label="硕士研究生" value="2" />
              <el-option label="大学本科" value="3" />
              <el-option label="大学专科" value="4" />
              <el-option label="中专" value="5" />
              <el-option label="大学及以下" value="6" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="现工作单位" prop="nowWorkUnit">
            <el-input
              v-model="formData.nowWorkUnit"
              clearable
              :disabled="!footer"
              placeholder="现工作单位"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="办公电话" prop="officePhone">
            <el-input
              v-model="formData.officePhone"
              clearable
              :disabled="!footer"
              placeholder="办公电话"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="电话" prop="phone">
            <el-input
              v-model="formData.phone"
              clearable
              :disabled="!footer"
              placeholder="电话"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <!-- <el-form-item label="照片" prop="img">
            <el-upload
              class="avatar-uploader"
              :action="baseApi + api"
              :show-file-list="false"
              :headers="headers"
              :on-preview="handlePreview"
              :on-success="handleSuccess"
              :file-list="formData.photo"
            >
              <img
                v-if="formData.photo"
                :src="baseApi + formData.photo"
                class="avatar"
              />
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </el-form-item> -->
        <el-col :span="12">
          <el-form-item label="技术职务" prop="technicalPosition">
            <el-input
              v-model="formData.technicalPosition"
              clearable
              :disabled="!footer"
              placeholder="取得何种技术职务"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="外语水平" prop="foreignLanguageLevel">
            <el-input
              v-model="formData.foreignLanguageLevel"
              clearable
              :disabled="!footer"
              placeholder="掌握何种外语水平"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="受过何种奖励" prop="award">
            <el-input
              v-model="formData.award"
              clearable
              type="textarea"
              rows="2"
              :disabled="!footer"
              placeholder="受过何种奖励"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="受过何种处分" prop="punishment">
            <el-input
              v-model="formData.punishment"
              clearable
              type="textarea"
              rows="2"
              :disabled="!footer"
              placeholder="受过何种处分"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>简历</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-button type="success" @click="handleAdd">新建</el-button>
          </div>
        </el-col>
        <el-col :span="24">
          <el-table :data="tableData">
            <el-table-column align="center" label="开始时间" prop="stareTime" />
            <el-table-column align="center" label="结束时间" prop="endTime" />
            <el-table-column
              align="center"
              label="在何地何部门(学习)工作"
              prop="oldWorkUnit"
            />
            <el-table-column align="center" label="职务" prop="position" />
            <el-table-column align="center" label="备注" prop="remark" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
              v-if="footer"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleEdit(row)">编辑</el-button>
                <el-button type="text" @click="handleDelete1(row)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>

        <el-col :span="24">
          <el-divider>合同审查</el-divider>
        </el-col>
        <!-- <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-button type="success" @click="handleAdd">新建</el-button>
          </div>
        </el-col> -->
        <el-col :span="24">
          <el-table :data="tableData1">
            <el-table-column align="center" label="序号" prop="text1" />
            <el-table-column align="center" label="合同编号" prop="text2" />
            <el-table-column align="center" label="金额" prop="text3" />
            <el-table-column align="center" label="审核时间" prop="text4" />
            <el-table-column align="center" label="审核人" prop="text5" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleEdit(row)">编辑</el-button>
                <el-button
                  type="text"
                  @click="handleDelete1(row)"
                  v-if="footer"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24">
          <el-divider>诉讼代理</el-divider>
        </el-col>
        <!-- <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-button type="success" @click="handleAdd">新建</el-button>
          </div>
        </el-col> -->
        <el-col :span="24">
          <el-table :data="tableData2">
            <el-table-column align="center" label="名称" prop="text1" />
            <el-table-column align="center" label="案号" prop="text1" />
            <el-table-column align="center" label="原告" prop="text1" />
            <el-table-column align="center" label="被告" prop="text1" />
            <el-table-column align="center" label="标的金额" prop="text1" />
            <el-table-column align="center" label="立案日期" prop="text1" />
            <el-table-column align="center" label="审理阶段" prop="text1" />
            <el-table-column align="center" label="代理人" prop="text1" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleEdit(row)">编辑</el-button>
                <el-button
                  type="text"
                  @click="handleDelete1(row)"
                  v-if="footer"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24">
          <el-divider>法律审核</el-divider>
        </el-col>
        <!-- <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-button type="success" @click="handleAdd">新建</el-button>
          </div>
        </el-col> -->
        <el-col :span="24">
          <el-table :data="tableData3">
            <el-table-column align="center" label="项目名称" prop="stareTime" />
            <el-table-column align="center" label="项目概述" prop="endTime" />
            <el-table-column
              align="center"
              label="审核时间"
              prop="oldWorkUnit"
            />
            <el-table-column align="center" label="参训人员" prop="审核人" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleEdit(row)">编辑</el-button>
                <el-button
                  type="text"
                  @click="handleDelete1(row)"
                  v-if="footer"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <!-- <el-col :span="24">
          <el-table :data="tableData3">
            <el-table-column align="center" label="培训主题" prop="stareTime" />
            <el-table-column align="center" label="培训内容" prop="endTime" />
            <el-table-column
              align="center"
              label="培训时间"
              prop="oldWorkUnit"
            />
            <el-table-column align="center" label="参训人员" prop="position" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleEdit(row)">编辑</el-button>
                <el-button
                  type="text"
                  @click="handleDelete1(row)"
                  v-if="footer"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col> -->
        <el-col :span="24">
          <el-divider>文章发表</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-button type="success" @click="handleAdd1">新建</el-button>
          </div>
        </el-col>
        <el-col :span="24">
          <el-table :data="tableData4">
            <el-table-column align="center" label="题目" prop="activityName" />
            <el-table-column
              align="center"
              label="发表载体"
              prop="articlePublishPublishingCarrier"
            />
            <el-table-column
              align="center"
              label="发表时间"
              prop="activityTime"
            />
            <el-table-column
              align="center"
              label="作者"
              prop="articlePublishAuthor"
            />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleEdit1(row)">
                  编辑
                </el-button>
                <el-button
                  type="text"
                  @click="handleDelete1(row)"
                  v-if="footer"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24">
          <el-divider>法律培训</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-button type="success" @click="handleAdd2">新建</el-button>
          </div>
        </el-col>
        <el-col :span="24">
          <el-table :data="tableData5">
            <el-table-column
              align="center"
              label="培训主题"
              prop="legalTrainingTopic"
            />
            <el-table-column
              align="center"
              label="培训内容"
              prop="legalTrainingContent"
            />
            <el-table-column
              align="center"
              label="培训时间"
              prop="legalAuditTime"
            />
            <el-table-column
              align="center"
              label="参训人员"
              prop="creatorName"
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
                  @click="handleDelete2(row)"
                  v-if="footer"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24">
          <el-divider>法律尽调</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-button type="success" @click="handleAdd3">新建</el-button>
          </div>
        </el-col>
        <el-col :span="24">
          <el-table :data="tableData6">
            <el-table-column
              align="center"
              label="尽调单位"
              prop="legalAdjustmentWorkUnit"
            />
            <el-table-column
              align="center"
              label="尽调内容"
              prop="legalAdjustmentContent"
            />
            <el-table-column
              align="center"
              label="尽调时间"
              prop="activityTime"
            />
            <el-table-column align="center" label="尽调人" prop="creatorName" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleEdit3(row)">
                  编辑
                </el-button>
                <el-button
                  type="text"
                  @click="handleDelete3(row)"
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
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </div>
    <template #footer v-if="footer">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </template>
    <resumeView ref="resumeView" @addList="addList" />
    <project-manage
      @projectManage="getChildlistPro"
      ref="manage"
    ></project-manage>
    <fljdView ref="fljdView" />
    <flpxView ref="flpxView" />
    <wzfbView ref="wzfbView" />
  </el-dialog>
</template>

<script>
  import store from '@/store'
  import projectManage from '@/views/audit/project/components/formComponents/projectManage.vue'
  import fljdView from './fljdView.vue'
  import flpxView from './flpxView.vue'
  import resumeView from './resumeView.vue'
  import wzfbView from './wzfbView.vue'

  import {
    addGSLS,
    deleteFile,
    deleteJLList,
    getGSLSDefaultInfo,
    uploadApi,
    rytzpersonnel,
  } from '@/api/fwgl/zzxx'
  import { GetusrDetail } from '@/api/setting/personnel'
  import { baseURL } from '@/config'
  const token = store.getters['user/token']
  export default {
    components: { resumeView, projectManage, fljdView, flpxView, wzfbView },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        formData: {
          practiceApplyName: '',
          age: '',
          identityCard: '',
          certificationNumber: '',
          schooloFgraduation: '',
          specialty: '',
          workDepartment: '',
          postalCode: '',
          sex: '',
          nation: '',
          politicsStatus: '',
          homeAddress: '',
          highestEducation: '',
          nowWorkUnit: '',
          officePhone: '',
          phone: '',
          technicalPosition: '',
          foreignLanguageLevel: '',
          award: '',
          punishment: '',
          fileIds: '',
          lawyerExtId: '',
        },
        dialogFormVisible: false,
        title: '详情',
        footer: true,
        tableData: [],
        tableData1: [],
        tableData2: [],
        tableData3: [],
        tableData4: [],
        tableData5: [],
        tableData6: [],
        fileList: [],
        baseApi: baseURL,
        api: uploadApi,
        headers: { token: token },
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      /**
       * @description: 选择组件回调
       * @param {*} val 已选数据
       * @return {*}
       */      
      async getChildlistPro(val) {
        let res = await GetusrDetail({
          staffid: val[0].staffid,
        })
        const data = res.data.usrInfo
        this.formData = {
          realname: data.realname,
          practiceApplyName: data.realname,
          // orgname: data.orgName,
          phone: data.miblephone,
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
      /**
       * @description: 打开选择人员组件
       * @return {*}
       */      
      projectManager() {
        this.$refs['manage'].showEdit()
      },
      /**
       * @description: 外部打开dialog
       * @param {*} title 表单类型
       * @param {*} row 行数据
       * @return {*}
       */      
      showEdit(title, row) {
        // return
        this.dialogFormVisible = true
        this.fileList = []
        this.tableData = []
        if (row) {
          rytzpersonnel({
            id: row.practiceApplyId,
          }).then((res) => {
            this.formData = Object.assign({}, res.data.personnel)
            this.formData.realname = res.data.personnel.practiceApplyName
            if (res.data.files) {
              this.fileList = res.data.files
            }
            if (res.data.practiceApplyExt) {
              this.tableData = res.data.practiceApplyExt
            }
            if (res.data.legalList) {
              this.tableData6 = res.data.legalList
            }
            if (res.data.legalReviewList) {
              this.tableData5 = res.data.legalReviewList
            }
            if (res.data.articleList) {
              this.tableData4 = res.data.articleList
            }
            // this.formData.sex = res.data.practiceApply.sex.toString()
          })
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else {
          this.title = '新增'
          this.formData = this.$options.data().formData
        }
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */      
      handleAdd() {
        this.$refs['resumeView'].showEdit('add', null)
      },
      /**
       * @description: 打开编辑表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */      
      async handleEdit(row) {
        // const data = await approachSummaryDisp({ enterid: row.enterid })
        await this.$refs['resumeView'].showEdit('edit', row)
      },
      addList(value) {
        const aaa = this.tableData.findIndex(
          (x) => x.practiceApplyExtId == value.practiceApplyExtId
        )
        if (aaa !== -1) {
          this.tableData.splice(aaa, 1, value)
        } else {
          this.tableData.push(value)
        }
      },
      handleDelete1(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await deleteJLList({
            id: row.practiceApplyExtId,
          })
          if (code == 200) {
            this.tableData.splice(
              this.tableData.findIndex(
                (x) => x.practiceApplyExtId == row.practiceApplyExtId
              ),
              1
            )
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          }
        })
      },
      /**
       * @description: 保存表单
       * @return {*}
       */      
      add() {
        if (this.fileList.length !== 0) {
          const bb = []
          this.fileList.forEach((e) => {
            bb.push(e.fileId)
          })
          this.formData.fileIds = bb.toString()
        }
        if (this.tableData.length !== 0) {
          const aa = []
          this.tableData.forEach((e) => {
            aa.push(e.practiceApplyExtId)
          })

          this.formData.practiceApplyExtId = aa.toString()
        }
        this.$emit('add', this.formData)
        addGSLS(this.formData).then((res) => {
          if (res.msg == '成功') {
            this.dialogFormVisible = false
            this.$emit('fetchData')
          }
        })
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.footer = true
        this.dialogFormVisible = false
      },
      // 照片上传
      handlePreview() {},
      handleSuccess(res) {
        if (res.code == 200) {
          this.formData.photo = res.data.fileIds[0].filePath
        }
      },
      //文件上传
      handleAvatarSuccess(res) {
        if (res.code == 200) {
          this.fileList.push(res.data.fileIds[0])
        }
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */      
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await deleteFile({ id: row.fileId })
          if (code == 200) {
            this.fileList.splice(
              this.fileList.findIndex((x) => x.fileId == row.fileId),
              1
            )
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          }
        })
      },
      handleAdd1() {
        this.$refs['wzfbView'].showEdit('add', null)
      },
      async handleEdit1(row) {
        // const data = await approachSummaryDisp({ enterid: row.enterid })
        await this.$refs['wzfbView'].showEdit('edit', row)
      },
      handleAdd2() {
        this.$refs['flpxView'].showEdit('add', null)
      },
      async handleEdit2(row) {
        // const data = await approachSummaryDisp({ enterid: row.enterid })
        await this.$refs['flpxView'].showEdit('edit', row)
      },
      handleAdd3() {
        this.$refs['fljdView'].showEdit('add', null)
      },
      async handleEdit3(row) {
        // const data = await approachSummaryDisp({ enterid: row.enterid })
        await this.$refs['fljdView'].showEdit('edit', row)
      },
    },
  }
</script>
<style scoped>
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
  .avatar-uploader .el-upload {
    width: 178px;
    height: 178px;
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409eff;
  }
  .avatar-uploader-icon {
    border: 1px dashed #d9d9d9;
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>
