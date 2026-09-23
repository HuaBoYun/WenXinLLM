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
        <el-col :span="12" style="height: 29px">
          <el-form-item label="编号" prop="resultcode">
            <el-input
              v-model="formData.resultcode"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入编号"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计项目名称" prop="projectname">
            <el-input
              v-model="formData.projectname"
              clearable
              placeholder="请输入审计项目名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 29px">
          <el-form-item label="被审计单位名称" prop="orgidnames">
            <el-input
              v-model="formData.orgidnames"
              clearable
              placeholder="请输入被审计单位名称"
              :style="{ width: '266px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="openDep('orgidnames')"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同编号" prop="contractcode">
            <el-input
              v-model="formData.contractcode"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入合同编号"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="合同名称" prop="contractname">
            <el-input
              v-model="formData.contractname"
              clearable
              placeholder="请输入合同名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="施工单位" prop="sgorgname">
            <el-input
              v-model="formData.sgorgname"
              clearable
              placeholder="请输入施工单位"
              :style="{ width: '266px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="openDep('sgorgname')"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同金额(元)" prop="contractmoney">
            <el-input
              v-model="formData.contractmoney"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入合同金额(元)"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="核增金额(元)" prop="hzmoney">
            <el-input
              v-model="formData.hzmoney"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入核增金额(元)"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="核减金额(元)" prop="hjmoney">
            <el-input
              v-model="formData.hjmoney"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入核减金额(元)"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计认定金额(元)" prop="sdmoney">
            <el-input
              v-model="formData.sdmoney"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入审计认定金额(元)"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="审计意见"
            label-width="140px"
            prop="changereason"
          >
            <el-input
              type="textarea"
              placeholder="请输入审计意见"
              :rows="5"
              v-model="formData.changereason"
            ></el-input>
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-divider>子项表</el-divider>
        </el-col>
        <el-col :span="24" style="margin-bottom: 20px">
          <div style="text-align: right; margin-bottom: 5px">
            <el-button type="success" @click="$refs.addchild.showEdit()">
              新增
            </el-button>
          </div>
          <el-table :data="tableData1">
            <el-table-column
              align="center"
              label="序号"
              prop="id"
              width="100"
            ></el-table-column>
            <el-table-column
              align="center"
              label="审计事项"
              prop="projectName"
            />
            <el-table-column
              align="center"
              label="立项理由及审计目的"
              prop="projectPurpose"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleTableDelete(row)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
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
              :on-success="handleSuccess"
              :file-list="tableData"
              :before-upload="handleBeforeUpload"
            >
              <el-button type="success">上传</el-button>
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
              <div style="margin-right: 10px">
                <el-button type="success">点击上传</el-button>
              </div>
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

    <!-- 部门 -->
    <DepartmentOptions ref="department" @selected="handleDepartmentSelected" />
    <!-- <AddChild ref="addchild" /> -->
  </el-dialog>
</template>

<script>
  import { baseURL } from '@/config'
  import store from '@/store'
  import { resultGetone, resultSaveOrUpdate } from '@/oapi/audit/implement'
  import { deleteFile, download } from '@/oapi/audit/report'
  import DepartmentOptions from '@/views/oilAudit/report/components/options/department.vue'
  // import AddChild from './addChild.vue'

  const token = store.getters['user/token']
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    components: { DepartmentOptions },
    inheritAttrs: false,
    props: ['fetchData'],
    data() {
      return {
        loading: false,
        baseApi: baseURL,
        // api: '/oiaudit/fileManage/upload',
        headers: { token: token },
        fileList: [],
        importApi: '/audit/auditPlan/mergePlanProjectManageInfoImport',
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        tableData: [],
        formData: {
          id: '',
          scoreItems: [],
        },
        footer: true,
        rules: {
          resultcode: [
            {
              required: true,
              message: '请输入编号',
              trigger: 'blur',
            },
          ],
          orgidnames: [
            {
              required: true,
              message: '请输入被审计单位名称',
              trigger: 'blur',
            },
          ],
          contractcode: [
            {
              required: true,
              message: '请输入合同编号',
              trigger: 'blur',
            },
          ],
          projectname: [
            {
              required: true,
              message: '请输入审计项目名称',
              trigger: 'blur',
            },
          ],
          contractname: [
            {
              required: true,
              message: '请输入合同名称',
              trigger: 'blur',
            },
          ],
          sgorgname: [
            {
              required: true,
              message: '请输入施工单位',
              trigger: 'blur',
            },
          ],
          contractmoney: [
            {
              required: true,
              message: '请输入合同金额(元)',
              trigger: 'blur',
            },
          ],
          hzmoney: [
            {
              required: true,
              message: '请输入核增金额(元)',
              trigger: 'blur',
            },
          ],
          hjmoney: [
            {
              required: true,
              message: '请输入核减金额(元)',
              trigger: 'blur',
            },
          ],
          sdmoney: [
            {
              required: true,
              message: '请输入审计认定金额(元)',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        depType: '',
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      // 添加子项
      handleAddTableData() {},
      // 上下移动
      handleMove(index, dir) {
        const curOptionData = this.formData.scoreItems.splice(index, 1)[0]
        const listData = JSON.parse(JSON.stringify(this.formData.scoreItems))
        const len = listData.length
        let _index = 0
        if (dir === 'up') {
          _index = index <= 0 ? 0 : index - 1
        } else if (dir === 'down') {
          _index = index >= len ? len : index + 1
        }

        listData.splice(_index, 0, curOptionData)

        this.formData.scoreItems = listData
      },

      // 删除归属重点项
      removeItem(index) {
        this.postForm.scoreItems.splice(index, 1)
      },
      getCurrentDate() {
        return new Date(+new Date() + 8 * 3600 * 1000)
          .toJSON()
          .substr(0, 19)
          .replace('T', ' ')
      },
      openDep(type) {
        this.depType = type
        this.$refs.department.show()
      },
      handleDepartmentSelected(node) {
        this.$set(this.formData, this.depType, node.name)
        if (this.depType == 'orgidnames') {
          this.$set(this.formData, `orgids`, node.id)
        } else if (this.depType == 'sgorgname') {
          this.$set(this.formData, `sgorgid`, node.id)
        }
      },
      async showEdit(row, title) {
        this.dialogFormVisible = true
        if (row) {
          const res = await resultGetone({ resultid: row.resultid })
          // this.formData.createType = res.data.createType
          // this.formData.dataBaseConnectionAddress =
          //   res.data.dataBaseConnectionAddress
          // this.formData.dataBaseOwnership = res.data.dataBaseOwnership
          // this.formData.dataBasePassWord = res.data.dataBasePassWord
          // this.formData.dataBaseType = res.data.dataBaseType
          // this.formData.dataBaseUsers = res.data.dataBaseUsers
          this.formData = res.data.data
        }

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else if (title == 'add') {
          this.title = '新增'
          let resL = JSON.parse(localStorage.getItem('userInfo')).realname
          this.formData = {
            ...this.formData,
            // createdUser: resL,
            // createdTime: this.getCurrentDate(),
          }
        }
      },
      close() {
        this.formData = {
          id: '',
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

            let params = { ...this.formData }
            // delete params.projectname
            const data = await resultSaveOrUpdate({
              ...params,
              attids,
            })
            if (data.code == 1) {
              this.$baseMessage(data.msg, 'success')
              this.$emit('fetchData')
              this.close()
            } else {
              this.$baseMessage(data.msg, 'error')
            }
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
      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        this.tableData = list
        await deleteFile({ attId: row.attid })
      },
      async handleTableDelete(row) {
        // let list = this.tableData
        // list = list.filter((item) => item.attid != row.attid)
        // this.tableData = list
        // await deleteFile({ attId: row.attid })
      },
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
  .el-form-item__contractname span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
