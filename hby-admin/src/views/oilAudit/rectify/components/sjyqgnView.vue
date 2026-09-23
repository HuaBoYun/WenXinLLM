<template>
  <el-dialog
    v-if="dialogFormVisible"
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
          <el-form-item label="文号" prop="projectCode">
            <el-input
              v-model="formData.projectCode"
              clearable
              placeholder="请输入文号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="标题" prop="projectId">
            <el-input
              v-model="formData.projectId"
              clearable
              placeholder="请输入标题"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人" prop="projectCode">
            <el-input
              v-model="formData.projectCode"
              clearable
              placeholder="请输入创建人"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间" prop="projectId">
            <el-date-picker
              v-model="formData.createTime"
              placeholder="请输入创建时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-success="handleSuccess"
              :file-list="fileList"
              :before-upload="handleBeforeUpload"
            >
              <el-button v-if="disabled" type="success">上传</el-button>
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
              <template #default="{ $index, row }">
                <el-button type="text" @click="handleDown(row)">下载</el-button>
                <el-button
                  type="text"
                  @click="handleDeleteFile($index, row)"
                  v-if="disabled"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>

    <template #footer v-if="disabled">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { download, getDefaultFIleData } from '@/oapi/audit/implement'
  import {
    auditSuggestAdd,
    createSuggestCode,
    deleteSuggestFile,
  } from '@/oapi/audit/report'
  import Tinymce from '@/components/Tinymce'
  import UEditor from '@/components/UEditor'
  import store from '@/store'
  const { baseURL } = require('@/config')
  export default {
    name: 'ProjectDataInfo',
    components: { Tinymce, UEditor },
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
          procode: undefined,
          proname: undefined,
        },
        fileList: [],
        tableDataFile: [],
        fileIds: [],
        disabled: true,
        rules: {},
        dialogFormVisible: false,
        title: '新增',
      }
    },
    methods: {
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      async showEdit(title, row) {
        this.dialogFormVisible = true
        this.fileIds = []
        this.tableDataFile = []
        if (row) {
          this.formData = row.proposal
          let res1 = await getDefaultFIleData({
            proid: row.proposal.proid,
          })
          //回填上传文件表格
          const arr = res1.data.data
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
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.disabled = false
        } else {
          this.title = '新增'
          this.disabled = true
          createSuggestCode().then((res) => {
            this.$set(this.formData, 'procode', res.data.autoCode.toString())
          })
        }
      },
      close() {
        this.formData = {}
        this.dialogFormVisible = false
        this.disabled = true
        this.tableData = []
        this.fileIds = []
        this.tableDataFile = []
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const data = await auditSuggestAdd({
              attids: this.fileIds.toString() || '',
              content: this.formData.content,
              procode: this.formData.procode,
              proid: this.formData.proid,
              proname: this.formData.proname,
            })
            if (data.code == 1) {
              this.$baseMessage('保存成功', 'success')
            } else {
              this.$baseMessage(data.msg, 'error')
            }
            this.$emit('fetch-data')
            this.close()
          } else {
            return false
          }
        })
      },
      async handleDown(row) {
        const data = await download({ attId: row.attid })
        let filename = row.name
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
      handleDeleteFile(index, row) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          //删除对应的id
          let res = await deleteSuggestFile({ attId: row.attid })
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
