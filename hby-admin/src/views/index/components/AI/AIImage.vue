<template>
  <div class="container">
    <div class="text-content">
      <div class="page-title">
        <div class="menu-item">
          <i :class="`menu-icon el-icon-picture-outline`"></i>
          <div class="menu-title">图像生成</div>
        </div>
      </div>
      <div class="intro">自定风格，搜集灵感，复制同款</div>
    </div>

    <div class="relative-cards">
      <div
        class="card-item"
        :class="{ 'card-item-actived': item.actived }"
        v-for="item in allList"
        :key="item.title"
        @click="onCardClick(item)"
      >
        <div class="card-item-content">
          <img class="card-icon" :src="item.icon" alt="" />
          <div class="card-title">{{ item.title }}</div>
        </div>
      </div>
    </div>

    <div class="relative-list">
      <div
        class="list-item"
        :class="{ 'list-item-actived no-hover': item.key === relativeKey }"
        v-for="item in relativeList"
        :key="item.title"
        @click="onTypeClick(item)"
      >
        {{ item.title }}
      </div>
    </div>

    <div class="images" style="height: inherit">
      <vue-waterfall-easy
        :imgsArr="imageList"
        @scrollReachBottom="getData"
        :gap="10"
        :width="1000"
      ></vue-waterfall-easy>
    </div>

    <div class="search-input">
      <div class="input-content">
        <el-input
          class="input-style"
          v-model="searchKey"
          clearable
          placeholder="描述你所想象的画面，角色，情绪，场景，风格..."
          @keydown.enter.native="onSendMessage"
        />
        <div class="btn-group">
          <div class="select-style">
            <div class="btn-item">
              <div class="btn-icon"><i class="el-icon-bangzhu"></i></div>
              <div class="btn-title">参考图</div>
            </div>
            <div class="btn-item">
              <div class="btn-icon"><i class="el-icon-edit-outline"></i></div>
              <div class="btn-title">比例</div>
            </div>
            <div class="btn-item">
              <div class="btn-icon"><i class="el-icon-edit-outline"></i></div>
              <div class="btn-title">风格</div>
            </div>
          </div>

          <div class="icons">
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
  import img1svg from './icon/img1.svg'
  import img2svg from './icon/img2.svg'
  import img3svg from './icon/img3.svg'
  import img4svg from './icon/img4.svg'
  // import Waterfall from 'vue-waterfall/lib/waterfall'
  // import WaterfallSlot from 'vue-waterfall/lib/waterfall-slot'
  import VueWaterfallEasy from 'vue-waterfall-easy'
  import voice from './voice.vue'
  export default {
    components: {
      // Waterfall,
      // WaterfallSlot,
      VueWaterfallEasy,
      voice,
    },
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
          { title: '精选', key: 'all', actived: true },
          { title: '人像摄影', key: 'work', actived: false },
          { title: '艺术', key: 'business', actived: false },
          { title: '国风插画', key: 'study', actived: false },
          { title: '动漫', key: 'article', actived: false },
          { title: '3D渲染', key: 'art', actived: false },
          { title: '商品', key: 'reply', actived: false },
          { title: '风景', key: 'reply', actived: false },
        ],
        allList: [
          { title: 'AI 抠图', desc: 'PDF 3MB', icon: img1svg, actived: false },
          { title: '擦除', desc: 'PDF 3MB', icon: img2svg, actived: false },
          { title: '区域重绘', desc: 'PDF 3MB', icon: img3svg, actived: false },
          { title: '阔图', desc: 'PDF 3MB', icon: img4svg, actived: false },
        ],
        imageList: [],
      }
    },
    created() {
      const userInfo = JSON.parse(localStorage.getItem('userInfo'))
      this.userName = userInfo.realname
      this.getData()
    },
    methods: {
      getData() {
        this.imageList = [
          {
            desc: '图片风格为人像摄影风格，埃里克·侯麦作品，浅紫头发的女生，微微侧脸，穿着简约的服装，街头背景，超高清，直射光，半身照',
            src: 'https://p3-flow-imagex-sign.byteimg.com/ocean-cloud-tos/image_generation/a4190f7caaee548bf450ddd8e61c921c_1724313028825542114.jpeg~tplv-a9rns2rl98-image.jpeg?rk3s=25bff839&x-expires=1742568889&x-signature=%2FT%2FyDcyXDBgOHW2R60rEtY9BhM0%3D',
          },
          {
            desc: '图片风格为人像摄影，，现代女性，坚定的眼神，比例2:3',
            src: 'https://p3-flow-imagex-sign.byteimg.com/ocean-cloud-tos/image_generation/dd5730f7aa44c4441a5efae9c423845e_1724383002663411224.jpeg~tplv-a9rns2rl98-image.jpeg?rk3s=25bff839&x-expires=1742568889&x-signature=m%2FpfQscO5JiFfdDtzITST9m7%2BMc%3D',
          },
          {
            desc: '，一只穿着传统服装的狐狸正在睡觉，树木，可爱',
            src: 'https://p3-flow-imagex-sign.byteimg.com/ocean-cloud-tos/image_generation/ffbf53b4b5fac450a9b03812ff19468a_1724314964782619748.jpeg~tplv-a9rns2rl98-image.jpeg?rk3s=25bff839&x-expires=1742568889&x-signature=XCNe%2FHSlLUxB%2B10gJP2h0Pwjb8M%3D',
          },
          {
            desc: '图片风格为人像摄影，一个帅气的男孩微微仰头，半边脸的特写，动态模糊，日系男孩，胶片感',
            src: 'https://p3-flow-imagex-sign.byteimg.com/ocean-cloud-tos/image_generation/a2c97902da0b913e7cf4db7bc6043a09_1724312167430747428.jpeg~tplv-a9rns2rl98-image.jpeg?rk3s=25bff839&x-expires=1742568889&x-signature=7bf3WxurOJaVgFraflk39biK%2Fy0%3D',
          },
          {
            desc: '一座山，背景中有一颗外星行星，天空黑暗，夜景，超写实风格，比例9:16',
            src: 'https://p3-flow-imagex-sign.byteimg.com/ocean-cloud-tos/image_generation/86b4cbca28666a5ba191066bba2d3170_1724381925256849360.jpeg~tplv-a9rns2rl98-image.jpeg?rk3s=25bff839&x-expires=1742568889&x-signature=Vpf3dMJ9XdUrmMHxpskB0n2h9rQ%3D',
          },
          {
            desc: '图片风格为人像摄影，街头摄影，一个穿着粉色衣服的年轻女性，走在香港闹市，侧脸，厚嘴唇，长发，脸上流着汗，阳光，比例4:3',
            src: 'https://p3-flow-imagex-sign.byteimg.com/ocean-cloud-tos/image_generation/e048edfef4f8d8e3b82c90e7cf68cb69_1724313269789725793.jpeg~tplv-a9rns2rl98-image.jpeg?rk3s=25bff839&x-expires=1742568889&x-signature=49bzvxVICEyXx3g0YDw%2F0bxHd5g%3D',
          },
          {
            desc: '图片风格为雕塑，猫，侧脸，室内照明，比例2:3',
            src: 'https://p3-flow-imagex-sign.byteimg.com/ocean-cloud-tos/image_generation/8048f76ae535366fd1cadadf9de54549_1724310340489828017.jpeg~tplv-a9rns2rl98-image.jpeg?rk3s=25bff839&x-expires=1742568889&x-signature=wPx8NLcq51ALgsUOws%2BkDfJfxZo%3D',
          },
          {
            desc: '在蒙古金色的草原上，一头牛在可爱和露营风格的氛围中安静地吃草。天蓝色和绿色，浅红色和绿色的色调，淡雅的绘画风格，深橙色和白色相互交织，营造出宁静的氛围，比例4:3',
            src: 'https://p3-flow-imagex-sign.byteimg.com/ocean-cloud-tos/image_generation/24d202a325f4379d245ed54143180c78_1724324373545208188.jpeg~tplv-a9rns2rl98-image.jpeg?rk3s=25bff839&x-expires=1742568889&x-signature=NxRSwUBDqFGfIA82iEE19nU2mYg%3D',
          },
          {
            desc: '图片风格为人像摄影，一群人在海边玩耍，群拍镜头，日光，比例2:3',
            src: 'https://p3-flow-imagex-sign.byteimg.com/ocean-cloud-tos/image_generation/d2176cbe84c9e5e7478d341634fd63d2_1724312300718546764.jpeg~tplv-a9rns2rl98-image.jpeg?rk3s=25bff839&x-expires=1742568889&x-signature=X%2BV8veNRVw870oYBGc2Sym59its%3D',
          },
          {
            desc: '图片风格为3D，一幅宁静的山水画，高山流水，云雾笼罩，瀑布直下，湖面平静，一条平坦的小船，两个人静静地坐着，深蓝色和浅蓝色的山，宁静而壮丽，比例2:3',
            src: 'https://p3-flow-imagex-sign.byteimg.com/ocean-cloud-tos/image_generation/9ab05fe00ce8076029460dcfa8be4387_1724308682619831152.jpeg~tplv-a9rns2rl98-image.jpeg?rk3s=25bff839&x-expires=1742568889&x-signature=vuQw1Yv5OlkRHuat1J2ZIoDMEMc%3D',
          },
          {
            desc: '图片风格为动漫，工装女生，疯狂的细节，超高清画质，特写上半身，比例4:3',
            src: 'https://p3-flow-imagex-sign.byteimg.com/ocean-cloud-tos/image_generation/a70329f4ad468dcf8eb74ec11664bdfb_1724321971423523302.jpeg~tplv-a9rns2rl98-image.jpeg?rk3s=25bff839&x-expires=1742568889&x-signature=AD9Wb41ZbdC4W4i8z2XbPOQXa0w%3D',
          },
          {
            desc: '图片风格为人像摄影，一位18岁的阳光少年，在浅灰色的背景下，拥有一张帅气的脸庞，短发、炯炯有神的眼睛，鼻子高挺而精致，短袖，韩系帅哥氛围感',
            src: 'https://p3-flow-imagex-sign.byteimg.com/ocean-cloud-tos/image_generation/351bf299473fd80fea154011f23b2cdf_1724312384454766868.jpeg~tplv-a9rns2rl98-image.jpeg?rk3s=25bff839&x-expires=1742568889&x-signature=78K6Oz8o9FzDjb1NIWKgQCkyfx8%3D',
          },
          {
            desc: '图片风格为3D，一个充满房屋和湖泊的天堂岛，等距插画，比例9:16',
            src: 'https://p3-flow-imagex-sign.byteimg.com/ocean-cloud-tos/image_generation/bf18dcc251a73baf9e54995c7e618464_1724315369308205012.jpeg~tplv-a9rns2rl98-image.jpeg?rk3s=25bff839&x-expires=1742568889&x-signature=gsPVLOv5c8QZhgGJMR3jy4sBsT0%3D',
          },
          {
            desc: '图片风格为宝丽来相机拍摄，一位可爱的日本高中女生，身穿校服，留着黑色短发，在高中的入口处微笑着摆姿势，比例4:3',
            src: 'https://p3-flow-imagex-sign.byteimg.com/ocean-cloud-tos/image_generation/6271919114d0f93c6c6b5889e9a2a903_1721200742021519183.jpeg~tplv-a9rns2rl98-image.jpeg?rk3s=25bff839&x-expires=1742568889&x-signature=AnMuDvasGU%2BOt%2BVEBAGDb92xnao%3D',
          },
          {
            desc: '图片风格为动漫，白衬衫的男人和狗望着云层，超广角，从远处拍摄，比例2:3',
            src: 'https://p3-flow-imagex-sign.byteimg.com/ocean-cloud-tos/image_generation/6201865df6be2c023a67dc44d24c69fd_1724322015913142011.jpeg~tplv-a9rns2rl98-image.jpeg?rk3s=25bff839&x-expires=1742568889&x-signature=CuQLTbA41zSeUZrAyTJ4VlSOhXk%3D',
          },
          {
            desc: '图片风格为3D渲染毛毡艺术，一只可爱的猫，蓝色米黄色的衣服，超高清画质',
            src: 'https://p3-flow-imagex-sign.byteimg.com/ocean-cloud-tos/image_generation/1c7ceb935fb4fb202b34dea2c9e71d71_1724380806706142507.jpeg~tplv-a9rns2rl98-image.jpeg?rk3s=25bff839&x-expires=1742568889&x-signature=xqkhonPAZT2yZVqxeJTFCaujHcI%3D',
          },
          {
            desc: '图片风格为油墨印刷，中国风，一个人站在船头，极简，大量留白的构图，比例4:3',
            src: 'https://p3-flow-imagex-sign.byteimg.com/ocean-cloud-tos/image_generation/b7dbbba63b1d4d7c4a17afb250c325c4_1724316609414254422.jpeg~tplv-a9rns2rl98-image.jpeg?rk3s=25bff839&x-expires=1742568889&x-signature=P0Ki%2BuDixOyQK8kT3kSun38ADVA%3D',
          },
          {
            desc: '图片风格为人像摄影，一个亚洲女性，在暖光下，复古，80年代，写实，画面极具故事感，比例2:3',
            src: 'https://p3-flow-imagex-sign.byteimg.com/ocean-cloud-tos/image_generation/a03bdb6f43fe1ac31bac91a022d4f32e_1724311881626975898.jpeg~tplv-a9rns2rl98-image.jpeg?rk3s=25bff839&x-expires=1742568889&x-signature=Rd0BG5WEIHDbMZHkl9fPqvZb05c%3D',
          },
          {
            desc: '图片风格为动漫，戴珍珠耳环的少女，约翰内斯·维米尔，受荷兰黄金时代绘画启发，背景是日本街头',
            src: 'https://p3-flow-imagex-sign.byteimg.com/ocean-cloud-tos/image_generation/73b8d311cb85b5a6623f7bb2fa0cb018_1724309963971044532.jpeg~tplv-a9rns2rl98-image.jpeg?rk3s=25bff839&x-expires=1742568889&x-signature=NNEZUd1SRMq7Qm5iyeDzFXh6l6U%3D',
          },
          {
            desc: '图片风格为油画，一位女性的黑白肖像，微妙地运用了光和影，徕卡M11，特写摄影，比例2:3',
            src: 'https://p3-flow-imagex-sign.byteimg.com/ocean-cloud-tos/image_generation/a9871eff1110f5e95a9efb330d6d0166_1721201056483000717.jpeg~tplv-a9rns2rl98-image.jpeg?rk3s=25bff839&x-expires=1742568889&x-signature=hbqOqylxY755fkCaNcd4gclVV6g%3D',
          },
          {
            desc: '图片风格为人像摄影，一张亚洲女性在家里的照片，轻松快乐地笑，阳光在脸上，奶油和绿色调的衣服，比例2:3',
            src: 'https://p3-flow-imagex-sign.byteimg.com/ocean-cloud-tos/image_generation/ee06b96a6a36c60547efc59c2e8aa42f_1724382874933714362.jpeg~tplv-a9rns2rl98-image.jpeg?rk3s=25bff839&x-expires=1742568889&x-signature=fmXScq8bOEtkf2yUypm3OfSO5b8%3D',
          },
          {
            desc: '图片风格为3D毛毡艺术，柴犬，色彩低调，高度细节化，户外背景，比例2:3',
            src: 'https://p3-flow-imagex-sign.byteimg.com/ocean-cloud-tos/image_generation/db96d3a504ce8d326a424e77d5f48560_1724314239147951982.jpeg~tplv-a9rns2rl98-image.jpeg?rk3s=25bff839&x-expires=1742568889&x-signature=4pyos2gELplSrdSOHSyBBleIFoI%3D',
          },
          {
            desc: '图片风格为水彩画，公园里长椅上的老年人，柔和的色彩，柔软的线条，艺术照明，黄金比例，比例4:3',
            src: 'https://p3-flow-imagex-sign.byteimg.com/ocean-cloud-tos/image_generation/fb52ac6d06437ca948539962a11639e8_1724310430996940457.jpeg~tplv-a9rns2rl98-image.jpeg?rk3s=25bff839&x-expires=1742568889&x-signature=qxBcarfeYMYdPpgnEOII2QBeW7I%3D',
          },
          {
            desc: '图片风格为3D动漫，捕捉东京独特的混合元素，包括标志性的东京塔和富士山的经典剪影，比例4:3',
            src: 'https://p3-flow-imagex-sign.byteimg.com/ocean-cloud-tos/image_generation/c451d82907876df3f447bda4c244011b_1726110306967760365.jpeg~tplv-a9rns2rl98-image.jpeg?rk3s=25bff839&x-expires=1742568889&x-signature=LZFWqYoEDM1BcT%2BSN9TuMdVtT%2FM%3D',
          },
          {
            desc: '图片风格为人像摄影，一位女性的黑白肖像，微妙地运用了光和影，由摄影师罗杰里奥·雷斯使用徕卡M11进行了特写摄影',
            src: 'https://p3-flow-imagex-sign.byteimg.com/ocean-cloud-tos/image_generation/721f8a1b8f237e52a135fc70e0afc6ea_1724311767741560472.jpeg~tplv-a9rns2rl98-image.jpeg?rk3s=25bff839&x-expires=1742568889&x-signature=cQ%2B7jDk4peLq8ezNVIEEWlejVok%3D',
          },
          {
            desc: '图片风格为人像摄影，低头沉思的女性，身穿少数名族服饰，丁达尔效应，人文摄影，比例2:3',
            src: 'https://p3-flow-imagex-sign.byteimg.com/ocean-cloud-tos/image_generation/c81e746ffcc2cd52052e5e6447ddbc63_1724313077603756906.jpeg~tplv-a9rns2rl98-image.jpeg?rk3s=25bff839&x-expires=1742568889&x-signature=Sp6yMEj0DgcnCCBkArxOBekZobc%3D',
          },
          {
            desc: '图片风格为人像摄影，身穿黑色紧身连衣裙的女孩，明暗分明，面板灯，比例2:3',
            src: 'https://p3-flow-imagex-sign.byteimg.com/ocean-cloud-tos/image_generation/ca420c1031636e3fabc6e40053c68724_1724312046581849729.jpeg~tplv-a9rns2rl98-image.jpeg?rk3s=25bff839&x-expires=1742568889&x-signature=3l9X4BDIZChwrG0jTkgMQ1V9C2s%3D',
          },
          {
            desc: '图片风格为钢笔手绘，一只瘦长精致的猫，居家，在沙发上，国潮，大鱼海棠，白色干净的背景',
            src: 'https://p3-flow-imagex-sign.byteimg.com/ocean-cloud-tos/image_generation/9ec5b89972236e33bee3e27ac9a0ed31_1724309504703894584.jpeg~tplv-a9rns2rl98-image.jpeg?rk3s=25bff839&x-expires=1742568889&x-signature=3EXBh1Cbg1xx1x1RetM6TJLxaRo%3D',
          },
          {
            desc: '图片风格为3D，中国风，一个巨大的展开的卷轴在空中飘浮，金银色调，玉髓雕刻，色彩细腻，广角镜头，blender 渲染，丰富细节，超高清，比例2:3',
            src: 'https://p3-flow-imagex-sign.byteimg.com/ocean-cloud-tos/image_generation/b0a8b39f8272d36e2b5e88b78f94a1f7_1724315486946210117.jpeg~tplv-a9rns2rl98-image.jpeg?rk3s=25bff839&x-expires=1742568889&x-signature=eE9AdkpueWhwQ1yFg9FnREvEv3o%3D',
          },
          {
            desc: '图片风格为人像摄影，东方女性，全身，梦幻，珊瑚色流体服饰，毛玻璃，模糊不清，柔和的粉红色',
            src: 'https://p3-flow-imagex-sign.byteimg.com/ocean-cloud-tos/image_generation/ce65c6ea83bd62149d892958e5a0bb97_1724312544819005774.jpeg~tplv-a9rns2rl98-image.jpeg?rk3s=25bff839&x-expires=1742568889&x-signature=nh2yI%2BG9cXc184iJBN9f2QXgAY0%3D',
          },
        ]
      },
      onTypeClick(item) {
        this.relativeKey = item.key
      },
      onCardClick(item) {
        console.log('item', item)
        this.$emit('chatNow', { msg: item.title })
      },
      onSendMessage() {
        this.$emit('chatNow', { msg: this.searchKey })
      },
      getMessage(rectxt) {
        this.searchKey = rectxt
      },
    },
  }
</script>

<style scoped>
  .container {
    /* padding-bottom: 80px; */
    /* width: 70vw; */
    /* padding: 0 100px; */
    text-align: center;
    display: flex;
    flex-direction: column;
    align-items: center;
  }
  .text-content {
    margin-top: 50px;
    width: 810px;
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
    flex-direction: column;
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
    width: 100%;
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
    margin-bottom: 30px;
    width: 810px;
  }

  .relative-list .list-item {
    margin-right: 10px;
    font-size: 14px;
    border-radius: 8px;
    padding: 7px 16px;
    cursor: pointer;
    border: 1px solid rgba(0, 0, 0, 0.08);
  }

  .relative-list .list-item:hover {
    background: rgba(0, 0, 0, 0.06);
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
    gap: 6px;
    width: 810px;
    /* margin-bottom: 20px; */
    /* padding-bottom: 180px; */
  }

  .card-item {
    border-radius: 12px;
    cursor: pointer;
    flex-grow: 1;
    font-size: 14px;
    font-weight: 500;
    margin-right: 8px;
    min-width: 126px;
    padding: 1px;
    position: relative;
    z-index: 0;
  }

  .card-item::before {
    background: linear-gradient(
      90deg,
      rgba(0, 97, 243, 0.2),
      rgba(255, 48, 207, 0.2)
    );
    border-radius: inherit;
    bottom: 0;
    content: '';
    left: 0;
    position: absolute;
    right: 0;
    top: 0;
    z-index: -1;
  }

  .card-item-content {
    align-items: center;
    background: #fff;
    border-radius: 11px;
    display: flex;
    flex-grow: 1;
    height: 62px;
    justify-content: center;
    overflow: hidden;
    position: relative;
    z-index: 0;
  }

  .card-item-content::before {
    background: linear-gradient(
      96deg,
      rgba(0, 97, 243, 0.05),
      rgba(255, 48, 207, 0.05)
    );
    bottom: 0;
    content: '';
    left: 0;
    position: absolute;
    right: 0;
    top: 0;
    visibility: hidden;
    z-index: -1;
  }

  .card-icon {
    margin-right: 4px;
  }

  .card-title {
    background: linear-gradient(90deg, #3c1c95, #640a86);
    background-clip: text;
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
  }

  .card-item:hover {
    box-shadow: 0 6px 10px 0 rgba(42, 60, 79, 0.1);
  }

  .btn-item {
    margin-right: 10px;
    font-size: 14px;
    border-radius: 8px;
    padding: 10px 12px;
    cursor: pointer;
    border: 1px solid rgba(0, 0, 0, 0.08);
  }

  .btn-item .btn-icon {
    font-size: 14px;
    font-weight: 700;
  }

  .btn-item:hover {
    background: rgba(0, 0, 0, 0.06);
  }
</style>
