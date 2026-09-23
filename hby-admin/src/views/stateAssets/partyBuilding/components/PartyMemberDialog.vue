<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="memberForm"
      :model="memberForm"
      :rules="memberRules"
      label-width="120px"
      v-loading="loading"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="党员姓名" prop="memberName">
            <el-input
              v-model="memberForm.memberName"
              placeholder="请输入党员姓名"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="党员编号" prop="memberCode">
            <el-input
              v-model="memberForm.memberCode"
              placeholder="请输入党员编号"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="所属组织" prop="organizationId">
            <el-select
              v-model="memberForm.organizationId"
              placeholder="请选择所属组织"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            >
              <el-option
                v-for="org in organizations"
                :key="org.id"
                :label="org.organizationName"
                :value="org.id"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="党员类型" prop="memberType">
            <el-select
              v-model="memberForm.memberType"
              placeholder="请选择党员类型"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            >
              <el-option label="正式党员" value="FORMAL"></el-option>
              <el-option label="预备党员" value="PROBATIONARY"></el-option>
              <el-option label="入党积极分子" value="ACTIVIST"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="性别" prop="gender">
            <el-select
              v-model="memberForm.gender"
              placeholder="请选择性别"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            >
              <el-option label="男" value="MALE"></el-option>
              <el-option label="女" value="FEMALE"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="出生日期" prop="birthDate">
            <el-date-picker
              v-model="memberForm.birthDate"
              type="date"
              placeholder="选择出生日期"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="身份证号" prop="idCard">
            <el-input
              v-model="memberForm.idCard"
              placeholder="请输入身份证号"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系电话" prop="phone">
            <el-input
              v-model="memberForm.phone"
              placeholder="请输入联系电话"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="邮箱" prop="email">
            <el-input
              v-model="memberForm.email"
              placeholder="请输入邮箱"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="职务" prop="position">
            <el-input
              v-model="memberForm.position"
              placeholder="请输入职务"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="入党时间" prop="joinDate">
            <el-date-picker
              v-model="memberForm.joinDate"
              type="date"
              placeholder="选择入党时间"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="转正时间" prop="formalDate">
            <el-date-picker
              v-model="memberForm.formalDate"
              type="date"
              placeholder="选择转正时间"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="学历" prop="education">
            <el-select
              v-model="memberForm.education"
              placeholder="请选择学历"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            >
              <el-option label="博士" value="DOCTOR"></el-option>
              <el-option label="硕士" value="MASTER"></el-option>
              <el-option label="本科" value="BACHELOR"></el-option>
              <el-option label="专科" value="COLLEGE"></el-option>
              <el-option label="高中" value="HIGH_SCHOOL"></el-option>
              <el-option label="其他" value="OTHER"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="政治面貌" prop="politicalStatus">
            <el-select
              v-model="memberForm.politicalStatus"
              placeholder="请选择政治面貌"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            >
              <el-option label="中共党员" value="PARTY_MEMBER"></el-option>
              <el-option label="中共预备党员" value="PROBATIONARY_MEMBER"></el-option>
              <el-option label="共青团员" value="LEAGUE_MEMBER"></el-option>
              <el-option label="群众" value="MASSES"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="家庭住址" prop="homeAddress">
        <el-input
          v-model="memberForm.homeAddress"
          placeholder="请输入家庭住址"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>

      <el-form-item label="工作单位" prop="workUnit">
        <el-input
          v-model="memberForm.workUnit"
          placeholder="请输入工作单位"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>

      <el-form-item label="个人简历" prop="resume">
        <el-input
          v-model="memberForm.resume"
          type="textarea"
          :rows="3"
          placeholder="请输入个人简历"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>

      <el-form-item label="备注" prop="remarks">
        <el-input
          v-model="memberForm.remarks"
          type="textarea"
          :rows="2"
          placeholder="请输入备注信息"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button
        v-if="dialogType !== 'view'"
        type="primary"
        @click="handleSubmit"
        :loading="loading"
      >
        确定
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { savePartyMember } from '@/api/stateAssets/partyBuilding'

export default {
  name: 'PartyMemberDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    memberData: {
      type: Object,
      default: () => ({})
    },
    dialogType: {
      type: String,
      default: 'add' // add, edit, view
    }
  },
  data() {
    return {
      loading: false,
      organizations: [], // 组织列表
      memberForm: {
        id: '',
        memberName: '',
        memberCode: '',
        organizationId: '',
        memberType: '',
        gender: '',
        birthDate: '',
        idCard: '',
        phone: '',
        email: '',
        position: '',
        joinDate: '',
        formalDate: '',
        education: '',
        politicalStatus: '',
        homeAddress: '',
        workUnit: '',
        resume: '',
        remarks: ''
      },
      memberRules: {
        memberName: [
          { required: true, message: '请输入党员姓名', trigger: 'blur' }
        ],
        memberCode: [
          { required: true, message: '请输入党员编号', trigger: 'blur' }
        ],
        organizationId: [
          { required: true, message: '请选择所属组织', trigger: 'change' }
        ],
        memberType: [
          { required: true, message: '请选择党员类型', trigger: 'change' }
        ],
        gender: [
          { required: true, message: '请选择性别', trigger: 'change' }
        ],
        birthDate: [
          { required: true, message: '请选择出生日期', trigger: 'change' }
        ],
        idCard: [
          { required: true, message: '请输入身份证号', trigger: 'blur' },
          { pattern: /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/, message: '请输入正确的身份证号', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入联系电话', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
        ],
        email: [
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ],
        joinDate: [
          { required: true, message: '请选择入党时间', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    },
    dialogTitle() {
      const titleMap = {
        add: '新增党员',
        edit: '编辑党员',
        view: '查看党员'
      }
      return titleMap[this.dialogType] || '新增党员'
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.initForm()
        this.loadOrganizations()
      }
    }
  },
  methods: {
    initForm() {
      if (this.dialogType === 'add') {
        this.memberForm = {
          id: '',
          memberName: '',
          memberCode: '',
          organizationId: '',
          memberType: '',
          gender: '',
          birthDate: '',
          idCard: '',
          phone: '',
          email: '',
          position: '',
          joinDate: '',
          formalDate: '',
          education: '',
          politicalStatus: '',
          homeAddress: '',
          workUnit: '',
          resume: '',
          remarks: ''
        }
      } else {
        this.memberForm = { ...this.memberData }
      }
      
      this.$nextTick(() => {
        if (this.$refs.memberForm) {
          this.$refs.memberForm.clearValidate()
        }
      })
    },

    loadOrganizations() {
      // 模拟加载组织数据
      this.organizations = [
        { id: '1', organizationName: '集团党委' },
        { id: '2', organizationName: '财务部党支部' },
        { id: '3', organizationName: '人力资源部党支部' },
        { id: '4', organizationName: '技术部党支部' }
      ]
    },

    handleSubmit() {
      this.$refs.memberForm.validate((valid) => {
        if (valid) {
          this.loading = true
          savePartyMember(this.memberForm).then(response => {
            if (response.code === 1) {
              this.$message.success(response.msg || '操作成功')
              this.handleClose()
              this.$emit('refresh')
            } else {
              this.$message.error(response.msg || '操作失败')
            }
            this.loading = false
          }).catch(error => {
            console.error('保存党员失败:', error)
            this.$message.error('操作失败')
            this.loading = false
          })
        }
      })
    },

    handleClose() {
      this.dialogVisible = false
      this.loading = false
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>
