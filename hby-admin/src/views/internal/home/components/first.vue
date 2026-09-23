<template>
  <el-card>
    <el-col :span="6">
      <div class="one-item">
        <div>
          <h2>{{ num1 }}</h2>
          <p>疑似问题数量</p>
        </div>
        <div>
          <i class="el-icon-money"></i>
        </div>
      </div>
    </el-col>
    <el-col :span="6">
      <div class="one-item">
        <div>
          <h2>{{ num2 }}</h2>
          <p>整改问题数量</p>
        </div>
        <div>
          <i class="el-icon-collection"></i>
        </div>
      </div>
    </el-col>
  </el-card>
</template>
<script>
 import { getTopcnt } from '@/api/internal/home'
  export default {
    props: {
      disputeCasesForm: {
        type: Object,
        default: () => {
          return {}
        },
      }
    },
    data() {
      return {
        num1: 0,
        num2: 0
      }
    },
    mounted() {
      this.getInfo()
    },
    methods: {
      async getInfo () {
        let res = await getTopcnt()
        this.num1 = res.data.suspected_cnt
        this.num2 = res.data.reform_cnt
      }
    },
  }
</script>
<style scoped>
  .one-item {
    display: flex;
    justify-content: space-between;
    background: white;
    padding: 10px;
    margin-bottom: 20px;
    border: 1px solid #d2d2d2;
  }
  i {
    font-size: 24px;
  }
</style>
