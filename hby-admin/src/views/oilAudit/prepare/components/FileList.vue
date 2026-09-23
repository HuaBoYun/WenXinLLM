<template>
  <div>
    <el-dialog
      :append-to-body="true"
      title="附件列表"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
      :close-on-click-modal="false"
      v-if="dialogFormVisible"
    >
      <el-button
        type="success"
        @click="handleTB"
        style="position: absolute; top: 84px; right: 10%; z-index: 999"
      >
        同步项目资料附件
      </el-button>
      <el-row :gutter="14">
        <el-col :span="24">
          <el-upload
            style="text-align: right; margin-bottom: 5px"
            class="upload-demo"
            :action="baseApi + api"
            :headers="headers"
            :on-success="handleSuccess"
            :show-file-list="false"
            multiple
            :file-list="fileList"
            :data="fileParams"
            :before-upload="handleBeforeUpload"
          >
            <div v-if="!disabled" style="margin-right: 10px">
              <el-button type="success">上传</el-button>
            </div>
          </el-upload>

          <div>
            <el-button
              type="success"
              @click="handlePerson"
              :disabled="select.length === 0"
            >
              下发
            </el-button>
          </div>

          <el-table
            :data="tableDataFile"
            ref="multipleTable"
            :row-key="getRowKeys"
            @selection-change="handleSelectionChange"
          >
            <el-table-column
              width="48"
              type="selection"
              :reserve-selection="true"
            ></el-table-column>
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
                  @click="handleDown(scope.row)"
                >
                  下载
                </el-button>
                <el-button
                  type="text"
                  @click="handlePreview(scope.row)"
                  :disabled="false"
                >
                  预览
                </el-button>
                <el-button
                  v-if="!disabled"
                  type="text"
                  @click="handleDeleteFile(scope.$index, scope.row)"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>
      <FileSelectPerson ref="select" />
    </el-dialog>
  </div>
</template>
<script>
  import { download } from '@/oapi/audit/implement'
  import {
    dataprojectFileList,
    deleteProjectFile,
    tbdata,
  } from '@/oapi/audit/preparation'
  import store from '@/store'
  import FileSelectPerson from './fileSelectPerson.vue'
  const { baseURL } = require('@/config')
  import { getPrivewAttInfo } from '@/oapi/contract/manage'
  export default {
    name: 'xxxxx',
    components: { FileSelectPerson },
    data() {
      return {
        dialogFormVisible: false,
        tableDataFile: [],
        fileIds: [],
        baseApi: baseURL,
        api: '/audit/auditReady/sjzl/dataproject_file_upload',
        headers: {
          token: store.getters['user/token'],
        },
        fileParams: {},
        select: [],
      }
    },
    methods: {
      async showEdit(row) {
        this.dialogFormVisible = true
        this.fileParams = { dataId: row.id }

        //获取附件列表接口
        let resF = await dataprojectFileList({
          dataId: row.id,
        })
        //回填上传文件表格
        const arr = resF.data.data
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
        this.dialogFormVisible = false
        this.select = []
      },
      handleDeleteFile(index, row) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          let res = await deleteProjectFile({ attId: row.attid })
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
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
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
      handleSuccess(response, file, fileList) {
        if (file.response.result == '200') {
          file.createPerson = JSON.parse(
            localStorage.getItem('userInfo')
          ).realname
          let arr = file.response.data
          const arr1 = {
            name: arr.attname,
            size: arr.attsize,
            createPerson: arr.uploader,
            attid: arr.attid,
          }
          this.tableDataFile.push(arr1)

          this.fileList = fileList
          let fileArr = []
          this.fileList.forEach((item) => {
            fileArr.push(item.response.data.attid)
          })
          this.fileIds = [...this.fileIds, ...fileArr]
          this.$baseMessage(file.response.msg, 'success')
        } else {
          this.$baseMessage(file.response.msg, 'error')
        }
      },
      handlePerson() {
        this.$refs['select'].showEdit(this.select)
      },
      // 2、设置row-key
      getRowKeys(row) {
        return row.attid
      },
      // 3、勾选列表操作
      handleSelectionChange(selection) {
        // selection.shift()
        this.select = selection.map((item) => item.attid)
      },
      // 4、回显已勾选的数据
      setCheckedRows() {
        let selectItem = []
        this.tableDataFile.forEach((item) => {
          this.select.forEach((id) => {
            if (item.attid === id) {
              selectItem.push(item)
            }
          })
        })
        this.$refs.multipleTable.toggleRowSelection(selectItem)
      },
      async handlePreview(row) {
        const { data } = await getPrivewAttInfo({
          attId: row.attid,
          attType: 2,
        })

        const url = data.previewurl + '?url=' + encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url })

        
      },
      handleTB() {
        tbdata({
          dataId: this.fileParams.dataId,
        }).then(async (res) => {
          if (res.msg === '成功') {
            //获取附件列表接口
            let resF = await dataprojectFileList({
              dataId: this.fileParams.dataId,
            })
            //回填上传文件表格
            const arr = resF.data.data
            const arr1 = arr.map((item) => {
              return {
                ...item,
                name: item.attname,
                size: item.attsize,
                createPerson: item.uploader,
              }
            })
            this.tableDataFile = arr1
          }
        })
      },
    },
  }
</script>
