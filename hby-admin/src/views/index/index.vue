<!--<template>-->
<!--  <div>-->
<!--    <head-menu />-->
<!--    <show />-->
<!--    <course />-->
<!--  </div>-->
<!--</template>-->

<!--<script>-->
<!--  import Show from '@/views/index/components/show'-->
<!--  import Course from '@/views/index/components/course'-->
<!--  import HeadMenu from '@/views/index/components/headMenu'-->
<!--  export default {-->
<!--    name: 'Index',-->
<!--    components: { HeadMenu, Course, Show },-->
<!--  }-->
<!--</script>-->

<!--<style scoped></style>-->

<template>
  <div style="position: relative; height: 100%">
    <div v-if="type == '1'" style="height: 100%">
      <head-menu />
      <!-- <iframe
        id="bb"
        class="iframe"
        frameborder="no"
        name="bb"
        src="/shome/home.html"
      /> -->
    </div>

    <div v-if="type == '2'" style="height: 100%">
      <newHeadMenu class="header" />
      <div style="padding: 50px 0px 0px 0px">
        <Page2></Page2>
      </div>
      <!-- <div
        :style="{
          position: 'absolute',
          top: '50px',
          left: 0,
          width: '100%',
          bottom: 0,
          backgroundImage: `url(${zhBG})`,
          backgroundSize: 'cover',
          backgroundPosition: 'center',
          backgroundRepeat: 'no-repeat',
        }"
      ></div> -->
    </div>
    <div v-if="type == '3'" style="height: 100%">
      <newHeadMenu class="header" />
      <div style="padding: 60px 10px 0 10px">
        <Page1></Page1>
      </div>
    </div>
  </div>
</template>

<script>
  // import Show from '@/views/index/components/show'
  // import Course from '@/views/index/components/course'
  import HeadMenu from '@/views/index/components/headMenu'
  import newHeadMenu from '@/views/index/components/newHeadMenu'
  import Page1 from '@/views/index/home.vue'
  import Page2 from '@/views/index/components/searchHome.vue'
  import { getHomePageListData } from '@/api/setting/loginPage'
  import zhBG from '../../../public/img/zhBg.jpeg'
  export default {
    name: 'Index',
    components: { HeadMenu, newHeadMenu, Page1, Page2 },
    data() {
      return {
        type: '2',
        zhBG,
      }
    },
    created() {
      this.getList()
    },
    methods: {
      getList() {
        getHomePageListData().then((res) => {
          this.type = res.data.homePath
        })
      },
    },
  }
</script>

<style scoped>
  .iframe {
    width: 100%;
    height: 100%;
  }
  .header {
    position: fixed;
    top: 0;
    width: 100%;
  }
</style>
