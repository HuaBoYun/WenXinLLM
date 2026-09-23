<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="700px"
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
        <!-- <el-col :span="24">
          <el-form-item label="执行方式" prop="implementType">
            <el-select
              v-model="formData.implementType"
              placeholder="请选择执行方式"
              clearable
              :style="{ width: '100%' }"
            >
              <el-option label="资源库方式运行" value="资源库方式运行" />
              <el-option label="文件方式运行" value="文件方式运行" />
              <el-option label="FTP方式运行" value="FTP方式运行" />
            </el-select>
          </el-form-item>
        </el-col> -->
        <div>
          <el-col :span="24">
            <el-form-item label="所属资源库" prop="repoName">
              <el-select
                v-model="formData.jobRepositoryId"
                placeholder="请选择资源库"
                clearable
                :style="{ width: '100%' }"
              >
                <el-option
                  v-for="item in resourcePoolOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="24">
            <el-form-item label="采集类型" prop="acquisitionType">
              <el-select
                v-model="formData.acquisitionType"
                placeholder="请选择采集类型"
                clearable
                :style="{ width: '100%' }"
              >
                <el-option label="作业" value="作业" />
                <el-option label="作业" value="作业" />
              </el-select>
            </el-form-item>
          </el-col> -->
          <!-- <el-col :span="24">
            <el-form-item label="作业路径" prop="jobformPath">
              <el-input
                v-model="formData.jobformPath"
                readonly
                placeholder="请选择作业路径"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col> -->
        </div>
        <!-- <div v-if="formData.implementType == '文件方式运行'">
          <el-col :span="24">
            <el-form-item label="作业上传" prop="jobformFileName">
              <el-input
                v-model="formData.jobformFileName"
                readonly
                placeholder="请上传作业"
                :style="{ width: '100%' }"
                :disabled="!footer"
              >
                <el-upload
                  action="https://jsonplaceholder.typicode.com/posts/"
                  :on-preview="handlePreview"
                  :before-upload="beforeUpload"
                  accept="image/png"
                  show-file-list="false"
                  multiple
                  :limit="3"
                  :on-exceed="handleExceed"
                  :file-list="fileList"
                  slot="append"
                >
                  <el-button size="small" type="primary">点击上传</el-button>
                </el-upload>
              </el-input>
              
            </el-form-item>
          </el-col>
        </div> -->
        <el-col :span="24">
          <el-form-item label="作业名称" prop="jobName">
            <el-input
              v-model="formData.jobName"
              clearable
              placeholder="请输入作业名称"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="24">
          <el-form-item label="作业参数" prop="jobformDate">
            <el-input
              v-model="formData.jobformDate"
              readonly
              placeholder="{}"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col> -->
        <el-col :span="24">
          <el-form-item label="定时策略" prop="cron">
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
          <el-form-item label="定时策略" prop="timingStrategy">
            <el-select
              v-model="formData.timingStrategy"
              placeholder="请选定时策略"
              clearable
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="item in timingOptions"
                :key="item.value"
                :label="item.value"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col> -->
        <el-col :span="24">
          <el-form-item label="记录日志级别" prop="jobLogLevel">
            <el-select
              v-model="formData.jobLogLevel"
              placeholder="请选择记录日志级别"
              clearable
              :style="{ width: '100%' }"
            >
              <el-option label="错误日志" value="error" />
              <el-option label="最小日志" value="minimal" />
              <el-option label="基本日志" value="basic" />
              <el-option label="详细日志" value="detail" />
              <el-option label="调试日志" value="debug" />
              <el-option label="行级日志（非常详细）" value="rowlevel" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="作业描述" prop="jobDescription">
            <el-input
              type="textarea"
              :rows="2"
              v-model="formData.jobDescription"
              clearable
              placeholder="请输入作业描述"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" v-if="footer">
      <el-button @click="close">取 消</el-button>
      <el-button @click="save" type="primary">保 存</el-button>
      <el-button
        @click="test"
        type="primary"
        v-if="formData.libraryType == '数据库资源库'"
      >
        测试连接
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
  import store from '@/store'
  import { baseURL } from '@/config'
  import { cron } from 'vue-cron'
  const token = store.getters['user/token']
  import { getRepositoryListAll, addJob, editJob } from '@/api/sjzt/etl/etl'
  import { formatDate } from '@/utils'
  export default {
    components: { cron },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        resourcePoolOptions: [],
        edit: '',
        cronPopover: false,
        loading: false,
        baseURL: baseURL,
        headers: { token: token },
        tableData: [],
        formData: {
          jobLogLevel: 'basic',
          jobType: 'File',
          jobPath: '/',
          jobRepositoryId: '',
          creator: '',
          staffid: '',
          belongGroupName: '',
          orgid: '',
          createdTime: '',
        },
        footer: true,
        rules: {
          jobName: [
            {
              required: true,
              message: '请输入作业名称',
              trigger: 'blur',
            },
          ],
          jobRepositoryId: [
            {
              required: true,
              message: '请选择所属资源库',
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
    created() {
      this.getList()
    },
    mounted() {},
    methods: {
      async getList() {
        const { data } = await getRepositoryListAll()
        data.forEach((item) => {
          this.resourcePoolOptions.push({
            label: item.repoName,
            value: item.id,
          })
          //
        })
        // const res = await getRepositoryListAll()
        //
      },
      changeCron(val) {
        this.formData.cron = val
      },
      // beforeUpload(file) {
      //
      //   this.formData.jobformFileName = file.name
      //
      //   return false
      // },
      handleRemove(file, fileList) {},
      handlePreview(file) {},
      handleExceed(files, fileList) {
        this.$message.warning(
          `当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${
            files.length + fileList.length
          } 个文件`
        )
      },
      beforeRemove(file, fileList) {
        return this.$confirm(`确定移除 ${file.name}？`)
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
        if (row) {
          this.formData = row
          this.edit = title
        } else {
          this.formData = {
            jobLogLevel: 'basic',
            jobType: 'File',
            jobPath: '/',
          }
        }
        this.dialogFormVisible = true
      },
      close() {
        this.formData = {
          jobLogLevel: 'basic',
          jobType: 'File',
          jobPath: '/',
        }
        this.tableData = []
        this.dialogFormVisible = false
        this.footer = true
      },
      async save() {
        this.loading = true
        if (this.edit == 'edit') {
          editJob(this.formData).then((res) => {
            if (res.msg == '操作成功') {
              this.dialogFormVisible = false
              this.$emit('fetch-data')
            }
          })
        } else {
          addJob(this.formData).then((res) => {
            if (res.msg == '操作成功') {
              this.dialogFormVisible = false
              this.$emit('fetch-data')
            }
          })
        }

        //
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
