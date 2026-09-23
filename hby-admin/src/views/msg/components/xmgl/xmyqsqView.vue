<template>
  <div>
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
          <el-form-item label="关联实施方案" prop="xmname">
            <el-input
              v-model="formData.xmname"
              :style="{ width: '75%' }"
              clearable
              placeholder="关联实施方案"
              disabled
            />
            <el-button
              type="primary"
              @click="$refs.planingModal.showEdit()"
              size="mini"
              style="margin-left: 10px"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计项目编号" prop="xmbh">
            <el-input
              v-model="formData.xmbh"
              :style="{ width: '100%' }"
              disabled
              placeholder="请输入审计项目编号"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计项目名称" prop="xmname">
            <el-input
              v-model="formData.xmname"
              :style="{ width: '100%' }"
              disabled
              placeholder="请输入审计项目名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计实施时间" prop="ssdate">
            <el-date-picker
              v-model="formData.ssdate"
              type="date"
              value-format="yyyy-MM-dd"
              format="yyyy-MM-dd"
              placeholder="选择日期"
              :style="{ width: '100%' }"
              disabled
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被审计单位">
            <el-input
              v-model="formData.borgname"
              :style="{ width: '75%' }"
              clearable
              placeholder="被审计单位"
              disabled
            />
            <el-button
              type="primary"
              @click="handleObject"
              size="mini"
              style="margin-left: 10px"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="实施审计部门">
            <el-input
              v-model="formData.ssorgname"
              :style="{ width: '75%' }"
              clearable
              placeholder="实施审计部门"
              disabled
            />
            <el-button
              type="primary"
              @click="openDep()"
              size="mini"
              style="margin-left: 10px"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="组长">
            <el-input
              v-model="formData.zzname"
              :style="{ width: '75%' }"
              clearable
              placeholder="组长"
              disabled
            />
            <el-button
              type="primary"
              @click="openPerson('zzname')"
              size="mini"
              style="margin-left: 10px"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="主审">
            <el-input
              v-model="formData.zsname"
              :style="{ width: '75%' }"
              clearable
              placeholder="主审"
              disabled
            />
            <el-button
              type="primary"
              @click="openPerson('zsname')"
              size="mini"
              style="margin-left: 10px"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="助审">
            <el-input
              v-model="formData.zyname"
              :style="{ width: '75%' }"
              clearable
              placeholder="助审"
              disabled
            />
            <el-button
              type="primary"
              @click="openPerson('zyname')"
              size="mini"
              style="margin-left: 10px"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="延期原因" prop="yqyy">
            <el-input
              v-model="formData.yqyy"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入延期原因"
              type="textarea"
              :rows="2"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="延期时间" prop="yqdate">
            <el-date-picker
              v-model="formData.yqdate"
              type="date"
              value-format="yyyy-MM-dd"
              format="yyyy-MM-dd"
              placeholder="选择日期"
              :style="{ width: '100%' }"
            ></el-date-picker>
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
    <SelectDepartment ref="audiTree" @select="getDepartmentInfo" />
    <!-- 人员 -->
    <project-manage @projectManage="getChildlistPro" ref="manage" />
    <!-- 部门 -->
    <DepartmentOptions ref="department" @selected="handleDepartmentSelected" />
    <planingModal ref="planingModal" @selected="handlePlaningModalSelected" />

    <div
      slot="footer"
      v-if="footer"
      style="text-align: right; margin: 10px 5px"
    >
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
      <el-button @click="ymsubmit" type="primary" :disabled="btnLoading">
        提 交
      </el-button>
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
  import { baseURL } from '@/config'
  import store from '@/store'
  import SelectDepartment from '@/components/departments.vue'
  import projectManage from '@/views/audit/project/components/formComponents/projectManage.vue'
  import DepartmentOptions from '@/views/oilAudit/report/components/options/department.vue'
  import { download } from '@/oapi/audit/report'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import planingModal from '@/views/oilAudit/plan/components/planingModal.vue'
  import {
    saveXmyqsqOrupdate,
    getXmyqsqone,
    getXmyqsqattList,
    deleteXmyqsqatt,
  } from '@/oapi/audit/xmpy'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  const token = store.getters['user/token']
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    components: {
      SelectDepartment,
      projectManage,
      DepartmentOptions,
      planingModal,
      Resubmit,
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
          xmname: '',
          ssdate: '',
          borgname: '',
          borgid: '',
          ssorgname: '',
          ssorgid: '',
          zzname: '',
          zzstaffid: '',
          zsname: '',
          zsstaffid: '',
          zyname: '',
          zystaffid: '',
          yqyy: '',
          yqdate: '',
          projectid: '',
        },
        radio: '',
        footer: true,
        rules: {
          xmname: [
            {
              required: true,
              message: '请选择实施方案',
              trigger: 'blur',
            },
          ],
          // ssdate: [
          //   {
          //     required: true,
          //     message: '请选择审计实施时间',
          //     trigger: 'change',
          //   },
          // ],
          yqyy: [
            {
              required: true,
              message: '请输入延期原因',
              trigger: 'blur',
            },
          ],
          yqdate: [
            {
              required: true,
              message: '请选择延期时间',
              trigger: 'change',
            },
          ],
          borgname: [
            {
              required: true,
              message: '请选择实施方案',
              trigger: 'change',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        personType: '',
        //提交
        fromId: '',
        flowtaskinfoflowid: '',
        ymFromId: '',
        status: '',
        fromIdcopy: '',
        btnLoading: false,
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      getCurrentDate() {
        return new Date(+new Date() + 8 * 3600 * 1000).toJSON().substr(0, 10)
      },
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
        console.log(row, 'row')
        if (row) {
          const res = await getXmyqsqone({ xmdqid: row })
          const arr = await getXmyqsqattList({ xmdqid: row })
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
          let resL = JSON.parse(localStorage.getItem('userInfo')).staffid
          this.formData = {
            ...this.formData,
            createstaffid: resL,
            createdate: this.getCurrentDate(),
          }
        }
      },
      close() {
        this.formData = {}
        this.tableData = []
        this.footer = true
        this.$bus.$emit('updateMsg', 0)
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

            const res = await saveXmyqsqOrupdate({
              ...params,
            })
            if (res.code == 1) {
              this.$baseMessage('保存成功', 'success')
            } else {
              this.$baseMessage(res.msg, 'error')
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
          let res = await deleteXmyqsqatt({ attid: row.attid })
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
       */ async handlePreviewFile(row) {
        const { data } = await getPrivewAttInfo({
          attId: row.attid,
          attType: 2,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url })
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
      handleObject() {
        this.$refs['audiTree'].showEdit()
      },
      getDepartmentInfo(val) {
        const ids = val.map((res) => res.id).toString()
        const names = val.map((res) => res.name).toString()
        this.formData.borgid = ids
        this.formData.borgname = names
      },
      openDep() {
        this.$refs.department.show()
      },
      handleDepartmentSelected(node) {
        this.formData.ssorgid = node.id
        this.formData.ssorgname = node.label
      },
      openPerson(type) {
        this.personType = type
        this.$refs.manage.showEdit()
      },
      getChildlistPro(val) {
        if (this.personType == 'zzname') {
          this.formData.zzstaffid = val[0].staffid
          this.formData.zzname = val[0].realname
        } else if (this.personType == 'zsname') {
          this.formData.zsname = val[0].realname
          this.formData.zsstaffid = val[0].staffid
        } else {
          this.formData.zyname = val[0].realname
          this.formData.zystaffid = val[0].staffid
        }
      },
      handlePlaningModalSelected(val) {
        console.log(val, 'val')
        this.formData.xmbh = val[0].qdcode
        this.formData.xmname = val[0].projectName
        this.formData.ssdate = val[0].planStarttime
        this.formData.zsname = val[0].zsname
        this.formData.zsstaffid = val[0].zsstaffid
        this.formData.zyname = val[0].fzname
        this.formData.zystaffid = val[0].fzstaffid
        this.formData.borgid = val[0].auditOrgId
        this.formData.borgname = val[0].auditOrgName
        this.formData.projectid = val[0].id
        if (val[0].teams.length > 0) {
          this.formData.zzname = val[0].teams[0].teamLeader.realname
          this.formData.zzstaffid = val[0].teams[0].teamLeader.staffid
        }
      },
      //提交
      async ymsubmit() {
        try {
          this.$refs['ruleForm'].validate(async (valid) => {
            if (valid) {
              this.btnLoading = true
              this.$refs.resubmit.ymsubmit()
            }
          })
        } catch (error) {
          this.btnLoading = false
        }
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
