<template>
  <div>
    <el-row :gutter="4">
      <el-form
        ref="ruleForm"
        label-width="120px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="formDisabled"
      >
        <el-col :span="12">
          <el-form-item label="序号" prop="suggestionNo">
            <el-input
              v-model="formData.suggestionNo"
              clearable
              disabled
              placeholder="请输入底稿编号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label-width="230px"
            label="重点关注领域、项目、事项和风险"
            prop="concerns"
          >
            <el-input
              v-model="formData.concerns"
              clearable
              placeholder="请输入重点关注领域、项目、事项和风险"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="关注内容" prop="concernsContent">
            <el-input
              v-model="formData.concernsContent"
              clearable
              placeholder="请输入关注内容"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="单位" prop="organizationName">
            <el-input
              v-model="formData.organizationName"
              clearable
              placeholder="请选择单位"
              :style="{ width: '70%' }"
            />
            <el-button
              @click="handleObject"
              style="margin-left: 10px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="项目类型" prop="projectType">
            <el-select
              v-model="formData.projectType"
              placeholder="请选择项目类型"
              :style="{ width: '100%' }"
            >
              <el-option label="工程类" value="工程类" />
              <el-option label="财务类" value="财务类" />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="备注" prop="remark">
            <el-input
              v-model="formData.remark"
              clearable
              placeholder="请输入备注"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人" prop="createUserName">
            <el-input
              v-model="formData.createUserName"
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
        </el-col>
      </el-form>
    </el-row>
    <div style="text-align: right; margin-top: 10px" v-if="!formDisabled">
      <el-button type="primary" @click="add">确定</el-button>
      <el-button type="primary" @click="ymsubmit">提交</el-button>
    </div>
    <Resubmit
      ref="resubmit"
      @fetchClose="close"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      :status="status"
    />
    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />
  </div>
</template>

<script>
  import { download, deleteFile } from '@/api/audit/implement'
  import { saveOrUpdate, detail } from '@/api/oilAudit/jhgl/xqjyb'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'

  export default {
    name: 'xqjybEdit',
    inheritAttrs: false,
    components: {
      SelectDepartment,
      Resubmit: () => import('@/views/msg/components/options/Resubmit.vue'),
    },
    props: [],
    data() {
      return {
        loading: false,
        formData: {
          concerns: undefined,
          suggestionNo: undefined,
          concernsContent: undefined,
          organization: {},
          organizationName: undefined,
          remark: undefined,
          createUserName: undefined,
          createTime: undefined,
        },
        formDisabled: true,
        tableData: [],
        rules: {
          // concerns: [
          //   {
          //     required: true,
          //     message: '请输入底稿编号',
          //     trigger: 'blur',
          //   },
          // ],
          // field2: [
          //   {
          //     required: true,
          //     message: '请输入建设单位',
          //     trigger: 'blur',
          //   },
          // ],
          // projectType: [
          //   {
          //     required: true,
          //     message: '请输入项目类别',
          //     trigger: 'blur',
          //   },
          // ],
        },
        flowtaskinfoflowid: '',
        fromId: '',
        ymFromId: '',
        fromIdcopy: '',
        status: '',
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      ymsubmit() {
        this.$refs['ruleForm'].validate((valid) => {
          if (valid) this.$refs.resubmit.ymsubmit()
        })
      },
      showEdit(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status
      ) {
        this.formDisabled = title === 'detail'
        this.fromId = formId
        this.ymFromId = ymFromId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.fromIdcopy = formId
        this.status = status
        this.fetchData({ id: formId })
      },
      async fetchData(row) {
        this.loading = true
        const res = await detail({ id: row.id })
        this.loading = false
        if (res && res.code == 1) {
          Object.assign(this.formData, res.data.data)
          this.formData.createUserName = res.data.data.createUser.realname
          this.formData.organizationName = res.data.data.organization.orgname
        } else {
          this.$message.error(res.msg || '操作失败！')
        }
      },
      close() {
        this.formData = {
          concerns: undefined,
          suggestionNo: undefined,
          concernsContent: undefined,
          organization: {},
          organizationName: undefined,
          projectType: undefined,
          remark: undefined,
          createUserName: undefined,
          createTime: undefined,
        }
        this.tableData = []
        this.formDisabled = true
        this.$bus.$emit('updateMsg', 0)
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            const res = await saveOrUpdate(this.formData)
            this.loading = false
            if (res && res.code == 1) {
              this.$message.success('保存成功！')
              this.$emit('fetch-data')
            } else {
              this.$message.error(res.msg || '保存失败！')
            }
          } else {
            return false
          }
        })
      },
      async handleDown(row) {
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
      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        this.tableData = list
        await deleteFile({ attId: row.attid })
      },
      handlePreview(file) {},
      handleSuccess(file) {
        if (file.result == '200') {
          let list = this.tableData
          list.push(file.data)
          this.tableData = list
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      handleObject() {
        this.$refs['audiTree'].showEdit()
      },
      getDepartmentInfo(val) {
        this.formData.organization = { orgid: val.id }
        this.formData.organizationName = val.label
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
