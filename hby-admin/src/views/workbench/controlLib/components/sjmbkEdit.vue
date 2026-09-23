<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
      :close-on-click-modal="false"
    >
      <el-row :gutter="15">
        <el-form
          ref="elForm"
          :disabled="allDisabled"
          label-width="125px"
          :model="formData"
          :rules="rules"
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
                :style="{ width: '76%' }"
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
          <el-col :span="24">
            <el-divider></el-divider>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模板编号" prop="mbcode">
              <el-input
                v-model="formData.mbcode"
                clearable
                placeholder="请填写模板编号"
                :style="{ width: '348px', height: '30px' }"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模板名称" prop="mbname">
              <el-input
                v-model="formData.mbname"
                clearable
                placeholder="请填写模板名称"
                :style="{ width: '348px', height: '30px' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="创建人" prop="staffid" v-show="false">
              <el-input
                v-model="formData.staffid"
                clearable
                disabled
                :style="{ width: '348px', height: '30px' }"
              />
            </el-form-item>
            <el-form-item label="创建人" prop="realname">
              <el-input
                v-model="formData.realname"
                clearable
                disabled
                :style="{ width: '348px', height: '30px' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="审计类型" prop="audittype">
              <el-select
                v-model="formData.audittype"
                placeholder="请选择审计类型"
                :style="{ width: '348px', height: '30px' }"
              >
                <el-option
                  v-for="item in sjlxList"
                  :key="item.typeId"
                  :label="item.auditType"
                  :value="item.auditType"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-divider>文件上传</el-divider>
          </el-col>
          <el-col :span="24">
            <div style="text-align: right; margin-bottom: 5px">
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
      <div slot="footer" v-if="!allDisabled">
        <el-button @click="close">取消</el-button>
        <el-button @click="add" type="primary">确定</el-button>
      </div>
    </el-dialog>
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </div>
</template>
<script>
  import { download } from '@/api/audit/implement'
  import {
    createSJMBKCode,
    deleteSjmbkFile,
    getSjlxList,
    getSjlxListOld,
    getSjmbkInfo,
    sjmbkAdd,
    sjmbkFileList,
  } from '@/api/workbench/auditTools'
  import store from '@/store'
  const { baseURL } = require('@/config')
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import ZXPerson from '@/components/selectPerson.vue'
  import { getMJ } from '@/api/setting/mjsz'
  import { hasMJ, couldMJ } from '@/utils'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    name: 'xxxx',
    components: { ZXPerson },

    data() {
      return {
        baseApi: baseURL,
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        headers: {
          token: store.getters['user/token'],
        },
        sjlxList: [],
        formData: {
          realname: '',
          staffid: '',
          audittype: '',
          mbcode: '',
          mbname: '',
          mbid: '',
          secrectLevelId: '',
          staffScopeIds: '',
          staffScopeNames: '',
        },

        allDisabled: false,
        dialogFormVisible: false,
        title: '',
        rules: {
          audittype: [
            {
              required: true,
              message: '请输入报告名称',
              trigger: 'blur',
            },
          ],
          mbcode: [
            {
              required: true,
              message: '请输入报告名称',
              trigger: 'blur',
            },
          ],
          mbname: [
            {
              required: true,
              message: '请输入报告名称',
              trigger: 'blur',
            },
          ],
        },
        fileList: [],
        tableDataFile: [],
        fileIds: [],
        mjId: '', // 密级id
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
    async created() {
      this.showMJ = couldMJ()
      if (this.showMJ) {
        // 获取密级,菜单id
        const res = await hasMJ('BaseConfig')
        this.menuId = res[0].menuid
        // 请求密级下拉数据
        const res2 = await getMJ({ rightId: res[0].menuid })
        this.MJoption = res2.data
      }
    },
    methods: {
      close() {
        this.formData = {
          realname: '',
          staffid: '',
          audittype: '',
          mbcode: '',
          mbname: '',
          mbid: '',
          secrectLevelId: '',
          staffScopeIds: '',
          staffScopeNames: '',
        }
        this.dialogFormVisible = false
        this.allDisabled = false
        this.tableDataFile = []
        this.fileIds = []
        this.fileList = []
      },
      showEdit(disabled, title, row) {
        this.dialogFormVisible = true
        const info = JSON.parse(localStorage.getItem('userInfo'))
        this.formData.staffid = info.staffid
        this.formData.realname = info.realname
        if (localStorage.getItem('model') == 'znsj') {
          getSjlxListOld().then((res) => {
            this.sjlxList = res.data.pageInfo.tlist
          })
        } else {
          getSjlxList().then((res) => {
            this.sjlxList = res.data.tlist
          })
        }
        if (title === '新建') {
          this.title = '添加'
          this.allDisabled = false
          createSJMBKCode().then((res) => {
            this.$set(this.formData, 'mbcode', res.data.autoCode.toString())
          })
        } else if (title === '修改') {
          this.title = '修改'
          this.getDefaultInfo(row)
          this.getFileList(row.mbid)
          this.allDisabled = false
        } else {
          this.title = '详情'
          this.getDefaultInfo(row)
          this.getFileList(row.mbid)
          this.allDisabled = true
        }
      },
      async getDefaultInfo(row) {
        const info = await getSjmbkInfo({ mbid: row.mbid })
        Object.keys(this.formData).forEach((key) => {
          this.formData[key] = info.data.mb[key]
        })
        this.formData.staffid = info.data.mb.createStaff.staffid
        this.formData.realname = info.data.mb.createStaff.realname
        this.formData.secrectLevelId = info.data.mb.secrectLevelId
        this.formData.staffScopeIds = info.data.mb.staffScopeIds
        this.formData.staffScopeNames = info.data.mb.staffScopeNames
      },
      async getFileList(mbid) {
        const res = await sjmbkFileList({ mbid })
        //回填上传文件表格
        const arr = res.data.attList
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
      add() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            let attids = ''
            this.tableDataFile.map((item) => {
              attids += item.attid
              attids += ','
            })
            attids = attids.substring(0, attids.length - 1)

            const {
              staffid,
              audittype,
              mbcode,
              mbname,
              mbid,
              secrectLevelId,
              staffScopeIds,
              staffScopeNames,
            } = this.formData
            const data = await sjmbkAdd({
              staffid,
              audittype,
              mbcode,
              mbname,
              mbid,
              secrectLevelId,
              staffScopeIds,
              staffScopeNames,
              attids: attids,
            })
            if (data.code == 1) {
              this.$baseMessage('保存成功', 'success')
              this.close()
              this.$emit('fetch-data')
            } else {
              this.$baseMessage(data.msg, 'error')
            }
          } else {
            return false
          }
        })
      },
      async handleDeleteFile(index, row) {
        let res = await deleteSjmbkFile({ attid: +row.attid })
        if (res.msg === '成功') {
          this.$message({
            type: 'success',
            message: '删除成功!',
          })
          this.fileIds.splice(index, 1)
          this.tableDataFile.splice(index, 1)
        }
      },
      handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.$set(this.formData, 'staffScopeIds', ids)
        this.$set(this.formData, 'staffScopeNames', names)
      },
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
