<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="140px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="项目名称" prop="auditName">
            <el-input
              v-model="formData.auditName"
              clearable
              placeholder="请输入项目名称"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="中介服务编号" prop="creatorName">
            <el-input
              v-model="formData.creatorName"
              clearable
              placeholder="请输入中介服务编号"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="使用部门" prop="workUnitName">
            <el-input
              v-model="formData.workUnitName"
              clearable
              placeholder="请选择使用部门"
              :style="{ width: '80%' }"
              disabled
            />
            <el-button type="primary" style="margin-left: 10px">选择</el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="主管部门" prop="workUnitName">
            <el-input
              v-model="formData.workUnitName"
              clearable
              placeholder="请选择主管部门"
              :style="{ width: '80%' }"
              disabled
            />
            <el-button type="primary" style="margin-left: 10px">选择</el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="评价类型" prop="workUnitName">
            <el-select
              v-model="value"
              placeholder="请选择"
              :style="{ width: '100%' }"
            >
              <el-option :key="1" label="项目评价" :value="1"></el-option>
              <el-option :key="2" label="年度评价" :value="2"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="评价时间" prop="createdTime">
            <el-date-picker
              style="width: 100%"
              v-model="formData.createdTime"
              placeholder="请输入评价时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              disabled
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="使用部门意见" prop="mattersInstructions">
            <el-input
              v-model="formData.mattersInstructions"
              clearable
              type="textarea"
              rows="3"
              placeholder="请输入使用部门意见"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="主管部门意见" prop="mattersInstructions">
            <el-input
              v-model="formData.mattersInstructions"
              clearable
              type="textarea"
              rows="3"
              placeholder="请输入主管部门意见"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" v-if="footer">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="save" type="primary">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import { formatDate } from '@/utils'
  export default {
    components: {},
    props: [],
    data() {
      return {
        loading: false,
        tableData: [],

        formData: {
          creatorName: '',
          staffid: '',
          workUnitName: '',
          orgid: '',
          createdTime: '',
        },
        footer: true,
        rules: {
          auditName: [
            {
              required: true,
              message: '请输入事项名称',
              trigger: 'blur',
            },
          ],
          creatorName: [
            {
              required: true,
              message: '请输入申请人',
              trigger: 'blur',
            },
          ],
          workUnitName: [
            {
              required: true,
              message: '请输入申请部门',
              trigger: 'blur',
            },
          ],
          createdTime: [
            {
              required: true,
              message: '请输入申请时间',
              trigger: 'blur',
            },
          ],
          mattersInstructions: [
            {
              required: true,
              message: '请输入事项说明',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        //提交
        visible: false,
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      handleSuccess(res, b, c) {
        if (res && res.data) {
          this.tableData = this.tableData.concat(res.data.fileIds || [])
        }
      },

      /**
       * @description: 打开选择组件
       * @return {*}
       */      
      projectManagers() {
        this.$refs['manages'].showEdit()
      },

      /**
       * @description: 外部打开dialog
       * @param {*} title 类型
       * @param {*} row 行数据
       * @return {*}
       */      
      async showEdit(title, row) {
        this.dialogFormVisible = true

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else {
          this.title = '新增'
          this.formData.createdTime = formatDate(new Date())
        }
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.formData = {}
        this.tableData = []
        this.clearType = true
        this.$emit('fetch-data')
        this.dialogFormVisible = false
        this.footer = true
      },
      /**
       * @description: 保存表单
       * @return {*}
       */      
      async save() {
        // this.$refs['ruleForm'].validate(async (valid) => {
        //   if (valid) {
        //     const res = await fetchApi(saveOrUpdate, this.formData)
        //     if (res && res.code === 200) {
        //       this.$message({ message: '保存成功', type: 'success' })
        //       // this.$emit('fetch-data')
        //       this.close()
        //     } else {
        //       this.$message({ message: res.message, type: 'error' })
        //     }
        //   } else {
        //
        //     return false
        //   }
        // })
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
