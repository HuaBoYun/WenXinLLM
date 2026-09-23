<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    v-if="dialogFormVisible"
  >
    <el-row :gutter="14" v-loading="loading">
      <el-form
        ref="ruleForm"
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="!footer"
      >
        <el-col :span="12">
          <el-form-item label="编号" prop="noticecode">
            <el-input
              v-model="formData.noticecode"
              disabled
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="名称 " prop="noticename">
            <el-input
              v-model="formData.noticename"
              clearable
              placeholder="请输入名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人名称" prop="createname">
            <el-input
              v-model="formData.createname"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间" prop=" createtime">
            <el-input
              v-model="formData.createtime"
              :style="{ width: '100%' }"
              disabled
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
              :action="baseURL + uploadApi"
              :headers="headers"
              :on-preview="handlePreview"
              :on-success="handleSuccess"
              :file-list="tableData"
            >
              <el-button type="success">上传</el-button>
            </el-upload>
          </div>
          <el-table :data="tableData">
            <el-table-column align="center" label="文件名称" prop="attname" />
            <el-table-column align="center" label="文件大小(KB)" prop="attsize">
              <template #default="{ row }">
                <div>
                  {{ row.attsize }}
                </div>
              </template>
            </el-table-column>
            <el-table-column align="center" label="创建人" prop="uploader" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="200"
            >
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="handlePreview(row)"
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
      <el-button @click="save" type="primary">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import { baseURL } from '@/config'
  import store from '@/store'

  import {
    getTZGGCode,
    editTZGG,
    getTZGGDefaultData,
    deleteTZGGFile,
    getTZGGDefaultFileData,
  } from '@/api/systemLog'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import { download } from '@/api/audit/implement'
  const token = store.getters['user/token']

  import { formatDate } from '@/utils/index'

  export default {
    components: {},
    inheritAttrs: false,
    props: ['fetchData'],
    data() {
      return {
        loading: false,
        baseURL: baseURL,
        uploadApi: '/fwgl/api-auth/fileManage/upload',
        headers: { token: token },
        tableData: [],
        formData: {
          attids: '',
          createname: '',
          createstaff: '',
          noticecode: '',
          noticename: '',
          noticeid: '',
          createtime: '',
        },
        footer: true,
        rules: {
          noticename: [
            {
              required: true,
              message: '请输入名称',
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
      handleSuccess(res, b, c) {
        if (res && res.code === 200 && res.data.fileIds && res.data.fileIds.length > 0) {
          const fileList = res.data.fileIds.map(item => ({
            attid: item.fileId,
            attname: item.fileName,
            attsize: item.fileSize,
            uploader: item.uploader,
            attpath: item.filePath,
            uploadtime: item.uploadTime,
            ispythonflag: item.isPythonFlag
          }))
          this.tableData = this.tableData.concat(fileList)
        }
      },
      /**
       * @description: 下载文件
       * @param {*} row
       * @return {*}
       */      
      async handleDown(row) {
        const data = await download({ attId: row.attid })
        let filename = row.attname
        let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
        let url = window.URL.createObjectURL(blob, {})
        const link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', filename)
        document.documentElement.appendChild(link)
        link.click()
        document.documentElement.removeChild(link)
      },
      async handleDelete(row) {
        const { attid } = row
        const res = await deleteTZGGFile({ attid: row.attid })
        const i = this.tableData.findIndex((x) => x.attid === attid)
        if (i > -1) {
          this.tableData.splice(i, 1)
        }
      },
      async showEdit(row, title) {
        this.dialogFormVisible = true
        if (row) {
          const res = await getTZGGDefaultData({ noticeid: row.noticeid })

          this.formData.createname = res.data.data.createname
          this.formData.createstaff = res.data.data.createstaff
          this.formData.noticecode = res.data.data.noticecode
          this.formData.noticename = res.data.data.noticename
          this.formData.noticeid = res.data.data.noticeid
          this.formData.createtime = res.data.data.createtime

          const file = await getTZGGDefaultFileData({ noticeid: row.noticeid })
          if (file.data.data && file.data.data.length > 0) {
            this.tableData = file.data.data.map(item => ({
              attid: item.fileId,
              attname: item.fileName,
              attsize: item.fileSize,
              uploader: item.uploader,
              attpath: item.filePath,
              uploadtime: item.uploadTime,
              ispythonflag: item.isPythonFlag
            }))
          } else {
            this.tableData = []
          }
        }

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else if (title == 'add') {
          this.title = '新增'
          //自动编码
          const name = await getTZGGCode()
          this.formData.noticecode = name.data.data
          const date = new Date()
          this.formData.createtime = formatDate(date)
          this.formData.createname = JSON.parse(
            localStorage.getItem('userInfo')
          ).realname
          this.formData.createstaff = JSON.parse(
            localStorage.getItem('userInfo')
          ).staffid
        }
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        ;(this.formData = {
          attids: '',
          createname: '',
          createstaff: '',
          noticecode: '',
          noticename: '',
          noticeid: '',
          createtime: '',
        }),
          (this.tableData = [])
        this.dialogFormVisible = false
        this.footer = true
      },
      /**
       * @description: 保存表单
       * @return {*}
       */      
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.formData.attids = this.tableData.map((x) => x.attid).join(',')
            const params = JSON.parse(JSON.stringify(this.formData))
            const res = await editTZGG(params)
            if (res && res.code == 1) {
              this.close()
              this.$emit('fetchData')
              this.$message({
                message: '提交成功！',
                type: 'success',
              })
            } else {
              this.$message({
                message: '提交失败',
                type: 'error',
              })
            }
          }
        })
      },
      async handlePreview(row) {
        const { data } = await getPrivewAttInfo({
          attId: row.attid,
          attType: 2,
        })

const url = data.previewurl + '?url=' + encodeURIComponent(Base64.encode(data.ftpUrl))
this.$iFrameDialog({ iframeUrl: url }) // iframe弹框预览形式
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
