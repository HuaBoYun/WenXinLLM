<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    v-if="dialogFormVisible"
  >
    <el-row :gutter="14">
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
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary" :loading="loading">确定</el-button>
      <el-button
        @click="handleApproval"
        type="primary"
        :disabled="!this.editId"
      >
        提交审批
      </el-button>
    </div>

    <project-manage
      @projectManage="getChildlistPro"
      ref="manage"
    ></project-manage>
    <IP @select="ipSelect" ref="ip"></IP>
    <ProcessList ref="process" @fetchData="close" />
  </el-dialog>
</template>

<script>
  import { baseURL } from '@/config'
  import store from '@/store'
  import projectManage from '@/views/audit/project/components/formComponents/projectManage.vue'
  import IP from '@/views/oilAudit/zhgl/components/selectIp.vue'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import { editInfor, getInfoDetail } from '@/oapi/ypns_zhgl/wwdlfwgl.js'

  const token = store.getters['user/token']

  export default {
    components: { projectManage, IP, ProcessList },
    inheritAttrs: false,
    props: ['fetchData'],
    data() {
      return {
        loading: false,
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
        dialogFormVisible: false,
        title: '新增',
        editId: '',
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      getCurrentDate() {
        return new Date(+new Date() + 8 * 3600 * 1000)
          .toJSON()
          .substr(0, 19)
          .replace('T', ' ')
      },
      async showEdit(row, title) {
        this.dialogFormVisible = true
        if (row) {
          this.editId = row.id
          const res = await getInfoDetail({ id: row.id })

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

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详情'
          this.footer = false
        } else if (title == 'add') {
          this.title = '新增'
          let resL = JSON.parse(localStorage.getItem('userInfo')).realname
          this.formData = {
            ...this.formData,
          }
        }
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
        this.editId = ''
        this.dialogFormVisible = false
        this.loading = false
        this.footer = true
        this.$emit('fetchData')
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            const params = JSON.parse(JSON.stringify(this.formData))
            const res = await editInfor(params)
            if (res && res.code == 200) {
              this.editId = res.data.id
              this.formData.id = res.data.id
              this.$message({
                message: '保存成功！',
                type: 'success',
              })
            } else {
              this.$message({
                message: '保存失败',
                type: 'error',
              })
            }
            this.loading = false
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
      openIP() {
        this.$refs.ip.showEdit()
      },
      ipSelect(val) {
        console.log(val, 'val')
        this.formData.ipAddress = val[0].ipAddress
        this.formData.ipInventoryId = val[0].id
      },
      handleApproval() {
        //提交审批
        this.$refs['process'].save(130, this.editId)
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
