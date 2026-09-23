<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="100px"
      :disabled="isDetail"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="标题" prop="title">
            <el-input
              v-model="form.title"
              placeholder="请输入标题"
              maxlength="100"
              show-word-limit
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="过期时间" prop="expirationTime">
            <el-date-picker
              v-model="form.expirationTime"
              type="date"
              placeholder="选择过期时间"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="内容" prop="acontent">
        <el-input
          v-model="form.acontent"
          type="textarea"
          :rows="6"
          placeholder="请输入内容"
          maxlength="1000"
          show-word-limit
        />
      </el-form-item>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="创建人">
            <el-input
              v-model="form.staffName"
              placeholder="创建人"
              :disabled="true"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间">
            <el-input
              v-model="form.createTime"
              placeholder="创建时间"
              :disabled="true"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button
        type="primary"
        @click="handleSubmit"
        :loading="loading"
        v-if="!isDetail"
      >
        确定
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
  import { getDetail, saveOrUpdate } from '@/api/internal/pubNode'

  export default {
    name: 'PubNodeEdit',
    data() {
      return {
        dialogVisible: false,
        loading: false,
        isDetail: false,
        form: {
          id: 0,
          title: '',
          acontent: '',
          expirationTime: '',
          staffid: '',
          staffName: '',
          createTime: '',
        },
        rules: {
          title: [
            { required: true, message: '请输入标题', trigger: 'blur' },
            {
              min: 1,
              max: 100,
              message: '标题长度在 1 到 100 个字符',
              trigger: 'blur',
            },
          ],
          acontent: [
            { required: true, message: '请输入内容', trigger: 'blur' },
            {
              min: 1,
              max: 1000,
              message: '内容长度在 1 到 1000 个字符',
              trigger: 'blur',
            },
          ],
          expirationTime: [
            { required: true, message: '请选择过期时间', trigger: 'change' },
          ],
        },
      }
    },
    computed: {
      title() {
        if (this.isDetail) {
          return '查看公告'
        }
        return this.form.id ? '编辑公告' : '新建公告'
      },
    },
    methods: {
      async showEdit(row, isDetail = false) {
        this.isDetail = isDetail
        this.dialogVisible = true

        if (row && row.id) {
          // 编辑模式：获取详情数据
          try {
            const { data, code } = await getDetail({ id: row.id })
            if (code == 1) {
              Object.assign(this.form, data)
              // 格式化创建时间，只保留日期部分
              if (this.form.createTime) {
                this.form.createTime = this.form.createTime.split(' ')[0]
              }
            }
          } catch (error) {
            console.error('获取详情失败:', error)
            this.$message.error('获取详情失败')
            // 使用传入的row数据作为备选
            let createTime = row.createTime || ''
            if (createTime) {
              createTime = createTime.split(' ')[0]
            }
            this.form = {
              id: row.id,
              title: row.title || '',
              acontent: row.acontent || '',
              expirationTime: row.expirationTime || '',
              staffid: row.staffid || '',
              staffName: row.staffName || '',
              createTime: createTime,
            }
          }
        } else {
          // 新建模式：设置用户信息和当前时间
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.form = {
            id: 0,
            title: '',
            acontent: '',
            expirationTime: '',
            staffid: userInfo.staffid,
            staffName: userInfo.realname,
            createTime: new Date().toISOString().split('T')[0], // 格式化为 YYYY-MM-DD
          }
        }

        this.$nextTick(() => {
          if (this.$refs.form) {
            this.$refs.form.clearValidate()
          }
        })
      },

      resetForm() {
        this.form = {
          id: 0,
          title: '',
          acontent: '',
          expirationTime: '',
          staffid: '',
          staffName: '',
          createTime: '',
        }
      },

      handleClose() {
        this.dialogVisible = false
        this.resetForm()
        this.isDetail = false
      },

      async handleSubmit() {
        try {
          await this.$refs.form.validate()
          this.loading = true

          // 调用保存接口
          const { code, message } = await saveOrUpdate({
            id: this.form.id,
            title: this.form.title,
            acontent: this.form.acontent,
            expirationTime: this.form.expirationTime,
          })

          if (code == 1) {
            this.$message.success(this.form.id ? '修改成功' : '新建成功')
            this.handleClose()
            this.$emit('fetch-data')
          } else {
            this.$message.error(response.message || '保存失败')
          }
        } catch (error) {
          console.error('保存失败:', error)
          this.$message.error('保存失败')
        } finally {
          this.loading = false
        }
      },
    },
  }
</script>

<style scoped>
  .dialog-footer {
    text-align: right;
  }
</style>
