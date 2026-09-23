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
            name: '文本翻译',
            tip: '点击添加文本',
            remake: '支持PNG，JPEG，JPG，BMP图片格式，且文件<=2.5M',
            button1: '添加图片',
            button2: '添加图片文件夹',
          },
          {
            id: 1,
            name: '图片翻译',
            tip: '点击添加图片',
            remake: '支持PNG，JPEG，JPG，BMP图片格式，且文件<=2.5M',
            button1: '添加图片',
            button2: '添加图片文件夹',
          },
          {
            id: 3,
            name: 'PDF翻译',
            tip: '点击添加PDF文件',
            remake:
              '支持PDF格式，为达到最佳效果，支持文件<50M，文件页数<400，字数<100万个',
            button1: '添加文件',
            button2: '添加文件夹',
          },
          {
            id: 4,
            name: 'Word翻译',
            tip: '点击添加Word文件',
            remake:
              '支持Word格式，为达到最佳效果，支持文件<50M，文件页数<400，字数<100万个',
            button1: '添加word文件',
            button2: '添加Word文件夹',
          },
          {
            id: 5,
            name: 'Excel翻译',
            tip: '点击添加Excel文件',
            remake:
              '支持Excel格式，为达到最佳效果，支持文件<50M，文件页数<400，字数<100万个',
            button1: '添加Excel文件',
            button2: '添加Excel文件夹',
          },
          {
            id: 6,
            name: 'PPT翻译',
            tip: '点击添加PPT文件',
            remake:
              '支持PPT格式，为达到最佳效果，支持文件<50M，文件页数<400，字数<100万个',
            button1: '添加PPT文件',
            button2: '添加PPT文件夹',
          },
          {
            id: 7,
            name: '音频翻译',
            tip: '点击添加音频',
            remake:
              '*支持MP3、WAV、M4A、AMR、WMA、OGG、AAC、FLAC格式，文件<200M&3小时',
            button1: '添加音频文件',
            button2: '添加音频文件夹',
          },
          {
            id: 8,
            name: '视频翻译',
            tip: '点击添加视频文件',
            remake:
              '*支持MP4、MKV、MOVE、AVIJ、MAV、OGV、FLV、F4V格式，文件<200M &3小时，文件数<10',
            button1: '添加视频文件',
            button2: '添加视频文件夹',
          },
          {
            id: 9,
            name: 'TXT翻译',
            tip: '点击添加TXT文件',
            remake: '支持TXT格式，为达到最佳效果，支持文件<50M， 字数<100万个',
            button1: '添加TXT文件',
            button2: '添加TXT文件夹',
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
