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
          <el-form
            ref="form"
            label-width="100px"
            :model="postForm"
            :rules="rules"
          >
            <el-form-item label="评分类型" prop="examineType">
              <el-select v-model="postForm.examineType" class="filter-item">
                <el-option
                  v-for="item in examineTypes"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="评分重点" prop="examineEmphasis">
              <el-input v-model="postForm.examineEmphasis" type="textarea" />
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
  import { saveOrUpdate, getDetail } from '@/api/fwgl/pfgl/pfzdgl'
  import { formatDate } from '@/utils'

  export default {
    name: 'pfzdglAdd',
    data() {
      return {
        title: '添加评分重点',
        dialogFormVisible: false,
        postForm: {
          employmentObjective: '',
          examineType: '',
          examineEmphasis: '',
          serialNumber: '',
        },
        loading: false,
        rules: {
          examineType: [{ required: true, message: '评分类型不能为空！' }],
          examineEmphasis: [{ required: true, message: '评分重点不能为空！' }],
        },
        examineTypes: [
          {
            value: 1,
            label: '外部监管考核',
          },
          {
            value: 2,
            label: '子单位考核',
          },
        ],
      }
    },

    created() {},
    methods: {
      showModal(data) {
        if (data) {
          getDetail({ id: data.id }).then((res) => {
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
        this.loading = true
        saveOrUpdate(this.postForm)
          .then((res) => {
            if (res.code == 200) {
              this.$message({
                type: 'success',
                message: '保存成功！',
              })
              this.close()
              this.$emit('get')
            }
          })
          .finally(() => {
            this.loading = false
          })
      },
    },
  }
</script>
