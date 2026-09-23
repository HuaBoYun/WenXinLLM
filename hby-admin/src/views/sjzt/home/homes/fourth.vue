<template>
  <div class="content">
    <div class="banner">
      <img src="@/assets/1.png" alt="" />
    </div>
    <div class="banner">
      <img src="@/assets/2.png" alt="" />
    </div>
    <div class="banner">
      <img src="@/assets/3.png" alt="" />
    </div>
    <div class="banner">
      <img src="@/assets/4.png" alt="" />
    </div>
  </div>
</template>

<script>
  export default {
    name: '222',

    data() {
      return {}
    },
  }
</script>

<style scoped lang="scss">
  .content {
    display: flex;
    justify-content: space-around;
    background-color: #fff;
    padding: 10px 0;
  }
  .banner {
    width: 24%;
    // height: 180px;
  }
  img {
    width: 100%;
    // height: 100%;
  }
</style>
