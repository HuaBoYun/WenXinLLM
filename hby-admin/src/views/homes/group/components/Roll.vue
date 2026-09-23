<template>
  <div class="above">
    <div class="carousel">
      <el-carousel
        height="400px"
        direction="vertical"
        :autoplay="false"
        indicator-position="none"
        ref="carousel"
      >
        <el-carousel-item
          v-for="(item, key) in newsList"
          :key="key"
          :name="item.index"
        >
          <h3 class="medium">{{ item.news }}</h3>
        </el-carousel-item>
      </el-carousel>
    </div>
    <div class="news">
      <div
        v-for="(item, key) in newsList"
        class="newsItem"
        :key="key"
        @mouseleave="removeActive($event, item)"
        @mouseenter="changeActive($event, item)"
      >
        <div class="title">{{ item.news }}</div>
        <div class="time">{{ item.time }}</div>
      </div>
    </div>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        newsList: [
          {
            news: '菜鸟助力国际奥委会疫苗项目 将首批中国新冠疫苗运抵巴拉圭',
            time: '2023-02-05',
            index: '0',
          },
          {
            news: '水滴保与前海再保险战略合作，为亚健康人群开发保险产品',
            time: '2023-02-05',
            index: '1',
          },
          {
            news: '自动驾驶道路安全等级标准立项 滴滴牵头编制',
            time: '2023-02-05',
            index: '2',
          },
          {
            news: '赢.战2021-华中战区总结暨行动计划！',
            time: '2023-02-05',
            index: '3',
          },
          {
            news: '凝聚智库力量 完善全球治理 ——二○一八当代中国与世界智库论坛开幕',
            time: '2023-02-05',
            index: '4',
          },
          {
            news: '管理资产超6000亿元，这家基金公司用蓝凌OA实现精细化管控',
            time: '2023-02-05',
            index: '5',
          },
        ],
      }
    },
    methods: {
      //手动切换幻灯片
      setActive(index) {
        this.$refs.carousel.setActiveItem(index)
      },
      changeActive(e, item) {
        this.setActive(item.index)
        e.currentTarget.className = 'activated'
      },
      removeActive(e, item) {
        e.currentTarget.className = 'newsItem'
      },
    },
  }
</script>
<style scoped lang="scss">
  .el-carousel__item h3 {
    color: #475669;
    font-size: 14px;
    opacity: 0.75;
    line-height: 200px;
    margin: 0;
  }

  .el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
  }

  .el-carousel__item:nth-child(2n + 1) {
    background-color: #d3dce6;
  }
  .above {
    display: flex;
    box-shadow: 2px 2px 5px 0px rgba(0, 0, 0, 0.3);
  }
  .carousel {
    flex: 3;
  }
  .news {
    flex: 2;
    display: flex;
    height: 400px;
    flex-direction: column;
    background: #fefefe;
  }
  .news .newsItem {
    display: flex;
    flex: 1;
    // background: #111;
  }
  .news .newsItem .title {
    flex: 8;
    padding: 10px;
  }
  .news .newsItem .time {
    flex: 2;
    padding: 10px;
  }
  .news .activated .time {
    flex: 2;
    padding: 10px;
  }
  .news .activated .title {
    flex: 8;
    padding: 10px;
  }
  .news .activated {
    display: flex;
    padding: 10px;
    flex: 2;
    border-bottom: 1px solid #555;
    background: #f1f1f1;
  }
</style>
