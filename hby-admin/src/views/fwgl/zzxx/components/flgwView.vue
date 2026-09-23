<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1200px"
    @close="close"
    v-if="dialogFormVisible"
  >
    <el-row :gutter="14">
      <el-col :span="24">
        <el-divider>基本信息</el-divider>
      </el-col>
      <el-form
        ref="ruleForm"
        label-width="120px"
        :model="formData"
        size="mini"
        :rules="rules"
      >
        <el-col :span="12">
          <el-form-item label="所属集团" prop="belongGroupName">
            <el-input disabled v-model="formData.belongGroupName" clearable />
            <!-- placeholder="请输入所属集团" -->
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
          <el-form-item label="姓名" prop="adviserName">
            <el-input
              :placeholder="footer ? '姓名' : ''"
              v-model="formData.adviserName"
              clearable
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="性别" prop="sex">
            <el-select
              :placeholder="footer ? '性别' : ''"
              :style="{ width: '100%' }"
              v-model="formData.sex"
              :disabled="!footer"
            >
              <el-option label="男" value="1" />
              <el-option label="女" value="0" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="出生年月" prop="birthday">
            <el-date-picker
              :placeholder="footer ? '出生年月' : ''"
              :style="{ width: '100%' }"
              v-model="formData.birthday"
              type="date"
              format="yyyy-MM-dd "
              value-format="yyyy-MM-dd HH:mm:ss"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否具备法律专业背景" prop="isSoleDuty">
            <el-select
              :style="{ width: '100%' }"
              v-model="formData.isSoleDuty"
              @input="change()"
              :disabled="!footer"
              :placeholder="footer ? '是否具备法律专业背景' : ''"
            >
              <el-option label="是" :value="1" />
              <el-option label="否" :value="0" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系电话" prop="phone">
            <el-input
              :placeholder="footer ? '联系电话' : ''"
              v-model="formData.phone"
              clearable
              :disabled="!footer"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <!--
          <el-form-item label="审批人" prop="auditPersonName">
            <el-input
              v-model="formData.auditPersonName"
              clearable
              disabled
              placeholder="审批人"
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
          <el-form-item label="工作单位" prop="workUnitName">
            <el-input
              :placeholder="footer ? '工作单位' : ''"
              v-model="formData.workUnitName"
              clearable
              disabled
              :style="{ width: '80%' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              :disabled="!footer"
              @click="showGroupLeader('unit')"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>

        <!-- <el-form-item label="法律顾问职级" prop="itemRank">
            <el-select
              :style="{ width: '100%' }"
              v-model="formData.itemRank"
              placeholder="法律顾问职级"
            >
              <el-option label="一级" value="1" />
              <el-option label="二级" value="2" />
              <el-option label="三级" value="3" />
              <el-option label="四级" value="4" />
              <el-option label="五级" value="5" />
            </el-select>
          </el-form-item>
          <el-form-item label="是否专职" prop="isSoleDuty">
            <el-select
              :style="{ width: '100%' }"
              v-model="formData.isSoleDuty"
              placeholder="是否专职总法律顾问"
            >
              <el-option label="是" value="1" />
              <el-option label="否" value="0" />
            </el-select>
          </el-form-item> -->
        <el-col :span="12">
          <el-form-item label="其他职务" prop="otherDuties">
            <el-input
              :placeholder="footer ? '其他职务' : ''"
              v-model="formData.position"
              clearable
              :disabled="!footer"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否聘用" prop="isHire">
            <el-select
              :placeholder="footer ? '是否聘用' : ''"
              :style="{ width: '100%' }"
              v-model="formData.isHire"
              :disabled="!footer"
            >
              <el-option label="是" value="1" />
              <el-option label="否" value="0" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否持有法律资格证书" prop="certificaTion">
            <el-select
              :placeholder="footer ? '是否聘用' : ''"
              :style="{ width: '100%' }"
              v-model="formData.certificaTion"
              :disabled="!footer"
            >
              <el-option label="是" :value="1" />
              <el-option label="否" :value="0" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="证书编号"
            prop="certificateNumber"
            v-if="formData.certificaTion == 1"
          >
            <el-input
              :placeholder="footer ? '请输入证书编号' : ''"
              v-model="formData.certificateNumber"
              clearable
              :disabled="!footer"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="手机" prop="mobilePhone">
            <el-input
              :placeholder="footer ? '手机' : ''"
              v-model="formData.mobilePhone"
              clearable
              :disabled="!footer"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="填报人" prop="creatorName">
            <el-input
              :placeholder="footer ? '填报人' : ''"
              v-model="formData.creatorName"
              clearable
              disabled
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <!-- <el-form-item label="审批时间" prop="auditTime">
            <el-date-picker
              :style="{ width: '100%' }"
              v-model="formData.auditTime"
              placeholder="审批时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd HH:mm:ss"
            />
          </el-form-item> -->

        <!-- <el-form-item label="照片" prop="img">
            <el-upload
              class="avatar-uploader"
              :action="baseApi + api"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload"
            >
              <img v-if="imageUrl" :src="imageUrl" class="avatar" />
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </el-form-item> -->
        <el-col :span="12">
          <el-form-item label="电子邮箱" prop="email">
            <el-input
              :placeholder="footer ? '电子邮箱' : ''"
              v-model="formData.email"
              clearable
              :disabled="!footer"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="填报时间" prop="fillInTime">
            <el-date-picker
              :placeholder="footer ? '填报时间' : ''"
              :style="{ width: '100%' }"
              v-model="formData.fillInTime"
              type="date"
              :disabled="!footer"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd HH:mm:ss"
            />
          </el-form-item>
        </el-col>
        <!-- <el-form-item label="单据状态" prop="businessType">
            <el-input
              v-model="formData.state"
              clearable
              placeholder="单据状态"
              :style="{ width: '100%' }"
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
                <el-button type="text" @click="handleEdit('detail', row)">
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
    <experienceView ref="experienceView" @addList="addList" :gzjltype="1" />
    <company-select-modal
      ref="companySelect"
      @selected="handleCompanyTreeSelected"
    />
    <project-manage
      @projectManage="getChildlistPro"
      ref="manage"
    ></project-manage>
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
  import { formatDay } from '@/utils'
  import { addFLGW, deleteGZJLList, getFLGWDefaultInfo } from '@/api/fwgl/zzxx'
  import CompanySelectModal from '@/components/CampanySelectModal'
  import store from '@/store'
  import projectManage from '@/views/audit/project/components/formComponents/projectManage.vue'
  import experienceView from './experienceView.vue'
  const { baseURL } = require('@/config')
  import {
    getFlowTaskInfo,
    ymWorkCandidates,
    ymWorkSubmit,
  } from '@/api/contract/manage'
  import { getFaqiInfo } from '@/api/setting/msg'
  import CandidateUserSelect from '@/components/CandidateUserSelect'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'

  export default {
    components: {
      experienceView,
      CompanySelectModal,
      projectManage,
      CandidateUserSelect,
      Resubmit,
    },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        formData: {
          adviserName: '',
          auditPersonName: '',
          auditTime: '',
          birthday: '',
          certificateNumber: '',
          email: '',
          creatorName: '',
          fillInTime: '',
          isHire: '',
          isSoleDuty: '',
          itemRank: '',
          mobilePhone: '',
          otherDuties: '',
          phone: '',
          photo: '',
          position: '',
          professionalBackground: '',
          certificaTion: '',
          sex: '',
          status: '',
        },
        formatDay: formatDay,
        rules: {
          // certificateNumber: [
          // { required: true, message: '请输入证书编号', trigger: 'blur' },
          // ],
          belongGroupName: [
            { required: true, message: '请输入所属集团', trigger: 'blur' },
          ],
          adviserName: [
            { required: true, message: '请输入姓名', trigger: 'blur' },
          ],
          sex: [{ required: true, message: '请选择性别', trigger: 'change' }],
          birthday: [
            { required: true, message: '请选择出生年月', trigger: 'change' },
          ],
          professionalBackground: [
            { required: true, message: '请输入专业背景情况', trigger: 'blur' },
          ],
          phone: [
            { required: true, message: '请输入联系电话', trigger: 'blur' },
          ],
          workUnitName: [
            { required: true, message: '请输入工作单位', trigger: 'blur' },
          ],
          itemRank: [
            {
              required: true,
              message: '请选择总法律顾问级别',
              trigger: 'change',
            },
          ],
          mobilePhone: [
            { required: true, message: '请输入手机', trigger: 'blur' },
          ],
          email: [
            { required: true, message: '请输入电子邮箱', trigger: 'blur' },
          ],
        },
        dialogFormVisible: false,
        title: '详情',
        footer: true,
        tableData: [],
        imageUrl: '',
        baseApi: baseURL,
        api: '/api-auth/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
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
      change() {
        this.$forceUpdate()
      },
      /**
       * @description: 打开选择人员组件
       * @return {*}
       */      
      projectManager() {
        this.$refs['manage'].showEdit()
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
       * @description: 外部打开dialog
       * @param {*} title 表单类型
       * @param {*} row 行数据
       * @return {*}
       */      
      showEdit(title, row) {
        this.dialogFormVisible = true

        this.tableData = []
        if (row) {
          getFLGWDefaultInfo({
            id: row.adviserId,
          }).then(async (res) => {
            this.formData = Object.assign({}, res.data.legalAdviser)

            if (
              res.data.legalAdviser.state == 2 ||
              res.data.legalAdviser.state == 3
            ) {
              const res2 = await getFlowTaskInfo({
                tableId: 24,
                formId: row.adviserId,
              })
              this.jurisdictionCode = res2.data.isFlowInfo
              if (res2.data.isFlowInfo) {
                this.flowtaskinfoflowid = res2.data.flowId + ''
                this.fromId = row.adviserId + ''
                this.fromIdcopy = row.adviserId + ''
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
            if (res.data.legalAdviser.certificateNumber) {
              this.formData.isSoleDuty = 1
            } else {
              this.formData.isSoleDuty = 0
            }
            if (res.data.legalAdviserExt) {
              this.tableData = res.data.legalAdviserExt
            }
            this.formData.sex = res.data.legalAdviser.sex.toString()
            this.formData.isHire = res.data.legalAdviser.isHire.toString()
          })
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.rules = []
          this.title = '详细'
          this.footer = false
        } else {
          this.title = '新增'
          this.formData = this.$options.data().formData
          this.formData.creatorName = this.userInfo.realname
          this.formData.belongGroupName = '浙江省国有资本运营有限公司'
        }
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
      addList(value) {
        const aaa = this.tableData.findIndex(
          (x) => x.adviserExtId == value.adviserExtId
        )
        if (aaa !== -1) {
          this.tableData.splice(aaa, 1, value)
        } else {
          this.tableData.push(value)
        }
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
              aa.push(e.adviserExtId)
            })
            this.formData.adviserExtId = aa.toString()

            addFLGW(this.formData).then((res) => {
              if (res.msg == '成功') {
                this.dialogFormVisible = false
                this.$baseMessage('成功', 'success')
                this.$emit('fetchData')
              }
            })
          }
        })
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */      
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await deleteGZJLList({ id: row.adviserExtId })
          if (code == 200) {
            this.tableData.splice(
              this.tableData.findIndex(
                (x) => x.adviserExtId == row.adviserExtId
              ),
              1
            )
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          }
        })
      },
      /**
       * @description: 选择组件回调
       * @param {*} val 已选数据
       * @return {*}
       */      
      async getChildlistPro(val) {
        this.$set(this.formData, 'auditPersonName', val[0].realname)
        this.$set(this.formData, 'auditPerson', val[0].staffid)
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
        this.rules = this.$options.data().rules
      },
      handleAvatarSuccess(file) {},
      beforeAvatarUpload() {},
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
