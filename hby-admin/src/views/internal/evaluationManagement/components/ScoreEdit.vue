<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="700px"
    @close="close"
  >
    <el-form ref="form" label-width="120px" :model="form" :rules="rules">
      <el-form-item label="评价分" prop="score">
        <el-input v-model.trim="form.score" placeholder="请输入评价分" />
      </el-form-item>
      <el-col :span="24">
        <el-form-item label="评价依据" prop="reason">
          <el-input
            v-model="form.reason"
            clearable
            placeholder="请输入评价依据"
            :style="{ width: '100%' }"
            type="textarea"
          />
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <div style="text-align: right; margin-bottom: 5px">
          <el-upload
            class="upload-demo"
            :show-file-list="false"
            :action="baseApi + api"
            :headers="headers"
            :on-success="handleSuccess"
            :file-list="tableData"
          >
            <el-button v-if="tableData.length < 1" type="success">
              上传
            </el-button>
          </el-upload>
        </div>
        <el-table :data="tableData">
          <el-table-column align="center" label="附件名称" prop="attname" />
          <el-table-column align="center" label="文件大小(KB)" prop="attsize" />
          <el-table-column align="center" label="创建人" prop="uploader" />
          <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="120"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleDown(row)">下载</el-button>
              <el-button type="text" @click="handlePreviewFile(row)">
                预览
              </el-button>
              <el-button type="text" @click="handleDelete(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-form>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  // import { doEdit } from '@/api/table'
  import { download, deleteFile, projCatList1Save } from '@/api/internal/score'
  import store from '@/store'
  const { baseURL } = require('@/config')
  import { getPrivewAttInfo } from '@/api/fwgl/gsls'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    name: 'ScoreEdit',
    data() {
      return {
        form: {
          examination: '',
          score: '',
        },
        tableData: [],
        rules: {
          reason: [
            { required: true, trigger: 'blur', message: '请输入评价依据' },
          ],
          score: [{ required: true, trigger: 'blur', message: '请输入评价分' }],
        },
        title: '',
        dialogFormVisible: false,
        baseApi: baseURL,
        api: '',
        headers: {
          token: store.getters['user/token'],
        },
        assStaffId: '',
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
      }
    },
    created() {},
    methods: {
      showEdit(row) {
        this.form = row
        this.api = '/nkhg/nbkz/pjgl/pjpf_upload?asseleid=' + row.assStaffId
        this.assStaffId = row.assStaffId
        this.dialogFormVisible = true
        if (row.attid) {
          this.tableData.push({
            attid: row.attid,
            attname: row.attname,
            attsize: row.attsize,
            uploader: row.uploader,
          })
        }
      },
      close() {
        this.tableData = []
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      async handleDown(row) {
        // const data = await download({ attId: row.attid })
        // let filename = row.attname
        // let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
        // let url = window.URL.createObjectURL(blob, {})
        // const link = document.createElement('a')
        // link.style.display = 'none'
        // link.href = url
        // link.setAttribute('download', filename)
        // document.documentElement.appendChild(link)
        // link.click()
        // document.documentElement.removeChild(link)
        await handleDown(row, this.headers, this.lodeapi)
      },
      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        this.tableData = list
        if (this.assStaffId) {
          await deleteFile({ attid: row.attid, asseleid: this.assStaffId })
          this.$message.success('删除成功')
          this.$emit('fetch-data')
        }
      },
      handleSuccess(file) {
        // console.log(file)
        if (file.code == '200') {
          // let list = this.tableData
          // list.push(file.data.Attachment)
          this.tableData = [...this.tableData, file.data.res]
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },

      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            // const { msg } = await doEdit(this.form)
            const { score, reason, assStaffId } = this.form
            let elements =
              assStaffId + '@' + score + '@' + (reason ? reason.trim() : '')
            const { msg, code } = await projCatList1Save({ elements })
            if (code === 1) {
              this.$baseMessage(
                '保存成功',
                'success',
                'vab-hey-message-success'
              )
              this.$emit('fetch-data')
              this.close()
            }
          }
        })
      },
      /**
       * @description: 文件预览
       * @param {*} row 文件信息
       * @return {*}
       */ async handlePreviewFile(row) {
        // if (row.isEncrypted === '1') {
        //   // 当文件是加密状态时，使用指定的在线预览链接
        //   const previewUrl = row.previewUrl
        //   window.open(previewUrl, '_blank')
        // } else {
        //   this.$iFrameDialog({ attid: row.attid })
        // }
        this.$iFrameDialog({ attid: row.attid })
        // const { data } = await getPrivewAttInfo({
        //   attId: row.attid,
        //   attType: 2,
        // })

        // const url =
        //   data.previewurl +
        //   '?url=' +
        //   encodeURIComponent(Base64.encode(data.ftpUrl))
        // this.$iFrameDialog({ iframeUrl: url }) // iframe弹框预览形式
      },
    },
  }
</script>
