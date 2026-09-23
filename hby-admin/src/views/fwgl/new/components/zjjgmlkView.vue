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
          <el-form-item label="机构名称" prop="auditName">
            <el-input
              v-model="formData.auditName"
              clearable
              placeholder="请输入机构名称"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="机构负责人" prop="auditName">
            <el-input
              v-model="formData.auditName"
              clearable
              placeholder="请输入机构名称"
              :style="{ width: '80%' }"
              :disabled="!footer"
            />
            <el-button type="primary" style="margin-left: 10px">选择</el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="机构编号" prop="creatorName">
            <el-input
              v-model="formData.creatorName"
              clearable
              placeholder="请输入机构编号"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="入场时间" prop="workUnitName">
            <el-date-picker
              v-model="formData.creatorName"
              type="date"
              placeholder="选择日期"
              :style="{ width: '100%', height: '28px' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合作时间" prop="workUnitName">
            <el-date-picker
              v-model="value1"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="有无违规" prop="workUnitName">
            <el-radio-group v-model="formData.aa">
              <el-radio :label="1">有</el-radio>
              <el-radio :label="0">无</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>

        <el-col :span="24" v-if="formData.aa == 1">
          <el-form-item label="违规信息" prop="mattersInstructions">
            <el-input
              v-model="formData.mattersInstructions"
              clearable
              type="textarea"
              rows="3"
              placeholder="请输入违规信息"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="mattersInstructions">
            <el-input
              v-model="formData.mattersInstructions"
              clearable
              type="textarea"
              rows="3"
              placeholder="请输入备注"
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
