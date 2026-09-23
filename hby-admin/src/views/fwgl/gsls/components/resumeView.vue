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
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="开始时间" label-width="140px" prop="stareTime">
            <el-date-picker
              :style="{ width: '100%' }"
              v-model="formData.stareTime"
              placeholder="开始时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd HH:mm:ss"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="结束时间" label-width="140px" prop="endTime">
            <el-date-picker
              :style="{ width: '100%' }"
              v-model="formData.endTime"
              placeholder="结束时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd HH:mm:ss"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="职务" label-width="140px" prop="position">
            <el-input
              v-model="formData.position"
              clearable
              placeholder="请输入职务"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="在何地何部门(学习)工作"
            label-width="140px"
            prop="oldWorkUnit"
          >
            <el-input
              v-model="formData.oldWorkUnit"
              clearable
              type="textarea"
              rows="2"
              placeholder="请输入在何地何部门(学习)工作"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
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
  import { addZYSQjl } from '@/api/fwgl/gsls'
  export default {
    name: 'SummanyInfo',
    components: {},
    inheritAttrs: false,
    props: [],
    data() {
      return {
        formData: {
          endTime: '',
          oldWorkUnit: '',
          position: '',
          remark: '',
          stareTime: '',
        },
        footer: true,
        rules: {
          stareTime: [
            {
              required: true,
              message: '请输入开始时间',
              trigger: 'blur',
            },
          ],
          endTime: [
            {
              required: true,
              message: '请输入结束时间',
              trigger: 'blur',
            },
          ],
          position: [
            {
              required: true,
              message: '请输入职务',
              trigger: 'blur',
            },
          ],
          oldWorkUnit: [
            {
              required: true,
              message: '请输入在何地何部门(学习)工作',
              trigger: 'blur',
            },
          ],
          remark: [
            {
              required: true,
              message: '请输入备注',
              trigger: 'blur',
            },
          ],
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
      /**
       * @description: 外部打开dialog
       * @param {*} title 表单类型
       * @param {*} row 行数据
       * @return {*}
       */      
      showEdit(title, row) {
        this.dialogFormVisible = true
        if (row) {
          this.formData = Object.assign({}, row)
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
        this.formData = {}
        this.dialogFormVisible = false

        this.footer = true
      },
      /**
       * @description: 保存表单
       * @return {*}
       */      
      add() {
        addZYSQjl(this.formData).then((res) => {
          if (res.msg == '成功') {
            this.dialogFormVisible = false
            this.$emit('addList', res.data)
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
