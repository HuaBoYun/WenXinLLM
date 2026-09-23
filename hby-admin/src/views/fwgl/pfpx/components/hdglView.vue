<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :append-to-body="true"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
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
            <el-form-item label="单位名称" prop="workUnitName">
              <el-input
                v-model="formData.workUnitName"
                clearable
                disabled
                placeholder="工作单位"
              />
              <!-- <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                :disabled="!footer"
                @click="showGroupLeader('unit')"
              >
                选择
              </el-button> -->
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="活动时间" prop="registerTime">
              <el-date-picker
                style="width: 100%"
                v-model="formData.registerTime"
                placeholder="选择登记时间"
                type="date"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd HH:mm:ss"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划" prop="planned">
              <el-select
                v-model="formData.planned"
                placeholder="请选择计划"
                :style="{ width: '100%' }"
                :disabled="!footer"
              >
                <el-option label="年度计划内" value="年度计划内" />
                <el-option label="年度计划外" value="年度计划外" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="知会人员" prop="informPersonnelName">
              <el-input
                v-model="formData.informPersonnelName"
                clearable
                disabled
                placeholder="知会人员"
                :style="{ width: '80%' }"
              />
              <el-button
                @click="projectManager"
                style="margin-left: 10px"
                type="primary"
                :disabled="!footer"
              >
                选择
              </el-button>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="12">
            <el-form-item label="计划外" prop="unplanned">
              <el-input
                v-model="formData.unplanned"
                clearable
                placeholder="请输入计划外"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col> -->
          <el-col :span="24">
            <el-form-item label="活动主题" prop="activityTopic">
              <el-input
                v-model="formData.activityTopic"
                clearable
                placeholder="请输入活动主题"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="活动内容简介" prop="activityContent">
              <el-input
                type="textarea"
                :rows="2"
                placeholder="请输入活动内容简介"
                v-model="formData.activityContent"
                :disabled="!footer"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="创建人" prop="activityManagementCreator">
              <el-input
                v-model="formData.activityManagementCreator"
                clearable
                placeholder="请选择创建人"
                :style="{ width: '100%' }"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="创建时间" prop="activityManagementCreatedTime">
              <el-date-picker
                :style="{ width: '100%' }"
                v-model="formData.activityManagementCreatedTime"
                placeholder="创建时间"
                type="date"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd HH:mm:ss"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item
              label="子公司公众号链接"
              prop="subsidiaryOfficialAccountLink"
            >
              <el-input
                type="textarea"
                :rows="2"
                placeholder="请输入子公司公众号链接"
                v-model="formData.subsidiaryOfficialAccountLink"
                :disabled="!footer"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="公司官网链接" prop="firmWebsiteLink">
              <el-input
                type="textarea"
                :rows="2"
                placeholder="请输入公司官网链接"
                v-model="formData.firmWebsiteLink"
                :disabled="!footer"
              ></el-input>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="24">
            <el-form-item label="现场照片" prop="sitePhoto">
              <el-upload
                class="avatar-uploader"
                :action="baseApi + api"
                :headers="headers"
                :show-file-list="false"
                :on-success="handleAvatarSuccess2"
                :before-upload="beforeAvatarUpload"
              >
                <img
                  v-if="formData.sitePhoto"
                  :src="baseApi + formData.sitePhoto"
                  class="avatar"
                />
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
            </el-form-item>
          </el-col> -->

          <el-col :span="24">
            <el-divider>现场照片</el-divider>
          </el-col>
          <el-col :span="24">
            <div style="text-align: right; margin-bottom: 5px" v-if="footer">
              <el-upload
                class="upload-demo"
                :show-file-list="false"
                :action="baseApi + api"
                :headers="headers"
                :on-success="handleSiteUploadSuccess"
                :file-list="sitePhotoList"
              >
                <el-button type="success">上传</el-button>
              </el-upload>
            </div>
            <el-table :data="sitePhotoList">
              <el-table-column
                align="center"
                label="附件名称"
                prop="fileName"
              />
              <el-table-column
                align="center"
                label="文件大小(KB)"
                prop="fileSize"
              >
                <template #default="{ row }">
                  <div>
                    {{ row.fileSize / 1000 }}
                  </div>
                </template>
              </el-table-column>
              <el-table-column align="center" label="创建人" prop="uploader" />
              <el-table-column
                align="center"
                label="操作"
                show-overflow-tooltip
                width="120"
              >
                <template #default="{ row }">
                  <el-button type="text" @click="handlePreviewFile(row)">
                    预览
                  </el-button>
                  <el-button type="text" @click="handleDown(row)">
                    下载
                  </el-button>
                  <el-button
                    type="text"
                    @click="handleDelete(row, 'sitePhotoList')"
                    v-if="footer"
                  >
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>

          <el-col :span="24">
            <el-divider>上传活动附件-培训通知</el-divider>
          </el-col>
          <el-col :span="24">
            <div style="text-align: right; margin-bottom: 5px" v-if="footer">
              <el-upload
                class="upload-demo"
                :show-file-list="false"
                :action="baseApi + api"
                :headers="headers"
                :on-success="handleAvatarSuccess"
              >
                <el-button type="success">上传</el-button>
              </el-upload>
            </div>
            <el-table :data="fileList">
              <el-table-column
                align="center"
                label="附件名称"
                prop="fileName"
              />
              <el-table-column
                align="center"
                label="文件大小(KB)"
                prop="fileSize"
              />
              <el-table-column align="center" label="创建人" prop="uploader" />
              <el-table-column
                align="center"
                label="操作"
                show-overflow-tooltip
                width="120"
              >
                <template #default="{ row }">
                  <el-button type="text" @click="handlePreviewFile(row)">
                    预览
                  </el-button>
                  <el-button type="text" @click="handleDown(row)">
                    下载
                  </el-button>
                  <el-button
                    type="text"
                    @click="handleDelete(row, 'fileList')"
                    v-if="footer"
                  >
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
          <!-- <el-col :span="24">
            <el-divider>上传活动附件-子公司公众号链接</el-divider>
          </el-col>
          <el-col :span="24">
            <div style="text-align: right; margin-bottom: 5px" v-if="footer">
              <el-upload
                class="upload-demo"
                :show-file-list="false"
                :action="baseApi + api"
                :headers="headers"
                :on-success="handleAvatarSuccess"
              >
                <el-button type="success">上传</el-button>
              </el-upload>
            </div>
            <el-table :data="fileList">
              <el-table-column
                align="center"
                label="附件名称"
                prop="fileName"
              />
              <el-table-column
                align="center"
                label="文件大小(KB)"
                prop="fileSize"
              />
              <el-table-column align="center" label="创建人" prop="uploader" />
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
                    v-if="footer"
                  >
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col> -->
          <!-- <el-col :span="24">
            <el-divider>上传活动附件-公司官网链接</el-divider>
          </el-col>
          <el-col :span="24">
            <div style="text-align: right; margin-bottom: 5px" v-if="footer">
              <el-upload
                class="upload-demo"
                :show-file-list="false"
                :action="baseApi + api"
                :headers="headers"
                :on-success="handleAvatarSuccess"
              >
                <el-button type="success">上传</el-button>
              </el-upload>
            </div>
            <el-table :data="fileList">
              <el-table-column
                align="center"
                label="附件名称"
                prop="fileName"
              />
              <el-table-column
                align="center"
                label="文件大小(KB)"
                prop="fileSize"
              />
              <el-table-column align="center" label="创建人" prop="uploader" />
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
                    v-if="footer"
                  >
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col> -->
          <el-col :span="24">
            <el-divider>上传活动附件-其他</el-divider>
          </el-col>
          <el-col :span="24">
            <div style="text-align: right; margin-bottom: 5px" v-if="footer">
              <el-upload
                class="upload-demo"
                :show-file-list="false"
                :action="baseApi + api"
                :headers="headers"
                :on-success="handleAvatarSuccess1"
              >
                <el-button type="success">上传</el-button>
              </el-upload>
            </div>
            <el-table :data="fileList1">
              <el-table-column
                align="center"
                label="附件名称"
                prop="fileName"
              />
              <el-table-column
                align="center"
                label="文件大小(KB)"
                prop="fileSize"
              />
              <el-table-column align="center" label="创建人" prop="uploader" />
              <el-table-column
                align="center"
                label="操作"
                show-overflow-tooltip
                width="120"
              >
                <template #default="{ row }">
                  <el-button type="text" @click="handlePreviewFile(row)">
                    预览
                  </el-button>
                  <el-button type="text" @click="handleDown(row)">
                    下载
                  </el-button>
                  <el-button
                    type="text"
                    @click="handleDelete(row, 'fileList1')"
                    v-if="footer"
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
        <el-button @click="close">关 闭</el-button>
        <el-button @click="add" type="primary">确定</el-button>
      </template>
    </el-dialog>
    <company-select-modal
      ref="companySelect"
      @selected="handleCompanyTreeSelected"
    />
    <project-manage
      @projectManage="getChildlistPro"
      ref="manage"
    ></project-manage>
  </div>
</template>
<script>
  import store from '@/store'
  import CompanySelectModal from '@/components/CampanySelectModal'
  import projectManage from './selectPerson.vue'
  import { uploadApi, deleteFile, download } from '@/api/fwgl/zzxx'
  import { addHDGL, getHDGLDefaultInfo } from '@/api/fwgl/pfpx'
  import { baseURL } from '@/config'
  const token = store.getters['user/token']
  import { formatDate } from '@/utils'
  import { getPrivewAttInfo } from '@/api/fwgl/gsls'
  export default {
    props: [],
    components: {
      CompanySelectModal,
      projectManage,
    },
    data() {
      return {
        dialogFormVisible: false,
        formData: {},
        footer: true,
        title: '新增',
        tableData: [],
        sitePhotoList: [],
        baseApi: baseURL,
        api: uploadApi,
        fileList: [],
        fileList1: [],
        headers: { token: token },
        rules: {
          registerTime: [
            { required: true, message: '请选择登记时间', trigger: 'blur' },
          ],
          planned: [
            { required: true, message: '请输入计划内', trigger: 'blur' },
          ],
          unplanned: [
            { required: true, message: '请输入计划外', trigger: 'blur' },
          ],
          activityTopic: [
            { required: true, message: '请输入活动主题', trigger: 'blur' },
          ],
          activityContent: [
            { required: true, message: '请输入活动内容', trigger: 'blur' },
          ],
          activityManagementCreator: [
            { required: true, message: '请输入创建人', trigger: 'blur' },
          ],
          activityManagementCreatedTime: [
            { required: true, message: '请选择创建时间', trigger: 'blur' },
          ],
          // subsidiaryOfficialAccountLink: [
          //   { required: true, message: '请输入子公众号链接', trigger: 'blur' },
          // ],
          // firmWebsiteLink: [
          //   { required: true, message: '请输入企业网站链接', trigger: 'blur' },
          // ],
          // sitePhoto: [
          //   { required: true, message: '请上传现场照片', trigger: 'blur' },
          // ],
        },
      }
    },
    // mounted() {
    //   const userInfo = JSON.parse(localStorage.userInfo)
    //   this.workUnitName = userInfo.currentOrg.orgname
    // },
    methods: {
      showGroupLeader(type) {
        if (type === 'group') {
          this.$refs.companySelect.show({
            labelKey: 'belongGroupName',
            idKey: 'belongGroupId',
            title: '所属集团',
          })
        }
        if (type === 'unit') {
          this.$refs.companySelect.show({
            labelKey: 'workUnitName',
            idKey: 'workUnitId',
            title: '工作单位',
          })
        }
      },
      /**
       * @description: 选择公司部门回调
       * @param {*} val 已选数据
       * @return {*}
       */      
      handleCompanyTreeSelected(val) {
        this.$set(this.formData, val.idKey, val.id)
        this.$set(this.formData, val.labelKey, val.label)
      },
      /**
       * @description: 外部打开dialog
       * @param {*} title 表单类型
       * @param {*} row 行数据
       * @return {*}
       */      
      showEdit(title, row) {
        this.dialogFormVisible = true
        this.fileList = []
        this.fileList1 = []
        this.sitePhotoList = []
        if (row) {
          getHDGLDefaultInfo({
            id: row.activityManagementId,
          }).then((res) => {
            this.formData = Object.assign({}, res.data.activityManagement)
            if (res.data.activityFile) {
              this.fileList = res.data.activityFile
            }
            if (res.data.activityOtherFile) {
              this.fileList1 = res.data.activityOtherFile
            }
            this.sitePhotoList = res.data.sitePhotoFile || []
          })
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else {
          this.title = '新增'
          this.formData = this.$options.data().formData

          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.activityManagementCreator = userInfo.realname
          this.formData.workUnitName = userInfo.linkOrg.orgname
          this.formData.workUnitId = userInfo.linkOrg.orgid
          this.formData.activityManagementCreatedTime = formatDate(new Date())
        }
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.dialogFormVisible = false
        this.footer = true
      },
      /**
       * @description: 保存表单
       * @return {*}
       */      
      add() {
        const aa = []
        this.fileList.forEach((e) => {
          aa.push(e.fileId)
        })
        this.formData.activityFileIds = aa.toString()
        const bb = []
        this.fileList1.forEach((e) => {
          bb.push(e.fileId)
        })
        // 现场照片ids处理
        this.formData.sitePhoto = (this.sitePhotoList || [])
          .map((x) => x.fileId)
          .join(',')

        this.formData.activityOtherFileIds = bb.toString()
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            addHDGL(this.formData).then((res) => {
              if (res.msg == '成功') {
                this.dialogFormVisible = false
                this.$emit('fetchData')
              }
            })
          } else {
            return false
          }
        })
      },
      handleAvatarSuccess(res) {
        if (res.code == 200) {
          this.fileList.push(res.data.fileIds[0])
        }
      },
      handleAvatarSuccess1(res) {
        if (res.code == 200) {
          this.fileList1.push(res.data.fileIds[0])
        }
      },
      handleAvatarSuccess2(res) {
        if (res.code == 200) {
          this.formData.sitePhoto = res.data.fileIds[0].filePath
        }
      },
      handleDelete(row, key) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await deleteFile({ id: row.fileId })
          if (code == 200) {
            const _key = key || 'fileList'
            const list = this[_key]

            list.splice(
              this.fileList.findIndex((x) => x.fileId == row.fileId),
              1
            )

            this.$set(this, key, list)

            // this.fileList.splice(
            //   this.fileList.findIndex((x) => x.fileId == row.fileId),
            //   1
            // )
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          }
        })
      },
      // 现场图片上传成功回调
      handleSiteUploadSuccess(res, b, c) {
        if (res && res.data) {
          this.sitePhotoList = this.sitePhotoList.concat(res.data.fileIds || [])
        }
      },
      /**
       * @description: 下载文件
       * @param {*} row
       * @return {*}
       */      
      async handleDown(row) {
        const data = await download({ fileId: row.fileId })
        let filename = row.fileName
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
       * @description: 文件预览
       * @param {*} row 文件信息
       * @return {*}
       */      
      async handlePreviewFile(row) {
        const { data } = await getPrivewAttInfo({
          fileId: row.fileId,
          attType: 2,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url }) // iframe弹框预览形式
      },
      /**
       * @description: 打开选择人员组件
       * @return {*}
       */      
      projectManager() {
        this.$refs['manage'].showEdit()
      },
      /**
       * @description: 选择组件回调
       * @param {*} val 已选数据
       * @return {*}
       */      
      async getChildlistPro(val) {
        const names = val.map((res) => res.realname).toString()
        const ids = val.map((res) => res.staffid).toString()

        this.$set(this.formData, 'informPersonnelName', names)
        this.$set(this.formData, 'informPersonnel', ids)
      },
    },
  }
</script>

<style scoped>
  .avatar-uploader .el-upload {
    width: 178px;
    height: 178px;
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409eff;
  }
  .avatar-uploader-icon {
    border: 1px dashed #d9d9d9;
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>
