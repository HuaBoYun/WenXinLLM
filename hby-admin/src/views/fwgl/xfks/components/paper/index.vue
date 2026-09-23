<template>
  <div>
    <el-card
      v-for="(item, index) in paperList"
      style="margin-bottom: 10px; line-height: 30px"
      :key="index"
    >
      <el-row :gutter="20">
        <el-col :span="12">考试时间：{{ item.createTime }}</el-col>

        <el-col :span="12">考试用时：{{ item.userTime }}分钟</el-col>

        <el-col :span="12">考试得分：{{ item.userScore }}</el-col>

        <el-col :span="12">
          是否合格：{{ item.userScore > item.qualifyScore ? '是' : '否' }}
        </el-col>

        <el-col :span="12">考试状态：{{ map[+item.state] }}</el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
  // import { listPaper } from '@/api/paper/paper'
  import { getPersonExamDetail } from '@/api/fwgl/ksgl'

  export default {
    name: 'UserPaperList',
    props: {
      examId: {
        type: String,
        default: '',
      },
      userId: {
        type: String,
        default: '',
      },
    },

    data() {
      return {
        value1: null,
        paperList: [],
        map: ['考试中', '待阅卷', '已考完', '已弃考'],
      }
    },

    watch: {
      // 检测查询变化
      examId: {
        handler() {
          this.fetchPaperList()
        },
        deep: true,
      },

      // 检测查询变化
      userId: {
        handler() {
          this.fetchPaperList()
        },
        deep: true,
      },
    },

    created() {
      this.fetchPaperList()
    },
    methods: {
      fetchPaperList() {
        // this.paperList = [
        //   {
        //     createTime: '2022-10-22 15:25:36',
        //     departId: '1318103313740320770',
        //     departId_dictText: '技术部',
        //     examId: '1583651770036535297',
        //     hasSaq: false,
        //     id: '1583721212506423298',
        //     limitTime: '2022-10-22 15:33:36',
        //     objScore: 0,
        //     qualifyScore: 300,
        //     realName: '超管A',
        //     state: 2,
        //     subjScore: 0,
        //     title: '123',
        //     totalScore: 12400,
        //     totalTime: 8,
        //     updateTime: '2022-10-22 15:39:11',
        //     userId: '10001',
        //     userId_dictText: '超管A',
        //     userScore: 0,
        //     userTime: 13,
        //   },
        //   {
        //     createTime: '2022-10-22 15:25:36',
        //     departId: '1318103313740320770',
        //     departId_dictText: '技术部',
        //     examId: '1583651770036535297',
        //     hasSaq: false,
        //     id: '1583721212506423298',
        //     limitTime: '2022-10-22 15:33:36',
        //     objScore: 0,
        //     qualifyScore: 300,
        //     realName: '超管A',
        //     state: 2,
        //     subjScore: 0,
        //     title: '123',
        //     totalScore: 12400,
        //     totalTime: 8,
        //     updateTime: '2022-10-22 15:39:11',
        //     userId: '10001',
        //     userId_dictText: '超管A',
        //     userScore: 0,
        //     userTime: 13,
        //   },
        //   {
        //     createTime: '2022-10-22 15:25:36',
        //     departId: '1318103313740320770',
        //     departId_dictText: '技术部',
        //     examId: '1583651770036535297',
        //     hasSaq: false,
        //     id: '1583721212506423298',
        //     limitTime: '2022-10-22 15:33:36',
        //     objScore: 0,
        //     qualifyScore: 300,
        //     realName: '超管A',
        //     state: 2,
        //     subjScore: 0,
        //     title: '123',
        //     totalScore: 12400,
        //     totalTime: 8,
        //     updateTime: '2022-10-22 15:39:11',
        //     userId: '10001',
        //     userId_dictText: '超管A',
        //     userScore: 0,
        //     userTime: 13,
        //   },
        // ]
        getPersonExamDetail({
          params: { userId: this.userId, examId: this.examId },
          current: 1,
          size: 5,
        }).then((response) => {
          this.paperList = response.data.tlist
        })
      },
    },
  }
</script>
