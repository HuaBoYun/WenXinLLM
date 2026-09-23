<template>
  <div class="container">
    <div class="tabBackColor">
      <!-- <i class="el-icon-s-custom"></i> -->
      <img src="@/assets/images/concat.png" />
      <div class="boxRight" @click="reset">
        <div>全部</div>
        <div>{{ this.total }}</div>
      </div>
    </div>
    <!-- <i
      class="el-icon-arrow-left"
      @click="move('left')"
      @mousedown="moveDown('left')"
      @mouseup="moveUp"
    ></i> -->
    <i class="el-icon-arrow-left" @click="move('left')"></i>

    <div class="content" ref="content">
      <div
        class="box"
        v-for="(item, index) in tabList"
        :key="index"
        ref="box"
        :class="{ active: isActive === index }"
      >
        <div class="boxContent" @click="selectOrog(item, index)">
          <!-- <i class="el-icon-s-custom"></i> -->
          <img src="@/assets/images/team.png" alt="" />
          <div class="boxRight">
            <div>{{ item.teamname }}</div>
            <div>{{ item.companyCount }}</div>
          </div>
        </div>
        <!-- <div class="line"></div> -->
      </div>
    </div>
    <!-- <i
      class="el-icon-arrow-right"
      @click="move('right')"
      @mousedown="moveDown('right')"
      @mouseup="moveUp"
    ></i> -->
    <i class="el-icon-arrow-right" @click="move('right')"></i>
    <div class="tabAdd" @click="addTab">
      <!-- <i class="el-icon-circle-plus-outline"></i> -->
      <img src="@/assets/images/add.png" alt="" />
      <div>新增分组</div>
    </div>
  </div>
</template>

<script>
  import { getTeamList } from '@/api/contract/opposite'
  export default {
    name: 'SupplierTabs',
    props: ['saveTeamId'],
    data() {
      return {
        tabList: [],
        dialogVisible: false,
        value: '',
        step: 49,
        scrollLeft: 0,
        len: 0,
        isActive: undefined,
        total: 0,
      }
    },
    created() {
      getTeamList().then((res) => {
        this.tabList = res.data.teams
        this.total = res.data.count
      })
    },

    methods: {
      reset() {
        this.$emit('rest')
      },
      reloadTabData() {
        getTeamList().then((res) => {
          this.tabList = res.data.teams
          this.total = res.data.count
        })
      },
      move(e) {
        let content = this.$refs.content
        if (content.scrollWidth > content.clientWidth) {
          if (e == 'left' && this.scrollLeft > 0) {
            // this.step++
            this.scrollLeft -= this.step
            content.scrollTo(this.scrollLeft, 0)
          } else if (
            e == 'right' &&
            this.scrollLeft + content.clientWidth < content.scrollWidth
          ) {
            console.log('right')
            // this.step++
            this.scrollLeft += this.step
            content.scrollTo(this.scrollLeft, 0)
          }
        }
      },
      //上移
      moveUp() {
        clearInterval(this.timer)
      },
      //下移
      moveDown(e) {
        const content = this.$refs.content

        if (content.scrollWidth > content.clientWidth) {
          this.timer = setInterval(() => {
            if (e == 'left' && this.scrollLeft > 0) {
              this.scrollLeft -= this.step
            } else if (
              e == 'right' &&
              this.scrollLeft + content.clientWidth < content.scrollWidth
            ) {
              this.scrollLeft += this.step
            }
            content.scrollTo(this.scrollLeft, 0)
          }, 100)
        }
      },
      //回调
      addTab() {
        this.$emit('openModal')
      },
      close() {
        this.dialogVisible = false
      },
      //回调
      selectOrog(info, index) {
        this.isActive = index
        this.$emit('select', info)
        // this.saveTeamId(info)
      },
    },
  }
</script>

<style lang="scss" scoped>
  .container {
    width: 100%;
    display: flex;
    align-items: center;
    // justify-content: space-between;
    height: 70px;
    margin-bottom: 20px;
    .el-icon-s-custom {
      font-size: 36px;
      color: #448ef7;
    }
    .tabBackColor {
      display: flex;
      align-items: center;
      justify-content: center;
      background-color: #f0f2f5;
      height: 100%;
      min-width: 180px;
      .boxRight {
        cursor: pointer;
        margin-left: 10px;
      }
    }
    .tabAdd {
      height: 100%;
      min-width: 80px;
      background-color: #448ef7;
      color: #fff;
      text-align: center;
      box-shadow: 0px 0px 10px 5px rgba(0, 0, 0, 0.3);
      padding-top: 5px;
      .el-icon-circle-plus-outline {
        font-size: 36px;
        color: #fff;
      }
    }
    .content::-webkit-scrollbar {
      display: none; /* chrome 隐藏滚动条 */
    }
    .content {
      width: 100%;
      height: 100%;
      display: flex;
      align-items: center;
      // overflow: hidden;
      overflow-x: auto;
      scrollbar-width: none;
      .active {
        color: red;
      }
      .box {
        display: flex;
        align-items: center;
        height: 100%;
        margin-right: 20px;
        /* color: red; */
        :hover {
          cursor: pointer; /*鼠标变小手*/
          /* background-color: red; */
        }
        .boxContent {
          left: 0;
          display: flex;
          align-items: center;
          justify-content: center;
          width: 180px;
          height: 100%;
          background-color: #f0f2f5;

          .boxRight {
            margin-left: 10px;
          }
        }
        .line {
          height: 100%;
          width: 25px;
          background: #fafafa;
          :hover {
            cursor: default;
          }
          // z-index: 100;
        }
      }
    }
    .el-icon-arrow-left,
    .el-icon-arrow-right {
      height: 100%;
      width: 70px;

      line-height: 70px;
      text-align: center;
      font-size: 36px;
      font-weight: bold;
      color: #448ef7;
    }
    img {
      width: 40px;
      height: 40px;
    }
  }
</style>
