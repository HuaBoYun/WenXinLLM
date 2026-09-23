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
        <el-col :span="12" v-if="showMJ">
          <el-form-item
            label="密级"
            prop="secrectLevelId"
            :rules="[
              { required: true, trigger: 'change', message: '请选择密级' },
            ]"
          >
            <el-select
              v-model="formData.secrectLevelId"
              clearable
              placeholder="密级"
              style="width: 100%"
              @change="changeMJ"
            >
              <el-option
                v-for="item in MJoption"
                :key="item.levelId"
                :label="item.levelName"
                :value="item.levelId"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="showMJ">
          <el-form-item label="知悉范围" prop="staffScopeNames">
            <el-input
              v-model="formData.staffScopeNames"
              disabled
              placeholder="请选择知悉范围"
              :style="{ width: '76%' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.ZXPerson.showEdit(formData.secrectLevelId)"
              :disabled="!formData.secrectLevelId || disabled"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider></el-divider>
        </el-col>

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
        <!-- <el-col :span="24">
          <el-divider>关联审计模板库</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <el-button type="success" @click="openSJMBKTable">
              添加审计模板库
            </el-button>
          </div>
          <el-table :data="tableData1">
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
        </el-col> -->

        <el-col :span="24" v-if="footerFlag">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24" v-if="footerFlag">
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
          <el-table :data="tableDataFile">
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
              <template slot-scope="scope">
                <el-button
                  :disabled="false"
                  type="text"
                  @click="handleDowns(scope.row)"
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

    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </el-dialog>
</template>

<script>
  import { download } from '@/api/audit/implement'
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
  } from '@/api/audit/preparation'
  import UEditor from '@/components/UEditor'
  import store from '@/store'
  import RelateSJJYKModal from './relateSJJYKModal.vue'
  import RelateSJMBKModal from './relateSJMBKModal.vue'
  const { baseURL } = require('@/config')
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import ZXPerson from '@/components/selectPerson.vue'
  import { getMJ } from '@/api/setting/mjsz'
  import { hasMJ, couldMJ } from '@/utils'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    name: 'ProjectDataInfo',
    components: {
      UEditor,
      RelateSJMBKModal,
      RelateSJJYKModal,
      ZXPerson,
    },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        baseApi: baseURL,
        importApi: '/audit/auditPlan/mergePlanProjectManageInfoImport',
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
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
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
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
        showMJ: false,
        MJoption: [],
      }
    },
    computed: {
      getFormLevel() {
        if (!this.formData.secrectLevelId) return ''
        const level = this.MJoption.find(
          (item) => item.levelId == this.formData.secrectLevelId
        )
        return level ? level.levelName : ''
      },
    },
    watch: {},
    async created() {
      this.showMJ = couldMJ()
      if (this.showMJ) {
        // 获取密级,菜单id
        const res = await hasMJ('PrepareProjectData')
        this.menuId = res[0].menuid
        // 请求密级下拉数据
        const res2 = await getMJ({ rightId: res[0].menuid })
        this.MJoption = res2.data
      }
    },
    mounted() {},
    methods: {
      changeMJ(selectedValue) {
        // selectedValue 就是选中的 value（即 levelId）
        const selectedItem = this.MJoption.find(
          (item) => item.levelId === selectedValue
        )
        if (selectedItem) {
          const label = selectedItem.levelName
          if (label == '非密' || label == '公开') {
            this.formData.staffScopeNames = '全部人员'
            this.formData.staffScopeIds = ''
          } else {
            this.formData.staffScopeNames = ''
            this.formData.staffScopeIds = ''
          }
        }
      },
      handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.formData.staffScopeIds = ids
        this.formData.staffScopeNames = names
      },
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      /**
       * @description: 外部打开内部dialog
       * @param {*} title 表单类型
       * @param {*} row 编辑、详情带入的数据
       * @return {*}
       */
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
              secrectLevelId: res.data.Doubtfulpoint.secrectLevelId,
              staffScopeNames: res.data.Doubtfulpoint.staffScopeNames,
              staffScopeIds: res.data.Doubtfulpoint.staffScopeIds,
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
      async handleDeleteFile(index, row) {
        let res = await deleteProjectFile({ attId: row.attid })
        if (res.msg === '成功') {
          this.$message({
            type: 'success',
            message: '删除成功!',
          })
          this.fileIds.splice(index, 1)
          this.tableDataFile.splice(index, 1)
        }
      },
      /**
       * @description: 保存表单
       * @return {*}
       */
      save() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            let attids = ''
            this.tableDataFile.map((item) => {
              attids += item.attid
              attids += ','
            })
            attids = attids.substring(0, attids.length - 1)
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
              secrectLevelId: this.formData.secrectLevelId,
              staffScopeNames: this.formData.staffScopeNames,
              staffScopeIds: this.formData.staffScopeIds,
              attids: attids,
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
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
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
        //删除对应的id
        this.tableData1.splice(index, 1)
        deleteSJMBKTableData({ mbid: row.mbid })
        this.$message({
          type: 'success',
          message: '删除成功!',
        })
      },
      openSJJYKTable() {
        this.$refs['SJJYK'].showEdit()
      },
      setTable2(e) {
        this.tableData2 = e
      },
      handleEditDelte2(row, index) {
        //删除对应的id
        this.tableData2.splice(index, 1)
        deleteSJJYKTableData({ jykid: row.jykid })
        this.$message({
          type: 'success',
          message: '删除成功!',
        })
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

        // 获取 el-upload 的 data 参数
        const formData = {
          formlevel: this.getFormLevel,
        }

        // 调用自定义上传函数
        new Promise((resolve, reject) => {
          customUpload({
            baseApi: this.baseApi,
            api: this.api,
            key: window.key,
            iv: window.iv,
            headers: this.headers,
            fileList: fileList, // 使用 fileList 而不是 file
            formData: formData, // 传递额外的表单数据
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
          this.tableDataFile = [...this.tableDataFile, ...file.data]
          // this.tableData = list
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
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
