<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    title="分配"
    :visible.sync="dialogJdVisible"
    width="1000px"
    @close="close"
    v-if="dialogJdVisible"
  >
    <el-form
      ref="ruleForm"
      label-width="140px"
      :model="formData"
      :rules="rules"
      size="mini"
      :disabled="formDisabled"
    >
      <el-row>
        <el-col :span="12">
          <el-form-item label="组长" prop="zznames">
            <el-input
              v-model="formData.zznames"
              clearable
              placeholder="请选择组长"
              style="width: 266px; height: 30px"
              disabled
            />
            <!-- <el-button
              @click="projectManager('zznames')"
              style="margin-left: 10px; height: 30px"
              type="primary"
            >
              选择
            </el-button> -->
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="副组长" prop="fznames">
            <el-input
              v-model="formData.fznames"
              clearable
              placeholder="请选择副组长"
              style="width: 266px; height: 30px"
              disabled
            />
            <!-- <el-button
              @click="projectManager('fznames')"
              style="margin-left: 10px; height: 30px"
              type="primary"
            >
              选择
            </el-button> -->
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="主审" prop="zsname">
            <el-input
              v-model="formData.zsname"
              clearable
              placeholder="请选择主审"
              style="width: 266px; height: 30px"
              disabled
            />
            <el-button
              @click="projectManager('zsname')"
              style="margin-left: 10px; height: 30px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="助审" prop="fzname">
            <el-input
              v-model="formData.fzname"
              clearable
              placeholder="请选择助审"
              style="width: 266px; height: 30px"
              disabled
            />
            <el-button
              @click="projectManagerS()"
              style="margin-left: 10px; height: 29px"
              type="primary"
              size="mini"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="clear: both">
          <el-form-item label="人数" prop="rsyq">
            <el-input
              v-model="formData.rsyq"
              clearable
              placeholder="请输入人数"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="现场开始时间" prop="xcsrarttime">
            <el-date-picker
              v-model="formData.xcsrarttime"
              placeholder="请选择现场开始时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="现场结束时间" prop="xcendtime">
            <el-date-picker
              v-model="formData.xcendtime"
              placeholder="请选择现场结束时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <div slot="footer" v-if="!formDisabled">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary" :loading="loading">
        确定
      </el-button>
    </div>

    <!-- 人员 -->
    <project-manage @projectManage="getChildlistPro" ref="manage" />
    <project-manage1 @projectManage="getChildlistPro1" ref="manage1" />

    <project-manage2 @save="onSave" ref="projectManage2" />
  </el-dialog>
</template>
<script>
  import projectManage from '@/components/danxuanPerson.vue'
  import projectManage1 from '@/components/selectPerson.vue'
  import projectManage2 from './cwxmrysbPerson.vue'
  import { audit2LsaveOrUpdate3 } from '@/api/oilAudit/jhgl/jhcg'

  export default {
    name: 'cwxmrysbAssignEdit',
    components: { projectManage, projectManage1, projectManage2 },
    data() {
      return {
        loading: false,
        dialogJdVisible: false,
        formDisabled: false,
        formData: {
          id: '',
          zznames: '',
          zzstaffids: '',
          fznames: '',
          fzstaffids: '',
          zsname: '',
          zsstaffid: '',
          fzname: '',
          fzstaffid: '',
          rsyq: '',
          xcsrarttime: '',
          xcendtime: '',
        },
        rules: {
          zznames: [{ required: true, message: '请选择组长', trigger: 'blur' }],
          // fznames: [{ required: true, message: '请选择副组长', trigger: 'blur' }],
          zsname: [{ required: true, message: '请选择主审', trigger: 'blur' }],
          fzname: [{ required: true, message: '请选择助审', trigger: 'blur' }],
          rsyq: [{ required: true, message: '请输入人数', trigger: 'blur' }],
          xcsrarttime: [
            { required: true, message: '请选择现场开始时间', trigger: 'blur' },
          ],
          xcendtime: [
            { required: true, message: '请选择现场结束时间', trigger: 'blur' },
          ],
        },
        rows: {},
      }
    },
    methods: {
      showEdit(row) {
        // TODO
        console.log('row', row)
        this.rows = row
        this.dialogJdVisible = true
        this.formData.id = row.id
        if (row.zznames && row.zzstaffids) {
          // 如果已分派，分派的人员数据
          this.formData.zznames = row.zznames
          this.formData.zzstaffids = row.zzstaffids
          this.formData.fznames = row.fznames
          this.formData.fzstaffids = row.fzstaffids
          this.formData.zsname = row.zsname
          this.formData.zsstaffid = row.zsstaffid
          this.formData.fzname = row.fzname
          this.formData.fzstaffid = row.fzstaffid
          this.formData.rsyq = row.rsyq
          this.formData.xcsrarttime = row.xcsrarttime
          this.formData.xcendtime = row.xcendtime
        } else {
          // 如果未分派，默认项目的人员数据
          this.formData.zznames = row.propsData.groupLeader
          this.formData.zzstaffids = row.propsData.groupLeaderId
          this.formData.fznames = row.propsData.fzzName
          this.formData.fzstaffids = row.propsData.fzzStafffId
          this.formData.zsname = row.propsData.approver
          this.formData.zsstaffid = row.propsData.approverId
          this.formData.fzname = row.propsData.assistApprover
          this.formData.fzstaffid = row.propsData.assistApproverId
          this.formData.xcsrarttime = row.propsData.xcsrarttime
          this.formData.xcendtime = row.propsData.xcendtime
        }
      },
      close() {
        this.formData.id = ''
        this.formData.zznames = ''
        this.formData.zzstaffids = ''
        this.formData.fznames = ''
        this.formData.fzstaffids = ''
        this.formData.zsname = ''
        this.formData.zsstaffid = ''
        this.formData.fzname = ''
        this.formData.fzstaffid = ''
        this.formData.rsyq = ''
        this.formData.xcsrarttime = ''
        this.formData.xcendtime = ''
        this.dialogJdVisible = false
      },
      save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.$baseConfirm(`确定要分配吗`, null, async () => {
              this.loading = true
              const { msg, code } = await audit2LsaveOrUpdate3(this.formData)
              this.loading = false
              if (code == 1) {
                this.$baseMessage(msg, 'success')
                this.$emit('save', { ...this.formData })
                this.close()
              } else {
                this.$baseMessage(msg, 'error')
              }
            })
          }
        })
      },
      projectManager(type) {
        this.proType = type
        this.$refs['manage'].showEdit()
      },
      projectManagerS() {
        if (this.rows.zznames && this.rows.zzstaffids) {
          this.$refs.projectManage2.showEdit({
            propsData: this.rows.fzname,
            projects: this.rows.fzstaffid,
          })
        } else {
          this.$refs.projectManage2.showEdit({
            propsData: this.rows.propsData.assistApprover,
            projects: this.rows.propsData.assistApproverId,
          })
        }
        // this.$refs['manage1'].showEdit()
      },
      onSave(val) {
        console.log(val)
        this.$set(this.formData, 'fzname', val.fzname.join())
        this.$set(this.formData, 'fzstaffid', val.fzstaffid.join())
      },
      getChildlistPro(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        if (this.proType == 'zznames') {
          this.$set(this.formData, 'zznames', names)
          this.$set(this.formData, 'zzstaffids', ids)
        } else if (this.proType == 'fznames') {
          this.$set(this.formData, 'fznames', names)
          this.$set(this.formData, 'fzstaffids', ids)
        } else if (this.proType == 'zsname') {
          this.$set(this.formData, 'zsname', names)
          this.$set(this.formData, 'zsstaffid', ids)
        }
      },
      getChildlistPro1(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.$set(this.formData, 'fzstaffid', ids)
        this.$set(this.formData, 'fzname', names)
      },
    },
  }
</script>
