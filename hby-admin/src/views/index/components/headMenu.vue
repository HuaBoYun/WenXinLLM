<template>
  <div class="header-view">
    <!--    <div class="header-left">-->
    <!--      <vab-icon v-if="logo" :icon="logo" is-custom-svg />-->
    <!--      <label>{{ title }}</label>-->
    <!--    </div>-->
    <div class="header-left">
      <img :src="imgUrl" alt="" style="width: 35px" />
    </div>
    <div class="header-right">
      <change-module :moduleLists="moduleLists" />
      <vab-avatar class="index-menu" />
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
    position: relative;
    top: 8px;
    left: 20px;
    /* padding: 10px 20px; */
  }
  .header-view > div {
    display: flex;
    align-items: center;
  }
  .header-left .vab-icon {
    width: 40px;
    height: 40px;
  }
  .header-left {
    position: absolute;
    left: 40px;
    top: 10px;
  }
  .header-left label {
    margin-left: 10px;
    font-size: 20px;
  }
  .header-right {
    display: flex;
    position: absolute;
    right: 40px;
    top: 10px;
  }
</style>
