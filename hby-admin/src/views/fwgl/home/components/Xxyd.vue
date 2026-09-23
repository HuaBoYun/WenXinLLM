<template>
  <div class="branch">
    <el-card class="branch-card" shadow="hover" style="height: 290px">
      <!-- <template #header>
        <span>
          <vab-icon icon="donut-chart-fill" />
          专项法务服务费统计
        </span>
      </template> -->
      <div class="title">学习园地</div>
      <div class="content">
        <el-table :data="list">
          <el-table-column align="center" label="编号" prop="lingcode">
            <template #default="{ row }">
              <el-button type="text" @click="handleRead(row)">
                {{ row.lingcode }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column align="center" label="名称" prop="lingdname" />
          <el-table-column align="center" label="创建人" prop="createname" />
          <el-table-column align="center" label="创建时间" prop="createtime" />
        </el-table>
        <div class="more" @click="goTo('/rcgl/Xxyd')">查看更多</div>
      </div>
    </el-card>
    <XXYDview ref="edit"></XXYDview>
  </div>
</template>

<script>
  import VabChart from '@/extra/VabChart'
  import { getXXYDList } from '@/api/systemLog'
  import XXYDview from '@/views/fwgl/rcgl/components/xxydView.vue'
  export default {
    components: {
      VabChart,
      XXYDview,
    },
    data() {
      return {
        messageList: [],
        list: [],
        queryForm: {
          name: '',
          code: '',
          pageNumber: 1,
          pageSize: 20,
        },
        initOptions: {
          renderer: 'svg',
        },
      }
    },
    async mounted() {
      const {
        data: {
          pageInfo: { tlist },
        },
      } = await getXXYDList(this.queryForm)
      const messageList = tlist.filter((v, i) => {
        return i < 2
      })
      this.list = messageList
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
      handleRead(row) {
        this.$refs['edit'].showEdit(row, 'detail')
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
