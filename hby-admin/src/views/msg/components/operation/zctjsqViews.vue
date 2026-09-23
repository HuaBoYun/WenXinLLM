<template>
  <div>
    <el-row :gutter="14" v-loading="loading">
      <el-form
        ref="ruleForm"
        label-width="135px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="!footer"
      >
        <el-col :span="12" style="height: 29px">
          <el-form-item label="资产名称" prop="assetName">
            <el-input
              v-model="formData.assetName"
              :style="{ width: '75%' }"
              disabled
              placeholder="请选择资产"
            />
            <el-button
              type="primary"
              style="margin-left: 20px"
              @click="$refs.zichan.show()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="规格型号" prop="specificationType">
            <el-input
              v-model="formData.specificationType"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入规格型号"
              disabled
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="计量单位" prop="measurement">
            <el-input
              v-model="formData.measurement"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入计量单位"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数量" prop="quantity">
            <el-input
              v-model="formData.quantity"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入数量"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="复合数量" prop="compositeQuantity">
            <el-input
              v-model="formData.compositeQuantity"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入复合数量"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="投产日期" prop="productionTime">
            <el-date-picker
              v-model="formData.productionTime"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="选择投产日期"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="停用时间" prop="discontinuedTime">
            <el-date-picker
              v-model="formData.discontinuedTime"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="选择停用时间"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="效用年限" prop="utilityAgeTime">
            <el-date-picker
              v-model="formData.utilityAgeTime"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="选择效用年限"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="已用年限" prop="useAgeTime">
            <el-date-picker
              v-model="formData.useAgeTime"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="选择已用年限"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="在用单位" prop="useBelongGroupName">
            <el-input
              v-model="formData.useBelongGroupName"
              :style="{ width: '75%' }"
              clearable
              disabled
              placeholder="请选择在用单位"
            />
            <el-button
              type="primary"
              style="margin-left: 20px"
              @click="chooseDept('onUse')"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="原单位" prop="originalBelongGroupName">
            <el-input
              v-model="formData.originalBelongGroupName"
              :style="{ width: '75%' }"
              clearable
              disabled
              placeholder="请选择原单位"
            />
            <el-button
              type="primary"
              style="margin-left: 20px"
              @click="chooseDept('old')"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="原使用单位" prop="originalUseBelongGroupName">
            <el-input
              v-model="formData.originalUseBelongGroupName"
              :style="{ width: '75%' }"
              clearable
              disabled
              placeholder="请选择原使用单位"
            />
            <el-button
              type="primary"
              style="margin-left: 20px"
              @click="chooseDept('oldUse')"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col> -->
        <el-col :span="12">
          <el-form-item label="使用人员" prop="useStaffName">
            <el-input
              v-model="formData.useStaffName"
              :style="{ width: '75%' }"
              clearable
              disabled
              placeholder="请选择使用人员"
            />
            <el-button
              type="primary"
              style="margin-left: 20px"
              @click="choosePerson()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="待调剂单位" prop="stayAdjustedBelongGroupName">
            <el-input
              v-model="formData.stayAdjustedBelongGroupName"
              :style="{ width: '75%' }"
              clearable
              disabled
              placeholder="请选择待调剂单位"
            />
            <el-button
              type="primary"
              style="margin-left: 20px"
              @click="chooseDept('wait')"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="原值" prop="originalValue">
            <el-input
              v-model="formData.originalValue"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="remark">
            <el-input
              v-model="formData.remark"
              type="textarea"
              :style="{ width: '100%' }"
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
                <el-button type="text" @click="handleDelete(row)" v-if="footer">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <div style="text-align: right; margin-top: 10px" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
      <el-button type="primary" @click="ymsubmit">提 交</el-button>
    </div>
    <ZichanDialog ref="zichan" @selected="handleZichanSelected" />
    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />
    <Person ref="person" @projectManage="getPersonSelect" />
    <!-- 流程相关 -->
    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
  </div>
</template>

<script>
  import { baseURL } from '@/config'
  import store from '@/store'
  import { editInfor, getInfoDetail } from '@/oapi/ypns_zhgl/zctjsq.js'
  import { deleteFileInfo } from '@/oapi/ypns_zhgl/filePublic'
  import { download } from '@/oapi/audit/report'
  import ZichanDialog from '@/views/oilAudit/zhgl/components/option/zichanDialog.vue'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import Person from '@/components/selectPerson.vue'
  const token = store.getters['user/token']
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    components: { ZichanDialog, SelectDepartment, Resubmit, Person },
    inheritAttrs: false,
    props: ['fetchData'],
    data() {
      return {
        loading: false,
        // baseApi: baseURL,
        // api: '/oiaudit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        fileList: [],
        baseApi: baseURL,
        importApi: '/audit/auditPlan/mergePlanProjectManageInfoImport',
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        tableData: [],
        formData: {
          assetMgtId: '',
          assetName: '',
          specificationType: '',
          measurement: '',
          quantity: '',
          compositeQuantity: '',
          productionTime: '',
          discontinuedTime: '',
          utilityAgeTime: '',
          useAgeTime: '',
          useBelongGroup: '',
          useBelongGroupName: '',
          // originalBelongGroup: '',
          // originalBelongGroupName: '',
          // originalUseBelongGroup: '',
          // originalUseBelongGroupName: '',
          useStaff: '',
          useStaffName: '',
          stayAdjustedBelongGroup: '',
          stayAdjustedBelongGroupName: '',
          originalValue: '',
          remark: '',
          fileIds: '',
          id: '',
        },
        deptType: '',
        footer: true,
        rules: {},
        dialogFormVisible: false,
        title: '新增',
        // 流程相关
        fromId: '',
        fromIdcopy: '',
        flowtaskinfoflowid: '',
        ymFromId: '',
        status: '',
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      chooseDept(type) {
        this.deptType = type
        this.$refs.audiTree.showEdit()
      },
      choosePerson() {
        this.$refs.person.showEdit()
      },
      getPersonSelect(val) {
        this.formData.useStaffName = val[0].realname
        this.formData.useStaff = val[0].staffid
      },
      // 选择单位
      getDepartmentInfo(node) {
        console.log(node)
        if (this.deptType == 'onUse') {
          this.$set(this.formData, 'useBelongGroupName', node.label)
          this.$set(this.formData, 'useBelongGroup', node.id)
        } else if (this.deptType == 'old') {
          this.$set(this.formData, 'originalBelongGroupName', node.label)
          this.$set(this.formData, 'originalBelongGroup', node.id)
        } else if (this.deptType == 'oldUse') {
          this.$set(this.formData, 'originalUseBelongGroupName', node.label)
          this.$set(this.formData, 'originalUseBelongGroup', node.id)
        } else if (this.deptType == 'wait') {
          this.$set(this.formData, 'stayAdjustedBelongGroupName', node.label)
          this.$set(this.formData, 'stayAdjustedBelongGroup', node.id)
        }
      },
      // 资产选择
      handleZichanSelected(node) {
        this.$set(this.formData, 'assetName', node.assetName)
        this.$set(this.formData, 'assetMgtId', node.id)
        this.formData.specificationType = node.specificationType
        this.formData.productionTime = node.productionTime
        this.formData.originalValue = node.finalOriginalValue
        this.formData.measurement = node.measurement
        this.formData.compositeQuantity = node.compositeQuantity
      },
      getCurrentDate() {
        return new Date(+new Date() + 8 * 3600 * 1000)
          .toJSON()
          .substr(0, 19)
          .replace('T', ' ')
      },
      async showEdit(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status
      ) {
        this.footer = title == 'detail' ? false : true
        // this.footer = isWfqdedit

        if (formId) {
          const res = await getInfoDetail({ id: formId })
          this.formData.assetMgtId = res.data.data.assetMgtId
          this.formData.assetName = res.data.data.assetName
          this.formData.specificationType = res.data.data.specificationType
          this.formData.measurement = res.data.data.measurement
          this.formData.quantity = res.data.data.quantity
          this.formData.compositeQuantity = res.data.data.compositeQuantity
          this.formData.productionTime = res.data.data.productionTime
          this.formData.discontinuedTime = res.data.data.discontinuedTime
          this.formData.utilityAgeTime = res.data.data.utilityAgeTime
          this.formData.useAgeTime = res.data.data.useAgeTime
          this.formData.useBelongGroup = res.data.data.useBelongGroup
          this.formData.useBelongGroupName = res.data.data.useBelongGroupName
          this.formData.originalBelongGroup = res.data.data.originalBelongGroup
          this.formData.useStaffName = res.data.data.useStaffName
          this.formData.useStaff = res.data.data.useStaff
          // this.formData.originalBelongGroupName =
          //   res.data.data.originalBelongGroupName
          // this.formData.originalUseBelongGroup =
          //   res.data.data.originalUseBelongGroup
          // this.formData.originalUseBelongGroupName =
          //   res.data.data.originalUseBelongGroupName
          // this.formData.stayAdjustedBelongGroup =
          //   res.data.data.stayAdjustedBelongGroup
          this.formData.stayAdjustedBelongGroupName =
            res.data.data.stayAdjustedBelongGroupName
          this.formData.originalValue = res.data.data.originalValue
          this.formData.remark = res.data.data.remark
          this.formData.id = res.data.data.id
          this.tableData = res.data?.file || []
        }

        // 流程相关
        this.fromId = formId
        this.fromIdcopy = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
      },
      close() {
        this.loading = false
        this.$bus.$emit('updateMsg', 0)
        this.formData = {
          assetMgtId: '',
          assetName: '',
          specificationType: '',
          measurement: '',
          quantity: '',
          compositeQuantity: '',
          productionTime: '',
          discontinuedTime: '',
          utilityAgeTime: '',
          useAgeTime: '',
          useBelongGroup: '',
          useBelongGroupName: '',
          // originalBelongGroup: '',
          // originalBelongGroupName: '',
          // originalUseBelongGroup: '',
          // originalUseBelongGroupName: '',
          useStaff: '',
          useStaffName: '',
          stayAdjustedBelongGroup: '',
          stayAdjustedBelongGroupName: '',
          originalValue: '',
          remark: '',
          fileIds: '',
          id: '',
        }
        this.tableData = []
        this.dialogFormVisible = false
        this.footer = true
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            // return
            const params = JSON.parse(JSON.stringify(this.formData))
            let fileIds = ''
            this.tableData.map((item) => {
              fileIds += item.attid
              fileIds += ','
            })
            fileIds = fileIds.substring(0, fileIds.length - 1)
            const res = await editInfor({
              ...params,
              fileIds,
            })
            if (res && res.code == 200) {
              this.$message({
                message: '提交成功！',
                type: 'success',
              })
            } else {
              this.$message({
                message: '提交失败',
                type: 'error',
              })
            }
          }
        })
      },
      // 附件列表 下载附件
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
      // 附件列表 删除附件
      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        await deleteFileInfo({ id: row.attid })
        this.tableData = list
      },
      // handlePreview(file) {},
      // // 附件上传成功
      // handleSuccess(file) {
      //   if (file.result == 200) {
      //     let list = this.tableData
      //     list.push(file.data)
      //     this.tableData = list
      //     this.$baseMessage(file.msg, 'success')
      //   } else {
      //     this.$baseMessage(file.msg, 'error')
      //   }
      // },
      async checkLink() {
        return
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const res = await LinkTest({
              dataBaseConnectionAddress:
                this.formData.dataBaseConnectionAddress,
              dataBasePassWord: this.formData.dataBasePassWord,
              dataBaseType: this.formData.dataBaseType,
              dataBaseUsers: this.formData.dataBaseUsers,
            })
            if (res.code == 1) {
              this.$message({
                message: res.msg,
                type: 'success',
              })
            } else {
              this.$message({
                message: res.msg,
                type: 'error',
              })
            }
          }
        })
      },
      // 流程相关-提交
      async ymsubmit() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            this.$refs.resubmit.ymsubmit()
          }
        })
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
        // 调用自定义上传函数
        new Promise((resolve, reject) => {
          customUpload({
            baseApi: this.baseApi,
            api: this.api,
            key: window.key,
            iv: window.iv,
            headers: this.headers,
            fileList: fileList, // 使用 fileList 而不是 file
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
<style scoped>
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
