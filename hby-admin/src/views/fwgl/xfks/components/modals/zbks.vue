<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :append-to-body="true"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="450px"
      @close="close"
    >
      <el-row :gutter="24" v-loading="loading">
        <el-col :span="24" style="margin-bottom: 20px">
          <el-alert
            title="点击`开始考试`后将自动进入考试，请诚信考试！"
            type="error"
            style="margin-bottom: 10px"
          />

          <el-card class="pre-exam">
            <div>
              <strong>考试名称：</strong>
              {{ detailData.title }}
            </div>
            <div>
              <strong>考试时长：</strong>
              {{ detailData.totalTime }}分钟
            </div>
            <div>
              <strong>试卷总分：</strong>
              {{ detailData.totalScore }}分
            </div>
            <div>
              <strong>及格分数：</strong>
              {{ detailData.qualifyScore }}分
            </div>
            <div>
              <strong>考试描述：</strong>
              {{ detailData.content }}
            </div>
            <div>
              <strong>开放类型：</strong>
              {{ detailData.openType }}
            </div>
          </el-card>
        </el-col>
      </el-row>
      <template #footer>
        <el-button @click="close">关 闭</el-button>
        <el-button
          :loading="loading"
          type="primary"
          icon="el-icon-caret-right"
          @click="handleCreate"
        >
          开始考试
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script>
  import { processDetail, createPaper } from '@/api/fwgl/xfks/zxks'

  export default {
    name: 'zbks',
    data() {
      return {
        title: '准备考试',
        dialogFormVisible: false,
        detailData: {},
        postForm: {
          examId: '',
          password: '',
        },
        rules: {
          password: [{ required: true, message: '考试密码不能为空！' }],
        },

        loading: false,
      }
    },

    methods: {
      async fetchData() {
        const res = await processDetail({ id: this.postForm.examId })

        if (res) {
          this.detailData = res.data
        }
      },

      async handleCreate() {
        const that = this

        this.loading = true
        const res = await createPaper(this.postForm)

        if (res && res.data && res.data.id) {
          setTimeout(function () {
            that.dialogVisible = false
            // that.$router.push({ name: 'StartExam', params: { id: res.data.id } })、
            that.loading = false
            that.$emit('start-exam', res.data.id)
          }, 1000)
          this.close()
        } else {
          this.loading = false
        }
      },
      showModal(examId) {
        this.postForm.examId = examId
        this.dialogFormVisible = true
        this.fetchData()
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.dialogFormVisible = false
      },
      save() {},
    },
  }
</script>

<style scoped>
  .pre-exam div {
    line-height: 42px;
    color: #555555;
  }
</style>
