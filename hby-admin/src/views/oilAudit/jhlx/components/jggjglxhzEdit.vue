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
        label-width="230px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="formDisabled"
      >
        <el-col :span="12">
          <el-form-item label="单位" prop="field1">
            <el-input
              v-model="formData.field1"
              clearable
              placeholder="请输入单位"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="重点关注领域、项目、事项和风险" prop="field2">
            <el-input
              v-model="formData.field2"
              clearable
              placeholder="请输入重点关注领域、项目、事项和风险"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="关注内容" prop="field4">
            <el-input
              v-model="formData.field4"
              clearable
              placeholder="请输入关注内容"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="项目类别" prop="field3">
            <el-select
              v-model="formData.field3"
              placeholder="项目类别"
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
          <el-form-item label="创建人" prop="creator">
            <el-input
              v-model="formData.creator"
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
    <div slot="footer" v-if="!formDisabled">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </div>
    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />
  </el-dialog>
</template>

<script>
  import {
    download,
    myDraftSave,
    myDraftFileList,
    deleteFile,
  } from '@/api/audit/implement'
  import SelectDepartment from './department.vue'
  export default {
    name: 'jggjglxhzEdit',
    inheritAttrs: false,
    components: { SelectDepartment },
    props: [],
    data() {
      return {
        formData: {
          field1: undefined,
          field2: undefined,
          field3: undefined,
          field4: undefined,
          field5: undefined,
          field6: undefined,
          field7: undefined,
          field8: undefined,
          field9: undefined,
          field10: undefined,
          field11: undefined,
          field12: undefined,
          field13: undefined,
          field14: undefined,
          field15: undefined,
          field16: undefined,
          field17: undefined,
          field18: undefined,
          field19: undefined,
          field20: undefined,
          remark: undefined,
          creator: undefined,
          createTime: undefined,
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
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      showEdit(row, disabled) {
        this.dialogFormVisible = true
        this.formDisabled = disabled
        if (!row) {
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.creator = userInfo.realname
          this.formData.createTime = new Date()
          return
        }
        if (row && !disabled) {
          this.title = '编辑'
        } else if (row && disabled) {
          this.title = '详细'
        }
      },
      close() {
        this.formData = {
          field1: undefined,
          field2: undefined,
          field3: undefined,
          field4: undefined,
          field5: undefined,
          field6: undefined,
          field7: undefined,
          field8: undefined,
          field9: undefined,
          field10: undefined,
          field11: undefined,
          field12: undefined,
          field13: undefined,
          field14: undefined,
          field15: undefined,
          field16: undefined,
          field17: undefined,
          field18: undefined,
          field19: undefined,
          field20: undefined,
          remark: undefined,
          creator: undefined,
          createTime: undefined,
        }
        this.dialogFormVisible = false
        this.tableData = []
        this.formDisabled = true
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            //   let attids = ''
            //   this.tableData.map((item) => {
            //     attids += item.attid
            //     attids += ','
            //   })
            //   attids = attids.substring(0, attids.length - 1)
            //   const data = await myDraftSave({
            //     ...this.formData,
            //     attids,
            //   })
            //   this.$emit('fetch-data')
            //   this.close()
            // } else {
            //   return false
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
