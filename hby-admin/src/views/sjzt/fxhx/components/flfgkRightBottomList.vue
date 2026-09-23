<template>
  <div>
    <div class="group-title" v-if="showMore" @click="handleShowMore">
      <h4 class="group-title-content">地方性法规（3189）</h4>
      <span class="group-title-more">更多</span>
    </div>
    <el-checkbox-group v-model="checkList">
      <div v-for="(item, index) in List" :key="index">
        <div class="group-item">
          <div class="group-top">
            <el-checkbox v-model="item.checked"></el-checkbox>
            <label name="sortNum">{{ index + 1 }}.</label>
            <span class="group-item-title">
              {{ item.title }}
            </span>
          </div>
          <div class="group-info">
            <span>现行有效</span>
            <span class="line"></span>
            <span>国务院规范性文件</span>
            <span class="line"></span>
            <span>国务院办公厅</span>
            <span class="line"></span>
            <span class="text">国办函〔2023〕51号</span>
            <span class="line"></span>
            <span class="text">2023.07.05公布</span>
            <span class="line"></span>
            <span class="text">2023.07.05施行</span>
          </div>
        </div>
        <div class="group-fn">
          <div>
            <el-button type="text" size="medium">概览</el-button>
            <el-button type="text" size="medium">发现</el-button>
          </div>
          <div class="group-fn-right">
            <div>
              第
              <el-input v-model="input4" style="width: 50px"></el-input>
              /3条
              <el-button size="mini" type="primary" plain>确定</el-button>
            </div>
            <el-input
              placeholder="文内检索"
              v-model="input4"
              style="width: 100px; margin-left: 20px"
            >
              <i slot="prefix" class="el-input__icon el-icon-search"></i>
            </el-input>
          </div>
        </div>
        <div class="group-down">
          <el-dropdown>
            <span class="el-dropdown-link">
              <i class="el-icon-download"></i>
              下载
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>Word</el-dropdown-item>
              <el-dropdown-item>PDF</el-dropdown-item>
              <el-dropdown-item>TXT</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>

          <el-button
            type="text"
            size="medium"
            style="margin-left: 10px; color: #606266"
          >
            <i class="el-icon-star-off"></i>
            收藏
          </el-button>
        </div>

        <div class="divider"></div>
      </div>
      <div class="loading-more" v-if="showMore" @click="handleShowMore">
        更多
      </div>
    </el-checkbox-group>
    <el-pagination
      v-if="!showMore"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage4"
      :page-size="100"
      layout="total, prev, pager, next, jumper"
      :total="400"
    ></el-pagination>
  </div>
</template>

<script>
  export default {
    name: 'flfgkRightTopList',
    props: ['List1'],
    data() {
      return {
        checkList: [],
        List: [
          {
            title: '中华人民共和国对外关系法',
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
        showMore: true,
      }
    },
    methods: {
      handleShowMore() {
        this.showMore = false
      },
    },
  }
</script>
<style scoped lang="scss">
  .group-title {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    padding: 5px 10px;
    margin-bottom: 10px;
    background: #f5f5f5;
    &-content {
      margin: 0;
      padding: 0;
      font-size: 16px;
      font-weight: 800;
      color: #218fc4;
      cursor: pointer;
    }
    &-more {
      font-size: 12px;
      cursor: pointer;
    }
  }
  .group-item {
    padding: 14px 0;
  }
  .group-top {
    padding-bottom: 10px;
    line-height: 36px;
    font-size: 16px;
  }
  .group-item-title {
    color: #333;
  }
  .group-info {
    font-size: 14px;
    color: #999;
    position: relative;
    max-height: 60px;
    overflow: hidden;
    .line {
      margin: 0 10px;
      height: 12px;
      width: 1px;
      font-size: 0;
      background: #ebeef2;
      display: inline-block;
    }
  }
  .group-fn {
    display: flex;
    align-items: center;
    justify-content: space-between;
    &-right {
      display: flex;
      font-size: 14px;
    }
  }
  .group-down {
    display: flex;
    justify-content: flex-end;
    align-items: center;
  }
  .divider {
    display: block;
    height: 1px;
    width: 100%;
    border: 1px solid rgba(0, 0, 0, 0.05);
  }
  .loading-more {
    padding: 20px 0 10px;
    width: 100%;
    text-align: center;
    color: #999;
    cursor: pointer;
  }
</style>
