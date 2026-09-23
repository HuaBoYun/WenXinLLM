<template>
  <div class="content">
    <div class="navBox">
      <div class="title">系统清单</div>
      <el-empty v-if="navList.length == 0" description="暂无数据"></el-empty>
      <div v-else :class="navIndex == index ? 'navItem navItemSelect' : 'navItem'" v-for="(i,index) in navList" :key="index" @click="chooseNav(i.id,index)">{{ i.name }}</div>
    </div>
    <div class="listBox">
      <el-empty v-if="dataList.length == 0" description="暂无数据"></el-empty>
      <el-collapse v-else v-model="activeNames">
        <el-collapse-item v-for="(item, index) in dataList" :key="index" :name="index.toString()">
          <template slot="title">{{ item.name }}</template>
          <el-tree node-key="value" :data="item.children" :props="defaultProps" highlight-current @node-click="handleNodeClick"></el-tree>
        </el-collapse-item>
      </el-collapse>
    </div>
    <div class="right" v-if="testValue">
      <div class="rightItem">
        <div class="itemKey">审查项:</div>
        <div class="itemValue">{{ testValue.reviewItemName }}</div>
      </div>
      <div class="rightItem rightItemWhite">
        <div class="itemKey">说明:</div>
        <div class="itemValue">{{ testValue.itemDescription }}</div>
      </div>
      <div class="rightItem rightItemWhite">
        <div class="itemKey">样例:</div>
        <div class="itemValue">{{ testValue.itemSample }}</div>
      </div>
      <div class="rightItem">
        <div class="itemKey">风险等级:</div>
        <div class="itemValue">{{ testValue.riskLevel == '01' ? '高风险' : testValue.riskLevel == '02' ? '中风险' : testValue.riskLevel == '03' ? '低风险' : '' }}</div>
      </div>
      <div class="rightItem">
        <div class="itemKey">风险提示:</div>
        <div class="itemValue">{{ testValue.riskWarning }}</div>
      </div>
      <div class="rightItem">
        <div class="itemKey">相关法规:</div>
        <div class="itemValue itemValueLink">
          <div v-for="(item,index) in rightValue" :key="index">
            <div class="itemValueName" @click="openContent(index)">{{ item.name }}</div>
            <div :class="[item.isshow == '1' ? 'itemValueContents' : 'itemValueContent']">{{ item.content }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Detail from './components/htfbkDetail.vue'
import Edit from './edit.vue'
import { getReviewCheckList, getReviewItemTree, getReviewItemInfo } from '@/api/sjzt/ht/index'
export default {
  name: 'htfxqd',
  components: {
    Detail,
    Edit,
  },
  data() {
    return {
      navList: [],
      dataList: [],
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      queryForm: {
        pageNumber: 1,
        pageSize: 20,
      },
      navIndex: 0,
      activeNames: ['0', '1', '2', '3'],
      testValue:'',
      rightValue: ''
    }
  },

  created() {
    this.fetchData()
  },
  methods: {
    openContent(item) {
      let arr = this.rightValue
      arr[item].isshow = arr[item].isshow == '1' ? '2' : '1'
      this.rightValue = arr
    },
    async fetchData() {
      let res = await getReviewCheckList(this.queryForm)
      this.navList = res.data.data.records
      this.getList(this.navList[this.navIndex].id)
    },
    chooseNav(id,index) {
      this.navIndex = index
      this.getList(id)
    },
    async getList(i) {
      let res = await getReviewItemTree({reviewId: i})
      this.dataList = res.data.data
    },
    async handleNodeClick(data) {
      
      let res = await getReviewItemInfo({reviewItemId: data.id})
      this.testValue = res.data.data
      this.rightValue = JSON.parse(res.data.data.relatedRegulations).map(i => {return {...i,isshow:1}})
      
    }
  },
}
</script>
<style scoped lang="scss">
.content {
  width: 100%;
  max-height: calc(100vh - 210px);
  padding: 15px;
  display: flex;

  .navBox {
    width: 240px;
    height: 100%;
    overflow: auto;
    padding-right: 10px;

    .title {
      font-size: 14px;
      font-weight: 700;
      color: #909399;
      line-height: 24px;
      margin-bottom: 10px;
    }

    .navItem {
      width: 100%;
      height: 40px;
      margin-bottom: 5px;
      display: flex;
      align-items: center;
      padding-left: 20px;
      box-sizing: border-box;
      font-size: 15px;
      color: #2f2e3f;  
      border-radius: 4px;
      cursor: pointer;
    }

    .navItemSelect {
      background: #f3f5f8;
    }
  }

  .listBox {
    padding-left: 15px;
    flex: 6;
    border-left: 1px solid #dcdfe5;
    height: calc(100vh - 240px);
    overflow-y: scroll;
  }

  .right {
    margin-left: 10px;
    flex: 4;
    border-radius: 4px;
    background: #f3f5f8;
    overflow: auto;
    box-sizing: border-box;
    padding: 10px 15px;
    .rightItem {
      display: flex;
      margin-bottom: 20px;
      .itemKey {
        width: 80px;
        text-align: right;
        font-size: 14px;
        font-weight: 700;
        letter-spacing: 0;
        line-height: 21px;
      }
      .itemValue{
        padding-left: 6px;
        flex: 1;
        width: 100%;
        font-size: 14px;
        letter-spacing: 0;
        line-height: 21px;
        white-space: pre-wrap;
      }
      .itemValueLink {
        
        .itemValueName {
          cursor: pointer;
          color: #406be2;
        }
        .itemValueContent {
          margin: 10px 0;
          background: #fff;
          padding: 10px;
          border-radius: 5px;
          height: auto;
        }
        .itemValueContents {
          height: 0;
          overflow: hidden;
        }
      }
    }
    .rightItemWhite {
        color: #909399;
      }
  }
}

:deep(.el-tree-node__content) {
  height: 40px !important;
  font-size: 14px;
  color: #2f2e3f;
  margin-bottom: 2px
}

:deep(.el-tree-node__label) {
  overflow: hidden; 
  text-overflow: ellipsis; 
  white-space: nowrap;
}


.search {
  padding: 20px 0;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
