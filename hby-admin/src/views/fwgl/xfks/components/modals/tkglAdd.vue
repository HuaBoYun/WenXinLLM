<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :append-to-body="true"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="800px"
      @close="close"
    >
      <el-row :gutter="24" v-loading="loading">
        <el-card>
          <el-form>
            <el-form-item label="题库编号" prop="code">
              <el-input v-model="postForm.code" />
            </el-form-item>
            <el-form-item label="题库名称" prop="title">
              <el-input v-model="postForm.title" />
            </el-form-item>

            <el-form-item label="题库备注" prop="remark">
              <el-input v-model="postForm.remark" type="textarea" />
            </el-form-item>
          </el-form>
        </el-card>
      </el-row>
      <template #footer>
        <el-button @click="close">关 闭</el-button>
        <el-button :loading="loading" type="primary" @click="save">
          保存
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script>
  // import { fetchDetail, saveData } from '@/api/qu/repo'
  import { saveWarehouse, detailWarehouse } from '@/api/fwgl/xfks/tkgl'
  import { formatDate } from '@/utils'

  export default {
    name: 'tkglAdd',
    data() {
      return {
        title: '添加题库',
        dialogFormVisible: false,
        postForm: {
          createTime: formatDate(new Date()),
        },
        loading: false,
        rules: {
          title: [{ required: true, message: '题库名称不能为空！' }],
        },
      }
    },

    created() {},
    methods: {
      submitForm() {
        this.$refs.postForm.validate((valid) => {
          if (!valid) {
            return
          }

          saveData(this.postForm).then(() => {
            // this.$notify({
            //   title: '成功',
            //   message: '题库保存成功！',
            //   type: 'success',
            //   duration: 2000,
            // })
            this.$message({
              type: 'success',
              message: '题库保存成功！',
            })
            this.close()
          })
        })
      },

      onCancel() {
        this.$router.push({ name: 'ListRepo' })
      },
      showModal(data) {
        if (data) {
          detailWarehouse({ id: data.id }).then((res) => {
            this.postForm = res.data
          })
        } else {
          this.postForm = {
            createTime: formatDate(new Date()),
          }
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
      /**
       * @description: 保存表单
       * @return {*}
       */      
      save() {
        saveWarehouse(this.postForm).then((res) => {
          if (res.code == 1) {
            this.dialogFormVisible = false
            this.$message({
              type: 'success',
              message: '题库保存成功！',
            })
            this.close()
            this.$emit('get')
          }
        })
      },
    },
  }
</script>
