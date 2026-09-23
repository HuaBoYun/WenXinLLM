<template>
  <div>
    <el-row :gutter="14">
      <el-form
        ref="elForm"
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="disabled"
      >
        <el-col :span="12">
          <el-form-item
            label="审计项目实施部门"
            label-width="140px"
            prop="department"
          >
            <el-input
              v-model="formData.department"
              disabled
              placeholder="请选择"
              :style="{ width: '80%', marginRight: '10px' }"
            />
            <el-button type="primary" @click="$refs.department.show()">
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="审计项目名称"
            label-width="140px"
            prop="projectName"
          >
            <el-input
              v-model="formData.projectName"
              disabled
              placeholder="请选择"
              :style="{ width: '80%', marginRight: '10px' }"
            />
            <el-button type="primary" @click="handlePlanRelate()">
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="审计实施时间"
            label-width="140px"
            prop="sjsstime"
          >
            <el-date-picker
              v-model="formData.sjsstime"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd HH:mm:ss"
              style="width: 100%"
              placeholder="选择审计实施时间"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="被审计单位全称"
            label-width="140px"
            prop="orgName"
          >
            <el-input
              v-model="formData.orgName"
              disabled
              placeholder="请输入"
              :style="{ width: '80%', marginRight: '10px' }"
            />
            <el-button type="primary" @click="$refs.unit.showEdit()">
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="组长" label-width="140px" prop="teamleader">
            <el-input
              v-model="formData.teamleader"
              clearable
              placeholder="请选择"
              disabled
              :style="{ width: '100%' }"
            />
            <!-- :style="{ width: '80%', marginRight: '10px' }" -->
            <!-- <el-button type="primary" @click="selectPeople('teamleader')">
              选择
            </el-button> -->
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="副组长" label-width="140px" prop="fznames">
            <el-input
              v-model="formData.fznames"
              clearable
              placeholder="请选择"
              disabled
              :style="{ width: '100%' }"
            />
            <!-- :style="{ width: '80%', marginRight: '10px' }" -->
            <!-- <el-button type="primary" @click="selectPeople('teamleader')">
              选择
            </el-button> -->
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="主审" label-width="140px" prop="mainreviewer">
            <el-input
              v-model="formData.mainreviewer"
              clearable
              placeholder="请选择"
              disabled
              :style="{ width: '100%' }"
            />
            <!-- <el-button type="primary" @click="selectPeople('mainreviewer')">
              选择
            </el-button> -->
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="助审" label-width="140px" prop="helpreviewer">
            <el-input
              v-model="formData.helpreviewer"
              clearable
              placeholder="请选择"
              disabled
              :style="{ width: '100%' }"
            />
            <!-- <el-button type="primary" @click="selectPeople('helpreviewer')">
              选择
            </el-button> -->
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="经办人" label-width="140px" prop="operator">
            <el-input
              v-model="formData.operator"
              clearable
              placeholder="请选择"
              disabled
              :style="{ width: '80%', marginRight: '10px' }"
            />
            <el-button type="primary" @click="selectPeople('operator')">
              选择
            </el-button>
          </el-form-item>
        </el-col>

        <!-- <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
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
            :before-upload="handleBeforeUpload"
          >
            <div v-if="!disabled" style="margin-right: 10px">
              <el-button type="success">上传</el-button>
            </div>
          </el-upload>
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
              <template slot-scope="scope">
                <el-button
                  :disabled="false"
                  type="text"
                  @click="handleDownload(scope.row)"
                >
                  下载
                </el-button>
                <el-button
                  type="text"
                  @click="handlePreviewFile(scope.row)"
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
        </el-col> -->
      </el-form>
    </el-row>

    <div slot="footer" style="text-align: right" v-if="!disabled">
      <!-- <el-button @click="close">取消</el-button> -->
      <el-button @click="save" type="primary">确定</el-button>
      <el-button @click="ymsubmit" type="primary" :disabled="btnLoading">
        提交
      </el-button>
    </div>
    <oaList ref="oaList" @selected="handleOA" />
    <!-- 选择人员弹窗 -->
    <!-- <executor-options ref="executor" @selected="handleExecutorSelected" /> -->
    <!-- 部门 -->
    <department-options ref="department" @selected="handleDepartmentSelected" />
    <!-- 单位选择 -->
    <SelectDepartment ref="unit" @submit="handleUnitSelected" />
    <danxuanPerson
      ref="danxuanPerson"
      @projectManage="handleExecutorSelected"
    />
    <ssfaList ref="project" @selected="projectSelect" />
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
  // import Tinymce from '@/components/Tinymce'
  import { getOaurl } from '@/oapi/contract/manage'
  import { download } from '@/oapi/audit/implement'
  import DepartmentOptions from '@/views/oilAudit/report/components/options/department.vue'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import oaList from '@/views/oilAudit/prepare/components/oaList.vue'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import danxuanPerson from '@/components/danxuanPerson.vue'
  import { currSsProject } from '@/oapi/audit/projectData'
  import ssfaList from '@/views/oilAudit/prepare/components/options/ssfaList.vue'
  import {
    createNoticeCode,
    deleteNoticeFile,
    getNoticeDefaultData,
    noticeFileList,
    addNotice,
    noticeaprDisp,
    noticeaprAdd,
  } from '@/oapi/audit/preparation'
  import { implementPlanDetail } from '@/oapi/audit/project'
  import store from '@/store'
  import * as dayjs from 'dayjs'
  const { baseURL } = require('@/config')
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'

  export default {
    name: 'NoticeaprEdit',
    inheritAttrs: false,
    props: [],
    components: {
      oaList,
      danxuanPerson,
      DepartmentOptions,
      SelectDepartment,
      ssfaList,
      Resubmit,
    },
    data() {
      return {
        baseApi: baseURL,
        api: '/oiaudit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        footerFlag: false,
        formData: {
          advicecoed: '',
          advicename: '',
          adviceid: '',
          attids: '',
          helpreviewer: '',
          mainreviewer: '',
          operator: '',
          projectName: '',
          progectid: '',
          proposal: '',
          sjsstime: '',
          teamleader: '',
          content: '',
          title: '',
          oaid: '',
          h5url: '',
          url: '',
          orgName: '',
          orgid: '',
          department: '',
          orgids: '',
          fznames: '',
          fzstaffids: '',
        },
        templates: [],
        fileList: [],
        tableDataFile: [],
        fileIds: [],
        disabled: false,
        rules: {},
        dialogFormVisible: false,
        title: '',
        adviceid: '',

        rules: {
          advicecoed: [
            { required: true, message: '请输入通知书编号', trigger: 'change' },
          ],
          advicename: [
            { required: true, message: '请输入通知书名称', trigger: 'change' },
          ],
          projectName: [
            { required: true, message: '请选择审计项目', trigger: 'change' },
          ],
          helpreviewer: [
            {
              required: true,
              message: '请选择审计助审',
              trigger: ['change', 'blur'],
            },
          ],
          mainreviewer: [
            {
              required: true,
              message: '请选择审计主审',
              trigger: ['change', 'blur'],
            },
          ],
          operator: [
            {
              required: true,
              message: '请选择经办人',
              trigger: ['change', 'blur'],
            },
          ],
          progectid: [
            {
              required: false,
              message: '请选择审计组长',
              trigger: ['change', 'blur'],
            },
          ],
          proposal: [
            {
              required: true,
              message: '请选择审批意见',
              trigger: 'blur',
            },
          ],
          sjsstime: [
            {
              required: true,
              message: '请选择审计实施时间',
              trigger: ['change', 'blur'],
            },
          ],
          teamleader: [
            {
              required: true,
              message: '请选择审计助审',
              trigger: ['change', 'blur'],
            },
          ],
        },
        selectPeopleType: undefined,
        fromId: '',
        flowtaskinfoflowid: '',
        ymFromId: '',
        status: '',
        fromIdcopy: '',
        btnLoading: false,
      }
    },
    computed: {},
    async created() {},
    mounted() {},
    methods: {
      //提交审批
      async ymsubmit() {
        try {
          this.$refs['elForm'].validate(async (valid) => {
            if (valid) {
              this.btnLoading = true
              this.$refs.resubmit.ymsubmit()
            }
          })
        } catch (error) {
          this.btnLoading = false
        }
      },
      selectPeople(type) {
        this.selectPeopleType = type
        this.$refs.danxuanPerson.showEdit()
      },
      // 选择经办人
      handleExecutorSelected(node) {
        if (this.selectPeopleType == 'teamleader') {
          this.$set(this.formData, 'teamleader', node[0].realname)
        } else if (this.selectPeopleType == 'mainreviewer') {
          this.$set(this.formData, 'mainreviewer', node[0].realname)
        } else if (this.selectPeopleType == 'helpreviewer') {
          this.$set(this.formData, 'helpreviewer', node[0].realname)
        } else if (this.selectPeopleType == 'operator') {
          this.$set(this.formData, 'operator', node[0].realname)
        }
        this.$forceUpdate()
      },
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      async showEdit(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status
      ) {
        if (formId) {
          this.disabled = true
          const formRes = await noticeaprDisp({
            adviceid: formId,
          })
          if (formRes && formRes.data && formRes.data.advice) {
            Object.assign(this.formData, formRes.data.advice)
            this.formData.projectName = formRes.data.project.projectName
            this.formData.sjsstime = dayjs(this.formData.sjsstime).format(
              'YYYY-MM-DD HH:mm:ss'
            )
            this.formData.orgName = formRes.data.advice.organization?.orgname
            this.formData.orgids = formRes.data.advice.organization?.orgid
          }
        }
        if (title == 'edit') {
          this.disabled = false
        }
        this.fromId = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
      },
      // handleSuccess(response, file, fileList) {
      //   if (file.response.result == '200') {
      //     file.createPerson = JSON.parse(
      //       localStorage.getItem('userInfo')
      //     ).realname
      //     let arr = file.response.data
      //     const arr1 = {
      //       name: arr.attname,
      //       size: arr.attsize,
      //       createPerson: arr.uploader,
      //       attid: arr.attid,
      //     }
      //     this.tableDataFile.push(arr1)

      //     this.fileList = fileList
      //     let fileArr = []
      //     this.fileList.forEach((item) => {
      //       fileArr.push(item.response.data.attid)
      //     })
      //     this.fileIds = [...this.fileIds, ...fileArr]
      //     this.$baseMessage(file.response.msg, 'success')
      //   } else {
      //     this.$baseMessage(file.response.msg, 'error')
      //   }
      // },
      // handleDeleteFile(index, row) {
      //   this.$confirm('此操作将永久删除, 是否继续?', '提示', {
      //     confirmButtonText: '确定',
      //     cancelButtonText: '取消',
      //     type: 'warning',
      //   }).then(async () => {
      //     let res = await deleteNoticeFile({ attId: row.attid })
      //     if (res.msg === '成功') {
      //       this.$message({
      //         type: 'success',
      //         message: '删除成功!',
      //       })
      //       this.fileIds.splice(index, 1)
      //       this.tableDataFile.splice(index, 1)
      //     } else {
      //       this.$message({
      //         type: 'info',
      //         message: '已取消删除',
      //       })
      //     }
      //   })
      // },
      close() {
        this.$refs['elForm'].resetFields()
        this.dialogFormVisible = false
        this.$bus.$emit('updateMsg', 0)
      },
      save() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            let fileIds = ''
            this.tableDataFile.map((item) => {
              fileIds += item.attid
              fileIds += ','
            })
            fileIds = fileIds.substring(0, fileIds.length - 1)
            delete this.formData.organization
            delete this.formData.tblNoteAtts
            let { code, msg } = await noticeaprAdd({
              ...this.formData,
              adviceid: this.formData.adviceid,
              attids: fileIds || '',
            })
            if (code == 1) {
              this.$baseMessage(
                '保存成功',
                'success',
                'vab-hey-message-success'
              )
              this.dialogFormVisible = false
              this.$emit('fetch-data')
            } else {
              this.$baseMessage(msg, 'error', 'vab-hey-message-error')
            }
          }
        })
      },
      // async handleDownload(row) {
      //   const res = await download({ attId: row.attid })
      //   this.downloadFileByBlob(res, row.name)
      // },
      // /**
      //  * @description: 文件预览
      //  * @param {*} row 文件信息
      //  * @return {*}
      //  */
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
      // downloadFileByBlob(blob, fileName = 'file') {
      //   let blobUrl = window.URL.createObjectURL(blob)
      //   let link = document.createElement('a')
      //   link.download = fileName || 'defaultName'
      //   link.style.display = 'none'
      //   link.href = blobUrl
      //   // 触发点击
      //   document.body.appendChild(link)
      //   link.click()
      //   // 移除
      //   document.body.removeChild(link)
      // },
      openOA() {
        this.$refs.oaList.show()
      },
      handleOA(e) {
        this.formData.title = e[0].subject
        this.formData.oaid = e[0].id
        this.formData.h5url = e[0].h5Url
        this.formData.url = e[0].url
      },
      async detail() {
        const res = await getOaurl()
        this.ticket = res.data.ticket
        this.oaurl = res.data.oaurl.substring(0, res.data.oaurl.length - 1)
        window.open(`${this.oaurl}${this.formData.url}&ticket=${this.ticket}`)
      },
      // 部门
      handleDepartmentSelected(node) {
        // //单位（科室）名称
        this.$set(this.formData, 'department', node.label)
      },
      /**
       * @description: 选择单位
       * @param {*}
       * @return {*}
       */
      handleUnitSelected(node) {
        console.log('node', node)
        this.formData.orgName = node.label
        this.formData.orgids = node.id
        // this.formData.unitRangeId = node.id
      },
      //选择项目
      handlePlanRelate() {
        this.$refs.project.showEdit()
      },
      projectSelect(val) {
        console.log(val, 'val')
        let data = val[0]
        this.$set(this.formData, `progectid`, data.id)
        this.$set(this.formData, `projectName`, data.projectName)
        this.$set(this.formData, `mainreviewer`, data.xmqd?.zsname)
        this.$set(this.formData, `zsstaffid`, data.xmqd?.zsstaffid)
        this.$set(this.formData, `helpreviewer`, data.xmqd?.assistApprover)
        this.$set(
          this.formData,
          `assistapproverid`,
          data.xmqd?.assistApproverId
        )
        this.$set(this.formData, `fzstaffids`, data.xmqd?.fzzStafffId)
        this.$set(this.formData, `fznames`, data.xmqd?.fzzName)
        implementPlanDetail({
          id: val[0].id,
        }).then(async (res) => {
          this.$set(
            this.formData,
            `teamleader`,
            res.data.data.teams[0].teamLeader.realname
          )
          this.$set(
            this.formData,
            `zcstaffid`,
            res.data.data.teams[0].teamLeader.staffid
          )
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

  ::v-deep .el-textarea__inner {
    resize: none;
  }
</style>
