<template>
  <div>
    <el-drawer
      append-to-body
      custom-class="vab-drawer"
      direction="rtl"
      size="760px"
      title="问心大模型"
      :visible.sync="drawerVisible"
    >
      <div class="module-view">
        <el-row :gutter="3.5">
          <el-divider content-position="center">法</el-divider>
          <el-col :span="3">
            <div class="module-item two" @click="openAi">
              <vab-icon :class="this.theme.themeName" icon="book-mark-fill" />
              <p>宪法</p>
            </div>
          </el-col>
          <el-col :span="3">
            <div class="module-item three" @click="openAi">
              <vab-icon :class="this.theme.themeName" icon="book-mark-fill" />
              <p>民法典</p>
            </div>
          </el-col>
          <el-col :span="3">
            <div class="module-item four" @click="openAi">
              <vab-icon :class="this.theme.themeName" icon="auction-line" />
              <p>知识产权</p>
            </div>
          </el-col>

          <el-col :span="3">
            <div class="module-item three" @click="openAi">
              <vab-icon :class="this.theme.themeName" icon="book-mark-fill" />
              <p>会计法</p>
            </div>
          </el-col>
          <el-col :span="3">
            <div class="module-item three" @click="openAi">
              <vab-icon :class="this.theme.themeName" icon="book-mark-fill" />
              <p>审计法</p>
            </div>
          </el-col>
          <el-col :span="3">
            <div class="module-item three" @click="openAi">
              <vab-icon :class="this.theme.themeName" icon="bank-line" />
              <p>金融业</p>
            </div>
          </el-col>
          <el-col :span="3">
            <div class="module-item four" @click="openAi">
              <vab-icon :class="this.theme.themeName" icon="coupon-2-line" />
              <p>证券期货</p>
            </div>
          </el-col>

          <el-col :span="3">
            <div class="module-item two" @click="openAi">
              <vab-icon :class="this.theme.themeName" icon="apps-line" />
              <p>公用</p>
            </div>
          </el-col>
          <el-col :span="3">
            <div class="module-item four" @click="openAi">
              <vab-icon :class="this.theme.themeName" icon="home-4-line" />
              <p>地产建筑</p>
            </div>
          </el-col>

          <el-col :span="3">
            <div class="module-item three" @click="openAi">
              <vab-icon :class="this.theme.themeName" icon="apps-line" />
              <p>农牧业</p>
            </div>
          </el-col>
          <el-col :span="3">
            <div class="module-item four" @click="openAi">
              <vab-icon :class="this.theme.themeName" icon="truck-line" />
              <p>交通运输</p>
            </div>
          </el-col>

          <el-col :span="3">
            <div class="module-item two" @click="openAi">
              <vab-icon
                :class="this.theme.themeName"
                icon="money-cny-circle-line"
              />
              <p>贸易</p>
            </div>
          </el-col>
          <el-col :span="3">
            <div class="module-item two" @click="openAi">
              <vab-icon :class="this.theme.themeName" icon="apps-line" />
              <p>军工</p>
            </div>
          </el-col>
          <el-col :span="3">
            <div class="module-item two" @click="openAi">
              <vab-icon :class="this.theme.themeName" icon="apps-line" />
              <p>制造</p>
            </div>
          </el-col>
          <el-col :span="3">
            <div class="module-item two" @click="openAi">
              <vab-icon :class="this.theme.themeName" icon="apps-line" />
              <p>能源</p>
            </div>
          </el-col>
          <el-col :span="3">
            <div class="module-item two" @click="openAi">
              <vab-icon :class="this.theme.themeName" icon="apps-line" />
              <p>其他</p>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="3.5" style="margin-top: -15px">
          <el-divider content-position="center">人</el-divider>
          <el-col :span="3">
            <div class="module-item four" @click="openAi">
              <vab-icon :class="this.theme.themeName" icon="apps-line" />
              <p>文案创作</p>
            </div>
          </el-col>
          <el-col :span="3">
            <div class="module-item four" @click="openAi">
              <vab-icon :class="this.theme.themeName" icon="apps-line" />
              <p>文章创作</p>
            </div>
          </el-col>
          <el-col :span="3">
            <div class="module-item four" @click="openAi">
              <vab-icon :class="this.theme.themeName" icon="apps-line" />
              <p>生成大纲</p>
            </div>
          </el-col>
          <el-col :span="3">
            <div class="module-item four" @click="openAi">
              <vab-icon :class="this.theme.themeName" icon="apps-line" />
              <p>总结摘要</p>
            </div>
          </el-col>
          <el-col :span="3">
            <div class="module-item five" @click="openAi">
              <vab-icon :class="this.theme.themeName" icon="apps-line" />
              <p>短视频脚本</p>
            </div>
          </el-col>
          <el-col :span="3">
            <div class="module-item four" @click="openAi">
              <vab-icon :class="this.theme.themeName" icon="apps-line" />
              <p>答疑解惑</p>
            </div>
          </el-col>
          <el-col :span="3">
            <div class="module-item four" @click="openAi">
              <vab-icon :class="this.theme.themeName" icon="apps-line" />
              <p>语法解释</p>
            </div>
          </el-col>
          <el-col :span="3">
            <div class="module-item four" @click="openAi">
              <vab-icon :class="this.theme.themeName" icon="apps-line" />
              <p>推广文案</p>
            </div>
          </el-col>
          <el-col :span="3">
            <div class="module-item five" @click="openAi">
              <vab-icon :class="this.theme.themeName" icon="apps-line" />
              <p>周报生成器</p>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="3.5" style="margin-top: -15px">
          <el-divider content-position="center">财</el-divider>
          <el-col :span="3">
            <div class="module-item two" @click="openAi">
              <vab-icon :class="this.theme.themeName" icon="apps-line" />
              <p>报告</p>
            </div>
          </el-col>
          <el-col :span="3">
            <div class="module-item two" @click="openAi">
              <vab-icon :class="this.theme.themeName" icon="apps-line" />
              <p>改写</p>
            </div>
          </el-col>
          <el-col :span="3">
            <div class="module-item four" @click="openAi">
              <vab-icon :class="this.theme.themeName" icon="apps-line" />
              <p>优化润色</p>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="3.5">
          <el-divider content-position="center">物</el-divider>
          <el-col :span="3">
            <div class="module-item two" @click="openAi">
              <vab-icon :class="this.theme.themeName" icon="apps-line" />
              <p>歌曲</p>
            </div>
          </el-col>
          <el-col :span="3">
            <div class="module-item two" @click="openAi">
              <vab-icon :class="this.theme.themeName" icon="apps-line" />
              <p>歌词</p>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-drawer>

    <Deaw ref="drew"></Deaw>
  </div>
</template>

<script>
  import { mapGetters } from 'vuex'
  import Deaw from './detail.vue'

  export default {
    name: 'typeModal',
    components: { Deaw },
    data() {
      return {
        drawerVisible: false,
      }
    },
    computed: {
      ...mapGetters({
        theme: 'settings/theme',
      }),
    },
    created() {
      this.$baseEventBus.$on('ai', () => {
        this.handleOpenAI1()
      })
    },
    methods: {
      openAi() {
        this.$refs['drew'].open()
      },
      handleOpenAI1() {
        this.drawerVisible = true
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
    margin-bottom: -10px;
    .module-item {
      background: white;
      padding: 1px 18px;
      margin: 2px 4px 15px 4px;
      box-shadow: 1px 1px 3px #888888;
      cursor: pointer;
      .menu-img {
        font-size: 48px;
        // color: $base-color-blue !important;
      }
    }
    .module-item:hover {
      box-shadow: 1px 1px 8px #888888;
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
