<template>
  <el-dialog
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-row :gutter="14">
      <el-form
        ref="elForm"
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="资料编号" prop="field101">
            <el-input
              v-model="formData.field102"
              clearable
              placeholder="请输入资料编号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="资料名称" prop="field102">
            <el-input
              v-model="formData.field102"
              clearable
              placeholder="请输入资料名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="所属项目" prop="field107">
            <el-input
              v-model="formData.field107"
              clearable
              placeholder="请输入所属项目"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="编辑器" prop="field108">
            <tinymce
              v-model="formData.field108"
              :height="300"
              placeholder="请输入编辑器"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer">
      <el-button @click="close">取消</el-button>
      <el-button type="primary">确定</el-button>
    </div>
    <template #footer>
      <el-button @click="close">关 闭</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import Tinymce from '@/components/Tinymce'

  export default {
    name: 'ProjectDataInfo',
    components: { Tinymce },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        formData: {
          field101: undefined,
          field102: undefined,
          field107: undefined,
          field108: null,
        },
        rules: {
          field101: [
            {
              required: true,
              message: '请输入资料编号',
              trigger: 'blur',
            },
          ],
          field102: [
            {
              required: true,
              message: '请输入资料名称',
              trigger: 'blur',
            },
          ],
          field107: [
            {
              required: true,
              message: '请输入所属项目',
              trigger: 'blur',
            },
          ],
          field108: [
            {
              required: true,
              message: '请输入编辑器',
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
      showEdit() {
        this.dialogFormVisible = true
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.dialogFormVisible = false
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
