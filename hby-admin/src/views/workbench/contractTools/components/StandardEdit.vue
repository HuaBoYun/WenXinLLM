<template>
  <div>
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
          label-width="120px"
          :model="formData"
          :rules="rules"
          size="medium"
        >
          <el-col :span="12">
            <el-form-item label="编号" prop="field101">
              <el-input
                v-model="formData.field101"
                clearable
                placeholder="请输入编号"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="名称" prop="field102">
              <el-input
                v-model="formData.field102"
                clearable
                placeholder="请输入名称"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="描述" prop="field103">
              <el-input
                v-model="formData.field103"
                :autosize="{ minRows: 4, maxRows: 8 }"
                placeholder="请输入描述"
                :style="{ width: '100%' }"
                type="textarea"
              />
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>
      <template #footer>
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script>
  import { doDelete } from '@/api/table'
  export default {
    name: 'PlanEdit',
    components: {},
    inheritAttrs: false,
    props: [],
    data() {
      return {
        title: '',
        dialogFormVisible: false,
        formData: {
          field101: undefined,
          field102: undefined,
          field103: undefined,
        },
        rules: {
          field101: [],
          field102: [],
          field103: [],
        },
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      save() {},
      showEdit(row) {
        if (!row) {
          this.title = '添加'
        } else {
          this.title = '编辑'
          this.form = Object.assign({}, row)
          this.form.org = '长江集团有限公司'
          this.form.code = 'XXXXXXXXXX'
          this.form.name = 'XXXXXXXXXX'
        }
        this.dialogFormVisible = true
      },
      close() {
        this.dialogFormVisible = false
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await doDelete({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          // await this.fetchData()
        })
      },
    },
  }
</script>
<style></style>
