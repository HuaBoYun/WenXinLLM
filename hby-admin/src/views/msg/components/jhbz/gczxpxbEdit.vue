<template>
  <!-- 工程专项 edit -->
  <div>
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
          <el-form-item label="建议科室" prop="suggestDeptName">
            <el-input
              v-model="formData.suggestDeptName"
              placeholder="请输入建议科室"
              :style="{ width: '75%' }"
              disabled
            />
            <el-button
              type="primary"
              style="margin-left: 10px"
              @click="chooseDept('xmfzcksmc')"
            >
              选择
            </el-button>
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
    <div style="text-align: right;margin-top: 20px;"  v-if="!formDisabled">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
      <el-button type="primary" @click="ymsubmit">提交</el-button>
    </div>
    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />
    <!-- 选择部门弹窗 -->
    <DepartmentOptions ref="department" @selected="handleDepartmentSelected" />

    <Resubmit
      ref="resubmit"
      @fetchClose="close"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      :status="status"
    />
  </div>
</template>

<script>
  // import {
  //   download,
  //   myDraftSave,
  //   myDraftFileList,
  //   deleteFile,
  // } from '@/api/audit/implement'
  import { gczxpxbUpdate, gczxpxbDetail } from '@/oapi/audit/plan'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import DepartmentOptions from '@/views/oilAudit/report/components/options/department.vue'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'

  export default {
    name: 'gczxpxbEdit',
    inheritAttrs: false,
    components: { SelectDepartment, DepartmentOptions, Resubmit },
    props: [],
    data() {
      return {
        formData: {
          suggestDeptName: '',
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
        dialogFormVisible: false,
        title: '新增',
        // 流程相关
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
      // 选择部门
      handleDepartmentSelected(node) {
        this.$set(this.formData, `suggestDeptName`, node.name)
        this.$set(this.formData, `suggestDeptId`, node.id)
      },
      chooseDept() {
        this.$refs.department.show()
      },
      async showEdit(title, row, flowtaskinfoflowid, ymFromId, isWfqdedit, status) {
        // 流程相关
        this.fromId = row
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status

        this.dialogFormVisible = true
        if (row) {
          const res = await gczxpxbDetail({ id: row }) // 请求详情接口 回显
          if (res.code === 1) {
            // 详情数据序列化
            const detailDataArrays = Object.entries(res.data.data)

            // 然后，有多少数据，回填复制formData中多少项
            detailDataArrays.forEach((item) => {
              this.formData[item[0]] = item[1]
            })
            this.formData.suggestDeptName = res.data.data?.suggestDept?.orgname
            this.formData.suggestDeptId = res.data.data?.suggestDept?.orgid
          }
        } else {
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.$set(this.formData, 'createUser', userInfo.realname || '') // 后台数据创建人为对象，会引发显示问题
          this.formData.createTime = new Date()
        }

        if (title == 'edit') {
          this.title = '编辑'
          this.formDisabled = false
        } else if (title == 'detail') {
          this.title = '详细'
          this.formDisabled = true
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
        this.dialogFormVisible = false
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
            const data = await gczxpxbUpdate({
              ...this.formData,
              attids,
            })

            if (data && data.code == 1) {
              // this.close()
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
      async ymsubmit() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.$refs.resubmit.ymsubmit()
          }
        })
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
