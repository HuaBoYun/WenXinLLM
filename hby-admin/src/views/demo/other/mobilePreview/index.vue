<template>
  <div class="mobile-preview-container">
    <el-row class="hidden-xs-only">
      <el-col :lg="12" :md="13" :sm="13" :xl="8" :xs="24">
        <div class="mobile-content">
          <iframe
            class="mobile-content-iframe"
            src="https://m.baike.so.com/doc/8246271-8563260.html"
          />
        </div>
      </el-col>
      <el-col :lg="12" :md="11" :sm="11" :xl="12" :xs="24">
        <el-alert :closable="false" type="success">
          <div>
            <h3>vab作者小众且片面的一些思考：</h3>
            <p>
              借用于正的一段话：每一个在前端娱乐圈冉冉升起的人在过程中总要经历各种谣言、诋毁、伤害…无一例外。因为你起来了，就要面临抢别人的饭碗、广告、以及各种商务…大家细想想，哪个明星上位过程中是一帆风顺的？没有！凭什么你起来了？凭什么你赚那么多钱？某些阴沟的蛆总是带着嫉妒、不屑的眼光等着看你的笑话，逮到机会拉你下马。你要是受不了就被打败了，你要是挺过去，继续往前走，等他们够不着你了，就会仰视你，那时候你红才是真的红。所以既然干了这一行，既然已经出成绩了，就要坦然面对公众的审视，不能有玻璃心，不能被打倒，更不能上别人的当，四处扫射。忍耐、前进、提高才华是唯一的道路。
            </p>
            <p>
              别人夸你别信，别人骂你别听。我们应该有自己的判断力，不管是网友，还是身边的亲人朋友，也许他们的本质是为了我们好，但是他们的思维会影响我们做出更适合我们自己的决定，会直接导致我们也过上和他们一样的生活，你想安安稳稳那就听取他们的意见，你想一飞冲天那就去探索一条属于自己的路，努力到无能为力，努力到刀枪不入。
            </p>
            <p>
              admin-pro的初衷很简单，赚钱！让每个使用它做项目的公司赚到钱，让每个前端用它的前后端程序员找到好工作，让私活不再难接，让天下没有难做的项目，我们所赚到的每一分钱，都是帮助他人解决问题后得到的回报，我们赚的心安理得。
            </p>
            <p>
              admin-pro走到今天，太多的不容易，我第一次接触前端是2014年碰到了一个叫Hbuilder的开发工具，因为他的代码提示，让我爱上了前端，再后来我遇到了layui，贤心的代码让我对前端有了新的认识，再后来我遇到了vue，他的作者尤雨溪是一个比我还好看的人，我的好多想法都跟他在知乎的一些想法不谋而和，后来遇到了element-ui，靠着element-ui我接到了第一个小项目，作者朱坤也是个很好看的小哥哥，vue-admin-beautiful的出现还应该感谢花裤衩的vue-element-admin，起初大多数的灵感都来源于这个项目，我看到它时他的标星已经突破了三万，我最初渴望做一个和他一样好用的框架，可惜他太强大，也许我穷尽一生也不能超越他的标星，最终我放弃了，不再把所有精力都放到开源版上，admin-pro终于诞生了，它不够完美，但它倾注了我的心血，它就像我的孩子，希望我的孩子能靠一己之力帮助千千万万个相信它的朋友。
            </p>
            <p>
              对了这是我知乎对于vue-admin-beautiful项目的回答，骂我可以，但请不要骂我的孩子。
              <a
                href="https://www.zhihu.com/question/423592827/answer/1502779880"
                target="_blank"
              >
                https://www.zhihu.com/question/423592827/answer/1502779880
              </a>
            </p>
            <p>
              admin-pro购买链接
              <a
                href="https://gitee.com/chu1204505056/image/raw/master/zfb_699.jpg"
                target="_blank"
              >
                https://gitee.com/chu1204505056/image/raw/master/zfb_699.jpg
              </a>
              ，付款后加QQ
              1204505056获取下载权限，是否购买pro填是即可(包含VIP专属问题解答群、PRO详细文档、PRO基础版源码、PRO标准版源码、绑定git随时克隆及更新权限、跟进Vue3.x)
            </p>
          </div>
        </el-alert>
      </el-col>
    </el-row>
    <el-row class="hidden-sm-and-up">
      <el-col :span="24">
        <el-alert
          title="手机端支持该组件预览，请切换到PC端查看效果"
          type="success"
        />
      </el-col>
    </el-row>
  </div>
</template>

<script>
  export default {
    name: 'MobilePreview',
    data() {
      return {}
    },
    created() {},
    mounted() {},
    methods: {},
  }
</script>

<style lang="scss" scoped>
  $base: mobile;
  .#{$base}-preview-container {
    .#{$base}-content {
      position: relative;
      float: right;
      width: 350px;
      height: 700px;
      margin-right: 10%;
      background: url('~@/assets/mobile_images/mobile.png');
      background-size: 100% 100%;

      &-iframe {
        position: absolute;
        top: 10px;
        right: 12.5px;
        bottom: 8px;
        left: 12px;
        width: 325.5px;
        height: 682px;
        padding: 10px;
        background: $base-color-white;
        border: 0;
        border-radius: 42.5px;
      }
    }

    ::v-deep {
      .el-alert__description {
        margin-top: 10px;
        font-size: 14px;
        line-height: 20px;

        p {
          text-indent: 2em;
        }
      }
    }
  }
</style>
