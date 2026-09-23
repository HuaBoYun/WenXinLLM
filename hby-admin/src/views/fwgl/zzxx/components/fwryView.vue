<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1200px"
    @close="close"
  >
    <el-row :gutter="14">
      <el-col :span="24">
        <el-divider>基本信息</el-divider>
      </el-col>
      <el-form
        ref="ruleForm"
        label-width="110px"
        :model="formData"
        size="mini"
        :rules="rules"
      >
        <el-col :span="12">
          <el-form-item label="所属集团" prop="belongGroupName">
            <el-input
              v-model="formData.belongGroupName"
              disabled
              :placeholder="footer ? '请输入所属集团' : ''"
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
          <el-form-item label="姓名" prop="personnelName">
            <el-input
              v-model="formData.personnelName"
              :placeholder="footer ? '姓名' : ''"
              :disabled="!footer"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="工作性质" prop="jobNature">
            <el-input
              v-model="formData.jobNature"
              clearable
              :disabled="!footer"
              :placeholder="footer ? '工作性质' : ''"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="学历" prop="education">
            <el-select
              :style="{ width: '100%' }"
              v-model="formData.education"
              :disabled="!footer"
              :placeholder="footer ? '学历' : ''"
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
          <el-form-item label="职称" prop="dutyTitle">
            <el-input
              v-model="formData.dutyTitle"
              clearable
              :disabled="!footer"
              :placeholder="footer ? '职称' : ''"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否法律专业" prop="isLawSpecialty">
            <el-select
              :style="{ width: '100%' }"
              v-model="formData.isLawSpecialty"
              :placeholder="footer ? '是否法律专业' : ''"
              :disabled="!footer"
            >
              <el-option label="是" :value="1" />
              <el-option label="否" :value="0" />
            </el-select>
          </el-form-item>
        </el-col>
        <!-- <el-form-item label="法律顾问资格" prop="isLawAdviser">
            <el-select
              :style="{ width: '100%' }"
              v-model="formData.isLawAdviser"
              :disabled="!footer"
              :placeholder="footer?是否取得法律顾问资格"
            >
              <el-option label="是" :value="1" />
              <el-option label="否" :value="0" />
            </el-select>
          </el-form-item> -->
        <el-col :span="12">
          <el-form-item label="联系电话" prop="phone">
            <el-input
              v-model="formData.phone"
              clearable
              :placeholder="footer ? '联系电话' : ''"
              :disabled="!footer"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="终止聘用时间" prop="endHireTime">
            <el-date-picker
              :style="{ width: '100%' }"
              v-model="formData.endHireTime"
              :placeholder="footer ? '终止聘用时间' : ''"
              type="date"
              :disabled="!footer"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd HH:mm:ss"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="填报时间" prop="fillInTime">
            <el-date-picker
              :style="{ width: '100%' }"
              v-model="formData.fillInTime"
              :placeholder="footer ? '填报时间' : ''"
              type="date"
              :disabled="!footer"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd HH:mm:ss"
            />
          </el-form-item>
        </el-col>
        <!-- <el-form-item label="单据状态" prop="businessType">
            <el-input
              v-model="formData.businessType"
              clearable
              :placeholder="footer?单据状态"
              :style="{ width: '100%' }"
            />
          </el-form-item> -->
        <el-col :span="12">
          <el-form-item label="工作单位" prop="workUnitName">
            <el-input
              v-model="formData.workUnitName"
              disabled
              :placeholder="footer ? '工作单位' : ''"
              :style="{ width: '80%' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="showGroupLeader('unit')"
              :disabled="!footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="性别" prop="sex">
            <el-select
              :style="{ width: '100%' }"
              v-model="formData.sex"
              :placeholder="footer ? '性别' : ''"
              :disabled="!footer"
            >
              <el-option label="男" :value="1" />
              <el-option label="女" :value="0" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="身份证号" prop="identityCard">
            <el-input
              v-model="formData.identityCard"
              clearable
              :placeholder="footer ? '身份证号' : ''"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="学位" prop="degree">
            <el-input
              v-model="formData.degree"
              clearable
              :placeholder="footer ? '学位' : ''"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="参加工作时间" prop="startWorkTime">
            <el-date-picker
              :style="{ width: '100%' }"
              v-model="formData.startWorkTime"
              :placeholder="footer ? '参加工作时间' : ''"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd HH:mm:ss"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="法律职业资格" prop="isLawOccupational">
            <el-select
              :style="{ width: '100%' }"
              v-model="formData.isLawOccupational"
              :placeholder="footer ? '是否取得法律职业资格' : ''"
              :disabled="!footer"
            >
              <el-option label="是" :value="1" />
              <el-option label="否" :value="0" />
            </el-select>
          </el-form-item>
        </el-col>
        <!-- <el-form-item label="资格证编号" prop="qualifications">
            <el-input
              v-model="formData.qualifications"
              clearable
              :disabled="!footer"
              :placeholder="footer?法律顾问资格证编号"
              :style="{ width: '100%' }"
            />
          </el-form-item> -->
        <el-col :span="12">
          <el-form-item label="毕业院校" prop="graduationGraduate">
            <el-input
              v-model="formData.graduationGraduate"
              clearable
              :disabled="!footer"
              :placeholder="footer ? '毕业院校' : ''"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否聘用" prop="isHire">
            <el-select
              :disabled="!footer"
              :style="{ width: '100%' }"
              v-model="formData.isHire"
              :placeholder="footer ? '是否聘用' : ''"
            >
              <el-option label="是" :value="1" />
              <el-option label="否" :value="0" />
            </el-select>
          </el-form-item>
        </el-col>
        <!-- <el-form-item label="审批人" prop="auditPerson">
            <el-input
              v-model="formData.auditPerson"
              clearable
              disabled
              :placeholder="footer?审批人"
              :style="{ width: '80%' }"
            />
            <el-button
              @click="projectManager"
              style="margin-left: 10px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item> -->

        <el-col :span="12">
          <el-form-item label="出生年月" prop="birthday">
            <el-date-picker
              :disabled="!footer"
              :style="{ width: '100%' }"
              v-model="formData.birthday"
              :placeholder="footer ? '出生年月' : ''"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd HH:mm:ss"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="政治面貌" prop="politicsStatus">
            <!-- <el-input
              v-model="formData.politicsStatus"
              clearable
              :disabled="!footer"
              :placeholder="footer?政治面貌"
              :style="{ width: '100%' }"
            /> -->
            <el-select
              :disabled="!footer"
              :style="{ width: '100%' }"
              v-model="formData.politicsStatus"
              :placeholder="footer ? '政治面貌' : ''"
            >
              <el-option label="中共党员" value="中共党员" />
              <el-option label="中共预备党员" value="中共预备党员" />
              <el-option label="共青团员" value="共青团员" />
              <el-option label="其他党派" value="其他党派" />
              <el-option label="群众" value="群众" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="职务" prop="position">
            <el-input
              v-model="formData.position"
              clearable
              :disabled="!footer"
              :placeholder="footer ? '职务' : ''"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="起始年份" prop="startYear">
            <el-date-picker
              :style="{ width: '100%' }"
              v-model="formData.startYear"
              :placeholder="footer ? '从事法律顾问起始年份' : ''"
              type="year"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="资格证书编号"
            prop="certificationNumber"
            v-if="this.formData.isLawOccupational == 1"
          >
            <el-input
              v-model="formData.certificationNumber"
              clearable
              :disabled="!footer"
              :placeholder="footer ? '法律职业资格证书编号' : ''"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <!-- <el-form-item label="最后注册时间" prop="lateRegisterTime">
            <el-date-picker
              :style="{ width: '100%' }"
              v-model="formData.lateRegisterTime"
              :placeholder="footer?法律顾问资格证最后注册时间"
              type="date"
              :disabled="!footer"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd HH:mm:ss"
            />
          </el-form-item> -->
        <el-col :span="12">
          <el-form-item label="开始聘用时间" prop="startHireTime">
            <el-date-picker
              :style="{ width: '100%' }"
              v-model="formData.startHireTime"
              :placeholder="footer ? '开始聘用时间' : ''"
              type="date"
              :disabled="!footer"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd HH:mm:ss"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="填报人" prop="creatorName">
            <el-input
              v-model="formData.creatorName"
              clearable
              disabled
              :placeholder="footer ? '填报人' : ''"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <!-- <el-form-item label="审批时间" prop="auditTime">
            <el-date-picker
              :style="{ width: '100%' }"
              v-model="formData.auditTime"
              :placeholder="footer?审批时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd HH:mm:ss"
            />
          </el-form-item> -->

        <el-col :span="24">
          <el-divider>工作经历</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-button type="success" @click="handleAdd">新建</el-button>
          </div>
        </el-col>
        <el-col :span="24">
          <el-table :data="tableData">
            <el-table-column align="center" label="开始时间" prop="startTime">
              <template #default="{ row }">
                <el-button @click="handleEdit('detail', row)" type="text">
                  {{ formatDay(row.startTime) }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column align="center" label="结束时间" prop="endTime">
              <template #default="{ row }">
                {{ formatDay(row.endTime) }}
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="工作单位"
              prop="workUnitExt"
            />
            <el-table-column align="center" label="职务" prop="position" />
            <!-- <el-table-column
              align="center"
              label="工作成果"
              prop="workAchievement"
            /> -->
            <el-table-column align="center" label="备注" prop="remark" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="handleEdit('edit', row)"
                  v-if="footer"
                >
                  编辑
                </el-button>
                <el-button type="text" @click="handleDelete(row)" v-if="footer">
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
      <el-button
        v-if="
          (formData.state == 2 || formData.state == 3) && jurisdictionCode == 1
        "
        @click="ymsubmit"
        type="primary"
      >
        提交
      </el-button>
    </template>
    <experienceView ref="experienceView" @addList="addList" :gzjltype="2" />

    <project-manage
      @projectManage="getChildlistPro"
      ref="manage"
    ></project-manage>
    <company-select-modal
      ref="companySelect"
      @selected="handleCompanyTreeSelected"
    />
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
  </el-dialog>
</template>

<script>
  import { addFWRY, deleteGZJLList, getFWRYDefaultInfo } from '@/api/fwgl/zzxx'
  import CompanySelectModal from '@/components/CampanySelectModal'
  import projectManage from '@/views/audit/project/components/formComponents/projectManage.vue'
  import experienceView from './experienceView.vue'
  import {
    getFlowTaskInfo,
    ymWorkCandidates,
    ymWorkSubmit,
  } from '@/api/contract/manage'
  import { getFaqiInfo } from '@/api/setting/msg'
  import CandidateUserSelect from '@/components/CandidateUserSelect'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import { formatDay } from '@/utils'
  export default {
    components: {
      experienceView,
      projectManage,
      CompanySelectModal,
      CandidateUserSelect,
      Resubmit,
    },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        formData: {
          auditPerson: '',
          auditTime: '',
          birthday: '',
          certificationNumber: '',
          degree: '',
          duty: '',
          dutyTitle: '',
          education: '',
          endHireTime: '',
          creatorName: '',
          fillInTime: '',
          graduationGraduate: '',
          identityCard: '',
          isHire: '',
          isLawAdviser: '',
          isLawOccupational: '',
          isLawSpecialty: '',
          jobNature: '',
          lateRegisterTime: '',
          legalStartTime: '',
          personnelId: '',
          personnelName: '',
          phone: '',
          politicsStatus: '',
          position: '',
          qualifications: '',
          sex: '',
          startHireTime: '',
          startWorkTime: '',
          startYear: '',
          personnelExtId: '',
          state: '',
        },
        formatDay: formatDay,
        rules: {
          belongGroupName: [
            { required: true, message: '请输入所属集团', trigger: 'blur' },
          ],
          personnelName: [
            { required: true, message: '请输入姓名', trigger: 'blur' },
          ],
          jobNature: [
            { required: true, message: '请输入工作性质', trigger: 'blur' },
          ],
          isLawSpecialty: [
            {
              required: true,
              message: '请选择是否法律专业',
              trigger: 'change',
            },
          ],
          isLawAdviser: [
            { required: true, message: '请输入法律顾问资格', trigger: 'blur' },
          ],
          phone: [
            { required: true, message: '请输入联系电话', trigger: 'blur' },
          ],
          workUnitName: [
            { required: true, message: '请输入工作单位', trigger: 'blur' },
          ],
          sex: [{ required: true, message: '请输入性别', trigger: 'blur' }],
          identityCard: [
            { required: true, message: '请输入身份证号', trigger: 'blur' },
          ],
          startWorkTime: [
            { required: true, message: '请输入参加工作时间', trigger: 'blur' },
          ],
          isLawOccupational: [
            { required: true, message: '请输入法律职业资格', trigger: 'blur' },
          ],
          birthday: [
            { required: true, message: '请输入出生年月', trigger: 'blur' },
          ],
          politicsStatus: [
            { required: true, message: '请输入政治面貌', trigger: 'blur' },
          ],
          startYear: [
            {
              required: true,
              message: '请输入从事法律顾问起始年份',
              trigger: 'blur',
            },
          ],
          startHireTime: [
            { required: true, message: '请输入开始聘用时间', trigger: 'blur' },
          ],
        },
        dialogFormVisible: false,
        title: '详情',
        footer: true,
        tableData: [],
        //提交
        jurisdictionCode: 0,
        fromId: 0,
        fromIdcopy: 0,
        ymFromId: 0,
        flowtaskinfoflowid: '',
        status: '',
      }
    },
    computed: {
      userInfo() {
        return JSON.parse(localStorage.getItem('userInfo'))
      },
    },
    watch: {},
    created() {},
    mounted() {},
    methods: {
      /**
       * @description: 外部打开dialog
       * @param {*} title 表单类型
       * @param {*} row 行数据
       * @return {*}
       */      
      showEdit(title, row) {
        this.dialogFormVisible = true

        this.tableData = []
        if (row) {
          getFWRYDefaultInfo({
            id: row.personnelId,
          }).then(async (res) => {
            this.$refs['ruleForm'].resetFields()

            this.formData = Object.assign({}, res.data.legalPersonnel)

            if (
              res.data.legalPersonnel.state == 2 ||
              res.data.legalPersonnel.state == 3
            ) {
              const res2 = await getFlowTaskInfo({
                tableId: 26,
                formId: row.personnelId,
              })
              this.jurisdictionCode = res2.data.isFlowInfo
              if (res2.data.isFlowInfo) {
                this.flowtaskinfoflowid = res2.data.flowId + ''
                this.fromId = row.personnelId + ''
                this.fromIdcopy = row.personnelId + ''
                this.ymFromId = res2.data.id + ''

                const res3 = await getFaqiInfo({
                  id: res2.data.id,
                  flowId: res2.data.flowId,
                })
                if (res3.code == 1) {
                  this.status = res3.data.dataJson.flowTaskInfo.status
                }
              }
            }
            if (res.data.legalPersonnelExt) {
              this.tableData = res.data.legalPersonnelExt
            }
            // this.formData.sex = res.data.legalAdviser.sex.toString()
            // this.formData.isHire = res.data.legalAdviser.isHire.toString()
            // this.formData.isSoleDuty =
            //   res.data.legalAdviser.isSoleDuty.toString()
          })
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else {
          this.title = '新增'
          this.reset()
        }
      },
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
       * @description: 选择公司部门回调
       * @param {*} val 已选数据
       * @return {*}
       */      
      handleCompanyTreeSelected(val) {
        this.$set(this.formData, val.idKey, val.id)
        this.$set(this.formData, val.labelKey, val.label)
      },
      /**
       * @description: 打开选择人员组件
       * @return {*}
       */      
      projectManager() {
        this.$refs['manage'].showEdit()
      },
      /**
       * @description: 选择组件回调
       * @param {*} val 已选数据
       * @return {*}
       */      
      async getChildlistPro(val) {
        this.$set(this.formData, 'auditPerson', val[0].staffid)
      },
      reset() {
        this.formData = {
          auditPerson: '',
          auditTime: '',
          birthday: '',
          certificationNumber: '',
          degree: '',
          duty: '',
          dutyTitle: '',
          education: '',
          endHireTime: '',
          creatorName: '',
          fillInTime: '',
          graduationGraduate: '',
          identityCard: '',
          isHire: '',
          isLawAdviser: '',
          isLawOccupational: '',
          isLawSpecialty: '',
          jobNature: '',
          lateRegisterTime: '',
          legalStartTime: '',
          personnelId: '',
          personnelName: '',
          phone: '',
          politicsStatus: '',
          position: '',
          qualifications: '',
          sex: '',
          startHireTime: '',
          startWorkTime: '',
          startYear: '',
          personnelExtId: '',
        }
        this.formData.belongGroupName = '浙江省国有资本运营有限公司'
        this.formData.creatorName = this.userInfo.realname
        this.$refs['ruleForm'].resetFields()
      },
      addList(value) {
        const aaa = this.tableData.findIndex(
          (x) => x.personnelExtId == value.personnelExtId
        )
        if (aaa !== -1) {
          this.tableData.splice(aaa, 1, value)
        } else {
          this.tableData.push(value)
        }
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */      
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await deleteGZJLList({ id: row.personnelExtId })
          if (code == 200) {
            this.tableData.splice(
              this.tableData.findIndex(
                (x) => x.personnelExtId == row.personnelExtId
              ),
              1
            )
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          }
        })
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */      
      handleAdd() {
        this.$refs['experienceView'].showEdit('add', null)
      },
      async handleEdit(title, row) {
        // const data = await approachSummaryDisp({ enterid: row.enterid })
        await this.$refs['experienceView'].showEdit(title, row)
      },
      /**
       * @description: 保存表单
       * @return {*}
       */      
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const aa = []
            this.tableData.forEach((e) => {
              aa.push(e.personnelExtId)
            })
            this.formData.personnelExtId = aa.toString()

            addFWRY(this.formData).then((res) => {
              if (res.msg == '成功') {
                this.dialogFormVisible = false
                this.$emit('fetchData')
              }
            })
          }
        })
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.footer = true
        this.clearType = true
        this.$emit('fetchData')
        this.dialogFormVisible = false
      },
      /**
       * @description: 流程提交
       * @return {*}
       */      
      async ymsubmit() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.$refs.resubmit.ymsubmit()
          }
        })
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
