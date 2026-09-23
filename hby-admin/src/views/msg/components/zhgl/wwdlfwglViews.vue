<template>
  <div>
    <el-row :gutter="14" v-loading="loading">
      <el-form
        ref="ruleForm"
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="!footer"
      >
        <el-col :span="12">
          <el-form-item label="IP地址" prop="ipAddress">
            <el-input
              v-model="formData.ipAddress"
              disabled
              placeholder="请输入IP地址"
              :style="{ width: '70%' }"
            />
            <el-button
              @click="openIP()"
              style="margin-left: 10px"
              type="primary"
              size="mini"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="MAC地址" prop="macAddress">
            <el-input
              v-model="formData.macAddress"
              clearable
              placeholder="请选择MAC地址"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="部门名称" prop="departmentsName">
            <el-input
              v-model="formData.departmentsName"
              clearable
              placeholder="请选择部门名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="使用人" prop="usePeople">
            <el-input
              v-model="formData.usePeople"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入使用人"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系电话" prop="contactPhone">
            <el-input
              v-model="formData.contactPhone"
              clearable
              placeholder="请输入联系电话"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="电子邮件" prop="mailAddress">
            <el-input
              v-model="formData.mailAddress"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入电子邮件"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="杀毒软件" prop="antivirusSoftware">
            <el-input
              v-model="formData.antivirusSoftware"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入杀毒软件"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="杀毒厂商" prop="antivirusManufacturer">
            <el-input
              v-model="formData.antivirusManufacturer"
              :style="{ width: '100%' }"
              placeholder="请输入杀毒厂商"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <div style="text-align: right; margin-top: 10px" v-if="footer">
      <el-button @click="save" type="primary" :loading="listLoading">确定</el-button>
      <el-button @click="ymsubmit" type="primary">提交</el-button>
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
    <project-manage
      @projectManage="getChildlistPro"
      ref="manage"
    ></project-manage>
    <IP @select="ipSelect" ref="ip"></IP>
  </div>
</template>

<script>
  import { baseURL } from '@/config'
  import store from '@/store'
  import projectManage from '@/views/audit/project/components/formComponents/projectManage.vue'
  import IP from '@/views/oilAudit/zhgl/components/selectIp.vue'
  import { editInfor, getInfoDetail } from '@/oapi/ypns_zhgl/wwdlfwgl.js'

  const token = store.getters['user/token']
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'

  export default {
    components: { projectManage, Resubmit, IP },
    inheritAttrs: false,
    data() {
      return {
        loading: false,
        listLoading: false,
        baseURL: baseURL,
        uploadApi: '/audit/fileManage/upload',
        headers: { token: token },
        tableData: [],
        formData: {
          id: '',
          ipAddress: '',
          macAddress: '',
          mailAddress: '',
          usePeople: '',
          antivirusManufacturer: '',
          antivirusSoftware: '',
          contactPhone: '',
          departmentsName: '',
          ipInventoryId: '',
        },
        footer: true,
        rules: {
          mailAddress: [
            {
              required: true,
              message: '请输入电子邮件',
              trigger: 'blur',
            },
          ],
          departmentsName: [
            {
              required: true,
              message: '请输入部门名称',
              trigger: 'blur',
            },
          ],
          ipAddress: [
            {
              required: true,
              message: '请输入IP地址',
              trigger: 'blur',
            },
          ],
          macAddress: [
            {
              required: true,
              message: '请输入MAC地址',
              trigger: 'blur',
            },
          ],
          contactPhone: [
            {
              required: true,
              message: '请输入联系电话',
              trigger: 'blur',
            },
          ],
          usePeople: [
            {
              required: true,
              message: '请输入使用人',
              trigger: 'blur',
            },
          ],
          antivirusSoftware: [
            {
              required: true,
              message: '请输入杀毒软件',
              trigger: 'blur',
            },
          ],
          antivirusManufacturer: [
            {
              required: true,
              message: '请输入杀毒厂商',
              trigger: 'blur',
            },
          ],
        },
        title: '新增',
        fromId: '',
        flowtaskinfoflowid: '',
        ymFromId: '',
        status: '',
        fromIdcopy: '',
      }
    },
    computed: {},
    watch: {},
    created() {},
    methods: {
      getCurrentDate() {
        return new Date(+new Date() + 8 * 3600 * 1000)
          .toJSON()
          .substr(0, 19)
          .replace('T', ' ')
      },
      async showEdit(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status
      ) {
        this.footer = isWfqdedit
        if (title == 'detail') {
          this.footer = false
        }
        if (formId) {
          const res = await getInfoDetail({ id: formId })
          this.formData.ipAddress = res.data.ipAddress
          this.formData.ipInventoryId = res.data.ipInventoryId
          this.formData.macAddress = res.data.macAddress
          this.formData.mailAddress = res.data.mailAddress
          this.formData.usePeople = res.data.usePeople
          this.formData.antivirusManufacturer = res.data.antivirusManufacturer
          this.formData.antivirusSoftware = res.data.antivirusSoftware
          this.formData.contactPhone = res.data.contactPhone
          this.formData.departmentsName = res.data.departmentsName
          this.formData.id = res.data.id
        }

        this.fromId = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
      },
      close() {
        this.formData = {
          ipAddress: '',
          macAddress: '',
          mailAddress: '',
          usePeople: '',
          antivirusManufacturer: '',
          antivirusSoftware: '',
          contactPhone: '',
          departmentsName: '',
          id: '',
          ipInventoryId: '',
        }
        this.$bus.$emit('updateMsg', 0)
        this.footer = true
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.listLoading = true
            const params = JSON.parse(JSON.stringify(this.formData))
            const res = await editInfor(params)
            if (res && res.code == 200) {
              this.$emit('fetchData')
              this.$message({
                message: '保存成功！',
                type: 'success',
              })
            } else {
              this.$message({
                message: '提交失败',
                type: 'error',
              })
            }
            this.listLoading = false
          }
        })
      },
      projectManager() {
        this.$refs['manage'].showEdit()
      },
      async getChildlistPro(val) {
        this.staffid = val[0].staffid
        this.$set(this.formData, 'orgName', val[0].realname)
      },
      //流程提交引迈
      async ymsubmit() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.$refs.resubmit.ymsubmit()
          }
        })
      },
      openIP() {
        this.$refs.ip.showEdit()
      },
      ipSelect(val) {
        console.log(val, 'val')
        this.formData.ipAddress = val[0].ipAddress
        this.formData.ipInventoryId = val[0].id
      },
    },
  }
</script>
<style scoped>
  .el-form-item__contractname span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
