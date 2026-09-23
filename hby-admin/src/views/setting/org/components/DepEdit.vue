<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="500px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-form
      ref="form"
      :class="{ disabled: disabled }"
      :disabled="disabled"
      label-width="140px"
      :model="form"
      :rules="rules"
    >
      <el-form-item label="机构编号" prop="orgnumber">
        <el-input v-model.trim="form.orgnumber" />
      </el-form-item>
      <el-form-item label="机构名称" prop="orgname">
        <el-input v-model.trim="form.orgname" />
      </el-form-item>
      <el-form-item label="发文代字" prop="orgname">
        <el-input v-model.trim="form.writtenByDept" />
      </el-form-item>
      <el-form-item label="部门负责人">
        <el-input
          v-model.trim="form.principalName"
          readonly
          :style="{ width: '79%' }"
        />
        <el-button
          v-if="!disabled"
          :style="{ marginLeft: '10px' }"
          type="primary"
          @click="handleUserSelect(['principalName', 'principalStaffId'])"
        >
          选择
        </el-button>
      </el-form-item>
      <el-form-item label="是否是主责部门">
        <el-radio-group v-model="form.auditType">
          <el-radio
            v-for="(item, index) in iszyoptions"
            :key="index"
            :label="item.value"
          >
            {{ item.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <!-- <el-form-item label="直属主管">
          <el-input
            v-model.trim="form.leaderName"
            readonly
            :style="{ width: '79%' }"
            placeholder="请选择直属主管"
          />
          <el-button
            v-if="!disabled"
            :style="{ marginLeft: '10px' }"
            type="primary"
            @click="handleUserSelect(['leaderName', 'chargeLeaderStaffId'])"
          >
            选择
          </el-button>
        </el-form-item>  -->
      <el-form-item label="机构简介">
        <el-input v-model.trim="form.orgmeno" type="textarea" />
      </el-form-item>
      <el-form-item label="备注">
        <el-input v-model.trim="form.memo" type="textarea" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button v-if="!disabled" type="primary" @click="save">确 定</el-button>
    </template>
    <org-user-dialog ref="orgUserDialog" @select="handSelectOrgUser" />
    <ProcessList ref="process" @fetchData="fetchData" />
  </el-dialog>
</template>

<script>
  import { orgDetail, orgSave } from '@/api/setting/org'
  import OrgUserDialog from '@/views/setting/specialist/components/OrgUserDialog.vue'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import { getFlowList } from '@/api/setting/auth'
  export default {
    name: 'CompanyEdit',
    components: { OrgUserDialog, ProcessList },
    data() {
      return {
        disabled: false,
        form: {
          orgname: '',
          orgnumber: '',
          orgmeno: '',
          memo: '',
          auditType: 0,
          fatherorgid: '',
          writtenByDept: '',
          principalName: '',
          principalStaffId: '',
          leaderName: '',
          chargeLeaderStaffId: '',
          userKeys: [],
        },
        rules: {
          orgnumber: [
            { required: true, trigger: 'blur', message: '请输入编号' },
          ],
          orgname: [{ required: true, trigger: 'blur', message: '请输入名称' }],
        },
        title: '',
        dialogFormVisible: false,
        iszyoptions: [
          {
            value: 0,
            label: '否',
          },
          {
            value: 1,
            label: '是',
          },
        ],
        requireValuedata: false, // 是否需要流程校验
      }
    },
    created() {
      const info = JSON.parse(localStorage.getItem('userInfo'))
      // 判断是否需要流程校验
      if (info.requireValuedata) {
        this.requireValuedata = info.requireValuedata
      }
    },
    methods: {
      async showEdit(pid, row, disabled) {
        this.disabled = disabled
        if (!row) {
          this.title = '添加'
        } else {
          this.title = disabled ? '查看' : '编辑'
          const res = await orgDetail({ orgId: row.orgid })
          if (res && res.code === 1) {
            this.form = Object.assign({}, this.form, res.data)
          }
        }
        this.form.fatherorgid = pid
        this.dialogFormVisible = true
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { orgcreate, tblOrganization, ...other } = this.form
            const { msg, data } = await orgSave(other)
            // 流程校验
            if (this.requireValuedata && this.title != '添加') {
              //  查询当前是否有流程
              getFlowList({
                targetId: data.recordId,
                targetType: 'dept',
                operationType: this.title == '添加' ? 1 : 2,
              }).then((res) => {
                if (res.data == 0) {
                  // 可以提交流程
                  this.$refs['process'].save(220, data.recordId)
                  this.$baseMessage(
                    '审批流程提交成功,请等待审批',
                    'success',
                    'vab-hey-message-success'
                  )
                  this.close()
                } else {
                  // 不可以提交流程
                  this.$baseMessage(
                    '当前用户流程已存在,请先走审批流程',
                    'error',
                    'vab-hey-message-error'
                  )
                  return
                }
              })
            } else if (this.title == '添加' && this.requireValuedata) {
              // 可以提交流程
              this.$refs['process'].save(220, data.recordId)
              this.$baseMessage(
                '审批流程提交成功,请等待审批',
                'success',
                'vab-hey-message-success'
              )
              this.close()
            } else {
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
              this.$emit('fetch-data')
              this.close()
            }
          }
        })
      },
      handleUserSelect(userKeys) {
        this.userKeys = userKeys
        this.$refs.orgUserDialog.show()
      },
      handSelectOrgUser(data) {
        this.form[this.userKeys[0]] = data.realname
        this.form[this.userKeys[1]] = data.staffid
      },
    },
  }
</script>
