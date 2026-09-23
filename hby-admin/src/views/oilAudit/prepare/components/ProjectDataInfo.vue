<template>
  <el-dialog
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-row :gutter="14">
      <el-form
        ref="elForm"
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="disabled"
      >
        <el-col :span="12" v-if="footerFlag">
          <el-form-item label="资料编号" prop="projectDatapreId">
            <el-input
              v-model="formData.projectDatapreId"
              clearable
              placeholder="请输入资料编号"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="footerFlag">
          <el-form-item label="资料名称" prop="dataName">
            <el-input
              v-model="formData.dataName"
              clearable
              placeholder="请输入资料名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="footerFlag">
          <el-form-item label="所属项目" prop="projectname">
            <el-input
              v-model="formData.projectname"
              clearable
              placeholder="请输入所属项目"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="">
            <UEditor
              ref="ueditor"
              v-model="formData.dataCapacity"
              :height="300"
              :templates="templates"
              style="margin-left: -100px"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>关联审计模板库</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <el-button type="success" @click="openSJMBKTable">
              添加审计模板库
            </el-button>
          </div>
          <el-table :data="tableData1">
            <!-- <el-table-column align="center" label="选择" prop="name" /> -->
            <el-table-column align="center" label="模板名称" prop="mbname" />
            <el-table-column align="center" label="模板编码 " prop="mbcode" />
            <el-table-column align="center" label="审计类型" prop="audittype" />
            <el-table-column
              align="center"
              label="人员信息"
              prop="createStaff.realname"
            />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row, $index }">
                <el-button type="text" @click="handleEditDelte1(row, $index)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24">
          <el-divider>关联审计经验库</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <el-button type="success" @click="openSJJYKTable">
              添加审计经验库
            </el-button>
          </div>
          <el-table :data="tableData2">
            <!-- <el-table-column align="center" label="选择" prop="name" /> -->
            <el-table-column align="center" label="标题" prop="tatle" />
            <el-table-column align="center" label="编码 " prop="code" />
            <el-table-column
              align="center"
              label="经验类型"
              prop="experiencetype"
            />
            <el-table-column
              align="center"
              label="人员信息"
              prop="createStaff.realname"
            />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row, $index }">
                <el-button type="text" @click="handleEditDelte2(row, $index)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>

        <el-col :span="24" v-if="footerFlag">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24" v-if="footerFlag">
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
                  @click="handleDown(scope.row)"
                >
                  下载
                </el-button>
                <el-button
                  type="text"
                  @click="handlePreview(scope.row)"
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
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer">
      <el-button @click="close">取消</el-button>
      <el-button type="primary" v-if="showFooter" @click="save">确定</el-button>
    </div>
    <!-- <template #footer>
      <el-button @click="close">关 闭</el-button>
    </template> -->
    <RelateSJMBKModal ref="SJMBK" @selected="setTable1" />
    <RelateSJJYKModal ref="SJJYK" @selected="setTable2" />
  </el-dialog>
</template>

<script>
  import { download } from '@/oapi/audit/implement'
  import {
    createProjectDataCode,
    currSsProject,
    dataprojectFileList,
    dataProjectSave,
    deleteProjectFile,
    deleteSJJYKTableData,
    deleteSJMBKTableData,
    getDataprojectView,
    getSJJYKDefaultInfo,
    getSJMBKDefaultInfo,
  } from '@/oapi/audit/preparation'
  import UEditor from '@/components/UEditor'
  import store from '@/store'
  import RelateSJJYKModal from './relateSJJYKModal.vue'
  import RelateSJMBKModal from './relateSJMBKModal.vue'
  const { baseURL } = require('@/config')
  import { getPrivewAttInfo } from '@/oapi/contract/manage'
  export default {
    name: 'ProjectDataInfo',
    components: { UEditor, RelateSJMBKModal, RelateSJJYKModal },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        baseApi: baseURL,
        api: '/audit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        footerFlag: false,
        showFooter: false,
        formData: {
          dataCapacity: '',
          // dataDate: '',
          id: '',
          dataName: '',
          projectDatapreId: '',
          projectid: '',
          projectname: '',
          //所属字段后端接口没有
        },
        fileList: [],
        tableDataFile: [],
        fileIds: [],
        templates: [],
        disabled: false,
        content: '',
        rules: {
          projectDatapreId: [
            { required: true, message: '请输入资料编号', trigger: 'change' },
          ],
          dataName: [
            { required: true, message: '请输入资料名称', trigger: 'change' },
          ],
          projectname: [
            { required: true, message: '请输入所属项目', trigger: 'change' },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        tableData1: [], //关联审计模板库
        tableData2: [], //关联审计经验库
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      async showEdit(row, flag) {
        this.dialogFormVisible = true
        if (!row) {
          this.getProject()
          this.disabled = false
          this.footerFlag = true
          this.showFooter = true
          createProjectDataCode().then((res) => {
            this.$set(
              this.formData,
              'projectDatapreId',
              res.data.autoCode.toString()
            )
          })
        } else if (row && flag) {
          let res = await getDataprojectView({
            dataId: row.id,
          })
          this.disabled = false
          this.title = '修改'
          //获取附件列表接口
          let resF = await dataprojectFileList({
            dataId: row.id,
          })
          //获取关联审计模板库接口
          let res11 = await getSJMBKDefaultInfo({
            dataperid: row.id,
          })
          this.tableData1 = res11.data.mblist
          //获取关联审计模板库接口
          let res22 = await getSJJYKDefaultInfo({
            dataperid: row.id,
          })
          this.tableData2 = res22.data.jyklist
          //回填上传文件表格
          const arr = resF.data.data
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
          this.$nextTick(() => {
            this.formData = {
              id: res.data.Doubtfulpoint.id,
              projectDatapreId: res.data.Doubtfulpoint.projectDatapreId,
              projectid: res.data.Doubtfulpoint.projectid,
              dataName: res.data.Doubtfulpoint.dataName,
              projectname: res.data.Doubtfulpoint.projectname,
              username: res.data.Doubtfulpoint.username,
              dataCapacity: res.data.Doubtfulpoint.dataCapacity,
            }
          })

          this.disabled = false
          this.footerFlag = true
          this.showFooter = true
        } else {
          this.title = '详情'
          this.disabled = true
          this.footerFlag = true
          this.showFooter = false
          let res = await getDataprojectView({
            dataId: row.id,
          })
          //获取关联审计模板库接口
          let res11 = await getSJMBKDefaultInfo({
            dataperid: row.id,
          })
          this.tableData1 = res11.data.mblist
          //获取关联审计模板库接口
          let res22 = await getSJJYKDefaultInfo({
            dataperid: row.id,
          })
          this.tableData2 = res22.data.jyklist

          this.formData = res.data.Doubtfulpoint
        }
      },
      async getProject() {
        const { data, code } = await currSsProject({})
        if (code == 1) {
          this.formData.projectname = data.pj.prjoectName
          this.formData.projectid = data.pj.projectid
        }
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
      handleDeleteFile(index, row) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          let res = await deleteProjectFile({ attId: row.attid })
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
      save() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            const ids1 =
              this.tableData1 && this.tableData1.map((res) => res.mbid)
            const ids2 =
              this.tableData2 && this.tableData2.map((res) => res.jykid)
            let { msg, code } = await dataProjectSave({
              id: this.formData.id,
              dataName: this.formData.dataName,
              projectDatapreId: this.formData.projectDatapreId,
              projectid: this.formData.projectid,
              projectname: this.formData.projectname,
              dataCapacity: this.formData.dataCapacity,
              attids: this.fileIds.toString() || '',
              mbids: ids1 ? ids1.toString() : '',
              jykids: ids2 ? ids2.toString() : '',
            })
            if (code == 1) {
              this.$baseMessage('成功', 'success', 'vab-hey-message-success')
              this.close()
              this.$emit('fetch-data')
            } else {
              this.$baseMessage(msg, 'error', 'vab-hey-message-error')
            }
          }
        })
      },
      close() {
        this.$refs['elForm'].resetFields()
        this.formData = this.$options.data().formData
        this.dialogFormVisible = false
        this.tableData1 = []
        this.tableData2 = []
      },
      openSJMBKTable() {
        this.$refs['SJMBK'].showEdit()
      },
      setTable1(e) {
        this.tableData1 = e
      },
      handleEditDelte1(row, index) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          //删除对应的id
          this.tableData1.splice(index, 1)
          deleteSJMBKTableData({ mbid: row.mbid })
          this.$message({
            type: 'success',
            message: '删除成功!',
          })
        })
      },
      openSJJYKTable() {
        this.$refs['SJJYK'].showEdit()
      },
      setTable2(e) {
        this.tableData2 = e
      },
      handleEditDelte2(row, index) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          //删除对应的id
          this.tableData2.splice(index, 1)
          deleteSJJYKTableData({ jykid: row.jykid })
          this.$message({
            type: 'success',
            message: '删除成功!',
          })
        })
      },
      async handlePreview(row) {
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
