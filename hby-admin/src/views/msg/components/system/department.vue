<template>
  <div>
    <el-row :gutter="20">
      <!-- 左侧：原始数据 -->
      <el-col :span="12" v-if="type != '新增'">
        <el-card>
          <div slot="header">原始数据</div>
          <el-form
            ref="oldform"
            :class="{ disabled: true }"
            :disabled="disabled"
            label-width="140px"
            :model="oldform"
            :rules="rules"
          >
            <el-form-item label="机构编号">
              <el-input v-model="oldform.orgnumber" readonly />
            </el-form-item>
            <el-form-item label="机构名称">
              <el-input v-model="oldform.orgname" readonly />
            </el-form-item>
            <el-form-item label="发文代字">
              <el-input v-model="oldform.writtenByDept" readonly />
            </el-form-item>
            <el-form-item label="部门负责人">
              <el-input v-model="oldform.principalName" readonly />
            </el-form-item>
            <el-form-item label="是否是主责部门">
              <el-radio-group v-model="oldform.auditType" disabled>
                <el-radio
                  v-for="(item, index) in iszyoptions"
                  :key="index"
                  :label="item.value"
                >
                  {{ item.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="机构简介">
              <el-input v-model="oldform.orgmeno" type="textarea" readonly />
            </el-form-item>
            <el-form-item label="备注">
              <el-input v-model="oldform.memo" type="textarea" readonly />
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <!-- 右侧：编辑表单 -->
      <el-col :span="type == '新增' ? 24 : 12">
        <el-card>
          <div slot="header">新数据</div>
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
            <el-form-item label="机构简介">
              <el-input v-model.trim="form.orgmeno" type="textarea" />
            </el-form-item>
            <el-form-item label="备注">
              <el-input v-model.trim="form.memo" type="textarea" />
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
    <div class="footer" style="text-align: right" v-if="!disabled">
      <!-- <el-button @click="close">取 消</el-button> -->
      <el-button type="primary" @click="save">确 定</el-button>
      <el-button @click="ymsubmit" type="primary">提交</el-button>
    </div>
    <org-user-dialog ref="orgUserDialog" @select="handSelectOrgUser" />
    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="formId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
  </div>
</template>

<script>
  import { orgDetail, orgSave } from '@/api/setting/org'
  import OrgUserDialog from '@/views/setting/specialist/components/OrgUserDialog.vue'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import { getFlowList } from '@/api/setting/auth'
  export default {
    name: 'CompanyEdit',
    components: { OrgUserDialog, Resubmit },
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
        oldform: {
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
        type: '',
        formId: '',
        flowtaskinfoflowid: '',
        ymFromId: '',
        isWfqdedit: '',
        status: '',
      }
    },

    methods: {
      async showEdit(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status,
        oldData,
        newData,
        type
      ) {
        this.disabled = title == 'detail'
        this.type = type //判断是类型 1:新增 2:修改 3:删除
        // 处理oldData - 左侧原始数据
        this.form = Object.assign({}, newData)
        this.oldform = Object.assign({}, oldData)
        this.formId = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.isWfqdedit = isWfqdedit
        this.status = status

        // this.form.fatherorgid = pid
        // this.dialogFormVisible = true
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.oldform = this.$options.data().oldform
        this.dialogFormVisible = false
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { orgcreate, tblOrganization, ...other } = this.form
            const { msg, data } = await orgSave(other)

            // this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
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
      async ymsubmit() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            this.$refs.resubmit.ymsubmit()
          }
        })
      },
    },
  }
</script>

<style scoped>
  .input-psword {
    -webkit-text-security: disc;
  }

  .compare-container {
    display: flex;
    gap: 20px;
    padding: 20px;
    position: relative;
    padding-bottom: 80px;
  }

  .compare-panel {
    flex: 1;
    border: 1px solid #e4e7ed;
    border-radius: 4px;
    background: #fff;
  }

  .panel-header {
    background: #f5f7fa;
    padding: 15px 20px;
    border-bottom: 1px solid #e4e7ed;
  }

  .panel-header h3 {
    margin: 0;
    color: #303133;
    font-size: 16px;
    font-weight: 500;
  }

  .compare-panel .el-form {
    padding: 20px;
  }

  .footer {
    /* position: absolute;
    bottom: 0;
    right: 0; */
    background: #fff;
    /* padding: 15px 20px; */
    text-align: center;
    z-index: 1000;
  }

  .color-red {
    color: #f56c6c;
    font-size: 12px;
    margin-top: 5px;
    display: block;
  }
</style>
