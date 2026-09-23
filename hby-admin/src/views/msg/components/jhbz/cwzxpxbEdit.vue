<template>
  <!-- 财务专项 edit -->
  <div>
    <el-row :gutter="4" v-loading="loading">
      <el-form
        ref="ruleForm"
        label-width="230px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="formDisabled"
      >
        <el-col :span="12">
          <el-form-item label="建议科室" prop="suggestDeptId">
            <el-input
              v-model="formData.suggestDeptId"
              clearable
              placeholder="请输入建议科室"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="排序" prop="sort">
            <el-input
              v-model="formData.sort"
              clearable
              placeholder="请输入排序"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计项目名称" prop="projectName">
            <el-input
              v-model="formData.projectName"
              clearable
              placeholder="请输入审计项目名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="立项理由及审计目的" prop="projectPurpose">
            <el-input
              v-model="formData.projectPurpose"
              clearable
              placeholder="请输入立项理由及审计目的"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="重点关注内容" prop="concernsContent">
            <el-input
              v-model="formData.concernsContent"
              clearable
              placeholder="请输入重点关注内容"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="单位范围" prop="unitRange">
            <el-input
              v-model="formData.unitRange"
              clearable
              placeholder="请输入单位范围"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="时间范围" prop="timeRange">
            <el-date-picker
              v-model="formData.timeRange"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd HH:mm:ss"
              placeholder="选择委托时间"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="创建人" prop="createUser">
            <el-input
              v-model="formData.createUser.realname"
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
  // import {
  //   download,
  //   myDraftSave,
  //   myDraftFileList,
  //   deleteFile,
  // } from '@/api/audit/implement'
  import { cwzxpxbUpdate, cwzxpxbListDetail } from '@/oapi/audit/plan'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  export default {
    name: 'cwzxpxbEdit',
    inheritAttrs: false,
    components: { SelectDepartment },
    props: [],
    data() {
      return {
        loading: false,
        formData: {
          suggestDeptId: '',
          sort: '',
          projectName: '',
          projectPurpose: '',
          concernsContent: '',
          unitRange: '',
          timeRange: '',
          createUser: {
            realname: '',
          },
          createTime: '',
        },
        formDisabled: true,
        tableData: [],
        rules: {
          // field1: [
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
          // field3: [
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
      async showEdit(
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
        this.loading = true
        const res = await cwzxpxbListDetail({ id: formId }) // 请求详情接口 回显
        this.loading = false
        if (res.code === 1) {
          // 详情数据序列化
          const detailDataArrays = Object.entries(res.data.data)

          // 然后，有多少数据，回填复制formData中多少项
          detailDataArrays.forEach((item) => {
            this.formData[item[0]] = item[1]
          })
        }
      },
      close() {
        this.formData = {
          suggestDeptId: '',
          sort: '',
          projectName: '',
          projectPurpose: '',
          concernsContent: '',
          unitRange: '',
          timeRange: '',
          createUser: {
            realname: '',
          },
          createTime: '',
        }
        this.tableData = []
        this.formDisabled = true
        this.$bus.$emit('updateMsg', 0)
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let attids = ''
            this.tableData.map((item) => {
              attids += item.attid
              attids += ','
            })
            attids = attids.substring(0, attids.length - 1)
            delete this.formData['createUser']
            this.loading = true
            const data = await cwzxpxbUpdate({
              ...this.formData,
              attids,
            })
            this.loading = false

            if (data && data.code === 1) {
              this.$emit('fetchData')
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
        this.$set(this.queryForm, 'audiorgName', val.name)
        this.$set(this.queryForm, 'audiorgid', val.id)
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
