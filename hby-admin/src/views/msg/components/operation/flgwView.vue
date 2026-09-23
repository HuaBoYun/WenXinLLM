<template>
  <div>
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
            <el-input
              disabled
              v-model="formData.belongGroupName"
              clearable
              placeholder="请输入所属集团"
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
          <el-form-item label="姓名" prop="adviserName">
            <el-input
              v-model="formData.adviserName"
              clearable
              placeholder="姓名"
              :disabled="!footer"
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
          <el-form-item label="出生年月" prop="birthday">
            <el-date-picker
              :style="{ width: '100%' }"
              v-model="formData.birthday"
              placeholder="出生年月"
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
              placeholder="是否具备法律专业背景"
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
            v-if="formData.isSoleDuty == 1"
          >
            <el-input
              v-model="formData.certificateNumber"
              clearable
              placeholder="专业背景证书编号(多项,:分隔)"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系电话" prop="phone">
            <el-input
              v-model="formData.phone"
              clearable
              placeholder="联系电话"
              :style="{ width: '100%' }"
              :disabled="!footer"
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
              :style="{ width: '64%' }"
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
              v-model="formData.workUnitName"
              clearable
              disabled
              placeholder="工作单位"
              :style="{ width: '64%' }"
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
              v-model="formData.position"
              clearable
              placeholder="其他职务"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否聘用" prop="isHire">
            <el-select
              :style="{ width: '100%' }"
              v-model="formData.isHire"
              placeholder="是否聘用"
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
              :style="{ width: '100%' }"
              v-model="formData.certificaTion"
              placeholder="是否聘用"
              :disabled="!footer"
            >
              <el-option label="是" :value="1" />
              <el-option label="否" :value="0" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="手机" prop="mobilePhone">
            <el-input
              v-model="formData.mobilePhone"
              clearable
              placeholder="手机"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="填报人" prop="creatorName">
            <el-input
              v-model="formData.creatorName"
              clearable
              disabled
              placeholder="填报人"
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
              v-model="formData.email"
              clearable
              placeholder="电子邮箱"
              :disabled="!footer"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="填报时间" prop="fillInTime">
            <el-date-picker
              :style="{ width: '100%' }"
              v-model="formData.fillInTime"
              placeholder="填报时间"
              type="date"
              format="yyyy-MM-dd"
              :disabled="!footer"
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
    <div style="text-align: right; margin-top: 10px" v-if="footer">
      <!-- <el-button @click="close">取 消</el-button> -->
      <el-button type="primary" @click="add">确 定</el-button>
      <el-button type="primary" @click="ymsubmit">提 交</el-button>
    </div>
    <!-- <template #footer v-if="footer">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </template> -->
    <experienceView ref="experienceView" @addList="addList" :gzjltype="1" />
    <company-select-modal
      ref="companySelect"
      @selected="handleCompanyTreeSelected"
    />
    <project-manage
      @projectManage="getChildlistPro"
      ref="manage"
    ></project-manage>

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
  import { addFLGW, deleteGZJLList, getFLGWDefaultInfo } from '@/api/fwgl/zzxx'
  import CompanySelectModal from '@/components/CampanySelectModal'
  import store from '@/store'
  import projectManage from '@/views/audit/project/components/formComponents/projectManage.vue'
  import experienceView from '@/views/fwgl/zzxx/components/experienceView.vue'
  const { baseURL } = require('@/config')
  import CandidateUserSelect from '@/components/CandidateUserSelect.vue'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import { changeFormSizeStyleFunc } from '@/utils/processMobile'
  import { formatDay } from '@/utils'
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
          sex: '',
          certificaTion: '',
        },
        allDisabled: false,
        rules: {
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
        ymFromId: 0,
        flowId: 0,
        fromId: 0,
        fromIdcopy: 0,
        flowtaskinfoflowid: '',
        status: 0,
        formatDay: formatDay,
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
      handleCompanyTreeSelected(val) {
        this.$set(this.formData, val.idKey, val.id)
        this.$set(this.formData, val.labelKey, val.label)
      },
      showEdit(title, formId, flowtaskinfoflowid, ymFromId, status) {
        this.dialogFormVisible = true
        this.tableData = []
        if (formId) {
          getFLGWDefaultInfo({
            id: formId,
          }).then((res) => {
            this.formData = Object.assign({}, res.data.legalAdviser)
            if (res.data.legalAdviserExt) {
              this.tableData = res.data.legalAdviserExt
            }
            this.formData.sex = res.data.legalAdviser.sex.toString()
            this.formData.isHire = res.data.legalAdviser.isHire.toString()
            if (res.data.legalAdviser.certificateNumber) {
              this.formData.isSoleDuty = 1
            } else {
              this.formData.isSoleDuty = 0
            }
          })
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
          this.allDisabled = true
        } else {
          this.title = '新增'
          this.formData = this.$options.data().formData
          this.formData.creatorName = this.userInfo.realname
        }
        this.fromId = formId
        this.fromIdcopy = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status

        if (this.$store.state.work.processMobile) {
          this.$nextTick(() => {
            changeFormSizeStyleFunc()
          })
        }
      },
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
                this.$baseMessage(
                  '保存成功',
                  'success',
                  'vab-hey-message-success'
                )
                this.dialogFormVisible = false
                this.$emit('fetchData')
              }
            })
          }
        })
      },
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
      async getChildlistPro(val) {
        this.$set(this.formData, 'auditPersonName', val[0].realname)
        this.$set(this.formData, 'auditPerson', val[0].staffid)
      },
      close() {
        this.footer = true
        this.allDisabled = false
        this.dialogFormVisible = false

        this.$bus.$emit('updateMsg', 0)
      },
      handleAvatarSuccess(file) {},
      beforeAvatarUpload() {},

      //提交
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
