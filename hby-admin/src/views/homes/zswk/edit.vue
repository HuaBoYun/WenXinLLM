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
          <el-form-item label="文档名称" label-width="140px" prop="wknane">
            <el-input
              v-model="formData.wknane"
              clearable
              placeholder="请输入文档名称"
              :style="{ width: '256px' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="文档类型" label-width="140px" prop="fgcategory">
            <el-input
              v-model="formData.fgcategory"
              clearable
              placeholder="请输入文档类型"
              :style="{ width: '256px' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="文档封面" label-width="140px">
            <SelectIcon
              :icon="formData.fgimage"
              @getSelectIcon="getSelectIcon"
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
              :on-success="handleSuccess"
              :file-list="fileList"
            >
              <el-button type="success">上传</el-button>
            </el-upload>
          </div>
          <el-table :data="tableDataFile">
            <el-table-column align="center" label="附件名称" prop="name" />
            <el-table-column align="center" label="文件大小(KB)" prop="size" />
            <el-table-column
              align="center"
              label="创建人"
              prop="createPerson"
            />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template slot-scope="scope">
                <el-button
                  :disabled="false"
                  type="text"
                  @click="handleDownload(scope.row)"
                >
                  下载
                </el-button>
                <el-button
                  v-if="footer"
                  type="text"
                  @click="handleDeleteFile(scope.$index, scope.row)"
                >
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
  </el-dialog>
</template>

<script>
  import { download } from '@/api/audit/implement'
  import { addWK, deleteWKFile } from '@/api/setting/auth'
  import SelectIcon from './selectIcon.vue'

  import store from '@/store'
  import { formatDay } from '@/utils/index'
  const { baseURL } = require('@/config')
  export default {
    name: 'ProjectDataInfo',
    components: { SelectIcon },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        baseApi: baseURL,
        api: '/setting/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        formData: {
          wknane: '',
          fgcategory: '',
          fgimage: '',
        },
        templates: [],
        footer: true,
        rules: {
          reportname: [
            {
              required: true,
              message: '请输入报告名称',
              trigger: 'blur',
            },
          ],
          reporttime: [
            {
              required: true,
              message: '请输入报告时间',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        disabled: false,
        fileList: [],
        tableDataFile: [],
        fileIds: [],
      }
    },
    methods: {
      showEdit() {
        this.dialogFormVisible = true
      },
      async getFileList(reportid) {
        const res = await reportFileList({ reportid })
        //回填上传文件表格
        const arr = res.data.data
        const arr1 = arr.map((item) => {
          return {
            ...item,
            name: item.attname,
            size: item.attsize,
            createPerson: item.uploader,
          }
        })
        const arr2 = arr.map((res) => {
          return res.attid
        })
        //收集id
        this.fileIds = arr2
        this.tableDataFile = arr1
      },

      close() {
        this.formData = {}
        this.dialogFormVisible = false
        this.tableData = []
        this.footer = true
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const { ...other } = this.formData

            const data = await addWK({
              ...other,
              attids: this.fileIds.toString() || '',
            })
            if (data.code == 1) {
              this.$baseMessage('保存成功', 'success')
              this.close()
              this.$emit('fetchData')
            } else {
              this.$baseMessage(data.msg, 'error')
            }
          } else {
            return false
          }
        })
      },

      async handleDownload(row) {
        const res = await download({ attId: row.attid })
        this.downloadFileByBlob(res, row.name)
      },
      downloadFileByBlob(blob, fileName = 'file') {
        let blobUrl = window.URL.createObjectURL(blob)
        let link = document.createElement('a')
        link.download = fileName || 'defaultName'
        link.style.display = 'none'
        link.href = blobUrl
        // 触发点击
        document.body.appendChild(link)
        link.click()
        // 移除
        document.body.removeChild(link)
      },

      handleSuccess(response, file, fileList) {
        console.log(file, 'file')
        console.log(fileList, 'fileList')
        if (file.response.code == '200') {
          file.createPerson = JSON.parse(
            localStorage.getItem('userInfo')
          ).realname
          let arr = file.response.data.fileIds[0]
          const arr1 = {
            name: arr.fileName,
            size: arr.fileSize,
            createPerson: arr.uploader,
            attid: arr.fileId,
          }
          this.tableDataFile.push(arr1)

          this.fileList = fileList
          let fileArr = []
          this.fileList.forEach((item) => {
            fileArr.push(item.response.data.fileIds[0].fileId)
          })
          this.fileIds = [...this.fileIds, ...fileArr]
          this.$baseMessage(file.response.msg, 'success')
        } else {
          this.$baseMessage(file.response.msg, 'error')
        }
      },
      handleDeleteFile(index, row) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          //删除对应的id
          let res = await deleteWKFile({ attId: row.attid })
          if (res.msg === '成功') {
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
            this.fileIds.splice(index, 1)
            this.tableDataFile.splice(index, 1)
          } else {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          }
        })
      },
      getSelectIcon(data) {
        this.formData.fgimage = data.id
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
