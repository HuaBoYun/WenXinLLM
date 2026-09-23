<template>
  <div class="container">
    <div class="search-input">
      <div class="page-title">
        <div class="menu-item">
          <i :class="`menu-icon el-icon-search`"></i>
          <div class="menu-title">AI 搜索</div>
        </div>
      </div>
      <div class="intro">实时资讯，丰富信源，整合搜索</div>
      <div class="input-content">
        <el-input
          class="input-style"
          v-model="searchKey"
          clearable
          type="textarea"
          :rows="1"
          autosize
          placeholder="搜索、提问或发消息"
          @keydown.enter.native="onSendMessage"
        />
        <div class="btn-group">
          <el-select class="select-style" v-model="source">
            <el-option value="全网搜索">全网搜索</el-option>
            <el-option value="学术搜索">学术搜索</el-option>
          </el-select>
          <el-button
            round
            icon="el-icon-top"
            size="small"
            @click="onSendMessage"
          ></el-button>
        </div>
      </div>
    </div>
    <div class="relative-list">
      <div class="list-item" v-for="(item, index) in relativeList" :key="index" @click="onChatNow(item)">
        <img :src="item.icon_url" alt="" />
        <div class="article">
          <h3 class="title">{{ item.title }}</h3>
          <div class="intro">{{ item.desc }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import agent1 from '@/views/index/components/AI/img/agent1.png'
  import agent2 from '@/views/index/components/AI/img/agent2.png'
  import agent3 from '@/views/index/components/AI/img/agent3.png'
  import agent4 from '@/views/index/components/AI/img/agent4.png'
  import agent5 from '@/views/index/components/AI/img/agent5.png'
  import agent6 from '@/views/index/components/AI/img/agent6.png'
  import agent7 from '@/views/index/components/AI/img/agent7.png'
  import agent8 from '@/views/index/components/AI/img/agent8.png'
  import agent9 from '@/views/index/components/AI/img/agent9.png'
  import agent10 from '@/views/index/components/AI/img/agent10.png'
  import agent11 from '@/views/index/components/AI/img/agent11.png'
  import agent12 from '@/views/index/components/AI/img/agent12.png'
  import agent13 from '@/views/index/components/AI/img/agent13.png'
  import agent14 from '@/views/index/components/AI/img/agent14.png'
  import agent15 from '@/views/index/components/AI/img/agent15.png'
  import agent16 from '@/views/index/components/AI/img/agent16.png'
  import agent17 from '@/views/index/components/AI/img/agent17.png'
  import agent18 from '@/views/index/components/AI/img/agent18.png'
  import agent19 from '@/views/index/components/AI/img/agent19.png'
  import agent20 from '@/views/index/components/AI/img/agent20.png'
  import agent21 from '@/views/index/components/AI/img/agent21.png'
  import agent22 from '@/views/index/components/AI/img/agent22.png'
  import agent23 from '@/views/index/components/AI/img/agent23.png'
  import agent24 from '@/views/index/components/AI/img/agent24.png'
  import agent25 from '@/views/index/components/AI/img/agent25.png'
  import agent26 from '@/views/index/components/AI/img/agent26.png'
  import agent27 from '@/views/index/components/AI/img/agent27.png'
  import agent28 from '@/views/index/components/AI/img/agent28.png'
  import agent29 from '@/views/index/components/AI/img/agent29.png'
  import agent30 from '@/views/index/components/AI/img/agent30.png'
  export default {
    data() {
      return {
        searchKey: '',
        source: '全网搜索',
        relativeList: [
          {
              "title": "英特尔或被博通、台积电拆分，具体怎么回事？",
              "icon_url": agent1,
              "value": "英特尔或被博通、台积电拆分，具体怎么回事？",
              "desc": "消息称博通对英特尔芯片设计与营销感兴趣，台积电则关注其工厂",
              "create_time": 0
          },
          {
              "title": "混合型模型 Claude 4 或将发布，具体有哪些亮点？",
              "icon_url": agent2,
              "value": "混合型模型 Claude 4 或将发布，具体有哪些亮点？",
              "desc": "该模型融合传统语言模型与推理能力，并且可灵活调节算力",
              "create_time": 0
          },
          {
              "title": "咖啡豆价格暴涨，韩国餐饮业开始做“无豆咖啡”了？",
              "icon_url": agent3,
              "value": "咖啡豆价格暴涨，韩国餐饮业开始做“无豆咖啡”了？",
              "desc": "其成本比普通咖啡低约 20%-25%，市场评价褒贬不一",
              "create_time": 0
          },
          {
              "title": "苹果翻新 Mac mini 定价引争议，具体怎么回事？",
              "icon_url": agent4,
              "value": "苹果翻新 Mac mini 定价引争议，具体怎么回事？",
              "desc": "性能更低的 M2 版本定价 559 美元，比 M4 版本更贵",
              "create_time": 0
          },
          {
              "title": "多家银行下调美元存款利率，原因是什么？",
              "icon_url": agent5,
              "value": "多家银行下调美元存款利率，原因是什么？",
              "desc": "多家银行美元存款告别 4%，有的利率几近“腰斩”",
              "create_time": 0
          },
          {
              "title": "继卖便当之后，库迪又要做便利店了？",
              "icon_url": agent6,
              "value": "继卖便当之后，库迪又要做便利店了？",
              "desc": "库迪咖啡欲靠便利店破局，商品涵盖咖啡、烤肠、便当热食",
              "create_time": 0
          },
          {
              "title": "ChatGPT 剧透一系列大动作，具体都有什么？",
              "icon_url": agent7,
              "value": "ChatGPT 剧透一系列大动作，具体都有什么？",
              "desc": "GPT-4.5 即将亮相，免费的 GPT-5 也或于数月内发布",
              "create_time": 0
          },
          {
              "title": "豆包大模型提出全新稀疏架构，大幅降低推理成本",
              "icon_url": agent8,
              "value": "豆包大模型提出全新稀疏架构，大幅降低推理成本",
              "desc": "新架构名为 UltraMem，推理成本较 MoE 架构最高可降低 83%",
              "create_time": 0
          },
          {
              "title": "饿了么或将入局桶装水配送赛道，具体什么情况？",
              "icon_url": agent9,
              "value": "饿了么或将入局桶装水配送赛道，具体什么情况？",
              "desc": "公司拟推出“筷马送水”，解决大容量饮用水“最后一公里”痛点",
              "create_time": 0
          },
          {
              "title": "京东官宣做外卖，首波打出了哪些“牌”？",
              "icon_url": agent10,
              "value": "京东官宣做外卖，首波打出了哪些“牌”？",
              "desc": "5 月 1 日前入驻商家全年免佣，外卖起送价仅 9.9 元",
              "create_time": 0
          },
          {
              "title": "马克龙拟投千亿欧元，法国版 AI “星际之门”要来了？",
              "icon_url": agent11,
              "value": "马克龙拟投千亿欧元，法国版 AI “星际之门”要来了？",
              "desc": "法国积极部署人工智能，为 Mistral 等本土公司创造发展空间",
              "create_time": 0
          },
          {
              "title": "与上汽合作基本敲定，华为“第五界”可能要来了？",
              "icon_url": agent12,
              "value": "与上汽合作基本敲定，华为“第五界”可能要来了？",
              "desc": "新品牌或命名为“尚界”，面向年轻化市场",
              "create_time": 0
          },
          {
              "title": "有哪些暖心不俗套的元宵节祝福语？",
              "icon_url": agent13,
              "value": "有哪些暖心不俗套的元宵节祝福语？",
              "desc": "元宵佳节乐融融，祝福声声暖人心",
              "create_time": 0
          },
          {
              "title": "21 款智驾车型集中发布，比亚迪开打“技术战”了？",
              "icon_url": agent14,
              "value": "21 款智驾车型集中发布，比亚迪开打“技术战”了？",
              "desc": "“天神之眼”高阶智驾系统正式发布，10 万元以上车型全系标配",
              "create_time": 0
          },
          {
              "title": "遍览产业兴衰的超级碗广告，今年透露了哪些信号？",
              "icon_url": agent15,
              "value": "遍览产业兴衰的超级碗广告，今年透露了哪些信号？",
              "desc": "AI 营销战升级，车企广告已寥寥无几",
              "create_time": 0
          },
          {
              "title": "苹果台灯机器人首次曝光，有哪些亮点？",
              "icon_url": agent16,
              "value": "苹果台灯机器人首次曝光，有哪些亮点？",
              "desc": "不仅造型可爱，交互方式多样，而且有“人味”",
              "create_time": 0
          },
          {
              "title": "OpenAI 突然公开 o3-mini 思维链，首秀却遭质疑？",
              "icon_url": agent17,
              "value": "OpenAI 突然公开 o3-mini 思维链，首秀却遭质疑？",
              "desc": "思维链被指“阉割”，推理过程展示混乱",
              "create_time": 0
          },
          {
              "title": "今年将开店 25 家，海底捞盯上了麻辣烫赛道？",
              "icon_url": agent18,
              "value": "今年将开店 25 家，海底捞盯上了麻辣烫赛道？",
              "desc": "新店主打牛肉麻辣烫，承诺 10 分钟快速出餐",
              "create_time": 0
          },
          {
              "title": "小米眼镜或提前发布，业内看好 AI 眼镜爆发",
              "icon_url": agent19,
              "value": "小米眼镜或提前发布，业内看好 AI 眼镜爆发",
              "desc": "有消息称小米 AI 眼镜已获得入网许可，或于 2 月发布",
              "create_time": 0
          },
          {
              "title": "苹果“大改版”的 iPhone SE 可能要来了？",
              "icon_url": agent20,
              "value": "苹果“大改版”的 iPhone SE 可能要来了？",
              "desc": "外媒称新手机最快或于下周亮相，本月晚些时候上市",
              "create_time": 0
          },
          {
              "title": "日本游戏机市场 5 年来首次陷入萎缩，原因是什么？",
              "icon_url": agent21,
              "value": "日本游戏机市场 5 年来首次陷入萎缩，原因是什么？",
              "desc": "2024 年索尼 PS5 销量暴跌 44%，任天堂 Switch 下降 23%",
              "create_time": 0
          },
          {
              "title": "微信春节数据报告发布，透露什么信号？",
              "icon_url": agent22,
              "value": "微信春节数据报告发布，透露什么信号？",
              "desc": "入境游客消费笔数倍增，书籍成为“蓝包”送礼首选",
              "create_time": 0
          },
          {
              "title": "日本松下官宣放弃电视机业务，具体什么情况？",
              "icon_url": agent23,
              "value": "日本松下官宣放弃电视机业务，具体什么情况？",
              "desc": "2 月 5 日，公司宣布放弃电视机业务并发布重大重组计划",
              "create_time": 0
          },
          {
              "title": "OpenAI 免费开放 ChatGPT 搜索",
              "icon_url": agent24,
              "value": "OpenAI 免费开放 ChatGPT 搜索",
              "desc": "用户无需登录账户即可使用该功能，搜索引擎市场或再生变",
              "create_time": 0
          },
          {
              "title": "A 股逾 2800 份业绩预告出炉，哪些公司增势猛？",
              "icon_url": agent25,
              "value": "A 股逾 2800 份业绩预告出炉，哪些公司增势猛？",
              "desc": "正丹股份业绩预增幅度居首，思特威、华北制药表现同样亮眼",
              "create_time": 0
          },
          {
              "title": "春节档票房再破纪录，《哪吒 2》成“顶梁柱”？",
              "icon_url": agent26,
              "value": "春节档票房再破纪录，《哪吒 2》成“顶梁柱”？",
              "desc": "春节档总票房破百亿，其中“哪吒”票房超 52 亿",
              "create_time": 0
          },
          {
              "title": "OpenAI 发布智能体 Operator",
              "icon_url": agent27,
              "value": "OpenAI 发布智能体 Operator",
              "desc": "能独立完成订票、购物、订餐等任务",
              "create_time": 0
          },
          {
              "title": "全球最佳 CEO 出炉：微软苹果英伟达揽前三",
              "icon_url": agent28,
              "value": "全球最佳 CEO 出炉：微软苹果英伟达揽前三",
              "desc": "小米公司 CEO 雷军以及比亚迪 CEO 王传福入围前十",
              "create_time": 0
          },
          {
              "title": "字节跳动豆包大模型 1.5 Pro 正式发布",
              "icon_url": agent29,
              "value": "字节跳动豆包大模型 1.5 Pro 正式发布",
              "desc": "综合能力显著增强，在多个测评基准上全球领先",
              "create_time": 0
          },
          {
              "title": "微信 PC 发布测试版 4.0.2，有哪些更新？",
              "icon_url": agent30,
              "value": "微信 PC 发布测试版 4.0.2，有哪些更新？",
              "desc": "包括群视频通话支持共享屏幕，手机电脑可互相迁移聊天记录等",
              "create_time": 0
          },
        ],
      }
    },
    methods: {
      onChatNow(data) {
        this.$emit('chatNow', {msg: data.title})
      },
      onSendMessage() {
        this.$emit('chatNow', {msg: this.searchKey})
      }
    }
  }
</script>

<style scoped>
  .container {
    width: 810px;
    overflow: auto;
    /* 隐藏 Firefox 浏览器的滚动条 */
    scrollbar-width: none;
    /* 隐藏 IE 和旧版 Edge 浏览器的滚动条 */
    -ms-overflow-style: none;
  }
  .search-input {
    position: sticky;
    top: 0;
    text-align: center;
    background: #f7f8faff;
    margin-top: 130px;
    padding-top: 40px;
  }

  .relative-list {
    width: 100%;
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

  .relative-list {
    margin-top: 10px;
    margin-bottom: 20px;
  }

  .list-item {
    display: flex;
    align-items: center;
    padding: 12px;
    border-radius: 20px;
    cursor: pointer;
  }

  .list-item img {
    width: 120px;
    height: 82px;
    border-radius: 12px;
    object-fit: cover;
    margin-right: 20px;
  }

  .list-item .title {
    margin: 0;
    margin-bottom: 8px;
    color: #000;
    font-size: 16px;
  }
  .list-item .intro {
    color: rgba(0, 0, 0, 0.5);
    font-size: 12px;
  }

  .list-item:hover {
    background: rgba(0, 0, 0, 0.04);
  }
</style>
