<!--
 * @Date: 2022-01-21 09:06:33
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-28 10:48:58
 * @FilePath: /hb-admin/src/views/setting/system/components/LcdyEdit.vue
-->
<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-form
      ref="form"
      label-width="80px"
      :model="form"
      :rules="rules"
      :disabled="disabled"
    >
      <el-row :gutter="14">
        <el-col :span="12" v-if="showMJ">
          <el-form-item
            label="密级"
            prop="secrectLevelId"
            :rules="[
              { required: true, trigger: 'change', message: '请选择密级' },
            ]"
          >
            <el-select
              v-model="form.secrectLevelId"
              clearable
              placeholder="密级"
              style="width: 100%"
              :disabled="disabled"
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
              v-model="form.staffScopeNames"
              disabled
              placeholder="请选择知悉范围"
              :style="{ width: '76%' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.ZXPerson.showEdit(form.secrectLevelId)"
              :disabled="!form.secrectLevelId || disabled"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider></el-divider>
        </el-col>

        <el-col :span="12">
          <el-form-item label="文件编号" prop="rulecode">
            <el-input v-model.trim="form.rulecode" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="文件名称" prop="rulename">
            <el-input v-model.trim="form.rulename" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发文文号" prop="rulenumber">
            <el-input v-model.trim="form.rulenumber" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发文部门" prop="publishorg">
            <el-input
              v-model="form.publishorg"
              clearable
              placeholder="请选择发文部门"
              style="width: 80%"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.DepartmentOption.showEdit()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="效力级别" prop="effectivelevel">
            <el-select
              style="width: 100%"
              v-model="form.effectivelevel"
              placeholder="请选择"
            >
              <el-option label="法律" value="法律"></el-option>
              <el-option label="行政法规" value="行政法规"></el-option>
              <el-option label="司法解释" value="司法解释"></el-option>
              <el-option
                label="部门章规及其他规范性文件"
                value="部门章规及其他规范性文件"
              ></el-option>
              <el-option label="地方法规规章" value="地方法规规章"></el-option>
              <el-option label="其他" value="其他"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="时效性" prop="timeliness">
            <el-select
              style="width: 100%"
              v-model="form.timeliness"
              placeholder="请选择"
            >
              <el-option label="现行有效" value="现行有效"></el-option>
              <el-option label="征求意见稿" value="征求意见稿"></el-option>
              <el-option label="已失效" value="已失效"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发文日期" prop="publishdate">
            <el-date-picker
              style="width: 100%"
              v-model="form.publishdate"
              type="date"
              value-format="yyyy-MM-dd"
              format="yyyy-MM-dd"
              placeholder="选择日期"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="生效日期" prop="takeeffecttime">
            <el-date-picker
              style="width: 100%"
              v-model="form.takeeffecttime"
              type="date"
              value-format="yyyy-MM-dd"
              format="yyyy-MM-dd"
              placeholder="选择日期"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="录入人" prop="enteringperson">
            <el-input :disabled="true" v-model="form.enteringperson" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="录入时间" prop="enteringtime">
            <el-input :disabled="true" v-model="form.enteringtime" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="摘要" prop="summaryinfo">
            <el-input
              type="textarea"
              :autosize="{ minRows: 4 }"
              placeholder="请输入摘要"
              v-model="form.summaryinfo"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="" prop="bodyinfo">
            <UEditor
              ref="ueditor"
              v-model="form.bodyinfo"
              :height="300"
              :templates="templates"
              style="margin-left: -80px"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
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
          <el-table
            :data="fileTableList"
            @selection-change="handleSelectionChangeFile"
          >
            <!-- <el-table-column type="selection" width="55"></el-table-column> -->
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
                <el-button
                  :disabled="false"
                  type="text"
                  @click="handleDowns(row)"
                >
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
                  v-if="!disabled"
                  type="text"
                  @click="handleEdit2(row)"
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
      <el-button type="primary" v-if="!disabled" @click="save">确 定</el-button>
    </template>

    <DepartmentOption
      ref="DepartmentOption"
      @submit="handleDepartmentSelected"
    />

    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </el-dialog>
</template>

<script>
  import UEditor from '@/components/UEditor'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import { deleteLawFile, download } from '@/api/audit/implement'
  import { addOutList, getOutDetails } from '@/api/workbench/auditTools'
  import { baseURL } from '@/config'
  import store from '@/store'
  import { parseTime } from '@/utils/index'
  import DepartmentOption from '@/components/departmentSelect.vue'
  import ZXPerson from '@/components/selectPerson.vue'
  import { getMJ } from '@/api/setting/mjsz'
  import { hasMJ, couldMJ } from '@/utils'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    name: 'LcdyEdit',
    components: { UEditor, ZXPerson, DepartmentOption },
    data() {
      return {
        fileTableList: [],
        headers: {
          token: store.getters['user/token'],
        },
        baseApi: baseURL,
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        form: {
          rulecode: '',
          rulename: '',
          innruletype: '',
          rulenumber: '',
          status: '',
          publishorg: '',
          publishorgid: '',
          publishdate: '',
          bodyinfo: '',
          attIds: [],
          secrectLevelId: '',
          staffScopeIds: '',
          staffScopeNames: '',
        },
        disabled: false,
        templates: [],
        fileList: [],
        // tableData: [],
        rules: {
          rulecode: [
            { required: true, trigger: 'blur', message: '请输入文件编号' },
          ],
          rulename: [
            { required: true, trigger: 'blur', message: '请输入文件名称' },
          ],
          rulenumber: [
            { required: true, trigger: 'blur', message: '请输入发文文号' },
          ],
          publishorg: [
            { required: true, trigger: 'blur', message: '请输入发文部门' },
          ],
          effectivelevel: [
            { required: true, trigger: 'blur', message: '请选择效力级别' },
          ],
          timeliness: [
            { required: true, trigger: 'blur', message: '请选择时效性' },
          ],
          publishdate: [
            { required: true, trigger: 'blur', message: '请选择发文日期' },
          ],
          takeeffecttime: [
            { required: true, trigger: 'blur', message: '请选择生效日期' },
          ],
          enteringperson: [
            { required: true, trigger: 'blur', message: '请输入录入人' },
          ],
          enteringtime: [
            { required: true, trigger: 'blur', message: '请输入录入时间' },
          ],
          summaryinfo: [
            { required: true, trigger: 'blur', message: '请输入摘要' },
          ],
          bodyinfo: [
            { required: true, trigger: 'blur', message: '请输入内容' },
          ],
        },
        title: '',
        dialogFormVisible: false,
        options: [],
        MJoption: [],
        showMJ: false,
      }
    },
    computed: {
      getFormLevel() {
        if (!this.form.secrectLevelId) return ''
        const level = this.MJoption.find(
          (item) => item.levelId == this.form.secrectLevelId
        )
        return level ? level.levelName : ''
      },
    },
    async created() {
      // this.showMJ = couldMJ()
      if (this.showMJ) {
        // 获取密级,菜单id
        // const res = await hasMJ('ManageSystem')
        // this.menuId = res[0].menuid
        // 请求密级下拉数据
        // const res2 = await getMJ({ rightId: res[0].menuid })
        const res2 = await getMJ({ rightId: 201 })
        this.MJoption = res2.data
      }
    },
    methods: {
      changeMJ(selectedValue) {
        const selectedItem = this.MJoption.find(
          (item) => item.levelId === selectedValue
        )
        if (selectedItem) {
          const label = selectedItem.levelName
          if (label == '非密' || label == '公开') {
            this.form.staffScopeNames = '全部人员'
            this.form.staffScopeIds = ''
          } else {
            this.form.staffScopeIds = ''
            this.form.staffScopeNames = ''
          }
        }
      },
      handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.$set(this.form, 'staffScopeIds', ids)
        this.$set(this.form, 'staffScopeNames', names)
      },
      async showEdit(row, flag) {
        if (!row) {
          this.disabled = false
          this.title = '添加'
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.form.enteringpersonName = userInfo.realname
          this.form.enteringperson = userInfo.username
          this.form.enteringtime = parseTime(new Date(), '{y}-{m}-{d}')
          this.fileTableList = []
        } else if (row && flag) {
          console.dir(row)
          let res = await getOutDetails({
            outrulid: row.outrulid,
          })
          this.form = {
            ...res.data.data,
          }
          this.disabled = true
          this.fileTableList = res.data.attList
          this.form.takeeffecttime = parseTime(
            this.form.takeeffecttime,
            '{y}-{m}-{d}'
          )
          this.form.enteringtime = parseTime(
            this.form.enteringtime,
            '{y}-{m}-{d}'
          )
          this.form.publishdate = parseTime(
            this.form.publishdate,
            '{y}-{m}-{d}'
          )
        } else {
          console.dir(row)
          let res = await getOutDetails({
            outrulid: row.outrulid,
          })
          // let res = await selectInnerRuleInfo(row.innrulid)

          this.title = '编辑'
          this.form = {
            ...res.data.data,
          }
          this.form.outerId = this.form.outrulid
          this.form.takeeffecttime = parseTime(
            this.form.takeeffecttime,
            '{y}-{m}-{d}'
          )
          this.form.enteringtime = parseTime(
            this.form.enteringtime,
            '{y}-{m}-{d}'
          )
          this.form.publishdate = parseTime(
            this.form.publishdate,
            '{y}-{m}-{d}'
          )
          this.disabled = false
          this.fileTableList = res.data.attList
        }
        this.dialogFormVisible = true
      },
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      handleDepartmentSelected(data) {
        this.form.publishorgid = data.id
        this.form.publishorg = data.label
      },
      async save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            let attids = ''
            this.fileTableList.forEach((item) => {
              attids += item.attid + ','
            })
            attids = attids.substring(0, attids.length - 1)
            this.form.attIds = attids
            delete this.form.tblOrganization
            let res = await addOutList(this.form)
            this.form.outerId = res.data.data.outrulid
            this.dialogFormVisible = false
            this.$emit('fetch-data')
            this.$message.success('操作成功')
            // const { msg } = await mergeInnerRule(this.form)
            // this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            // this.close()
          }
        })
      },
      // handleSuccess(response, file, fileList) {
      //   this.fileTableList.push(response.data)
      //   console.dir(this.fileTableList)
      // },
      //删除选中的已上传的文件
      // handleDelFile() {
      //   let newArr = this.multipleSelectionFile.map((item) => {
      //     return item.id
      //   })
      //   this.form.attIds = this.form.attIds.filter((item) => {
      //     return !newArr.includes(item.id)
      //   })
      // },
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
      async handleEdit2(row) {
        let list = this.fileTableList
        list = list.filter((item) => item.attid != row.attid)
        this.fileTableList = list
        await deleteLawFile({ attId: row.attid })
        this.$message.success('删除成功')
      },
      handleSelectionChangeFile(val) {
        this.multipleSelectionFile = val
      },
      // async handlePreview(row) {
      //   const { data } = await getPrivewAttInfo({
      //     attId: row.attid,
      //     attType: 0,
      //   })

      //   const url =
      //     data.previewurl +
      //     '?url=' +
      //     encodeURIComponent(Base64.encode(data.ftpUrl))
      //   this.$iFrameDialog({ iframeUrl: url })
      // },
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
          this.fileTableList = [...this.fileTableList, ...file.data]
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
