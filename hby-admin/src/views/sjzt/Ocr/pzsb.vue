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
            name: '识别身份证头像面',
            tip: '点击添加图片',
            remake: '支持PNG，JPEG，JPG，BMP图片格式，且文件<=2.5M',
            button1: '添加图片',
            button2: '添加图片文件夹',
          },
          {
            id: 1,
            name: '识别身份证国徽面',
            tip: '点击添加图片',
            remake: '支持PNG，JPEG，JPG，BMP图片格式，且文件<=2.5M',
            button1: '添加图片',
            button2: '添加图片文件夹',
          },
          {
            id: 3,
            name: '识别增值税发票',
            tip: '点击添加图片',
            remake: '支持PNG，JPEG，JPG，BMP图片格式，且文件<=2.5M',
            button1: '添加图片',
            button2: '添加图片文件夹',
          },
          {
            id: 4,
            name: '识别定额发票',
            tip: '点击添加图片',
            remake: '支持PNG，JPEG，JPG，BMP图片格式，且文件<=2.5M',
            button1: '添加图片',
            button2: '添加图片文件夹',
          },
          {
            id: 5,
            name: '识别银行卡',
            tip: '点击添加图片',
            remake: '支持PNG，JPEG，JPG，BMP图片格式，且文件<=2.5M',
            button1: '添加图片',
            button2: '添加图片文件夹',
          },
          {
            id: 6,
            name: '识别营业执照',
            tip: '点击添加图片',
            remake: '支持PNG，JPEG，JPG，BMP图片格式，且文件<=2.5M',
            button1: '添加图片',
            button2: '添加图片文件夹',
          },
          {
            id: 7,
            name: '识别驾驶证',
            tip: '点击添加图片',
            remake: '支持PNG，JPEG，JPG，BMP图片格式，且文件<=2.5M',
            button1: '添加图片',
            button2: '添加图片文件夹',
          },
          {
            id: 8,
            name: '识别护照',
            tip: '点击添加图片',
            remake: '支持PNG，JPEG，JPG，BMP图片格式，且文件<=2.5M',
            button1: '添加图片',
            button2: '添加图片文件夹',
          },
          {
            id: 9,
            name: '识别出租车票',
            tip: '点击添加图片',
            remake: '支持PNG，JPEG，JPG，BMP图片格式，且文件<=2.5M',
            button1: '添加图片',
            button2: '添加图片文件夹',
          },
          {
            id: 10,
            name: '识别火车票',
            tip: '点击添加图片',
            remake: '支持PNG，JPEG，JPG，BMP图片格式，且文件<=2.5M',
            button1: '添加图片',
            button2: '添加图片文件夹',
          },
          {
            id: 11,
            name: '识别驾驶证',
            tip: '点击添加图片',
            remake: '支持PNG，JPEG，JPG，BMP图片格式，且文件<=2.5M',
            button1: '添加图片',
            button2: '添加图片文件夹',
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
