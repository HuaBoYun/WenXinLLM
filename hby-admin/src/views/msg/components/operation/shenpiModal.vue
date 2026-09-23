<template>
  <div>
    <el-dialog
      v-if="dialogFormVisible"
      :append-to-body="true"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
      :close-on-click-modal="false"
    >
      <el-tabs v-show="dialogFormVisible" v-model="activeName" type="card">
        <el-tab-pane label="基本信息" name="first">
          <h3>通知书-审批</h3>
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
                  label="审计通知书编号"
                  label-width="140px"
                  prop="advicecoed"
                >
                  <el-input
                    v-model="formData.advicecoed"
                    clearable
                    placeholder="请输入审计通知书编号"
                    :style="{ width: '100%' }"
                    :disabled="!cloudEdit"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item
                  label="审计通知书名称"
                  label-width="140px"
                  prop="advicename"
                >
                  <el-input
                    v-model="formData.advicename"
                    clearable
                    placeholder="请输入审计通知书名称"
                    :style="{ width: '100%' }"
                    :disabled="!cloudEdit"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="编辑器" prop="content">
                  <UEditor
                    ref="ueditor"
                    v-model="formData.content"
                    :height="300"
                    :templates="templates"
                    :disabled="!UEditorCloudEdit"
                  />
                </el-form-item>
              </el-col>

              <el-col :span="24">
                <el-divider>文件上传</el-divider>
              </el-col>

              <el-upload
                style="text-align: right; margin-bottom: 5px"
                class="upload-demo"
                :action="baseApi + api"
                :headers="headers"
                :on-success="handleSuccess"
                :show-file-list="false"
                multiple
                :file-list="fileList"
                :disabled="!cloudEdit"
                :before-upload="handleBeforeUpload"
              >
                <div style="margin-right: 10px">
                  <el-button type="success" :disabled="!cloudEdit">
                    点击上传
                  </el-button>
                </div>
              </el-upload>
              <el-table :data="tableDataFile">
                <el-table-column align="center" label="附件名称" prop="name" />
                <el-table-column
                  align="center"
                  label="文件大小(KB)"
                  prop="size"
                />
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
                    <el-button type="text" @click="handleDownload(scope.row)">
                      下载
                    </el-button>
                    <el-button
                      :disabled="!cloudEdit"
                      type="text"
                      @click="handleDeleteFile(scope.$index, scope.row)"
                    >
                      删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
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
                  >
                    <template slot-scope="scope">
                      <div>{{ formatDay(scope.row.createDate) }}</div>
                    </template>
                  </el-table-column>
                  >
                  <el-table-column
                    align="center"
                    label="结果"
                    prop="optState"
                  />
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
                    placeholder="请输入审批意见"
                    v-model="formData.optDesc"
                    :disabled="hiddenButton"
                  ></el-input>
                </el-form-item>
              </el-col>

              <el-col :span="24">
                <div v-if="!hiddenButton">
                  <el-button @click="saveInfo" v-if="cloudEdit" type="primary">
                    保存
                  </el-button>
                  <el-button
                    type="primary"
                    v-for="(value, index) in buttonList"
                    :key="index"
                    @click="handleSubmit(index)"
                  >
                    {{ value ? value : '提交' }}
                  </el-button>
                </div>
              </el-col>
            </el-form>
          </el-row>
        </el-tab-pane>
        <el-tab-pane label="审批查看" name="second">
          <el-row :gutter="24">
            <el-col :span="24">
              <img
                alt="审批图"
                :src="imgurl"
                style="margin-bottom: 20px; width: 100%"
              />
            </el-col>
          </el-row>
        </el-tab-pane>
      </el-tabs>

      <!-- <div slot="footer">
          <el-button v-for="(value ,index) in buttonList" :key="index" @click="handleSubmit">{{value}}</el-button>
    </div> -->
      <!-- <template #footer>
      <el-button @click="close">关 闭</el-button>
    </template> -->
    </el-dialog>
  </div>
</template>
<script>
  import { download } from '@/api/audit/implement'
  import {
    deleteNoticeFile,
    noticeAdd,
    noticeFileList,
  } from '@/api/audit/preparation'
  import { handleButtonClick, handleHanlde } from '@/api/workbench/auditTools'
  import UEditor from '@/components/UEditor'
  import { baseURL } from '@/config'
  import store from '@/store'
  import { formatDay } from '@/utils/index'
  export default {
    name: 'shenpiModal',
    components: { UEditor },
    props: ['UEditorCloudEdit'],
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
        dialogFormVisible: false,
        formData: {
          optDesc: '',
          content: '',
        },
        handleData: {},
        rules: {
          advicecoed: [
            { required: true, message: '请输入通知书编号', trigger: 'change' },
          ],
          advicename: [
            { required: true, message: '请输入通知书名称', trigger: 'change' },
          ],
          optDesc: [
            { required: true, message: '请输入审批意见', trigger: 'change' },
          ],
        },
        templates: [],
        buttonList: [],
        rowsData: {},
        activeName: 'first',
        imgurl: '',
        cloudEdit: false, //是否可编辑
        fileList: [],
        tableDataFile: [],
        fileIds: [],
        disabled: false,
        aoptionList: [],
        hiddenButton: false,
        formatDay,
      }
    },
    // watch: {
    //   'formData.content'(val) {
    //
    //
    //     if (this.$refs['ueditor'].editor.openTemplate) {
    //       this.$refs['ueditor'].editor.openTemplate = false
    //       let s = val
    //       const arr = [
    //         ['$[contract.contractno]', 'contractno'],
    //         ['$[contract.contractname]', 'contractname'],
    //         ['$[contract.contractamount]', 'contractmoney'],
    //         ['$[contract.contractItem]', 'contractitem'],
    //         ['$[contract.executor]', 'realname'],
    //         ['$[contract.rmbinwords]', 'hzsumowing'],

    //         ['$[counterpart.coupersion]', 'counterpartcode'],
    //         ['$[counterpart.personincharge]', 'contractbd'],
    //         ['$[counterpart.counterpartHank]', 'bankkhyh'],
    //         ['$[counterpart.counumber]', 'counterpartno'],
    //         ['$[counterpart.couname]', 'budgetname'],
    //         ['$[counterpart.couaddress]', 'counterpartaddress'],
    //         ['$[counterpart.coupersion]', 'contacts'],
    //         ['$[counterpart.contactsPhone]', 'contactsphone'],
    //         ['$[counterpart.counterpartHankAccount]', 'bankaccount'],
    //         ['$[counterpart.legarepresentative]', 'contacts'],
    //         ['$[counterpart.pctelephonenumber]', 'contractzd'],
    //         // ['$[counterpart.taxpayeridentification]', 'hzsumowing'], //纳税人识别号
    //       ]
    //       arr.forEach((i) => {
    //         if (this.formData[i[1]]) {
    //           s = s.replace(i[0], this.formData[i[1]])
    //         }
    //       })
    //       this.formData.content = s
    //     }
    //   },
    // },
    methods: {
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      async showEdit(row, names) {
        if (names === '查看') {
          this.cloudEdit = false
          this.hiddenButton = true
        }
        this.dialogFormVisible = true
        const ids = row.cyurl.split('=')[1]
        let res = await handleHanlde({
          adviceid: ids,
          cyId: row.cyid,
          taskId: row.taskid,
        })

        this.imgurl =
          baseURL + `/audit/nbsjapproval/picture?taskId=` + row.taskid
        let userInfo = JSON.parse(localStorage.getItem('userInfo'))
        // 当viewOppsiteProcessInfo接口中的cystaffid与获取用户信息中的staffid相等且cystate等于需调整 基本信息改成可编辑 编号不可编辑
        if (
          row.cystaffid == userInfo.staffid &&
          res.data.cy.cystate == '需调整'
        ) {
          this.cloudEdit = true
        } else {
          this.cloudEdit = false
        }
        this.formData = {
          advicecoed: res.data.advice.advicecoed,
          advicename: res.data.advice.advicename,
          adviceid: res.data.advice.adviceid,
          content: res.data.advice.content,
          optDesc: '',
        }
        this.aoptionList = res.data.aoptionList || []
        this.buttonList = res.data.btnList ? res.data.btnList : ['提交']
        this.handleData = res.data.advice
        this.rowsData = row

        //编辑按钮
        let res1 = await noticeFileList({
          adviceid: ids,
        })
        //回填上传文件表格
        const arr = res1.data.data
        const arr1 = arr.map((item) => {
          return {
            ...item,
            name: item.attname,
            size: item.attsize,
            createPerson: item.uploader,
          }
        })
        const arr2 = arr.map((res) => {
          return res.attid
        })
        //收集id
        this.fileIds = arr2
        this.tableDataFile = arr1
      },
      handleSubmit(index) {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            const params = {
              adviceid: this.handleData.adviceid,
              optDesc: this.formData.optDesc,
              cyId: this.rowsData.cyid,
              taskId: this.rowsData.taskid,
              transition: this.buttonList[index] || '提交',
            }
            let { msg } = await handleButtonClick({
              ...params,
            })
            if (msg === '成功') {
              this.$message.success('办理成功')
              this.dialogFormVisible = false
              this.$emit('reload-data')
            } else {
              this.$message.error('办理失败')
              this.$emit('reload-data')
            }
          }
        })
      },
      close() {
        this.dialogFormVisible = false
        this.activeName = this.$options.data().activeName
      },
      handleSuccess(response, file, fileList) {
        if (file.response.result == '200') {
          file.createPerson = JSON.parse(
            localStorage.getItem('userInfo')
          ).realname
          let arr = file.response.data
          const arr1 = {
            name: arr.attname,
            size: arr.attsize,
            createPerson: arr.uploader,
            attid: arr.attid,
          }
          this.tableDataFile.push(arr1)

          this.fileList = fileList
          let fileArr = []
          this.fileList.forEach((item) => {
            fileArr.push(item.response.data.attid)
          })
          this.fileIds = [...this.fileIds, ...fileArr]
          this.$baseMessage(file.response.msg, 'success')
        } else {
          this.$baseMessage(file.response.msg, 'error')
        }
      },
      handleDeleteFile(index) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          let res = await deleteNoticeFile({ attId: row.attid })
          if (res.msg === '成功') {
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
            this.fileIds.splice(index, 1)
            this.tableDataFile.splice(index, 1)
          } else {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          }
        })
      },
      async handleDownload(row) {
        const res = await download({ attid: row.attid })
        this.downloadFileByBlob(res, row.name)
      },
      downloadFileByBlob(blob, fileName = 'file') {
        let blobUrl = window.URL.createObjectURL(blob)
        let link = document.createElement('a')
        link.download = fileName || 'defaultName'
        link.style.display = 'none'
        link.href = blobUrl
        // 触发点击
        document.body.appendChild(link)
        link.click()
        // 移除
        document.body.removeChild(link)
      },
      saveInfo() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            let res = await noticeAdd({
              advicecoed: this.formData.advicecoed,
              adviceid: this.formData.adviceid,
              advicename: this.formData.advicename,
              content: this.formData.content,
              attids: this.fileIds.toString() || '',
            })
          }
        })
      },
    },
  }
</script>
