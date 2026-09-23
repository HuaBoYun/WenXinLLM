<template>
  <div class="container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="集团站点导航" name="first">
        <div class="content">
          <div class="banner">
            <img
              src="https://www.huabao.example.com/upload/thumb_src/314_130/1522672286.png"
              alt=""
            />
          </div>
          <div class="banner">
            <img
              src="https://www.huabao.example.com/upload/thumb_src/314_130/1522672033.png"
              alt=""
            />
          </div>
          <div class="banner">
            <img
              src="https://www.huabao.example.com/upload/thumb_src/314_130/1522672361.png"
              alt=""
            />
          </div>
          <div class="banner">
            <img
              src="https://www.huabao.example.com/upload/thumb_src/314_130/1522721004.png"
              alt=""
            />
          </div>
          <div class="banner">
            <img
              src="https://www.huabao.example.com/upload/thumb_src/314_130/1522672592.png"
              alt=""
            />
          </div>
          <div class="banner">
            <img
              src="https://www.huabao.example.com/upload/thumb_src/314_130/1522672464.png"
              alt=""
            />
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
  export default {
    name: '222',
    data() {
      return {
        activeName: 'first',
      }
    },
  }
</script>

<style scoped lang="scss">
  .container {
    background-color: #fff;
  }
  .content {
    display: flex;
    justify-content: space-around;
    background-color: #fff;
    padding: 10px 0;
  }
  .banner {
    width: 15%;
  }
  img {
    width: 100%;
  }
</style>
