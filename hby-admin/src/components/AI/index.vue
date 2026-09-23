<template>
  <div class="content" ref="content" v-if="modalStatus" style="z-index: 9999">
    <i class="el-icon-circle-close close_icon" @click="closeAI"></i>
    <div class="wk-chat">
      <div
        class="answer"
        v-if="messages.length % 2 == 0 && messages.length > 0"
      >
        {{
          messages.length % 2 == 0 && messages.length > 0
            ? messages[messages.length - 1].content
            : ''
        }}
      </div>
      <div class="suggest" v-if="isLastWord && showButton">
        <div class="btn" @click="insertText">插入</div>
        <div class="btn">放弃</div>
        <div class="btn" @click="reloadQuestion">重新写作</div>
      </div>
      <div class="middle-card">
        <div class="chat-entry-wrapper_logo"></div>
        <div class="entry-event">
          <div class="submit-wrap">
            <el-input
              placeholder="请输入您需要的内容"
              v-model="question"
              v-if="!isLoading"
            >
              <i
                slot="suffix"
                class="el-input__icon el-icon-s-promotion"
                @click="submit()"
              ></i>
            </el-input>
            <img
              src="./loading.gif"
              alt=""
              style="width: 50px; height: 20px; margin-left: 20px"
              v-if="isLoading"
            />
          </div>
        </div>
      </div>
    </div>
    <div class="sug-wrap small">
      <ul class="sug-list">
        <li class="sug-item sug-item-cmd">
          <img
            src="https://edu-wenku.bdimg.com/v1/pc/aigc/xuxie-1685086334133.png"
            alt="图标"
            class="sug-icon"
          />
          续写
        </li>
        <li class="sug-item sug-item-cmd checked">
          <img
            src="https://edu-wenku.bdimg.com/v1/pc/aigc/zongjie-1685086335478.png"
            alt="图标"
            class="sug-icon"
          />
          总结
        </li>
        <li class="sug-item sug-item-cmd">
          <img
            src="https://edu-wenku.bdimg.com/v1/pc/aigc/runse-1685086331872.png"
            alt="图标"
            class="sug-icon"
          />
          润色
        </li>
        <li class="sug-item sug-item-cmd">
          <img
            src="https://edu-wenku.bdimg.com/v1/pc/aigc/xiuding-1685086333409.png"
            alt="图标"
            class="sug-icon"
          />
          词汇&amp;语法修订
        </li>
        <li class="sug-item sug-item-cmd">
          <img
            src="https://edu-wenku.bdimg.com/v1/pc/aigc/suoxie-1685086332371.png"
            alt="图标"
            class="sug-icon"
          />
          缩写
        </li>
        <li class="sug-item sug-item-cmd">
          <img
            src="https://edu-wenku.bdimg.com/v1/pc/aigc/kuoxie-1685086330817.png"
            alt="图标"
            class="sug-icon"
          />
          扩写
        </li>
        <li class="sug-item sug-item-cmd">
          <img
            src="https://edu-wenku.bdimg.com/v1/pc/aigc/yuqi-1685086334615.png"
            alt="图标"
            class="sug-icon"
          />
          改变语气
          <span class="arrow"></span>
        </li>
        <li class="sug-item sug-item-cmd">
          <img
            src="https://edu-wenku.bdimg.com/v1/pc/aigc/tounaofengbao-1685086333042.png"
            alt="图标"
            class="sug-icon"
          />
          头脑风暴
        </li>
        <li class="sug-item sug-item-cmd">
          <img
            src="https://edu-wenku.bdimg.com/v1/pc/aigc/dagang-1685086330225.png"
            alt="图标"
            class="sug-icon"
          />
          大纲
        </li>
        <li class="sug-item sug-item-cmd">
          <img
            src="https://edu-wenku.bdimg.com/v1/pc/aigc/qifa-1685086775018.png"
            alt="图标"
            class="sug-icon"
          />
          启发一下
        </li>
        <li class="sug-item sug-item-cmd">
          <img
            src="https://edu-wenku.bdimg.com/v1/pc/aigc/lishi-1685086331289.png"
            alt="图标"
            class="sug-icon"
          />
          历史指令
          <span class="arrow"></span>
        </li>
      </ul>
    </div>
  </div>
</template>
<script>
  import axios from 'axios'
  export default {
    name: 'VabAiEditDetail',
    components: {},
    computed: {},
    data() {
      return {
        x: 0,
        y: 0,
        isDragging: false,
        startX: 0,
        startY: 0,
        modalStatus: false,
        option: [
          { name: '帮我写一篇工作计划', id: 1 },
          { name: '帮我写一篇竞选发言稿', id: 2 },
          { name: '帮我写一篇工作汇报', id: 3 },
        ],
        nowDate: '',
        question: '',
        lastQuestion: '',
        messages: [], //对话
        intervalId: null, //定时器
        isLoading: false, //正在回复
        isLastWord: true, //内容有没有全部显示出来
        showButton: false, //一开始是否显示按钮
        currentObj: {}, //传来的数据
        stopLoading: false,
        searchInfo: '',
        dom: {},
        historyArr: [], //历史的所有数据
      }
    },
    // created() {
    //   this.$baseEventBus.$on('newAiEdit', () => {
    //     this.modalStatus =
    //       this.modalStatus == 0
    //         ? (this.modalStatus = 1)
    //         : (this.modalStatus = 0)
    //   })
    // },
    mounted() {},
    methods: {
      show(data) {
        this.modalStatus = true

        if (data.source == 'textarea') {
          this.dom = data.dom
        }
      },
      insertText() {
        if (this.messages.length > 0) {
          this.dom.$emit(
            'input',
            (this.dom.value || '') +
              this.messages[this.messages.length - 1].content
          )

          this.modalStatus = false
        }
      },

      closeAI() {
        this.modalStatus = false
      },
      handleIconClick() {},
      handleSelect(info) {
        this.question = info
      },

      //提交对话
      submit() {
        this.showButton = false
        this.stopLoading = false

        if (this.isLoading) {
          return
        }
        if (!this.question) {
          this.$message.error('不能发送空白消息哦')
          return
        }

        this.lastQuestion = this.question //存上一次的提问问题，用于重新生成功能

        const arr = this.messages
        arr.push({ position: 'right', content: this.question })
        this.messages = arr
        this.isLoading = true

        axios({
          url: `https://hit-mitlab.cn:7860/generate_doc`,
          method: 'post',
          headers: { 'Content-Type': 'application/json' },
          data: {
            prompt: this.question,
            temperature: 1.0,
            max_tokens: 2048,
            // history: this.historyArr,
            history: this.handleData(this.historyArr),
          },
        }).then((res) => {
          this.addMessage({
            position: 'left',
            content: res.data.text,
            loading: false,
          })
          this.historyArr.push({ role: 'user', content: this.question }) //保存问题对话
          this.historyArr.push({ role: 'assistant', content: res.data.text }) //保存接口回答的对话
          this.isLoading = false
          this.question = ''
        })
      },
      // 生成对话
      addMessage(message) {
        const arr = this.messages
        arr.push({ ...message, content: '' })
        this.messages = arr
        let i = 0
        const msgLength = message.content.length
        const typingDelay = Math.floor(Math.random() * 100) + 50 // 随机生成打字的延迟时间
        this.isLastWord = false
        const typeNextLetter = () => {
          this.messages[this.messages.length - 1].content +=
            message.content.charAt(i)
          i++
          if (i <= msgLength) {
            //点击停止按钮，清除定时器
            if (!this.stopLoading) {
              setTimeout(typeNextLetter, typingDelay)
            } else {
              clearTimeout(this.intervalId)
            }
          } else {
            clearTimeout(this.intervalId)
            this.isLastWord = true
            this.showButton = true
          }
        }
        clearTimeout(this.intervalId)
        this.intervalId = setTimeout(typeNextLetter, 500)
      },
      reloadQuestion() {
        this.showButton = false
        this.isLoading = true
        const arr = this.messages

        arr.push({ position: 'right', content: this.lastQuestion })
        this.messages = arr

        axios({
          url: `https://hit-mitlab.cn:7860/generate_doc`,
          method: 'post',
          headers: { 'Content-Type': 'application/json' },
          data: {
            prompt: this.lastQuestion,
            temperature: 1.0,
            max_tokens: 2048,
            history: this.handleData(this.historyArr),
          },
        }).then((res) => {
          this.addMessage({
            position: 'left',
            content: res.data.text,
            loading: false,
          })
          this.historyArr.push({ role: 'user', content: this.lastQuestion }) //保存问题对话
          this.historyArr.push({ role: 'assistant', content: res.data.text }) //保存接口回答的对话
          this.isLoading = false
          this.question = ''
        })
      },
      stop() {
        this.stopLoading = true
        const arr = this.messages
        //把最后一个回答删除
        arr.splice(arr.length - 1, 1)
        this.isLastWord = true
        this.messages = arr
      },
      handleData(data) {
        const info = data.slice().reverse()

        let accumulatedLength = 0
        const arr = []

        for (const item of info) {
          accumulatedLength += item.content.length
          if (accumulatedLength <= 4000) {
            arr.push(item)
          } else {
            break
          }
        }

        return arr.slice().reverse()
      },
    },

    directives: {
      drag(el) {
        let oDiv = el
        document.onselectstart = function () {
          return false
        }
        oDiv.onmousedown = function (e) {
          const viewWidth = document.documentElement.clientWidth
          const eWidth = e.target.offsetWidth
          const eParentWith = e.target.parentNode.offsetWidth
          const viewHeight = document.documentElement.clientHeight
          const eHeight = e.target.offsetHeight
          const eParentHeight = e.target.parentNode.offsetHeight
          if (e.target.className != 'content') {
            return false
          }

          let disX = e.clientX - oDiv.offsetLeft
          let disY = e.clientY - oDiv.offsetTop
          document.onmousemove = function (e) {
            let l = Math.max(e.clientX - disX, eWidth / 2)
            let t = Math.max(e.clientY - disY, eHeight / 2)
            console.log(viewHeight + 'px', t + 'px')
            oDiv.style.left =
              Math.min(l, viewWidth - eParentWith - eWidth / 2) + 'px'
            oDiv.style.top =
              Math.min(t, viewHeight - eParentHeight - eHeight / 2) + 'px'
          }
          document.onmouseup = function (e) {
            document.onmousemove = null
            document.onmouseup = null
          }
          return false
        }
      },
    },
  }
</script>
<style lang="scss" scoped>
  // ::v-deep .el-input__inner {
  //   border: none !important;
  // }

  .content {
    position: fixed;
    top: 30%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 60%;
    z-index: 999999;
    .close_icon {
      position: absolute;
      right: 4px;
      top: 0;
      color: #0075ff;
      &:hover {
        cursor: pointer;
      }
    }

    .wk-chat {
      width: 100%;
      z-index: 999999;
      background-color: #fff;
      // background-image: url(https://edu-wenku.bdimg.com/v1/pc/aigc/ai-in-bg1-1684918158021.png);
      background-repeat: no-repeat;
      background-size: 100% 100%;
      border: 1px solid #0075ff;
      border-radius: 12px;
      box-shadow: 0 0 50px #ececec;
      box-sizing: border-box;
      cursor: pointer;
      padding: 6px;
      transition: all 0s;
      padding: 10px;
      .answer {
        padding: 10px 0;
      }
      .suggest {
        display: flex;
        padding: 10px 0 20px 0;
        .btn {
          align-items: center;
          border: 1px solid #e2e2e2;
          border-radius: 6px;
          box-sizing: border-box;
          color: #222;
          cursor: pointer;
          display: flex;
          font-family: PingFangSC-Semibold;
          font-size: 12px;
          height: 32px;
          margin-right: 8px;
          padding: 0 15px;
          &:nth-child(1) {
            background: #21ab86;
            border: 1px solid transparent;
            color: #fff;
          }
        }
      }

      .chat-entry-wrapper_logo {
        background-image: url('./aiLogo.png');
        background-repeat: no-repeat;
        background-size: 100% 100%;
        border-radius: 50%;
        cursor: grab;
        height: 36px;
        // left: -21px;
        // position: absolute;
        // top: -21px;
        width: 36px;
        transition: all 0s;
      }

      .middle-card {
        width: 100%;
        height: 100%;
        display: flex;
        align-items: center;

        .entry-event {
          width: 100%;
          height: 100%;
          cursor: pointer;

          .message {
            background-color: #fff;
            border-radius: 5px;
            box-sizing: border-box;
            color: #1f1f1f;
            display: inline-block;
            font-size: 12px;
            margin-bottom: 15px;
            margin-left: 33px;
            min-height: 36px;
            padding: 15px 18px 15px 21px;
            position: relative;
            text-align: left;
            vertical-align: top;
            width: 211px;

            .message-title {
              color: #222;
              font-size: 17px;
              font-weight: 600;
              letter-spacing: 0;
              margin-bottom: 5px;
            }

            .message-tip {
              color: #777;
              font-size: 12px;
              font-weight: 400;
              letter-spacing: 0;
              line-height: 18px;
            }

            .instruct-list {
              margin-top: 10px;

              .item-wrap {
                align-items: flex-start;
                background-color: #f7f7f7;
                border-radius: 0 12px 12px 12px;
                color: #222;
                cursor: pointer;
                display: flex;
                flex-direction: row;
                justify-content: flex-start;
                margin-bottom: 7px;
                max-width: none;
                padding: 7px 13px;
                width: auto;
                word-break: break-all;
                font-size: 13px;
                line-height: 18px;
                transition: all 0s;
              }

              .item-wrap:hover {
                background-color: #e3ebf7;
              }
            }
          }

          .message:before {
            background-image: url(https://edu-wenku.bdimg.com/v1/pc/aigc/white-message-arrow-1684049709785.svg);
            background-size: 100% 100%;
            content: '';
            height: 15px;
            left: -9px;
            position: absolute;
            top: 5px;
            width: 10px;
          }

          .submit-wrap {
            // background: #fff;
            border-radius: 8px;
            // height: 82px;
            // position: relative;
            // width: 245px;
            // .search_input {
            //   width: 100%;
            //   border: none !important;

            // }
            .el-input--small {
              .el-input__inner {
                border: none !important;
              }
            }
            .el-input--suffix {
              .el-input__inner {
                border: none !important;
              }
            }

            .submit-wrap-text {
              color: #a2b4c9;
              font-size: 14px;
              font-weight: 400;
              left: 19px;
              position: relative;
              top: 12px;
            }

            .submit-wrap-text:before {
              background: #a2b4c9;
              content: '';
              display: inline-block;
              height: 16px;
              margin-right: 4px;
              position: relative;
              top: 3px;
              width: 2px;
            }

            .submit-wrap-btn {
              background-image: url(https://edu-wenku.bdimg.com/v1/pc/aigc/enter-btn-1683944881372.png);
              background-position: 0 -10px;
              background-repeat: no-repeat;
              background-size: 12px 20px;
              height: 10px;
              left: 225px;
              position: absolute;
              top: 63px;
              width: 12px;
            }
          }
        }
      }
    }

    .sug-wrap {
      background: #fff;
      border: 1px solid #e2e8f0;
      border-radius: 6px;
      box-shadow: 0 6px 12px 0 rgba(19, 98, 192, 0.07);
      box-sizing: border-box;
      font-size: 12px;
      margin-top: 6px;
      padding: 0 0 8px 8px;
      position: absolute;
      top: 100%;
      left: 0;
      width: 216px;
      z-index: 999999;
      .sug-list {
        max-height: 170px;
        overflow-y: scroll;
        .sug-item {
          border-radius: 6px;
          box-sizing: border-box;
          color: #222;
          cursor: pointer;
          font-family: PingFangSC-Medium;
          font-size: 14px;
          height: 36px;
          letter-spacing: 0;
          line-height: 36px;
          margin-right: 6px;
          overflow: hidden;
          padding-left: 40px;
          padding-right: 12px;
          position: relative;
          text-overflow: ellipsis;
          white-space: nowrap;
          .sug-icon {
            height: 20px;
            left: 11px;
            position: absolute;
            top: 8px;
            width: 20px;
          }
        }
      }
    }
  }

  body,
  div,
  dl,
  dt,
  dd,
  ul,
  ol,
  li,
  h1,
  h2,
  h3,
  h4,
  h5,
  h6,
  pre,
  code,
  form,
  fieldset,
  legend,
  textarea,
  p,
  blockquote,
  th,
  td {
    padding: 0;
    margin: 0;
  }
</style>
