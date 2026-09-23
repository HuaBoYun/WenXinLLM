<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="4">
      <el-form
        ref="ruleForm"
        label-width="150px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="formDisabled"
      >
        <el-col :span="24">
          <el-form-item label="公司领导" prop="gsldxm">
            <el-input
              v-model="formData.gsldxm"
              clearable
              placeholder="请输入公司领导"
              :style="{ width: '90%' }"
            />
            <el-button
              @click="projectManager"
              style="margin-left: 25px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="立项要求" prop="lxyq">
            <el-input
              v-model="formData.lxyq"
              type="textarea"
              :rows="4"
              clearable
              placeholder="请输入立项要求"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="落实建议" prop="lsjy">
            <el-input
              v-model="formData.lsjy"
              type="textarea"
              :rows="4"
              clearable
              placeholder="请输入落实建议"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="拟稿人" prop="cjrxm">
            <el-input
              v-model="formData.cjrxm"
              disabled
              placeholder="请输入拟稿人"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="拟稿日期" prop="cjsj">
            <el-date-picker
              v-model="formData.cjsj"
              placeholder="请输入拟稿日期"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" v-if="!formDisabled">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary" :loading="loading">
        确定
      </el-button>
    </div>
    <project-manage
      @projectManage="getChildlistPro"
      ref="manage"
      :multiple="false"
    ></project-manage>
  </el-dialog>
</template>

<script>
  import { fgldhzSaveOrUpdate, fgldhzDetail} from '@/api/monitor/question'
  import projectManage from '@/components/selectPerson.vue'

  export default {
    name: 'gcjgysjhEdit',
    inheritAttrs: false,
    props: [],
    components: { projectManage },
    data() {
      return {
        loading: false,
        formData: {
          gsldxm: '',
          gsldid: '',
          lxyq: '',
          lsjy: '',
          cjrxm: '',
          cjsj: '',
        },
        fgldhzid: '',
        formDisabled: false,
        rules: {
          gsldxm: [
            {
              required: true,
              message: '请输入公司领导',
              trigger: 'change',
            },
          ],
          lxyq: [
            {
              required: true,
              message: '请输入立项要求',
              trigger: 'blur',
            },
          ],
          lsjy: [
            {
              required: true,
              message: '请输入落实建议',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
      }
    },
    methods: {
      async showEdit(row, disabled) {
        this.dialogFormVisible = true
        this.formDisabled = !!disabled
        if (row) {
         const res = await fgldhzDetail({fgldhzid:row.fgldhzid})
          this.title = disabled ? '详细' : '编辑'
          this.fgldhzid = row.fgldhzid
          Object.keys(this.formData).forEach((key) => {
            this.formData[key] = res.data[key]
          })
        } else {
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          const createUserName = userInfo.realname
          const createTime = new Date().toJSON().split('T')[0]
          this.formData.cjrxm = createUserName
          this.formData.cjsj = createTime
        }
      },
      close() {
        this.formData = {
          gsldxm: '',
          gsldid: '',
          lxyq: '',
          lsjy: '',
          cjrxm: '',
          cjsj: '',
        }
        this.dialogFormVisible = false
        this.formDisabled = false
        this.fgldhzid = ''
      },
      save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            let params = { ...this.formData }
            if (this.title === '编辑') params.fgldhzid = this.fgldhzid
            const res = await fgldhzSaveOrUpdate(params)
            this.loading = false
            if (res && res.code == 1) {
              this.$message.success('保存成功！')
              this.$emit('fetchData')
              this.close()
            } else {
              this.$message.error(res.msg || '保存失败！')
            }
          }
        })
      },
      projectManager() {
        this.$refs['manage'].showEdit()
      },
      async getChildlistPro(val) {
        const ids=val.map(res=>res.staffid)
        const names=val.map(res=>res.realname)
        this.$set(this.formData, 'gsldid', ids.toString())
        this.$set(this.formData, 'gsldxm', names.toString())
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
