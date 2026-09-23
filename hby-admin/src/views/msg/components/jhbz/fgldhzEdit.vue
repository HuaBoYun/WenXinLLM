<template>
  <div>
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
              disabled
              :style="{ width: '75%' }"
            />
            <el-button
              @click="projectManager"
              style="margin-left: 10px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="立项要求" prop="lxyq">
            <el-input
              type="textarea"
              :rows="4"
              v-model="formData.lxyq"
              clearable
              placeholder="请输入立项要求"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="落实建议" prop="lsjy">
            <el-input
              type="textarea"
              :rows="4"
              v-model="formData.lsjy"
              clearable
              placeholder="请输入落实建议"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="拟稿人" prop="cjr">
            <el-input
              v-model="formData.cjr"
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
    <div v-if="!formDisabled" style="text-align: right; margin-top: 20px">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
      <el-button type="primary" @click="ymsubmit">提交</el-button>
    </div>
    <project-manage
      @projectManage="getChildlistPro"
      ref="manage"
      :multiple="false"
    ></project-manage>

    <Resubmit
      ref="resubmit"
      @fetchClose="close"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      :status="status"
    />
  </div>
</template>

<script>
import { fgldhzDetail, fgldhzSaveOrUpdate } from '@/api/monitor/question'
import projectManage from '@/views/fwgl/pfpx/components/selectPerson.vue'
import Resubmit from '@/views/msg/components/options/Resubmit.vue'

export default {
  name: 'gcjgysjhEdit',
  inheritAttrs: false,
  props: [],
  components: { projectManage, Resubmit },
  data() {
    return {
      formData: {},
      formDisabled: false,
      tableData: [],
      rules: {
        gsldxm: [
          {
            required: true,
            message: '请输入公司领导',
            trigger: 'blur',
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
      // 流程相关
      flowtaskinfoflowid: '',
      fromId: '',
      ymFromId: '',
      fromIdcopy: '',
      status: '',
    }
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  methods: {
    getCurrentDate() {
      return new Date(+new Date() + 8 * 3600 * 1000).toJSON().substr(0, 10)
      // .replace('T', ' ')
    },
    async showEdit(
      title,
      row,
      flowtaskinfoflowid,
      ymFromId,
      isWfqdedit,
      status
    ) {
      // 流程相关
      this.fromId = row
      this.flowtaskinfoflowid = flowtaskinfoflowid
      this.ymFromId = ymFromId
      this.status = status

      console.log('row', row)

      if (title == 'edit') {
        this.title = '编辑'
      } else if (title == 'detail') {
        this.title = '详细'
        this.formDisabled = true
      } else if (title == 'add') {
        this.title = '新增'
        let resL = JSON.parse(localStorage.getItem('userInfo')).realname
        this.formData = {
          ...this.formData,
          cjr: resL,
          cjsj: this.getCurrentDate(),
        }
      }

      this.dialogFormVisible = true
      if (row) {
        const res = await fgldhzDetail({ fgldhzid: row })
        this.formData = res.data
      }
    },
    close() {
      this.formData = {}
      this.dialogFormVisible = false
      this.tableData = []
      this.formDisabled = false
      this.$bus.$emit('updateMsg', 0)
    },
    save() {
      this.$refs['ruleForm'].validate(async (valid) => {
        if (valid) {
          const res = await fgldhzSaveOrUpdate({
            ...this.formData,
          })
          if (res.code == 1) {
            this.$baseMessage('保存成功', 'success')
            this.$emit('fetchData')
            // this.close()
          } else {
            this.$baseMessage(res.msg, 'error')
          }
        }
      })
    },
    async handleDown(row) {
      const data = await download({ attId: row.attid })
      let filename = row.attname
      let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
      let url = window.URL.createObjectURL(blob, {
        type: 'application/vnd.ms-excel',
      })
      const link = document.createElement('a')
      link.style.display = 'none'
      link.href = url
      link.setAttribute('download', filename)
      document.documentElement.appendChild(link)
      link.click()
      document.documentElement.removeChild(link)
    },
    async handleDelete(row) {
      let list = this.tableData
      list = list.filter((item) => item.attid != row.attid)
      this.tableData = list
      await deleteFile({ attId: row.attid })
    },
    handlePreview(file) {},
    handleSuccess(file) {
      if (file.result == '200') {
        let list = this.tableData
        list.push(file.data)
        this.tableData = list
        this.$baseMessage(file.msg, 'success')
      } else {
        this.$baseMessage(file.msg, 'error')
      }
    },
    projectManager() {
      this.$refs['manage'].showEdit()
    },
    async getChildlistPro(val) {
      this.$set(this.formData, 'gsldid', val[0].staffid)
      this.$set(this.formData, 'gsldxm', val[0].realname)
    },
    async ymsubmit() {
      this.$refs['ruleForm'].validate(async (valid) => {
        if (valid) {
          this.$refs.resubmit.ymsubmit()
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
