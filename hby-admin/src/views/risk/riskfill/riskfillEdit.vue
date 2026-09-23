<template>
  <div>
    <el-row :gutter="4">
      <el-form
        ref="ruleForm"
        label-width="140px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="formDisabled"
        style="display: flex; flex-wrap: wrap"
      >
        <el-col :span="12">
          <el-form-item label="涉及企业名称" prop="entName">
            <el-input
              v-model="formData.entName"
              clearable
              placeholder="请输入涉及企业名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="涉及企业层级" prop="entLevel">
            <el-input
              v-model="formData.entLevel"
              clearable
              placeholder="请输入涉及企业层级"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="风险事件名称" prop="riskName">
            <el-input
              v-model="formData.riskName"
              clearable
              placeholder="请输入风险事件名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="风险类别" prop="riskType">
            <el-input
              v-model="formData.riskType"
              clearable
              placeholder="请输入风险类别"
              :style="{ width: '100%' }"
            />
            <!-- <el-select
            v-model="formData.riskType"
            placeholder="请选择风险类别"
            :style="{ width: '100%' }"
          >
            <el-option label="类别一" value="类别一" />
            <el-option label="类别二" value="类别二" />
          </el-select> -->
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="事件发生时间" prop="eveTime">
            <el-date-picker
              v-model="formData.eveTime"
              placeholder="请输入事件发生时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="当期情况描述" prop="sitDetails">
            <el-input
              v-model="formData.sitDetails"
              clearable
              placeholder="请输入当期情况描述"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="损失（风险）金额" prop="lossAmount">
            <el-input
              v-model="formData.lossAmount"
              clearable
              placeholder="请输入损失（风险）金额（万元）"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="处理进展情况" prop="disSituation">
            <el-input
              v-model="formData.disSituation"
              clearable
              placeholder="请输入处理进展情况"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否涉诉" prop="isIvn">
            <el-select
              v-model="formData.isIvn"
              placeholder="请选择是否涉诉"
              :style="{ width: '100%' }"
            >
              <el-option label="是" value="是" />
              <el-option label="否" value="否" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否境外" prop="isOver">
            <el-select
              v-model="formData.isOver"
              placeholder="请选择是否境外"
              :style="{ width: '100%' }"
            >
              <el-option label="是" value="是" />
              <el-option label="否" value="否" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="remarks">
            <el-input
              v-model="formData.remarks"
              clearable
              placeholder="请输入备注"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>附件</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-top: 5px" v-if="!formDisabled">
            <!-- <el-upload
            class="upload-demo"
            :show-file-list="false"
            :action="baseApi + api"
            :headers="headers"
            :on-success="handleSuccess"
          >
            <el-button type="success">上传</el-button>
          </el-upload> -->
            <el-upload
              class="upload-demo"
              :action="baseApi + api"
              :show-file-list="false"
              :data="{
                moduleType: 'ZDFX',
                moduleId: '123',
              }"
              :on-success="handleSuccess"
              :headers="headers"
            >
              <el-button type="success" :loading="uploadLoading">
                上传
              </el-button>
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
              width="200"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleDownload(row)">
                  下载
                </el-button>
                <el-button type="text" @click="handlePreviewFile(row)">
                  预览
                </el-button>
                <el-button type="text" @click="handleDelete(row)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <div v-if="!formDisabled" style="text-align: right; margin-top: 20px">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary" v-loading="loading">
        确定
      </el-button>
    </div>
    <!-- <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" /> -->
  </div>
</template>

<script>
  import {
    riskReportingSaveOrUpdate,
    riskReportingDetails,
    getRiskReportingAttInfo,
  } from '@/api/risk/riskfill'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  // import SelectDepartment from './department.vue'
  import store from '@/store'
  import { formatDay } from '@/utils/index'
  const { baseURL } = require('@/config')
  import { downFieldById, getGroupPlanCode } from '@/api/risk/riskEvents'

  export default {
    name: 'xqjybEdit',
    inheritAttrs: false,
    // components: { SelectDepartment },
    props: {
      curRow: {
        type: Object,
        default: () => ({}),
      },
    },
    data() {
      return {
        loading: false,
        formData: {
          id: '',
          entName: '',
          entLevel: '',
          riskName: '',
          riskType: '',
          eveTime: '',
          sitDetails: '',
          lossAmount: '',
          disSituation: '',
          isIvn: '',
          isOver: '',
          remarks: '',
        },
        id: '',
        formDisabled: false,
        rules: {
          entName: [
            { required: true, message: '请输入涉及企业名称', trigger: 'blur' },
          ],
          entLevel: [
            { required: true, message: '请输入涉及企业层级', trigger: 'blur' },
          ],
          riskName: [
            { required: true, message: '请输入风险事件名称', trigger: 'blur' },
          ],
          riskType: [
            { required: true, message: '请选择风险类别', trigger: 'change' },
          ],
          eveTime: [
            {
              required: true,
              message: '请选择事件发生时间',
              trigger: 'change',
            },
          ],
          sitDetails: [
            { required: true, message: '请输入当期情况描述', trigger: 'blur' },
          ],
          lossAmount: [
            {
              required: true,
              message: '请输入损失（风险）金额（万元）',
              trigger: 'blur',
            },
          ],
          disSituation: [
            { required: true, message: '请输入处理进展情况', trigger: 'blur' },
          ],
          isIvn: [
            { required: true, message: '请选择是否涉诉', trigger: 'change' },
          ],
          isOver: [
            { required: true, message: '请选择是否境外', trigger: 'change' },
          ],
          remarks: [{ required: true, message: '请输入备注', trigger: 'blur' }],
        },
        baseApi: baseURL,
        // api: '/riskcontrol/attachment/uploadFileAttInfo',
        api: '/riskcontrol/attachment/uploadFileAttInfo',
        headers: {
          'X-Requested-With': 'XMLHttpRequest',
          token: store.getters['user/token'],
        },
        fileList: [],
        removeIds: [],
        tableDataFile: [],
        fileIdList: [],
        uploadLoading: false,
      }
    },
    mounted() {
      if (this.curRow) {
        this.fetchData()
      }
    },
    methods: {
      async fetchData(row) {
        this.formDisabled = this.curRow.disabled
        const res = await riskReportingDetails({ id: this.curRow.id })
        const fileRes = await getRiskReportingAttInfo({ id: this.curRow.id })
        // const res = await riskReportingDetails({ id: '1852903094876131300' })
        console.log('fileRes', fileRes)
        if (res && res.data && res.data.data && res.data.data.pageInfo) {
          const formData = res.data.data.pageInfo
          Object.assign(this.formData, formData)
        }
      },
      close() {
        this.$emit('close')
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            const attIds = this.tableDataFile.map((x) => x.attid).join(',')
            let params = { ...this.formData, attIds: attIds }
            const res = await riskReportingSaveOrUpdate(params)
            this.loading = false
            if (res && res.code == 1) {
              this.$message.success('保存成功！')
              this.$emit('fetchData')
              this.close()
            } else {
              this.$message.error(res.msg || '保存失败！')
            }
          }
        })
      },
      handleObject() {
        this.$refs['audiTree'].showEdit()
      },
      getDepartmentInfo(val) {
        this.formData.organizationId = val.id
        this.formData.organizationName = val.name
      },
      handleObject() {
        this.$refs['audiTree'].showEdit()
      },
      getDepartmentInfo(val) {
        this.formData.organizationId = val.id
        this.formData.organizationName = val.name
      },
      /**
       * @description: 下载
       * @return {*}
       */
      async handleDownload(row) {
        console.log('downlaod', row)
        const res = await downFieldById({ id: row.attid })
        console.log(res)
        if (!res) return
        let filename = row.attname
        let blob = new Blob([res]) //res即为blob数据，请注意自己的数据形式
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
       * @description: 删除附件
       * @return {*}
       */
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, () => {
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
        })
      },
      /**
       * @description: 上传成功
       * @return {*}
       */
      handleSuccess(e) {
        console.log(e, 'hengheng')
        if (e.result === 200) {
          // this.showEdit(this.showRow)
          let attInfo = {}
          attInfo = e.data
          this.fileIdList.push(attInfo.attid)
          this.tableDataFile.push(attInfo)
          this.$baseMessage('上传成功', 'success', 'vab-hey-message-success')
        } else {
          this.$baseMessage('上传失败', 'error', 'vab-hey-message-error')
        }
        this.uploadLoading = false
      },
      /**
       * @description: 预览
       * @return {*}
       */
      async handlePreviewFile(row) {
        const { data } = await getPrivewAttInfo({
          //此接口通用
          attId: row.attid,
          attType: 2,
        })

        window.open(
          data.previewurl +
            '?url=' +
            encodeURIComponent(Base64.encode(data.ftpUrl))
        )
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
