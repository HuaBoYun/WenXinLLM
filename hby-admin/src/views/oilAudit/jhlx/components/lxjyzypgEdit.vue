<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14">
      <el-form ref="ruleForm" label-width="110px" :model="formData" :rules="rules" size="mini">
        <el-col :span="12">
          <el-form-item label="排序" prop="sortNumber">
            <el-input
              v-model="formData.sortNumber"
              clearable
              placeholder="请输入排序"
              :style="{ width: '100%' }"
              type="number"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计项目名称" prop="projectName">
            <el-input
              v-model="formData.projectName"
              clearable
              placeholder="请输入审计项目名称"
              :style="{ width: '75%' }"
              :disabled="!footer"
            />
            <el-button
              type="primary"
              size="mini"
              style="margin-left: 10px"
              @click="$refs.SelectPlan.showEdit(formData.projectType)"
              :disabled="!footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="建议科室" prop="departmentName">
            <el-input
              v-model="formData.departmentName"
              placeholder="请选择建议科室"
              :style="{ width: '80%' }"
              disabled
            />
            <el-button
              type="primary"
              size="mini"
              style="margin-left: 10px"
              @click="$refs.department.show()"
              :disabled="!footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="单位范围" prop="unitRange">
            <el-input
              v-model="formData.unitRange"
              placeholder="请选择单位范围"
              :style="{ width: '75%' }"
              disabled
            />
            <el-button
              type="primary"
              size="mini"
              style="margin-left: 10px"
              @click="$refs.audiTree.showEdit()"
              :disabled="!footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计范围" prop="auditScope">
            <!-- <el-date-picker
              :style="{ width: '100%' }"
              v-model="formData.timeRange"
              placeholder="请选择时间范围"
              type="daterange"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              range-separator="至"
              start-placeholder="开始时间"
              end-placeholder="结束时间"
              :disabled="!footer"
            /> -->
            <el-input
              v-model="formData.auditScope"
              placeholder="请输入审计范围"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 47px">
          <el-form-item label="项目类型" prop="itemType">
            <el-input
              v-model="formData.itemType"
              disabled
              placeholder="请选择"
              :style="{ width: '75%' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              :disabled="!footer"
              @click.native="showtypeView"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="重点关注内容" prop="concernsContent">
            <el-input
              v-model="formData.concernsContent"
              clearable
              placeholder="请输入重点关注内容"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="2"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="立项理由及审计目的" prop="projectPurpose">
            <el-input
              v-model="formData.projectPurpose"
              clearable
              placeholder="请输入立项理由及审计目的"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="2"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="remark">
            <el-input
              v-model="formData.remark"
              clearable
              placeholder="请输入备注"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="2"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人" prop="createUser">
            <el-input v-model="formData.createUser" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间" prop="createTime">
            <el-input v-model="formData.createTime" disabled />
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
              :on-success="handleAvatarSuccess"
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
                <el-button type="text" @click="handleDowns(row)">下载</el-button>
                <el-button type="text" @click="handlePreviewFile(row)">
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
      <el-button @click="add" type="primary">确定</el-button>
    </div>
    <typeView ref="typeView" @submit="setType" />

    <!-- 部门选择 -->
    <DepartmentOptions ref="department" @selected="handleDepartmentSelected" />
    <!-- 单位选择 -->
    <SelectDepartment ref="audiTree" @submit="handleUnitSelected" />
    <SelectPlan ref="SelectPlan" @submit="handlePlanSelected" />
  </el-dialog>
</template>

<script>
  import { download, deleteReportFile } from '@/oapi/audit/report'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import { baseURL } from '@/config'
  import store from '@/store'
  import { formatDate } from '@/utils'
  const token = store.getters['user/token']
  import DepartmentOptions from '@/views/oilAudit/report/components/options/department.vue'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import { saveOrUpdate, detail } from '@/api/oilAudit/jhgl/lxjyzypg'
  import * as dayjs from 'dayjs'
  import typeView from '@/views/oilAudit/lrjjzr/components/type'
  import SelectPlan from '@/views/oilAudit/jhlx/components/selectPlan.vue'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    components: { DepartmentOptions, SelectDepartment, typeView, SelectPlan },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        formData: {
          id: undefined,
          sortNumber: '',
          projectName: '',
          departmentId: '',
          departmentName: '',
          unitRange: '',
          unitRangeId: '',
          // timeRange: undefined,
          auditScope: undefined,
          timeRangel: '',
          timeRangeR: '',
          concernsContent: '',
          projectPurpose: '',
          remark: '',
          createUser: '',
          createTime: '',
          projectType: '',
          itemType: '',
          suggestionId: '',
        },
        rules: {
          sortNumber: [
            {
              required: true,
              message: '请输入排序',
              trigger: 'blur',
            },
          ],
          projectName: [
            {
              required: true,
              message: '请输入审计项目名称',
              trigger: 'blur',
            },
          ],
          unitRange: [
            {
              required: true,
              message: '请选择单位范围',
              trigger: 'blur',
            },
          ],
        },
        footer: true,
        dialogFormVisible: false,
        title: '新增',
        tableData: [],
        fileList: [],
        baseApi: baseURL,
        // api: '/oiaudit/fileManage/upload',
        headers: { token: token },
        importApi: '/audit/auditPlan/mergePlanProjectManageInfoImport',
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      setType(e) {
        this.formData.itemType = e.auditType
        this.$forceUpdate()
      },
      showtypeView() {
        this.$refs['typeView'].showEdit()
      },
      showEdit(title, row, type) {
        console.log(title, row, type)
        this.dialogFormVisible = true
        this.tableData = []
        this.formData.projectType = type
        this.title =
          title == 'edit' ? '编辑' : title == 'detail' ? '详细' : '新增'
        this.footer = title !== 'detail'
        if (row) {
          detail({ id: row.id }).then((res) => {
            if (res.data && res.data.data) {
              this.tableData = res.data.attList
              Object.assign(this.formData, res.data.data)
              this.formData.createUser = this.formData.createStaff.realname
              // this.formData.timeRange = [
              //   dayjs(this.formData.timeRangel).format('YYYY-MM-DD'),
              //   dayjs(this.formData.timeRangeR).format('YYYY-MM-DD'),
              // ]
            }
          })
        } else {
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.createUser = userInfo.realname
          this.formData.createTime = formatDate(new Date())
        }
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
      close() {
        ;(this.formData.id = undefined), (this.formData.sortNumber = '')
        this.formData.projectName = ''
        this.formData.departmentId = ''
        this.formData.departmentName = ''
        this.formData.unitRange = ''
        this.formData.unitRangeId = ''
        // ;(this.formData.timeRange = undefined), (this.formData.timeRangel = '')
        // this.formData.timeRangeR = ''
        this.formData.auditScope = ''
        this.formData.concernsContent = ''
        this.formData.projectPurpose = ''
        this.formData.remark = ''
        this.formData.createUser = ''
        this.formData.createTime = ''
        this.formData.itemType = ''
        this.formData.projectType = ''
        this.formData.suggestionId = ''
        this.dialogFormVisible = false
        this.footer = true
      },
      /**
       * @description: 保存表单
       * @return {*}
       */
      add() {
        const aa = []
        const params = {}
        this.tableData.forEach((e) => {
          aa.push(e.attid)
        })
        params.attids = aa.toString()
        Object.assign(params, this.formData)
        // params.timeRangel = this.formData.timeRange[0]
        // params.timeRangeR = this.formData.timeRange[1]
        // delete params.timeRange
        delete params.createTime
        delete params.createUser
        delete params.createStaff
        saveOrUpdate(params).then((res) => {
          if (res.code == '1') {
            this.dialogFormVisible = false
            this.$emit('selected', res.data)
            this.close()
            this.$emit('fetchData')
          }
        })
      },
      // handleAvatarSuccess(res) {
      //   console.log('res', res)
      //   if (res.result == 200) {
      //     this.tableData.push(res.data)
      //   }
      // },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      handleDelete(row) {
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
            let list = this.tableData
            list = list.filter((item) => item.attid != row.attid)
            this.tableData = list
          } else {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          }
        })
      },
      /**
       * @description: 下载文件
       * @param {*} row
       * @return {*}
       */
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
      /**
       * @description: 选择部门
       * @param {*}
       * @return {*}
       */
      handleDepartmentSelected(node) {
        console.log('node', node)
        this.formData.departmentName = node.label
        this.formData.departmentId = node.id
      },
      /**
       * @description: 选择单位
       * @param {*}
       * @return {*}
       */
      handleUnitSelected(node) {
        this.formData.unitRange = node.label
        this.formData.unitRangeId = node.id
      },
      handlePlanSelected(val) {
        console.log(val, 'val')
        this.formData.projectName = val[0].projectName
        this.formData.suggestionId = val[0].id
        this.formData.concernsContent = val[0].concernsContent
        this.formData.projectPurpose = val[0].projectPurpose
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
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
