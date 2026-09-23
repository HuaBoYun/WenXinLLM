<template>
  <div class="index-container">
    <el-row :gutter="20">
      <!-- <el-col :lg="24" :md="24" :sm="24" :xl="24" :xs="24"></el-col> -->
      <el-col :lg="6" :md="6" :sm="24" :xl="6" :xs="24">
        <transTotal :transTotal="transTotal" />
      </el-col>
      <el-col :lg="6" :md="6" :sm="24" :xl="6" :xs="24">
        <transRun :transRun="transRun" />
      </el-col>
      <el-col :lg="6" :md="6" :sm="24" :xl="6" :xs="24">
        <jobTotal :jobTotal="jobTotal" />
      </el-col>
      <el-col :lg="6" :md="6" :sm="24" :xl="6" :xs="24">
        <jobRun :jobRun="jobRun" />
      </el-col>
      <el-col :lg="12" :md="12" :sm="24" :xl="12" :xs="24">
        <Line1 :transSuccess="transSuccess" :transFail="transFail" />
      </el-col>
      <el-col :lg="12" :md="12" :sm="24" :xl="12" :xs="24">
        <Line2 :jobSuccess="jobSuccess" :jobFail="jobFail" />
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import transRun from './components/transRun'
  import transTotal from './components/transTotal'
  import jobRun from './components/jobRun'
  import jobTotal from './components/jobTotal'
  import Line1 from './components/Line1'
  import Line2 from './components/Line2'
  import { getCount } from '@/api/sjzt/etl/etl'

  export default {
    name: 'Index',
    components: {
      Line1,
      Line2,
      transRun,
      transTotal,
      jobRun,
      jobTotal,
    },
    data() {
      return {
        transRun: '',
        transTotal: '',
        transSuccess: [],
        transFail: [],
        jobRun: '',
        jobTotal: '',
        jobSuccess: [],
        jobFail: [],
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      async fetchData() {
        const {
          data: { job, tran },
        } = await getCount()
        this.transRun = tran.runTran
        this.transTotal = tran.totalTran
        this.transFail = this.sortDate(tran.fail)
        this.transSuccess = this.sortDate(tran.success)

        this.jobRun = job.runJob
        this.jobTotal = job.totalJob
        this.jobFail = this.sortDate(job.fail)
        this.jobSuccess = this.sortDate(job.success)
      },
      sortDate(arr) {
        //对日期进行排序
        arr.sort((a, b) => {
          return Date.parse(a.date) - Date.parse(b.date)
        })
        return arr
      },
    },
  }
</script>

<style lang="scss" scoped>
  .index-container {
    padding: 0 !important;
    background: $base-color-background !important;

    ::v-deep {
      .access,
      .authorization,
      .version-information {
        min-height: 268px;
      }

      .el-card {
        .el-card__header {
          position: relative;

          .card-header-tag {
            position: absolute;
            top: 15px;
            right: $base-margin;
          }

          > div > span {
            display: flex;
            align-items: center;

            i {
              margin-right: 3px;
            }
          }
        }

        .el-card__body {
          position: relative;
          .echarts {
            width: 100%;
            height: 127px;
          }

          .card-footer-tag {
            position: absolute;
            right: $base-margin;
            bottom: 15px;
          }
        }
      }

      .bottom {
        padding-top: 20px;
        margin-top: 5px;
        color: #595959;
        text-align: left;
        border-top: 1px solid $base-border-color;
      }
    }
  }
</style>
