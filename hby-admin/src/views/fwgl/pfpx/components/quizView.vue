<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="提问人" prop="name">
            <el-input
              v-model="formData.name"
              disabled
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="知识类型" prop="knowledgeType">
            <el-select
              v-model="formData.knowledgeType"
              clearable
              placeholder="知识类型"
              :style="{ width: '100%' }"
            >
              <el-option :value="1" label="劳动用工"></el-option>
              <el-option :value="2" label="知识产权"></el-option>
              <el-option :value="3" label="投融资"></el-option>
              <el-option :value="4" label="法律尽调"></el-option>
              <el-option :value="5" label="法律纠纷"></el-option>
              <el-option :value="6" label="其他"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="创建时间" prop="createTime">
            <el-date-picker
              :style="{ width: '100%' }"
              v-model="formData.createTime"
              placeholder="创建时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="timestamp"
              disabled
            />
          </el-form-item>
        </el-col> -->
        <el-col :span="24">
          <el-form-item label="内容" prop="content">
            <el-input
              v-model="formData.content"
              clearable
              type="textarea"
              :rows="8"
              placeholder="请输入内容"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <template #footer v-if="footer">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="addProblem" type="primary">确定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { addMsg } from '@/api/fwgl/pfpx'
  import { uploadApi } from '@/api/fwgl/zzxx'
  import { baseURL } from '@/config'
  import store from '@/store'
  import { formatDate } from '@/utils'
  const token = store.getters['user/token']
  export default {
    components: {},
    inheritAttrs: false,
    props: [],
    data() {
      return {
        formData: {
          content: '',
          // createTime: '',
          // id: '',
          ip: '',
          nickName: '',
          name: '',
          // replied: false,
          replyContent: '',
          replyName: '',
          replyTime: '',
        },
        rules: {
          nickName: [
            { required: true, message: '请输入昵称', trigger: 'blur' },
          ],
          knowledgeType: [
            { required: true, message: '请选择知识类型', trigger: 'blur' },
          ],
          content: [{ required: true, message: '请输入内容', trigger: 'blur' }],
        },
        footer: true,
        dialogFormVisible: false,
        title: '新增',
        tableData: [],
        fileList: [],
        baseApi: baseURL,
        api: uploadApi,
        headers: { token: token },
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      /**
       * @description: 外部打开dialog
       * @param {*} title 表单类型
       * @param {*} row 行数据
       * @return {*}
       */      
      showEdit(title, row) {
        this.formData = this.$options.data().formData
        const userInfo = JSON.parse(localStorage.getItem('userInfo'))
        this.$set(this.formData, 'name', userInfo.realname)
        this.$set(this.formData, 'nickName', userInfo.staffid)
        this.dialogFormVisible = true
        this.fileList = []

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else {
          this.title = '提问'
        }
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.dialogFormVisible = false
        this.footer = true
      },
      addProblem() {
        this.$refs.ruleForm.validate(async (valid) => {
          if (valid) {
            this.formData.createTime = formatDate(new Date())
            const res = await addMsg(this.formData)
            this.$emit('fentch-data')
            this.dialogFormVisible = false
          } else {
            return false
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
