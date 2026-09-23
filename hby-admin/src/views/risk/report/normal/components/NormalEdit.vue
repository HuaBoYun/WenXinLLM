<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
      append-to-body
    >
      <el-row :gutter="15">
        <el-form
          ref="elForm"
          label-width="120px"
          :model="formData"
          :rules="rules"
          size="medium"
          :disabled="alldisabled"
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
                readonly
                placeholder="请选择知悉范围"
                :style="{ width: '75%' }"
                disabled
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="$refs.ZXPerson.showEdit(formData.secrectLevelId)"
                :disabled="!formData.secrectLevelId"
              >
                选择
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="showMJ">
            <el-divider>基本信息</el-divider>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报告名称" prop="reportname">
              <el-input
                v-model="formData.reportname"
                clearable
                placeholder="请输入报告名称"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报告时间" prop="reporttime">
              <el-date-picker
                v-model="formData.reporttime"
                clearable
                format="yyyy-MM-dd"
                placeholder="请选择报告时间"
                :style="{ width: '100%' }"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报告类型" prop="reporttype">
              <el-select
                v-model="formData.reporttype"
                clearable
                placeholder="请选择报告类型"
                :style="{ width: '100%' }"
              >
                <el-option
                  v-for="(item, index) in reporttypeOptions"
                  :key="index"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报告方式" prop="reportmode">
              <el-select
                v-model="formData.reportmode"
                clearable
                placeholder="请选择报告方式"
                :style="{ width: '100%' }"
              >
                <el-option
                  v-for="(item, index) in reportmodeOptions"
                  :key="index"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报告层级" prop="reportlevel">
              <el-select
                v-model="formData.reportlevel"
                clearable
                placeholder="请选择报告层级"
                :style="{ width: '100%' }"
              >
                <el-option
                  v-for="(item, index) in reportmodeOptions2"
                  :key="index"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报告人" prop="reporter">
              <el-input
                v-model="formData.reporter"
                readonly
                style="width: 75%; margin-right: 8px"
              ></el-input>
              <el-button type="primary" @click="handleShowUser">选择</el-button>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报告部门" prop="reportdepartment">
              <el-input
                v-model="formData.reportdepartment"
                readonly
                style="width: 75%; margin-right: 8px"
              ></el-input>
              <el-button type="primary" @click="handleShowCompent">
                选择
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <UEditor
              ref="ueditor"
              v-model="formData.repdesc"
              :height="300"
              :templates="templates"
              :isCopyRisk="true"
              template="FXBGGL"
            />
          </el-col>
          <el-col :span="24">
            <el-divider>附件</el-divider>
          </el-col>
          <el-col :span="24">
            <div
              style="text-align: right; margin-bottom: 5px"
              v-if="!alldisabled"
            >
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
                    v-if="!alldisabled"
                  >
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-form>
      </el-row>
      <template #footer>
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
        <!-- <el-button
          v-if="
            (formData.status == 2 || formData.status == 3) &&
            jurisdictionCode == 1
          "
          @click="ymsubmit"
          type="primary"
        >
          提交
        </el-button> -->
      </template>
    </el-dialog>
    <vab-upload
      ref="vabUpload"
      :limit="50"
      name="file"
      :size="2"
      url="/upload"
    />
    <!-- 部门选择 -->
    <CompanyTreeModel ref="comTreeRef" @selected="handleSelectCompany" />
    <CompanySelectUserByTree
      ref="userTreeRef"
      @selected="handleExecutorSelected"
    />
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </div>
</template>
<script>
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import {
    getDefaultReportInfo,
    saveReportData,
    addReportData,
  } from '@/api/risk/report'
  import { deleteFieldById, downFieldById } from '@/api/risk/riskEvents'
  import UEditor from '@/components/UEditor'
  // import Tinymce from '@/components/Tinymce'
  import CompanySelectUserByTree from '@/components/CompanySelectUserByTree'
  import CompanyTreeModel from '@/components/CompanyTreeModel'
  import VabUpload from '@/extra/VabUpload'
  import store from '@/store'
  import { formatDay } from '@/utils/index'
  const { baseURL } = require('@/config')
  import {
    getFlowTaskInfo,
    ymWorkCandidates,
    ymWorkSubmit,
  } from '@/api/contract/manage'
  import CandidateUserSelect from '@/components/CandidateUserSelect'
  import ZXPerson from '@/components/selectPerson'
  import { couldMJ, hasMJ } from '@/utils'
  import { getMJ } from '@/api/setting/mjsz'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    name: 'NormalEdit',
    components: {
      // Tinymce,
      VabUpload,
      UEditor,
      CompanyTreeModel,
      CompanySelectUserByTree,
      CandidateUserSelect,
      ZXPerson,
    },
    props: [],
    data() {
      return {
        uploadLoading: false,
        title: '',
        templates: [],
        fileIdList: [],
        dialogFormVisible: false,
        formData: {
          reportname: undefined,
          reporttime: undefined,
          reporttype: null,
          reportmode: null,
          reportdepartmentid: undefined,
          reportdepartment: undefined,
          reporter: undefined,
          reporterid: undefined,
          repdesc: undefined,
          reportid: undefined,
          reportlevel: undefined,
          status: undefined,
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
        },
        showRow: {},
        list: [],
        tableData: [],
        reportid: undefined,
        rules: {
          reportname: [
            {
              required: true,
              message: '请输入报告名称',
              trigger: 'blur',
            },
          ],
          reporttime: [
            {
              required: true,
              message: '请选择报告时间',
              trigger: 'blur',
            },
          ],
          reporttype: [
            {
              required: true,
              message: '请选择报告类型',
              trigger: 'change',
            },
          ],
          reportmode: [
            {
              required: true,
              message: '请选择报告方式',
              trigger: 'change',
            },
          ],
          reportlevel: [
            {
              required: true,
              message: '请选择报告层级',
              trigger: 'change',
            },
          ],
          reporter: [
            {
              required: true,
              message: '请选择报告人',
              trigger: 'change',
            },
          ],
          reportdepartment: [
            {
              required: true,
              message: '请选择报告部门',
              trigger: 'change',
            },
          ],
        },
        reporttypeOptions: [
          {
            label: '对内报告',
            value: '对内报告',
          },
          {
            label: '对外报告',
            value: '对外报告',
          },
        ],
        reportmodeOptions: [
          {
            label: '定期报告',
            value: '定期报告',
          },
          {
            label: '非定期报告',
            value: '非定期报告',
          },
        ],
        reportmodeOptions2: [
          {
            label: '部门级',
            value: '部门级',
          },
          {
            label: '公司级',
            value: '公司级',
          },
        ],
        baseApi: baseURL,
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        fileList: [],
        headers: {
          token: store.getters['user/token'],
        },
        fileList: [],
        removeIds: [],
        tableDataFile: [],
        //提交

        status: 0,
        jurisdictionCode: 0,
        showMJ: false,
        MJoption: [],
        alldisabled: false,
      }
    },
    async created() {
      this.showMJ = couldMJ()
      if (this.showMJ) {
        const res = await hasMJ('ReportNormal')
        this.menuId = res[0].menuid
        const res2 = await getMJ({ rightId: res[0].menuid })
        this.MJoption = res2.data
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
    methods: {
      changeMJ(selectedValue) {
        const selectedItem = this.MJoption.find(
          (item) => item.levelId === selectedValue
        )
        if (selectedItem) {
          const label = selectedItem.levelName
          if (label == '非密' || label == '公开') {
            this.formData.staffScopeNames = '全部人员'
            this.formData.staffScopeIds = ''
          } else {
            this.formData.staffScopeIds = ''
            this.formData.staffScopeNames = ''
          }
        }
      },
      async handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.formData.staffScopeIds = ids
        this.formData.staffScopeNames = names
      },

      // 调起部门选择
      handleShowCompent() {
        this.$refs['comTreeRef'].show()
      },
      // 部门选择赋值
      handleSelectCompany(e) {
        this.$set(this.formData, 'reportdepartment', e.name)
        this.$set(this.formData, 'reportdepartmentid', e.id)
      },
      // 部门人员选择
      handleShowUser() {
        this.$refs['userTreeRef'].show()
      },
      /**
       * @description: 选择人员回调
       * @return {*}
       */
      handleExecutorSelected(e) {
        this.$set(this.formData, 'reporter', e.realname)
        this.$set(this.formData, 'reporterid', e.staffid)
      },
      handleEdit2(row) {
        console.log('rrr', row)
      },
      /**
       * @description: 保存
       * @return {*}
       */
      save() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            const info = {
              ...this.formData,
              reporterid: +this.formData.reporterid,
              reportdepartmentid: +this.formData.reportdepartmentid,
              type: 'fx_zdy',
              // attids: '',
              reporttime: formatDay(this.formData.reporttime),
              reportid: this.reportid || '',
              reporteds: this.fileIdList.toString(),
              removeReporteds: this.removeIds.toString(),
            }
            if (this.reportid) {
              console.log(info, 'this.reportid')
              saveReportData(info).then((res) => {
                if (res.code === 1) {
                  this.$baseMessage('保存成功', 'success')
                  this.$emit('reloadTable')
                  this.close()
                  this.reportid = res.data.newReportId
                }
              })
            } else {
              console.log(info, 'this.reportid')
              addReportData(info).then((res) => {
                this.$baseMessage('保存成功', 'success')
                this.formData.reportid = res.data.newReportId
                this.showRow = {
                  reportid: res.data.newReportId,
                  ...this.formData,
                }
                this.reportid = res.data.newReportId
                this.$emit('reloadTable')
                this.close()
              })
            }
          } else {
            console.log('error submit!!')
            return false
          }
        })
      },

      /**
       * @description: 页面初始化，获取数据
       * @return {*}
       */
      async showEdit(row, type) {
        this.fileIdList = []
        this.removeIds = []
        this.tableDataFile = []
        if (!row) {
          this.title = '添加'
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.$set(this.formData, 'reporter', userInfo.realname)
          this.$set(this.formData, 'reporterid', userInfo.staffid)
          this.$set(
            this.formData,
            'reportdepartment',
            userInfo.linkDetp.orgname
          )
          this.$set(
            this.formData,
            'reportdepartmentid',
            userInfo.linkDetp.orgid
          )
        } else {
          if (type == 'edit') {
            this.title = '编辑'
            this.alldisabled = false
          } else {
            this.title = '详情'
            this.alldisabled = true
          }
          this.showRow = row
          this.reportid = row.reportid

          let res = await getDefaultReportInfo({
            id: row.reportid,
            type: 'fx_zdy',
          })

          Object.keys(this.formData).forEach((key) => {
            this.formData[key] = res.data.report[key]
          })
          this.tableDataFile = res.data.attachmentList || []
          this.fileIdList = []
          console.log(res, 'resres')
          if (res.data.attachmentList) {
            res.data.attachmentList.forEach((item) => {
              this.fileIdList.push(item.attid)
            })
          }

          this.removeIds = []
        }
        this.dialogFormVisible = true
      },
      /**
       * @description: 关闭页面
       * @return {*}
       */
      close() {
        this.formData = this.$options.data().formData
        this.dialogFormVisible = false
        this.reportid = ''
        this.formData = {
          reportname: undefined,
          reporttime: undefined,
          reporttype: null,
          reportmode: null,
          reportdepartmentid: undefined,
          reportdepartment: undefined,
          reporter: undefined,
          reporterid: undefined,
          repdesc: undefined,
          reportid: undefined,
          reportlevel: undefined,
          status: undefined,
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
        }
        this.tableDataFile = []
        this.fileIdList = []
        this.alldisabled = false
      },
      /**
       * @description: 附件删除
       * @return {*}
       */
      handleDelete(row) {
        const deleteId = row.attid
        this.removeIds.push(deleteId)
        //删除formData要返回给后端的id
        this.fileIdList = this.fileIdList.filter((item) => {
          return item != deleteId
        })
        //删除tableDataFile，假删除
        this.tableDataFile = this.tableDataFile.filter((item) => {
          return item != row
        })
        this.$baseMessage('删除成功', 'success', 'vab-hey-message-success')
        // await this.fetchData()
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
          // 确保 file.data 是数组格式
          const uploadedFiles = Array.isArray(file.data)
            ? file.data
            : [file.data]

          // 更新文件列表
          this.fileList = [...this.fileList, ...uploadedFiles]
          this.tableDataFile = [...this.tableDataFile, ...uploadedFiles]

          // 保存上传文件的attid到fileIdList
          uploadedFiles.forEach((fileItem) => {
            if (fileItem.attid) {
              this.fileIdList.push(fileItem.attid)
            }
          })

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
<style></style>
