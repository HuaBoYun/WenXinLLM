<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogVisible"
    width="1000px"
    :before-close="handleClose"
  >
    <div
      style="
        width: 100%;
        height: 200px;
        margin-bottom: 20px;
        overflow-x: scroll;
      "
    >
      <div :class="list.length > 5 ? 'timeaxis' : 'timeaxis1'">
        <div class="box1 li">
          <div class="line"></div>
        </div>
        <div class="boxs li" v-for="item in list" :key="item.INDEXS">
          <div class="line"></div>
          <div class="circular">{{ item.YEAR }}</div>
          <div class="desc">{{ item.CONDATION }}</div>
        </div>
        <div class="box1 li">
          <div class="line"></div>
          <div
            style="
              width: 0;
              height: 0;
              border: 10px solid transparent;
              border-left-color: #1890ff;
              position: absolute;
              right: -10px;
              top: 50%;
              transform: translateY(-50%);
            "
          ></div>
        </div>
      </div>
    </div>
  </el-dialog>
</template>

<script>
  import { getTimeLineData } from '@/api/fwgl/legal'
  export default {
    name: 'TimeLine',
    data() {
      return {
        dialogVisible: false,
        title: '过程',
        list: [],
      }
    },
    methods: {
      open(row) {
        this.dialogVisible = true
        getTimeLineData({ litigationId: row.litigationid }).then((res) => {
          this.list = res.proceList
        })
      },
      handleClose(done) {
        this.dialogVisible = false
      },
    },
  }
</script>

<style scoped>
  .timeaxis {
    width: max-content;
    position: relative;
    height: 150px;
    /* left: 50%;
    transform: translateX(-50%); */
  }
  .timeaxis1 {
    width: max-content;
    position: relative;
    height: 150px;
    left: 50%;
    transform: translateX(-50%);
  }

  .box1 {
    width: 60px;
    float: left;
    height: 150px;
    position: relative;
  }

  .boxs {
    overflow: hidden;
    float: left;
    position: relative;
    width: 180px;
    height: 150px;
  }

  .line {
    border-bottom: 1px solid #409eff;
    position: absolute;
    width: 100%;
    top: 50%;
    transform: translateY(-50%);
    left: 0;
  }

  .circular {
    font-size: 14px;
    line-height: 50px;
    text-align: center;
    z-index: 10;
    border: 2px solid #1890ff;
    width: 100px;
    height: 50px;
    border-radius: 50px;
    background: white;
    margin: auto;
    position: absolute;
    top: 50%;
    transform: translate(-50%, -50%);
    left: 50%;
  }

  .boxs:nth-child(odd) .desc {
    text-align: center;
    position: absolute;
    bottom: 0;
    /* top: 0; */
    width: 100%;
  }

  .boxs:nth-child(even) .desc {
    text-align: center;
    position: absolute;
    bottom: 0;
    width: 100%;
  }
</style>
