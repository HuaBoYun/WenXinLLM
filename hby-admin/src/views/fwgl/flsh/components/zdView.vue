<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :modal="false"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item
            label="制度名称"
            label-width="140px"
            prop="institutionName"
          >
            <el-input
              v-model="formData.institutionName"
              clearable
              placeholder="请输入制度名称"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="制度分类"
            label-width="140px"
            prop="institutionType"
          >
            <el-select
              v-model="formData.institutionType"
              placeholder="请选择制度分类"
              :style="{ width: '100%' }"
              :disabled="!footer"
            >
              <el-option
                v-for="item in zdflOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" label-width="140px" prop="remark">
            <el-input
              v-model="formData.remark"
              clearable
              type="textarea"
              rows="2"
              placeholder="请输入备注"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseURL + uploadApi"
              :headers="headers"
              :on-preview="handlePreview"
              :on-success="handleSuccess"
              :file-list="tableData"
            >
              <el-button type="success">上传</el-button>
            </el-upload>
          </div>
          <el-table :data="tableData">
            <el-table-column align="center" label="附件名称" prop="fileName" />
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
              width="200"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handlePreviewFile(row)">
                  预览
                </el-button>
                <el-button type="text" @click="handleDown(row)">下载</el-button>
                <el-button type="text" @click="handleDelete(row)">
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
  </el-dialog>
</template>

<script>
  import { uploadApi, fetchApi, legalReviewInstitution } from '@/api/fwgl/api'
  import { download } from '@/api/fwgl/zzxx'
  import { baseURL } from '@/config'
  import store from '@/store'
  const token = store.getters['user/token']
  import { getPrivewAttInfo, getZDDefaultInfo } from '@/api/fwgl/gsls'
  const { addZJ, getZJDetail } = legalReviewInstitution
  export default {
    name: 'SummanyInfo',
    components: {},
    inheritAttrs: false,
    props: [],
    data() {
      return {
        baseURL: baseURL,
        uploadApi: uploadApi,
        headers: { token: token },
        tableData: [],
        formData: {
          institutionName: '',
          institutionType: '',
          remark: '',
          institutionAuditExtId: '',
        },
        footer: true,
        rules: {
          institutionName: [
            { required: true, message: '请输入制度名称', trigger: 'blur' },
          ],
          institutionType: [
            { required: true, message: '请选择制度分类', trigger: 'blur' },
          ],
        },
        zdflOptions: [
          {
            value: 1,
            label: '经营管理类-基本制度',
          },
          {
            value: 2,
            label: '经营管理类-重要制度',
          },
          {
            value: 3,
            label: '经营管理类-一般制度',
          },
          {
            value: 4,
            label: '非经营管理类',
          },
        ],
        dialogFormVisible: false,
        title: '新增',
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      /**
       * @description: 下载文件
       * @param {*} row
       * @return {*}
       */      
      async handleDown(row) {
        const data = await download({ fileId: row.fileId })
        let filename = row.fileName
        let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
        let url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', filename)
        document.documentElement.appendChild(link)
        link.click()
        document.documentElement.removeChild(link)
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */      
      handleDelete(row) {
        const { fileId } = row
        const i = this.tableData.findIndex((x) => x.fileId === fileId)
        if (i > -1) {
          this.tableData.splice(i, 1)
        }
      },
      handlePreview() {},
      /**
       * @description: 上传成功回调
       * @param {*} res
       * @return {*}
       */      
      handleSuccess(res) {
        if (res && res.data) {
          this.tableData = this.tableData.concat(res.data.fileIds || [])
        }
      },
      /**
       * @description: 外部打开dialog
       * @param {*} title 表单类型
       * @param {*} row 行数据
       * @param {*} institutionAuditId
       * @return {*}
       */      
      async showEdit(title, row, institutionAuditId) {
        this.dialogFormVisible = true

        if (row) {
          const res = await getZDDefaultInfo({ id: row.institutionAuditExtId })
          this.formData = {
            institutionName: res.data.institutionAuditExt.institutionName,
            institutionType: res.data.institutionAuditExt.institutionType,
            remark: res.data.institutionAuditExt.remark,
            institutionAuditExtId:
              res.data.institutionAuditExt.institutionAuditExtId,
          }
          this.tableData = res.data.files || []
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else {
          this.title = '新增'
          this.formData = this.$options.data().formData
        }
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.dialogFormVisible = false
        this.formData = {}
        this.tableData = []
        this.footer = true
      },
      /**
       * @description: 保存表单
       * @return {*}
       */      
      add() {
        const aa = []
        this.tableData.forEach((e) => {
          aa.push(e.fileId)
        })
        this.formData.fileIds = aa.toString()
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const res = await fetchApi(addZJ, this.formData)

            if (res.code === 200) {
              this.$message.success('成功')
              this.$emit('refush', [res.data])
              this.close()
            }
          } else {
            return false
          }
        })
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
