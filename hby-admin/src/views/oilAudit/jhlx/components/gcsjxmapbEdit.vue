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
      <el-form
        ref="ruleForm"
        label-width="140px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="!formDisabled"
      >
        <el-col :span="12">
          <el-form-item label="编号" prop="code">
            <el-input
              v-model="formData.code"
              placeholder="请输入编号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="关联计划" prop="planname">
            <el-input
              v-model="formData.planname"
              :style="{ width: '75%' }"
              disabled
              placeholder="关联计划"
            />
            <el-button
              @click="handlePlan"
              style="margin-left: 10px"
              type="primary"
              size="mini"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计项目名称" prop="name">
            <el-input
              v-model="formData.name"
              :style="{ width: '75%' }"
              clearable
              disabled
              placeholder="请输入审计项目名称"
            />
            <el-button
              @click="handlePlanxmname"
              style="margin-left: 10px"
              type="primary"
              size="mini"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="实施单位" prop="exePhraseUnit">
            <el-input
              v-model="formData.exePhraseUnit"
              clearable
              placeholder="请选择实施单位"
              :style="{ width: '75%', height: '28px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px', height: '28px' }"
              type="primary"
              @click="openDep('exePhraseUnit')"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被审计单位" prop="auditUnit">
            <el-input
              v-model="formData.auditUnit"
              clearable
              placeholder="请选择被审计单位"
              :style="{ width: '100%' }"
              disabled
            />
            <!-- <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="openDep('auditUnit')"
              size="small"
            >
              选择
            </el-button> -->
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目数量" prop="projectNum">
            <el-input
              v-model="formData.projectNum"
              clearable
              placeholder="请输入项目数量"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="金额（元）" prop="amount">
            <el-input
              v-model="formData.amount"
              clearable
              placeholder="请输入金额（元）"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="人数" prop="rsyq">
            <el-input
              v-model="formData.rsyq"
              clearable
              placeholder="请输入人数"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="小组" prop="auditGroup">
            <el-input
              v-model="formData.auditGroup"
              clearable
              placeholder="请输入小组"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="组长" prop="groupLeader">
            <el-input
              v-model="formData.groupLeader"
              clearable
              placeholder="请选择组长"
              style="width: 75%; height: 30px"
              disabled
            />
            <el-button
              @click="projectManager('groupLeader')"
              style="margin-left: 10px; height: 30px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="副组长" prop="fzzName">
            <el-input
              v-model="formData.fzzName"
              clearable
              placeholder="请选择副组长"
              style="width: 75%; height: 30px"
              disabled
            />
            <el-button
              @click="projectManager('fzzName')"
              style="margin-left: 10px; height: 30px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="主审" prop="approver">
            <el-input
              v-model="formData.approver"
              clearable
              placeholder="请选择主审"
              style="width: 75%; height: 28px"
              disabled
            />
            <el-button
              @click="projectManager('approver')"
              style="margin-left: 10px; height: 28px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>

        <el-col :span="12" style="clear: both">
          <el-form-item label="助审" prop="assistApprover">
            <el-input
              v-model="formData.assistApprover"
              clearable
              placeholder="请选择助审"
              style="width: 75%; height: 28px"
              disabled
            />
            <el-button
              @click="projectManagerS('assistApprover')"
              style="margin-left: 10px; height: 28px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="现场开始时间" prop="xcsrarttime">
            <el-date-picker
              v-model="formData.xcsrarttime"
              placeholder="请选择现场开始时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="现场结束时间" prop="xcendtime">
            <el-date-picker
              v-model="formData.xcendtime"
              placeholder="请选择现场结束时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人" prop="creater">
            <el-input
              v-model="formData.creater"
              disabled
              placeholder="请输入创建人"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间" prop="createTime">
            <el-date-picker
              v-model="formData.createTime"
              placeholder="请输入创建时间"
              type="dateTime"
              format="yyyy-MM-dd HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="备注" prop="remarks">
            <el-input
              v-model="formData.remarks"
              placeholder="请输入备注"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div
            style="text-align: right; margin-bottom: 5px"
            v-if="formDisabled"
          >
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
                  @click="handleDelete(row)"
                  v-if="formDisabled"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" v-if="formDisabled">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
    </div>

    <!-- 人员 -->
    <project-manage @projectManage="getChildlistPro" ref="manage" />
    <project-manage1 @projectManage="getChildlistPro1" ref="manage1" />

    <!-- 部门 -->
    <DepartmentOptions ref="department" @submit="handleDepartmentSelected" />

    <SjxmTable ref="sjxmTable" @fetch-table="getTable"></SjxmTable>
    <!-- 关联计划 -->
    <planModal ref="planModal" @selected="handlePlanSelected" />

    <planNameModal
      ref="planxmnameModalRef"
      @selected="handlePlanxmnameSelected"
      :isGcsjxm="true"
    />
  </el-dialog>
</template>

<script>
  import {
    planArrangeDetail,
    planArrangeSaveOrUpdate,
  } from '@/api/monitor/question'
  import { download, deleteReportFile } from '@/oapi/audit/report'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import DepartmentOptions from '@/views/oilAudit/jhlx/components/department.vue'
  import projectManage from '@/components/danxuanPerson.vue'
  import projectManage1 from '@/components/selectPerson.vue'
  import SjxmTable from '@/views/oilAudit/jhlx/components/table/sjxmTable'
  import planModal from '@/views/oilAudit/plan/components/planModal.vue'
  import planNameModal from '@/views/oilAudit/plan/components/planNameModal.vue'
  import store from '@/store'
  const { baseURL } = require('@/config')
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    components: {
      DepartmentOptions,
      projectManage,
      SjxmTable,
      projectManage1,
      planModal,
      planNameModal,
    },
    name: 'gcxmzjEdit',
    inheritAttrs: false,
    props: [],
    data() {
      return {
        baseApi: baseURL,
        // api: '/oiaudit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        fileList: [],
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        formData: {
          id: undefined,
          name: undefined,
          exePhraseUnit: undefined,
          exePhraseUnitId: undefined,
          auditUnit: undefined,
          auditUnitId: undefined,
          projectNum: undefined,
          amount: undefined,
          auditGroup: undefined,
          rsyq: undefined,
          auditRange: undefined,
          approver: undefined,
          assistApprover: undefined,
          assistApproverId: undefined,
          groupLeader: undefined,
          groupLeaderId: undefined,
          creater: undefined,
          createrId: undefined,
          createTime: undefined,
          fzzName: undefined,
          fzzStafffId: undefined,
          remarks: undefined,
          xcsrarttime: undefined,
          xcendtime: undefined,
          dataIndex: '',
          planname: '',
          planid: '',
          gljhxmid: '',
          gljhxmlx: '',
          dataIndex: undefined,
          code: '',
        },
        formDisabled: true,
        tableData: [],
        rules: {
          name: [
            {
              required: true,
              message: '请输入审计项目名称',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        depType: '',
        proType: '',
        isMoadl: false,
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      getTable(val) {
        console.log(val)
        this.$set(this.formData, 'name', val[0].sjxmmc)
        // this.formData.name = val[0].sjxmmc
      },
      getCurrentDate() {
        return new Date(+new Date() + 8 * 3600 * 1000)
          .toJSON()
          .substr(0, 19)
          .replace('T', ' ')
      },
      openDep(type) {
        this.depType = type
        this.$refs.department.showEdit()
      },
      handleDepartmentSelected(node) {
        this.$set(this.formData, this.depType, node.label)
        if (this.depType == 'exePhraseUnit') {
          //保存名称和对应的ID
          this.$set(this.formData, `exePhraseUnitId`, node.id)
        } else {
          this.$set(this.formData, `auditUnitId`, node.id)
        }
      },
      projectManager(type) {
        this.proType = type
        this.$refs['manage'].showEdit()
      },
      getChildlistPro(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname)
        this.$set(this.formData, this.proType, names.toString())
        if (this.proType == 'approver') {
          this.$set(this.formData, 'approverId', ids)
        } else if (this.proType == 'groupLeader') {
          this.$set(this.formData, 'groupLeaderId', ids)
        } else if (this.proType == 'fzzName') {
          this.$set(this.formData, 'fzzStafffId', ids)
        } else if (this.proType == 'assistApprover') {
          this.$set(this.formData, 'assistApproverId', ids)
        }
      },
      getChildlistPro1(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()

        this.$set(this.formData, 'assistApproverId', ids)
        this.$set(this.formData, 'assistApprover', names)
      },
      async showEdit(row, title) {
        this.dialogFormVisible = true
        if (row) {
          const res = await planArrangeDetail({ id: row.id })
          this.tableData = res.data.enginProjectAttDtoList || []
          this.formData = res.data
          this.formData.dataIndex = row.index
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.formDisabled = false
        } else if (title == 'add') {
          this.title = '新增'
          let resL = JSON.parse(localStorage.getItem('userInfo')).realname
          let ids = JSON.parse(localStorage.getItem('userInfo')).staffid
          this.formData = {
            ...this.formData,
            creater: resL,
            createrId: ids,
            createTime: this.getCurrentDate(),
          }
        }
      },
      close() {
        this.formData = {
          id: undefined,
          name: undefined,
          exePhraseUnit: undefined,
          exePhraseUnitId: undefined,
          auditUnit: undefined,
          auditUnitId: undefined,
          projectNum: undefined,
          amount: undefined,
          auditGroup: undefined,
          rsyq: undefined,
          auditRange: undefined,
          approver: undefined,
          assistApprover: undefined,
          assistApproverId: undefined,
          groupLeader: undefined,
          groupLeaderId: undefined,
          creater: undefined,
          createrId: undefined,
          createTime: undefined,
          fzzName: undefined,
          fzzStafffId: undefined,
          remarks: undefined,
          xcsrarttime: undefined,
          xcendtime: undefined,
          dataIndex: '',
          planname: '',
          planid: '',
          gljhxmid: '',
          gljhxmlx: '',
          dataIndex: undefined,
          code: '',
        }
        this.dialogFormVisible = false
        this.tableData = []
        this.formDisabled = true
      },
      save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let enginProjectAttEntityList = []
            this.tableData.map((item) => {
              enginProjectAttEntityList.push({
                attachmentId: item.attid,
              })
            })
            let params = { ...this.formData }
            delete params.enginProjectAttDtoList
            const data = await planArrangeSaveOrUpdate({
              ...params,
              enginProjectAttEntityList,
            })
            console.log(data, 'data')
            if (data.code == 1) {
              this.$baseMessage(data.msg, 'success')

              this.$emit('getNewData', {
                data: [data.data.data],
                index: this.formData.dataIndex,
              })
              this.close()
            } else {
              this.$baseMessage(data.msg, 'error')
            }
          } else {
            return false
          }
        })
      },
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
      handlePlan() {
        this.$refs['planModal'].showEdit()
      },
      handlePlanSelected(val) {
        this.formData.planname = val[0].sjxmmc
        this.formData.planid = val[0].jhid
      },
      handlePlanxmname() {
        if (!this.formData.planid) {
          this.$message({
            type: 'error',
            message: '请先选择关联计划',
          })
          return
        }
        this.isMoadl = true
        this.$nextTick(() => {
          console.log('planxmnameModalRef引用:', this.$refs.planxmnameModalRef)
          // 添加安全检查
          if (
            this.$refs.planxmnameModalRef &&
            typeof this.$refs.planxmnameModalRef.showEdit === 'function'
          ) {
            this.$refs.planxmnameModalRef.showEdit({
              jhcgid: this.formData.planid,
            })
          } else {
            console.error(
              'planxmnameModalRef 组件未正确加载或 showEdit 方法不存在'
            )
            this.$message({
              type: 'error',
              message: '组件加载失败，请刷新页面重试',
            })
          }
        })
      },
      handlePlanxmnameSelected(val, type) {
        console.log(val, 'val')
        this.formData.gljhxmid = val.id
        this.formData.name = val.name
        this.formData.auditUnit = val.unitRange
        this.formData.auditUnitId = val.unitRangeId
        this.formData.projectNum = val.projectCount
        this.formData.amount = val.projectAmount
        this.formData.gljhxmlx = type
      },
      projectManagerS() {
        this.$refs['manage1'].showEdit()
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
