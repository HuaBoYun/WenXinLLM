<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="activityForm"
      :model="activityForm"
      :rules="activityRules"
      label-width="120px"
      v-loading="loading"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="活动名称" prop="activityName">
            <el-input
              v-model="activityForm.activityName"
              placeholder="请输入活动名称"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="活动类型" prop="activityType">
            <el-select
              v-model="activityForm.activityType"
              placeholder="请选择活动类型"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            >
              <el-option label="学习教育" value="STUDY"></el-option>
              <el-option label="组织生活" value="ORGANIZATION"></el-option>
              <el-option label="志愿服务" value="VOLUNTEER"></el-option>
              <el-option label="主题党日" value="THEME_DAY"></el-option>
              <el-option label="培训讲座" value="TRAINING"></el-option>
              <el-option label="文体活动" value="CULTURAL"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="主办组织" prop="organizationId">
            <el-select
              v-model="activityForm.organizationId"
              placeholder="请选择主办组织"
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
          <el-form-item label="活动状态" prop="status">
            <el-select
              v-model="activityForm.status"
              placeholder="请选择状态"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            >
              <el-option label="计划中" value="PLANNED"></el-option>
              <el-option label="进行中" value="ONGOING"></el-option>
              <el-option label="已完成" value="COMPLETED"></el-option>
              <el-option label="已取消" value="CANCELLED"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="活动日期" prop="activityDate">
            <el-date-picker
              v-model="activityForm.activityDate"
              type="date"
              placeholder="选择活动日期"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="活动时间" prop="activityTime">
            <el-time-picker
              v-model="activityForm.activityTime"
              placeholder="选择活动时间"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="活动地点" prop="location">
            <el-input
              v-model="activityForm.location"
              placeholder="请输入活动地点"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="组织者" prop="organizer">
            <el-input
              v-model="activityForm.organizer"
              placeholder="请输入组织者"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="预计参与人数" prop="expectedParticipants">
            <el-input-number
              v-model="activityForm.expectedParticipants"
              :min="1"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="实际参与人数" prop="actualParticipants">
            <el-input-number
              v-model="activityForm.actualParticipants"
              :min="0"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="活动费用" prop="budget">
            <el-input-number
              v-model="activityForm.budget"
              :min="0"
              :precision="2"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系电话" prop="contactPhone">
            <el-input
              v-model="activityForm.contactPhone"
              placeholder="请输入联系电话"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="活动目的" prop="purpose">
        <el-input
          v-model="activityForm.purpose"
          type="textarea"
          :rows="2"
          placeholder="请输入活动目的"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>

      <el-form-item label="活动内容" prop="content">
        <el-input
          v-model="activityForm.content"
          type="textarea"
          :rows="4"
          placeholder="请输入活动内容"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>

      <el-form-item label="活动要求" prop="requirements">
        <el-input
          v-model="activityForm.requirements"
          type="textarea"
          :rows="3"
          placeholder="请输入活动要求"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>

      <el-form-item label="活动总结" prop="summary" v-if="activityForm.status === 'COMPLETED'">
        <el-input
          v-model="activityForm.summary"
          type="textarea"
          :rows="4"
          placeholder="请输入活动总结"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>

      <el-form-item label="备注" prop="remarks">
        <el-input
          v-model="activityForm.remarks"
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
import { savePartyActivity } from '@/api/stateAssets/partyBuilding'

export default {
  name: 'PartyActivityDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    activityData: {
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
      activityForm: {
        id: '',
        activityName: '',
        activityType: '',
        organizationId: '',
        status: 'PLANNED',
        activityDate: '',
        activityTime: '',
        location: '',
        organizer: '',
        expectedParticipants: 1,
        actualParticipants: 0,
        budget: 0,
        contactPhone: '',
        purpose: '',
        content: '',
        requirements: '',
        summary: '',
        remarks: ''
      },
      activityRules: {
        activityName: [
          { required: true, message: '请输入活动名称', trigger: 'blur' }
        ],
        activityType: [
          { required: true, message: '请选择活动类型', trigger: 'change' }
        ],
        organizationId: [
          { required: true, message: '请选择主办组织', trigger: 'change' }
        ],
        activityDate: [
          { required: true, message: '请选择活动日期', trigger: 'change' }
        ],
        activityTime: [
          { required: true, message: '请选择活动时间', trigger: 'change' }
        ],
        location: [
          { required: true, message: '请输入活动地点', trigger: 'blur' }
        ],
        organizer: [
          { required: true, message: '请输入组织者', trigger: 'blur' }
        ],
        expectedParticipants: [
          { required: true, message: '请输入预计参与人数', trigger: 'blur' }
        ],
        contactPhone: [
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
        ],
        purpose: [
          { required: true, message: '请输入活动目的', trigger: 'blur' }
        ],
        content: [
          { required: true, message: '请输入活动内容', trigger: 'blur' }
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
        add: '新建党建活动',
        edit: '编辑党建活动',
        view: '查看党建活动'
      }
      return titleMap[this.dialogType] || '新建党建活动'
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
        this.activityForm = {
          id: '',
          activityName: '',
          activityType: '',
          organizationId: '',
          status: 'PLANNED',
          activityDate: '',
          activityTime: '',
          location: '',
          organizer: '',
          expectedParticipants: 1,
          actualParticipants: 0,
          budget: 0,
          contactPhone: '',
          purpose: '',
          content: '',
          requirements: '',
          summary: '',
          remarks: ''
        }
      } else {
        this.activityForm = { ...this.activityData }
      }
      
      this.$nextTick(() => {
        if (this.$refs.activityForm) {
          this.$refs.activityForm.clearValidate()
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
      this.$refs.activityForm.validate((valid) => {
        if (valid) {
          this.loading = true
          savePartyActivity(this.activityForm).then(response => {
            if (response.code === 1) {
              this.$message.success(response.msg || '操作成功')
              this.handleClose()
              this.$emit('refresh')
            } else {
              this.$message.error(response.msg || '操作失败')
            }
            this.loading = false
          }).catch(error => {
            console.error('保存党建活动失败:', error)
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
