<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="4">
      <el-form
        ref="ruleForm"
        label-width="140px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="formDisabled"
      >
        <el-col :span="12">
          <el-form-item label="序号" prop="sortindex">
            <el-input
              v-model="formData.sortindex"
              clearable
              placeholder="请输入序号"
              :style="{ width: '100%' }"
              type="number"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同编号" prop="completioncode">
            <el-input
              v-model="formData.completioncode"
              clearable
              placeholder="请输入合同编号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="工程或费用名称" prop="completionname">
            <el-input
              v-model="formData.completionname"
              clearable
              placeholder="请输入工程或费用名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="填报单位" prop="completionreportunitname">
            <el-input
              v-model="formData.completionreportunitname"
              readonly
              clearable
              placeholder="请选择填报单位"
              :style="{ width: '78%' }"
            />
            <el-button
              @click="handleObject('tbdw')"
              style="margin-left: 15px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="金额单位" prop="completionamountunit">
            <el-input
              v-model="formData.completionamountunit"
              clearable
              placeholder="请输入金额单位"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="实施单位" prop="completionimplunitname">
            <el-input
              v-model="formData.completionimplunitname"
              readonly
              clearable
              placeholder="请选择实施单位"
              :style="{ width: '78%' }"
            />
            <el-button
              @click="handleObject('ssdw')"
              style="margin-left: 15px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="批复概算投资" prop="completionapproval">
            <el-input
              v-model="formData.completionapproval"
              clearable
              placeholder="请输入批复概算投资"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同金额" prop="contractmoney">
            <el-input
              v-model="formData.contractmoney"
              clearable
              placeholder="请输入合同金额"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="结算金额" prop="completionsetmoney">
            <el-input
              v-model="formData.completionsetmoney"
              clearable
              placeholder="请输入结算金额"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="投资节超" prop="completionjiechao">
            <el-input
              v-model="formData.completionjiechao"
              clearable
              placeholder="请输入投资节超"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="投资节超情况说明" prop="completionjiechaodeal">
            <el-input
              v-model="formData.completionjiechaodeal"
              clearable
              type="textarea"
              :rows="6"
              placeholder="请输入投资节超情况说明"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <!-- <el-col :span="12">
          <el-form-item label="创建人" prop="createUser">
            <el-input
              v-model="formData.createUser"
              disabled
              placeholder="请输入创建人"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间" prop="createTime">
            <el-date-picker
              v-model="formData.createTime"
              placeholder="请输入创建时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col> -->

        <!-- <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div
            style="text-align: right; margin-bottom: 5px"
            v-if="!formDisabled"
          >
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + uploadApi"
              :headers="headers"
              :on-success="handleSuccess"
              :file-list="tableData"
            >
              <el-button type="success">上传</el-button>
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
                  @click="handleDown(row)"
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
                  @click="handleDeleteFile(row)"
                  v-if="!formDisabled"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col> -->
      </el-form>
    </el-row>
    <div slot="footer" v-if="!formDisabled">
      <el-button @click="close">取消</el-button>
      <el-button @click="submit" type="primary" v-loading="loading">
        确定
      </el-button>
    </div>
    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />
  </el-dialog>
</template>

<script>
  import { saveCompletion } from '@/oapi/audit/jhbz'

  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import store from '@/store'
  const token = store.getters['user/token']
  const { baseURL } = require('@/config')
  export default {
    components: { SelectDepartment },
    data() {
      return {
        baseApi: baseURL,
        uploadApi: '/oiaudit/fileManage/upload',
        headers: { token: token },
        dialogFormVisible: false,
        title: '新增',
        loading: false,
        formData: {
          sortindex: '',
          completionapproval: '',
          completioncode: '',
          completionimplunitid: '',
          completionimplunitname: '',
          completionjiechao: '',
          completionjiechaodeal: '',
          completionname: '',
          completionsetmoney: '',
          contractmoney: '',
          completionreportunitname: '',
          completionreportunitid: '',
          completionamountunit: '',
        },
        rules: {
          completioncode: { required: true, message: '请输入合同编号' },
        },
        formDisabled: false,
        tableData: [],
      }
    },

    mounted() {},

    methods: {
      handleObject(type) {
        this.type = type
        this.$refs['audiTree'].showEdit()
      },
      showEdit(type, data) {
        this.dialogFormVisible = true

        if (type == 'add') {
          this.title = '新增'
          this.formData.createUser = JSON.parse(
            localStorage.getItem('userInfo')
          ).realname
          this.formData.createTime = new Date().toJSON().split('T')[0]
        } else if (type == 'edit') {
          this.title = '修改'
          this.formData = { ...data }
        } else if (type == 'detail') {
          this.title = '详情'
          this.formData = { ...data }
          this.formDisabled = true
        }
      },
      async submit() {
        this.$refs.ruleForm.validate((valid) => {
          if (!valid) return
          this.saveData()
          this.close()
        })
      },
      async saveData() {
        const { data, msg, code } = await saveCompletion(this.formData)
        if (code == 1) {
          this.$message.success(msg)
          this.close()
          this.$emit('fetchData')
        }
      },
      close() {
        this.formDisabled = false
        this.dialogFormVisible = false
        this.formData = this.$options.data().formData
        this.$refs['ruleForm'].resetFields()
      },
      getDepartmentInfo(val) {
        if (this.type == 'ssdw') {
          this.formData.completionimplunitname = val.label
          this.formData.completionimplunitid = val.id
        } else {
          this.formData.completionreportunitname = val.label
          this.formData.completionreportunitid = val.id
        }
      },
      handleSuccess(file) {
        // 父编辑上传回显
        if (file.result === 200) {
          this.tableData.push(file.data)
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      async handleDown(row) {
        // 下载
        const data = await download({ attId: row.attid })
        let filename = row.attname
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
      async handleDeleteFile(row) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          //删除对应的id
          let res = await deleteReportFile({ attId: row.attid })
          if (res.msg === '成功') {
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
            let list = this.tableData
            list = list.filter((item) => item.attid != row.attid)
            this.tableData = list
          } else {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
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
          attId: row.attid,
          attType: 2,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url })
      },
    },
  }
</script>

<style lang="less" scoped></style>
