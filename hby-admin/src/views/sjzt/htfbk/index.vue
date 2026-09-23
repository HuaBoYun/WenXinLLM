<template>
  <div>
    <div class="search">
      <el-form
        ref="form"
        :inline="true"
        label-width="0"
        :model="queryForm"
        @submit.native.prevent
      >
        <el-form-item>
          <el-input
            v-model="queryForm.flowName"
            clearable
            placeholder="国家合同示范文本库"
          />
        </el-form-item>
        <el-form-item>
          <el-button
            icon="el-icon-search"
            native-type="submit"
            type="primary"
            @click="queryData"
          >
            查询
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="guess-list-box">
      <div style="display: flex; justify-content: space-between">
        <div class="guess-all">
          <span class="search-size">
            “国家合同示范文本库”共找到
            <span class="search-size-num">10000+个</span>
            结果
          </span>
        </div>
        <el-button
          type="success"
          size="mini"
          style="margin-right: 20px; margin-bottom: 10px"
          @click="open"
        >
          新建
        </el-button>
      </div>
    </div>
    <div style="display: flex">
      <div class="tabs-list">
        <span
          :class="['tabs-list-item', currentTab == 1 ? 'tab-active' : '']"
          @click="handleTab(1)"
        >
          综合排序
        </span>
        <el-divider direction="vertical"></el-divider>
        <span
          :class="['tabs-list-item', currentTab == 2 ? 'tab-active' : '']"
          @click="handleTab(2)"
        >
          热门下载
        </span>
        <el-divider direction="vertical"></el-divider>
        <span
          :class="['tabs-list-item', currentTab == 3 ? 'tab-active' : '']"
          @click="handleTab(3)"
        >
          最新上传
        </span>
      </div>
      <el-select
        v-model="fileTypeValue"
        @change="handleTypeChange"
        placeholder="全部格式"
        class="select-right"
      >
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        ></el-option>
      </el-select>
    </div>

    <div class="content">
      <div class="contentList">
        <div
          class="contentList-item"
          v-for="(item, index) in list"
          :key="index"
          @click="handleDownFile(item)"
        >
          <div class="file-show-img">
            <img :src="item.fgimage" class="img-info" />

            <div class="contentList-item-mask">
              <div class="file-operate">
                <el-button
                  type="danger"
                  class="downBtn"
                  icon="el-icon-download"
                  round
                >
                  立即下载
                </el-button>
              </div>
            </div>
          </div>
          <div class="file-title">{{ item.wknane }}</div>
        </div>
      </div>
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-size="100"
        layout="total, prev, pager, next, jumper"
        :total="400"
      ></el-pagination>
    </div>
    <Detail ref="htfbkDetail" />
    <Edit ref="edit" @fetchData="fetchData" />
  </div>
</template>

<script>
  import Detail from './components/htfbkDetail.vue'
  import Edit from './edit.vue'
  import { htfbkList, deleteWKList, previewWK } from '@/api/setting/auth'
  export default {
    name: 'htfbk',
    components: {
      Detail,
      Edit,
    },
    data() {
      return {
        queryForm: {
          flowName: '国家合同示范文本库',
          sort: 'all',
          type: 'all',
        },
        currentTab: 1,
        fileTypeValue: 'all',
        options: [
          { value: 'all', label: '全部格式' },
          { value: 'word', label: 'Word' },
          { value: 'excel', label: 'Excel' },
          { value: 'ppt', label: 'PPT' },
        ],
        currentPage: 1,
        list: [
          {
            url: '//pic.iask.com.cn/1i2UsmC5NCf.png',
            name: ' 利工程施工监理合同 (国家合同示范文本)',
          },
          {
            url: '//pic.iask.com.cn/1caSdzLZU7zw.png',
            name: '解读建设部, 国家工商总局示范',
          },

          {
            url: '//pic.iask.com.cn/1caIuDOIh11a.png',
            name: '国家工商管理局测绘合同（示范文本）',
          },

          {
            url: '//pic.iask.com.cn/1cdILtgEhLXY.png',
            name: '国家劳务合同',
          },

          {
            url: '//pic.iask.com.cn/24XqfRtmHBZ.png',
            name: '监理合同示范文本',
          },

          {
            url: '//pic.iask.com.cn/1cbAKkj7zcEc.png',
            name: '招标合同示范文本',
          },

          {
            url: '//pic.iask.com.cn/1cGiYpnDRxt.png',
            name: '保管合同示范文本',
          },

          {
            url: '//pic.iask.com.cn/XTDm99dQfE8.png',
            name: '施工合同示范文本',
          },

          {
            url: '//pic.iask.com.cn/9EbGts3JIG14.png',
            name: '装修合同示范文本',
          },

          {
            url: '//pic.iask.com.cn/1AniBljTZvx.png',
            name: '居间合同示范文本',
          },

          {
            url: '//pic.iask.com.cn/1c9vqQXJO4qa.png',
            name: '居间合同示范文本',
          },

          {
            url: '//pic.iask.com.cn/9EcOxJGekQPo.png',
            name: '居间合同示范文本',
          },

          {
            url: '//pic.iask.com.cn/9Fg9EOHMgdbe.png',
            name: '个人借款合同示范',
          },

          {
            url: '//pic.iask.com.cn/bXhYDYNoRUc8.png',
            name: '保管合同示范本',
          },

          {
            url: '//pic.iask.com.cn/7pDRTke2hbq.png',
            name: '保管合同示范文本',
          },

          {
            url: '//pic.iask.com.cn/alRPgXrEUn.png',
            name: '担保合同示范文本',
          },

          {
            url: '//pic.iask.com.cn/9F045aw12DDc.png',
            name: '装修合同示范文本',
          },

          {
            url: '//pic.iask.com.cn/bY5UGkKn3TtE.png',
            name: '租赁合同示范文本',
          },

          {
            url: '//pic.iask.com.cn/1klcXQzAZwX.png',
            name: '购房合同示范文本',
          },

          {
            url: '//pic.iask.com.cn/9F3WiK1Zr47k.png',
            name: '20xx示范合同',
          },

          {
            url: '//pic.iask.com.cn/9EtJP9XE52oo.png',
            name: '技术合同示范文本',
          },

          {
            url: '//pic.iask.com.cn/1dSUKfIRErd.png',
            name: '施工合同示范文本',
          },

          {
            url: '//pic.iask.com.cn/bXMzjFGd4hHM.png',
            name: '监理合同示范文本',
          },

          {
            url: '//pic.iask.com.cn/1c93tZSoxDKY.png',
            name: '国家采购招标合同',
          },

          {
            url: '//pic.iask.com.cn/7uo7GiRXSL.png',
            name: '保管合同示范文本',
          },

          {
            url: '//pic.iask.com.cn/dhDpbD9uYn.png',
            name: '房屋租赁示范合同',
          },

          {
            url: '//pic.iask.com.cn/9Ett6yeERYWK.png',
            name: '购房合同示范文本',
          },

          {
            url: '//pic.iask.com.cn/1w183PCAW3J.png',
            name: '仓储合同示范文本',
          },

          {
            url: '//pic.iask.com.cn/bXgdbwCh8tds.png',
            name: '赠与合同示范文本',
          },

          {
            url: '//pic.iask.com.cn/9FaAekVo28uc.png',
            name: '技术合同示范文本',
          },

          {
            url: '//pic.iask.com.cn/bXrSjLDQwGfK.png',
            name: '施工合同示范文本',
          },

          {
            url: '//pic.iask.com.cn/9EynRgKpd3V4.png',
            name: '国家房屋租赁合同',
          },

          {
            url: '//pic.iask.com.cn/bX6rwehjcdH0.png',
            name: '居间合同示范文本',
          },

          {
            url: '//pic.iask.com.cn/bXkqw6t4BwBw.png',
            name: '国家助学贷款合同',
          },

          {
            url: '//pic.iask.com.cn/1c7S7ay137IM.png',
            name: '个人借款合同示范',
          },

          {
            url: '//pic.iask.com.cn/9F4otkYrryQI.png',
            name: '施工合同示范文本',
          },

          {
            url: '//pic.iask.com.cn/1cc2l7tWgWGy.png',
            name: '销售合同示范文本',
          },

          {
            url: '//pic.iask.com.cn/1cdouRmWELmk.png',
            name: '国家房屋租赁合同',
          },

          {
            url: '//pic.iask.com.cn/9EGQmrnJFKZu.png',
            name: '国家房屋租赁合同',
          },

          {
            url: '//pic.iask.com.cn/9ETG8QBuzl5W.png',
            name: '国家助学借款合同',
          },
        ],
      }
    },

    created() {
      this.fetchData()
    },
    methods: {
      queryData() {
        this.fetchData()
      },
      async fetchData() {
        // const arr = [...this.list]
        // let length = this.list.length,
        //   randomIndex,
        //   temp
        // while (length) {
        //   randomIndex = Math.floor(Math.random() * length--)
        //   temp = arr[randomIndex]
        //   arr[randomIndex] = arr[length]
        //   arr[length] = temp
        // }
        // this.list = arr
        const arr = await htfbkList()
        this.list = arr.data.pageInfo.tlist
      },
      handleTab(type) {
        this.currentTab = type
        this.queryForm.sort = type
        this.fetchData()
      },
      handleTypeChange(type) {
        this.fileTypeValue = type
        this.queryForm.type = type
        this.fetchData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      handleDownFile(row) {
        this.$refs['htfbkDetail'].showEdit(row)
      },
      open() {
        this.$refs.edit.showEdit()
      },
    },
  }
</script>

<style scoped lang="scss">
  .search {
    padding: 20px 0;
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .guess-list-box {
    padding-left: 20px;
    .gueess-all {
      padding-top: 18px;
      padding-bottom: 2px;
      font-size: 14px;
      border-bottom: 0 solid #ececec;
    }

    .search-size {
      display: inline-block;
      font-size: 14px;
      color: #333;
      margin-right: 30px;
      margin-bottom: 14px;
    }

    .search-size-num {
      font-weight: 700;
      color: #ce4042;
    }
  }

  .tabs-list {
    padding: 10px 20px;
    flex: 1;
    .tabs-list-item {
      padding: 10px;
      cursor: pointer;
      font-size: 14px;
      color: #666;
    }
    .tab-active {
      font-weight: 700;
      color: #333;
    }
  }
  .select-right {
    margin-right: 20px;
    width: 150px;
  }
  .contentList {
    padding: 20px 80px;
    display: grid;
    justify-content: space-between;
    grid-template-columns: repeat(auto-fill, 278px);
    grid-gap: 10px;

    &-item {
      // display: inline-block;
      width: 278px;
      -webkit-box-shadow: 0 3px 15px 0 rgba(0, 0, 0, 0.15);
      box-shadow: 0 3px 15px 0 rgba(0, 0, 0, 0.15);
      margin-bottom: 24px;
      border-radius: 2px;
      -webkit-box-sizing: border-box;
      box-sizing: border-box;
      color: #333;
      text-decoration: none;
      line-height: 0;
      overflow: hidden;
      cursor: pointer;
      background: #fff;
      &:hover {
        .contentList-item-mask {
          bottom: 0px;
          height: 80px;
        }
      }

      .file-show-img {
        position: relative;
        overflow: hidden;
        height: 380px;
        .img-info {
          width: calc(100% - 2px);
          height: calc(100% - 2px);
          border: 1px solid #fff;
        }
      }

      .contentList-item-mask {
        position: absolute;
        bottom: -80px;
        left: 0;
        display: -webkit-box;
        display: -ms-flexbox;
        display: flex;
        -webkit-box-align: center;
        -ms-flex-align: center;
        align-items: center;
        width: 100%;
        height: 0px;
        -webkit-transition: height 0.5s;
        transition: height 0.5s;
        margin: 1px 1px 0;
        background: -webkit-gradient(
          linear,
          left top,
          left bottom,
          from(hsla(0, 0%, 100%, 0)),
          color-stop(30%, #fff)
        );
        background: linear-gradient(180deg, hsla(0, 0%, 100%, 0), #fff 30%);
        z-index: 2;
        .file-operate {
          width: 100%;
          text-align: center;
          .downBtn {
            margin-top: 20px;
            height: 40px;
            width: 200px;
          }
        }
      }

      .file-title {
        height: 42px;
        line-height: 42px;
        font-size: 14px;
        padding: 0 11px;
        color: #333;
        text-decoration: none;
        border-radius: 4px;
        overflow: hidden;
        cursor: pointer;
        white-space: nowrap;
        text-overflow: ellipsis;
        -webkit-box-sizing: border-box;
        box-sizing: border-box;
      }
    }
  }
</style>
