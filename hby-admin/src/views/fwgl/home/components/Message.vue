<template>
  <div class="branch">
    <el-card class="branch-card" shadow="hover" style="height: 290px">
      <!-- <template #header>
        <span>
          <vab-icon icon="donut-chart-fill" />
          专项法务服务费统计
        </span>
      </template> -->
      <div class="title">会议通知</div>
      <div class="content">
        <el-table :data="list">
          <el-table-column
            align="center"
            label="会议名称"
            prop="conferenceName"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleDetail(row)">
                {{ row.conferenceName }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="会议主持人"
            prop="compereName"
          />
          <el-table-column
            align="center"
            label="会议时间"
            prop="conferenceTime"
          />
          <el-table-column
            align="center"
            label="会议内容"
            prop="content"
            width="400"
            show-overflow-tooltip
          />
          <el-table-column align="center" label="创建时间" prop="createdTime" />
        </el-table>
        <div class="more" @click="goTo('/rcgl/hygl')">查看更多</div>
      </div>
    </el-card>
    <hyglView ref="hyglView" />
  </div>
</template>

<script>
  import VabChart from '@/extra/VabChart'
  import { getAlerts } from '@/api/fwgl/echarts'
  import hyglView from '@/views/fwgl/rcgl/components/hyglView.vue'
  export default {
    components: {
      VabChart,
      hyglView,
    },
    data() {
      return {
        messageList: [],
        list: [],
        queryForm: {
          compere: '',
          conferenceBeginDate: '',
          conferenceEndDate: '',
          conferenceName: '',
          pageNumber: 1,
          pageSize: 20,
          isHome: 0,
        },
        initOptions: {
          renderer: 'svg',
        },
      }
    },
    mounted() {
      getAlerts(this.queryForm).then((res) => {
        if (res.code === 200) {
          const messageList = res.data.tlist.filter((v, i) => {
            return i < 2
          })
          this.list = messageList
        }
      })
    },
    methods: {
      async handleDetail(row) {
        await this.$refs['hyglView'].showEdit('detail', {
          id: row.conferenceId,
        })
      },
      goTo(page) {
        this.$router.push(page)
      },
    },
  }
</script>

<style scoped>
  .branch-echart1 {
    height: 300px !important;
  }

  .title {
    text-align: center;
    font-size: 18px;
    font-weight: 600;
  }

  .content {
    height: 265px;
    padding-top: 35px;
  }

  p {
    line-height: 30px;
    text-align: center;
  }
  .more {
    margin-top: 20px;
    width: 100%;
    text-align: center;
    color: #409eff;
    cursor: pointer;
  }
</style>
