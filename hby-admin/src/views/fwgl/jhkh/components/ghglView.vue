<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :append-to-body="true"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000"
      @close="close"
      v-if="dialogFormVisible"
    >
      <el-row :gutter="24">
        <el-form
          ref="ruleForm"
          label-width="100px"
          :model="formData"
          :rules="rules"
          size="mini"
        >
          <!-- <el-col :span="12">
            <el-form-item label="序号" prop="number">
              <el-input
                v-model="formData.number"
                clearable
                placeholder="请输入序号"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col> -->
          <el-col :span="12">
            <el-form-item label="规划类型" prop="type">
              <el-select
                :style="{ width: '100%' }"
                v-model="formData.type"
                placeholder="规划类型"
                :disabled="!footer"
              >
                <el-option label="法治建设规划" value="法治建设规划" />
                <el-option label="普法规划" value="普法规划" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="formData.type == '法治建设规划'">
            <el-form-item label="登记类型" prop="registerType">
              <el-select
                :style="{ width: '100%' }"
                v-model="formData.registerType"
                placeholder="登记类型"
                :disabled="!footer"
              >
                <el-option label="规划登记" value="规划登记" />
                <el-option label="中期调整" value="中期调整" />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12" v-if="formData.type == '法治建设规划'">
            <el-form-item label="规划名称" prop="planManagementName">
              <el-input
                v-model="formData.planManagementName"
                clearable
                placeholder="请输入规划名称"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="formData.type == '规划考核'">
            <el-form-item label="考核材料名称" prop="planManagementName">
              <el-input
                v-model="formData.planManagementName"
                clearable
                placeholder="请输入考核材料名称"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="创建人" prop="planManagementCreator">
              <el-input
                v-model="formData.planManagementCreator"
                clearable
                placeholder="请输入发布单位"
                :disabled="true"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="创建时间" prop="createdTime">
              <el-date-picker
                style="width: 100%"
                v-model="formData.createdTime"
                placeholder="选择创建时间"
                type="datetime"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd HH:mm:ss"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-divider>相关附件</el-divider>
          </el-col>
          <el-col :span="24">
            <div style="text-align: right; margin-bottom: 5px" v-if="footer">
              <el-upload
                class="upload-demo"
                :show-file-list="false"
                :action="baseApi + api"
                :headers="headers"
                :on-success="handleSuccess"
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
                  <el-button type="text" @click="handlePreviewFile(row)">
                    预览
                  </el-button>
                  <el-button type="text" @click="handleDown(row)">
                    下载
                  </el-button>
                  <el-button
                    type="text"
                    @click="handleDelete(row)"
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
        <el-button @click="add" type="primary">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script>
  import { addGHGL, deleteFile, getGHGLDefaultInfo } from '@/api/fwgl/jhkh'
  import { uploadApi, download } from '@/api/fwgl/zzxx'
  import { baseURL } from '@/config'
  import { getPrivewAttInfo } from '@/api/fwgl/gsls'
  import { formatDate } from '@/utils/index'
  import store from '@/store'
  const token = store.getters['user/token']

  export default {
    props: [],
    data() {
      return {
        dialogFormVisible: false,
        title: '新增',
        baseApi: baseURL,
        api: uploadApi,
        headers: { token: token },
        fileList: [],
        footer: true,
        formData: {
          createdTime: new Date().toString(),
          fileIds: '',
          planManagementCreator: '',
          planManagementId: '',
          planManagementName: '',
          registerType: '',
          type: '法治建设规划',
        },

        rules: {
          name: [
            {
              required: true,
              message: '请输入商标名称',
              trigger: 'blur',
            },
          ],
        },
      }
    },
    created() {},
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
          getGHGLDefaultInfo({
            id: row.planManagementId,
          }).then((res) => {
            this.formData = Object.assign({}, res.data.planManagement)
            this.fileList = res.data.files || []
          })
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else {
          this.title = '新增'
          const info = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.planManagementCreator = info.realname
          // this.formData.planManagementCreatedTime = formatDate(new Date());
        }
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.formData = {
          type: '法治建设规划',
        }
        this.dialogFormVisible = false

        this.footer = true
      },
      /**
       * @description: 保存表单
       * @return {*}
       */      
      add() {
        const ids = this.fileList.map((res) => res.fileId)
        const p = JSON.parse(JSON.stringify(this.formData))
        delete p.createdTime
        p.fileIds = ids.toString()
        addGHGL(p).then((res) => {
          if (res.msg == '成功') {
            this.$message.success('操作成功')
            this.dialogFormVisible = false
            this.$emit('fentch')
            this.close()
          }
        })
      },
      //文件上传
      handleSuccess(res) {
        if (res.code == 200) {
          this.fileList.push(res.data.fileIds[0])
        }
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */      
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await deleteFile({ id: row.fileId })
          if (code == 200) {
            this.fileList.splice(
              this.fileList.findIndex((x) => x.fileId == row.fileId),
              1
            )
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
