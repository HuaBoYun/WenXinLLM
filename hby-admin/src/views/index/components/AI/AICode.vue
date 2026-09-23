<template>
  <div class="container">
    <div class="search-input">
      <div class="text-content">
        <div class="page-title">
          <div class="menu-item">
            <i :class="`menu-icon el-icon-cpu`"></i>
            <div class="menu-title">Ai 编程</div>
          </div>
        </div>
        <div class="intro">秒懂代码，智能编码，项目精解</div>
      </div>
      <div class="input-content">
        <el-input
          class="input-style"
          v-model="searchKey"
          clearable
          type="textarea"
          :rows="1"
          autosize
          placeholder="粘贴代码或描述你的问题"
          @keydown.enter.native="onSendMessage"
        />
        <div class="btn-group">
          <div class="select-style">
            <div class="btn-item">
              <div class="btn-icon"><i class="el-icon-bangzhu"></i></div>
              <div class="btn-title">代码文件</div>
            </div>
            <div class="btn-item">
              <div class="btn-icon"><i class="el-icon-edit-outline"></i></div>
              <div class="btn-title">GitHub 仓库</div>
            </div>
          </div>

          <div class="icons">
            <voice class="icon" @getMessage="getMessage" />
            <i class="icon el-icon-top send" @click="onSendMessage"></i>
          </div>
        </div>
      </div>
    </div>

    <div class="relative-list">
      <div class="list-item" v-for="item in relativeList" :key="item.title">
        <el-button round class="icon-btn" @click="$emit('chatNow', { msg: item.title })">
          <!-- <i
            :class="`icon el-icon-${item.icon}`"
            :style="{ color: item.color }"
          ></i> -->
          <img class="icon" :src="item.icon" alt="">
          <span class="btn-title">{{ item.title }}</span>
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
  import githubIcon from './icon/github.svg'
  import code1Icon from './icon/code1.svg'
  import code2Icon from './icon/code2.svg'
  import voice from './voice.vue'
  export default {
    components: { voice },
    data() {
      return {
        searchKey: '',
        source: '全网搜索',
        userName: '',
        relativeList: [
          {
            title: 'coloruicss: 专注视觉的小程序组件库',
            icon: githubIcon,
            color: '#2fbc52',
          },
          {
            title: 'algorithms: 基于JS的算法与数据结构',
            icon: githubIcon,
            color: '#ff9500',
          },
          {
            title: 'C++的动态内存分配原理',
            icon: code1Icon,
            color: '#0057ff',
          },
          {
            title: '用 SQL 创建名为 students 的表，包含 id、name 和 age 字段',
            icon: code2Icon,
            color: '#ff3b30',
          },
          {
            title: '使用 CSS 实现一个元素的阴影效果',
            icon: code2Icon,
            color: '#0057ff',
          },
          {
            title: '使用 CSS 实现一个元素的渐变背景',
            icon: code2Icon,
            color: '#ff3b30',
          },
        ],
      }
    },
    created() {
      const userInfo = JSON.parse(localStorage.getItem('userInfo'))
      this.userName = userInfo.realname
    },
    methods: {
      onSendMessage() {
        this.$emit('chatNow', { msg: this.searchKey })
      },
      getMessage(rectxt) {
        this.searchKey = rectxt
      },
    }
  }
</script>

<style scoped>
  .container {
    width: 810px;
  }

  .icon-btn >>> span {
    display: flex;
    align-items: center;
  }
  .search-input {
    text-align: center;
    padding-top: 120px;
  }

  .text-content {
    margin-top: 50px;
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
    font-size: 24px;
    margin-top: 12px;
    margin-bottom: 24px;
    color: #000;
    text-align: center;
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
    /* width: 100px; */
    display: flex;
  }

  .select-style >>> input {
    border-radius: 10px;
  }

  .btn-group {
    width: 100%;
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

  .relative-list {
    margin-top: 30px;
    padding: 0 50px;
    gap: 7px;
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
  }

  .relative-list .list-item {
    /* margin-right: 10px; */
    font-size: 18px;
  }

  .relative-list .icon {
    font-size: 14px;
    font-weight: 700;
    margin-right: 4px;
  }
  .relative-list .btn-title {
    font-size: 14px;
  }

  .btn-item {
    margin-right: 10px;
    font-size: 14px;
    border-radius: 8px;
    padding: 10px 12px;
    cursor: pointer;
    border: 1px solid rgba(0,0,0,.08);
    display: flex;
    align-items: center;
  }

  .btn-item .btn-icon {
    font-size: 14px;
    font-weight: 700;
  }

  .btn-item:hover {
    background: rgba(0,0,0,.06);
  }

  .btn-item .btn-title {
    margin: 0 4px;
  }
</style>
