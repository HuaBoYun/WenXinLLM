<template>
  <div class="module-view">
    <el-row :gutter="3.5">
      <el-col :span="4" v-for="item in lists" :key="item.id">
        <div class="module-item two" @click="openAi(item)">
          <vab-icon :class="theme.themeName" icon="book-mark-fill" id="icon" />
          <p>{{ item.name }}</p>
        </div>
      </el-col>
    </el-row>
    <Text ref="wenzi"></Text>
    <File ref="file"></File>
  </div>
</template>

<script>
  import { mapGetters } from 'vuex'
  import File from './components/fileModal.vue'
  import Text from './components/textModal.vue'
  export default {
    name: '',
    components: { Text, File },
    data() {
      return {
        lists: [
          {
            id: 0,
            name: 'PDF阅读',
            tip: '点击添加PDF文件',
            remake: '只支持PDF格式',
            button1: '添加文件',
            button2: '添加文件夹',
          },
          {
            id: 1,
            name: 'PDF编辑',
            tip: '点击添加PDF文件',
            remake: '只支持PDF格式',
            button1: '添加文件',
            button2: '添加文件夹',
          },
          {
            id: 3,
            name: 'PDF转长图',
            tip: '点击添加PDF文件',
            remake: '只支持PDF格式',
            button1: '添加文件',
            button2: '添加文件夹',
          },
          {
            id: 4,
            name: 'PDF合并',
            tip: '点击添加PDF文件',
            remake: '只支持PDF格式',
            button1: '添加文件',
            button2: '添加文件夹',
          },
          {
            id: 5,
            name: 'PDF分割',
            tip: '点击添加PDF文件',
            remake: '只支持PDF格式',
            button1: '添加文件',
            button2: '添加文件夹',
          },
          {
            id: 6,
            name: 'PDF解密',
            tip: '点击添加PDF文件',
            remake: '只支持PDF格式',
            button1: '添加文件',
            button2: '添加文件夹',
          },
          {
            id: 7,
            name: 'PDF加密',
            tip: '点击添加PDF文件',
            remake: '只支持PDF格式',
            button1: '添加文件',
            button2: '添加文件夹',
          },
          {
            id: 8,
            name: 'PDF图片提取',
            tip: '点击添加PDF文件',
            remake: '只支持PDF格式',
            button1: '添加文件',
            button2: '添加文件夹',
          },
          {
            id: 9,
            name: 'PDF去内页',
            tip: '点击添加PDF文件',
            remake: '只支持PDF格式',
            button1: '添加文件',
            button2: '添加文件夹',
          },
          {
            id: 10,
            name: 'PDF旋转',
            tip: '点击添加PDF文件',
            remake: '只支持PDF格式',
            button1: '添加文件',
            button2: '添加文件夹',
          },
          {
            id: 11,
            name: 'PDF加页码',
            tip: '点击添加PDF文件',
            remake: '只支持PDF格式',
            button1: '添加文件',
            button2: '添加文件夹',
          },
        ],
      }
    },
    computed: {
      ...mapGetters({
        theme: 'settings/theme',
      }),
    },

    methods: {
      openAi(name) {
        this.$refs['file'].show(name)
      },
    },
  }
</script>

<style lang="scss" scoped>
  .module-view {
    text-align: center;
    width: 100%;
    max-height: 720px;
    overflow-x: hidden;
    // margin-bottom: -10px;
    padding: 20px 50px;
    .module-item {
      // background: white;
      // padding: 1px 18px;
      // margin: 2px 4px 15px 4px;
      // box-shadow: 1px 1px 3px #888888;
      cursor: pointer;
      .menu-img {
        font-size: 48px;
        // color: $base-color-blue !important;
      }
    }
    .module-item:hover {
      // box-shadow: 1px 1px 8px #888888;
      #icon {
        transform: scale(1.2); /* 悬停时放大 20% */
      }
    }
  }
  .tank {
    height: 450px !important;
  }
  .five {
    padding: 1px 1px !important;
  }
  .two {
    padding: 1px 22px !important;
  }
  .three {
    padding: 1px 18px !important;
  }
  .four {
    padding: 1px 11.5px !important;
  }
  .green {
    font-size: 48px;
    color: #41b584;
  }
  .red {
    font-size: 48px;
    color: #e50113;
  }
  .default {
    font-size: 48px;
    color: $base-color-blue !important;
  }
  .ocean {
    font-size: 48px;
    color: $base-color-blue !important;
  }
  .white {
    font-size: 38px;
    color: $base-color-blue !important;
  }
</style>
