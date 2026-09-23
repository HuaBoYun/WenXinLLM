<template>
  <div class="container">
    <div v-if="!showChatPage">
      <div class="text-content">
        <div class="page-title">
          <div class="menu-item">
            <i :class="`menu-icon el-icon-s-check`"></i>
            <div class="menu-title">超能合同</div>
          </div>
        </div>
        <div class="intro">合规精准，专业到位，AI加持法律服务</div>
      </div>

      <div style="display: flex">
        <div class="relative-list">
          <div
            class="list-item"
            :class="{ 'list-item-actived no-hover': item.key === actived }"
            v-for="(item, $index) in tempRelativeList"
            :key="item.title + $index"
            @click="onTypeClick(item)"
          >
            {{ item.title }}
          </div>
        </div>
        <div @click="open = !open" style="cursor: pointer; margin-top: 8px">
          <i v-if="open" class="el-icon-arrow-up"></i>
          <i v-else class="el-icon-arrow-down"></i>
          {{ open ? '收起' : '展开' }}
        </div>
      </div>

      <div class="relative-cards">
        <div
          class="card-item"
          :class="{ 'card-item-actived': item.actived }"
          v-for="item in allList"
          :key="item.id"
          @click="onCardClick(item)"
        >
          <div class="card-img"><img :src="contractPng" alt="" /></div>
          <div class="card-title">
            {{ item.title }}
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
            placeholder="生成一篇合同，类型为【合同类型】，具体要求是："
            @keydown.enter.native="onSendMessage"
          />
          <div class="btn-group">
            <div class="switch-group">
              <div
                @click="think = !think"
                class="deep"
                :style="`${
                  think
                    ? 'color:#4D6BFE;background:#DBEAFE;border-color:rgba(0, 122, 255, 0.15);'
                    : ''
                }`"
              >
                深度思考
                <i v-if="!think" class="btn-icon el-icon-turn-off"></i>
                <i v-else class="btn-icon el-icon-open"></i>
              </div>
              <div
                @click="search = !search"
                class="deep"
                :style="`${
                  search
                    ? 'color:#4D6BFE;background:#DBEAFE;border-color:rgba(0, 122, 255, 0.15);'
                    : ''
                }`"
              >
                联网搜索
                <i v-if="!search" class="btn-icon el-icon-turn-off"></i>
                <i v-else class="btn-icon el-icon-open"></i>
              </div>
              <div
                @click="editor = !editor"
                class="deep"
                :style="`${
                  editor
                    ? 'color:#4D6BFE;background:#DBEAFE;border-color:rgba(0, 122, 255, 0.15);'
                    : ''
                }`"
              >
                <el-select v-model="contractType" class="select-style">
                  <el-option
                    v-for="item in contractTypes"
                    :label="item.label"
                    :value="item.value"
                    :key="item.value"
                  ></el-option>
                </el-select>
                <!-- <i v-if="!editor" class="btn-icon el-icon-turn-off"></i>
                <i v-else class="btn-icon el-icon-open"></i> -->
              </div>
            </div>

            <div class="icons">
              <fileUpload ref="fileUpload" v-model="fileObject" />
              <voice class="icon" @getMessage="getMessage" />
              <i class="icon el-icon-top send" @click="onSendMessage"></i>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-else class="chatPage">
      <div
        style="
          position: absolute;
          top: 30px;
          font-size: 22px;
          cursor: pointer;
          z-index: 100;
        "
        @click="close"
      >
        <i class="el-icon el-icon-close"></i>
      </div>
      <AIChat v-if="showChatPage" :msgData="msgData" />
    </div>

    <el-dialog
      :title="curSelected.title"
      :visible.sync="officeViewDialog"
      append-to-body
      width="800px"
      @close="officeViewDialog = false"
    >
      <div style="height: 600px">
        <div
          style="
            width: 98%;
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin: 10px;
          "
        >
          <div style="font-size: 12px; line-height: 12px; color: #999">
            文件大小：43.50K | 下载次数：1497 | 阅读数：10807
          </div>
          <div class="df">
            <div class="btn default-btn">下载</div>
            <div class="btn primary-btn" @click="improve">完善当前合同</div>
          </div>
        </div>
        <iframe
          frameborder="0"
          style="width: 100%; height: 100%"
          src="https://office.wenxin.example.com/media/uploads/1677_%E5%8D%95%E8%AF%8D%E8%A1%A81742734036.7470608_pQzyKAi.pdf"
        ></iframe>
      </div>
    </el-dialog>

    <el-dialog
      :title="curSelected.title"
      :visible.sync="improveOptionDialog"
      append-to-body
      width="800px"
      @close="improveOptionDialog = false"
    >
      <div class="improveOption">
        <div>
          <div class="option-title">1. 选择你的立场</div>
          <div class="options-1">
            <div
              @click="radio = '1'"
              :class="`option-item ${
                radio == '1' ? 'option-item-actived' : ''
              }`"
            >
              <el-radio v-model="radio" label="1">
                <span>受托方</span>
                <div
                  style="
                    margin-top: 12px;
                    font-size: 18px;
                    font-weight: 700;
                    color: #000;
                  "
                >
                  甲方
                </div>
              </el-radio>
            </div>
            <div
              @click="radio = '2'"
              :class="`option-item ${
                radio == '2' ? 'option-item-actived' : ''
              }`"
            >
              <el-radio v-model="radio" label="2">
                <span>委托方</span>
                <div
                  style="
                    margin-top: 12px;
                    font-size: 18px;
                    font-weight: 700;
                    color: #000;
                  "
                >
                  乙方
                </div>
              </el-radio>
            </div>
          </div>
        </div>
        <div style="margin-top: 20px">
          <div class="option-title">2. 选择你的修改尺度</div>
          <div class="options-2">
            <div
              @click="optionValue2 = '1'"
              :class="`option-item ${
                optionValue2 == '1' ? 'option-item-actived' : ''
              }`"
            >
              强势
            </div>
            <div
              @click="optionValue2 = '2'"
              :class="`option-item ${
                optionValue2 == '2' ? 'option-item-actived' : ''
              }`"
            >
              中立
            </div>
            <div
              @click="optionValue2 = '3'"
              :class="`option-item ${
                optionValue2 == '3' ? 'option-item-actived' : ''
              }`"
            >
              弱势
            </div>
          </div>
          <div class="tip">
            合同双方谈判地位势均，你所代表的阵营无明显优势。系统在修改合同时应以完善合同内容、增强合同的可操作性为出发点，以权利义务对等为原则，对合同内容进行修改。
          </div>
        </div>
        <div style="margin-top: 20px">
          <div class="option-title">3. 编辑合同修改重点</div>
          <div class="tip">编辑设置合同重点，影响代表方的修改倾向</div>
          <div class="options-3">
            <div class="option-item">
              1.
              代理费的计算和支付方式：确保按照协议规定收取代理费，保障经济利益
            </div>
            <div class="option-item">
              2.
              甲方责任的免除条款：避免因非甲方原因导致的合同无法履行而产生的责任
            </div>
          </div>
        </div>

        <div class="df" style="justify-content: flex-end; margin-top: 24px">
          <div class="btn default-btn" style="margin-right: 6px">跳过步骤</div>
          <div class="btn primary-btn" @click="next">下一步</div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  // import pdfsvg from './icon/pdf.svg'
  import voice from '@/views/index/components/AI/voice.vue'
  import dataMap from '@/views/index/components/AI/mock/dataMap.js'
  import AIChat from '@/views/index/components/AI/AIChat.vue'
  import fileUpload from '@/views/index/components/AI/fileUpload.vue'
  import contractPng from '@/assets/contract.png'
  export default {
    components: { voice, AIChat, fileUpload },
    data() {
      return {
        searchKey: '',
        source: '参考资料',
        userName: '',
        actived: '精选',
        value1: '',
        value2: '',
        open: false,
        radio: '1',
        contractPng,
        optionValue2: '2',
        contractType: '通用合同',
        contractTypes: [
          { label: '通用合同', value: '通用合同' },
          { label: '离婚协议书', value: '离婚协议书' },
          { label: '租赁合同', value: '租赁合同' },
          { label: '劳动合同', value: '劳动合同' },
          { label: '委托授权书', value: '委托授权书' },
          { label: '借款合同', value: '借款合同' },
          { label: '承揽合同', value: '承揽合同' },
          { label: '购销合同', value: '购销合同' },
        ],
        // pdfsvg,
        relativeList: [
          { title: '精选', key: '精选', actived: true },
          { title: '离婚协议书', key: '离婚协议书', actived: false },
          { title: '租赁合同', key: '租赁合同', actived: false },
          { title: '劳动合同', key: '劳动合同', actived: false },
          { title: '委托授权书', key: '委托授权书', actived: false },
          { title: '借款合同', key: '借款合同', actived: false },
          { title: '承揽合同', key: '承揽合同', actived: false },
          { title: '购销合同', key: '购销合同', actived: false },
          { title: '投资合同', key: '投资合同', actived: false },
          { title: '技术服务合同', key: '技术服务合同', actived: false },
          { title: '销售合同', key: '销售合同', actived: false },
          { title: '保密协议', key: '保密协议', actived: false },
          { title: '运输合同', key: '运输合同', actived: false },
        ],
        tempRelativeList: [],
        allList: dataMap.contractList,
        curSelected: {},
        officeViewDialog: false,
        improveOptionDialog: false,
        think: false,
        search: false,
        editor: false,
        fileObject: {},
        showChatPage: false,
      }
    },
    created() {
      const userInfo = JSON.parse(localStorage.getItem('userInfo'))
      this.userName = userInfo.realname
      this.tempRelativeList = this.relativeList.slice(0, 8)
    },
    watch: {
      open() {
        console.log('this.open', this.open)
        if (this.open) {
          this.tempRelativeList = this.relativeList
        } else {
          this.tempRelativeList = this.relativeList.slice(0, 8)
        }
      },
    },
    methods: {
      onTypeClick(item) {
        this.actived = item.key
      },
      onCardClick(item) {
        console.log('item', item)
        this.curSelected = item
        this.officeViewDialog = true
      },
      onSendMessage() {
        this.msgData = {
          msg: this.searchKey,
          think: this.think,
          fileObject: this.fileObject,
        }
        this.showChatPage = true
      },
      getMessage(rectxt) {
        this.searchKey = rectxt
      },
      improve() {
        this.officeViewDialog = false
        setTimeout(() => {
          this.improveOptionDialog = true
        }, 500)
      },
      next() {
        this.improveOptionDialog = false
        this.searchKey = '完善合同'
        this.msgData = {
          msg: this.searchKey,
          think: this.think,
          fileObject: this.fileObject,
        }
        this.showChatPage = true
      },
      close() {
        ;(this.showChatPage = false), (this.searchKey = '')
      },
    },
  }
</script>

<style scoped lang="scss">
  ::v-deep .el-dialog {
    border-radius: 12px;
  }
  ::v-deep .el-dialog__header {
    padding-bottom: 0;
  }
  ::v-deep .el-dialog__body {
    padding: 0;
  }

  .df {
    display: flex;
    align-items: center;
  }

  .btn {
    width: 120px;
    height: 36px;
    text-align: center;
    line-height: 36px;
    border-radius: 8px;
    font-size: 14px;
    font-weight: 600;
    cursor: pointer;
  }

  .default-btn {
    border: 1px solid #41b596;
    color: #20a884;
    margin-right: 12px;
  }

  .primary-btn {
    background-color: #20a884;
    color: #fff;
  }

  .improveOption {
    padding: 20px 22px;
    .option-title {
      color: #1f1f1f;
      font-family: PingFang SC;
      font-size: 18px;
      font-weight: 600;
      margin: 10px 0;
    }

    .options-1 {
      display: flex;
      align-items: center;
      .option-item {
        flex: 1;
        height: 88px;
        border: 1px solid #d3d3d3;
        border-radius: 6px;
        margin-right: 6px;
        padding: 20px;
        cursor: pointer;
      }
      .option-item-actived {
        border-color: #22ab82;
        background: rgba(32, 168, 132, 0.05);
      }
    }

    .options-2 {
      display: flex;
      align-items: center;
      background-color: #f7f7f7;
      border-radius: 8px;
      margin: 15px 0;
      padding: 0 4px;
      width: 752px;
      height: 50px;

      .option-item {
        align-items: center;
        cursor: pointer;
        display: flex;
        flex: 1;
        height: 90%;
        justify-content: center;
        min-width: 246px;
        color: #000;
        font-weight: 600;
      }

      .option-item-actived {
        background-color: #fff;
        border-radius: 8px;
      }
    }

    .options-3 {
      .option-item {
        width: 100%;
        height: 50px;
        display: flex;
        align-items: center;
        border: 1px solid #d3d3d3;
        border-radius: 6px;
        padding: 0 10px;
        margin-top: 6px;
      }
    }
  }
  .container {
    width: 810px;
    margin: 0 auto;
    padding: 30px 0 120px 0;

    .text-content {
      // margin-top: 50px;
      .menu-item {
        display: flex;
        align-items: center;
        justify-content: flex-start;
        font-weight: 700;
        .menu-icon {
          width: 24px;
          height: 24px;
          line-height: 24px;
          text-align: center;
        }
      }
      .intro {
        font-size: 24px;
        margin-top: 12px;
        margin-bottom: 24px;
        color: #000;
        text-align: left;
        font-weight: 600;
      }
    }

    .relative-list {
      // margin-top: 30px;
      display: flex;
      flex-wrap: wrap;
      flex: 1;

      .list-item {
        margin-right: 4px;
        font-size: 14px;
        border-radius: 8px;
        padding: 7px 16px;
        cursor: pointer;
        border: 1px solid rgba(0, 0, 0, 0.08);
        margin-bottom: 6px;
      }

      .list-item:hover {
        background: rgba(0, 0, 0, 0.06);
      }

      .list-item-actived:hover {
        background: #232629;
      }

      .list-item-actived {
        background: #232629;
        color: #fff;
      }

      .icon {
        font-size: 14px;
        font-weight: 700;
        margin-right: 4px;
      }
      .btn-title {
        font-size: 14px;
      }
    }

    .relative-cards {
      margin-top: 20px;
      display: grid;
      grid-template-columns: repeat(5, 1fr);
      gap: 10px;
      margin-bottom: 20px;
      /* padding-bottom: 180px; */
      .card-item {
        width: 140px;
        height: 220px;
        cursor: pointer;
        // border: 1px solid rgba(0,0,0,.08);
        // border-radius: 16px;
        // overflow: hidden;
        background: #fff;

        .card-img {
          width: 100%;
          position: relative;
          height: 186px;
          box-shadow: 4px 4px 0 #f5f7fb;
          border: 1px solid #eee;
          overflow: hidden;
          &:hover {
            transform: translateY(-6px);
            transition: all 0.1s linear;
            box-shadow: 0 6px 10px 0 rgba(42, 60, 79, 0.1);
          }
          img {
            width: 100%;
            object-position: top;
            object-fit: cover;
            box-shadow: 0px 6px 10px 0px rgba(0, 0, 0, 0.08),
              0px 0px 1px 0px rgba(0, 0, 0, 0.15);
            height: fit-content;
          }
        }
        .card-title {
          padding: 10px 2px;
          align-items: center;
          width: 100%;
          justify-content: space-between;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
          width: 140px;
          color: #000000;
        }
      }
      .card-item-actived {
        border-color: #0057ff;
      }
    }

    .search-input {
      width: 810px;
      text-align: center;
      position: fixed;
      bottom: -40px;
      padding-bottom: 20px;
      height: 200px;
      background: #fff;

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
        ::v-deep textarea {
          border: none;
          outline: none;
          padding: 14px;
          margin: 0;
          background-image: none;
          background-color: transparent;
          width: 100%;
          resize: none;
          font-size: 16px;
          height: 60px;
        }

        ::v-deep textarea:focus {
          outline: none;
        }

        ::v-deep .textarea_ai {
          display: none;
        }

        .btn-group {
          display: flex;
          justify-content: space-between;
          margin-left: 14px;
          /* margin-top: 0px; */

          .switch-group {
            display: flex;
            align-items: center;

            .deep {
              display: flex;
              align-items: center;
              margin-right: 10px;
              padding: 5px 8px;
              border: 1px solid rgba(0, 0, 0, 0.5);
              border-radius: 14px;
              cursor: pointer;
              font-size: 14px;

              .select-style {
                ::v-deep input {
                  border: none !important;
                  width: 115px;
                  height: 20px;
                  padding-left: 10px;
                }
                ::v-deep .el-input__suffix {
                  display: flex;
                  align-items: center;
                }
              }

              &:hover {
                background: #dbeafe;
                border-color: rgba(0, 122, 255, 0.15);
              }
              .btn-icon {
                width: 18px;
                height: 18px;
                font-size: 18px;
                margin-right: 0;
                margin-left: 4px;
              }
            }
          }

          .icons {
            display: flex;
            justify-content: center;
            align-items: center;
            .icon {
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
          }
        }
      }
    }

    .improvePage {
      position: absolute;
      width: 100%;
      top: 0px;
      z-index: 100;
      height: 100%;
      background: #fff;
    }
    .chatPage {
      height: 94vh;
      background: #fff;
    }
  }
</style>
