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
        <el-card style="margin-top: 20px">
          <div class="qu-content">
            <p>
              【{{ quData.quType === 1 ? '单选题' : '多选题' }}】{{
                quData.content
              }}
            </p>
            <div v-if="quData.quType === 1">
              <el-radio-group v-model="radioValues" readonly>
                <el-radio
                  v-for="an in quData.answerList"
                  :label="an.id"
                  :key="an.id"
                  readonly
                >
                  {{ an.content }}
                </el-radio>
              </el-radio-group>
            </div>

            <!-- 多选题 -->
            <div v-if="quData.quType === 2">
              <el-checkbox-group v-model="multiValues" readonly>
                <el-checkbox
                  v-for="an in quData.answerList"
                  :label="an.id"
                  :key="an.id"
                >
                  {{ an.content }}
                </el-checkbox>
              </el-checkbox-group>
            </div>
          </div>
        </el-card>

        <el-card class="qu-analysis" style="margin-top: 20px">
          整题解析：
          <p>{{ quData.analysis }}</p>
          <p v-if="!quData.analysis">暂无解析内容！</p>
        </el-card>

        <el-card
          class="qu-analysis"
          style="margin-top: 20px; margin-bottom: 30px"
        >
          选项解析：
          <div
            v-for="an in quData.answerList"
            class="qu-analysis-line"
            :key="an.id"
          >
            <template v-if="an.analysis">
              <p style="color: #555">{{ an.content }}：</p>
              <p style="color: #1890ff">{{ an.analysis }}</p>
            </template>
          </div>
          <p v-if="analysisCount === 0">暂无选项解析</p>
        </el-card>
      </el-row>
      <template #footer>
        <el-button @click="close">关 闭</el-button>
        <el-button
          :loading="loading"
          type="primary"
          icon="el-icon-caret-right"
          @click="save"
        >
          保存
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script>
  import { fetchDetail } from '@/api/fwgl/xfks/wdcj'

  export default {
    name: 'tmxq',
    data() {
      return {
        title: '试题详情',
        dialogFormVisible: false,
        loading: false,
        quData: {},
        radioValues: '',
        multiValues: [],
        analysisCount: 0,
      }
    },

    created() {},
    methods: {
      fetchData(id) {
        fetchDetail({ id: id }).then((response) => {
          this.quData = response.data

          this.quData.answerList.forEach((an) => {
            // 解析数量
            if (an.analysis) {
              this.analysisCount += 1
            }

            // 用户选定的
            if (an.isRight) {
              if (this.quData.quType === 1) {
                this.radioValues = an.id
              } else {
                this.multiValues.push(an.id)
              }
            }
          })
        })
      },
      showModal(data) {
        this.dialogFormVisible = true
        if (typeof data !== 'undefined') {
          this.fetchData(data)
        }
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
