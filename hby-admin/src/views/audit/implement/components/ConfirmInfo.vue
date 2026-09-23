<template>
  <el-dialog
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
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
          <el-form-item label="确认书编号" label-width="140px" prop="factcode">
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
          <el-form-item label="复核人" label-width="140px" prop="fhstaffname">
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
          <el-form-item label="拟稿时间" label-width="140px" prop="createtime">
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
          <el-form-item label="事实描述" label-width="140px" prop="describe">
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
                <el-button type="text" @click="handleEditDelte(row)">
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
                <el-button type="text" @click="handleDown(row)">下载</el-button>
                <el-button type="text" @click="handleDelete(row)" v-if="footer">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
      <projectManage
        ref="manage"
        :modal="false"
        @reviewTypeSelect="reviewTypeSelect"
      />
    </el-row>
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </div>
    <template #footer v-if="footer">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </template>
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
    getProjectName,
  } from '@/api/audit/implement'
  import store from '@/store'
  import { formatDate } from '@/utils/index'
  import ConfirmTable from './ConfirmTable'
  const { baseURL } = require('@/config')
  export default {
    name: 'ProjectDataInfo',
    components: { projectManage, ConfirmTable },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        baseApi: baseURL,
        api: '/audit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        formData: {
          projectid: undefined,
          factname: undefined,
          factcode: undefined,
          realname: undefined,
          createtime: undefined,
          factstaffid: undefined,
        },
        footer: true,
        tableData: [],
        tableData2: [],
        rules: {
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
      }
    },
    methods: {
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      async handleEditDelte(row) {
        let copyData = [...this.tableData2]
        let index = 0
        await copyData.map((item, i) => {
          if (item.questionId === row.questionId) {
            index = i
          }
        })
        copyData.splice(index, 1)
        this.tableData2 = copyData
      },
      projectManager1() {
        this.$refs['manage'].showEdit('fhstaff')
        this.$refs['ruleForm'].clearValidate()
      },
      projectManager2() {
        this.$refs['manage'].showEdit('factstaff')
        this.$refs['ruleForm'].clearValidate()
      },
      reviewTypeSelect(e) {
        this.$set(this.formData, `${e.reviewType}name`, e.id[0].realname)
        this.$set(this.formData, `${e.reviewType}id`, e.id[0].staffid)
        this.$forceUpdate()
      },
      setTable2(current) {
        let copData = [...this.tableData2]
        if (!this.tableData2.length) {
          this.tableData2 = current
        } else {
          const tablearr = this.tableData2.map((item) => item.questionId)
          let arr = [...copData]
          current.map((item) => {
            if (!tablearr.includes(item.questionId)) {
              arr.push(item)
            }
          })

          this.tableData2 = arr
        }
      },
      openTable() {
        this.$refs['table'].showEdit()
      },
      /**
       * @description: 外部打开内部dialog
       * @param {*} title 表单类型
       * @param {*} row 编辑、详情带入的数据
       * @return {*}
       */      
      async showEdit(title, row) {
        this.dialogFormVisible = true
        // this.$refs['ruleForm'].resetFields()
        this.tableData2 = []
        this.formData = {}

        if (row) {
          this.formData = row.sheet
          this.getFileList(row.sheet.factid)
          this.getTable(row.sheet.factid)
        } else {
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.createstaffname = userInfo.realname
          this.formData.createstaffid = userInfo.staffid
          this.formData.createtime = formatDate(new Date())
        }
        const res = await getProjectName()

        if (res) {
          this.$set(this.formData, 'projectCode', res.data.pj.projectCode)
          this.$set(this.formData, 'prjoectName', res.data.pj.prjoectName)
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else {
          this.title = '新增'
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
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.dialogFormVisible = false
        this.formData = {}
        this.tableData = []
        this.footer = true
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let attids = ''
            this.tableData.map((item) => {
              attids += item.attid
              attids += ','
            })
            let questionIds = ''
            this.tableData2.map((item) => {
              questionIds += item.questionId
              questionIds += ','
            })
            attids = attids.substring(0, attids.length - 1)

            let obj = {
              ...this.formData,
            }
            delete obj.createtime
            delete obj.prjoectName
            delete obj.projectCode
            delete obj.tblNbsjProject
            delete obj.tblStaffByFactstaffid

            const data = await confirmSave({
              ...obj,
              // createstaffid,
              attids,
              questionIds,
            })
            if (data.code === 1) {
              this.$baseMessage('保存成功', 'success')
              this.$emit('fetch-data')
              this.close()
            }
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
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */      
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
