<template>
  <div class="container">
    <div class="text-content">
      <div class="page-title">
        <div class="menu-item">
          <i :class="`menu-icon el-icon-view`"></i>
          <div class="menu-title">AI 阅读</div>
        </div>
      </div>
      <div class="intro">论文课件，财报合同，翻译总结</div>
    </div>

    <div class="relative-cards">
      <div class="card-item" :class="{ 'card-item-actived': item.actived }" v-for="item in allList" :key="item.title" @click="onCardClick(item)">
        <div class="card-img"><img :src="item.icon" alt="" ></div>
        <div class="card-title">
          <img class="card-icon" :src="pdfsvg" alt="">
          <div>
            <div class="card-title-1">{{ item.title }}</div>
            <div class="card-desc">{{ item.desc }}</div>
          </div>
        </div>
      </div>
    </div>

    <div class="search-input">
      <div class="input-content">
        <el-input
          class="input-style"
          v-model="searchKey"
          clearable
          placeholder="询问这篇文章的任何问题"
          @keydown.enter.native="onSendMessage"
        />
        <div class="btn-group">
          <div class="icons">
            <i class="icon el-icon-paperclip"></i>
            <!-- <i class="icon el-icon-picture"></i>
            <i class="icon el-icon-scissors"></i> -->
            <voice class="icon" @getMessage="getMessage" />
            <i class="icon el-icon-top send" @click="onSendMessage"></i>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import pdfsvg from './icon/pdf.svg'
  import voice from './voice.vue'
  export default {
    components: { voice },
    data() {
      return {
        searchKey: '',
        source: '参考资料',
        userName: '',
        relativeKey: 'all',
        value1: '',
        value2: '',
        pdfsvg,
        relativeList: [
          { title: '全部', key: 'all', actived: true },
          { title: '工作', key: 'work', actived: false },
          { title: '商业营销', key: 'business', actived: false },
          { title: '学习/教育', key: 'study', actived: false },
          { title: '社媒文章', key: 'article', actived: false },
          { title: '文学艺术', key: 'art', actived: false },
          { title: '回复和改写', key: 'reply', actived: false },
        ],
        allList: [
          {title: '吴恩达：如何在 AI 领域制定职业规划.pdf', desc: 'PDF 3MB', content:'我是一名公众号博主，帮我写一篇关于[主题]的文章',icon: 'https://p3-flow-imagex-sign.byteimg.com/ocean-cloud-tos/pdf/80e2f3bbe9ab4a21803dd6e9d65dbfad.pdf_0_2400.jpg~tplv-a9rns2rl98-image-qvalue.jpeg?rk3s=1567c5c4&x-expires=1771125006&x-signature=JmOFf0vVZdsJrGdKAvXZLq%2Ft3jI%3D', actived: false},
          {title: '2024全国高考理科数学真题.pdf', desc: 'PDF 3MB', content:'我是一个博主，帮我写一篇关于[主题]的[平台：如公众号、知乎、头条等]文章，需要符合该平台写作风格。',icon: 'https://p9-flow-imagex-sign.byteimg.com/ocean-cloud-tos/pdf/7d8ccaf99a6fca4a9b2f8d9300757f02_0_960.jpg~tplv-a9rns2rl98-image-qvalue.jpeg?rk3s=1567c5c4&x-expires=1771378767&x-signature=O7hcHZDN8i7g1tVhCG5dNRjU3vY%3D', actived: false},
          {title: '中等收入陷阱.pdf', desc: 'PDF 3MB', content:'帮我写 5 个面向[人群]宣传[产品]的品牌营销slogan，简洁吸睛，富有创意。',icon: 'https://p3-flow-imagex-sign.byteimg.com/ocean-cloud-tos/pdf/b9248a3d7a72a629563ecb66cd084405_0_960.jpg~tplv-a9rns2rl98-image-qvalue.jpeg?rk3s=1567c5c4&x-expires=1771378768&x-signature=sTZYCXyTm4NTna%2BFk57RWJzG%2F08%3D', actived: false},
          {title: '中华人民共和国民法典.pdf', desc: 'PDF 3MB', content:'帮我写 5 个面向[人群]宣传[产品]的品牌营销slogan，简洁吸睛，富有创意。',icon: 'https://p3-flow-imagex-sign.byteimg.com/ocean-cloud-tos/pdf/8ddf03557b0d6ea1e7cc5889ffb1a5d0_0_960.jpg~tplv-a9rns2rl98-image-qvalue.jpeg?rk3s=1567c5c4&x-expires=1771378767&x-signature=bTwiPy8uF43G02STG%2Fatnb%2Br6so%3D', actived: false},
          {title: '2024全国高考理科数学真题.pdf', desc: 'PDF 3MB', content:'帮我写 5 个面向[人群]宣传[产品]的品牌营销slogan，简洁吸睛，富有创意。',icon: 'https://p9-flow-imagex-sign.byteimg.com/ocean-cloud-tos/pdf/f42870d0cac2540f4ddf1658b93915c4_0_960.jpg~tplv-a9rns2rl98-image-qvalue.jpeg?rk3s=1567c5c4&x-expires=1771378767&x-signature=Iok5DBU3aPRttCwR8lf96ieEPBE%3D', actived: false},
        ]
      }
    },
    created() {
      const userInfo = JSON.parse(localStorage.getItem('userInfo'))
      this.userName = userInfo.realname
    },
    methods: {
      onCardClick(item) {
        console.log('item', item)
        // this.$emit('chatNow', {msg: item.title})
        this.$emit('readChat', {msg: item.title})
      },
      onSendMessage() {
        this.$emit('chatNow', {msg: this.searchKey})
      },
      getMessage(rectxt) {
        this.searchKey = rectxt
      },
    }
  }
</script>

<style scoped>
  .container {
    /* padding-bottom: 80px; */
    width: 810px;
  }
  .text-content {
    margin-top: 50px;
  }
  .search-input {
    text-align: center;
    /* background: #fff; */
    position: fixed;
    bottom: 0;
    width: 810px;
    padding-bottom: 20px;
  }

  .menu-item {
    display: flex;
    align-items: center;
    justify-content: flex-start;
    font-weight: 700;
  }

  .menu-icon {
    width: 24px;
    height: 24px;
    line-height: 24px;
    text-align: center;
  }

  .intro {
    font-size: 24px;
    margin-top: 12px;
    margin-bottom: 24px;
    color: #000;
    text-align: left;
  }

  .input-content {
    display: flex;
    border: 1px solid rgba(0, 0, 0, 0.1);
    box-shadow: 0 6px 10px 0 rgba(42, 60, 79, 0.1);
    border-radius: 20px;
    display: flex;
    gap: 8px;
    justify-content: space-between;
    padding: 12px 14px 12px 2px;
    /* flex-direction: column; */
    align-items: center;
    flex: 1;
    background: #fff;
  }

  .input-style {
    flex: 1;
  }

  .input-style >>> input {
    border: none;
    outline: none;
    padding: 14px;
    margin: 0;
    background-image: none;
    background-color: transparent;
    width: 100%;
    resize: none;
    font-size: 16px;
  }

  .input-style >>> input:focus {
    outline: none;
  }

  .input-style >>> .textarea_ai {
    display: none;
  }

  .select-style {
    /* width: 100px; */
    display: flex;
  }

  .btn-item {
    display: flex;
    align-items: center;
  }

  .btn-item .btn-title {
    margin: 0 4px;
  }

  .btn-item >>> .el-switch__core {
    width: 30px !important;
    height: 16px !important;
  }

  .btn-item >>> .el-switch__core::after {
    width: 12px;
    height: 12px;
    /* margin-left: -13px !important;
    left: 15px !important; */
  }

  .btn-group {
    display: flex;
    justify-content: space-between;
    margin-left: 14px;
    /* margin-top: 0px; */
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

  .relative-list {
    margin-top: 30px;
    display: flex;
    flex-wrap: wrap;
  }

  .relative-list .list-item {
    margin-right: 10px;
    font-size: 14px;
    border-radius: 8px;
    padding: 7px 16px;
    cursor: pointer;
    border: 1px solid rgba(0,0,0,.08);
  }
  
  .relative-list .list-item:hover {
    background: rgba(0,0,0,.06);
  }

  .relative-list .list-item-actived:hover {
    background: #232629;
  }

  .relative-list .list-item-actived {
    background: #232629;
    color: #fff;
  }

  .relative-list .icon {
    font-size: 14px;
    font-weight: 700;
    margin-right: 4px;
  }
  .relative-list .btn-title {
    font-size: 14px;
  }

  .relative-cards {
    margin-top: 20px;
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 10px;
    margin-bottom: 20px;
    /* padding-bottom: 180px; */
  }

  .card-item {
    border: 1px solid rgba(0,0,0,.08);
    width: 252px;
    height: 171px;
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
    height: 120px;
    background: #f3f4f6;
    display: flex;
    justify-content: center;
    padding-top: 10px;
    padding-left: 20px;
    padding-right: 20px;
    overflow: hidden;
  }

  .card-img img {
    width: 150px;
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
  .card-title-1 {
    font-size: 14px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    font-weight: 700;
    width: 190px;
  }
  .card-icon {
    width: 30px;
    height: 30px;
  }
  .card-desc {
    font-size: 11px;
    color: rgba(0, 0, 0, .5);
    margin-top: 6px;
  }

  .btn-item {
    margin-right: 10px;
    font-size: 14px;
    border-radius: 8px;
    padding: 10px 12px;
    cursor: pointer;
    border: 1px solid rgba(0,0,0,.08);
  }

  .btn-item .btn-icon {
    font-size: 14px;
    font-weight: 700;
  }

  .btn-item:hover {
    background: rgba(0,0,0,.06);
  }
</style>
