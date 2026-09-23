<template>
  <div class="branch">
    <div class="content">
      <el-table :data="list">
        <el-table-column align="center" label="会议名称" prop="conferenceName">
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.conferenceName }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column align="center" label="会议主持人" prop="compereName" />
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
    </div>

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
          isHome: 1,
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
            return i < 4
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
  }

  p {
    line-height: 30px;
    text-align: center;
  }
</style>
