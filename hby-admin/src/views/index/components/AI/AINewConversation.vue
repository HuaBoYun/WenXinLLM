<template>
  <div style="width: 810px;">
    <div class="search-input">
      <div class="intro">{{ during }}，{{ userName }}</div>
      <div class="input-content">
        <el-input
          class="input-style"
          v-model="searchKey"
          clearable
          type="textarea"
          :rows="1"
          autosize
          :placeholder="curAgent.bg_word"
          @keydown.enter.native="onSendMessage"
        />
        <div class="btn-group">
          <div class="icons" style="display: flex;align-items: center;">
            <div @click="think = !think" class="deep" :style="`${think?'color:#4D6BFE;background:#DBEAFE;border-color:rgba(0, 122, 255, 0.15);':''}`">
              深度思考
              <i v-if="!think" class="btn-icon el-icon-turn-off"></i>
              <i v-else class="btn-icon el-icon-open"></i>
            </div>
            <div @click="search = !search" class="deep" :style="`${search?'color:#4D6BFE;background:#DBEAFE;border-color:rgba(0, 122, 255, 0.15);':''}`">
              联网
              <i v-if="!search" class="btn-icon el-icon-turn-off"></i>
              <i v-else class="btn-icon el-icon-open"></i>
            </div>
            <fileUpload v-model="fileObject" />
          </div>

          <div class="icons">
            <voice class="icon" @getMessage="getMessage" />
            <i class="icon el-icon-s-promotion send" @click="onSendMessage"></i>
          </div>
        </div>
      </div>
    </div>

    <div class="relative-list">
      <div :class="`list-item ${mode === item.skill_id ? item.color : ''}`" v-for="item in relativeList" :key="item.title" @click="onSelectMode(item)">
        <img class="icon" :src="item.icon" alt="">
        <div class="btn-title">{{ item.title }}</div>
      </div>
    </div>

    <div class="divider">— 文档 用户案例展示 —</div>

    <div class="relative-cards">
      <div class="card-item" :class="{ 'card-item-actived': item.actived }" v-for="item in curAgent.user_case" :key="item.case_id" @click="onCardClick(item)">
        <div class="card-img">
          <img :src="item.bg_pic[0]" alt="" >
        </div>
        <div class="card-title">
          {{ item.title }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import voice from './voice.vue'
  import fileUpload from './fileUpload.vue'
  import doc from './icon/doc.svg'
  import ppt from './icon/ppt.svg'
  import excel from './icon/excel.svg'
  import html from './icon/html.svg'
  import normal from './icon/normal.png'
  import config_list from './mock/agentMode.js'

  export default {
    components: { voice, fileUpload },
    data() {
      return {
        searchKey: '',
        source: '全网搜索',
        userName: '',
        during: '上午好',
        think: false,
        search: false, // 联网搜索
        mode: '101',
        relativeList: [
          {
            title: '文档模式',
            icon: doc,
            color: 'blue',
            skill_id: '101'
          },
          {
            title: 'PPT模式',
            icon: ppt,
            color: 'red',
            skill_id: '102'
          },
          {
            title: '表格模式',
            icon: excel,
            color: 'green',
            skill_id: '103'
          },
          {
            title: '网页模式',
            icon: html,
            color: 'purple',
            skill_id: '105'
          },
          {
            title: '通用模式',
            icon: normal,
            color: 'weekblue',
            skill_id: '100'
          },
        ],
        allList: [
          {title: '吴恩达：如何在 AI 领域制定职业规划.pdf', desc: 'PDF 3MB', content:'我是一名公众号博主，帮我写一篇关于[主题]的文章',icon: 'https://p3-flow-imagex-sign.byteimg.com/ocean-cloud-tos/pdf/80e2f3bbe9ab4a21803dd6e9d65dbfad.pdf_0_2400.jpg~tplv-a9rns2rl98-image-qvalue.jpeg?rk3s=1567c5c4&x-expires=1771125006&x-signature=JmOFf0vVZdsJrGdKAvXZLq%2Ft3jI%3D', actived: false},
          {title: '2024全国高考理科数学真题.pdf', desc: 'PDF 3MB', content:'我是一个博主，帮我写一篇关于[主题]的[平台：如公众号、知乎、头条等]文章，需要符合该平台写作风格。',icon: 'https://p9-flow-imagex-sign.byteimg.com/ocean-cloud-tos/pdf/7d8ccaf99a6fca4a9b2f8d9300757f02_0_960.jpg~tplv-a9rns2rl98-image-qvalue.jpeg?rk3s=1567c5c4&x-expires=1771378767&x-signature=O7hcHZDN8i7g1tVhCG5dNRjU3vY%3D', actived: false},
          {title: '中等收入陷阱.pdf', desc: 'PDF 3MB', content:'帮我写 5 个面向[人群]宣传[产品]的品牌营销slogan，简洁吸睛，富有创意。',icon: 'https://p3-flow-imagex-sign.byteimg.com/ocean-cloud-tos/pdf/b9248a3d7a72a629563ecb66cd084405_0_960.jpg~tplv-a9rns2rl98-image-qvalue.jpeg?rk3s=1567c5c4&x-expires=1771378768&x-signature=sTZYCXyTm4NTna%2BFk57RWJzG%2F08%3D', actived: false},
          {title: '中华人民共和国民法典.pdf', desc: 'PDF 3MB', content:'帮我写 5 个面向[人群]宣传[产品]的品牌营销slogan，简洁吸睛，富有创意。',icon: 'https://p3-flow-imagex-sign.byteimg.com/ocean-cloud-tos/pdf/8ddf03557b0d6ea1e7cc5889ffb1a5d0_0_960.jpg~tplv-a9rns2rl98-image-qvalue.jpeg?rk3s=1567c5c4&x-expires=1771378767&x-signature=bTwiPy8uF43G02STG%2Fatnb%2Br6so%3D', actived: false},
          {title: '2024全国高考理科数学真题.pdf', desc: 'PDF 3MB', content:'帮我写 5 个面向[人群]宣传[产品]的品牌营销slogan，简洁吸睛，富有创意。',icon: 'https://p9-flow-imagex-sign.byteimg.com/ocean-cloud-tos/pdf/f42870d0cac2540f4ddf1658b93915c4_0_960.jpg~tplv-a9rns2rl98-image-qvalue.jpeg?rk3s=1567c5c4&x-expires=1771378767&x-signature=Iok5DBU3aPRttCwR8lf96ieEPBE%3D', actived: false},
        ],
        curAgent: {},
        fileObject: {},
        fileContent: ''
      }
    },
    created() {
      const userInfo = JSON.parse(localStorage.getItem('userInfo'))
      this.userName = userInfo.realname
      const time = new Date().getHours()
      if (time >= 0 && time < 12) {
        this.during = '上午好'
      }
      if (time >= 12 && time < 18) {
        this.during = '下午好'
      }
      if (time >= 18) {
        this.during = '晚上好'
      }

      this.curAgent = config_list.find(x => x.skill_id === '101')
    },
    methods: {
      onSendMessage() {
        if (!this.searchKey) return
        this.$emit('chatNow', { msg: this.searchKey, think: this.think, search: this.search, fileObject: this.fileObject })
      },
      getMessage(rectxt) {
        this.searchKey = rectxt
      },
      onSelectMode(item) {
        this.mode = item.skill_id
        this.curAgent = config_list.find(x => x.skill_id === item.skill_id)
      },
      onCardClick(item) {
        this.$emit('chatNow', { msg: item.content, think: this.think, search: this.search })
      }
    }
  }
</script>

<style scoped>
  .search-input {
    text-align: center;
    /* background: #fff; */
    padding-top: 120px;
  }

  .menu-item {
    display: flex;
    align-items: center;
    justify-content: center;
    font-weight: 700;
  }

  .menu-icon {
    width: 24px;
    height: 24px;
    line-height: 24px;
    text-align: center;
  }

  .search-input .intro {
    font-size: 28px;
    margin-top: 12px;
    margin-bottom: 24px;
    color: #000;
    font-weight: 700;
  }

  .input-content {
    display: flex;
    border: 1px solid rgba(0, 0, 0, 0.1);
    border: 1px solid rgba(0, 0, 0, 0.1);
    border-radius: 20px;
    display: flex;
    gap: 8px;
    justify-content: space-between;
    padding: 12px 14px 12px 2px;
    flex-direction: column;
    background: #fff;
  }

  .input-style >>> textarea {
    border: none;
    outline: none;
    padding: 14px;
    margin: 0;
    background-image: none;
    background-color: transparent;
    width: 100%;
    resize: none;
  }

  .input-style >>> textarea:focus {
    outline: none;
  }

  .input-style >>> .textarea_ai {
    display: none;
  }

  .select-style {
    width: 100px;
  }

  .select-style >>> input {
    border-radius: 10px;
  }

  .btn-group {
    display: flex;
    justify-content: space-between;
    margin-left: 14px;
    margin-top: 20px;
  }

  .icons .icon {
    font-size: 20px;
    font-weight: 700;
    cursor: pointer;
    margin-right: 10px;
  }

  .send {
    width: 32px;
    height: 32px;
    line-height: 32px;
    border-radius: 50%;
    background-color: rgba(0, 0, 0, 0.15);
    color: #fff;
    margin-left: 12px;
  }

  .send:hover {
    background-color: rgba(0, 0, 0, 0.4);
  }

  .relative-list {
    margin-top: 20px;
    display: grid;
    grid-template-columns: repeat(5, 1fr);
    gap: 12px;
    text-align: center;
  }

  .relative-list .list-item {
    font-size: 18px;
    padding: 16px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    border-radius: 12px;
    cursor: pointer;
    background: #fff;
  }

  .relative-list .list-item:hover {
    border: 1px solid #dde8ff;
  }

  .blue {
    border: 1px solid #dde8ff;
    background: #ecf2ff !important;
    color: #4d5effff;
  }

  .red {
    border: 1px solid #FCE7EC;
    background: #FDF2F3 !important;
    color: #FF576D;
  }

  .green {
    border: 1px solid #C6FFEF;
    background: #E7FFF9 !important;
    color: #00C9A7;
  }

  .purple {
    border: 1px solid #E9E8FF;
    background: #F3F2FF !important;
    color: #7357FF;
  }

  .weekblue {
    border: 1px solid #E0EDFF;
    background: #F0F6FF !important;
    color: #3385FF;
  }
  

  .relative-list .icon {
    font-size: 14px;
    font-weight: 700;
    margin-bottom: 10px;
    border-radius: 50%;
    padding: 5px;
    background-color: #fff;
    width: 28px;
    height: 28px;
  }
  .relative-list .btn-title {
    font-size: 14px;
  }

  .deep {
    display: flex;
    align-items: center;
    margin-right: 10px;
    padding: 5px 8px;
    border: 1px solid rgba(0, 0, 0, 0.5);
    border-radius: 14px;
    cursor: pointer;
    font-size: 14px;
  }

  .deep:hover {
    background:#DBEAFE;
    border-color:rgba(0, 122, 255, 0.15);
  }

  .btn-icon {
    width: 18px;
    height: 18px;
    font-size: 18px;
    margin-right: 0;
    margin-left: 4px;
  }

  .divider {
    text-align: center;
    padding: 20px 0;
    color: #8e8e8e;
  }

  .relative-cards {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 10px;
    margin-bottom: 20px;
    /* padding-bottom: 180px; */
  }

  .card-item {
    border: 1px solid rgba(0,0,0,.08);
    width: 262px;
    height: 216px;
    cursor: pointer;
    border-radius: 16px;
    overflow: hidden;
    background: #fff;
  }

  .card-item:hover {
    box-shadow: 0 6px 10px 0 rgba(42, 60, 79, .1);
  }
  .card-item-actived {
    border-color: #0057ff;
  }

  .card-img {
    width: 100%;
    height: 160px;
    background: #f3f4f6;
    display: flex;
    justify-content: center;
    padding-top: 10px;
    padding-left: 20px;
    padding-right: 20px;
    overflow: hidden;
  }

  .card-img img {
    width: 100%;
    object-position: top;
    object-fit: cover;
    box-shadow: 0px 6px 10px 0px rgba(0,0,0,.08),0px 0px 1px 0px rgba(0,0,0,.15);
    border-radius: 4px;
    height: fit-content;
  }
  .card-title {
    padding: 10px 12px;
    display: flex;
    align-items: center;
    width: 100%;
    justify-content: space-between;
  }
</style>
