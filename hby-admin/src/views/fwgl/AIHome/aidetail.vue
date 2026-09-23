<template>
  <div class="system-log-container">
    <el-dialog
      :append-to-body="true"
      title="AI问答"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
      :close-on-click-modal="false"
    >
      <el-row type="flex" justify="center" :gutter="24">
        <el-col :span="10" class="el-col1">
          <el-card :body-style="{ padding: 0 }" class="card">
            <div class="tab-card">
              <vab-icon class="red" :icon="currentObj.icon" />
              <div class="body">
                <div class="header clearfix">
                  <span>{{ currentObj.name }}</span>
                </div>
                <div>{{ currentObj.des }}</div>
                <div style="margin-top: 15px">请问您有什么问题或需求？</div>
                <div class="bottom clearfix">
                  <div>
                    <svg
                      stroke="currentColor"
                      fill="currentColor"
                      stroke-width="0"
                      viewBox="0 0 16 16"
                      height="1em"
                      width="1em"
                      xmlns="http://www.w3.org/2000/svg"
                    >
                      <path
                        d="M8 16c3.314 0 6-2 6-5.5 0-1.5-.5-4-2.5-6 .25 1.5-1.25 2-1.25 2C11 4 9 .5 6 0c.357 2 .5 4-2 6-1.25 1-2 2.729-2 4.5C2 14 4.686 16 8 16Zm0-1c-1.657 0-3-1-3-2.75 0-.75.25-2 1.25-3C6.125 10 7 10.5 7 10.5c-.375-1.25.5-3.25 2-3.5-.179 1-.25 2 1 3 .625.5 1 1.364 1 2.25C11 14 9.657 15 8 15Z"
                      ></path>
                    </svg>
                    {{ currentObj.hot }}
                  </div>
                  <el-button
                    type="text"
                    class="button right-btn"
                    v-if="!currentObj.isStar"
                    @click="currentObj.isStar = true"
                  >
                    <i class="el-icon-star-off"></i>
                    收藏 {{ currentObj.star }}
                  </el-button>
                  <el-button
                    type="text"
                    class="button right-btn"
                    v-else
                    @click="currentObj.isStar = false"
                  >
                    <i class="el-icon-star-on orange"></i>
                    已收藏 {{ +currentObj.star + 1 }}
                  </el-button>
                </div>
              </div>
            </div>
          </el-card>
          <div
            class="chat-bubble"
            v-for="(item, index) in messages"
            :key="index"
          >
            <div class="right-chat" v-if="item.position == 'right'">
              <span>{{ item.content }}</span>
            </div>

            <el-card class="card left-chat" v-if="item.position == 'left'">
              <div class="ai-avatar" style="display: flex; align-items: center">
                <img
                  src="@/assets/deepseek.svg"
                  style="width: 30px; height: 30px; margin-right: 4px"
                  alt=""
                />
                问心AI：
              </div>
              <span>{{ item.content }}</span>
            </el-card>
          </div>
          <Loading v-if="isLoading" loadintText="思考中" />
        </el-col>
      </el-row>

      <div class="footer">
        <el-row type="flex" justify="center" :gutter="24">
          <el-col :span="10">
            <div class="footer-box">
              <input
                type="text"
                name="question"
                placeholder="输入问题或需求 ..."
                autocomplete="off"
                v-model="question"
              />
              <div class="footer-btn" @click="submit">开始对话</div>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import axios from 'axios'
  import Loading from '@/components/Loading.vue'
  export default {
    components: { Loading },
    data() {
      return {
        question: '', //问题
        messages: [], //对话
        intervalId: null, //定时器
        isLoading: false, //正在回复
        currentObj: {}, //传来的数据
        dialogFormVisible: false,
      }
    },
    mounted() {},
    methods: {
      //提交对话
      submit() {
        if (this.isLoading) {
          return
        }
        if (!this.question) {
          this.$message.error('请输入问题')
          return
        }
        this.isLoading = true
        const arr = this.messages
        arr.push({ position: 'right', content: this.question })
        this.messages = arr

        axios({
          url: `https://hit-mitlab.cn:7860/generate_law`,
          method: 'post',
          headers: { 'Content-Type': 'application/json' },
          data: {
            prompt: this.question,
            temperature: 1.0,
          },
        }).then((res) => {
          this.addMessage({
            position: 'left',
            content: res.data.text,
          })
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
        const typeNextLetter = () => {
          this.messages[this.messages.length - 1].content +=
            message.content.charAt(i)
          i++
          if (i <= msgLength) {
            setTimeout(typeNextLetter, typingDelay)
          } else {
            clearTimeout(this.intervalId)
          }
        }
        clearTimeout(this.intervalId)
        this.intervalId = setTimeout(typeNextLetter, 500)
      },
      show(row) {
        this.dialogFormVisible = true
        //拿数据
        this.currentObj = row
      },
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style scoped lang="scss">
  .system-log-container {
    position: relative;
    background: #ebefef !important;
  }

  .el-col1 {
    padding: 0 20px 100px;
    position: relative;
    height: calc(100vh - 350px);
    overflow-y: auto;
    width: 100%;
  }
  .card {
    // position: relative;
    border-radius: 10px;
  }

  .chat-bubble {
    padding: 10px 0;
  }

  .left-chat {
    line-height: 25px;
    word-break: break-word;
  }

  .right-chat {
    display: flex;
    justify-content: right;
  }

  .tab-card {
    display: flex;
    align-items: center;
    padding: 20px;
    background-color: #fff;
  }

  .body {
    flex: 1;
  }

  .header {
    font-size: 20px;
    font-weight: 600;
    margin-bottom: 20px;
    .right-btn {
      font-size: 18px;
      font-weight: 600;
      color: #aaa;
    }
  }

  .bottom {
    padding: 20px 0 10px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    color: #333;
    font-size: 14px;
  }
  .right-btn {
    font-size: 14px;
    float: right;
    padding: 3px 0;
    color: #333;
  }

  .footer {
    width: 100%;
    position: relative;
    bottom: 20px;
    z-index: 99;

    .footer-box {
      width: 100%;
      flex: 1;
      display: flex;
      align-items: center;
      position: relative;
      height: 50px;
      background-color: #333;
      border-radius: 50px;
      input {
        caret-color: #ffa502;
        flex: 1;
        height: 50px;
        border-radius: 50px;
        padding: 12px 20px;
        box-sizing: border-box;
        -webkit-transition: 0.5s;
        background-color: #333;
        transition: 0.5s;
        outline: none;
        border: 0;
        color: #fff;
      }
    }

    .footer-btn {
      position: absolute;
      right: 20px;
      z-index: 9;
      color: #ddd;
      cursor: pointer;
      &:hover {
        color: green;
      }
    }
  }

  .red {
    font-size: 48px;
    color: #e50113;
    width: 100px;

    &.mini {
      font-size: 18px;
      margin-right: 3px;
      width: auto;
    }
  }
</style>
