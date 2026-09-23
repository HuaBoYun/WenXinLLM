<template>
  <div>
    <el-row :gutter="14">
      <el-col :span="24">
        <el-divider>基本信息</el-divider>
      </el-col>
      <el-form
        ref="ruleForm"
        label-width="100px"
        :rules="rules"
        :model="formData"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="所属集团" prop="belongGroupName">
            <el-input
              v-model="formData.belongGroupName"
              clearable
              disabled
              placeholder="请输入所属集团"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="机构名称" prop="organizationName">
            <el-input
              v-model="formData.organizationName"
              clearable
              :disabled="!footer"
              placeholder="机构名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="负责人" prop="responsiblePerson">
            <el-input
              v-model="formData.responsiblePerson"
              clearable
              :disabled="!footer"
              placeholder="负责人"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联络人电话" prop="contactPhone">
            <el-input
              v-model="formData.contactPhone"
              clearable
              :disabled="!footer"
              placeholder="联络人电话"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="制单人" prop="makingPeople">
            <el-input
              v-model="formData.makingPeople"
              clearable
              :disabled="!footer"
              placeholder="制单人"
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
              :disabled="!footer"
            />
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
              :disabled="!footer"
              type="primary"
              @click="showGroupLeader('unit')"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="机构性质" prop="nature">
            <el-input
              v-model="formData.nature"
              clearable
              :disabled="!footer"
              placeholder="机构性质"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="负责人电话" prop="responsiblePersonPhone">
            <el-input
              v-model="formData.responsiblePersonPhone"
              clearable
              :disabled="!footer"
              placeholder="负责人电话"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联络人手机" prop="contactMobilePhone">
            <el-input
              v-model="formData.contactMobilePhone"
              clearable
              :disabled="!footer"
              placeholder="联络人手机"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="制单日期" prop="makingTime">
            <el-date-picker
              :style="{ width: '100%' }"
              v-model="formData.makingTime"
              placeholder="制单日期"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd HH:mm:ss"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <!-- <el-form-item label="单据状态" prop="businessType">
            <el-input
              v-model="formData.businessType"
              clearable
              :disabled="!footer"
              placeholder="单据状态"
              :style="{ width: '100%' }"
            />
          </el-form-item> -->

        <el-col :span="12">
          <el-form-item label="年度" prop="annual">
            <el-date-picker
              :style="{ width: '100%' }"
              v-model="formData.annual"
              placeholder="年度"
              type="year"
              format="yyyy"
              value-format="yyyy"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="具体联络人" prop="contact">
            <el-input
              v-model="formData.contact"
              clearable
              :disabled="!footer"
              placeholder="具体联络人"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联络邮箱" prop="contactEmail">
            <el-input
              v-model="formData.contactEmail"
              clearable
              :disabled="!footer"
              placeholder="联络邮箱"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <!-- <el-form-item label="审批人" prop="auditPersonName">
            <el-input
              v-model="formData.auditPersonName"
              clearable
              disabled
              placeholder="审批人"
              :style="{ width: '64%' }"
            />
            <el-button
              :disabled="!footer"
              @click="projectManager"
              style="margin-left: 10px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item> -->

        <el-col :span="24">
          <el-divider>年度法律审核情况</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-button type="success" @click="handleAdd">新建</el-button>
          </div>
        </el-col>
        <el-col :span="24">
          <el-table :data="tableData">
            <el-table-column
              align="center"
              label="企业规章制度审核件数"
              prop="firmRegulationsAuditNumber"
            />
            <el-table-column
              align="center"
              label="规章制度审核率(%)"
              prop="regulationsAuditRatio"
            />
            <el-table-column
              align="center"
              label="企业合同审核件数"
              prop="firmEconomicsContractNumber"
            />
            <el-table-column
              align="center"
              label="合同审核率(%)"
              prop="economicsContractRatio"
            />
            <el-table-column
              align="center"
              label="企业重要决策审核件数"
              prop="firmMajorDecisionNumber"
            />
            <el-table-column
              align="center"
              label="重要决策审核率(%)"
              prop="majorDecisionRatio"
            />

            <el-table-column align="center" label="备注" prop="remark" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleEdit(row)" v-if="footer">
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
    <checkView ref="checkView" @addList="addList" />
    <company-select-modal
      ref="companySelect"
      @selected="handleCompanyTreeSelected"
    />
    <project-manage
      @projectManage="getChildlistPro"
      ref="manage"
    ></project-manage>
    <div style="text-align: right; margin-top: 10px" v-if="footer">
      <!-- <el-button @click="close">取 消</el-button> -->
      <el-button type="primary" @click="add">确 定</el-button>
      <el-button type="primary" @click="ymsubmit">提 交</el-button>
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
  </div>
</template>

<script>
  import checkView from '@/views/fwgl/zzxx/components/checkView.vue'
  import CandidateUserSelect from '@/components/CandidateUserSelect.vue'
  import CompanySelectModal from '@/components/CampanySelectModal'
  import projectManage from '@/views/audit/project/components/formComponents/projectManage.vue'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import { changeFormSizeStyleFunc } from '@/utils/processMobile'

  import {
    addFWJGFZRL,
    getFWJGFZRLDefaultInfo,
    deletFLSHeList,
  } from '@/api/fwgl/zzxx'
  export default {
    components: {
      checkView,
      CandidateUserSelect,
      CompanySelectModal,
      projectManage,
      Resubmit,
    },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        formData: {
          annual: '',
          auditPerson: '',
          auditTime: '',
          contact: '',
          contactEmail: '',
          contactMobilePhone: '',
          contactPhone: '',
          makingPeople: '',
          makingTime: '',
          nature: '',
          // organizationExtId: '',
          // organizationId: 0,
          belongGroupId: '',
          organizationName: '',
          responsiblePerson: '',
          responsiblePersonPhone: '',
        },
        rules: {
          belongGroupId: [
            { required: true, message: '请输入所属集团', trigger: 'blur' },
          ],
          organizationName: [
            { required: true, message: '请输入机构名称', trigger: 'blur' },
          ],
          responsiblePerson: [
            { required: true, message: '请输入负责人', trigger: 'blur' },
          ],
          contactPhone: [
            { required: true, message: '请输入联系电话', trigger: 'blur' },
          ],
          position: [
            { required: true, message: '请输入工作单位', trigger: 'blur' },
          ],
          nature: [
            { required: true, message: '请输入机构性质', trigger: 'blur' },
          ],
          responsiblePersonPhone: [
            { required: true, message: '请输入负责人电话', trigger: 'blur' },
          ],
          contactMobilePhone: [
            { required: true, message: '请输入联络人手机', trigger: 'blur' },
          ],
          annual: [
            { required: true, message: '请选择年度', trigger: 'change' },
          ],
          contact: [
            { required: true, message: '请输入具体联络人', trigger: 'blur' },
          ],
          contactEmail: [
            { required: true, message: '请输入联络邮箱', trigger: 'blur' },
          ],
        },
        dialogFormVisible: false,
        title: '详情',
        footer: true,
        tableData: [],
        //提交
        ymFromId: 0,
        flowId: 0,
        fromId: 0,
        fromIdcopy: 0,
        flowtaskinfoflowid: '',
        status: 0,
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      showEdit(title, formId, flowtaskinfoflowid, ymFromId, status) {
        this.dialogFormVisible = true
        this.tableData = []
        if (formId) {
          getFWJGFZRLDefaultInfo({
            id: formId,
          }).then((res) => {
            this.formData = Object.assign({}, res.data.legalOrganization)
            if (res.data.legalOrganizationExt) {
              this.tableData = res.data.legalOrganizationExt
            }
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
      projectManager() {
        this.$refs['manage'].showEdit()
      },
      async getChildlistPro(val) {
        this.$set(this.formData, 'auditPersonName', val[0].realname)
        this.$set(this.formData, 'auditPerson', val[0].staffid)
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
      handleAdd() {
        this.$refs['checkView'].showEdit('add', null)
      },
      async handleEdit(row) {
        // const data = await approachSummaryDisp({ enterid: row.enterid })
        await this.$refs['checkView'].showEdit('edit', row)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await deletFLSHeList({
            id: row.organizationExtId,
          })
          if (code == 200) {
            this.tableData.splice(
              this.tableData.findIndex(
                (x) => x.organizationExtId == row.organizationExtId
              ),
              1
            )
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          }
        })
      },
      addList(value) {
        const aaa = this.tableData.findIndex(
          (x) => x.organizationExtId == value.organizationExtId
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
              aa.push(e.organizationExtId)
            })
            this.formData.organizationExtId = aa.toString()

            this.$emit('add', this.formData)
            addFWJGFZRL(this.formData).then((res) => {
              if (res.msg == '成功') {
                this.$message({ message: '保存成功', type: 'success' })
                this.dialogFormVisible = false
                this.$emit('fetchData')
              }
            })
          }
        })
      },
      close() {
        this.footer = true

        this.$bus.$emit('updateMsg', 0)
      },
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
