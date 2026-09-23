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
              @input="handleInput1($event, 'sortindex')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同编号" prop="settlementcode">
            <el-input
              v-model="formData.settlementcode"
              clearable
              placeholder="请输入合同编号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="工程名称" prop="settlementname">
            <el-input
              v-model="formData.settlementname"
              clearable
              placeholder="请输入工程名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="建设单位" prop="settlementunitname">
            <el-input
              v-model="formData.settlementunitname"
              readonly
              clearable
              placeholder="请选择建设单位"
              :style="{ width: '78%' }"
            />
            <el-button
              @click="handleObject('jsdw')"
              style="margin-left: 15px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="二审审查金额" prop="settlementinstanceamount">
            <el-input
              v-model="formData.settlementinstanceamount"
              clearable
              @input="(value) => handleInput(value, 'settlementinstanceamount')"
              placeholder="请输入二审审查金额"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="施工单位" prop="settlementconsttunitname">
            <el-input
              v-model="formData.settlementconsttunitname"
              readonly
              clearable
              placeholder="请选择施工单位"
              :style="{ width: '78%' }"
            />
            <el-button
              @click="handleObject('sgdw')"
              style="margin-left: 15px"
              type="primary"
            >
              选择
            </el-button>
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
  import { saveOrUpdate } from '@/oapi/audit/jhbz'
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
          settlementcode: '',
          settlementconsttunitid: '',
          settlementconsttunitname: '',
          settlementid: '',
          settlementinstanceamount: '',
          settlementname: '',
          settlementunitid: '',
          settlementunitname: '',
        },
        rules: {
          settlementcode: { required: true, message: '请输入合同编号' },
        },
        formDisabled: false,
        tableData: [],
      }
    },

    mounted() {},

    methods: {
      handleInput1(event, key) {
        // 允许数字和小数点
        const regex = /^\d*\.?\d*$/
        // 如果输入不匹配，就撤回到上一个合法的值
        if (!regex.test(event)) {
          // 这里假设你已经有一个变量 value 来绑定输入的值
          this.formData[key] = this.formData[key]
            .replace(/[^0-9.]/g, '')
            .replace(/(\..*)\./g, '$1')
        }
      },
      handleObject(type) {
        this.type = type
        this.$refs['audiTree'].showEdit()
      },
      showEdit(type, data) {
        console.log('🚀 ~ showEdit ~ data:', data)
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
        const { data, msg, code } = await saveOrUpdate(this.formData)
        if (code == 1) {
          this.$message.success(msg)
          this.close()
          this.$emit('fetchData')
        }
      },
      close() {
        this.dialogFormVisible = false
        this.formDisabled = false
        this.formData = this.$options.data().formData
      },
      getDepartmentInfo(val) {
        if (this.type == 'jsdw') {
          this.formData.settlementunitname = val.label
          this.formData.settlementunitid = val.id
        } else {
          this.formData.settlementconsttunitname = val.label
          this.formData.settlementconsttunitid = val.id
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
      handleInput(value, key) {
        // 清除"数字"和"."以外的字符
        value = value.replace(/[^\d.]/g, '')
        // 只保留第一个, 清除多余的
        value = value.replace(/\.{2,}/g, '.')
        // 只能输入小数点后两位
        value = value.replace('.', '$#$').replace(/\./g, '').replace('$#$', '.')

        while (value.indexOf('.') !== -1 && value.split('.')[1].length > 2) {
          value = value.slice(0, value.length - 1)
        }
        this.formData[key] = value
      },
    },
  }
</script>

<style lang="less" scoped></style>
