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
      <el-form
        ref="ruleForm"
        label-width="100px"
        :rules="rules"
        :model="formData"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="所属集团" prop="belongGroupName">
            <el-input v-model="formData.belongGroupName" clearable disabled />
            <!-- placeholder="请输入所属集团" -->
            <!-- <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              :disabled="!footer"
              @click="showGroupLeader('group')"
            >
              选择
            </el-button> -->
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="机构名称" prop="organizationName">
            <el-input
              v-model="formData.organizationName"
              clearable
              :disabled="!footer"
              :style="{ width: '100%' }"
              :placeholder="footer ? '机构名称' : ''"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="负责人" prop="responsiblePerson">
            <el-input
              v-model="formData.responsiblePerson"
              clearable
              :disabled="!footer"
              :style="{ width: '100%' }"
              :placeholder="footer ? '负责人' : ''"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联络人电话" prop="contactPhone">
            <el-input
              v-model="formData.contactPhone"
              clearable
              :disabled="!footer"
              :style="{ width: '100%' }"
              :placeholder="footer ? '联络人电话' : ''"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="制单人" prop="makingPeople">
            <el-input
              v-model="formData.makingPeople"
              clearable
              :disabled="!footer"
              :style="{ width: '100%' }"
              :placeholder="footer ? '制单人' : ''"
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
              :style="{ width: '80%' }"
              :placeholder="footer ? '工作单位' : ''"
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
            <!-- <el-input
              v-model="formData.nature"
              clearable
              :disabled="!footer"
              placeholder="机构性质"
              :style="{ width: '100%' }"
            /> -->
            <el-select
              :disabled="!footer"
              :style="{ width: '100%' }"
              v-model="formData.nature"
              :placeholder="footer ? '机构性质' : ''"
            >
              <el-option label="单列" value="单列" />
              <el-option label="合署" value="合署" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="负责人电话" prop="responsiblePersonPhone">
            <el-input
              v-model="formData.responsiblePersonPhone"
              clearable
              :disabled="!footer"
              :style="{ width: '100%' }"
              :placeholder="footer ? '负责人电话' : ''"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联络人手机" prop="contactMobilePhone">
            <el-input
              v-model="formData.contactMobilePhone"
              clearable
              :disabled="!footer"
              :style="{ width: '100%' }"
              :placeholder="footer ? '联络人手机' : ''"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="制单日期" prop="makingTime">
            <el-date-picker
              :placeholder="footer ? '制单日期' : ''"
              :style="{ width: '100%' }"
              v-model="formData.makingTime"
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
              type="year"
              format="yyyy"
              value-format="yyyy"
              :disabled="!footer"
              :placeholder="footer ? '年度' : ''"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="具体联络人" prop="contact">
            <el-input
              v-model="formData.contact"
              clearable
              :disabled="!footer"
              :style="{ width: '100%' }"
              :placeholder="footer ? '具体联络人' : ''"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联络邮箱" prop="contactEmail">
            <el-input
              v-model="formData.contactEmail"
              clearable
              :disabled="!footer"
              :style="{ width: '100%' }"
              :placeholder="footer ? '联络邮箱' : ''"
            />
          </el-form-item>
        </el-col>
        <!-- <el-form-item label="审批人" prop="auditPersonName">
            <el-input
              v-model="formData.auditPersonName"
              clearable
              disabled
              placeholder="审批人"
              :style="{ width: '80%' }"
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

        <!-- <el-col :span="24">
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
        </el-col> -->
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
    <checkView ref="checkView" @addList="addList" />
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
  import {
    addFWJGFZRL,
    deletFLSHeList,
    getFWJGFZRLDefaultInfo,
  } from '@/api/fwgl/zzxx'
  import CompanySelectModal from '@/components/CampanySelectModal'
  import projectManage from '@/views/audit/project/components/formComponents/projectManage.vue'
  import checkView from './checkView.vue'
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
      checkView,
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
          annual: new Date().getFullYear().toString(),
          auditPersonName: '',
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
          belongGroupName: '',
          organizationName: '',
          responsiblePerson: '',
          responsiblePersonPhone: '',
          state: 0,
        },
        rules: {
          belongGroupName: [
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
          workUnitName: [
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
        jurisdictionCode: 0,
        fromId: 0,
        fromIdcopy: 0,
        ymFromId: 0,
        flowtaskinfoflowid: '',
        status: '',
      }
    },
    computed: {},
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
          getFWJGFZRLDefaultInfo({
            id: row.organizationId,
          }).then(async (res) => {
            this.formData = Object.assign({}, res.data.legalOrganization)

            if (
              res.data.legalOrganization.state == 2 ||
              res.data.legalOrganization.state == 3
            ) {
              const res2 = await getFlowTaskInfo({
                tableId: 25,
                formId: row.organizationId,
              })
              this.jurisdictionCode = res2.data.isFlowInfo
              if (res2.data.isFlowInfo) {
                this.flowtaskinfoflowid = res2.data.flowId + ''
                this.fromId = row.organizationId + ''
                this.fromIdcopy = row.organizationId + ''
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
          this.formData.belongGroupName = '浙江省国有资本运营有限公司'
        }
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
       * @description: 打开新增表单弹框
       * @return {*}
       */      
      handleAdd() {
        this.$refs['checkView'].showEdit('add', null)
      },
      /**
       * @description: 打开编辑表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */      
      async handleEdit(row) {
        // const data = await approachSummaryDisp({ enterid: row.enterid })
        await this.$refs['checkView'].showEdit('edit', row)
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */      
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
      /**
       * @description: 保存表单
       * @return {*}
       */      
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
       * @description: 引迈流程提交
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
