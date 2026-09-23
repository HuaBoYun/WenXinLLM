<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="dialogVisible"
      width="1000px"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      @close="close"
    >
      <el-row :gutter="24">
        <el-form
          ref="ruleForm"
          label-width="100px"
          :model="formData"
          :rules="rules"
          size="mini"
        >
          <el-col :span="12">
            <el-form-item label="类别" prop="type">
              <el-select
                v-model="formData.type"
                disabled
                placeholder="请选择"
                style="width: 100%"
              >
                <el-option label="商标" :value="1"></el-option>
                <el-option label="版权" :value="2"></el-option>
                <el-option label="专利" :value="3"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="formData.type == '1'">
            <el-form-item label="商标名称" prop="registerName">
              <el-input
                v-model="formData.registerName"
                clearable
                :disabled="!footer"
                placeholder="请输入商标名称"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="formData.type == '1'">
            <el-form-item label="商标类别" prop="category">
              <el-input
                v-model="formData.category"
                clearable
                :disabled="!footer"
                placeholder="请输入商标类别"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="formData.type == '2'">
            <el-form-item label="版权名称" prop="registerName">
              <el-input
                v-model="formData.registerName"
                clearable
                :disabled="!footer"
                placeholder="请输入版权名称"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="formData.type == '2'">
            <el-form-item label="版权类型" prop="category">
              <el-input
                v-model="formData.category"
                clearable
                :disabled="!footer"
                placeholder="请输入版权类型"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="formData.type == '3'">
            <el-form-item label="专利名称" prop="registerName">
              <el-input
                v-model="formData.registerName"
                clearable
                :disabled="!footer"
                placeholder="请输入专利名称"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="formData.type == '3'">
            <el-form-item label="专利类型" prop="category">
              <el-input
                v-model="formData.category"
                clearable
                :disabled="!footer"
                placeholder="请输入专利类型"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="注册时间" prop="registerTime">
              <el-date-picker
                style="width: 100%"
                v-model="formData.registerTime"
                :disabled="!footer"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="选择注册时间"
                type="date"
              />
            </el-form-item>
          </el-col>

          <el-col :span="24" v-if="formData.type == '1'">
            <el-divider>商标注册证书</el-divider>
          </el-col>
          <el-col :span="24" v-if="formData.type == '2'">
            <el-divider>版权证书</el-divider>
          </el-col>
          <el-col :span="24" v-if="formData.type == '3'">
            <el-divider>专利证书</el-divider>
          </el-col>
          <el-col :span="24">
            <div style="text-align: right; margin-bottom: 5px" v-if="footer">
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
        <el-button @click="close" type="primary">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script>
  import { addDJGL, getDJGLDefaultInfo } from '@/api/fwgl/zscq'
  import store from '@/store'
  import { getPrivewAttInfo } from '@/api/fwgl/gsls'
  import { uploadApi, deleteFile, download } from '@/api/fwgl/zzxx'
  import { baseURL } from '@/config'
  const token = store.getters['user/token']
  export default {
    data() {
      return {
        dialogVisible: false,
        formData: {
          registerName: '',
          category: '',
          registerTime: '',
          type: '',
        },
        rules: {
          registerName: [
            {
              required: true,
              message: '请输入名称',
              trigger: 'blur',
            },
          ],
          category: [
            {
              required: true,
              message: '请输入类别',
              trigger: 'blur',
            },
          ],
          registerTime: [
            {
              required: true,
              message: '请选择注册日期',
              trigger: 'blur',
            },
          ],
        },
        title: '详情',
        footer: true,
        tableData: [],
        fileList: [],
        baseApi: baseURL,
        api: uploadApi,
        headers: { token: token },
      }
    },
    methods: {
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
      showEdit(title, row, type) {
        this.dialogVisible = true
        this.formData = {
          registerName: '',
          category: '',
          registerTime: '',
          type: '',
        }
        this.fileList = []
        if (row) {
          getDJGLDefaultInfo({
            id: row.registerManagementId,
          }).then((res) => {
            this.formData = Object.assign({}, res.data.registerManagement)
            if (res.data.files) {
              this.fileList = res.data.files
            }
            this.formData.registerName =
              res.data.registerManagement.registerName
            this.formData.category = res.data.registerManagement.category
            this.formData.registerTime =
              res.data.registerManagement.registerTime
            this.formData.type = res.data.registerManagement.type
          })
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else {
          this.title = '新增'
          this.formData = this.$options.data().formData
        }

        this.formData.type = type
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
            addDJGL(this.formData).then((res) => {
              if (res.msg == '成功') {
                this.dialogVisible = false
                this.$message.success('操作成功')
                this.$emit('fetchData')
              }
            })
          } else {
            return false
          }
        })
        // this.$emit('add', this.formData)
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        // this.footer = true
        this.dialogVisible = false
      },
      handleAvatarSuccess(res) {
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
    },
  }
</script>
