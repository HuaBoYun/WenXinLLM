<template>
  <div class="container">
    <div v-if="!dialogVisible" class="agent-list" v-loading="loading">
      <div class="scorll-content">
        <div class="text-content">
          <div class="page-title">
            <div class="menu-item">
              <i :class="`menu-icon el-icon-edit`"></i>
              <div class="menu-title">帮我写作</div>
            </div>
          </div>
          <div class="intro">多种体裁，润色校对，一键成文</div>
        </div>
        <div class="relative-list">
          <div class="list-item" :class="{ 'list-item-actived no-hover': item.label === relativeKey }" v-for="item in relativeList" :key="item.label" @click="onTypeClick(item)">
            {{ item.label }}
          </div>
        </div>
        <div class="relative-cards">
          <div class="card-item" :class="{ 'card-item-actived': item.id === curCard.id }" v-for="item in allFillterList" :key="item.id" @click="onCardClick(item)">
            <div class="card-icon"><img :src="iconList[Number(item.agentPicture)] || defaultAvatar" alt="" ></div>
            <div class="card-title">{{ item.agentName }}</div>
            <div class="card-desc">{{ item.describe }}</div>
          </div>
        </div>
      </div>
      
      <div class="search-input">
        <div class="input-content">
          <el-input
            class="input-style"
            v-model="searchKey"
            clearable
            type="textarea"
            :rows="1"
            autosize
            placeholder="输入你要撰写的主题"
            @keydown.enter.native="onAgent"
          />
          <div class="btn-group">
            <div class="switch-group">
              <div @click="think = !think" class="deep" :style="`${think?'color:#4D6BFE;background:#DBEAFE;border-color:rgba(0, 122, 255, 0.15);':''}`">
                深度思考
                <i v-if="!think" class="btn-icon el-icon-turn-off"></i>
                <i v-else class="btn-icon el-icon-open"></i>
              </div>
              <div @click="search = !search" class="deep" :style="`${search?'color:#4D6BFE;background:#DBEAFE;border-color:rgba(0, 122, 255, 0.15);':''}`">
                搜索资料
                <i v-if="!search" class="btn-icon el-icon-turn-off"></i>
                <i v-else class="btn-icon el-icon-open"></i>
              </div>
              <div @click="editor = !editor" class="deep" :style="`${editor?'color:#4D6BFE;background:#DBEAFE;border-color:rgba(0, 122, 255, 0.15);':''}`">
                文档编辑器
                <i v-if="!editor" class="btn-icon el-icon-turn-off"></i>
                <i v-else class="btn-icon el-icon-open"></i>
              </div>
            </div>
  
            <div class="icons">
              <voice class="icon" @getMessage="getMessage" />
              <i class="icon el-icon-top send" @click="onAgent"></i>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-else class="agent-chat">
      <div style="margin: 0 auto;position: relative;" :style="`width: 52%;`" id="iframeRef">

        <i class="el-cion el-icon-back" style="cursor: pointer;font-size: 25px;position: absolute;left: 15px;top: 15px;" @click="dialogVisible = false"></i>
        <iframe
          :src="iframeUrl"
          style="width: 100%; height: 100%; min-height: 700px"
          frameborder="0"
          allow="microphone">
        </iframe>
      </div>
      <div class="right-content" v-if="review">
        <div class="top-nav">
          <i class="icon el-icon-close" @click="review = false"></i>
          <div class="title">beijing_school_district_report.html</div>
          <el-button type="info" size="mini">预览</el-button>
          <el-button class="btn" type="default" size="mini">代码</el-button>
          <img class="icon" :src="fullscreenSvg" alt="">
          <i class="icon el-icon-download"></i>
        </div>
        <div class="file-content">
          Lorem ipsum dolor sit amet consectetur, adipisicing elit. Dolorem quo maiores aliquam incidunt temporibus, non nostrum deserunt expedita. Error, nihil eum exercitationem libero eveniet in provident officiis repellendus eaque fugiat!
          Pariatur repellat facilis fugiat, incidunt dolore illum nisi. Deleniti eveniet ex corrupti qui fugiat, libero ea repellat, culpa expedita laborum sint et facere laboriosam vel iste non sit dolorem cum!
          Delectus, quia! Eaque consectetur vitae, reiciendis neque officia a magnam qui vero eius ut fugit voluptatem cum veniam nobis natus necessitatibus adipisci deserunt, blanditiis esse? Consequatur voluptatem tenetur nisi commodi.
          Commodi ipsum a quos! Impedit non at sint iusto voluptas labore, facere pariatur qui quas dolor magni inventore facilis aut ex aspernatur eligendi omnis doloribus dolores numquam quidem excepturi ut.
          Neque dignissimos minus, itaque deleniti voluptatibus voluptate maxime repellendus tempore a non, aperiam, eveniet omnis qui numquam optio dolores architecto? Est aspernatur delectus ullam excepturi ea repellendus aut eligendi neque?
          Pariatur dignissimos quisquam minima molestias commodi quos vitae facilis libero, repellat ut corrupti quae debitis odio architecto? Minus labore ipsam aliquam deserunt suscipit obcaecati? Excepturi optio delectus consequatur voluptatibus recusandae?
          Incidunt quo pariatur reprehenderit sit illum, labore optio enim minima architecto, quis reiciendis eum? Autem velit molestiae ullam, nam perspiciatis ex adipisci, at hic, modi magni quod neque nemo reiciendis.
          Voluptates dolor, doloribus perferendis magni iste aspernatur. Tempora ducimus esse fuga ipsam recusandae quos? Id nesciunt, minima eveniet deleniti inventore ut possimus repellat at eaque culpa officia consectetur temporibus consequuntur?
          Suscipit sunt quidem fuga est veritatis officiis delectus nemo non cum minus quis esse quia accusantium dolorum quos ipsam vero nam corporis quibusdam, vitae facere maxime. Alias mollitia eligendi illo.
          Dicta minima modi excepturi asperiores quaerat voluptatibus, iusto odio animi eligendi, iste quis quidem repudiandae eaque ab, architecto voluptatem? Provident nulla soluta quasi officia est distinctio tenetur ab cupiditate commodi.
          Lorem ipsum dolor sit amet consectetur adipisicing elit. Facilis recusandae cupiditate nihil accusantium vitae voluptatem perspiciatis, eveniet vel architecto libero nesciunt modi cum, deleniti ad ducimus sed laborum amet. Quo?
          Lorem, ipsum dolor sit amet consectetur adipisicing elit. Modi provident unde corporis inventore pariatur, distinctio ut vel rerum, necessitatibus maiores nam ad ducimus error amet eveniet nesciunt vitae repellendus est.
          Molestias corporis cumque nesciunt ab at ad facere officiis quaerat suscipit aut, culpa repellendus quas nihil atque saepe velit asperiores aliquid praesentium nobis corrupti rerum in perspiciatis consectetur. Necessitatibus, incidunt!
          Nobis voluptates maiores dolorum dolor nihil quae consectetur? Voluptas illum ex optio expedita beatae? Suscipit sed maiores eos officia optio. Beatae ipsam animi aspernatur illum ad in laudantium aliquam doloribus.
          Debitis, explicabo magnam officiis quasi esse harum architecto sunt suscipit qui officia cum voluptate necessitatibus cumque nihil illum aspernatur perferendis quae ipsam quaerat cupiditate saepe! Laboriosam nisi accusantium dolores laborum.
          Cum nam deleniti assumenda. Vel expedita esse corporis cum dignissimos, quia veritatis? Nisi perferendis officia ullam, quo dolore quaerat alias praesentium temporibus omnis aliquid a voluptatum repellat, illo enim consequatur?
          Saepe, nulla dolore delectus eligendi, numquam ut quaerat aliquid nesciunt, consequatur accusamus voluptatum. Quo rem aliquam vitae dolorum voluptate autem, velit repellendus sapiente repudiandae quas quibusdam et ullam, dicta labore.
          Minus nam consequatur nihil ducimus facilis incidunt asperiores, ut rem. Blanditiis adipisci neque aliquam quos quidem officia hic nam reiciendis labore voluptatum. Reiciendis neque quibusdam harum recusandae commodi quasi facilis.
          Natus ipsa facilis, molestiae soluta dolores, ex cupiditate ullam doloribus sapiente, officiis aliquid quisquam recusandae quis libero. Placeat velit odit voluptatibus eum blanditiis similique ex aliquid quisquam! Voluptas, doloremque ut.
          Praesentium beatae quas ipsa rem ullam possimus quasi officia ratione quis quidem voluptate tempore sunt provident adipisci, sit hic inventore id perferendis repellat ab? Dolorem ipsum cum laborum deserunt recusandae.
          Atque magnam a nisi omnis corrupti. Expedita cum odit accusamus maiores, unde quam in quasi possimus impedit illum repudiandae at voluptatum soluta ratione quas sapiente! Accusantium labore minus omnis quo.
          Fuga dolores minus consequatur voluptatibus deleniti. Unde numquam nemo eveniet sunt rerum fugit, similique iure architecto odio deleniti quam et inventore vitae, tenetur, corporis fugiat aspernatur distinctio quisquam ad. Voluptatibus!
          Ducimus libero labore voluptate? Temporibus dicta officiis magnam dolores a veniam obcaecati expedita quidem ducimus ad, numquam nobis cum illum quaerat debitis! Qui dicta quibusdam, nobis atque amet dolorem facere.
          Vero quam explicabo dolor eos delectus. Perspiciatis, nobis culpa numquam minima voluptatum temporibus cupiditate iure, dignissimos dolor doloremque ipsa tempore nam voluptas ratione cum, asperiores eos vero ea dolorem molestiae?
          Beatae sequi eius nemo accusantium similique suscipit commodi, id sapiente ab. Voluptas assumenda nihil, ipsa vero animi deleniti quam, ducimus consequuntur rerum perspiciatis magni, dolores tenetur numquam impedit veniam mollitia?
          Laborum veritatis dolor odit, dolores beatae quidem provident fugiat explicabo repellendus at voluptas facilis ipsa eligendi velit quam doloribus aliquam qui nobis molestiae, voluptatem architecto inventore illum ad. Optio, autem!
          Illum quisquam tempore eligendi blanditiis id dolor totam? Amet voluptate assumenda vero beatae officia tempora voluptas? Ut aperiam, ducimus modi tempore neque optio officia, rem eaque porro quod vitae similique?
          Quos dignissimos, quaerat velit, sint mollitia quod maxime dolore commodi veniam voluptates distinctio excepturi magnam et? Quos corrupti dicta, nisi totam obcaecati, quae aperiam quaerat quam necessitatibus pariatur dignissimos corporis!
          Iusto accusantium pariatur dolores animi delectus qui nulla corporis repudiandae. Asperiores eum est saepe illum delectus possimus omnis, obcaecati quia distinctio, facilis nostrum similique doloribus. Cumque, eos. Delectus, perferendis odit.
          Molestias ea, possimus veniam eius iste vitae, placeat officia ex voluptatem minus cum nisi quo laudantium nobis. Molestias, delectus quidem expedita praesentium accusantium, quod dolor dolores, nemo temporibus laboriosam atque.
          Porro dolorem eveniet perferendis, voluptates incidunt nostrum id nihil sunt perspiciatis explicabo, enim libero beatae consectetur voluptate ut eum ullam facere eaque, quo cumque illo quam? Ut molestiae ad reprehenderit?
        </div>
        <div class="footer">
          <i class="el-icon-back"></i>
          <div class="page">1</div>
          <i class="el-icon-right"></i>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import voice from '@/views/index/components/AI/voice.vue'
  import mapData from '@/views/index/components/AI/mock/dataMap.js'
  import fullscreenSvg from '@/views/index/components/AI/icon/fullscreen.svg'
  import { moduleAgentList } from '@/api/ai/index.js'
  import defaultAvatar from '@/views/index/components/AI/icon/default-avatar.png'
  import a0 from '@/views/index/components/AI/icon/a0.png'
  import a1 from '@/views/index/components/AI/icon/a1.png'
  import a2 from '@/views/index/components/AI/icon/a2.png'
  import a3 from '@/views/index/components/AI/icon/a3.png'
  import a4 from '@/views/index/components/AI/icon/a4.png'
  import a5 from '@/views/index/components/AI/icon/a5.png'
  import a6 from '@/views/index/components/AI/icon/a6.png'
  import a7 from '@/views/index/components/AI/icon/a7.png'
  import a8 from '@/views/index/components/AI/icon/a8.png'
  import a9 from '@/views/index/components/AI/icon/a9.png'
  import a10 from '@/views/index/components/AI/icon/a10.png'
  import a11 from '@/views/index/components/AI/icon/a11.png'
  import a12 from '@/views/index/components/AI/icon/a12.png'
  import a13 from '@/views/index/components/AI/icon/a13.png'
  import a14 from '@/views/index/components/AI/icon/a14.png'
  import a15 from '@/views/index/components/AI/icon/a15.png'
  import a16 from '@/views/index/components/AI/icon/a16.png'
  import a17 from '@/views/index/components/AI/icon/a17.png'
  import a18 from '@/views/index/components/AI/icon/a18.png'
  import a19 from '@/views/index/components/AI/icon/a19.png'
  import a20 from '@/views/index/components/AI/icon/a20.png'
  import a21 from '@/views/index/components/AI/icon/a21.png'
  import a22 from '@/views/index/components/AI/icon/a22.png'
  import a23 from '@/views/index/components/AI/icon/a23.png'
  import a24 from '@/views/index/components/AI/icon/a24.png'
  import a25 from '@/views/index/components/AI/icon/a25.png'
  import a26 from '@/views/index/components/AI/icon/a26.png'
  import a27 from '@/views/index/components/AI/icon/a27.png'
  import a28 from '@/views/index/components/AI/icon/a28.png'
  import a29 from '@/views/index/components/AI/icon/a29.png'
  import a30 from '@/views/index/components/AI/icon/a30.png'
  import a31 from '@/views/index/components/AI/icon/a31.png'
  import a32 from '@/views/index/components/AI/icon/a32.png'
  import a33 from '@/views/index/components/AI/icon/a33.png'
  import a34 from '@/views/index/components/AI/icon/a34.png'
  import a35 from '@/views/index/components/AI/icon/a35.png'
  import a36 from '@/views/index/components/AI/icon/a36.png'
  import a37 from '@/views/index/components/AI/icon/a37.png'
  import a38 from '@/views/index/components/AI/icon/a38.png'

  export default {
    props: {
      menuInfo: {
        type: Object,
        default: () => ({})
      }
    },
    components: { voice },
    data() {
      return {
        defaultAvatar,
        loading: false,
        searchKey: '',
        source: '参考资料',
        userName: '',
        relativeKey: '全部',
        value1: '',
        value2: '',
        think: false,
        search: false,
        editor: false,
        relativeList: mapData.modelAgentCategory,
        allList: [],
        dialogVisible: false,
        curCard: {},
        review: true,
        iframeUrl: '',
        fullscreenSvg,
        iconList: [a0,a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,a23,a24,a25,a26,a27,a28,a29,a30,a31,a32,a33,a34,a35,a36,a37,a38],
      }
    },
    computed: {
      allFillterList() {
        return this.allList.filter(x => this.relativeKey === '全部' || x.agentType === this.relativeKey)
      }
    },
    created() {
      const userInfo = JSON.parse(localStorage.getItem('userInfo'))
      this.userName = userInfo.realname
      console.log('relativeList', this.relativeList)
      this.fetchData()
    },
    methods: {
      async fetchData() {
        this.loading = true
        const model = localStorage.getItem('model') || ''
        moduleAgentList({pageSize: 99999, moduleRoute: model}).then(res => {
          console.log('res', res)
          if (res && res.data && res.data.tlist.length) {
            this.allList = res.data.tlist
          }
        }).finally(() => {
          this.loading = false
        })
      },
      onTypeClick(item) {
        this.relativeKey = item.label
      },
      onCardClick(item) {
        console.log('item', item)
        this.curCard = item
        this.searchKey = item.describe
        this.iframeUrl = item.jumpAddress
      },
      onAgent() {
        if (!this.curCard || !this.curCard.id) return this.$message.error('请选择智能体！')
        if (!this.iframeUrl) return this.$message.error('智能体对象创建失败！')
        this.dialogVisible = true
      },
      getMessage(rectxt) {
        this.searchKey = rectxt
      },
    }
  }
</script>

<style scoped>
  .container {
    height: 79vh;
    overflow: hidden;
  }
  .agent-list {
    width: 810px;
    margin: 0 auto;
    height: 100%;
    padding-bottom: 150px;
  }
  .agent-chat {
    display: flex;
    height: 100%;
    overflow: hidden;
    /* padding: 50px 0; */
  }
  .text-content {
    margin-top: 50px;
  }
  .search-input {
    width: 810px;
    text-align: center;
    position: absolute;
    bottom: 0;
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

  .scorll-content {
    overflow-y: auto;
    height: 100%;
    /* 隐藏 Firefox 浏览器的滚动条 */
    scrollbar-width: none;
    /* 隐藏 IE 和旧版 Edge 浏览器的滚动条 */
    -ms-overflow-style: none;
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
    font-size: 16px;
  }

  .input-style >>> textarea:focus {
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

  .switch-group {
    display: flex;
    align-items: center;
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
    margin-right: 4px;
    margin-bottom: 4px;
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
    grid-template-columns: repeat(4, 1fr);
    gap: 10px;
    margin-bottom: 20px;
    /* padding-bottom: 180px; */
  }

  .card-item {
    border: 1px solid rgba(0,0,0,.08);
    width: 190px;
    height: 130px;
    padding-top: 16px;
    padding-left: 16px;
    padding-right: 16px;
    cursor: pointer;
    border-radius: 16px;
  }

  .card-item:hover {
    box-shadow: 0 6px 10px 0 rgba(42, 60, 79, .1);
  }
  .card-item-actived {
    border-color: #0057ff;
  }

  .card-icon {
    width: 24px;
    height: 24px;
  }

  .card-icon img {
    width: 100%;
    height: 100%;
  }
  .card-title {
    font-size: 16px;
    margin-top: 12px;
    margin-bottom: 4px;
    font-weight: 700;
  }
  .card-desc {
    overflow: hidden;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 2;
    align-items: flex-end;
    display: flex;
    height: 36px;
    line-height: 18px;
    margin-bottom: 16px;
    font-size: 13px;
    text-overflow: ellipsis;
    color: rgba(0,0,0,.3);
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

  .right-content {
    flex: 1;
    /* border: 1px solid #000; */
    border-radius: 16px;
    background: #fff;
    box-shadow: rgba(0, 8, 24, 0.12) 1px 3px 28.8px;
    padding: 16px;
    overflow: hidden;
  }
  
  .right-content .top-nav {
    display: flex;
    align-items: center;
    width: 100%;
    height: 50px;
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);
    margin-bottom: 10px;
  }

  .right-content .title {
    flex: 1;
  }

  .right-content .icon {
    width: 24px;
    height: 24px;
    font-size: 24px;
    cursor: pointer;
    margin-right: 6px;
  }

  .right-content .btn {
    margin-right: 12px;
  }

  .right-content .file-content {
    max-width: 800px;
    margin: 0px auto;
    padding: 24px 40px 50px 40px;
    background-color: #fff;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    border-radius: 8px;
    height: 88%;
    overflow: auto;
  }

  .right-content .footer {
    border-top: 1px solid rgba(0, 0, 0, 0.1);
    margin-top: 10px;
    font-size: 20px;
    display: flex;
    align-items: center;
  }

  .right-content .page {
    margin: 0 16px;
  }
</style>
