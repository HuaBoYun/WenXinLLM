<template>
  <div>
    <div class="appcontent" v-show="modalStatus == 1">
      <div class="chat-header">
        <!-- <div class="chat-name">AI咨询师</div> -->
        <div class="chat-name">问心AI</div>
        <!-- <div class="wkapp-btn">添加到桌面</div> -->
        <div class="close" @click="modalStatus = 2"></div>
        <!-- <i
          class="el-icon-circle-close close_button"
          @click="modalStatus = 0"
        ></i> -->
      </div>
      <div class="ai-chat">
        <div class="ai-chat-wrap">
          <div class="chat-history">以上是历史消息</div>

          <div class="chat-message">
            <div class="robot-chat-wrap">
              <div class="message">
                <!-- <div class="avatar"></div> -->
                <div class="content">
                  你好！我是你的文档助手，可以辅助你快速写作
                </div>
                <div class="instruct-list">
                  <div
                    class="item-wrap"
                    v-for="item in option"
                    :key="item.id"
                    @click="handleSelect(item.name)"
                  >
                    {{ item.name }}
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="chat-message">
            <div class="time-wrap">{{ nowDate }}</div>
          </div>

          <div
            class="chat-bubble"
            v-for="(item, index) in messages"
            :key="index"
          >
            <div
              class="user-chat-wrap-question1"
              v-if="item.position == 'right'"
            >
              <!-- <div class="avatar"></div> -->
              <span class="question_content">{{ item.content }}</span>
            </div>
            <!-- <img src="./loading.gif" v-if="isLoading" /> -->
            <el-card class="user-chat-wrap-question2" v-else>
              <!-- <div class="avatar"></div> -->
              <span>
                {{ item.content }}
              </span>
            </el-card>
          </div>
          <div
            class="reload"
            v-if="isLastWord && showButton"
            @click="reloadQuestion"
          >
            <i class="el-icon-refresh-right"></i>
            重新生成
          </div>
          <div v-if="!isLastWord" class="stop_content">
            <div class="stop" @click="stop()">停止</div>
          </div>

          <!-- <div class="chat-message">
            <div class="user-chat-wrap">
              <div class="avatar"></div>
              <div class="user-msg-reference">
                如何提高教师素质和教学能力？如何提高教师素质和教学能力？如何提高教师素质和教学能力？如何提高教师素质和教学能力？如何提高教师素质和教学能力？如何提高教师素质和教学能力？
              </div>
            </div>
          </div> -->
          <!-- <div class="chat-message">
            <div class="robot-chat-wrap">
              <div class="message">
                <div class="avatar"></div>
                <div class="chat-array-pager">
                  <div class="pager-left disabled"></div>
                  <div class="pager-content">
                    <span class="pager-text">1</span>
                    <span class="pager-text">/</span>
                    <span class="pager-text">1</span>
                  </div>
                  <div class="pager-right disabled"></div>
                </div>
                <div class="content">
                  你好！我是你的文档助手，可以辅助你快速写作
                </div>
                <div class="divider"></div>
                <div class="btn-wrap">
                  <el-button class="btn">复制</el-button>
                  <el-button class="btn">下载</el-button>
                  <el-button class="btn" type="primary">编辑</el-button>
                </div>
              </div>
              <div class="bottom-box">
                <div class="bottom-action-wrap">
                  <div class="regenerate">
                    <span class="icon icon-regenerate"></span>
                    <span class="text">重新生成</span>
                  </div>
                </div>
                <div class="feedback-btn-wrap">
                  <div class="icon positive"></div>
                  <div class="line"></div>
                  <div class="icon negative"></div>
                </div>
              </div>
            </div>
          </div> -->
          <!-- <div class="chat-message">
            <div class="rec-chat-wrap">
              <div class="message">
                <div class="avatar"></div>
                <div class="rec-tip">为你精选更多相关内容，请查收：</div>
                <div class="doc-info">
                  <div class="cover">
                    <div class="doc-icon-wrap"></div>
                  </div>
                  <div class="detail">
                    <div class="doc-title">多措并举 打造教育强县</div>
                    <div>
                      <span class="score">4.3分</span>
                      <span class="view-count">2阅读</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div> -->
          <div style="height: 30px"></div>
        </div>
      </div>
      <div class="bottom-area">
        <div class="bar">
          <div class="associate-list-wrap">
            <div class="associate-list">
              <div class="associate-list-item" @click="onAIForm">AI风控</div>
              <div
                class="associate-list-item"
                v-for="item in option"
                :key="item.id"
                @click="handleSelect(item.name)"
              >
                {{ item.name }}
              </div>
            </div>
          </div>
        </div>
        <div class="submit-area">
          <div class="user-input-wrap">
            <div class="top-area">
              <div class="textarea-wrap">
                <textarea
                  placeholder='小助手最新黑科技：AI辅助生成PPT，输入"/"选取功能快速体验'
                  autocomplete="off"
                  class="user-input"
                  style="text-indent: 0px"
                  v-model="question"
                ></textarea>
              </div>
            </div>
            <div class="bottom-info-area">
              <span class="user-input-count">
                <em>0</em>
                /400
              </span>
              <div class="send-btn" @click="submit"></div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="wk-chat" ref="wkChat" v-show="modalStatus == 2" v-drag>
      <div class="chat-entry-wrapper_logo"></div>
      <div class="middle-card">
        <div class="entry-event" @click="modalStatus = 1">
          <div class="message">
            <div class="message-title">Hi 我是文档助手</div>
            <div class="message-tip">可以辅助你快速写作，试试以下对话吧</div>
            <div class="instruct-list">
              <div
                class="item-wrap"
                v-for="item in option"
                :key="item.id"
                @click="handleSelect(item.name)"
              >
                {{ item.name }}
              </div>
            </div>
          </div>
          <div class="submit-wrap">
            <span class="submit-wrap-text">请输入您需要的内容</span>
            <div class="submit-wrap-btn"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
  import { mapGetters } from 'vuex'
  import { formatDate } from '@/utils/index'
  import axios from 'axios'
  export default {
    name: 'VabAiTest',
    components: {},
    computed: {},
    data() {
      return {
        x: 0,
        y: 0,
        isDragging: false,
        startX: 0,
        startY: 0,
        modalStatus: 0,
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
        historyArr: [], //历史的所有数据
      }
    },
    created() {
      this.$baseEventBus.$on('newAi', () => {
        this.modalStatus =
          this.modalStatus == 0
            ? (this.modalStatus = 1)
            : (this.modalStatus = 0)
      })

      this.nowDate = formatDate(new Date())
    },
    mounted() {},
    methods: {
      isElementDisplayed(element) {
        // 递归检查所有祖先元素是否隐藏
        function checkParents(el) {
          if (!el) return true;
          const style = window.getComputedStyle(el);
          if (
            style.display === 'none' ||
            style.visibility === 'hidden' ||
            style.opacity === '0'
          ) {
            return false;
          }
          return checkParents(el.parentElement);
        }

        // 检查元素本身及其祖先
        return element && checkParents(element) && element.offsetParent !== null;
      },
      async onAIForm() {
        const obj = {}
        const form = document.getElementById('aiForm')
        if (!form) {
          return this.$message.error('请打开可分析表单页面！')
        }
        
        const formVisible = this.isElementDisplayed(form)
        if (!formVisible) {
          return this.$message.error('请打开可分析表单页面！')
        }

        const formItems = form.getElementsByClassName('el-form-item')

        for (let i = 0;i<formItems.length;i++) {
          const label = formItems[i].getElementsByClassName('el-form-item__label')[0].innerText

          const inputClass = formItems[i].getElementsByClassName('el-input__inner')
          const radioClass = formItems[i].getElementsByClassName('is-checked')
          const textareaClass = formItems[i].getElementsByClassName('el-textarea__inner')
          let value = ''
          if (inputClass && inputClass.length) {
            value = inputClass[0].value
          }
          
          if (textareaClass && textareaClass.length) {
            value = textareaClass[0].value
          }

          if (radioClass && radioClass.length) {
            value = radioClass[0].getElementsByClassName('el-radio__original')[0].value
          }

          console.log(label + ': ' + value)
          obj[label] = value
        }
        console.log('obj', obj)
        const res = await this.fetchAi(obj)
        if (res && res.data) {

          this.addMessage({
            position: 'left',
            content: res.data.text,
            loading: false,
          })
          this.historyArr.push({ role: 'user', content: '帮我分析一下这些表单字段' }) //保存问题对话
          this.historyArr.push({ role: 'assistant', content: res.data.text }) //保存接口回答的对话
        }
      },
      async fetchAi(obj) {
        return new Promise((resolve, reject) => {
          axios({
            url: `https://hit-mitlab.cn:7860/generate_contract`,
            method: 'post',
            headers: { 'Content-Type': 'application/json' },
            data: {
              prompt: '帮我分析一下这些表单字段',
              think: false,
              max_tokens: 2048,
              contract: obj
            },
          }).then((res) => {
            resolve(res)
          }).catch(err => {
            reject(err)
          })
        })
      },
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
          if (e.target.className != 'chat-entry-wrapper_logo') {
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
      closeAi() {
        this.modalStatus = 0
      },
    },
  }
</script>
<style lang="scss" scoped>
  .appcontent {
    position: fixed;
    right: 15px;
    bottom: 15px;
    z-index: 9999;
    border: 1px solid #0075ff;
    height: calc(100vh - 76px);
    opacity: 1;
    pointer-events: auto;
    width: 344px;
    background-color: #fff;
    background-image: url(https://edu-wenku.bdimg.com/v1/pc/aigc/ai-chat-bg-1683892068238.png);
    background-repeat: no-repeat;
    background-size: 100% 100%;
    border-radius: 14px;
    box-shadow: -6px 4px 30px 0 rgba(102, 125, 151, 0.12);
    box-sizing: border-box;
    cursor: default;
    overflow: hidden;

    .close_button {
      position: absolute;
      right: 3px;
      top: 0;
      font-size: 20px;
      &:hover {
        cursor: pointer;
      }
    }

    .chat-header {
      border-bottom: 1px solid #c7d7eb;
      margin: 20px 20px 0;
      transition: 0s;

      .chat-name {
        color: #222;
        font-size: 20px;
        margin-bottom: 10px;
      }

      .wkapp-btn {
        align-items: center;
        border: 1px solid rgba(0, 117, 255, 0.3);
        border-radius: 11.5px;
        box-sizing: border-box;
        color: #0075ff;
        cursor: pointer;
        display: flex;
        font-family: PingFangSC-Medium;
        font-size: 12px;
        font-weight: 500;
        height: 23px;
        justify-content: center;
        line-height: 21px;
        position: absolute;
        right: 60px;
        text-align: center;
        top: 20px;
        width: 93px;
        z-index: 10;
      }

      .wkapp-btn:before {
        background-image: url(https://edu-wenku.bdimg.com/v1/pc/view/ToolBar/icon-wkapp-btn.svg);
        background-position: 50%;
        background-repeat: no-repeat;
        background-size: cover;
        content: '';
        display: inline-block;
        height: 12px;
        margin-right: 2px;
        width: 12px;
      }

      .wkapp-btn:hover {
        background: rgba(0, 117, 255, 0.06);
      }

      .close {
        background-image: url(https://edu-wenku.bdimg.com/v1/pc/aigc/chat-close-1684846755722.svg);
        background-position: 0 0;
        background-repeat: no-repeat;
        background-size: 23px 46px;
        border-radius: 50%;
        cursor: pointer;
        height: 23px;
        position: absolute;
        right: 20px;
        top: 20px;
        width: 23px;
        z-index: 10;
      }
    }

    .ai-chat {
      box-sizing: border-box;
      height: calc(100% - 198px);
      margin: 0 20px 10px;
      overflow-y: auto;
      position: relative;
      z-index: 10;
      transition: all 0s;

      .ai-chat-wrap {
        .chat-history {
          color: #959fa9;
          font-size: 12px;
          font-weight: 400;
          letter-spacing: 0;
          line-height: 12px;
          margin: 16px 0;
          position: relative;
          text-align: center;
          width: 100%;
        }

        .chat-history:after {
          background-image: linear-gradient(
            90deg,
            hsla(0, 0%, 93%, 0),
            rgba(180, 195, 221, 0.6)
          );
          content: '';
          display: inline-block;
          height: 1px;
          position: relative;
          top: -4px;
          width: 90px;
        }

        .chat-history:after {
          margin-left: 8px;
          transform: scaleX(-1);
        }

        .chat-history:before {
          margin-right: 8px;
        }

        .chat-history:before {
          background-image: linear-gradient(
            90deg,
            hsla(0, 0%, 93%, 0),
            rgba(180, 195, 221, 0.6)
          );
          content: '';
          display: inline-block;
          height: 1px;
          position: relative;
          top: -4px;
          width: 90px;
        }

        .user-chat-wrap {
          padding-left: 16px;
          display: flex;

          .avatar {
            background-image: url('./aiLogo.png');
            background-repeat: no-repeat;
            background-size: 26px;
            border-radius: 50%;
            display: inline-block;
            flex-shrink: 0;
            height: 26px;
            margin-bottom: 8px;
            width: 26px;
          }

          .user-msg-reference {
            word-wrap: break-word;
            border-radius: 5px;
            box-sizing: border-box;
            color: #222;
            cursor: pointer;
            display: inline-block;
            flex: 1;
            font-family: PingFangSC-Medium;
            font-size: 14px;
            font-weight: 500;
            line-height: 22px;
            margin-bottom: 16px;
            margin-left: 6px;
            min-height: 26px;
            outline: none;
            padding: 2px;
            position: relative;
            text-align: left;
            vertical-align: top;
            white-space: normal;
          }
        }

        .robot-chat-wrap,
        .rec-chat-wrap {
          color: #959fa9;
          font-size: 12px;
          font-weight: 400;
          letter-spacing: 0;
          line-height: 12px;
          margin: 16px 0;
          position: relative;
          text-align: center;
          width: 100%;

          .message {
            background-color: #fff;
            border-radius: 10px;
            border-top-left-radius: 0;
            box-sizing: border-box;
            margin-bottom: 16px;
            max-width: 304px;
            overflow: hidden;
            padding: 12px 16px 16px;
            text-align: left;
            color: #1f1f1f;

            .avatar {
              background-image: url('./aiLogo.png');
              background-size: 26px;
              border-radius: 50%;
              display: inline-block;
              flex-shrink: 0;
              height: 26px;
              margin-bottom: 8px;
              width: 26px;
            }

            .rec-tip {
              border-bottom: 1px solid #eee;
              font-size: 13px;
              margin-bottom: 11px;
              padding-bottom: 10px;
            }

            .doc-info {
              cursor: pointer;
              display: flex;
              margin-bottom: 11px;

              .cover {
                background-size: 100% 100%;
                border: 1px solid #eee;
                border-radius: 4px;
                height: 57px;
                margin-right: 8px;
                position: relative;
                width: 41px;
                background-image: url('https://wkimg.bdimg.com/img/c1a248102bf90242a8956bec0975f46527d3a7bb?new=1&w=500&p=1');

                .doc-icon-wrap {
                  bottom: 0;
                  height: 16px !important;
                  position: absolute;
                  right: 0;
                  width: 16px !important;
                  background-image: url(https://edu-wenku.bdimg.com/v1/pc/view/FileTypeIcon2022/word.svg);
                  background-repeat: no-repeat;
                  background-size: 100% 100%;
                }
              }

              .detail {
                display: flex;
                flex: 1;
                flex-direction: column;
                font-size: 12px;
                justify-content: center;

                .doc-title {
                  -webkit-box-orient: vertical;
                  -webkit-line-clamp: 2;
                  color: #1f1f1f;
                  display: -webkit-box;
                  font-size: 12px;
                  font-weight: 600;
                  line-height: 18px;
                  margin-bottom: 4px;
                  margin-top: -4px;
                  overflow: hidden;
                  text-overflow: ellipsis;
                  word-break: break-word;
                }

                .score,
                .view-count {
                  margin-right: 10px;
                  color: #858585;
                  font-size: 12px;
                }
              }
            }

            .chat-array-pager {
              align-items: center;
              display: flex;
              float: right;
              padding-top: 4px;

              .pager-left {
                background-image: url(https://edu-wenku.bdimg.com/v1/pc/sulacard/PCCalendarEntry/arrow-black.svg);
                background-repeat: no-repeat;
                background-size: cover;
                height: 12px;
                margin: 0 5px;
                width: 12px;
                transform: rotate(-180deg);
              }

              .pager-right {
                background-image: url(https://edu-wenku.bdimg.com/v1/pc/sulacard/PCCalendarEntry/arrow-black.svg);
                background-repeat: no-repeat;
                background-size: cover;
                height: 12px;
                margin: 0 5px;
                width: 12px;
              }

              .pager-content {
                .pager-text {
                  color: #222;
                  font-size: 13px;
                  letter-spacing: 0;
                  line-height: 18px;
                  margin: 0 3px;
                }
              }
            }

            .content {
              box-sizing: border-box;
              color: #222;
              font-size: 14px;
              width: 100%;
              font-weight: 500;
              line-height: 20px;
            }

            .divider {
              border-top: 1px dashed #eee;
              height: 0;
              margin: 11px auto;
              width: 100%;
            }

            .btn-wrap {
              align-items: center;
              display: flex;
              flex-direction: row;
              justify-content: space-between;
              margin-bottom: 4px;

              .btn {
                border-radius: 8px;
                box-sizing: border-box;
                cursor: pointer;
                font-family: PingFangSC-Medium;
                font-size: 13px;
                font-weight: 500;
                height: 36px;
                padding: 0;
                text-align: center;
                width: 82px;
              }
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
                margin-bottom: 12px;
                max-width: none;
                padding: 12px 13px;
                width: auto;
                word-break: break-all;
                font-size: 14px;
                line-height: 18px;
                transition: all 0s;
              }

              .item-wrap:hover {
                background-color: #e3ebf7;
              }
            }
          }

          .bottom-box {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-top: -8px;

            .bottom-action-wrap {
              height: 20px;
              display: flex;

              .regenerate {
                color: #1f1f1f;
                cursor: pointer;
                display: inline-block;
                font-family: PingFangSC-Regular;
                font-size: 12px;

                .icon-regenerate {
                  background-size: cover;
                  display: inline-block;
                  height: 14px;
                  margin-right: 4px;
                  // margin-top: -2px;
                  width: 14px;
                  background-image: url(https://edu-wenku.bdimg.com/v1/pc/aigc/icon-regenerate-1686194141114.svg);
                }
              }
            }

            .feedback-btn-wrap {
              align-items: center;
              display: flex;
              margin-top: -5px;

              .icon {
                background-position: 0 0;
                background-repeat: no-repeat;
                background-size: 12px 24px;
                cursor: pointer;
                height: 12px;
                width: 12px;
                transition: all 0s;
              }

              .positive {
                background-image: url(https://edu-wenku.bdimg.com/v1/pc/aigc/zan-btn-1684415190889.svg);
              }

              .negative {
                background-image: url(https://edu-wenku.bdimg.com/v1/pc/aigc/cai-btn-1684415191335.svg);
              }

              .positive:hover {
                background-position: 0 -13px;
              }

              .negative:hover {
                background-position: 0 -13px;
              }

              .line {
                background: #919191;
                height: 10px;
                margin: 0 8px;
                opacity: 0.2;
                width: 1px;
              }
            }
          }
        }

        .chat-message {
          .time-wrap {
            color: #959fa9;
            font-size: 12px;
            font-weight: 400;
            letter-spacing: 0;
            line-height: 12px;
            margin-bottom: 16px;
            margin-top: 8px;
            text-align: center;
            width: 100%;
          }
        }
      }
    }

    .bottom-area {
      bottom: 0;
      position: absolute;
      text-align: center;
      width: 100%;
      z-index: 11;

      .bar {
        align-items: center;
        background-color: #ebf3f9;
        display: flex;
        justify-content: flex-start;
        padding: 0 20px;

        .associate-list-wrap {
          -ms-overflow-style: none;
          box-sizing: border-box;
          margin-bottom: 8px;
          margin-top: 5px;
          overflow-x: auto;
          scrollbar-width: none !important;
          white-space: nowrap;
          width: 100%;

          // scrollbar-width: none;
          // -webkit-scrollbar-width: none;
          .associate-list {
            display: inline-block;

            .associate-list-item {
              background: #fff;
              border: 1px solid transparent;
              border-radius: 13px;
              box-sizing: border-box;
              color: #222;
              cursor: pointer;
              display: inline-block;
              font-family: PingFangSC-Regular;
              font-size: 12px;
              font-weight: 400;
              height: 26px;
              letter-spacing: 0;
              line-height: 24px;
              margin-right: 8px;
              max-width: 100%;
              overflow: hidden;
              padding: 0 8px;
              text-align: center;
              text-overflow: ellipsis;
              white-space: nowrap;
            }
          }
        }
      }

      .submit-area {
        box-sizing: border-box;
        padding: 0 20px 20px;
        width: 100%;

        .user-input-wrap {
          background-color: #fff;
          border-radius: 8.5px;
          box-sizing: border-box;
          padding: 10px 8px 7px 16px;
          position: relative;
          transition: opacity 0.5s;
          width: 100%;

          .top-area {
            display: flex;
            position: relative;

            .textarea-wrap {
              flex: 1;
              font-size: 14px;
              overflow: hidden;
              position: relative;

              .user-input {
                border: none;
                box-sizing: border-box;
                color: #222;
                height: 50px;
                line-height: 20px;
                outline: 0;
                overflow-y: auto;
                width: 100%;
                resize: none;
              }
            }
          }

          .bottom-info-area {
            display: flex;
            justify-content: space-between;

            .user-input-count {
              color: #aaa;
              font-family: PingFangSC-Regular;
              font-size: 12px;
              font-weight: 400;
              height: 21px;
              line-height: 21px;
              text-align: left;
            }

            .send-btn {
              background-color: #fff;
              border-radius: 4px;
              height: 20px;
              line-height: 30px;
              text-align: center;
              width: 20px;
              cursor: pointer;
            }

            .send-btn:before {
              background-image: url(https://edu-wenku.bdimg.com/v1/pc/aigc/enter-btn-1683944881372.png);
              background-position: 0 0;
              background-repeat: no-repeat;
              background-size: 12px 20px;
              content: '';
              display: inline-block;
              height: 10px;
              width: 12px;
            }
          }
        }
      }
    }
  }

  .wk-chat {
    top: 100px;
    width: 276px;
    position: fixed;
    z-index: 999999;
    background-color: #fff;
    background-image: url(https://edu-wenku.bdimg.com/v1/pc/aigc/ai-in-bg1-1684918158021.png);
    background-repeat: no-repeat;
    background-size: 100% 100%;
    border: 1px solid #0075ff;
    border-radius: 12px;
    box-shadow: 0 0 50px #ececec;
    box-sizing: border-box;
    cursor: pointer;
    // display: none;
    padding: 16px;
    right: 15px;
    // bottom: 15px;
    // cursor: move;
    transition: all 0s;

    .chat-entry-wrapper_logo {
      background-image: url('./aiLogo.gif');
      background-repeat: no-repeat;
      background-size: 100% 100%;
      border-radius: 50%;
      cursor: grab;
      height: 56px;
      left: -21px;
      position: absolute;
      top: -21px;
      width: 56px;
      transition: all 0s;
    }

    .middle-card {
      width: 100%;
      height: 100%;

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
          background: #fff;
          border-radius: 8px;
          height: 82px;
          position: relative;
          width: 245px;

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

  .user-chat-wrap-question1 {
    display: flex;
    // align-items: center;
    .avatar {
      background-image: url('./user.jpg');
      background-repeat: no-repeat;
      background-size: 26px;
      border-radius: 50%;
      display: inline-block;
      flex-shrink: 0;
      height: 26px;
      margin-bottom: 8px;
      width: 26px;
      margin-right: 10px;
    }
    .question_content {
      font-size: 14px;
      font-weight: 500;
      max-width: 250px;
      word-wrap: break-word;
      line-height: 28px;
    }
  }
  .user-chat-wrap-question2 {
    display: flex;
    // align-items: center;
    .loading {
      background-image: url('./loading.gif');
      background-repeat: no-repeat;
      background-size: 26px;
    }
    .avatar {
      background-image: url('./aiLogo.png');
      background-repeat: no-repeat;
      background-size: 26px;
      border-radius: 50%;
      display: block;
      flex-shrink: 0;
      height: 26px;
      margin-bottom: 8px;
      width: 26px;
      margin-right: 10px;
    }
    .question_content {
      font-size: 14px;
      font-weight: 500;
      max-width: 250px;
      word-wrap: break-word;
      line-height: 28px;
    }
  }
  .reload {
    &:hover {
      cursor: pointer;
    }
  }
  .stop_content {
    text-align: center;
    .stop {
      background-color: #fff;
      border: 1px solid #e2e2e2;
      border-radius: 8px;
      color: #222;
      cursor: pointer;
      display: inline-block;
      font-family: PingFangSC-Semibold;
      font-size: 12px;
      font-weight: 600;
      height: 32px;
      line-height: 32px;
      margin-bottom: 14px;
      margin-top: 2px;
      padding: 0 28px;
      text-align: center;
    }
  }
</style>
