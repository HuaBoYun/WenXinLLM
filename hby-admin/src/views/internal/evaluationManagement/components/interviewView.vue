<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
      v-if="dialogFormVisible"
    >
      <el-form
        ref="form"
        label-width="120px"
        :model="formData"
        :rules="rules"
        :validate-on-rule-change="false"
      >
        <el-row>
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
                readonly
                placeholder="请选择知悉范围"
                :style="{ width: '80%' }"
                disabled
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="$refs.ZXPerson.showEdit(formData.secrectLevelId)"
                :disabled="!(formData.secrectLevelId && footer)"
              >
                选择
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="showMJ">
            <el-divider>基本信息</el-divider>
          </el-col>
          <el-col :span="12" style="height: 48px">
            <el-form-item label="被访谈人" prop="interviewee">
              <el-input
                v-model="formData.interviewee"
                clearable
                placeholder="请输入被访谈人"
                :disabled="!footer"
              />
              <!-- <el-input
                v-model="formData.interviewee"
                clearable
                placeholder="请选择被访谈人"
                :style="{ width: '78%' }"
                disabled
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="choosePerson('interviewee')"
              >
                选择
              </el-button> -->
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="时间" prop="times">
              <el-date-picker
                style="width: 100%"
                v-model="formData.times"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="选择日期"
                :disabled="!footer"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="职务" prop="post">
              <el-input
                v-model.trim="formData.post"
                :disabled="!footer"
                @input="numChange"
                placeholder="请输入职务"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系方式" prop="contact">
              <el-input
                v-model.trim="formData.contact"
                :disabled="!footer"
                placeholder="请输入联系方式"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="业务模块" prop="module">
              <el-input
                v-model.trim="formData.module"
                :disabled="!footer"
                placeholder="请输入业务模块"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="提问人" prop="questioner">
              <el-input
                v-model="formData.questioner"
                clearable
                placeholder="请输入提问人"
                :disabled="!footer"
              />
              <!-- <el-input
                v-model="formData.questioner"
                clearable
                placeholder="请选择提问人"
                :style="{ width: '78%' }"
                disabled
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="choosePerson('questioner')"
              >
                选择
              </el-button> -->
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="对应控制标准表" prop="standardtable">
              <el-input
                v-model.trim="formData.standardtable"
                :disabled="!footer"
                placeholder="请输入对应控制标准表"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="参加人" prop="participants">
              <el-input
                v-model="formData.participants"
                clearable
                placeholder="请输入参加人"
                :disabled="!footer"
              />
              <!-- <el-input
                v-model="formData.participants"
                clearable
                placeholder="请选择参加人"
                :style="{ width: '78%' }"
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="choosePerson('participants')"
              >
                选择
              </el-button> -->
              <!-- <el-input
                v-model="formData.participants"
                disabled
                placeholder="请选择参加人"
                :style="{ width: '78%' }"
              ></el-input>
              <el-button
                :style="{ marginLeft: '10px' }"
                @click="$refs['select'].showEdit()"
                type="primary"
              >
                选择
              </el-button> -->
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="记录人" prop="staffname">
              <el-input
                v-model="formData.staffname"
                clearable
                placeholder="请选择记录人"
                :style="{ width: '78%' }"
                disabled
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="choosePerson('staffname')"
              >
                选择
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="隶属部门" prop="deptname">
              <el-input
                placeholder="请选择隶属部门"
                v-model.trim="formData.deptname"
                disabled
                style="width: 78%"
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="$refs.departmentTree.show(0, null, '隶属部门')"
              >
                选择
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="一般访谈内容" prop="content">
              <el-input
                type="textarea"
                :rows="6"
                v-model.trim="formData.content"
                :disabled="!footer"
                placeholder="请输入一般访谈内容"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="总结" prop="summary">
              <el-input
                type="textarea"
                :rows="6"
                v-model.trim="formData.summary"
                :disabled="!footer"
                placeholder="请输入总结"
              />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-divider>文件上传</el-divider>
          </el-col>
          <el-col :span="24">
            <div style="text-align: right; margin-bottom: 5px" v-if="footer">
              <!-- <el-upload
                class="upload-demo"
                :show-file-list="false"
                :action="
                  assid ? baseApi + api + '?assId=' + assid : baseApi + api2
                "
                :headers="{
                  token: token,
                }"
                :on-preview="handlePreview"
                :on-success="handleSuccess"
                :file-list="tableData"
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
                width="120"
              >
                <template #default="{ row }">
                  <el-button type="text" @click="handleDowns(row)">
                    下载
                  </el-button>
                  <el-button type="text" @click="handlePreviewFile(row)">
                    预览
                  </el-button>
                  <el-button
                    v-if="footer"
                    type="text"
                    @click="handleDelete(row)"
                  >
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="close">取 消</el-button>
        <el-button v-if="footer" type="primary" @click="save">确 定</el-button>
      </template>
      <!-- 单选人员 -->
      <executor-options
        ref="executor"
        @projectManage="handleExecutorSelected"
      />
      <!-- 多选人员 -->
      <CompanySelectUserByTree
        :isUserName="true"
        ref="select"
        @projectManage="handleSelectUser"
      />
      <!-- 部门 -->
      <DepartmentTreeModal
        ref="departmentTree"
        @selected="handleDepartmentTreeSelected"
      />
      <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
    </el-dialog>
  </div>
</template>

<script>
  import {
    getPjftDetails,
    savePjftOrUpdate,
    download,
    deleteFTfile,
  } from '@/api/internal/project'
  const { baseURL } = require('@/config')
  import store from '@/store'
  import ExecutorOptions from '@/components/danxuanPerson.vue'
  import CompanySelectUserByTree from '@/components/selectPerson.vue'
  import DepartmentTreeModal from '@/components/DepartmentTreeModal'
  import { getPrivewAttInfo } from '@/api/contract/manage.js'
  import ZXPerson from '@/components/selectPerson.vue'
  import { getMJ } from '@/api/setting/mjsz'

  import { hasMJ, couldMJ } from '@/utils'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  const token = store.getters['user/token']
  export default {
    name: 'ProjectView',
    components: {
      ExecutorOptions,
      CompanySelectUserByTree,
      DepartmentTreeModal,
      ZXPerson,
    },
    data() {
      return {
        baseApi: baseURL,
        // api: '/nkhg/nbkz/pjgl/updateupload ',
        // api2: '/nkhg/nbkz/pjbg/addupload',
        // token: store.getters['user/token'],
        headers: { token: token },
        fileList: [],
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        formData: {
          id: '',
          interviewee: '',
          times: '',
          post: '',
          contact: '',
          module: '',
          questioner: '',
          causeanalysis: '',
          standardtable: '',
          participants: '',
          content: '',
          summary: '',
          staffname: '',
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
        },
        templates: [],
        tableData: [],
        renderData: [],
        modalType: 'new',
        assid: '', // 用于修改保存
        rules: {
          defectscode: [
            { required: true, trigger: 'blur', message: '请输入评价计划编号' },
          ],
          defectsname: [
            { required: true, trigger: 'blur', message: '请输入计划名称' },
          ],
        },
        footer: true,
        title: '',
        dialogFormVisible: false,
        MJoption: [],
        menuId: 0,
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
      if (couldMJ()) {
        // 获取密级,菜单id
        const res = await hasMJ('Interview')
        this.menuId = res[0].menuid
        // 请求密级下拉数据
        const res2 = await getMJ({ rightId: res[0].menuid })
        this.MJoption = res2.data
      }
      this.showMJ = couldMJ()
    },
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
      numChange(event) {
        // 允许数字和小数点
        const regex = /^\d*\.?\d*$/
        if (!regex.test(event)) {
          this.formData.num = this.formData.num
            .replace(/[^0-9.]/g, '')
            .replace(/(\..*)\./g, '$1')
        }
      },
      // 选择部门
      handleDepartmentTreeSelected(val) {
        this.$set(this.formData, 'dept', val.id)
        this.$set(this.formData, 'deptname', val.name)
      },
      //选择人员

      handleSelectUser(val) {
        let name = val.map((item) => {
          return item.realname
        })
        let ids = val.map((item) => {
          return item.staffid
        })
        this.formData.participants = name.join(',')
      },
      choosePerson(type) {
        this.$refs.executor.showEdit()
        this.personType = type
      },
      handleExecutorSelected(node) {
        console.log('🚀 ~ handleExecutorSelected ~ node:', node)
        this.formData[this.personType] = node[0].realname
        if (this.personType == 'staffname') {
          this.formData.staffid = node[0].staffid
        }
      },
      async showEdit(row, title) {
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'add') {
          this.title = '添加'
          let userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.staffname = userInfo.realname
          this.formData.staffid = userInfo.staffid
          this.formData.deptname = userInfo.linkDetp.orgname
          this.formData.dept = userInfo.linkDetp.orgid
        } else {
          this.title = '查看'
          this.footer = false
        }
        if (row) {
          let { data } = await getPjftDetails({ id: row.id })
          this.formData = data.entity
          this.tableData = data.atts
          console.log('🚀 ~ showEdit ~ this.formData:', this.formData)
        }
        this.dialogFormVisible = true
      },
      close() {
        this.formData = {
          id: '',
          interviewee: '',
          times: '',
          post: '',
          contact: '',
          module: '',
          questioner: '',
          causeanalysis: '',
          standardtable: '',
          participants: '',
          content: '',
          summary: '',
          staffname: '',
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
        }
        this.tableData = []
        this.footer = true
        this.dialogFormVisible = false
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            let attids = ''
            this.tableData.map((item) => {
              attids += item.attid
              attids += ','
            })
            attids = attids.substring(0, attids.length - 1)
            const params = {
              ...this.formData,
              attids: attids,
              secrectLevelId: this.formData.secrectLevelId,
              staffScopeIds: this.formData.staffScopeIds,
              staffScopeNames: this.formData.staffScopeNames,
            }
            const { msg, data, code } = await savePjftOrUpdate(params)
            this.$message.success('新增成功')
            this.$emit('fetch-data')
            this.close()
          }
        })
      },

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
      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        this.tableData = list
        const res = await deleteFTfile({ attid: row.attid })
        if (res && res.code === 200) {
          this.$message.success('删除成功')
        } else {
          this.$message.error('删除失败')
        }
      },
      // handlePreview(file) {},
      // handleSuccess(file) {
      //   if (file.code == '200') {
      //     let list = this.tableData || []
      //     list.push(file.data.Attachment)
      //     this.tableData = list
      //     this.$baseMessage('上传成功', 'success')
      //   } else {
      //     this.$baseMessage(file.msg, 'error')
      //   }
      // },
      /**
       * @description: 文件预览
       * @param {*} row 文件信息
       * @return {*}
       */
      // async handlePreviewFile(row) {
      //   const { data } = await getPrivewAttInfo({
      //     attId: row.attid,
      //     attType: 2,
      //   })

      //   const url =
      //     data.previewurl +
      //     '?url=' +
      //     encodeURIComponent(Base64.encode(data.ftpUrl))
      //   this.$iFrameDialog({ iframeUrl: url }) // iframe弹框预览形式
      // },
      handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.formData.staffScopeIds = ids
        this.formData.staffScopeNames = names
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
