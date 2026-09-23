<template>
  <div>
    <div class="top">
      <div class="title"></div>
      <div class="tj">
        <div>
          <el-select class="select" v-model="data1">
            <el-option key="1" value="标题"></el-option>
            <el-option key="2" value="全文"></el-option>
            <el-option key="3" value="发文字号">发文字号</el-option>
          </el-select>
          <el-input class="input" />
          <el-button class="search" type="primary">开始检索</el-button>
        </div>
        <div style="margin-top: 10px">
          <el-checkbox v-model="checked">同义词</el-checkbox>
          <span class="line">|</span>
          <el-radio-group v-model="radio">
            <el-radio :label="3">精确</el-radio>
            <el-radio :label="6">模糊</el-radio>
          </el-radio-group>
        </div>
      </div>
    </div>
    <el-card>
      <div class="middle_search">
        <span
          v-for="item in list"
          :key="item.id"
          :class="avtivecolor == item.id ? 'avtive' : ''"
          @mouseover="Mouseover(item.id)"
          @mouseleave="Mouseleave()"
        >
          {{ item.name }}
        </span>
      </div>
    </el-card>
    <div class="content">
      <div class="left">
        <el-card>
          <el-collapse v-model="activeNames">
            <el-collapse-item title="相关提示" name="1">
              <div>本月新颁（463）</div>
              <div>本月生效（403）</div>
            </el-collapse-item>
          </el-collapse>
          <el-collapse v-model="activeNames1">
            <el-collapse-item title="效力位阶" name="1">
              <div>地方性法规（463）</div>
              <div>地方政府规章（403）</div>
              <div>地方规范性文件（503）</div>
              <div>地方司法文件（463）</div>
              <div>地方工作文件（403）</div>
              <div>行政许可批复（503）</div>
            </el-collapse-item>
          </el-collapse>
          <el-collapse v-model="activeNames2">
            <el-collapse-item title="专题分类" name="1">
              <div>依法治国（463）</div>
              <div>民法典（403）</div>
              <div>中央一号文件（503）</div>
              <div>网络犯罪（463）</div>
              <div>利用外资（403）</div>
              <div>扫黑除恶（503）</div>
              <div>法律（463）</div>
              <div>行政法规（403）</div>
              <div>监察法规（503）</div>
            </el-collapse-item>
          </el-collapse>
          <el-collapse v-model="activeNames3">
            <el-collapse-item title="制定机关" name="1">
              <div>北京市（463）</div>
              <div>天津市（403）</div>
              <div>河北省（503）</div>
              <div>山西省（463）</div>
              <div>内蒙古自治区（403）</div>
              <div>辽宁省（503）</div>
            </el-collapse-item>
          </el-collapse>
          <el-collapse v-model="activeNames4">
            <el-collapse-item title="时效性" name="1">
              <div>现行有效（463）</div>
              <div>失效（403）</div>
              <div>已被修改（503）</div>
              <div>尚未施行（463）</div>
              <div>部分失效（403）</div>
            </el-collapse-item>
          </el-collapse>
          <el-collapse v-model="activeNames5">
            <el-collapse-item title="法规类别" name="1">
              <div>宪法（463）</div>
              <div>国家机关（403）</div>
              <div>机关工作（503）</div>
              <div>法制工作（463）</div>
            </el-collapse-item>
          </el-collapse>
          <el-collapse v-model="activeNames6">
            <el-collapse-item title="公布年份" name="1">
              <div>2023（463）</div>
              <div>2022（403）</div>
              <div>2021（503）</div>
              <div>2020（463）</div>
            </el-collapse-item>
          </el-collapse>
        </el-card>
      </div>
      <div class="right">
        <el-card>
          <div slot="header" class="clearfix">
            <el-checkbox v-model="checked">全选</el-checkbox>
            <el-button type="text" size="medium" style="margin-left: 10px">
              批量下载
            </el-button>
            <el-dropdown style="margin-left: 10px">
              <span class="el-dropdown-link">
                <i class="el-icon-more el-icon--right"></i>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>批量收藏</el-dropdown-item>
                <el-dropdown-item>批量转发</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
            <div class="card_right">
              <div>
                共
                <span style="color: #fe9935">433551</span>
                篇
              </div>
              <el-dropdown>
                <span class="el-dropdown-link">
                  分组：效力位阶
                  <i class="el-icon-arrow-down el-icon--right"></i>
                </span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item>效力位阶</el-dropdown-item>
                  <el-dropdown-item>时效性</el-dropdown-item>
                  <el-dropdown-item>不分组</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
              <el-dropdown>
                <span class="el-dropdown-link">
                  排序：↓公布日期
                  <i class="el-icon-arrow-down el-icon--right"></i>
                </span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item>↓公布日期</el-dropdown-item>
                  <el-dropdown-item>↑公布日期</el-dropdown-item>
                  <el-dropdown-item>↑施行日期</el-dropdown-item>
                  <el-dropdown-item>↓施行日期</el-dropdown-item>
                  <el-dropdown-item>引用量</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
              <el-button>切换图表</el-button>
            </div>
          </div>
          <div v-for="(item1, index1) in list" :key="index1">
            <flfgkRightBottomList></flfgkRightBottomList>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
  import flfgkRightTopList from './components/flfgkRightTopList.vue'
  import flfgkRightBottomList from './components/flfgkRightBottomList.vue'
  export default {
    name: '',
    components: {
      flfgkRightTopList,
      flfgkRightBottomList,
    },
    data() {
      return {
        data1: undefined,
        radio: undefined,
        checked: undefined,
        avtivecolor: 0,
        activeNames: ['1'],
        activeNames1: ['1'],
        activeNames2: ['1'],
        activeNames3: ['1'],
        activeNames4: ['1'],
        activeNames5: ['1'],
        activeNames6: ['1'],
        list: [
          {
            id: 1,
            name: '中央法规',
          },
          {
            id: 2,
            name: '地方法规',
          },
          {
            id: 3,
            name: '立法资料',
          },
          {
            id: 4,
            name: '立法计划',
          },
          {
            id: 5,
            name: '中外条约',
          },
          {
            id: 6,
            name: '外国法规',
          },
          {
            id: 7,
            name: '香港法规',
          },
          {
            id: 8,
            name: '澳门法规',
          },
          {
            id: 9,
            name: '台湾地区法规',
          },
          {
            id: 10,
            name: '法律动态',
          },
          {
            id: 11,
            name: '合同范本',
          },
          {
            id: 12,
            name: '法律文书',
          },
        ],
        rightTopActiveName: 'first',
        testList: [
          {
            title: '数字平台滥用市场支配地位行为的创新损害分析',
            info: ' 《政法论丛》 / 2023年 / 第3期 / 第102页 / 李潇洋',
          },
          {
            title: '数字平台滥用市场支配地位行为的创新损害分析',
            info: ' 《政法论丛》 / 2023年 / 第3期 / 第102页 / 李潇洋',
          },
          {
            title: '数字平台滥用市场支配地位行为的创新损害分析',
            info: ' 《政法论丛》 / 2023年 / 第3期 / 第102页 / 李潇洋',
          },
          {
            title: '数字平台滥用市场支配地位行为的创新损害分析',
            info: ' 《政法论丛》 / 2023年 / 第3期 / 第102页 / 李潇洋',
          },
          {
            title: '数字平台滥用市场支配地位行为的创新损害分析',
            info: ' 《政法论丛》 / 2023年 / 第3期 / 第102页 / 李潇洋',
          },
        ],
        test1List: [],
      }
    },
    methods: {
      // 鼠标"悬停"触发此方法
      Mouseover(index) {
        this.avtivecolor = index
      },

      // 鼠标"离开"触发此方法
      Mouseleave(index) {
        this.avtivecolor = 0
      },
      handleClickRightTopTabs() {},
    },
  }
</script>
<style scoped lang="scss">
  .top {
    height: 100px;
    background-color: #fff;
    display: flex;
    align-items: center;
    /* justify-content: center; */
    .input {
      width: 400px;
      /* height: 60px; */
    }
    .select {
      width: 100px;
      /* height: 60px; */
    }
    .search {
      height: 32px;
      /* color: #c3c6c9; */
      /* background-color: #2d4d68;
      border-color: #2d4d68; */
    }
    .title {
      font-size: 30px;
      font-weight: bold;
      margin-right: 130px;
      margin-left: 100px;
      color: #000;
    }
    .tj {
      display: flex;
      flex-direction: column;
    }
    .line {
      font-size: 16px;
      margin: 0 10px;
    }
  }
  .middle_search {
    display: flex;
    justify-content: space-around;
    padding: 0 80px;
  }
  .avtive {
    cursor: pointer;
    color: #448ef7;
  }
  .content {
    display: flex;
    flex-direction: row;

    .left {
      width: 250px;
    }
    .right {
      margin-left: 20px;
      flex: 1;
    }
  }

  .card_right {
    float: right;
    width: 500px;
    display: flex;
    align-items: center;
    justify-content: space-around;
  }
</style>
