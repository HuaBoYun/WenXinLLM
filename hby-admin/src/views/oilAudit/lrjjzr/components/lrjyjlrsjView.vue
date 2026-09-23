<template>
  <!-- 二级单位及成员离任审计 新增修改 -->
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="135px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="formDisabled"
      >
        <el-col :span="12">
          <el-form-item label="序号" prop="leaveNo">
            <el-input
              v-model="formData.leaveNo"
              :style="{ width: '100%' }"
              clearable
              disabled
              placeholder="请输入序号"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计项目名称" prop="projectName">
            <el-input
              v-model="formData.projectName"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入审计项目名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 47px">
          <el-form-item label="被审计单位" prop="auditOrgName">
            <el-input
              v-model="formData.auditOrgName"
              disabled
              placeholder="请输入被审计单位"
              :style="{ width: '272px' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click.native="showGroupLeader"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>

        <el-col :span="12" style="height: 47px">
          <el-form-item label="委托书编号" prop="entrustNo">
            <el-input
              v-model="formData.entrustNo"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入委托书编号"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 47px">
          <el-form-item label="委托时间" prop="entrustTime">
            <el-date-picker
              v-model="formData.entrustTime"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="选择委托时间"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 47px">
          <el-form-item label="审计任职期间" prop="auditTimeRange">
            <el-date-picker
              v-model="formData.auditTimeRange"
              type="daterange"
              range-separator="~"
              start-placeholder="审计任职开始日期"
              end-placeholder="审计任职结束日期"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 47px">
          <el-form-item label="项目类型" prop="projectType">
            <el-input
              v-model="formData.projectType"
              disabled
              placeholder="请选择"
              :style="{ width: '272px' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click.native="showtypeView"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 47px">
          <el-form-item label="创建人" prop="createUser">
            <el-input
              v-model="formData.createUser"
              placeholder="请输入创建人"
              disabled
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 47px">
          <el-form-item label="创建时间" prop="createTime">
            <el-date-picker
              v-model="formData.createTime"
              type="date"
              disabled
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="选择日期"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="remarks">
            <el-input
              v-model="formData.remarks"
              clearable
              placeholder="请输入备注"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />
    <typeView ref="typeView" @submit="setType" />
    <div slot="footer" v-if="!formDisabled">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary" v-loading="loading">
        确定
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
  import { ejdwlrsjUpdate, ejdwlrsjDetail, getNumber } from '@/oapi/audit/plan'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import typeView from './type'
  export default {
    components: { SelectDepartment, typeView },
    inheritAttrs: false,
    data() {
      return {
        loading: false,
        formData: {
          projectName: '',
          auditOrgId: '',
          auditOrgName: '',
          entrustNo: '',
          entrustTime: '',
          auditTimeRange: [],
          createUser: '',
          createTime: '',
          projectType: '',
          leaveNo: '',
          remarks: '',
        },
        formDisabled: true,
        rules: {
          projectName: [
            {
              required: true,
              message: '请输入审计项目名称',
              trigger: 'blur',
            },
          ],
          auditOrgName: [
            {
              required: true,
              message: '请输入被审计单位',
              trigger: 'change',
            },
          ],
          entrustNo: [
            {
              required: true,
              message: '请输入委托书编号',
              trigger: 'blur',
            },
          ],
          entrustTime: [
            {
              required: true,
              message: '请输入委托书时间',
              trigger: 'blur',
            },
          ],
          auditTimeRange: [
            {
              type: 'array',
              required: true,
              message: '请选择审计任职期间',
              trigger: 'change',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
      }
    },
    methods: {
      setType(e) {
        this.formData.projectType = e.auditType
        this.$forceUpdate()
      },
      showtypeView() {
        this.$refs['typeView'].showEdit()
      },
      // 选公司
      showGroupLeader() {
        this.$refs['audiTree'].showEdit()
      },
      // 选公司后处理
      getDepartmentInfo(val) {
        this.formData.auditOrgId = val.id
        this.formData.auditOrgName = val.label
      },
      async showEdit(row, disabled) {
        this.dialogFormVisible = true
        this.formDisabled = !!disabled
        if (row) {
          const res = await ejdwlrsjDetail({ id: row.id })
          this.title = disabled ? '详细' : '编辑'
          this.id = row.id
          Object.keys(this.formData).forEach((key) => {
            this.formData[key] = res.data.data[key]
          })
          this.formData.createUser = res.data.data.createUser?.realname
          this.formData.auditOrgName = res.data.data.auditOrg?.orgname
          this.formData.auditTimeRange = [
            res.data.data.auditStartTime,
            res.data.data.auditEndTime,
          ]
        } else {
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          const createUserName = userInfo.realname
          const createTime = new Date().toJSON().split('T')[0]
          this.formData.createUser = createUserName
          this.formData.createTime = createTime
          this.getAutoNo()
        }
      },
      async getAutoNo() {
        const { code, msg, data } = await getNumber()
        if (code != 1) return this.$message.error(msg)
        this.formData.leaveNo = data
        this.$forceUpdate()
      },
      close() {
        this.formData = {
          projectName: '',
          auditOrgId: '',
          auditOrgName: '',
          entrustNo: '',
          entrustTime: '',
          auditTimeRange: [],
          createUser: '',
          createTime: '',
          leaveNo: '',
          projectType: '',
          remarks: '',
        }
        this.dialogFormVisible = false
        this.formDisabled = true
        this.id = ''
      },

      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            let params = { ...this.formData }
            params.auditStartTime = params.auditTimeRange[0]
            params.auditEndTime = params.auditTimeRange[1]
            delete params.createUser
            delete params.auditTimeRange
            if (this.title === '编辑') params.id = this.id
            const res = await ejdwlrsjUpdate(params)
            this.loading = false
            if (res && res.code == 1) {
              this.$message.success('保存成功！')
              this.$emit('selected', res.data)
              this.$emit('fetchData')
              this.close()
            } else {
              this.$message.error(res.msg || '保存失败！')
            }
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
</style>
