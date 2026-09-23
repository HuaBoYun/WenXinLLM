<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :append-to-body="true"
      title="考试详情"
      :visible.sync="dialogFormVisible"
      width="1200px"
      @close="close"
    >
      <el-row :gutter="24" v-loading="loading">
        <el-card>
          <div style="float: right; font-weight: bold; color: #ff0000">
            试卷总分：{{ detailData.totalScore }}分
          </div>

          <el-table
            :data="detailData.repoList || []"
            :border="false"
            empty-text="暂无题库数据"
            style="width: 100%; margin-top: 15px"
          >
            <el-table-column label="题库名称" width="200">
              <template slot-scope="scope">
                <repo-select
                  v-model="scope.row.repoId"
                  :multi="false"
                  disabled
                />
              </template>
            </el-table-column>
            <el-table-column label="单选数量" align="center">
              <template slot-scope="scope">
                {{ scope.row.radioCount }} / {{ scope.row.totalRadio }}
              </template>
            </el-table-column>

            <el-table-column label="单选分数" align="center">
              <template slot-scope="scope">
                {{ scope.row.radioScore }}
              </template>
            </el-table-column>

            <el-table-column label="多选数量" align="center">
              <template slot-scope="scope">
                {{ scope.row.multiCount }} / {{ scope.row.totalMulti }}
              </template>
            </el-table-column>

            <el-table-column label="多选分数" align="center">
              <template slot-scope="scope">
                {{ scope.row.multiScore }}
              </template>
            </el-table-column>

            <el-table-column label="判断题数量" align="center">
              <template slot-scope="scope">
                {{ scope.row.judgeCount }} / {{ scope.row.totalJudge }}
              </template>
            </el-table-column>

            <el-table-column label="判断题分数" align="center">
              <template slot-scope="scope">
                {{ scope.row.judgeScore }}
              </template>
            </el-table-column>
          </el-table>
        </el-card>

        <h3>考试配置</h3>
        <el-card style="margin-top: 20px">
          <el-form
            ref="detailForm"
            :model="detailData"
            label-position="left"
            label-width="120px"
          >
            <el-form-item label="考试名称">
              <span>{{ detailData.title }}</span>
            </el-form-item>

            <el-form-item label="考试描述">
              <span>{{ detailData.content }}</span>
            </el-form-item>

            <el-form-item label="总分数">
              <span>{{ detailData.totalScore }}</span>
            </el-form-item>

            <el-form-item label="及格分">
              <span>{{ detailData.qualifyScore }}</span>
            </el-form-item>

            <el-form-item label="考试时长(分钟)">
              <span>{{ detailData.totalTime }}</span>
            </el-form-item>

            <el-form-item label="是否限时">
              <span>{{ detailData.timeLimit ? '是' : '否' }}</span>
            </el-form-item>

            <el-form-item
              v-if="detailData.timeLimit"
              label="考试时间"
            >
              <span>{{ formatDate(detailData.startTime) }} ~ {{ formatDate(detailData.endTime) }}</span>
            </el-form-item>

            <el-form-item label="开放类型">
              <span>{{ detailData.openType === 1 ? '完全开放' : '定向考试' }}</span>
            </el-form-item>

            <el-form-item label="考试状态">
              <span>{{ getExamState(detailData.state) }}</span>
            </el-form-item>
          </el-form>
        </el-card>
      </el-row>
      <template #footer>
        <el-button @click="close">关 闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
  import { getDetail } from '@/api/fwgl/ksgl'
  import RepoSelect from './../RepoSelect'

  export default {
    name: 'ksglDetail',
    components: { RepoSelect },
    data() {
      return {
        loading: false,
        dialogFormVisible: false,
        detailData: {
          totalScore: 0,
          repoList: [],
          title: '',
          content: '',
          qualifyScore: 0,
          totalTime: 0,
          timeLimit: false,
          startTime: '',
          endTime: '',
          openType: 1,
          state: 0,
        },
      }
    },
    methods: {
      formatDate(timestamp) {
        if (!timestamp) return ''
        const n = new Date(timestamp)
        return (
          n.toLocaleDateString().replace(/\//g, '-') +
          ' ' +
          n.toTimeString().substr(0, 8)
        )
      },
      getExamState(state) {
        const states = ['进行中', '已禁用', '待开始', '已结束']
        return states[+state] || '未知'
      },
      showModal(data) {
        if (data) {
          this.loading = true
          this.dialogFormVisible = true
          getDetail({ id: data.id }).then((response) => {
            this.detailData = response.data
            this.loading = false
          }).catch(() => {
            this.loading = false
          })
        }
      },
      close() {
        this.detailData = {
          totalScore: 0,
          repoList: [],
          title: '',
          content: '',
          qualifyScore: 0,
          totalTime: 0,
          timeLimit: false,
          startTime: '',
          endTime: '',
          openType: 1,
          state: 0,
        }
        this.dialogFormVisible = false
      },
    },
  }
</script>

<style scoped>
  h3 {
    margin-top: 20px;
    margin-bottom: 15px;
  }
</style>
