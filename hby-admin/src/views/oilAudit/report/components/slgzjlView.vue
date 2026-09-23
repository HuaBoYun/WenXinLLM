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
        label-width="135px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="!footer"
      >
        <el-col :span="12">
          <el-form-item label="审计项目名称" prop="projectName">
            <el-input
              v-model="formData.projectName"
              clearable
              placeholder="请选择审计项目名称"
              style="width: 266px; height: 30px"
              disabled
            />
            <el-button
              @click="$refs.project.show()"
              style="margin-left: 10px; height: 30px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="审计组长" prop="sjzz">
            <el-input
              v-model="formData.sjzz"
              clearable
              placeholder="请选择审计组长"
              style="width: 266px; height: 30px"
              disabled
            />
            <el-button
              @click="projectManager('sjzz')"
              style="margin-left: 10px; height: 30px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
          <!-- <el-form-item label="审计组长" prop="timer">
            <el-input
              v-model="formData.code"
              :style="{ width: '75%' }"
              clearable
              placeholder="请选择审计组长"
            />
            <el-button type="primary" style="margin-left: 20px">选择</el-button>
          </el-form-item> -->
        </el-col>
        <el-col :span="12">
          <el-form-item label="主审" prop="zs">
            <el-input
              v-model="formData.zs"
              clearable
              placeholder="请选择主审"
              style="width: 266px; height: 30px"
              disabled
            />
            <el-button
              @click="projectManager('zs')"
              style="margin-left: 10px; height: 30px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="实施审理时间" prop="ssslsj">
            <el-date-picker
              v-model="formData.ssslsj"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="选择申请时间"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="审理关注的要点" prop="slgzyd">
            <el-input
              v-model="formData.slgzyd"
              :style="{ width: '100%' }"
              clearable
              :rows="6"
              placeholder="请输入审理关注的要点"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="审理发现问题摘要" prop="slfawt">
            <el-input
              v-model="formData.slfawt"
              :style="{ width: '100%' }"
              clearable
              :rows="6"
              placeholder="请输入审理发现问题摘要"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="创建人" prop="createUser">
            <el-input
              v-model="formData.createUser"
              clearable
              placeholder="请输创建人"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间" label-width="140px" prop="createTime">
            <el-date-picker
              v-model="formData.createTime"
              placeholder="选择创建时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col> -->
      </el-form>
      <el-col :span="24">
        <el-divider>文件上传</el-divider>
      </el-col>
      <el-col :span="24">
        <div style="text-align: right; margin-bottom: 5px" v-if="footer">
          <!-- <el-upload
            class="upload-demo"
            :show-file-list="false"
            :action="baseApi + api"
            :headers="headers"
            :on-preview="handlePreview"
            :on-success="(response, file) => handleSuccess(response, file, '1')"
            :file-list="tableData"
            :before-upload="handleBeforeUpload"
          >
            <el-button type="success">文件上传</el-button>
          </el-upload> -->
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
            <div v-if="footer" style="margin-right: 10px">
              <el-button type="success">点击上传</el-button>
            </div>
          </el-upload>
        </div>
        <el-table :data="tableData" class="mb30">
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
              <el-button
                type="text"
                @click="handleDowns(row)"
                :disabled="false"
              >
                下载
              </el-button>
              <el-button
                type="text"
                @click="handlePreviewFile(row)"
                :disabled="false"
              >
                预览
              </el-button>
              <el-button
                type="text"
                @click="handleDelete(row, '1')"
                v-if="footer"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
    </div>
    <project-manage @projectManage="getChildlistPro" ref="manage" />
    <projectName ref="project" @selected="selectedPro" />
  </el-dialog>
</template>

<script>
  import { baseURL } from '@/config'
  import store from '@/store'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import { download, deleteReportFile } from '@/oapi/audit/report'
  import { saveSjgzjl, getSjgzjlDetail } from '@/oapi/audit/report'
  import projectManage from '@/components/danxuanPerson.vue'
  import projectName from './options/projectName.vue'
  const token = store.getters['user/token']
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    components: { projectManage, projectName },
    inheritAttrs: false,
    props: ['fetchData'],
    data() {
      return {
        loading: false,
        // baseApi: baseURL,
        // api: '/oiaudit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        fileList: [],
        baseApi: baseURL,
        importApi: '/audit/auditPlan/mergePlanProjectManageInfoImport',
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        tableData: [],
        formData: {
          projectId: '',
          projectName: '',
          sjzz: '',
          zs: '',
          ssslsj: '',
          slgzyd: '',
          slfawt: '',
        },
        radio: '',
        footer: true,
        rules: {
          projectName: [
            {
              required: true,
              message: '请输入审计项目名称',
              trigger: 'blur',
            },
          ],
          sjzz: [
            {
              required: true,
              message: '请选择审计组长',
            },
          ],
          zs: [
            {
              required: true,
              message: '请选择主审',
            },
          ],
          ssslsj: [
            {
              required: true,
              message: '请选择时间',
            },
          ],
          slgzyd: [
            {
              required: true,
              message: '审理关注的要点',
              trigger: 'blur',
            },
          ],
          slfawt: [
            {
              required: true,
              message: '审理发现问题摘要',
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
      // handleBeforeUpload(file) {
      //   const isLt2M = file.size / 1024 / 1024 < 100
      //   if (!isLt2M) {
      //     this.$message.error('文件大小不能超过 200MB!')
      //   }
      //   return isLt2M
      // },
      getCurrentDate() {
        return new Date(+new Date() + 8 * 3600 * 1000)
          .toJSON()
          .substr(0, 19)
          .replace('T', ' ')
      },
      async showEdit(row, title) {
        console.log('🚀 ~ showEdit ~ row:', row)
        this.dialogFormVisible = true
        if (row) {
          this.getDetail(row.id)
        }

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else if (title == 'add') {
          this.title = '新增'
          // let resL = JSON.parse(localStorage.getItem('userInfo')).realname
          // this.formData = {
          //   ...this.formData,
          //   createUser: resL,
          //   createTime: this.getCurrentDate(),
          // }
        }
      },
      async getDetail(id) {
        const { data } = await getSjgzjlDetail({ id })
        Object.assign(this.formData, data)
        this.tableData = data.attachments
      },
      projectManager(type) {
        this.proType = type
        this.$refs['manage'].showEdit()
      },
      getChildlistPro(val) {
        this.$set(this.formData, this.proType, val[0].realname)
      },
      selectedPro(val) {
        this.formData.projectName = val[0].projectName
        this.formData.projectId = val[0].id
      },
      close() {
        this.formData = {
          projectName: '',
          projectId: '',
          sjzz: '',
          zs: '',
          ssslsj: '',
          slgzyd: '',
          slfawt: '',
        }
        this.dialogFormVisible = false
        this.footer = true
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let attids = ''
            this.tableData.map((item) => {
              attids += item.attid
              attids += ','
            })
            attids = attids.substring(0, attids.length - 1)
            const params = { ...this.formData, attIds: attids }
            delete params.createUser
            delete params.createTime
            delete params.attachments
            const res = await saveSjgzjl(params)
            this.close()
            this.$emit('fetchData')
          }
        })
      },
      // async handleDown(row) {
      //   const data = await download({ attId: row.attid })
      //   let filename = row.attname
      //   let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
      //   let url = window.URL.createObjectURL(blob, {
      //     type: 'application/vnd.ms-excel',
      //   })
      //   const link = document.createElement('a')
      //   link.style.display = 'none'
      //   link.href = url
      //   link.setAttribute('download', filename)
      //   document.documentElement.appendChild(link)
      //   link.click()
      //   document.documentElement.removeChild(link)
      // },
      handleDelete(row, type) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          //删除对应的id
          let res = await deleteReportFile({ attId: row.attid })
          if (res.msg === '成功') {
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
            switch (type) {
              case '1':
                let list = this.tableData
                list = list.filter((item) => item.attid != row.attid)
                this.tableData = list
                break
              case '2':
                let list1 = this.tableData1
                list1 = list1.filter((item) => item.attid != row.attid)
                this.tableData1 = list1
                break
              case '3':
                let list2 = this.tableData2
                list2 = list2.filter((item) => item.attid != row.attid)
                this.tableData2 = list2
                break
            }
          } else {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          }
        })
      },
      // handlePreview(file) {},
      // handleSuccess(response, file, type) {
      //   if (response.result == '200') {
      //     switch (type) {
      //       case '1':
      //         this.tableData.push(response.data)
      //         break
      //       case '2':
      //         this.tableData1.push(response.data)
      //         break
      //       case '3':
      //         this.tableData2.push(response.data)
      //         break
      //     }
      //     this.$baseMessage(response.msg, 'success')
      //   } else {
      //     this.$baseMessage(response.msg, 'error')
      //   }
      // },
      /**
       * @description: 文件预览
       * @param {*} row 文件信息
       * @return {*}
       */
      // async handlePreviewFile(row) {
      //   const { data } = await getPrivewAttInfo({
      //     attId: row.attid,
      //     attType: 2,
      //   })

      //   const url =
      //     data.previewurl +
      //     '?url=' +
      //     encodeURIComponent(Base64.encode(data.ftpUrl))
      //   this.$iFrameDialog({ iframeUrl: url })
      // },
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
        // 调用自定义上传函数
        new Promise((resolve, reject) => {
          customUpload({
            baseApi: this.baseApi,
            api: this.api,
            key: window.key,
            iv: window.iv,
            headers: this.headers,
            fileList: fileList, // 使用 fileList 而不是 file
            onProgress: this.handleProgress,
            onSuccess: (response) => {
              this.handleSuccess(response)
              resolve(response) // 成功时调用 resolve
            },
            onError: (error) => {
              // this.handleError(error)
              reject(error) // 失败时调用 reject
            },
          })
        })
      },
      handleSuccess(file) {
        if (file.code == 200) {
          this.fileList = [...this.fileList, ...file.data]
          this.tableData = [...this.tableData, ...file.data]
          // this.tableData = list
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
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
      //下载公共方法调用
      async handleDowns(row) {
        try {
          // 调用 handleDown 并传递自定义的下载接口
          await handleDown(row, this.headers, this.lodeapi)
        } catch (error) {
          console.error('自定义下载失败:', error)
        }
      },
      handlePreviewFile(row) {
        if (row.isEncrypted === '1') {
          // 当文件是加密状态时，使用指定的在线预览链接
          const previewUrl = row.previewUrl
          window.open(previewUrl, '_blank')
        } else {
          this.$iFrameDialog({ attid: row.attid })
        }
      },
      async handlePreview(row) {
        const { data } = await getPrivewAttInfo({
          attId: row.attid,
          attType: 0,
        })
        let url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        if (
          data.ftpUrl.includes('.pdf') ||
          data.ftpUrl.includes('.doc') ||
          data.ftpUrl.includes('.docx')
        ) {
          url = url + '&officePreviewType=pdf'
        }
        window.open(url)
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
