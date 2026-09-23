<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    append-to-body
  >
    <el-row :gutter="24">
      <el-form
        :model="formData"
        :rules="rules"
        label-width="140px"
        :disabled="formDisabled"
        ref="ruleForm"
      >
        <el-col :span="12">
          <el-form-item label="文件编号" prop="ruleCode">
            <el-input
              placeholder="请输入文件编号"
              v-model="formData.ruleCode"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="文件名称" prop="ruleName">
            <el-input
              placeholder="请输入文件名称"
              v-model="formData.ruleName"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发文文号" prop="ruleNumber">
            <el-input
              placeholder="请输入发文文号"
              v-model="formData.ruleNumber"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发文部门" prop="publishOrg">
            <el-input
              placeholder="请选择发文部门"
              v-model="formData.publishOrg"
              readonly
              :disabled="formDisabled"
              style="width: 78%"
            ></el-input>
            <el-button
              @click="selectDept"
              style="margin-left: 10px"
              type="primary"
              :disabled="formDisabled"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发文日期" prop="publishDate">
            <el-date-picker
              v-model="formData.publishDate"
              type="date"
              style="width: 100%"
              value-format="yyyy-MM-dd"
              placeholder="选择发文日期"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="生效日期" prop="takeEffectTime">
            <el-date-picker
              v-model="formData.takeEffectTime"
              type="date"
              style="width: 100%"
              value-format="yyyy-MM-dd"
              placeholder="选择生效日期"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="时效性" prop="timeLiness">
            <el-select
              v-model="formData.timeLiness"
              placeholder="请选择时效性"
              style="width: 100%"
              :disabled="formDisabled"
            >
              <el-option label="现行有效" value="现行有效"></el-option>
              <el-option label="征求意见稿" value="征求意见稿"></el-option>
              <el-option label="已失效" value="已失效"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="录入人" prop="enteringPerson">
            <el-input
              placeholder="请选择录入人"
              v-model="formData.enteringPerson"
              readonly
              :disabled="formDisabled"
              style="width: 78%"
            />
            <el-button
              @click="openPersonModal"
              style="margin-left: 10px"
              type="primary"
              :disabled="formDisabled"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="摘要" prop="summaryInfo">
            <el-input
              placeholder="请输入摘要"
              v-model="formData.summaryInfo"
              type="textarea"
              :rows="4"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div
            style="text-align: right; margin-bottom: 5px"
            v-if="!formDisabled"
          >
            <el-upload
              style="text-align: right; margin-bottom: 5px"
              class="upload-demo"
              :show-file-list="false"
              action=""
              :headers="headers"
              :on-preview="handlePreview"
              :on-success="handleSuccess"
              :file-list="fileList"
              :before-upload="handleBeforeUpload"
              :multiple="true"
            >
              <div style="margin-right: 10px">
                <el-button type="success">点击上传</el-button>
              </div>
            </el-upload>
          </div>
          <el-table :data="tableData">
            <el-table-column align="center" label="附件名称" prop="attname" />
            <el-table-column
              align="center"
              label="文件大小(KB)"
              prop="attsize"
            />
            <el-table-column align="center" label="创建人" prop="uploader" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="200"
            >
              <template #default="{ row }">
                <el-button
                  :disabled="false"
                  type="text"
                  @click="handleDowns(row)"
                >
                  下载
                </el-button>
                <el-button
                  type="text"
                  :disabled="false"
                  @click="handlePreviewFile(row)"
                >
                  预览
                </el-button>
                <el-button
                  type="text"
                  @click="handleDelete(row)"
                  v-if="!formDisabled"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <template slot="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="submit" type="primary">确认</el-button>
    </template>

    <!-- 部门选择组件 -->
    <DepartmentOption ref="DepartmentOption" @submit="selectedDept" />
    <!-- 人员选择组件 -->
    <ExecutorOptions ref="executor" @projectManage="handleExecutorSelected" />
  </el-dialog>
</template>

<script>
  import { saveOrUpdate, getDetail } from '@/api/internal/nowModo.js'
  import store from '@/store'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  import DepartmentOption from '@/components/departmentSelect.vue'
  import ExecutorOptions from '@/components/danxuanPerson.vue'
  const { baseURL } = require('@/config')
  const token = store.getters['user/token']

  export default {
    components: {
      DepartmentOption,
      ExecutorOptions,
    },
    data() {
      return {
        title: '',
        dialogFormVisible: false,
        formDisabled: false,
        fileList: [],
        tableData: [],
        baseApi: baseURL,
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        headers: { token: token },

        formData: {
          id: '',
          ruleCode: '',
          ruleName: '',
          ruleNumber: '',
          publishOrg: '',
          publishDate: '',
          takeEffectTime: '',
          timeLiness: '',
          summaryInfo: '',
          enteringPerson: '',
          createOrgId: '',
          createStaffId: '',
        },
        rules: {
          ruleCode: [
            { required: true, message: '请输入文件编号', trigger: 'blur' },
          ],
          ruleName: [
            { required: true, message: '请输入文件名称', trigger: 'blur' },
          ],
          ruleNumber: [
            { required: true, message: '请输入发文文号', trigger: 'blur' },
          ],
          publishOrg: [
            { required: true, message: '请输入发文部门', trigger: 'blur' },
          ],
          publishDate: [
            { required: true, message: '请选择发文日期', trigger: 'change' },
          ],
          takeEffectTime: [
            { required: true, message: '请选择生效日期', trigger: 'change' },
          ],
          timeLiness: [
            { required: true, message: '请选择时效性', trigger: 'change' },
          ],
        },
      }
    },
    methods: {
      async showEdit(row, disabled) {
        this.formDisabled = disabled
        this.dialogFormVisible = true
        this.tableData = []

        if (row) {
          this.title = disabled ? '查看现行标准' : '编辑现行标准'
          try {
            const { data, code } = await getDetail({ id: row.id })
            if (code === 1) {
              Object.assign(this.formData, data.data)
              if (data.attList && data.attList.length > 0) {
                this.tableData = data.attList
              }
            }
          } catch (error) {
            console.error('获取详情失败:', error)
          }
        } else {
          this.title = '新建现行标准'
          // 获取当前登录用户信息
          const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
          this.formData = {
            id: '',
            ruleCode: '',
            ruleName: '',
            ruleNumber: '',
            publishOrg: '',
            publishDate: '',
            takeEffectTime: '',
            timeLiness: '',
            summaryInfo: '',
            enteringPerson: userInfo.realname || '',
            createOrgId: '',
            createStaffId: userInfo.staffid || '',
          }
        }
      },
      submit() {
        this.$refs.ruleForm.validate(async (valid) => {
          if (valid) {
            await this.saveData()
          } else {
            console.log('error submit!!')
            return false
          }
        })
      },
      async saveData() {
        let attids = ''
        this.tableData.map((item) => {
          attids += item.attid
          attids += ','
        })
        attids = attids.substring(0, attids.length - 1)

        try {
          const { data, code, msg } = await saveOrUpdate({
            ...this.formData,
            attids: attids,
          })
          if (code === 1) {
            this.$message.success('保存成功')
            this.$emit('fetch-data')
            this.close()
          } else {
            this.$message.error(msg || '保存失败')
          }
        } catch (error) {
          console.error('保存失败:', error)
          this.$message.error('保存失败')
        }
      },
      close() {
        this.formDisabled = false
        this.dialogFormVisible = false
        // 获取当前登录用户信息
        const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
        this.formData = {
          id: '',
          ruleCode: '',
          ruleName: '',
          ruleNumber: '',
          publishOrg: '',
          publishDate: '',
          takeEffectTime: '',
          timeLiness: '',
          summaryInfo: '',
          enteringPerson: userInfo.realname || '',
          createOrgId: '',
          createStaffId: userInfo.staffid || '',
        }
        this.tableData = []
        this.fileList = []
      },
      // 删除附件
      handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        this.tableData = list
        this.$message({
          type: 'success',
          message: '删除成功!',
        })
      },
      // 上传前处理
      handleBeforeUpload(file, fileList) {
        const isLt2M = file.size / 1024 / 1024 < 100 // 检查文件大小是否小于100MB
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 100MB!')
          return false // 返回false停止上传
        }
        // 如果文件大小合适，则调用自定义上传逻辑
        this.customUploadWrapper({ file })
        return false // 停止默认上传行为
      },
      // 自定义上传包装器
      customUploadWrapper(options) {
        if (
          !this.baseApi ||
          !this.api ||
          !this.headers ||
          !window.key ||
          !window.iv
        ) {
          return
        }
        // 确保 fileList 是一个数组
        const fileList = Array.isArray(options.file)
          ? options.file
          : [options.file]
        // 获取 el-upload 的 data 参数
        const formData = {}
        // 调用自定义上传函数
        new Promise((resolve, reject) => {
          customUpload({
            baseApi: this.baseApi,
            api: this.api,
            key: window.key,
            iv: window.iv,
            headers: this.headers,
            fileList: fileList,
            formData: formData,
            onProgress: this.handleProgress,
            onSuccess: (response) => {
              this.handleSuccess(response)
              resolve(response)
            },
            onError: (error) => {
              reject(error)
            },
          })
        })
      },
      // 上传成功处理
      handleSuccess(file) {
        if (file.code == 200) {
          this.tableData = [...this.tableData, ...file.data]
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      // 下载文件
      async handleDowns(row) {
        try {
          await handleDown(row, this.headers, this.lodeapi)
        } catch (error) {
          console.error('下载失败:', error)
        }
      },
      // 预览文件
      handlePreviewFile(row) {
        if (row.isEncrypted === '1') {
          const previewUrl = row.previewUrl
          window.open(previewUrl, '_blank')
        } else {
          this.$iFrameDialog({ attid: row.attid })
        }
      },
      handlePreview(row) {
        // 预览逻辑
      },
      handleProgress(event) {
        // 上传进度处理
      },
      // 选择部门
      selectDept() {
        this.$refs['DepartmentOption'].showEdit()
      },
      // 部门选择回调
      selectedDept(e) {
        this.formData.createOrgId = e.id
        this.formData.publishOrg = e.label
      },
      // 打开人员选择弹窗
      openPersonModal() {
        this.$refs.executor.showEdit()
      },
      // 处理选中的人员
      handleExecutorSelected(data) {
        this.formData.enteringPerson = data[0].realname
        this.formData.createStaffId = data[0].staffid
      },
    },
  }
</script>

<style lang="scss" scoped></style>
