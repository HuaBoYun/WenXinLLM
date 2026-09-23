<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-row :gutter="15">
      <el-form
        ref="elForm"
        class="disabled"
        :disabled="true"
        label-width="125px"
        :model="formData"
        :rules="rules"
        size="medium"
      >
        <el-col :span="12">
          <el-form-item label="计划编号" prop="field101">
            <el-input
              v-model="formData.plancode"
              clearable
              placeholder="请输入计划编号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划名称" prop="field102">
            <el-input
              v-model="formData.planname"
              clearable
              placeholder="请输入计划名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划年度" prop="field103">
            <el-input
              v-model="formData.palnyear"
              clearable
              placeholder="请输入计划年度"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划类别" prop="field106">
            <el-input
              v-model="formData.plantype"
              clearable
              placeholder="请输入计划类别"
              :style="{ vwidth: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划费用估算" prop="field107">
            <el-input
              v-model="formData.palncost"
              clearable
              placeholder="请输入计划费用估算"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划时间" prop="field133">
            <el-date-picker
              v-model="formData.spanDate"
              clearable
              end-placeholder="结束日期"
              format="yyyy-MM-dd"
              range-separator="-"
              start-placeholder="开始日期"
              :style="{ width: '100%' }"
              type="daterange"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="编制人" prop="field108">
            <el-input
              v-model="formData.createStaff.realname"
              clearable
              placeholder="请输入编制人"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="编制日期" prop="field108">
            <el-input
              v-model="formData.createtime"
              clearable
              placeholder="请输入编制日期"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被审计单位" prop="field108">
            <el-input
              v-model="formData.auditOrgInfo.orgname"
              clearable
              placeholder="请输入被审计单位"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="field132">
            <el-input
              v-model="formData.remarks"
              clearable
              placeholder="请输入备注"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
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
                  :disabled="false"
                  type="text"
                  @click="handleDown(row)"
                >
                  下载
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24">
          <el-divider>计划项目</el-divider>
        </el-col>
        <el-col :span="24">
          <el-table-column align="center" label="项目编号" prop="name" />
          <el-table-column align="center" label="项目名称" prop="name" />
          <el-table-column align="center" label="项目来源" prop="name" />
          <el-table-column align="center" label="项目经理" prop="name" />
          <el-table-column align="center" label="项目目前状态" prop="name" />
          <el-table-column align="center" label="计划开始时间" prop="name" />
          <el-table-column align="center" label="计划结束时间" prop="name" />
        </el-col>
      </el-form>
    </el-row>
  </el-dialog>
</template>

<script>
  import { doEdit } from '@/api/table'
  import { auditPlanViewDetail, getAuditPlanAttInfo } from '@/api/audit/plan'
  import { UTCformat } from '@/utils'
  import { download } from '@/api/audit/implement'
  export default {
    name: 'MaintainEdit',
    data() {
      return {
        formData: {},
        rules: {},
        field111Options: [
          {
            label: '是',
            value: 1,
          },
          {
            label: '否',
            value: 2,
          },
        ],
        title: '',
        dialogFormVisible: false,
        tableData: [],
      }
    },
    created() {},
    methods: {
      /**
       * @description  组件初始化
       * @param {*}  
        * @return {*}
       */  
      /**
       * @description: 外部打开内部dialog
       * @param {*} title 表单类型
       * @param {*} row 编辑、详情带入的数据
       * @return {*}
       */      
      async showEdit(row) {
        let res = await auditPlanViewDetail({
          planid: row.planId,
        })
        this.formData = res.data.auditPlan
        this.formData.createtime = UTCformat(res.data.auditPlan.createtime)
        this.formData.spanDate = [
          UTCformat(res.data.auditPlan.starttime),
          UTCformat(res.data.auditPlan.endtime),
        ]
        //获取审计计划的附件
        let resFile = await getAuditPlanAttInfo({
          planId: row.planid,
        })
        this.tableData = resFile.data
        this.dialogFormVisible = true
      },
      /**
       * @description  关闭组件
       * @param {*}  
        * @return {*}
       */  
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        // this.$refs['form'].resetFields()
        // this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
       /**
       * @description  无意义
       * @param {*}  
        * @return {*}
       */  
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { msg } = await doEdit(this.form)
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
      submitForm() {
        this.$refs['elForm'].validate((valid) => {
          if (!valid) return
          // TODO 提交表单
        })
      },
      resetForm() {
        this.$refs['elForm'].resetFields()
      }, 
       /**
       * @description  保存
       * @param {*}  
        * @return {*}
       */  
      save1(row) {
        row.show = false
      },
       /**
       * @description  添加点击按钮
       * @param {*}  
        * @return {*}
       */   
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */      
      handleAdd() {
        this.tableData.push({
          periodId: '',
          performanceTarget: '',
          mzyj: '',
          performanceDeductRatio: '',
          outpatientPerformance: '',
          outpatientProfit: '',
          show: true,
        })
      },
       /**
       * @description  附件下载
       * @param {*}  
        * @return {*}
       */  
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
       /**
       * @description  附件删除
       * @param {*}  
        * @return {*}
       */  
      handleDelete(index) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(() => {
            this.tableData.splice(index, 1)
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          })
      },
    },
  }
</script>
<style scoped></style>
