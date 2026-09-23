<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
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
          <el-form-item label="工程名称" prop="contractname">
            <el-input
              v-model="formData.contractname"
              clearable
              placeholder="请输入工程名称"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 29px">
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
              disabled
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报审金额(元)" prop="contractmoney">
            <el-input
              v-model="formData.contractmoney"
              :style="{ width: '100%' }"
              placeholder="请输入报审金额(元)"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审定金额(元)" prop="sdmoney">
            <el-input
              v-model="formData.sdmoney"
              :style="{ width: '100%' }"
              placeholder="请输入审定金额(元)"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审减金额(元)" prop="hjmoney">
            <el-input
              v-model="formData.hjmoney"
              :style="{ width: '100%' }"
              placeholder="请输入审减金额(元)"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审减率" prop="hjl">
            <el-input
              v-model="formData.hjl"
              :style="{ width: '100%' }"
              placeholder="请输入审减率"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计人员" prop="rwnames">
            <el-input
              v-model="formData.rwnames"
              :style="{ width: '100%' }"
              placeholder="请输入审计人员"
            />
          </el-form-item>
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
              <div v-if="footer" style="margin-right: 10px">
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

    <!-- <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
    </div> -->

    <div style="text-align: right; margin-top: 10px" v-if="footer">
      <!-- <el-button @click="close">取 消</el-button> -->
      <el-button type="primary" @click="save">确 定</el-button>
      <!-- <el-button type="primary" @click="ymsubmit">提 交</el-button> -->
    </div>
    <!-- 部门 -->
    <DepartmentOptions ref="department" @selected="handleDepartmentSelected" />
    <AddChild ref="addchild" @AddChild="onAddChild" />

    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
  </el-dialog>
</template>

<script>
  import { baseURL } from '@/config'
  import store from '@/store'
  import {
    resultGetone,
    resultSaveOrUpdate,
    getattList,
  } from '@/oapi/audit/implement'
  import { deleteFile, download } from '@/oapi/audit/report'
  import DepartmentOptions from '@/views/oilAudit/report/components/options/department.vue'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import AddChild from '@/views/oilAudit/implement/components/addChild.vue'

  const token = store.getters['user/token']
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    components: { DepartmentOptions, Resubmit, AddChild },
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
        tableData1: [],
        formData: {
          id: '',
          resultcode: undefined,
          contractname: undefined,
          sgorgname: undefined,
          contractcode: undefined,
          contractname: undefined,
          sgorgname: undefined,
          contractmoney: undefined,
          hzmoney: undefined,
          hjmoney: undefined,
          sdmoney: undefined,
          overview: '',
          served: '',
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
          sgorgname: [
            {
              required: true,
              message: '请输入施工单位',
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
          contractname: [
            {
              required: true,
              message: '请输入工程名称',
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
              message: this.isJZK ? '请输入供应商' : '请输入施工单位',
              trigger: 'blur',
            },
          ],
          contractmoney: [
            {
              required: true,
              message: '请输入报审金额(元)',
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
              message: '请输入审定金额(元)',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        depType: '',
        // 流程相关
        fromId: '',
        fromIdcopy: '',
        flowtaskinfoflowid: '',
        ymFromId: '',
        status: '',
        isJZK: false,
      }
    },
    computed: {},
    watch: {},
    created() {
      //判断权限是否有经责科.展示不同title
      let userInfo = JSON.parse(localStorage.getItem('userInfo'))
      if (!userInfo.roleNames.includes('经责科审计人员')) {
        this.isJZK = false
      } else {
        this.isJZK = true
      }
    },
    mounted() {},
    methods: {
      async ymsubmit() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.$refs.resubmit.ymsubmit()
          }
        })
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
        this.$set(this.formData, this.depType, node.label)
        if (this.depType == 'sgorgname') {
          this.$set(this.formData, `orgids`, node.id)
        } else if (this.depType == 'sgorgname') {
          this.$set(this.formData, `sgorgid`, node.id)
        }
      },
      async showEdit(row, type) {
        this.dialogFormVisible = true
        this.formDisabled = type == 'detail' ? true : false
        console.log(row)
        if (type == 'edit') {
          this.title = '编辑'
          this.formData = row
        } else if (type == 'detail') {
          this.title = '详细'
          this.formData = row
          this.footer = false
        } else if (type == 'add') {
          this.title = '新增'
          let resL = JSON.parse(localStorage.getItem('userInfo')).realname
          this.formData = {
            ...this.formData,
            // createdUser: resL,
            // createdTime: this.getCurrentDate(),
          }
        }
      },
      // async showEdit(
      //   title,
      //   formId,
      //   flowtaskinfoflowid,
      //   ymFromId,
      //   isWfqdedit,
      //   status
      // ) {
      //   console.log('title,', title)
      //   // 流程相关
      //   this.fromId = formId
      //   this.fromIdcopy = formId
      //   this.flowtaskinfoflowid = flowtaskinfoflowid
      //   this.ymFromId = ymFromId
      //   this.status = status
      //   this.dialogFormVisible = true
      //   if (formId) {
      //     const res = await resultGetone({ resultid: formId })
      //     this.formData = res.data.data
      //     Object.assign(this.formData, res.data.data)
      //     this.tableData1 = res.data.data.zixbs
      //     const data = await getattList({ resultid: formId })
      //     this.tableData = data.data
      //   }

      //   if (title == 'edit') {
      //     this.title = '编辑'
      //   } else if (title == 'detail') {
      //     this.title = '详细'
      //     this.footer = false
      //   } else if (title == 'add') {
      //     this.title = '新增'
      //     let resL = JSON.parse(localStorage.getItem('userInfo')).realname
      //     this.formData = {
      //       ...this.formData,
      //       // createdUser: resL,
      //       // createdTime: this.getCurrentDate(),
      //     }
      //   }
      // },
      close() {
        this.$bus.$emit('updateMsg', 0)
        this.formData = {
          id: '',
          resultcode: undefined,
          contractname: undefined,
          sgorgname: undefined,
          contractcode: undefined,
          contractname: undefined,
          sgorgname: undefined,
          contractmoney: undefined,
          hzmoney: undefined,
          hjmoney: undefined,
          sdmoney: undefined,
          overview: '',
          served: '',
        }
        this.tableData = []
        this.tableData1 = []
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
            let zixbids = ''
            this.tableData1.map((item) => {
              zixbids += item.resultid
              zixbids += ','
            })
            zixbids = zixbids.substring(0, zixbids.length - 1)
            let params = { ...this.formData, attids, zixbids }
            delete params.zixbs
            delete params.realname
            // const data = await resultSaveOrUpdate({
            //   ...params,
            //   zixbids,
            //   attids,
            // })
            // if (data.code == 1) {
            //   this.$baseMessage(data.msg, 'success')
            //   this.$emit('fetchData')
            //   // this.close()
            // } else {
            //   this.$baseMessage(data.msg, 'error')
            // }
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
      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        this.tableData = list
        await deleteFile({ attId: row.attid })
      },
      // handlePreview(file) {},
      // handleSuccess(file) {
      //   if (file.result == '200') {
      //     let list = this.tableData
      //     list.push(file.data)
      //     this.tableData = list
      //     this.$baseMessage(file.msg, 'success')
      //   } else {
      //     this.$baseMessage(file.msg, 'error')
      //   }
      // },
      onAddChild(rowData) {
        let index = this.tableData1.findIndex(
          (item) => item.resultid == rowData.resultid
        )
        if (index !== -1) {
          this.$set(this.tableData1, index, rowData)
          // this.tableData1[index] = rowData // 如果ID相同，替换
        } else {
          this.tableData1.push(rowData) // 如果ID不同，或者不存在，添加进数组
        }
        this.calculateTotalAmount(this.tableData1)
      },
      calculateTotalAmount(items) {
        let contractmoney = 0
        let hzmoney = 0
        let hjmoney = 0
        let sdmoney = 0
        items.forEach((item) => {
          contractmoney += Number(item.contractmoney)
          hzmoney += Number(item.hzmoney)
          hjmoney += Number(item.hjmoney)
          sdmoney += Number(item.sdmoney)
        })
        this.formData.contractmoney = contractmoney
        this.formData.hzmoney = hzmoney
        this.formData.hjmoney = hjmoney
        this.formData.sdmoney = sdmoney
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
