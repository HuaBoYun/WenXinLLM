<template>
  <el-dialog
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
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
          <el-form-item label="底稿编号" label-width="140px" prop="sheetCode">
            <el-input
              v-model="formData.sheetCode"
              clearable
              placeholder="请输入底稿编号"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="底稿名称" label-width="140px" prop="sheetName">
            <el-input
              v-model="formData.sheetName"
              clearable
              placeholder="请输入底稿名称"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被审计单位" label-width="140px" prop="orgname">
            <el-input
              v-model="formData.orgname"
              clearable
              placeholder="请输入被审计单位"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="审计事项"
            label-width="140px"
            prop="businessAffiliation"
          >
            <el-input
              v-model="formData.businessAffiliation"
              clearable
              placeholder="请输入审计事项"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计人员" label-width="140px" prop="realname">
            <el-input
              v-model="formData.realname"
              clearable
              placeholder="请输入审计人员"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="底稿创建时间"
            label-width="140px"
            prop="createTime"
          >
            <el-date-picker
              v-model="formData.createTime"
              placeholder="请输入底稿创建时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计目的" label-width="140px" prop="sheetTarget">
            <el-input
              v-model="formData.sheetTarget"
              clearable
              placeholder="请输入审计目的"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="是否发现问题"
            label-width="140px"
            prop="riskLevel"
          >
            <el-select
              v-model="formData.riskLevel"
              placeholder="是否发现问题"
              @change="risklevelChange"
              :disabled="!footer"
            >
              <el-option label="是" value="是" />
              <el-option label="否" value="否" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计分项" label-width="140px" prop="targetName">
            <el-input
              v-model="formData.targetName"
              clearable
              placeholder="请输入审计分项"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="业务单元"
            label-width="140px"
            prop="businessType"
          >
            <el-input
              v-model="formData.businessType"
              clearable
              placeholder="请输入业务单元"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24" v-if="formData.riskLevel == '是'">
          <el-form-item
            label="问题标题"
            label-width="140px"
            prop="problemTitle"
          >
            <el-input
              v-model="formData.problemTitle"
              clearable
              placeholder="请输入问题标题"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="审计程序"
            label-width="140px"
            prop="suditProcess "
          >
            <el-input
              v-model="formData.suditProcess"
              clearable
              placeholder="请输入审计程序"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="审计程序执行过程"
            label-width="140px"
            prop="auditDesc"
          >
            <el-input
              v-model="formData.auditDesc"
              clearable
              placeholder="请输入审计程序执行过程"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24" v-if="formData.risklevel == '是'">
          <el-form-item
            label="审计发现"
            label-width="140px"
            prop="auditDiscoverable "
          >
            <el-input
              v-model="formData.auditDiscoverable"
              clearable
              placeholder="请输入审计发现"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="审计意见及建议"
            label-width="140px"
            prop="auditCourse"
          >
            <el-input
              v-model="formData.auditCourse"
              clearable
              placeholder="请输入审计意见及建议"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="审计备忘录" label-width="140px" prop="sjbwl">
            <el-input
              v-model="formData.sjbwl"
              clearable
              placeholder="请输入审计备忘录"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-preview="handlePreview"
              :on-success="handleSuccess"
              :file-list="tableData"
              :before-upload="handleBeforeUpload"
            >
              <el-button type="success">上传</el-button>
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
                <el-button type="text" @click="handleDown(row)">下载</el-button>
                <el-button type="text" @click="handleDelete(row)" v-if="footer">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </div>
    <template #footer v-if="footer">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { download, myDraftSave, myDraftFileList } from '@/api/audit/implement'
  import store from '@/store'
  import { formatDate } from '@/utils/index'
  const { baseURL } = require('@/config')
  export default {
    name: 'MyDraftInfo',
    inheritAttrs: false,
    props: [],
    data() {
      return {
        baseApi: baseURL,
        api: '/audit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        formData: {
          sheetCode: undefined,
          sheetName: undefined,
          orgname: undefined,
          businessAffiliation: undefined,
          auditStaffId: undefined,
          sheetTarget: undefined,
          riskLevel: '否',
          targetName: undefined,
          businessType: undefined,
          suditProcess: undefined,
          auditDiscoverable: undefined,
          auditDesc: undefined,
          auditCourse: undefined,
          sjbwl: undefined,
        },
        footer: true,
        tableData: [],
        rules: {
          sheetId: [
            {
              required: true,
              message: '请输入底稿编号',
              trigger: 'blur',
            },
          ],
          sheetName: [
            {
              required: true,
              message: '请输入底稿名称',
              trigger: 'blur',
            },
          ],
          orgname: [
            {
              required: true,
              message: '请输入被审计单位',
              trigger: 'blur',
            },
          ],
          realname: [
            {
              required: true,
              message: '请输入审计人员',
              trigger: 'blur',
            },
          ],
          riskLevel: [
            {
              required: true,
              message: '是否发现问题',
              trigger: 'blur',
            },
          ],
          auditDesc: [
            {
              required: true,
              message: '请输入审计程序执行过程',
              trigger: 'blur',
            },
          ],
          auditDiscoverable: [],
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
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      risklevelChange(index) {
        if (index == '是') {
          this.rules.auditDiscoverable = [
            {
              required: true,
              message: '请输入审计发现',
              trigger: 'blur',
            },
          ]
        } else {
          this.rules.auditDiscoverable = []
        }
      },
      /**
       * @description: 打开表单
       * @param {*} row 传入数据
       * @return {*}
       */      
      showEdit(row) {
        this.dialogFormVisible = true
        if (row) {
          this.formData = row.project
          this.formData.realname = row.project.pmStaff.realname
          let list = []
          list.push(row.attachment)
          this.tableData = list
        }
      },
      // async getFileList(sheetId) {
      //   const data = await myDraftFileList({ sheetid: sheetId })
      //   this.tableData = data.data.data || []
      // },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.formData = {}
        this.dialogFormVisible = false
        this.tableData = []
        this.footer = true
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let attids = ''
            this.tableData.map((item) => {
              attids += item.attid
              attids += ','
            })
            attids = attids.substring(0, attids.length - 1)
            const { createTime, state, ...other } = this.formData
            const data = await myDraftSave({
              ...other,
              attids,
            })
            this.$emit('fetch-data')
            this.close()
          } else {
            return false
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
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */      
      handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        this.tableData = list
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
