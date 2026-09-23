<template>
  <div class="system-log-container">
    <el-card shadow="never" class="secondCard">
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane
          :label="item.tab"
          :name="item.key"
          v-for="(item, index) in tabList"
          :key="index"
        >
          <el-row :gutter="24">
            <el-col :span="8" v-for="item2 in itemList" :key="item2.name">
              <el-card :body-style="{ padding: 0 }" class="card">
                <div class="tab-card" @click="handNext(item2)">
                  <div class="header clearfix">
                    <vab-icon class="red" :icon="item2.icon" />
                    <span style="margin-left: 25px">{{ item2.name }}</span>
                    <el-button class="right-btn" type="text">
                      <i class="el-icon-right"></i>
                    </el-button>
                  </div>
                  <div>
                    {{ item2.des }}
                  </div>
                </div>

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
                    {{ item2.hot }}
                  </div>
                  <el-button
                    v-if="!item2.isStar"
                    type="text"
                    class="button right-btn"
                    @click="item2.isStar = true"
                  >
                    <i class="el-icon-star-off"></i>
                    收藏 {{ item2.star }}
                  </el-button>
                  <el-button
                    v-else
                    type="text"
                    class="button right-btn"
                    @click="item2.isStar = false"
                  >
                    <i class="el-icon-star-on orange"></i>
                    已收藏 {{ +item2.star + 1 }}
                  </el-button>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </el-tab-pane>
      </el-tabs>
    </el-card>
    <Detail ref="detail" />
  </div>
</template>

<script>
  import Detail from './aidetail.vue'
  export default {
    components: { Detail },
    data() {
      return {
        //选中key
        activeName: 'a',
        //菜单
        tabList: [
          // { tab: '所有', key: 'a' },
          // { tab: '其他', key: 'b' },
          // { tab: '编程', key: 'c' },
          // { tab: '医疗', key: 'd' },
          // { tab: '旅游', key: 'e' },
          // { tab: '数学', key: 'f' },
          // { tab: '文学', key: 'g' },
          // { tab: '心理学', key: 'h' },
          { tab: '所有', key: 'a' },
          { tab: '法', key: 'b' },
          { tab: '人', key: 'c' },
          { tab: '财', key: 'd' },
          { tab: '物', key: 'e' },
        ],
        //全部
        allList: [
          {
            des: '可以回答所有宪法问题',
            name: '宪法',
            hot: '88',
            star: '99',
            isStar: false,
            class: 'b',
            icon: 'book-mark-fill',
          },
          {
            des: '可以回答所有民法典问题',
            name: '民法典',
            hot: '88',
            star: '99',
            isStar: false,
            class: 'b',
            icon: 'book-mark-fill',
          },
          {
            des: '可以回答所有知识产权问题',
            name: '知识产权',
            hot: '88',
            star: '99',
            isStar: false,
            class: 'b',
            icon: 'auction-line',
          },
          {
            des: '可以回答所有会计法问题',
            name: '会计法',
            hot: '88',
            star: '99',
            isStar: false,
            class: 'b',
            icon: 'book-mark-fill',
          },
          {
            des: '可以回答所有审计法问题',
            name: '审计法',
            hot: '88',
            star: '99',
            isStar: false,
            class: 'b',
            icon: 'book-mark-fill',
          },
          {
            des: '可以回答所有金融业问题',
            name: '金融业',
            hot: '88',
            star: '99',
            isStar: false,
            class: 'b',
            icon: 'bank-line',
          },
          {
            des: '可以回答所有证券期货问题',
            name: '证券期货',
            hot: '88',
            star: '99',
            isStar: false,
            class: 'b',
            icon: 'coupon-2-line',
          },
          {
            des: '可以回答所有公用问题',
            name: '公用',
            hot: '88',
            star: '99',
            isStar: false,
            class: 'b',
            icon: 'apps-line',
          },
          {
            des: '可以回答所有地产建筑问题',
            name: '地产建筑',
            hot: '88',
            star: '99',
            isStar: false,
            class: 'b',
            icon: 'home-4-line',
          },
          {
            des: '可以回答所有农牧业问题',
            name: '农牧业',
            hot: '88',
            star: '99',
            isStar: false,
            class: 'b',
            icon: 'apps-line',
          },
          {
            des: '可以回答所有交通运输问题',
            name: '交通运输',
            hot: '88',
            star: '99',
            isStar: false,
            class: 'b',
            icon: 'truck-line"',
          },
          {
            des: '可以回答所有贸易问题',
            name: '贸易',
            hot: '88',
            star: '99',
            isStar: false,
            class: 'b',
            icon: 'money-cny-circle-line',
          },
          {
            des: '可以回答所有军工问题',
            name: '军工',
            hot: '88',
            star: '99',
            isStar: false,
            class: 'b',
            icon: 'apps-line',
          },
          {
            des: '可以回答所有制造问题',
            name: '制造',
            hot: '88',
            star: '99',
            isStar: false,
            class: 'b',
            icon: 'apps-line',
          },
          {
            des: '可以回答所有能源问题',
            name: '能源',
            hot: '88',
            star: '99',
            isStar: false,
            class: 'b',
            icon: 'apps-line',
          },
        ],
        //选中
        itemList: [],
      }
    },
    created() {
      //默认全部
      this.itemList = [...this.allList]
    },
    methods: {
      // 切换tab
      handleClick() {
        if (this.activeName === 'a') {
          //全部
          this.itemList = [...this.allList]
        } else {
          //分类
          const arr = this.allList.filter((ele) => {
            return ele.class === this.activeName
          })
          this.itemList = arr
        }
      },
      //跳转到对话
      handNext(item) {
        // this.$router.push({ name: 'AI详情', query: { ...item } })
        this.$refs['detail'].show({ ...item })
      },
    },
  }
</script>
<style scoped lang="scss">
  .system-log-container {
    background: #f6f8f9 !important;
    padding: 0 !important;
  }

  .secondCard {
    margin-top: -5px !important;
    padding-top: 10px;
  }
  .pagination {
    margin-bottom: 20px !important;
  }

  .card {
    border-radius: 10px;
  }

  .tab-card {
    padding: 20px 20px 50px;
    background-color: #fff;
    cursor: pointer;

    &:hover {
      background-color: #eee;
      .right-btn {
        color: #333 !important;
      }
    }
    .header {
      padding: 20px 0;
      font-size: 16px;
      font-weight: 600;
      margin-bottom: 20px;
      .right-btn {
        font-size: 18px;
        font-weight: 600;
        color: #aaa;
      }
    }
  }

  .bottom {
    padding: 20px;
    background-color: rgba(243, 244, 246, 0.8);
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

  .green {
    font-size: 32px;
    color: #41b584;
  }
  .red {
    font-size: 32px;
    color: #e50113;
  }
</style>
