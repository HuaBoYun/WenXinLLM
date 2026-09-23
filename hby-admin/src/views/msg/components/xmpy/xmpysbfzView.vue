<template>
  <div>
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="140px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="编号" prop="num">
            <el-input
              v-model="formData.num"
              clearable
              placeholder="请输编号"
              :disabled="!disabled"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="评选组" prop="selectionTeam">
            <el-input
              v-model="formData.selectionTeam"
              :style="{ width: '75%' }"
              disabled
              placeholder="请选择评选组"
            />
            <el-button
              type="primary"
              style="margin-left: 20px"
              @click="showPeople"
              :disabled="!disabled"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="填报人" prop="createdUser">
            <el-input
              v-model="formData.createdUser"
              clearable
              placeholder="请输填报人"
              disabled
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="填报时间" prop="createdTime">
            <el-input
              v-model="formData.createdTime"
              clearable
              placeholder="请输填报时间"
              disabled
              style="width: 100%"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-divider>汇总项目</el-divider>
        </el-col>
        <el-col :span="24">
          <div
            style="text-align: right; margin-bottom: 5px"
            v-if="disabled && formData.selectionTeam"
          >
            <el-button type="success" @click="openRelate">选择项目</el-button>
          </div>
          <el-table :data="tableData1">
            <el-table-column
              align="center"
              label="项目名称"
              prop="implementationProjectName"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleDetail(row)">
                  {{ row.implementationProjectName }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="申报单位"
              prop="approvalBelongGroupName"
            />
            <el-table-column
              align="center"
              prop="resultSort"
              label="排序"
              width="100"
            >
              <!-- <template slot-scope="scope">
                <el-input
                  @input="handleInput(scope.$index, scope.row)"
                  v-model="scope.row.resultSort"
                  size="mini"
                  style="width: 90%"
                  type="number"
                />
              </template> -->
            </el-table-column>
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template slot-scope="scope">
                <el-button type="text" @click="PresonSort(scope.$index)">
                  评选组排序
                </el-button>
                <el-button
                  type="text"
                  @click="deleteTable1(scope.row)"
                  v-if="disabled"
                >
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
          <div style="text-align: right; margin-bottom: 5px" v-if="disabled">
            <!-- <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
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
                <el-button type="text" @click="handleDowns(row)">
                  下载
                </el-button>
                <el-button type="text" @click="handlePreviewFile(row)">
                  预览
                </el-button>
                <el-button
                  type="text"
                  @click="handleDelete(row)"
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
    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />
    <!-- 选择人员弹窗 -->
    <selectPerson ref="executor" @projectManage="handleExecutorSelected" />
    <RelateSB ref="sb" @selected="handleRelate" />
    <PresonSort ref="PresonSort" @selected="handlePresonSortRelate" />
    <Edit ref="edit" @fetch-data="fetchData" />
    <div v-if="disabled" style="text-align: right; margin: 10px 5px">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="add" type="primary">确定</el-button>
      <el-button @click="ymsubmit" type="primary">提 交</el-button>
    </div>
    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
  </div>
</template>

<script>
  import { download, deleteReportFile } from '@/oapi/audit/report'
  import { getXmpyhzDetail, addOrUpdateXmpyhz } from '@/oapi/audit/xmpy'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import selectPerson from '@/components/selectPerson'
  import RelateSB from '@/views/oilAudit/xmpy/xmpyhz/relateSB.vue'
  import PresonSort from '@/views/oilAudit/xmpy/xmpyhz/PresonSort.vue'
  import store from '@/store'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import { formatDate, formatDay } from '@/utils/index'
  import Edit from '@/views/oilAudit/xmpy/xmpysb/edit.vue'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  const { baseURL } = require('@/config')
  const resL = JSON.parse(localStorage.getItem('userInfo'))?.realname
  const createdTime = formatDay(new Date())
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    name: 'xmpyhzEdit',
    inheritAttrs: false,
    components: {
      SelectDepartment,
      selectPerson,
      RelateSB,
      PresonSort,
      Resubmit,
      Edit,
    },
    props: [],
    data() {
      return {
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
        formData: {
          num: undefined,
          selectionTeam: undefined,
          selectionTeamIds: undefined,
          createdUser: resL,
          createdTime: createdTime,
          id: '',
          reviewTeamPersonnelIds: '',
        },
        auditProjectSelectList: [],
        tableData: [],
        tableData1: [],
        detailTableData1: [],
        disabled: true,
        rules: {
          title: [
            {
              required: true,
              message: '请输入标题',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        renderPersonList: [],
        editIndex: 0, //当前编辑的索引
        //提交
        fromId: '',
        flowtaskinfoflowid: '',
        ymFromId: '',
        status: '',
        fromIdcopy: '',
      }
    },
    methods: {
      // handleBeforeUpload(file) {
      //   const isLt2M = file.size / 1024 / 1024 < 100
      //   if (!isLt2M) {
      //     this.$message.error('文件大小不能超过 200MB!')
      //   }
      //   return isLt2M
      // },
      async showEdit(
        title,
        row,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status
      ) {
        this.dialogFormVisible = true
        this.fromId = row
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
        if (row) {
          const res = await getXmpyhzDetail({ id: row })
          this.tableData = res.data.file || []
          this.tableData1 = res.data.data.projectDeclareInfoList || []
          this.detailTableData1 = res.data.data.projectDeclareInfoList || []
          this.formData.createdUser = res.data.data.creatorName
          this.formData.createdTime = res.data.data.createdTime
          this.formData.id = res.data.data.id
          this.formData.num = res.data.data?.num
          const ids = res.data.data.userInfoList
            .map((item) => item.staffId)
            .toString()
          const names = res.data.data.userInfoList
            .map((item) => item.realName)
            .toString()
          this.formData.reviewTeamPersonnelIds = ids
          this.formData.selectionTeam = names
          //处理选中的人物信息，用于编辑保存
          const sortList = res.data.data.userInfoList.map((x) => {
            return {
              ...x,
              resultSort: '',
              staffid: x.staffId,
              realname: x.realName,
              userGrade: '',
            }
          })
          this.renderPersonList = sortList
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.disabled = false
        } else {
          this.title = '新增'
          this.disabled = true
          let userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData = {
            ...this.formData,
            cjr: userInfo.realname,
            cjsj: new Date().toJSON().split('T')[0],
          }
        }
      },
      close() {
        this.formData = {
          num: undefined,
          selectionTeam: undefined,
          selectionTeamIds: undefined,
          createdUser: '',
          createdTime: '',
          id: '',
          reviewTeamPersonnelIds: '',
        }
        this.dialogFormVisible = false
        this.tableData = []
        this.tableData1 = []
        this.detailTableData1 = []
        this.disabled = true
        this.$bus.$emit('updateMsg', 0)
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let attIds = ''

            this.tableData.map((item) => {
              attIds += item.attid
              attIds += ','
            })

            attIds = attIds.substring(0, attIds.length - 1)
            const ids = this.tableData1.map((res) => res.id).toString()

            const idsRemoved = this.detailTableData1
              .filter(
                (item) =>
                  !this.tableData1.some((otherItem) => otherItem.id === item.id)
              )
              .map((item) => item.id)
            let params = { ...this.formData }
            delete params.attachments
            delete params.createdTime
            delete params.createdUser
            const data = await addOrUpdateXmpyhz({
              ...params,
              fileIds: attIds.toString(),
              projectDeclareInfoList: this.tableData1,
              deleteIds: idsRemoved,
            })
            if (data.code == 200) {
              this.$baseMessage('保存成功', 'success')
            }
            this.$emit('fetch-data')
          } else {
            return false
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
      handleObject() {
        this.$refs['audiTree'].showEdit()
      },
      showPeople() {
        this.$refs.executor.showEdit()
      },
      getDepartmentInfo(val) {
        this.formData.declareBelongGroup = val.id
        this.formData.declareBelongGroupName = val.name
      },
      // 选择人员
      handleExecutorSelected(val) {
        const names = val.map((x) => x.realname).join(',')
        const ids = val.map((x) => x.staffid).join(',')
        this.formData.selectionTeam = names
        this.formData.selectionTeamIds = ids
        this.formData.reviewTeamPersonnelIds = ids
        const sortList = val.map((x) => {
          return { ...x, resultSort: '' }
        })
        this.renderPersonList = sortList
      },
      openRelate() {
        this.$refs['sb'].show(this.tableData1)
      },
      handleRelate(val) {
        const arr = this.renderPersonList.map((i) => {
          return { ...i, resultSort: '', userGrade: '' }
        })
        const info = val.map((res, index) => {
          return {
            ...res,
            resultSort: '',
            reviewTeamList: arr,
          }
        })
        this.tableData1 = this.tableData1.concat(info)
      },
      deleteTable1(row) {
        let list = this.tableData1
        list = list.filter((item) => item.id != row.id)
        this.tableData1 = list
      },
      handleInput(a, b) {
        //a是索引
        this.tableData1[a] = b
      },
      PresonSort(index) {
        this.editIndex = index
        if (this.title == '新增') {
          this.$refs['PresonSort'].showEdit(
            this.tableData1[index].reviewTeamList
          )
        } else {
          const arr = JSON.parse(JSON.stringify(this.renderPersonList))
          arr.forEach((j, i) => {
            this.tableData1[index].reviewTeamList.forEach((k, h) => {
              if (j.staffid == k.userId) {
                arr[i].userGrade = k.userGrade
              }
            })
          })

          this.$refs['PresonSort'].showEdit(arr)
        }
      },
      handlePresonSortRelate(val) {
        this.tableData1[this.editIndex].reviewTeamList = val
      },
      // async handlePreviewFile(row) {
      //   if (!row.attid) return this.$message.error('请先上传文件')
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
      //提交
      async ymsubmit() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.$refs.resubmit.ymsubmit()
          }
        })
      },
      async handleDetail(row) {
        await this.$refs['edit'].showEdit('detail', row)
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
