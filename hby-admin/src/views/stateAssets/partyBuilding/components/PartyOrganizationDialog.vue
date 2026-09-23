<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="organizationForm"
      :model="organizationForm"
      :rules="organizationRules"
      label-width="120px"
      v-loading="loading"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="组织名称" prop="organizationName">
            <el-input
              v-model="organizationForm.organizationName"
              placeholder="请输入组织名称"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="组织类型" prop="organizationType">
            <el-select
              v-model="organizationForm.organizationType"
              placeholder="请选择组织类型"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            >
              <el-option label="党委" value="PARTY_COMMITTEE"></el-option>
              <el-option label="党总支" value="PARTY_BRANCH"></el-option>
              <el-option label="党支部" value="PARTY_CELL"></el-option>
              <el-option label="党小组" value="PARTY_GROUP"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="上级组织" prop="parentOrganizationId">
            <el-select
              v-model="organizationForm.parentOrganizationId"
              placeholder="请选择上级组织"
              style="width: 100%"
              clearable
              :disabled="dialogType === 'view'"
            >
              <el-option
                v-for="org in parentOrganizations"
                :key="org.id"
                :label="org.organizationName"
                :value="org.id"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="组织状态" prop="status">
            <el-select
              v-model="organizationForm.status"
              placeholder="请选择状态"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            >
              <el-option label="正常" value="ACTIVE"></el-option>
              <el-option label="停用" value="INACTIVE"></el-option>
              <el-option label="筹建中" value="PREPARING"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="成立时间" prop="establishDate">
            <el-date-picker
              v-model="organizationForm.establishDate"
              type="date"
              placeholder="选择成立时间"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="党员人数" prop="memberCount">
            <el-input-number
              v-model="organizationForm.memberCount"
              :min="0"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="书记" prop="secretary">
            <el-input
              v-model="organizationForm.secretary"
              placeholder="请输入书记姓名"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="副书记" prop="deputySecretary">
            <el-input
              v-model="organizationForm.deputySecretary"
              placeholder="请输入副书记姓名"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="联系电话" prop="contactPhone">
            <el-input
              v-model="organizationForm.contactPhone"
              placeholder="请输入联系电话"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系邮箱" prop="contactEmail">
            <el-input
              v-model="organizationForm.contactEmail"
              placeholder="请输入联系邮箱"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="办公地址" prop="officeAddress">
        <el-input
          v-model="organizationForm.officeAddress"
          placeholder="请输入办公地址"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>

      <el-form-item label="组织职责" prop="responsibilities">
        <el-input
          v-model="organizationForm.responsibilities"
          type="textarea"
          :rows="3"
          placeholder="请输入组织职责"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>

      <el-form-item label="组织简介" prop="description">
        <el-input
          v-model="organizationForm.description"
          type="textarea"
          :rows="4"
          placeholder="请输入组织简介"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>

      <el-form-item label="备注" prop="remarks">
        <el-input
          v-model="organizationForm.remarks"
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
import { savePartyOrganization } from '@/api/stateAssets/partyBuilding'

export default {
  name: 'PartyOrganizationDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    organizationData: {
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
      parentOrganizations: [], // 上级组织列表
      organizationForm: {
        id: '',
        organizationName: '',
        organizationType: '',
        parentOrganizationId: '',
        status: 'ACTIVE',
        establishDate: '',
        memberCount: 0,
        secretary: '',
        deputySecretary: '',
        contactPhone: '',
        contactEmail: '',
        officeAddress: '',
        responsibilities: '',
        description: '',
        remarks: ''
      },
      organizationRules: {
        organizationName: [
          { required: true, message: '请输入组织名称', trigger: 'blur' }
        ],
        organizationType: [
          { required: true, message: '请选择组织类型', trigger: 'change' }
        ],
        status: [
          { required: true, message: '请选择组织状态', trigger: 'change' }
        ],
        establishDate: [
          { required: true, message: '请选择成立时间', trigger: 'change' }
        ],
        secretary: [
          { required: true, message: '请输入书记姓名', trigger: 'blur' }
        ],
        contactPhone: [
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
        ],
        contactEmail: [
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
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
        add: '新建党组织',
        edit: '编辑党组织',
        view: '查看党组织'
      }
      return titleMap[this.dialogType] || '新建党组织'
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.initForm()
        this.loadParentOrganizations()
      }
    }
  },
  methods: {
    initForm() {
      if (this.dialogType === 'add') {
        this.organizationForm = {
          id: '',
          organizationName: '',
          organizationType: '',
          parentOrganizationId: '',
          status: 'ACTIVE',
          establishDate: '',
          memberCount: 0,
          secretary: '',
          deputySecretary: '',
          contactPhone: '',
          contactEmail: '',
          officeAddress: '',
          responsibilities: '',
          description: '',
          remarks: ''
        }
      } else {
        this.organizationForm = { ...this.organizationData }
      }
      
      this.$nextTick(() => {
        if (this.$refs.organizationForm) {
          this.$refs.organizationForm.clearValidate()
        }
      })
    },

    loadParentOrganizations() {
      // 模拟加载上级组织数据
      this.parentOrganizations = [
        { id: '1', organizationName: '集团党委' },
        { id: '2', organizationName: '财务部党支部' },
        { id: '3', organizationName: '人力资源部党支部' }
      ]
    },

    handleSubmit() {
      this.$refs.organizationForm.validate((valid) => {
        if (valid) {
          this.loading = true
          savePartyOrganization(this.organizationForm).then(response => {
            if (response.code === 1) {
              this.$message.success(response.msg || '操作成功')
              this.handleClose()
              this.$emit('refresh')
            } else {
              this.$message.error(response.msg || '操作失败')
            }
            this.loading = false
          }).catch(error => {
            console.error('保存党组织失败:', error)
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
