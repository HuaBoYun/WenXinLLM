<template>
  <el-dialog
    v-if="dialogFormVisible"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="400px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-form
      ref="ruleForm"
      label-width="100px"
      :model="formData"
      :rules="rules"
      size="mini"
    >
      <el-form-item label="分类名称" prop="typeName">
        <el-input
          v-model="formData.typeName"
          clearable
          placeholder="请输入名称"
          :style="{ width: '100%' }"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="close">关 闭</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </template>
  </el-dialog>
</template>
<script>
  import { saveInfo, getDefaultTreeInfo } from '@/api/setting/org'
  export default {
    data() {
      return {
        dialogFormVisible: false,
        title: '',
        formData: {
          typeName: '',
          mpdeltype: 'ZNSJ',
        },
        rules: {
          typeName: [
            {
              required: true,
              message: '请输入分类名称',
              trigger: 'blur',
            },
          ],
        },
        nodeId: '',
      }
    },

    methods: {
      async show(row, title) {
        this.formData.typeName = ''
        if (row) {
          this.nodeId = row.nodeId
        }
        if (title == '新增') {
          this.title = '新增'
        } else {
          this.title = '编辑'
          const res = await getDefaultTreeInfo({
            nodeId: row.nodeId,
          })
          this.formData.typeName = res.data.data.typeName
          this.formData.typeId = res.data.data.typeId
        }
        this.dialogFormVisible = true
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
      close() {
        this.dialogFormVisible = false
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            saveInfo({
              parentId: this.title == '编辑' ? '' : this.nodeId,
              typeName: this.formData.typeName,
              typeId: this.title == '新增' ? '' : this.formData.typeId,
              mpdeltype: 'ZNSJ',
            }).then((res) => {
              if (res.code == 1) {
                this.$emit('FetchData')
                this.dialogFormVisible = false
              }
            })
          }
        })
      },
    },
  }
</script>
