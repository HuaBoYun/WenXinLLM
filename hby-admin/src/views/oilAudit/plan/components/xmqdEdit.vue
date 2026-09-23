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
          <el-form-item label="编号" prop="qdcode">
            <el-input
              v-model="formData.qdcode"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入编号"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="关联计划" prop="planname">
            <el-input
              v-model="formData.planname"
              :style="{ width: '75%' }"
              disabled
              placeholder="关联计划"
            />
            <el-button
              @click="handlePlan"
              style="margin-left: 15px"
              type="primary"
              size="mini"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col> -->
        <!-- <el-col :span="12">
          <el-form-item label="审计项目名称" prop="xmname">
            <el-input
              v-model="formData.xmname"
              :style="{ width: '75%' }"
              clearable
              disabled
              placeholder="请输入审计项目名称"
            />
            <el-button
              @click="handlePlanxmname"
              style="margin-left: 15px"
              type="primary"
              size="mini"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col> -->
        <el-col :span="12" style="height: 47px">
          <el-form-item label="审计项目名称" prop="xmname">
            <el-input
              v-model="formData.xmname"
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
          <el-form-item label="审计项目类型" prop="xmtype">
            <!-- <el-input
              v-model="formData.xmtype"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入审计项目类型"
            /> -->
            <el-input
              v-model="formData.xmtype"
              disabled
              clearable
              placeholder="请选择"
              :style="{ width: '75%' }"
            />
            <el-button
              style="margin-left: 10px"
              type="primary"
              @click.native="showtypeView"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="实施类型" prop="sstype">
            <el-select
              v-model="formData.sstype"
              placeholder="请选择实施类型"
              :style="{ width: '100%' }"
            >
              <el-option label="自审" value="自审" />
              <el-option label="外包" value="外包" />
              <el-option label="授权自审" value="授权自审" />
              <el-option label="委托审计" value="委托审计" />
              <el-option label="交叉审计" value="交叉审计" />
              <el-option label="重大专项审计" value="重大专项审计" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计流程类型" prop="flowtype">
            <el-select
              v-model="formData.flowtype"
              placeholder="请选择审计流程类型"
              :style="{ width: '100%' }"
            >
              <el-option label="标准审计程序" value="标准审计程序" />
              <el-option label="简化审计程序" value="简化审计程序" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被审计单位" prop="borgname">
            <el-input
              v-model="formData.borgname"
              :style="{ width: '75%' }"
              clearable
              placeholder="被审计单位"
              disabled
            />
            <el-button
              @click="handleObject"
              style="margin-left: 15px"
              type="primary"
              size="mini"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="实施审计机构" prop="ssorgname">
            <el-input
              v-model="formData.ssorgname"
              :style="{ width: '75%' }"
              clearable
              placeholder="请输入实施审计机构"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="openDep()"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目经理" prop="xmjlname">
            <el-input
              v-model="formData.xmjlname"
              :style="{ width: '75%' }"
              clearable
              placeholder="项目经理"
              disabled
            />
            <el-button
              @click="openPerson('xmjlname')"
              style="margin-left: 10px; height: 30px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="clear: both">
          <el-form-item label="主审" prop="zsname">
            <el-input
              v-model="formData.zsname"
              :style="{ width: '75%' }"
              clearable
              placeholder="主审"
              disabled
            />
            <el-button
              @click="openPerson('zsname')"
              style="margin-left: 10px; height: 30px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否工程项目" prop="isgc">
            <el-select
              v-model="formData.isgc"
              placeholder="请选择"
              style="width: 266px"
              :disabled="disabled"
            >
              <el-option
                v-for="item in wwArr1"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="clear: both">
          <el-form-item label="现场结束时间" prop="siteEndTime">
            <el-date-picker
              v-model="formData.siteEndTime"
              placeholder="请选择"
              :style="{ width: '100%' }"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :disabled="disabled"
            />
          </el-form-item>
        </el-col>
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
            <div v-if="!disabled" style="margin-right: 10px">
              <el-button type="success">点击上传</el-button>
            </div>
          </el-upload>
        </div>
        <el-table :data="tableData">
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
    </el-row>
    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />
    <!-- 人员 -->
    <project-manage @projectManage="getChildlistPro" ref="manage" />
    <!-- 部门 -->
    <DepartmentOptions ref="department" @selected="handleDepartmentSelected" />
    <!-- 关联计划 -->
    <planModal ref="planModal" @selected="handlePlanSelected" />
    <planxmnameModal
      ref="planxmnameModal"
      @selected="handlePlanxmnameSelected"
    />
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
    </div>
    <typeView ref="typeView" @submit="setType" />

    <projectName ref="project" @selected="selectedPro" xctype="xmqd" />
  </el-dialog>
</template>

<script>
  import { baseURL } from '@/config'
  import store from '@/store'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import planModal from './planModal'
  import planxmnameModal from './planxmnameModal'
  import projectManage from '@/components/danxuanPerson.vue'
  import DepartmentOptions from '@/views/oilAudit/report/components/options/department.vue'
  import projectName from '@/views/oilAudit/report/components/options/projectName.vue'
  import {
    xmglqdDetail,
    xmglqdSaveOrUpdate,
    xmglqGetFile,
    xmglqddeleteAtt,
  } from '@/oapi/audit/project'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import { download } from '@/oapi/audit/report'
  const token = store.getters['user/token']
  import typeView from '@/views/oilAudit/lrjjzr/components/type'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'

  export default {
    components: {
      SelectDepartment,
      projectManage,
      DepartmentOptions,
      planModal,
      planxmnameModal,
      typeView,
      projectName,
    },
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
          zsstaffid: '',
          siteEndTime: '',
          zsname: '',
          xmtype: '',
          xmname: '',
          xmjlstaffid: '',
          xmjlname: '',
          xmdqid: '',
          sstype: '',
          ssorgname: '',
          ssorgid: '',
          flowtype: '',
          createstaffid: '',
          createdate: '',
          borgname: '',
          borgid: '',
          planname: '',
          planid: '',
          qdcode: '',
          gljhxmid: '',
          gljhxmlx: '',
          isgc: '0',
        },
        footer: true,
        rules: {
          xmname: [
            {
              required: true,
              message: '请输入审计项目名称',
              trigger: 'blur',
            },
          ],
          xmtype: [
            {
              required: true,
              message: '请输入审计项目类型',
              trigger: 'blur',
            },
          ],
          sstype: [
            {
              required: true,
              message: '请输入实施类型',
              trigger: 'blur',
            },
          ],
          flowtype: [
            {
              required: true,
              message: '请输入审计流程类型',
              trigger: 'blur',
            },
          ],
          borgname: [
            {
              required: true,
              message: '请输入被审计单位',
              trigger: 'blur',
            },
          ],
          ssorgname: [
            {
              required: true,
              message: '请输入实施审计机构',
              trigger: 'blur',
            },
          ],
          xmjlname: [
            {
              required: true,
              message: '请输入项目经理',
              trigger: 'blur',
            },
          ],
          zsname: [
            {
              required: true,
              message: '请输入主审',
              trigger: 'blur',
            },
          ],
          isgc: [
            {
              required: true,
              message: '请输是否工程项目',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        options: [],
        personType: '',
        wwArr1: [
          { value: '1', label: '是', key: '1' },
          { value: '0', label: '否', key: '2' },
        ],
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      setType(e) {
        this.formData.xmtype = e.auditType
        this.$forceUpdate()
      },
      showtypeView() {
        this.$refs['typeView'].showEdit()
      },
      getCurrentDate() {
        return new Date(+new Date() + 8 * 3600 * 1000).toJSON().substr(0, 10)
      },
      async showEdit(row, title) {
        this.dialogFormVisible = true
        if (row) {
          const res = await xmglqdDetail({ xmdqid: row.xmdqid })
          const arr = await xmglqGetFile({ xmdqid: row.xmdqid })
          this.formData = res.data.data
          this.tableData = arr.data.data
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
            isgc: '0',
            cjr: resL,
            cjsj: this.getCurrentDate(),
          }
        }
      },
      close() {
        this.formData = {
          zsstaffid: '',
          siteEndTime: '',
          zsname: '',
          xmtype: '',
          xmname: '',
          xmjlstaffid: '',
          xmjlname: '',
          xmdqid: '',
          sstype: '',
          ssorgname: '',
          ssorgid: '',
          flowtype: '',
          createstaffid: '',
          createdate: '',
          borgname: '',
          borgid: '',
          planname: '',
          planid: '',
          qdcode: '',
          gljhxmid: '',
          gljhxmlx: '',
          isgc: '0',
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
            let params = { ...this.formData, attids }
            delete params.attachments
            const res = await xmglqdSaveOrUpdate({
              ...params,
            })
            if (res.code == 1) {
              this.$baseMessage('保存成功', 'success')
              this.$emit('fetchData')
              this.close()
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
      handleDelete(row) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          //删除对应的id
          let res = await xmglqddeleteAtt({ attid: row.attid })
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
      handleObject() {
        this.$refs['audiTree'].showEdit()
      },
      getDepartmentInfo(val) {
        this.formData.borgid = val.id
        this.formData.borgname = val.label
      },
      showPeople(type) {
        this.peopleType = type
        this.$refs.executor.show()
      },
      getChildlistPro(val) {
        if (this.personType == 'xmjlname') {
          this.formData.xmjlstaffid = val[0].staffid
          this.formData.xmjlname = val[0].realname
        } else {
          this.formData.zsname = val[0].realname
          this.formData.zsstaffid = val[0].staffid
        }
      },
      handleDepartmentSelected(node) {
        this.$set(this.formData, 'ssorgid', node.id)
        this.$set(this.formData, 'ssorgname', node.label)
      },
      openDep() {
        this.$refs.department.show()
      },
      openPerson(type) {
        this.personType = type
        this.$refs.manage.showEdit()
      },
      handlePlan() {
        this.$refs['planModal'].showEdit()
      },
      handlePlanSelected(val) {
        console.log(val, 'val')
        this.formData.planname = val[0].jhmc
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
        this.$refs['planxmnameModal'].showEdit({ jhcgid: this.formData.planid })
      },
      handlePlanxmnameSelected(val, type) {
        console.log(val, 'val')
        this.formData.gljhxmid = val.id
        this.formData.xmname = val.name
        this.formData.gljhxmlx = type
      },
      selectedPro(val) {
        this.$set(this.formData, 'xmname', val[0].projectName)
        this.$set(this.formData, 'gljhxmid', val[0].id)
        this.$set(this.formData, 'borgname', val[0].auditOrgName)
        this.$set(this.formData, 'borgid', val[0].auditOrgId)
        this.$set(this.formData, 'zsname', val[0].zsname)
        this.$set(this.formData, 'zsstaffid', val[0].zsstaffid)
        this.$set(this.formData, 'xmjlname', val[0].projectOrderName)
        this.$set(this.formData, 'xmjlstaffid', val[0].projectOrderId)
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
