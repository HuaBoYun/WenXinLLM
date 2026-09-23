<template>
  <div class="header-view">
    <div class="header-left">
      <img :src="imgUrl" alt="" style="width: 30px; margin-left: 20px" />
      <!-- <div class="title">AI合规</div> -->
      <!-- <div class="title">星光AI</div> -->
      <!-- <div class="title">中核风控合法审一体化系统</div> -->
      <div class="title">问心大模型</div>
      <!-- <vab-icon v-if="!imgUrl" :icon="logo" is-custom-svg /> -->
      <!-- <label>{{ title }}</label> -->
    </div>
    <div class="header-right">
      <change-module :moduleLists="moduleLists" />
      <vab-avatar />
    </div>
  </div>
</template>

<script>
  import ChangeModule from '@/components/change/module'
  import { mapGetters } from 'vuex'
  import VabAvatar from '@/vab/components/VabAvatar'
  import { getModuleList } from '@/api/setting/system'
  import defaultHomeLogo from '@/assets/zhezi.png' //默认首页logo图

  export default {
    name: 'HeadMenu',
    components: { VabAvatar, ChangeModule },
    data() {
      return {
        moduleLists: [],
        imgUrl: defaultHomeLogo,
      }
    },
    computed: {
      ...mapGetters({
        logo: 'settings/logo',
        title: 'settings/title',
      }),
    },
    beforeCreate() {
      //
      // this.imgUrl =
      //   JSON.parse(localStorage.getItem('UsingHomeLogoPic')) || defaultHomeLogo
      //
    },
    mounted() {
      this.moduleList()
    },
    methods: {
      moduleList() {
        getModuleList({
          pageNumber: 1,
          pageSize: 20,
        }).then((res) => {
          this.moduleLists = res.data
        })

        this.imgUrl =
          localStorage.getItem('UsingHomeLogoPic') || defaultHomeLogo
      },
    },
  }
</script>

<style scoped>
  .header-view {
    z-index: 999;
    background: linear-gradient(to right, #2c3239, #444e5d) !important;
    /* background-color: #000000; */
    height: 50px;
    .el-dropdown {
      color: #fff !important;
    }
  }
  /* .header-view {
    position: absolute;
    top: 8px;
    right: 20px;
    padding: 10px 20px;
    z-index: 999;
  } */
  .header-view {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 15px 10px;
  }

  .header-left {
    display: flex;
    align-items: center;
    color: #fff;
    font-size: 18px;
  }

  .title {
    margin-left: 5px;
    letter-spacing: 3px;
  }
  .header-left .vab-icon {
    width: 40px;
    height: 40px;
  }
  .header-left label {
    margin-left: 10px;
    font-size: 20px;
  }
  .header-right {
    display: flex;
    align-items: center;
  }
  .index-menu {
    color: red !important;
  }
</style>
