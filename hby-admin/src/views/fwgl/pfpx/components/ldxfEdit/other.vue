<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :append-to-body="true"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
    >
      <el-row :gutter="24">
        <el-form
          ref="ruleForm"
          label-width="100px"
          :model="formData"
          :rules="rules"
          size="mini"
          :disabled="allDisabled"
        >
          <el-col :span="12">
            <el-form-item label="活动主题" prop="otherTopic">
              <el-input
                v-model="formData.otherTopic"
                clearable
                placeholder="请输入活动主题"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="活动时间" prop="otherTime">
              <el-date-picker
                style="width: 100%"
                v-model="formData.otherTime"
                placeholder="选择活动时间"
                type="datetime"
                format="yyyy-MM-dd HH:mm:ss"
                value-format="yyyy-MM-dd HH:mm:ss"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="活动内容" prop="otherContent">
              <el-input
                type="textarea"
                v-model="formData.otherContent"
                clearable
                placeholder="请输入活动内容"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="活动对象" prop="otherParticipant">
              <el-input
                type="textarea"
                v-model="formData.otherParticipant"
                clearable
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="新闻链接" prop="otherNewslink">
              <el-input
                placeholder="请输入新闻链接"
                v-model="formData.otherNewslink"
                :style="{ width: '100%' }"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="创建人" prop="activityManagementCreator">
              <el-input
                v-model="formData.activityManagementCreator"
                clearable
                placeholder="请选择创建人"
                :style="{ width: '100%' }"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="创建时间" prop="activityManagementCreatedTime">
              <el-date-picker
                :style="{ width: '100%' }"
                v-model="formData.activityManagementCreatedTime"
                placeholder="创建时间"
                type="date"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd HH:mm:ss"
              />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-divider>上传附件</el-divider>
          </el-col>
          <el-col :span="24">
            <div style="text-align: right; margin-bottom: 5px">
              <el-upload
                class="upload-demo"
                :show-file-list="false"
                :action="baseApi + api"
                :headers="headers"
                :on-success="handleAvatarSuccess"
              >
                <el-button type="success">上传</el-button>
              </el-upload>
            </div>
            <el-table :data="fileList">
              <el-table-column
                align="center"
                label="附件名称"
                prop="fileName"
              />
              <el-table-column
                align="center"
                label="文件大小(KB)"
                prop="fileSize"
              />
              <el-table-column align="center" label="创建人" prop="uploader" />
              <el-table-column
                align="center"
                label="操作"
                show-overflow-tooltip
                width="120"
              >
                <template #default="{ row }">
                  <el-button
                    type="text"
                    @click="handlePreviewFile(row)"
                    :disabled="false"
                  >
                    预览
                  </el-button>
                  <el-button
                    type="text"
                    @click="handleDown(row)"
                    :disabled="false"
                  >
                    下载
                  </el-button>
                  <el-button
                    type="text"
                    @click="handleDelete(row, 'fileList')"
                    v-if="footer"
                  >
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-form>
      </el-row>
      <template #footer>
        <el-button @click="close">关 闭</el-button>
        <el-button @click="add" type="primary" v-if="!allDisabled">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script>
  import store from '@/store'
  import CompanySelectModal from '@/components/CampanySelectModal'
  import { uploadApi, deleteFile, download } from '@/api/fwgl/zzxx'
  import { baseURL } from '@/config'
  import { updateZXZXXData, getLDXFDefaultInfo } from '@/api/fwgl/pfpx'
  const token = store.getters['user/token']

  import { formatDate } from '@/utils'
  import { getPrivewAttInfo } from '@/api/fwgl/gsls'
  export default {
    props: [],
    components: {
      CompanySelectModal,
    },
    data() {
      return {
        dialogFormVisible: false,
        formData: {
          otherContent: '',
          otherNewslink: '',
          otherParticipant: '',
          otherTime: '',
          otherTopic: '',
          leaderType: '4',
        },
        footer: true,
        allDisabled: true,
        title: '新增',
        tableData: [],
        sitePhotoList: [],
        // baseApi: 'http://cn-gz-txy.starryfrp.example:28691',
        baseApi: baseURL,
        api: uploadApi,
        fileList: [],
        fileList1: [],
        headers: { token: token },
        rules: {
          otherTopic: [
            { required: true, message: '请输入活动主题', trigger: 'blur' },
          ],
          otherTime: [
            { required: true, message: '请选择活动时间', trigger: 'blur' },
          ],
        },
      }
    },

    methods: {
      /**
       * @description: 外部打开dialog
       * @param {*} title 表单类型
       * @param {*} row 行数据
       * @return {*}
       */      
      showEdit(title, row) {
        this.dialogFormVisible = true
        this.fileList = []
        if (row) {
          getLDXFDefaultInfo({
            id: row.id,
          }).then((res) => {
            this.formData = Object.assign({}, res.data.leaderXf)
            this.formData.activityManagementCreator =
              res.data.leaderXf.creatorName
            this.formData.activityManagementCreatedTime = formatDate(
              res.data.leaderXf.createdTime
            )
            if (res.data.files) {
              this.fileList = res.data.files
            }
          })
        }

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
          this.allDisabled = true
        } else {
          this.title = '新增'
          this.formData = this.$options.data().formData

          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.activityManagementCreator = userInfo.realname
          this.formData.workUnitName = userInfo.linkOrg.orgname
          this.formData.workUnitId = userInfo.linkOrg.orgid
          this.formData.activityManagementCreatedTime = formatDate(new Date())
        }
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.dialogFormVisible = false
        this.footer = true
        this.allDisabled = false
      },
      /**
       * @description: 保存表单
       * @return {*}
       */      
      add() {
        const aa = []
        this.fileList.forEach((e) => {
          aa.push(e.fileId)
        })
        this.formData.fileIds = aa.toString()
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            updateZXZXXData(this.formData).then((res) => {
              if (res.msg == '成功') {
                this.$baseMessage(res.msg, 'success', 'vab-hey-message-success')
                this.dialogFormVisible = false
                this.$emit('fetch-data')
              }
            })
          }
        })
      },
      handleAvatarSuccess(res) {
        if (res.code == 200) {
          this.$baseMessage('上传成功', 'success', 'vab-hey-message-success')
          this.fileList.push(res.data.fileIds[0])
        }
      },

      handleDelete(row, key) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await deleteFile({ id: row.fileId })
          if (code == 200) {
            const _key = key || 'fileList'
            const list = this[_key]

            list.splice(
              this.fileList.findIndex((x) => x.fileId == row.fileId),
              1
            )

            this.$set(this, key, list)
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          }
        })
      },

      /**
       * @description: 下载文件
       * @param {*} row
       * @return {*}
       */      
      async handleDown(row) {
        const data = await download({ fileId: row.fileId })
        let filename = row.fileName
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
       * @description: 文件预览
       * @param {*} row 文件信息
       * @return {*}
       */      
      async handlePreviewFile(row) {
        const { data } = await getPrivewAttInfo({
          fileId: row.fileId,
          attType: 2,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url }) // iframe弹框预览形式
      },
    },
  }
</script>

<style scoped>
  .avatar-uploader .el-upload {
    width: 178px;
    height: 178px;
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409eff;
  }
  .avatar-uploader-icon {
    border: 1px dashed #d9d9d9;
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>
