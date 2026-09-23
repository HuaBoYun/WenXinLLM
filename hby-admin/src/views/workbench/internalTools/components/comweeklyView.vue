<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    append-to-body
  >
    <el-row :gutter="24">
      <el-form
        :model="formData"
        :rules="rules"
        label-width="140px"
        :disabled="formDisabled"
        ref="ruleForm"
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
          <el-form-item label="标题" prop="weeklytitle">
            <el-input
              placeholder="请输入标题"
              v-model="formData.weeklytitle"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="生效日期" prop="effdate">
            <el-date-picker
              v-model="formData.effdate"
              type="date"
              style="width: 100%"
              value-format="yyyy-MM-dd"
              placeholder="选择日期"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="生效状态" prop="effstatus">
            <el-radio v-model="formData.effstatus" :label="0">未生效</el-radio>
            <el-radio v-model="formData.effstatus" :label="1">生效</el-radio>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="简介" prop="memo">
            <el-input
              placeholder="请输入简介"
              v-model="formData.memo"
              type="textarea"
              :rows="8"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div
            style="text-align: right; margin-bottom: 5px"
            v-if="!formDisabled"
          >
            <!-- <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-preview="handlePreview"
              :on-success="handleSuccess"
              :accept="'video/mp4,image/gif,application/pdf,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/msword'"
              :file-list="tableData"
              multiple
            >
              <el-button type="success">上传</el-button>
            </el-upload> -->
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
                <el-button
                  type="text"
                  @click="handleDelete(row)"
                  v-if="!formDisabled"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <template #footer v-if="!formDisabled">
      <el-button @click="close">取消</el-button>
      <el-button @click="submit" type="primary">确认</el-button>
    </template>
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </el-dialog>
</template>
<script>
  import {
    deleteFile,
    download,
    getPrivewAttInfo,
    complianceWeenlySave,
    getComplianceWeenlyDetail,
  } from '@/api/fwgl/hgzk'
  import store from '@/store'
  import ZXPerson from '@/components/selectPerson.vue'
  import { getMJ } from '@/api/setting/mjsz'
  import { hasMJ, couldMJ } from '@/utils'
  const { baseURL } = require('@/config')
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  const token = store.getters['user/token']
  export default {
    components: { ZXPerson },
    data() {
      return {
        baseApi: baseURL,
        // api: '/audit/fileManage/upload',
        // headers: {
        //   token: store.getters['user/token'],
        // },
        headers: { token: token },
        fileList: [],
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        dialogFormVisible: false,
        title: '新增',
        formData: {
          effdate: '',
          effstatus: 0,
          memo: '',
          weeklytitle: '',
          lytype: '',
          secrectLevelId: undefined,
          staffScopeNames: undefined,
          staffScopeIds: undefined,
        },
        tableData: [],
        rules: {
          // weeklytitle: [{ required: true, message: '请输入标题' }],
          // effdate: [{ required: true, message: '请选择日期' }],
        },
        formDisabled: false,
        MJoption: [],
        menuId: 0,
        showMJ: false,
      }
    },
    async created() {
      this.showMJ = couldMJ()
      if (this.showMJ) {
        const res = await hasMJ('neikongzy')
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
      handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.formData.staffScopeIds = ids
        this.formData.staffScopeNames = names
      },
      showEdit(type, data, lytype) {
        this.formData.lytype = lytype
        this.dialogFormVisible = true
        if (type == 'add') {
          this.title = '新增'
        } else if (type == 'edit') {
          this.title = '修改'
          this.getDetail(data.id)
        } else {
          this.getDetail(data.id)
          this.title = '查看'
          this.formDisabled = true
        }
      },
      async getDetail(id) {
        const { data, code, msg } = await getComplianceWeenlyDetail({
          id: id,
        })
        if (code != 1) return
        this.formData = data.tblComplianceWeekly
        this.tableData = data.files || []
      },
      submit() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.saveData()
          } else {
            console.log('error submit!!')
            return false
          }
        })
      },
      async saveData() {
        let attids = ''
        this.tableData.map((item) => {
          attids += item.attid
          attids += ','
        })
        attids = attids.substring(0, attids.length - 1)
        const { data, code, msg } = await complianceWeenlySave({
          ...this.formData,
          fileIds: attids,
        })
        if (code != 1) return
        this.$message.success('保存成功')
        this.$emit('fetchData')
        this.close()
      },
      close() {
        this.formDisabled = false
        this.dialogFormVisible = false
        this.formData = {
          effdate: '',
          effstatus: '0',
          memo: '',
          weeklytitle: '',
          lytype: '',
        }
        this.tableData = []
      },
      // 预览
      // async handlePreview(row) {
      //   const { data } = await getPrivewAttInfo({
      //     attId: row.attid,
      //     attType: 4,
      //   })
      //   let url =
      //     data.previewurl +
      //     '?url=' +
      //     encodeURIComponent(Base64.encode(data.ftpUrl))
      //   if (
      //     data.ftpUrl.includes('.pdf') ||
      //     data.ftpUrl.includes('.doc') ||
      //     data.ftpUrl.includes('.docx')
      //   ) {
      //     url = url + '&officePreviewType=pdf'
      //   }
      //   window.open(url)
      // },
      // 下载
      // async handleDown(row) {
      //   const data = await download({ attId: row.attid })
      //   let filename = row.attname
      //   let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
      //   let url = window.URL.createObjectURL(blob, {
      //     type: 'application/vnd.ms-excel',
      //   })
      //   const link = document.createElement('a')
      //   link.style.display = 'none'
      //   link.href = url
      //   link.setAttribute('download', filename)
      //   document.documentElement.appendChild(link)
      //   link.click()
      //   document.documentElement.removeChild(link)
      // },
      // 删除
      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        this.tableData = list
        deleteFile({ attId: row.attid })
        this.$message.success('删除成功')
      },
      // 上传成功
      // handleSuccess(file) {
      //   if (file.result == '200') {
      //     let list = this.tableData
      //     list.push(file.data)
      //     this.tableData = list
      //     this.$baseMessage(file.msg, 'success')
      //   } else {
      //     this.$baseMessage(file.msg, 'error')
      //   }
      // },
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
          // this.fileList = [...this.fileList, ...file.data]
          this.tableData = [...this.tableData, ...file.data]
          // this.tableData = list
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
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
<style lang="scss" scoped></style>
