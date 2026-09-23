<template>
  <el-dialog
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-tabs v-model="activeName" type="card">
      <el-tab-pane label="基本信息" name="first">
        <el-row :gutter="24">
          <el-form
            ref="ruleForm"
            label-width="100px"
            :model="formData"
            :rules="rules"
            size="mini"
          >
            <el-col :span="12">
              <el-form-item label="项目编号" label-width="140px">
                <el-input
                  v-model="formData.projectCode"
                  clearable
                  placeholder="请输入项目编号"
                  :style="{ width: '100%' }"
                  :disabled="true"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="项目名称" label-width="140px">
                <el-input
                  v-model="formData.prjoectName"
                  clearable
                  placeholder="请输入项目名称"
                  :style="{ width: '100%' }"
                  :disabled="true"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="确认书编号"
                label-width="140px"
                prop="factcode"
              >
                <el-input
                  v-model="formData.factcode"
                  clearable
                  placeholder="请输入确认书编号"
                  :style="{ width: '100%' }"
                  :disabled="!footer"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="拟稿人"
                label-width="140px"
                prop="createstaffname"
              >
                <el-input
                  v-model="formData.createstaffname"
                  clearable
                  placeholder="请输入拟稿人"
                  :style="{ width: '100%' }"
                  disabled
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="复核人"
                label-width="140px"
                prop="fhstaffname"
              >
                <el-input
                  v-model="formData.fhstaffname"
                  clearable
                  placeholder="请选择复核人"
                  style="width: 260px"
                  disabled
                />
                <el-button
                  @click="projectManager1"
                  style="margin-left: 10px"
                  type="primary"
                  :disabled="!footer"
                >
                  选择
                </el-button>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="事实确认人"
                label-width="140px"
                prop="factstaffname"
              >
                <el-input
                  v-model="formData.factstaffname"
                  clearable
                  placeholder="请选择事实确认人"
                  style="width: 260px"
                  disabled
                />
                <el-button
                  @click="projectManager2"
                  style="margin-left: 10px"
                  type="primary"
                  :disabled="!footer"
                >
                  选择
                </el-button>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="拟稿时间"
                label-width="140px"
                prop="createtime"
              >
                <el-date-picker
                  style="width: 100%"
                  v-model="formData.createtime"
                  placeholder="选择拟稿时间"
                  type="date"
                  format="yyyy-MM-dd hh:mm:ss"
                  value-format="yyyy-MM-dd hh:mm:ss"
                  disabled
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item
                label="事实描述"
                label-width="140px"
                prop="describe"
              >
                <el-input
                  v-model="formData.describe"
                  type="textarea"
                  clearable
                  placeholder="请输入事实描述"
                  :style="{ width: '100%' }"
                  :disabled="!footer"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-divider>审计发现</el-divider>
            </el-col>
            <el-col :span="24">
              <div style="text-align: right; margin-bottom: 5px">
                <el-button type="success" v-if="footer" @click="openTable">
                  添加审计发现
                </el-button>
              </div>
              <el-table :data="tableData2">
                <!-- <el-table-column align="center" label="选择" prop="name" /> -->
                <el-table-column
                  align="center"
                  label="所属底稿编号"
                  prop="nbsjSheet.sheetCode"
                />
                <el-table-column
                  align="center"
                  label="审计事项"
                  prop="nbsjSheet.auditDiscoverable"
                />
                <el-table-column
                  align="center"
                  label="审计发现"
                  prop="nbsjSheet.auditDiscoverable"
                />
                <el-table-column
                  align="center"
                  label="操作"
                  show-overflow-tooltip
                  width="120"
                >
                  <template #default="{ row }">
                    <el-button
                      type="text"
                      @click="handleEdit2(row)"
                      :disabled="!footer"
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
              <div style="text-align: right; margin-bottom: 5px" v-if="footer">
                <el-upload
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
                </el-upload>
              </div>
              <el-table :data="tableData">
                <el-table-column
                  align="center"
                  label="附件名称"
                  prop="attname"
                />
                <el-table-column
                  align="center"
                  label="文件大小(KB)"
                  prop="attsize"
                />
                <el-table-column
                  align="center"
                  label="创建人"
                  prop="uploader"
                />
                <el-table-column
                  align="center"
                  label="操作"
                  show-overflow-tooltip
                  width="120"
                >
                  <template #default="{ row }">
                    <el-button type="text" @click="handleDown(row)">
                      下载
                    </el-button>
                    <el-button
                      type="text"
                      @click="handleDelete(row)"
                      :disabled="!footer"
                    >
                      删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-col>
            <el-col
              :span="24"
              v-if="aoptionList.length"
              style="margin-bottom: 16px"
            >
              <h3>审批意见</h3>
              <el-table :data="aoptionList">
                <el-table-column
                  align="center"
                  label="审批人"
                  width="200px"
                  prop="staffidName"
                />
                <el-table-column align="center" label="意见" prop="optDesc" />
                <el-table-column
                  align="center"
                  label="时间"
                  prop="createDate"
                  :formatter="
                    (e) => {
                      return dayjs(e.createDate).format('YYYY-MM-DD HH:mm:ss')
                    }
                  "
                />
                <el-table-column align="center" label="结果" prop="optState" />
              </el-table>
            </el-col>

            <el-col :span="24">
              <el-divider>审批意见</el-divider>
            </el-col>
            <el-col :span="24">
              <el-form-item label="审批意见" prop="optDesc">
                <el-input
                  type="textarea"
                  :rows="2"
                  :disabled="false"
                  placeholder="请输入审批意见"
                  v-model="formData.optDesc"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-form>
          <projectManage ref="manage" @reviewTypeSelect="reviewTypeSelect" />
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="审批查看" name="second">
        <img :src="imgSrc" alt="" />
      </el-tab-pane>
    </el-tabs>
    <div slot="footer" v-if="activeName === 'first'" style="text-align: left">
      <el-button type="primary" v-if="footer" @click="save">保存</el-button>
      <el-button
        v-for="item in btnList"
        :key="item"
        type="primary"
        @click="handleClick(item)"
      >
        {{ item ? item : '提交' }}
      </el-button>
    </div>
    <ConfirmTable ref="table" @selected="setTable2" />
  </el-dialog>
</template>

<script>
  import projectManage from '@/views/audit/project/components/formComponents/projectManage.vue'

  import {
    confirmationFileList,
    confirmationQuestionLink,
    confirmSave,
    deleteFile,
    download,
    getDetailsById,
    savedealTblNbsjFactbookApporval,
  } from '@/api/audit/implement'
  import { baseURL } from '@/config'
  import store from '@/store'
  import { paramObj } from '@/utils/index'
  import ConfirmTable from '@/views/audit/implement/components/ConfirmTable'
  import * as dayjs from 'dayjs'
  export default {
    name: 'ProjectDataInfo',
    components: { projectManage, ConfirmTable },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        baseApi:
          process.env.NODE_ENV === 'development'
            ? '/vab-mock-server/audit'
            : process.env.VUE_APP_BASE_API,
        api: '/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        baseURL: baseURL,
        formData: {
          projectid: undefined,
          factname: undefined,
          factcode: undefined,
          realname: undefined,
          createtime: undefined,
          factstaffid: undefined,
        },
        dayjs: dayjs,
        footer: true,
        tableData: [],
        tableData2: [],
        rules: {
          optDesc: [
            { required: true, message: '请输入审批意见', trigger: 'change' },
          ],
          describe: [
            {
              required: true,
              message: '请输入事实描述',
              trigger: 'blur',
            },
          ],
          factstaffname: [
            {
              required: true,
              message: '请选择拟稿人',
              trigger: 'blur',
            },
          ],
          factname: [
            {
              required: true,
              message: '请输入项目名称',
              trigger: 'blur',
            },
          ],
          factcode: [
            {
              required: true,
              message: '请输入所属项目',
              trigger: 'blur',
            },
          ],
          factstaffname: [
            {
              required: true,
              message: '请选择事实确认',
              trigger: 'blur',
            },
          ],
          fhstaffname: [
            {
              required: true,
              message: '请选择复核人',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        taskId: '',
        cyObj: {},
        btnList: [],
        activeName: 'first',
        imgSrc: '',
        factid: '',
        aoptionList: [],
      }
    },
    methods: {
      async getImg(taskId) {
        this.imgSrc =
          await `${this.baseURL}/audit/nbsjapproval/picture?taskId=${taskId}`
      },
      async handleClick(item) {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const res = await savedealTblNbsjFactbookApporval({
              cyId: this.cyObj.cyid,
              factid: this.factid,
              optDesc: this.formData.optDesc,
              taskId: this.taskId,
              transition: item || '提交',
            })
            if (res.data === '成功') {
              this.$message.success(res.msg)
            } else {
              this.$message.error(res.msg)
            }
            this.$emit('fetch-data')
            this.close()
          } else {
            return false
          }
        })
      },
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      projectManager1() {
        this.$refs['manage'].showEdit('fhstaff')
      },
      projectManager2() {
        this.$refs['manage'].showEdit('factstaff')
      },
      reviewTypeSelect(e) {
        this.$set(this.formData, `${e.reviewType}name`, e.id[0].realname)
        this.$set(this.formData, `${e.reviewType}id`, e.id[0].staffid)
      },
      setTable2(current) {
        this.tableData2.push(current)
      },
      openTable() {
        this.$refs['table'].showEdit()
      },
      async showEdit(title, row) {
        this.dialogFormVisible = true

        this.factid = paramObj(row.cyurl).spid
        await this.getImg(row.taskid)
        const res = await getDetailsById({
          cyId: row.cyid,
          taskId: row.taskid,
          factid: this.factid,
        })
        this.formData = res.data.fact
        this.formData.prjoectName = res.data.project.prjoectName
        this.formData.projectCode = res.data.project.projectCode
        this.cyObj = res.data.cy
        this.taskId = res.data.taskId
        const userInfo = JSON.parse(localStorage.getItem('userInfo'))
        this.formData.createstaffname = userInfo.realname
        this.formData.createstaffid = userInfo.staffid
        this.btnList = res.data.btnList ? res.data.btnList : ['提交']
        this.aoptionList = res.data.aoptionList || []
        this.getFileList(this.formData.factid)
        this.getTable(this.formData.factid)
        if (title === '事实确认书') {
          this.title = '事实确认书'
          this.footer = false
        }
        if (
          Number(this.cyObj.cyStaffid) === userInfo.staffid &&
          this.cyObj.cystate === '需调整'
        ) {
          this.footer = true
        }
      },
      async getFileList(factid) {
        const data = await confirmationFileList({ factid })
        this.tableData = data.data.data || []
      },
      async getTable(factid) {
        const data = await confirmationQuestionLink({ factid })
        this.tableData2 = data.data.pageInfo.tlist || []
      },
      close() {
        this.dialogFormVisible = false
        this.formData = {}
        this.tableData = []
        this.footer = true
      },
      save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let attids = ''
            this.tableData.map((item) => {
              attids += item.attid
              attids += ','
            })
            attids = attids.substring(0, attids.length - 1)
            let obj = { ...this.formData }
            delete obj.createtime
            delete obj.projectid
            const data = await confirmSave({
              ...obj,
              factid: this.factid,
              // createstaffid,
              attids,
            })
            if (data.msg == '成功') {
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
        let filename = row.attname
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
      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        this.tableData = list
        await deleteFile({ attId: row.attid })
      },
      handlePreview(file) {},
      handleSuccess(file) {
        if (file.result == '200') {
          let list = this.tableData
          list.push(file.data)
          this.tableData = list
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
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
