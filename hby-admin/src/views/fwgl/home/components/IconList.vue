<template>
  <div>
    <el-col
      v-for="(item, index) in iconList"
      :key="index"
      :lg="3"
      :md="3"
      :sm="6"
      :xl="3"
      :xs="12"
    >
      <el-card class="icon-panel" shadow="hover" @click.native="toCard(item)">
        <vab-icon :icon="item.icon" :style="{ color: item.color }" />
        <el-badge class="item" :value="12">
          <p>{{ item.title }}</p>
        </el-badge>
      </el-card>
    </el-col>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        // 卡片图标
        iconList: [
          {
            click: 'randomTheme',
            icon: 'archive-line',
            title: '我的待办',
            link: '/workbench/msg/wddb',
            color: '#95de64',
          },
          {
            click: 'changeTheme',
            icon: 'anticlockwise-2-line',
            title: '我的传阅',
            link: '/workbench/msg/wdcy',
            color: '#69c0ff',
          },
          {
            click: 'changeTheme',
            icon: 'aline-chart-fill',
            title: '我发起的',
            link: '/workbench/msg/wfqd',
            color: '#69c0ff',
          },
          {
            icon: 'message-2-line',
            title: '我的评价',
            link: '',
            color: '#5cdbd3',
          },
          {
            icon: 'book-3-line',
            title: '规则预警',
            link: '',
            color: '#1890FF',
          },
          {
            icon: 'book-2-line',
            title: '规则方案预警',
            link: '/vab/player',
            color: '#ffc069',
          },
          {
            icon: 'line-chart-fill',
            title: '指标预警',
            link: '',
            color: '#b37feb',
          },
          {
            click: 'handleMore',
            icon: 'layout-left-2-line',
            title: '指标方案预警',
            link: '',
            color: '#ff85c0',
          },
          {
            click: 'handleMore',
            icon: 'layout-4-line',
            title: '模型预警',
            link: '',
            color: '#ffd666',
          },
        ],
      }
    },
    methods: {
      changeTheme() {
        this.$baseEventBus.$emit('theme')
      },
      handleMore() {
        this.$baseAlert('敬请期待！')
      },
      randomTheme() {
        this.$baseEventBus.$emit('random-theme')
      },
      toCard(item) {
        console.warn('tocard', item)
        this.$router.push(item.link)
      },
    },
  }
</script>

<style lang="scss" scoped>
  .icon-panel {
    height: 120px;
    text-align: center;
    cursor: pointer;

    &:hover {
      i {
        transform: scale(1.15);
      }
    }

    i {
      display: block;
      width: 50px;
      height: 50px;
      margin: auto;
      font-size: 40px;
      transition: all ease-in-out 0.3s;
    }

    p {
      margin-top: 10px;
    }
  }
</style>
