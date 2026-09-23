<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14" v-loading="loading">
      <el-form
        ref="ruleForm"
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="24">
          <el-form-item label="执行策略名称" prop="executionName">
            <el-input
              v-model="formData.executionName"
              clearable
              placeholder="请输入执行策略名称"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="执行策略" prop="cron">
            <el-popover v-model="cronPopover">
              <cron @change="changeCron" @close="cronPopover = false"></cron>
              <el-input
                slot="reference"
                @click="cronPopover = true"
                v-model="formData.cron"
                placeholder="请输入定时策略"
                i18n="cn"
              ></el-input>
            </el-popover>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="24">
          <el-form-item label="执行策略" prop="executionStrategy">
            <el-input
              placeholder="请输入执行策略"
              v-model="formData.executionStrategy"
              :style="{ width: '90%' }"
              :disabled="!footer"
              clearable
            ></el-input>
            <el-button
              icon="el-icon-search"
              @click="handleAdd"
              :style="{ width: '9%', marginLeft: '5px' }"
            ></el-button>
          </el-form-item>
        </el-col> -->
      </el-form>
    </el-row>
    <div slot="footer" v-if="footer">
      <el-button @click="close">取 消</el-button>
      <el-button @click="save" type="primary">保 存</el-button>
    </div>
    <CromEdit ref="edit" />
  </el-dialog>
</template>

<script>
  import store from '@/store'
  import { baseURL } from '@/config'
  import CromEdit from './cronEdit.vue'
  import { cron } from 'vue-cron'
  // import 'element-ui/lib/theme-chalk/index.css'

  const token = store.getters['user/token']
  import { formatDate } from '@/utils'

  export default {
    components: { CromEdit, cron },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        cron: '',
        cronPopover: false,
        loading: false,
        baseURL: baseURL,
        headers: { token: token },
        tableData: [],
        formData: {
          cron: '',
          creator: '',
          staffid: '',
          belongGroupName: '',
          orgid: '',
          createdTime: '',
        },
        footer: true,
        rules: {
          executionName: [
            {
              required: true,
              message: '请输入执行策略名称',
              trigger: 'blur',
            },
          ],
          cron: [
            {
              required: true,
              message: '请选择执行策略',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      changeCron(val) {
        this.formData.cron = val
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handlePreview() {},
      handleSuccess(res, b, c) {
        if (res && res.data) {
          this.tableData = this.tableData.concat(res.data.fileIds || [])
        }
      },
      handleDown(row) {},
      handleDelete(row) {
        const { fileId } = row
        const i = this.tableData.findIndex((x) => x.fileId === fileId)
        if (i > -1) {
          this.tableData.splice(i, 1)
        }
      },
      async showEdit(title, row) {
        this.formData = {}
        this.dialogFormVisible = true
      },
      close() {
        this.formData = {}
        this.tableData = []
        this.dialogFormVisible = false
        this.footer = true
      },
      async save() {
        this.loading = true
        this.loading = false
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
