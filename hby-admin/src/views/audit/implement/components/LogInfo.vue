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
        ref="ruleForm"
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
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
              :disabled="!footer"
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
              :disabled="!formData.secrectLevelId || !footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider></el-divider>
        </el-col>

        <el-col :span="12">
          <el-form-item label="日志名称" prop="reportname">
            <el-input
              v-model="formData.reportname"
              clearable
              placeholder="请输入日志名称"
              :style="{ width: '256px' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="日志时间" prop="reporttime">
            <el-date-picker
              v-model="formData.reporttime"
              placeholder="选择日志时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :disabled="!footer"
              :style="{ width: '256px' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报告类型" prop="reporttype">
            <el-select
              v-model="formData.reporttype"
              placeholder="请选择报告类型"
              :disabled="!footer"
              :style="{ width: '256px' }"
            >
              <el-option label="日报" value="日报" />
              <el-option label="周报" value="周报" />
              <el-option label="总结报告" value="总结报告" />
              <!-- <el-option label="月报" value="月报" />
              <el-option label="季报" value="季报" />
              <el-option label="年报" value="年报" />
              <el-option label="综合" value="综合" /> -->
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报告人" prop="reporter">
            <el-input
              v-model="formData.reporter"
              clearable
              placeholder="请输入报告人"
              :style="{ width: '256px' }"
              :disabled="true"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              :disabled="!footer"
              @click="$refs.executor.showEdit()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="false">
          <el-form-item label="报告部门" prop="reportdepartment">
            <el-input
              v-model="formData.reportdepartment"
              clearable
              placeholder="请输入报告部门"
              :style="{ width: '256px' }"
              :disabled="true"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              :disabled="!footer"
              @click="$refs.department.show()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="" prop="memo">
            <!-- <tinymce
              v-model="formData.repdesc"
              :height="300"
              placeholder="请输入编辑器"
            /> -->
            <UEditor
              ref="ueditor"
              v-model="formData.memo"
              :height="300"
              :disabled="!footer"
              :templates="templates"
              template="gzrz"
              style="margin-left: -100px"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
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
                <el-button
                  type="success"
                  :disabled="showMJ && !formData.secrectLevelId"
                >
                  点击上传
                </el-button>
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
                <el-button
                  type="text"
                  :disabled="false"
                  @click="handlePreviewFile(row)"
                >
                  预览
                </el-button>
                <el-button type="text" @click="handleDelete(row)" v-if="footer">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </div>
    <template #footer v-if="footer">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </template>
    <department-options ref="department" @selected="handleDepartmentSelected" />
    <executor-options ref="executor" @projectManage="handleExecutorSelected" />

    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </el-dialog>
</template>

<script>
  import {
    deleteFile,
    download,
    workReportFileList,
    workReportSave,
  } from '@/api/audit/implement'
  import Tinymce from '@/components/Tinymce'
  import UEditor from '@/components/UEditor'
  import store from '@/store'
  import { formatDay } from '@/utils/index'
  import DepartmentOptions from './options/department.vue'
  // import ExecutorOptions from './options/executor.vue'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import ExecutorOptions from '@/components/danxuanPerson.vue'
  import ZXPerson from '@/components/selectPerson.vue'
  import { getMJ } from '@/api/setting/mjsz'
  import { hasMJ, couldMJ } from '@/utils'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  const { baseURL } = require('@/config')
  export default {
    name: 'LoginInfo',
    components: {
      Tinymce,
      UEditor,
      DepartmentOptions,
      ExecutorOptions,
      ZXPerson,
    },
    inheritAttrs: false,
    props: {
      projectId: {
        type: Number,
        default: null,
      },
    },
    data() {
      return {
        baseApi: baseURL,
        importApi: '/audit/auditPlan/mergePlanProjectManageInfoImport',
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        headers: {
          token: store.getters['user/token'],
        },
        formData: {
          reportname: undefined,
          reporttime: undefined,
          reporttype: '日报',
          memo: undefined,
          repdesc: undefined,
          realname: undefined,
          reporter: undefined,
          reporterid: undefined,
          reportdepartment: undefined,
          reportdepartmentid: undefined,
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
        },
        footer: true,
        tableData: [],
        templates: [],
        rules: {
          reportname: [
            {
              required: true,
              message: '请输入日志名称',
              trigger: 'blur',
            },
          ],
          reporttime: [
            {
              required: true,
              message: '请选择日志时间',
              trigger: 'blur',
            },
          ],
          reporttype: [
            {
              required: true,
              message: '请选择报告类型',
              trigger: 'blur',
            },
          ],
          reporter: [
            {
              required: true,
              message: '请输入报告人',
              trigger: 'blur',
            },
          ],
          reportdepartment: [
            {
              required: true,
              message: '请输入报告部门',
              trigger: 'blur',
            },
          ],
          memo: [
            {
              required: true,
              message: '请输入编辑器',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        MJoption: [],
        showMJ: false,
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
    watch: {
      'formData.describe'(val) {
        if (this.$refs['ueditor'].editor.openTemplate) {
          this.$refs['ueditor'].editor.openTemplate = false
          let s = val
          const arr = [
            ['$[contract.contractno]', 'contractno'],
            ['$[contract.contractname]', 'contractname'],
            ['$[contract.contractamount]', 'contractmoney'],
            ['$[contract.contractItem]', 'contractitem'],
            ['$[contract.executor]', 'realname'],
            ['$[contract.rmbinwords]', 'hzsumowing'],

            ['$[counterpart.coupersion]', 'counterpartcode'],
            ['$[counterpart.personincharge]', 'contractbd'],
            ['$[counterpart.counterpartHank]', 'bankkhyh'],
            ['$[counterpart.counumber]', 'counterpartno'],
            ['$[counterpart.couname]', 'budgetname'],
            ['$[counterpart.couaddress]', 'counterpartaddress'],
            ['$[counterpart.coupersion]', 'contacts'],
            ['$[counterpart.contactsPhone]', 'contactsphone'],
            ['$[counterpart.counterpartHankAccount]', 'bankaccount'],
            ['$[counterpart.legarepresentative]', 'contacts'],
            ['$[counterpart.pctelephonenumber]', 'contractzd'],
            // ['$[counterpart.taxpayeridentification]', 'hzsumowing'], //纳税人识别号
          ]
          arr.forEach((i) => {
            if (this.formData[i[1]]) {
              s = s.replace(i[0], this.formData[i[1]])
            }
          })
          this.formData.repdesc = s
        }
      },
    },
    async created() {
      const info = localStorage.getItem('userInfo')
      const aa = JSON.parse(info)
      this.$set(this.formData, 'reporter', aa.realname)
      this.$set(this.formData, 'reporterid', aa.staffid)
      this.$set(this.formData, 'reportdepartment', aa.linkDetp.orgname)
      this.$set(this.formData, 'reportdepartmentid', aa.linkDetp.orgid)
      this.formData.reporter = aa.realname
      this.formData.reporterid = aa.staffid
      this.formData.reportdepartment = aa.linkDetp.orgname
      this.formData.reportdepartmentid = aa.linkDetp.orgid

      this.showMJ = couldMJ()
      if (this.showMJ) {
        // 获取密级,菜单id
        const res = await hasMJ('ImplementLog')
        this.menuId = res[0].menuid
        // 请求密级下拉数据
        const res2 = await getMJ({ rightId: res[0].menuid })
        console.log(res2.data)
        this.MJoption = res2.data
      }
    },
    mounted() {},
    methods: {
      changeMJ(selectedValue) {
        // 切换密级时清空知悉范围
        this.formData.staffScopeIds = ''
        this.formData.staffScopeNames = ''
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
        // 切换密级时清空附件列表
        this.tableData = []
      },
      handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.formData.staffScopeIds = ids
        this.formData.staffScopeNames = names
      },
      handleDepartmentSelected(node) {
        this.$nextTick(() => {
          this.$refs['ruleForm'].clearValidate()
        })
        this.formData.reportdepartmentid = node.id
        this.formData.reportdepartment = node.name
      },
      handleExecutorSelected(node) {
        this.$nextTick(() => {
          this.$refs['ruleForm'].clearValidate()
        })
        this.formData.reporter = node[0].realname
        this.formData.reporterid = node[0].staffid
      },
      showEdit(title, row) {
        console.log(row, 'row')
        this.dialogFormVisible = true
        if (row) {
          const { reportdepartment, reporter, ...other } = row.wr
          console.log(reportdepartment, 'reportdepartment')
          console.log(reporter, 'reporter')
          this.formData = {
            ...other,
            reportdepartment: reportdepartment?.orgname,
            reporter: reporter?.realname,
            reportname: row.wr.reportname,
            reporttime: formatDay(row.wr.reporttime),
          }
          this.getFileList(row.wr.reportid)
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        }
      },
      async getFileList(reportid) {
        const data = await workReportFileList({ reportid, projectId: this.projectId })
        this.tableData = data.data.data || []
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
      close() {
        this.dialogFormVisible = false
        this.formData = {
          reportname: undefined,
          reporttime: undefined,
          reporttype: '日报',
          memo: undefined,
          repdesc: undefined,
          realname: undefined,
          reporter: undefined,
          reporterid: undefined,
          reportdepartment: undefined,
          reportdepartmentid: undefined,
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
        }
        this.tableData = []
        this.footer = true
        this.$emit('close')
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let attids = ''
            this.tableData.map((item) => {
              attids += item.attid
              attids += ','
            })
            attids = attids.substring(0, attids.length - 1)
            const { reporter, reportdepartment, reporttime, ...other } =
              this.formData
            let params = { ...other }
            delete params.reportfile
            delete params.itemtype
            delete params.tblOrganization
            delete params.fhStaff
            delete params.zqyjStaff
            delete params.yjdes
            delete params.tblAttachments
            delete params.tblReporttemple
            delete params.reportmode
            const data = await workReportSave({
              reporttime: formatDay(reporttime),
              ...params,
              attids,
            })
            if (data.code == 1) {
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
        this.$message.success('删除成功')
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
          this.tableData = [...this.tableData, ...file.data]
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
