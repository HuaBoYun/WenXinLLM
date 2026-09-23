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
      <el-row :gutter="20">
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
              readonly
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
          <el-form-item label="制度编号" prop="rulecode">
            <el-input v-model.trim="form.rulecode" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="制度名称" prop="rulename">
            <el-input v-model.trim="form.rulename" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="类别" prop="innruletype">
            <el-input v-model.trim="form.innruletype" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="制度类型" prop="zdtype">
            <el-input v-model.trim="form.zdtype" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发文文号" prop="rulenumber">
            <el-input v-model.trim="form.rulenumber" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-select
              style="width: 100%"
              :clearable="true"
              v-model="form.status"
              placeholder="请选择状态"
            >
              <el-option label="草稿" value="草稿"></el-option>
              <el-option label="发布待审核" value="发布待审核"></el-option>
              <el-option label="已发布" value="已发布"></el-option>
              <el-option label="发布审核拒绝" value="发布审核拒绝"></el-option>
              <el-option label="已修订" value="已修订"></el-option>
              <el-option label="已废止" value="已废止"></el-option>
              <el-option label="废止待审核" value="废止待审核"></el-option>
              <el-option label="废止审核拒绝" value="废止审核拒绝"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="发文机构" prop="orgname">
            <el-input
              v-model.trim="form.orgname"
              :style="{ width: '80%' }"
              :disabled="true"
            />
            <el-button
              type="primary"
              size="small"
              style="float: right"
              @click="$refs.manageTreeDialog.showDialog()"
              class="fl_r"
            >
              选 择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="生效日期" prop="pulishdate">
            <el-date-picker
              v-model.trim="form.pulishdate"
              placeholder="生效时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              style="width: 100%"
            />
          </el-form-item>
        </el-col> -->
        <!-- <el-col :span="24">
          <el-form-item label="" prop="bodyinfo">
            <UEditor
              ref="ueditor"
              v-model="form.bodyinfo"
              :height="300"
              :templates="templates"
              style="margin-left: -80px"
            />
          </el-form-item>
        </el-col> -->
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
      <el-button v-if="!disabled" @click="close">取 消</el-button>
      <el-button type="primary" v-if="!disabled" @click="save">确 定</el-button>
    </template>
    <Department ref="department" @selected="handleDepartmentSelected" />
    <ManageTreeDialog
      @selectNode="selectNode"
      ref="manageTreeDialog"
      :defaultExpandedH="3"
    ></ManageTreeDialog>
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </el-dialog>
</template>

<script>
  import { deleteManageFile, download } from '@/api/audit/implement'
  import {
    createManageCode,
    mergeInnerRule,
    selectInnerRuleInfo,
  } from '@/api/workbench/auditTools'
  import UEditor from '@/components/UEditor'
  import { baseURL } from '@/config'
  import store from '@/store'
  import { parseTime } from '@/utils/index'
  import Department from '@/views/contract/contractManage/components/options/department.vue'
  import ManageTreeDialog from './components/ManageTreeDialog.vue'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import ZXPerson from '@/components/selectPerson.vue'
  import { getMJ } from '@/api/setting/mjsz'
  import { hasMJ, couldMJ } from '@/utils'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    name: 'LcdyEdit',
    components: { Department, UEditor, ManageTreeDialog, ZXPerson },
    data() {
      return {
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
          publishdate: undefined,
          bodyinfo: '',
          attIds: [],
          zdtype: '',
          secrectLevelId: '',
          staffScopeIds: '',
          staffScopeNames: '',
        },
        disabled: false,
        templates: [],
        fileTableList: [],
        fileList: [],
        // tableData: [],
        rules: {
          rulecode: [{ required: true, trigger: 'blur', message: '请输入' }],
          rulename: [{ required: true, trigger: 'blur', message: '请输入' }],
          rulenumber: [{ required: true, trigger: 'blur', message: '请选择' }],
          innruletype: [{ required: true, trigger: 'blur', message: '请输入' }],
          zdtype: [{ required: true, trigger: 'blur', message: '请输入' }],
          status: [{ required: true, trigger: 'blur', message: '请输入' }],
          orgname: [{ required: true, trigger: 'blur', message: '请输入' }],
          pulishdate: [{ required: true, trigger: 'blur', message: '请输入' }],
          remark: [
            { required: true, trigger: 'blur', message: '请输入模板描述' },
          ],
        },
        title: '',
        dialogFormVisible: false,
        options: [],
        flowtaskinfoflowid: '',
        status: '',
        btnLoading: false,
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
      console.log('🚀 ~ created ~     this.showMJ :', this.showMJ)
      if (this.showMJ) {
        // 获取密级,菜单id
        // const res = await hasMJ('ManageSystem')
        // this.menuId = res[0].menuid
        // 请求密级下拉数据
        const res2 = await getMJ({ rightId: 201 })
        this.MJoption = res2.data
      }
    },
    methods: {
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
      // handleBeforeUpload(file) {
      //   const isLt2M = file.size / 1024 / 1024 < 100
      //   if (!isLt2M) {
      //     this.$message.error('文件大小不能超过 200MB!')
      //   }
      //   return isLt2M
      // },
      selectNode(node) {
        this.form.publishorg = node.id
        this.form.orgname = node.name
        console.dir(this.form)
        this.$forceUpdate()
      },

      async showEdit(row, flag) {
        console.log(row)
        if (!row) {
          this.disabled = false
          this.title = '添加'
          createManageCode().then((res) => {
            this.$set(this.form, 'rulecode', res.data.autoCode.toString())
          })
          this.form.innruletype = flag
        } else if (row && flag) {
          this.title = '详情'
          let res = await selectInnerRuleInfo(row.innrulid)
          this.fileTableList = res.data.attList
          this.form = {
            orgname: res.data.tblNbsjInnerrule.orgname,
            rulecode: res.data.tblNbsjInnerrule.rulecode,
            rulename: res.data.tblNbsjInnerrule.rulename,
            innruletype: res.data.tblNbsjInnerrule.innruletype,
            rulenumber: res.data.tblNbsjInnerrule.rulenumber,
            status: res.data.tblNbsjInnerrule.status,
            publishorg: res.data.tblNbsjInnerrule.publishorg,
            pulishdate: res.data.tblNbsjInnerrule.publishdate
              ? parseTime(res.data.tblNbsjInnerrule.publishdate, '{y}-{m}-{d}')
              : '',
            bodyinfo: res.data.tblNbsjInnerrule.bodyinfo,
            innrulid: res.data.tblNbsjInnerrule.innrulid,
            zdtype: res.data.tblNbsjInnerrule.zdtype,
            attIds: [],
            secrectLevelId: res.data.tblNbsjInnerrule.secrectLevelId,
            staffScopeIds: res.data.tblNbsjInnerrule.staffScopeIds,
            staffScopeNames: res.data.tblNbsjInnerrule.staffScopeNames,
          }

          this.disabled = true
        } else {
          let res = await selectInnerRuleInfo(row.innrulid)
          this.fileTableList = res.data.attList
          this.title = '编辑'
          this.form = {
            orgname: res.data.tblNbsjInnerrule.orgname,
            rulecode: res.data.tblNbsjInnerrule.rulecode,
            rulename: res.data.tblNbsjInnerrule.rulename,
            innruletype: res.data.tblNbsjInnerrule.innruletype,
            rulenumber: res.data.tblNbsjInnerrule.rulenumber,
            status: res.data.tblNbsjInnerrule.status,
            publishorg: res.data.tblNbsjInnerrule.publishorg,
            pulishdate: res.data.tblNbsjInnerrule.publishdate
              ? parseTime(res.data.tblNbsjInnerrule.publishdate, '{y}-{m}-{d}')
              : '',
            bodyinfo: res.data.tblNbsjInnerrule.bodyinfo,
            innrulid: res.data.tblNbsjInnerrule.innrulid,
            zdtype: res.data.tblNbsjInnerrule.zdtype,
            attIds: [],
            secrectLevelId: res.data.tblNbsjInnerrule.secrectLevelId,
            staffScopeIds: res.data.tblNbsjInnerrule.staffScopeIds,
            staffScopeNames: res.data.tblNbsjInnerrule.staffScopeNames,
          }
          this.disabled = false
        }
        this.dialogFormVisible = true
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
        this.fileTableList = []
      },
      handleDepartmentSelected(node) {
        const data = node.id ? node : node.checked
        this.form.orgid = data.id
        this.form.orgname = data.text
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          // this.form.publishdate = this.form.publishdate.substring(0, 11)
          if (valid) {
            let attids = ''
            this.fileTableList.forEach((item) => {
              attids += item.attid + ','
            })
            attids = attids.substring(0, attids.length - 1)
            this.form.attIds = attids
            const { msg } = await mergeInnerRule(this.form)
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
      handleSuccess(response, file, fileList) {
        if (response && response.data) {
          this.fileTableList.push(response.data)
        } else {
          this.$message.error('上传失败！')
        }
      },
      //删除选中的已上传的文件
      // handleDelFile() {
      //   let newArr = this.multipleSelectionFile.map((item) => {
      //     return item.id
      //   })
      //   this.form.attIds = this.form.attIds.filter((item) => {
      //     return !newArr.includes(item.id)
      //   })
      // },
      async handleEdit2(row) {
        let list = this.fileTableList
        list = list.filter((item) => item.attid != row.attid)
        this.fileTableList = list
        await deleteManageFile({ attId: row.attid })

        this.$message({
          type: 'success',
          message: '删除成功!',
        })
      },
      handleSelectionChangeFile(val) {
        this.multipleSelectionFile = val
      },
      // 预览
      // async handlePreview(row, type) {
      //   if (!type) {
      //     try {
      //       const { data } = await getPrivewAttInfo({
      //         attId: row.attid,
      //         attType: 0,
      //       })
      //       return {
      //         url:
      //           data.previewurl +
      //           '?url=' +
      //           encodeURIComponent(Base64.encode(data.ftpUrl)),
      //         type: data.ftpUrl,
      //       }
      //     } catch (error) {
      //       return '' // 或者返回其他默认值
      //     }
      //   } else {
      //     const { data } = await getPrivewAttInfo({
      //       attId: row.attid,
      //       attType: 0,
      //     })
      //     let url = data.ftpUrl.includes('.pdf')
      //       ? data.previewurl +
      //         '?url=' +
      //         encodeURIComponent(Base64.encode(data.ftpUrl)) +
      //         '&officePreviewType=pdf'
      //       : data.previewurl +
      //         '?url=' +
      //         encodeURIComponent(Base64.encode(data.ftpUrl))
      //     window.open(url)
      //   }
      // },

      handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.$set(this.form, 'staffScopeIds', ids)
        this.$set(this.form, 'staffScopeNames', names)
      },
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
