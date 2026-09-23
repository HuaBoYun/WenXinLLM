<template>
  <div>
    <div v-for="(item, index) in List" :key="index">
      <div class="list-item">
        <div class="list-top">
          <label name="sortNum">{{ index + 1 }}.</label>
          <span class="list-title">
            {{ item.title }}
          </span>
        </div>
        <div class="list-info">
          {{ item.info }}
        </div>
      </div>

      <div class="divider"></div>
    </div>
    <div class="loading-more">加载更多</div>
  </div>
</template>

<script>
  export default {
    name: 'flfgkRightTopList',
    props: ['List'],
    data() {
      return {}
    },
    methods: {},
  }
</script>
<style scoped lang="scss">
  .list-item {
    padding: 14px 0;
  }
  .list-top {
    padding-bottom: 10px;
    line-height: 36px;
    font-size: 16px;
  }
  .list-title {
    color: #333;
  }
  .list-info {
    font-size: 14px;
    color: #999;
    position: relative;
    max-height: 60px;
    overflow: hidden;
  }
  .divider {
    display: block;
    height: 1px;
    width: 100%;
    border: 1px dashed #dcdfe6;
  }
  .loading-more {
    padding: 20px 0 10px;
    width: 100%;
    text-align: center;
    color: #999;
    cursor: pointer;
  }
</style>
